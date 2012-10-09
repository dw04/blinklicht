package modules.led;

import device.OutputRGB;
import modules.ModuleLED;

public class Random extends ModuleLED implements Runnable{

	private int interval;
	
	/**
	 * 
	 * @param out
	 * @param interval interval of color in ms
	 */
	public Random(OutputRGB out, int interval){
		super(out);
		this.interval = interval;
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
