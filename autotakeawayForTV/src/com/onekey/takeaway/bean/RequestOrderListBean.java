package com.onekey.takeaway.bean;

public class RequestOrderListBean extends TokenBean {
	String shopID;
	String orderId;
	int pageIndex;
	String orderState;
	int devType;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getShopID() {
		return shopID;
	}

	public void setShopID(String shopID) {
		this.shopID = shopID;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public String getOrderState() {
		return orderState;
	}

	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}

	public int getDevType() {
		return devType;
	}

	public void setDevType(int devType) {
		this.devType = devType;
	}

	@Override
	public String toString() {
		return "RequestOrderListBean [shopID=" + shopID + ", orderId="
				+ orderId + ", pageIndex=" + pageIndex + ", orderState="
				+ orderState + ", devType=" + devType + "]";
	}

}
