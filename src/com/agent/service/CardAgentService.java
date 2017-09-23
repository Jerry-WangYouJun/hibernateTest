package com.agent.service;

import java.io.UnsupportedEncodingException;
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
				String packageType  = "";
				try {
					 packageType =  new String(qo.getType().getBytes("ISO-8859-1"),"UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				sql += " and  packageType  like '%" + packageType + "%'" ;
			}
			 String finalSql = Dialect.getLimitString(sql, page.getPageNo(), page.getPageSize(), "MYSQL");
			 return dao.queryDataList(finalSql);
		 }
		 
		 public int queryCardTotal(Integer agentid ,  QueryData qo ){
			 String sql = "select count(*) total from card_agent a , cmtp c "
				 		+ "where a.iccid = c.ICCID  and a.agentid = " + agentid;
			if(StringUtils.isNotEmpty(qo.getIccidStart())){
				 sql += " and c.ICCID >= '" + qo.getIccidStart() + "'" ;
			}
			if(StringUtils.isNotEmpty(qo.getIccidEnd())){
				 sql += " and  c.ICCID <= '" + qo.getIccidEnd() + "'" ;
			}
			if(StringUtils.isNotEmpty(qo.getType())){
				String packageType  = "";
				try {
					 packageType =  new String(qo.getType().getBytes("ISO-8859-1"),"UTF-8");
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				sql += " and  packageType  like '%" + packageType + "%'" ;
			}
			 return dao.queryDataTotal(sql);
		 }

		public List<Map<String,String>> queryKickbackInfo(Integer id , QueryData qo ,  Pagination page , int timeType) {
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
			if(timeType == 7 ) {
				sql += " and  DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= date(h.update_date)";
			}else if(timeType == 30) {
				sql += " and DATE_FORMAT( h.update_date, '%Y%m' ) = DATE_FORMAT( CURDATE( ) , '%Y%m' )";
			}else if(timeType == 60) {
				sql +=" and PERIOD_DIFF( date_format( now( ) , '%Y%m' ) , date_format( h.update_date, '%Y%m' ) ) =1";
			}
			sql += " order by h.update_date  desc ";
			String finalSql = Dialect.getLimitString(sql, page.getPageNo(), page.getPageSize(), "MYSQL");
			list = dao.queryKickbackList(finalSql);
			return list;
		}
		
		 public Map<String , Double > queryKickbackTotal(Integer agentid ,  QueryData qo  , int timeType){
			 String sql = "select   sum(h.money) - sum(u.cost) sumKick , count(*) total  "
						+ "from history h , cmtp c , card_agent a , a_agent u "
						+ " where h.iccid = c.iccid and c.iccid = a.iccid "
						+ " and  u.id = a.agentid  and  u.id = " + agentid  ;
				if(StringUtils.isNotEmpty(qo.getDateStart())){
					 sql += " and h.update_date >= '" + qo.getDateStart() + "'" ;
				}
				if(StringUtils.isNotEmpty(qo.getDateEnd())){
					 sql += " and  h.update_date <= '" + qo.getDateEnd() + "'" ;
				}
				if(timeType == 7 ) {
					sql += " and  DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= date(h.update_date)";
				}else if(timeType == 30) {
					sql += " and DATE_FORMAT( h.update_date, '%Y%m' ) = DATE_FORMAT( CURDATE( ) , '%Y%m' )";
				}else if(timeType == 60) {
					sql +=" and PERIOD_DIFF( date_format( now( ) , '%Y%m' ) , date_format( h.update_date, '%Y%m' ) ) =1";
				}
			 return dao.querySumKick(sql);
		 }
}
