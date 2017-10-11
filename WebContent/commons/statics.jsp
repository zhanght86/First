<%-- 所有的静态资源导入都放在这里--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- 样式 默认就有 type="text/css" --%>

<link rel="stylesheet"  id="easyuiTheme" type="text/css" href="${contextPath}/js/easyui/themes/${cookie.easyuiThemeName.value==null?'bootstrap':cookie.easyuiThemeName.value}/easyui.css">
<link rel="stylesheet" type="text/css" href="${contextPath}/js/easyui/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${contextPath}/js/easyui/demo.css">

<%-- js 默认就有 type="text/javascript" --%>
<script type="text/javascript" src="<c:url value='/js/easyui/jquery.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/easyui/jquery.blockUI.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/easyui/jquery.easyui.min.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/easyui/plugins/jquery.cookie.js' />"></script>
<script type="text/javascript" src="<c:url value='/commons/jsutil.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/easyui/load.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/easyui/locale/easyui-lang-zh_CN.js' />"></script>
<script type="text/javascript" src="<c:url value='/js/easyui/changeEasyuiTheme.js' />"></script>
<!-- 国际化中文支持 -->  
<%-- <script type="text/javascript" src="${contextPath}/js/plupload/zh_CN.js"></script>   --%>
<script>
	$.ajaxSetup({cache:false});
</script>
