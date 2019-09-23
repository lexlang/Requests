package com.tianluo.CrawlUtils.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* 提取域名 
* @author lexlang
* @version 2018年7月24日 下午14:15:49
* 
*/
public class Domain {
	static String[] arr={"ad","ae","af","ag","ai","al","am","ao","aq","ar","as","at","au","aw","ax","az","ba","bb","bd","be","bf","bg","bh","bi","bj","bl","bm","bn","bo","bq","br","bs","bt","bv","bw","by","bz","ca","cc","cf","ch","cl","cm","co","cr","cu","cv","cx","cy","cz","de","dj","dk","dm","do","dz","ec","ee","eg","eh","er","es","fi","fj","fk","fm","fo","fr","ga","gd","ge","gf","gh","gi","gl","gn","gp","gq","gr","gs","gt","gu","gw","gy","hk","hm","hn","hr","ht","hu","id","ie","il","im","in","io","iq","ir","is","it","je","jm","jo","jp","kh","ki","km","kw","ky","lb","li","lk","lr","ls","lt","lu","lv","ly","ma","mc","md","me","mf","mg","mh","mk","ml","mm","mo","mq","mr","ms","mt","mv","mw","mx","my","na","ne","nf","ng","ni","nl","no","np","nr","om","pa","pe","pf","pg","ph","pk","pl","pn","pr","ps","pw","py","qa","re","ro","rs","ru","rw","sb","sc","sd","se","sg","si","sj","sk","sl","sm","sn","so","sr","ss","st","sv","sy","sz","tc","td","tg","th","tk","tl","tn","to","tr","tv","tz","ua","ug","us","uy","va","ve","vg","vi","vn","wf","ws","ye","yt","za","zm","zw","cn","cg","cd","mz","gg","gm","mp","et","nc","vu","tf","nu","um","ck","gb","tt","vc","tw","nz","sa","la","kp","kr","pt","kg","kz","tj","tm","uz","kn","pm","sh","lc","mu","ci","ke","mn"};
	static HashSet<String> guojia=new HashSet<String>();
	
	static String[] label={"com","edu","gov","net","org","ac","info","name","pro","cc","int"};
	static HashSet<String> topDomain=new HashSet<String>();
	static {
		for(int i=0;i<arr.length;i++){guojia.add(arr[i]);}
		for(int i=0;i<label.length;i++){topDomain.add(label[i]);}
	}
	
	public static String getDomain(String url){
		List<String> lise=new ArrayList<String>();
		
		if(url.startsWith("http")){
	        Matcher matcher = Pattern.compile("(?<=//).+?(?=(/|$|:|#))").matcher(url);  
	        if(matcher.find()){
	        	String[] arr=matcher.group().split("\\.");
	        	if(guojia.contains(arr[arr.length-1])){
	        		if(topDomain.contains(arr[arr.length-2])){
	        			return arr[arr.length-3]+"."+arr[arr.length-2]+"."+arr[arr.length-1];
	        		}else{
	        			return arr[arr.length-2]+"."+arr[arr.length-1];
	        		}
	        	}else{
	        		return arr[arr.length-2]+"."+arr[arr.length-1];
	        	}
	        }
		}
		return url;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//http://zhibimo.com/books/xiaolai/wo-ye-you-hua-yao-shuo--pu-tong-ren-de-jiang-yan-ji-neng
		System.out.println(getDomain("http://zhibimo.com/books/xiaolai/wo-ye-you-hua-yao-shuo--pu-tong-ren-de-jiang-yan-ji-neng"));
	}

}
