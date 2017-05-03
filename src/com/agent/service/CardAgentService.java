package com.agent.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

		public List<Map<String,String>> queryKickbackInfo(Integer id) {
			List<Map<String,String>>  list = new ArrayList<>();
			Map<String,String > map =new  HashMap<>();
			map.put("iccid", "1111111111111111");
			map.put("money", "20");
			map.put("packageType", "10MB/每月（包年）");
			map.put("update_date", "2017-04-09");
			map.put("kickback", "17.000");
			list.add(map);
			list = dao.queryKickbackList(id);
			return list;
		}
}
