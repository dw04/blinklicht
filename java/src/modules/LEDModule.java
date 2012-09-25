package modules;

import output.LEDOutput;

public abstract class LEDModule {

	
	protected boolean STOP;
	protected LEDOutput out;
	
	public LEDModule(LEDOutput out){
		this.out = out;
		STOP = false;
	}
	
	public void stop(){
		STOP = true;
	}
	
	public  void pause(){
		
	}
}
