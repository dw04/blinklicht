package io;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.LinkedList;

import device.LEDcolor;
import device.OutputLED;
import device.OutputOutlet;
import device.Device;
import device.DeviceLED;

public class ConnectionManager {

	
	

	LinkedList<OutputLED> outputLEDList;
	public LinkedList<OutputLED> getOutputLEDList() {
		return outputLEDList;
	}
	public OutputLED getOutputLED(int id) {
		for(OutputLED outputLED : outputLEDList)
			if(outputLED.getID()==id)
				return outputLED;
		return null;
	}
	LinkedList<OutputOutlet> outputOutletList;
	public LinkedList<OutputOutlet> getOutputOutletList() {
		return outputOutletList;
	}
	private LinkedList<Device> allDevices;
	
	private static final int DATA_RATE = 115200;
	public static int getDataRate() {
		return DATA_RATE;
	}

	public ConnectionManager() {
		outputLEDList = new LinkedList<OutputLED>();
		outputOutletList = new LinkedList<OutputOutlet>();
		allDevices = new LinkedList<Device>();
	}
	
	/**
	 * Connects all devices and creates the socket connection
	 * @return returns true if at least one device is connected 
	 */
	public boolean connectSerialDevices() {
		Enumeration<?> portEnum = CommPortIdentifier.getPortIdentifiers();
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();	
			System.out.print(currPortId.getName()+": ");
			if (currPortId.getName().contains("tty") && !currPortId.getName().contains("ttyS0")) {
				System.out.println("try connect...");
				Device sd = new Device(currPortId.getName(),DATA_RATE);
				String s;
				try {
					s = sd.connect();
					if(s != null && !s.isEmpty()) {
						if(!parseInput(s,sd)){
							sd.close();
						}
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			else{
				System.out.println("skipped.");
			}
		}
		
		return (allDevices.size()>0);
	}
	
	/**
	 * This method matches an input string to a known device
	 * @param str
	 * @param sp
	 * @throws IOException
	 */
	private boolean parseInput(String str, Device sp) throws IOException{
		System.out.println("   input string: " + str );
		if(str.equals("LED-1-DCODE")){
			DeviceLED led = new DeviceLED(sp,DeviceLED.Code.D_CODE);
			outputLEDList.add(new OutputLED(led, 1, LEDcolor.RGB));
			allDevices.add(led);
			return true;
		}else if(str.contains("TCODE")){
			System.out.println("   found TCODE device");
			DeviceLED led = new DeviceLED(sp,DeviceLED.Code.T_CODE);
			allDevices.add(led);
			String[] outputs=str.split("-")[1].split(";");
			for(String output : outputs){
				int id=Integer.parseInt(output.substring(3,4));
				String type=output.substring(5);
				if(type.equals("RGB"))
					outputLEDList.add(new OutputLED(led, id, LEDcolor.RGB));		
				if(type.equals("WHITE"))
					outputLEDList.add(new OutputLED(led, id, LEDcolor.WHITE));					
			}
			return true;
		}
		return false;
	}

	public void closeAllDevices(){
		for(Device sd : allDevices){
			sd.close();
		}
	}
	



	

}
