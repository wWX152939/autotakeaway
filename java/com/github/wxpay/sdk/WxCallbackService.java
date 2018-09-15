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


public class WxCallbackService extends HttpServlet {
    private static final long serialVersionUID = 3725026636532417987L;

    public void destroy() {
	super.destroy();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	System.out.println("IP:[" + request + " WxCallbackService doGet====");
    	String msgSignature = request.getParameter("signature");
        String msgTimestamp = request.getParameter("timestamp");
        String msgNonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");
        boolean flag = false;
        try {
			if (WXPublicUtils.verifyUrl(msgSignature, msgTimestamp, msgNonce)) {
				flag = true;
			}
		} catch (AesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		PrintWriter out = response.getWriter();
		System.out.println("IP:[" + request.getRemoteHost() + "echostr=" + echostr + " flag=" + flag);
		System.out.println("IP:[" + request);
		System.out.println("IP:[" + request.getParameterMap());
		if (flag) {
			out.write(echostr);
		}
    	out.flush();
    	out.close();
    }
    
    public static class WXPublicUtils {

        /**
         * 验证Token
         * @param msgSignature 签名串，对应URL参数的signature
         * @param timeStamp 时间戳，对应URL参数的timestamp
         * @param nonce 随机串，对应URL参数的nonce
         *
         * @return 是否为安全签名
         * @throws AesException 执行失败，请查看该异常的错误码和具体的错误信息
         */
        public static boolean verifyUrl(String msgSignature, String timeStamp, String nonce)
                throws AesException {
            // 这里的 WXPublicConstants.TOKEN 填写你自己设置的Token就可以了
            String signature = SHA1.getSHA1("ab123c", timeStamp, nonce);
            if (!signature.equals(msgSignature)) {
                throw new AesException(AesException.ValidateSignatureError);
            }
            return true;
        }
    }
    
    public static class AesException extends Exception {

        public final static int OK = 0;
        public final static int ValidateSignatureError = -40001;
        public final static int ParseXmlError = -40002;
        public final static int ComputeSignatureError = -40003;
        public final static int IllegalAesKey = -40004;
        public final static int ValidateAppidError = -40005;
        public final static int EncryptAESError = -40006;
        public final static int DecryptAESError = -40007;
        public final static int IllegalBuffer = -40008;
        public final static int EncodeBase64Error = -40009;
        public final static int DecodeBase64Error = -40010;
        public final static int GenReturnXmlError = -40011;

        private int code;

        private static String getMessage(int code) {
            switch (code) {
            case ValidateSignatureError:
                return "签名验证错误";
            case ParseXmlError:
                return "xml解析失败";
            case ComputeSignatureError:
                return "sha加密生成签名失败";
            case IllegalAesKey:
                return "SymmetricKey非法";
            case ValidateAppidError:
                return "appid校验失败";
            case EncryptAESError:
                return "aes加密失败";
            case DecryptAESError:
                return "aes解密失败";
            case IllegalBuffer:
                return "解密后得到的buffer非法";
            case EncodeBase64Error:
                return "base64加密错误";
            case DecodeBase64Error:
                return "base64解密错误";
            case GenReturnXmlError:
                return "xml生成失败";
            default:
                return null;
            }
        }

        public int getCode() {
            return code;
        }

        public AesException(int code) {
            super(getMessage(code));
            this.code = code;
        }
    }
    
    public static class SHA1 {

        /**
         * 用SHA1算法验证Token
         *
         * @param token     票据
         * @param timestamp 时间戳
         * @param nonce     随机字符串
         * @return 安全签名
         * @throws AesException
         */
        public static String getSHA1(String token, String timestamp, String nonce) throws AesException {
            try {
                String[] array = new String[]{token, timestamp, nonce};
                StringBuffer sb = new StringBuffer();
                // 字符串排序
                Arrays.sort(array);
                for (int i = 0; i < 3; i++) {
                    sb.append(array[i]);
                }
                String str = sb.toString();
                // SHA1签名生成
                MessageDigest md = MessageDigest.getInstance("SHA-1");
                md.update(str.getBytes());
                byte[] digest = md.digest();

                StringBuffer hexstr = new StringBuffer();
                String shaHex = "";
                for (int i = 0; i < digest.length; i++) {
                    shaHex = Integer.toHexString(digest[i] & 0xFF);
                    if (shaHex.length() < 2) {
                        hexstr.append(0);
                    }
                    hexstr.append(shaHex);
                }
                return hexstr.toString();
            } catch (Exception e) {
                e.printStackTrace();
                throw new AesException(AesException.ComputeSignatureError);
            }
        }
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
