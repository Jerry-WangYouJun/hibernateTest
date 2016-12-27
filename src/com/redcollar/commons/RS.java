package com.redcollar.commons;

/**
* <p>Title: RS.java</p>
* <p>Description: </p>
* <p>Copyright: Copyright (c) 2013</p>
* <p>Company: RCOLLAR</p>
* @author <a href="mailto:fanjinhu@gmail.com">fjh</a>
* @date 2014-12-20
* @version 1.0
 */
public class RS {

	private int s;
	
	private Object rs;

	public int getS() {
		return s;
	}

	public void setS(int s) {
		this.s = s;
	}

	public Object getRs() {
		return rs;
	}

	public void setRs(Object rs) {
		this.rs = rs;
	}

	@Override
	public String toString() {
		return "Result [s=" + s + ", rs=" + rs + "]";
	}
	
	public static RS ok(Object o) {
		RS rs = new RS();
		rs.setS(0);
		rs.setRs(o);
		return rs;
	}
	
	public static RS bad(Object o) {
		RS rs = new RS();
		rs.setS(1);
		rs.setRs(o);
		return rs;
	}
	
	public static RS ok() {
		RS rs = new RS();
		rs.setS(0);
		rs.setRs(null);
		return rs;
	}
	
	/*
	public static JsonNode okJson(Object o) {
		RS rs = new RS();
		rs.setS(0);
		rs.setRs(o);
		return Json.toJson(rs);
	}
	
	public static JsonNode okJson() {
		RS rs = new RS();
		rs.setS(0);
		rs.setRs(null);
		return Json.toJson(rs);
	}
	
	public static JsonNode badJson(Object o) {
		RS rs = new RS();
		rs.setS(1);
		rs.setRs(o);
		return Json.toJson(rs);
	}
	
	public JsonNode toJson() {
		return Json.toJson(this);
	}
	*/
}
