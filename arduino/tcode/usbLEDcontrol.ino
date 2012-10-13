#define LED1r 5
#define LED1g 6
#define LED1b 3
#define LED2w 9
#define LED3w 10
#define LED4w 11

void setup()
{
  pinMode(LED1r, OUTPUT);
  pinMode(LED1g, OUTPUT);
  pinMode(LED1b, OUTPUT);
  pinMode(LED2w, OUTPUT);
  pinMode(LED3w, OUTPUT);
  pinMode(LED4w, OUTPUT);
  ///debug: set color green on LED1
  analogWrite(LED1r, 0);
  analogWrite(LED1g, 100);
  analogWrite(LED1b, 0);
  //debug: set 100 on LED2
  analogWrite(LED2w, 100);
  // set up Serial library at 115200 bps
  Serial.begin(115200);
  delay(1000);
  //transmit capabilities
  Serial.print("TCODE-LED1:RGB;LED2:WHITE;LED3:WHITE;LED4:WHITE");
}

int parseHex(char c) {
    if(c < '0')
      return 0;
    if(c <= '9')
      return c - '0';
    if(c < 'A')
      return 0;
    if(c <= 'F')
      return (c - 'A')+10;
    if(c > 'F')
      return 15;
}

void serialEvent(){
  if(Serial.available()>6){
    int stripID=parseHex(Serial.read());
    int rValue=parseHex(Serial.read())*16+parseHex(Serial.read());
    int gValue=parseHex(Serial.read())*16+parseHex(Serial.read());
    int bValue=parseHex(Serial.read())*16+parseHex(Serial.read());
    
    Serial.print("strip:");
    Serial.print(stripID);
    
    if(stripID==1){
      Serial.print("r:");
      Serial.print(rValue);
      Serial.print("g:");
      Serial.print(gValue);
      Serial.print("b:");
      Serial.println(bValue);
      
      analogWrite(LED1r, rValue);
      analogWrite(LED1g, gValue);
      analogWrite(LED1b, bValue);
    }
    if(stripID==2){
      Serial.print("w:");
      Serial.println(rValue);
      analogWrite(LED2w, rValue);
    }
    if(stripID==3){
      Serial.print("w:");
      Serial.println(rValue);
      analogWrite(LED3w, rValue);
    }
    if(stripID==4){
      Serial.print("w:");
      Serial.println(rValue);
      analogWrite(LED4w, rValue);
    }
  }
}

void loop()
{
}
