package com.agent.model;

import java.util.List;

import com.poiexcel.vo.InfoVo;

public class Agent {
	  private Integer id ; 
	  private String code ; 
	  private String name ; 
	  private Double cost ; 
	  private Double renew;
	  private String type ;
	  private String creater ; 
	  private String createdate ;
	  private String iccid;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
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
	public Double getCost() {
		return cost;
	}
	public void setCost(Double cost) {
		this.cost = cost;
	}
	public Double getRenew() {
		return renew;
	}
	public void setRenew(Double renew) {
		this.renew = renew;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public String getCreatedate() {
		return createdate;
	}
	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}
	public String getIccid() {
		return iccid;
	}
	public void setIccid(String iccid) {
		this.iccid = iccid;
	}
}
