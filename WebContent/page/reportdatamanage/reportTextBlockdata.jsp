<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>
<%@include file="/commons/statics.jsp"%>
<%@include file="/commons/kindeditor.jsp"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>指标文本块数据</title>
<style type="text/css">
.datagrid-header-row td {
	height: 40px;
	font-weight: bold;
}
</style>
</head>
<%
	String path = request.getContextPath();
	String basepath = request.getContextPath()
			+ request.getServletPath().substring(0,
					request.getServletPath().lastIndexOf("/") + 1);
%>
<body>
<!-- 搜索模块 -->
	<div class="easyui-panel" style="height: auto; width:100%; padding: 8px 0px 0px 0px;overflow: hidden;">
		<form  id="serachFrom" style="margin-bottom:0;">		
			<table cellpadding="5" border='0' style="width: 100%; overflow: hidden;table-layout: fixed;">		
		    		<tr >
					    <td style="width:8%;text-align: right;" >
		    				<label>报送号:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input id="reportId" name="reportId" class="easyui-textbox" data-options="prompt: '报送号'"  style="width:100%;">
		    			</td>
			    		<td style="width:10%;text-align: right;" >
		    				<label>报送年度:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<!-- <input id="year" name="year" class="easyui-textbox" data-options="prompt: '报送年度'"  style="width:100%;"> -->
		    				<input style="width: 100%;" id="year" name="year" class="easyui-textbox" data-options="  editable:false , disabled:false , missingMessage:'请输入年份' "/>
		    			</td>
		    			 <td style="width:8%;text-align: right;" >
		    				<label>报送季度:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input id="quarter" name="quarter" class="easyui-combobox"  style="width:100%;" data-options="  prompt: '报送季度',editable:false, missingMessage:'请添加季度',url: '${pageContext.request.contextPath}/codeSelect.do?type=quarter', method: 'get',valueField:'value', textField:'text',groupField:'group'">
		    			</td>
			    		<td style="width:8%;text-align: right;" >
		    				<label>报送类型:</label>
		    			</td>
		    			<td style="width:13%;">
							<select class="easyui-combobox" id="reportRate" name="reportRate" style="width:100%;" readonly>
							<option value="2">季度报告</option>
					    	</select>
					    </td>	
				    </tr>
				    <!-- 隐藏掉 -->
				    <!-- 
				    <tr>
				    	<td style="width:8%;text-align: right;" >
		    				<label>报送状态:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input id="reportState" name="reportState" class="easyui-combobox"  style="width:100%;" data-options=" prompt: '报送状态',editable:false, url: '${pageContext.request.contextPath}/codeSelect.do?type=cfreportstate', method: 'get', valueField:'value',textField:'text', groupField:'group'">
		    			</td>
			    		<td style="width:8%;text-align: right;" >
		    				<label>是否完成手工导入:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<select id="handleFlag" name="handleFlag" class="easyui-combobox" data-options="prompt: '是否完成手工导入',editable:false,"   style="width:100%;">
		    					<option value=""></option>
		    					<option value="0">否</option>
		    					<option value="1">是</option>
		    				</select>
		    			</td>
				    </tr>
				    -->
				    <tr>
	    				<td colspan="8" style="text-align: right; padding-right: 20px" >
		    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="serach()"  data-options="iconCls:'icon-search'" style="width:8%;">搜索</a>
		    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="reset()" data-options="iconCls:'icon-reload'" style="width:8%;">重置</a>
	    				</td>
				    </tr>
				 </table>
		</form>		
	</div>
	<!-- <div id="ft" style="text-align:center;width:auto;border-top: 0;background: #fff;">
        	<a href="#" style="height:20px;border-top: 0;border: 1px solid #95B8E7;border-bottom: 0;border-bottom-right-radius: 0;
   			 border-bottom-left-radius: 0;"  class="easyui-linkbutton" id="lkbtn" iconCls="icon-arrows-down" plain="true" onclick="moresearch()">更多条件</a>
    </div> -->
