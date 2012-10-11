package main;

import io.ConnectionManager;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import device.LEDcolor;
import device.OutputLED;

import modules.Module;
import modules.ModuleLED;
import modules.led.ConstantColor;
import modules.led.Fade;
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
			ModuleLED mod = new None(conManager.getOutputLED(out.getID()));
			mod.run();
			outputOccupation.put(out.getID(), mod);
		}
	}
	
	public void action(int output, String module){
		action(output, module, new HashMap<String, Integer>());
	}
	
	public void action(int output, String moduleName, Map<String, Integer> parameters){
		if(output==0){
			for(OutputLED out :conManager.getOutputLEDList()){
				action(out.getID(), moduleName, parameters);
			}
		}else if(!outputOccupation.get(output).getName().equals(moduleName)){
			System.out.println("setting output "+output+" to "+moduleName+" was "+outputOccupation.get(output).getName());
			outputOccupation.get(output).stop();
			ModuleLED mod;
			if(moduleName.equals("none"))
				mod = new None(conManager.getOutputLED(output));
			else if(moduleName.equals("constant"))
				mod = new ConstantColor(conManager.getOutputLED(output),parameters.get("r"),parameters.get("g"),parameters.get("b"));
			else if(moduleName.equals("fade"))
				mod = new Fade(conManager.getOutputLED(output),500);
			else{
				mod = new None(conManager.getOutputLED(output));
				System.out.println("unknown module :"+moduleName);
			}
			mod.run();
			outputOccupation.put(output, mod);
		}else{//module is allready running
			System.out.println("output "+output+" allready is "+moduleName);
			if(moduleName.equals("constant")){
				if(parameters.get("id")==0){
					for(OutputLED out : conManager.getOutputLEDList()){
						if(out.getColor()==LEDcolor.RGB){
							out.sendRGB(parameters.get("r"), parameters.get("g"), parameters.get("b"));
						}else if(out.getColor()==LEDcolor.WHITE){
							out.sendRGB(parameters.get("d"), 0, 0);
						}
					}
				}else{
					OutputLED out = conManager.getOutputLED(parameters.get("id"));
					if(out.getColor()==LEDcolor.RGB){
						out.sendRGB(parameters.get("r"), parameters.get("g"), parameters.get("b"));
					}else if(out.getColor()==LEDcolor.WHITE){
						out.sendRGB(parameters.get("d"), 0, 0);
					}
				}
			}
		}
	}
}
