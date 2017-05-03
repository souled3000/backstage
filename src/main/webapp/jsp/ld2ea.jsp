<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
<base href="<%=basePath%>">
<script>
	function f(v) {
		var pos1 = v.lastIndexOf("\\");
		document.getElementById("fileName").value = v.substring(pos1 + 1,
				v.length);
	}
	function ff() {
		document.forms[0].submit();
	}
	function fff() {
		document.location.href="<%=request.getContextPath()%>/ld2";
	}
</script>
</head>
<body>
	<form:form modelAttribute="cfg" method="post" action="ld2/save">
		<c:if test="${!empty cfg.devType}">
			<form:hidden path="devType" />
		</c:if>
		<table>
			<tr>
				<td><form:errors id="*" cssClass="error" /></td>
			</tr>
			<c:if test="${empty cfg.devType}">
				<tr>
					<td>设备类型:</td>
					<td><form:input path="devType" maxlength="8" />&nbsp;&nbsp;<form:errors  path="devType" cssClass="error" /></td>
				</tr>
			</c:if>
			<c:if test="${!empty cfg.devType}">
				<tr>
					<td>设备类型:</td>
					<td>${cfg.devType }</td>
				</tr>
			</c:if>
			<tr>
				<td>版本号:</td>
				<td><form:input path="version" maxlength="2" />&nbsp;&nbsp;<form:errors path="version" cssClass="error" /></td>
			</tr>
			<tr>
				<td>与或:</td>
				<td><form:input path="andOr" maxlength="2" />&nbsp;&nbsp;<form:errors path="andOr" cssClass="error" /></td>
			</tr>
			<tr>
				<td>报警编码:</td>
				<td><form:input path="strAlarms" size="50"/>&nbsp;&nbsp;<form:errors path="strAlarms" cssClass="error" /></td>
			</tr>
			<tr>
				<td>联动类型</td>
				<%-- <td><form:select path="ld2t"> --%>
				<td><form:select path="ld2Type">
						<form:option value="-" label="--Please Select" />
						<form:options items="${ld2Types}" itemValue="code" itemLabel="name" />
					</form:select>&nbsp;&nbsp;<form:errors path="ld2Type" cssClass="error" /></td>
			</tr>
			<tr>
				<td>动作起始位置:</td>
				<td><form:input path="actionOrigin" maxlength="2" />&nbsp;&nbsp;<form:errors path="actionOrigin" cssClass="error" /></td>
			</tr>
			<tr>
				<td>动作内容:</td>
				<td><form:input path="actionCtn"  size="50"/>&nbsp;&nbsp;<form:errors path="actionCtn" cssClass="error" /></td>
			</tr>
			<tr>
				<td colspan="2"><input type="button" onclick="ff()" value="<spring:message code="save" />" /><input type="button" onclick="fff()" value="返回" /></td>
			</tr>
		</table>
	</form:form>
</body>
</html>