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
<title>行可扩展因子管理</title>
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
	    		<tr >
	    			<td style="width:8%;text-align: right;" >
	    				<label id="rrr">表代码:</label>
	    			</td>
	    			<td style="width:13%;">
	    				<input style="width:100%;" name="tableCode" class="easyui-textbox" data-options="prompt: '表代码'">
	    			</td>
	    			<td style="width:8%;text-align: right;" >
	    				<label>表名称:</label>
	    			</td>
	    			<td style="width:13%;">
	    				<input style="width:100%;"name="tableName" class="easyui-textbox" data-options="prompt: '表名称'"  >
	    			</td>
	    			<td style="width:8%;text-align: right;" >
	    				<label>列代码:</label>
	    			</td>
	    			<td style="width:13%;">
	    				<input style="width:100%;" name="colCode" class="easyui-textbox" data-options="prompt: '列代码'" >
	    			</td>
	    			<td style="width:8%;text-align: right;" >
		    			<label>列名称:</label>
		    		</td>
		    		<td style="width:13%;">
		    			<input style="width:100%;" name="colName" class="easyui-textbox" data-options="prompt: '列名称'">
		    		</td>
	    		</tr>
	    		<tr>
	    			<td colspan="8" style="text-align: right; padding-right: 20px" >
	    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="serach()"  data-options="iconCls:'icon-search'" style="width:8%">查询</a>
		    			<a href="javascript:void(0)" class="easyui-linkbutton" onclick="reset()" data-options="iconCls:'icon-reload'" style="width:8%;">重置</a>
	    			</td>
	    		</tr>
	    	</table>
		   <!-- <div class="easyui-panel" id="searchmore" style="height:auto; width:inherit;border:0;margin:0" data-options="collapsed:true">
				<table style="width: 100%;" >
					<tr height="30">
		    			<td style="width:8%;" >
		    				<label>列名称:</label>
		    			</td>
		    			<td style="width:20%;">
		    				<input  name="colName" class="easyui-textbox" data-options="prompt: '列名称'">
		    			</td>
		    			<td style="width:8%;" >
		    				<label>是否启用:</label>
		    			</td>
		    			<td style="width:20%;">
		    				<select  class="easyui-combobox dillcombox show" name="temp2" style="width: 50%" >
							    <option value="1">是</option>
							    <option value="0">否</option>
							</select>
		    			</td>
		    			<td style="width:8%;">
		    			</td>
		    			<td style="width:20%;">
		    			</td>
		    			<td style="width:16%;">
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
		<table id="dg" class="easyui-datagrid"
			toolbar="#toolbar" 
			rownumbers="true" 
			singleSelect="true" 
			pagination="true" 
			striped="true"
			title=""
			nowrap="false"
			autoRowHeight="false"
			multiSort="true"
			remoteSort="true"
			style="table-layout:fixed;word-break:break-all;width:100%;height:auto;"		
			loadMsg="加载数据中..." 
			>
			<div id="toolbar">
				<a href="#" class="easyui-linkbutton" iconCls="icon-more" plain="true" onclick="showReport()">查看</a> 
			</div>
			<thead>
				<tr>
					<th field="ck" checkbox=true></th>
					<th field="tableCode" width="10%" align="center" halign="center" data-options="sortable:true">表代码</th>
					<th field="tableName" width="25%" align="left" halign="center" data-options="sortable:true">表名称</th>
					<th field="colCode" width="15%" align="center" halign="center" data-options="sortable:true">列代码</th>
					<th field="colName" width="15%" align="center" halign="center" data-options="sortable:true">列名称</th>
