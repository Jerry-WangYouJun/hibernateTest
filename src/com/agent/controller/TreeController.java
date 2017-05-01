package com.agent.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.agent.model.TreeNode;


@Controller
@RequestMapping("/treeindex")
public class TreeController {
	  
	
		@RequestMapping("/tree")
	    public void getTreeData(HttpServletRequest request, HttpServletResponse response){
	    	  try {
	    		List<TreeNode> list = new ArrayList<>();
	    		TreeNode  treeNode = new TreeNode();
	    		treeNode.setText("SIM卡查询");
	    		list.add(treeNode);
	    		List<TreeNode> listChild = new ArrayList<>();
	    		TreeNode  childNode = new TreeNode();
	    		childNode.setId("6666");
	    		childNode.setText("测试12");
	    		childNode.getAttributes().setPriUrl("${basePath}/inventory/list");
	    		listChild.add(childNode);
	    		treeNode.setChildren(listChild);
	    		//list.add(new TreeData("库存查询222" ,"${basePath}/inventory/list"))
	    		JSONArray json = JSONArray.fromObject(list);
	    		
    		    response.setCharacterEncoding("utf-8");
				response.getWriter().write(json.toString());
				response.getWriter().flush(); 
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	 
}
