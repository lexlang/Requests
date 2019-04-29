package com.lexlang.Requests.interceptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebResponseData;
import com.gargoylesoftware.htmlunit.util.FalsifyingWebConnection;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
* @author lexlang
* @version 2019年4月29日 上午8:59:51
* 
*/
public class Intercept extends FalsifyingWebConnection{
	
	/**
	 *缓存相应 
	 */
	private final static LinkedHashMap<String, WebResponse> responseStore= new LinkedHashMap<String, WebResponse>() {
		private static final long serialVersionUID = 1L;
		protected boolean removeEldestEntry(Map.Entry<String, WebResponse> eldest) {
            return size() > 300;
        }
	};
	
	public Intercept(WebClient webClient) throws IllegalArgumentException {
		super(webClient);
	}
	
    @Override
    public WebResponse getResponse(WebRequest request) throws IOException {
    	String url=request.getUrl().toString();
    	
    	if(responseStore.containsKey(url)){
    		return responseStore.get(url);
    	}else{
            WebResponse response=super.getResponse(request);
            if(storeResponseOrNot(url)){
            	responseStore.put(url, response);
            }
            if(modifyResponseOrNot(url)){
            	return modifyResponse(response);
            }else{
            	return response;
            }
    	}
    }
    
    /**
     * 是否缓存这个Response
     * @param url
     * @return
     */
    public boolean storeResponseOrNot(String url){
    	return false;
    }
    
    /**
     * 是否修改这个Response
     * @param url
     * @return
     */
    public boolean modifyResponseOrNot(String url){
    	return false;
    }
    
    /**
     * 修改这个response
     * @param response
     * @return
     */
    public WebResponse modifyResponse(WebResponse response){
    	//String html=response.getContentAsString();
    	return response;
    }
    
    //做一个假页面
    public WebResponse makeResponse(WebResponse response,String content)  throws IOException{
        final List<NameValuePair> headers =new ArrayList<NameValuePair>();
        List<NameValuePair> realHeaders =response.getResponseHeaders();
        for(int i=0;i<realHeaders.size();i++){
            NameValuePair kv = realHeaders.get(i);
            if(kv.getName().equals("Content-Encoding")){continue;}
            headers.add(new NameValuePair(kv.getName(),kv.getValue()));
        }
        final byte[] body = content.getBytes("utf-8");
        final WebResponseData wrd = new WebResponseData(body,  200, "Ok", headers);
        return new WebResponse(wrd, response.getWebRequest().getUrl(), response.getWebRequest().getHttpMethod(), 0);
    }
	
}