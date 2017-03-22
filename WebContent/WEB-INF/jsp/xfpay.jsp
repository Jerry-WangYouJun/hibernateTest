<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/jquery.mobile-1.3.0.min.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/jquery.mobile-new.css" />
   <link href="${basePath}/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="${basePath}/css/style.css" />
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="${basePath}/js/jquery-3.1.1.min.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="${basePath}/js/bootstrap.min.js"></script>
</head>
<body>
<div data-role="page">
  <div data-role="content">
    <form method="post">
      <div data-role="fieldcontain">
        <!-- <label for="paymoney">支付金额：</label>
        <input type="text" name="paymoney" id="paymoney" placeholder="请输入支付金额.." value = "36" disabled="disabled">       
        <p id="p1">你输入的金额为：</p>
        <p>微信支付需要加收1%手续费</p>
        <p id="p3">合计需要支付金额</p> -->
        <br><br><br><br><br>
        <h1>支付功能升级中。。</h1>
        
      </div>
    </form>
  </div>
</div>
<div class="container-fluid">
			<div class="row dingwei" >
				<p class="text-center">
					<button type="button" class="btn btn-primary btn-lg  col-xs-6" onclick="window.location.href='${basePath}/card/xinfu_wechat_pay'">充值</button>
				</p>
			</div>
		</div>
     <script type="text/javascript" src="${basePath}/static/js/jquery.js"></script>
     <script type="text/javascript" src="${basePath}/static/js/jquery.mobile-1.3.0.min.js"></script>
     <script>
		$(document).ready(function(){
			$("#paymoney").change(function(){
              var pay=$("#paymoney").val();
              var rate = ${rate};
			  $("#p1").html('你输入的金额为：'+pay+'元');
			  $("#p3").html('合计需要支付金额'+parseFloat((pay*rate).toPrecision(12))+'元');
			});
		});
     </script>
	 <script type="text/javascript">
	    function doWeixinPay(){
	    	var pay=$("#paymoney").val();
	    	if(pay == null || pay == ""){
	    		alert("请输入支付金额！");
	    		return;
	    	}else{
	    		var rate = ${rate};
	    		pay = parseFloat((pay*rate).toPrecision(12)); 
	    		window.location.href="${pageContext.request.contextPath}/wx/userAuth?totalFee="+pay;
	    	}
	    }
	 </script>

</body>
</html>
