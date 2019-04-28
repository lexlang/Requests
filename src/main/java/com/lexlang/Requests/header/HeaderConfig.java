package com.lexlang.Requests.header;
/**
* @author lexlang
* @version 2018年7月13日 下午3:05:06
* 
*/
public class HeaderConfig {
	private final static String GET="GET";
	private final static String POST="POST";
	
	//POST 提交常见的编码方式 content-type
	public final static String POSTFORM="application/x-www-form-urlencoded";
	public final static String POSTFORMDATA="multipart/form-data";
	public final static String POSTJSON="application/json";
	public final static String POSTXML="text/xml";
	
	public static Config getBuilder(){
		return new Config(GET);
	}
	
	public static Config postBuilder(){
		return new Config(POST);
	}
	
}
