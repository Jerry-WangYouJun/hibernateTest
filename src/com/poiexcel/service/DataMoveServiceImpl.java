/**
 * 
 */
package com.poiexcel.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poiexcel.dao.DataMoveDao;
import com.poiexcel.util.Dialect;
import com.poiexcel.util.StringUtils;
import com.poiexcel.vo.InfoVo;
import com.poiexcel.vo.Pagination;

/**
 * @author lx g
 *
 */
@Service
public class DataMoveServiceImpl implements DataMoveService {
	
	String columuns = "cardcode,	remark,	IMSI,	ICCID,	userStatus,	cardStatus,	gprsUsed,	messageUsed,"
			+ "  openDate,	withMessageService,	withGPRSService,	packageType,	monthTotalStream , updateTime , apiCode ";

	public Properties pro = new Properties();
	@Autowired
	public DataMoveDao dataMoveDao;
	StringBuffer message;


	public Properties initParam() throws IOException {
		// 获取properties配置文件
		InputStream inputStream = this.getClass().getClassLoader()
				.getResourceAsStream("tableList.properties");
		// 初始化properties对象
		Properties p = new Properties();
		// 根据配置文件获取properties对象
		p.load(inputStream);
		return p;
	}

	@Override
	public void deleteDataTemp() {
		dataMoveDao.deleteDataTemp();
	}
	
	@Override
	public void insertDataToTemp(List<List<Object>> listob , String apiCode) {
		dataMoveDao.insertDataTemp(listob , apiCode);
	}

	@Override
	public int updateExistData(String apiCode) {
		String updateSql = "select t.* from cmtp c , cmtp_temp t where c.iccid = t.iccid and  "
				+ "  ( c.cardcode != t.cardcode or  c.remark !=t.remark or  "
				+ "  c.IMSI !=t.IMSI or c.ICCID !=t.ICCID or  c.cardStatus !=t.cardStatus or "
				+ "	 c.gprsUsed !=t.gprsUsed or  c.messageUsed !=t.messageUsed or "
				+ "	 c.openDate !=t.openDate or c.withMessageService !=t.withMessageService or "
				+ "  c.withGPRSService !=t.withGPRSService or  c.packageType !=t.packageType OR "
				+ "  c.monthTotalStream !=t.monthTotalStream  or c.updateTime != t.updateTime  ) ";
		System.out.println(updateSql);
		List<InfoVo>  list =  dataMoveDao.queryDataList(updateSql);
		return dataMoveDao.updateTables(list);
	}


	/**
	 * @param tableName
	 *            需要进行转移而表名
	 */
	public int dataMoveSql2Oracle(String apiCode) {
		int num = 0 ;
		try {
			String insertNewDataSql = "select *  from cmtp_temp where  iccid not in (select iccid from cmtp ) " ;
			System.out.println(insertNewDataSql);
			List<InfoVo>  list =  dataMoveDao.queryDataList(insertNewDataSql);
			num = dataMoveDao.insertTables(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  num ;
	}


	@Override
	public List<InfoVo> queryDataList(String dateBegin, String dateEnd, String status , Pagination pagination  , String iccid ) {
		String wheresql = "" ; 
		if(StringUtils.isNotEmpty(dateBegin)){
			 wheresql += " and openDate >= '" + dateBegin + "' " ;
		}
		if(StringUtils.isNotEmpty(dateEnd)){
			 wheresql += " and  openDate <= '" + dateEnd + "' " ;
		}
		if(StringUtils.isNotEmpty(iccid)){
			 wheresql += " and  iccid  like  '%" + iccid + "%' " ;
		}
		if(StringUtils.isNotEmpty(status)){
			 if("1".equals(status)){
				 wheresql += " and status is not null "  ;
			 }else{
				 wheresql += " and  status is null "  ;
			 }
		}
		String selectSql = "select * from cmtp where 1=1 " + wheresql ; 
		String finalSql = Dialect.getLimitString(selectSql, pagination.getPageNo(), pagination.getPageSize(), "MYSQL");
		return  dataMoveDao.queryDataList(finalSql );
	}
	
	@Override
	public int queryDataSize(String dateBegin, String dateEnd, String status ,String iccid ) {
		String wheresql = "" ; 
		if(StringUtils.isNotEmpty(dateBegin)){
			 wheresql += " and openDate >= '" + dateBegin  + "' ";
		}
		if(StringUtils.isNotEmpty(dateEnd)){
			 wheresql += " and  openDate <= '" + dateEnd  + "' ";
		}
		if(StringUtils.isNotEmpty(iccid)){
			 wheresql += " and  iccid  like  '%" + iccid + "%' " ;
		}
		if(StringUtils.isNotEmpty(status)){
			 if("1".equals(status)){
				 wheresql += " and status is not null "  ;
			 }else{
				 wheresql += " and  status is null "  ;
			 }
		}
		String selectSql = "select count(1) total from cmtp where 1=1 " + wheresql ; 
		return  dataMoveDao.queryTotal(selectSql );
	}

	@Override
	public void updateDataStatus(String id , String color) {
		String updateDataSql = "UPDATE cmtp SET status =  '" + color + "'"
				+ " WHERE id = " + id  ;
		dataMoveDao.update(updateDataSql);
	}
	
}
