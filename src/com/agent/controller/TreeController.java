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

import com.agent.common.CodeUtil;
import com.agent.common.ContextString;
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
	AgentService service;

	@Autowired
	CardAgentService cardAgentService;

	@RequestMapping("/tree")
	public void getTreeData(HttpSession session, HttpServletResponse response, HttpServletRequest request) {
			TreeNode treeNode = new TreeNode();
			treeNode.setText("SIM卡管理");
			getTreeDataBytype(session, response, request, "card", treeNode);
	}
	
	@RequestMapping("/tree_unicom")
	public void getUnicomTreeData(HttpSession session, HttpServletResponse response, HttpServletRequest request) {
			TreeNode treeNode = new TreeNode();
			treeNode.setText("SIM卡管理");
			getTreeDataBytype(session, response, request, "unicom_card", treeNode);
	}

	@RequestMapping("/kickback")
	public void getKickbackData(HttpSession session, HttpServletResponse response, HttpServletRequest request) {
		TreeNode treeNode = new TreeNode();
		treeNode.setText("返佣管理");
		if(ContextString.ROLE_ADMIN.equals(session.getAttribute("roleid"))
				|| ContextString.ROLE_AGENT.equals(session.getAttribute("roleid"))) {
			getTreeDataBytype(session, response, request, "kickback", treeNode);
		}else {
			getTreeDataBytype(session, response, request, "unicom_kickback", treeNode);
		}
	}

	public void getTreeDataBytype(HttpSession session, HttpServletResponse response, HttpServletRequest request 
			, String urlType , TreeNode treeNode) {
		try {
			List<TreeNode> list = new ArrayList<>();
			list.add(treeNode);
			// 子节点
			List<TreeNode> listChild = new ArrayList<>();
			Integer agentid = Integer.valueOf(session.getAttribute("agentId").toString());
			listChild = service.getTreeData(agentid, urlType , request);
			treeNode.setChildren(listChild);
			JSONArray json = JSONArray.fromObject(treeNode);
			response.setCharacterEncoding("utf-8");
			response.getWriter().write(json.toString());
			response.getWriter().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/card_query/{agentId}")
	public void queryCard(@PathVariable("agentId") Integer agentId, HttpServletResponse response,
			HttpServletRequest request, HttpSession session, QueryData qo) {
		String pageNo = request.getParameter("pageNo");
		String pageSize = request.getParameter("pageSize");
		// System.out.println(userName);
		Grid grid = new Grid();
		Pagination page = new Pagination(pageNo, pageSize, 100);
		CodeUtil.initPagination(page);
		List<InfoVo> list = cardAgentService.queryCardInfo(agentId, page, qo);
		grid.setTotal(Long.valueOf(cardAgentService.queryCardTotal(agentId, qo)));
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

	@RequestMapping("/kickback_query/{agentId}")
	public void queryKickback(@PathVariable("agentId") Integer agentId, HttpServletResponse response,
			HttpServletRequest request, HttpSession session, QueryData qo) {
		String pageNo = request.getParameter("pageNo");
		String pageSize = request.getParameter("pageSize");
		// System.out.println(userName);
		Grid grid = new Grid();
		Pagination page = new Pagination(pageNo, pageSize, 100);
		CodeUtil.initPagination(page);
		List<Map<String, String>> list = cardAgentService.queryKickbackInfo(agentId, qo, page, qo.getTimeType());
		Map<String, Double> map = cardAgentService.queryKickbackTotal(agentId, qo, qo.getTimeType());
		grid.setTotal(map.get("total").longValue());
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

	@RequestMapping("/kickback_sum/{agentId}")
	public void queryKickbackSum(@PathVariable("agentId") Integer agentId, HttpServletResponse response,
			HttpServletRequest request, HttpSession session, QueryData qo) {
		String pageNo = request.getParameter("pageNo");
		String pageSize = request.getParameter("pageSize");
		// System.out.println(userName);
		Pagination page = new Pagination(pageNo, pageSize, 100);
		CodeUtil.initPagination(page);
		Map<String, Double> map = cardAgentService.queryKickbackTotal(agentId, qo, qo.getTimeType());
		PrintWriter out;
		try {
			response.setContentType("text/html;charset=UTF-8");
			out = response.getWriter();
			out.println(map.get("sumKick"));
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
