<%@ page contentType="text/html; charset=UTF-8"%>

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
System.out.println("in jsapi.jsp");
%>
<!DOCTYPE html>
<html>
	<head>
		<script src="/js/jquery-3.1.1.min.js"></script>
		<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
		<link href="${basePath}/css/bootstrap.min.css" rel="stylesheet">
		<link rel="stylesheet" href="${basePath}/css/style.css" />
		<script src="${basePath}/js/bootstrap.min.js"></script>
		<script type="text/javascript">  
		
		
		wx.config({
		    //debug: true, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
		    appId: '${appid}', // 必填，公众号的唯一标识
		    timeStamp: "${timeStamp}", // 必填，生成签名的时间戳
		    nonceStr: '${nonceStr}', // 必填，生成签名的随机串
		    signature: '${sign}',// 必填，签名，见附录1
		    jsApiList: ['chooseWXPay'] // 必填，需要使用的JS接口列表，这里只写支付的
		});
		function pay(){
				wx.chooseWXPay({
					debug: true,
				    timestamp: "${timeStamp}",
				    nonceStr: '${nonceStr}', 
				    package: "${packageValue}", 
				    signType: 'MD5',
				    paySign :  "${sign}", 
				    success: function (res) {
				        alert("支付成功");
				        alert("${pageContext.request.contextPath}/wx/success?orderId=${orderId}");
				        window.location.href = "${pageContext.request.contextPath}/wx/success?orderId=${orderId}";
				    },
				    fail:function(res){
				    	alert(JSON.stringify(res));
				    },
				    error:function(res){
				    	alert(res);
				    }
			});
				
		}
				/*
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
			    	   alert(res);
			           if(res.err_msg == "get_brand_wcpay_request:ok" ) {
			        	   alert(123);
			        	   //window.location.href=$("#path").val()+url;
			           }     // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。 
			           else{
			        	   $.alert("fail");
			           }
			       }
			   ); 
			}*/
		</script>	
	    <title>订单-支付</title>
	</head>
<body>
    	<div class="container-fluid">
			<div class="row">
				<!-- Tab panes -->
				<form  action = "/card/querySingle" method="post">
				<br/><br/><br/><br/>
					<div class="col-lg-12">
						<!-- /input-group -->
						<button type="submit" class="btn btn-primary btn-lg btn-block" style="border: none;" onclick="pay();" >确认支付</button>
					</div>
				</form>
				<!-- /.col-lg-6 -->
			</div>
			<!-- /.row -->
		</div>
</body>


</html>
