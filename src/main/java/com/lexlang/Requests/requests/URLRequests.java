package com.lexlang.Requests.requests;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.lexlang.Requests.proxy.ProxyPara;
import com.lexlang.Requests.responses.Response;
import com.lexlang.Requests.util.HsToList;


/**
* @author lexlang
* @version 2019年3月12日 上午11:53:46
* 
*/
public class URLRequests extends Request{
	private static final int TIME_OUT=60000;//默认超时时间
	
	private int timeout;
	private ProxyPara proxy;
	
	public URLRequests(){
		this.timeout=TIME_OUT;
		this.proxy=null;
	}
	
	/**
	 * 设置超时时间
	 * @param timeout
	 */
	public URLRequests(int timeout){
		this.timeout=timeout;
		this.proxy=null;
	}
	
	/**
	 * 设置代理
	 * @param proxy
	 */
	public URLRequests(ProxyPara proxy){
		this.timeout=TIME_OUT;
		this.proxy=proxy;
	}
	
	/**
	 * 
	 * @param timeout 设置超时
	 * @param proxy 设置代理
	 */
	public URLRequests(int timeout,ProxyPara proxy){
		this.timeout=timeout;
		this.proxy=proxy;
	} 
	
	/**
	 * 
	 * @param proxy 设置搭理
	 * @param timeout 设置超时
	 */
	public URLRequests(ProxyPara proxy,int timeout){
		this.timeout=timeout;
		this.proxy=proxy;
	} 
	
	
	@Override
	public Response get(String url) throws IOException {
		return getUseHeaderAndDecode(url, null, "utf-8");
	}

	@Override
	public Response getUseHeader(String url, Map<String, String> headers) throws IOException {
		return getUseHeaderAndDecode(url, headers, "utf-8");
	}

	@Override
	public Response getUseHeaderAndDecode(String url, Map<String, String> headers, String decode) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) getConnection(url,headers);
        // 建立实际的连接
        connection.connect();
        // 获取所有响应头字段
        Map<String, List<String>> map = connection.getHeaderFields();
        return new Response(HsToList.turnHsToList(map),connection.getInputStream(),decode,url,connection.getResponseCode());
	}

	@Override
	public Response post(String url, String data) throws IOException {
		return postUseHeaderAndDecode(url, data, null, "utf-8");
	}

	@Override
	public Response postUseHeader(String url, String data, Map<String, String> headers) throws IOException {
		return postUseHeaderAndDecode(url, data, headers, "utf-8");
	}

	@Override
	public Response postUseHeaderAndDecode(String url, String data, Map<String, String> headers, String decode) throws IOException {
		HttpURLConnection conn = (HttpURLConnection) getConnection(url,headers);
		conn.setRequestMethod("POST"); 
		
		if(headers==null){
			conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
		}
		
		// 发送POST请求必须设置如下两行
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setUseCaches(false);

        DataOutputStream dos=new DataOutputStream(conn.getOutputStream());
        dos.writeBytes(data);
        dos.flush();
        dos.close();
        
        Map<String, List<String>> map = conn.getHeaderFields();
        return new Response(HsToList.turnHsToList(map),conn.getInputStream(),decode,url,conn.getResponseCode());
	}
	

	
	private URLConnection getConnection(String url,Map<String,String> headers) throws IOException{
        URL realUrl = new URL(url);
        // 打开和URL之间的连接
        URLConnection connection =null; 
        //设置
        if(proxy!=null){
            // 创建代理服务器  
            InetSocketAddress addr = new InetSocketAddress(proxy.getHost(),proxy.getPort());  
            Proxy proxy = new Proxy(Proxy.Type.HTTP, addr); // http 代理  
            connection =realUrl.openConnection(proxy); 
        }else{
        	connection =realUrl.openConnection();
        }
        //设置超时时间
        connection.setConnectTimeout(timeout);
        connection.setReadTimeout(timeout);
        
        if(headers!=null){
        	Set<String> keys = headers.keySet();
        	for(String key:keys){
        		connection.setRequestProperty(key, headers.get(key));
        	}
        }
   
		return connection;
	}
	
}
