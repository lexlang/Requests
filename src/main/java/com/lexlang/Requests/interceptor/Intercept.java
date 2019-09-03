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
import com.lexlang.Requests.proxy.ProxyPara;

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
    	
    	if(rejectResponseOrNot(url)){
    		return null;
    	}
    	
    	if(responseStore.containsKey(url)){
    		return responseStore.get(url);
    	}else{
            WebResponse response=super.getResponse(request);
            if(storeResponseOrNot(url)){
            	responseStore.put(url, response);
            }
            if(modifyResponseOrNot(url)){
            	return modifyResponse(response,url);
            }else{
            	return response;
            }
    	}
    }
    
    /**
     * 添加随机代理,减少代理开支
     * 重要请求访问,不重要的本地请求
     * @param request
     * @param para
     */
    public void addProxy(WebRequest request,ProxyPara para){
    	request.setProxyHost(para.getHost());
    	request.setProxyPort(para.getPort());
    }
    
    /**
     * 屏蔽指定链接
     * @param url
     * @return
     */
    public boolean rejectResponseOrNot(String url){
    	return false;
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
    public WebResponse modifyResponse(WebResponse response,String url) throws IOException{
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
