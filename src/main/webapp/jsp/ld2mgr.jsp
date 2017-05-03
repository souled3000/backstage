<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<link href="" />
<script type="text/javascript">
	function f() {
		document.location.href="<%=request.getContextPath() %>/ld2/add";
	}
</script>
</head>
<body>
<a href="<%=request.getContextPath()%>/menu">back</a><br>
	<button onclick="f()">添加</button>
	<div>
		<table border="1">
			<tr>
				<td>设备类型</td>
				<td>联动类型</td>
				<td>版本号</td>
				<td>校验值</td>
				<td>报警数量</td>
				<td>操作</td>
			</tr>
			<c:forEach items="${cfgs }" var="c">
				<tr>
					<td><a href="<%=request.getContextPath()%>/ld2/dtl/${c.devType }">${c.devType }</a></td>
					<td><c:choose>
							<c:when test="${c.ld2Type eq '00'}">无效数据</c:when>
							<c:when test="${c.ld2Type eq '01'}">源报警类型设备</c:when>
							<c:when test="${c.ld2Type eq '02'}">预留</c:when>
							<c:when test="${c.ld2Type eq '03'}">预留</c:when>
							<c:when test="${c.ld2Type eq '04'}">动作执行数据完整数据</c:when>
							<c:when test="${c.ld2Type eq '05'}">局域控制数据类型</c:when>
							<c:otherwise>未知</c:otherwise>
						</c:choose></td>
					<td>${c.version }</td>
					<td>${c.checksum }</td>
					<td>${c.alarmAmount }</td>
					<td><a href="<%=request.getContextPath()%>/ld2/edit/${c.devType }">编辑</a>&nbsp;&nbsp;<a href="<%=request.getContextPath()%>/ld2/del/${c.devType }">删除</a></td>
				</tr>
			</c:forEach>
		</table>
	</div>
</body>
</html>