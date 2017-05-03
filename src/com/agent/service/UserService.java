package com.agent.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agent.dao.AgentDao;
import com.agent.dao.UserDao;
import com.agent.model.User;
import com.poiexcel.vo.Pagination;

@Service
public class UserService {
	
	@Autowired
	UserDao  dao ;
	@Autowired
	AgentDao  agentDao ;

	public User checkUser(User user) {
		
		return dao.checkUser(user);
	}

	public List<User> queryList(User user, Pagination page) {
		return dao.queryList(user ,page);
	}

	public void insert(User user) {
		 dao.insert(user);
	}

	public void update(User user) {
		dao.update(user);
		
	}

	public void delete(Integer id) {
		dao.delete(id);
	}
	 	 
	
}	 
