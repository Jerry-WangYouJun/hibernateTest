package com.redcollar.service;

public interface DataMoveService {
	

	public void dataMoveSql2Oracle();
	
	public String dataMoveBytable(String tableName, Boolean initFlag);
	
	
}
