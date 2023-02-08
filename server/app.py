from flask import Flask, make_response, jsonify
from services.auth import login, register, token_required
from db.db import db
from services.user import update_user, get_all_users, get_current_user, get_all_feedbacks, add_user_feedback
from services.device import create_device, get_device, get_all_devices, delete_device, add_device, update_location, remove_device, update_device
from services.alert import create_alert
from flask_cors import CORS
from services.reward import add_reward

user_ref = db.collection('users')
device_ref = db.collection('devices')
alert_ref = db.collection('alerts')

# Init app
app = Flask(__name__)
app.debug = True
CORS(app, resources={r"/*": {"origins": "*"}})


# Api routes --------------------


@app.route('/')
def hello_world():
    return 'Hello, World!'


# User routes -------------------

@app.route('/users', methods=['GET'])
@token_required
def get_users(current_user):
    return get_all_users(current_user)


@app.route('/user', methods=['GET'])
@token_required
def get_user(current_user):
    return get_current_user(current_user)


@app.route('/user', methods=['POST'])
@token_required
def handle_update_user(current_user):
    return update_user(current_user)


@app.route('/feedbacks', methods=['GET'])
@token_required
def get_feedbacks(current_user):
    return get_all_feedbacks(current_user)


@app.route('/feedbacks', methods=['POST'])
@token_required
def add_feedback(current_user):
    return add_user_feedback(current_user)

# Auth routes --------------------


@app.route('/auth/login', methods=['POST'])
def handle_login():
    return login()


@app.route('/auth/register', methods=['POST'])
def handle_register():
    return register()


# Alert routes -------------------

@app.route('/alert', methods=['POST'])
def handle_alert():
    return create_alert()


# Reward System routes ----------------------

@app.route('/reward/<device_id>', methods=['POST'])
def handle_reward(device_id):
    return add_reward(device_id)


# Device Routes ------------------
@app.route('/devices', methods=['POST'])
@token_required
def handle_create_device(current_user):
    return create_device(current_user)


@app.route('/devices/<device_id>', methods=['GET'])
@token_required
def handle_get_device(current_user, device_id):
    return get_device(current_user, device_id)


@app.route('/devices', methods=['GET'])
@token_required
def handle_get_all_devices(current_user):
    return get_all_devices(current_user)


@app.route('/devices/<device_id>', methods=['DELETE'])
@token_required
def handle_delete_device(current_user, device_id):
    return delete_device(current_user, device_id)


@app.route('/devices/<device_id>', methods=['PUT'])
@token_required
def handle_update_device(current_user, device_id):
    return update_device(current_user, device_id)


@app.route('/devices/add/<device_id>', methods=['PUT'])
@token_required
def handle_add_device(current_user, device_id):
    return add_device(current_user, device_id)


@app.route('/devices/remove/<device_id>', methods=['PUT'])
@token_required
def handle_remove_device(current_user, device_id):
    print('remove')
    return remove_device(current_user, device_id)


@app.route('/location/<device_id>', methods=['POST'])
def handle_update_location(device_id):
    return update_location(device_id)


@app.route('/dashboard', methods=['GET'])
@token_required
def get_dashboard(current_user):

    if (current_user[0].to_dict()['role'] != 'admin'):
        return make_response(jsonify({'message': 'Not allowed'}), 401)

    no_of_devices = device_ref.get()
    no_of_devices = len(no_of_devices)
    no_of_users = user_ref.get()
    no_of_users = len(no_of_users) - 1
    no_of_alerts = alert_ref.get()
    no_of_alerts = len(no_of_alerts)

    res = {
        'users': no_of_users,
        'devices': no_of_devices,
        'alerts': no_of_alerts
    }

    return make_response(jsonify(res), 200)


if __name__ == '__main__':
    app.run(debug=False, host='0.0.0.0')
