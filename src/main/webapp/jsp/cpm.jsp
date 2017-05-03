<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>CPM</title>

<style type="text/css">
.control-group .control-lable {
	width: 140px;
	padding-top: 5px;
	margin-bottom: 5px;
}

.control-group .controls {
	margin-left: 160px;
}

.form-actions {
	padding: 17px 20px 18px;
	margin-top: 18px;
	margin-bottom: 18px;
	border-top: 1px solid #e5e5e5;
	padding-left: 160px;
}
</style>
</head>
<body>
	<div class="wrapper">
可输入设备mac、用户账户、用户id、设备id、或什么也不输入直接点SEARCH<br>
		<form method="POST" action="<%=request.getContextPath()%>/cpm/query">
			<div class="control-group">
				<input type=text name="in" value="${in}" size="30" />
			</div>
			<button type="submit" class="btn btn-primary">SEARCH</button>
		</form>
	</div>
	<a href="<%=request.getContextPath()%>/menu">back</a>
	<pre style="font-size: 18px">
${out }
	</pre>
</body>
</html>