package com.agent.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.agent.model.User;
import com.agent.service.UserService;


@Controller
public class UserController {
	 
	@Autowired
	UserService service ;
	
	@RequestMapping("/checkUser")
	public String checkUser(User user , HttpServletRequest request , HttpSession session){
		String agentcode = service.checkUser(user);
		
		if(agentcode != null ){
			session.setAttribute("agentcode", agentcode);
			session.setAttribute("user", user.getUserNo());
			return "/agent/index" ;
		}else{
			request.setAttribute("msg", "用户名或者密码错误");
			return "/agent/login" ;
		}
	}
	
	@RequestMapping("/login")
	public String login(){
		return "/agent/login";
	}
	
	@RequestMapping("/user_query")
	public ModelAndView quetyList(User user){
		  ModelAndView mv = new ModelAndView("/agent/user/user_list");
		  List<User> list = service.queryList(user);
		  mv.addObject("list", list);
		  return  mv ;
	}
	
	@RequestMapping("/addInit")
	public ModelAndView addInit(){
		ModelAndView  mv = new ModelAndView( "/agent/user/user_add");
		User user = new User();
		mv.addObject("user", user);
		return mv;
	}
	
	@RequestMapping("/insert")
	public void insert(User user , HttpSession session ,HttpServletResponse response ){
		user.setPwd(user.getPwd().split(",")[0]);
		if(user.getId()!=null && user.getId() >0){
			service.update(user);
		}else{
			service.insert(user);
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
		ModelAndView  mv = new ModelAndView( "/agent/user/user_add");
		User user = new User();
		user.setId(id );
		List<User> list = service.queryList(user);
		mv.addObject("user", list.get(0));
		return mv;
	}
	
	@RequestMapping(value="/user_delete/{id}")
	public void delete(@PathVariable("id") Integer id, HttpServletResponse response ){
		response.setContentType("text/text;charset=UTF-8");
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
