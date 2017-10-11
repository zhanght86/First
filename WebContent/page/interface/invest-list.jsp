 <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>
<%@include file="/commons/statics.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>投资数据管理</title>
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
							<option value="0">存款数据</option>
							<option value="1">理财数据</option>
							<option value="2">证劵投资基金估值数据</option>
							<option value="3">债券基础信息</option>
							<option value="4">股票基础信息</option>
							<option value="5">基金基础信息</option>
							<option value="6">沪深300成分及权重</option>
						</select>
					</td>
	    			<td style="width:80px;text-align: right;padding-right: 40px;" >
	    				<label>会计月度：</label>
	    			</td>
	    			<td>
	    				<input  class="easyui-textbox" name="period"    data-options="prompt: '会计月度'"  style="width: 200px;">
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
                        <input class="easyui-datebox" name="enddate" style="width: 200px;">	    			</td>
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
        <div title="存款数据">
	    <table id="dg0"  class="easyui-datagrid" style="width:100%;"
	        url=${pageContext.request.contextPath}/invest/list.do?userCode=${currentUser.userCode}
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
    		    <th field="deposittype"          width="10%"  data-options="sortable:true">存款类型</th>
    		    <th field="yearmonth"    width="10%"   data-options="sortable:true">会计期间</th>
    			<th field="datetime"  width="10%"  data-options="sortable:true" >日期</th>
    		    <th field="booktype"    width="10%"  data-options="sortable:true">账套</th>
    		    <th field="startdate"    width="10%" data-options="sortable:true">业务日期</th>
    		    <th field="enddate"    width="10%" data-options="sortable:true">到期日期</th>
    		    <th field="depositstate"    width="10%" data-options="sortable:true">存款状态</th>
    			<th field="principal"    width="20%"  data-options="sortable:true" align="right" formatter="jsutil.tool.formatCurrency">本金</th>
    			<th field="year"    width="100"  data-options="sortable:true" hidden></th>
    			<th field="quarter"    width="100"  data-options="sortable:true" hidden></th>
    			<th field="interestincodeitem"    width="100"  data-options="sortable:true" hidden></th>
    			<th field="interestitem"    width="100"  data-options="sortable:true" hidden></th>
    			<th field="principalitemcode"    width="100"  data-options="sortable:true" hidden></th>
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
	    			<td><label>账套:</label></td>
	    			<td>
						 <input class="easyui-textbox" type="text"  id="booktype" name="booktype" class="easyui-validatebox"data-options="prompt:'账套'"  readonly="readonly">
					</td>
					
					<td><label>存款类别:</label></td>
	    			<td>
						 <input class="easyui-textbox" type="text"  id="deposittype" name="deposittype" class="easyui-validatebox"data-options="prompt:'账套'"  readonly="readonly">
					</td>
	    		</tr>
	    		<tr>
	    			<td><label>利息科目:</label></td>
	    			<td>
						 <input class="easyui-textbox" type="text"  id="interestitem" name="interestitem" class="easyui-validatebox"data-options="prompt:'利息科目'"  readonly="readonly">
					</td>
					
					<td><label>本金科目:</label></td>
	    			<td>
						<input class="easyui-textbox" type="text"  id="principalitemcode" name="principalitemcode" class="easyui-validatebox textbox" data-options="prompt:'本金科目'" readonly="readonly">
					</td>
	    		</tr>
	    		<tr>
	    			<td><label>利息收入科目:</label></td>
	    			<td>
						<input class="easyui-textbox" type="text"  id="interestincodeitem" name="interestincodeitem" class="easyui-validatebox textbox" data-options="prompt:'利息收入科目科目'" readonly="readonly">
					</td>
	    		</tr>
	    		<tr>
	    			<td><label>业务日期:</label></td>
	    			<td>
						 <input class="easyui-textbox" type="text"  id="startdate" name="startdate" class="easyui-validatebox"data-options="prompt:'业务日期'"  readonly="readonly">
					</td>
					
					<td><label>到期日期:</label></td>
	    			<td>
						<input class="easyui-textbox" type="text"  id="enddate" name="enddate" class="easyui-validatebox textbox" data-options="prompt:'到期日期'" readonly="readonly">
					</td>
	    		</tr>
	    		<tr>
					<td><label>本金:</label></td>
	    			<td>
						<input  type="text"  id="principal" name="principal" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2"   readonly="readonly">
					</td>
					
					<td><label>本金<font  color="red">(修改为)</font>:</label></td>
	    			<td>
						<input  type="text"  id="principal_after" name="principal_after" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2" >
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
    <div title="理财数据">
	    <table id="dg1"  class="easyui-datagrid" style="width:100%;"
	        url=${pageContext.request.contextPath}/invest/list1.do?userCode=${currentUser.userCode}
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
					<th field="financetype"          width="6%"  data-options="sortable:true">理财方式</th>
    		        <th field="yearmonth"    width="6%" data-options="sortable:true">会计期间</th>
    			    <th field="datetime"  width="6%"  data-options="sortable:true" >日期</th>
					<th field="securitiescode" width="6%">证券代码</th>
					<th field="securityname" width="8%">证券名称</th>
					<th field="startdate" width="6%">发行日期</th>
					<th field="enddate" width="6%">到期日期</th>
					<th field="simpleorcompound" width="6%">单利/复利</th>
					<th field="profittype" width="6%">收益类别</th>
					<th field="intereststartdate" width="6%">起息日</th>
					<th field="interestfristpaydate" width="6%">首次付息日</th>
					<th field="interestpayrate" width="5%">付息频率</th>
					<th field="interestcomputeway" width="21%">利息计算方法</th>
					<th field="year"    width="100"  data-options="sortable:true" hidden></th>
    				<th field="quarter"    width="100"  data-options="sortable:true" hidden></th>
    		</tr>
    	</thead>
    </table>
    </div>
    <div title="证券投资基金估值数据">
	    <table id="dg2"  class="easyui-datagrid" style="width:100%;"
	        url=${pageContext.request.contextPath}/invest/list2.do?userCode=${currentUser.userCode}
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
    		    <th field="booktype"          width="10%"  data-options="sortable:true">账套类型</th>
    		    <th field="yearmonth"    width="10%"  data-options="sortable:true">会计期间</th>
    		    <th field="datetime"    width="10%"  data-options="sortable:true">日期</th>
    		    <th field="itemcode"    width="10%" data-options="sortable:true">科目代码</th>
    			<th field="itemname"    width="20%"  data-options="sortable:true">科目名称</th>
    			<th field="amount"    width="10%"  data-options="sortable:true" align="right" formatter="jsutil.tool.formatCurrency">数量</th>
    			<th field="cost"  width="12%"  data-options="sortable:true" align="right" formatter="jsutil.tool.formatCurrency">成本</th>
    			<th field="marketvalue"  width="12%"  data-options="sortable:true" align="right" formatter="jsutil.tool.formatCurrency">市值</th>
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
	    			<td><label>账套类型:</label></td>
	    			<td>
						 <input class="easyui-textbox" type="text"  id="booktype" name="booktype" class="easyui-validatebox"data-options="prompt:'账套类型'"  readonly="readonly">
					</td>
	    		</tr>
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
					<td><label>数量:</label></td>
	    			<td>
						<input  type="text"  id="amount" name="amount" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2"   readonly="readonly">
					</td>
					
					<td><label>数量<font  color="red">(修改为)</font>:</label></td>
	    			<td>
						<input  type="text"  id="amount_after" name="amount_after" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2" >
					</td>
	    		</tr>
	    		<tr>
					<td><label>成本:</label></td>
	    			<td>
						<input  type="text"  id="cost" name="cost" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2"   readonly="readonly">
					</td>
					
					<td><label>成本<font  color="red">(修改为)</font>:</label></td>
	    			<td>
						<input  type="text"  id="cost_after" name="cost_after" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2" >
					</td>
	    		</tr>
	    		<tr>
					<td><label>市值:</label></td>
	    			<td>
						<input  type="text"  id="marketvalue" name="marketvalue" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2"   readonly="readonly">
					</td>
					
					<td><label>市值<font  color="red">(修改为)</font>:</label></td>
	    			<td>
						<input  type="text"  id="marketvalue_after" name="marketvalue_after" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2" >
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
    <div title="债券基础信息">
	    <table id="dg3"  class="easyui-datagrid" style="width:100%;"
	        url=${pageContext.request.contextPath}/invest/list3.do?userCode=${currentUser.userCode}
			toolbar="#toolbar3" 
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
    		    <th field="bondtype"          width="6%"  data-options="sortable:true">债券类型</th>
    		    <th field="yearmonth"    width="6%"  data-options="sortable:true">会计期间</th>
    		    <th field="datetime"    width="6%"  data-options="sortable:true">日期</th>
    		    <th field="securitycode"          width="6%"  data-options="sortable:true">证券代码</th>
    		    <th field="securityname"    width="8%"  data-options="sortable:true">证券简称</th>
    		    <th field="issuercname"    width="8%" data-options="sortable:true">发行人</th>
    			<th field="debtlevel"    width="6%"  data-options="sortable:true">评级</th>
    			<th field="closingpricecorrectduration"    width="6%"  data-options="sortable:true">修正久期</th>
    			<th field="couponrate"    width="6%"  data-options="sortable:true">票面利率</th>
    			<th field="intereststartdate"    width="6%"  data-options="sortable:true">起息日</th>
    			<th field="deadlineforinterest"    width="6%"  data-options="sortable:true">止息日</th>
    			<th field="interestpayrateofyear"    width="6%"  data-options="sortable:true">每年付息次数</th>
    			<th field="ratetype"    width="6%"  data-options="sortable:true">利率类型</th>
    			<th field="baseinterestratename"    width="6%"  data-options="sortable:true">基准利率名称</th>
    			<th field="rightdebt"    width="6%"  data-options="sortable:true">是否含权债</th>
    			<th field="nextexercisedate"    width="6%"  data-options="sortable:true">下一行权</th> 
    	   		<th field="year"    width="100"  data-options="sortable:true" hidden></th>
    			<th field="quarter"    width="100"  data-options="sortable:true" hidden></th>
    	   	</tr>
    	</thead>
    </table>
    <div id="toolbar3">
    	<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="edit3()">修改</a>
    </div>
    <div id="dlg3" class="easyui-dialog" style="width: 600px; padding: 10px 20px" align="center"  data-options="modal:true,closed:true,resizable:true,collapsible:true,minimizable:true,maximizable:true"  buttons="#dlg-buttons3">
    	<form id="fm3" method="post">
    		<table cellpadding="5" >
	    		<tr>
	    			<td><label>债券类型:</label></td>
	    			<td>
						 <input class="easyui-textbox" type="text"  id="bondtype" name="bondtype" class="easyui-validatebox"data-options="prompt:'账套类型'"  readonly="readonly">
					</td>
	    		</tr>
	    		<tr>
	    			<td><label>证券代码:</label></td>
	    			<td>
						 <input class="easyui-textbox" type="text"  id="securitycode" name="securitycode" class="easyui-validatebox"data-options="prompt:'科目代码'"  readonly="readonly">
					</td>
					
					<td><label>证券简称:</label></td>
	    			<td>
						<input class="easyui-textbox" type="text"  id="securityname" name="securityname" class="easyui-validatebox textbox" data-options="prompt:'科目名称'" readonly="readonly">
					</td>
	    		</tr>
	    		<tr>
					<td><label>修正久期:</label></td>
	    			<td>
						<input  type="text"  id="closingpricecorrectduration" name="closingpricecorrectduration" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2"   readonly="readonly">
					</td>
					
					<td><label>修正久期<font  color="red">(修改为)</font>:</label></td>
	    			<td>
						<input  type="text"  id="closingpricecorrectduration_after" name="closingpricecorrectduration_after" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2" >
					</td>
	    		</tr>
	    		<tr>
					<td><label>票面利率:</label></td>
	    			<td>
						<input  type="text"  id="couponrate" name="couponrate" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2"   readonly="readonly">
					</td>
					
					<td><label>票面利率<font  color="red">(修改为)</font>:</label></td>
	    			<td>
						<input  type="text"  id="couponrate_after" name="couponrate_after" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2" >
					</td>
	    		</tr>
	    	</table>
    		<input  type="hidden"  name="token"  value="${token}"/>
    		<input  type="hidden"  name="userCode"  value="${currentUser.userCode}"/>
    	</form>
    <div id="dlg-buttons3">
    	<a href="#" class="easyui-linkbutton" iconCls="icon-ok"     onclick="save3()">保存</a>
    	<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg3').dialog('close');$('#fm3').form('clear');">取消</a>
    </div>
  </div>
    </div>
    <div title="股票基础信息">
	    <table id="dg4"  class="easyui-datagrid" style="width:100%;"
	        url=${pageContext.request.contextPath}/invest/list4.do?userCode=${currentUser.userCode}
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
    		    <th field="yearmonth"    width="10%"  data-options="sortable:true">会计期间</th>
    		    <th field="datetime"    width="10%"  data-options="sortable:true">日期</th>
    		    <th field="securitycode"          width="10%"  data-options="sortable:true">证券代码</th>
    		    <th field="securityname"    width="15%"  data-options="sortable:true">证券简称</th>
    			<th field="marketboard"    width="10%"  data-options="sortable:true">科目名称上市板</th>
    			<th field="totalmarrketvalue"    width="25%"  data-options="sortable:true" align="right" formatter="jsutil.tool.formatCurrency">总市值</th>
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
	    			<td><label>证券代码:</label></td>
	    			<td>
						 <input class="easyui-textbox" type="text"  id="securitycode" name="securitycode" class="easyui-validatebox"data-options="prompt:'科目代码'"  readonly="readonly">
					</td>
					
					<td><label>证券简称:</label></td>
	    			<td>
						<input class="easyui-textbox" type="text"  id="securityname" name="securityname" class="easyui-validatebox textbox" data-options="prompt:'科目名称'" readonly="readonly">
					</td>
	    		</tr>
	    		<tr>
					<td><label>总市值:</label></td>
	    			<td>
						<input  type="text"  id="totalmarrketvalue" name="totalmarrketvalue" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2"   readonly="readonly">
					</td>
					
					<td><label>总市值<font  color="red">(修改为)</font>:</label></td>
	    			<td>
						<input  type="text"  id="totalmarrketvalue_after" name="totalmarrketvalue_after" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2" >
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
    <div title="基金基础信息">
	    <table id="dg5"  class="easyui-datagrid" style="width:100%;"
	        url=${pageContext.request.contextPath}/invest/list5.do?userCode=${currentUser.userCode}
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
    		    <th field="yearmonth"    width="10%"  data-options="sortable:true">会计期间</th>
    		    <th field="datetime"    width="10%"  data-options="sortable:true">日期</th>
    		    <th field="securitycode"          width="10%"  data-options="sortable:true">证券代码</th>
    		    <th field="sucrityname"    width="15%"  data-options="sortable:true">证券简称</th>
    		    <th field="fundpromoter"    width="15%" data-options="sortable:true">基金发起人</th>
    			<th field="investtype"    width="10%"  data-options="sortable:true">投资类型（一级分类）</th>
    			<th field="gradingfund"    width="10%"  data-options="sortable:true">是否分级基金</th>
    			<th field="gradingfundtype"    width="15%"  data-options="sortable:true">分级基金类别</th>
    			<th field="year"    width="100"  data-options="sortable:true" hidden></th>
    			<th field="quarter"    width="100"  data-options="sortable:true" hidden></th>
    		</tr>
    	</thead>
    </table>
    <div id="toolbar5">
    	<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="edit()">修改</a>
    </div>
    </div>
    <div title="沪深300成分及权重">
	    <table id="dg6"  class="easyui-datagrid" style="width:100%;"
	        url=${pageContext.request.contextPath}/invest/list6.do?userCode=${currentUser.userCode}
			toolbar="#toolbar6" 
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
    		    <th field="yearmonth"    width="5%"  data-options="sortable:true">会计期间</th>
    		    <th field="datetime"    width="5%"  data-options="sortable:true">日期</th>
    		    <th field="rank"          width="5%"  data-options="sortable:true">排名</th>
    		    <th field="code"    width="5%"  data-options="sortable:true">代码</th>
    		    <th field="name"    width="10%" data-options="sortable:true">简称</th>
    			<th field="close"    width="5%"  data-options="sortable:true" align="right" formatter="jsutil.tool.formatCurrency">收盘（元）</th>
    			<th field="weight"    width="5%"  data-options="sortable:true" align="right" formatter="jsutil.tool.formatCurrency">权重（%）</th>
    			<th field="upordowm"  width="5%"  data-options="sortable:true" align="right" formatter="jsutil.tool.formatCurrency">涨跌</th>    			
    			<th field="changefloat"  width="5%"  data-options="sortable:true" align="right" formatter="jsutil.tool.formatCurrency">涨跌幅（%）</th>
    			<th field="indexcontributepoint"  width="5%"  data-options="sortable:true" align="right" formatter="jsutil.tool.formatCurrency">指数贡献点</th>
    			<th field="volume"    width="5%"  data-options="sortable:true" align="right" formatter="jsutil.tool.formatCurrency">成交量（万股）</th>
    			<th field="turnvolume"  width="5%"  data-options="sortable:true" align="right" formatter="jsutil.tool.formatCurrency">成交额（万元）</th>
    			<th field="totalshares"    width="5%"  data-options="sortable:true" align="right" formatter="jsutil.tool.formatCurrency">总股本（亿股）</th>
    			<th field="flowshares"    width="5%"  data-options="sortable:true" align="right" formatter="jsutil.tool.formatCurrency">流通股本（亿股）</th>
    			<th field="totalmarketvalue"    width="5%"  data-options="sortable:true" align="right" formatter="jsutil.tool.formatCurrency">总市值（亿元）</th>
    			<th field="folwmarketvalue"    width="5%"  data-options="sortable:true" align="right" formatter="jsutil.tool.formatCurrency">流通市值（亿元）</th>
    			<th field="commissionindustry"    width="10%"  data-options="sortable:true">证监会行业</th>
    			<th field="windindustry"    width="10%"  data-options="sortable:true">Wind行业</th>
    			<th field="year"    width="100"  data-options="sortable:true" hidden></th>
    			<th field="quarter"    width="100"  data-options="sortable:true" hidden></th>
    		</tr>
    	</thead>
    </table>
    <div id="toolbar6">
    	<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="edit6()">修改</a>
    </div>
    <div id="dlg6" class="easyui-dialog" style="width: 600px; padding-top: 200px" align="center"  data-options="modal:true,closed:true,resizable:true,collapsible:true,minimizable:true,maximizable:true"  buttons="#dlg-buttons6">
    	<form id="fm6" method="post">
    		<table cellpadding="5" >
	    		<tr>
	    			<td><label>代码:</label></td>
	    			<td>
						 <input class="easyui-textbox" type="text"  id="code" name="code" class="easyui-validatebox"data-options="prompt:'科目代码'"  readonly="readonly">
					</td>
					
					<td><label>简称:</label></td>
	    			<td>
						<input class="easyui-textbox" type="text"  id="name" name="name" class="easyui-validatebox textbox" data-options="prompt:'科目名称'" readonly="readonly">
					</td>
	    		</tr>
	    		<tr>
					<td><label>收盘(元):</label></td>
	    			<td>
						<input  type="text"  id="close" name="close" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2"   readonly="readonly">
					</td>
					
					<td><label>收盘(元)<font  color="red">(修改为)</font>:</label></td>
	    			<td>
						<input  type="text"  id="close_after" name="close_after" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2" >
					</td>
	    		</tr>
	    		<tr>
					<td><label>权重(%):</label></td>
	    			<td>
						<input  type="text"  id="weight" name="weight" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2"   readonly="readonly">
					</td>
					
					<td><label>权重(%)<font  color="red">(修改为)</font>:</label></td>
	    			<td>
						<input  type="text"  id="weight_after" name="weight_after" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2" >
					</td>
	    		</tr>
	    		<tr>
					<td><label>涨跌:</label></td>
	    			<td>
						<input  type="text"  id="upordowm" name="upordowm" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2"   readonly="readonly">
					</td>
					
					<td><label>涨跌<font  color="red">(修改为)</font>:</label></td>
	    			<td>
						<input  type="text"  id="upordowm_after" name="upordowm_after" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2" >
					</td>
	    		</tr>
	    		<tr>
					<td><label>涨跌幅(%):</label></td>
	    			<td>
						<input  type="text"  id="changefloat" name="changefloat" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2"   readonly="readonly">
					</td>
					
					<td><label>涨跌幅(%)<font  color="red">(修改为)</font>:</label></td>
	    			<td>
						<input  type="text"  id="changefloat_after" name="changefloat_after" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2" >
					</td>
	    		</tr>
	    		<tr>
					<td><label>指数贡献点:</label></td>
	    			<td>
						<input  type="text"  id="indexcontributepoint" name="indexcontributepoint" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2"   readonly="readonly">
					</td>
					
					<td><label>指数贡献点<font  color="red">(修改为)</font>:</label></td>
	    			<td>
						<input  type="text"  id="indexcontributepoint_after" name="indexcontributepoint_after" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2" >
					</td>
	    		</tr>
	    		<tr>
					<td><label>成交量(万股):</label></td>
	    			<td>
						<input  type="text"  id="volume" name="volume" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2"   readonly="readonly">
					</td>
					
					<td><label>成交量(万股)<font  color="red">(修改为)</font>:</label></td>
	    			<td>
						<input  type="text"  id="volume_after" name="volume_after" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2" >
					</td>
	    		</tr>
	    		<tr>
					<td><label>成交额(万元):</label></td>
	    			<td>
						<input  type="text"  id="turnvolume" name="turnvolume" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2"   readonly="readonly">
					</td>
					
					<td><label>成交额(万元)<font  color="red">(修改为)</font>:</label></td>
	    			<td>
						<input  type="text"  id="turnvolume_after" name="turnvolume_after" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2" >
					</td>
	    		</tr>
	    		<tr>
					<td><label>总股本(亿股):</label></td>
	    			<td>
						<input  type="text"  id="totalshares" name="totalshares" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2"   readonly="readonly">
					</td>
					
					<td><label>总股本(亿股)<font  color="red">(修改为)</font>:</label></td>
	    			<td>
						<input  type="text"  id="totalshares_after" name="totalshares_after" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2" >
					</td>
	    		</tr>
	    		<tr>
					<td><label>流通股本(亿股):</label></td>
	    			<td>
						<input  type="text"  id="flowshares" name="flowshares" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2"   readonly="readonly">
					</td>
					
					<td><label>流通股本(亿股)<font  color="red">(修改为)</font>:</label></td>
	    			<td>
						<input  type="text"  id="flowshares_after" name="flowshares_after" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2" >
					</td>
	    		</tr>
	    		<tr>
					<td><label>总市值(亿元):</label></td>
	    			<td>
						<input  type="text"  id="totalmarketvalue" name="totalmarketvalue" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2"   readonly="readonly">
					</td>
					
					<td><label>总市值(亿元)<font  color="red">(修改为)</font>:</label></td>
	    			<td>
						<input  type="text"  id="totalmarketvalue_after" name="totalmarketvalue_after" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2" >
					</td>
	    		</tr>
	    		<tr>
					<td><label>流通市值(亿元):</label></td>
	    			<td>
						<input  type="text"  id="folwmarketvalue" name="folwmarketvalue" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2"   readonly="readonly">
					</td>
					
					<td><label>流通市值(亿元)<font  color="red">(修改为)</font>:</label></td>
	    			<td>
						<input  type="text"  id="folwmarketvalue_after" name="folwmarketvalue_after" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2" >
					</td>
	    		</tr>
	    		
	    	</table>
    		<input  type="hidden"  name="token"  value="${token}"/>
    		<input  type="hidden"  name="userCode"  value="${currentUser.userCode}"/>
    	</form>
    <div id="dlg-buttons6">
    	<a href="#" class="easyui-linkbutton" iconCls="icon-ok"     onclick="save6()">保存</a>
    	<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg6').dialog('close');$('#fm4').form('clear');">取消</a>
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
			case 6:
				$('#dg6').datagrid('reload');
				break;
			default:
				$('#dg0').datagrid('reload');
				break;
			}
		}
	});
});


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
	case "6":
		$('#accdata').tabs('select',6);
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
    }else if(index==6){
    	$('#dg6').datagrid('load',params);
        $('#dg6').datagrid({
  			onLoadSuccess:function(params){
  				if(params.total>0) return  false;
  				$("#dg5").datagrid('appendRow',{period:'没有相关记录'});
  				}
  		} ); 
    }
}

 function reset(){
	 $('#serachFrom').form('clear');
 }
 
