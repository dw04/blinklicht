package device;

import java.io.IOException;

public class OutputLED {

	private DeviceLED device;
	private int id;
	LEDcolor color;
	
	public int getID(){
		return id;
	}
	
	public LEDcolor getColor(){
		return color;
	}
	
	public OutputLED(DeviceLED device, int id, LEDcolor color) {
		this.device=device;
		this.id=id;
		this.color=color;
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
