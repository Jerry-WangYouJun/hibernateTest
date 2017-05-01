<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户添加页面</title>
<style type="text/css">
	table{
		font-size:12px;
	}
	textarea{
		font-size:12px;
	}
</style>
<script type="text/javascript">
	function doServlet(){
		if(checkInfo()){
			$.ajax({
		    	url : "${basePath}/agent/insert",
	       		type:'post',  
	       		data: $("#userForm").serialize(),
	       		dataType: 'json',
	       		success: function(data){
		           	parent.$.messager.alert('提示',data.msg);
		       		if(data.success == true) {
		       			parent.doSearch();
		       			parent.$('#dlg-frame').dialog("close");
			       	}
	       		},
	       		error: function(transport) { 
	       			$.messager.alert('提示',"系统产生错误,请联系管理员!","error");
	        	} 
	       	});
		}
		parent.doSearch();
	}
	function checkInfo(){
		if($("#name").val() == ""){
			$.messager.alert("提示","代理商名称不能为空!","error");
			return false;
		}
		if($("#cost").val() == ""){
			$.messager.alert("提示","成本价不能为空!","error");
			return false;
		}
		if($("#type").val() == ""){
			$.messager.alert("提示","套餐类型不能为空!","error");
			return false;
		}
		if($("#renew").val() == ""){
			$.messager.alert("提示","续费价不能为空!","error");
			return false;
		}
		
		return true;
	}
</script>
</head>
<body>
	<form:form id="userForm" action="${basePath }/agent/insert" modelAttribute="agent" method="post">
		<form:hidden path="id" id = "id" />
		<table width="100%">
		  	<tr >
		  		<td >代理商名称：</td>
		  		<td style="padding: 20px">
		  			<form:input id="name" path="name" />
		  		</td>
		  		<td >成本价：</td>
		  		<td style="padding: 20px">
		  			<form:input id="cost" path="cost"   />
		  		</td>
		  	</tr>
		  	<tr>
		  		<td>套餐类型：</td>
		  		<td style="padding: 20px">
		  			<form:input id="type" path="type" />
		  		</td>
		  		<td>续费价：</td>
		  		<td style="padding: 20px">
		  			<form:input id="renew" path="renew"   />
		  		</td>
		  	</tr>
		  </table>
	</form:form>
</body>
</html>