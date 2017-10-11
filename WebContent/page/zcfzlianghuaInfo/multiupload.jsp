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
var year = '${param.year}';
var quarter = '${param.quarter}';
var reportId = '${param.reportId}';
var department='account';
var reporttype='${param.reporttype}';
var reportname='${param.reportname}';
var comCode ='${param.comCode}';
var comCode ='${param.deptflag}';

var max_file_size = '31mb';
var filters = {title : "文档", extensions : "zip,rar,xls,xlsx,xlsm"};
var theRequst = GetRequest();

var data = "?year="+year+"&quarter="+quarter +"&reportId="+reportId+"&department="+department+"&reportname="+reportname+"&reporttype="+reporttype;

//alert(data);
$("#uploader").pluploadQueue($.extend({
	runtimes : 'html5,flash,html4',
	url : '${pageContext.request.contextPath}/lianghuaupload/upload.do'+  data,
	max_file_size : max_file_size,
	file_data_name:'file',
	unique_names:true,
	multi_selection:true,
	multipart:true,
	filters : [filters],
	flash_swf_url : '../../js/plupload/plupload.flash.swf',
	init:{//上传完成时，调用这个方法
		FileUploaded:function(uploader,file,response){
			if(response.response){
				var rs = $.parseJSON(response.response);
				if(rs.status){
					//files.push(file.name);
					alert("上传成功!");
					target.window("close");
				}else{
					//errors.push(file.name);
					if(rs.data==null||rs.data=="") {
						alert(rs.err);
						target.window("close");
					}else {
						$.messager.alert("提示",rs.err,"warning",function(){
							parent.openDialog(rs.data);
						});
					}
				}
			}
		}
	}
},(chunk ? {chunk_size:'10mb'} : {})));

function closeWindow(){
	target.window("close");
}

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
