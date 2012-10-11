package modules.led;

import device.OutputLED;
import modules.ModuleLED;

public class None extends ModuleLED implements Runnable {

	public None(OutputLED out) {
		super(out);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		output.sendRGB(0, 0, 0);
	}
	
	@Override
	public String getName() {
		return "none";
	}

}
