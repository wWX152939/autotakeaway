package com.onekey.takeaway.bean;


public class ModifyCabinetBean {
	
	String doorId;
	String status;
	String doorDirection;
	public String getDoorId() {
		return doorId;
	}
	public void setDoorId(String doorId) {
		this.doorId = doorId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDoorDirection() {
		return doorDirection;
	}
	public void setDoorDirection(String doorDirection) {
		this.doorDirection = doorDirection;
	}
	@Override
	public String toString() {
		return "ModifyCabinetBean [doorId=" + doorId + ", status=" + status
				+ ", doorDirection=" + doorDirection + "]";
	}

	

}
