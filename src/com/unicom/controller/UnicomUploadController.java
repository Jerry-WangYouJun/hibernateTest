package com.unicom.controller;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.agent.common.ContextString;
import com.poiexcel.util.DateUtils;
import com.unicom.service.UnicomUploadService;

@Controller
@RequestMapping("/unicomUpload")
public class UnicomUploadController {
	
	@Autowired
	private UnicomUploadService dataServices;
	
	@RequestMapping(value = "uploadUnicomInit", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String  uploadUnicomInit(HttpSession session ){
		String roleId =  session.getAttribute("roleid").toString();
		if(!ContextString.ROLE_ADMIN_UNICOM.equals(roleId)){
			   return "unicom/agent";
		}
		 return "unicom/main";
	}
	

	@ResponseBody
	@RequestMapping(value = "uploadExcelUnicom", method = { RequestMethod.GET,
			RequestMethod.POST })
	public void uploadExcelUnicom(HttpServletRequest request,
			HttpServletResponse response , String act) throws Exception {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		response.setCharacterEncoding("utf-8");
		Long startTime = System.currentTimeMillis(); 
		PrintWriter out =  response.getWriter();
		System.out.println("导入表数据开始：" + DateUtils.formatDate("MM-dd:HH-mm-ss"));
		List<List<Object>> listob = dataServices.getDataList(multipartRequest, response);
		System.out.println("读取xls数据用时：" + (System.currentTimeMillis() - startTime));
		String msg = "";
		dataServices.deleteDataTemp("u_cmtp_temp");
		if("insert".equals(act)) {
			System.out.println("删除临时表,用时" + (System.currentTimeMillis() - startTime));
			if("insert".equals(act)) {
				msg = dataServices.insertUnicomList(listob );
			}else if("update".equals(act)) {
				msg = dataServices.updateUnicomList(listob );
			}
		}
		out.print(msg);
		out.flush();
		out.close();
	}
}
