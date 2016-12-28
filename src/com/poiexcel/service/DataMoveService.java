package com.poiexcel.service;

import java.util.List;

public interface DataMoveService {
	

	public void dataMoveSql2Oracle();
	
	public String dataMoveBytable(String tableName, Boolean initFlag);

	public void insertDataToTemp(List<List<Object>> listob);
	
	
}
