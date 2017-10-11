 <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>
<%@include file="/commons/statics.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>财务数据管理</title>
</head>
<%	String path = request.getContextPath();
	String basepath=request.getContextPath()+ request.getServletPath().substring(0,request.getServletPath().lastIndexOf("/") + 1); %>
<body>
	<!-- 操作部分 -->
	<div class="easyui-panel" style="padding: 10px 0px 10px 0px;">
		<form  id="serachFrom">
		
		<table cellpadding="5"  width="100%"  >		
	    		<tr>
	    		<td style="width: 80px; text-align: right; padding-right: 40px;">
						<label>表&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名：</label>
					</td>
					<td style="width: 300px;">
						<select id="tablename" name="tablename" class="easyui-combobox" data-options="prompt: '表名',editable:false" style="width: 200px;">
							<option value="0">科目余额表</option>
							<option value="1">资产负债表</option>
							<option value="2">存款及应收利息明细</option>
							<option value="3">最新险种统计表</option>
							<option value="4">保护质押贷款表</option>
							<option value="5">分保账款表</option>
						</select>
					</td>
	    			<td style="width:80px;text-align: right;padding-right: 40px;" >
	    				<label>会计月度：</label>
	    			</td>
	    			<td>
	    				<input  class="easyui-textbox" name="yearmonth"    data-options="prompt: '会计月度'"  style="width: 200px;">
	    			</td>
	    	   </tr>
	    	   <tr>
	    			<td style="width:80px;text-align: right;padding-right: 40px;" >
	    				<label>起始日期：</label>
	    			</td>
	    			<td>
                        <input class="easyui-datebox" name="startdate" style="width: 200px;">	    			</td>
	    			<td style="width:80px;text-align: right;padding-right: 40px;" >
	    				<label>截止日期：</label>
	    			</td>
	    			<td>
                        <input class="easyui-datebox" name="enddate" style="width: 200px;">	
           			</td>
	    			<td style="width:280px; padding-right: 40px;" align="right" colspan="4">
						<a href="javascript:void(0)" class="easyui-linkbutton" onclick="serach()"  data-options="iconCls:'icon-search'" style="width:80px;">查询</a>
						&nbsp;&nbsp;&nbsp;&nbsp;
	    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="reset()"   data-options="iconCls:'icon-reload'" style="width:80px;">重置</a>
					</td>
	    	   </tr>
	    </table>
		
	</form>	
