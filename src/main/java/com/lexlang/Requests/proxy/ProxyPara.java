package com.lexlang.Requests.proxy;
/**
* @author lexlang
* @version 2019年4月16日 下午2:10:47
* 
*/
public class ProxyPara {
	private String host;
	private int port;
	
	public ProxyPara(String host,int port){
		this.host=host;
		this.port=port;
	}
	
	public String getHost(){
		return host;
	}
	public int getPort(){
		return port;
	}
	
}
