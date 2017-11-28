package com.unicom.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.agent.common.CodeUtil;
import com.agent.model.Agent;
import com.agent.model.Grid;
import com.agent.model.QueryData;
import com.agent.service.UserService;
import com.poiexcel.vo.Pagination;
import com.unicom.model.UnicomInfoVo;
import com.unicom.service.UnicomAgentService;
import com.unicom.service.UnicomCardAgentService;

import net.sf.json.JSONObject;

@RequestMapping("/unicom")
@Controller
public class UnicomAgentController {
	 
	
	@Autowired
	UnicomAgentService service ;
	@Autowired
	UserService userService ;
	@Autowired
	UnicomCardAgentService cardAgentService;
	
	
	@RequestMapping("/card_query/{agentId}")
	public void queryCard(@PathVariable("agentId") Integer agentId, HttpServletResponse response, 
			HttpServletRequest request  ,HttpSession session , QueryData qo) {
		String pageNo = request.getParameter("pageNo");
		String pageSize = request.getParameter("pageSize");
		//System.out.println(userName);
		Grid grid = new Grid();
		Pagination page =  new Pagination(pageNo, pageSize , 100) ;
	    CodeUtil.initPagination(page);
	    List<UnicomInfoVo>  list = cardAgentService.queryCardInfo(agentId , page , qo);
		grid.setTotal(Long.valueOf(cardAgentService.queryCardTotal(agentId  , qo)));
		grid.setRows(list);
		PrintWriter out;
			try {
				response.setContentType("text/html;charset=UTF-8");
				out = response.getWriter();
				JSONObject json = new JSONObject();
				json = JSONObject.fromObject(grid);
				out.println(json);
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	@RequestMapping("/card_move")
	public void moveCard(String iccids , String agentid ,HttpServletResponse response  ){
		PrintWriter out;
		try {
			service.updateCardAgent(iccids,agentid);
			out = response.getWriter();
			JSONObject json = new JSONObject();
			json.put("msg", "操作成功");
			json.put("success", true);
			out.println(json);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/move")
	public ModelAndView  moveCardInit(QueryData  qo , HttpSession session , String pageNo , String pageSize ,String iccids){
		ModelAndView  mv = new ModelAndView("/unicom/agent_move");
		List<Agent> list =  new ArrayList<>();
		String agentCode = session.getAttribute("agentcode").toString();
		qo.setAgentCode(agentCode);
		Pagination page = new Pagination();
		page.setPageNo(pageNo==null?1:Integer.valueOf(pageNo));
		page.setPageSize(pageSize ==null?50:Integer.valueOf(pageSize));
		page.setTotal(service.queryTatol(qo ));
		CodeUtil.initPagination(page);
		list = service.queryList(qo , page);
		mv.addObject("list", list);
		 mv.addObject("page", page);
		 mv.addObject("ids", iccids);
		return mv ;
	}
	
	@RequestMapping(value="/status/{id}")
	public void delete(@PathVariable("id") Integer id, HttpServletResponse response ){
		PrintWriter out;
		try {
			service.updateOrderStatus(id);
			response.setCharacterEncoding("UTF-8"); 
			out = response.getWriter();
			JSONObject json = new JSONObject();
			json.put("msg", "操作成功");
			json.put("success", true);
			out.println(json);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
