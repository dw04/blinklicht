package output;

import gnu.io.CommPortIdentifier;

import java.util.Enumeration;
import java.util.LinkedList;

public class ConnectionManager {

	LinkedList<SerialDevice> serialDeviceList;
	
	public ConnectionManager(){
		serialDeviceList = new LinkedList<SerialDevice>();
	}
	
	
	public boolean connectDevices(){
		boolean success = false;
		
		//determine the port to use
				String port = null;
				Enumeration<?> portEnum = CommPortIdentifier.getPortIdentifiers();
				while (portEnum.hasMoreElements()) {
					CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
					System.out.println(currPortId.getName() + " " +currPortId.getPortType() + " " +currPortId.isCurrentlyOwned());
//					for (String portName : PORT_NAMES) {
//						if (currPortId.getName().equals(portName)) {
//							port = portName;
//							break;
//						}
//					} 
				}
				if (port == null) {
					System.out.println("Could not find any COM port.");
					return false;
				}
		
		return success;
	}
	
	
}
