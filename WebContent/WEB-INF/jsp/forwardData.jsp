<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page isELIgnored="false"%>
<%@include file="/common/taglibs.jsp"%>
<html>
<head>
	<link href="${basePath}/css/bootstrap.min.css" type="text/css"
	rel="stylesheet" />
	<script type="text/javascript" src="${basePath}/js/jquery-3.1.1.min.js"></script>
   	<script type="text/JavaScript" src="${basePath}/js/jquery.form.js"></script>
	<script type="text/javascript" src="${basePath}/js/bootstrap.min.js"></script>

<title>生成跳转网址</title>
<script type="text/javascript">
	function exportInit(){
		 $("#myModalLabel").html("导入模板");
	    $("#iframeDialog").attr("src", "${basePath}/uploadExcel/uploadInit");
	    $("#myModal").modal({backdrop:"static"});
	}
	
	   //关闭Modal框
    function closeModal() {
        $("#myModal").modal('hide');
    }
</script>
   <style type="text/css">
     .h.jumbotron{
         padding: 10px;
     } 
   </style>
</head>

    <div class="container theme-showcase" role="main">
      <!-- Main jumbotron for a primary marketing message or call to action -->
      <div class="h jumbotron" >
        <h3>卡信息管理</h3>
      </div>
      	<form class="form-horizontal" role="form">
               <div class="form-group">
                  <label class="col-sm-1 control-label" for="ds_host">ICCID</label>
                  <div class="col-sm-3">
                     <input class="form-control" id="ds_host" type="text" placeholder="查询的iccid"/>
                  </div>
                  <label class="col-sm-1 control-label" for="ds_name"> 网址类型 </label>
                  <div class="col-sm-3">
                     <select  class="form-control">
				      <option>1</option>
				      <option>2</option>
				    </select>
                  </div>
                  <div class="col-sm-1">
	                  <button type="button" class="btn btn-primary ">查询</button>
                  </div>
                  <div class="col-sm-1">
		       		  <button type="button" class="btn btn-success">重置</button>
                  </div>
                  <div class="col-sm-1">
	                  <button type="button" class="btn btn-primary " onclick="exportInit()">导入生成数据</button>
                  </div>
               </div>
         </form>
      <div class="row">
        <div class="col-md-12">
          <table class="table table-bordered">
            <thead>
              <tr>
                <th>#</th>
                <th>First Name</th>
                <th>Last Name</th>
                <th>Username</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td rowspan="2">1</td>
                <td>Mark</td>
                <td>Otto</td>
                <td>@mdo</td>
              </tr>
              <tr>
                <td>Mark</td>
                <td>Otto</td>
                <td>@TwBootstrap</td>
              </tr>
              <tr>
                <td>2</td>
                <td>Jacob</td>
                <td>Thornton</td>
                <td>@fat</td>
              </tr>
              <tr>
                <td>3</td>
                <td colspan="2">Larry the Bird</td>
                <td>@twitter</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>



      <div class="well">
        <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas sed diam eget risus varius blandit sit amet non magna. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Praesent commodo cursus magna, vel scelerisque nisl consectetur et. Cras mattis consectetur purus sit amet fermentum. Duis mollis, est non commodo luctus, nisi erat porttitor ligula, eget lacinia odio sem nec elit. Aenean lacinia bibendum nulla sed consectetur.</p>
      </div>


    </div> 
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
