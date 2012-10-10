package modules.led;


import java.awt.AWTException;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;

import device.OutputLED;

import modules.ModuleLED;


public class ConstantColor extends ModuleLED implements Runnable {
	
	int red;
	int green;
	int blue;
	
	/**
	 * Display one color. Default color is white (255,255,255).
	 * 
	 * @param output
	 *            The object to send the output to.
	 */
	public ConstantColor(OutputLED output) {
		this(output, 254, 254, 254);
	}

	/**
	 * Display one color.
	 * 
	 * @param output
	 *            The object to send the output to.
	 * @param red
	 *            the value of the red color component. range: [0-255]
	 * @param green
	 *            the value of the green color component. range: [0-255]
	 * @param blue
	 *            the value of the blue color component. range: [0-255]
	 */
	public ConstantColor(OutputLED output, int red, int green, int blue) {
		super(output);
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	@Override
	public void run() {
		output.sendRGB(red, green, blue);
	}
}
