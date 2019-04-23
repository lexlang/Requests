package com.lexlang.Requests.requests;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.BrowserVersion.BrowserVersionBuilder;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.lexlang.Requests.proxy.ProxyPara;
import com.lexlang.Requests.responses.Response;


/**
* @author lexlang
* @version 2019年4月17日 下午4:01:04
* 
*/
public class HtmlUnitRequests  extends Request{
	static{
		   Logger htmlunilLogger = Logger.getLogger("com.gargoylesoftware.htmlunit");
	       htmlunilLogger.setLevel(Level.OFF);
	 }
	
	private static final int RENDER_TIME=2000;//默认渲染时长
	private static final int TIME_OUT=30000;//默认超时时间
	private WebClient webClient;
	private ProxyPara proxy;//默认代理
	
	public HtmlUnitRequests(){
		this(TIME_OUT,null);
	}
	
	public HtmlUnitRequests(int timeout){
		this(timeout,null);
	}
	
	public HtmlUnitRequests(ProxyPara proxy){
		this(TIME_OUT,proxy);
	}
	
	public HtmlUnitRequests(ProxyPara proxy,int timeout){
		this(timeout,proxy);
	}
	
	public HtmlUnitRequests(int timeout,ProxyPara proxy){
		this.webClient= new WebClient(); 
		//设置一个UA
		BrowserVersion[] bvs= {BrowserVersion.FIREFOX_52,BrowserVersion.EDGE,BrowserVersion.INTERNET_EXPLORER};
		Random rand=new Random();
		BrowserVersionBuilder browser = new  BrowserVersion.BrowserVersionBuilder(bvs[rand.nextInt(bvs.length)]);
		webClient.getBrowserVersion().setDefault(browser.build());
		
		if(proxy!=null){ //手工指定一个代理
			setProxy(proxy);
		}
		
	    webClient.getOptions().setUseInsecureSSL(true);
	    webClient.getOptions().setCssEnabled(false);
       // webClient.TsetCssErrorHandler(new SilentCssErrorHandler());
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        //webClient.getOptions().setRedirectEnabled(redirectEnable);//默认自动跳转
        webClient.getOptions().setAppletEnabled(false);
        //webClient.getOptions().setJavaScriptEnabled(javaScriptEnabeled);//默认打开js
        webClient.getOptions().setTimeout(timeout);
        webClient.setJavaScriptTimeout(60000);
        webClient.getOptions().setPrintContentOnFailingStatusCode(false);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController()); 
	}
	
	/**
	 * 设置代理
	 * @param proxyPara
	 */
	private void setProxy(ProxyPara proxyPara){
		ProxyConfig proxyConfig = this.webClient.getOptions().getProxyConfig();
		proxyConfig.setProxyHost(proxyPara.getHost());
		proxyConfig.setProxyPort(proxyPara.getPort());
	}
	
	@Override
	public Response get(String url) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getUseHeader(String url, Map<String, String> headers) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getUseHeaderAndDecode(String url, Map<String, String> headers, String decode) throws Exception {
		webClient.getOptions().setRedirectEnabled(true);
		webClient.getOptions().setJavaScriptEnabled(true);
		
		WebRequest webGet=new WebRequest(new URL(url),HttpMethod.GET);
		setHeader(webGet,headers);
		
		return null;
	}

	@Override
	public Response post(String url, String data) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response postUseHeader(String url, String data, Map<String, String> headers) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response postUseHeaderAndDecode(String url, String data, Map<String, String> headers, String decode)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Response getNoRedirect(String url) throws Exception{
		new RuntimeException("使用的实体未实现该方法");
		return null;
	}
	
	@Override
	public Response getUseHeaderNoRedirect(String url,Map<String,String> headers) throws Exception{
		new RuntimeException("使用的实体未实现该方法");
		return null;
	}
	
	@Override
	public Response getUseHeaderAndDecodeNoRedirect(String url,Map<String,String> headers,String decode) throws Exception{
		new RuntimeException("使用的实体未实现该方法");
		return null;
	}
	
	@Override
	public Response postNoRedirect(String url,String data) throws Exception{
		new RuntimeException("使用的实体未实现该方法");
		return null;
	}
	
	@Override
	public Response postUseHeaderNoRedirect(String url,String data,Map<String,String> headers) throws Exception{
		new RuntimeException("使用的实体未实现该方法");
		return null;
	}
	
	@Override
	public Response postUseHeaderAndDecodeNoRedirect(String url,String data,Map<String,String> headers,String decode) throws Exception{
		new RuntimeException("使用的实体未实现该方法");
		return null;
	}
	
	private Response request(WebRequest request,boolean renderEnabled,String decode) throws FailingHttpStatusCodeException, IOException{
		if(renderEnabled){
			HtmlPage page=webClient.getPage(request);
			this.webClient.waitForBackgroundJavaScript(RENDER_TIME);//等待页面渲染时长
			if(decode!=null){
				return null;
			}else{
				return null;
			}
		}else{
			Page page=webClient.getPage(request);
			if(decode!=null){
				return null;
			}else{
				return new Response(page.getWebResponse().getResponseHeaders(),page.getWebResponse().getContentAsStream(),"",page.getUrl().toString());
			}
		}
	}
	


	private void setHeader(WebRequest request,Map<String,String> headers){
		if(headers==null){return ;}
		Set<String> keys = headers.keySet();
		for(String key:keys){
			request.setAdditionalHeader(key, headers.get(key));
		}
	}
	
	
	@Override
	public Response getAjax(String url) throws Exception{
		new RuntimeException("使用的实体未实现该方法");
		return null;
	}
	
	@Override
	public Response getUseHeaderAjax(String url,Map<String,String> headers) throws Exception{
		new RuntimeException("使用的实体未实现该方法");
		return null;
	}
	
	@Override
	public Response getUseHeaderAndDecodeAjax(String url,Map<String,String> headers,String decode) throws Exception{
		new RuntimeException("使用的实体未实现该方法");
		return null;
	}
	
	@Override
	public Response postAjax(String url,String data) throws Exception{
		new RuntimeException("使用的实体未实现该方法");
		return null;
	}
	
	@Override
	public Response postUseHeaderAjax(String url,String data,Map<String,String> headers) throws Exception{
		new RuntimeException("使用的实体未实现该方法");
		return null;
	}
	
	@Override
	public Response postUseHeaderAndDecodeAjax(String url,String data,Map<String,String> headers,String decode) throws Exception{
		new RuntimeException("使用的实体未实现该方法");
		return null;
	}
	
	public void clearAllCookies(){
		this.webClient.getCookieManager().clearCookies();
	}
	
	/**
	 * 添加cookie值
	 * @param cookies 复制cookie值
	 * @param domain eg:sbgg.saic.gov.cn
	 */
	public void addCookie(String cookies,String domain){
		CookieManager cookieManger = this.webClient.getCookieManager();
		//
		if(cookies.contains(";")){
			String[] cs=cookies.split(";");
			for(String cookie:cs){
				addSetCookie(cookie.trim(),domain,cookieManger);
			}
		}else{
			addSetCookie(cookies.trim(),domain,cookieManger);
		}
	}
	
	private void addSetCookie(String cookie,String domain,CookieManager cookieManger){
		if(cookie.contains("=")){
			String name=cookie.substring(0,cookie.indexOf("="));
			String value=cookie.substring(cookie.indexOf("=")+1, cookie.length());
			try{cookieManger.addCookie(new Cookie(domain, name,value));}catch(Exception ex){}
		}
	}
	
	/**
	 * 获得指定cookie值
	 * @param name
	 * @return
	 */
	public String getCookieValue(String name){
		Set<Cookie> cs = webClient.getCookieManager().getCookies();
		for(Cookie cook:cs){
			if(cook.getName().equals(name)){
				return cook.getValue();
			}
		}
		return null;//没有找到对应的cookie值
	}
	
	/**
	 * 获得cookies
	 * @return
	 */
	public String getCookie(){
		Set<Cookie> cs = webClient.getCookieManager().getCookies();
		List<NameValuePair> list=new ArrayList<NameValuePair>();
		StringBuilder sb=new StringBuilder();
		for(Cookie cookie:cs){
			//list.add(new NameValuePair(cookie.getName(),cookie.getValue()));
			if(sb.toString().length()==0){
				sb.append(cookie.getName()+"="+cookie.getValue());
			}
			else{
				sb.append(";"+cookie.getName()+"="+cookie.getValue());
			}
		}
		return sb.toString();
	}
	
}
