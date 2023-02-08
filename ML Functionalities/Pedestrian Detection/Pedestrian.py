import cv2
import numpy as np 
import IPython.display as ipd
vid='sample video.mp4'

def load_yolo():
	net = cv2.dnn.readNet("yolov3.weights", "yolov3.cfg.txt")
	classes = []
	with open("coco.names.txt", "r") as f:
		classes = [line.strip() for line in f.readlines()] 
	
	output_layers = [layer_name for layer_name in net.getUnconnectedOutLayersNames()]
	colors = np.random.uniform(0, 255, size=(len(classes), 3))
	return net, classes, colors, output_layers

def detect_objects(img, net, outputLayers):			
	blob = cv2.dnn.blobFromImage(img, scalefactor=0.00392, size=(320, 320), mean=(0, 0, 0), swapRB=True, crop=False)
	net.setInput(blob)
	outputs = net.forward(outputLayers)
	return blob, outputs

def get_box_dimensions(outputs, height, width):
	boxes = []
	confs = []
	class_ids = []
	for output in outputs:
		for detect in output:
			scores = detect[5:]
			print(scores)
			class_id = np.argmax(scores)
			conf = scores[class_id]
			if conf > 0.3:
				center_x = int(detect[0] * width)
				center_y = int(detect[1] * height)
				w = int(detect[2] * width)
				h = int(detect[3] * height)
				x = int(center_x - w/2)
				y = int(center_y - h / 2)
				boxes.append([x, y, w, h])
				confs.append(float(conf))
				class_ids.append(class_id)
	return boxes, confs, class_ids

def draw_labels(boxes, confs, colors, class_ids, classes, img): 
	indexes = cv2.dnn.NMSBoxes(boxes, confs, 0.5, 0.4)
	font = cv2.FONT_HERSHEY_PLAIN
	for i in range(len(boxes)):
		if i in indexes:
			x, y, w, h = boxes[i]
			label = str(classes[class_ids[i]])
			if(label=='person'):
				ipd.Audio('beep.mp3', autoplay=True)
			color = colors[i]
			cv2.rectangle(img, (x,y), (x+w, y+h), color, 2)
			cv2.putText(img, label, (x, y - 100), font, 1, color, 1)
	cv2.imshow("Image", img)

def start_video(video_path=vid):
		model, classes, colors, output_layers = load_yolo()
		cap = cv2.VideoCapture(video_path)
		frame_no=0
		frame_skip=30
		while True:
			_, frame = cap.read()
			frame_no+=1
			if (frame_no % frame_skip == 0):
				height, width, channels = frame.shape
				blob, outputs = detect_objects(frame, model, output_layers)
				boxes, confs, class_ids = get_box_dimensions(outputs, height, width)
				draw_labels(boxes, confs, colors, class_ids, classes, frame)
				key = cv2.waitKey(1)
				if key == 27:
					break
		cap.release()

start_video()

