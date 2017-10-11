<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>finereport</title>
<body>
<%@include file="/commons/statics.jsp"%>
<div class="easyui-layout" style="width:900px;height:400px;">
    	<div region="center" split="true" title="报表打印" style="width:100%;">
<iframe id="reportFrame" width="900" height="400" src="<%=basePath%>/ReportServer?reportlet=upload.cpt"></iframe>
    	</div>
    </div>
</body>
</html>
