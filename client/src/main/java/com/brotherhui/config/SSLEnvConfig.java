package com.brotherhui.config;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class SSLEnvConfig {

    @Value("${ssltest.trust-store-password}")
    private String trustStorePassword;
    
    @Value("${ssltest.trust-store}")
    private Resource trustStore;
    
	@Value("${ssltest.key-store-password}")
	private String keyStorePassword;
	
	@Value("${ssltest.key-password}")
	private String keyPassword;
	
	@Value("${ssltest.key-store}")
	private Resource keyStore; 
	
	@PostConstruct
	private void init(){
		try {
			System.setProperty("javax.net.ssl.trustStore", trustStore.getURL().getFile());
			System.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword);
			System.setProperty("javax.net.ssl.keyStore", keyStore.getURL().getPath());
			System.setProperty("javax.net.ssl.keyStorePassword", keyStorePassword);
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
		new javax.net.ssl.HostnameVerifier() {

			public boolean verify(String hostname,
					javax.net.ssl.SSLSession sslSession) {
				if (hostname.equals("localhost")) {
					return true;
				}
				return false;
			}
		});
	}
	
//	final String KEYSTORE_PASSWORD = "s3cr3t";
	
//	static
//	{
//
//		System.setProperty("javax.net.ssl.trustStore", ClientApplication.class.getClassLoader().getResource("client.jks").getFile());
//		System.setProperty("javax.net.ssl.trustStorePassword", KEYSTORE_PASSWORD);
//		System.setProperty("javax.net.ssl.keyStore", ClientApplication.class.getClassLoader().getResource("client.jks").getFile());
//		System.setProperty("javax.net.ssl.keyStorePassword", KEYSTORE_PASSWORD);
//
//		javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
//				new javax.net.ssl.HostnameVerifier() {
//
//					public boolean verify(String hostname,
//							javax.net.ssl.SSLSession sslSession) {
//						if (hostname.equals("localhost")) {
//							return true;
//						}
//						return false;
//					}
//				});
//	}
}
