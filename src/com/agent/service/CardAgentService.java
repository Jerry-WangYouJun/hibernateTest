package com.agent.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poiexcel.dao.DataMoveDao;
import com.poiexcel.util.Dialect;
import com.poiexcel.vo.InfoVo;
import com.poiexcel.vo.Pagination;

@Service
public class CardAgentService {
	   
		 @Autowired
		 DataMoveDao dao ;
		 
		 
		 String sql = "select c.* from card_agent a , cmtp c "
		 		+ "where a.iccid = c.ICCID  and a.agentid =   ";
		 
		 public List<InfoVo> queryCardInfo(Integer agentid , Pagination page ){
			 String sql = "select c.* from card_agent a , cmtp c "
				 		+ "where a.iccid = c.ICCID  and a.agentid = " + agentid;
			 String finalSql = Dialect.getLimitString(sql, page.getPageNo(), page.getPageSize(), "MYSQL");
			 return dao.queryDataList(finalSql);
		 }

		public List<Map<String,String>> queryKickbackInfo(Integer id , Pagination page) {
			List<Map<String,String>>  list = new ArrayList<>();
			String sql = "select h.iccid , h.money , c.packageType , h.update_date , h.money - u.cost  kickback "
					+ "from history h , cmtp c , card_agent a , a_agent u "
					+ " where h.iccid = c.iccid and c.iccid = a.iccid "
					+ " and  u.id = a.agentid  and  u.id = " + id  ;
			String finalSql = Dialect.getLimitString(sql, page.getPageNo(), page.getPageSize(), "MYSQL");
			list = dao.queryKickbackList(finalSql);
			return list;
		}
}
