package io;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.LinkedList;

public class ConnectionManager {

	
	
	
	LinkedList<LEDOutput> ledOutputList;
	private LinkedList<Device> allDevices;

	private static final int DATA_RATE = 115200;

	public ConnectionManager() {
		ledOutputList = new LinkedList<LEDOutput>();
		allDevices = new LinkedList<Device>();
	}

	/**
	 * Connects all devices and creates the socket connection
	 * @return
	 */
	public boolean createConnections(){
		//connect SerialDevices
		connectSerialDevices();
		
		//create ProtobufInputSocket
		new Thread(new ProtobufInput()).start();
		
		
		return false;
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
			if (currPortId.getName().contains("tty")) {
				SerialDevice sd = new SerialDevice(currPortId.getName(),DATA_RATE);
				String s;
				try {
					s = sd.connect();
					if(!s.isEmpty()) {
						if(!parseInput(s,sd)){
							sd.close();
						}
					}
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
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
	private boolean parseInput(String str, SerialDevice sp) throws IOException{
		if(str.equals("LED-1-DCODE")){
			SerialLEDDevice led = new SerialLEDDevice(sp,SerialLEDDevice.Code.D_CODE);
			ledOutputList.add(led);
			allDevices.add(led);
			return true;
		}
		return false;
	}

	public void closeAll(){
		for(Device sd : allDevices){
			sd.close();
		}
	}

	public LinkedList<LEDOutput> getLEDOutputList() {
		return ledOutputList;
	}

	public void setSerialLEDDeviceList(
			LinkedList<LEDOutput> serialLEDOutputList) {
		this.ledOutputList = serialLEDOutputList;
	}

	public static int getDataRate() {
		return DATA_RATE;
	}
	

}
