/*
  Blink
  Turns on an LED on for one second, then off for one second, repeatedly.
 
  This example code is in the public domain.
 */

byte input = 0;
long lastDebounceTime = 0;
int mode = 1;
int numberOfModes = 2;

void setup() {                
  // initialize the digital pin as an output.
  // Pin 13 has an LED connected on most Arduino boards:
  pinMode(9, OUTPUT);    
  pinMode(10, OUTPUT);   
  pinMode(11, OUTPUT);    
  pinMode(2, INPUT);
  //mode = 2;
  Serial.begin(115200);
  attachInterrupt(0, pressed, FALLING);
  Serial.println("Arduino started");
}


void pressed(){
  
  if((millis() - lastDebounceTime) > 500){ //Button entprellen
    lastDebounceTime = millis();
     mode++;
     if(mode > numberOfModes){
       mode = 1;
     }
     Serial.println(mode);
  }
  
 
} 


int serReadInt()
{
 int i, serAva;                           // i is a counter, serAva hold number of serial available
 char inputBytes [7];                 // Array hold input bytes
 char * inputBytesPtr = &inputBytes[0];  // Pointer to the first element of the array
     
 if (Serial.available()>0)            // Check to see if there are any serial input
 {
   //delay(1);                              // Delay for terminal to finish transmitted
   delayMicroseconds(200); //0.5ms        // 5mS work great for 9600 baud (increase this number for slower baud)
   serAva = Serial.available();  // Read number of input bytes
   for (i=0; i<serAva; i++)       // Load input bytes into array
     inputBytes[i] = Serial.read();
   inputBytes[i] =  '\0';             // Put NULL character at the end
   return atoi(inputBytesPtr);    // Call atoi function and return result
 }
 else
   return -1;                           // Return -1 if there is no input
}


void loop() {
  
  switch(mode){
    case 1:
    
      if(Serial.available() > 4){   //Empty serial port stack
         delayMicroseconds(200); //0.5ms
         while(Serial.available() > 0){
           input = Serial.read();
         }
      }  
      if(Serial.available()){
        //Serial.println("got here");
        //Serial.println(Serial.available());
        input = Serial.read();
        int val = -1;
       // delay(1);
        delayMicroseconds(200); //0.5ms
        if(Serial.available()){
           val = serReadInt();
        }
        if(val >= 0 && val < 255){
          if(input == 'R'){
            analogWrite(11, val); //rot
          }
          else if(input == 'G'){
            analogWrite(10, val); //grün
          }
          else if(input == 'B'){
            analogWrite(9, val); //blau
          }
        }
      }
       break;
    case 2:
    //fade up
      for (int brightness = 0; brightness < 255; brightness++) {
          analogWrite(9, brightness); //blau
          analogWrite(10, brightness);  //grün
          analogWrite(11, brightness);  //rot
          delay(12);
      } 
  
      delay(400);
  
      //fade down
      for (int brightness = 255; brightness > 0; brightness--) {
          analogWrite(9, brightness);
          analogWrite(10, brightness);
          analogWrite(11, brightness);
      delay(12);
      } 
      delay(200); 
     break; 
    default:
     Serial.println("This point should not be reached");
     break;
  }
     
     
      
}
