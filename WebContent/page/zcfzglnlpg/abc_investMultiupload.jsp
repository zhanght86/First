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
    <div id="uploader">
    
    </div>
   </form>
<script type="text/javascript">
var files = [];
var errors = [];
var type = 'file';
var chunk = '${param.chunk2}';
var itemCode = '${param.itemCode}';
var reportId= '${param.reportId}';
var deptNo = '${param.deptNo}';
var number= '${param.number}';

var max_file_size = '31mb';
var filters = {title : "文档", extensions : "zip,rar,xls,xlsx,xlsm,doc,docx,eml,pdf,txt,wps,et,htm,html,shtml,jpg,png,dps,pptx,ppt"};
var theRequst = GetRequest();

var data = "?itemCode="+itemCode+"&reportId="+reportId +"&deptNo="+deptNo+"&number="+number;

//alert(data);
$("#uploader").pluploadQueue($.extend({
	runtimes : 'html5,flash,html4',
	url : '${pageContext.request.contextPath}/alm_reportdata/multiupload.do'+data,
	max_file_size : max_file_size,
	file_data_name:'file',
	unique_names:true,
	multi_selection:true,
	multipart:true,
	filters : [filters],
	flash_swf_url : '../../js/plupload/plupload.flash.swf',
	init:{
		FileUploaded:function(uploader,file,response){
			if(response.response){
				var rs = $.parseJSON(response.response);
				
				if(rs.status){
					alert("上传成功！");
					files.push(file.name);
				}else{
					alert(rs.errMessage);
					errors.push(rs.errMessage);
				}
			}
		},
		UploadComplete:function(uploader,fs){
//			var e= errors.length ? ",失败"+errors.length+"个("+errors.join("、")+")。" : "。";
//			alert("上传完成！共"+fs.length+"个。成功"+files.length+e);
			target.window("close");
		}
	}
},(chunk ? {chunk_size:'10mb'} : {})));

function GetRequest() {   
	   var url = location.search; //获取url中"?"符后的字串   
	   var theRequest = new Object();   
	   if (url.indexOf("?") != -1) {   
	      var str = url.substr(1);   
	      strs = str.split("&");   
	      for(var i = 0; i < strs.length; i ++) {   
	         theRequest[strs[i].split("=")[0]]=unescape(strs[i].split("=")[1]);   
	      }   
	   }   
	   return theRequest;   
	}   
</script>
  </body>
</html>
