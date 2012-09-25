import gnu.io.CommPortIdentifier;

import java.io.IOException;
import java.util.Enumeration;

import modules.led.CursorColor;
import modules.led.Fade;

import output.SerialDevice;
import output.SerialLEDDevice;

public class Test {

	private static final String PORT_NAMES[] = { 
		"/dev/ttyUSB0", // Linux
		"/dev/tty.usbserial-A8008EGb", // Mac OS X
		"COM3" // Windows
	};
	
	public static void main(String[] args) {
		//determine the port to use
		String port = null;
		Enumeration<?> portEnum = CommPortIdentifier.getPortIdentifiers();
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			for (String portName : PORT_NAMES) {
				if (currPortId.getName().equals(portName)) {
					port = portName;
					break;
				}
			}
		}
		if (port == null) {
			System.out.println("Could not find COM port.");
			return;
		}

		try {
			//TODO: automatically determine code version
			SerialLEDDevice led = new SerialLEDDevice(port, 115200, SerialLEDDevice.Code.T_CODE);
			
			Fade fade = new Fade(led,1000);
			System.out.println("run module Fade for 5 seconds");
			new Thread(fade).start();
			Thread.sleep(5000);
			fade.stop();
			System.out.println("module Fade done");
			
			CursorColor cursor = new CursorColor(led);
			System.out.println("run module CursorColor for 20 seconds, move your cursor arround!");
			new Thread(cursor).start();
			Thread.sleep(20000); //run module CursorColor for 20 seconds
			fade.stop();
			System.out.println("module CursorColor done");
			
			led.close();
			System.out.println("connection closed, test complete");
			
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
