package com.github.wxpay.sdk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.wzpay.demo.HttpRequest;


public class PayCallbackService extends HttpServlet {
    private static final long serialVersionUID = 3725026636532417987L;

    public void destroy() {
	super.destroy();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//    	PrintWriter writer = response.getWriter();
////		writer.println("GET " + request.getRequestURL() + " "
////				+ request.getQueryString());
// 
    	if (true) {
    		
    		System.out.println("doGet IP:[" + request.getRemoteHost());
    		Map<String, String[]> params = request.getParameterMap();
    		String queryString = "";
    		for (String key : params.keySet()) {
    			String[] values = params.get(key);
    			for (int i = 0; i < values.length; i++) {
    				String value = values[i];
    				queryString += key + "=" + value + "&";
    			}
    		}
    		
//		// 去掉最后一个空格
//		queryString = queryString.substring(0, queryString.length() - 1);
//		writer.println("GET " + request.getRequestURL() + " " + queryString);
//
//	    try {
//    	    // System.out.println("IP:["+request.getRemoteHost()+"] JSON:" +
//    	    // sb.toString());
////    	    JSONObject jsonObject = new JSONObject(sb.toString());
//    		System.out.println("IP:[" + request.getRemoteHost() + "] jsonObject:" + queryString);
//    	} catch (Exception e) {
//    	    e.printStackTrace();
//    	}
    		response.setContentType("text/html; charset=utf-8");
    		PrintWriter out = response.getWriter();
//	    JSONObject retJsonObject = new JSONObject();
//    	String outString = retJsonObject.toString();
    		System.out.println("IP:[" + request.getRemoteHost() + "queryString=" + queryString);
//    	out.write(outString);
//    	out.flush();
//    	out.close();
    		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
    		out.println("<HTML>");
    		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
    		out.println("<table border=\"1\">");
    		out.println("<tr>");
    		out.println("<th>目录ID</th>");
    		out.println("<th>目录code</th>");
    		out.println("<th>目录名</th>");
    		out.println("<th>文件名</th>");
    		out.println(" </tr>");
    		// out.println(documentsService.getTaskReport1("1"));
    		out.println("  </table></BODY>");
    		out.println("</HTML>");
    		out.flush();
    		out.close();
    	}
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	response.setHeader( "Content-type", "text/html;charset=UTF-8" );
        InputStream in = request.getInputStream();
        BufferedReader br = new BufferedReader( new InputStreamReader( in, "UTF-8" ) );
        StringBuffer result = new StringBuffer();
        String line = "";
        Map<String, String> map = null;
        while ( ( line = br.readLine() ) != null ) 
        {
          result.append(line);
        }
        try {
			map = WXPayUtil.xmlToMap( result.toString() );
			System.out.println("doPost wx response IP:[" + request.getRemoteHost() + "map=" + map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
//        map={transaction_id=4200000179201808230117235787, nonce_str=k4HVUf4BTYKh0yCMu971Nn1NAu9ihQAL, bank_type=CFT, openid=oQebE0xSdITCRhjpeFpdEoQpr19Q,
//        		sign=96AD642190E05449DE5B1CF2A823A1ED, fee_type=CNY, mch_id=1508330081, cash_fee=1, device_info=1000, out_trade_no=1535033306350, 
//        		appid=wx119f0392d940d6fe, total_fee=1, trade_type=JSAPI, result_code=SUCCESS, time_end=20180823220842, is_subscribe=Y, return_code=SUCCESS}

		writeMessageToResponse(response, "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>");
		
        //发送请求给自己服务器
	    if (map != null) {
	    	
	    	
//	    	1197#111$1198#112$1199#113
	    	String orders = map.get("device_info");
	    	String[] orderArray = orders.split("@");
	    	for (int i = 0; i < orderArray.length; i++) {
	    		String[] infos = orderArray[i].split("#");
	    		AddPayRequestBean AddPayRequestBean = new AddPayRequestBean();
//		    	AddPayRequestBean.actPay = Integer.parseInt(map.get("cash_fee")) / 100;
		    	AddPayRequestBean.actPay = (int) (Float.parseFloat(infos[1]) * 100);
		    	AddPayRequestBean.orderID = (infos[0]);
		    	AddPayRequestBean.userId = map.get("openid");
		    	String result2 = HttpRequest.jsonPost("http://140.143.161.135:8080/auto-sell-web/wechat/addPay.do", AddPayRequestBean);
		    	System.out.println("doPost addPay response=" + result2);
	    	}
	    }
 	   
    	
    	System.out.println("doPost IP:[" + request.getRemoteHost());
//		Map<String, String[]> params = request.getParameterMap();
//		String queryString = "";
//		for (String key : params.keySet()) {
//			String[] values = params.get(key);
//			for (int i = 0; i < values.length; i++) {
//				String value = values[i];
//				queryString += key + "=" + value + "&";
//			}
//		}
    }
    
    private class AddPayRequestBean {
    	String orderID;
    	int actPay;
    	String userId;
    }
    
    protected void writeMessageToResponse(HttpServletResponse response, String message) {
        PrintWriter writer = null;
        try {
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            writer = response.getWriter();
            writer.write(message);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null)
                writer.close();
        }
    }

}
