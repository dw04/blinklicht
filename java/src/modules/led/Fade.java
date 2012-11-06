package modules.led;
import device.OutputLED;
import modules.*;

public class Fade extends ModuleLED implements Runnable{

	private int fadeInterval; // in milliseconds
	
	public int getFadeInterval() {
		return fadeInterval;
	}

	public void setFadeInterval(int fadeInterval) {
		this.fadeInterval = fadeInterval;
	}

	/**
	 * 
	 * @param out
	 * @param fadeinterval time to fade up in milliseconds
	 */
	public Fade(OutputLED out, int fadeinterval){
		super(out);
		this.fadeInterval = fadeinterval;
		stop = false;
		pause = false;
		
	}
	
	public Fade(OutputLED out){
		this(out,1000);
	}
	


	@Override
	public void run() {
		try {
			while(!stop){
				if(pause)
					Thread.sleep(fadeInterval);
				else{
					for(int i = 0; i <= 254; i++){
						if(stop) break;
						output.sendRGB(i, i, i);
						Thread.sleep(fadeInterval/255);
					}
					for(int i = 254; i >= 0; i--){
						if(stop) break;
						output.sendRGB(i, i, i);		
						Thread.sleep(fadeInterval/255);			
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
		return "fade";
	}
}
