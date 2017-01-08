/**
 * 
 */
package com.redcollar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.redcollar.commons.RS;
import com.redcollar.service.ShowServices;

/**
 * <p>Title: ShowDataAction.java</p>
 * <p>Description:显示表名</p>
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>Company: RCOLLAR</p>
 * @author zhy.ma
 * @date 2015年4月14日
 * @version 1.0
 */
public class ShowAction{
	
	private ShowServices show;
	@RequestMapping(value="/showdata.do", method=RequestMethod.GET)
	@ResponseBody
	public RS showData(){
		return RS.ok(show.getShow());
	}

	

}

