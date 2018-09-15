package com.onekey.takeaway.bean;


public class DoorBean extends MsgBean{
	InnerDoorBean door;
	
	public InnerDoorBean getDoor() {
		return door;
	}

	public void setDoor(InnerDoorBean door) {
		this.door = door;
	}

	@Override
	public String toString() {
		return "DoorBean [door=" + door + "]";
	}

	public static class InnerDoorBean {
		String doorCode;
		String devCode;
		public String getDoorCode() {
			return doorCode;
		}
		public void setDoorCode(String doorCode) {
			this.doorCode = doorCode;
		}
		public String getDevCode() {
			return devCode;
		}
		public void setDevCode(String devCode) {
			this.devCode = devCode;
		}
		@Override
		public String toString() {
			return "DoorBean [doorCode=" + doorCode + ", devCode=" + devCode + "]";
		}
	}
	
	
	
}
