<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title></title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
   <jsp:include page="/common.jsp"></jsp:include>
  </head>
  <body id="mainpanel" >
 	<div id="tb" region="north" title="查询条件区" class="easyui-panel"  iconCls="icon-search"  style="padding:5px; height:60px; width: 80%;margin-bottom: 3px "  >
		<span>套餐名:</span>
		<input id="search-typename" name="typename"/>
		<a  href="javascript:void(0)"
			class="easyui-linkbutton" plain="true" iconCls="icon-search"
			onclick="doSearch()">查询</a> 
	</div>
 	<div id="main_layout" data-options="region:'center',border:false,showHeader:false" style="width:80%;height:70%;">
 		<table id="company_table" class="easyui-datagrid" fit="true" ></table>
 	</div>
	<div data-options="region:'south',border:false,showHeader:false" style="width:900px;height:20%;">
 		 &nbsp;
 	</div>
    <script type="text/javascript">
    	$(function(){
			$('#company_table').datagrid({
				url:'${basePath}/pac/query',
				rownumbers : true,
				pagination: true,
				fitColumns: true,
				singleSelect: true,
				columns:[[
					{field:'id',align : 'center',halign:'center',checkbox : true}, 
					{field:'typename',title:'套餐名称',align:'center'},
					{field:'discrip',title:'套餐描述',align:'center'},
					{field:'cost',title:'成本价',align:'center'},				
					{field:'renew',title:'续费价',align:'center'},
					{field:'remark',title:'备注',align:'center'}
				]],
				toolbar: [
				{
					text:'添加',
					iconCls: 'icon-add',
					handler: function(){addUser();}
				},'-',{
					text:'修改',
					iconCls: 'icon-edit',
					handler: function(){updateUser();}
				},'-',{
					text:'删除',
					iconCls: 'icon-remove',
					handler: function(){deleteUser();}
				},'-',{
					text:'修改密码',
					iconCls: 'icon-mini-edit',
					handler: function(){updatePassword();}
				}]
			});
	    	$('#company_table').datagrid('getPager').pagination({  
	            pageSize: 10,  
	           /*  pageNumber: "${page.pageNo}", */ 
	            pageList: [ 5 ,10 , 30, 50],  
	           /*  beforePageText: '第',//页数文本框前显示的汉字   
	            afterPageText: '页    共 ${page.pageIndex} 页',  
	            displayMsg: '  共 ${page.total} 条记录',   */
	            showRefresh:false ,
	            onSelectPage:function(pageNumber, pageSize){
	            		doSearch();
		        	}
	    });
	    	
	    	$("input:checkbox:not(input:checkbox[name])").click(function(){
	    			      $("input:checkbox").each(function(){
	    			    	   	     $(this).attr("checked","checked");
	    			      })
	    		});
	    	
	    	$('#dlg-frame').dialog( {
				title : '用户管理',
				width :  700,
				height : 320,
				top:50,
				left:100,
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
    	
	    	function doSearch(){
	    		var pageNo = $(".pagination-num").val(); 
			var pageSize = $(".pagination-page-list").val();
	    		var typename = $("#search-typename").val();
		    $('#company_table').datagrid('reload',{
		    		typename : typename
			} );
		 }
	    	function addUser() {
	    		var path = "${basePath}/pac/addInit";
	    		document.getElementById('frameContent').src = path;
	    		$('#dlg-frame').dialog('open');
	    	}

	    	function updateUser() {
	    		var id = getChecked();
	    		if (id > 0) {
	    			var path = "${basePath}/pac/updateInit/" + id;
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
	    			var url = "${basePath}/pac/pac_delete/" + id;
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
	    	
	    	function updatePassword(){
				var id = $('#company_table').datagrid('getSelected').id;
				if (id > 0) {
					var path = "${basePath}/updateInit/" + id;
					document.getElementById('frameContent').src = path;
					$('#dlg-frame').dialog('open');
				}
			}
	    	
			function getChecked() {
				var id;
				var checkTotal = 0;
				$("input[type=checkbox]").each(function() {
					if (this.checked && $(this).val() != "on") {
						id = $(this).val();
						checkTotal++;
					}
				});
				if (checkTotal == 0) {
					$.messager.alert('提示', "请先选中一行(只允许单行操作)", 'error');
					return 0;
				} else if (checkTotal > 1) {
					$.messager.alert('提示', "只能选中一行(只允许单行操作)", 'error');
					return 0;
				}
				return id;
			}
    </script>
    
    <div id="dlg-frame">
			<iframe width="99%" height="98%" name="frameContent" id="frameContent"
				frameborder="0"></iframe>
	</div>
</body>
</html>
