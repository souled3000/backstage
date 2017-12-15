<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>设备二维码</title>

<style type="text/css">
.control-group .control-lable {
	width: 140px;
	padding-top: 5px;
	margin-bottom: 5px;
	text-align: right;
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
<script type="text/javascript">
	function batch() {

	}
	function genImg() {
		var imgObj = document.getElementByID("qrImg");
		imgObj.src = "";
	}
</script>
</head>
<body>
	<a href="<%=request.getContextPath()%>/menu">back</a>
	<div class="wrapper">
		<form method="POST" action="<%=request.getContextPath()%>/qr">
			<div class="control-group">
				<label class="control-lable">设备类型</label> <input type=text
					name="deviceType" value="${deviceType}" size="30" />
			</div>
			<div class="control-group">
				<label class="control-lable">串码类型</label> <select name="codeType">
					<option value="SN">SN</option>
					<option value="SE">SE</option>
				</select>
			</div>
			<div class="control-group">
				<label class="control-lable">类型标识码</label> <input type=text
					name="typeId" value="${typeId}" size="30" />
			</div>
			<div class="control-group">
				<label class="control-lable">生产日期</label> <input type=text
					name="ptDate" value="${ptDate}" size="30" />
			</div>
			<div class="control-group">
				<label class="control-lable">编号</label> <input type=text
					name="start" value="${start}" size="15" /> <input type=text
					name="end" value="${end}" size="15" />
			</div>
			<div class="control-group">
				<label class="control-lable">SE唯一码</label> <input type=text
					name="soleCode" value="${soleCode}" size="30" />
			</div>
			<label id="qrTxt" />
			<div class="control-group">
				<button type="button" onclick="batch()" class="btn btn-primary">批量</button>
				<button type="button" onclick="genImg()" class="btn btn-primary">单个</button>
			</div>
		</form>
		<img alt="" src="" id="qrImg">
	</div>
</body>
</html>