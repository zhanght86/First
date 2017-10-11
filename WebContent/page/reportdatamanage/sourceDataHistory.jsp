<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>
<%@include file="/commons/statics.jsp"%>
<%
	String path = request.getContextPath();
	String basepath = request.getContextPath()
			+ request.getServletPath().substring(0,
					request.getServletPath().lastIndexOf("/") + 1);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>源数据修改轨迹查询</title>
<script type="text/javascript">
	
	//搜索
	function serach() {
		$('#dgpanel').panel('open');	
	}
	
	//重置
	function reset() {
		$('#serachFrom').form("clear");
	}
</script>
</head>
<body>
	<!-- 操作部分 -->
	<div class="easyui-panel" style="padding: 10px 0px 10px 0px;">
		<form id="serachFrom">
			<table cellpadding="5" width="100%">
				<tr>
					<td style="width: 80px; text-align: right; padding-right: 40px;">
						<label>字&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;段：</label>
					</td>
					<td style="width: 300px;"><input id="field" name="field"
						class="easyui-textbox" data-options="prompt: '字段'"
						style="width: 200px;"></td>
					<td style="width: 80px; text-align: right; padding-right: 40px;">
						<label>科目编码：</label>
					</td>
					<td><input name="outItemCode" class="easyui-textbox"
						data-options="prompt: '科目编码'" style="width: 200px;"></td>
				</tr>
				<tr>
					<td style="width: 80px; text-align: right; padding-right: 40px;">
						<label>起始日期：</label>
					</td>
					<td><input id="startDate" name="startDate"
						class="easyui-datebox"
						data-options="prompt: '加载日期起',editable:false"
						style="width: 200px;"></td>
					<td style="width: 80px; text-align: right; padding-right: 40px;">
						<label>截止日期：</label>
					</td>
					<td><input id="endDate" name="endDate" class="easyui-datebox"
						data-options="prompt: '加载日期起',editable:false"
						style="width: 200px;"></td>

				</tr>
				<tr>
					<td style="width: 80px; text-align: right; padding-right: 40px;">
						<label>数据来源：</label>
					</td>
					<td style="width: 300px;"><input name="report_code"
						class="easyui-textbox" data-options="prompt: '数据来源'"
						style="width: 200px;"></td>
					<td style="width: 80px; text-align: right; padding-right: 40px;">
					</td>
					<td style="width: 280px; padding-right: 40px;" align="right">
						<a href="javascript:void(0)" class="easyui-linkbutton"
						onclick="serach()" data-options="iconCls:'icon-search'"
						style="width: 80px;">查询</a> <a href="javascript:void(0)"
						class="easyui-linkbutton" onclick="reset()"
						data-options="iconCls:'icon-reload'" style="width: 80px;">重置</a>

					</td>
				</tr>
			</table>

		</form>
	</div>
	<!-- 展示部分 -->
	<div id="dgpanel" class="easyui-panel" style="width:100%" data-options="closed:true">
	<table id="dg" class="easyui-datagrid" style="width: 100%;"
		url=
		rownumbers="true" fitColumns="false"
		singleSelect="true" pagination="true" sortName="oper_date"
		striped="true" nowrap="true" loadMsg="加载数据中...">
		<thead>
			<tr>
				<th field="field" width="110" data-options="sortable:true">字段</th>
				<th field="outitemcode" width="110" data-options="sortable:true">科目代码</th>
				<th field="tablename" width="110" data-options="sortable:true">来源表名称</th>
				<th field="report_mvalue" width="110"
					data-options="sortable:true,precision:2" class="easyui-numberbox"
					formatter="jsutil.tool.formatCurrency" align="right">数值型值</th>
				<th field="report_mvalue_after" width="110"
					data-options="sortable:true,precision:2" class="easyui-numberbox"
					formatter="jsutil.tool.formatCurrency" align="right">数值型值（修改后）</th>
				<th field="text_value" width="110" data-options="sortable:true">文本型值</th>
				<th field="text_value_after" width="110"
					data-options="sortable:true">文本型值（修改后）</th>
				<th field="oper_date" width="150" data-options="sortable:true">修改日期</th>
				<th field="user_name" width="110" data-options="sortable:true"
					align="center">修改人</th>
			</tr>
		</thead>
	</table>
	</div>
</body>
</html>