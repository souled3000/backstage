<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%--<spring:message code="key" /> --%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">    
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<title>IoT</title>
<link type="text/css" rel="stylesheet" href="./lib/bootstrap-responsive.min.css">
<link type="text/css" rel="stylesheet" href="./lib/bootstrap.min.css">
<script type="text/javascript" src="./lib/bootstrap.min.js"></script>
<style>
<!--
form {
	margin-left: 72px;
	margin-top: 56px;
	width: 210px;
	heigh: 210px;
}

.d1 {
	height : 343px;
	margin-top:74px;
	width: 355px;
	border-style: groove groove groove groove;
	border-color: #0976AD;
	border-width: 1px;
	border-radius: 9px;
}
.padding_botton_1{
	margin-top:0px;
	margin-bottom:0px;
	padding-top:0px;
	padding-left:0px;
	padding-bottom:38px;
}
.padding_botton_2{
	margin-top:0px;
	margin-bottom:0px;
	padding-top:0px;
	padding-left:0px;
	padding-bottom:10px;
}
.padding_botton_3{
	margin-top:0px;
	margin-bottom:0px;
	padding-top:0px;
	padding-left:0px;
	padding-bottom:18px;
}

.btn_1{
	height:38px;
	font-size:8;
}
.error{
	height:38px;
	font-size:small;
	color:orange;
	font-weight:bolder;
}
-->
</style>
</head>

<body style="background-color: #0B7EB7;margin:0;">
	<div class="d1 center-block">
		<form:form modelAttribute="user" class="form-horizontal" role="form" >
			<div class="form-group padding_botton_1">
				<img src="./lib/logo.png">
			</div>
			<div class="form-group padding_botton_3">
				<form:input path="name" class="form-control" style="height:38px;" placeholder="管理员" />
			</div>
			<div class="form-group padding_botton_4">
				<form:password path="pwd" class="form-control" style="height:38px" placeholder="密码" />
			</div>
			<div class="form-group">
				<button type="submit" class="btn_1 btn btn-primary form-control" >登录</button>
			</div>
			<div class="form-group" style="height:38px">
				<form:errors path="name" cssClass="error" /><form:errors path="pwd" cssClass="error"/><form:errors id="*" cssClass="error"/>
			</div>
		</form:form>
	</div>
</body>


</html>
