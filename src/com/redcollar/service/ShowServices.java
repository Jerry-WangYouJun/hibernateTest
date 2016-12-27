/**
 * 
 */
package com.redcollar.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import javax.swing.filechooser.FileSystemView;

import org.springframework.stereotype.Component;

/**
 * @author lx g
 *
 */
@Component
public class ShowServices {

	@SuppressWarnings("rawtypes")
	public String[] getShow() {
//		try {
//			String[] showArr = new String[132];
//		// 获取properties配置文件
//		InputStream inputStream = this.getClass().getClassLoader()
//				.getResourceAsStream("tableList.properties");
//		// 根据配置文件获取properties对象
//		Properties p = new Properties();
//		p.load(inputStream);
//		int i = 0 ;
//		// 初始化properties对象
//		for (Map.Entry entry : p.entrySet()) {
//			String key = entry.getKey().toString();
//			showArr[i] = key.split("-")[0]+"_"+ key.split("-")[1];
//			i+=1;
//			if(key.split("-").length==3 ){
//				for(int j =0 ; j <key.split("-")[2].split(",").length;j++){
//					showArr[i] = key.split("-")[0]+"_"+ key.split("-")[2].split(",")[j];
//					i+=1;
//				}
//			}
//			
//		}
//		return showArr;
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		return null;
	}
	

}
