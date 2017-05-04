package com.agent.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agent.model.QueryData;
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
		 
		 public List<InfoVo> queryCardInfo(Integer agentid , Pagination page, QueryData qo ){
			 String sql = "select c.* from card_agent a , cmtp c "
				 		+ "where a.iccid = c.ICCID  and a.agentid = " + agentid;
			if(StringUtils.isNotEmpty(qo.getIccidStart())){
				 sql += " and c.ICCID >= '" + qo.getIccidStart() + "'" ;
			}
			if(StringUtils.isNotEmpty(qo.getIccidEnd())){
				 sql += " and  c.ICCID <= '" + qo.getIccidEnd() + "'" ;
			}
			if(StringUtils.isNotEmpty(qo.getType())){
				 sql += " and  packageType  like '%" + qo.getIccidEnd() + "%'" ;
			}
			 String finalSql = Dialect.getLimitString(sql, page.getPageNo(), page.getPageSize(), "MYSQL");
			 return dao.queryDataList(finalSql);
		 }

		public List<Map<String,String>> queryKickbackInfo(Integer id , QueryData qo ,  Pagination page) {
			List<Map<String,String>>  list = new ArrayList<>();
			String sql = "select h.iccid , h.money , c.packageType , h.update_date , h.money - u.cost  kickback "
					+ "from history h , cmtp c , card_agent a , a_agent u "
					+ " where h.iccid = c.iccid and c.iccid = a.iccid "
					+ " and  u.id = a.agentid  and  u.id = " + id  ;
			if(StringUtils.isNotEmpty(qo.getDateStart())){
				 sql += " and h.update_date >= '" + qo.getDateStart() + "'" ;
			}
			if(StringUtils.isNotEmpty(qo.getDateEnd())){
				 sql += " and  h.update_date <= '" + qo.getDateEnd() + "'" ;
			}
			
			String finalSql = Dialect.getLimitString(sql, page.getPageNo(), page.getPageSize(), "MYSQL");
			list = dao.queryKickbackList(finalSql);
			return list;
		}
}
