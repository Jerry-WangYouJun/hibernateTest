package com.redcollar.commons;

import java.io.UnsupportedEncodingException;
import java.net.URL;

public class PropertyCommons {
	 
	public String appid = "SODE1NH" ;
	public String httpClientPwd = "7FNMY2";
	public String ebid = "0001000000008";
	String transid = appid + "20170228121212" + "00000001"   ;
	String token = Encrypt.SHA256(appid + httpClientPwd + ebid);
	String BASE_URL = "http://183.230.96.66:8087/v2/";
	String COLUMNS_URL =  "?appid=" + appid + "&transid=" + transid + "&ebid=" + ebid + "&token=" + token ;
	 public static void main(String[] args) {
		 PropertyCommons p = new PropertyCommons();
		 try {
			 ResponseURLDataUtil.getReturnData(p.batchSmsusedSyDate());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	 
	 /**
	  * 用户当月 GPRSGPRS 查
	  * http://183.230.96.66:8087/v2/gprsusedinfosingle?appid=xxx&transid=xxx&ebid=xxx&token=xxx&msisdn=xxx
	  * 返回参数说明：
			参数                         是否必须   默认值      含义
			total_gprs 是               无             用户当月使用的总的 GPRS 流量（单位：KB）
	  * 
	  * 
	  */
	 public String gprsUsedInfoSingle(){
		  String url = BASE_URL + "/gprsusedinfosingle" + COLUMNS_URL + "&msisdn=460040411808795"  ;
		  System.out.println(url);
		  return url ;
	 }
	 
	 /**
	  *  短信批量查询
	  *  http://183.230.96.66:8087/v2/batchgprsusedbydate?appid=xxx&ebid=xxx&transid=xxx
			&token=xxx&query_date=xxx&msisdns=xxx_xxx_xxx
	  *   参数：query_date 查询时间（例如：系统当前日期 20150421，7日内，即 20150414-20150420 有效）
	  *   
	  *   返回参数说明：
			参数		 是否必须 		默认值 		含义
			GPRS 	是 			无 			当日使用的 GPRS 流量总量（单位：KB）
			MSISDN 	是 			无 			物联网专网卡号
	  *   
	  * @return
	  */
	public String  batchSmsusedSyDate(){
		String url = BASE_URL + "/batchsmsusedbydate?appid=" + appid + 
				"&ebid=" + ebid + "&transid=" + transid + "&token=" + token +
				"&query_date=" + "20170309" + "&msisdns=460040411808795"  ;
		System.out.println(url);
		return url;
	}
	 
	 /**
	  * 流量信息批量查询
	  *
	  *	
	  * /batchgprsusedbydate?appid=xxx&ebid=xxx&transid=xxx&token=xxx&query_date=xxx&msisdns=xxx_xxx_xxx
	  * msisdns 是 无 所查询的专网号码，多个号码用下划线分隔。
	  *		例如：xxxx xxxx xxxx
	  *		query_date 是 无 查询时间（例如：系统当前日期 20150421，7
	  *		日内，即 20150414-20150420 有效）
	  *返回参数说明：
		参数			 是否必须		 默认值 		含义
		total_gprs	 是 			无 			用户当月使用的总的 GPRS 流量（单位：KB）
	  * @return
	  */
	 public String batchGprsUsedByDate(){
		 String url = BASE_URL + "/batchgprsusedbydate" + COLUMNS_URL + 
				 "&query_date=" + "20170309" + "&msisdns=460040411808795"  ;
	      return url;	 
	 }
	 
	 
	 /**
	  * balancerealsingle－用户余额信息实时查询
	  * http://183.230.96.66:8087/v2/balancerealsingle?appid=xxxx&ebid=xxxxxxxxxx&transid=xxxxxxxx&token=xxxxxxxxx&msisdn=xxxxxxxx
	  * 返回参数说明：
			参数		 是否必须	 默认值	 含义
			balance 是 		无		 用户余额（单位：元)
	  * @return
	  */
	 public String balanceRealSingle(){
		  String url = BASE_URL + "/balancerealsingle" + COLUMNS_URL + "&msisdn=460040411808795"  ;
				 
		  return url;
	 }
	 
	 /**
	  * smsusedinfosingle－用户当月短信查询
	  * 
	  * http://183.230.96.66:8087/v2/smsusedinfosingle?appid=xxx&transid=xxx&ebid=xxx&token=xxx&msisdn=xxx
	  *	返回参数说明：
		参数	 是否必须	 默认值	 含义
		sms 是		 无		 短信条数（单位：条）
	  * @return
	  */
	 public String smsUsedInfoSingle(){
		 String url = BASE_URL + "/smsusedinfosingle" + COLUMNS_URL + "&msisdn=460040411808795"  ;
		  return url;
	 }
	 
	 /**
	  * groupuserinfo－集团用户数查询
	  * http://183.230.96.66:8087/v2/groupuserinfo?appid=xxx&ebid=xxx&transid=xxx&token=xxx&msisdn=xxx&query_date=xxx
	  *	返回参数说明：
		参数	   	 是否必须		 默认值	 含义
		total 	是 			无 		用户总数
	  * @return
	  */
	 public String groupUserInfo(){
		 String url = BASE_URL + "/groupuserinfo" + COLUMNS_URL + "&query_date=" + "20170309" + "&msisdn=460040411808795"  ;
		  return url;
	 }
	 
	 
	 /**
	  * smsusedbydate－用户短信使用查询
	  * http://183.230.96.66:8087/v2/smsusedbydate?appid=xxx&ebid=xxx&transid=xxx&token=xxx&msisdn=xxx&query_date=xxx
	  * query_date  所查询日期（服务端时间为准）
	  *	返回参数说明：
		参数	   	 是否必须		 默认值	 含义
		sms 	是			 无 		用户当月短信条数（条）

	  * @return
	  */
	 public String smsUsedByDate(){
		 String url = BASE_URL + "/smsusedbydate" + COLUMNS_URL + "&query_date=" + "20170309" + "&msisdn=460040411808795"  ;
		  return url;
	 }
	 
	 /**
	  * gprsrealtimeinfo－套餐内 GPRS 流量使用情况实
	  * 
	  * http://183.230.96.66:8087/v2/gprsrealtimeinfo?appid=xxx&ebid=xxx&transid=xxx&token=xxx&msisdn=xxx
	  *	返回参数说明：
		参数 			是否必须 	默认值 	含义
		gprs 		是 		 无		 套餐内 GPRS 流量使用情况查询结果
		prodid 		是		 无 		套餐 ID
		prodname 	是	             无		 套餐名称
		total 		是 		无 		套餐总量（单位：MB）
		used 		是 		无 		套餐使用量（单位：KB）
		left 		是 		无 		套餐剩余量（单位：KB）

	  * @return
	  */
	 public String gprsRealTimeInfo(){
		 String url = BASE_URL + "/gprsrealtimeinfo" + COLUMNS_URL + "&msisdn=460040411808795"  ;
		  return url;
	 }
	 
	 /**
	  * gprsrealsingle  API2001－在线信息实时查询
	  * 
	  * 示例：http://183.230.96.66:8087/v2/gprsrealsingle?appid=xxx&transid=xxx&ebid=xxx&token=xxx&msisdn=xxx
	  *	返回参数说明：
			参数			 是否必须	 默认值	 含义
			GPRSSTATUS 	是 		无 		GPRS 在线状态： 00-离线 01-在线
			IP			是		 无 		用户 IP 地址
			APN 		是 		无 		用户接入的 APN
			RAT 		是		 无		1（返回 1 指 3G） 2（返回 2 指 2G） 6（返回 6 指 4G 及其他）
	  * @return
	  */
	 public String gprsRealSingle(){
		 String url = BASE_URL + "/gprsrealsingle" + COLUMNS_URL + "&msisdn=460040411808795"  ;
		  return url;
	 }
	 
	 /**
	  * CMIOT_API2002－用户状态信息实时查询   userstatusrealsingle
	  * 
	  * 示例：http://183.230.96.66:8087/v2/userstatusrealsingle?appid=xxx&transid=xxx&ebid=xxx&token=xxx&msisdn=xxx
	  *	返回参数说明：
			参数			 是否必须	 默认值	 含义
			STATUS 		是		 否		00-正常；01-单向停机；02-停机；03-预销号；04-销号；05-过户；06-休眠；07-待激；99-号码不存在
	  * @return
	  */
	 public String userStatusRealSingle(){
		 String url = BASE_URL + "/userstatusrealsingle" + COLUMNS_URL + "&msisdn=460040411808795"  ;
		  return url;
	 }
	 
	 /**
	  * CMIOT_API2003－码号信息查询  cardinfo  
			根据 ICCID、IMSI、MSISDN 任意 1 个查询剩余 2 个的能力

	  *	示例：http://183.230.96.66:8087/v2/cardinfo?appid=xxx&transid=xxx&ebid=xxx&token=xxx&card_info=xxx&type=xxx	 
	  * card_info 是 无 所查询用户的 msisdn、imsi 或 iccid
		type 是 无  0—msisdn 1—imsi 2—iccid
	  *	返回参数说明：
			参数			 是否必须	 默认值	 含义
			IMSI 		是 		否 		专网用户的 IMSI
			MSISDN 		是		 否 		专网号码
			ICCID 		是		 否 		专网用户的 ICCID
	  * @return
	  */
	 public String cardInfo(){
		String url = BASE_URL + "/cardinfo?appid=" + appid + 
				"&transid=" + transid + "&ebid=" + ebid + "&token=" + token + "&card_info=460040411808795&type=msisdn"  ;
		System.out.println(url);
		return url ;
	}
	
	 /**
	  * CMIOT_API2008－开关机信息实时查询  onandoffrealsingle
	  *	提供单个 MSISDN 号卡的开关机状态实时查询功能
	  * 
	  * 示例：http://183.230.96.66:8087/v2/onandoffrealsingle?appid=xxx&transid=xxx&ebid=xxx&token=xxx&msisdn=xxx
	  *	返回参数说明：
			参数			 是否必须	 默认值	 含义
			STATUS 		是		 否		0-关机 1-开机
	  * @return
	  */
	 public String onAndOffRealSingle(){
		 String url = BASE_URL + "/onandoffrealsingle" + COLUMNS_URL + "&msisdn=460040411808795"  ;
		  return url;
	 }
	 
	 /**
	  * CMIOT_API2029－物联卡多 APN 信息实时查询/multiapninfo
		 为集团客户提供查询某卡号的多 APN 信息实时查询
	  * 
	  * 示例：http://183.230.96.66:8087/v2/multiapninfo?appid=xxx&ebid=xxx&transid=xxx&token=xxx&msisdn=xxx
	  *	返回参数说明：
			参数			 是否必须	 默认值	 含义
			apns 		是		 无 		多 APN 查询结果
			ip 			是 		无 		终端 IP
			apn 		是		 无 		接入点
			rat			是 		无 		接入方式 1—表示 3G  2—表示 2G  6—表示 4G 及其他
			gprsStatus  是		 无 		在线状态 00--表示离线 01--表示在线
 			timestamp   是 		无		 状态变更时的时间(毫秒时间戳)	
	  * @return
	  */
	 public String multiAPNInfo(){
		 String url = BASE_URL + "/multiapninfo" + COLUMNS_URL + "&msisdn=460040411808795"  ;
		  return url;
	 }
	 
	 /**
	  * CMIOT_API4001－短信状态重置/smsstatusreset
		提供集团客户主动刷新自己用户的短信状态
	  * 示例：http://183.230.96.66:8087/v2/smsstatusreset?appid=xxx&transid=xxx&ebid=xxx&token=xxx&msisdn=xxx
	  *	返回参数说明：
			参数			 是否必须	 默认值	 含义
			status 		是 		否 	0-重置成功100-短信重置刷新失败
	  * @return
	  */
	 public String smsStatusReset(){
		 String url = BASE_URL + "/smsstatusreset" + COLUMNS_URL + "&msisdn=460040411808795"  ;
		  return url;
	 }
	
    public String getReturnData(String urlString) throws UnsupportedEncodingException {  
        String res = "";   
        try {   
            URL url = new URL(urlString);  
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection)url.openConnection();  
            conn.setDoOutput(true);  
            conn.setRequestMethod("POST");  
            java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream(),"UTF-8"));  
            String line;  
            while ((line = in.readLine()) != null) {  
                res += line;  
            }  
            in.close();  
        } catch (Exception e) {  
            System.out.println("error in wapaction,and e is " + e.getMessage());  
        }  
        	System.out.println(res);  	
        return res;  
    }  
}
