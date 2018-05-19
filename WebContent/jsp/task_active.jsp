<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<jsp:include page="/common.jsp"></jsp:include>
<script type="text/javascript">
	   function taskActive(){
		   var url = "${basePath}/task/active" ;
			$.ajax({
				url : url,
				data :{
				   
					},
				dataType : 'json',
				success : function(data) {
					$("#money").text(data);
				},
			});
	   }
</script>
<body>
	   
	   <a href="#" onclick="taskActive()">执行任务</a>
</body>
</html>