from flask import jsonify, make_response, request
from db.db import db
import math
import datetime


def get_reward_data(paramters):
    a = 30
    b = 5
    c = 4
    dist = int(paramters['dist'])
    n_drowsy = int(paramters['n_drowsy'])
    if (n_drowsy == 1 or n_drowsy == 2):
        n_drowsy = 3
    elif (n_drowsy >= 17):
        n_drowsy = 17
    elif (n_drowsy != 0):
        n_drowsy += 3
    n_drinkdrive = int(paramters['n_drinkdrive'])
    if n_drinkdrive == 1 or n_drinkdrive == 2:
        n_drinkdrive = 4
    elif n_drinkdrive >= 9:
        n_drinkdrive = 13
    elif n_drinkdrive != 0:
        n_drinkdrive += 5
    n_reckless = int(paramters['n_reckless'])
    if (n_reckless == 1 or n_reckless == 2):
        n_reckless = 5
    elif (n_reckless >= 9):
        n_reckless = 8
    elif (n_reckless != 0):
        n_reckless += 7
    n_seatbelt = int(paramters['n_seatbelt'])
    if (n_seatbelt == 1 or n_seatbelt == 2):
        n_seatbelt = 5
    elif (n_seatbelt >= 9):
        n_seatbelt = 8
    elif (n_seatbelt != 0):
        n_seatbelt += 7
    t_overspeeding = int(paramters['t_overspeeding'])
    if (t_overspeeding >= 50):
        t_overspeeding = 45

    ideal_factor = a/(1+math.exp(c-(dist/b)))
    depreciation_factor_drowsy = 50*(pow(n_drowsy/20, n_drowsy+1))
    depreciation_factor_drinkdrive = 34*(pow(n_drinkdrive/13, n_drinkdrive+1))
    depreciation_factor_reckless = 9*(pow(n_reckless/15, n_reckless+1))
    depreciation_factor_seatbelt = 10*(pow(n_seatbelt/15, n_seatbelt+1))
    depreciation_factor_overspeeding = 0.8*(pow(1 - (t_overspeeding/65), -2))

    tot_reward = ideal_factor - depreciation_factor_drowsy - depreciation_factor_drinkdrive - \
        depreciation_factor_reckless - depreciation_factor_seatbelt - \
        depreciation_factor_overspeeding
    reason = []
    if (depreciation_factor_drowsy != 0):
        reason.append('drowsy')
    if (depreciation_factor_drinkdrive != 0):
        reason.append('drinkdrive')
    if (depreciation_factor_reckless != 0):
        reason.append('reckless')
    if (depreciation_factor_seatbelt != 0):
        reason.append('seatbelt')
    if (depreciation_factor_overspeeding != 0):
        reason.append('overspeeding')
    reason = ', '.join(reason)
    if (reason == ''):
        reason = 'No reason'

    return {
        'points_given': ideal_factor,
        'points_deducted': tot_reward - ideal_factor,
        'reason': reason,
        'timestamp': datetime.datetime.now(),
        'overall_points_of_session': tot_reward,
    }


reward_ref = db.collection('rewards')
device_ref = db.collection('devices')


def get_all_rewards(device_id):
    rewards = reward_ref.where('device_id', '==', device_id).get()
    rewards = [reward.to_dict() for reward in rewards]
    return make_response(jsonify(rewards), 200)


def add_reward(device_id):
    data = request.get_json()
    if (device_id == None or device_id == ''):
        return make_response(jsonify({'error': 'Device ID is required'}), 400)

    parameters = {
        'dist': data['dist'],
        'n_drowsy': data['n_drowsy'],
        'n_drinkdrive': data['n_drinkdrive'],
        'n_reckless': data['n_reckless'],
        'n_seatbelt': data['n_seatbelt'],
        't_overspeeding': data['t_overspeeding'],
    }

    #  data will contain parameters of reward formula

    reward_data = get_reward_data(parameters)

    # get and update reward in device collection
    try:
        device = device_ref.where('unique_id', '==', device_id).get()
        doc_id = device[0].id
        device = device[0].to_dict()
        net_reward = device['reward'] + \
            reward_data['overall_points_of_session']
        device['reward'] = net_reward
        device_ref.document(doc_id).update(device)
        reward_data['device_id'] = device['id']
        reward_ref.add(reward_data)
        return make_response(jsonify({'message': 'Reward updated', 'doc': data}), 200)

    except Exception as e:
        return make_response(jsonify({'error': str(e)}), 400)
