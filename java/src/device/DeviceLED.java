package device;

import java.io.IOException;

public class DeviceLED extends Device {

	private DeviceCode codeType;
	DeviceCode getCodeType(){
		return codeType;
	}

	public DeviceLED(Device sp, DeviceCode c) {
		super(sp.PORT, sp.BAUD);
		this.input = sp.input;
		this.output = sp.output;
		this.codeType = c;
		this.serialPort = sp.serialPort;
		this.TIMEOUT = sp.TIMEOUT;
	}

	public DeviceLED(String port, int baud, DeviceCode c) throws InterruptedException {
		super(port, baud);
		super.connect();

		this.codeType = c;
	}

	public void setCodeType(DeviceCode c) {
		this.codeType = c;
	}
}
