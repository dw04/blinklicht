package device;

import java.io.IOException;

public class DeviceLED extends Device implements OutputRGB {

	public enum Code {
		D_CODE, T_CODE
	}

	Code codeType;

	public DeviceLED(Device sp, Code c) {
		super(sp.PORT, sp.BAUD);
		this.input = sp.input;
		this.output = sp.output;
		this.codeType = c;
		this.serialPort = sp.serialPort;
		this.TIMEOUT = sp.TIMEOUT;
	}

	public DeviceLED(String port, int baud, Code c) throws InterruptedException {
		super(port, baud);
		super.connect();

		this.codeType = c;
	}

	public void setCodeType(Code c) {
		this.codeType = c;
	}

	public void sendRGB(int r, int g, int b) {
		try {
			if (codeType == Code.D_CODE) {
				super.send("R" + r);
				super.send("G" + g);
				super.send("B" + b);
			} else if (codeType == Code.T_CODE) {
				super.send(Integer.toHexString(1)+Integer.toHexString(r)+Integer.toHexString(g)+Integer.toHexString(b));
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
