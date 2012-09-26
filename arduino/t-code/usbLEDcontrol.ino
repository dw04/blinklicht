#define redPin 5
#define greenPin 6
#define bluePin 3

int redvalue;
int greenvalue;
int bluevalue;

void setup()
{
  pinMode(redPin, OUTPUT);
  pinMode(greenPin, OUTPUT);
  pinMode(bluePin, OUTPUT);
  //set color red
  analogWrite(redPin, 100);
  analogWrite(greenPin, 0);
  analogWrite(bluePin, 0);
  // set up Serial library at 115200 bps
  Serial.begin(115200);
}



void loop()
{
  while (Serial.available() > 11)
  {
    redvalue = Serial.parseInt();
    greenvalue = Serial.parseInt();
    bluevalue = Serial.parseInt();
    //if (Serial.read() == '\n') {
      redvalue = constrain(redvalue, 0, 255);
      greenvalue = constrain(greenvalue, 0, 255);
      bluevalue = constrain(bluevalue, 0, 255); 
      analogWrite(redPin, redvalue);
      analogWrite(greenPin, greenvalue);
      analogWrite(bluePin, bluevalue);
    //}
  }
}
