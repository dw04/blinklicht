import java.io.IOException;

import output.SerialConnection;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SerialConnection con = new SerialConnection("/dev/tty.usbserial-A8008EGb", 115200);
		try {
			con.connect();
			Thread.sleep(2000);
			con.send("G254");
			
			Thread.sleep(5000);
			con.close();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
