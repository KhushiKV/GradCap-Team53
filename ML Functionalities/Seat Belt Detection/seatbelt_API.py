#API Used - https://modelplace.ai/models/seat-belt-detector/?utm_source=blog_modelplace&utm_medium=refferal&utm_campaign=blog_modelplace&utm_content=seat-belt-detector

import pandas as pd
import IPython.display as ipd
data = [[False, False]]
df = pd.DataFrame(data, columns=['Label', 'Machine_dct'])

import cv2
import time

cam = cv2.VideoCapture(0)

while True:
    ret, frame = cam.read()
    cv2.imshow("test", frame)
    k = cv2.waitKey(1)
    if k%256 == 27:
        # ESC pressed
        print("Escape hit, closing...")
        break
    elif k%256 == 32:
        # SPACE pressed
        cv2.imshow("IMG", frame)
        cv2.imwrite('IMG.jpg', frame)
        cv2.destroyWindow('IMG')
        break
     
import json
import time
import requests

response_inference = json.loads(
    requests.post(
        "https://api.modelplace.ai/v3/process",
        params={"model_id": "64",},
        files={"upload_data": open("IMG.jpg", "rb"),},
    ).content
)

print("Waiting for completion", end="")
while True:
    response_task = json.loads(
        requests.get(
            "https://api.modelplace.ai/v3/task", params=response_inference
        ).content
    )
    if response_task["status"] != "finished":
        print(end=".")
        time.sleep(5)
    else:
        if(len(response_task["result"])==1):
          if(response_task["result"][0]['class_name']=='Seat_belt'):
            df['Label'][0]=True
        else:
            df['Label'][0]=False
            ipd.Audio('beep.mp3', autoplay=True)
        print("/n")
        print(df)
        
        exit(0)
        break