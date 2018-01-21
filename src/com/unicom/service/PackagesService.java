package com.unicom.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poiexcel.vo.Pagination;
import com.unicom.dao.PackageDao;
import com.unicom.mapping.PackageMapper;
import com.unicom.model.Packages;

@Service
public class PackagesService {

	@Autowired
	PackageDao dao ;
	
	@Autowired
	PackageMapper pacDao;
	
	public List<Packages> queryList(Packages pac, Pagination page) {
		
		return dao.queryList(pac, page);
	}

	public void update(Packages pac) {
		pacDao.update(pac);
	}

	public void insert(Packages pac) {
		pacDao.insertData(pac);
	}

	public void delete(Integer id) {
		pacDao.delete(id);
	}

	public int queryCardTotal(Packages pac) {
		return dao.queryTotal(pac);
	}

}
