<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page isELIgnored="false"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<link>
<link href="${basePath}/css/bootstrap.min.css" type="text/css"
	rel="stylesheet" />
<link href="${basePath}/css/styles.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="${basePath}/js/jquery-3.1.1.min.js"></script>
<script type="text/JavaScript" src="${basePath}/js/jquery.form.js"></script>
<script type="text/javascript" src="${basePath}/js/bootstrap.min.js"></script>
<title>My JSP 'index.jsp' starting page</title>
<script type="text/javascript">
	//ajax 方式上传文件操作
	$(document).ready(function() {
		$('#btn').click(function() {
			if (checkData()) {
				$('#form1').ajaxSubmit({
					url : 'uploadExcel/ajaxUpload.do',
					dataType : 'text',
					success : resutlMsg,
					error : errorMsg
				});
				function resutlMsg(msg) {
					alert(msg);
					$("#upfile").val("");
				}
				function errorMsg() {
					alert("导入excel出错！");
				}
			}
		});
	});

	//JS校验form表单信息
	function checkData() {
		var fileDir = $("#upfile").val();
		var suffix = fileDir.substr(fileDir.lastIndexOf("."));
		if ("" == fileDir) {
			alert("选择需要导入的Excel文件！");
			return false;
		}
		if (".xls" != suffix && ".xlsx" != suffix) {
			alert("选择Excel格式的文件导入！");
			return false;
		}
		return true;
	}

	//ajax 方式下载文件操作
	$(document).ready(function() {
		$('#exportFile').click(function() {
			$('#form2').ajaxSubmit({
				dataType : 'text',
				error : errorMsg
			});
			function errorMsg() {
				alert("导出excel出错！");
			}
		});
	});
	
</script>
</head>

<body class="innerbody">
	<div>文件以模板的方式导出，模板存放在项目中(WEB-INF/ExcelDemoFile/)</div>
	<form action="ExportExcel/ajaxExport.do" method="post" id="form2">
		<input type="submit" id="exportExcel" name="exportExcel"
			value="Excel导出" />
	</form>
		<form class="form-signin"   role="form" method="POST"
			enctype="multipart/form-data" id="form1"
			action="uploadExcel/upload.do">
				<div >
					<table style="width: 30%"
						class="table col-md-4 col-md-offset-4">
						<tr>
							<td colspan="2">
								<div class="row form-group">
									<div class="col-xs-12 col-sm-6 col-md-12">1.通过简单的form表单提交方式</div>
									<div class="col-xs-6 col-md-12">
										2.通过jquery.form.js插件提供的form表单异步提交功能</div>
								</div>
							</td>
						</tr>
						<tr>
							<td >
								<div class="row form-group">
									<label for="inputEmail3" class="control-label col-md-12">上传文件:</label>
									<input id="upfile" type="file" name="upfile"
										class="control-label col-md-12" required>
								</div>
							</td>
							<td>
								<div class="row form-group">
								<label for="inputEmail3" class="control-label col-md-12">下载模板:</label>
									<a id = "exportFile" style="cursor:pointer;">点击下载</a>文件以模板的方式导出
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="2">
								<div class="row form-group ">
									<button
										class="btn btn-primary col-sm-6 col-md-4 col-md-offset-4"
										type="submit" onclick="checkData();">提交</button>
								</div>
							</td>
						</tr>
					</table>
				</div>
				<div class="content">
					<div class="main-list">
						<div class="main-list-cont">
							<table width="100%" class="table">
								<tbody>
								<tr class = "main-list-top">
										<td width = "8%">卡号</td>
										<td width = "8%">IMSI</td>
										<td width = "8%">ICCID</td>
										<td width = "5%">用户状态</td>
										<td width = "5%">工作状态</td>
										<td width = "10%">本月累计使用流量(MB)</td>
										<td width = "6%">本月累计使用短信(条)</td>
										<td width = "9%">开户日期</td>
										<td width = "6%">是否签约短信服务</td>
										<td width = "6%">是否签约GPRS服务</td>
										<td width = "10%">套餐资费</td>
										<td width = "5%">本月总量</td>
										<td width = "5%">备注</td>
									</tr>
									<c:forEach var="order" items="${list}" varStatus="status">
										<tr>
										<%-- 	<c:forEach var="prcessIndex" items="${order}"
												varStatus="status">

												<td>${prcessIndex}</td>
											</c:forEach> --%>
										<td width = "8%">${order[0]}</td>
										<td width = "8%">${order[2]}</td>
										<td width = "8%">${order[3]}</td>
										<td width = "5%">${order[4]}</td>
										<td width = "5%">${order[5]}</td>
										<td width = "10%">${order[6]}</td>
										<td width = "6%">${order[7]}</td>
										<td width = "9%">${order[8]}</td>
										<td width = "6%">${order[9]}</td>
										<td width = "6%">${order[10]}</td>
										<td width = "10%">${order[11]}</td>
										<td width = "5%">${order[12]}</td>
										<td width = "5%">${status.index + 1}</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</div>
				</div>
		</form>
</body>
</html>
