package com.lexlang.Requests.requests;

import java.io.IOException;
import java.io.PrintWriter;
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
		URLConnection connection = getConnection(url,headers);
        // 建立实际的连接
        connection.connect();
        // 获取所有响应头字段
        Map<String, List<String>> map = connection.getHeaderFields();
        return new Response(turnHsToList(map),connection.getInputStream(),decode,url);
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
		URLConnection conn = getConnection(url,headers);
		// 发送POST请求必须设置如下两行
		conn.setDoOutput(true);
		conn.setDoInput(true);
		// 获取URLConnection对象对应的输出流
		PrintWriter out = new PrintWriter(conn.getOutputStream());
        // 发送请求参数
		out.print(data);
        // flush输出流的缓冲
        out.flush();
        Map<String, List<String>> map = conn.getHeaderFields();
        return new Response(turnHsToList(map),conn.getInputStream(),decode,url);
	}
	
	private List<NameValuePair> turnHsToList(Map<String, List<String>> map){
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
