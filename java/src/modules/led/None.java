package modules.led;

import device.OutputRGB;
import modules.ModuleLED;

public class None extends ModuleLED implements Runnable {

	public None(OutputRGB output) {
		super(output);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void run() {
		output.sendRGB(0, 0, 0);
	}

}
