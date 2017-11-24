package com.unicom.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poiexcel.service.DataMoveServiceImpl;
import com.unicom.dao.UnicomUploadDao;

@Service
public class UnicomUploadService extends DataMoveServiceImpl {
	
	@Autowired
	UnicomUploadDao uploadDao;

	public String insertUnicomList(List<List<Object>> listObject) {
	  	int total =  uploadDao.insertDataTemp(listObject);
	  	int actual = uploadDao.insertData();
	  	return  "共"+ total + "条数据, 插入成功 " + actual + "条" ;
	}

	public String updateUnicomList(List<List<Object>> listObject) {
		int total =  uploadDao.insertDataTemp(listObject);
	  	int actual = uploadDao.updateData();
	  	return  "共"+ total + "条数据, 插入成功 " + actual + "条" ;
	}
}
