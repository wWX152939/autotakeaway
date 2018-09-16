package com.onekey.takeaway.bean;

import java.util.List;

public class DeviceBean extends MsgBean{
	
	List<InnerDeviceBean> devList;

	public List<InnerDeviceBean> getDevList() {
		return devList;
	}
	
	public void addDev(InnerDeviceBean dev) {
		if (dev != null && this.devList != null) {
			this.devList.add(dev);
		}
	}


	public void setDevList(List<InnerDeviceBean> devList) {
		this.devList = devList;
	}


	@Override
	public String toString() {
		return "DeviceBean [devList=" + devList + "]";
	}


	public static class InnerDeviceBean {
		String deviceID;
		String name;
		/**
		 * 0 空闲 1 工作  2 完成  3 故障
		 */
		int status;
		public String getDeviceID() {
			return deviceID;
		}
		public void setDeviceID(String deviceID) {
			this.deviceID = deviceID;
		}
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
		public int getStatusInt() {
			return status;
		}
		
		public String getStatus() {
			String s = "";
			switch(status) {
			case 0:
				s = "空闲";
				break;
			case 1:
				s = "工作";
				break;
			case 2:
				s = "完成";
				break;
			case 3:
				s = "故障";
				break;
			}
			return s;
		}
		public void setStatus(int status) {
			this.status = status;
		}
		@Override
		public String toString() {
			return "InnerDeviceBean [deviceID=" + deviceID + ", name="
					+ name + ", status=" + status + "]";
		}
		
				
		
	}
}
