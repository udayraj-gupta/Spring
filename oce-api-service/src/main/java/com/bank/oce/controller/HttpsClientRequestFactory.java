package com.bank.oce.controller;


import java.net.HttpURLConnection;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.http.client.SimpleClientHttpRequestFactory;

public class HttpsClientRequestFactory extends SimpleClientHttpRequestFactory {
	 
    @Override
    protected void prepareConnection(HttpURLConnection connection, String httpMethod) {
        try {
            if (!(connection instanceof HttpsURLConnection)) {
                throw new RuntimeException("An instance of HttpsURLConnection is expected");
            }
                         if (connection instanceof HttpsURLConnection) {// modified protocol version
                SSLContext ctx = SSLContext.getInstance("TLSv1.2");
                X509TrustManager tm = new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain,
                                                   String authType) {
                    }
                    @Override
                    public void checkServerTrusted(X509Certificate[] chain,
                                                   String authType) {
                    }
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                };
                ctx.init(null, new TrustManager[]{tm}, null);
//                @SuppressWarnings("deprecation")
//				org.apache.http.conn.ssl.SSLSocketFactory ssf = new org.apache.http.conn.ssl.SSLSocketFactory(ctx, org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                ((HttpsURLConnection) connection).setSSLSocketFactory(ctx.getSocketFactory());
            }
 
            HttpsURLConnection httpsConnection = (HttpsURLConnection) connection;
 
            super.prepareConnection(httpsConnection, httpMethod);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
