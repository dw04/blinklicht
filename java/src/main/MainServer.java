package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import device.LEDcolor;
import device.OutputLED;

import io.ConnectionManager;
import io.ProtobufInput;
import io.WebServer;


public class MainServer {
	public static void main(String[] args) {
		System.out.println("Connecting Devices ...");
		ConnectionManager conManager = new ConnectionManager();
		conManager.connectSerialDevices();
		conManager.printResult();
		
	//	if(conManager.getOutputLEDList().size()>0){
			WebServer ws = new WebServer();
			ws.start(8000, conManager);
			
			/*
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			try {
				while(true){
					System.out.print("device: ");
					int id=Integer.parseInt(br.readLine());
					if(id==99)
						break;
					OutputLED device=conManager.getOutputLED(id);
					if(device.getColor()==LEDcolor.RGB){
						System.out.print("r: ");
						int r=Integer.parseInt(br.readLine());
						System.out.print("g: ");
						int g=Integer.parseInt(br.readLine());
						System.out.print("b: ");
						int b=Integer.parseInt(br.readLine());
						device.sendRGB(r, g, b);
					}else if(device.getColor()==LEDcolor.WHITE){
						System.out.print("w: ");
						int white=Integer.parseInt(br.readLine());
						device.sendWhite(white);
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			conManager.closeAllDevices();*/
	//	}
	}
}
