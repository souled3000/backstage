<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<script>
</script>
</head>
<body>
	<table border=1>
		<tr>
			<td>设备类型</td>
			<td>${c.devType }</td>
		</tr>
		<tr>
			<td>校验值</td>
			<td>${c.checksum }</td>
		</tr>
		<tr>
			<td>版本号</td>
			<td>${c.version }</td>
		</tr>
		<tr>
			<td>联动类型</td>
			<td>
			<c:choose>
				<c:when test="${c.ld2Type eq '00'}">无效数据</c:when>
				<c:when test="${c.ld2Type eq '01'}">源报警类型设备</c:when>
				<c:when test="${c.ld2Type eq '02'}">预留</c:when>
				<c:when test="${c.ld2Type eq '03'}">预留</c:when>
				<c:when test="${c.ld2Type eq '04'}">动作执行数据完整数据</c:when>
				<c:when test="${c.ld2Type eq '05'}">局域控制数据类型</c:when>
				<c:otherwise>未知</c:otherwise>
			</c:choose>
			</td>
		</tr>
		<tr>
			<td>与或</td>
			<td>
			<c:choose>
				<c:when test="${c.andOr eq '00'}">与</c:when>
				<c:when test="${c.andOr eq '01'}">或</c:when>
				<c:otherwise>未知</c:otherwise>
			</c:choose>
			</td>
		</tr>
		<tr>
			<td>报警编码数量</td>
			<td>${c.alarmAmount }</td>
		</tr>
		<tr>
			<td>报警编码</td>
			<td>
			 <c:forEach items="${c.alarms}" var="a" varStatus="status">
			 ${a }<br>
			 </c:forEach>
			</td>
		</tr>
		<tr>
			<td>动作起始位置</td>
			<td>${c.actionOrigin}</td>
		</tr>
		<tr>
			<td>动作数据内容</td>
			<td>${c.actionCtn}</td>
		</tr>
		<tr>
			<td>二进制</td>
			<td>${c.strEnd}</td>
		</tr>
	</table>
	<a href="<%=request.getContextPath()%>/ld2">返回</a>
</body>
</html>