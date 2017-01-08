/**
 * 
 */
package com.redcollar.job;

import java.util.Date;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.redcollar.service.DataMoveService;

/**
 * @author lx g
 *
 */
public class DataMoveJob {
	
	public Properties pro = new Properties();
	public DataMoveService service;

	public void processDataMove() {
		System.out.println("--------------Job执行开始"+new Date()+"--------------------");
		service.dataMoveSql2Oracle();
		System.out.println("--------------Job执行结束"+new Date()+"--------------------");
	}

}
