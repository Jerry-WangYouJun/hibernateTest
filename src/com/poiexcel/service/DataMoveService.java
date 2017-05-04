package com.poiexcel.service;

import java.util.List;

import com.poiexcel.vo.InfoVo;
import com.poiexcel.vo.Pagination;

public interface DataMoveService {
	
	public void deleteDataTemp();

	public int dataMoveSql2Oracle(String apiCode);
	
	public void insertDataToTemp(List<List<Object>> listob, String apiCode);

	public int updateExistData(String apiCode);

	public List<InfoVo> queryDataList(String dateBegin, String dateEnd, String status , Pagination pagination  , String iccid );

	public void updateDataStatus(String id, String color);

	public int queryDataSize(String dateBegin, String dateEnd, String status  , String iccid );

	public void insertAgentCard();

	
	
}
