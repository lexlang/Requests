package com.lexlang.Requests;

import java.io.IOException;
import java.net.URISyntaxException;

import com.lexlang.Requests.proxy.ProxyPara;
import com.lexlang.Requests.requests.HtmlUnitRequests;
import com.lexlang.Requests.requests.HttpClientRequests;
import com.lexlang.Requests.requests.JsoupRequests;
import com.lexlang.Requests.requests.URLRequests;
import com.lexlang.Requests.responses.Response;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception
    {	
    	URLRequests requests=new URLRequests(new ProxyPara("114.232.94.27",14831),60000);
    	Response response = requests.get("http://139.224.63.232:5001/getLocalIp");
    	System.out.println(response.getContent());
    }
}
