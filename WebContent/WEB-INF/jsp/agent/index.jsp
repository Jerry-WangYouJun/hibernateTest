<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/common.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>丰宁信息后台管理首页</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript">
		function loginOut(){
					window.location.href = "${basePath}/loginOut" ;
		}
		$(function(){
			var treeData = [{
				text:"用户管理",	
				children:[{
					text:"代理商管理",
					attributes:{
						url:"${basePath}/agent/agent_query"
					}
				},{
					text:"密码管理",
					attributes:{
						url:"${basePath}/user_query"
					}
				}]
			}];
			$("#fixedtree").tree({
				data:treeData,
				lines:true,
				onClick:function(node ){
					if(node.attributes){
						openTab(node.text,node.attributes.url);
					}
				}
			});
			$("#cardtree").tree({
				url:"${basePath}/treeindex/tree", 
				loadFilter: function(data){	
							 return data;	
					},
				lines:true,
				onClick:function( node){
					if(node.attributes.priUrl != ""){
						openTab("卡信息-"+node.text,node.attributes.priUrl);
						return false;
					}
				}
			});
			$("#kickbacktree").tree({
				url:"${basePath}/treeindex/kickback", 
				loadFilter: function(data){	
							 return data;	
					},
				lines:true,
				onClick:function( node){
					if(node.attributes.priUrl != ""){
						openTab("返佣"+node.text,node.attributes.priUrl);
						return false;
					}
				}
			});
			
			function openTab(text,url){
				if($("#tabs").tabs('exists',text)){
					$("#tabs").tabs('select',text);
				}else{
					var content="<iframe frameborder='0' scrolling='auto' style='width:100%;height:100%' src="+url+"></iframe>";
					$("#tabs").tabs('add',{
						id:text,
						title:text,
						closable:true,
						content:content
					});
				}
			}
		});
	</script>
  </head>
  
 <body class="easyui-layout">
	<div region="north" style="height:150px; background-color:#13A7D5;">
		<table width="100%">
			<tr>
				<td align="center"> 
					
				</td>
			</tr>
			<tr>
				<td align="right">
					<font color="white" size="2">当前用户： </font>
					<font color="red" size="4">${sessionScope.user }</font>
					<a href="javascript:loginOut();" style="text-decoration: none;font-size: 12px; color:black;">
						<img alt="" src="${basePath}/images/login_out.png"> 退出
					</a>
					
					 &nbsp;&nbsp;&nbsp;&nbsp;
				</td>
			</tr>
		</table>
	
	</div>
	
	<div region="west" style="width:200px;" title="导航菜单" split="true">
		<ul id="fixedtree">
		
		</ul>
		<ul id="cardtree">
		
		</ul>
		<ul id="kickbacktree">
		
		</ul>
	</div>
	
	<div region="center" class="easyui-tabs" fit="true" border="false" id="tabs">
		<div title="首页" >
			<div align="center" style="padding-top: 100px;"><font color="red" size="10">欢迎使用丰宁物联网管理系统</font></div>		
		</div>
	</div>
	<div region="south" style="height:25px;background-color:#13A7D5;" align="center"  >
		<font color="#ffffff"> 版权所有:wang_yjun@163.com&copy;丰宁物联网公司</font>
	</div>
</body>
</html>
