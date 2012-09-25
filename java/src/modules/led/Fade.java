package modules.led;
import output.LEDOutput;
import modules.*;

public class Fade implements Runnable, Module{

	private LEDOutput out;
	private boolean stop;
	private boolean pause;
	private int fadeInterval; // in milliseconds
	
	/**
	 * 
	 * @param out
	 * @param fadeinterval time to fade up in milliseconds
	 */
	public Fade(LEDOutput out, int fadeinterval){
		this.out = out;
		this.fadeInterval = fadeinterval;
		stop = false;
		pause = false;
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
						out.sendRGB(i, i, i);
						Thread.sleep(fadeInterval/255);
					}
					for(int i = 254; i >= 0; i--){
						if(stop) break;
						out.sendRGB(i, i, i);		
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
