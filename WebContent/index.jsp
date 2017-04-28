<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>库存后台管理首页</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<jsp:include page="common.jsp"></jsp:include>
	<script type="text/javascript">
		
		function loginOut(){
			$.ajax({
				url : "${basePath}/user/loginOut",
				type : 'post',
				success:function(data){
					window.location.href = "${basePath}/login.jsp" ;
				},
				error : function(transport) {
					$.messager.alert('提示', "系统产生错误,请联系管理员!", "error");
				}
			});
		}
		
		
		$(function(){
			var treeData = [{
				text:"库存管理",
				children:[{
					text:"库存查询",
					attributes:{
						url:"${basePath}/inventory/list"
					}
				},{
					text:"入库管理",
					attributes:{
						url:"${basePath}/instock/list"
					}
				},{
					text:"出库管理",
					attributes:{
						url:"${basePath}/outstock/list"
					}
				}]
			},{
				text:"报表管理",	
				children:[{
					text:"库存月报",
					attributes:{
						url:"${basePath}/report/month_input"
					}
				},{
					text:"物资台账",
					attributes:{
						url:"${basePath}/report/product_input"
					}
				}]
			},{
				text:"基础信息管理",	
				children:[{
					text:"用户管理",
					attributes:{
						url:"${basePath}/init/user_list"
					}
				},{
					text:"部门管理",
					attributes:{
						url:"${basePath}/init/dept_list"
					}
				},{
					text:"仓库管理",
					attributes:{
						url:"${basePath}/init/stock_list"
					}
				},{
					text:"供应商管理",
					attributes:{
						url:"${basePath}/init/supplier_list"
					}
				},{
					text:"产品管理",
					attributes:{
						url:"${basePath}/init/goods_list"
					}
				},{
					text:"角色管理",
					attributes:{
						url:"${basePath}/init/role_list"
					}
				},{
					text:"字典管理",
					attributes:{
						url:"${basePath}/init/dictionary_list"
					}
				}]
			}];
			
			$("#tree").tree({
				data:treeData,
				lines:true,
				onClick:function(node){
					if(node.attributes){
						openTab(node.text,node.attributes.url);
					}
				}
			});
			
			function openTab(text,url){
				if($("#tabs").tabs('exists',text)){
					$("#tabs").tabs('select',text);
					//$("#tabs").tabs('getSelected').panel('refresh',url);
					
					/* $("#tabs").tabs('update',{
						tab:$("#tabs").tabs('getSelected'),
						closable:true,
						options 
					}); */
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
					<img alt="" src="${basePath}/images/title.jpg" align="middle">
				</td>
			</tr>
			<tr>
				<td align="right">
					<font color="white" size="2">当前用户： </font>
					<font color="red" size="4">${sessionScope.userName }</font>
					<a href="javascript:loginOut();" style="text-decoration: none;font-size: 12px; color:black;">
						<img alt="" src="${basePath}/images/login_out.png"> 退出
					</a>
					
					 &nbsp;&nbsp;&nbsp;&nbsp;
				</td>
			</tr>
		</table>
	
	</div>
	
	<div region="west" style="width:200px;" title="导航菜单" split="true">
		<ul id="tree">
		
		</ul>
	</div>
	
	<div region="center" class="easyui-tabs" fit="true" border="false" id="tabs">
		<div title="首页" >
			<div align="center" style="padding-top: 100px;"><font color="red" size="10">欢迎使用库存管理系统</font></div>		
		</div>
	</div>
	<div region="south" style="height:25px;background-color:#13A7D5;" align="center"  >
		<font color="#ffffff"> 版权所有:yangjx@121ugrow.com&copy;英谷教育</font>
	</div>
</body>
</html>
