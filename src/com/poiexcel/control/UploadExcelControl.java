package com.poiexcel.control;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.agent.common.ContextString;
import com.poiexcel.service.DataMoveService;
import com.poiexcel.util.DateUtils;
import com.poiexcel.util.ImportExcelUtil;
import com.poiexcel.vo.InfoVo;
import com.poiexcel.vo.Pagination;

@Controller
@RequestMapping("/uploadExcel/*")
public class UploadExcelControl {

	public static void main(String[] args) {
		Format format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		System.out.println(format.format(new Date(System.currentTimeMillis())));
	}

	@Autowired
	private DataMoveService moveDataServices;
	
	@RequestMapping(value = "uploadInit", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String  uploadInit(){
		 return "main";
	}
	
	@RequestMapping(value = "uploadUnicomInit", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String  uploadUnicomInit(HttpSession session ){
		String roleId =  session.getAttribute("roleid").toString();
		if(!ContextString.ROLE_ADMIN_UNICOM.equals(roleId)){
			   return "unicom/agent";
		}
		 return "unicom/main";
	}
	
	@RequestMapping(value = "forward", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String  forwardInit(){
		 return "forwardData";
	}
	
	

	@RequestMapping(value = "dataList.do", method = { RequestMethod.GET,
			RequestMethod.POST })
	public ModelAndView dataList(HttpServletRequest request,
			HttpServletResponse response, Model model  , @RequestParam("dateBegin") String dateBegin ,
			@RequestParam("dateEnd") String dateEnd , String datastatus ,String iccid,Pagination pagination ) {
		if(pagination.getTotal() == 0 ){
			pagination.setTotal(moveDataServices.queryDataSize(dateBegin,dateEnd,datastatus , iccid));
		}
		if(pagination.getPageIndex() == 0 ){
			pagination.setPageIndex( pagination.getTotal() / pagination.getPageSize() + (pagination.getTotal() % pagination.getPageSize() > 0 ? 1 :0 ) );
		}
		List<InfoVo> list = moveDataServices.queryDataList(dateBegin,dateEnd,datastatus , pagination , iccid);
		model.addAttribute("list", list);
		model.addAttribute("pagination", pagination);
		model.addAttribute("dateBegin", dateBegin);
		model.addAttribute("dateEnd", dateEnd);
		model.addAttribute("iccid", iccid);
		ModelAndView mv = new ModelAndView("dataList");
		return mv;
	}

	@RequestMapping(value = "updateStatus.do", method = { RequestMethod.GET })
	public String updateStatus(HttpServletResponse response,
			@RequestParam("color") String color, @RequestParam("id") String id) {
		moveDataServices.updateDataStatus(id , color);
		response.setCharacterEncoding("UTF-8");
		try {
			PrintWriter out = response.getWriter();
			out.print(id + ":" + color);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@ResponseBody
	@RequestMapping(value = "ajaxUpload.do", method = { RequestMethod.GET,
			RequestMethod.POST })
	public void ajaxUploadExcel(HttpServletRequest request,
			HttpServletResponse response , String apiCode) throws Exception {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		response.setCharacterEncoding("utf-8");
		PrintWriter out =  response.getWriter();
		System.out.println("导入表数据开始：" + DateUtils.formatDate("MM-dd:HH-mm-ss"));
		List<List<Object>> listob = getDataList(multipartRequest, response);
		System.out.println("删除表插入开始：" + System.currentTimeMillis());
		moveDataServices.deleteDataTemp();
		System.out.println("临时表插入开始：" + System.currentTimeMillis());
		moveDataServices.insertDataToTemp(listob, apiCode);
		System.out.println("覆盖数据开始    ：" + System.currentTimeMillis());
		int updateNum = moveDataServices.updateExistData(apiCode);
		System.out.println("插入代理商卡数据开始：" + System.currentTimeMillis());
		moveDataServices.insertAgentCard();
		System.out.println("插入新数据开始：" + System.currentTimeMillis());
		int insertNum = moveDataServices.dataMoveSql2Oracle(apiCode);
		System.out.println("执行结束            ：" + System.currentTimeMillis());
		out.print("更新数据" + updateNum + "条 ; 新增数据" + insertNum  + "条");
		out.flush();
		out.close();
	}
	
	@ResponseBody
	@RequestMapping(value = "uploadExcelUnicom", method = { RequestMethod.GET,
			RequestMethod.POST })
	public void uploadExcelUnicom(HttpServletRequest request,
			HttpServletResponse response , String act) throws Exception {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		response.setCharacterEncoding("utf-8");
		PrintWriter out =  response.getWriter();
		System.out.println("导入表数据开始：" + DateUtils.formatDate("MM-dd:HH-mm-ss"));
		List<List<Object>> listob = getDataList(multipartRequest, response);
		if("insert".equals(act)) {
			moveDataServices.insertUnicomList();
		}
		out.flush();
		out.close();
	}
	
	public List<List<Object>>  getDataList(HttpServletRequest request,
			HttpServletResponse response )  throws Exception {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		PrintWriter out = null;
		response.setCharacterEncoding("utf-8");
		out = response.getWriter();
		InputStream in = null;
		List<List<Object>> listob = null;
		MultipartFile file = multipartRequest.getFile("upfile");
		if (file == null  || file.isEmpty()) {
			out.print("未添加上传文件或者文件中内容为空！");
			out.flush();
			out.close();
			return  null;
		}
		in = file.getInputStream();
		System.out.println("导入表数据开始：" + DateUtils.formatDate("MM-dd:HH-mm-ss"));
		listob = new ImportExcelUtil().getBankListByExcel(in,
				file.getOriginalFilename());
		in.close();
		return listob ;
	}

}
