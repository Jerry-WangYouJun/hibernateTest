<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@include file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户管理</title>
<script type="text/javascript">
	$(function() {
		$('#data-table').datagrid({
			rownumbers : true,
			autoRowHeight : true,
			singleSelect : true,
			pagination : true,
			nowrap : false
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
	});

	function doSearch() {
		var type = $("#search-type").val();
		var iccidStart = $("#search-iccidStart").val();
		var iccidEnd = $("#search-iccidEnd").val();
		window.location.href = "${basePath}/agent/agent_query?type=" + type +
				"&iccidStart="+iccidStart + "&iccidEnd" + iccidEnd;
	}
	function doClear() {
		$("#search-type").val("");
		$("#search-iccidStart").val("");
		$("#search-iccidEnd").val("");
	}
</script>

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
	<div region="center" border="0">
		<form:form id="dataForm" action="${basePath}/user/user_delete"
			modelAttribute="user" method="post">
			<input type="hidden" name="_method" value="DELETE" />
			<table class="easyui-datagrid" id="data-table">
				<thead>
					<tr>
						<th data-options="field:'iccid'">iccid</th>
						<th data-options="field:'money'">money</th>
						<th data-options="field:'packageType'">packageType</th>
						<th data-options="field:'update_date'">update_date</th>
						<th data-options="field:'kickback'">kickback</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${list}" var="card">
						<tr>
							<td>${card.iccid}</td>
							<td>${card.money}</td>
							<td>${card.packageType}</td>
							<td>${card.update_date}</td>
							<td>${card.kickback}</td>
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