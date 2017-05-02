package com.agent.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agent.dao.AgentDao;
import com.agent.model.Agent;
import com.agent.model.QueryData;
import com.agent.model.TreeNode;

@Service
public class AgentService {
	  
	 @Autowired
	 AgentDao dao ;
	 Map<Integer , List<Agent>> mapTree = new HashMap<>();

	public List<Agent> queryList(QueryData qo) {
		return dao.queryList(qo);
	}

	public void insert(Agent agent ) {
		 dao.queryPrentIdByCode(agent.getCode());
		 dao.insert(agent );
	}

	public void update(Agent agent) {
		dao.update(agent);
	}

	public void delete(Integer id) {
		dao.delete(id);
	}

	public List<TreeNode> getTreeData(Integer agentid  , String urlType) {
		QueryData qo = new QueryData();
		 List<Agent> agentList = dao.queryList(qo);
		 mapTree  = getMap(agentList);
		 return getNodeList(agentList , agentid , urlType);
	}

	private List<TreeNode> getNodeList(List<Agent> agentList ,Integer agentId , String urlType) {
		List<TreeNode> nodeList = new ArrayList<>();
		for(Agent agent : agentList){
			  if(agentId.equals(agent.getId())){
				  TreeNode  node =  new TreeNode();
				  node.setId(agent.getId()+"");
				  node.setText(agent.getName());
				  node.getAttributes().setPriUrl("http://localhost:8080/spring_mvc_demo/treeindex/" + urlType + "/" + agent.getId()); 
				  List<Agent> firstListTemp = new ArrayList<>();
				  firstListTemp =mapTree.get(agentId);
				  node.setChildren(agentTreeData(firstListTemp , urlType));
				  nodeList.add(node);
			  }
		}
		return nodeList;
	}
	
	private  List<TreeNode>  agentTreeData(List<Agent> listTemp  , String urlType){
			List<TreeNode> nodeList = new ArrayList<>();
			 for(Agent agent : listTemp){
				 TreeNode tn = new TreeNode();
					tn.setId(agent.getId() + "");
					tn.setText(agent.getName());
					tn.getAttributes().setPriUrl("http://localhost:8080/spring_mvc_demo/treeindex/" + urlType + "/" + agent.getId()); 
					if(mapTree.containsKey(agent.getId())){
						tn.setChildren(agentTreeData(mapTree.get(agent.getId()), urlType));
					}
					nodeList.add(tn);
			 }
		return nodeList ;
	}
	
	private Map<Integer, List<Agent>> getMap(List<Agent> agentList) {
		mapTree =  new HashMap<>(); 
		for(Agent agent : agentList){
			   Integer  parentId = agent.getParengId() ;
			   if(mapTree.containsKey(parentId)){
				   mapTree.get(parentId).add(agent);
			   }else{
				  List<Agent> listTemp = new ArrayList<>();
				  listTemp.add(agent);
				   mapTree.put(parentId, listTemp);
			   }
		}
		return mapTree;
	}

	 
}
