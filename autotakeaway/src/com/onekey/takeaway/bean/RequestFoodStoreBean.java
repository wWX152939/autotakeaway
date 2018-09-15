package com.onekey.takeaway.bean;


public class RequestFoodStoreBean {

	String storeId;
	String foodId;

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getFoodId() {
		return foodId;
	}

	public void setFoodId(String foodId) {
		this.foodId = foodId;
	}

	@Override
	public String toString() {
		return "RequestFoodStoreBean [storeId=" + storeId + ", foodId="
				+ foodId + "]";
	}
}
