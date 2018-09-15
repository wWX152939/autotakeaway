package com.onekey.takeaway.bean;

public class LoginBean {
	String account;
	String password;
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "LoginBean [account=" + account + ", password=" + password + "]";
	}
	
}
