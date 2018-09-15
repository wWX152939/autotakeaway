package com.github.wzpay.demo;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.json.JSONException;
import org.json.JSONObject;

public class MyListener implements ServletContextListener {
	public final static String APPID = "wx119f0392d940d6fe";
	public final static String APPSECRET = "b8d29c377c872d07bf43e0cf860b5de0";
	public static String mTicket;
	public static String mToken;
	public final static String mTimeStamp = "1533732105";
	private MyThread myThread;

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		if (myThread != null && myThread.isInterrupted()) {

			myThread.interrupt();

		}
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		String str = null;
		System.out.println("____FUCK start");

		if (str == null && myThread == null) {

			myThread = new MyThread();

			myThread.start(); // servlet 上下文初始化时启动 socket

		}
	}

	class MyThread extends Thread {

		public void run() {

			while (!this.isInterrupted()) {// 线程未中断执行循环

				try {

					System.out.println("____FUCK start1");
					// 请求token
					String result = HttpRequest.sendGet("https://api.weixin.qq.com/cgi-bin/token", "grant_type=client_credential&appid=" + APPID + "&secret=" + APPSECRET);

	        	    JSONObject jsonObject;
					try {
						jsonObject = new JSONObject(result);
						System.out.println("____FUCK result=" + result);
//						int sleepTime = jsonObject.getInt("expires_in");
						mToken = jsonObject.getString("access_token");
//						if (sleepTime == 7200) {
//							
//						}
						// 请求ticket
						String result2 = HttpRequest.sendGet("https://api.weixin.qq.com/cgi-bin/ticket/getticket", "access_token=" + mToken + "&type=jsapi");

						System.out.println("____FUCK result2 =" + result2);
						jsonObject = new JSONObject(result2);

						int errCode = jsonObject.getInt("errcode");
						if (errCode == 0) {
							mTicket = jsonObject.getString("ticket");
						}
						Thread.sleep(60 * 60 * 1000); // 每隔1000ms执行一次
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						Thread.sleep(1000);
						e.printStackTrace();
					}
	        	    

				} catch (InterruptedException e) {

					e.printStackTrace();

				}

				// ------------------ 开始执行 ---------------------------

//				System.out.println("____FUCK TIME:"
//						+ System.currentTimeMillis());

			}

		}

	}

}