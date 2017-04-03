package com.poiexcel.vo;

import java.util.List;

public class Pagination {
	//第几页
	 private int pageNo = 1;
	 //每页几条
	 private int pageSize = 100;
	 //共几页
	 private int pageIndex  ;
	 //总条数
	 private int total ;
	 private List list;
	 
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public List getList() {
		return list;
	}
	public void setList(List list) {
		this.list = list;
	}
	 
}
