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
<title>数据查询</title>
</head>
<body>
	<%@include file="/commons/statics.jsp"%>
	
	<!-- 搜索模块 -->
	<div style="border:1px solid #D8D8D8;padding: 10px 0px 10px 0px;">
		<form id="serachFrom" method="post">
			<table cellpadding="2" style="width: 100%;" >
	    		<tr>
	    			<td style="text-align: right;padding-right: 20px;"  >
	    				<label>年份:</label>
	    			</td>
	    			<td >
	    				<input id="year" name="year" class="easyui-combobox" data-options="prompt: '年份' ,required:true">
	    			</td>
	    			<td>
	    				<label>评估单号:</label>
	    			</td>
	    			<td >
	    				<input id="reportId" name="reportId" class="easyui-combobox" style="width:230px;" data-options="editable:false,required:true,
	    																						   prompt:'请先输入年份',disabled:true">
	    			</td>
	    			<td>
	    				<label>评估项目:</label>
	    			</td>
	    			<td >
	    				<input id="projectCode" name="projectCode" class="easyui-combobox" data-options="editable:false,required:true,
	    																						   prompt:'请先选择项目'">
	    			</td>
	    			<td colspan="3" style="text-align: right;padding-right: 20px">
	    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="serach()"  data-options="iconCls:'icon-search'" style="width:80px;">查询</a>	    			
		    			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="reset()" data-options="iconCls:'icon-reload'" style="width:80px;">重置</a>	    					    			
		    			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="modeldownload()"  data-options="iconCls:'icon-print'" style="width:90px;">结果导出</a>
		    		</td>
	    		
	    		</tr>
	    		</table>
		</form>
	</div>
	
	<!-- 	操作，展示部分 -->
	<div id="weigtData1">
		<table id="dg1" class="easyui-datagrid" style="width:auto;height:420px"
			data-options="
			rowStyler: 
				function(index,row){
						return 'background-color:#FFFFFF;color:#19070B;';
					}">
			<thead style="height:50px;">
				<tr>
					<th field="..." width="25%" align="center" colspan="2">评估标准</th>
					<th field="standardScore" width="10%" align="center" rowspan="2">标准分值小计</th>
					<th field="..." width="15%" align="center" colspan="3">制度健全性</th>
					<th field="..." width="15%" align="center" colspan="3">遵循有效性</th>
					<th field="subtotalScore" width="10%" align="center" rowspan="2">得分小计<br/>（制度健全性得分<br/>+遵循有效性得分）</th>
					<th field="sevBase" width="25%" align="center" rowspan="2">评分依据</th>
				</tr>
				<tr>
					<th field="itemNum" width="5%" align="center">序号</th>
					<th field="itemName" width="20%">评估名称</th>
					<th field="sysIntegrityScore" width="5%" align="center">标准分值</th>
					<th field="intResult" width="5%" align="center" >评估结果</th>
					<th field="integrityScore" width="5%" align="center" >得分</th>
					<th field="folEfficiencyScore" width="5%" align="center" >标准分值</th>
					<th field="effResult" width="5%" align="center" >评估结果</th>
					<th field="efficiencyScore" width="5%" align="center">得分</th>
				</tr>
			</thead>
		</table>
	</div>
	<div id="weigtData2">
		<table id="dg2" class="easyui-datagrid" style="width:auto;height:420px"
			data-options="
			rowStyler: 
				function(index,row){
						return 'background-color:#FFFFFF;color:#19070B;';
					}">
			<thead style="height:50px;">
				<tr>
					<th field="projectName" width="12%" align="center" rowspan="2">评估项目</th>
					<th field="jcstandardScore" width="8%" align="center" rowspan="2">标准分值</th>
					<th field="..." width="30%" align="center" colspan="3">评分结果（不适用项目调整前）</th>
					<th field="adjSubtotalScore" width="12%" align="center" rowspan="2">评分结果<br/>（不适用项目调整后）</th>
					<th field="weight" width="8%" align="center" rowspan="2">权重</th>
					<th field="jcscore" width="8%" align="center" rowspan="2">基础能力得分</th>
					<th field="tsstandardScore" width="8%" align="center" rowspan="2">标准分值</th>
					<th field="tsscore" width="8%" align="center" rowspan="2">提升能力得分</th>
					<th field="rate" width="8%" align="center" rowspan="2">最终得分</th>
				</tr>
				<tr>
					<th field="integrityScore" width="10%" align="center" >制度健全性</th>
					<th field="efficiencyScore" width="10%" align="center" >遵循有效性</th>
					<th field="subtotalScore" width="10%" align="center" >合计</th>
				</tr>
			</thead>
		</table>
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
		//导出数据以excel文件格式导出
    	function modeldownload() {
    		if($('#reportId').combobox('getValue')==""){$.messager.show({
        		title:'提示',
        		msg:'请选择评估单号',
        		showType:'show'
        	}); return false;};
    		var reportId=$('#reportId').combobox('getValue');
    		//alert(reportId);
    		//alert(reportId!="");
    		if(reportId!=""){
    			//alert(reportId);
        		var param = {reportId:$('#reportId').combobox('getValue')};
            	//使用参数执行一次查询，且将查询数据导出
            	jsutil.core.download("${pageContext.request.contextPath}/alm_compute/download",param);
    		}else{
    			$.messager.alert('警告',"需要选择年份和评估单号！",'error');
    		}
		}  
    	//重置
    	function reset(){
    		$('#serachFrom').form("clear");
    	}
		function serach(){
        	//验证是否输入使用层级查询条件
			if($('#reportId').combobox('getValue')==""){$.messager.show({
			        		title:'提示',
			        		msg:'请选择评估单号再查询',
			        		showType:'show'
			        	}); return false;};
			if($('#projectCode').combobox('getValue')==""){$.messager.show({
			        		title:'提示',
			        		msg:'请选择评估项目再查询',
			        		showType:'show'
			        	}); return false;};
        	//获取到选择的项目id 根据不同的项目选择不同的模板
        	var projectCode=$('#projectCode').combobox('getValue');
        	//alert(projectCode);
        	var reportId=$('#reportId').combobox('getValue');
        	//alert(reportId);
        	if(projectCode=="p00"){
        		$("#weigtData1").hide();
        		$("#weigtData2").show();
        		$("#dg2").datagrid({
            		url:'${pageContext.request.contextPath}/alm_compute/list.do?projectCode='+projectCode+'&reportId='+reportId ,
    				toolbar:'#toolbar',
    				fitColumns:true,
    				singleSelect:true,
    				striped:true,
    				nowrap:false,
    				loadMsg:'加载数据中...',
        		});
        	}else{
        		$("#weigtData2").hide();
        		$("#weigtData1").show();
            	//console.log(params);
            	//$('#dg').datagrid('cancelCellTip');
            	//使用参数执行一次查询
            	$("#dg1").datagrid({
            		//queryParams: params,
            		url:'${pageContext.request.contextPath}/alm_compute/list.do?projectCode='+projectCode+'&reportId='+reportId ,
    				toolbar:'#toolbar',
    				fitColumns:true,
    				singleSelect:true,
    				striped:true,
    				nowrap:false,
    				loadMsg:'加载数据中...',
    				onLoadSuccess: function(data){
    					//当前选中的评估项目通过不同项目进行不用的表格合并
    					//alert(projectCode);
    					if(projectCode=="p01"){
    						$("#dg1").datagrid("mergeCells", {
    		                    index:0,
    		                    field:'sysIntegrityScore',
    		                    rowspan:null,
    		                    colspan:8
    		                });
    						$("#dg1").datagrid("mergeCells", {
    		                    index:7,
    		                    field:'sysIntegrityScore',
    		                    rowspan:null,
    		                    colspan:8
    		                });
    						$("#dg1").datagrid("mergeCells", {
    		                    index:16,
    		                    field:'sysIntegrityScore',
    		                    rowspan:null,
    		                    colspan:8
    		                });
    						$("#dg1").datagrid("mergeCells", {
    		                    index:29,
    		                    field:'sysIntegrityScore',
    		                    rowspan:null,
    		                    colspan:8
    		                });
    						$("#dg1").datagrid("mergeCells", {
    		                    index:36,
    		                    field:'sysIntegrityScore',
    		                    rowspan:null,
    		                    colspan:8
    		                });   					
    						//最后三行得分小计合并显示空
    						$("#dg1").datagrid("mergeCells", {
    		                    index:39,
    		                    field:'sevBase',
    		                    rowspan:3,
    		                    colspan:null
    		                });
    						$("#dg1").datagrid("mergeCells", {
    		                    index:40,
    		                    field:'sysIntegrityScore',
    		                    rowspan:2,
    		                    colspan:6
    		                });
    					}else if(projectCode=="p02"){
    						$("#dg1").datagrid("mergeCells", {
    		                    index:0,
    		                    field:'sysIntegrityScore',
    		                    rowspan:null,
    		                    colspan:8
    		                });
    						$("#dg1").datagrid("mergeCells", {
    		                    index:13,
    		                    field:'sysIntegrityScore',
    		                    rowspan:null,
    		                    colspan:8
    		                });
    						//最后三行得分小计合并显示空
    						$("#dg1").datagrid("mergeCells", {
    		                    index:28,
    		                    field:'sevBase',
    		                    rowspan:3,
    		                    colspan:null
    		                });
    						$("#dg1").datagrid("mergeCells", {
    		                    index:29,
    		                    field:'sysIntegrityScore',
    		                    rowspan:2,
    		                    colspan:6
    		                });			
    					}else if(projectCode=="p03"){
    						$("#dg1").datagrid("mergeCells", {
    		                    index:0,
    		                    field:'sysIntegrityScore',
    		                    rowspan:null,
    		                    colspan:8
    		                });
    						$("#dg1").datagrid("mergeCells", {
    		                    index:16,
    		                    field:'sysIntegrityScore',
    		                    rowspan:null,
    		                    colspan:8
    		                });
    						//最后三行得分小计合并显示空
    						$("#dg1").datagrid("mergeCells", {
    		                    index:28,
    		                    field:'sevBase',
    		                    rowspan:3,
    		                    colspan:null
    		                });
    						$("#dg1").datagrid("mergeCells", {
    		                    index:29,
    		                    field:'sysIntegrityScore',
    		                    rowspan:2,
    		                    colspan:6
    		                });	
    					}else if(projectCode=="p04"){
    						$("#dg1").datagrid("mergeCells", {
    		                    index:0,
    		                    field:'sysIntegrityScore',
    		                    rowspan:null,
    		                    colspan:8
    		                });  					
    						//最后三行得分小计合并显示空
    						$("#dg1").datagrid("mergeCells", {
    		                    index:12,
    		                    field:'sevBase',
    		                    rowspan:3,
    		                    colspan:null
    		                });
    						$("#dg1").datagrid("mergeCells", {
    		                    index:13,
    		                    field:'sysIntegrityScore',
    		                    rowspan:2,
    		                    colspan:6
    		                });	
    					}else if(projectCode=="p05"){
    						$("#dg1").datagrid("mergeCells", {
    		                    index:0,
    		                    field:'sysIntegrityScore',
    		                    rowspan:null,
    		                    colspan:8
    		                });  					
    						//最后三行得分小计合并显示空
    						$("#dg1").datagrid("mergeCells", {
    		                    index:9,
    		                    field:'sevBase',
    		                    rowspan:3,
    		                    colspan:null
    		                });
    						$("#dg1").datagrid("mergeCells", {
    		                    index:10,
    		                    field:'sysIntegrityScore',
    		                    rowspan:2,
    		                    colspan:6
    		                });	
    					}	
            		}
            	});
            	
        	}        	
    	}
		$(function() {
			//设置先选 然后再选 有先后顺序的规则
			$('#weigtData1').hide();//隐藏datagrid1
			$('#weigtData2').hide();//隐藏datagrid2
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
		 					prompt:'评估单号',
		 	    			valueField: 'value',
		 			        textField: 'text',
		 			        method: 'get',
		 			        url: '${pageContext.request.contextPath}/codeSelect/hasParam.do?type=reportId&params='+record.value,
		 			        editable:false,
		 			        onLoadSuccess: function(data){	 					
		 			        	if(data.length != 0){
			 						$(this).combobox('select', data[0].value);
			 						//serach();
			 					}				
			 				}
	 	            	}); 
	 				}
	          });
			 $('#projectCode').combobox({
	             url:'${pageContext.request.contextPath}/codeSelect.do?type=projectCode1',
	             valueField:'value',  
	             textField:'text',
	             editable:false,
	             method: 'get',
	             onLoadSuccess: function(data){	 					
			        	if(data.length != 0){
	 						$(this).combobox('select', data[0].value);		 						
	 					}				
	 				}
	         });			
		});
	</script>
</html>