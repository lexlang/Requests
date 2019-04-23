package com.lexlang.Requests;

import java.io.IOException;
import java.net.URISyntaxException;

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
    public static void main( String[] args ) throws IOException, URISyntaxException
    {
    	HttpClientRequests requests=new HttpClientRequests();
    	Response response = requests.get("http://www.bejson.com/");
    	System.out.println(response.getContent());
    }
}
