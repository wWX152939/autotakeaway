package com.github.wxpay.sdk;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.github.wzpay.demo.HttpRequest;
import com.github.wzpay.demo.MyConfig;
import com.github.wzpay.demo.MyListener;


public class Service extends HttpServlet {
    private static final long serialVersionUID = 3725026636532417987L;

    public void destroy() {
	super.destroy();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	response.setContentType("text/html; charset=utf-8");
	System.out.println("____FUCK doGet start");
	PrintWriter out = response.getWriter();
	// DocumentsService documentsService = new DocumentsService();
	// ProjectService projectService = new ProjectService();
	// try {
	// projectService.importWBS("工程建筑",57);
	// projectService.importDocument("0", 57);
	// } catch (SQLException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	String mppFilePath = "F:\\qq.mpp"; 
	File file = new File(mppFilePath);
	List lst = new ArrayList<>();
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
	out.print(lst.toString());
	out.flush();
	out.close();
    }

	/**
     * 生成32位md5码
     * @param password
     * @return
     */
    public String md5Password(String password) {

        try {
            // 得到一个信息摘要器
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(password.getBytes());
            StringBuffer buffer = new StringBuffer();
            // 把每一个byte 做一个与运算 0xff;
            for (byte b : result) {
                // 与运算
                int number = b & 0xff;// 加盐
                String str = Integer.toHexString(number);
                if (str.length() == 1) {
                    buffer.append("0");
                }
                buffer.append(str);
            }

            // 标准的md5加密后的结果
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }

    }
    
    private String getSign(String nonceStr) {
    	String ticket = MyListener.mTicket;
    	String str = "jsapi_ticket=" + ticket +"&noncestr=" + nonceStr + "&timestamp=" + MyListener.mTimeStamp + "&url=http://www.dewucanyin.com";
    	return DigestUtils.shaHex(str);
    }
    
    private String getSignForScan(String nonceStr, String code) {
    	String ticket = MyListener.mTicket;
    	String url = "http://www.dewucanyin.com/?action=2&code=" + code + "&state=STATE";
    	String str = "jsapi_ticket=" + ticket +"&noncestr=" + nonceStr + "&timestamp=" + MyListener.mTimeStamp + "&url=" + url;
    	return DigestUtils.shaHex(str);
    }
    
    private String getPaySign(String nonceStr, String prepay_id) {
    	String stringA="appId=wx119f0392d940d6fe&nonceStr=" + nonceStr + "&package=prepay_id=" + prepay_id + "&signType=MD5&timeStamp=" + MyListener.mTimeStamp;
    	String stringSignTemp=stringA+"&key=888888888888888888888888888888Ba"; //注：key为商户平台设置的密钥key
    	return md5Password(stringSignTemp).toUpperCase();
    }
    
