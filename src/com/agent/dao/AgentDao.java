package com.agent.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.agent.common.CodeUtil;
import com.agent.model.Agent;
import com.agent.model.QueryData;


@Repository
public class AgentDao {
	  
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Agent> queryList(QueryData qo) {
		String sql = "select * from a_agent where 1=1 " ;
		if(StringUtils.isNotEmpty(qo.getType())){
			sql += " and   type =  '" + qo.getType() + "' ";
		}
		if(StringUtils.isNotEmpty(qo.getIccidStart())){
			sql += " and   iccid  >=  '" + qo.getIccidStart() + "' ";
		}
		if(StringUtils.isNotEmpty(qo.getIccidEnd())){
			sql += " and   iccid <=  '" + qo.getIccidEnd() + "' ";
		}
		if(StringUtils.isNotEmpty(qo.getAgentName())){
			sql += " and   name =  '" + qo.getAgentName() + "' ";
		}
		if(StringUtils.isNotEmpty(qo.getAgentCode())){
			sql += " and   code  like   '" + qo.getAgentCode() + "%' ";
			
		}
		if(StringUtils.isNotEmpty(qo.getAgentid())){
			sql += " and   id =  '" + qo.getAgentid() + "' ";
		}
         final  List<Agent> list =   new ArrayList<>();
         jdbcTemplate.query(sql, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
					Agent  vo = new Agent(); 
					vo.setId(rs.getInt("id"));
					vo.setCode(rs.getString("code"));
					vo.setName(rs.getString("name"));
					vo.setType(rs.getString("type"));
					vo.setCost(rs.getDouble("cost"));
					vo.setRenew(rs.getDouble("renew"));
					vo.setCreater(rs.getString("creater"));
					list.add(vo);
				 return null ;
			}
		});
		return list;
	}

	public void insert(final Agent agent) {
		jdbcTemplate.update("insert into a_agent (code,name,cost,renew,type,creater,parentid) values(?,?,?,?,?,?,?)",   
                new PreparedStatementSetter(){  
              
                    @Override  
                    public void setValues(PreparedStatement ps) throws SQLException { 
                    	int parentId = queryPrentIdByCode(agent.getCode()) ;
                        ps.setString(1,  getMaxCode(agent.getCode(),parentId));  
                        ps.setString(2, agent.getName()); 
                        ps.setDouble(3, agent.getCost());
                        ps.setDouble(4 , agent.getRenew());
                        ps.setString(5, agent.getType());
                        ps.setString(6, agent.getCreater());
                        ps.setInt(7, parentId);
                    }  
        });  
	}

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getMaxCode(String createrCode , int parentId){
		String sql = "select code  from a_agent  where code   like   '" + createrCode + "%' and parentid =  " + parentId ;
		final List<String> list = new ArrayList<>();
		jdbcTemplate.query(sql, new RowMapper() {
				public Object mapRow(ResultSet rs, int arg1) throws SQLException {
					list.add(rs.getString("code"));
					 return null ;
				}
		});
		int temp = 0;
		if(list.size()==0){
			 return createrCode + "-01"  ;  
		} 
		 for(int i = 0   ;  i < list.size() ; i ++){
			 String a = list.get(i).replaceFirst( createrCode + "-" , "");
			  int t = Integer.valueOf(a);
			  if(t > temp){
				    temp = t ;
			  }
		 }
		return createrCode + CodeUtil.getFixCode(temp+1);
	}
	
	/*public static void main(String[] args) {
		String a = "aaabvvv";
		 System.out.println(a.replaceFirst("a", ""));
	}*/
	public void update(final Agent agent) {
		jdbcTemplate.update("update a_agent set  name=? , cost=?, renew = ? , type = ?  where id = ? ",   
                new PreparedStatementSetter(){  
              
                    @Override  
                    public void setValues(PreparedStatement ps) throws SQLException {  
                        ps.setString(1, agent.getName()); 
                        ps.setDouble(2, agent.getCost());
                        ps.setDouble(3 , agent.getRenew());
                        ps.setString(4, agent.getType());
                        ps.setString(5, agent.getId()+ "");
                    }  
        });
	}

	public void delete(Integer id) {
		 jdbcTemplate.update(  
	                "delete from a_agent where id = ?",   
	                new Object[]{id},   
	                new int[]{java.sql.Types.INTEGER});  
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Integer queryPrentIdByCode(String code) {
		String sql = "select id  from a_agent  where code = '" + code + "'" ;
		final  List<Integer> list = new ArrayList<>();
		jdbcTemplate.query(sql, new RowMapper() {
				public Object mapRow(ResultSet rs, int arg1) throws SQLException {
					list.add(rs.getInt("id"));
					 return null ;
				}
		});
		if(list.size()>0){
			  return  list.get(0);
		}else{
			return 0 ;
		}
	}
}
