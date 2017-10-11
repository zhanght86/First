<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>
<%@include file="/commons/statics.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>固定因子查看</title>
<style type="text/css">
.datagrid-header-row td {
	height: 40px;
	font-weight: bold;
}
</style>
</head>
<%	String path = request.getContextPath();
	String basepath=request.getContextPath()+ request.getServletPath().substring(0,request.getServletPath().lastIndexOf("/") + 1); %>
<body>
	<!-- 操作部分 -->
	<div class="easyui-panel" style="height: auto; width:100%; padding: 8px 0px 0px 0px;overflow: hidden;">
		<form  id="serachFrom" style="margin-bottom:0;">
			<table cellpadding="5" border='0' style="width: 100%; overflow: hidden;table-layout: fixed;">
	    		<tr>
	    			<td style="width:8%;text-align: right;" >
		    				<label>年度：</label>
		    			</td>
		    			<td style="width:13%;">
		    				<!-- <input type="text" id="month" name="month"   class="easyui-numberbox" data-options="prompt: '年度'"  style="width:100%;"> -->
		    				<input style="width: 100%;" id="month" name="month" class="easyui-textbox" data-options="  editable:false , disabled:false , missingMessage:'请输入年份' "/>
		    			</td>
		    			<td style="width:8%;text-align: right;"  >
		    				<label>季度：</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input   id="quarter"  name="quarter"  class="easyui-combobox"  style="width: 100%;"  
		    				data-options="
		    				prompt: '季度', 
		    				required : true,
		    				editable:false,
		    				url:'<%=path%>/codeSelect.do?type=quarter',
		    				method: 'get',
							valueField:'value',
							textField:'text',
							groupField:'group' " >
		    			</td>
		    			<td style="width:8%;text-align:right;" >
		    				<label>报告类型：</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input class="easyui-combobox"  name="reportRate"   style="width:100%" 
						    data-options=" url: '<%=path%>/codeSelect.do?type=reporttype',
							prompt:'报表类型',
							editable:false,
							method: 'get',
							valueField:'value',
							textField:'text' ">
							 
		    			</td>
		    			<td style="width:8%;text-align: right;" >
		    				<label>报告名称：</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input  class="easyui-combobox" name="reportType" style="width: 100%;" data-options="
		    				prompt: '报告名称',
		    				editable:false,
		    				url:'<%=path%>/codeSelect.do?type=reportname',
		    				method: 'get',
							valueField:'value',
							textField:'text' ">
		    				
		    			</td>
		    			
	    			<tr>
		    			<td style="width:8%;text-align:right;" >
		    				<label>因子代码：</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input  name="outItemCode"  class="easyui-textbox" data-options="prompt: '因子代码'" style="width: 100%;">
		    			</td>
		    			<td style="width:8%;text-align:right;" >
		    				<label>因子名称：</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input  name="reportItemName"  class="easyui-textbox" data-options="prompt: '因子名称'" style="width: 100%;">
		    			</td>	    				    		
		    		    <td style="width:8%;text-align:right;" >
		    				<label>表代码：</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input  name="sheetCode"  class="easyui-textbox" data-options="prompt: '表代码'" style="width: 100%;">
		    			</td>
		    			<td style="width:8%;text-align: right;" >
		    			    <label>表名称：</label>
		    			</td>
		    			<td>
		    				<input  name="sheetName"  class="easyui-combobox"  style="width: 100%;"  
		    				data-options=" url:'<%=path%>/codeSelect.do?type=sheet',
		    				prompt: '表名称', 
		    				editable:false,
		    				method: 'get',
							valueField:'value',
							textField:'text',
							groupField:'group' " >						
		    			</td>
	    			</tr>
	    			<tr>
	    				<td colspan="8" style="text-align: right; padding-right: 20px" >
							<a href="javascript:void(0)" class="easyui-linkbutton" onclick="serach()"  data-options="iconCls:'icon-search'" style="width:8%;">查询</a>
		    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="reset()"   data-options="iconCls:'icon-reload'" style="width:8%;">重置</a>
		    				<!-- <a href="javascript:void(0)" class="easyui-linkbutton" onclick="exportXls('dg','信息导出excel')"   data-options="iconCls:'icon-print'"  style="width:8%;">导出</a> -->
		    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="exportByConditionAll()"   data-options="iconCls:'icon-print'"  style="width:8%;">导出</a>
		    				
	    			</td>
	    		</tr>
	    		
	    	</table>
		
	</form>	
