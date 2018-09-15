package com.github.wzpay.demo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;

import com.github.wxpay.sdk.WXPay;

public class WXPayExample {

	/**
     * 生成32位md5码
     * @param password
     * @return
     */
    public static String md5Password(String password) {

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
    
    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
    
    
    private static class AddPayRequestBean {
    	String orderID;
    	int actPay;
    	String custom;
    	String userId;
    }
    
    public static void main(String[] args) throws Exception {
    	String orders = "1210#10.0@1211#28.0";
    	String[] orderArray = orders.split("@");
    	for (int i = 0; i < orderArray.length; i++) {
	    	System.out.println("doPost addPay orderArray[i]=" + orderArray[i]);
    		String[] infos = orderArray[i].split("#");
	    	System.out.println("doPost addPay infos[i]=" + infos[i]);
    		AddPayRequestBean AddPayRequestBean = new AddPayRequestBean();
	    	AddPayRequestBean.actPay = (int) (Float.parseFloat(infos[1]) * 100);
	    	AddPayRequestBean.orderID = infos[0];
	    	AddPayRequestBean.userId = "111111";
	    	String result2 = HttpRequest.jsonPost("http://140.143.161.135:8080/auto-sell-web/wechat/addPay.do", AddPayRequestBean);
	    	System.out.println("doPost addPay response=" + result2);
    	}
    	
//    	Map<String, String> params = new HashMap<String, String>();
//	    params.put("shopID", "6fdf23bd-9172-4775-b5ac-8b5efd6c677f");
//	    params.put("foodID", "983f1741-6386-40c6-9ea5-ca6d8a0bf610");
//	    params.put("custom", "15851545454");
//	    String result = HttpRequest.jsonPost("http://140.143.161.135:8080/auto-sell-web/wechat/addOrder.do", params);
//	    
//	    System.out.println("result=" + result);

//        Map<String, String> map = new HashMap<>();
//        map.put("cash_fee", "1200");
//        map.put("device_info", "1179");
//        map.put("openid", "15851545454");
//	    AddPayRequestBean AddPayRequestBean = new AddPayRequestBean();
//    	AddPayRequestBean.actPay = Integer.parseInt(map.get("cash_fee")) / 100;
//    	AddPayRequestBean.orderID = map.get("device_info");
//    	AddPayRequestBean.custom = map.get("openid");
//    	String result2 = HttpRequest.jsonPost("http://140.143.161.135:8080/auto-sell-web/wechat/addPay.do", AddPayRequestBean);
//    	System.out.println("doPost addPay response=" + result2);
    	
//    	CertHttpUtil.postData("", "", "1508330081", "./key");
    	String nonceStr = "8tINHD4zzP8XUtwM";
//    	String timestamp = create_timestamp();
    	String ticket = "HoagFKDcsGMVCIY2vOjf9kR2yUaHDkCH1KNZkDsRzO_FZZ8fw4JyhcePa9gInS1ekeHTJu6jmGRUyqkTvEp19g";
    	String prepay_id="wx121116058474995325ce78141027670213";
    	String str = "jsapi_ticket=" + ticket +"&noncestr=" + nonceStr + "&timestamp=1533732105&url=http://www.dewucanyin.com";
    	String sign = DigestUtils.shaHex(str);
    	System.out.println("sign=" + sign);
//    	9cf9aec8db163fdbd61c5d5fa174648b
//    	11f571f1b370d3146dd54a984cad6d9c301cb1ed
//    	{result_code=SUCCESS, sign=C97ECCF1B6952F74DF29FF1F0EF9BB03E7836B590852165B2F0E8B319F4B9ADD, mch_id=1508330081, 
//    	prepay_id=wx08204236560082dc814fd3b42546646558, return_msg=OK, appid=wx119f0392d940d6fe, nonce_str=t17o2kgkt2DhzOzt, return_code=SUCCESS, trade_type=JSAPI}
    	String stringA="appId=wx119f0392d940d6fe&nonceStr=" + nonceStr + "&package=prepay_id=" + prepay_id + "&signType=MD5&timeStamp=1533732105";
    	String stringSignTemp=stringA+"&key=888888888888888888888888888888Ba"; //注：key为商户平台设置的密钥key
    	System.out.println("stringSignTemp=" + stringSignTemp +"\nsignPay=" + md5Password(stringSignTemp).toUpperCase());
//		Map<String, Object> params = ImmutableMap.<String, Object> builder()
//				.put("appId", appId)
//				.put("timeStamp", timeStamp) 
//				.put("nonceStr", nonceStr)
//				.put("package", packageStr)
//				.put("signType", signType)
//				.build();
    	if (false) {
    		
    		MyConfig config = new MyConfig();
    		WXPay wxpay = new WXPay(config);
    		
    		Map<String, String> data = new HashMap<String, String>();
    		data.put("body", "腾讯充值中心-QQ会员充值");
    		data.put("out_trade_no", "2016090910595900000015");
    		data.put("device_info", "1000");
    		data.put("fee_type", "CNY");
    		data.put("total_fee", "1");
    		data.put("spbill_create_ip", "123.12.12.123");
    		data.put("notify_url", "http://www.dewucanyin.com");
    		data.put("sign_type", "MD5");
    		data.put("openid", "oQebE0xSdITCRhjpeFpdEoQpr19Q"); // mate8
//    		data.put("openid", "oQebE0zSYs4eZmKSLTTXx5MaL8Ow"); // apple
//        data.put("nonce_str", "y4n8fmSFipsfxOFj");
    		data.put("trade_type", "JSAPI");  // 此处指定为扫码支付
//        data.put("product_id", "12");
    		
    		try {
    			Map<String, String> resp = wxpay.unifiedOrder(data);
    			System.out.println(resp);
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    	}
    }

}
