<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>
<%@include file="/commons/statics.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>校验关系管理</title>
<style type="text/css">
.datagrid-header-row td {
	height: 40px;
	font-weight: bold;
}
</style>
</head>
<%	String path = request.getContextPath();
	String basepath=request.getContextPath()+ request.getServletPath().substring(0,request.getServletPath().lastIndexOf("/") + 1); 
	%>
<body>

	<!-- 搜索模块 -->
	<div class="easyui-panel" style="height: auto; width:100%; padding: 8px 0px 0px 0px;overflow: hidden;">
		<form id="serachFrom" method="post" style="margin-bottom: 0">
			<table cellpadding="5" border='0' style="width: 100%; overflow: hidden;table-layout: fixed;">		
	    		<tr>
	    			<td style="width:8%;text-align: right;" >
	    				<label>校验关系:</label>
	    			</td>
	    			
	    			<td style="width:16%;">
	    				<input class="easyui-combobox" id="checkSchemaCode" name="checkSchemaCode" style="width:100%;"data-options="url: '<%=path%>/codeSelect.do?type=datacheck',prompt:'校验关系',method: 'get',valueField:'value',editable:false,textField:'text',required:true">
	    			</td>    		
	    		</tr>
	    		<tr>
	    			<td colspan="8" style="text-align: right;padding-right: 20px" >
	    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="serach()"  data-options="iconCls:'icon-search'" style="width:8%;">查询</a>
						<!-- <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-print'"  style="width:8%;" onclick="exportXls('dg')">导出</a> -->
	    				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-print'"  style="width:80px;" onclick="exportByConditionAll()">导出</a>
	    			
	    			</td>
	    		</tr>	    		
	    	</table>
	</form>	
</div>  


<!-- 	操作，展示部分 -->
<div id="relation1" class="easyui-panel" style="border: 0;width: 100%">
	<table id="dg1" class="easyui-datagrid" 
			url=${pageContext.request.contextPath}/dataCheck/relationList.do
			toolbar="#toolbar1" 
			rownumbers="true" 
			fitColumns="true"
			singleSelect="true" 
			pagination="true" 
			striped="true"
			nowrap="false"
			sortName="roleCode"
			autoRowHeight="false"
			multiSort="true"
			remoteSort="true"
			style="table-layout:fixed;word-break:break-all;width:100%;height:auto;"
			loadMsg="加载数据中..." 
			>
			<div id="toolbar1">
			<!--  
				<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addNewCheckRelation()">新建校验关系</a> 
				<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editUser()">编辑校验关系</a> 
				<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="deleteInfo()">删除校验关系</a>
			-->
			</div>
			<thead>
				<tr>
					<th field="ck" checkbox=true></th>
					<th field="id" width="7%" align="center" halign="center">规则流水号</th>
					<th field="calFormula" width="41%" align="left" halign="center">校验计算公式</th>
					<th field="remark" width="24%" align="left" halign="center">算法说明</th>
					<th field="calType" width="8%" align="center" halign="center">公司标示</th>
					<th field="isNeedChk" data-options="formatter:reporttype" width="8%" align="center" halign="center">报告类型</th> 
					<!-- <th field="reportType" >报告类型</th>  -->
					<th field="tolerance" width="8%" align="center" halign="center">容差</th>

				</tr>
			</thead>
		</table>
