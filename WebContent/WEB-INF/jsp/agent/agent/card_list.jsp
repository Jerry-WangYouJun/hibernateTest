<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@include file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- <script type="text/javascript" src="http://www.w3cschool.cc/try/jeasyui/datagrid-detailview.js"></script> -->
<title>用户管理</title>
<script type="text/javascript">
	$(function() {
		$('#data-table').datagrid({
			rownumbers : true,
			autoRowHeight : true,
			singleSelect : true,
			pagination : true,
			nowrap : false,
			fit: true, 
			pagePosition: 'both'
		});
		$('#data-table').datagrid('getPager').pagination({  
            pageSize: "${page.pageSize}",  
            pageNumber: "${page.pageNo}",  
            pageList: [3 , 10, 30, 50 , 100],  
            beforePageText: '第',//页数文本框前显示的汉字   
            afterPageText: '页    共 ${page.pageIndex} 页    ${page.total} 条记录',  
            showRefresh:false ,
       });
		

		 $(".pagination-num").val("${page.pageNo}");
		
		 $(".pagination-num").change(function(){
			 doSearch();  
		 });
		 $(".pagination-page-list").change(function(){
			 doSearch();  
		 });
		 $(".pagination-first").click(function(){
			 doSearch("1");  
		 });
		 $(".pagination-prev").click(function(){
			 doSearch("prev");  
		 });
		 $(".pagination-next").click(function(){
			 doSearch("next");  
		 });
		 $(".pagination-last").click(function(){
			 doSearch("last");  
		 });
		 
		$('#dlg-frame').dialog({
			title : '代理商管理',
			width : 700,
			height : 320,
			top : 50,
			left : 100,
			closed : true,
			cache : false,
			modal : true,
			buttons : [ {
				text : '确定',
				iconCls : 'icon-ok',
				handler : function() {
					if (confirm("确定执行下一步操作？")) {
						frameContent.window.doServlet();
					}
				}
			}, {
				text : '关闭',
				iconCls : 'icon-cancel',
				handler : function() {
					$('#dlg-frame').dialog("close");
				}
			} ]
		});
		 $("#datagridDiv").height($(".layout-panel-center")[0].offsetHeight - 200);
	});

	function doSearch(index) {
		var type = $("#search-type").val();
		var iccidStart = $("#search-iccidStart").val();
		var iccidEnd = $("#search-iccidEnd").val();
		var pageNo = $(".pagination-num").val(); 
		var pageSize = $(".pagination-page-list").val();
		var pageTotal = ${page.pageIndex};
		alert(iccidStart);
		if(index == "1"){
			pageNo = 1 ;
		}else if(index == "prev" && pageNo != 1 ){
			 pageNo  -= 1 ;
		}else if(index == "next" && pageNo != pageTotal){
			pageNo  = parseInt(pageNo)+parseInt(1); 
		}else if(index == "last" ){
			pageNo  = pageTotal; 
		}
		window.location.href = "${basePath}/treeindex/card/${agentid}?type=" + type +
				"&iccidStart="+iccidStart + "&iccidEnd=" + iccidEnd + 
				"&pageNo=" + pageNo + "&pageSize=" + pageSize ;
	}
	function doClear() {
		$("#search-type").val("");
		$("#search-iccidStart").val("");
		$("#search-iccidEnd").val("");
	}
	function addUser() {
		var path = "${basePath}/agent/addInit";
		document.getElementById('frameContent').src = path;
		$('#dlg-frame').dialog('open');
	}

	function updateUser() {
		var id = getChecked();
		if (id > 0) {
			var path = "${basePath}/agent/updateInit/" + id;
			document.getElementById('frameContent').src = path;
			$('#dlg-frame').dialog('open');
		}
	}

	function deleteUser() {
		var del = confirm("确认删除？");
		if (!del) {
			return false;
		}
		var id = getChecked();
		if (id > 0) {
			var url = "${basePath}/agent/agent_delete/" + id;
			$.ajax({
				url : url,
				type : 'post',
				data : $("#dataForm").serialize(),
				dataType : 'json',
				success : function(data) {
					if (data.success) {
						$.messager.alert('提示', data.msg);
						doSearch();
					} else {
						$.messager.alert('提示', data.msg, "error");
					}

				},
				error : function(transport) {
					$.messager.alert('提示', "系统产生错误,请联系管理员!", "error");
				}
			});
		}
	}

	function getChecked() {
		var id;
		var checkTotal = 0;
		$("input[type=checkbox]").each(function() {
			if (this.checked) {
				id = $(this).val();
				checkTotal++;
			}
		});
		if (checkTotal == 0) {
			alert("请选中一条数据！");
			return 0;
		} else if (checkTotal > 1) {
			alert("只能选择一条数据！");
			return 0;
		}
		return id;
	}
</script>
<style type="text/css">
.datagrid-view{
	height : 2000px;
}

</style>
</head>
<body class="easyui-layout">
	<div id="tb" region="north" title="查询条件区" class="easyui-panel"
		iconCls="icon-search" style="padding: 3px; height: 60px; width: 86%">
		<span>套餐类型:</span> <input id="search-type" name="type" /> 
		<span>ICCID:</span>
		<input id="search-iccidStart" name="iccidStart" /> - 
		<input
			id="search-iccidEnd" name="iccidEnd" />
		<a href="####"
			class="easyui-linkbutton" plain="true" iconCls="icon-search"
			onclick="doSearch()">查询</a> <a href="####" class="easyui-linkbutton"
			plain="true" iconCls="icon-clear" onclick="doClear()">清除</a>
	</div>
	<div id="datagridDiv" region="center" border="0" style="height: 2800px">
		<form:form id="dataForm" action="${basePath}/user/user_delete"
			modelAttribute="user" method="post">
			<input type="hidden" name="_method" value="DELETE" />
			<table class="easyui-datagrid" id="data-table"  title="数据列表" width="86%">
				<thead>
					<tr>
						<th data-options="field:'id'"></th>
						<th data-options="field:'cardCode'">cardCode</th>
						<th data-options="field:'ICCID'">ICCID</th>
						<th data-options="field:'gprsUsed'">gprsUsed</th>
						<th data-options="field:'packageType'">packageType</th>
						<th data-options="field:'openDate'">openDate</th>
						<th data-options="field:'remark'">remark</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${list}" var="card">
						<tr>
							<td><input type="checkbox" name="id" value="${card.id}" /></td>
							<td>${card.cardCode}</td>
							<td>${card.ICCID}</td>
							<td>${card.gprsUsed}</td>
							<td>${card.packageType}</td>
							<td>${card.openDate}</td>
							<td>${card.remark}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

		</form:form>

		<div id="dlg-frame">
			<iframe width="99%" height="98%" name="frameContent"
				id="frameContent" frameborder="0"></iframe>
		</div>
	</div>


</body>
</html>