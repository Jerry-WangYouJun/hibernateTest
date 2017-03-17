<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="${basePath}/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="${basePath}/css/style.css" />
<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
<script src="${basePath}/js/jquery-3.1.1.min.js"></script>
<!-- Include all compiled plugins (below), or include individual files as needed -->
<script src="${basePath}/js/bootstrap.min.js"></script>
<title>中国移动</title>
</head>
<body>
		<div class="container-fluid">
			<div class="row">
				<div class="user-head">
					<div class="inbox-avatar">
						<img src="${basePath}/img/logo.png" alt="">
					</div>
					<button type="button" class="btn btn-info pull-right" onclick="window.location.href='${basePath}/search.html'">切换</button>
				</div>
				<div class="container user-name">
					<h3>${info.IMSI }<span>[${info.userStatus }]</span></h3>
					<p>剩余时间313天<span>(到期时间  ${info.openDate} )</span></p>
				</div>
			</div>
			<div class="row container">
				<section class="panel">
					<div class="twt-feed blue-bg">
						<p>剩余流量</p>
						<h1>${info.detail.gprsrest }<span>MB</span></h1>
						<div class="weather-category twt-category">
							<ul>
								<li class="active">
									<h5>总流量</h5>${info.detail.gprs }MB
								</li>
								<li>
									<h5>已用流量</h5>${info.detail.gprsused }MB
								</li>
							</ul>
						</div>
					</div>

				</section>
			</div>
			<div class="row container">
				<ul class="nav nav-tabs" role="tablist">
					<li role="presentation" class="col-md-6 col-xs-6 active">
						<a href="#home" role="tab" data-toggle="tab">当前套餐</a>
					</li>
					<li role="presentation" class="col-md-6 col-xs-6">
						<a href="#profile" role="tab" data-toggle="tab">已续费套餐</a>
					</li>
				</ul>

				<!-- Tab panes -->
				<div class="tab-content" style="margin-bottom: 93px;">
					<div role="tabpanel" class="tab-pane active" id="home">
						<c:forEach items = "${info.packageList }" var = "p" >
						    <div>
								<h3> ${ p.packageName } </h3>
								<p> ${ p.remark}  </p>
								<hr />
							</div>
						</c:forEach>
					</div>
					<div role="tabpanel" class="tab-pane" id="profile">
						<c:forEach items = "${info.packageList }" var = "p" >
						    <div>
								<h3> ${ p.packageName } (已续费) </h3>
								<p> ${ p.remark}  </p>
								<hr />
							</div>
						</c:forEach>
					</div>
				</div>
			</div>
			<div class="row dingwei" >
				<p class="text-center">
					<button type="button" class="btn btn-primary btn-lg  col-xs-6" onclick="window.location.href='http://localhost:8080/xinfu_wechat_pay'">充值续费</button>
				</p>
				<p class="text-center">
					<a href="${basePath}/card/search?imsi=${info.IMSI}">历史续费查询</a>
				</p>
				
			</div>
		</div>

</body>
</html>