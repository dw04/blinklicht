package modules;

import device.OutputLED;

public abstract class ModuleLED implements Module, Runnable {
	
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
		stop = true;
	}

	@Override
	public void resume() {
		pause=false;
	}

	@Override
	public void pause() {
		pause=true;
	}
	
	@Override
	public String getName() {
		return "unknown";
	}
}
