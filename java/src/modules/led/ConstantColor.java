package modules.led;


import java.awt.AWTException;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.util.LinkedList;

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
		while(!stop){
			output.sendRGB(red, green, blue);	
			try {
				synchronized(this.getModuleThread()){
					this.getModuleThread().wait();
				}
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public String getName() {
		return "constant";
	}
	
	public int getRed() {
		return red;
	}

	public void setRed(int red) {
		this.red = red;
	}

	public int getGreen() {
		return green;
	}

	public void setGreen(int green) {
		this.green = green;
	}

	public int getBlue() {
		return blue;
	}

	public void setBlue(int blue) {
		this.blue = blue;
	}
}
