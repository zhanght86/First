<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@include file="/commons/taglibs.jsp"%>
<html>
  <head>
    <title>文件上传</title>
    <%@include file="/commons/statics.jsp"%>
    <link rel="stylesheet" href="<c:url value='/js/plupload/queue/css/jquery.plupload.queue.css' />" type="text/css"></link>
	<script type="text/javascript" src="<c:url value='/js/plupload/plupload.js' />"></script>
	<script type="text/javascript" src="<c:url value='/js/plupload/plupload.html4.js' />"></script>
	<script type="text/javascript" src="<c:url value='/js/plupload/plupload.html5.js' />"></script>
    <script type="text/javascript" src="<c:url value='/js/plupload/queue/jquery.plupload.queue.js' />"></script>
	<script type="text/javascript" src="<c:url value='/js/plupload/plupload.flash.js' />"></script>
	<script type="text/javascript" src="<c:url value='/js/plupload/zh_CN.js' />"></script>
  <body style="padding: 0;margin: 0;">
  <form action="">
    <div id="uploader">&nbsp;</div>
   </form>
<script type="text/javascript">
var files = [];
var errors = [];
var type = 'file';
var chunk = eval('${param.chunk}');
var max_file_size = '31mb';
var filters = {title : "文档", extensions : "zip,doc,docx,xls,xlsx,ppt,pptx"};
$("#uploader").pluploadQueue($.extend({
	runtimes : 'flash,html4,html5',
	url : '${pageContext.request.contextPath}/upload/upload.do',
	max_file_size : max_file_size,
	file_data_name:'file',
	unique_names:true,
	multipart:true,
	filters : [filters],
	flash_swf_url : '../../js/plupload/plupload.flash.swf',
	init:{
		FileUploaded:function(uploader,file,response){
			if(response.response){
				var rs = $.parseJSON(response.response);
				if(rs.status){
					files.push(file.name);
				}else{
					errors.push(file.name);
				}
			}
		},
		UploadComplete:function(uploader,fs){
			var e= errors.length ? ",失败"+errors.length+"个("+errors.join("、")+")。" : "。";
			alert("上传完成！共"+fs.length+"个。成功"+files.length+e);
			target.window("close");
		}
	}
},(chunk ? {chunk_size:'1mb'} : {})));
</script>
  </body>
</html>
