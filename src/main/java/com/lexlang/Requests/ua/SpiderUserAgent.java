package com.lexlang.Requests.ua;

import java.util.Random;

/**
* 常见通用搜索引擎爬虫 ua头 
* @author lexlang
* @version 2018年7月20日 上午9:07:57
* 
*/
public class SpiderUserAgent {
	private final static String baiduSpider="Mozilla/5.0 (compatible; Baiduspider-render/2.0; +http://www.baidu.com/search/spider.html)";
	private final static String soSpider="Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)";
	private final static String googleSpider="Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)";
	private final static String bingSpider="Mozilla/5.0 (compatible; bingbot/2.0; +http://www.bing.com/bingbot.htm)";
	private final static String sosoSpider="Sosospider+(+http://help.soso.com/webspider.htm)";
	private final static String soGouSpider="Sogou web spider/4.0(+http://www.sogou.com/docs/help/webmasters.htm#07)";
	
	private final static String[] spiders={baiduSpider,soSpider,googleSpider,bingSpider,sosoSpider,soGouSpider};
	
	//国外爬虫
	private final static String speedySpider="Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US) Speedy Spider (http://www.entireweb.com/about/search_tech/speedy_spider/)";
	private final static String yandexSpider="Mozilla/5.0 (compatible; YandexBot/3.0; +http://yandex.com/bots)";
	private final static String easouSpider="Mozilla/5.0 (compatible; EasouSpider; +http://www.easou.com/search/spider.html)";
	
	//手机UA 苹果浏览器，安卓浏览器
	private final static String ISOSpider="Mozilla/5.0 (iPhone; CPU iPhone OS 8_4 like Mac OS X) AppleWebKit/600.1.4 (KHTML, like Gecko) Mobile/12H143";
	private final static String androidSpider="Mozilla/5.0 (Linux; U; Android 6.0.1; zh-CN; SM-C7000 Build/MMB29M) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/40.0.2214.89 UCBrowser/11.6.2.948 Mobile Safari/537.36";
	
	//CHROME
	private final static String chromeSpider="Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.116 Safari/537.36";
	private final static String firefoxSpider="Mozilla/5.0 (Windows NT 10.0; WOW64; rv:61.0) Gecko/20100101 Firefox/61.0";
	private final static String ieSpider="Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; WOW64; Trident/7.0; SLCC2; .NET CLR 2.0.50727; .NET4.0C; .NET4.0E)";
	
	
	public static SpiderUserAgent getIeSpider(){
		return new SpiderUserAgent(ieSpider);
	}
	
	public static SpiderUserAgent getFirefoxSpider(){
		return new SpiderUserAgent(firefoxSpider);
	}
	
	
	public static SpiderUserAgent getChromeSpider(){
		return new SpiderUserAgent(chromeSpider);
	}
	
	public static SpiderUserAgent getISOSpider(){
		return new SpiderUserAgent(ISOSpider);
	}
	
	public static SpiderUserAgent getAndroidSpider(){
		return new SpiderUserAgent(androidSpider);
	}

	
	/**
	 * 百度UA
	 * @return
	 */
	public static SpiderUserAgent getBaiDuSpider(){
		return new SpiderUserAgent(baiduSpider);
	}
	
	/**
	 * 360UA
	 * @return
	 */
	public static SpiderUserAgent getSoSpider(){
		return new SpiderUserAgent(soSpider);
	}
	
	/**
	 * 必应UA
	 * @return
	 */
	public static SpiderUserAgent getBingSpider(){
		return new SpiderUserAgent(bingSpider);
	}
	
	/**
	 * google  UA
	 * @return
	 */
	public static SpiderUserAgent getGoogleSpider(){
		return new SpiderUserAgent(googleSpider);
	}
	/**
	 * 腾讯 搜搜 UA
	 * @return
	 */
	public static SpiderUserAgent getSosoSpider(){
		return new SpiderUserAgent(sosoSpider);
	}
	/**
	 * 搜狗 UA
	 * @return
	 */
	public static SpiderUserAgent getSoGouSpider(){
		return new SpiderUserAgent(soGouSpider);
	}
	
	/**
	 * 随机UA
	 * @return
	 */
	public static SpiderUserAgent getRandomSpider(){
		Random rand=new Random();
		return new SpiderUserAgent(spiders[rand.nextInt(spiders.length)]);
	}
	
	
	private String userAgent;
	private SpiderUserAgent(String userAgent){
		this.userAgent=userAgent;
	}
	
	public String getUserAgent(){
		return this.userAgent;
	}
}
