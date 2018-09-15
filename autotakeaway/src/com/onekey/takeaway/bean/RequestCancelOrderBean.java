package com.onekey.takeaway.bean;

public class RequestCancelOrderBean {
	String orderID;


	public String getOrderID() {
		return orderID;
	}


	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}


	@Override
	public String toString() {
		return "RequestDevBean [shopIO=" + orderID + "]";
	}

}
