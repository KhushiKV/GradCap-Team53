from flask import request, make_response, jsonify
from db.db import db
from werkzeug.security import generate_password_hash, check_password_hash
import jwt
from functools import wraps
import uuid

user_ref = db.collection('users')


# Decorator to check if token is valid
def token_required(f):
    @wraps(f)
    def decorated(*args, **kwargs):
        token = None
        if 'Authorization' in request.headers:
            token = request.headers['Authorization']
        if not token:
            return jsonify({'message': 'Token is missing !!'}), 401

        try:
            # decoding the payload to fetch the stored details
            data = jwt.decode(token, 'secret', algorithms=['HS256'])
            current_user = user_ref.where('id', '==', data['sup']).get()
        except Exception as e:
            return make_response(jsonify({'error': e}), 401)
        # returns the current logged in users contex to the routes
        return f(current_user, *args, **kwargs)

    return decorated


def login():
    email = request.json.get('email')
    password = request.json.get('password')

    if not email or not password:
        return make_response(jsonify({'error': 'Email or password is empty'}), 400)

    # find user by email
    users = user_ref.where('email', '==', email).get()
    if len(users) == 0:
        return make_response(jsonify({'error': 'Email not found'}), 400)
    user = users[0].to_dict()

    if not check_password_hash(user['password'], password):
        return make_response(jsonify({'error': 'Password is incorrect'}), 400)

    # generate token
    token = jwt.encode({
        'sup': user['id'],
        'email': user['email'],
    }, 'secret', 'HS256')
    del user['password']
    return make_response(jsonify({'token': token, 'user': user, 'message': "Logged in successfully"}), 200)


def register():

    email = request.json.get('email')
    password = request.json.get('password')

    if not email or not password:
        return make_response(jsonify({'error': 'Email or password is empty'}), 400)

    # find user by email
    users = user_ref.where('email', '==', email).get()
    if len(users) > 0:
        return make_response(jsonify({'error': 'Email already exists'}), 400)

    userDoc = {
        'id': str(uuid.uuid4()),
        'name': request.json.get('name'),
        'email': email,
        'password': generate_password_hash(password),
        'role': 'user',
    }
    try:
        user_ref.add(userDoc)
        return make_response(jsonify({'message': "Registered Successfully"}), 201)
    except Exception as e:
        return make_response(jsonify({'error': str(e)}), 400)
