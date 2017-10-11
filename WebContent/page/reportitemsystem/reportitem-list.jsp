<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%
	String path=request.getContextPath()+ request.getServletPath().substring(0,request.getServletPath().lastIndexOf("/") + 1); 
%>
<title>固定因子管理</title>
<style type="text/css">
.datagrid-header-row td {
	height: 40px;
	font-weight: bold;
}
</style>
</head>
<body>
<%@include file="/commons/statics.jsp"%>
	<!-- 搜索模块 -->
	<div class="easyui-panel" style="height: auto; width:100%; padding: 8px 0px 0px 0px;overflow: hidden;">
		<form id="serachFrom" method="post" >
		<table cellpadding="5" border='0' style="width: 100%; overflow: hidden;table-layout: fixed;">
	    		<tr>
	    			<td style="width:8%;text-align: right;" >
	    				<label>指标代码:</label>
	    			</td>
	    			<td style="width:13%;">
	    				<input style="width:100%;" name="reportItemCode" class="easyui-textbox" data-options="prompt: '指标代码'">
	    			</td>
	    			<td style="width:8%;text-align: right;" >
	    				<label>指标名称:</label>
	    			</td>
	    			<td style="width:13%;">
	    				<input style="width:100%;" name="reportItemName" class="easyui-textbox" data-options="prompt: '指标名称'"  >
	    			</td>
	    			<td style="width:8%;text-align: right;" >
	    				<label>报表代码:</label>
	    			</td>
	    			<td style="width:13%;">
	    				<input style="width:100%;" name="reportCode" class="easyui-textbox" data-options="prompt: '报表代码'" >
	    			</td>
	    			<td style="width:8%;text-align: right;">
		    			<label>报表名称:</label>
		    		</td>
		    		<td style="width:13%;">
		    			<input style="width:100%;" name="outItemNote" class="easyui-textbox" data-options="prompt: '报表名称'">
		    		</td>
	    		</tr>
	    		<tr>
	    		<td style="width:8%;text-align: right;" >
		    		<label>开始时间:</label>
		    	</td>
    			<td style="width:13%;">
    				<input style="width:100%;" class="easyui-datebox" id="beginTime" name="beginTime" data-options="prompt:'日期',validType:'md[\'2015-12-25\']'">
    			</td>
    			<td style="width:8%;text-align: right;" >
    				<label>结束时间:</label>
    			</td>
    			<td style="width:13%;">
    				<input style="width:100%;" class="easyui-datebox" id="endTime" name="endTime" data-options="prompt:'日期',validType:'md[\'2015-12-25\']'">
    			</td>
	    		<td style="width:8%;text-align: right;" >
		    		<label>是否启用:</label>
		    	</td>
    			<td style="width:13%;">
    				<select  class="easyui-combobox dillcombox show" name="temp2" data-options="prompt: '请选择是否启用',editable:false" style="width: 100%" >
					    <option style="width:43%;" value="1">是</option>
					    <option style="width:43%;" value="0">否</option>
					</select>
    			</td>	    		
	    		</tr>
	    		<tr>
	    			<td colspan="8" style="text-align: right; padding-right: 20px" >
	    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="serach()"  data-options="iconCls:'icon-search'" style="width:8%">查询</a>
		    			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="reset()" data-options="iconCls:'icon-reload'" style="width:8%;">重置</a>
	    			</td>
	    		
	    		</tr>
	    	</table>
		<!--    <div class="easyui-panel" id="searchmore" style="height:auto; width:inherit;border:0;margin:0" data-options="collapsed:true">
				<table style="width: 100%;" >
					<tr height="30">
						<td >
		    				<label>报表名称:</label>
		    			</td>
		    			<td >
		    				<input  name="outItemNote" class="easyui-textbox" data-options="prompt: '报表名称'">
		    			</td>
		    			<td style="width:8%;" >
		    				<label>开始时间:</label>
		    			</td>
		    			<td style="width:20%;">
		    				<input class="easyui-datebox" id="beginTime" name="beginTime" data-options="prompt:'日期',validType:'md[\'2015-12-25\']'">
		    			</td>
		    			<td style="width:8%;" >
		    				<label>结束时间:</label>
		    			</td>
		    			<td style="width:20%;">
		    				<input class="easyui-datebox" id="endTime" name="endTime" data-options="prompt:'日期',validType:'md[\'2015-12-25\']'">
		    			</td>
		    			<td style="width:16%;">
		    			</td>
		    		</tr>
		    		<tr height="30">
		    			<td style="width:8%;" >
		    				<label>是否启用:</label>
		    			</td>
		    			<td style="width:20%;">
		    				<select  class="easyui-combobox dillcombox show" name="temp2" style="width: 50%" >
							    <option value="1">是</option>
							    <option value="0">否</option>
							</select>
		    			</td>
		    			<td>
		    				<label>版本:</label>
		    			</td>
		    			<td>
		    				<input name="outItemVersion" class="easyui-textbox" data-options="prompt: '版本'">
		    			</td>
		    			<td></td>
		    			<td></td>
		    			<td>
		    			</td>
		    		</tr>
		    	</table>
			</div> -->
		</form>
	</div>
	<!-- <div id="ft" style="text-align:center;width:auto;border-top: 0;background: #fff;">
        	<a href="#" style="height:20px;border-top: 0;border: 1px solid #95B8E7;border-bottom: 0;border-bottom-right-radius: 0;
   			 border-bottom-left-radius: 0;"  class="easyui-linkbutton" id="lkbtn" iconCls="icon-arrows-down" plain="true" onclick="moresearch()">更多条件</a>
    </div> -->
