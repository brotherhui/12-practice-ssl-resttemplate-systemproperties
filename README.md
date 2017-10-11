Notice:
There are 2 ways for a client to access B.
1. set b's public certificate into JDK
2. program the ssl certificate (add client's keystore and trusted store(which include B's public key) into SSLContext)

This example shows how a normal client calls ssl server's API via set the certification into system.properties. (1st way)

In this way, there is no need to set any ssl inforamtion into RestTemplate codings

### Key concepts
Key-store: include public and private key
Trust-store: include the public keys which are trusted

### Overview
A | A's public key | A's private key | A's key-store (include A's public and private key) | A's trust-store(include B's public key)
B | B's public key | B's private key | B's key-store (include B's public and private key) | B's trust-store(include A's public key)


#### Server

server:
  port: 8443
  ssl:
    key-store: classpath:server.jks
    key-store-password: s3cr3t
    key-password: s3cr3t
    trust-store: classpath:server.jks
    trust-store-password: s3cr3t
    client-auth: need
    
#### Client
package com.brotherhui.config;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
<code>
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
</code>


#### try
In this way, anyone can access http://localhost:8080/door?uri=https://localhost:8443/user




### Keys generate
1. We can genreate the jks with a single cmd:
keytool -genkeypair -alias serverkey -keyalg RSA -dname "CN=Server,OU=Accenture,O=Development,L=Australia,S=sydney,C=AU" -keypass s3cr3t -keystore c:\Hunter\server.jks -storepass s3cr3t 
keytool -genkeypair -alias clientkey -keyalg RSA -dname "CN=Client,OU=Accenture,O=Development,L=Australia,S=sydney,C=AU" -keypass s3cr3t -keystore c:\Hunter\client.jks -storepass s3cr3t

2 Export Client and server certificates
keytool -exportcert -alias clientkey -file c:\Hunter\client-public.cer -keystore c:\Hunter\client.jks -storepass s3cr3t 
keytool -exportcert -alias serverkey -file c:\Hunter\server-public.cer -keystore c:\Hunter\server.jks -storepass s3cr3t 

3 Import client certificate onto server keystore (and vice versa)
keytool -importcert -keystore c:\Hunter\server.jks -alias clientcert -file c:\Hunter\client-public.cer -storepass s3cr3t -noprompt
keytool -importcert -keystore c:\Hunter\client.jks -alias servercert -file c:\Hunter\server-public.cer -storepass s3cr3t -noprompt



