/**
 * 
 */
package com.redcollar.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
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
	
	@Override
	public void insertDataToTemp(List<List<Object>> listob) {
		dataMoveDao.insertDataTemp(listob);
		
	}

	/**
	 * @param tableName
	 *            需要进行转移而表名
	 */
	@SuppressWarnings("rawtypes")
	public void dataMoveSql2Oracle() {
		try {
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
