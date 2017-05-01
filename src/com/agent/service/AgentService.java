package com.agent.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agent.dao.AgentDao;
import com.agent.model.Agent;
import com.agent.model.QueryData;

@Service
public class AgentService {
	  
	 @Autowired
	 AgentDao dao ;

	public List<Agent> queryList(QueryData qo) {
		return dao.queryList(qo);
	}

	public void insert(Agent agent ) {
		 dao.insert(agent );
	}

	public void update(Agent agent) {
		dao.update(agent);
	}

	public void delete(Integer id) {
		dao.delete(id);
	}
	 
}
