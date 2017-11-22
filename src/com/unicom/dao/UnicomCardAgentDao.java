package com.unicom.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UnicomCardAgentDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	
}
