/**
 * 
 */
package com.redcollar.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.redcollar.dao.DataMoveDao;

/**
 * @author lx g
 *
 */
@Service
public class DataMoveServiceImpl implements DataMoveService {

	public Properties pro = new Properties();
	@Autowired
	public DataMoveDao dataMoveDao;
	StringBuffer message;

	/**
	 * @param tableName
	 *            需要进行转移而表名
	 */
	@SuppressWarnings("rawtypes")
	public void dataMoveSql2Oracle() {
		try {
			//读取配置文件
//			pro = initParam();
//			message = new StringBuffer("");
//			message.append("start:"+new Date()+"<br>");
//			Boolean initFlag = true;
			// List list = new ArrayList();
			// 循环size次数与map长度一致
//			for (int i = 1; i <= pro.size(); i++) {
//				// 循环配置文件中的内容
//				for (Map.Entry entry : pro.entrySet()) {
//					// key形式为 1—tablename 前面为序号 便于排序
//					String key = entry.getKey().toString();
//					// value形式为 1_xfxf , 2 __cyxf 格式
//					String value = entry.getValue().toString();
//					// 当前循环值与key的序号相等时 存入对象中
//					if (String.valueOf(i).equals(key.split("-")[0])) {
//						// HashMap<String, String> map = new HashMap<String,
//						// String>();
//						// map.put(key, value);
//						System.out.println("--------------数据库操作"+key+"执行开始"
//								+ System.currentTimeMillis()
//								+ "--------------------");
//						if (value == null || "".equals(value)) {
//							message.append(dataMoveDao.dataMove(key,initFlag));
//						} else {
//							message.append(dataMoveDao.dataMoveOneToMany(key, value,initFlag));
//						}
//						System.out.println("--------------数据库操作执行结束"
//								+ System.currentTimeMillis()
//								+ "--------------------");
//						initFlag = false;
//						break;
//					}
//				}
//			}
			
			dataMoveDao.updateTables();
//			message.append("end:"+new Date()+"<br>");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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


	public String dataMoveBytable(String tableName ,Boolean initFlag ) {
		//读取配置文件
		try {
			pro = initParam();
			message = new StringBuffer("");
			// 循环配置文件中的内容
			for (Map.Entry entry : pro.entrySet()) {
				
				// key形式为 1—tablename 前面为序号 便于排序
				String key = entry.getKey().toString();
				// value形式为 1_xfxf , 2 __cyxf 格式
				String value = entry.getValue().toString();
				// 当前循环值与key的序号相等时 存入对象中
				if (tableName.split("_")[0].equals(key.split("-")[0])) {
					System.out.println("--------------数据库操作执行开始"
							+ System.currentTimeMillis()
							+ "--------------------");
					if (value == null || "".equals(value)) {
						message.append(dataMoveDao.dataMove(key,  initFlag));
					} else {
						message.append(dataMoveDao.dataMoveOneToMany(key, value,  initFlag));
					}
					System.out.println("--------------数据库操作执行结束"
							+ System.currentTimeMillis()
							+ "--------------------");
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return message.toString();
		
	}




}