//存款数据表修改
 function edit(){
        var row = $('#dg0').datagrid('getSelected');
        if (row){
        	$('#dlg0').dialog('open').dialog('setTitle','修改信息');
        	$('#fm0').form('load',row);
        	var array=row.yearmonth+'&year='+row.year+'&quarter='+row.quarter+'&datadate='+row.datetime;
        	url = '${pageContext.request.contextPath}/invest/update.do?yearmonth='+array;
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
//证券投资基金估值表修改
 function edit2(){
        var row = $('#dg2').datagrid('getSelected');
        if (row){
        	$('#dlg2').dialog('open').dialog('setTitle','修改信息');
        	$('#fm2').form('load',row);
        	var array=row.yearmonth+'&year='+row.year+'&quarter='+row.quarter+'&datadate='+row.datetime;
        	url = '${pageContext.request.contextPath}/invest/update2.do?yearmonth='+array;
        }
 }
 function save2(){
  	$('#fm2').form('submit',{
  		url: url,
  		onSubmit: function(){
  			return $(this).form('validate');
  		},
  		success: function(result){
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
//债券基础信息表修改
 function edit3(){
        var row = $('#dg3').datagrid('getSelected');
        if (row){
        	$('#dlg3').dialog('open').dialog('setTitle','修改信息');
        	$('#fm3').form('load',row);
        	var array=row.yearmonth+'&year='+row.year+'&quarter='+row.quarter+'&datadate='+row.datetime;
        	url = '${pageContext.request.contextPath}/invest/update3.do?yearmonth='+array;
        }
 }
 function save3(){
  	$('#fm3').form('submit',{
  		url: url,
  		onSubmit: function(){
  			return $(this).form('validate');
  		},
  		success: function(result){
  		 var result1 = eval('('+result+')');
  			if (result1.success){
				$('#dg3').datagrid('reload');	// reload the user data
				$('#dlg3').dialog('close');		// close the dialog
				$('#fm3').form('clear');
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
//股票基础信息表修改
 function edit4(){
        var row = $('#dg4').datagrid('getSelected');
        if (row){
        	$('#dlg4').dialog('open').dialog('setTitle','修改信息');
        	$('#fm4').form('load',row);
        	var array=row.yearmonth+'&year='+row.year+'&quarter='+row.quarter+'&datadate='+row.datetime;
        	url = '${pageContext.request.contextPath}/invest/update4.do?yearmonth='+array;
        }
 }
 function save4(){
  	$('#fm4').form('submit',{
  		url: url,
  		onSubmit: function(){
  			return $(this).form('validate');
  		},
  		success: function(result){
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
//沪深300成分及权重表修改
 function edit6(){
        var row = $('#dg6').datagrid('getSelected');
        if (row){
        	$('#dlg6').dialog('open').dialog('setTitle','修改信息');
        	$('#fm6').form('load',row);
        	var array=row.yearmonth+'&year='+row.year+'&quarter='+row.quarter+'&datadate='+row.datetime;
        	url = '${pageContext.request.contextPath}/invest/update6.do?yearmonth='+array;
        }
 }
 function save6(){
  	$('#fm6').form('submit',{
  		url: url,
  		onSubmit: function(){
  			return $(this).form('validate');
  		},
  		success: function(result){
  		 var result1 = eval('('+result+')');
  			if (result1.success){
				$('#dg6').datagrid('reload');	// reload the user data
				$('#dlg6').dialog('close');		// close the dialog
				$('#fm6').form('clear');
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