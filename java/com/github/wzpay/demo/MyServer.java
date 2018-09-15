package com.github.wzpay.demo;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyServer extends HttpServlet {
	private MyThread myThread1;

	public void init() {

		String str = null;

		if (str == null && myThread1 == null) {

			myThread1 = new MyThread();

			myThread1.start(); // servlet 上下文初始化时启动 socket

		}

	}

	public void doGet(HttpServletRequest httpservletrequest,
			HttpServletResponse httpservletresponse)

	throws ServletException, IOException {

	}

	public void destory() {

		if (myThread1 != null && myThread1.isInterrupted()) {

			myThread1.interrupt();

		}

	}

	class MyThread extends Thread {

		public void run() {

			while (!this.isInterrupted()) {// 线程未中断执行循环

				try {

					Thread.sleep(2000); // 每隔2000ms执行一次

				} catch (InterruptedException e) {

					e.printStackTrace();

				}

				// ------------------ 开始执行 ---------------------------

				System.out.println("____FUCK TIME:"
						+ System.currentTimeMillis());

			}

		}

	}

}