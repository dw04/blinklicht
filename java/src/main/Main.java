package main;

import io.ConnectionManager;
import io.ProtobufInput;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	
		System.out.println("Connecting Devices ...");
		ConnectionManager conManager = new ConnectionManager();
		conManager.createConnections();
		System.out.println("...finished." + " result:  leddevices: " + conManager.getLEDOutputList().size());
		
		if(conManager.getLEDOutputList().size()>0){
			System.out.println("Starting protobuf socket...");
			new Thread(new ProtobufInput()).start();
			System.out.println("...finished.");
			
			System.out.println("Creating TaskManager...");
			TaskManager taskManager = new TaskManager(conManager);
			System.out.println("...finished.");
		}
		
		
//		Thread tarray[] = new Thread[100];
//		int num = Thread.enumerate(tarray);
//		for(int i = 0; i <num;i++){
//			tarray[i].interrupt();
//		}
	
		
	}

}
