<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<script>
	function f(v) {
		var pos1 = v.lastIndexOf("\\");
		document.getElementById("fileName").value = v.substring(pos1 + 1,
				v.length);
	}
	function ff() {
		document.forms[0].submit();
	}
</script>
</head>
<body>

	<form:form modelAttribute="chip" enctype="multipart/form-data">
		<form:hidden path="fileName" />&nbsp;&nbsp;
		<table>
			<tr>
				<td><form:errors id="*" cssClass="error" /></td>
			</tr>
			<tr>
				<td>文件:</td>
				<td><input type="file" name="file" value="${chip.fileName }" onchange="f(this.value)" />&nbsp;&nbsp;<form:errors path="fileName" cssClass="error" /></td>
			</tr>
			<tr>
				<td>最小版本号:</td>
				<td><form:input path="minVer" maxlength="4" />&nbsp;&nbsp;<form:errors path="minVer" cssClass="error" /></td>
			</tr>
			<tr>
				<td colspan="2"><input type="button" onclick="ff()" value="<spring:message code="uploadBtn" />" /></td>
			</tr>
		</table>
	</form:form>
	<div>
		<table border="1">
			<tr>
				<td>文件名</td>
				<td>当前版本</td>
				<td>下载</td>
				<td>MD5</td>
			</tr>
			<c:forEach items="${chips }" var="c">
				<tr>
					<td>${c.fileName }</td>
					<td>${c.curVer }</td>
					<td><a href="${c.url }">下载</a></td>
					<td>${c.md5 }</td>
				</tr>
			</c:forEach>
		</table>
	</div>
</body>
</html>