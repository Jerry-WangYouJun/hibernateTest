/**
 * 
 */
package com.redcollar.dao;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/**
 * @author lx g
 *
 */
@Repository
public class DataMoveDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private JdbcTemplate jdbcTemplate2;
	// G.取得ResultSet 对象中各字段名称
	StringBuffer colStr;
	// 存储占位符字符串
	StringBuffer valueStr = new StringBuffer(" ");
	// 存储字段类型字符串
	String[] colTypeArr;
	// 存储数据数组List
	List<String[]> valueList = new ArrayList<String[]>();
	// 存储当前表的插入语句
	String INSERTSQL = "";
	// 存储当前插入表的缓存 ， 未进行插入操作的表为false 插入的表为true
	public static Map<String, Boolean> insertFlagMap = new HashMap<String, Boolean>();
	// 当表数据在oracle中需要根据entitytype进行拆分时 ， map存储id名称与id值串的对应关系
	public Map<String, String> deleteMap = new HashMap<String, String>();
	StringBuffer mailMessage = new StringBuffer("");
	public Map<String, String[]> fkMap;
	public Map<String, String> mapTemp = new HashMap<String, String>();
	int record = 0;
	public Map<String, String> maxIdMap = new HashMap<String, String>();

	/**
	 * 
	 * @param tableName
	 *            读取的properties key：插入表的相关信息 index-tablename-childtablename
	 *            index为序号 tablename为表名 childtablename 如有则为该标的子表
	 * 
	 *            example: 1-c_flbwdn-c_flbwdnmx=1_xfxf
	 *            第一个执行插入操作，c_flbwdn为要插入的表明，c_flbwdnmx为c_flbwdn的子表
	 *            _xfxf为entitytype=1的数据插入到c_flbwdn_xfxf以及c_flbwdnxmx_xfxf表中
	 * @param initFlag
	 * 
	 */
	public String dataMove(final String tableName, Boolean initFlag) {
		try {
			fkMap = getFK();
			if (initFlag)
				insertFlagMap = new HashMap<String, Boolean>();
			mailMessage = new StringBuffer("");
			String sqlBase = "select * from ";
			// sqlserver查询语句
			String SQL = sqlBase + tableName.split("-")[1];
			String insertTable = tableName.split("-")[1];
			if (insertTable.equals("c_cylzxh")||insertTable.equals("c_lwdyhlk")) {
				migrationCylzxh(insertTable);
				return mailMessage.toString();
			}
			queryDataBySQL(SQL, insertTable, tableName, false);
			// 判断tablename由三部分组成，即有相关子表存在
			if (tableName.split("-").length > 2) {
				getChildTable(tableName, "", null, false);
			}
		} catch (Exception e) {
			mailMessage.append("执行时出错 " + e.getMessage() + "<br>");
			e.printStackTrace();
		}
		return mailMessage.toString();
	}

	private void migrationCylzxh(final String insertTable) {
		String sql = "select * from " + insertTable;
		record = 0;
		@SuppressWarnings("unchecked")
		List list = jdbcTemplate2.query(sql, new RowMapper() {
			// public void processRow(ResultSet rs) throws SQLException {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				// 取得ResultSet 对象中的字段数量
				String[] col_value = new String[2];
				// 将当前数据中每个字段的值存入到String数组中
				col_value[0] = rs.getString(1);
				col_value[1] = rs.getString(2);
				valueList.add(col_value);
				// 为防止占用内存过大，每5000条数据则进行一次插入/更新序列操作
				if (valueList.size() == 5000) {
					jdbcTemplate.update(" TRUNCATE TABLE " + insertTable);
					record += insertDataCylzxh( insertTable,record);
					maxIdMap.put(insertTable, record + "");
					maxIdMap = new HashMap<String, String>();
					// 插入操作完成 从新计数
					valueList = new ArrayList<String[]>();
					deleteMap = new HashMap<String, String>();
				}
				return null;
			}

		});

		// 为防止占用内存过大，每5000条数据则进行一次插入/更新序列操作
		if (valueList.size() < 5000) {
			
			jdbcTemplate.update(" TRUNCATE TABLE " + insertTable);
			record += insertDataCylzxh(insertTable, record);
			maxIdMap.put(insertTable, record + "");
			maxIdMap = new HashMap<String, String>();
			// 插入操作完成 从新计数
			valueList = new ArrayList<String[]>();
			deleteMap = new HashMap<String, String>();
		}
		mailMessage.append("向表" + insertTable + "中成功插入了" + record + "数据<br>");
	}

	private int insertDataCylzxh(String insertTable, final int idIndex) {
		
		String insert ;
		if( insertTable.equals("c_lwdyhlk")){
			insert = "insert into "+insertTable+" (ID,CREATEBY,CREATETIME,MODIFYBY,MODIFYTIME,LW,HLK) VALUES (?,?,?,?,?,?,?)  ";
		}else{
			insert="insert into "+insertTable+" (ID,CREATEBY,CREATETIME,MODIFYBY,MODIFYTIME,YPMC,XH) VALUES (?,?,?,?,?,?,?)  " ;
		}
		mailMessage.append("执行" + insert + "<br>");
		// batchUpdate可以高效进行批量插入操作
		try {
			jdbcTemplate.batchUpdate(insert,
					new BatchPreparedStatementSetter() {
						public void setValues(PreparedStatement ps, int i) {
							try {
								String[] value = (String[]) valueList.get(i);
								// 并根据数据类型对Statement 中的占位符进行赋值
								ps.setInt(1, idIndex + i);
								ps.setString(2, "cnbl");
								ps.setDate(3,
										new Date(System.currentTimeMillis()));
								ps.setString(4, "cnbl");
								ps.setDate(5,
										new Date(System.currentTimeMillis()));
								ps.setString(6, value[0]);
								ps.setString(7, value[1]);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						public int getBatchSize() {
							return valueList.size();
						}
					});
		} catch (Exception e) {
			mailMessage.append("执行" + INSERTSQL + "时发生错误：" + e.getMessage()
					+ "<br>");
			// try {
			// SendMail.sendmail(mailMessage.toString());
			// } catch (MessagingException e1) {
			// e1.printStackTrace();
			// }
			e.printStackTrace();
		}
		// }
		return valueList.size();

	}

	/**
	 * 
	 * @param tableName
	 *            读取的properties key：插入表的相关信息（详见上一方法）
	 * @param value
	 *            读取的properties value：格式为：suffindex_suffix 为oracle需要拆分保存的表
	 *            suffindex为 entitytype的值 suffix为对应的后置名
	 * @param initFlag
	 * @throws Exception
	 */
	public String dataMoveOneToMany(final String tableName, String value,
			Boolean initFlag) {
		try {
			fkMap = getFK();
			if (initFlag)
				insertFlagMap = new HashMap<String, Boolean>();
			// mailMessage = new StringBuffer("");
			// 插入oracle时拆分后的新表的后缀
			String[] Entitytype = value.split(";");
			// 存储返回的需要拆分的表的id名称以及包含的id的字符串
			Map<String, String> map;
			for (int i = 0; i < Entitytype.length; i++) {
				String sqlBase = "select * from ";
				// 解析读取的properties key值
				String[] tableNameArr = tableName.split("-");
				// sqlserver 查询数据的表
				String SQL = sqlBase + tableNameArr[1];
				int index = Entitytype[i].indexOf("_");
				String insertTable = tableNameArr[1]
						+ Entitytype[i].substring(index);
				// 根据不同的entitytype 生成相应查询条件
				String whereSQL = " where entitytype ="
						+ Entitytype[i].substring(0, index);
				map = queryDataBySQL(SQL + whereSQL, insertTable, tableName,
						true);
				if (tableNameArr.length == 3) {
					// 判断tablename由三部分组成，即有相关子表存在
					getChildTable(tableName, Entitytype[i].substring(index),
							map, true);
				}
				updateEntitype(
						tableNameArr[1] + Entitytype[i].substring(index),
						Entitytype[i].substring(0, index));
			}
		} catch (Exception e) {
			mailMessage.append("执行时出错 " + e.getMessage() + "<br>");
			e.printStackTrace();
		}
		return mailMessage.toString();
	}

	private void updateEntitype(String tableName, String substring) {
		jdbcTemplate.execute("update " + tableName + " set ENTITYTYPE =  "
				+ substring);
	}

	/**
	 * 
	 * @param tableName
	 *            key
	 * @param tableSuff
	 *            子表的后缀名
	 * @param map
	 *            封装需要拆封的oracle表所含的的ID数据 exam: key=xggbid value =
	 *            (97,9,452,460,103,10,105,15) 表示当前子表对应主表的哪些数据
	 */
	@SuppressWarnings("rawtypes")
	public String getChildTable(String tableName, String tableSuff,
			Map<String, String> map, boolean splitFlag) {
		String whereSQL = "";
		String[] tableNameArr = tableName.split("-");
		// 子表的名字
		String[] subTable = tableNameArr[2].split(",");
		for (int j = 0; j < subTable.length; j++) {
			// map不为null即oracle中需要进行拆分处理
			if (map != null) {
				String key = "";
				String whereStr = "";
				for (Map.Entry entry : map.entrySet()) {
					// 当前子表的id名称 exam:xggbid
					key = entry.getKey().toString();
					// 当前子表对应主表的id包含的id值
					whereStr = entry.getValue().toString();
				}
				// 读取子表包含的全部id值并生成相应的查询条件
				String[] id = whereStr.split(",");
				// oracle中对in中的字段个数有上限控制
				if (id.length > 1000) {
					StringBuffer whereBuff = new StringBuffer("");
					for (int k = 1; k < id.length; k++) {
						// 将id值按每800个存放在一个in查询条件中 m 格式为： id in (....) or id in
						// (....)
						int m = 0;
						// 第一段 不需要加or
						if (k == 1) {
							whereBuff.append(key + " in (" + id[k - 1] + ",");
							// 出去第一个每段开始 为 or id in (...)
						} else if (k == m * 800 + 1) {
							whereBuff.append(" or " + key + " in (" + id[k - 1]
									+ ",");
							// 每段中间 为 id,id,id,
						} else if (k != m * 800 - 1) {
							whereBuff.append(id[k - 1] + ",");
							// 每段结束为 id)
						} else if (k == m * 800 - 1) {
							whereBuff.append(id[k - 1] + ")");
						}
					}
					// 生成查询条件
					whereSQL = " where " + whereBuff.toString();
				} else {
					if (whereStr.endsWith(",")) {
						whereStr = whereStr.substring(0, whereStr.length() - 1);
					}
					whereSQL = " where " + key + " in (" + whereStr + ")";
				}
			}
			// 子表名
			String childTable = subTable[j] + tableSuff;
			queryDataBySQL("select * from " + subTable[j] + whereSQL,
					childTable, tableName, splitFlag);
		}
		return whereSQL;
	}

	/**
	 * 
	 * @param sql
	 *            从sqlserver查询数据的sql语句
	 * @param tableName
	 *            做插入操作的表名
	 * @param splitFlag
	 *            是否在oracle中做拆分存储的标志
	 * @return 需要拆分的时候 存储 id名，主表包含的所有ID的值串
	 */
	@SuppressWarnings("rawtypes")
	public Map<String, String> queryDataBySQL(String sql,
			final String insertTable, final String tableName,
			final boolean splitFlag) {
		record = 0;
		final String[] tableArr = tableName.split("-");
		// 当前插入表为操作则在 临时map中加入
		if (insertFlagMap.get(insertTable) == null)
			insertFlagMap.put(insertTable, false);
		// 如已经进行过插入则返回null 应对部分表关系为 a的子表为b b的子表为c 防止重复操作
		if (insertFlagMap.get(insertTable))
			return null;
		try {
			if (splitFlag) {
				tableSplitFKAct(tableArr, insertTable, true);
			} else {
				tableFKAct(tableArr, insertTable, true);
			}
			mailMessage.append("执行SQL：TRUNCATE TABLE " + insertTable + "<br>");
			// jdbcTemplate.update(" TRUNCATE TABLE " + insertTable);
			// 当表数据在oracle中需要根据entitytype进行拆分时 ， idBuff 存储对应的id值
			final StringBuffer idBuff = new StringBuffer("");
			// 当表数据在oracle中需要根据entitytype进行拆分时 ， map存储id名称与id值串的对应关系
			mapTemp = new HashMap<String, String>();
			@SuppressWarnings("unchecked")
			List list = jdbcTemplate2.query(sql, new RowMapper() {
				// public void processRow(ResultSet rs) throws SQLException {
				public Object mapRow(ResultSet rs, int arg1)
						throws SQLException {
					// 取得ResultSet 结果集相关的信息
					ResultSetMetaData md = rs.getMetaData();
					// 取得ResultSet 对象中的字段数量
					int no_cols = md.getColumnCount();
					if (splitFlag && !isSubTable(insertTable)) {
						no_cols -= 1;
					}
					String[] col_value = new String[no_cols];
					colTypeArr = new String[no_cols];
					colStr = new StringBuffer(" ");
					valueStr = new StringBuffer(" ");
					// 根据表中字段值数量 循环处理生成插入SQL语句
					for (int i = 1; i <= no_cols; i++) {
						// int temp = 0;
						// 不是最后一个值则 加入 “,”进行分割 ，否则不添加
						if (i != no_cols) {
							colStr.append(md.getColumnLabel(i) + ",");
							valueStr.append(" ?,");
						} else {
							colStr.append(md.getColumnLabel(i));
							valueStr.append(" ? ");
						}
						// 将获取当前字段的类型，以便判断数据库中的数据类型并进行插入
						colTypeArr[i - 1] = md.getColumnTypeName(i);
						// 判断当前表是否需要在orale中进行拆分操作
						if (splitFlag && i == 1) {
							// if (i != no_cols) {
							idBuff.append(rs.getString(i) + ",");
							// 将获得的ID 的键值对润如返回的map中
							String id = colStr.toString().split(",")[0];
							mapTemp.put(id, idBuff.toString());
							// deleteMap.put(id, idBuff.toString());
						}
					}
					// 将当前数据中每个字段的值存入到String数组中
					for (int i = 1; i <= no_cols; i++) {
						col_value[i - 1] = rs.getString(i);
						if (i == 1) {
							if (maxIdMap.containsKey(insertTable)) {
								if (Long.valueOf(maxIdMap.get(insertTable)) < Long
										.valueOf(col_value[0])) {
									maxIdMap.put(insertTable, col_value[0]);
								}
							} else {
								maxIdMap.put(insertTable, col_value[0]);
							}
						}
					}
					valueList.add(col_value);
					// 生成当前oracle表的插入语句
					INSERTSQL = "insert into " + insertTable + " ("
							+ colStr.toString() + ")" + " values ( "
							+ valueStr.toString() + ")";
					// 为防止占用内存过大，每5000条数据则进行一次插入/更新序列操作
					if (valueList.size() == 5000) {
						record += insertData();
						updateSeq(insertTable);
						maxIdMap = new HashMap<String, String>();
						// 插入操作完成 从新计数
						valueList = new ArrayList<String[]>();
						deleteMap = new HashMap<String, String>();
					}
					return null;
				}

			});
			// 插入不足5000条以及超出5000条后剩余的数据
			if (valueList.size() > 0) {
				record += insertData();
				valueList = new ArrayList<String[]>();
				updateSeq(insertTable);
				maxIdMap = new HashMap<String, String>();
			}
			if (splitFlag) {
				tableSplitFKAct(tableArr, insertTable, false);
			} else {
				tableFKAct(tableArr, insertTable, false);
			}
		} catch (Exception e) {
			mailMessage.append("执行时出错 " + e.getMessage() + "<br>");
			// try {
			// SendMail.sendmail(mailMessage.toString());
			// } catch (MessagingException e1) {
			// e1.printStackTrace();
			// }
			e.printStackTrace();
		}
		// 插入操作完成后 ，进行插入标记
		insertFlagMap.put(insertTable, true);
		mailMessage.append("向表" + insertTable + "中成功插入了" + record + "数据<br>");
		return mapTemp;
	}

	@SuppressWarnings("unchecked")
	private void updateSeq(final String insertTable) {
		String sql = "select sequence_name , last_number from all_sequences where sequence_owner = 'CNBL' and sequence_name = '"
				+ insertTable.toUpperCase() + "_SEQ'";
		final Map<String, String> map = new HashMap<String, String>();
		jdbcTemplate.query(sql, new RowMapper() {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				map.put(insertTable, rs.getString(2));
				return map;
			}
		});
		if (maxIdMap.get(insertTable) != null) {
			if (Long.valueOf(map.get(insertTable)) < Long.valueOf(maxIdMap
					.get(insertTable)))
				;
			updateMaxSeq(
					Long.valueOf(maxIdMap.get(insertTable))
							- Long.valueOf(map.get(insertTable)), insertTable);
		}
	}

	/**
	 * 根据当前表的插入语句以及valueList中存储的数据 进行数据库插入操作
	 * 
	 * @param map
	 * 
	 */
	public int insertData() {
		// batchUpdate可以高效进行批量插入操作
		try {
			jdbcTemplate.batchUpdate(INSERTSQL,
					new BatchPreparedStatementSetter() {
						public void setValues(PreparedStatement ps, int i) {
							try {
								// 并根据数据类型对Statement 中的占位符进行赋值
								for (int j = 0; j < colTypeArr.length; j++) {
									String[] value = (String[]) valueList
											.get(i);
									// if(colTypeArr[j]==null&&INSERTSQL.contains("s_employee")){
									// continue;
									// }

									String[] arr = colStr.toString().split(",");
									if ("int".equals(colTypeArr[j])
											|| colTypeArr[j] == "int") {
										if (value[j] == null) {
											ps.setString(j + 1, "");
										} else {
											ps.setInt(j + 1,
													Integer.valueOf(value[j]));
										}
									} else if (colTypeArr[j]
											.contains("varchar")
											|| "text".equals(colTypeArr[j])
											|| colTypeArr[j] == "text") {
										if (colStr.toString().split(",")[j]
												.equals("loginpassword")) {
											ps.setString(j + 1, "1");
										} else {
											ps.setString(j + 1, value[j]);
										}
									} else if ("datetime".equals(colTypeArr[j])
											|| colTypeArr[j] == "datetime") {
										ps.setTimestamp(
												j + 1,
												value[j] == null ? null
														: Timestamp
																.valueOf(value[j]));
									} else if ("number".equals(colTypeArr[j])
											|| colTypeArr[j] == "number") {
										ps.setDouble(j + 1,
												Double.valueOf(value[j]));
									} else if ("decimal".equals(colTypeArr[j])
											|| colTypeArr[j] == "decimal") {
										ps.setBigDecimal(j + 1,
												value[j] == null ? null
														: new BigDecimal(
																value[j]));
									} else if (colTypeArr[j] == "char"
											|| "char".equals(colTypeArr[j])) {
										ps.setString(j + 1,
												value[j] == null ? null
														: value[j]);
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						public int getBatchSize() {
							return valueList.size();
						}
					});
		} catch (Exception e) {
			mailMessage.append("执行" + INSERTSQL + "时发生错误：" + e.getMessage()
					+ "<br>");
			// try {
			// SendMail.sendmail(mailMessage.toString());
			// } catch (MessagingException e1) {
			// e1.printStackTrace();
			// }
			e.printStackTrace();
		}
		// }
		return valueList.size();
	}

	private void updateMaxSeq(Long increment, String inseryTable) {
		String sql = "SELECT " + inseryTable.toUpperCase()
				+ "_SEQ.NEXTVAL FROM DUAL";
		for (int i = 0; i <= increment; i++) {
			jdbcTemplate.execute(sql);
		}
	}

	public void tableFKAct(String[] tableArr, String insertTable, boolean flag) {
		if (tableArr.length == 3 && insertTable.equals(tableArr[1])) {
			String[] subTableArr = tableArr[2].split(",");
			for (int i = 0; i < subTableArr.length; i++) {
				String subTable = subTableArr[i];
				String[] arr = fkMap.get(subTable.toUpperCase());
				String fkName = "";
				String fkID = "";
				if (arr != null) {
					fkName = arr[1];
					fkID = arr[0];
					if (flag) {
						jdbcTemplate.execute("ALTER TABLE  " + subTable
								+ " drop CONSTRAINT  \"" + fkName + "\"");
						jdbcTemplate.update(" TRUNCATE TABLE " + subTable);
					} else {
						jdbcTemplate.execute("ALTER TABLE  " + subTable
								+ " add  CONSTRAINT \"" + subTable + "_fk\""
								+ "foreign key (" + fkID + ") references  "
								+ tableArr[1] + " (" + fkID
								+ ") ON DELETE CASCADE");
					}
				}
			}
		} else if (tableArr.length == 4) {
			if (insertTable.equals(tableArr[2])) {
				String subTable = tableArr[3];
				String[] arr = fkMap.get(subTable.toUpperCase());
				String fkName = "";
				String fkID = "";
				if (arr != null) {
					fkName = arr[1];
					fkID = arr[0];
					if (flag) {
						jdbcTemplate.execute("ALTER TABLE  " + subTable
								+ " drop CONSTRAINT  \"" + fkName + "\"");
						jdbcTemplate.update(" TRUNCATE TABLE " + subTable);
					} else
						jdbcTemplate.execute("ALTER TABLE  " + subTable
								+ " add  CONSTRAINT \"" + subTable + "_fk\""
								+ "foreign key (" + fkID + ") references  "
								+ tableArr[2] + " (" + fkID
								+ ") ON DELETE CASCADE");
				}
			} else if (insertTable.equals(tableArr[1])) {
				String subTable = tableArr[2];
				String[] arr = fkMap.get(subTable.toUpperCase());
				String fkName = "";
				String fkID = "";
				if (arr != null) {
					fkName = arr[1];
					fkID = arr[0];
					if (flag) {
						if (flag)
							jdbcTemplate.execute("ALTER TABLE  " + subTable
									+ " drop CONSTRAINT  \"" + fkName + "\"");
						else
							jdbcTemplate.execute("ALTER TABLE  " + subTable
									+ " add  CONSTRAINT \"" + subTable
									+ "_fk\"" + "foreign key (" + fkID
									+ ") references  " + tableArr[1] + " ("
									+ fkID + ") ON DELETE CASCADE");
					}
				}
			}
		}
		if (flag) {
			jdbcTemplate.update(" TRUNCATE TABLE " + insertTable);
		}
	}

	public void tableSplitFKAct(String[] tableArr, String insertTable,
			boolean flag) {
		String table = tableArr[1] + "_" + insertTable.split("\\_")[2];
		if (tableArr.length == 3 && insertTable.equals(table)) {
			String[] subTableArr = tableArr[2].split(",");
			for (int i = 0; i < subTableArr.length; i++) {
				String subTable = subTableArr[i] + "_"
						+ insertTable.split("\\_")[2];
				String[] arr = fkMap.get(subTable.toUpperCase());
				String fkName = "";
				String fkID = "";
				if (arr != null) {
					fkName = arr[1];
					fkID = arr[0];
					if (flag) {
						jdbcTemplate.update(" TRUNCATE TABLE " + subTable);
						jdbcTemplate.execute("ALTER TABLE  " + subTable
								+ " drop CONSTRAINT  \"" + fkName + "\"");
					} else {
						jdbcTemplate.execute("ALTER TABLE  " + subTable
								+ " add  CONSTRAINT \"" + subTable + "_fk\""
								+ "foreign key (" + fkID + ") references  "
								+ table + " (" + fkID + ") ON DELETE CASCADE");
					}
				}
			}
		}
		if (flag && insertTable.equals(table)) {
			jdbcTemplate.update(" TRUNCATE TABLE " + table);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, String[]> getFK() throws Exception {
		String sql = "select c.table_name pt ,d.column_name pid ,c.constraint_name fk "
				+ "from user_constraints a left join user_cons_columns b on a.constraint_name=b.constraint_name "
				+ "left join user_constraints C ON C.R_CONSTRAINT_NAME=a.constraint_name "
				+ "left join user_cons_columns d on c.constraint_name=d.constraint_name "
				+ "where a.constraint_type='P'	and  C.OWNER = 'CNBL' "
				+ " order by a.table_name";
		final Map<String, String[]> map = new HashMap<String, String[]>();
		jdbcTemplate.query(sql, new RowMapper() {
			// public void processRow(ResultSet rs) throws SQLException {
			public Object mapRow(ResultSet rs, int arg1) throws SQLException {
				map.put(rs.getString("pt"), new String[] { rs.getString("pid"),
						rs.getString("fk") });
				return null;
			}
		});
		return map;
	}

	public boolean isSubTable(String table) {
		return table.contains("mx") || table.contains("c_xkkzbdqdxs");
	}

	public void updateTables() {
		try {
			jdbcTemplate
					.update("insert into cmtp (id) values (3)");
		} catch (Exception e) {
			mailMessage.append("执行中出错：" + e.getMessage());
		}

	}

}
