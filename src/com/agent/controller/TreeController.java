package com.agent.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.agent.common.CodeUtil;
import com.agent.model.TreeNode;
import com.agent.service.AgentService;
import com.agent.service.CardAgentService;
import com.poiexcel.vo.InfoVo;
import com.poiexcel.vo.Pagination;


@Controller
@RequestMapping("/treeindex")
public class TreeController {
	  
		@Autowired
		AgentService service ;
		
		@Autowired
		CardAgentService cardAgentService;
	
		@RequestMapping("/tree")
	    public void getTreeData(HttpSession session, HttpServletResponse response , HttpServletRequest request){
	    	  try {
	    		  
	    		List<TreeNode> list = new ArrayList<>();
	    		TreeNode  treeNode = new TreeNode();
	    		treeNode.setText("SIM卡管理");
	    		list.add(treeNode);
	    		//子节点
	    		List<TreeNode> listChild = new ArrayList<>();
	    		Integer agentid = Integer.valueOf(session.getAttribute("agentId").toString());
	    		listChild = service.getTreeData(agentid , "card" , request);
	    		treeNode.setChildren(listChild);
	    		JSONArray json = JSONArray.fromObject(treeNode);
    		    response.setCharacterEncoding("utf-8");
				response.getWriter().write(json.toString());
				response.getWriter().flush(); 
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
		
		@RequestMapping("/kickback")
	    public void getKickbackData(HttpSession session, HttpServletResponse response , HttpServletRequest request ){
	    	  try {
	    		
	    		List<TreeNode> list = new ArrayList<>();
	    		TreeNode  treeNode = new TreeNode();
	    		treeNode.setText("返佣管理");
	    		list.add(treeNode);
	    		//子节点
	    		List<TreeNode> listChild = new ArrayList<>();
	    		Integer agentid = Integer.valueOf(session.getAttribute("agentId").toString());
	    		listChild = service.getTreeData(agentid , "kickback" ,request);
	    		treeNode.setChildren(listChild);
	    		JSONArray json = JSONArray.fromObject(treeNode);
    		    response.setCharacterEncoding("utf-8");
				response.getWriter().write(json.toString());
				response.getWriter().flush(); 
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	 
		
		@RequestMapping(value="/card/{id}")
		public ModelAndView cardInfo(@PathVariable("id") Integer id, HttpServletResponse response
				, String pageNo , String pageSize){
				  ModelAndView mv = new ModelAndView("/agent/agent/card_list");
				  Pagination page = new Pagination();
					List<InfoVo> tatolList = cardAgentService.queryCardInfo(id , page);
					page.setPageNo(pageNo==null?1:Integer.valueOf(pageNo));
					page.setPageSize(pageSize ==null?50:Integer.valueOf(pageSize));
					page.setTotal(tatolList.size());
					CodeUtil.initPagination(page);
				  List<InfoVo>  list = cardAgentService.queryCardInfo(id , page);
				  mv.addObject("list", list);
				  mv.addObject("page", page);
				  return mv ;
		}
		
		@RequestMapping(value="/kickback/{id}")
		public ModelAndView Info(@PathVariable("id") Integer id, HttpServletResponse response
				, String pageNo , String pageSize ){
				  ModelAndView mv = new ModelAndView("/agent/agent/kickback_list");
				  Pagination page = new Pagination();
					List<Map<String,String>> tatolList = cardAgentService.queryKickbackInfo(id , page);
					page.setPageNo(pageNo==null?1:Integer.valueOf(pageNo));
					page.setPageSize(pageSize ==null?50:Integer.valueOf(pageSize));
					page.setTotal(tatolList.size());
					CodeUtil.initPagination(page);
				  List<Map<String,String>>  list = cardAgentService.queryKickbackInfo(id, page);
				  mv.addObject("list", list);
				  mv.addObject("page", page);
				  return mv ;
		}
		
		
}
