<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page isELIgnored="false"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
<link>
<link href="${basePath}/css/bootstrap.min.css" type="text/css"
	rel="stylesheet" />
<link href="${basePath}/css/styles.css" type="text/css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" media="all"
	href="${basePath}/css/bootstrap-colorpalette.css" />
<link rel="stylesheet" type="text/css" media="all"
	href="${basePath}/css/example.css" />
<script type="text/javascript" src="${basePath}/js/jquery-3.1.1.min.js"></script>
<script type="text/JavaScript" src="${basePath}/js/jquery.form.js"></script>
<script type="text/javascript" src="${basePath}/js/bootstrap.min.js"></script>
<script type="text/javascript"
	src="${basePath}/js/bootstrap-colorpalette.js" charset="utf-8"></script>
<title>My JSP 'index.jsp' starting page</title>
<style>
.list-bottom{
	position: relative;
    float: left;
    padding: 6px 12px;
    margin-left: -1px;
    line-height: 1.42857143;
    color: #428bca;
    text-decoration: none;
    background-color: #fff;
    border: 1px solid #ddd;
    padding: 5px 10px;
    font-size: 12px;
    }
.list-bottom input,.list-bottom select{
	border:1px;
	padding:0 !important;
	height:17px;
}
</style>
<script type="text/javascript">
	$(function() {
		$("tr:odd").css("background", "#bbffff");
	});
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
					window.location.href="uploadExcel/dataList.do?dateBegin=&dateEnd=&status=";
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
	
	function updateStatus(id , color ){
		$.ajax({
			type: "GET",
            url: "${basePath}/uploadExcel/updateStatus.do",
            data: {id : id , color : color},
            success: function(data){
            	 
            },
            error:function(){
            	
            }
		});
	}
	
	$(function(){
		//$('#selected-color_2666'  ).css('background-color', '#FFFF00');
	});
	
	function query(pageNo){
		var status = "" ; 
		 $(":radio").each(function(){
			  if(this.checked){
				    status = this.value;
			  }
		 });
		 $("#pageNo").val(pageNo);
		/*  window.location.href = "${basePath}/uploadExcel/dataList.do?dateBegin=" + 
				 $("#dateBegin").val() + "&dateEnd=" +  $("#dateEnd").val() + "&datastatus=" + status 
				 + "&pagination.pageNo = " + pageNo + "&pagination.pageIndex = " + ; */
				 $("#form1").submit();
		
	}
	
	function exportInit(){
		 $("#myModalLabel").html("导入模板");
         $("#iframeDialog").attr("src", "${basePath}/uploadExcel/uploadInit");
	     $("#myModal").modal({backdrop:"static"});
	}
	
	function importForwardData(){
		window.location.href="${basePath}/uploadExcel/forward"	
	
	}
	
	   //关闭Modal框
    function closeModal() {
        $("#myModal").modal('hide');
    }
	   
	function queryLast(){
		 var pageNo = $("#pageNo").val();
		 if (pageNo != undefined) {
				$("#pageNo").val(pageNo);
			}
		 if(parseInt(pageNo) > 1 ){
			 query(parseInt(pageNo) - 1);
		 }
	}
	
	function queryNext(){
		 var pageNo = $("#pageNo").val();
		 if (pageNo != undefined) {
				$("#pageNo").val(pageNo);
			}
		 if(parseInt(pageNo) < parseInt( "${pagination.pageIndex}")){
			 query(parseInt(pageNo) + 1);
		 }
	}
	
	
</script>
</head>

