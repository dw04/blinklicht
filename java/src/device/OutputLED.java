package device;

public class OutputLED {

	private DeviceLED device;
	private int id;
	LEDcolor color;
	
	public int getID(){
		return id;
	}
	
	public OutputLED(DeviceLED device, int id, LEDcolor color) {
		this.device=device;
		this.id=id;
		this.color=color;
	}
	
	public void sendWhite(int x){
		
	}
	
	public void sendRGB(int r, int b, int g){
		
	}
		
}