</div>  <!-- 展示部分 -->
	 <div class="easyui-panel" style="border: 0;width: 100%">
	    <table id="dg"  class="easyui-datagrid" style="width:100%;"
				toolbar="#toolbar" 
				rownumbers="true" 
				singleSelect="true" 
				fitColumns="true"
				pagination="true" 
				striped="true"
				nowrap="false"
				sortName="outItemCode"
				style="table-layout:fixed;word-break:break-all;width:100%;height:auto;"				
				pageList="[10,100,1000,5000]"
				loadMsg="加载数据中..." >
    	<thead>
    		<!-- <tr>  
    		   <th field="ck" checkbox="true"></th> 
    			<th field="codeName"    align="center" halign="center"     width="15%" data-options="sortable:true" >报告类型</th>
    			<th field="outItemCode" align="center" halign="center"     width="15%" data-options="sortable:true" >因子代码</th>
    			<th field="month"    align="center" halign="center"     width="5%" data-options="sortable:true" >年度</th>
    			<th field="quarter" align="center" halign="center"     width="8%" data-options="sortable:true" >季度</th>
    			<th field="reportItemName" align="left" halign="center"  width="27%" data-options="sortable:true" >因子名称</th>
    			<th field="reportItemValue" align="center" halign="center"  width="13%" data-options="sortable:true,precision:2" class="easyui-numberbox"  >数值型值</th>
    			<th field="textValue"     align="left" halign="center"   width="13%" data-options="sortable:true"     >文本型值</th>
    	   </tr> -->
    	   <tr>
    		  <!--   <th field="ck"   checkbox="true">选择</th> -->
    			<th field="month" align="center" halign="center" width="5%" data-options="sortable:true" >年度</th>
    			<th field="quarter" align="center" halign="center"     width="7%" data-options="sortable:true" >季度</th>
    		    <th field="codeName" width="6%"   align="center" halign="center" data-options="sortable:true" >报告类型</th>
    			<th field="reportType" width="8%"   align="center" halign="center" data-options="sortable:true" >报告名称</th>
    			<th field="outItemCode" width="10%"   align="center" halign="center" data-options="sortable:true" >因子代码</th>
    			<th field="reportItemName" width="34%"  align="left" halign="center" data-options="sortable:true">因子名称</th>
    			<th field="sheetCode" width="10%"  align="center" halign="center" data-options="sortable:true" >表代码</th>
    		    <!-- <th field="sheetName" width="10%"  align="center" halign="center" data-options="sortable:true" >表名称</th>  -->
    			<th field="reportItemValue" width="10%"  align="center" halign="center" data-options="sortable:true,precision:2" class="easyui-numberbox" formatter="jsutil.tool.formatCurrency" >数值型值</th>
    			<th field="textValue" width="8%"  align="center" halign="center" data-options="sortable:true" >文本型值</th>
	       </tr> 
    	</thead>
    </table>
  </div>
   <!--  <div id="toolbar">
    	<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="edit()">修改</a>
    </div> -->
    <div id="dlg" class="easyui-dialog" style="width: 600px; padding: 10px 20px" align="center"  data-options="modal:true,closed:true,resizable:true,collapsible:true,minimizable:true,maximizable:true"  buttons="#dlg-buttons">
    	<form id="fm" method="post">
    		<table cellpadding="5" >
	    		<tr>
	    			<td><label>报告类型:</label></td>
	    			<td>
						 <input class="easyui-textbox" type="text"  id="codeName" name="codeName" class="easyui-validatebox"data-options="prompt:'报告类型'"  readonly="readonly">
					</td>
					
					<td><label>因子名称:</label></td>
	    			<td>
						<input class="easyui-textbox" type="text"  id="reportItemName" name="reportItemName" class="easyui-validatebox textbox" data-options="prompt:'因子名称'" readonly="readonly">
					</td>
	    		</tr>
	    		<tr>
					<td><label>数值型值:</label></td>
	    			<td>
						<input  type="text"  name="reportItemValue" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2"   readonly="readonly">
					</td>
					
					<td><label>数值型值<font  color="red">（修改为）</font>:</label></td>
	    			<td>
						<input id="shuzhixiugai"  type="text"  name="reportItemValue_after" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2" >
					</td>
	    		</tr>
	    		</table>
	    		<table>
	    		<tr>
	    		    <td>
	    		        <label>文本型值:</label>
	    		    </td>
	    		    <td>
	    		        <input type="text"  id="textValue" style="width: 360px" name="textValue" class="easyui-textbox" data-options="prompt:'文本型值'"  readonly="readonly">
	    		    </td>
	    		</tr>
	    		<tr>    
	    		    <td>
	    		        <label>文本型值<font  color="red">（修改为）</font>:</label>
	    		    </td>
	    		    <td>
	    		        <input  type="text"  id="textValue_after" style="width: 360px" name="textValue_after" class="easyui-textbox" data-options="prompt:'文本型值'">
	    		    </td>
	    		</tr>
	    		
	    	</table>
    		<input  type="hidden"  name="token"  value="${token}"/>
    		<input  type="hidden"  name="userCode"  value="${currentUser.userCode}"/>
    	</form>
    <div id="dlg-buttons">
    	<a href="#" class="easyui-linkbutton" iconCls="icon-ok"     onclick="save()">保存</a>
    	<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="reset2()">取消</a>
    </div>
  </div>
   
