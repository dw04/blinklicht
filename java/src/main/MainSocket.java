package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import io.ConnectionManager;
import io.ProtobufInput;

public class MainSocket {
	public static void main(String[] args) {
		
		System.out.println("Connecting Devices ...");
		ConnectionManager conManager = new ConnectionManager();
		conManager.connectSerialDevices();
		System.out.println("...finished." + " result: RGBdevices: "+conManager.getOutputLEDList().size()+" LEDdevices: "+conManager.getOutputLEDList().size());
		
		if(conManager.getOutputLEDList().size()>0){
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			try {
				System.out.print("device: ");
				int id=Integer.parseInt(br.readLine());
				
				System.out.print("device");
				//device=conManager.getOutputLED(id);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			conManager.closeAllDevices();
		}
	}
}
