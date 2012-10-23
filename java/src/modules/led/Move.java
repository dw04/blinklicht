package modules.led;
import java.util.List;

import device.OutputLED;
import modules.*;

public class Move extends ModuleLED implements Runnable{
	protected List<OutputLED> outputs;
	private int fadeInterval; // in milliseconds
	
	/**
	 * 
	 * @param out
	 * @param fadeinterval time to fade up in milliseconds
	 */
	public Move(List<OutputLED> outs, int fadeinterval){
		super(outs.get(0));
		this.fadeInterval = fadeinterval;
		stop = false;
		pause = false;
	}
	
	@Override
	public void run() {
		try {
			for(OutputLED output : outputs)
				output.sendRGB(0, 0, 0);
			outputs.get(0).sendRGB(255, 0, 0);
			while(!stop){
				if(pause)
					Thread.sleep(fadeInterval);
				else{					
					for(int n=0;n<outputs.size();n++){
						for(int i=0;i<255;i+=2){
							outputs.get(n).sendRGB(255-i, 0, 0);
							if(n<outputs.size()-1)
								outputs.get(n+1).sendRGB(i, 0, 0);
							else
								outputs.get(0).sendRGB(i, 0, 0);
							Thread.sleep(fadeInterval/255);
						}
					}
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public String getName() {
		return "move";
	}
}
