package com.agent.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poiexcel.dao.DataMoveDao;
import com.poiexcel.vo.InfoVo;

@Service
public class CardAgentService {
	   
		 @Autowired
		 DataMoveDao dao ;
		 
		 
		 String sql = "select c.* from card_agent a , cmtp c "
		 		+ "where a.iccid = c.ICCID  and a.agentid =   ";
		 
		 public List<InfoVo> queryCardInfo(Integer agentid){
			 String sql = "select c.* from card_agent a , cmtp c "
				 		+ "where a.iccid = c.ICCID  and a.agentid = " + agentid;
			 return dao.queryDataList(sql);
		 }
}
