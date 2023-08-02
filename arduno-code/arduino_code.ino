#include <SoftwareSerial.h>

//************ Sensor bluetooth *********//
#define TX_PIN A1
#define RX_PIN A2
char value;
SoftwareSerial bluetooth(RX_PIN, TX_PIN);
int baudRate[] = {300, 1200, 2400, 4800, 9600, 19200, 38400, 57600, 115200};
char kytu = "";
String chuoi = "";
int runningMode = 1;

#define m1 6  //Right Motor MA1
#define m2 7  //Right Motor MA2
#define m3 9  //Left Motor MB1
#define m4 8  //Left Motor MB2
#define e1 3  //Right Motor Enable Pin EA
#define e2 11 //Left Motor Enable Pin EB

//**********3 Channel IR Sensor Connection**********//
#define ir2 13
#define ir3 A0
#define ir4 12

#define MAX_SPEED 50
#define FORWARD_SPEED 50

void setup() {
  Serial.begin(9600);
  while (!Serial) {}

  Serial.println("Configuring, please wait...");

  for (int i = 0 ; i < 9 ; i++) {
    bluetooth.begin(baudRate[i]);
    String cmd = "AT+BAUD4";
    bluetooth.print(cmd);
    bluetooth.flush();
    delay(100);
  }

  bluetooth.begin(9600);
  Serial.println("Config done");
  while (!bluetooth) {}


  pinMode(m1, OUTPUT);
  pinMode(m2, OUTPUT);
  pinMode(m3, OUTPUT);
  pinMode(m4, OUTPUT);
  pinMode(e1, OUTPUT);
  pinMode(e2, OUTPUT);
  pinMode(ir2, INPUT);
  pinMode(ir3, INPUT);
  pinMode(ir4, INPUT);
}

void loop() {
  //Reading Sensor Values
  int s2 = digitalRead(ir2);  //Left Sensor
  int s3 = digitalRead(ir3);  //Middle Sensor
  int s4 = digitalRead(ir4);  //Right Sensor
  
  // Serial.println("------------------");
  // Serial.println(s2);
  // Serial.println(s3);
  // Serial.println(s4);
  // Serial.println("------------------");


  if (bluetooth.available()) {
    delay(10); //Delay added to make thing stable
    kytu = bluetooth.read();
    chuoi = chuoi + kytu;
    Serial.println(kytu);

    if (chuoi == "c") {
      if (runningMode == 1) {
        runningMode = 0;
        Serial.println(runningMode);
      } else if (runningMode == 0) {
        runningMode = 1;
        stop();
        Serial.println(runningMode);
      }
      chuoi = "";
    }
    if (runningMode == 1) {
      if (chuoi == "f") {
        forward(FORWARD_SPEED);
        delay(2000); 
        stop();
        Serial.println(chuoi);
        chuoi = "";

      }
      if (chuoi == "b") {
        di_lui(MAX_SPEED);
        delay(2000);
        stop();
        Serial.println(chuoi);
        chuoi = "";

      }
      if (chuoi == "l") {
        turn_left(MAX_SPEED);
        delay(500);
        stop();
        Serial.println(chuoi);
        chuoi = "";

      }
      if (chuoi == "r") {
        turn_right(MAX_SPEED);
        delay(500);
        stop();
        Serial.println(chuoi);
        chuoi = "";

      }
    }
  }
  else {
    if (runningMode == 0) {
      if ((s2 == 0) && (s3 == 0) && (s4 == 0))
      {
        // forward(FORWARD_SPEED);
        stop();
      }
      else if ((s2 == 0) && (s3 == 0) && (s4 == 1))
      {
        turn_left(MAX_SPEED);
      }
      else if ((s2 == 0) && (s3 == 1) && (s4 == 1))
      {
        turn_left(MAX_SPEED);
      }
      else if ((s2 == 1) && (s3 == 0) && (s4 == 0))
      {
        turn_right(MAX_SPEED);
      }
      else if ((s2 == 1) && (s3 == 0) && (s4 == 1))
      {
        turn_right(MAX_SPEED);
      }
      else if ((s2 == 1) && (s3 == 1) && (s4 == 0))
      {
        turn_right(MAX_SPEED);
      }
      else if (((s2 == 0) && (s3 == 1) && (s4 == 0)) || ((s2 == 1) && (s3 == 1) && (s4 == 1)))
      { //going forward
        forward(FORWARD_SPEED);
      }
    }
  }
}

void forward(int speed_val) {
  analogWrite(e1, 100); //you can adjust the speed of the motors from 0-255
  analogWrite(e2, 100); //you can adjust the speed of the motors from 0-255
  digitalWrite(m1, HIGH);
  digitalWrite(m2, LOW);
  digitalWrite(m3, HIGH);
  digitalWrite(m4, LOW);
}

void turn_left(int speed_val) {
  analogWrite(e1, 100); //you can adjust the speed of the motors from 0-255
  analogWrite(e2, 100); //you can adjust the speed of the motors from 0-255
  digitalWrite(m1, LOW);
  digitalWrite(m2, LOW);
  digitalWrite(m3, HIGH);
  digitalWrite(m4, LOW);
}

void turn_right(int speed_val) {
  analogWrite(e1, speed_val); //you can adjust the speed of the motors from 0-255
  analogWrite(e2, speed_val); //you can adjust the speed of the motors from 0-255
  digitalWrite(m1, HIGH);
  digitalWrite(m2, LOW);
  digitalWrite(m3, LOW);
  digitalWrite(m4, LOW);
}


void di_lui(int speed_val) {
  analogWrite(e1, speed_val); //you can adjust the speed of the motors from 0-255
  analogWrite(e2, speed_val); //you can adjust the speed of the motors from 0-255
  digitalWrite(m1, LOW);
  digitalWrite(m2, HIGH);
  digitalWrite(m3, LOW);
  digitalWrite(m4, HIGH);
}

void stop() {
  digitalWrite(m1, LOW);
  digitalWrite(m2, LOW);
  digitalWrite(m3, LOW);
  digitalWrite(m4, LOW);
}

