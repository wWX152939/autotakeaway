package com.onekey.takeaway.bean;

public class ModifyFoodBean extends TokenBean {
	String storeId;
	String foodId;
	int type;
	int total;
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
		return "ModifyFoodBean [storeId=" + storeId + ", foodId=" + foodId
				+ ", type=" + type + ", total=" + total + "]";
	}


}
