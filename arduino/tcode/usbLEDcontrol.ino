#define LED1r 5
#define LED1g 6
#define LED1b 3
#define LED2w 9

int stripID;
int LED1rValue;
int LED1gValue;
int LED1bValue;
int LED2wValue;

void setup()
{
  pinMode(LED1r, OUTPUT);
  pinMode(LED1g, OUTPUT);
  pinMode(LED1b, OUTPUT);
  pinMode(LED2w, OUTPUT);
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
  Serial.println("TCODE-LED1:RGB;LED2:WHITE");
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
    stripID=parseHex(Serial.read());
    if(stripID==1){
      LED1rValue=parseHex(Serial.read())*16+parseHex(Serial.read());
      LED1gValue=parseHex(Serial.read())*16+parseHex(Serial.read());
      LED1bValue=parseHex(Serial.read())*16+parseHex(Serial.read());
  
      Serial.print("strip:");
      Serial.print(stripID);
      Serial.print("r:");
      Serial.print(LED1rValue);
      Serial.print("g:");
      Serial.print(LED1gValue);
      Serial.print("b:");
      Serial.println(LED1bValue);
      
      analogWrite(LED1r, LED1rValue);
      analogWrite(LED1g, LED1gValue);
      analogWrite(LED1b, LED1bValue);
    }
    if(stripID==2){
      LED2wValue=parseHex(Serial.read())*16+parseHex(Serial.read());
      Serial.read();Serial.read();Serial.read();Serial.read();
      
      Serial.print("strip:");
      Serial.print(stripID);
      Serial.print("w:");
      Serial.println(LED2wValue);
      
      analogWrite(LED2w, LED2wValue);
    }
  }
}

void loop()
{
}
