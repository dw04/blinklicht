package main;
import io.ConnectionManager;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

import device.OutputLED;

import protobuf.Commands;
import protobuf.Commands.Command;

import modules.Module;
import modules.ModuleLED;
import modules.led.*;

/**
 * This class is responsible for starting and stopping modules
 * All inputs are processed within this class. Therefore a job queue is used.
 * @author danielwilbers
 *
 */
public class TaskManager {

	//static ConcurrentLinkedQueue<Command> jobs = new ConcurrentLinkedQueue<Command>();
	LinkedList<Module> moduleList = new LinkedList<Module>();
	
	HashMap<Command.Module,Class> classMap = new HashMap<Command.Module,Class>();
	
    ConnectionManager conManager;
	
	public TaskManager(ConnectionManager con){
		conManager = con; 
		for(OutputLED out :conManager.getOutputLEDList()){
			None n = new None(out);
			n.run();
		}
		
		classMap.put(Command.Module.CONSTANT, ConstantColor.class);
		classMap.put(Command.Module.CURSOR, CursorColor.class);
		classMap.put(Command.Module.FADE, Fade.class);
		classMap.put(Command.Module.NONE, None.class);
		classMap.put(Command.Module.RANDOM, Random.class);
		classMap.put(Command.Module.SCREEN, ScreenColor.class);
		
		
	}
	
	private void manageThreadAndModule(ModuleLED m){
		Thread t = new Thread(m);
		t.start();
		m.setModuleThread(t);
		moduleList.add(m);
	}
	
	public void executeCommand(Command c){
		//jobs.add(c);
		System.out.println("execute command: " + c.getAction() + " " + c.getModule());
		
		//choose device  //TODO:currently only first device is used
		if(conManager.getOutputLEDList().size() == 0) return;
	
		OutputLED out = conManager.getOutputLEDList().getFirst();
		
		if(c.hasModule()){
			
			boolean moduleExists = false;
			if(out.getCurrentModule() == null || out.getCurrentModule().getClass() != classMap.get(c.getModule()) ){
				if(out.getCurrentModule() != null) out.getCurrentModule().stop();
				moduleList.remove(out.getCurrentModule());
				moduleExists = false;
			}
			else{
				moduleExists = true;
			}
			
			switch (c.getModule()){
				case NONE:	
					break;
				case FADE:{
					Fade fade;
					if(!moduleExists){ //create module			
						
						if(c.hasInterval() ){
							fade = new Fade(out,c.getInterval());;
						}else{
							fade = new Fade(out); //with default color
						}
						out.setCurrentModule(fade);
						manageThreadAndModule(fade);
						
					}else{ //only change existing module
						fade = (Fade) out.getCurrentModule();
						if(c.hasInterval()) fade.setFadeInterval(c.getInterval());								
					}
					
					break;}
				case CONSTANT:{
					//check if module exists for specified output, only create new if not existing
					ConstantColor cons;
					if(!moduleExists){ //create module			
						
						if(c.hasBlue() || c.hasRed() || c.hasGreen()  ){
							cons = new ConstantColor(out,c.getRed(),c.getGreen(),c.getBlue());
						}else{
							cons = new ConstantColor(out); //with default color
						}
						out.setCurrentModule(cons);
						manageThreadAndModule(cons);
						
					}else{ //only change existing module
						cons = (ConstantColor) out.getCurrentModule();
						if(c.hasRed()) cons.setRed(c.getRed());
						if(c.hasGreen()) cons.setGreen(c.getGreen());
						if(c.hasBlue()) cons.setBlue(c.getBlue());
						
						synchronized(cons.getModuleThread()){
							cons.getModuleThread().notify();
						}					
								
					}
					
					break;}
				case RANDOM:{
					Random random;
					if(!moduleExists){ //create module			
						
						if(c.hasInterval() ){
							random = new Random(out,c.getInterval());;
						}else{
							random = new Random(out); //with default color
						}
						out.setCurrentModule(random);
						manageThreadAndModule(random);
						
					}else{ //only change existing module
						random = (Random) out.getCurrentModule();
						if(c.hasInterval()) random.setInterval(c.getInterval());								
					}
					break;}
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


//STEVE!!!
