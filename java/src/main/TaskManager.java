package main;
import io.ConnectionManager;
import io.LEDOutput;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

import modules.Module;
import modules.led.*;
import clientcommands.Commands;
import clientcommands.Commands.Command;

/**
 * This class is responsible for starting and stopping modules
 * All inputs are processed within this class. Therefore a job queue is used.
 * @author danielwilbers
 *
 */
public class TaskManager {

	//static ConcurrentLinkedQueue<Command> jobs = new ConcurrentLinkedQueue<Command>();
	static LinkedList<Module> moduleList = new LinkedList<Module>();
	
	
	static ConnectionManager conManager;
	
	public TaskManager(ConnectionManager con){
		conManager = con; 
		for(LEDOutput out :conManager.getLEDOutputList()){
			None n = new None(out);
			n.run();
		}
		
	}
	
	public static void executeCommand(Command c){
		//jobs.add(c);
		System.out.println("execute command: " + c.getAction() + " " + c.getModule());
		
		//choose device  //TODO:currently only first device is used
		if(conManager.getLEDOutputList().size() > 0){
			LEDOutput out = conManager.getLEDOutputList().getFirst();
			
			//choose action
			if(c.getAction() == Command.Action.START){
				switch (c.getModule()){
					case NONE:	
						break;
					case FADE:
						Fade fade = new Fade(out,1000);
						new Thread(fade).start();
						moduleList.add(fade);
						break;
					case CONSTANT:
						ConstantColor cons = new ConstantColor(out);
						new Thread(cons).start();
						moduleList.add(cons);
						break;
					case RANDOM:
						Random random = new Random(out,1000);
						new Thread(random).start();
						moduleList.add(random);
						break;
			}
			}
			else if (c.getAction() == Command.Action.STOP){
			
				for(Module m : moduleList){ //currently just for testing
					m.stop();
				}
				
				None n = new None(out);
				n.run();
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	
		
		
		
		
	}
	

}


//STEVE!!!
