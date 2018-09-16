package com.onekey.takeaway.bean;

public class ShopBean extends MsgBean {
	String shopID;
	String title;
	String token;
	String info;
	public String getShopID() {
		return shopID;
	}
	public void setShopID(String shopID) {
		this.shopID = shopID;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	@Override
	public String toString() {
		return "ShopBean [shopID=" + shopID + ", title=" + title + ", token="
				+ token + ", info=" + info + "]";
	}
	
}
