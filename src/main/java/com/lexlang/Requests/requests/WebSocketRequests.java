package com.lexlang.Requests.requests;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;

public class WebSocketRequests {
	
	private WebSocket ws;
	private final static int TIMEOUT=5000;
	private ConcurrentLinkedQueue<String> responseContents=new ConcurrentLinkedQueue<String>();//响应体
	
	public WebSocketRequests(String wss) throws NoSuchAlgorithmException, IOException, WebSocketException{
		this(wss,TIMEOUT);
	}
	
	public WebSocketRequests(String wss,int timeout) throws NoSuchAlgorithmException, IOException, WebSocketException{
		if(wss.startsWith("wss")){
			WebSocketFactory factory=getSSLsocket();
			ws = factory.createSocket(wss, timeout);
		}else{
			WebSocketFactory factory = new WebSocketFactory().setConnectionTimeout(timeout);
			ws = factory.createSocket(wss);
		}
		ws.addListener(new WebSocketAdapter() {
		    @Override
		    public void onTextMessage(WebSocket websocket, String message) throws Exception {
		    	System.out.println("相应:"+message);
		    	responseContents.add(message);
		    }
		});
		ws.connect();
	}
	
	public void sendText(String txt){
		ws.sendText(txt);
	}
	
	public void sendBinary(byte[] message){
		ws.sendBinary(message);
	}
	
	public String getResponse(){
		if(responseContents.size()>0){
			return responseContents.poll();
		}
		return null;
	}
	
	public WebSocketFactory getSSLsocket() throws NoSuchAlgorithmException {
		 SSLContext context = getInstance("TLS");
		 WebSocketFactory factory = new WebSocketFactory();
		 factory.setSSLContext(context);
		 factory.setVerifyHostname(false);
		 return factory;
	}
	
	public static SSLContext getInstance( String protocol) throws NoSuchAlgorithmException{
       SSLContext sslCtx = SSLContext.getInstance( protocol);
       init( sslCtx);
       return sslCtx;
    }
	 
	private static void init( SSLContext context){
       try
       {
           context.init( null, new TrustManager[] { new NaiveTrustManager() }, new java.security.SecureRandom());
           System.out.println( "------------- Initialisation du NaiveSSLContext ---------------------");
       }
       catch( java.security.KeyManagementException e)
       {
           throw new RuntimeException( "Failed to initialize an SSLContext.", e);
       }
   }
	 
	private static class NaiveTrustManager implements X509TrustManager
    {

        public X509Certificate[] getAcceptedIssuers()
        {
            System.out.println( "------------- NaiveTrustManager.getAcceptedIssuers() ---------------------");
            return null;
        }

        public void checkClientTrusted( X509Certificate[] certs, String authType)
        {
            System.out.println( "------------- NaiveTrustManager.checkClientTrusted( " + certs.toString() + ", " + authType
                    + ") ---------------------");
        }

        public void checkServerTrusted( X509Certificate[] certs, String authType)
        {
            System.out.println( "------------- NaiveTrustManager.checkServerTrusted( " + certs.toString() + ", " + authType
                    + ") ---------------------");
        }
	
	 }

}
