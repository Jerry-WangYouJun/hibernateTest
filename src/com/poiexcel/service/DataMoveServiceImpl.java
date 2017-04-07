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
	public void updateExistData(String apiCode) {
		String updateDataSql = "UPDATE cmtp c, cmtp_temp t SET c.cardcode = t.cardcode, c.remark=t.remark, "
				+ "c.IMSI=t.IMSI, c.ICCID=t.ICCID, c.cardStatus=t.cardStatus, "
				+ "c.gprsUsed=t.gprsUsed, c.messageUsed=t.messageUsed, c.openDate=t.openDate, "
				+ "c.withMessageService=t.withMessageService, c.withGPRSService=t.withGPRSService,"
				+ " c.packageType=t.packageType, c.monthTotalStream=t.monthTotalStream ,c.updateTime = t.updateTime "
				+ " ,c.apiCode = '" + apiCode + "'"
				+ " WHERE	t.ICCID = c.iccid" ;
		dataMoveDao.updateTables(updateDataSql);
	}


	/**
	 * @param tableName
	 *            需要进行转移而表名
	 */
	public void dataMoveSql2Oracle(String apiCode) {
		try {
			String insertNewDataSql = "INSERT INTO cmtp ( " + columuns + " ) 	SELECT	distinct " 
		+ columuns + " from	cmtp_temp t  where iccid not in (select iccid  from cmtp )" ;
			System.out.println(insertNewDataSql);
			dataMoveDao.updateTables(insertNewDataSql);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public List<InfoVo> queryDataList(String dateBegin, String dateEnd, String status , Pagination pagination) {
		String wheresql = "" ; 
		if(StringUtils.isNotEmpty(dateBegin)){
			 wheresql += " and updateTime > " + dateBegin ;
		}
		if(StringUtils.isNotEmpty(dateEnd)){
			 wheresql += " and  updateTime > " + dateEnd ;
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
	public int queryDataSize(String dateBegin, String dateEnd, String status) {
		String wheresql = "" ; 
		if(StringUtils.isNotEmpty(dateBegin)){
			 wheresql += " and updateTime > " + dateBegin ;
		}
		if(StringUtils.isNotEmpty(dateEnd)){
			 wheresql += " and  updateTime > " + dateEnd ;
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
		dataMoveDao.updateTables(updateDataSql);
	}
	
}
