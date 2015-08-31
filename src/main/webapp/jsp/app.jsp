<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>App管理页面</title>

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
		<form action="/backstage/appupload" method="POST"
			enctype="multipart/form-data">
			<div class="control-group">
				<label class="control-lable">选择app类型</label>
				<div class="controls">
					<select id="appType" name="appType">
						<option value="android">安卓</option>
						<option value="iphone">IOS</option>
					</select>
				</div>
			</div>
			<div class="control-group">
				<label class="control-lable">选择上传文件</label>
				<div class="controls">
					<input type="file" name="file" accept=".apk" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-lable">当前版本号</label>
				<div class="controls">
					<input name="curVer" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-lable">系统兼容最小版本</label>
				<div class="controls">
					<input name="minVer" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-lable">版本描述</label>
				<div class="controls">
					<textarea class="autogrow" name="msg" rows="5" cols="30"></textarea>
				</div>
			</div>
			<div class="form-actions">
				<button type="submit" class="btn btn-primary">Save</button>
			</div>
		</form>
		<div>
			<table border="1">
				<tr>
					<td>App类型</td>
					<td>最新版本</td>
					<td>系统支持最小版本</td>
					<td>版本描述</td>
					<td>下载地址</td>
				</tr>
				<c:forEach items="${applist}" var="c">
					<tr>
						<td>${c.id}</td>
						<td>${c.last}</td>
						<td>${c.avail}</td>
						<td>${c.msg}</td>
						<td><textarea class="autogrow" cols="30" readonly="readonly">${c.msg}</textarea></td>
						<td>${c.url}</td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</div>
</body>
</html>