<!-- 					<th field="outItemNote" width="25%" data-options="sortable:true">说明</th> -->
					<th field="reportTypeName" align="center" halign="center" width="10%" >报告名称</th>
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
					<th field="halfYearReportName" align="center" halign="center"  width="7%" >季报</th>
					<th field="quarterReportName" align="center" halign="center"  width="7%" >季度快报</th>
					<th field="yearReportName" align="center" halign="center" width="7%" >临时报告</th>
				</tr>
			</thead>
		</table>
	</div>
	<!-- 弹出窗口 -->
		<div id="dlg" class="easyui-dialog" style="width:auto;height:auto;padding:10px 30px;"
	    		closed="true" buttons="#dlg-buttons" align="center" modal="true" data-options="resizable:true">
    	<form id="fm" method="post">
	    	<table cellpadding="5">
	    			<tr>
		    			<td align="left" style="width: 15%""><label >表代码:</label></td>
		    			<td align="left" style="width: 35%">
							 <input class="easyui-textbox easyui-validatebox show" name="tableCode" id="tableCode" type="text" data-options="prompt:'表代码',required:true,missingMessage:'请添加表代码'"><span class="primary-tooltip"></span>
						</td>
						<td align="left" style="width: 15%">
							<label>数据类型:</label>
						</td>
						<td align="left" style="width: 15%">
		    				<input id="colType" class="easyui-combobox  show" name="colType" url=${pageContext.request.contextPath}/CfCodeManage.do?type=datatype data-options="valueField:'value',textField:'text',editable:false,prompt:'数据类型',method:'get',panelHeight:'100',required:true,missingMessage:'请添加指标数据类型'">
						</td>
		    		</tr>
		    		<tr >
		    			<td align="left" style="width: 15%"><label>表名称:</label></td>
		    			<td colspan="3"  align="left" style="width: 35%">
							 <input style="width: 98%;"class="easyui-textbox easyui-validatebox show" name="tableName" id="tableName" type="text" data-options="prompt:'指标名称',required:true,missingMessage:'请添加表名称'">
						</td>
						
		    		</tr>
		    		<tr>
		    			<td align="left" style="width: 15%"><label>列代码:</label></td>
		    			<td align="left" style="width: 35%">
							 <input class="easyui-textbox easyui-validatebox show" name="colCode" id="colCode" type="text" data-options="prompt:'列代码',required:true,missingMessage:'请添加列代码'"><span class="primary-tooltip"></span>
						</td>
						<td align="left" style="width: 15%"><label>列名称:</label></td>
			    		<td  colspan="3" align="left" style="width: 35%">
			    			<input class="easyui-textbox easyui-validatebox show" name="colName" id="colName" data-options="prompt:'列名称'"><font color="red">&nbsp;</font>
						</td>
		    		</tr>
		    		
		    		<tr>
						<td align="left" style="width: 15%"><label>报告名称:</label></td>
		    			<td align="left" style="width: 35%">
		    				<input id="reportType" class="easyui-combobox  show" name="reportType" url=${pageContext.request.contextPath}/codeSelect.do?type=reportname data-options="valueField:'value',textField:'text',editable:false,prompt:'报告名称',method:'get',panelHeight:'100',required:true,missingMessage:'请选择报告名称'"><span class="primary-tooltip"></span>
						</td>
						<td align="left" style="width: 15%"><label>日期:</label></td>
			    		<td align="left" style="width: 35%">
							<input class="easyui-datebox  show" name="outItemCreatTime" id="outItemCreatTime"  data-options="prompt:'日期',validType:'md[\'2015-12-25\']',required:true,missingMessage:'请添加指标名称'">
						</td>
		    		</tr>
		    		
		    		<tr >
		    			<td align="left" style="width: 15%"><label>指标说明:</label></td>
		    			<td colspan="3" >
		    				<textarea style="width: 90%;height:50px;border-radius:5px;border: 1px solid #95B8E7;" class="easyui-validatebox showtextarea" name="outItemNote" id="outItemNote"></textarea>
		    			</td>
		    		</tr>
		    		<tr>
		    			<td colspan="4" style="margin:0;padding:0;">
		    				<table style="width: 100%">
		    					<tr style="width: 100%">
		    						<td style="width: 15%" ><label>产险:</label></td>
		    						<td style="width:18%">
		    							<select data-options="editable:false" class="easyui-combobox dillcombox show" name="propertyInsurance1" style="width: 80%" >
										    <option value="1" selected>是</option>
										    <option value="0">否</option>
										</select>
		    						</td>
		    						<td><label>寿险:</label></td>
		    						<td>
		    							<select data-options="editable:false" class="easyui-combobox dillcombox show" name="lifeInsurance1" style="width: 80%">
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
		    						<td><label>季报:</label></td>
		    						<td>
		    							<select data-options="editable:false" class="easyui-combobox dillcombox show" name="quarterReport" style="width: 80%">
										    <option value="1" selected>是</option>
										    <option value="0">否</option>
										</select>
		    						</td>
		    						<td><label>季度快报:</label></td>
		    						<td>
		    							<select data-options="editable:false" class="easyui-combobox dillcombox show" name="halfYearReport" style="width: 80%">
										    <option value="1" selected>是</option>
										    <option value="0">否</option>
										</select>
		    						</td>
		    						<td><label>临时报告:</label></td>
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
   <!--  <div id="dlg-buttons">
    	<a href="#" class="easyui-linkbutton showbutton" iconCls="icon-ok" onclick="saveReport()">保存</a>
    	<a href="#" class="easyui-linkbutton showbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">取消</a>
    </div> -->
