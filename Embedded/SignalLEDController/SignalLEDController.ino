#include <SoftwareSerial.h>

// digital pin 2 has a pushbutton attached to it. Give it a name:
int input1 = 3;
int input2 = 4;

int signal1Red = 5;
int signal1Green = 6;
int signal2Red = 7;
int signal2Green = 8;
int signal3Red = 9;
int signal3Green = 10;

SoftwareSerial mySerial(11, 12); // RX, TX

// the setup routine runs once when you press reset:
void setup() {
  // initialize serial communication at 115200 bits per second:
  Serial.begin(9600);

  Serial.println("STS Started!!!");
  // set the data rate for the SoftwareSerial port
  mySerial.begin(9600);

  // make pins as input coming from wifi
  pinMode(input1, INPUT);
  pinMode(input2, INPUT);
  // make pins as output controlling the light
  pinMode(signal1Red, OUTPUT);
  digitalWrite(signal1Red, LOW);
  pinMode(signal1Green, OUTPUT);
  digitalWrite(signal1Green, LOW);
  pinMode(signal2Red, OUTPUT);
  digitalWrite(signal2Red, LOW);
  pinMode(signal2Green, OUTPUT);
  digitalWrite(signal2Green, LOW);
  pinMode(signal3Red, OUTPUT);
  digitalWrite(signal3Red, LOW);
  pinMode(signal3Green, OUTPUT);
  digitalWrite(signal3Green, LOW);
  Serial.println("Traffic signal started!!!");

}
void manupulateSignals(int input1, int input2) {
  if (input1 == LOW && input2 == HIGH) {
    digitalWrite(signal1Red, LOW);
    digitalWrite(signal1Green, HIGH); // ON

    digitalWrite(signal2Red, HIGH); // ON
    digitalWrite(signal2Green, LOW);

    digitalWrite(signal3Red, HIGH); // ON
    digitalWrite(signal3Green, LOW);

  }
  else if (input1 == HIGH && input2 == LOW) {
    digitalWrite(signal1Red, HIGH); // ON
    digitalWrite(signal1Green, LOW);

    digitalWrite(signal2Red, LOW);
    digitalWrite(signal2Green, HIGH); // ON

    digitalWrite(signal3Red, HIGH); // ON
    digitalWrite(signal3Green, LOW);

  } else if (input1 == HIGH && input2 == HIGH) {
    digitalWrite(signal1Red, HIGH); // ON
    digitalWrite(signal1Green, LOW);

    digitalWrite(signal2Red, HIGH); // ON
    digitalWrite(signal2Green, LOW);

    digitalWrite(signal3Red, LOW);
    digitalWrite(signal3Green, 1); // ON
  }
}

void serialDataExchange() {
  if (mySerial.available()) {
    Serial.write(mySerial.read());
  }
  if (Serial.available()) {
    mySerial.write(Serial.read());
  }
}

// the loop routine runs over and over again forever:
void loop() {
  serialDataExchange();
  // read the input pin:
  manupulateSignals(digitalRead(input1), digitalRead(input2));
}

