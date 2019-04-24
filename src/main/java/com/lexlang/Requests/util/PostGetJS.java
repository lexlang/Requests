package com.lexlang.Requests.util;

import java.util.Map;
import java.util.Set;

/**
* @author lexlang
* @version 2019年4月24日 上午9:40:48
* 
*/
public class PostGetJS {
	
	/**
	 * 
	 * @param url 
	 * @param method
	 * @param headers
	 * @param postData
	 * @return
	 */
	public static String postGetJS(String url,String method,Map<String,String> headers,String postData){
		StringBuilder sb=new StringBuilder();
		sb.append("var xmlhttp=new XMLHttpRequest();\n");
		sb.append("xmlhttp.open(\""+method+"\",\""+url+"\",false);\n");
		if(headers!=null){
			Set<String> keys = headers.keySet();
			for(String key:keys){
				sb.append( "xmlhttp.setRequestHeader(\""+key+"\",\""+headers.get(key)+"\");\n");
			}
		}else{
			if(method.toLowerCase().equals("post")){
				sb.append( "xmlhttp.setRequestHeader(\""+"content-type"+"\",\""+"application/x-www-form-urlencoded"+"\");\n");
			}
		}
		sb.append("xmlhttp.send(\""+postData+"\");\n");
		sb.append("window.getContent=xmlhttp.responseText;");
		sb.append("var headers = request.getAllResponseHeaders();");
		sb.append("var arr = headers.trim().split(/[\r\n]+/);");
		sb.append("var hd =[];");
		sb.append("arr.forEach(function (line) {var headerMap = {}; var parts = line.split(': ');var header = parts.shift();var value = parts.join(': ');headerMap[header] = value;hd.push(headerMap);});");
		sb.append("window.getHeader=JSON.stringify(hd);");
		return sb.toString();
	}
}