<!-- 	操作，展示部分 -->
	 <div class="easyui-panel" style="border: 0;width: 100%">
	<%--  url=${pageContext.request.contextPath}/reportItem/listpage.do --%>
		<table id="dg" class="easyui-datagrid"
			toolbar="#toolbar" 
			rownumbers="true" 
			singleSelect="true"
			pagination="true" 
			striped="true"
			title=""
			nowrap="false"
			autoRowHeight="true"
			style="table-layout:fixed;word-break:break-all;width:100%;height:auto;"
			loadMsg="加载数据中..." 
			data-options="pageSize:10,pageList:[10,50,500,1000,5000]"
			>
			<div id="toolbar">
				<a href="#" class="easyui-linkbutton" iconCls="icon-more" plain="true" onclick="showReport()">查看</a> 
			</div>
			<thead>
				<tr>
					<th field="ck" checkbox=true></th>
					<th field="reportItemCode" width="12%" align="center" halign="center" data-options="sortable:true">指标代码</th>
					<th field="reportItemName" width="20%" align="left" halign="center">指标名称</th>
					<th field="reportCode" width="6%" align="center" halign="center"  >报表代码</th>
					<th field="outItemNote" width="25%" align="left" halign="center" >报表名称</th>
					<th field="reportTypeName" width="11%" align="left" halign="center"  >报告名称</th>
					<th field="outItemTypeName" width="7%" align="center" halign="center" >指标数据类型</th>
