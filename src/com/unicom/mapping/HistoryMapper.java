package com.unicom.mapping;

import java.util.List;

import com.unicom.model.UnicomHistory;



public interface HistoryMapper {

    public int insertBatch(List<UnicomHistory>  list);

	public void insertData();
	
	public void deleteAll();

}