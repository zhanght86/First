<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%
	String path=request.getContextPath()+ request.getServletPath().substring(0,request.getServletPath().lastIndexOf("/") + 1); 
%>
<title>模板计算</title>
</head>
<body>
	<%@include file="/commons/statics.jsp"%>
	
	<!-- 搜索模块 -->
	<div style="border:1px solid #D8D8D8;padding: 10px 0px 10px 0px;">
		<form id="serachFrom" method="post">
			<table cellpadding="5" border='0' style="width: 100%;">
	    		<tr>
	    			<td style="width:90px;text-align: right;padding-right: 20px;" >
	    				<label>年份:</label>
	    			</td>
	    			<td >
	    				<input id="year" name="year" class="easyui-combobox" data-options="prompt: '请选择年份',required:true">
	    			</td>
	    			<td style="width:90px;text-align: right;padding-right: 20px;" >
	    				<label>评估单号:</label>
	    			</td>
	    			<td >
	    				<input id="reportId" name="reportId" class="easyui-combobox" style="width:230px;" data-options="editable:false,required:true,
	    																						   prompt:'请先选择年份',disabled:true">
	    			</td>
	    			<td colspan="2" style="text-align: right;padding-right: 20px">
	    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="indexCalculation()"  id="btn" data-options="iconCls:'icon-search'" style="width:80px;">计算</a>
	    			</td>
	    		</tr>
	    	</table>
		</form>
	</div>
	<div id="dlg" class="easyui-dialog" top=265px
		style="width: 300px; height:100px; padding: 10px 20px" closed="true" align="center" 
		data-options="modal:true,closed:true,onOpen:onOpen,onClose:onClose">
		<div id="msg"></div>
    </div>
</body>
	<script type="text/javascript">	
		//给每个都加上这个事件如果value为空则就变色 合并单元格后按照合并的那个进行显示判断
		//data-options="styler:bianse"
		function bianse(value,row,index){
			if(value==null){
				return 'background-color:#808080;';
			}
		}
		//指标计算
        function indexCalculation(){
        	
        	if($('#year').combobox('getValue')==""){$.messager.show({
		        		title:'提示',
		        		msg:'请选择年份查询',
		        		showType:'show'
		        	}); return false;};
			if($('#reportId').combobox('getValue')==""){$.messager.show({
		        		title:'提示',
		        		msg:'请选择评估单号查询',
		        		showType:'show'
		        	}); return false;};
		    $('#btn').linkbutton('disable');//防止表单重复提交
        	var reportId=$('#reportId').combobox('getValue');
        	$.get('${pageContext.request.contextPath}/alm_compute/verify.do?reportId='+reportId,
    				function(data){
    					if(!data.success){
    						$.messager.alert('提示',data.errorMsg,'warning');
    						$('#btn').linkbutton('enable');
    					}else{
    						$.messager.confirm('确认','确认要计算么?',function(r){
    							if(r){
    								//开始计算该单号下的								
    								dataCalculation();
    								$('#btn').linkbutton('enable');
    							}
    						});    					
    					}
    				}
        	);
		}
		function dataCalculation(){
			$('#msg').text('正在指标计算，请稍后.');
			$('#dlg').dialog('open').dialog('setTitle','数据计算');
			var reportId=$('#reportId').combobox('getValue');
        	$.get('${pageContext.request.contextPath}/alm_compute/calculation.do?reportId='+reportId,
    				function(data){
    					if(data.success){
    						$("#dlg").dialog("close");
    						$.messager.show({
	       						type: 'success',
	       						content:"所有指标计算完成，均执行成功！"
	       					}); 
    					}else{
    						$.messager.show({	// show error message
								title: 'Error',
								msg: result.errorMsg
							});					
    					}
    				}
        	);
		}
		//打开窗口
    	var loading;
    	function onOpen(){
    		loading=setInterval(showalert, 500);
    	}
    	var i=2;
    	function showalert(){
    		var text="";
    		if(i==1){
    			text="正在指标计算，请稍后.";
    		}else if(i==2){
    			text="正在指标计算，请稍后..";
    		}else if(i==3){
    			text="正在指标计算，请稍后...";
    			i=0;
    		}
    		i++;
    		$("#msg").text(text);
    	}
    	//关闭窗口
    	function onClose(){
    		clearInterval(loading);
    	}
		$(function() {
			//设置先选 然后再选 有先后顺序的规则
			 $('#year').combobox({
	             	editable:false,
	             	url:'${pageContext.request.contextPath}/codeSelect.do?type=year',
	                valueField:'value',
	 				textField:'text',
	 				method: 'get',
	 				onLoadSuccess: function(data){	 					
	 					if(data.length != 0){
	 						$(this).combobox('select', data[0].value);	
	 					}					
	 				},
	 				onSelect:function(record){
	 					$('#reportId').combobox({
		 					disabled: false,
		 					prompt:'请选择评估单号',
		 	    			valueField: 'value',
		 			        textField: 'text',
		 			        method: 'get',
		 			        url: '${pageContext.request.contextPath}/codeSelect/hasParam.do?type=reportId&params='+record.value,
		 			        editable:false,
		 			        onLoadSuccess: function(data){	 					
		 			        	if(data.length != 0){
			 						$(this).combobox('select', data[0].value);	
			 					}				
			 				}
	 	            	}); 
	 				}
	          });
		});
	</script>
</html>