</body>
<script  type="text/javascript">
reset();
//editable:false设置不能手动输入
//覆盖easyui默认的校验格式，校验时间的输入格式。
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

$(function(){
	serach();
})

$('.dillcombox').combobox({
	panelHeight:'50'
});
	//新建
	var dialogType="";
	function newReport(flag){
		dialogType="new";
		//弹出框关闭之后，触发校验事件关闭，防止编辑查看窗口触发
		$('#dlg').dialog({
			onClose:function(){
				$('#tableCode,#colCode').textbox({
					onChange:$
				});
				$('#reportType').combobox({
					onChange:$
				});
				$('.primary-tooltip').tooltip('hide');
			}
		});
	   	$('#dlg').dialog('open').dialog('setTitle','新建');
	   	$('#fm').form('clear');
	   	$('.dillcombox').combobox('setValue','1');
	   	url='${pageContext.request.contextPath}/reportRowAdd/add.do';
	   	
	   	//触发校验的事件
	   	$('#tableCode,#colCode').textbox({
			onChange:primaryValidata
		});
		$('#reportType').combobox({
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
				$('#reportType,#colCode,#tableCode').combo('enable',true);
			}
		});
		if(row){
			var _url='&colCode='+row.colCode+'&reportType='+row.reportType;
			url='${pageContext.request.contextPath}/reportRowAdd/update.do?tableCode='+row.tableCode+_url;
			$('#dlg').dialog('open').dialog('setTitle','编辑');
			//编辑逻辑
			$('#fm').form('load',row);
			$('#reportType,#colCode,#tableCode').combo('disable',true);
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
						var tableCode="";
						var colCode="";
						var reportType="";
						for (var i=0;i<row.length;i++)
						{
							tableCode=tableCode+row[i].tableCode+"|";
							colCode=colCode+row[i].colCode+"|";
							reportType=reportType+row[i].reportType+"|";
						}
						$.post('${pageContext.request.contextPath}/reportRowAdd/delete.do',{tableCode:tableCode,colCode:colCode,reportType:reportType},function(result){
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
	//时间校验   未完待续....
/* 	function timeparser(time){
		var t=Date.parse(time);
		console.log(time+"|"+t);
		if (!isNaN(t)){
			console.log(new Date(t));
			return new Date(t);
		} else {
			console.log(new Date());
			return new Date();
		}
	} */
	//主键校验提示框内容
	function primaryValidata(){
		var url='${pageContext.request.contextPath}/reportRowAdd/find.do';
		var param= {};
		param.tableCode="tableCode";
		param.colCode="colCode";
		param.reportType="reportType";
		var flag = jsutil.primaryValidata(url,param);
		return flag;
	}
	//查询
	function serach(){
		/* var endTime=$('#endTime').datebox('isValid');
		var beginTime=$('#beginTime').datebox('isValid');
		var endval=$('#endTime').datebox('getValue');
		var beginval=$('#beginTime').datebox('getValue');
		if(!(beginval<=endval)){ */
			/* if(!endval==""){
				$.messager.alert({
					title:'提示',
					msg:'结束日期不能早于开始日期',
					icon:'warning'
				});
				return false;
			} */
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
				url:'${pageContext.request.contextPath}/reportRowAdd/listpage.do',
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
	}
</script>
</html>