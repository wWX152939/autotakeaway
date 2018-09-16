package com.onekey.takeaway.bean;

public class RequestShopOrderBean extends TokenBean {
	String shopID;
	String orderId;

	public String getShopID() {
		return shopID;
	}

	public void setShopID(String shopID) {
		this.shopID = shopID;
	}


	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@Override
	public String toString() {
		return "RequestShopOrderBean [shopID=" + shopID + ", orderId="
				+ orderId + "]";
	}

}
