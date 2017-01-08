package com.poiexcel.service;

import java.util.List;

import com.poiexcel.vo.InfoVo;

public interface DataMoveService {
	
	public void deleteDataTemp();

	public void dataMoveSql2Oracle();
	
	public void insertDataToTemp(List<List<Object>> listob);

	public void updateExistData();

	public List<InfoVo> queryDataList(String dateBegin, String dateEnd, String status);

	public void updateDataStatus(String id, String color);

	
	
}
