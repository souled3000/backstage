<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>joesph</title>

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
		<form method="POST" action="<%=request.getContextPath()%>/joseph/query">
			<div class="control-group">
				<label class="control-lable">选择设备类型</label>
					<select id="dt" name="dt">
						<option value="ALL">ALL</option>
						<option value="2">故障电弧检测空开</option>
						<option value="4">有害气体传感器</option>
						<option value="5">水传感器</option>
						<option value="6">入侵检测</option>
						<option value="9">接入网关类</option>
						<option value="10">扩展IO报警接入类</option>
					</select>
				<button type="submit" class="btn btn-primary">SEARCH</button>
			</div>
		</form>
	</div>
		<div>${msg}</div>
</body>
</html>