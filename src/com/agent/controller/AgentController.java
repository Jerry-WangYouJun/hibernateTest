package com.agent.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.agent.common.CodeUtil;
import com.agent.model.Agent;
import com.agent.model.QueryData;
import com.agent.service.AgentService;
import com.poiexcel.vo.Pagination;

@RequestMapping("/agent")
@Controller
public class AgentController {
	 
	
	@Autowired
	AgentService service ;
	
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
		ModelAndView  mv = new ModelAndView("/agent/card/agent_query");
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
	
	
	
	@RequestMapping("/agent_query")
	public ModelAndView  agentList(QueryData  qo , HttpSession session , String pageNo , String pageSize ){
		ModelAndView  mv = new ModelAndView("/agent/agent/agent_query");
		List<Agent> list =  new ArrayList<>();
		String agentCode = session.getAttribute("agentcode").toString();
		qo.setAgentCode(agentCode);
		Pagination page = new Pagination();
		page.setPageNo(pageNo==null?1:Integer.valueOf(pageNo));
		page.setPageSize(pageSize ==null?50:Integer.valueOf(pageSize));
		page.setTotal(service.queryTatol(qo ));
		CodeUtil.initPagination(page);
		List<String> typeList = service.getTypeList();
		list = service.queryList(qo , page);
		mv.addObject("list", list);
		 mv.addObject("page", page);
		 mv.addObject("typeList", typeList);
		return mv ;
	}
	
	@RequestMapping("/checkAgent")
	public void  checkAgent(String agentName , HttpServletResponse response ,HttpSession session   ){
		QueryData qo = new QueryData();
		qo.setAgentName(agentName);
		qo.setAgentCode(session.getAttribute("agentcode").toString());
		List<Agent> list =  service.queryList(qo , new Pagination());
		response.setContentType("text/text;charset=UTF-8");
		PrintWriter out;
		try {
			
			out = response.getWriter();
			JSONObject json = new JSONObject();
			json.put("msg", "操作成功");
			if(list.size() > 0 ){
				 json.put("success", true);
				 json.put("agentid", list.get(0).getId());
			}else{
				json.put("success", false);
			}
			out.println(json);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/addInit")
	public ModelAndView addInit(){
		ModelAndView  mv = new ModelAndView( "/agent/agent/agent_add");
		Agent agent = new Agent();
		mv.addObject("agent", agent);
		return mv;
	}
	
	@RequestMapping("/insert")
	public void insert(Agent agent , HttpSession session ,HttpServletResponse response ){
		if(agent.getId()!=null && agent.getId() >0){
			service.update(agent);
		}else{
			agent.setCreater(session.getAttribute("user").toString());
			agent.setCode(session.getAttribute("agentcode").toString());
			service.insert(agent );
		}
		response.setContentType("text/text;charset=UTF-8");
		PrintWriter out;
		try {
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
	
	@RequestMapping(value="/updateInit/{id}")
	public ModelAndView  updateInit(@PathVariable("id") Integer id){
		ModelAndView  mv = new ModelAndView( "/agent/agent/agent_add");
		QueryData qo = new QueryData();
		qo.setAgentid(id+ "");
		List<Agent> list = service.queryList(qo , new Pagination());
		mv.addObject("agent", list.get(0));
		return mv;
	}
	
	@RequestMapping(value="/agent_delete/{id}")
	public void delete(@PathVariable("id") Integer id, HttpServletResponse response ){
		PrintWriter out;
		try {
			service.delete(id);
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