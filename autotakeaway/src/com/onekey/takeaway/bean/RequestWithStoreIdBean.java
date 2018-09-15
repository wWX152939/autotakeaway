package com.onekey.takeaway.bean;


public class RequestWithStoreIdBean {
	
	String storeId;
	String orderId;

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@Override
	public String toString() {
		return "RequestWithStoreIdBean [storeId=" + storeId + ", orderId="
				+ orderId + "]";
	}

	

}