</div>  <!-- 展示部分 -->
<div class="easyui-tabs" id="accdata" style="width:100%">     
        <div title="科目余额表">
	    <table id="dg0"  class="easyui-datagrid" style="width:100%;"
	        url=${pageContext.request.contextPath}/finance/list.do?userCode=${currentUser.userCode}
			toolbar="#toolbar0" 
			rownumbers="true" 
			singleSelect="true" 
			pagination="true" 
			striped="true"
			nowrap="true"
			autoRowHeight="false"
			multiSort="true"
			remoteSort="true"
			pageList="[10,20,50,200,500,1000]"
			style="width:100%;"
			loadMsg="加载数据中..." >
    	<thead>
    		<tr>  
                <th field="ck" checkbox=true></th>
    		    <th field="itemcode"    width="100"  data-options="sortable:true">科目代码</th>
    		    <th field="itemname"    width="100"  data-options="sortable:true">科目名称</th>
    		    <th field="flag"    width="100" data-options="sortable:true">是否末级科目</th>
    			<th field="balance_qc"    width="150"  data-options="sortable:true" align="right" formatter="jsutil.tool.formatCurrency">期初余额</th>
    			<th field="DEBIT_BQ"    width="150"  data-options="sortable:true" align="right" formatter="jsutil.tool.formatCurrency">借方发生额</th>
    			<th field="CREDIT_BQ"    width="150"  data-options="sortable:true" align="right" formatter="jsutil.tool.formatCurrency">贷方发生额</th>
    			<th field="BALANCE_QM"    width="150"  data-options="sortable:true" align="right" formatter="jsutil.tool.formatCurrency">期末余额</th>
    			<th field="YEARMONTH"  width="80"  data-options="sortable:true" >会计期间</th>
    			<th field="DATETIME"  width="80"  data-options="sortable:true" >日期</th>
    			<th field="year"    width="100"  data-options="sortable:true" hidden></th>
    			<th field="quarter"    width="100"  data-options="sortable:true" hidden></th>
    	</tr>
    	</thead>
    </table>
    <div id="toolbar0">
    	<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="edit()">修改</a>
    </div>
    <div id="dlg0" class="easyui-dialog" style="width: 600px; padding: 10px 20px" align="center"  data-options="modal:true,closed:true,resizable:true,collapsible:true,minimizable:true,maximizable:true"  buttons="#dlg-buttons0">
    	<form id="fm0" method="post">
    		<table cellpadding="5" >
	    		<tr>
	    			<td><label>科目代码:</label></td>
	    			<td>
						 <input class="easyui-textbox" type="text"  id="itemcode" name="itemcode" class="easyui-validatebox"data-options="prompt:'科目代码'"  readonly="readonly">
					</td>
					
					<td><label>科目名称:</label></td>
	    			<td>
						<input class="easyui-textbox" type="text"  id="itemname" name="itemname" class="easyui-validatebox textbox" data-options="prompt:'科目名称'" readonly="readonly">
					</td>
	    		</tr>
	    		<tr>
					<td><label>期初余额:</label></td>
	    			<td>
						<input  type="text"  id="balance_qc" name="balance_qc" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2"   readonly="readonly">
					</td>
					
					<td><label>期初余额<font  color="red">(修改为)</font>:</label></td>
	    			<td>
						<input  type="text"  id="balance_qc_after" name="balance_qc_after" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2" >
					</td>
	    		</tr>
	    		<tr>
	    		    <td>
	    		        <label>借方发生额:</label>
	    		    </td>
	    		    <td>
	    		        <input   type="text"  id="DEBIT_BQ"  name="DEBIT_BQ" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2"  readonly="readonly">
	    		    </td>
	    		    
	    		    <td>
	    		        <label>借方发生额<font  color="red">(修改为)</font>:</label>
	    		    </td>
	    		    <td>
	    		        <input    type="text"  id="DEBIT_BQ_after"  name="DEBIT_BQ_after" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2">
	    		    </td>
	    		</tr>
	    		<tr>
	    		    <td>
	    		        <label>贷方发生额:</label>
	    		    </td>
	    		    <td>
	    		        <input   type="text"  id="CREDIT_BQ"  name="CREDIT_BQ" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2"  readonly="readonly">
	    		    </td>
	    		    
	    		    <td>
	    		        <label>贷方发生额<font  color="red">(修改为)</font>:</label>
	    		    </td>
	    		    <td>
	    		        <input    type="text"  id="CREDIT_BQ_after"  name="CREDIT_BQ_after" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2">
	    		    </td>
	    		</tr>
	    		<tr>
	    		    <td>
	    		        <label>期末余额:</label>
	    		    </td>
	    		    <td>
	    		        <input   type="text"  id="BALANCE_QM"  name="BALANCE_QM" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2"  readonly="readonly">
	    		    </td>
	    		    
	    		    <td>
	    		        <label>期末余额<font  color="red">(修改为)</font>:</label>
	    		    </td>
	    		    <td>
	    		        <input    type="text"  id="BALANCE_QM_after"  name="BALANCE_QM_after" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2">
	    		    </td>
	    		</tr>
	    	</table>
    		<input  type="hidden"  name="token"  value="${token}"/>
    		<input  type="hidden"  name="userCode"  value="${currentUser.userCode}"/>
    	</form>
    <div id="dlg-buttons0">
    	<a href="#" class="easyui-linkbutton" iconCls="icon-ok"     onclick="save()">保存</a>
    	<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg0').dialog('close');$('#fm0').form('clear');">取消</a>
    </div>
  </div>
    </div>
    <div title="资产负债表">
	    <table id="dg1"  class="easyui-datagrid" style="width:100%;"
	        url=${pageContext.request.contextPath}/finance/list1.do?userCode=${currentUser.userCode}
			toolbar="#toolbar1" 
			rownumbers="true" 
			singleSelect="true" 
			pagination="true" 
			striped="true"
			nowrap="true"
			autoRowHeight="false"
			multiSort="true"
			remoteSort="true"
			pageList="[10,20,50,200,500,1000]"
			style="width:100%;"
			loadMsg="加载数据中..."  >
    	<thead>
    		<tr>  
					<th field="ck" checkbox=true></th>
					<th field="tablecode" width="100" data-options="sortable:true">表代码</th>
					<th field="tablename" width="100">表名称</th>
					<th field="itemcode" width="100">指标编码</th>
					<th field="itemname" width="100">指标名称</th>
					<th field="bookvalueqc" width="120">账面价值（期初）</th>
					<th field="amountbq" width="100">本期发生额</th>
					<th field="amountqm" width="100">累计发生额</th>
					<th field="bookvalueqm" width="120">账面价值（期末）</th>
					<th field="yearmonth" width="100">会计期间</th>
					<th field="datetime" width="100">日期</th>
					<th field="year"    width="100"  data-options="sortable:true" hidden></th>
    				<th field="quarter"    width="100"  data-options="sortable:true" hidden></th>
	    	</tr>
    	</thead>
    </table>
    <div id="toolbar1">
    	<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="edit1()">修改</a>
    </div>
    <div id="dlg1" class="easyui-dialog" style="width: 600px; padding: 10px 20px" align="center"  data-options="modal:true,closed:true,resizable:true,collapsible:true,minimizable:true,maximizable:true"  buttons="#dlg-buttons1">
    	<form id="fm1" method="post">
    		<table cellpadding="5" >
	    		<tr>
	    			<td><label>表代码:</label></td>
	    			<td>
						 <input class="easyui-textbox" type="text"  id="tablecode" name="tablecode" class="easyui-validatebox textbox" data-options="prompt:'表代码'"  readonly="readonly">
					</td>
					
					<td><label>表名称:</label></td>
	    			<td>
						<input class="easyui-textbox" type="text"  id="tablename" name="tablename" class="easyui-validatebox textbox" data-options="prompt:'表名称'" readonly="readonly">
					</td>
	    		</tr>
	    		<tr>
					<td><label>指标代码:</label></td>
	    			<td>
						<input  class="easyui-textbox" type="text" id="itemcode" name="itemcode" class="easyui-validatebox textbox" data-options="prompt:'指标代码'"   readonly="readonly">
					</td>
					
					<td><label>指标名称:</label></td>
	    			<td>
						<input  class="easyui-textbox" type="text" id="itemname" name="itemname" class="easyui-validatebox textbox" data-options="prompt:'指标名称'" readonly="readonly">
					</td>
	    		</tr>
	    		<tr>
	    		    <td>
	    		        <label>账面价值(期初)</label>
	    		    </td>
	    		    <td>
	    		        <input   type="text"  id="bookvalueqc"  name="bookvalueqc" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2"  readonly="readonly">
	    		    </td>
	    		    
	    		    <td>
	    		        <label>账面价值(期初)<font  color="red">(修改为)</font>:</label>
	    		    </td>
	    		    <td>
	    		        <input    type="text"  id="bookvalueqc_after"  name="bookvalueqc_after" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2">
	    		    </td>
	    		</tr>
	    		<tr>
	    		    <td>
	    		        <label>本期发生额</label>
	    		    </td>
	    		    <td>
	    		        <input   type="text"  id="amountbq"  name="amountbq" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2"  readonly="readonly">
	    		    </td>
	    		    
	    		    <td>
	    		        <label>本期发生额<font  color="red">(修改为)</font>:</label>
	    		    </td>
	    		    <td>
	    		        <input    type="text"  id="amountbq_after"  name="amountbq_after" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2">
	    		    </td>
	    		</tr>
	    		<tr>
	    		    <td>
	    		        <label>累计发生额</label>
	    		    </td>
	    		    <td>
	    		        <input   type="text"  id="amountqm"  name="amountqm" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2"  readonly="readonly">
	    		    </td>
	    		    
	    		    <td>
	    		        <label>累计发生额<font  color="red">(修改为)</font>:</label>
	    		    </td>
	    		    <td>
	    		        <input    type="text"  id="amountqm_after"  name="amountqm_after" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2">
	    		    </td>
	    		</tr>
	    		<tr>
	    		    <td>
	    		        <label>账面价值(期末)</label>
	    		    </td>
	    		    <td>
	    		        <input   type="text"  id="bookvalueqm"  name="bookvalueqm" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2"  readonly="readonly">
	    		    </td>
	    		    
	    		    <td>
	    		        <label>账面价值(期末)<font  color="red">(修改为)</font>:</label>
	    		    </td>
	    		    <td>
	    		        <input    type="text"  id="bookvalueqm_after"  name="bookvalueqm_after" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2">
	    		    </td>
	    		</tr>
	    		
	    	</table>
    		<input  type="hidden"  name="token"  value="${token}"/>
    		<input  type="hidden"  name="userCode"  value="${currentUser.userCode}"/>
    	</form>
    <div id="dlg-buttons1">
    	<a href="#" class="easyui-linkbutton" iconCls="icon-ok"     onclick="save1()">保存</a>
    	<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg1').dialog('close');$('#fm1').form('clear');">取消</a>
    </div>
  </div>
    </div>
    <div title="存款及应收利息明细">
	    <table id="dg2"  class="easyui-datagrid" style="width:100%;"
	        url=${pageContext.request.contextPath}/finance/list2.do?userCode=${currentUser.userCode}
			toolbar="#toolbar2" 
			rownumbers="true" 
			singleSelect="true" 
			pagination="true" 
			striped="true"
			nowrap="true"
			autoRowHeight="false"
			multiSort="true"
			remoteSort="true"
			pageList="[10,20,50,200,500,1000]"
			style="width:100%;"
			loadMsg="加载数据中..." >
    	<thead>
    		<tr>  
    	     	<th field="ck" checkbox=true></th>
    		    <th field="tablecode"          width="100"  data-options="sortable:true">表代码</th>
    		    <th field="tablename"    width="100"  data-options="sortable:true">表名称</th>
    		    <th field="accountcode"    width="100" data-options="sortable:true">账户编码</th>
    			<th field="accountname"    width="100"  data-options="sortable:true">账户名称</th>
    			<th field="balanceqc"    width="120"  data-options="sortable:true" align="right" formatter="jsutil.tool.formatCurrency">期初余额</th>
    			<th field="debit_bq"    width="120"  data-options="sortable:true" align="right" formatter="jsutil.tool.formatCurrency">借方金额</th>
    			<th field="credit_bq"  width="120"  data-options="sortable:true" align="right" formatter="jsutil.tool.formatCurrency">贷方金额</th>
    			<th field="balanceqm"  width="120"  data-options="sortable:true" align="right" formatter="jsutil.tool.formatCurrency">期末余额</th>
    			<th field="interestvalue"  width="120"  data-options="sortable:true" align="right" formatter="jsutil.tool.formatCurrency">应收利息</th>
    		    <th field="yearmonth"    width="100" data-options="sortable:true">会计期间</th>
    			<th field="datetime"    width="100"  data-options="sortable:true">日期</th>
    			<th field="year"    width="100"  data-options="sortable:true" hidden></th>
    			<th field="quarter"    width="100"  data-options="sortable:true" hidden></th>
        	</tr>
    	</thead>
    </table>
    <div id="toolbar2">
    	<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="edit2()">修改</a>
    </div>
    <div id="dlg2" class="easyui-dialog" style="width: 600px; padding: 10px 20px" align="center"  data-options="modal:true,closed:true,resizable:true,collapsible:true,minimizable:true,maximizable:true"  buttons="#dlg-buttons2">
    	<form id="fm2" method="post">
    		<table cellpadding="5" >
	    		<tr>
	    			<td><label>表代码:</label></td>
	    			<td>
						 <input class="easyui-textbox" type="text"  id="tablecode" name="tablecode" class="easyui-validatebox textbox" data-options="prompt:'表代码'"  readonly="readonly">
					</td>
					
					<td><label>表名称:</label></td>
	    			<td>
						<input class="easyui-textbox" type="text"  id="tablename" name="tablename" class="easyui-validatebox textbox" data-options="prompt:'表名称'" readonly="readonly">
					</td>
	    		</tr>
	    		<tr>
					<td><label>账户编码:</label></td>
	    			<td>
						<input  class="easyui-textbox" type="text" id="accountcode" name="accountcode" class="easyui-validatebox textbox" data-options="prompt:'账户编码'"   readonly="readonly">
					</td>
					
					<td><label>账户名称:</label></td>
	    			<td>
						<input  class="easyui-textbox" type="text" id="accountname" name="accountname" class="easyui-validatebox textbox" data-options="prompt:'账户名称'" readonly="readonly">
					</td>
	    		</tr>
	    		<tr>
	    		    <td>
	    		        <label>期初余额</label>
	    		    </td>
	    		    <td>
	    		        <input   type="easyui-textbox"  id="balanceqc"  name="balanceqc" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2"  readonly="readonly">
	    		    </td>
	    		    
	    		    <td>
	    		        <label>期初余额<font  color="red">(修改为)</font>:</label>
	    		    </td>
	    		    <td>
	    		        <input    type="easyui-textbox"  id="balanceqc_after"  name="balanceqc_after" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2">
	    		    </td>
	    		</tr>
	    		<tr>
	    		    <td>
	    		        <label>期末余额</label>
	    		    </td>
	    		    <td>
	    		        <input   type="easyui-textbox"  id="balanceqm"  name="balanceqm" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2"  readonly="readonly">
	    		    </td>
	    		    
	    		    <td>
	    		        <label>期末余额<font  color="red">(修改为)</font>:</label>
	    		    </td>
	    		    <td>
	    		        <input    type="easyui-textbox"  id="balanceqm_after"  name="balanceqm_after" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2">
	    		    </td>
	    		</tr>
	    		<tr>
	    		    <td>
	    		        <label>借方金额</label>
	    		    </td>
	    		    <td>
	    		        <input   type="easyui-textbox"  id="debit_bq"  name="debit_bq" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2"  readonly="readonly">
	    		    </td>
	    		    
	    		    <td>
	    		        <label>借方金额<font  color="red">(修改为)</font>:</label>
	    		    </td>
	    		    <td>
	    		        <input    type="easyui-textbox"  id="debitbq_after"  name="debitbq_after" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2">
	    		    </td>
	    		</tr>
	    		<tr>
	    		    <td>
	    		        <label>贷方金额</label>
	    		    </td>
	    		    <td>
	    		        <input   type="easyui-textbox"  id="credit_bq"  name="credit_bq" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2"  readonly="readonly">
	    		    </td>
	    		    
	    		    <td>
	    		        <label>贷方金额<font  color="red">(修改为)</font>:</label>
	    		    </td>
	    		    <td>
	    		        <input    type="easyui-textbox"  id="creditbq_after"  name="creditbq_after" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2">
	    		    </td>
	    		</tr>
	    		<tr>
	    		    <td>
	    		        <label>应收利息</label>
	    		    </td>
	    		    <td>
	    		        <input   type="easyui-textbox"  id="interestvalue"  name="interestvalue" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2"  readonly="readonly">
	    		    </td>
	    		    
	    		    <td>
	    		        <label>应收利息<font  color="red">(修改为)</font>:</label>
	    		    </td>
	    		    <td>
	    		        <input    type="easyui-textbox"  id="interestvalue_after"  name="interestvalue_after" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2">
	    		    </td>
	    		</tr>
	    		
	    	</table>
    		<input  type="hidden"  name="token"  value="${token}"/>
    		<input  type="hidden"  name="userCode"  value="${currentUser.userCode}"/>
    	</form>
    <div id="dlg-buttons2">
    	<a href="#" class="easyui-linkbutton" iconCls="icon-ok"     onclick="save2()">保存</a>
    	<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg2').dialog('close');$('#fm2').form('clear');">取消</a>
    </div>
  </div>
    </div>
    <div title="最新险种统计表">
	    <table id="dg3"  class="easyui-datagrid" style="width:100%;"
	        url=${pageContext.request.contextPath}/finance/list3.do?userCode=${currentUser.userCode}
			rownumbers="true" 
			singleSelect="true" 
			pagination="true" 
			striped="true"
			nowrap="true"
			autoRowHeight="false"
			multiSort="true"
			remoteSort="true"
			pageList="[10,20,50,200,500,1000]"
			style="width:100%;"
			loadMsg="加载数据中..." >
    	<thead>
    		<tr>  
        		<th field="ck" checkbox=true></th>
    		    <th field="productcode"          width="5%"  data-options="sortable:true">产品编码</th>
    		    <th field="productname"    width="5%"  data-options="sortable:true">产品名称</th>
    			<th field="flag"    width="5%"  data-options="sortable:true">个团标志</th>
    			<th field="firstrisktype"    width="5%"  data-options="sortable:true">产品大类</th>
    			<th field="firstrisktypename"    width="5%"  data-options="sortable:true">产品大类名称</th>
    			<th field="secondrisktype"    width="5%"  data-options="sortable:true">产品中类</th>
    			<th field="secondrisktypename"    width="5%"  data-options="sortable:true">产品中类名称</th>
    			<th field="thirdrisktype"    width="5%"  data-options="sortable:true">产品小类</th>
    			<th field="thirdrisktypename"    width="5%"  data-options="sortable:true">产品小类名称</th>
    			<th field="payperiod"    width="5%"  data-options="sortable:true">缴费期</th>
    			<th field="paytype"    width="5%"  data-options="sortable:true">缴费类型</th> 
       			<th field="channel"    width="5%"  data-options="sortable:true">渠道</th>
    			<th field="longorshortrisk"    width="5%"  data-options="sortable:true">长短险</th> 
    	    	<th field="primaryorraddrisk"    width="5%"  data-options="sortable:true">主/附加险</th>
    			<th field="rate"    width="5%"  data-options="sortable:true">结算利率</th> 
       			<th field="accounttype"    width="5%"  data-options="sortable:true">账户分类</th>
    			<th field="dutyfreeornot"    width="5%"  data-options="sortable:true">可否免税</th>
       			<th field="yearmonth"    width="5%"  data-options="sortable:true">会计期间</th>
    			<th field="datetime"    width="8%"  data-options="sortable:true">日期</th>
    			<th field="year"    width="100"  data-options="sortable:true" hidden></th>
    			<th field="quarter"    width="100"  data-options="sortable:true" hidden></th>
    	   	</tr>
    	</thead>
    </table>
    </div>
    <div title="保护质押贷款表">
	    <table id="dg4"  class="easyui-datagrid" style="width:100%;"
	        url=${pageContext.request.contextPath}/finance/list4.do?userCode=${currentUser.userCode}
			toolbar="#toolbar4" 
			rownumbers="true" 
			singleSelect="true" 
			pagination="true" 
			striped="true"
			nowrap="true"
			autoRowHeight="false"
			multiSort="true"
			remoteSort="true"
			pageList="[10,20,50,200,500,1000]"
			style="width:100%;"
			loadMsg="加载数据中..." >
    	<thead>
    		<tr>  
    	    	<th field="ck" checkbox=true></th>
    		    <th field="productcode"     width="100"  data-options="sortable:true">产品编码</th>
    		    <th field="productname"     width="100"  data-options="sortable:true">产品名称</th>
    			<th field="premiumsreceivable"    width="100"  data-options="sortable:true" align="right" formatter="jsutil.tool.formatCurrency">应收保费</th>
    			<th field="policyloans"    width="100"  data-options="sortable:true" align="right" formatter="jsutil.tool.formatCurrency">保户质押贷款</th>
    			<th field="interestreceivable"    width="100"  data-options="sortable:true" align="right" formatter="jsutil.tool.formatCurrency">应收利息</th>
    			<th field="accounttype"  width="100"  data-options="sortable:true">账户分类</th>
    			<th field="yearmonth"  width="100"  data-options="sortable:true">会计期间</th>
    			<th field="datetime"  width="100"  data-options="sortable:true">日期</th>
    			<th field="year"    width="100"  data-options="sortable:true" hidden></th>
    			<th field="quarter"    width="100"  data-options="sortable:true" hidden></th>
    	</tr>
    	</thead>
    </table>
    <div id="toolbar4">
    	<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="edit4()">修改</a>
    </div>
    <div id="dlg4" class="easyui-dialog" style="width: 600px; padding: 10px 20px" align="center"  data-options="modal:true,closed:true,resizable:true,collapsible:true,minimizable:true,maximizable:true"  buttons="#dlg-buttons4">
    	<form id="fm4" method="post">
    		<table cellpadding="5" >
	    		<tr>
	    			<td><label>产品代码:</label></td>
	    			<td>
						 <input class="easyui-textbox" type="text"  id="productcode" name="productcode" class="easyui-validatebox textbox" data-options="prompt:'表代码'"  readonly="readonly">
					</td>
					
					<td><label>产品名称:</label></td>
	    			<td>
						<input class="easyui-textbox" type="text"  id="productname" name="productname" class="easyui-validatebox textbox" data-options="prompt:'表名称'" readonly="readonly">
					</td>
	    		</tr>
	    		<tr>
	    		    <td>
	    		        <label>应收保费</label>
	    		    </td>
	    		    <td>
	    		        <input   type="easyui-textbox"  id="premiumsreceivable"  name="premiumsreceivable" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2"  readonly="readonly">
	    		    </td>
	    		    
	    		    <td>
	    		        <label>应收保费<font  color="red">(修改为)</font>:</label>
	    		    </td>
	    		    <td>
	    		        <input    type="easyui-textbox"  id="premiumsreceivable_after"  name="premiumsreceivable_after" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2">
	    		    </td>
	    		</tr>
	    		<tr>
	    		    <td>
	    		        <label>保户质押贷款</label>
	    		    </td>
	    		    <td>
	    		        <input   type="easyui-textbox"  id="policyloans"  name="policyloans" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2"  readonly="readonly">
	    		    </td>
	    		    
	    		    <td>
	    		        <label>保户质押贷款<font  color="red">(修改为)</font>:</label>
	    		    </td>
	    		    <td>
	    		        <input    type="easyui-textbox"  id="policyloans_after"  name="policyloans_after" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2">
	    		    </td>
	    		</tr>
	    		<tr>
	    		    <td>
	    		        <label>应收利息</label>
	    		    </td>
	    		    <td>
	    		        <input   type="easyui-textbox"  id="interestreceivable"  name="interestreceivable" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2"  readonly="readonly">
	    		    </td>
	    		    
	    		    <td>
	    		        <label>应收利息<font  color="red">(修改为)</font>:</label>
	    		    </td>
	    		    <td>
	    		        <input    type="easyui-textbox"  id="interestreceivable_after"  name="interestreceivable_after" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2">
	    		    </td>
	    		</tr>
	    		
	    	</table>
    		<input  type="hidden"  name="token"  value="${token}"/>
    		<input  type="hidden"  name="userCode"  value="${currentUser.userCode}"/>
    	</form>
    <div id="dlg-buttons4">
    	<a href="#" class="easyui-linkbutton" iconCls="icon-ok"     onclick="save4()">保存</a>
    	<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg4').dialog('close');$('#fm4').form('clear');">取消</a>
    </div>
  </div>
    </div>
    <div title="分保账款表">
	    <table id="dg5"  class="easyui-datagrid" style="width:100%;"
	        url=${pageContext.request.contextPath}/finance/list5.do?userCode=${currentUser.userCode}
			toolbar="#toolbar5" 
			rownumbers="true" 
			singleSelect="true" 
			pagination="true" 
			striped="true"
			nowrap="true"
			autoRowHeight="false"
			multiSort="true"
			remoteSort="true"
			pageList="[10,20,50,200,500,1000]"
			style="width:100%;"
			loadMsg="加载数据中..." >
    	<thead>
    		<tr>  
    	    	<th field="ck" checkbox=true></th>
    		    <th field="itemcode"          width="90"  data-options="sortable:true">科目代码</th>
    		    <th field="itemname"    width="100"  data-options="sortable:true">科目名称</th>
    		    <th field="remark"    width="150"  data-options="sortable:true">摘要</th>
    		    <th field="sumdebit"    width="120"  data-options="sortable:true" align="right" formatter="jsutil.tool.formatCurrency">借方发生额之和</th>
    			<th field="suncredit"    width="120"  data-options="sortable:true" align="right" formatter="jsutil.tool.formatCurrency">贷方发生额之和</th>
    			<th field="yearmonth"    width="100"  data-options="sortable:true">会计期间</th>
    			<th field="datetime"    width="100"  data-options="sortable:true">日期</th>
    			<th field="year"    width="100"  data-options="sortable:true" hidden></th>
    			<th field="quarter"    width="100"  data-options="sortable:true" hidden></th>
    		</tr>
    	</thead>
    </table>
    <div id="toolbar5">
    	<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="edit5()">修改</a>
    </div>
    <div id="dlg5" class="easyui-dialog" style="width: 600px; padding: 10px 20px" align="center"  data-options="modal:true,closed:true,resizable:true,collapsible:true,minimizable:true,maximizable:true"  buttons="#dlg-buttons5">
    	<form id="fm5" method="post" accept-charset="UTF-8">
    		<table cellpadding="5" >
	    		<tr>
	    			<td><label>科目代码:</label></td>
	    			<td>
						 <input class="easyui-textbox" type="text"  id="itemcode" name="itemcode" class="easyui-validatebox textbox" data-options="prompt:'表代码'"  readonly="readonly">
					</td>
					
					<td><label>科目名称:</label></td>
	    			<td>
						<input class="easyui-textbox" type="text"  id="itemname" name="itemname" class="easyui-validatebox textbox" data-options="prompt:'表名称'" readonly="readonly">
					</td>
	    		</tr>
	    		<tr>
	    			<td><label>摘要:</label></td>
	    			<td>
					 <input class="easyui-textbox" type="text"  id="remark" name="remark" class="easyui-validatebox textbox" data-options="prompt:'摘要'"  readonly="readonly">
					</td>
	    		</tr>
	    		<tr>
	    		    <td>
	    		        <label>借方发生额之和</label>
	    		    </td>
	    		    <td>
	    		        <input   type="easyui-textbox"  id="sumdebit"  name="sumdebit" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2"  readonly="readonly">
	    		    </td>
	    		    
	    		    <td>
	    		        <label>借方发生额之和<font  color="red">(修改为)</font>:</label>
	    		    </td>
	    		    <td>
	    		        <input    type="easyui-textbox"  id="sumdebit_after"  name="sumdebit_after" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2">
	    		    </td>
	    		</tr>
	    		<tr>
	    		    <td>
	    		        <label>贷方发生额之和</label>
	    		    </td>
	    		    <td>
	    		        <input   type="easyui-textbox"  id="suncredit"  name="suncredit" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2"  readonly="readonly">
	    		    </td>
	    		    
	    		    <td>
	    		        <label>贷方发生额之和<font  color="red">(修改为)</font>:</label>
	    		    </td>
	    		    <td>
	    		        <input    type="easyui-textbox"  id="suncredit_after"  name="suncredit_after" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2">
	    		    </td>
	    		</tr>
	    		
	    	</table>
    		<input  type="hidden"  name="token"  value="${token}"/>
    		<input  type="hidden"  name="userCode"  value="${currentUser.userCode}"/>
    	</form>
    <div id="dlg-buttons5">
    	<a href="#" class="easyui-linkbutton" iconCls="icon-ok"     onclick="save5()">保存</a>
    	<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg5').dialog('close');$('#fm5').form('clear');">取消</a>
    </div>
  </div>
    </div>

