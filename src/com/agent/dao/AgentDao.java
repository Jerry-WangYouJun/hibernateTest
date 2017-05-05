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
import com.poiexcel.util.Dialect;
import com.poiexcel.vo.Pagination;


@Repository
public class AgentDao {
	  
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Agent> queryList(QueryData qo, Pagination page) {
		String sql = "select * from a_agent where 1=1 " + whereSQL(qo) ;
		String finalSql = Dialect.getLimitString(sql, page.getPageNo(), page.getPageSize(), "MYSQL");
         final  List<Agent> list =   new ArrayList<>();
         jdbcTemplate.query(finalSql, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
					Agent  vo = new Agent(); 
					vo.setId(rs.getInt("id"));
					vo.setCode(rs.getString("code"));
					vo.setName(rs.getString("name"));
					vo.setType(rs.getString("type"));
					vo.setCost(rs.getDouble("cost"));
					vo.setRenew(rs.getDouble("renew"));
					vo.setParengId(rs.getInt("parentid"));
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Agent> queryTreeData() {
		String sql = "select a.id pid , a.name pname , b.id sid ,  b.name sname  "
				+ " from a_agent a LEFT JOIN  a_agent b on  a.id  = b.parentId " ;
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

	public void updateCardAgent(String iccids, String agentid) {
		String sql = "update card_agent set  agentid = " +
				agentid + "  where iccid in ("
				+ " select iccid from cmtp  where id in (" + iccids  + "  0 ) )" ;
		jdbcTemplate.update(sql);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<String> queryTypeList() {
		String sql = "select distinct(packageType)  from  cmtp " ;
		final List<String> list = new ArrayList<>();
		jdbcTemplate.query(sql, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				list.add(rs.getString("packageType"));
				 return null ;
			}
	});
		return list;
	}
	
	public String whereSQL(QueryData qo){
		String whereSql = "";
		if(StringUtils.isNotEmpty(qo.getAgentName())){
			whereSql += " and   name  like  '%" + qo.getAgentName() + "%' ";
		}
		if(StringUtils.isNotEmpty(qo.getAgentCode())){
			whereSql += " and   code  like   '" + qo.getAgentCode() + "%' ";
			
		}
		if(StringUtils.isNotEmpty(qo.getAgentid())){
			whereSql += " and   id =  '" + qo.getAgentid() + "' ";
		}
		return whereSql ;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int queryTotal(QueryData qo) {
		final Integer[] total =  {0} ;
		String  sql  = "select count(*) total from a_agent where 1=1 " + whereSQL(qo) ;
		jdbcTemplate.query(sql, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				 total[0] = rs.getInt("total");
				 return null ;
			}
		});
		return total[0];
	}
}
