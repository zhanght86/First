<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<title>财务的接口数据管理</title>
<meta http-equiv="Content-type" content="text/html;charset=UTF-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<%@include file="/commons/statics.jsp"%>
<link rel="stylesheet" type="text/css"
	href="<c:url value='/js/easyui/themes/color.css' />">
</head>
<script type="text/javascript">
	/* 初始化下载表格信息 */

	$(function() {
		// 下拉框选择控件，下拉框的内容是动态查询数据库信息
		$('#interfacetype').combobox("setValue", "财务底稿数据");
	})
	/**
	 $('#interfacetype').combobox({ 
	  prompt: '数据类型',
	     required:true,
	     missingMessage:'请添加数据类型',
	  url: '${pageContext.request.contextPath}/codeSelect.do?type=FinanceType',
	  method: 'get',
	  valueField:'value',
	  textField:'text',
	  groupField:'group'
	            });  
	 
	 var interfaceType = $("input[name='interfacetype']").val();
	 if(interfaceType=="财务底稿数据"){
	 $("#divZC").hide();
	 $("#divGD").hide();
	 $("#divKM").show();
	 $("#digaoType").show();
	 }else if(interfaceType=="全帐套估值表数据"){
	 $("#divKM").hide();
	 $("#divGD").hide();
	 $("#divZC").show();
	 $("#digaoType").css("display","none");
	 }else if(interfaceType=="到期日数据"){
	 $("#divZC").hide();
	 $("#divKM").hide();
	 $("#divGD").show();
	 $("#digaoType").css("display","none");
	 }  
	 });
	   
	 */