</div>
</body>
<script  type="text/javascript">

$(function(){
	$('#tablename').combobox('setValue','');
	$('#accdata').tabs({
		onSelect:function(title,index){
			//console.log(index)
			switch (index) {
			case 0:
				$('#dg0').datagrid('reload');
				break;
			case 1:
				$('#dg1').datagrid('reload');
				break;
			case 2:
				$('#dg2').datagrid('reload');
				break;
			case 3:
				$('#dg3').datagrid('reload');
				break;
			case 4:
				$('#dg4').datagrid('reload');
				break;
			case 5:
				$('#dg5').datagrid('reload');
				break;
			default:
				$('#dg0').datagrid('reload');
				break;
			}
		}
	});
});

//重置
function reset() {
	$('#serachFrom').form("clear");
}
//搜索
 function serach(){
	var params = {};
	$('#serachFrom').find('input').each(function(){
        var obj = $(this);
        var name = obj.attr('name');
        if(name){
        	params[name] = obj.val();
        }
    });
	var val = $('#tablename').combobox('getValue');
	switch (val) {
	case "0":
		$('#accdata').tabs('select',0);
		break;
	case "1":
		$('#accdata').tabs('select',1);
		break;
	case "2":
		$('#accdata').tabs('select',2);
		break;
	case "3":
		$('#accdata').tabs('select',3);
		break;
	case "4":
		$('#accdata').tabs('select',4);
		break;
	case "5":
		$('#accdata').tabs('select',5);
		break;
	default:
		$('#accdata').tabs('select',0);
		//$('#tb_asset').datagrid('reload');
		break;
	}
	var index = $('#accdata').tabs('getTabIndex',$('#accdata').tabs('getSelected'));
    if(index==0){
	  $('#dg0').datagrid('load',params);
      $('#dg0').datagrid({
			onLoadSuccess:function(params){
				if(params.total>0) return  false;
				$("#dg0").datagrid('appendRow',{period:'没有相关记录'});
				}
		} );    	
    }else if(index==1){
    	$('#dg1').datagrid('load',params);
        $('#dg1').datagrid({
  			onLoadSuccess:function(params){
  				if(params.total>0) return  false;
  				$("#dg1").datagrid('appendRow',{period:'没有相关记录'});
  				}
  		} ); 
    }else if(index==2){
    	$('#dg2').datagrid('load',params);
        $('#dg2').datagrid({
  			onLoadSuccess:function(params){
  				if(params.total>0) return  false;
  				$("#dg2").datagrid('appendRow',{period:'没有相关记录'});
  				}
  		} ); 
    }else if(index==3){
    	$('#dg3').datagrid('load',params);
        $('#dg3').datagrid({
  			onLoadSuccess:function(params){
  				if(params.total>0) return  false;
  				$("#dg3").datagrid('appendRow',{period:'没有相关记录'});
  				}
  		} ); 
    }else if(index==4){
    	$('#dg4').datagrid('load',params);
        $('#dg4').datagrid({
  			onLoadSuccess:function(params){
  				if(params.total>0) return  false;
  				$("#dg4").datagrid('appendRow',{period:'没有相关记录'});
  				}
  		} ); 
    }else if(index==5){
    	$('#dg5').datagrid('load',params);
        $('#dg5').datagrid({
  			onLoadSuccess:function(params){
  				if(params.total>0) return  false;
  				$("#dg5").datagrid('appendRow',{period:'没有相关记录'});
  				}
  		} ); 
    }
}

