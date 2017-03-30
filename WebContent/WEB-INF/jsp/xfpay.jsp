<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<link href="${basePath}/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="${basePath}/css/style.css" />
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="${basePath}/js/jquery-3.1.1.min.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="${basePath}/js/bootstrap.min.js"></script>
</head>
<body>
<div class="container-fluid">
			<div class="container user-name" style="border: 1px solid #00C7FE; padding: 20px; -webkit-border-radius: 10px 10px 10px 10px;">
				<h3 style="text-align: center;">ICCID号</h3>
				<h3 style="text-align: center; margin-top: 20px;">${iccid}</h3>
				<p style="text-align: center; margin-top: 10px; color: #fd4a4a;">请仔细核对ICCID号，支付后将无法撤回</p>
			</div>
			<div role="tabpanel" class="tab-pane active" style="margin-top: 10px;">
				<div class="taocan">
					<h3>30M（首年激活套餐）</h3>
					<p>30M流量，流量不清零，一年有效，全国通用总流量用完即停机，可累加包年套餐。</p>
					<h3 style="float: left;">支付金额：￥36</h3>
				</div>
			</div>

			<div class="row dingwei">
				<p class="text-center">
					<button style="border: none;" type="button" class="btn btn-primary btn-lg  col-xs-6" onclick="doWeixinPay();">提交订单</button>
				</p>
			</div>
		</div>
	 <script type="text/javascript">
	    function doWeixinPay(){
	    		window.location.href="${basePath}/wx/userAuth?totalFee="+ 36 + "&iccid=${iccid}";
	    }
	 </script>

</body>
</html>
