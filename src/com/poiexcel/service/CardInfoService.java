package com.poiexcel.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poiexcel.dao.DataMoveDao;
import com.poiexcel.vo.InfoVo;


@Service
public class CardInfoService {
	
	@Autowired
	DataMoveDao  dao ;
	
	public InfoVo queryInfoByISMI(String imsi) {
		String sql = "select * from cmtp where imsi = '" + imsi + "'";
		 List<InfoVo>  infoList =  dao.queryDataList(sql);
		 if(infoList.size() > 0 ){
			 InfoVo  info = infoList.get(0);
//			 	dao.queryDetail(info);
//			 	dao.queryPackage(info);
//			 	dao.queryHistory(info);
			   return info;
		 }
		return  null ;
	}

}
