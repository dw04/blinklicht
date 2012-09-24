package output;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import gnu.io.CommPortIdentifier; 
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent; 
import gnu.io.SerialPortEventListener; 

public class SerialConnection {

	SerialPort serialPort;

	private String PORT; //"/dev/tty.usbserial-A8008EGb"

	private OutputStream output;
	private int TIMEOUT = 2000;
	private int BAUD; // = 115200;
	
	public SerialConnection(String port, int baud){
		BAUD = baud;
		PORT = port;
	}
	
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.close();
		}
	}

	public void connect() throws InterruptedException {
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
			System.out.println("NO SERIAL PORTS AVAILABLE!");
			return;
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

		} catch (Exception e) {
			System.err.println(e.toString());
		}
		
		Thread.sleep(1500);
	}

	public void send(String str) throws InterruptedException, IOException{
		Thread.sleep(0, 200000); //0.5ms
		output.write(str.getBytes());
		output.flush();
	}
	
}
