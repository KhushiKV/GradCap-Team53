from db.db import db
from flask import request, make_response, jsonify
from twilio.rest import Client

alert_ref = db.collection('alerts')

client = Client("AC621f85babe090a21bc31c54d9bd74474",
                "a4d73d1f589c9117223c514a51ff1d06")


def create_alert():
    # SMS
    location = request.json.get('location')
    link = 'https://www.google.com/maps/search/?api=1&query=' + \
        str(location[0]) + '%2c' + str(location[1]) + ''

    client.messages.create(to=["+916393716756"], from_="+16064044591",
                           body="This is the location of emergency: " +
                           link + " (Shared via SMS)")

    # CALL

    call = client.calls.create(
        twiml='<Response><Say voice="alice">An emergency has been detected! Location has been shared via SMS.</Say></Response>',
        url='https://handler.twilio.com/twiml/EHa25f9617d549a5c9b6ae1f41dbd27205', to='+916393716756', from_='+16064044591')
    print(call.sid)

    alert_ref.add(request.json)

    return make_response(jsonify({'message': 'Alert created - SMS and Call'}), 200)

    # data = request.get_json()
    # alert_ref.add(data)
    # sendSMS()
