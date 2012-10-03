package main;
import io.ConnectionManager;

import java.util.concurrent.ConcurrentLinkedQueue;
import clientcommands.Commands;
import clientcommands.Commands.Command;

/**
 * This class is responsible for starting and stopping modules
 * All inputs are processed within this class. Therefore a job queue is used.
 * @author danielwilbers
 *
 */
public class TaskManager {

	static ConcurrentLinkedQueue<Command> jobs = new ConcurrentLinkedQueue<Command>();
	
	ConnectionManager conManager;
	
	public TaskManager(ConnectionManager con){
		conManager = con;
	}
	
	public static void addCommand(Command c){
		jobs.add(c);
		System.out.println("added job: " + c.getAction() + " " + c.getModule());
	}

}


//STEVE!!!
