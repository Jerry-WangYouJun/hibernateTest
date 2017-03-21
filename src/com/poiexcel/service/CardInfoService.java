package com.poiexcel.service;

import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poiexcel.dao.DataMoveDao;
import com.poiexcel.vo.InfoVo;
import com.redcollar.commons.ResponseURLDataUtil;
import com.redcollar.commons.WlkAPIUtil;


@Service
public class CardInfoService {
	
	@Autowired
	DataMoveDao  dao ;
	
	public InfoVo queryInfoByISMI(String iccid) {
		String sql = "select * from cmtp where iccid = '" + iccid + "'";
		 List<InfoVo>  infoList =  dao.queryDataList(sql);
		 if(infoList.size() > 0 ){
			 InfoVo  info = infoList.get(0);
//			 	dao.queryDetail(info);
//			 	dao.queryPackage(info);
//			 	dao.queryHistory(info);
			 try {
				getDetail(info);
			} catch (Exception e) {
				System.out.println("接口信息错误");
				e.printStackTrace();
			}
			   return info;
		 }
		return  null ;
	}
	
	public void getDetail(InfoVo  info) throws Exception{
			JSONObject  jsonCardInfo = getResultData(info,"0001000000008");
		    String gprsStatus = jsonCardInfo.get("GPRSSTATUS").toString();
		    if(info.getCardStatus()!=null && !info.getCardStatus().equals(gprsStatus)){
		    	info.setCardStatus(gprsStatus);
		    }
		    JSONObject  jsonUserStatus = getResultData(info,"0001000000009");
		    String userStatus = jsonUserStatus.getString("STATUS").toString();
		    if(info.getUserStatus()!=null && !info.getUserStatus().equals(userStatus)){
		    	info.setUserStatus(userStatus);
		    }
		    JSONObject  jsonGprsUsed = getResultData(info,"0001000000012");
		     	String  gprsUsed = jsonGprsUsed.getString("total_gprs").toString();
		    if(info.getGprsUsed()!=null && !info.getGprsUsed().equals(gprsUsed)){
		    	info.setGprsUsed(gprsUsed);
		    }
	}

	
	public  JSONObject  getResultData(InfoVo info ,  String ebid) throws Exception{
		 String jsonString =	ResponseURLDataUtil.getReturnData(WlkAPIUtil.getUrl(info,ebid)) ;
		 JSONObject jsonObject = JSONObject.fromObject(jsonString);  
		 if(!"正确".equals(jsonObject.get("message"))){
			 throw new Exception(jsonObject.get("message").toString());
		 }
		 return jsonObject.getJSONArray("result").getJSONObject(0);
	}
}
