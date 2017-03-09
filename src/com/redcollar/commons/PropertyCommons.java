package com.redcollar.commons;

public class PropertyCommons {
	 
	public String appid = "SODE1NH" ;
	public String httpClientPwd = "7FNMY2";
	public String ebid = "0001000000008";
	String transid = appid + "20170228121212" + "00000001"   ;
	 public static void main(String[] args) {
		 PropertyCommons p = new PropertyCommons();
		 p.batchsmsusedbydate();
	}
	 
	 public void userstatusrealsingle(){
		Encrypt e = new Encrypt();
		String enCode = e.SHA256(appid + httpClientPwd + ebid);
		String url = "http://183.230.96.66:8087/v2/gprsrealsingle?appid=" + appid + 
				"&transid=" + transid + "&ebid=" + ebid + "&token=" + enCode + "&msisdn=460040411808795"  ;
		System.out.println(url);
	}
	
	public void  batchsmsusedbydate(){
		Encrypt e = new Encrypt();
		String enCode = e.SHA256(appid + httpClientPwd + ebid);
		String url = "http://183.230.96.66:8087/v2/batchsmsusedbydate?appid=" + appid + 
				"&ebid=" + ebid + "&transid=" + transid + "&token=" + enCode +
				"&query_date=" + "20170309" + "&msisdns=460040411808795"  ;
		System.out.println(url);
	}
}