    private Map<String, String> preparePay(String openId, String device_info, int totalPay) {

		try {
	    	MyConfig config = new MyConfig();
			WXPay wxpay = new WXPay(config);
			
			Map<String, String> data = new HashMap<String, String>();
			data.put("body", "得物机电餐饮");
			long timestamp = System.currentTimeMillis();
			System.out.println(" prepare timestamp=" + timestamp);
			data.put("out_trade_no", timestamp + "");
			data.put("device_info", device_info);
			data.put("fee_type", "CNY");
			data.put("total_fee", "1");
			data.put("spbill_create_ip", "123.12.12.123");
			data.put("notify_url", "http://www.dewucanyin.com:8080/weixin/PayService");
			data.put("sign_type", "MD5");
//			data.put("openid", "oQebE0xSdITCRhjpeFpdEoQpr19Q"); // mate8
//			data.put("openid", "oQebE0zSYs4eZmKSLTTXx5MaL8Ow"); // apple
			data.put("openid", openId); // apple
//	    data.put("nonce_str", "y4n8fmSFipsfxOFj");
			data.put("trade_type", "JSAPI");  // 此处指定为扫码支付
//	    data.put("product_id", "12");
			Map<String, String> resp = wxpay.unifiedOrder(data);
			System.out.println(resp);
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
    }
    
    public static class PayList {
    	public String code;
    	public String shopId;

    	List<InnerPayList> payList;
    	
    	class InnerPayList {
    		public String foodID;
    		public String pay;
			@Override
			public String toString() {
				return "InnerPayList [foodID=" + foodID + ", pay=" + pay + "]";
			}
    	}

		@Override
		public String toString() {
			return "PayList [code=" + code + ", shopId=" + shopId
					+ ", payList=" + payList + "]";
		}
    	
    }

    /* 
     * 
     * 
     * (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	int function = 0;
    	String arg1 = null;

    	BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), "utf-8"));
    	StringBuffer sb = new StringBuffer("");
    	String temp;
    	while ((temp = br.readLine()) != null) {
    	    sb.append(temp);
    	}
    	br.close();
	    JSONObject retJsonObject = new JSONObject();
    	try {
    	    // System.out.println("IP:["+request.getRemoteHost()+"] JSON:" +
    	    // sb.toString());
    	    JSONObject jsonObject = new JSONObject(sb.toString());
    	    function = jsonObject.getInt("function");
    		System.out.println("------------------下单请求IP:[" + request.getRemoteHost() + "] 请求方法:" + function);
    		if (function == 1) {
    			//下单
        	    
        	    PayList payList = (PayList) DataUtils.getObject(PayList.class, sb.toString());
        	    arg1 = payList.code;
        	    String arg2 = payList.shopId;
        	    
        		System.out.println("IP:[" + request.getRemoteHost() + "] 请求方法:" + function + "|请求payList:" + payList);
    			// 1.获取openid
    			String code = arg1;
    			
    			String result = HttpRequest.sendGet("https://api.weixin.qq.com/sns/oauth2/access_token", "appid=wx119f0392d940d6fe&secret=b8d29c377c872d07bf43e0cf860b5de0&code=" + code + "&grant_type=authorization_code");
    			System.out.println("openid result=" + result);
        	    JSONObject jsonObject2 = new JSONObject(result);
        	    String openId = jsonObject2.getString("openid");

//        	    String openId = "1111";
//        	    String nickName = "2222";
        	    
        	    result = HttpRequest.sendGet("https://api.weixin.qq.com/cgi-bin/user/info?access_token=" + MyListener.mToken + "&openid=" + openId + "&lang=zh_CN");
        	    System.out.println("user info result=" + result);
        	    jsonObject2 = new JSONObject(result);
        	    String nickName = jsonObject2.getString("nickname");

        		
        	    String deviceInfo = "";
    			for (int i = 0; i < payList.payList.size(); i++) {
    				
    				// 自己服务器先下单
    				Map<String, String> params = new HashMap<String, String>();
    				params.put("shopID", arg2);
    				params.put("foodID", payList.payList.get(i).foodID);
    				params.put("custom", nickName);
    				params.put("userId", openId);
    				result = HttpRequest.jsonPost("http://140.143.161.135:8080/auto-sell-web/wechat/addOrder.do", params);
    				System.out.println("addOrder result=" + result);
    				jsonObject = new JSONObject(result);
    				int orderId = jsonObject.getInt("orderId");
    				deviceInfo += orderId + "#" + payList.payList.get(i).pay + "@";
    			}
    			if (deviceInfo.isEmpty()) {
    				return;
    			}
    			deviceInfo = deviceInfo.substring(0, deviceInfo.length() - 1);
				System.out.println("addOrder deviceInfo=" + deviceInfo);
//        	    String openId = "111111";
    	    	// 2.下单
        	    Map<String, String> retPay = preparePay(openId, deviceInfo, 1);
        	    if (retPay != null) {
        	    	String sign = getSign(retPay.get("nonce_str"));
        	    	String paySign = getPaySign(retPay.get("nonce_str"), retPay.get("prepay_id"));
        	    	retJsonObject.put("signature", sign);
        	    	retJsonObject.put("paySign", paySign);
        	    	retJsonObject.put("package", "prepay_id=" + retPay.get("prepay_id"));
        	    	retJsonObject.put("nonce_str", retPay.get("nonce_str"));
        	    	retJsonObject.put("appId", MyListener.APPID);
        	    	retJsonObject.put("timeStamp", MyListener.mTimeStamp);
        	    }
    		} else if (function == 2) {
        	    String code = jsonObject.getString("arg1");
    			String result = HttpRequest.sendGet("https://api.weixin.qq.com/sns/oauth2/access_token", "appid=wx119f0392d940d6fe&secret=b8d29c377c872d07bf43e0cf860b5de0&code=" + code + "&grant_type=authorization_code");
    			System.out.println("openid result=" + result);
        	    JSONObject jsonObject2 = new JSONObject(result);
        	    String openId = jsonObject2.getString("openid");
    			//扫码
    			String nonce = "1122334455667788";
    			String sign = getSignForScan(nonce, code);
    	    	retJsonObject.put("signature", sign);
    	    	retJsonObject.put("nonce_str", nonce);
    	    	retJsonObject.put("appId", MyListener.APPID);
    	    	retJsonObject.put("timeStamp", MyListener.mTimeStamp);
    	    	retJsonObject.put("ticket", MyListener.mTicket);
    	    	retJsonObject.put("openId", openId);
    		}
    	} catch (JSONException e1) {
    	    e1.printStackTrace();
    	} catch (Exception e) {
    	    e.printStackTrace();
    	}
    	response.setContentType("text/html; charset=utf-8");
    	PrintWriter out = response.getWriter();
    	String outString = retJsonObject.toString();
		System.out.println("IP:[" + request.getRemoteHost() + "outString=" + outString);
    	out.write(outString);
    	out.flush();
    	out.close();
    }

}
