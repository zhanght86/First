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
<title>结果调整</title>
</head>
<body>
	<%@include file="/commons/statics.jsp"%>

	<!-- 搜索模块 -->
	<div style="border:1px solid #D8D8D8;padding: 10px 0px 10px 0px;">
		<form id="serachFrom" method="post">
			<table cellpadding="5" border='0' style="width: 100%;">
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
	    			<td style="width:90px;text-align: right;padding-right: 20px;" >
	    				<label>评估项目:</label>
	    			</td>
	    			<td >
	    				<input id="projectCode" name="projectCode" class="easyui-combobox" ddata-options="prompt:'项目名称'">
	    			</td>
	    			<td colspan="2" style="text-align: right;padding-right: 20px">
	    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="serach()"  data-options="iconCls:'icon-search'" style="width:80px;">查询</a>    			
	    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="reset()" data-options="iconCls:'icon-reload'" style="width:80px;">重置</a>	
	    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="submit()"  data-options="iconCls:'icon-remove'" style="width:80px;">提交</a>	    	
	    			</td>
	    		</tr>
	    	</table>
		</form>
	</div>
	<!-- 	操作，展示部分 -->
	<div  id="weigtData">
		<table id="dg" class="easyui-datagrid"
			toolbar="#toolbar" 
			fitColumns="true"
			singleSelect="true" 
			striped="true"
			nowrap="false"
			autoRowHeight="false"
			style="width:100%;height:383px;"
			loadMsg="加载数据中..." 
			method="get"
			>
			<thead>
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
					<th field="itemName" width="20%" >评估名称</th>
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
	<div id="toolbar">
	    	<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="edit()">编辑</a>
	</div>
    <!--  新增弹框界面-->
	<div id="dlg" class="easyui-dialog" style="width: 600px; padding: 10px 20px" align="center"  data-options="modal:true,closed:true,resizable:true,collapsible:true,minimizable:true,maximizable:true"  buttons="#dlg-buttons">
	   	<form id="fm" method="post">
	   		<table cellpadding="5">	   			
	   			<input type="hidden" name="itemCode">
	   			<input type="hidden" name="reportId">	
	   			<input type="hidden" name="sysIntegrityScore">	
	   			<input type="hidden" name="folEfficiencyScore">		
	   			<tr>	   				
	   				<td>制度健全性评分结果:</td>
	   				<td><input class="easyui-combobox" 
							   name="integritySevResult"
							   style="width:150px;" 
			                   data-options="
					                 url:'${pageContext.request.contextPath}/codeSelect.do?type=EvaluateResult',
					                 method:'get',
					                 valueField:'value',
					                 textField:'text',
					                 panelHeight:'auto'
			         "></td>
	   			</tr>
	   			<tr>
	   				<td>遵循有效性评分结果:</td>
	   				<td><input class="easyui-combobox" 
							   name="efficiencySevResult"
							   style="width:150px;" 
			                   data-options="
					                 url:'${pageContext.request.contextPath}/codeSelect.do?type=EvaluateResult',
					                 method:'get',
					                 valueField:'value',
					                 textField:'text',
					                 panelHeight:'auto'
			         "></td>
	   			</tr>
	   			<tr>
	   				<td>调整意见:</td>
	   				<td><textarea name="adjustOpinion" class="easyui-validatebox" style="width: 200px; height: 60px;"></textarea></td>
	   			</tr>
	   		</table>
	   	</form>
	    <div id="dlg-buttons">
    	<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="save()">保存</a>
    	<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">取消</a>
    </div>	
    </div>
        <!--  新增弹框界面2-->
	<div id="dlg2" class="easyui-dialog" style="width: 600px; padding: 10px 20px" align="center"  data-options="modal:true,closed:true,resizable:true,collapsible:true,minimizable:true,maximizable:true"  buttons="#dlg2-buttons">
	   	<form id="fm2" method="post">
	   		<table cellpadding="5">	   			
	   			<input type="hidden" name="itemCode">
	   			<input type="hidden" name="reportId">	
	   			<input type="hidden" name="sysIntegrityScore">	
	   			<input type="hidden" name="folEfficiencyScore">	
	   			<input type="hidden" name="standardScore">		
	   			<tr>	   				
	   				<td>制度健全性评分结果:</td>
	   				<td><input class="easyui-combobox" 
							   name="integritySevResult"
							   style="width:150px;" 
			                   data-options="
					                 url:'${pageContext.request.contextPath}/codeSelect.do?type=EvaluateResult2',
					                 method:'get',
					                 valueField:'value',
					                 textField:'text',
					                 panelHeight:'auto'
			         "></td>
	   			</tr>
	   			<tr>
	   				<td>遵循有效性评分结果:</td>
	   				<td><input class="easyui-combobox" 
							   name="efficiencySevResult"
							   style="width:150px;" 
			                   data-options="
					                 url:'${pageContext.request.contextPath}/codeSelect.do?type=EvaluateResult2',
					                 method:'get',
					                 valueField:'value',
					                 textField:'text',
					                 panelHeight:'auto'
			         "></td>
	   			</tr>
	   			<tr>
	   				<td>调整意见:</td>
	   				<td><textarea name="adjustOpinion" class="easyui-validatebox" style="width: 200px; height: 60px;"></textarea></td>
	   			</tr>
	   		</table>
	   	</form>
	    <div id="dlg2-buttons">
    	<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="save2()">保存</a>
    	<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg2').dialog('close')">取消</a>
    </div>	
    </div>
