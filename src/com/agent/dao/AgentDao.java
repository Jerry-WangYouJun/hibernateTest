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
		jdbcTemplate.update("insert into a_agent (code,name,cost,renew,type,creater) values(?,?,?,?,?,?)",   
                new PreparedStatementSetter(){  
              
                    @Override  
                    public void setValues(PreparedStatement ps) throws SQLException {  
                        ps.setString(1,  agent.getCode());  
                        ps.setString(2, agent.getName()); 
                        ps.setDouble(3, agent.getCost());
                        ps.setDouble(4 , agent.getRenew());
                        ps.setString(5, agent.getType());
                        ps.setString(6, agent.getCreater());
                    }  
        });  
	}

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Long getChildCode(String createrCode){
		String sql = "select max(code) maxval from a_agent  where code  = (select code from a_user ) '" + createrCode + "%'" ;
		final Long[] max = new Long[1];
		jdbcTemplate.query(sql, new RowMapper() {
				public Object mapRow(ResultSet rs, int arg1) throws SQLException {
					 max[0] = rs.getLong("maxval");
					 return null ;
				}
			});
		return max[0];
	}

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
}
