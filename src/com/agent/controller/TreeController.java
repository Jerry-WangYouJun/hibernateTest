package com.agent.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.agent.common.CodeUtil;
import com.agent.model.Grid;
import com.agent.model.QueryData;
import com.agent.model.TreeNode;
import com.agent.service.AgentService;
import com.agent.service.CardAgentService;
import com.poiexcel.vo.InfoVo;
import com.poiexcel.vo.Pagination;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


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
				, String pageNo , String pageSize , QueryData qo){
				  ModelAndView mv = new ModelAndView("/agent/agent/card_list");
				  Pagination page = new Pagination();
					page.setPageNo(pageNo==null?1:Integer.valueOf(pageNo));
					page.setPageSize(pageSize ==null?50:Integer.valueOf(pageSize));
					page.setTotal(cardAgentService.queryCardTotal(id  , qo));
					CodeUtil.initPagination(page);
				  List<InfoVo>  list = cardAgentService.queryCardInfo(id , page , qo);
				  mv.addObject("list", list);
				  mv.addObject("page", page);
				  mv.addObject("agentid", id);
				  List<String> typeList = service.getTypeList();
				  mv.addObject("typeList", typeList);
				  return mv ;
		}
		
		@RequestMapping("/card_query/{agentId}")
		public void queryTest(@PathVariable("agentId") Integer agentId, HttpServletResponse response, 
				HttpServletRequest request  ,HttpSession session , QueryData qo) {
			String pageNo = request.getParameter("pageNo");
			String pageSize = request.getParameter("pageSize");
			//System.out.println(userName);
			Grid grid = new Grid();
			Pagination page =  new Pagination(pageNo, pageSize , 100) ;
		    CodeUtil.initPagination(page);
		    List<InfoVo>  list = cardAgentService.queryCardInfo(agentId , page , qo);
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
		
		
		@RequestMapping(value="/kickback/{id}")
		public ModelAndView Info(@PathVariable("id") Integer id, QueryData qo,  HttpServletResponse response
				, String pageNo , String pageSize , Integer timeType ){
				  ModelAndView mv = new ModelAndView("/agent/agent/kickback_list");
				  Pagination page = new Pagination();
					page.setPageNo(pageNo==null?1:Integer.valueOf(pageNo));
					page.setPageSize(pageSize ==null?50:Integer.valueOf(pageSize));
					Map<String , Double > map = cardAgentService.queryKickbackTotal(id , qo , timeType);
					page.setTotal( map.get("total").intValue());
					CodeUtil.initPagination(page);
				  List<Map<String,String>>  list = cardAgentService.queryKickbackInfo(id, qo  , page , timeType);
				  mv.addObject("list", list);
				  mv.addObject("page", page);
				  mv.addObject("agentid", id);
				  mv.addObject("timeType", timeType);
				  mv.addObject("sumKick", map.get("sumKick"));
				  return mv ;
		}
		
		
}
