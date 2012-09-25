package modules;

import output.LEDOutput;

public abstract class LEDModule implements Module {
	protected LEDOutput output;
	protected boolean stop;
	protected boolean pause;
	
	public LEDModule(LEDOutput output){
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
}
