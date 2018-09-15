package com.github.wzpay.demo;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.github.wxpay.sdk.IWXPayDomain;
import com.github.wxpay.sdk.WXPayConfig;

public class MyConfig extends WXPayConfig {

    private byte[] certData = new byte[1024];

    public MyConfig() {
//        String certPath = "/path/to/apiclient_cert.p12";
//        String certPath = "./key";
//        File file = new File(certPath);
//        InputStream certStream = new FileInputStream(file);
//        this.certData = new byte[(int) file.length()];
//        certStream.read(this.certData);
//        certStream.close();
    }

    public String getAppID() {
        return "wx119f0392d940d6fe";
    }

    public String getMchID() {
        return "1508330081";
    }

    public String getKey() {
        return "888888888888888888888888888888Ba";
    }

    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    public int getHttpReadTimeoutMs() {
        return 10000;
    }

	@Override
	public IWXPayDomain getWXPayDomain() {
		IWXPayDomain IWXPayDomain = new IWXPayDomain() {
			
			@Override
			public void report(String domain, long elapsedTimeMillis, Exception ex) {
				
			}
			
			@Override
			public DomainInfo getDomain(WXPayConfig config) {
				DomainInfo DomainInfo = new DomainInfo("api.mch.weixin.qq.com", true);
				return DomainInfo;
			}
		};
		return IWXPayDomain;
	}
}