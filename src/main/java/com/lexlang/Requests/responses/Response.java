package com.lexlang.Requests.responses;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.lexlang.Requests.util.UrlUtils;

/**
* @author lexlang
* @version 2019年3月12日 上午11:34:12
* 
*/
public class Response {
	private List<NameValuePair> headers=new ArrayList<NameValuePair>();
	private ByteArrayOutputStream baos;
	private String currentUrl;
	private String decode;
	
	//要不要添加
	private int statusCode;
	
	public Response(List<NameValuePair> headers, InputStream inputStream, String decode,String currentUrl,int statusCode) {
		this.headers=headers;
		try {this.baos = getByteStream(inputStream);} catch (IOException e) {System.out.println("流转换发生错误");}
		this.currentUrl=currentUrl;
		this.decode=decode;
		this.statusCode=statusCode;
	}
	
	

	/**
	 * 复制流    new ByteArrayInputStream(baos.toByteArray());
	 * @param input
	 * @return
	 * @throws IOException
	 */
	private ByteArrayOutputStream getByteStream(InputStream input) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();		 
		byte[] buffer = new byte[1024];
		int len;
		while ((len = input.read(buffer)) > -1 ) {
		    baos.write(buffer, 0, len);
		}
		baos.flush();
		return baos;
	}
	
	private String inputStream2String(InputStream inputStream,String decode) throws IOException{
        // 定义 BufferedReader输入流来读取URL的响应，设置utf8防止中文乱码
        String result = "";
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, decode));

        String line;
        while ((line = in.readLine()) != null) {
            result += line;
        }
        if (in != null) {
            in.close();
        }
        return result;
	}
	
	/**
	 * 返回状态码
	 * @return
	 */
	public int getStatusCode(){
		return statusCode;
	}
	
	/**
	 * 获得response流
	 * @return
	 */
	public InputStream getInputStream(){
		return new ByteArrayInputStream(baos.toByteArray());
	}
	
	/**
	 * 获得图片对象
	 * @return
	 * @throws IOException
	 */
	public BufferedImage getImage() throws IOException{
		return ImageIO.read(new ByteArrayInputStream(baos.toByteArray()));
	}
	
	/**
	 * 获得response文本
	 * @return
	 */
	public String getContent(){
		try {
			if(decode!=null){
				return inputStream2String(new ByteArrayInputStream(baos.toByteArray()), decode);
			}else{
				String[] decs={"utf-8","gbk","gb2312"};
				for(String dec:decs){
					String con=inputStream2String(new ByteArrayInputStream(baos.toByteArray()), dec);
					if(! isMessyCode(con)){
						return con;
					}
				}
				return inputStream2String(new ByteArrayInputStream(baos.toByteArray()), "utf-8");
			}
		} catch (IOException e) {System.out.println("流转字符串发生错误");}
		return "";
	}
	
	/**
	 * 获得json map对象
	 * @return
	 */
	public JSONObject getJsonObject(){
		return JSONObject.parseObject(getContent());
	}
	
	/**
	 * 获得json list对象
	 * @return
	 */
	public JSONArray getJsonArray(){
		return JSONArray.parseArray(getContent());
	}
	
	/**
	 * 补全链接的文本
	 * @return
	 */
	public String getFixHrefContent(){
		return UrlUtils.fixAllRelativeHrefs(getContent(), currentUrl);
	}
	
    /**
     * 获取页面的document
     * @return
     */
    public Document getFixHrefDocument(){
    	return Jsoup.parse(getFixHrefContent());
    }
	
	/**
	 * 获得消息头
	 * @return
	 */
    public List<NameValuePair> getHeaders(){
    	return this.headers;
    }
    
    /**
     * 
     * @param key
     * @return
     */
    public String getHeaderValue(String key){
    	List<NameValuePair> list = getHeaders();
    	for(int i=0;i<list.size();i++){
    		NameValuePair keyValue = list.get(i);
    		if(keyValue.getName().toLowerCase().equals(key.toLowerCase())){
    			return keyValue.getValue();
    		}
    	}
    	return null;
    }
    
    /**
     * 获取页面的document
     * @return
     */
    public Document getDocument(){
    	return Jsoup.parse(UrlUtils.fixAllRelativeHrefs(getContent().replace("&nbsp;", " "),currentUrl));
    }
    
    /**
     * 获得最终的URL
     * @return
     */
    public String getCurrentUrl(){
    	return currentUrl;
    }
    
    /**
     * \uFFFF编码转程字符串
     * @return
     * @throws UnsupportedEncodingException 
     * @throws Exception 
     */
    public String getUnicode2String() throws UnsupportedEncodingException{
    	String result=getContent();
        Matcher m = Pattern.compile("\\\\u\\w{4}").matcher(getContent());
        while(m.find()){
        	result=result.replace(m.group(),""+(char) Integer.parseInt(m.group().replace("\\u", ""), 16));
        }
        return turn16(result);
    }
    
    /**
     * excel word pdf 一切流文件写到本地
     * @param destination 输出的路径
     * @throws IOException
     */
	public void writeToLocal(String destination) throws IOException {  
		InputStream inputStream=new ByteArrayInputStream(baos.toByteArray());
	    int index;  
	    byte[] bytes = new byte[1024];  
	    FileOutputStream downloadFile = new FileOutputStream(destination);  
	    while ((index = inputStream.read(bytes)) != -1) {  
	        downloadFile.write(bytes, 0, index);  
	        downloadFile.flush(); 
	    }  
	    inputStream.close();
	    downloadFile.close();   
	}  
	
	public ByteArrayOutputStream getBaos(){
		return baos;
	}
    
    
    /** 
     * 判断字符串是否是乱码 
     * 
     * @param strName 字符串 
     * @return 是否是乱码 
     */  
    private static boolean isMessyCode(String strName) {  
        Pattern p = Pattern.compile("\\s*|t*|r*|n*");  
        Matcher m = p.matcher(strName);  
        String after = m.replaceAll("");  
        String temp = after.replaceAll("\\p{P}", "");  
        char[] ch = temp.trim().toCharArray();  
        float chLength = ch.length;  
        float count = 0;  
        for (int i = 0; i < ch.length; i++) {  
            char c = ch[i];  
            if (!Character.isLetterOrDigit(c)) {  
                if (!isChinese(c)) {  
                    count = count + 1;  
                }  
            }  
        }  
        float result = count / chLength;  
        if (result > 0.2) {  
            return true;  
        } else {  
            return false;  
        }  
   
    }  
    
    private static boolean isChinese(char c) {  
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);  
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS  
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS  
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A  
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION  
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION  
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {  
            return true;  
        }  
        return false;  
    } 
    
    /**
     * 十六进制转成文本
     * @param content
     * @return
     * @throws UnsupportedEncodingException 
     * @throws Exception
     */
	public String turn16(String content) throws UnsupportedEncodingException {
		Matcher m = Pattern.compile("\\\\x.{2}").matcher(content);
		while(m.find()){
			String xx=m.group();
			content=content.replace(xx, fromHexString(xx.replace("\\x", "")));
		}
		return content;
	}

	public  String fromHexString(String hexString) throws UnsupportedEncodingException{
	    // 用于接收转换结果
	    String result = "";
	    // 转大写
	    hexString = hexString.toUpperCase();
	    // 16进制字符
	    String hexDigital = "0123456789ABCDEF";
	    // 将16进制字符串转换成char数组
	    char[] hexs = hexString.toCharArray();
	    // 能被16整除，肯定可以被2整除
	    byte[] bytes = new byte[hexString.length() / 2];
	    int n;
	     
	    for (int i = 0; i < bytes.length; i++) {
	        n = hexDigital.indexOf(hexs[2 * i]) * 16 + hexDigital.indexOf(hexs[2 * i + 1]);
	        bytes[i] = (byte) (n & 0xff);
	    }
	    return new String(bytes, "UTF-8");
	}
    
}
