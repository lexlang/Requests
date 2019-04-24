package com.lexlang.Requests.requests;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
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

import javax.imageio.ImageReader;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.FormEncodingType;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.TopLevelWindow;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.BrowserVersion.BrowserVersionBuilder;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.KeyDataPair;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.lexlang.Requests.proxy.ProxyPara;
import com.lexlang.Requests.responses.Response;
import com.lexlang.Requests.util.PostGetJS;
import com.lexlang.Requests.util.UrlUtils;


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
		return getUseHeader(url,null);
	}

	@Override
	public Response getUseHeader(String url, Map<String, String> headers) throws Exception {
		return getUseHeaderAndDecode(url,headers,null);
	}

	@Override
	public Response getUseHeaderAndDecode(String url, Map<String, String> headers, String decode) throws Exception {
		webClient.getOptions().setRedirectEnabled(true);
		webClient.getOptions().setJavaScriptEnabled(true);
		WebRequest webGet=new WebRequest(new URL(url),HttpMethod.GET);
		setHeader(webGet,headers);
		return request(webGet,true,decode);
	}

	@Override
	public Response post(String url, String data) throws Exception {
		return postUseHeader(url,data,null);
	}

	@Override
	public Response postUseHeader(String url, String data, Map<String, String> headers) throws Exception {
		return postUseHeaderAndDecode(url,data,headers,null);
	}

	@Override
	public Response postUseHeaderAndDecode(String url, String data, Map<String, String> headers, String decode)
			throws Exception {
		webClient.getOptions().setRedirectEnabled(true);
		webClient.getOptions().setJavaScriptEnabled(true);
		WebRequest webPost=new WebRequest(new URL(url),HttpMethod.POST);
		setHeader(webPost,headers);
		webPost.setRequestBody(data);
		return request(webPost,true,decode);
	}
	
	@Override
	public Response getNoRedirect(String url) throws Exception{
		return getUseHeaderNoRedirect(url,null);
	}
	
	@Override
	public Response getUseHeaderNoRedirect(String url,Map<String,String> headers) throws Exception{
		return getUseHeaderAndDecodeNoRedirect(url,headers,null);
	}
	
	@Override
	public Response getUseHeaderAndDecodeNoRedirect(String url,Map<String,String> headers,String decode) throws Exception{
		webClient.getOptions().setRedirectEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		WebRequest webGet=new WebRequest(new URL(url),HttpMethod.GET);
		setHeader(webGet,headers);
		return request(webGet,true,decode);
	}
	
	@Override
	public Response postNoRedirect(String url,String data) throws Exception{
		return postUseHeaderNoRedirect(url,data,null);
	}
	
	@Override
	public Response postUseHeaderNoRedirect(String url,String data,Map<String,String> headers) throws Exception{
		return postUseHeaderAndDecodeNoRedirect(url,data,headers,null);
	}
	
	@Override
	public Response postUseHeaderAndDecodeNoRedirect(String url,String data,Map<String,String> headers,String decode) throws Exception{
		webClient.getOptions().setRedirectEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);
		WebRequest webPost=new WebRequest(new URL(url),HttpMethod.POST);
		setHeader(webPost,headers);
		webPost.setRequestBody(data);
		return request(webPost,true,decode);
	}
	
	@Override
	public Response getAjax(String url) throws Exception{
		return getUseHeaderAjax(url,null);
	}
	
	@Override
	public Response getUseHeaderAjax(String url,Map<String,String> headers) throws Exception{
		return getUseHeaderAndDecodeAjax(url,headers,null);
	}
	
	@Override
	public Response getUseHeaderAndDecodeAjax(String url,Map<String,String> headers,String decode) throws Exception{
		String postJs = PostGetJS.postGetJS(url, "GET", headers, "")
				.replace("return xmlhttp.responseText", "window.getContent=xmlhttp.responseText");
		executeJavaScript(postJs);
		String content=getHtmlPage().executeJavaScript("window.getContent").getJavaScriptResult().toString();
		JSONArray result=JSONArray.parseArray(getHtmlPage().executeJavaScript("window.getContent").getJavaScriptResult().toString());
		List<NameValuePair> hd=new ArrayList<NameValuePair>();
		for(int index=0;index<result.size();index++){
			JSONObject item = result.getJSONObject(index);
			Set<String> keys = item.keySet();
			for(String key:keys){
				hd.add(new NameValuePair(key,item.getString(key)));
			}
		}
		return new Response(hd,new ByteArrayInputStream(content.getBytes("utf-8")),"utf-8",url);
	}
	
	@Override
	public Response postAjax(String url,String data) throws Exception{
		return postUseHeaderAjax(url,data,null);
	}
	
	@Override
	public Response postUseHeaderAjax(String url,String data,Map<String,String> headers) throws Exception{
		return postUseHeaderAndDecodeAjax(url,data,headers,null);
	}
	
	@Override
	public Response postUseHeaderAndDecodeAjax(String url,String data,Map<String,String> headers,String decode) throws Exception{
		String postJs = PostGetJS.postGetJS(url, "POST", headers, data)
						.replace("return xmlhttp.responseText", "window.getContent=xmlhttp.responseText");
		executeJavaScript(postJs);
		String content=getHtmlPage().executeJavaScript("window.getContent").getJavaScriptResult().toString();
		JSONArray result=JSONArray.parseArray(getHtmlPage().executeJavaScript("window.getContent").getJavaScriptResult().toString());
		List<NameValuePair> hd=new ArrayList<NameValuePair>();
		for(int index=0;index<result.size();index++){
			JSONObject item = result.getJSONObject(index);
			Set<String> keys = item.keySet();
			for(String key:keys){
				hd.add(new NameValuePair(key,item.getString(key)));
			}
		}
		return new Response(hd,new ByteArrayInputStream(content.getBytes("utf-8")),"utf-8",url);
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
	
	private Response request(WebRequest request,boolean renderEnabled,String decode) throws FailingHttpStatusCodeException, IOException{
		if(renderEnabled){
			HtmlPage page=webClient.getPage(request);
			webClient.waitForBackgroundJavaScript(RENDER_TIME);//等待页面渲染时长
			return new Response(page.getWebResponse().getResponseHeaders(),new ByteArrayInputStream(page.asXml().getBytes("utf-8")),"utf-8",page.getUrl().toString());
		}else{
			Page page=webClient.getPage(request);
			if(decode!=null){
				return new Response(page.getWebResponse().getResponseHeaders(),page.getWebResponse().getContentAsStream(),null,page.getUrl().toString());
			}else{
				return new Response(page.getWebResponse().getResponseHeaders(),page.getWebResponse().getContentAsStream(),decode,page.getUrl().toString());
			}
		}
	}
	
	/**
	 * 
	 * @param url 上传url
	 * @param dataMap 除了file之外的上传值
	 * @param headers 消息头
	 * @param files   文件相关的值    key:"uploadedfile", "1.txt", "text/plain", "utf-8"
	 * @param renderEnabled 是否渲染
	 * @param decode  解码方式
	 * @param sleepTime 渲染时长
	 * @return
	 * @throws FailingHttpStatusCodeException
	 * @throws IOException
	 */
	public Response postFile(String url,Map<String,String> dataMap,Map<String,String> headers,Map<String,File> files,boolean renderEnabled,String decode,int sleepTime) throws FailingHttpStatusCodeException, IOException{
		WebRequest webPost=new WebRequest(new URL(url),HttpMethod.POST);
		setHeader(webPost,headers);
		webPost.setEncodingType(FormEncodingType.MULTIPART);
		List<NameValuePair> list=new ArrayList<NameValuePair>();
		
		Set<String> keys = dataMap.keySet();
		for(String key:keys){
			list.add(new NameValuePair(key,dataMap.get(key)));
		}
		
		//File file = File.createTempFile("temp", ".txt");//创建临时文件
		Set<String> fileKeys=files.keySet();
		for(String key:fileKeys){
			// "uploadedfile",files.get(key), "1.txt", "text/plain", "utf-8"
			// key值为 上传文件属性值，文件名，文件类型，文件编码方式，   中间用制表符隔开
			String[] arr=key.split("\t");
			list.add(new KeyDataPair(arr[0],files.get(key), arr[1], arr[2], arr[3]));
		}
		webPost.setRequestParameters(list);
		return request(webPost,true,decode);
	}
	


	private void setHeader(WebRequest request,Map<String,String> headers){
		if(headers==null){return ;}
		Set<String> keys = headers.keySet();
		for(String key:keys){
			request.setAdditionalHeader(key, headers.get(key));
		}
	}
	
	public HtmlPage getHtmlPage(){
		HtmlPage page = (HtmlPage) webClient.getCurrentWindow().getEnclosedPage();
		//page.executeJavaScript(sourceCode)
		return page;
	}
	
	/**
	 * 执行js代码
	 * @param javascript
	 */
	public void executeJavaScript(String javascript){
		getHtmlPage().executeJavaScript(javascript);
	}
	
	/**
	 * 获得当前页面URL
	 * @return
	 */
	public String getCurrentUrl(){
		return webClient.getCurrentWindow().getEnclosedPage().getUrl().toString();
	}
	
	/**
	 * 获得图片
	 * @param cssSelector
	 * @return
	 * @throws IOException
	 */
	public BufferedImage getBufferedImage(String cssSelector) throws IOException{
		HtmlPage page = (HtmlPage) webClient.getCurrentWindow().getEnclosedPage();
		HtmlImage htmlImage=page.querySelector(cssSelector);
		ImageReader imgReader = htmlImage.getImageReader();
		return imgReader.read(0);
	}
	
	/**
	 * 清除历史任务
	 */
	public void close(){
		//清除当前历史数据
		webClient.getCurrentWindow().getHistory().removeCurrent();
		//清除当前JS任务
        webClient.getCurrentWindow().getJobManager().removeAllJobs();
        webClient.close();
	}
	
	/**
	 * 清楚所有的js代码任务
	 */
	public void shutDownAllJavaScript(){
		List<WebWindow> wins = getWetClient().getWebWindows();
		for(WebWindow win:wins){
			win.getHistory().removeCurrent();
			win.getJobManager().removeAllJobs();
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public WebClient getWetClient(){
		return this.webClient;
	}
	
	/**
	 * 点击链接名为sectionName,切换链接页面,只能处理a标签下面的text()
	 * @param sectionName
	 * @throws IOException
	 */
	public void switchSection(String sectionName) throws IOException{
		clickSection("//a[text()='"+sectionName+"']");
	}
	
	/**
	 * 点击页面对应文字得标签
	 * @param xpath //a[text()='下一页'] 或者   a#btnSearch
	 * @throws IOException 
	 */
	public void clickSection(String xpathOrCssSelector) throws IOException{
		if(xpathOrCssSelector.startsWith("//")){
			DomElement btn = (DomElement) getHtmlPage().getByXPath(xpathOrCssSelector).get(0);
			btn.click();
			waitForBackgroundJavaScript();
		}else{
			DomElement btn = (DomElement) getHtmlPage().querySelector(xpathOrCssSelector);
			btn.click();
			waitForBackgroundJavaScript();
		}
	}
	
	/**
	 * 等待页面渲染后,加载页面
	 */
	public void waitForBackgroundJavaScript(){
		webClient.waitForBackgroundJavaScript(2000);
	}
	
	
	/**
	 * 知道元素出现才执行下一步,否则抛异常
	 * @param cssSelector
	 */
	public void utilShowUp(String cssSelector){
		for(int i=0;i<10*30;i++){
			try{
				if(cssSelector.startsWith("//")){
					getHtmlPage().getFirstByXPath(cssSelector);
				}else{
					getHtmlPage().querySelector(cssSelector);
				}
				return;
			}catch(Exception ex){}
			try{Thread.sleep(200);}catch(Exception ex){}
		}
		throw new RuntimeException("对应的元素未出现");
	}
	
	/**
	 * 补全链接的页面
	 * @return
	 */
	public String getFixAllRelativeHrefsContent(){
		return UrlUtils.fixAllRelativeHrefs(getHtmlPage().asXml(), getCurrentUrl());
	}
	
	/**
	 * 切换页面,不关闭其他页面
	 * @param match 切换链接符合这个正则
	 */
	public void switchToWindow(String match){
		switchToWindow(match,false);
	}
	
	/**
	 * 切换页面,关闭其他页面
	 * @param match 切换链接符合这个正则
	 */
	public void switchToWindowCloseOthers(String match){
		switchToWindow(match,true);
	}
	
	private void switchToWindow(String match,boolean flagClose){
		List<WebWindow> wins = getWetClient().getWebWindows();
		String currentUrl=null;
		//切换到想要得页面
		for(WebWindow win:wins){
			if(win.getEnclosedPage().getUrl().toString().matches(match)){
				getWetClient().setCurrentWindow(win);
				WebWindow currentWin = win.getEnclosedPage().getEnclosingWindow();
				currentUrl=currentWin.getEnclosedPage().getUrl().toString();
			}
		}
		//关掉其他页面
		if(flagClose && currentUrl!=null){
			for(WebWindow win:wins){
				if(! win.getEnclosedPage().getUrl().toString().matches(match)){
					WebWindow currentWin = win.getEnclosedPage().getEnclosingWindow();
					if(checkWinIsTopLevelWindow(currentWin)){
						TopLevelWindow closeAble = (TopLevelWindow) currentWin;
						if(! currentUrl.equals(closeAble.getEnclosedPage().getUrl().toString())){
							closeAble.close();
						}
					}
				}
			}
		}
	}
	
	/**
	 * 检查是否是顶级窗口
	 * @param win
	 * @return
	 */
	private boolean checkWinIsTopLevelWindow(WebWindow win){
		try{
			TopLevelWindow closeAble = (TopLevelWindow) win;
			return true;
		}catch(Exception ex){}
		return false;
	}
	
	/**
	 * 开关js执行
	 * @param javaScriptEnabeled
	 */
	public void setJavaScriptEnabled(boolean javaScriptEnabeled){
		webClient.getOptions().setJavaScriptEnabled(javaScriptEnabeled);
	}
	
	
}
