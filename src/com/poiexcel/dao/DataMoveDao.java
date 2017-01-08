package com.poiexcel.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.poiexcel.vo.InfoVo;

/**
 * @author lx g
 *
 */
@Repository
public class DataMoveDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	// 存储数据数组List
	List<List<Object>> objectList = new ArrayList<List<Object>>();
	// 存储当前表的插入语句
	String INSERTSQL = "";
	StringBuffer mailMessage = new StringBuffer("");
	String columuns = "cardcode,	remark,	IMSI,	ICCID,	userStatus,	cardStatus,	gprsUsed,"
			+ "	messageUsed,	openDate,	withMessageService,	withGPRSService,	packageType,	monthTotalStream , updateTime ";

	public void updateTables(String sql) {
		
		try {
			jdbcTemplate
					.update(sql);
		} catch (Exception e) {
			mailMessage.append("执行中出错：" + e.getMessage());
		}

	}
	

	public void deleteDataTemp() {
		String insertNewDataSql = "delete from cmtp_temp" ;
		try {
			jdbcTemplate
					.update(insertNewDataSql);
		} catch (Exception e) {
			mailMessage.append("执行中出错：" + e.getMessage());
		}
	}
	
	public int insertDataTemp(List<List<Object>> listob) {
		 String insertsqlTemp = "INSERT INTO cmtp_temp ( " + columuns + ") "
		 		+ "VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ? )";
		 objectList = listob ;
		// batchUpdate可以高效进行批量插入操作
		try {
			jdbcTemplate.batchUpdate(insertsqlTemp,
					new BatchPreparedStatementSetter() {
						public void setValues(PreparedStatement ps, int i) {
							try {
								Format format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
								// 并根据数据类型对Statement 中的占位符进行赋值
									List<Object> valueList = objectList.get(i);
									ps.setString(1, String.valueOf(valueList.get(0)));
									ps.setString(2, String.valueOf(valueList.get(1)));
									ps.setString(3, String.valueOf(valueList.get(2)));
									ps.setString(4, String.valueOf(valueList.get(3)));
									ps.setString(5, String.valueOf(valueList.get(4)));
									ps.setString(6, String.valueOf(valueList.get(5)));
									ps.setString(7, String.valueOf(valueList.get(6)));
									ps.setString(8, String.valueOf(valueList.get(7)));
									ps.setString(9, String.valueOf(valueList.get(8)));
									ps.setString(10, String.valueOf(valueList.get(9)));
									ps.setString(11, String.valueOf(valueList.get(10)));
									ps.setString(12, String.valueOf(valueList.get(11)));
									ps.setString(13, String.valueOf(valueList.get(12)));
									ps.setString(14,format.format(new Date(System.currentTimeMillis())));
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						public int getBatchSize() {
							return objectList.size();
						}
					});
		} catch (Exception e) {
			mailMessage.append("执行" + INSERTSQL + "时发生错误：" + e.getMessage()
					+ "<br>");
			e.printStackTrace();
		}
		// }
		return objectList.size();
	
		
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<InfoVo> queryDataList(String selectSql) {
		final List<InfoVo> list = new ArrayList<InfoVo>();
		//Map<String, String> map = new HashMap<String, String>();
		jdbcTemplate.query(selectSql, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				InfoVo  vo = new InfoVo(); 
				vo.setId(rs.getString("id"));
				vo.setCardCode(rs.getString("CardCode"));
				vo.setIMSI(rs.getString("IMSI"));
				vo.setICCID(rs.getString("ICCID"));
				vo.setUserStatus(rs.getString("userStatus"));
				vo.setCardStatus(rs.getString("cardStatus"));
				vo.setGprsUsed(rs.getString("gprsUsed"));
				vo.setMessageUsed(rs.getString("messageUsed"));
				vo.setOpenDate(rs.getString("openDate"));
				vo.setWithMessageService(rs.getString("withMessageService"));
				vo.setWithGPRSService(rs.getString("withGPRSService"));
				vo.setPackageType(rs.getString("packageType"));
				vo.setMonthTotalStream(rs.getString("monthTotalStream"));
				vo.setRemark(rs.getString("remark"));
				vo.setStatus(rs.getString("status"));
				list.add(vo);
				return null;
			}
		});
		return list ;
	}


}
