package modules.led;

import device.OutputLED;
import modules.ModuleLED;

public class Random extends ModuleLED implements Runnable{

	private int interval;
	
	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	/**
	 * 
	 * @param out
	 * @param interval interval of color in ms
	 */
	public Random(OutputLED out, int interval){
		super(out);
		this.interval = interval;
		
	}
	
	public Random(OutputLED out){
		this(out,1000);
	}
	
	@Override
	public void run() {
		try {
			while(!stop){		
				//System.out.println((int)Math.random()*254);
					output.sendRGB((int)(Math.random()*254.), (int)(Math.random()*254.), (int)(Math.random()*254.));
					Thread.sleep(interval);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}



}
