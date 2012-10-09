package device;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;


import gnu.io.CommPortIdentifier; 
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent; 
import gnu.io.SerialPortEventListener; 

public class SerialDevice implements Device{

	SerialPort serialPort;

	 String PORT; //"/dev/tty.usbserial-A8008EGb"

	 OutputStream output;
	 InputStream input;
	 int TIMEOUT = 2000;
	 int BAUD; // = 115200;
	
	public SerialDevice(String port, int baud){
		BAUD = baud;
		PORT = port;
	}
	
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.close();
		}
	}

	public String connect() throws InterruptedException {
		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		// iterate through, looking for the port
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();		
				if (currPortId.getName().equals(PORT)) {
					portId = currPortId;
					break;
				}	
		}

		if (portId == null) {
			System.out.println("SERIAL DEVICE " + PORT + " NOT FOUND!" );
			return "";
		}

		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
					TIMEOUT);

			// set port parameters
			serialPort.setSerialPortParams(BAUD,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			// open the streams	
			output = serialPort.getOutputStream();
			input = serialPort.getInputStream();
			
			long currentTime = System.currentTimeMillis();
			byte indata[] = new byte[0];
			String data = "";
			System.out.println("   waiting for input... ");
			while ((System.currentTimeMillis() - currentTime)<5000) { //wait 2seconds
				if(input.available() > 0){
					indata = new byte[input.available()];
					input.read(indata, 0, input.available());
					data += new String(indata);
				}
			}
			
			if(data.length() > 0) {
				return data;
			}
			else{
				return null;
			}
			
		} catch (Exception e) {
			System.err.print(portId.getName() + ": ");
			System.err.println(e.toString());
		}
		
		
		
		return "";	
	}

	public void send(String str) throws InterruptedException, IOException{
		//Thread.sleep(0, 200000); //0.5ms
		output.write(str.getBytes());
		output.flush();
	}
	
}
