package com.lexlang.Requests.requests;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.ssl.SSLContexts;

import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.lexlang.Requests.proxy.ProxyPara;
import com.lexlang.Requests.responses.Response;

/**
* @author lexlang
* @version 2019年4月17日 下午2:55:38
* 
*/
public class HttpClientRequests  extends Request {
	static{//屏蔽日志
		java.util.logging.Logger.getLogger("org.apache.http.wire").setLevel(java.util.logging.Level.FINEST);
		java.util.logging.Logger.getLogger("org.apache.http.headers").setLevel(java.util.logging.Level.FINEST);
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.SimpleLog");
		System.setProperty("org.apache.commons.logging.simplelog.showdatetime", "true");
		System.setProperty("org.apache.commons.logging.simplelog.log.httpclient.wire", "ERROR");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http", "ERROR");
		System.setProperty("org.apache.commons.logging.simplelog.log.org.apache.http.headers", "ERROR");
	}
	
	
	private static final int TIME_OUT=60000;//默认超时时间
	
	private int timeout;
	private ProxyPara proxy;
	private HttpClient httpClient;
	private CookieStore cookieStore;
	
	public HttpClientRequests(){
		this(TIME_OUT,null);
	}
	
	public HttpClientRequests(int timeout){
		this(timeout,null);
	}
	
	public HttpClientRequests(ProxyPara proxy){
		this(TIME_OUT,proxy);
	}
	
	
	public HttpClientRequests(ProxyPara proxy,int timeout){
		this(timeout,proxy);
	}
	
	public HttpClientRequests(int timeout,ProxyPara proxy){
		this.timeout=timeout;
		//最大链接数
		PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager();
		// 设置连接池大
		connMgr.setMaxTotal(100);
		connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());
		
		//设置超时时间
		Builder configBuilder = RequestConfig.custom();
		configBuilder.setConnectTimeout(timeout);
		configBuilder.setSocketTimeout(timeout);
		configBuilder.setConnectionRequestTimeout(timeout);
		
		if(proxy!=null){
			setProxy(configBuilder,proxy);
		}
		
		RequestConfig requestConfig=configBuilder.build();
		//cookie值存储
		cookieStore = new BasicCookieStore();
		try {
			httpClient = HttpClients.custom().setSSLContext(createIgnoreVerifySSL())
					.setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).setDefaultCookieStore(cookieStore).build();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 设置代理
	 * @param builder
	 * @param proxyPara
	 */
	private void setProxy(Builder builder,ProxyPara proxyPara){
		HttpHost pro=new HttpHost(proxyPara.getHost(), proxyPara.getPort());
		builder.setProxy(pro);
	}
	
	@Override
	public Response get(String url) throws ClientProtocolException, URISyntaxException, IOException {
		// TODO Auto-generated method stub
		return getUseHeader(url,null);
	}

	@Override
	public Response getUseHeader(String url, Map<String, String> headers) throws ClientProtocolException, URISyntaxException, IOException  {
		// TODO Auto-generated method stub
		return getUseHeaderAndDecode(url,headers,null);
	}

	@Override
	public Response getUseHeaderAndDecode(String url, Map<String, String> headers, String decode) throws URISyntaxException, ClientProtocolException, IOException {
		HttpGet httpGet=new HttpGet();
		httpGet.setURI(new URI(url));
		if(headers!=null){
			Set<String> keys = headers.keySet();
			for(String key:keys){
				httpGet.setHeader(key, headers.get(key));
			}
		}
		HttpResponse response = httpClient.execute(httpGet);
		return new Response(turnHsToList(response.getAllHeaders())
				,response.getEntity().getContent(),decode,url,response.getStatusLine().getStatusCode());
	}
	
	private List<NameValuePair> turnHsToList(Header[] hds){
		List<NameValuePair> headers=new ArrayList<NameValuePair>();

		for(int index=0;index<hds.length;index++){
			Header item = hds[index];
			headers.add(new NameValuePair(item.getName(),item.getValue()));
		}
		
		return headers;
	};
	
	@Override
	public Response post(String url, String data) throws ClientProtocolException, IOException, URISyntaxException{
		return postUseHeader(url,data,null);
	}

	@Override
	public Response postUseHeader(String url, String data, Map<String, String> headers) throws ClientProtocolException, IOException, URISyntaxException {
		return postUseHeaderAndDecode(url,data,headers,null);
	}

