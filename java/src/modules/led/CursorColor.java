package modules.led;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;

import output.LEDOutput;
import modules.Module;

public class CursorColor implements Runnable, Module {

	private LEDOutput output;
	private boolean stop;
	private boolean pause;
	private int refreshInterval;
	
	/**
	 * Change color depending on the color of the pixel the mouse pointer is
	 * pointing at. Default refresh interval is 50 milliseconds.
	 * 
	 * @param output
	 *            The object to send the output to.
	 */
	public CursorColor(LEDOutput output) {
		this(output, 50);
	}
	
	/**
	 * Change color depending on the color of the pixel the mouse pointer is
	 * pointing at.
	 * 
	 * @param output
	 *            The object to send the output to.
	 * @param refreshInterval
	 *            How many milliseconds the thread should wait before the next
	 *            refresh. default is 50.
	 */
	public CursorColor(LEDOutput output, int refreshInterval) {
		this.output = output;
		this.refreshInterval = refreshInterval;
		stop = false;
		pause = false;
	}

	@Override
	public void run() {
		try {
	        Point coord;
	        Robot robot = new Robot();
	        
			while(!stop){
				if(pause)
					Thread.sleep(refreshInterval);
				else{
					coord = MouseInfo.getPointerInfo().getLocation();       
					Color color = robot.getPixelColor((int)coord.getX(), (int)coord.getY());
					output.sendRGB(color.getRed(),color.getGreen(),color.getBlue()-100);
					robot.delay(refreshInterval);
				}
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AWTException e) {
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
