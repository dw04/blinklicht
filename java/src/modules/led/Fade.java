package modules.led;
import output.LEDOutput;
import modules.*;

public class Fade extends LEDModule implements Runnable{

	
	
	private int fadeinterval; // in milliseconds
	
	/**
	 * 
	 * @param out
	 * @param fadeinterval time to fade up in milliseconds
	 */
	public Fade(LEDOutput out, int fadeinterval){
		super(out);
		this.fadeinterval = fadeinterval;
		STOP = false;
	}
	
	@Override
	public void run() {
		try {
			while(!STOP){
				for(int i = 0; i <= 254; i++){
					if(STOP) break;
					out.sendRGB(i, i, i);
					Thread.sleep(fadeinterval/255);
				}
				for(int i = 254; i >= 0; i--){
					if(STOP) break;
					out.sendRGB(i, i, i);		
					Thread.sleep(fadeinterval/255);			
				}
				if(STOP) break;
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	public void stop() {
		STOP = true;
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

}
