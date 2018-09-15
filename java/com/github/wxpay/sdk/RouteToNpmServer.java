package com.github.wxpay.sdk;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.wzpay.demo.HttpRequest;


public class RouteToNpmServer extends HttpServlet {
    private static final long serialVersionUID = 3725026636532417987L;

    public void destroy() {
	super.destroy();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	System.out.println("IP:[" + request + " WxCallbackService doGet====");
        String echostr = "http://localhost:10086";

//    	HttpRequest.sendGet(echostr, null);
        request.getRequestDispatcher(echostr).forward(request, response);
		PrintWriter out = response.getWriter();
		System.out.println("IP:[" + request.getRemoteHost());
		System.out.println("IP:[" + request.getParameterMap());
		out.write(echostr);
    	out.flush();
    	out.close();
    }
    


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	System.out.println("doPost IP:[" + request.getRemoteHost());
		Map<String, String[]> params = request.getParameterMap();
		String queryString = "";
		for (String key : params.keySet()) {
			String[] values = params.get(key);
			for (int i = 0; i < values.length; i++) {
				String value = values[i];
				queryString += key + "=" + value + "&";
			}
		}
		System.out.println("doPost IP:[" + request.getRemoteHost() + "queryString=" + queryString);
    }

}
