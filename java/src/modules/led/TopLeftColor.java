package modules.led;

import io.LEDOutput;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;

import modules.LEDModule;
import modules.Module;

public class TopLeftColor extends LEDModule implements Runnable {

	private int refreshInterval;

	/**
	 * Change color depending on the color of the pixel in the top left corner.
	 * Default refresh interval is 500 milliseconds.
	 * 
	 * @param output
	 *            The object to send the output to.
	 */
	public TopLeftColor(LEDOutput output) {
		this(output, 100);
	}

	/**
	 * Change color depending on the color of the pixel in the top left corner.
	 * 
	 * @param output
	 *            The object to send the output to.
	 * @param refreshInterval
	 *            How many milliseconds the thread should wait before the next
	 *            refresh. default is 50.
	 */
	public TopLeftColor(LEDOutput output, int refreshInterval) {
		super(output);
		this.refreshInterval = refreshInterval;
	}

	@Override
	public void run() {
		try {
			Robot robot = new Robot();

			while (!stop) {
				if (pause)
					Thread.sleep(refreshInterval);
				else {
					Color color = robot.getPixelColor(1,1);
					output.sendRGB(color.getRed(), color.getGreen(),
							color.getBlue() - 100);
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

}
