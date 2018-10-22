package com.onekey.takeaway.bean;

import java.util.List;

public class CabinetBean extends MsgBean{
	
	List<InnerCabinetBean> devicelist;
	
	public List<InnerCabinetBean> getDevicelist() {
		return devicelist;
	}

	public void setDevicelist(List<InnerCabinetBean> devicelist) {
		this.devicelist = devicelist;
	}

	@Override
	public String toString() {
		return "CabinetBean [devicelist=" + devicelist + "]";
	}

	public static class InnerCabinetBean {
		String deviceID;
		int status;
		String name;
		String deviceType;
		List<DoorListBean> doorList;
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
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getDeviceType() {
			return deviceType;
		}
		public void setDeviceType(String deviceType) {
			this.deviceType = deviceType;
		}
		
		public List<DoorListBean> getSlots() {
			return doorList;
		}
		public void setSlots(List<DoorListBean> doorList) {
			this.doorList = doorList;
		}
		@Override
		public String toString() {
			return "InnerCabinetBean [deviceID=" + deviceID + ", status="
					+ status + ", name=" + name + ", deviceType=" + deviceType
					+ ", slots=" + doorList + "]";
		}
		
		
	}
	
	public static class DoorListBean {

		/**
		 * 0 no 1 has
		 */
		String containFood;
		String doorCode;
		/**
		 * 0 no 1 has
		 */
		String backDoor;
		/**
		 * 0 no 1 has
		 */
		String frontDoor;
		public String getContainFood() {
			return containFood;
		}
		public void setContainFood(String containFood) {
			this.containFood = containFood;
		}
		public String getDoorCode() {
			return doorCode;
		}
		public void setDoorCode(String doorCode) {
			this.doorCode = doorCode;
		}
		public String getBackDoor() {
			return backDoor;
		}
		public void setBackDoor(String backDoor) {
			this.backDoor = backDoor;
		}
		public String getFrontDoor() {
			return frontDoor;
		}
		public void setFrontDoor(String frontDoor) {
			this.frontDoor = frontDoor;
		}
		@Override
		public String toString() {
			return "DoorListBean [containFood=" + containFood + ", doorCode="
					+ doorCode + ", backDoor=" + backDoor + ", frontDoor="
					+ frontDoor + "]";
		}
		
	}
}