	@Override
	public Response postUseHeaderAndDecode(String url, String data, Map<String, String> headers, String decode) throws ClientProtocolException, IOException, URISyntaxException {
		HttpPost httpPost=new HttpPost();
		httpPost.setURI(new URI(url));
	
		if(headers!=null){
			Set<String> keys = headers.keySet();
			for(String key:keys){
				httpPost.setHeader(key, headers.get(key));
			}
		}else{
			//设置一个默认的post header
			httpPost.setHeader("content-type", "application/x-www-form-urlencoded");
		}
		
		//添加实体
		httpPost.setEntity(new StringEntity(data));
		
		HttpResponse response = httpClient.execute(httpPost);
		
		return new Response(turnHsToList(response.getAllHeaders()),response.getEntity().getContent(),decode,url,response.getStatusLine().getStatusCode());
	}
	
	@Override
	public Response getNoRedirect(String url) throws ClientProtocolException, URISyntaxException, IOException{
		return getUseHeaderNoRedirect(url,null);
	}
	
	@Override
	public Response getUseHeaderNoRedirect(String url,Map<String,String> headers) throws ClientProtocolException, URISyntaxException, IOException {
		return getUseHeaderAndDecodeNoRedirect(url,headers,null);
	}
	
	@Override
	public Response getUseHeaderAndDecodeNoRedirect(String url,Map<String,String> headers,String decode) throws URISyntaxException, ClientProtocolException, IOException {
		HttpGet httpGet=new HttpGet();
		httpGet.setURI(new URI(url));
		
		RequestConfig requestConfig = RequestConfig.custom()
				.setRedirectsEnabled(false)
				.setConnectTimeout(timeout)
				.setSocketTimeout(timeout)
				.setConnectionRequestTimeout(timeout)
				.build();
		httpGet.setConfig(requestConfig);

		if(headers!=null){
			Set<String> keys = headers.keySet();
			for(String key:keys){
				httpGet.setHeader(key, headers.get(key));
			}
		}
		HttpResponse response = httpClient.execute(httpGet);
		
		return new Response(turnHsToList(response.getAllHeaders()),response.getEntity().getContent(),decode,url,response.getStatusLine().getStatusCode());
	}
	
	@Override
	public Response postNoRedirect(String url,String data) throws UnsupportedOperationException, IOException, URISyntaxException{
		return postUseHeaderNoRedirect(url,data,null);
	}
	
	@Override
	public Response postUseHeaderNoRedirect(String url,String data,Map<String,String> headers) throws UnsupportedOperationException, IOException, URISyntaxException {
		return postUseHeaderAndDecodeNoRedirect(url,data,headers,null);
	}
	
	
	@Override
	public Response postUseHeaderAndDecodeNoRedirect(String url,String data,Map<String,String> headers,String decode) throws UnsupportedOperationException, IOException, URISyntaxException {
		HttpPost httpPost=new HttpPost();
		httpPost.setURI(new URI(url));
		
		RequestConfig requestConfig = RequestConfig.custom()
				.setRedirectsEnabled(false)
				.setConnectTimeout(timeout)
				.setSocketTimeout(timeout)
				.setConnectionRequestTimeout(timeout)
				.build();
		httpPost.setConfig(requestConfig);
		
		if(headers!=null){
			Set<String> keys = headers.keySet();
			for(String key:keys){
				httpPost.setHeader(key, headers.get(key));
			}
		}else{
			//设置一个默认的post header
			httpPost.setHeader("content-type", "application/x-www-form-urlencoded");
		}
		
		//添加实体
		httpPost.setEntity(new StringEntity(data));
		
		HttpResponse response = httpClient.execute(httpPost);
		
		return new Response(turnHsToList(response.getAllHeaders()),response.getEntity().getContent(),decode,url,response.getStatusLine().getStatusCode());
	}
	
