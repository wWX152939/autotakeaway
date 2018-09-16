package com.onekey.takeaway.bean;


public class RequestFoodBean {
	
	String shopID;

	public String getShopID() {
		return shopID;
	}

	public void setShopID(String shopID) {
		this.shopID = shopID;
	}

	@Override
	public String toString() {
		return "RequestFoodBean [shopID=" + shopID + "]";
	}
	

}
