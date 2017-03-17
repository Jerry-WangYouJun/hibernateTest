package com.redcollar.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.redcollar.commons.RS;
import com.redcollar.service.DataMoveService;

/**
 * <p>Title: MoveDataAction.java</p>
 * <p>Description:进行迁移</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company: RCOLLAR</p>
 * @author zhy.ma
 * @date 2015年6月25日
 * @version 1.0
 */
public class MoveDataAction {
	
//	private Logger logger  =  Logger.getLogger(MoveDataAction. class );
	private DataMoveService moveDataServices;
	
	@RequestMapping(value="/movedata.do", method=RequestMethod.GET)
	@ResponseBody
	public RS moveData(){
			moveDataServices.dataMoveSql2Oracle();
			return RS.ok();
	}
	
	
}
