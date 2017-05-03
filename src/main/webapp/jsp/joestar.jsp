<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>joestar</title>

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
<a href="<%=request.getContextPath()%>/menu">back</a><br>
	<div class="wrapper">
		<form method="POST" action="<%=request.getContextPath()%>/joestar/query">
			<div class="control-group">
				<label class="control-lable">BEGID</label><br>
				<input type=text name="fs" value="${fs}" size="30" />${fsmsg }<br>fdffffffffffffff
			</div>
			<div class="control-group">
				<label class="control-lable">密证</label><br>
				<input type=text name="mz" value="${mz}" size="90"/><br>
				e.g.fb95ab046904be5a76c1c652667c317a
			</div>
			<div class="control-group">
				<label class="control-lable">报文</label><br>
				<input type=text name="graph" size="220" value="${graph}" /><br>
				e.g.:c2000f032e6a9c019c012e6a9c012e6a9c019ebf0162d6759201ea6a840151f4699fae56b4b84642f9f89811e84758ae89b11f34c785ced170c525d6c4056d60
			</div>
			<button type="submit" class="btn btn-primary">SEARCH</button>
		</form>
	</div>
	<pre>${msg}</pre>
</body>
</html>