<!-- 					<th field="propertyInsurance1Name" width="5%" >产险Ⅰ</th>
					<th field="propertyInsurance2Name" width="5%" >产险Ⅱ</th>
					<th field="propertyInsurance3Name" width="5%" >产险Ⅲ</th>
					<th field="lifeInsurance1Name" width="5%" >寿险Ⅰ</th>
					<th field="lifeInsurance2Name" width="5%" >寿险Ⅱ</th>
					<th field="lifeInsurance3Name" width="5%" >寿险Ⅲ</th>
					<th field="lifeInsurance4Name" width="5%" >寿险Ⅳ</th>
					<th field="reinsuranceName" width="5%" >再保险</th>
					<th field="assetName" width="5%" >资产</th>
					<th field="group1Name" width="5%" >集团Ⅰ</th>
					<th field="group2Name" width="5%" >集团Ⅱ</th>
					<th field="group3Name" width="5%" >集团Ⅲ</th> -->
					<th field="halfYearReportName" width="5%" align="center" halign="center"  >季报</th>
					<th field="quarterReportName" width="5%"  align="center" halign="center" >快报</th>
					<th field="yearReportName" width="5%" align="center" halign="center" >临时报告</th>
				</tr>
			</thead>
		</table>
	</div>
	<!-- 弹出窗口 -->
		<div id="dlg" class="easyui-dialog" style="width:auto;height:auto;padding: 0px 0px 10px 30px;"
	    		closed="true" buttons="#dlg-buttons" align="center" modal="true" data-options="resizable:true">
    	<form id="fm" method="post">
	    	<table cellpadding="5" border="0">
		    		<tr>
		    			<td align="right" style="width: 15%"><label >指标代码:</label></td>
		    			<td style="width: 35%" >
							 <input class="easyui-textbox easyui-validatebox show" name="reportItemCode" id="reportItemCode" type="text" data-options="prompt:'指标代码',required:true,missingMessage:'请添加指标代码'"><span  class="primary-tooltip"></span>
						</td>
						<td align="right"><label>报表代码:</label></td>
		    			<td	>
							 <input class="easyui-textbox easyui-validatebox show" name="reportCode" id="reportCode" type="text" data-options="prompt:'报表代码',required:true,missingMessage:'请添加报表代码'">
						</td>
					</tr>
					<tr>	
						<td align="right" style="width: 15%"><label>指标名称:</label></td>
		    			<td  style="width: 35%" colspan="3">
							 <input style="width:97%" class="easyui-textbox easyui-validatebox show" name="reportItemName" id="reportItemName" type="text" data-options="prompt:'指标名称',required:true,missingMessage:'请添加指标名称'">
						</td>
		    		</tr>
		    		<tr>
		    			
						<td align="right" style="width: 15%"><label>报表名称:</label></td>
		    			<td style="width: 35%" colspan="3">
		    				<input style="width:97%" class="easyui-textbox easyui-validatebox show" name="outItemNote" id="outItemNote" type="text" data-options="prompt:'报表名称',required:true,missingMessage:'请添加报表名称'">
		    			</td>
		    		</tr>
		    		<tr>	    		
						<td align="right" style="width: 15%"><label>报告名称:</label></td>
		    			<td style="width: 35%" >
							 <input id="reportType" class="easyui-combobox  show" name="reportType" url=${pageContext.request.contextPath}/codeSelect.do?type=reportname data-options="editable:false,valueField:'value',textField:'text',prompt:'报告名称',method:'get',panelHeight:'100',required:true,missingMessage:'请添加报告名称'">
						</td>
						<td align="right" style="width: 15%"><label>数据类型:</label></td>
		    			<td style="width: 35%">
		    				<input id="outItemType" class="easyui-combobox  show" name="outItemType" url=${pageContext.request.contextPath}/CfCodeManage.do?type=datatype data-options="valueField:'value',textField:'text',editable:false,prompt:'指标数据类型',method:'get',panelHeight:'100',required:true,missingMessage:'请添加指标数据类型'">
						</td>
		    		</tr>
		    		<tr>
<!-- 						<td align="left"><label>版本:</label></td>
		    			<td align="left">
		    				 <input class="easyui-textbox easyui-validatebox show" name="outItemVersion" id="outItemVersion"data-options="prompt:'版本'"><font color="red">&nbsp;</font>
						</td> -->
						<td align="right" style="width: 15%"><label>日期:</label></td>
		    			<td style="width: 35%">
							 <input class="easyui-datebox  show" name="outItemCreatTime" id="outItemCreatTime"  data-options="prompt:'日期',validType:'md[\'2015-12-25\']',required:true,missingMessage:'请选择日期'">
						</td>
						<td align="right" style="width: 15%"><label>是否启用:</label></td>
   						<td style="width: 35%">
   							<select  class="easyui-combobox dillcombox show" name="temp2" style="width: 97%" data-options="editable:false">
							    <option value="1" selected>是</option>
							    <option value="0">否</option>
							</select>
   						</td>
		    		</tr>
