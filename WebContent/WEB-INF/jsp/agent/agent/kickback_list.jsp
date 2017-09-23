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
			nowrap : false,
			fitColumns  : true,
			pagePosition: 'top'
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
	});

	function doSearch(index) {
		var type = $("#search-type").val();
		var dateStart = document.getElementsByName("dateStart")[0].value;
		var dateEnd = document.getElementsByName("dateEnd")[0].value;
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
		window.location.href = "${basePath}/treeindex/kickback/${agentid}?dateStart="+dateStart + 
				"&dateEnd=" + dateEnd + "&pageNo=" + pageNo + "&pageSize=" + pageSize + "&timeType=" + $("#timeType").val();
	}
	function doClear() {
		$("#search-type").val("");
		$("#search-dateStart").val("");
		$("#search-dateEnd").val("");
	}
</script>

</head>
<body class="easyui-layout">
	<div id="tb" region="north" title="查询条件区" class="easyui-panel"
		iconCls="icon-search" style="padding: 3px; height: 60px; width: 86%">
		<span>查询时间:</span>
		<input id="search-dateStart" name="dateStart" class="easyui-datebox"  type="text" /> - 
		<input
			id="search-dateEnd" name="dateEnd" class="easyui-datebox"  type="text"/>
		<span>查询固定时间</span>
		<select id="timeType"  name="timeType">
			  <option value="0">--请选择--</option>
			  <option value="7">--本周--</option>
			  <option value="30">--本月--</option>
			  <option value="60">--上月--</option>
		</select>
		<a href="####"
			class="easyui-linkbutton" plain="true" iconCls="icon-search"
			onclick="doSearch()">查询</a> 
		<a href="####" class="easyui-linkbutton"
			plain="true" iconCls="icon-clear" onclick="doClear()">清除</a>
	</div>
	<div id = "datagridDiv" region="center" border="0"  >
		<form:form id="dataForm" action="${basePath}/user/user_delete"
			modelAttribute="user" method="post">
			<input type="hidden" name="_method" value="DELETE" />
			<table class="easyui-datagrid" id="data-table" width="85%" height="400px">
				<thead>
					<tr>
						<th data-options="field:'iccid'">ICCID</th>
						<th data-options="field:'money'">充值金额</th>
						<th data-options="field:'packageType'">套餐类型</th>
						<th data-options="field:'update_date'">充值时间</th>
						<th data-options="field:'kickback'">返佣</th>
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
	      <table >
				<thead>
					<tr>
						<th >充值总金额</th>
						<th >${sumKick }</th>
					</tr>
				</thead>
			</table>

		<div id="dlg-frame">
			<iframe width="99%" height="98%" name="frameContent"
				id="frameContent" frameborder="0"></iframe>
		</div>
	</div>


</body>
</html>