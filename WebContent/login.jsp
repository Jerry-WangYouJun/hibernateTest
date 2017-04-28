<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%
	String msg = (String)request.getAttribute("msg");
	if(msg == null){
		msg = "";
	}
 %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
      <title>登录页面</title>
	
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="this is my page">
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <script type="text/javascript" src="js/jquery-2.0.3.js"></script>
<%-- 	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/common.css"> --%>
    <style type="text/css">
    	.login{
    		position: absolute;
    		top: 50%;
    		left: 40%;
    	}
    	.loginText{
    		font-family: 
    	}
    </style>
	<script type="text/javascript">
		function checkUser(){
			var userNo = $("#userNo").val();
			var pwd = $("#pwd").val();
			//var vcode = $("#vcode").val();
			if(userNo == ""){
				alert("用户名不能为空!");
				return;
			}
			if(pwd == ""){
				alert("密码不能为空!");
				return;
			}
			//if(vcode == ""){
				//alert("验证码不能为空!");
				//return;
			//}
			document.forms[0].submit();
		}
	</script>
  </head>
  
  <body bgcolor="#11A5D5">
  <div style="background-image: url('./images/login.jpg'); background-repeat:no-repeat; background-position:center; width: 100%;height: 99%">
    <form action="${pageContext.request.contextPath}/user/checkUser" method="post">
    		<table align="center" class="login" cellspacing="1px" cellpadding="1px"  >
    		<tr>
    			<td class="loginText">用户名：</td>
    			<td><input type="text" id="userNo" name="userNo"/></td>
    		</tr>
    		<tr>
    			<td>密码：</td>
    			<td><input type="password" id="pwd" name = "pwd"/></td>
    		</tr>
    		<tr>
    			<td colspan="2">
    				<a href="javascript:checkUser();" ><!--  -->
    					<img alt="" src="${pageContext.request.contextPath}/images/login_btn.jpg" align="right" width="80px;">
    				</a>
    			</td>
    		</tr>
    		<tr>
    			<td colspan="2">
    				<font color="red"><%=msg  %></font>
    			</td>
    		</tr>
    	</table>
    	</form> 
    </div>
    	
  </body>
</html>
