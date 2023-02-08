from flask import request, make_response, jsonify
from db.db import db
import uuid

device_ref = db.collection('devices')
reward_ref = db.collection('rewards')


def get_all_devices(current_user):
    if (current_user[0].to_dict()['role'] != 'admin'):
        user_id = current_user[0].id
        devices = device_ref.where('user_id', '==', user_id).get()
        devices = [device.to_dict() for device in devices]
        return make_response(jsonify(devices), 200)
    else:
        devices = device_ref.get()
        devices = [device.to_dict() for device in devices]
        return make_response(jsonify(devices), 200)


# Only for admin
def create_device(current_user):
    if (current_user[0].to_dict()['role'] != 'admin'):
        return make_response(jsonify({'error': 'Unauthorized'}), 401)

    data = request.get_json()
    print(data)
    if (data['rmn'] == None or data['rmn'] == ''):
        return make_response(jsonify({'error': 'RMN is required'}), 400)
        # fomart data: rmn = 84xxxxxxxxx

    data['user_id'] = None
    data['id'] = str(uuid.uuid4())
    data['reward'] = 0

    try:
        device_ref.add(data)
        return make_response(jsonify({'message': 'Device created', 'doc': data}), 200)
    except Exception as e:
        return make_response(jsonify({'error': str(e)}), 400)


def delete_device(current_user, device_id):

    if (current_user[0].to_dict()['role'] != 'admin'):
        return make_response(jsonify({'error': 'Unauthorized'}), 401)

    device = device_ref.where(
        'id', '==', device_id).get()

    doc_id = device[0].id
    if len(device) == 0:
        return make_response(jsonify({'error': 'Device not found'}), 400)

    rewards = reward_ref.where('device_id', '==', device_id).get()
    for reward in rewards:
        reward_ref.document(reward.id).delete()

    device_ref.document(doc_id).delete()

    return make_response(jsonify({'message': 'Device deleted'}), 200)


# Only for user
def add_device(current_user, device_id):
    print(device_id)
    user_id = current_user[0].id
    device = device_ref.where(
        'unique_id', '==', device_id).get()
    if len(device) == 0:
        return make_response(jsonify({'error': 'Device not found'}), 400)
    device_ref.document(device[0].id).update({'user_id': user_id})
    return make_response(jsonify({'message': 'Device added'}), 200)


def get_device(current_user, device_id):
    user_id = current_user[0].id
    device = device_ref.where('user_id', '==', user_id).where(
        'id', '==', device_id).get()
    if len(device) == 0:
        return make_response(jsonify({'error': 'Device not found'}), 400)
    return make_response(device[0].to_dict(), 200)


def remove_device(current_user, device_id):
    print(device_id)
    user_id = current_user[0].id
    device = device_ref.where('user_id', '==', user_id).where(
        'id', '==', device_id).get()
    if len(device) == 0:
        return make_response(jsonify({'error': 'Device not found'}), 400)
    device_ref.document(device[0].id).update({'user_id': None})
    return make_response(jsonify({'message': 'Device removed'}), 200)


def update_device(current_user, device_id):
    user_id = current_user[0].id
    devices = device_ref.where('user_id', '==', user_id).where(
        'id', '==', device_id).get()
    if len(devices) == 0:
        return make_response(jsonify({'error': 'Device not found'}), 400)
    doc_id = devices[0].id
    device = devices[0].to_dict()
    data = request.get_json()
    device['name'] = data['name']
    device_ref.document(doc_id).update(device)
    return make_response(jsonify({'message': 'Device updated', 'device': device}), 200)


# Only for Hardware
def update_location(device_id):
    device = device_ref.where(
        'unique_id', '==', device_id).get()
    if len(device) == 0:
        return make_response(jsonify({'error': 'Device not found'}), 400)
    data = request.get_json()
    device_id = device[0].id
    device = device[0].to_dict()
    device['location'] = data['location']
    device_ref.document(device_id).update(device)
    return make_response(jsonify({'message': 'Location updated', 'location': device['location']}), 200)
