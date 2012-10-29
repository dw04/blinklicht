package device;

public class OutputRadio implements OutputOutlet{

	private String identifier;
	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getUnitCode() {
		return unitCode;
	}

	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}

	private String unitCode;
	private DeviceRadio device;
	
	public OutputRadio(DeviceRadio device, String identifier, String unitCode){
		this.device = device;
		this.identifier = identifier;
		this.unitCode = unitCode;
		
		device.addSwitchableDevice(identifier, unitCode);
	}

	@Override
	public void turnOn() {
		device.turnOn(identifier);
	}

	@Override
	public void turnOff() {
		device.turnOff(identifier);
	}
	
}