</div>


	<!--  新增弹框界面-->
	<div id="dlg1" class="easyui-dialog"
		style="width: 460px; height: 370px; padding-right: 30px; padding: 10px 20px"  closed="true" align="left" 
		data-options="modal:true,closed:true,resizable:true,collapsible:true,minimizable:true,maximizable:true" 
		buttons="#dlg-buttons1">
		<form id="fm1" method="post">
			<table cellpadding="5">
	    		<tr>
	    			<td><label>校验公式:</label></td>
	    			<td>
						 <input class="easyui-textbox" type="text"  id="calFormula" name="calFormula" class="easyui-validatebox" style="width:300px;height:70px" data-options="multiline:true,prompt:'公式格式形如: #因子代码#=#因子代码#',required:true,missingMessage:'请添加校验计算公式'"/>
					</td>
	    		</tr>
	    		<tr>
	    			<td><label>算法说明:</label></td>
	    			<td>
						<input class="easyui-textbox" type="text" id="remark" name="remark" class="easyui-validatebox textbox" style="width:300px;height:70px" data-options="multiline:true,prompt:'对校验公式进行文字说明',required:true,missingMessage:'请添加算法说明'"/>
					</td>
	    		</tr>
	    		
	    		<tr>
	    			<td><label>容&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;差:</label></td>
	    			<td>
						<input class="easyui-textbox" type="text" id="tolerance" name="tolerance" class="easyui-validatebox" style="width:50%;" data-options="prompt: '容差'"/>
					</td>
	    		</tr>
	    		
	    		<tr>
	    		<td ><label>公司标示:</label></td>
			  
	    			<td>
						<input class="easyui-textbox" type="text" id="calType" name="calType" class="easyui-validatebox" style="width:50%;" 
						data-options="prompt: '多个用&连接',required:true,missingMessage:'L-寿险公司、P-财险公司、R-再保险公司'"/>
					</td>
	    		</tr>
	    			
	    		<tr>	
					<td ><label>季度快报:</label></td>
					<td >
					<select data-options="panelHeight:'auto' , editable:false"  
					     id="quarterFalshReport1"  name="quarterFalshReport"
						class="easyui-combobox dillcombox show" style="width: 50%">
							<option value="1" >是</option>
							<option value="">否</option>
					</select>
					</td>
				</tr>	
				<tr>	
					<td><label>季度报告:</label></td>
					<td >
					<select data-options="panelHeight:'auto' , editable:false"   id="quarterReport1"
						class="easyui-combobox dillcombox show" name="quarterReport"
						style="width: 50%">
							<option value="2" >是</option>
							<option value="">否</option>
					</select>
					</td>
				</tr>
				
				<tr>
					<td><label>临时报告:</label></td>
					<td >
					<select data-options="panelHeight:'auto' , editable:false"  id="tempReport1"
						class="easyui-combobox dillcombox show" name="tempReport"
						style="width: 50%">
							<option value="3" >是</option>
							<option value="">否</option>
					</select>
					</td>
				</tr>
	    	</table>
	    	<input type="hidden" name="id" value="000"/>
		</form>
		 <div id="dlg-buttons1" style=" padding-right: 30px; ">
	    	<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveRelation()">保存</a>
	    	<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg1').dialog('close')">取消</a>
	    </div> 
    </div>
    <!-- 1结束 -->
    
    <!-- 	操作，展示部分 -->
<div id="relation2" class="easyui-panel" style="border: 0;width: 100%">
	<table id="dg2" class="easyui-datagrid" width="100%"  cellpadding="5"
			url=${pageContext.request.contextPath}/dataCheck/relationList.do
			toolbar="#toolbar2" 
			rownumbers="true" 
			fitColumns="true"
			singleSelect="true" 
			pagination="true" 
			striped="true"
			nowrap="false"
			sortName="roleCode"
			style="table-layout:fixed;word-break:break-all;width:100%;height:auto;"
			autoRowHeight="false"
			loadMsg="加载数据中..." 
			>
			<div id="toolbar2">
			<!--  
				<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addNewCheckRelation()">新建校验关系</a> 
				<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editUser()">编辑校验关系</a> 
				<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="deleteInfo()">删除校验关系</a>
			-->
			</div>
			<thead>
				<tr>
					<th field="ck" checkbox=true></th>
					<th field="id" width="7%" align="center" halign="center">规则流水号</th>
					<th field="tableCode" width="8%" align="center" halign="center">表名</th>
					<th field="calFormula" width="30%" align="center" halign="center">校验计算公式</th>
					<th field="remark" width="33%" align="left" halign="center">算法说明</th>
					<th field="calType" width="7%" align="center" halign="center">公司标示</th>
					<th field="isNeedChk" width="6%" align="center" halign="center" data-options="formatter:reporttype">报告类型</th>
					<th field="tolerance" width="5%" align="center" halign="center">容差</th>
				</tr>
			</thead>
		</table>
