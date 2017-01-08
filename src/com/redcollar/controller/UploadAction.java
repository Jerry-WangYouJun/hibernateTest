
package com.redcollar.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * @author wang_yjun
 *
 */
public class UploadAction {

	private ServletContext servletContext;

	/**
	 * 导入题库Excel
	 * 
	 * @param uploadExcel
	 *            上传的excel文件
	 * 
	 * @param request
	 *            请求
	 * 
	 * @param resposne
	 *            响应
	 * @throws Exception
	 * 
	 */
	@RequestMapping(value = "upload.do", method = RequestMethod.POST)
	public String leadInExcelQuestionBank(
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		System.out.println("开始");
		
		// 转型为MultipartHttpRequest：   
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;   
        // 获得文件：   
        MultipartFile file = multipartRequest.getFile("file");   
		String code = file.getContentType();
		String rspCode = response.getCharacterEncoding();
		response.setCharacterEncoding("UTF-8");
		String path = request.getSession().getServletContext()
				.getRealPath("upload");
		String fileName = file.getOriginalFilename();
		// String fileName = new Date().getTime()+".jpg";
		System.out.println(path);
		File targetFile = new File(path, fileName);
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}
		String str = new String(file.getBytes() );
		
		// 保存
		try {
			file.transferTo(targetFile);
			excelToList(targetFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "result";
	}

	private List excelToList(File file)
			throws  IOException {
		InputStream input = new FileInputStream(file);  
		HSSFWorkbook workBook = new HSSFWorkbook(input);  
        HSSFSheet sheet = workBook.getSheetAt(0);  
        HSSFRow columnRow = sheet.getRow(0); 
        int index = columnRow.getLastCellNum() ;
        StringBuffer colmunStr  = new StringBuffer();
        List<String[]> valueList = new ArrayList<String[]>();
        for(int i = 0 ; i < index ; i ++){
        	HSSFCell cell = columnRow.getCell(i); 
        	colmunStr.append(cell.toString() + ",") ; 
        }
        for(int i = 2 ; i <= sheet.getLastRowNum() ; i ++){
        	HSSFRow valueRow = sheet.getRow(i); 
        	String[] valueArr = new String[index];
        	 for(int j = 0 ; j < index; j ++){
             	valueArr[j] =valueRow.getCell(j).toString() ; 
             }
        	 valueList.add(valueArr);
        	 
        }
		return null;
	}

}

