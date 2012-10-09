package device;

import java.io.IOException;


public class SerialLEDDevice extends SerialDevice implements OutputRGB{

	public enum Code{
		D_CODE,
		T_CODE
	}
	
	Code codeType;
	
	public SerialLEDDevice(SerialDevice sp, Code c){
		super(sp.PORT,sp.BAUD);
		this.input = sp.input;
		this.output = sp.output;
		this.codeType = c;
		this.serialPort = sp.serialPort;
		this.TIMEOUT = sp.TIMEOUT;
	}
	
	public SerialLEDDevice(String port, int baud, Code c) throws InterruptedException{
		super(port,baud);
		super.connect();
		
		this.codeType = c;
	}
	
	public void setCodeType(Code c){
		this.codeType = c;
	}
	
	public void sendRGB(int r, int g, int b){
		if(codeType == Code.D_CODE){
			try {
				super.send("R"+r);
				super.send("G"+g);
				super.send("B"+b);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} //TODO: check if rgb values are in valid range
		
		}
		else if(codeType == Code.T_CODE){
			try {
				super.send(r+","+g+","+b+".");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} //TODO: check if rgb values are in valid range
		
		}
		
	}
	
	
}