</div>
	
	<!--  新增弹框界面-->
	<div id="dlg2" class="easyui-dialog"
		style="width: 460px; height: 405px; padding-right: 30px; padding: 10px 20px"  closed="true" align="left" 
		data-options="modal:true,closed:true,resizable:true,collapsible:true,minimizable:true,maximizable:true" 
		buttons="#dlg-buttons2">
		<form id="fm2" method="post">
			<table cellpadding="5">
				<tr>
	    			<td><label>表&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名:</label></td>
	    			<td><input class="easyui-textbox" type="text" id="tableCode" name="tableCode" class="easyui-validatebox" data-options="prompt: '表名'"></td>
	    		</tr>
	    		
	    		<tr>
	    			<td><label>校验公式:</label></td>
	    			<td>
						 <input class="easyui-textbox" type="text"  id="calFormula" name="calFormula" class="easyui-validatebox" style="width:300px;height:70px" data-options="multiline:true,prompt:'公式格式形如: #因子代码#=#因子代码#',required:true,missingMessage:'请添加校验计算公式'"/>
					</td>
	    		</tr>
	    		
	    		<tr>
	    			<td><label>算法说明:</label></td>
	    			<td>
						<input class="easyui-textbox" type="text" id="remark" name="remark" class="easyui-validatebox textbox" style="width:300px;height:70px" data-options="multiline:true,prompt:'对校验公式进行文字说明',required:true,missingMessage:'请添加算法说明'"/>
					</td>
	    		</tr>
	    		<tr>
	    			<td><label>容&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;差:</label></td>
	    			<td>
						<input class="easyui-textbox" type="text" id="tolerance" name="tolerance" class="easyui-validatebox" style="width:50%;" data-options="prompt: '容差'"/>
					</td>
	    		</tr>
	    		
	    		<tr>
	    		<td ><label>公司标示:</label></td>
			  
	    			<td>
						<input class="easyui-textbox" type="text" id="calType" name="calType" class="easyui-validatebox" style="width:50%;" 
						data-options="prompt: '多个用&连接',required:true,missingMessage:'L-寿险公司、P-财险公司、R-再保险公司'"/>
					</td>
	    		</tr>
	    		<tr>	
					<td ><label>季度快报:</label></td>
					<td > 
					<select data-options="panelHeight:'auto'  , editable:false" id="quarterFalshReport2"
						class="easyui-combobox dillcombox show" name="quarterFalshReport"
						style="width: 50%">
							<option value="1" selected>是</option>
							<option value="">否</option>
					</select>
					</td>
				</tr>	
				<tr>	
					<td><label>季度报告:</label></td>
					<td >
					<select data-options="panelHeight:'auto' , editable:false" id="quarterReport2"
						class="easyui-combobox dillcombox show" name="quarterReport"
						style="width: 50%">
							<option value="2" selected>是</option>
							<option value="">否</option>
					</select>
					</td>
				</tr>
				
				<tr>
					<td><label>临时报告:</label></td>
					<td >
					<select data-options="panelHeight:'auto' , editable:false" id="tempReport2"
						class="easyui-combobox dillcombox show" name="tempReport"
						style="width: 50%">
							<option value="3" selected>是</option>
							<option value="">否</option>
					</select>
					</td>
				</tr>
	    	</table>
	    	<input type="hidden" name="id" value="000"/>
		</form>
		<div id="dlg-buttons2" style=" padding-right: 30px; ">
	    	<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveRelation()">保存</a>
	    	<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg2').dialog('close')">取消</a>
	</div>
    </div>
    <!-- 2结束 -->
    
      <!-- 	操作，展示部分 -->
<div id="relation3" class="easyui-panel" style="border: 0;width: 100%">
		
	<table id="dg3" class="easyui-datagrid" width="100%"  cellpadding="5"
			url=${pageContext.request.contextPath}/dataCheck/relationList.do
			toolbar="#toolbar3" 
			rownumbers="true" 
			fitColumns="true"
			singleSelect="true" 
			pagination="true" 
			striped="true"
			nowrap="false"
			sortName="roleCode"
			autoRowHeight="false"
			style="table-layout:fixed;word-break:break-all;width:100%;height:auto;"
			loadMsg="加载数据中..." 
			>
			<div id="toolbar3">
				<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addNewCheckRelation()">新建校验关系</a> 
				<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editUser()">编辑校验关系</a> 
				<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="deleteInfo()">删除校验关系</a>
			</div>
			<thead>
				<tr>
					<th field="ck" checkbox=true></th>
					<th field="id" width="7%" align="center" halign="center">规则流水号</th>
					<th field="reportItemCode" width="12%" align="center" halign="center">因子指标</th>
					<th field="tableCode" width="8%" align="center" halign="center">表名</th>
					<th field="colCode" width="12%" align="center" halign="center">列名</th>
					<th field="remark" width="35%" align="left" halign="center">算法说明</th>
					<th field="calType" width="8%" align="center" halign="center">公司标示</th>
					<th field="isNeedChk" width="8%" align="center" halign="center" data-options="formatter:reporttype">报告类型</th>
					<th field="tolerance" width="6%" align="center" halign="center">容差</th>
				</tr>
			</thead>
		</table>
