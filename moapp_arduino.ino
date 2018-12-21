int led1 = 8;
int led2 = 9;
int led3 = 10;

char serial_input;

void setup() {
  // put your setup code here, to run once:
  pinMode(led1,OUTPUT);
  pinMode(led2,OUTPUT);
  pinMode(led3,OUTPUT);
  Serial.begin(9600);
}

void loop() {
  // put your main code here, to run repeatedly:
  if(Serial.available()){
    serial_input = Serial.read();
  }

  if(serial_input == '3'){
    digitalWrite(led1,HIGH);
  }
  else if(serial_input == '4'){
    digitalWrite(led1,LOW);
  }
  else if(serial_input == '5'){
    digitalWrite(led2,HIGH);
  }
  else if(serial_input == '6'){
    digitalWrite(led2,LOW);
  }
  else if(serial_input == '7'){
    digitalWrite(led3,HIGH);
  }
  else if(serial_input == '8'){
    digitalWrite(led3,LOW);
  }

  delay(100);
}
