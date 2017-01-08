package com.poiexcel.control;

import java.io.File;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.poiexcel.util.ExportExcelUtil;
import com.poiexcel.vo.InfoVo;
 
@Controller
@RequestMapping("/ExportExcel/*")  
public class ExportExcelControl {
	
	/**
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="ajaxExport.do",method={RequestMethod.GET,RequestMethod.POST})
	public  String  ajaxUploadExcel(HttpServletRequest request,HttpServletResponse response) throws Exception {
		
		OutputStream os = null;  
		Workbook wb = null;    
		
		try {
			List<InfoVo> lo = new ArrayList<InfoVo>();
			for (int i = 0; i < 8; i++) {
				InfoVo vo = new InfoVo();
				vo.setCode("110"+i);
				vo.setDate("2015-11-0"+i);
				vo.setMoney("1000"+i+".00"); 
				vo.setName("导出模板"+i);
				lo.add(vo);
			}
			
			ExportExcelUtil util = new ExportExcelUtil();
			File file =util.getExcelDemoFile("/ExcelDemoFile/测试模板.xlsx");
			String sheetName="sheet1";  
			wb = util.writeNewExcel(file, sheetName,lo); 
			
			String fileName="移动充值卡.xlsx";
		    response.setContentType("application/vnd.ms-excel");
		    response.setHeader("Content-disposition", "attachment;filename="+ URLEncoder.encode(fileName, "utf-8"));
		    os = response.getOutputStream();
			wb.write(os);  
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			os.flush();
			os.close();
			wb.close();
		} 
		return null;
	}


}
