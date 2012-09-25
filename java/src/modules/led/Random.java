package modules.led;

import output.LEDOutput;
import modules.LEDModule;

public class Random extends LEDModule implements Runnable{

	private int interval;
	
	/**
	 * 
	 * @param out
	 * @param interval interval of color in ms
	 */
	public Random(LEDOutput out, int interval){
		super(out);
		this.interval = interval;
	}
	
	@Override
	public void run() {
		try {
			while(!STOP){		
				//System.out.println((int)Math.random()*254);
					out.sendRGB((int)(Math.random()*254.), (int)(Math.random()*254.), (int)(Math.random()*254.));
					Thread.sleep(interval);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}



}
