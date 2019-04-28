package com.lexlang.Requests.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
* @author lexlang
* @version 2019年4月28日 下午2:34:40
* 
*/
public class HsToList {
	
	public static List<NameValuePair> turnHsToList(Map<String, List<String>> map){
		List<NameValuePair> headers=new ArrayList<NameValuePair>();
		Set<String> keys = map.keySet();
		for(String key:keys){
			List<String> list = map.get(key);
			for(int index=0;index<list.size();index++){
				headers.add(new NameValuePair(key,list.get(index)));
			}
		}
		return headers;
	};
	
}
