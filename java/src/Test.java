import java.io.IOException;

import modules.led.Fade;

import output.SerialDevice;
import output.SerialLEDDevice;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		
		try {
			SerialLEDDevice led = new SerialLEDDevice("/dev/tty.usbserial-A8008EGb", 115200, SerialLEDDevice.Code.D_CODE);
			
			Fade fade = new Fade(led,5000);
			Thread t1 = new Thread(fade);
			t1.start();
			
			Thread.sleep(20000);
			fade.stop(); //noch unschön evt Fade direkt als thread o.ä. t1.stop würde gehen davon wird aber abgeraten
			
			led.close();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
		
//		SerialConnection con = new SerialConnection("/dev/tty.usbserial-A8008EGb", 115200);
//		try {
//			con.connect();
//			Thread.sleep(2000);
//			con.send("G254");
//			
//			Thread.sleep(5000);
//			con.close();
//			
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}

}
