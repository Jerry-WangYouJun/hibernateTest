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
<title>导入联通卡信息</title>
<script type="text/javascript">
	//ajax 方式上传文件操作
	$(document).ready(function() {
		$('#btn_insert').click(function() {
				$('#form1').ajaxSubmit({
					url : '${basePath}/unicomUpload/uploadExcelUnicom?act=insert',
					dataType : 'text',
					success : resutlMsg,
					error : errorMsg
				});
				function resutlMsg(msg) {
					alert(msg);
					parent.$('#dlg-frame').dialog('close');
					//window.location.href = "${basePath}/uploadExcel/dataList.do?dateBegin=&dateEnd=&status=";
					$("#upfile").val("");
				}
				function errorMsg() {
					alert("导入excel出错！");
				}
		});
		$('#btn_update').click(function() {
			$('#form1').ajaxSubmit({
				url : '${basePath}/unicomUpload/uploadExcelUnicom?act=update',
				dataType : 'text',
				success : resutlMsg,
				error : errorMsg
			});
			function resutlMsg(msg) {
				alert(msg);
				parent.$('#dlg-frame').dialog('close');
				$("#upfile").val("");
			}
			function errorMsg() {
				alert("导入excel出错！");
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
					</form>
			<div class="box-b" style="padding-left: 100px;padding-top: 20px">
				<div class="col-xs-3">
					<button type="button" class="btn btn-primary btn-sm btn-block" id="btn_insert"  >上传新数据</button>
				</div>
				<div class="col-xs-3">
					<button type="button" class="btn btn-primary btn-sm btn-block" id="btn_update"  >上传更新数据</button>
				</div>
			</div>
</body>
</html>