</div>
	
	<!--  新增弹框界面-->
	<div id="dlg3" class="easyui-dialog"
		style="width: 460px; height: 388px; padding-right: 30px; padding: 10px 20px"  closed="true" align="left" 
		data-options="modal:true,closed:true,resizable:true,collapsible:true,minimizable:true,maximizable:true" 
		buttons="#dlg-buttons3">
		<form id="fm3" method="post">
			<table cellpadding="5">
			<tr>
	    			<td><label>因子指标:</label></td>
	    			<td><input class="easyui-textbox" type="text" id="reportItemCode" name="reportItemCode" class="easyui-validatebox" data-options="prompt: '因子指标'"></td>
	    		</tr>
				<tr>
	    			<td><label>表&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名:</label></td>
	    			<td><input class="easyui-textbox" type="text" id="tableCode" name="tableCode" class="easyui-validatebox" data-options="prompt: '表名'"></td>
	    		</tr>
	    		<tr>
	    			<td><label>列&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名:</label></td>
	    			<td><input class="easyui-textbox" type="text" id="colCode" name="colCode" class="easyui-validatebox" data-options="prompt: '列名'"></td>
	    		</tr>
	    		
	    		<tr>
	    			<td><label>算法说明:</label></td>
	    			<td>
						<input class="easyui-textbox" type="text" id="remark" name="remark" class="easyui-validatebox textbox" style="width:300px;height:70px" data-options="multiline:true,prompt:'对校验公式进行文字说明',required:true,missingMessage:'请添加算法说明'"/>
					</td>
	    		</tr>

	    		<tr>
	    			<td><label>容&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;差:</label></td>
	    			<td>
						<input class="easyui-textbox" type="text" id="tolerance" name="tolerance" class="easyui-validatebox" style="width:50%;" data-options="prompt: '容差'"/>
					</td>
	    		</tr>
	    		
	    		<tr>
	    		<td ><label>公司标示:</label></td>
			  
	    			<td>
						<input class="easyui-textbox" type="text" id="calType" name="calType" class="easyui-validatebox" style="width:50%;" 
						data-options="prompt: '多个用&连接',required:true,missingMessage:'L-寿险公司、P-财险公司、R-再保险公司'"/>
					</td>
	    		</tr>
	    		
	    		<tr>	
					<td ><label>季度快报:</label></td>
					<td >
					<select data-options="panelHeight:'auto' , editable:false" id="quarterFalshReport3"
						class="easyui-combobox dillcombox show" name="quarterFalshReport"
						style="width: 50%">
							<option value="1">是</option>
							<option value="">否</option>
					</select>
					</td>
				</tr>	
				<tr>	
					<td><label>季度报告:</label></td>
					<td >
					<select data-options="panelHeight:'auto' , editable:false" id="quarterReport3"
						class="easyui-combobox dillcombox show" name="quarterReport"
						style="width: 50%">
							<option value="2">是</option>
							<option value="">否</option>
					</select>
					</td>
				</tr>
				
				<tr>
					<td><label>临时报告:</label></td>
					<td >
					<select data-options="panelHeight:'auto' , editable:false" id="tempReport3"
						class="easyui-combobox dillcombox show" name="tempReport"
						style="width: 50%">
							<option value="3">是</option>
							<option value="">否</option>
					</select>
					</td>
				</tr>
	    	</table>
	    	<input type="hidden" name="id" value="000"/>
		</form>
		 <div id="dlg-buttons3" style=" padding-right: 30px; ">
	    	<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveRelation()">保存</a>
	    	<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg3').dialog('close')">取消</a>
	    </div> 
    </div>
    <!-- 3结束 -->
    
    
          <!-- 	操作，展示部分 -->
<div id="relation4" class="easyui-panel" style="border: 0;width: 100%">
		
	<table id="dg4" class="easyui-datagrid" width="100%"  cellpadding="5"
			url=${pageContext.request.contextPath}/dataCheck/relationList.do
			toolbar="#toolbar4" 
			rownumbers="true" 
			fitColumns="true"
			singleSelect="true" 
			pagination="true" 
			striped="true"
			nowrap="false"
			sortName="roleCode"
			autoRowHeight="false"
			style="table-layout:fixed;word-break:break-all;width:100%;height:auto;"
			loadMsg="加载数据中..." 
			>
			<div id="toolbar4">
				<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="addNewCheckRelation()">新建校验关系</a> 
				<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editUser()">编辑校验关系</a> 
				<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="deleteInfo()">删除校验关系</a>
			</div>
			<thead>
				<tr>
					<th field="ck" checkbox=true></th>
					<th field="id" width="7%" align="center" halign="center">规则流水号</th>
					<th field="itemCodeBq" width="12%" align="center" halign="center">指标代码本期数</th>
					<th field="itemCodeSq" width="12%" align="center" halign="center">指标代码上期数</th>
					<th field="relationOperator" width="8%" align="center" halign="center">相关操作人</th>
					<th field="remark" width="37%" align="left" halign="center">算法说明</th>
					<th field="calType" width="7%" align="center" halign="center">公司标示</th>
					<th field="isNeedChk" width="7%" align="center" halign="center" data-options="formatter:reporttype">报告类型</th>
					<th field="tolerance" width="6%" align="center" halign="center">容差</th>
				</tr>
			</thead>
		</table>
