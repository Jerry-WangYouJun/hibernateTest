package com.poiexcel.vo;

import java.util.Date;


//将Excel每一行数值转换为对象
public class InfoVo {
	
	private String code;
	private String name;
	private String date;
	private String money;
	
	private String  IMSI ; 
	private String ICCID ;
	private String userStatus  ; 
	private String cardStatus ;
	private String gprsUsed;
	private String messageUsed;
	private Date openDate;
	private String withMessageService ;
	private String withGPRSService ;
	private String packageType ;
	private String monthTotalStream;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getIMSI() {
		return IMSI;
	}
	public void setIMSI(String iMSI) {
		IMSI = iMSI;
	}
	public String getICCID() {
		return ICCID;
	}
	public void setICCID(String iCCID) {
		ICCID = iCCID;
	}
	public String getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}
	public String getCardStatus() {
		return cardStatus;
	}
	public void setCardStatus(String cardStatus) {
		this.cardStatus = cardStatus;
	}
	public String getGprsUsed() {
		return gprsUsed;
	}
	public void setGprsUsed(String gprsUsed) {
		this.gprsUsed = gprsUsed;
	}
	public String getMessageUsed() {
		return messageUsed;
	}
	public void setMessageUsed(String messageUsed) {
		this.messageUsed = messageUsed;
	}
	public Date getOpenDate() {
		return openDate;
	}
	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}
	public String getWithMessageService() {
		return withMessageService;
	}
	public void setWithMessageService(String withMessageService) {
		this.withMessageService = withMessageService;
	}
	public String getWithGPRSService() {
		return withGPRSService;
	}
	public void setWithGPRSService(String withGPRSService) {
		this.withGPRSService = withGPRSService;
	}
	public String getPackageType() {
		return packageType;
	}
	public void setPackageType(String packageType) {
		this.packageType = packageType;
	}
	public String getMonthTotalStream() {
		return monthTotalStream;
	}
	public void setMonthTotalStream(String monthTotalStream) {
		this.monthTotalStream = monthTotalStream;
	}
	
	
}
