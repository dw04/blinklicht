package device;

import java.io.IOException;
import java.util.HashMap;


public class DeviceRadio extends Device {
	
	HashMap<String,String> switchableDevices = new HashMap<String, String>();
		
	public DeviceRadio(Device sp) {
		super(sp.PORT, sp.BAUD);
		this.input = sp.input;
		this.output = sp.output;
		this.serialPort = sp.serialPort;
		this.TIMEOUT = sp.TIMEOUT;	
	}
	
	
	public DeviceRadio(String port, int baud) {
		super(port, baud);	
	}

	/**
	 * 
	 * @param identifier
	 * @param unitCode must only contain 0 or 1 ! from 1000 to 1111, e.g.  1000 represents A ON and B,C,D OFF
	 */
	public void addSwitchableDevice(String identifier , String unitCode){
		//identifier:
		// 1,2,3,4,5,6,7,8,9,...  for turning a device on
		// A,B,C,D,E,F,G,H,I,...	for turning a device off
		// so a device is e.g. the pair [1,A]
		
		//convert unitCode in serial protocol representation:
		if(unitCode.length()>4){
			System.err.println("unitCode invaild currently only up to 9 devices are supported");
			return;
		}
	//	String code = new StringBuffer(unitCode).reverse().toString();;
	//	Integer i = Integer.parseInt(code, 2);

	//	switchableDevices.put(identifier, i.toString());
		switchableDevices.put(identifier,unitCode);
	}


	public void turnOff(String identifier) {
		//always add + 16 ASCII value to get the corresponding OFF character
		String out = Character.toString((char)(((int)switchableDevices.get(identifier).charAt(0))+16));
		try {
			super.send(out);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	public void turnOn(String identifier) {
		try {
			super.send(switchableDevices.get(identifier));
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
