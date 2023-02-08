import serial
import time
import requests
import random
crash_test=""
flag=False
api_url = "https://movesafe-server-production.up.railway.app"

ser=serial.Serial(
		port="/dev/ttyUSB2",
		baudrate=115200,
		timeout =1,
	
		)
		
def update_location(lat,lon):
		x = requests.post(api_url+"/location/movesafe-06-02-2023-9-40", json= {'location': [lat, lon]}, headers= {'Content-Type': 'application/json'})
		print(x.text)
		
def update_reward():
		x = requests.post(api_url+"/reward/movesafe-06-02-2023-9-40", json= {
		'dist' : int(35+ random.random()*5),
		'n_drowsy' : int(0+ random.random()*7),
		'n_drinkdrive' : int(0+ random.random()),
		'n_reckless' : int(0+ random.random()*7),
		'n_seatbelt' : int(0+ random.random()*7),
		't_overspeeding' : int(1+ random.random()*20)
		}, headers= {'Content-Type': 'application/json'})
		print(x.text)
	
	
c = 0

update_reward()
def accident_detection(lat,lon):
		x = requests.post(api_url+"/alert", json= {'location': [lat, lon],
		 'device_id':"movesafe-06-02-2023-9-40"}, headers= {'Content-Type': 'application/json'})
		print(x.text)


	

while(True):
		data = ser.readline().decode().split(',')
		print(data)
		c+=1
		if data==[""]:
			continue
		if(c%10==0 and c>10):
				lat = data[1]
				lon = data[2]
				crash_test=data[4]				# actual index value is 6
				update_location("26.1924", "75.7873")
		if flag==True:
			continue
		if crash_test=="1" :
			accident_detection("26.1924", "75.7873")
			flag=True
			print("")
		
