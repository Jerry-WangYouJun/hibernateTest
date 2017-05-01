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
			//url : '${basePath}/agent/agent_query',
			rownumbers : true,
			autoRowHeight : true,
			singleSelect : true,
			pagination : true,
			nowrap : false,
			toolbar : [ {
				text : '添加',
				iconCls : 'icon-add',
				handler : function() {
					addUser();
				}
			}, '-', {
				text : '修改',
				iconCls : 'icon-edit',
				handler : function() {
					updateUser();
				}
			}, '-', {
				text : '删除',
				iconCls : 'icon-remove',
				handler : function() {
					deleteUser();
				}
			} ]
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
						<th data-options="field:'id'"></th>
						<th data-options="field:'name'">代理商</th>
						<th data-options="field:'cost'">成本价</th>
						<th data-options="field:'renew'">续费价</th>
						<th data-options="field:'type'">套餐类型</th>
						<th data-options="field:'creater'">创建者</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${list}" var="agent">
						<tr>
							<td><input type="checkbox" name="id" value="${agent.id}" /></td>
							<td>${agent.name}</td>
							<td>${agent.cost}</td>
							<td>${agent.renew}</td>
							<td>${agent.type}</td>
							<td>${agent.creater}</td>
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