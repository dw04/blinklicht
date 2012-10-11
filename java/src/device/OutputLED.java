package device;

import java.io.IOException;

import modules.ModuleLED;
import modules.led.ConstantColor;

public class OutputLED {

	private DeviceLED device;
	private int id;
	LEDcolor color;
	private int r;
	private int g;
	private int b;
	
	public int getID(){
		return id;
	}
	
	public LEDcolor getColor(){
		return color;
	}
	
	public int getR(){
		return r;
	}
	
	public int getG(){
		return g;
	}
	
	public int getB(){
		return b;
	}
	
	public OutputLED(DeviceLED device, int id, LEDcolor color) {
		this.device=device;
		this.id=id;
		this.color=color;
		r=255;
		g=255;
		b=255;
	}
	
	private static String toHex(int in){
		String out = Integer.toHexString(in);
		if(out.length()<2)
			return "0"+out;
		else return out;
	}
	
	public void sendWhite(int x){
		sendRGB(x,0,0);
	}
	
	public void sendRGB(int r, int g, int b) {
		this.r=r;
		this.g=g;
		this.b=b;
		try {
			if (device.getCodeType() == DeviceCode.D_CODE) {
				device.send("R" + r);
				device.send("G" + g);
				device.send("B" + b);
			} else if (device.getCodeType() == DeviceCode.T_CODE) {
				device.send(Integer.toHexString(id)+toHex(r)+toHex(g)+toHex(b));
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // TODO: check if rgb values are in valid range
	}
}
