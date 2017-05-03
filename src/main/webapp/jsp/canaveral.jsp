<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>canaveral</title>

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
<script type="text/javascript" src="<%=request.getContextPath()%>/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
function show(){
	dv=document.getElementById("dv").value;
	start=document.getElementById("start").value;
	end=document.getElementById("end").value;
	url="<%=request.getContextPath()%>/canaveral/time?start="+start+"&end="+end+"&dv="+dv+"&r="+Math.random();
//	url="<%=request.getContextPath()%>/canaveral/line?start="+start+"&end="+end+"&dv="+dv+"&r="+Math.random();
//	url="<%=request.getContextPath()%>/canaveral/bar?start="+start+"&end="+end+"&dv="+dv+"&r="+Math.random();
//	url="<%=request.getContextPath()%>/canaveral/area?start="+start+"&end="+end+"&dv="+dv+"&r="+Math.random();
	document.getElementById("001").src="";
	document.getElementById("001").src=url;
}
</script>
</head>
<body>
	<div class="wrapper">
		<div class="control-group">
			<label class="control-lable">设备类型</label>
			<input id="dv" type="text" name="dv" value="0"/>				
			<label class="control-lable">开始时间</label>
			<input id="start" type="text" name="start" value="2016111104" onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'end\')}',dateFmt:'yyyyMMddHH'})"/>				
			<label class="control-lable">结束时间</label>
			<input id="end" type="text" name="end" value="2016111117" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'start\')}',dateFmt:'yyyyMMddHH'})"/>				
			<button type="button" class="btn btn-primary" onclick="show()">SEARCH</button>
		</div>
	</div>
	<div>
	<img alt="" src="" id="001" >
	</div>
	<a href="<%=request.getContextPath()%>/menu">back</a>
	<br>
</body>
</html>