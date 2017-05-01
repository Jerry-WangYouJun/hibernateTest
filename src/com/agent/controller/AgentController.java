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

import com.agent.model.Agent;
import com.agent.model.QueryData;
import com.agent.service.AgentService;

@RequestMapping("/agent")
@Controller
public class AgentController {
	 
	
	@Autowired
	AgentService service ;
	
//	@RequestMapping("/list")
//	public String list(){
//		return "/agent/agent_query";
//	}
	
	@RequestMapping("/agent_query")
	public ModelAndView  agentList(QueryData  qo ){
		ModelAndView  mv = new ModelAndView("/agent/agent/agent_query");
		List<Agent> list =  new ArrayList<>();
		list = service.queryList(qo);
		mv.addObject("list", list);
		return mv ;
	}
	
	@RequestMapping("/checkAgent")
	public void  checkAgent(String agentName , HttpServletResponse response ){
		QueryData qo = new QueryData();
		qo.setAgentName(agentName);
		List<Agent> list =  service.queryList(qo);
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
		List<Agent> list = service.queryList(qo);
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