</div>
	
	<!--  新增弹框界面-->
	<div id="dlg4" class="easyui-dialog"
		style="width: 470px; height: 360px; padding-right: 30px; padding: 10px 20px"  closed="true" align="left" 
		data-options="modal:true,closed:true,resizable:true,collapsible:true,minimizable:true,maximizable:true" 
		buttons="#dlg-buttons4">
		<form id="fm4" method="post">
			<table cellpadding="5">
				<tr>
	    			<td><label>指标代码本期数:</label></td>
	    			<td><input class="easyui-textbox" type="text" id="itemCodeBq" name="itemCodeBq" class="easyui-validatebox" data-options="prompt: '指标代码本期数'"></td>
	    		</tr>
	    		<tr>
	    			<td><label>指标代码上期数:</label></td>
	    			<td><input class="easyui-textbox" type="text" id="itemCodeSq" name="itemCodeSq" class="easyui-validatebox" data-options="prompt: '指标代码上期数'"></td>
	    		</tr>
 	    		<tr>
	    			<td><label>相关操作符:</label></td>
	    			<td>
						<input class="easyui-textbox"  type="text" id="relationOperator" name="relationOperator" class="easyui-validatebox " />
					</td>
	    		</tr> 
	    		<tr>
	    			<td><label>算法说明:</label></td>
	    			<td>
						<input class="easyui-textbox" type="text" id="remark" name="remark" class="easyui-validatebox textbox" style="width:300px;height:70px" data-options="multiline:true,prompt:'对校验公式进行文字说明',required:true,missingMessage:'请添加算法说明'"/>
					</td>
	    		</tr>

	    		<tr>
	    			<td><label>容&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;差:</label></td>
	    			<td>
						<input class="easyui-textbox" type="text" id="tolerance" name="tolerance" class="easyui-validatebox" style="width:50%;" data-options="prompt: '容差'"/>
					</td>
	    		</tr>
	    		
	    		<tr>
	    		<td ><label>公司标示:</label></td>
			  
	    			<td>
						<input class="easyui-textbox" type="text" id="calType" name="calType" class="easyui-validatebox" style="width:50%;" 
						data-options="prompt: '多个用&连接',required:true,missingMessage:'L-寿险公司、P-财险公司、R-再保险公司'"/>
					</td>
	    		</tr>
	    		
	    		<tr>	
					<td ><label>季度快报:</label></td>
					<td >
					<select data-options="panelHeight:'auto' , editable:false" id="quarterFalshReport4"
						class="easyui-combobox dillcombox show" name="quarterFalshReport"
						style="width: 50%">
							<option value="1">是</option>
							<option value="">否</option>
					</select>
					</td>
				</tr>	
				<tr>	
					<td><label>季度报告:</label></td>
					<td >
					<select data-options="panelHeight:'auto' , editable:false" id="quarterReport4"
						class="easyui-combobox dillcombox show" name="quarterReport"
						style="width: 50%">
							<option value="2">是</option>
							<option value="">否</option>
					</select>
					</td>
				</tr>
				
				<tr>
					<td><label>临时报告:</label></td>
					<td >
					<select data-options="panelHeight:'auto' , editable:false" id="tempReport4"
						class="easyui-combobox dillcombox show" name="tempReport"
						style="width: 50%">
							<option value="3">是</option>
							<option value="">否</option>
					</select>
					</td>
				</tr>
	    	</table>
	    	<input type="hidden" name="id" value="000"/>
		</form>
		 <div id="dlg-buttons4" style=" padding-right: 30px; ">
	    	<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveRelation()">保存</a>
	    	<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg4').dialog('close')">取消</a>
	    </div> 
    </div>
    
