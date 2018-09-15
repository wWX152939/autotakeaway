package com.onekey.takeaway.bean;


public class MsgBean {
	int errorcode;
	String errorInfo;

	public int getErrorcode() {
		return errorcode;
	}

	public void setErrorcode(int errorcode) {
		this.errorcode = errorcode;
	}

	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}

	@Override
	public String toString() {
		return "MsgBean [errorcode=" + errorcode + ", errorInfo=" + errorInfo
				+ "]";
	}
	
}
