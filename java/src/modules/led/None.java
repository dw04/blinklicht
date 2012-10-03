package modules.led;

import io.LEDOutput;
import modules.LEDModule;

public class None extends LEDModule implements Runnable {

	public None(LEDOutput output) {
		super(output);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		output.sendRGB(0, 0, 0);
	}

}
