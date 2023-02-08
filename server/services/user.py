from flask import request, make_response, jsonify
from db.db import db

user_ref = db.collection('users')
feedbacks_ref = db.collection('feedbacks')


def get_all_users(current_user):
    if (current_user[0].to_dict()['role'] != 'admin'):
        return make_response(jsonify({'error': 'Unauthorized'}), 401)

    users = user_ref.get()
    users = [user.to_dict() for user in users]
    return make_response(jsonify(users), 200)


def get_current_user(current_user):
    user = current_user[0].to_dict()
    del user['password']
    return make_response(user, 200)


def update_user(current_user):
    data = request.get_json()
    try:
        user_ref.document(current_user[0].id).update(data)
        user = user_ref.document(current_user[0].id).get().to_dict()
        del user['password']
        return make_response(jsonify({'message': 'User updated successfully', 'doc': user}), 200)
    except Exception as e:
        return make_response(jsonify({'error': str(e)}), 400)


def add_user_feedback(current_user):
    feedback = request.get_json()['feedback']
    user_id = current_user[0].to_dict()['id']
    data = {
        'user_id': user_id,
        'feedback': feedback
    }

    try:
        feedbacks_ref.add(data)
        return make_response(jsonify({'message': 'Feedback submitted successfully'}), 200)
    except Exception as e:
        return make_response(jsonify({'error': str(e)}), 400)


def get_all_feedbacks(current_user):
    if (current_user[0].to_dict()['role'] != 'admin'):
        return make_response(jsonify({'error': 'Unauthorized'}), 401)

    feedbacks = feedbacks_ref.get()
    feedbacks = [feedback.to_dict() for feedback in feedbacks]
    return make_response(jsonify(feedbacks), 200)
