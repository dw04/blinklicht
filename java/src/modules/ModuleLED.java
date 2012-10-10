package modules;

import device.OutputLED;

public abstract class ModuleLED implements Module {
	protected OutputLED output;
	protected boolean stop;
	protected boolean pause;
	
	public ModuleLED(OutputLED output){
		this.output = output;
		stop = false;
		pause = false;
	}
	
	@Override
	public void stop() {
	
	
			stop = true; //important that this is set before sendRGB(0,0,0), so the run method terminates

		
	}

	@Override
	public void resume() {
		pause=false;
	}

	@Override
	public void pause() {
		pause=true;
	}
}
