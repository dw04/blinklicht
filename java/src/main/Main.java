package main;

import io.ConnectionManager;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	
		ConnectionManager conManager = new ConnectionManager();
		conManager.createConnections();
		
		TaskManager taskManager = new TaskManager(conManager);
		
		
		
	}

}
