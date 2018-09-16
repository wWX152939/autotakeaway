package com.onekey.takeaway.bean;

import java.util.List;

public class DeviceFoodBean extends MsgBean{
	
	List<InnerDeviceBean> devicelist;
	
	public List<InnerDeviceBean> getDevicelist() {
		return devicelist;
	}

	public void setDevicelist(List<InnerDeviceBean> devicelist) {
		this.devicelist = devicelist;
	}

	@Override
	public String toString() {
		return "CabinetBean [devicelist=" + devicelist + "]";
	}

	public static class InnerDeviceBean {
		String deviceID;
		int status;
		String name;
		int deviceType;
		List<InnerDeviceFoodBean> slots;
		public String getDeviceID() {
			return deviceID;
		}
		public void setDeviceID(String deviceID) {
			this.deviceID = deviceID;
		}
		public String getStatusName() {
			String ret = "";
			switch (status) {
			case 0:
				ret = "空闲";
				break;
			case 1:
				ret = "工作";
				break;
			case 3:
				ret = "故障";
				break;
			case 2:
				ret = "完成";
				break;
			}
			return ret;
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
		
		public int getDeviceType() {
			return deviceType;
		}
		public void setDeviceType(int deviceType) {
			this.deviceType = deviceType;
		}
		public List<InnerDeviceFoodBean> getSlots() {
			return slots;
		}
		public void setSlots(List<InnerDeviceFoodBean> slots) {
			this.slots = slots;
		}
		@Override
		public String toString() {
			return "InnerDeviceBean [deviceID=" + deviceID + ", status="
					+ status + ", name=" + name + ", deviceType=" + deviceType
					+ ", slots=" + slots + "]";
		}
		
		
	}
	
	public static class InnerDeviceFoodBean {

		String foodId;
		int stock;
		String slotCode;
		String foodName;
		public String getFoodId() {
			return foodId;
		}
		public void setFoodId(String foodId) {
			this.foodId = foodId;
		}
		public int getStock() {
			return stock;
		}
		public void setStock(int stock) {
			this.stock = stock;
		}
		public String getSlotCode() {
			return slotCode;
		}
		public void setSlotCode(String slotCode) {
			this.slotCode = slotCode;
		}
		public String getFoodName() {
			return foodName;
		}
		public void setFoodName(String foodName) {
			this.foodName = foodName;
		}
		@Override
		public String toString() {
			return "InnerDeviceFoodBean [foodId=" + foodId + ", stock=" + stock
					+ ", slotCode=" + slotCode + ", foodName=" + foodName + "]";
		}
		
	}
}
