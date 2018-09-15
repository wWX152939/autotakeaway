package com.onekey.takeaway.bean;

public class ModifyDevBean extends TokenBean {
	String deviceID;
	int status;
	public String getDeviceID() {
		return deviceID;
	}
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "ModifyDevBean [deviceID=" + deviceID + ", status=" + status
				+ "]";
	}


}
