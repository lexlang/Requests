package com.lexlang.Requests.requests;

import java.util.Map;

import com.lexlang.Requests.responses.Response;

/**
* @author lexlang
* @version 2019年3月12日 上午11:31:54
* 
*/
public abstract class Request {
	
	/**
	 * 简单get请求 
	 * @param url 请求的url
	 * @return
	 */
	public abstract Response get(String url) throws Exception; 
	

	
	/**
	 * get请求使用消息头
	 * @param url 请求的url
	 * @param headers 请求的消息头
	 * @return
	 */
	public abstract Response getUseHeader(String url,Map<String,String> headers) throws Exception;
	
	

	
	/**
	 * get 请求使用消息头并指定编码
	 * @param url 请求的url
	 * @param headers 请求的消息头
	 * @param decode  指定编码
	 * @return
	 */
	public abstract Response getUseHeaderAndDecode(String url,Map<String,String> headers,String decode) throws Exception;
	
	
	/**
	 * 简单post请求
	 * @param url 请求的url
	 * @param data 请求的消息体
	 * @return
	 */
	public abstract Response post(String url,String data) throws Exception;
	

	/**
	 * post请求使用消息头
	 * @param url 请求的url
	 * @param data 请求的消息体
	 * @param headers 请求的消息体
	 * @return
	 */
	public abstract Response postUseHeader(String url,String data,Map<String,String> headers) throws Exception;
	
	
	/**
	 * post 请求使用消息头并指定编码
	 * @param url 请求的url
	 * @param headers 请求的消息头
	 * @param decode  指定编码
	 * @param data 请求的消息体
	 * @return
	 */
	public abstract Response postUseHeaderAndDecode(String url,String data,Map<String,String> headers,String decode) throws Exception;
	
	
	/**
	 * 简单get请求不跳转
	 * @param rul
	 * @return
	 * @throws Exception
	 */
	public Response getNoRedirect(String url) throws Exception{
		new RuntimeException("使用的实体未实现该方法");
		return null;
	}
	

	
	/**
	 * get请求使用消息头不跳转
	 * @param rul
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	public Response getUseHeaderNoRedirect(String url,Map<String,String> headers) throws Exception{
		new RuntimeException("使用的实体未实现该方法");
		return null;
	}
	
	/**
	 * get 请求使用消息头并指定编码 不跳转
	 * @param url
	 * @param headers
	 * @param decode
	 * @return
	 * @throws Exception
	 */
	public Response getUseHeaderAndDecodeNoRedirect(String url,Map<String,String> headers,String decode) throws Exception{
		new RuntimeException("使用的实体未实现该方法");
		return null;
	}
	
	/**
	 * 简单post请求 不跳转
	 * @param url
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public Response postNoRedirect(String url,String data) throws Exception{
		new RuntimeException("使用的实体未实现该方法");
		return null;
	}
	
	/**
	 * post请求使用消息头 不跳转
	 * @param url
	 * @param data
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	public Response postUseHeaderNoRedirect(String url,String data,Map<String,String> headers) throws Exception{
		new RuntimeException("使用的实体未实现该方法");
		return null;
	}
	
	
	/**
	 * post 请求使用消息头并指定编码 不跳转
	 * @param url
	 * @param data
	 * @param headers
	 * @param decode
	 * @return
	 * @throws Exception
	 */
	public Response postUseHeaderAndDecodeNoRedirect(String url,String data,Map<String,String> headers,String decode) throws Exception{
		new RuntimeException("使用的实体未实现该方法");
		return null;
	}
	
	/**
	 * 简单get请求Ajax
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public Response getAjax(String url) throws Exception{
		new RuntimeException("使用的实体未实现该方法");
		return null;
	}
	

	
	/**
	 * get请求使用消息头Ajax
	 * @param rul
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	public Response getUseHeaderAjax(String url,Map<String,String> headers) throws Exception{
		new RuntimeException("使用的实体未实现该方法");
		return null;
	}
	
	/**
	 * get 请求使用消息头并指定编码 Ajax
	 * @param url
	 * @param headers
	 * @param decode
	 * @return
	 * @throws Exception
	 */
	public Response getUseHeaderAndDecodeAjax(String url,Map<String,String> headers,String decode) throws Exception{
		new RuntimeException("使用的实体未实现该方法");
		return null;
	}
	
	/**
	 * 简单post请求 Ajax
	 * @param url
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public Response postAjax(String url,String data) throws Exception{
		new RuntimeException("使用的实体未实现该方法");
		return null;
	}
	
	/**
	 * post请求使用消息头 Ajax
	 * @param url
	 * @param data
	 * @param headers
	 * @return
	 * @throws Exception
	 */
	public Response postUseHeaderAjax(String url,String data,Map<String,String> headers) throws Exception{
		new RuntimeException("使用的实体未实现该方法");
		return null;
	}
	
	
	/**
	 * post 请求使用消息头并指定编码 Ajax
	 * @param url
	 * @param data
	 * @param headers
	 * @param decode
	 * @return
	 * @throws Exception
	 */
	public Response postUseHeaderAndDecodeAjax(String url,String data,Map<String,String> headers,String decode) throws Exception{
		new RuntimeException("使用的实体未实现该方法");
		return null;
	}
	
}