	/**
	 * 
	 * @param url        访问的链接
	 * @param dataMap    提交key value值
	 * @param headers    消息头
	 * @param files      提交的文件 key值为 "uploadedfile", "1.txt", "text/plain", "utf-8"
	 * @param decode     编码方式
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public Response postFile(String url,Map<String,String> dataMap,Map<String,String> headers,Map<String,File> files,String decode) throws ClientProtocolException, IOException, URISyntaxException{
		HttpPost httpPost=new HttpPost();
		httpPost.setURI(new URI(url));
		
		if(headers!=null){
			Set<String> keys = headers.keySet();
			for(String key:keys){
				httpPost.setHeader(key, headers.get(key));
			}
		}else{
			//设置一个默认的post header
			httpPost.setHeader("content-type", "application/x-www-form-urlencoded");
		}
		
		MultipartEntityBuilder build = MultipartEntityBuilder.create();
		
		// 相当于<input type="file" name="file"/>
		Set<String> keyFile = files.keySet();
		for(String key:keyFile){
			// "uploadedfile",files.get(key), "1.txt", "text/plain", "utf-8"
			// key值为 上传文件属性值，文件名，文件类型，文件编码方式，   中间用制表符隔开
			String[] arr=key.split("\t");
			build.addBinaryBody(arr[0],files.get(key), ContentType.create(arr[2], Charset.forName(arr[3])), arr[1]);
		}
		
		// 相当于<input type="text" name="userName" value=userName>
		Set<String> keys=dataMap.keySet();
		for(String key:keys){
			build.addTextBody(key, dataMap.get(key), ContentType.create("text/plain", Charset.forName(decode)));
		}
		
		httpPost.setEntity(build.build());
	   
		HttpResponse response = httpClient.execute(httpPost);
		return new Response(turnHsToList(response.getAllHeaders()),response.getEntity().getContent(),decode,url,response.getStatusLine().getStatusCode());
	}
	
	
    /** 
     * 绕过ssl验证 
     *   
     * @return 
     * @throws NoSuchAlgorithmException  
     * @throws KeyManagementException  
     */  
    private static SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {  
        SSLContext sc = SSLContext.getInstance("SSLv3");  
      
        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法  
        X509TrustManager trustManager = new X509TrustManager() {  
            public void checkClientTrusted(  
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,  
                    String paramString) throws CertificateException {  
            }  
      
            public void checkServerTrusted(  
                    java.security.cert.X509Certificate[] paramArrayOfX509Certificate,  
                    String paramString) throws CertificateException {  
            }  
      
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {  
                return null;  
            }  
        };  
      
        sc.init(null, new TrustManager[] { trustManager }, null);  
        return sc;  
    }  
	
    /** 
     * 设置信任自签名证书 
     *   
     * @param keyStorePath      密钥库路径 
     * @param keyStorepass      密钥库密码 
     * @return 
     */  
    private static SSLContext custom(String keyStorePath, String keyStorepass){  
        SSLContext sc = null;  
        FileInputStream instream = null;  
        KeyStore trustStore = null;  
        try {  
            trustStore = KeyStore.getInstance(KeyStore.getDefaultType());  
            instream = new FileInputStream(new File(keyStorePath));  
            trustStore.load(instream, keyStorepass.toCharArray());  
            // 相信自己的CA和所有自签名的证书  
            sc = SSLContexts.custom().loadTrustMaterial(trustStore, new TrustSelfSignedStrategy()).build();  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                instream.close();  
            } catch (IOException e) {  
            }  
        }  
        return sc;  
    }  
	
    
	/**
	 * 添加cookie值
	 * @param cookies 复制cookie值
	 * @param domain
	 */
	public void addCookie(String cookies,String domain){
		if(cookies.contains(";")){
			String[] cs=cookies.split(";");
			for(String cookie:cs){
				addSetCookie(cookie.trim(),domain);
			}
		}else{
			addSetCookie(cookies.trim(),domain);
		}
	}
	
	
	private void addSetCookie(String cookie, String domain) {
		 //新建一个COOKIE
		if(cookie.contains("=")){
			 String name=cookie.substring(0,cookie.indexOf("="));
			 String value=cookie.substring(cookie.indexOf("=")+1, cookie.length());
		     BasicClientCookie ck = new BasicClientCookie(name,value);
		     ck.setVersion(0);
		     ck.setDomain(domain);
		     cookieStore.addCookie(ck);
		}
	}
	
	
	/**
	 * 获得cookies
	 * @return
	 */
	public String getCookie(){
		List<Cookie> list = cookieStore.getCookies();
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<list.size();i++){
			Cookie item = list.get(i);
			if(sb.toString().length()==0){
				sb.append(item.getName()+"="+item.getValue());
			}else{
				sb.append(";"+item.getName()+"="+item.getValue());
			}
		}
		return sb.toString();
	}
	
	public HttpClient getHttpClient(){
		return this.httpClient;
	}
    
}