<!-- 报送号的	操作，展示部分 -->
 <div class="easyui-panel" style="border: 0;width: 100%">
		<table id="dg" class="easyui-datagrid"
		data-options="
			iconCls:'icon-edit',
			toolbar:'#toolbar' ,
			rownumbers:'true',
			fitColumns:'true',
			singleSelect:'true', 
			pagination:'true', 
			pageSize:5,
			pageList:[5,10,15,20],
			striped:'true',
			nowrap:'false',
			onClickRow:onClickRow,
			autoRowHeight:'false',
			style:'width:100%;height:auto;',
			loadMsg:'加载数据中...'
			"
			style="table-layout:fixed;word-break:break-all;width:100%;height:auto;"			
			>
			<div id="toolbar" style="width: 100%">
				<a href="#" class="easyui-linkbutton"  iconCls="icon-search" plain="true" onclick="showLook();">查看文本块指标</a> 
			    <a href="#" class="easyui-linkbutton"  iconCls="icon-add" plain="true" onclick="wordCopy();">文本块指标复制</a> 
			</div>
			<thead>
				<tr>
					<th field="ck" checkbox=true></th>
					<th field="reportId" width="31%" align="center" halign="center">报送号</th>
					<th field="year" width="23%" align="center" halign="center">报送年度</th>
					<th field="quarter" width="23%" hidden>报送季度</th>
					<th field="quarterName" width="23%" align="center" halign="center">报送季度</th>
					<th field="reportType" width="23%" hidden>报送类型</th>
					<th field="reportTypeName" width="23%" align="center" halign="center" >报送类型</th>
					<!-- 
					<th field="department" width="30" 
						formatter="resultCategory"  
						editor="{type:'combobox',options:{
								valueField:'value',
								textField:'text',
								method:'get',
								url:'${pageContext.request.contextPath}/codeSelect.do?type=collectdepart',
							}}">选择部门</th>
							 -->
					<!-- 		 
					<th field="reportState" width="25" hidden>报送状态</th>
					<th field="reportStateName" width="14%" align="center" halign="center">报送状态</th>
					<th field="handleFlag" width="14%" formatter="isYON" align="center" halign="center">是否完成手工导入</th>
				    -->
				</tr>
			</thead>
		</table>
	</div>
	<br>
    <br>
	<!-- 文本块的编辑	操作，展示部分 -->
	<div id="look"  class="easyui-panel" style="border: 0; width: 100%;" >
 		<table id="dg1" class="easyui-datagrid"  style="table-layout:fixed;word-break:break-all;width:100%;height:auto;">
		</table>
	</div>
	<div id="dlg" class="easyui-dialog"
		style="width: 90%;  padding: 10px 20px ;top :20px" align="center"
		data-options="modal:true,closed:true,resizable:true,collapsible:true,minimizable:true,maximizable:true"
		buttons="#dlg-buttons">
		<form id="fm" method="post" style="width: 90%;">
			<table cellpadding="5" border="0" style="width: 90%;">
				<tr>
					<td style="width: 8%">
						<label>季&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;度：</label>
					</td>
					<td style="width: 20%">
						<select id="quarter" name="quarter" class="easyui-combobox"
							style="width: 70%;"
							data-options="
		    				prompt: '季度', 
		    				url:'<%=path%>/codeSelect.do?type=quarter',
		    				method: 'get',
							valueField:'value',
							textField:'text',
							groupField:'group'" readonly>
						</select>
					</td>
					<td style="width: 8%">
						<label>年&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;度：</label>
					</td>
					<td style="width: 20%"><input type="text" id="month" name="month"
						class="easyui-numberbox" data-options="prompt: '年度'" style="width: 70%;" readonly></td>
				<!-- </tr>
				<tr> -->
					<td style="width: 8%"><label>报告类型:</label></td>
						<td style="width: 20%">
							<select class="easyui-combobox" id="reportRate" name="reportRate" style="width:70%;" readonly>
								<option value="2">季度报告</option>
						    </select>
					</td>
				</tr>
				<tr>
					<td style="width: 8%"><label>因子代码:</label></td>
					<td style="width:20%;"><input   class="easyui-textbox" type="text"  
						id="outItemCode" name="outItemCode" style="width: 80%"
						class="easyui-validatebox textbox" data-options="prompt:'因子代码'"
						readonly="readonly" >
					</td>
					<td style="width: 8%"><label>报送号:</label></td>
					<td style="width: 20%"><input class="easyui-textbox" type="text"
						id="reportId" name="reportId"
						class="easyui-validatebox textbox" data-options="prompt:'报送号'"
						readonly="readonly" style="width: 90%;"></td>
					
				</tr>
				<tr>		
					<td><label>因子名称:</label></td>
					<td colspan="8"><input class="easyui-textbox" type="text"
						id="reportItemName" name="reportItemName"
						class="easyui-validatebox textbox" data-options="prompt:'因子名称'"
						readonly="readonly" style="width: 96%;"></td>
				</tr> 
			</table>
				<!-- <textarea id="descText" name="descText" cols="100" rows="8" style="width:700px;height:200px;visibility:hidden;"></textarea> -->
				 <textarea id="descText" name="descText"  cols="100" rows="6" style="width:90%;height:300px;visibility:hidden;"></textarea>
		</form>
		<div id="dlg-buttons">
			<a href="#" class="easyui-linkbutton" iconCls="icon-ok"
				onclick="save()">保存</a> <a href="#" class="easyui-linkbutton"
				iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">取消</a>
		</div>
	</div>
	<div style="display:none">
		<form id="hiddenFrom">
			<input id="month1" name="month" >
			<input id="reportRate1" name="reportRate" >
			<input id="quarter1" name="quarter" >
			<input id="reportId1" name="reportId" >
		</form>
	</div>