</body>
<script  type="text/javascript">
//报告类型
function reporttype(value,row,index){
	//alert(value);
	if(value == 1){
		return "季度快报";
	}else if(value == 2){
		return "季度报告";
	}else if(value == 3){
		return "临时报告";
	}
	
}

//导入Excel
function exportXls(gridId) {
	var xlsName = $('#checkSchemaCode').combobox('getText');
	var param=jsutil.core.exportXls(gridId+params.checkSchemaCode,xlsName);
	jsutil.core.download("${pageContext.request.contextPath}/downloadExcel",param);
}

$(function(){
	$('#relation1').hide();
	$('#relation2').hide();
	$('#relation3').hide();
	$('#relation4').hide();
});

// 新增
function addNewCheckRelation(){	
 	$('#dlg'+params.checkSchemaCode).dialog('open').dialog('setTitle','新增校验关系');
	$('#fm'+params.checkSchemaCode).form('clear');

//	url='${pageContext.request.contextPath}/dataCheck/add.do?checkSchemaCode='+params.checkSchemaCode+'&relationOperator=${currentUser.userCode}';
	url='${pageContext.request.contextPath}/dataCheck/add.do?checkSchemaCode='+params.checkSchemaCode;
 } 
//新增保存
function saveRelation(){
	$('#fm'+params.checkSchemaCode).form('submit',{
		url: url,
		onSubmit: function(){
			var quarterFalshReport = $('#quarterFalshReport').combobox('getValue');
			var quarterReport = $('#quarterReport').combobox('getValue');
			var tempReport = $('#tempReport').combobox('getValue');

		 	if("" ==quarterFalshReport && "" == quarterReport && "" == tempReport){
				jsutil.msg.alert('请确保季度快报、季度报告、临时报告至少有一个被选中！');
				return false;
			} 
			return $(this).form('validate');
		},
		
	success: function(result){
		var result = eval('('+result+')');
		if (result.errorMsg){
			jsutil.msg.alert(result.errorMsg);
		} else {
			$('#dlg'+params.checkSchemaCode).dialog('close');		// close the dialog
			$('#dg'+params.checkSchemaCode).datagrid('reload');	// reload the user data
		}
	}
});
}  


function addNode(){
	var ids=[];
	var checkData = $('#menuTree').tree('getChecked');
	if(checkData.length==0){
		//$.messager.alert('消息提示','请勾选菜单信息!','info');
		jsutil.msg.alert('请勾选菜单信息!');
		return ;
	}
	for(var index in checkData){
		ids.push(checkData[index].id);
		var parent = $('#menuTree').tree('getParent',checkData[index].target);
		if(parent==null) 
			continue;
		else
			ids.push(parent.id);
	}
	return ids;
} 
var params = {};
//查找
function serach(){

		$('#serachFrom').find('input').each(function(){
			
        var obj = $(this);
        var name = obj.attr('name');
        if(name){
            params[name] = obj.val();    
            if(params[name] == 1){
            	$('#relation1').show();
            	$('#relation2').hide();
            	$('#relation3').hide();
            	$('#relation4').hide();
            	
            } else if(params[name] == 2) {
            	$('#relation1').hide();
            	$('#relation2').show();
            	$('#relation3').hide();
            	$('#relation4').hide();
            	
            }else if(params[name] == 3) {
            	$('#relation1').hide();
            	$('#relation2').hide();
            	$('#relation3').show();
            	$('#relation4').hide();
            	
            }else if(params[name] == 4) {
            	$('#relation1').hide();
            	$('#relation2').hide();
            	$('#relation3').hide();
            	$('#relation4').show();
            }
        }
        
    }); 
	
		
	$('#dg'+params.checkSchemaCode).datagrid("load", params);
	
	$('#dg'+params.checkSchemaCode).datagrid({
		//加载成功
		onLoadSuccess: function(data){
			if (data.total == 0){
				$("#dg"+params.checkSchemaCode).datagrid('appendRow',{id:'没有符合条件的查询结果'}); //这里的id指的是查询后显示的字段名：规则流水号
			}
		}	
	}); 
	
}
 //删除
