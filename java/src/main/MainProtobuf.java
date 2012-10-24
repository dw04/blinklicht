package main;

import io.ConnectionManager;
import io.ProtobufInput;

public class MainProtobuf {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	
		System.out.println("Connecting Devices ...");
		ConnectionManager conManager = new ConnectionManager();
		conManager.connectSerialDevices();
		conManager.printResult();

		
		if(conManager.getOutputLEDList().size()>0){
			System.out.print("Starting protobuf socket...");
			new Thread(new ProtobufInput()).start();
			System.out.println("finished.");
			
			System.out.print("Creating TaskManager...");
			TaskManager taskManager = new TaskManager(conManager);
			System.out.println("finished.");
		}
		
		
//		Thread tarray[] = new Thread[100];
//		int num = Thread.enumerate(tarray);
//		for(int i = 0; i <num;i++){
//			tarray[i].interrupt();
//		}
	
		
	}

}
