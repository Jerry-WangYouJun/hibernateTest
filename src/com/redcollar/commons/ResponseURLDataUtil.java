package com.redcollar.commons;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import net.sf.json.JSONObject;

public class ResponseURLDataUtil {
	public static JSONObject getLmbJsonData(String url) throws UnsupportedEncodingException {
		String jsonString;
		JSONObject jsonObject  = null;
		try {
			jsonString = ResponseURLDataUtil.getReturnDataWithCookie(url);
			 jsonObject  = JSONObject.fromObject(jsonString);  
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}
	
	/**
	 * 
	 * @param timeStart 开始时间
	 * @param timeEnd   结束时间
	 * @param psize  数据总数
	 * @param p    当前页码
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getUrlMlb(String timeStart ,String timeEnd ,int psize , int p) throws UnsupportedEncodingException {
		String url = "https://www.m-m10086.com/api/NewReport/RenewalsOrder?"
				+ "holdId=12896&p="+ p +"&psize="+ psize 
				+ "&payee=&source=&PayState=1,2&RenewalsState=&"
				+ "packageType=&oldPackageType=&timeType=4"
				+ "&stime=" + URLEncoder.encode(timeStart,"utf-8")
				+ "&etime=" +URLEncoder.encode(timeEnd,"utf-8")
				+ "&order=&id=&minamonth=&access=&simState=-1&"
				+ "commercialTenant=&batchType=iccid&batchValues=&groupHoldId=0";
		
		return url;
	}
	
	public static String getCookie(String token) {
		 String cookie = "aliyungf_tc=AQAAAA6GsVRs/QIAIIfHKofNPgd8nuCB; "
		 		+ "ASP.NET_SessionId=yae04reoticdwhqecaeqzlwe;"
		 		+ " RememberCookie=UserName=青岛丰宁贸易新&UserPwd=8989123; "
		 		+ "UserCookie=UserID=544939&UserName=%e9%9d%92%e5%b2%9b%e4%b8%b0%e5%ae%81%e8%b4%b8%e6%98%93%e6%96%b0"
		 		+ "&UserType=1&HoldID=12896&HoldName=%e9%9d%92%e5%b2%9b%e4%b8%b0%e5%ae%81%e8%b4%b8%e6%98%93%e6%96%b0"
		 		+ "&HoldLevel=4&HoldType=6&Token="
		 		+ token
		 		+ "&LoginFromType=0&OEMClient=";
		 
		 return cookie ;
	}
	/**
	 * 执行相应的url获取
	 * @param urlString
	 * @return
	 * @throws UnsupportedEncodingException
	 */
    public static String getReturnData(String urlString) throws UnsupportedEncodingException {  
        String res = "";   
        try {   
            URL url = new URL(urlString);  
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection)url.openConnection();  
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            conn.setDoOutput(true);  
            conn.setRequestMethod("GET");   
            java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream(),"UTF-8"));  
            String line;  
            while ((line = in.readLine()) != null) {  
                res += line;  
            }  
            in.close();  
        } catch (Exception e) {  
            System.out.println("error in wapaction,and e is " + e.getMessage());  
        }  
        	//System.out.println(res);  	
        return res;  
    }  
    
    public static String getReturnDataWithCookie(String urlString ) throws UnsupportedEncodingException {  
        String res = "";   
        try {   
            URL url = new URL(urlString);  
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection)url.openConnection();  
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            conn.setDoOutput(true);  
            conn.setRequestProperty("cookie", getCookie(getToken("青岛丰宁贸易新", "8989123")));
            conn.setRequestMethod("GET");   
            java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream(),"UTF-8"));  
            String line;  
            while ((line = in.readLine()) != null) {  
                res += line;  
            }  
            in.close();  
        } catch (Exception e) {  
            System.out.println("error in wapaction,and e is " + e.getMessage());  
        }  
        	//System.out.println(res);  	
        return res;  
    } 
    
    
    /**
	 * 模拟登陆 ， 获取token生成cookie
	 * 
	 * @param userName
	 *            用户名
	 * @param pwd
	 *            密码
	 * 
	 * **/
	public static String getToken(String userName, String pwd) throws Exception {
		// 第一次请求
		Connection con = Jsoup
				.connect("https://www.m-m10086.com/User/LoginYD");// 获取连接
		con.header("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");// 配置模拟浏览器
		Response rs = con.execute();// 获取响应
		Document d1 = Jsoup.parse(rs.body());// 转换为Dom树
		List<Element> et = d1.select("form");// 获取form表单，可以通过查看页面源码代码得知
		// 获取，cooking和表单属性，下面map存放post时的数据
		Map<String, String> datas = new HashMap<>();
		for (Element e : et.get(0).getAllElements()) {
			if (e.attr("name").equals("userName")) {
				e.attr("value", userName);// 设置用户名
			}
			if (e.attr("name").equals("userPwd")) {
				e.attr("value", pwd); // 设置用户密码
			}
			if (e.attr("name").length() > 0) {// 排除空值表单属性
				datas.put(e.attr("name"), e.attr("value"));
			}
		}
		/**
		 * 第二次请求，post表单数据，以及cookie信息
		 * 
		 * **/
		Connection con2 = Jsoup
				.connect("https://www.m-m10086.com/User/LoginYD");
		con2.header("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64; rv:29.0) Gecko/20100101 Firefox/29.0");
		// 设置cookie和post上面的map数据
		Response login = con2.ignoreContentType(true).method(Method.POST)
				.data(datas).cookies(rs.cookies()).execute();
		// 打印，登陆成功后的信息
		//System.out.println(login.body());
		String token = "";
		// 登陆成功后的cookie信息，可以保存到本地，以后登陆时，只需一次登陆即可
		Map<String, String> map = login.cookies();
		for (String s : map.keySet()) {
			String[] cookieArr = map.get(s).split("&");
			for(String cookie : cookieArr) {
				  if(cookie.startsWith("Token")) {
					        token = cookie.substring(6);
				  }
			}
 		}
		System.out.println(token);
		return token ;
	}
}