function deleteInfo(){
	var rows = $('#dg'+params.checkSchemaCode).datagrid('getSelections');
	var idArr = new Array();
	for(var i=0;i<rows.length;i++){
		idArr[i] = rows[i].id;
	}
	
	var data =[{name:'ids',value:idArr.join(',') }];
	console.log(rows);	
	if(rows.length==0){
		jsutil.msg.err('请选择要删除的数据信息!');
		//$.messager.alert('消息提示','请选择要删除的数据信息!','error');
		return ;
	}
	jsutil.msg.confirm('确认删除该信息吗?',function(r){
		if (r){
			$.post('${pageContext.request.contextPath}/dataCheck/delete.do?checkSchemaCode='+params.checkSchemaCode ,data,function(result){
				if (result.success){
					$('#dg'+params.checkSchemaCode).datagrid('reload');	// reload the user data
				} else {
					jsutil.msg.err(result.errorMsg);
				}
			},'json');
		}
	});

		/* $.messager.confirm('删除','确认删除该信息吗?',function(r){
			if (r){
				$.post('${pageContext.request.contextPath}/dataCheck/delete.do?checkSchemaCode='+params.checkSchemaCode ,data,function(result){
					if (result.success){
						$('#dg'+params.checkSchemaCode).datagrid('reload');	// reload the user data
					} else {
						jsutil.msg.err(result.errorMsg);
						$.messager.show({	// show error message
							title: 'Error',
							msg: result.errorMsg
						}); 
					}
				},'json');
			}
		}); */
}
//	重置
function reset(){
	$('#serachFrom').form("clear");
} 

 //修改
function editUser(){
    var rows = $('#dg'+params.checkSchemaCode).datagrid('getSelections');
    
    if(rows.length == 0){
		jsutil.msg.err('请选择要编辑的数据信息!');
//    	$.messager.alert('消息提示','请选择要编辑的数据信息!','error');
    	return ;
    }
    
    if(rows.length>1){
		jsutil.msg.err('请选择一条数据进行编辑!');
    	return ;
    }
    
    $('#fm'+params.checkSchemaCode).form('clear');
	var row = $('#dg'+params.checkSchemaCode).datagrid('getSelected');
	
 	if(row.isNeedChk.indexOf(1)!=-1){
 		//将select下拉框里的值为1选上
 		$("#quarterFalshReport"+params.checkSchemaCode).combobox('setValue',1);
	}
	
 	if(row.isNeedChk.indexOf(2)!=-1){
 		$("#quarterReport"+params.checkSchemaCode).combobox('setValue',2);
	}
 	
 	if(row.isNeedChk.indexOf(3)!=-1){
 		$("#tempReport"+params.checkSchemaCode).combobox('setValue',3);
	}

	$('#dlg'+params.checkSchemaCode).dialog('open').dialog('setTitle','编辑校验关系');
	$('#fm'+params.checkSchemaCode).form('load',row);
	
	url = '${pageContext.request.contextPath}/dataCheck/update.do?id='+row.id + '&checkSchemaCode='+params.checkSchemaCode ;
} 

 
function exportByConditionAll(){
	//准备参数
	
	var xlsName =  $('#checkSchemaCode').combobox('getText');
	if(xlsName=="指标数据校验"){
	var result = prepareParam1(xlsName); 
	var grid = $("#dg1");
	if(result=="false") {
		return false;
	}
	//将查询数据导出
	jsutil.core.download("${contextPath}/downloadCheck/download1",result);
	}else if(xlsName=="行可扩展数据表数据校验"){
		var result = prepareParam2(xlsName); 
		var grid = $("#dg2");
		if(result=="false") {
			return false;
		}
		//将查询数据导出
		jsutil.core.download("${contextPath}/downloadCheck/download2",result);
	}else if(xlsName=="因子与行可扩展数据校验"){
		var result = prepareParam3(xlsName); 
		var grid = $("#dg3");
		if(result=="false") {
			return false;
		}
		//将查询数据导出
		jsutil.core.download("${contextPath}/downloadCheck/download3",result);
	}else if(xlsName=="期间数据校验"){
		var result = prepareParam4(xlsName); 
		var grid = $("#dg4");
		if(result=="false") {
			return false;
		}
		//将查询数据导出
		jsutil.core.download("${contextPath}/downloadCheck/download4",result);
	}
}
//准备参数
function prepareParam1(xlsName){
	var param = {name: xlsName, queryConditions: {}, cols: []};
	var grid = $("#dg1");
	//返回加载完毕后的数据
	var datas = grid.datagrid("getRows");
	if (datas.length > 0) {
		//返回列字段
		var cols = grid.datagrid("getColumnFields");
 		 if (cols.length > 0 && cols[0] == "ck") {
			cols.shift();
		}
		if (cols.length > 0 && cols[0] == "id") {
			cols.shift();
		}
		var col;
		for (var i = 0; i < cols.length; i++) {
			//返回指定列属性
			col = grid.datagrid("getColumnOption", cols[i]);
			param.cols[i] = {field: col.field, title: col.title, width: col.width};
		
		}
		$('#serachFrom').find('input').each(function(){
    		var obj = $(this);
    		var id =obj.attr('id');
    		 if(id){
    			if(id=='calFormula'){
        			param.queryConditions[id]=obj.textbox('getText');
    			} else { 
        			param.queryConditions[id]=obj.combobox('getValue');
    			}
    		}
    	});
		//console.log(param);
		return param;
	} else {
		$.messager.alert("提示","请先查询或者没有要导出的数据","warning");
		return "false";
	}
};