</body>
	<script type="text/javascript">
	$(function() {
		$('#weigtData').hide();//隐藏datagrid1
		$('#projectCode').combobox({
             url:'${pageContext.request.contextPath}/codeSelect.do?type=projectCode2',
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
	});
	//搜索
	function serach(){
		$("#weigtData").show();
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
		var params = {};
		$('#serachFrom').find('input').each(function(){
	        var obj = $(this);
	        var name = obj.attr('name');
	        if(name){
	            params[name] = obj.val();
	        }
	    });
		console.log(params);
		$('#dg').datagrid({
			url:'${pageContext.request.contextPath}/alm_result/list.do?',
			queryParams:params
		});
	}
	//重置
	function reset(){
		$('#serachFrom').form("clear");
	}
	//修改结果明细项
    function edit(){
	    var rows = $('#dg').datagrid('getSelections');
	    if(rows.length==0){$.messager.alert('消息提示','请选择修改的明细项!','warning');return ;}
	    if(rows.length>1){$.messager.alert('消息提示','请选择一条明细项进行修改!','warning');return ;}
	    var row = $('#dg').datagrid('getSelected');
	    if(row.itemType==0){
	    	$('#dlg').dialog('open').dialog('setTitle','结果调整');
	 		$('#fm').form('load',row);
	 		url='${pageContext.request.contextPath}/alm_result/edit.do';
	    }else{
	    	$('#dlg2').dialog('open').dialog('setTitle','结果调整');
	 		$('#fm2').form('load',row);
	 		url='${pageContext.request.contextPath}/alm_result/edit.do';
	    }	
	}
    function save(){
		$('#fm').form('submit',{
			url: url,
			onSubmit: function(){
				return $(this).form('validate');
			},
			success: function(result){
				var result = eval('('+result+')');
				if (result.errorMsg){
					$.messager.show({
						title: 'Error',
						msg: result.errorMsg
					});
				} else {
					$('#dlg').dialog('close');		// close the dialog
					$('#dg').datagrid('reload');	// reload the user data
				}
			}
		});
    }
    function save2(){
		$('#fm2').form('submit',{
			url: url,
			onSubmit: function(){
				return $(this).form('validate');
			},
			success: function(result){
				var result = eval('('+result+')');
				if (result.errorMsg){
					$.messager.show({
						title: 'Error',
						msg: result.errorMsg
					});
				} else {
					$('#dlg2').dialog('close');		// close the dialog
					$('#dg').datagrid('reload');	// reload the user data
				}
			}
		});
    }
    //提交调整结果 一旦提交不能再修改
    function submit(){
    	var reportId=$('#reportId').combobox('getValue');
    	if($('#reportId').combobox('getValue')==""){$.messager.show({
    		title:'提示',
    		msg:'请选择评估单号再查询',
    		showType:'show'
    	}); return false;};
    	
    	$.messager.confirm('提醒','确认提交么?',function(r){
			if (r){
		    	$.get('${pageContext.request.contextPath}/alm_result/submit.do?reportId='+reportId,
						function(data){
							if(!data.success){
								$.messager.alert('警告',data.errorMsg,'error');
							}else{
								$('#dg').datagrid('reload');
								$.messager.show({
									title:'提示',
									msg:"提交成功！",
									showType:'show'
								});  					
							}
						}
		    	);
			}
		});
    }
	//导出数据以excel文件格式导出
	function modeldownload() {
		if($('#reportId').textbox('getText')==""){return $.messager.alert('提示',"错误无效",'warning');}
		var param = {reportId:$('#reportId').combobox('getValue')};
    	//使用参数执行一次查询，且将查询数据导出
    	jsutil.core.download("${pageContext.request.contextPath}/alm_result/download",param);
	} 
	</script>
</html>