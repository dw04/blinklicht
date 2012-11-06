package main;
import io.ConnectionManager;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

import device.OutputLED;

import protobuf.Commands;
import protobuf.Commands.Command;

import modules.Module;
import modules.led.*;

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
		for(OutputLED out :conManager.getOutputLEDList()){
			None n = new None(out);
			n.run();
		}
		
	}
	
	public static void executeCommand(Command c){
		//jobs.add(c);
		System.out.println("execute command: " + c.getAction() + " " + c.getModule());
		
		//choose device  //TODO:currently only first device is used
		if(conManager.getOutputLEDList().size() > 0){
			OutputLED out = conManager.getOutputLEDList().getFirst();
			
			//choose action
			//if(c.hasAction() && c.getAction() == Command.Action.START){
			if(c.hasModule()){
				switch (c.getModule()){
					case NONE:	
						break;
					case FADE:
						Fade fade = new Fade(out,1000);
						new Thread(fade).start();
						moduleList.add(fade);
						break;
					case CONSTANT:
						//check if module exists for specified output, only create new if not existing
						//System.out.println(out.getCurrentModule().getClass());
						if(out.getCurrentModule() == null || out.getCurrentModule().getClass() != ConstantColor.class ){
							//stop running module
							if(out.getCurrentModule() != null) out.getCurrentModule().stop();
							
							//start constant
							ConstantColor cons;
							if(c.hasBlue() || c.hasRed() || c.hasGreen()  ){
								cons = new ConstantColor(out,c.getRed(),c.getGreen(),c.getBlue());
							}else{
								cons = new ConstantColor(out);
							}
							out.setCurrentModule(cons);
							Thread t = new Thread(cons);
							t.start();
							cons.setModuleThread(t);
							moduleList.add(cons);
						}else{ //module for out is constantcolor
							ConstantColor cons = (ConstantColor) out.getCurrentModule();
							if(c.hasRed()) cons.setRed(c.getRed());
							if(c.hasGreen()) cons.setGreen(c.getGreen());
							if(c.hasBlue()) cons.setBlue(c.getBlue());
							
							try{
								synchronized(cons.getModuleThread()){
									cons.getModuleThread().notify();
								}					
							}
							catch(Exception e){
								e.printStackTrace(System.err);
							}
							
							
							
						}
						
						break;
					case RANDOM:
						Random random = new Random(out,1000);
						new Thread(random).start();
						moduleList.add(random);
						break;
			}
			}
			else if (c.hasAction() && c.getAction() == Command.Action.STOP){
			
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
