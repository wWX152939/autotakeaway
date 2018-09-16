package com.onekey.takeaway.bean;

import java.util.List;

public class ModifyDeviceFoodBean extends TokenBean {
	
	List<InnerModifyDeviceFoodBean> slotList;	
	


	public List<InnerModifyDeviceFoodBean> getSlotList() {
		return slotList;
	}


	public void setSlotList(List<InnerModifyDeviceFoodBean> slotList) {
		this.slotList = slotList;
	}



	@Override
	public String toString() {
		return "ModifyDeviceFoodBean [slotList=" + slotList + "]";
	}



	public static class InnerModifyDeviceFoodBean {
		String slotID;
		int type;
		int total;
		public String getSlotID() {
			return slotID;
		}
		public void setSlotID(String slotID) {
			this.slotID = slotID;
		}
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type = type;
		}
		public int getTotal() {
			return total;
		}
		public void setTotal(int total) {
			this.total = total;
		}
		@Override
		public String toString() {
			return "InnerModifyDeviceFoodBean [slotID=" + slotID + ", type="
					+ type + ", total=" + total + "]";
		}
		
	}

}