<!-- 		    		<tr >
		    			<td align="left"><label>指标说明:</label></td>
		    			<td colspan="3" align="left">
		    				<textarea style="width: 90%;height:50px;border-radius:5px;border: 1px solid #95B8E7;" class="easyui-validatebox showtextarea" name="outItemNote" id="outItemNote"></textarea>
		    			</td>
		    		</tr> -->
		    		<tr>
		    			<td align="right" colspan="4" style="margin:0;padding:0;">
		    				<table style="width: 100%">
		    					<tr style="width: 100%">
		    						<td style="width: 15%" align="right" ><label>产险:</label></td>
		    						<td style="width:18%">
		    							<select data-options="editable:false" class="easyui-combobox dillcombox show" name="propertyInsurance1" style="width: 80%" >
										    <option value="1" selected>是</option>
										    <option value="0">否</option>
										</select>
		    						</td>
		    						<td style="width: 15%" align="right" ><label>寿险:</label></td>
		    						<td style="width:18%">
		    							<select data-options="editable:false" class="easyui-combobox dillcombox show" name="lifeInsurance1" style="width: 80%">
										    <option value="1" selected>是</option>
										    <option value="0">否</option>
										</select>
		    						</td>
		    						<td style="width: 15%" align="right" ><label>再保险:</label></td>
		    						<td style="width:18%">
		    							<select data-options="editable:false" class="easyui-combobox dillcombox show" name="reinsurance" style="width: 80%">
										    <option value="1" selected>是</option>
										    <option value="0">否</option>
										</select>
		    						</td>
		    					</tr>
<!-- 		    					<tr>
		    						<td><label>寿险1:</label></td>
		    						<td>
		    							<select data-options="editable:false" class="easyui-combobox dillcombox show" name="lifeInsurance1" style="width: 80%">
										    <option value="1" selected>是</option>
										    <option value="0">否</option>
										</select>
		    						</td>
		    						<td><label>寿险2:</label></td>
		    						<td>
		    							<select data-options="editable:false" class="easyui-combobox dillcombox show" name="lifeInsurance2" style="width: 80%">
										    <option value="1" selected>是</option>
										    <option value="0">否</option>
										</select>
		    						</td>
		    						<td><label>寿险3:</label></td>
		    						<td>
		    							<select data-options="editable:false" class="easyui-combobox dillcombox show" name="lifeInsurance3" style="width: 80%">
										    <option value="1" selected>是</option>
										    <option value="0">否</option>
										</select>
		    						</td>
		    					</tr>
		    					<tr>
		    						<td><label>寿险4:</label></td>
		    						<td>
		    							<select data-options="editable:false" class="easyui-combobox dillcombox show" name="lifeInsurance4" style="width: 80%">
										    <option value="1" selected>是</option>
										    <option value="0">否</option>
										</select>
		    						</td>
		    						<td><label>再保险:</label></td>
		    						<td>
		    							<select data-options="editable:false" class="easyui-combobox dillcombox show" name="reinsurance" style="width: 80%">
										    <option value="1" selected>是</option>
										    <option value="0">否</option>
										</select>
		    						</td>
		    						<td><label>资产:</label></td>
		    						<td>
		    							<select data-options="editable:false" class="easyui-combobox dillcombox show" name="asset" style="width: 80%">
										    <option value="1" selected>是</option>
										    <option value="0">否</option>
										</select>
		    						</td>
		    					</tr>
		    					<tr>
		    						<td><label>集团1:</label></td>
		    						<td>
		    							<select data-options="editable:false" class="easyui-combobox dillcombox show" name="group1" style="width: 80%">
										    <option value="1" selected>是</option>
										    <option value="0">否</option>
										</select>
		    						</td>
		    						<td><label>集团2:</label></td>
		    						<td>
		    							<select data-options="editable:false" class="easyui-combobox dillcombox show" name="group2" style="width: 80%">
										    <option value="1" selected>是</option>
										    <option value="0">否</option>
										</select>
		    						</td>
		    						<td><label>集团3:</label></td>
		    						<td>
		    							<select data-options="editable:false" class="easyui-combobox dillcombox show" name="group3" style="width: 80%">
										    <option value="1" selected>是</option>
										    <option value="0">否</option>
										</select>
		    						</td>
		    					</tr> -->
		    					<tr>
		    						<td style="width: 15%" align="right" ><label>季报:</label></td>
		    						<td>
		    							<select data-options="editable:false" class="easyui-combobox dillcombox show" name="halfYearReport" style="width: 80%">
										    <option value="1" selected>是</option>
										    <option value="0">否</option>
										</select>
		    						</td>
		    						<td style="width: 15%" align="right" ><label>季度快报:</label></td>
		    						<td>
		    							<select data-options="editable:false" class="easyui-combobox dillcombox show" name="quarterReport" style="width: 80%">
										    <option value="1" selected>是</option>
										    <option value="0">否</option>
										</select>
		    						</td>
		    						<td style="width: 15%" align="right" ><label>临时报告:</label></td>
		    						<td>
		    							<select data-options="editable:false" class="easyui-combobox dillcombox show"  name="yearReport" style="width: 80%">
										   	<option value="1" selected="selected">是</option>
										    <option value="0">否</option>
										</select>
		    						</td>
		    					</tr>
		    				</table>
		    			</td>
		    		</tr>
	    	</table>
    	</form>
    </div>
    <!-- <div id="dlg-buttons">
    	<a href="#" class="easyui-linkbutton showbutton" iconCls="icon-ok" onclick="saveReport()">保存</a>
    	<a href="#" class="easyui-linkbutton showbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">取消</a>
    </div> -->
