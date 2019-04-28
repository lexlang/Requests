package com.lexlang.Requests.header;

import java.util.HashMap;
import java.util.Map;

/**
* @author lexlang
* @version 2018年7月13日 下午3:12:18
* 
*/
public class Config{
	private String method;
	private Map<String,String> map=new HashMap<String,String>();
	public Config(String method) {
		this.method=method;
	}
	
	public Config setHeader(String name,String value){
		map.put(name, value);
		return this;
	}
	
	public Config setReferer(String referer){
		return setHeader("Referer",referer);
	}
	
	public Config setContentType(String contentType){
		return setHeader("Content-Type",contentType);
	}
	
	public Config setUserAgent(String userAgent){
		return setHeader("User-Agent",userAgent);
	}
	
	public Config setXRequestWith(){
		return setHeader("X-Requested-With","XMLHttpRequest");
	}
	
	public Config setAccept(String accept){
		return setHeader("Accept",accept);
	}
	
	public Config setAcceptEncoding(String acceptEncoding){
		return setHeader("Accept-Encoding",acceptEncoding);
	}
	
	public Config setHost(String host){
		return setHeader("Host",host);
	}
	
	public Config setCookie(String cookie){
		//System.out.println("提示信息------>>>>>>只有Jsoup和URL方法需要手工配置Cookie值，其他都自动管理，切记切记");
		return setHeader("Cookie",cookie);
	}
	
	public Map<String,String> build(){
		if(method.equals("POST")){
			if(! map.containsKey("Content-Type")){
				if(! map.containsKey("content-type")){
					throw new RuntimeException("POST请求必须指定内容编码类型");
				}
			}
		}
		return map;
	}
}
