# Requests
## 简介
本工具参照<u>python</u>中requests,对java的工具进行封装,使其使用相对统一,封装的工具有URL,<u>Jsoup</u>,HttpClient,HtmlUnit
## 使用方法
* 构造浏览器对象,需要调用的基础对象 加 Requests,例如调用htmlunit工具,就用HtmlUnitRequests.调用HttpClient工具,就用HttpClientRequests.以此类推:
```Java
HtmlUnitRequests requests = new HtmlUnitRequests();
```
* 构造器可以传入参数有超时时间和代理ip
```Java
HtmlUnitRequests requests = new HtmlUnitRequests(5000);//5秒钟超时
HtmlUnitRequests requests = new HtmlUnitRequests(new ProxyPara("127.0.0.1",12345));//设置代理
HtmlUnitRequests requests = new HtmlUnitRequests(5000,new ProxyPara("127.0.0.1",12345));//设置代理和超时时间
```
* 常用方法介绍
```Java
get(url);//请求一个url
getNoRedirect(url);//请求一个url,遇到状态码302或301,不自动跳转,支持htmlunit,httpClient实现类
getAjax(url);//使用ajax交互,请求一个url,只支持htmlunit实现类
getUseHeader(url,hd);//请求一个url,使用消息头,类似还有getUseHeaderNoRedirect,getUseHeaderAjax
getUseHeaderAndDecode(url,hd,"utf-8");//请求一个url,使用消息头和编码,类似还有getUseHeaderAndDecodeNoRedirect,getUseHeaderAndDecodeAjax
post(url,body);//一个url的post请求,消息体为body,post就比get多一个消息体,其他实现方法参考get实现
```
* 相应体Response介绍
```Java
getStatusCode();//获得状态码
getHeaders();//获得响应头
getContent();//获得相应文本   getFixHrefContent()获得自动补全文档
getInputStream();//获得相应流
getImage();//请求为图片,可以使用此方法
getJsonObject();//getJsonArray() 请求为json可以调用此方法
getDocument();//获得jsoup文档  getFixHrefDocument() 获得补全后的jsoup文档
getUnicode2String();//\uFFFF或者\x64 unicode或16进制编码 调用此方法获得对应文本
```
* cookie处理
HttpClientRequests和HtmlunitRequests的addCookie和getCookie,添加设置cookie
* 辅助工具介绍
HeaderConfig工具生成消息头
```Java
Map<String, String> hds = HeaderConfig.postBuilder().setReferer("http://www.baidu.com").build();
```
Intercept拦截器,功能拦截请求和响应,修改响应或者存储响应<br>
1.比如,使用htmlunit访问一个页面,需要渲染,每次请求时都会请求对应的js文件和图片,可以把js文件和图片缓存到本地,下次访问直接拿本地缓存,加快渲染速度,覆写storeResponseOrNot,下列缓存js和css
```Java
new Intercept(requests.getWetClient()){
	@Override
    public boolean storeResponseOrNot(String url){
		if(url.endsWith(".js") || url.startsWith(".css")){
			return true;
		}else{
			return false;
		}
    }
};
```
2.使用htmlunit访问一个页面,修改页面dom,屏蔽反爬措施或者修剪页面,覆写modifyResponseOrNot修改链接符合条件,覆写modifyResponse修改内容
```Java
new Intercept(requests.getWetClient()){
	@Override
    public boolean modifyResponseOrNot(String url){
    	return url.matches(".+getCar.+");
    }
    @Override
    public WebResponse modifyResponse(WebResponse response) throws IOException{
    	String html=response.getContentAsString();
    	return makeResponse(response,html.replaceAll("[u4e00-u9fa5]", ""));
    }
};
```
3.使用htmlunit访问一个页面,页面可能加载了百度指数,对采集没有任何帮助的请求可以屏蔽掉,覆写rejectResponseOrNot,下面屏蔽google指数
```Java
HtmlUnitRequests requests=new HtmlUnitRequests();
	new Intercept(requests.getWetClient()){
		@Override
	    public boolean rejectResponseOrNot(String url){
	    	return url.contains("www.google-analytics.com/analytics");
	    }
};
```
* 使用java执行js,把js传入JavaScript对象中,然后调用invokeFunction函数
```Java
JavaScript js=new JavaScript("function add(a,b){return a+b;}");
String result=js.invokeFunction("add", 1,2);
```
* SpiderUserAgent类集成了常见的UA头
* htmlunit特殊方法介绍
```Java
executeJavaScript();// 执行js代码 例如：executeJavaScript("document.querySelector(\"a[href*='detail']\").click();"),点击属性href包含datail的a标签
getBufferedImage();// 获得页面的图片 例如:getBufferedImage("img[src*='captcha']"),属性src包含captcha的img标签对应的图片
close();// 关闭htmlunit浏览器
shutDownAllJavaScript();// 关闭所有的js任务
clickSection();// 点击某部分,里面为选择器或者xpath
waitForBackgroundJavaScript();// 等待后台js运行
utilShowUp();// 等待某元素出现
switchToWindow();// 执行点击后,浏览器会打开多个窗口，切换到符合对应正则的窗口
switchToWindowCloseOthers();// 关闭除符合正则所有窗口,并切换到对应窗口
setJavaScriptEnabled();// 是否执行js
```