
#include <TinyGPSPlus.h>
#include <SoftwareSerial.h>

#define MQ3pin 0
#define led_b 5
#define led_r 6
#define led_y 7
#define led_g 8
#define buzz 9

#include <MPU6050_tockn.h>
#include <Wire.h>

float acc_x;
float acc_y;
float acc_z;
int gyroAng_x;
int gyroAng_y;
int gyroAng_z;

#define button 10
#define lim_switch 11

bool button_val = false;
bool lim_switch_val = true;

MPU6050 mpu6050(Wire);

const float acc_thresh = 1.5;

long timer = 0;

bool crash = false;
bool drunk = false;
int crash_timer = 0;

int counter = 0;

float sensorValue;
static const int RXPin = 4, TXPin = 3;
static const uint32_t GPSBaud = 9600;

// The TinyGPSPlus object
TinyGPSPlus gps;

// The serial connection to the GPS device
SoftwareSerial ss(RXPin, TXPin);

void setup()
{
  Serial.begin(115200);
  ss.begin(GPSBaud);

  Wire.begin();
  mpu6050.begin();
  mpu6050.calcGyroOffsets(true);

  pinMode(led_g, OUTPUT);
  pinMode(led_y, OUTPUT);
  pinMode(led_r, OUTPUT);
  pinMode(led_b, OUTPUT);
  pinMode(buzz, OUTPUT);
  pinMode(button, INPUT);
  pinMode(lim_switch, INPUT);
  Serial.print("Sensor warming up");
  delay(1000); // allow the MQ3 to warm up

}

void loop()
{

  mpu6050.update();

  acc_x = mpu6050.getAccX();
  acc_y = mpu6050.getAccY();
  acc_z = mpu6050.getAccZ();

  lim_switch_val = digitalRead(lim_switch);
  button_val = digitalRead(button);

  if((acc_x > acc_thresh) || (acc_y > acc_thresh) || (acc_z > acc_thresh) || (lim_switch_val == false)){
        crash = true;
        counter++;
          
    }
        
  else{
    crash = false;  
  }
  
  sensorValue = analogRead(MQ3pin); // read analog input pin 0

  if(sensorValue<140){
    digitalWrite(led_g, HIGH);
    digitalWrite(led_y, LOW);
    digitalWrite(led_r, LOW);
    drunk = false;
  }

  if(sensorValue>=140 && sensorValue<200){
    digitalWrite(led_g, LOW);
    digitalWrite(led_y, HIGH);
    digitalWrite(led_r, LOW);
    drunk = false;
  }

  if(sensorValue>200){
    digitalWrite(led_g, LOW);
    digitalWrite(led_y, LOW);
    digitalWrite(led_r, HIGH);
    drunk = true;
  }
  
  static const double LONDON_LAT = 51.508131, LONDON_LON = -0.128002;
  
  unsigned long distanceKmToLondon =
    (unsigned long)TinyGPSPlus::distanceBetween(
      gps.location.lat(),
      gps.location.lng(),
      LONDON_LAT, 
      LONDON_LON) / 1000;

  double courseToLondon =
    TinyGPSPlus::courseTo(
      gps.location.lat(),
      gps.location.lng(),
      LONDON_LAT, 
      LONDON_LON);

  const char *cardinalToLondon = TinyGPSPlus::cardinal(courseToLondon);
  
  smartDelay(1000);

  if(millis() - timer > 1000){
  
    printDateTime(gps.date, gps.time);
    Serial.print(",");
    printFloat(gps.location.lat(), gps.location.isValid(), 11, 6);
    Serial.print(",");
    printFloat(gps.location.lng(), gps.location.isValid(), 12, 6);
    Serial.print(",");
    printFloat(gps.speed.kmph(), gps.speed.isValid(), 6, 2);
    Serial.print(",");
    Serial.print(drunk);
    Serial.print(",");
    Serial.print(crash);
    Serial.print(",");
    Serial.print(button_val);
    Serial.print(",");
    Serial.println(lim_switch_val);

    timer = millis();
  }

  if(gps.speed.kmph()>4 || sensorValue>200){
    digitalWrite(led_b, HIGH);
    digitalWrite(buzz, HIGH);
  }
  else{
    digitalWrite(led_b, LOW);
    digitalWrite(buzz, LOW);
  }

}

// This custom version of delay() ensures that the gps object
// is being "fed".
static void smartDelay(unsigned long ms)
{
  unsigned long start = millis();
  do 
  {
    while (ss.available())
      gps.encode(ss.read());
  } while (millis() - start < ms);
}

static void printFloat(float val, bool valid, int len, int prec)
{
  if (!valid)
  {
    while (len-- > 1)
      Serial.print('*');
  }
  else
  {
    Serial.print(val, prec);
    int vi = abs((int)val);
    int flen = prec + (val < 0.0 ? 2 : 1); // . and -
    flen += vi >= 1000 ? 4 : vi >= 100 ? 3 : vi >= 10 ? 2 : 1;
    for (int i=flen; i<len; ++i)
      Serial.print(' ');
  }
  smartDelay(0);
}

static void printInt(unsigned long val, bool valid, int len)
{
  char sz[32] = "*****************";
  if (valid)
    sprintf(sz, "%ld", val);
  sz[len] = 0;
  for (int i=strlen(sz); i<len; ++i)
    sz[i] = ' ';
  if (len > 0) 
    sz[len-1] = ' ';
  Serial.print(sz);
  smartDelay(0);
}

static void printDateTime(TinyGPSDate &d, TinyGPSTime &t)
{
  if (!d.isValid())
  {
    Serial.print(F("**********"));
  }
  else
  {
    char sz[32];
    sprintf(sz, "%02d/%02d/%02d ", d.month(), d.day(), d.year());
    Serial.print(sz);
  }
  
  if (!t.isValid())
  {
    Serial.print(F("********"));
  }
  else
  {
    char sz[32];
    sprintf(sz, "%02d:%02d:%02d ", t.hour(), t.minute(), t.second());
    Serial.print(sz);
  }

  printInt(d.age(), d.isValid(), 5);
  smartDelay(0);
}

static void printStr(const char *str, int len)
{
  int slen = strlen(str);
  for (int i=0; i<len; ++i)
    Serial.print(i<slen ? str[i] : ' ');
  smartDelay(0);
}
