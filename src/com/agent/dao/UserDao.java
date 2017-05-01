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

import com.agent.model.User;


@Repository
public class UserDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int checkUser(User user) {
		String sql = " select * from a_user where userNo = '" + user.getUserNo()
				+ "' and pwd = '" + user.getPwd() + "' ";
		final Integer[] total = {-1};
		jdbcTemplate.query(sql, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				total[0] = new Integer(rs.getInt("agentid"));
				return rs.getInt("agentid");
			}
		});
		return total[0];
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<User> queryList(User user) {
		String sql = "select u.id , u.userno , u.username ,u.pwd , u.roleid , u.agentid , a.name , u.agentid "
				+ "from a_user u , a_agent a where u.agentid = a.id  " ;
		if(StringUtils.isNotEmpty(user.getUserNo())){
			sql += " and   userno  like  '%" + user.getUserNo() + "%' ";
		}
		if(StringUtils.isNotEmpty(user.getUserName())){
			sql += " and   u.username  >=  '%" + user.getUserName() + "%' ";
		}
		if(StringUtils.isNotEmpty(user.getRoleId())){
			sql += " and   u.roleid =  '" + user.getRoleId() + "' ";
		}
		if(StringUtils.isNotEmpty(user.getAgentName())){
			sql += " and   a.name  like  '%" + user.getAgentName() + "%' ";
		}
		if(user.getId()!=null && StringUtils.isNotEmpty(user.getId()+"")){
			sql += " and   u.id =  '" + user.getId() + "' ";
		}
         final  List<User> list =   new ArrayList<>();
         jdbcTemplate.query(sql, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
					User  vo = new User(); 
					vo.setId(rs.getInt("id"));
					vo.setUserNo(rs.getString("userno"));
					vo.setUserName(rs.getString("username"));
					vo.setAgentName(rs.getString("name"));
					vo.setRoleId(rs.getString("roleid"));
					vo.setAgentId(rs.getInt("agentid"));
					list.add(vo);
				 return null ;
			}
		});
		return list;
	}

	public void insert(final User user) {
		jdbcTemplate.update("insert into a_user (userno ,username , pwd , roleid , agentid ) values(?,?,?,?,?)",   
                new PreparedStatementSetter(){  
              
                    @Override  
                    public void setValues(PreparedStatement ps) throws SQLException {  
                        ps.setString(1,  user.getUserNo());  
                        ps.setString(2, user.getUserName()); 
                        ps.setString(3, user.getPwd());
                        ps.setString(4 , "2");
                        ps.setInt(5, user.getAgentId());
                    }  
        });  
	}

	public void update(final User user) {
		jdbcTemplate.update("update a_user set  pwd = ?  where id = ? ",   
                new PreparedStatementSetter(){  
                    @Override  
                    public void setValues(PreparedStatement ps) throws SQLException {  
                        ps.setString(1, user.getPwd()); 
                        ps.setInt(2, user.getId());
                    }  
        });
	}
	
	public void delete(Integer id) {
		 jdbcTemplate.update(  
	                "delete from a_user where id = ?",   
	                new Object[]{id},   
	                new int[]{java.sql.Types.INTEGER});  
	}
}
