<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
  <div> 1.通过简单的form表单提交方式</br> 
  		2.通过jquery.form.js插件提供的form表单异步提交功能 </div></br> 
  	<form method="POST" enctype="multipart/form-data" id="form1" action="uploadExcel/upload.do">
 	 	<table>
 	 	 <tr>
 	 	 	<td>上传文件: </td>
 	 	 	<td> <input id="upfile" type="file" name="upfile"></td>
 	 	 </tr>
  		<tr>
 	 	 	<td><input type="submit" value="提交" onclick="return checkData()"></td>
 	 	 	<td><input type="button" value="ajax方式提交" id="btn" name="btn" ></td>
 	 	 </tr>
  		</table>	
	</form>
	</br></br>
	<hr></br>
	<div>
		文件以模板的方式导出，模板存放在项目中(WEB-INF/ExcelDemoFile/)
	</div>
	</br>
	<form action="ExportExcel/ajaxExport.do" method="post" id="form2">
		<input type="submit" id="exportExcel" name="exportExcel" value="Excel导出"/>
	</form>
