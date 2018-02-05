<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page isELIgnored="false"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<link>
<link href="${basePath}/css/bootstrap.min.css" type="text/css"
	rel="stylesheet" />
	<link href="${basePath}/css/style.css" type="text/css"
	rel="stylesheet" />
<script type="text/javascript" src="${basePath}/js/jquery-3.1.1.min.js"></script>
<script type="text/JavaScript" src="${basePath}/js/jquery.form.js"></script>
<script type="text/javascript" src="${basePath}/js/bootstrap.min.js"></script>
<title>My JSP 'index.jsp' starting page</title>
<script type="text/javascript">
	//ajax 方式上传文件操作
	$(document).ready(function() {
		$('#btn').click(function() {
			if($("#apiCode").val() == "0"){
				  alert("请选择数据接口");
				  return false ;
			}
			//if (checkData()) {
				$('#form1').ajaxSubmit({
					url : '${basePath}/uploadExcel/ajaxUpload.do',
					dataType : 'text',
					success : resutlMsg,
					error : errorMsg
				});
				function resutlMsg(msg) {
					alert(msg);
					parent.$('#dlg-frame').dialog("close");
					//window.location.href = "${basePath}/uploadExcel/dataList.do?dateBegin=&dateEnd=&status=";
					$("#upfile").val("");
				}
				function errorMsg() {
					alert("导入excel出错！");
				}
			//}
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
		$("#form1").submit();
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
	                            
           $('input[id=lefile]').change(function() {  
           	$('#photoCover').val($(this).val());  
           	});                         
</script>
</head>

<body class="innerbody">
					<form class="form-signin" role="form" method="POST"
						enctype="multipart/form-data" id="form1"
						action="${basePath}/uploadExcel/upload.do">
						<div class="row " style="padding-left: 100px;padding-top: 40px">
							  <div class="col-xs-3"  >
									<label  class="control-label col-md-12 col-sm-12" style="float:left">上传文件</label>
							   </div>
							   <div class="col-xs-4" style="padding-left: 0px">
									 <input style="float:left"
									id="upfile" type="file" name="upfile"
									class="col-md-12" required>
								</div>
						</div>
						<div class="row " style="padding-left: 100px;padding-top: 20px">
						  <div class="col-xs-3 ">
						    <label for="name"  class="control-label col-md-12 col-sm-12">选择接口</label>
						  </div>
						  <div class="col-xs-4 " style="height: 20px">
						    <select name="apiCode" id = "apiCode">
						      <option value="0">--选择--</option>
						      <option value="1">永思接口数据</option>
						      <option value="2">丰宁接口数据</option>
						    </select>
						  </div>
						</div>
					</form>
					<%-- <form action="${basePath}/ExportExcel/ajaxExport.do" method="post" id="form2">
						<div class="row" style="padding-left: 100px;padding-top: 20px">
							  <div class="col-xs-3 ">
									<label  class="control-label col-md-12 col-sm-12" style="float:left">下载模板</label> 
							  </div>
						     <div class="col-xs-4 ">
						        <a  id="exportFile" style="cursor: pointer;" style="float:left;position: relative;" >点击下载模板</a>
						     </div>
						  </div>
					</form> --%>
			<div class="box-b" style="padding-left: 100px;padding-top: 20px">
				<div class="col-xs-3">
					<button type="button" class="btn btn-primary btn-sm btn-block" id="btn"  >上传</button>
				</div>
				<div class="col-xs-3">
					<button type="button" class="btn btn-primary btn-sm btn-block" onclick="parent.closeModal()">取消</button>
				</div>
			</div>
</body>
</html>
