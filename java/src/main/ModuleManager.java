package main;

import io.ConnectionManager;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import device.OutputLED;

import modules.Module;
import modules.led.None;

public class ModuleManager {
	private Map<Integer, Module> outputOccupation = new HashMap<Integer, Module>();
	private ConnectionManager conManager;
	
	public ConnectionManager getConnectionManager(){
		return conManager;
	}
	
	public ModuleManager(ConnectionManager connection){
		conManager = connection; 
		for(OutputLED out :conManager.getOutputLEDList()){
			start(out.getID(), "none");
		}
	}
	
	public void start(int output, String module){
		start(output, module, new HashMap<String, Integer>());
	}
	
	public void start(int output, String module, Map<String, Integer> parameters){
		if(module.equals("none")){
			None n = new None(conManager.getOutputLED(output));
			n.run();
			outputOccupation.put(output, n);
		}
	}
}
