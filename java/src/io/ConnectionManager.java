package io;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.LinkedList;

import device.OutputRGB;
import device.Device;
import device.DeviceLED;

public class ConnectionManager {

	
	
	
	LinkedList<OutputRGB> ledOutputList;
	private LinkedList<Device> allDevices;

	private static final int DATA_RATE = 115200;

	public ConnectionManager() {
		ledOutputList = new LinkedList<OutputRGB>();
		allDevices = new LinkedList<Device>();
	}

	/**
	 * Connects all devices and creates the socket connection
	 * @return
	 */
	public boolean createConnections(){
		//connect SerialDevices
	
		return connectSerialDevices();
	}
	
	/**
	 * 
	 * @return returns true if at least one device is connected 
	 */
	public boolean connectSerialDevices() {
		boolean success = false;

		// determine the port to use
		String port = null;
		Enumeration<?> portEnum = CommPortIdentifier.getPortIdentifiers();
		SerialPort serialPort;
		int timeout = 2000;
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();	
			System.out.print(currPortId.getName()+" ... ");
			if (currPortId.getName().contains("tty") && !currPortId.getName().contains("ttyS0")) {
				System.out.println("try connect.");
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
			ledOutputList.add(led);
			allDevices.add(led);
			return true;
		}
		return false;
	}

	public void closeAllDevices(){
		for(Device sd : allDevices){
			sd.close();
		}
	}

	
	public LinkedList<OutputRGB> getLEDOutputList() {
		return ledOutputList;
	}

	public void setSerialLEDDeviceList(
			LinkedList<OutputRGB> serialLEDOutputList) {
		this.ledOutputList = serialLEDOutputList;
	}

	public static int getDataRate() {
		return DATA_RATE;
	}
	

}