</script>
<body>
	<!-- 搜索模块 -->
	<div style="border: 1px solid #95B8E7; width: auto; padding: 10px;">
		<form id="serachFrom" method="post">
			<table style="width: 100%;">


				<tr height="30">
					<td style="width: 80px; text-align: right; padding-right: 40px;">
						<label>报&nbsp;送&nbsp;号&nbsp;:</label>
					</td>
					<td style="width: 300px;"><input id="reportId" name="reportId"
						class="easyui-combobox" style="width: 200px;"
						data-options="
						        prompt: '报  送  号',	
						        required:true,
						        missingMessage:'请先选择报送号',		        
							    url: '<%=path%>/codeSelect.do?type=reportid_import',
							    method: 'get',
							    valueField:'value',
							    textField:'text',
							    groupField:'group'
						        ">
					</td>
					<td style="width: 10%;"><label>数据类型:</label></td>
					<td style="width: 30%;"><input id="interfacetype"
						name="interfacetype" class="easyui-combobox" style="width: 60%;"
						data-options="  
					     	required:true,
					     	missingMessage:'请先加数据类型',
							url: '<%=path%>/codeSelect.do?type=FinanceType',
							method: 'get',
							valueField:'value',
							textField:'text',
							groupField:'group'
						     ">
					</td>
				</tr>
				<!-- 财务底稿明细表类型 -->

				<tr id="digaoType">
					<td style="width: 80px; text-align: right; padding-right: 40px;">
						<label>财务底稿明细表类型:</label>
					</td>
					<td style="width: 300px;"><input id="finadigaoType"
						name="finadigaoType" class="easyui-combobox" style="width: 200px;"
						data-options="
						        prompt: '财务底稿明细表类型',	
						        required:true,
						        missingMessage:'请先选择财务底稿明细表类型',		        
							    url: '<%=path%>/codeSelect.do?type=finadigaoType',
							    method: 'get',
							    valueField:'value',
							    textField:'text',
							    groupField:'group',
							   
						        ">
					</td>
				</tr>
				<tr height="30">

					<td style="width: 10%;"><span style="width: 10%;"></span></td>
					<td style="width: 30%;"><span style="width: 10%;"></span></td>
					<td style="width: 10%;"><span style="width: 10%;"></span></td>
					<td style="width: 30%;"><span style="width: 10%;"></span></td>
					<td style="width: 40%;"><a href="javascript:void(0)"
						class="easyui-linkbutton" onclick="serach()"
						data-options="iconCls:'icon-search'" style="width: 80px;">查询</a> <a
						href="javascript:void(0)" class="easyui-linkbutton"
						onclick="reset()" data-options="iconCls:'icon-reload'"
						style="width: 80px;">重置</a></td>
				</tr>
			</table>
		</form>
	</div>

	<!-- 	操作，展示部分 -->
	<div id="divKM">
		<h1>财务底稿数据</h1>
		<table id="dg" class="easyui-datagrid"
			url=${pageContext.request.contextPath}/interTabFinaInfo/listpage.do
			toolbar="#toolbar" rownumbers="true" fitColumns="true"
			singleSelect="false" pagination="true" striped="true" nowrap="true"
			autoRowHeight="false" style="width: 100%; height: auto;"
			loadMsg="加载数据中...">
			<thead>
				<tr>
					<th field="year" width="10" sortable="true">年度</th>
					<th field="quarter" width="6" sortable="true">季度</th>
					<th field="reportDate" width="15" sortable="true">报送日期</th>
					<th field="reportId" width="26" sortable="true">报送号</th>
					<th field="itemCode" width="30" sortable="true">科目编码</th>
					<th field="itemName" width="40" sortable="true">名称</th>
					<th field="qcSum" width="12" sortable="true">期初合计值</th>
					<th field="qmSum" width="12" sortable="true">期末合计值</th>
					<th field="tabName" width="12" sortable="true">明细表名称</th>
				</tr>
			</thead>
		</table>
	</div>
	<div id="divZC">
		<h1>全帐套估值表数据</h1>
		<table id="dgZC" class="easyui-datagrid"
			url=${pageContext.request.contextPath}/interTabFinaEnValue/listpage.do
			toolbar="#toolbarZC" rownumbers="true" fitColumns="true"
			singleSelect="false" pagination="true" striped="true" nowrap="true"
			autoRowHeight="false" style="width: 100%; height: auto;"
			loadMsg="加载数据中...">
			<thead>
				<tr>
					<th field="year" width="10" sortable="true">年度</th>
					<th field="quarter" width="6" sortable="true">季度</th>
					<th field="reportDate" width="20" sortable="true">报送日期</th>
					<th field="reportId" width="35" sortable="true">报送号</th>
					<th field="accountType" width="15" sortable="true">账户</th>
					<th field="itemCode" width="40" sortable="true">科目编码</th>
					<th field="itemName" width="30" sortable="true">科目名称</th>
					<th field="enCounts" width="20" sortable="true">数量</th>
					<th field="enCosts" width="20" sortable="true">成本</th>
					<th field="enValue" width="20" sortable="true">市值</th>
					<th field="temp1" width="20" sortable="true">证券编码</th>


				</tr>
			</thead>
		</table>
	</div>
	<div id="divGD">
		<h1>到期日数据</h1>
		<table id="dgGD" class="easyui-datagrid"
			url=${pageContext.request.contextPath}/interTabFinaDateInfo/listpage.do
			toolbar="#toolbarGD" rownumbers="true" fitColumns="true"
			singleSelect="false" pagination="true" striped="true" nowrap="true"
			autoRowHeight="false" style="width: 100%; height: auto;"
			loadMsg="加载数据中...">
			<thead>
				<tr>
					<th field="year" width="10" sortable="true">年度</th>
					<th field="quarter" width="6" sortable="true">季度</th>
					<th field="reportDate" width="20" sortable="true">报送日期</th>
					<th field="reportId" width="30" sortable="true">报送号</th>
					<th field="accountType" width="15" sortable="true">账户</th>
					<th field="properCode" width="15" sortable="true">债券编码</th>
					<th field="properName" width="25" sortable="true">债券名称</th>
					<th field="startDate" width="25" sortable="true">领息日</th>
					<th field="endDate" width="25" sortable="true">到期日</th>
					<th field="currDate" width="25" sortable="true">当前季度日期</th>

				</tr>
			</thead>
		</table>
	</div>
	<div id="divWNGZ">
		<h1>万能险估值表数据</h1>
		<table id="dgWNGZ" class="easyui-datagrid"
			url=${pageContext.request.contextPath}/interTabFinaEnValue/listpage.do
			toolbar="#toolbarZC" rownumbers="true" fitColumns="true"
			singleSelect="false" pagination="true" striped="true" nowrap="true"
			autoRowHeight="false" style="width: 100%; height: auto;"
			loadMsg="加载数据中...">
			<thead>
				<tr>
					<th field="year" width="10" sortable="true">年度</th>
					<th field="quarter" width="6" sortable="true">季度</th>
					<th field="reportDate" width="20" sortable="true">报送日期</th>
					<th field="reportId" width="35" sortable="true">报送号</th>
					<th field="accountType" width="15" sortable="true">账户</th>
					<th field="itemCode" width="40" sortable="true">科目编码</th>
					<th field="itemName" width="30" sortable="true">科目名称</th>
					<th field="enCounts" width="20" sortable="true">数量</th>
					<th field="enCosts" width="20" sortable="true">成本</th>
					<th field="enValue" width="20" sortable="true">市值</th>
					<th field="temp1" width="20" sortable="true">证券编码</th>


				</tr>
			</thead>
		</table>
	</div>
	<div id="divFHGZ">
		<h1>分红险估值表数据</h1>
		<table id="dgFHGZ" class="easyui-datagrid"
			url=${pageContext.request.contextPath}/interTabFinaEnValue/listpage.do
			toolbar="#toolbarZC" rownumbers="true" fitColumns="true"
			singleSelect="false" pagination="true" striped="true" nowrap="true"
			autoRowHeight="false" style="width: 100%; height: auto;"
			loadMsg="加载数据中...">
			<thead>
				<tr>
					<th field="year" width="10" sortable="true">年度</th>
					<th field="quarter" width="6" sortable="true">季度</th>
					<th field="reportDate" width="20" sortable="true">报送日期</th>
					<th field="reportId" width="35" sortable="true">报送号</th>
					<th field="accountType" width="15" sortable="true">账户</th>
					<th field="itemCode" width="40" sortable="true">科目编码</th>
					<th field="itemName" width="30" sortable="true">科目名称</th>
					<th field="enCounts" width="20" sortable="true">数量</th>
					<th field="enCosts" width="20" sortable="true">成本</th>
					<th field="enValue" width="20" sortable="true">市值</th>
					<th field="temp1" width="20" sortable="true">证券编码</th>


				</tr>
			</thead>
		</table>
	</div>

