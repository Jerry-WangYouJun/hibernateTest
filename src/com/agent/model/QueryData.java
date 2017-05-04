package com.agent.model;


public class QueryData {
	//代理商套餐类型
	  private String type ;
	 //代理商iccid开始卡号
	  private String iccidStart ; 
	  //代理商iccid结束卡号
	  private String iccidEnd ;
	  //
	  private  String agentid;
	  //代理商名称
	  private String agentName ;
	  //代理商编号
	  private String agentCode;
	  //用户名
	  private String userNo ; 
	  //用户名称
	  private String userName ;
	  //角色
	  private String roleId;
	  private String userId ;
	  
	  private String dateStart ; 
	  private String dateEnd ;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIccidStart() {
		return iccidStart;
	}
	public void setIccidStart(String iccidStart) {
		this.iccidStart = iccidStart;
	}
	public String getIccidEnd() {
		return iccidEnd;
	}
	public void setIccidEnd(String iccidEnd) {
		this.iccidEnd = iccidEnd;
	}
	public String getAgentid() {
		return agentid;
	}
	public void setAgentid(String agentid) {
		this.agentid = agentid;
	}
	public String getUserNo() {
		return userNo;
	}
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public String getAgentCode() {
		return agentCode;
	}
	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}
	public String getDateStart() {
		return dateStart;
	}
	public void setDateStart(String dateStart) {
		this.dateStart = dateStart;
	}
	public String getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
	} 
	
}
