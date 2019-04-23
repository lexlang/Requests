package com.lexlang.Requests.requests;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.Header;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.lexlang.Requests.proxy.ProxyPara;
import com.lexlang.Requests.responses.Response;

/**
* @author lexlang
* @version 2019年4月16日 下午2:47:38
* 
*/
public class JsoupRequests extends Request {
	private static final int TIME_OUT=60000;//默认超时时间
	
	private int timeout;
	private ProxyPara proxy;
	
	public JsoupRequests(){
		this.timeout=TIME_OUT;
		this.proxy=null;
	}
	
	public JsoupRequests(int timeout){
		this.timeout=timeout;
		this.proxy=null;
	}
	
	public JsoupRequests(ProxyPara proxy){
		this.timeout=TIME_OUT;
		this.proxy=proxy;
	}
	
	public JsoupRequests(int timeout,ProxyPara proxy){
		this.timeout=timeout;
		this.proxy=proxy;
	}
	
	public JsoupRequests(ProxyPara proxy,int timeout){
		this.timeout=timeout;
		this.proxy=proxy;
	}
	
	@Override
	public Response get(String url) throws IOException{
		org.jsoup.Connection.Response resp=getResp(url, null);
		return new Response(turnHsToList(resp.headers()),new ByteArrayInputStream(resp.body().getBytes("utf-8")),"utf-8",url);
	}

	@Override
	public Response getUseHeader(String url, Map<String, String> headers) throws IOException{
		org.jsoup.Connection.Response resp=getResp(url, headers);
		return new Response(turnHsToList(resp.headers()),new ByteArrayInputStream(resp.body().getBytes("utf-8")),"utf-8",url);
	}

	@Override
	public Response getUseHeaderAndDecode(String url, Map<String, String> headers, String decode) throws IOException{
		org.jsoup.Connection.Response resp=getResp(url, headers);
		return new Response(turnHsToList(resp.headers()),new ByteArrayInputStream(resp.bodyAsBytes()),decode,url);
	}
	
	private org.jsoup.Connection.Response getResp(String url, Map<String, String> headers) throws IOException{
		Connection con=getConnection(url,headers)
						.followRedirects(true)
						.method(Method.GET);
		org.jsoup.Connection.Response resp = con.execute();
		return resp;
	}
	
	
	@Override
	public Response post(String url, String data) throws IOException {
		org.jsoup.Connection.Response resp = postResp(url,data,null);
		return new Response(turnHsToList(resp.headers()),new ByteArrayInputStream(resp.body().getBytes("utf-8")),"utf-8",url);
	}

	@Override
	public Response postUseHeader(String url, String data, Map<String, String> headers) throws IOException {
		org.jsoup.Connection.Response resp = postResp(url,data,headers);
		return new Response(turnHsToList(resp.headers()),new ByteArrayInputStream(resp.body().getBytes("utf-8")),"utf-8",url);
	}

	@Override
	public Response postUseHeaderAndDecode(String url, String data, Map<String, String> headers, String decode) throws IOException{
		org.jsoup.Connection.Response resp = postResp(url,data,headers);
		return new Response(turnHsToList(resp.headers()),new ByteArrayInputStream(resp.bodyAsBytes()),decode,url);
	}
	
	
	private List<NameValuePair> turnHsToList(Map<String, String> hds){
		List<NameValuePair> headers=new ArrayList<NameValuePair>();
		
		Set<String> keys = hds.keySet();
		for(String key:keys){
			headers.add(new NameValuePair(key,hds.get(key)));
		}

		return headers;
	};
	
	private org.jsoup.Connection.Response postResp(String url, String data,Map<String, String> headers) throws IOException{
		Connection con=getConnection(url,headers).followRedirects(true).method(Method.POST);
		con.data(data);
		org.jsoup.Connection.Response resp = con.execute();
		return resp;
	}
	
	private Connection getConnection(String url,Map<String,String> headers){
		Connection con = Jsoup.connect(url);
		if(proxy!=null){
			con.proxy(proxy.getHost(), proxy.getPort());
		}
		if(headers!=null){
			Set<String> keys = headers.keySet();
			for(String key:keys){
				con.header(key,headers.get(key));
			}
		}
		con.timeout(timeout);
		return con;
	}
	
}
