package com.onekey.common;


public interface DataCallback {
	void callback(byte[] data);
	void onConnected();
    void onDisconnected();
}