/*  function reset(){
	 $('#serachFrom').form('clear')	
 } */
//科目余额表修改
 function edit(){
        var row = $('#dg0').datagrid('getSelected');
        if (row){
        	$('#dlg0').dialog('open').dialog('setTitle','修改信息');
        	$('#fm0').form('load',row);
        	var  array=row.YEARMONTH+'&year='+row.year+'&quarter='+row.quarter+'&datetime='+row.datetime;
        	url = '${pageContext.request.contextPath}/finance/update.do?yearmonth='+array;
        }
 } 
 function save(){
  	$('#fm0').form('submit',{
  		url: url,
  		onSubmit: function(){
  			return $(this).form('validate');
  		},
  		success: function(result){
  		 var result1 = eval('('+result+')');
  			console.log(result1.success);
  			if (result1.success){
				$('#dg0').datagrid('reload');	// reload the user data
				$('#dlg0').dialog('close');		// close the dialog
				$('#fm0').form('clear');
				$.messager.show({
					title: '成功',
					msg:"操作成功"
				});		
			}else if (result1.errorMsg!=null&&result1.errorMsg!=""){
				console.log(result1.errorMsg);
				$.messager.alert({
					title: '错误',
					msg: result1.errorMsg,
					icon:'error'
				});
			} 
  		}
  	});
   }
