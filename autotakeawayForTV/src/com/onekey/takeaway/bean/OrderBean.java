package com.onekey.takeaway.bean;

import java.util.List;

public class OrderBean extends MsgBean{
	String totalPage;
	List<InnerOrderBean> orderList;
	

	public List<InnerOrderBean> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<InnerOrderBean> orderList) {
		this.orderList = orderList;
	}

	public String getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(String totalPage) {
		this.totalPage = totalPage;
	}

	@Override
	public String toString() {
		return "OrderBean [totalPage=" + totalPage + ", orderList=" + orderList
				+ "]";
	}
	
	public static class InnerOrderBean {
		String custom;
		int devType;
//		String xiaocai;
//		String tang;
		String doorId;
		int state;
		String foodName;
		String foodId;
		String pay;
		String devId;
		int orderId;
		String genTime;
		String userId;
		
		boolean backDoorClose;
		boolean containFood;
		
		
//		"devType":1,
//		"devId":null,
//		"foodName":"蛋炒饭",
//		"orderId":1478,
//		"custom":"汽车掉果冻里了",
//		"foodId":"983f1741-6386-40c6-9ea5-ca6d8a0bf610",
//		"pay":"100.0","state":2,
//		"genTime":"2018-09-0522:17:56",
//		"doorId":null

		public String getGenTime() {
			return genTime;
		}

		public boolean isBackDoorClose() {
			return backDoorClose;
		}

		public void setBackDoorClose(boolean backDoorClose) {
			this.backDoorClose = backDoorClose;
		}

		public boolean isContainFood() {
			return containFood;
		}

		public void setContainFood(boolean containFood) {
			this.containFood = containFood;
		}

		public void setGenTime(String genTime) {
			this.genTime = genTime;
		}

		public String getStatus() {
			String ret = "";
			switch (state) {
			case 1:
				ret = "未支付";
				break;
			case 2:
				ret = "已支付";
				break;
			case 3:
				ret = "制作中";
				break;
			case 4:
				ret = "制作完成";
				break;
			case 5:
				ret = "已放入箱柜，未取走";
				break;
			case 6:
				ret = "已取走，订单完成结束";
				break;
			case 7:
				ret = "订单取消";
				break;
				
			}
			return ret;
		}
		
		public InnerOrderBean() {
			
		}
				
		public InnerOrderBean(String custom, String food,
				String doorId, String devId, int state,  String genTime) {
			super();
			this.custom = custom;
			this.doorId = doorId;
			this.devId = devId;
			this.state = state;
			this.foodName = food;
			this.genTime = genTime;
		}

		public String getCustom() {
			return custom;
		}

		public void setCustom(String custom) {
			this.custom = custom;
		}


		public String getDoorId() {
			return doorId;
		}

		public void setDoorId(String doorId) {
			this.doorId = doorId;
		}
		
		public int getStateInt() {
			return state;
		}

		public String getState() {
			String ret = "";
			switch (state) {
			case 1:
				ret = "未支付";
				break;
			case 2:
				ret = "已支付";
				break;
			case 3:
				ret = "制作中";
				break;
			case 4:
				ret = "制作完成";
				break;
			case 5:
				ret = "您的餐已备好 请扫码取餐";
				break;
			case 6:
				ret = "已取走，订单完成结束";
				break;
			case 7:
				ret = "订单取消";
				break;
				
			}
			return ret;
		}

		public void setState(int state) {
			this.state = state;
		}


		public String getFoodName() {
			return foodName;
		}

		public void setFoodName(String foodName) {
			this.foodName = foodName;
		}

		public String getFoodId() {
			return foodId;
		}

		public void setFoodId(String foodId) {
			this.foodId = foodId;
		}

		public String getPay() {
			return pay;
		}

		public void setPay(String pay) {
			this.pay = pay;
		}

		public int getDevType() {
			return devType;
		}

		public void setDevType(int devType) {
			this.devType = devType;
		}

		public String getDevId() {
			return devId;
		}

		public void setDevId(String devId) {
			this.devId = devId;
		}

		public int getOrderId() {
			return orderId;
		}

		public void setOrderId(int orderId) {
			this.orderId = orderId;
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		@Override
		public String toString() {
			return "InnerOrderBean [custom=" + custom + ", devType=" + devType
					+ ", doorId=" + doorId + ", state=" + state + ", foodName="
					+ foodName + ", foodId=" + foodId + ", pay=" + pay
					+ ", devId=" + devId + ", orderId=" + orderId
					+ ", genTime=" + genTime + ", userId=" + userId
					+ ", backDoorClose=" + backDoorClose + ", containFood="
					+ containFood + "]";
		}

		
	}
}
