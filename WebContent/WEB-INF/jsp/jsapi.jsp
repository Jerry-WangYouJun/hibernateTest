<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
System.out.println("in jsapi.jsp");
%>
<!DOCTYPE html>
<html>
	<head>
		<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<link href="${basePath}/css/bootstrap.min.css" rel="stylesheet">
		<link rel="stylesheet" href="${basePath}/css/style.css" />
		<script src="${basePath}/js/jquery-3.1.1.min.js"></script>
		<script src="${basePath}/js/bootstrap.min.js"></script>
		<script type="text/javascript">  
			function pay(){
				 var timestamp = "${timeStamp}";
				var nonceStr = '${nonceStr}';
				var appid = '${appid}';
				var paySign = '${sign}';
				var packages = "${packageValue}";
				var url = "${pageContext.request.contextPath}/wx/success?orderId=${orderId}";
				WeixinJSBridge.invoke('getBrandWCPayRequest', {
			           "appId" : appid,     //公众号名称，由商户传入     
			           "timeStamp" : timestamp,  //时间戳，自1970年以来的秒数     
			           "nonceStr" : nonceStr, //随机串     
			           "package" : packages,     
			           "signType" : "MD5",  //微信签名方式：     
			           "paySign" : paySign //微信签名 
			       },
			       function(res){
			           if(res.err_msg == "get_brand_wcpay_request:ok" ) {
			        	  // alert(123);
			        	   window.location.href="${pageContext.request.contextPath}/wx/success?orderId=${orderId}";
			           }     // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。 
			           else{
			        	   $.alert("fail");
			           }
			       }
			   ); 
			}
		</script>	
	    <title>订单-支付</title>
	</head>
<body>
    	<div class="container-fluid">
			<div class="row">
				<!-- Tab panes -->
				<br/><br/><br/><br/>
					<div class="col-lg-12">
						<!-- /input-group -->
						<button type="button" class="btn btn-primary btn-lg btn-block" style="border: none;" onclick="pay();" >确认支付</button>
					</div>

				<!-- /.col-lg-6 -->
			</div>
			<!-- /.row -->
		</div>
</body>


</html>