//资产负债表修改
 function edit1(){
        var row = $('#dg1').datagrid('getSelected');
        if (row){
        	$('#dlg1').dialog('open').dialog('setTitle','修改信息');
        	$('#fm1').form('load',row);
        	var  array=row.yearmonth+'&year='+row.year+'&quarter='+row.quarter+'&datetime='+row.datetime;
        	url = '${pageContext.request.contextPath}/finance/update1.do?yearmonth='+array;
        }
 } 
 function save1(){
  	$('#fm1').form('submit',{
  		url: url,
  		onSubmit: function(){
  			return $(this).form('validate');
  		},
  		success: function(result){
  			console.log(result);
  		 var result1 = eval('('+result+')');
  			if (result1.success){
				$('#dg1').datagrid('reload');	// reload the user data
				$('#dlg1').dialog('close');		// close the dialog
				$('#fm1').form('clear');
				$.messager.show({
					title: '成功',
					msg:"操作成功"
				});		
			}else if (result1.errorMsg!=null&&result1.errorMsg!=""){
				$.messager.alert({
					title: '错误',
					msg: result1.errorMsg,
					icon:'error'
				});
			} 
  		}
  	});
   }
//存款及应收利息明细表修改
 function edit2(){
        var row = $('#dg2').datagrid('getSelected');
        if (row){
        	$('#dlg2').dialog('open').dialog('setTitle','修改信息');
        	$('#fm2').form('load',row);
        	var  array=row.yearmonth+'&year='+row.year+'&quarter='+row.quarter+'&datetime='+row.datetime;
        	url = '${pageContext.request.contextPath}/finance/update2.do?yearmonth='+array;
        }
 } 
 function save2(){
  	$('#fm2').form('submit',{
  		url: url,
  		onSubmit: function(){
  			return $(this).form('validate');
  		},
  		success: function(result){
  			console.log(result);
  		 var result1 = eval('('+result+')');
  			if (result1.success){
				$('#dg2').datagrid('reload');	// reload the user data
				$('#dlg2').dialog('close');		// close the dialog
				$('#fm2').form('clear');
				$.messager.show({
					title: '成功',
					msg:"操作成功"
				});		
			}else if (result1.errorMsg!=null&&result1.errorMsg!=""){
				$.messager.alert({
					title: '错误',
					msg: result1.errorMsg,
					icon:'error'
				});
			} 
  		}
  	});
   }
