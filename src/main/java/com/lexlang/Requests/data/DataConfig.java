package com.lexlang.Requests.data;

import java.util.HashMap;
import java.util.Map;

/**
* @author lexlang
* @version 2018年7月17日 上午9:51:55
* 
*/
public class DataConfig {
	private Map<String,String> map=new HashMap<String,String>();
	
	public DataConfig setData(String name,String value){
		map.put(name, value);
		return this;
	}
	
	public Map<String,String> build(){
		return map;
	}
}
