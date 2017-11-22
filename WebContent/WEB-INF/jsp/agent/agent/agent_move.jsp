<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@include file="/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户管理</title>
<style type="text/css">     
    .mask {       
            position: absolute; top: 0px; filter: alpha(opacity=60); background-color: #777;     
            z-index: 1002; left: 0px;     
            opacity:0.5; -moz-opacity:0.5;     
        }     
</style>  
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
				text : '分配',
				iconCls : 'icon-add',
				handler : function() {
					moveCard();
				}
			}]
		});
		
		$('#data-table').datagrid('getPager').pagination({  
            pageSize: "${page.pageSize}",  
            pageNumber: "${page.pageNo}",  
            pageList: [3 , 10, 30, 50],  
            beforePageText: '第',//页数文本框前显示的汉字   
            afterPageText: '页    共 ${page.pageIndex} 页',  
            displayMsg: '  共 ${page.total} 条记录',  
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
	});

	function doSearch(index) {
		var type = $("#search-type").val();
		var iccidStart = $("#search-iccidStart").val();
		var iccidEnd = $("#search-iccidEnd").val();
		var pageNo = $(".pagination-num").val(); 
		var pageSize = $(".pagination-page-list").val();
		var pageTotal = ${page.pageIndex};
		if(index == "1"){
			pageNo = 1 ;
		}else if(index == "prev" && pageNo != 1 ){
			 pageNo  -= 1 ;
		}else if(index == "next" && pageNo != pageTotal){
			pageNo  = parseInt(pageNo)+parseInt(1); 
		}else if(index == "last" ){
			pageNo  = pageTotal; 
		}
		window.location.href = "${basePath}/agent/agent_query?type=" + type +
				"&iccidStart="+iccidStart + "&iccidEnd" + iccidEnd +
				"&pageNo=" + pageNo + "&pageSize=" + pageSize ;
	}
	
	function doClear() {
		$("#search-type").val("");
		$("#search-iccidStart").val("");
		$("#search-iccidEnd").val("");
	}
	function moveCard() {
			var del = confirm("确认转移？");
			if (!del) {
				return false;
			}
			var id = getChecked();
			if (id > 0) {
				var url = "${basePath}/agent/card_move";
				$.ajax({
					url : url,
					type : 'post',
					data : {iccids:"${ids}" , agentid : id},
					dataType : 'json',
					beforeSend: function () {
					    $.messager.progress({ 
					       title: '提示', 
					       msg: '操作中，请稍候……', 
					       text: '' 
					    });
					    },
					complete: function () {
					         $.messager.progress('close');
					},
					success : function(data) {
						if (data.success) {
							parent.doSearch();
			       			parent.$('#dlg-frame').dialog("close");
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
	<div id="mask" region="center" border="0">
		<form:form id="dataForm" action="${basePath}/user/user_delete"
			modelAttribute="user" method="post">
			<input type="hidden" name="_method" value="DELETE" />
			<table class="easyui-datagrid" id="data-table"  title="数据列表" width="86%" >
				<thead>
					<tr>
						<th data-options="field:'id'"></th>
						<th data-options="field:'name'">代理商</th>
						<th data-options="field:'code'">代理商代码</th>
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
							<td>${agent.code}</td>
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