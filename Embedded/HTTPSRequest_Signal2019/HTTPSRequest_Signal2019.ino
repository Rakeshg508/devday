#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <ArduinoJson.h>

const char* signalId_1 = "u101";
const char* signalId_2 = "u102";
const char* signalId_3 = "u103";
const char* signalId_4 = "u104";

const char* host = "10.198.83.155";
const char* port = "8080";

int defaultTime_1 = 60;
int defaultTime_2 = 60;
int defaultTime_3 = 60;
int defaultTime_4 = 60;

int currentDefaultTime = 60;

String url = String("http://");

const char* ssid = "Wless-innovation";
const char* password = "Inn0v@t!0n";

int singnalNumber = 0;
int defaultDuration = 30;
int calculatedDuration = 0;

int output1 = 4;
int output2 = 5;

void setup() {
  // make pins as output controlling the light
  pinMode(output1, OUTPUT);
  digitalWrite(output1, LOW);
  pinMode(output2, OUTPUT);
  digitalWrite(output2, LOW);

  // prepare serial
  Serial.begin(115200);
  Serial.println();
  Serial.print("connecting to ");
  Serial.println(ssid);
  WiFi.begin(ssid, password);
  int count = 40;
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
    if (count > 0)
    {
      --count;
    }
    else
    {
      break;
    }
  }
  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
  Serial.println("===================================================================================================================================================");
}

int calculateDuration(String json, int singnalNumber)
{
  int length = json.length();
  char jsonChar[json.length() + 1];

  for (int i = 0; i <= length; ++i)
  {
    jsonChar[i] = json.charAt(i);
  }
  jsonChar[length] = 0;
  StaticJsonBuffer<100> jsonBuffer;
  JsonObject& root = jsonBuffer.parseObject(jsonChar);
  if (!root.success())
  {
    Serial.println("parseObject() failed");
    return 0;
  }
  String signalId = root["signalId"];
  int currentTime = root["currentTime"];
  int defaultTime = root["defaultTime"];
  switch (singnalNumber) {
    case 0:
      defaultTime_1 = defaultTime;
      break;
    case 1:
      defaultTime_2 = defaultTime;
      break;
    case 2:
      defaultTime_3 = defaultTime;
      break;
  }
  Serial.println("SignalId : " + signalId);
  Serial.println("Default time : " + String(defaultTime));
  Serial.println("Current time : " + String(currentTime));
  return currentTime;
}

void sendCommandToTurnOnLight(int count)
{
  switch (count)
  {
    case 0:
      digitalWrite(output1, 0);
      digitalWrite(output2, 1);
      //manupulateSignals(0,1);
      break;
    case 1:
      digitalWrite(output1, 1);
      digitalWrite(output2, 0);
      //manupulateSignals(1,0);
      break;
    case 2:
      digitalWrite(output1, 1);
      digitalWrite(output2, 1);
      //manupulateSignals(1,1);
      break;
  }
}
/*void manupulateSignals(int input1, int input2) {
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
*/
String getUrl(String unitId) {
  String urlComplete = url + host + ":" + port + "/itcu/traffic/info?" + "signalId=" + unitId;
  Serial.println(urlComplete);
  return urlComplete;
}

void loop() {
  calculatedDuration = 0;
  if (WiFi.status() == WL_CONNECTED)
  {
    String url;
    switch (singnalNumber)
    {
      case 0:
        url = getUrl(signalId_1);
        currentDefaultTime = defaultTime_1;
        break;
      case 1:
        url = getUrl(signalId_2);
        currentDefaultTime = defaultTime_2;
        break;
      case 2:
        url = getUrl(signalId_3);
        currentDefaultTime = defaultTime_3;
        break;
    }
    HTTPClient http;
    Serial.print("Requesting URL: ");
    Serial.println(url);
    http.begin(url);
    int httpCode = http.GET();
    //Check the returning code
    Serial.println("request sent");
    if (httpCode > 0) {
      // Parsing
      String line = http.getString();
      Serial.println("Responce:");
      Serial.println("==========");
      Serial.println(line);
      Serial.println("==========");
      Serial.println("closing connection");

      // calculate delta duration
      calculatedDuration = calculateDuration(line, singnalNumber);
    }
    http.end();   //Close connection  }
  }
  else
  {
    WiFi.begin(ssid, password);
    Serial.println("Connecting to wifi...");
  }
  if (calculatedDuration == 0)
  {
    calculatedDuration = currentDefaultTime;
  }

  sendCommandToTurnOnLight(singnalNumber);
  if (singnalNumber == 2) {
    singnalNumber = 0;
  }
  else {
    ++singnalNumber;
  }

  Serial.println("Green time: " + String(calculatedDuration));
  Serial.println("===================================================================================================================================================");
  delay(calculatedDuration * 1000);
}