</body>
<script type="text/javascript">
$(function(){
	$('#year').combobox({
	    disabled:false,
	    prompt:'年度',
	    valueField:'value',
	    textField:'text',
	    url:'${contextPath}/json/year.json'
	});
	serach();
});
function isYON(value){
	if(value=='0'){
		return '否';
	}else{
		return '是';
		} 
}
function resultCategory(value,row){
	return row.value;
}
var editIndex = undefined;
function endEditing(){
	if (editIndex == undefined){return true}
	if ($('#dg').datagrid('validateRow', editIndex)){
		var ed = $('#dg').datagrid('getEditor', {index:editIndex,field:'department'});
		var name = $(ed.target).combobox('getText');
		$('#dg').datagrid('getRows')[editIndex]['value'] = name;
		$('#dg').datagrid('endEdit', editIndex);
		editIndex = undefined;
		return true;
	} else {
		return false;
	}
}
function onClickRow(index){
	if (editIndex != index){
		if (endEditing()){
			$('#dg').datagrid('selectRow', index)
					.datagrid('beginEdit', index);
			editIndex = index;
		} else {
			$('#dg').datagrid('selectRow', editIndex);
		}
	}
}
//搜索
function serach(){
	$('#look').panel('close');
	var params = {};
	$('#serachFrom').find('input').each(function(){
        var obj = $(this);
        var name = obj.attr('name');
        if(name){
            params[name] = obj.val();
        }
    });
	//$('#dg').datagrid("load", params);
	$("#dg").datagrid({
		url:'${pageContext.request.contextPath}/calCfReportInfo/listpage.do?reportCateGory=0&reportType=2',
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
	// 重置
	function reset() {
		$('#serachFrom').form("clear");
		$('#look').panel('close');
	}
	
	function showLook(){
		var userBumenCode='${currentUser.deptCode}';	
		$('#reportId1').val();
		$('#reportRate1').val();
		$('#month1').val();
		$('#quarter1').val();
		var rows = $('#dg').datagrid('getSelections');
		if(rows.length==0||rows.length>1){$.messager.alert('消息提示','请选择一条报送号进行查看!','error');return ;}
		var department = userBumenCode;
		var reportId='';
		reportId=rows[0].reportId;
		/*
		if(rows[0].department==undefined){
			department='';
		}else{
			department=rows[0].department;
		}
		*/
		//$('#dg1').datagrid("load",params);
		
		$('#dg1').datagrid({
				url:'${pageContext.request.contextPath}/reportItem/listalltextblock.do',
				queryParams: {
					department: department,
					reportId: reportId
				},
				rownumbers:true,
				fitColumns:true,
				singleSelect:true,
				pagination:true,
				striped:true,
				title:'固定因子文本块管理',
				nowrap:false,
				autoRowHeight:true,
				loadMsg:"加载数据中...",
				toolbar:[{
					iconCls:'icon-edit',
					plain:true,
					handler:'newReportTextBlock',
					text:'查看编辑'
				},'-',{
					iconCls:'icon-cancel',
					plain:true,
					handler:'destroyReportTextBlock',
					text:'删除文本块数据'
				}],
				columns:[[
				  		{field:'ck',checkbox:true},
				  		{field:'reportItemCode',width:'12%',title:'指标代码',align:'center', halign:'center'},
				  		{field:'reportItemName',width:'20%',title:'指标名称',align:'left', halign:'center'},
				  		//{field:'departmentName',width:'8%',title:'部门',align:'center', halign:'center'},
				  		{field:'reportCode',width:'6%',title:'报表代码',align:'center', halign:'center'},
				  		{field:'reportTypeName',width:'6%',title:'报告名称',align:'center', halign:'center'},
				  		{field:'outItemNote',width:'18%',title:'指标说明',align:'left', halign:'center'},
				  		{field:'outItemTypeName',width:'8%',title:'指标数据类型',align:'center', halign:'center'},
				  		//{field:'state',width:'5%',title:'是否录入',align:'center', halign:'center'},
				  		{field:'quarterReportName',width:'9%',title:'季度快报',align:'center', halign:'center'},
				  		{field:'halfYearReportName',width:'9%',title:'季度报告',align:'center', halign:'center'},
				  		{field:'yearReportName',width:'9%',title:'临时报告',align:'center', halign:'center'}
				      ]]
			});
		$('#look').panel('open');
		$('#reportId1').val(rows[0].reportId);
		$('#reportRate1').val(rows[0].reportType);
		$('#month1').val(rows[0].year);
		$('#quarter1').val(rows[0].quarter);
	}
	//文本块复制
	function wordCopy(){
	var rows = $('#dg').datagrid('getSelections');
	//endEditing();
	if(rows.length==0||rows.length>1){
		$.messager.alert('消息提示','请选择一条报送号进行查看!','error');
		return ;
		}
	var data =[{name:'reportId',value:rows[0].reportId},{name:'mYear',value:rows[0].year},{name:'mQuarter',value:rows[0].quarter},{name:'mCfReportType',value:rows[0].reportType},{name:'mCfReportRate',value:rows[0].reportType},{name:'wordType',value:rows[0].wordType}];
	if (rows){
   		$.messager.confirm('复制文本块','是否复制相同的文本块',function(r){
   			if (r){   				
   				$.messager.alert('消息提示','请稍后...');
   				$.post('${pageContext.request.contextPath}/reportItem/wordCopy.do',data,function(result){
   					if (result.success){
   						$('#dlg').dialog('close');		// close the dialog
   						$('#dg1').datagrid('reload');	// reload the user data
   						jsutil.msg.alert("文本块复制完成");
   					} else {
   						$('#dlg').dialog('close');		// close the dialog
   						$('#dg1').datagrid('reload');	// reload the user data
   						jsutil.msg.alert(result.errorMsg);
   					}
   				},'json');  						
   					} else {
   						$.messager.show({	// show error message
   							title: 'Error',
   							msg: result.errorMsg
   						});
   					}
   				},'json');
   			}	
}

	//  查看编辑
	function newReportTextBlock() {
		var reportRate=$('#reportRate1').val();
		var month=$('#month1').val();
		var quarter=$('#quarter1').val();
		var reportId=$('#reportId1').val();
		var row = $('#dg1').datagrid('getSelected');
		if(!row){$.messager.alert('消息提示','请选择要查看的数据信息!','error');return ;}
		if (row) {
			//var map=jsutil.core.invoke("${pageContext.request.contextPath}/indexCode/getTextBlock.do",{"reportRate":reportRate,"month":month,"quarter":quarter,"outItemCode":row.reportItemCode});
			//$('#descText').html(map.textBlock);
			$('#dlg').dialog('open').dialog('setTitle', '编辑数据'); // OUTITEMCODE, COMCODE, MONTH, QUARTER, REPORTRATE
			$('#fm').form('load', {"reportId":reportId,"reportRate":reportRate,"month":month,"quarter":quarter,"outItemCode":row.reportItemCode,"reportItemName":row.reportItemName});
			var map=jsutil.core.invoke("${pageContext.request.contextPath}/indexCode/getTextBlock.do",{"reportId":reportId,"reportRate":reportRate,"month":month,"quarter":quarter,"outItemCode":row.reportItemCode});
			$('#descText').html(map.textBlock);
			KindEditor.html('#descText', map.textBlock);
			$("#descText").kindeditor({
				cssPath : '${contextPath}/js/kindeditor/plugins/code/prettify.css',
		    	wellFormatMode:true,
		    	designMode:true,
		    	themeType : 'default',
		    	themesPath:'${contextPath}/js/kindeditor/themes/',
		        resizeType : 1,
		        allowPreviewEmoticons : false,
		        allowImageUpload : false,
		        items : [
						'source', '|', 'undo', 'redo', '|', 'preview', 'print', 'template', 'code', 'cut', 'copy', 'paste',
						'plainpaste', 'wordpaste', '|', 'justifyleft', 'justifycenter', 'justifyright',
						'justifyfull', 'insertorderedlist', 'insertunorderedlist', 'indent', 'outdent', 'subscript',
						'superscript', 'clearhtml', 'quickformat', 'selectall', '|', 'fullscreen', '/',
						'formatblock', 'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold',
						'italic', 'underline', 'strikethrough', 'lineheight', 'removeformat', '|', 'image', 'multiimage',
						'flash', 'media', 'insertfile', 'table', 'hr', 'emoticons', 'baidumap', 'pagebreak',
						'anchor', 'link', 'unlink', '|', 'about'],
		        afterChange:function(){
		        	$('#descText').html(map.textBlock);
		            this.sync();//这个是必须的,如果你要覆盖afterChange事件的话,请记得最好把这句加上.
		        },
		        afterCreate:function(){
					this.sync();
				}
			});
			//console.log(jsutil.core.invoke("${pageContext.request.contextPath}/indexCode/getTextBlock.do",{"reportRate":reportRate,"month":month,"quarter":quarter,"outItemCode":row.reportItemCode}));
			//var textBlock=jsutil.core.invoke("${pageContext.request.contextPath}/indexCode/getTextBlock.do",{"reportRate":reportRate,"month":month,"quarter":quarter,"outItemCode":row.reportItemCode});
			//console.log(textBlock);
			url = '${pageContext.request.contextPath}/indexCode/insertTextBlock.do';
		}
	}
	//  删除
	function destroyReportTextBlock() {
		var reportRate=$('#reportRate1').val();
		var month=$('#month1').val();
		var quarter=$('#quarter1').val();
		var reportId=$('#reportId1').val();
		var row = $('#dg1').datagrid('getSelected');
		if(!row){$.messager.alert('消息提示','请选择要删除的数据信息!','error');return ;}
    	if (row){
    		//添加提示信息中的‘季度’
    		$.messager.confirm('删除','确认要删除'+month+'年'+$('#quarter1').val()+'季度'+row.reportItemName+'的文本块数据吗?',function(r){
    			if (r){
    				$.post('${pageContext.request.contextPath}/indexCode/deleteTextBlock.do',{"reportId":reportId,"reportRate":reportRate,"month":month,"quarter":quarter,"outItemCode":row.reportItemCode},function(result){
    					if (result.success){
    						$('#dg1').datagrid('reload');	// reload the user data
    						jsutil.msg.alert("删除成功");
    					} else {
    						$.messager.show({	// show error message
    							title: 'Error',
    							msg: result.errorMsg
    						});
    					}
    				},'json');
    			}
    		});
    	}
	}
	function save() {
		$('#fm').form('submit', {
			url : url,
			onSubmit : function() {
				return $(this).form('validate');
			},
			success : function(result) {
				var result = eval('(' + result + ')');
				if (result.errorMsg) {
					$.messager.show({
						title : 'Error',
						msg : result.errorMsg
					});
				} else {
					$('#dg1').datagrid('reload');
					$('#dlg').dialog('close');
					jsutil.msg.alert("编辑成功");
				}
			}
		});
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
			}
	}
	/* KindEditor.ready(function(K) {
		var editor1 = K.create('textarea[name="descText"]', {
			cssPath : '${contextPath}/js/kindeditor/plugins/code/prettify.css',
			//uploadJson : '../jsp/upload_json.jsp',
			//fileManagerJson : '../jsp/file_manager_json.jsp',
			allowFileManager : true,
			afterChange :function(){
				var self = this;
				self.sync();
			}
			afterCreate : function() {
				var self = this;
				K.ctrl(document, 13, function() {
					self.sync();
					console.log($('#descText').val());
					//$('#descText').val($('#dexcText').val());
					//document.forms['example'].submit();
				});
				K.ctrl(self.edit.doc, 13, function() {
					self.sync();
					console.log($('#descText').val());
					$('#descText').val($('#dexcText').val());
					//document.forms['example'].submit();
				});
			} 
		});
		prettyPrint();
	}); */

</script>
</html>