</body>
<script  type="text/javascript">
//editable:false设置不能手动输入
//覆盖easyui默认的校验格式，校验时间的输入格式。
reset();
$.extend($.fn.validatebox.defaults.rules, {
	md: {
		validator: function(value, param){
			/* var d1 = $.fn.datebox.defaults.parser(param[0]);
			va r d2 = $.fn.datebox.defaults.parser(value);*/
			//var d3=$(this).datebox('getValue');
			//console.log(value+param);
			return /^\d{4}-\d{2}-\d{2}$/.test(value);
		},
		message: '时间格式必须按照这样来写 {0}.'
	}
});
$('.dillcombox').combobox({
	panelHeight:'50'
});
	var dialogType="";
	//新建
	function newReport(flag){
		dialogType="new";
		$('#dlg').dialog({
			onClose:function(){
				$('#reportItemCode').textbox({
					onChange:$
				});
				$('.primary-tooltip').tooltip('hide');
			}
		});
	   	$('#dlg').dialog('open').dialog('setTitle','新建固定因子');
	   	$('#fm').form('clear');
	   	$('.dillcombox').combobox('setValue','1');
	   	url='${pageContext.request.contextPath}/reportItem/add.do';
	   	$('#reportItemCode').textbox({
			onChange:primaryValidata
		});
   	}
	//编辑
	function editReport(){
		var rows = $('#dg').datagrid('getSelections');
		if(rows.length>1){
			jsutil.msg.warn("请选择一条数据进行编辑");
			return;
		}
		dialogType="edit";
		$('#fm').form('clear');
		var row = $('#dg').datagrid('getSelected');
		$('#dlg').dialog({
			onClose:function(){
				$('#reportItemCode').combo('enable',true);
			}
		});
		if(row){
			url='${pageContext.request.contextPath}/reportItem/update.do?id='+row.reportItemCode;
			$('#dlg').dialog('open').dialog('setTitle','编辑固定因子');
			//编辑逻辑
			$('#fm').form('load',row);
			$('#reportItemCode').combo('disable',true);
		}else{
			$.messager.alert({
				title:'提示！',
				msg:'请先选择要编辑的元素',
				icon:'warning'
			});
		}
	}
	//查看
	function showReport(){
		var rows = $('#dg').datagrid('getSelections');
		if(rows.length>1){
			jsutil.msg.warn("请选择一条数据查看");
			return;
		}
		dialogType="show";
		$('#fm').form('clear');
		var row = $('#dg').datagrid('getSelected');
		$('#dlg').dialog({
			onClose:function(){
				$('.show').combo('readonly',false);
				$('.showtextarea').attr('disabled',false);
				$('.showbutton').linkbutton('enable');
			}
		});
		if(row){
			$('.show').combo('readonly',true);
			$('.showtextarea').attr('disabled',true);
			$('#dlg').dialog('open').dialog('setTitle','查看固定因子');
			//编辑逻辑
			$('#fm').form('load',row);
		 	$('.showbutton').linkbutton('disable'); 
		}else{
			$.messager.alert({
				title:'提示！',
				msg:'请先选择要查看的元素',
				icon:'warning'
			});
		}
	}
	//删除
	function destroyReport(){
		var row = $('#dg').datagrid('getSelections');
		if(row.length>0){
			$.messager.confirm({
				title:'提示！',
				msg:'确定删除数据吗？',
				fn:function(r){
					if(r){
						//删除逻辑
						var param="";
						for (var i=0;i<row.length;i++)
						{
							param=param+row[i].reportItemCode+"|";
						}
						$.post('${pageContext.request.contextPath}/reportItem/delete.do',{id:param},function(result){
							if(result.success){
								$('#dg').datagrid('reload');
								$.messager.show({
									title:'提示',
									msg:'删除成功'
								});
							}else{
								$.messager.alert({
									title:'错误',
									msg:'删除失败',
									icon:'error'
								});
							}
						});
					}
				}
			});
		}else{
			$.messager.alert({
				title:'提示！',
				msg:'请先选择要删除的元素',
				icon:'warning'
			});
		}
		
	}
	//保存
	function saveReport(){
		if(dialogType=="new"){
			if(!primaryValidata()){
				return false;
			}
		}
		$.ajax({
			url:url,
			data:$('#fm').serialize(),
			type:'post',
			beforeSend: function(){
				var flag=$('#fm').form('validate');
				if(!flag){
					$.messager.show({
						title: '提示',
						msg:"请填写带*信息栏"
					});	
				}
				return flag;
			},
			success:function(result){
				if (result.success){
					$('#dg').datagrid('reload');	// reload the user data
					$('#dlg').dialog('close');		// close the dialog
					$.messager.show({
						title: '成功',
						msg:"操作成功"
					});		
				}else if (result.errorMsg){
					$.messager.alert({
						title: '错误',
						msg: result.errorMsg,
						icon:'error'
					});
				} 
			}
		});
	}
	//重置搜索框
	function reset(){
		$('#serachFrom').form('clear');
	}
	//高级搜索框

	function moresearch(){
		$('#lkbtn').blur();
			var flag=$('#searchmore').panel('options').collapsed;
			if(flag){
				//展开
				$('.icon-arrows-down').addClass('icon-arrows-up');
				$('.icon-arrows-down').removeClass('icon-arrows-down');
				$('#searchmore').panel('expand',true); 
				flag=false;
			}else{
				//关闭
				$('.icon-arrows-up').addClass('icon-arrows-down');
				$('.icon-arrows-up').removeClass('icon-arrows-up');
				$('#searchmore').panel('collapse',true);
				flag=true;
				reset();
			}
	}
	//查询
	function serach(){
		var endTime=$('#endTime').datebox('isValid');
		var beginTime=$('#beginTime').datebox('isValid');
		var endval=$('#endTime').datebox('getValue');
		var beginval=$('#beginTime').datebox('getValue');
		if(!(beginval<=endval)){
			if(!endval==""){
				$.messager.alert({
					title:'提示',
					msg:'结束日期不能早于开始日期',
					icon:'warning'
				});
				return false;
			}
		}
		if(endTime&&beginTime){
			var params = {};
			$('#serachFrom').find('input').each(function(){
		        var obj = $(this);
		        var name = obj.attr('name');
		        if(name){
		        	var value=obj.val();
		        	if(value!="")
		            params[name] = obj.val();
		        }
		    });
			//$('#dg').datagrid('load', params);
			$("#dg").datagrid({
				url:'${pageContext.request.contextPath}/reportItem/listpage.do',
				queryParams: params,
				onBeforeLoad: function(){
					if(!$('#serachFrom').form('validate')) return false;
				},
				onLoadSuccess: function(data){
					if(data.total>0) {
		 				return  false;
		 			}
		 			$.messager.alert("提示","未查询到相关数据","info");
				}
			}); 
		}else{
			$.messager.alert({
				title:'提示',
				msg:'日期格式错误！',
				icon:'warning'
			});
		}
	}
	//主键校验提示框内容
	function primaryValidata(){
		var url='${pageContext.request.contextPath}/reportItem/find.do';
		var param= {};
		param.reportItemCode="reportItemCode";
		var flag = jsutil.primaryValidata(url,param);
		return flag;
	}

</script>
</html>