<body >
	<form class="form-signin" role="form" method="POST"
		enctype="multipart/form-data" id="form1"
		action="${basePath}/uploadExcel/dataList.do">
		<div class="content">
			<p class="content-top">
				<span>开户日期</span>
				 <input class="top-data" type="date" name="dateBegin" id="dateBegin" value="${dateBegin}" />
				  - <input type="date" id="dateEnd" name="dateEnd" value="${dateEnd}" />
					<span>是否充值</span> 
					 <input class="top-data" type="radio" name="datastatus" value="1" style="height: 15px; width: 20px"/>是 
					<input class="top-data" type="radio" name="datastatus" value="0" style="height: 15px; width: 20px"/>否 
					<span>ICCID</span> <input class="top-data" type="text" name="iccid" id="iccid" value="${iccid}" />
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<input class="bta" type="button" value="查询" onclick="query()" />
					<input class="bta" type="reset" value="清除"  />
					<input class="btd" type="button" value="导入数据" onclick="exportInit()">
					<input class="btd" type="button" value="跳转数据" onclick="importForwardData()">
			</p>
			 <div class="main-list">
				<div class="main-list-top">
					<table width="100%" cellspacing="0" cellpadding="0">
						<thead>
							<tr style="text-align: center;">
								<td width="2%"></td>
								<td width="6%">卡号</td>
								<td width="8%">IMSI</td>
								<td width="8%">ICCID</td>
								<td width="5%">用户状态</td>
								<td width="5%">工作状态</td>
								<td width="10%">本月累计使用流量(MB)</td>
								<td width="6%">本月累计使用短信(条)</td>
								<td width="9%">开户日期</td>
								<td width="6%">是否签约短信服务</td>
								<td width="6%">是否签约GPRS服务</td>
								<td width="10%">套餐资费</td>
								<td width="5%">本月总量</td>
								<td width="5%">备注</td>
							</tr>
						</thead>
					</table>
				</div>
				<div class="main-list-cont">
					<table width="100%" cellspacing="0" cellpadding="0">
						<tbody>
							<c:forEach var="order" items="${list}" varStatus="p">
								<tr>
								   <td width="2%" style="text-align: center" >
										<div class="btn-group" >
											<div id="selected-color_${order.id}" class="btn btn-mini dropdown-toggle" style="padding: 6 6 6 6px;background-color: ${order.status}"
												data-toggle="dropdown"  >
												  
											</div>
											<ul class="dropdown-menu" style="width: 293px;">
												<li style="display: inline-block;">
													<div>&nbsp;</div>
													<div id="colorpalette_${order.id}"></div>
												</li>
											</ul>
										</div>
									</td>
									<td width="6%"
										style="word-wrap: break-word; word-break: break-all;">${order.cardCode}</td>
									<td width="8%"
										style="word-wrap: break-word; word-break: break-all;">${order.IMSI}</td>
									<td width="8%"
										style="word-wrap: break-word; word-break: break-all;">${order.ICCID}</td>
									<td width="5%"
										style="word-wrap: break-word; word-break: break-all;">${order.userStatus}</td>
									<td width="5%"
										style="word-wrap: break-word; word-break: break-all;">${order.cardStatus}</td>
									<td width="10%"
										style="word-wrap: break-word; word-break: break-all;">${order.gprsUsed}</td>
									<td width="6%"
										style="word-wrap: break-word; word-break: break-all;">${order.messageUsed}</td>
									<td width="9%"
										style="word-wrap: break-word; word-break: break-all;">${order.openDate}</td>
									<td width="6%"
										style="word-wrap: break-word; word-break: break-all;">${order.withMessageService}</td>
									<td width="6%"
										style="word-wrap: break-word; word-break: break-all;">${order.withGPRSService}</td>
									<td width="10%"
										style="word-wrap: break-word; word-break: break-all;">${order.packageType}</td>
									<td width="5%"
										style="word-wrap: break-word; word-break: break-all;">${order.monthTotalStream}</td>
									<td width="5%"
										style="word-wrap: break-word; word-break: break-all;">${order.remark}</td>
								</tr>
								<script>
											$('#colorpalette_' + "${order.id}"+ '').colorPalette().on('selectColor', function(e) {
												$('#selected-color_' + "${order.id}" ).css('background-color', e.color);
												$('#status_' + "${order.id}" ).val(e.color);
												updateStatus("${order.id}" , e.color);
											});
								</script>
							</c:forEach>

						</tbody>
					</table>
				</div>
			</div> 
		<div >
			<nav>
				<ul class="pagination pagination-sm " style="width: 100%">
					<li ><a href="###" onclick = "query(1)">首页</a></li>
					<li ><a href="###" onclick = "queryLast()"><<</a></li>
					<li class="list-bottom" >第<input type="text" style="width: 24px ;text-align:center;margin:0 3px;" id="pageNo"
						name="pagination.pageNo" onchange="query(this.value)" value="${pagination.pageNo}" />页
					</li>
					<li ><a href="###" onclick = "queryNext()">>></a></li>
					<li ><a href="###" onclick = "query(${pagination.pageIndex})">尾页</a></li>
					<li class="list-bottom">共 ${pagination.pageIndex}  页
					</li>
					<li class="list-bottom"><input type="hidden" name = "pagination.total" value=" ${pagination.total}">共 ${pagination.total} 条</li>
				</ul>
				
			</nav>
		</div>
		</div>
		
	</form>
<!-- 模态框（Modal） -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <iframe frameborder="0" width="100%" height="250px;" id="iframeDialog" name="iframeDialog"
                    marginwidth="0" marginheight="0" frameborder="0" scrolling="no"></iframe>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
</body>
</html>
