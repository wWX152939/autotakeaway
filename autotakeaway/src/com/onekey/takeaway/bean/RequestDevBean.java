package com.onekey.takeaway.bean;

public class RequestDevBean extends TokenBean {
	String shopID;

	public String getShopID() {
		return shopID;
	}

	public void setShopID(String shopID) {
		this.shopID = shopID;
	}

	@Override
	public String toString() {
		return "RequestDevBean [shopIO=" + shopID + "]";
	}

}