</body>
<script type="text/javascript">
	//	重置
	function reset() {
		$('#serachFrom').form("clear");
	}
	//校验输入项
	function checkInput() {
		//var Year = $("input[name='year']").val();
		//var quarter = $("input[name='quarter']").val();
		var interfaceType = $("input[name='interfacetype']").val();
		if (interfaceType == "" || interfaceType == null) {
			alert("请先填写数据类型！");
			return false;
		}
		/**else if(Year==""||Year==null){
			alert("请先填写年度！");
			return false;
		} else if(quarter==""||quarter==null){
			alert("请先填写季度！");
			return false;
		} 
		 **/
		return true;
	}

	//查询功能
	function serach() {

		if (checkInput()) {
			displayData();
			return true;
		}
		return false;

	}
	//财务底稿数据类型
/**	function getType(){
		
		var type1=$("#digaoType").combobox('getValue');
        var url = 'url=${pageContext.request.contextPath}/interTabFinaInfo/listpage.do?type='+type1;
        $('#dg').combobox('reload', url);
	}
*/
	$('#interfacetype').combobox({
		onSelect : function() {
			var interfaceType = $("input[name='interfacetype']").val();
			if (interfaceType == 1 || interfaceType == "财务底稿数据") {
				$("#digaoType").show();
			} else if (interfaceType == 2 || interfaceType == "全帐套估值表数据") {
				$("#digaoType").hide();
			} else if (interfaceType == 3 || interfaceType == "到期日数据") {
				$("#digaoType").hide();
			}else if (interfaceType == 4|| interfaceType == "万能险估值表数据") {
				$("#digaoType").hide();
			}else if (interfaceType == 5 || interfaceType == "分红险估值表数据") {
				$("#digaoType").hide();
			}
		}
	});
	//选择数据类型后显示不同的列表
	$("#interfacetype").combobox({
		onChange : function() {
			var interfaceType = $("input[name='interfacetype']").val();
			if (interfaceType == 1 || interfaceType == "财务底稿数据") {
				$("#divZC").hide();
				$("#divGD").hide();
				$("#divKM").show();
				$("#divWNGZ").hide();
				$("#divFHGZ").hide();
				$('#dg').datagrid('reload');
				displayData();
			} else if (interfaceType == 2 || interfaceType == "全帐套估值表数据") {
				$("#divZC").show();
				$("#divKM").hide();
				$("#divGD").hide();
				$("#divWNGZ").hide();
				$("#divFHGZ").hide();
				$('#dgZC').datagrid('reload');
				displayData();
			} else if (interfaceType == 3 || interfaceType == "到期日数据") {
				$("#divGD").show();
				$("#divZC").hide();
				$("#divKM").hide();
				$("#divWNGZ").hide();
				$("#divFHGZ").hide();
				$('#dgGD').datagrid('reload');
				displayData();
			} else if (interfaceType == 4 || interfaceType == "万能险估值表数据") {
				$("#divGD").hide();
				$("#divZC").hide();
				$("#divKM").hide();
				$("#divWNGZ").show();
				$("#divFHGZ").hide();
				$('#dgWNGZ').datagrid('reload');
				displayData();
			} else if (interfaceType == 5 || interfaceType == "分红险估值表数据") {
				$("#divGD").hide();
				$("#divZC").hide();
				$("#divKM").hide();
				$("#divWNGZ").hide();
				$("#divFHGZ").show();
				$('#dgFHGZ').datagrid('reload');
				displayData();
			}
			
		}
	});
	function displayData() {
		var interfaceType = $("input[name='interfacetype']").val();
		var digaoType = $("input[name='finadigaoType']").val();
		var reportId=$("input[name='reportId']").val();
		
		var accountType=null;
		/*$('#serachFrom').find('input').each(function() {
			var obj = $(this);
			var name = obj.attr('name');
			if (name) {
				params[name] = obj.val();
			}
		});
		*/
		if (interfaceType == "1" || interfaceType == "财务底稿数据") {
			var params = {"type1":digaoType,"reportId":reportId};
			$('#dg').datagrid("reload", params);
		} else if (interfaceType == "2" || interfaceType == "全帐套估值表数据") {
			accountType="全账套";
			var params = {"type1":digaoType,"reportId":reportId,"accountType":accountType};
			$('#dgZC').datagrid("reload", params);
		} else if (interfaceType == "3" || interfaceType == "到期日数据") {
			var params = {"type1":digaoType,"reportId":reportId};
			$('#dgGD').datagrid("reload", params);
		}else if (interfaceType == "4" || interfaceType == "万能险估值表数据") {
			accountType="万能险";
			var params = {"type1":digaoType,"reportId":reportId,"accountType":accountType};
			$('#dgWNGZ').datagrid("reload", params);
		}else if (interfaceType == "5" || interfaceType == "分红险估值表数据") {
			accountType="分红险";
			var params = {"type1":digaoType,"reportId":reportId,"accountType":accountType};
			$('#dgFHGZ').datagrid("reload", params);
		}
	}
</script>
</html>