function prepareParam2(xlsName){
	var param = {name: xlsName, queryConditions: {}, cols: []};
	var grid = $("#dg2");
	//返回加载完毕后的数据
	var datas = grid.datagrid("getRows");
	if (datas.length > 0) {
		//返回列字段
		var cols = grid.datagrid("getColumnFields");
		if (cols.length > 0 && cols[0] == "ck") {
			cols.shift();
		}
		if (cols.length > 0 && cols[0] == "id") {
			cols.shift();
		}
		var col;
		for (var i = 0; i < cols.length; i++) {
			//返回指定列属性
			col = grid.datagrid("getColumnOption", cols[i]);
			param.cols[i] = {field: col.field, title: col.title, width: col.width};
		}
		$('#serachFrom').find('input').each(function(){
    		var obj = $(this);
    		var id =obj.attr('id');
    		 if(id){
    			if(id=='tableCode'){
        			param.queryConditions[id]=obj.textbox('getText');
    			} else { 
        			param.queryConditions[id]=obj.combobox('getValue');
    			}
    		}
    	});
		//console.log(param);
		return param;
	} else {
		$.messager.alert("提示","请先查询或者没有要导出的数据","warning");
		return "false";
	}
};

function prepareParam3(xlsName){
	var param = {name: xlsName, queryConditions: {}, cols: []};
	var grid = $("#dg3");
	//返回加载完毕后的数据
	var datas = grid.datagrid("getRows");
	if (datas.length > 0) {
		//返回列字段
		var cols = grid.datagrid("getColumnFields");
		if (cols.length > 0 && cols[0] == "ck") {
			cols.shift();
		}
		if (cols.length > 0 && cols[0] == "id") {
			cols.shift();
		}
		var col;
		for (var i = 0; i < cols.length; i++) {
			//返回指定列属性
			col = grid.datagrid("getColumnOption", cols[i]);
			param.cols[i] = {field: col.field, title: col.title, width: col.width};
		}
		$('#serachFrom').find('input').each(function(){
    		var obj = $(this);
    		var id =obj.attr('id');
    		 if(id){
    			if(id=='reportItemCode'){
        			param.queryConditions[id]=obj.textbox('getText');
    			} else { 
        			param.queryConditions[id]=obj.combobox('getValue');
    			}
    		}
    	});
		//console.log(param);
		return param;
	} else {
		$.messager.alert("提示","请先查询或者没有要导出的数据","warning");
		return "false";
	}
};

function prepareParam4(xlsName){
	var param = {name: xlsName, queryConditions: {}, cols: []};
	var grid = $("#dg4");
	//返回加载完毕后的数据
	var datas = grid.datagrid("getRows");
	if (datas.length > 0) {
		//返回列字段
		var cols = grid.datagrid("getColumnFields");
		if (cols.length > 0 && cols[0] == "ck") {
			cols.shift();
		}
		if (cols.length > 0 && cols[0] == "id") {
			cols.shift();
		}
		if (cols.length > 0 && cols[0] == "relationOperator") {
			cols.shift();
		}

		var col;
		for (var i = 0; i < cols.length; i++) {
			//返回指定列属性
			col = grid.datagrid("getColumnOption", cols[i]);
			param.cols[i] = {field: col.field, title: col.title, width: col.width};
		}
		$('#serachFrom').find('input').each(function(){
    		var obj = $(this);
    		var id =obj.attr('id');
    		 if(id){
    			if(id=='itemCodeBq'){
        			param.queryConditions[id]=obj.textbox('getText');
    			} else { 
        			param.queryConditions[id]=obj.combobox('getValue');
    			}
    		}
    	});
		//console.log(param);
		return param;
	} else {
		$.messager.alert("提示","请先查询或者没有要导出的数据","warning");
		return "false";
	}
};
</script>
</html>