//保护质押贷款表修改
 function edit4(){
        var row = $('#dg4').datagrid('getSelected');
        if (row){
        	$('#dlg4').dialog('open').dialog('setTitle','修改信息');
        	$('#fm4').form('load',row);
        	var  array=row.yearmonth+'&year='+row.year+'&quarter='+row.quarter+'&datetime='+row.datetime;
        	url = '${pageContext.request.contextPath}/finance/update4.do?yearmonth='+array;
        }
 } 
 function save4(){
  	$('#fm4').form('submit',{
  		url: url,
  		onSubmit: function(){
  			return $(this).form('validate');
  		},
  		success: function(result){
  			console.log(result);
  		 var result1 = eval('('+result+')');
  			if (result1.success){
				$('#dg4').datagrid('reload');	// reload the user data
				$('#dlg4').dialog('close');		// close the dialog
				$('#fm4').form('clear');
				$.messager.show({
					title: '成功',
					msg:"操作成功"
				});		
			}else if (result1.errorMsg!=null&&result1.errorMsg!=""){
				$.messager.alert({
					title: '错误',
					msg: result1.errorMsg,
					icon:'error'
				});
			} 
  		}
  	});
   }
//分保账款表修改
 function edit5(){
        var row = $('#dg5').datagrid('getSelected');
        if (row){
        	$('#dlg5').dialog('open').dialog('setTitle','修改信息');
        	$('#fm5').form('load',row);
        	var  array=row.yearmonth+'&year='+row.year+'&quarter='+row.quarter+'&datetime='+row.datetime;
        	url = '${pageContext.request.contextPath}/finance/update5.do?yearmonth='+array;
        }
 } 
 function save5(){
  	$('#fm5').form('submit',{
  		url: url,
  		onSubmit: function(){
  			return $(this).form('validate');
  		},
  		success: function(result){
  			console.log(result);
  		 var result1 = eval('('+result+')');
  			if (result1.success){
				$('#dg5').datagrid('reload');	// reload the user data
				$('#dlg5').dialog('close');		// close the dialog
				$('#fm5').form('clear');
				$.messager.show({
					title: '成功',
					msg:"操作成功"
				});		
			}else if (result1.errorMsg!=null&&result1.errorMsg!=""){
				$.messager.alert({
					title: '错误',
					msg: result1.errorMsg,
					icon:'error'
				});
			} 
  		}
  	});
   }
 //-----------------------
  jsutil.tool = {
    		//加载HTML
    		loadHtml:function(src) {
    			var html = "";
    			$.ajax({
    				type:"GET",
    				async:false,
    				cache:false,
    				dataType:"text",
    				url:src,
    				success:function(rtn) {
    					html = rtn;
    				}
    			});
    			return html;
    		},
    		//加载JS
    		loadJs:function(src) {
    			$.ajax({
    				type:"GET",
    				async:false,
    				cache:false,
    				dataType:"text",
    				url:src,
    				success:function(rtn) {
    					eval(rtn);
    				}
    			});
    		},
    		//替换变量
    		replaceVar:function(str, obj) {
    	        var key = "";
    	        var res = "";
    	        for (var i = 0; i < str.length; i++) {
    	            if (str.charAt(i) == '$' && i + 1 < str.length && str.charAt(i + 1) == '{') {
    	                i += 2;
    	                key = "";
    	                while (i < str.length) {
    	                    if (str.charAt(i) == '}') {
    	                    	if (obj[key]) {
    	                    		res += obj[key];
    	                    	} else {
    	                    		res += "${" + key + "}"
    	                    	}
    	                        break;
    	                    } else {
    	                        key += str.charAt(i++);
    	                    }
    	                }
    	            } else {
    	                res += str.charAt(i);
    	            }
    	        }
    	        return res;
    		},
    		refreshToken : function(url,$element){
    			$.get(url+'/springmvc.token').done(function(data){
    				$element.val(data);
    			});
    		},
    		/**  
    		 * 将数值四舍五入(保留2位小数)后格式化成金额形式  
    		 *  
    		 * @param num 数值(Number或者String)  
    		 * @return 金额格式的字符串,如'1,234,567.45'  
    		 * @type String  
    		 */    
    		formatCurrency :function(num) {  
    			if(num==""||isNaN(num)||num==null){
    				return ""; 
    			}else{
    				num = num.toString().replace(/\$|\,/g,'');    
    				if(isNaN(num))    
    					 return "无效数字2";    
    				sign = (num == (num = Math.abs(num)));    
    				num = Math.floor(num*100+0.50000000001);    
    				cents = num%100;    
    				num = Math.floor(num/100).toString();    
    				if(cents<10)    
    					cents = "0" + cents;    
    				for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)    
    					num = num.substring(0,num.length-(4*i+3))+','+    
    					num.substring(num.length-(4*i+3));    
    				return (((sign)?'':'-') + num + '.' + cents);    
    			}
    		},
    		/**  
    		 * 将数值四舍五入(保留1位小数)后格式化成金额形式  
    		 *  
    		 * @param num 数值(Number或者String)  
    		 * @return 金额格式的字符串,如'1,234,567.4'  
    		 * @type String  
    		 */   
    		formatCurrencyTenThou:function(num) {  
    			if(num==""||isNaN(num)||num==null){
    				return "无效数字3"; 
    			}else{
    		    num = num.toString().replace(/\$|\,/g,'');    
    		    if(isNaN(num))    
    		    	return "无效数字4";    
    		    //num = "0";    
    		    sign = (num == (num = Math.abs(num)));    
    		    num = Math.floor(num*10+0.50000000001);    
    		    cents = num%10;    
    		    num = Math.floor(num/10).toString();    
    		    for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)    
    		    num = num.substring(0,num.length-(4*i+3))+','+    
    		    num.substring(num.length-(4*i+3));    
    		    return (((sign)?'':'-') + num + '.' + cents);    
    			}    
    		}
    	}
</script>


</html>