</body>
<script  type="text/javascript">
$(function(){
	$('#month').combobox({
	    disabled:false,
	    required : true,
	    prompt:'年度',
	    valueField:'value',
	    textField:'text',
	    url:'${contextPath}/json/year.json'
	});
	//serach();
});
//搜索
function serach(){
 if($('#serachFrom').form('validate')){
	var params = {};
	$('#serachFrom').find('input').each(function(){
        var obj = $(this);
        var name = obj.attr('name');
        if(name){
        	params[name] = obj.val();
        }
    }) 
	 // $('#dg').datagrid('load',params)
    $("#dg").datagrid({
       url:'${pageContext.request.contextPath}/indexCode/list.do?userCode=${currentUser.userCode}',
     	queryParams: params,
		onBeforeLoad: function(){
			if(!$('#serachFrom').form('validate')) return false;
		},
		onLoadSuccess: function(data){
			if(data.total>0) {					
 				return false;
 			}
 			$.messager.alert("提示","未查询到相关数据","info");
		}
	});
 }else{
		$.messager.show({
			title : '提示',
			msg : '请选择年、季再查询相关数据',
			timeout:2000
		});
	}
}
	  // console.log(params);     在控制台打印

// 重置
function reset(){
	$('#serachFrom').form("clear");
}
function reset2(){
	$('#dlg').dialog('close');
	
}	  

//  修改
 function edit(){
        var row = $('#dg').datagrid('getSelected');
        if(!row){$.messager.alert('消息提示','请选择要编辑的数据信息!','error');return ;}
        if (row){
        	$('#dlg').dialog('open').dialog('setTitle','修改信息'); // OUTITEMCODE, COMCODE, MONTH, QUARTER, REPORTRATE
        	$('#fm').form('load',row);
        	var  array=row.outItemCode+'&comCode='+row.comCode+'&month='+row.month+'&quarter='+row.quarter+'&reportRate='+row.reportRate
        	url = '${pageContext.request.contextPath}/indexCode/update.do?outItemCode='+array;
        	$('#shuzhixiugai').numberbox('setValue','');
        	$('#textValue_after').textbox('setValue','');
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
  				
  				$('#dg').datagrid('reload');
  				$('#dlg').dialog('close');	
  				jsutil.msg.alert("修改成功");
  			}
  		}
  	});
   }
 
 function exportByConditionAll(){
		//准备参数
		
		var xlsName ="固定因子数据管理";		
		var result = prepareParam(xlsName); 
		var grid = $("#dg");
		if(result=="false") {
			return false;
		}
		//将查询数据导出
		jsutil.core.download("${pageContext.request.contextPath}/indexCode/downloadAll",result);

		
	}
//准备参数
 function prepareParam(xlsName){
 	var param = {name: xlsName, queryConditions: {}, cols: []};
 	var grid = $("#dg");
 	//返回加载完毕后的数据
 	var datas = grid.datagrid("getRows");
 	if (datas.length > 0) {
 		//返回列字段
 		var cols = grid.datagrid("getColumnFields");
  		 if (cols.length > 0 && cols[0] == "ck") {
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
     		var name =obj.attr('name');    		
     		 if(name){
     			/* if(id=='outItemCode'){
         			param.queryConditions[id]=obj.textbox('getText');
     			} else {  */
         			param.queryConditions[name]=obj.val();
     			/* } */
     		}
     	}); 
 		//console.log(param);
 		return param;
 	} else {
 		$.messager.alert("提示","请先查询或者没有要导出的数据","warning");
 		return "false";
 	}
 };
 
 
    function exportXls(gridId, xlsName) {
		 var param=jsutil.core.exportXls(gridId,xlsName);
		jsutil.core.download("${pageContext.request.contextPath}/indexCode/download",param);
	} 
    
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