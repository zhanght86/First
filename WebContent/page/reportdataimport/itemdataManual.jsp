<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>
<%@include file="/commons/statics.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>手工录入</title>
</head>
<%	String path = request.getContextPath();
	String basepath=request.getContextPath()+ request.getServletPath().substring(0,request.getServletPath().lastIndexOf("/") + 1); %>
<body>
		<!-- 搜索模块 -->
	<div class="easyui-panel" style="padding: 10px 0px 10px 0px;">
		<form id="serachFrom" method="post">
		<table cellpadding="5">
		       <tr>
	    			<td style="width:80px;text-align:right;padding-right: 40px;" >
	    				<label>报告类型:</label>
	    			</td>
	    			<td style="width:300px;">
	    				<input class="easyui-combobox  validatebox"  id="reportRate" name="reportRate" style="width:200px"
					    data-options="
						url: '<%=path%>/codeSelect.do?type=reporttype',
						prompt:'报表类型',
						method: 'get',
						valueField:'value',
						textField:'text',
						groupField:'group',
						required:true">
	    			</td>
	    			<td style="width:80px;text-align: right;padding-right: 40px;" >
	    				<label>报告名称:</label>
	    			</td>
	    			<td>
	    				<input id="reportName"  name="reportName" class="easyui-combobox"   style="width: 200px;"
	    				data-options=" url:'<%=path%>/codeSelect.do?type=sheet',
	    				prompt: 'sheet', 
	    				method: 'get',
						valueField:'value',	
						textField:'text',
						groupField:'group'">
	    				   
	    			</td>
	    		</tr>
	    		
	    		<tr>
	    			<td style="width:80px;text-align: right;padding-right: 40px;" >
	    				<label>年&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;度:</label>
	    			</td>
	    			<td>
	    				<input  class="easyui-numberbox  validatebox"  id='month' name='month' data-options="prompt: '年度',required:true"  style="width: 200px;">
	    			</td>
	    			<td style="width:80px;text-align: right;   padding-right: 40px;"  >
	    				<label>季&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;度:</label>
	    			</td>
	    			<td>
	    				<input  class="easyui-combobox  validatebox"  style="width: 200px;"  name="quarter" 
	    				data-options="
	    				prompt: '季度', 
	    				url:'<%=path%>/codeSelect.do?type=quarter',
	    				method: 'get',
						valueField:'value',
						textField:'text',
						groupField:'group',
						required:true" >
	    			</td>
	    			
	    		</tr>
	    		<tr>
	    			<td style="width:80px;text-align:right;padding-right: 40px;" >
	    				<label>指标代码:</label> 
	    			</td>
	    			<td style="width:300px;">
	    				<input id="reportItemCode" name="reportItemCode" class="easyui-textbox" data-options="prompt: '指标代码'" style="width: 200px;">
	    			</td>
	    			<td style="width:80px;text-align: right;padding-right: 40px;" ></td>
	    			<td></td>
	    			<td colspan="2" style="width:280px;text-align: right;padding-right: 40px;" >
						<a href="javascript:void(0)" class="easyui-linkbutton" onclick="serach()"  data-options="iconCls:'icon-search'" style="width:80px;">搜索</a>
	    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="reset()" data-options="iconCls:'icon-reload'" style="width:80px;">重置</a>
					</td>
	    		</tr>
	    		<tr>
	    		</tr>
	    	</table>
	</form>	
</div>
	    <table id="dg"  class="easyui-datagrid" style="width:100%"
    		url=${pageContext.request.contextPath}/itemdatamanual/list.do?userCode=${currentUser.userCode}
    		toolbar="#toolbar"
    		rownumbers="true" fitColumns="true" singleSelect="true" pagination="true" sortName="reportCode">
    	<thead>
    		<tr>
    			<th field="ck" checkbox=true></th> 
    			<th field="reportCode"      width="30"  data-options="sortable:true">报表代码</th>
    			<th field="reportName"      width="80"  data-options="sortable:true">报表名称</th>
    			<th field="reportItemCode"  width="30"  data-options="sortable:true">指标代码</th>
    			<th field="reportItemName"  width="80"  data-options="sortable:true">指标名称</th> 
    			<th field="reportItemValue" width="30"  data-options="precision:2" class="easyui-numberbox" align="right" formatter="jsutil.tool.formatCurrency">数值型值</th>
    			<th field="textValue"       width="40"  align="center" >文本型值</th>
    			<th field="computElevel"    hidden="true">computElevel</th>
    		</tr>
    	</thead>
    </table>
    <div id="toolbar">
    	<a href="#" class="easyui-linkbutton" iconCls="icon-add"    plain="true" onclick="add()">录入数据</a>
    	<a href="#" class="easyui-linkbutton" iconCls="icon-edit"   plain="true" onclick="edit()">编辑数据</a>
    </div>
     <!-- ------------------------------------ 录  入  窗  口--------------------------------------- -->
	<div id="dlg" class="easyui-dialog" style="width: 600px; padding: 10px 20px" align="center" data-options="modal:true,closed:true" buttons="#dlg-buttons_add">
    	<form id="fm" method="post">
    		<table cellpadding="5">	
    		    <tr>
	    			<td><label>指标代码:<font color="red">*</font></label></td>
	    			<td>
						<input class="easyui-textbox easyui-validatebox show"  name="reportItemCode"  data-options="prompt:'指标代码',required:true">
					</td>
					<td><label>报告类型:<font color="red">*</font></label></td>
	    			<td>
						<input class="easyui-combobox"  name="reportRate" 
					    data-options="url: '<%=path%>/codeSelect.do?type=reporttype',prompt:'报表类型',
						method: 'get',valueField:'value',textField:'text',groupField:'group',required:true">
					</td>
	    		</tr>
    		   
	    		<tr>
	    			<td><label>年&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;度:<font color="red">*</font></label></td>
	    			<td>
	    				<input  class="easyui-numberbox"  name='month' data-options="prompt: '年度',required:true" >
	    			</td>
	    			<td ><label>季&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;度:<font color="red">*</font></label></td>
	    			<td>
	    				<input  class="easyui-combobox" name="quarter" 
	    				data-options="prompt: '季度', url:'<%=path%>/codeSelect.do?type=quarter',
	    				method: 'get',valueField:'value',textField:'text',groupField:'group',required:true" >
	    			</td>
	    		</tr>
	    		<tr>
	    			<td><label>数值型值:<font color="red">*</font></label></td>
	    			<td>
						 <input class="easyui-numberbox"   name="reportItemValue" data-options="prompt:'数值型值',precision:2" formatter="jsutil.tool.formatCurrency">
					</td>
					<td><label>文本型值:<font color="red">*</font></label></td>
	    			<td>
						 <input class="easyui-textbox"   name="textValue" data-options="prompt:'文本型值'">
					</td>
	    		</tr>
	    	</table>
    		<input  type="hidden"  name="userCode" value="${currentUser.userCode}">
    	</form>
    	<div id="dlg-buttons_add">
    	<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="save()">保存</a>
    	<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">取消</a>
    </div>
  </div>
  <!-- ------------------------------------编   辑  窗  口 --------------------------------------- -->
  <div id="edit_dlg" class="easyui-dialog" style="width: 600px; padding: 10px 20px" align="center" data-options="modal:true,closed:true" buttons="#dlg-buttons_edit">
    	<form id="edit_fm" method="post">
    		<table cellpadding="5">	
    		    <tr>
	    			<td><label>报表代码:</label></td>
	    			<td>
						<input class="easyui-textbox"  name="reportCode" readOnly="readonly">
					</td>
					<td><label>报表名称:</label></td>
	    			<td>
						<input class="easyui-textbox"  name="reportName" readOnly="readonly">
					</td>
	    		</tr>
    		   
	    		<tr>
	    			<td><label>指标代码:</label></td>
	    			<td>
	    				<input  class="easyui-textbox"  name="reportItemCode" readOnly="readonly">
	    			</td>
	    			<td ><label>指标名称:</label></td>
	    			<td>
	    				<input  class="easyui-textbox" name="reportItemName" readOnly="readonly">
	    			</td>
	    		</tr>
	    		<tr>
	    			<td><label>数值型值:<font color="red">*</font></label></td>
	    			<td>
						 <input class="easyui-numberbox"   name="reportItemValue" data-options="prompt:'数值型值',precision:2" formatter="jsutil.tool.formatCurrency">
					</td>
					<td><label>文本型值:<font color="red">*</font></label></td>
	    			<td>
						 <input class="easyui-textbox"   name="textValue" data-options="prompt:'文本型值'">
					</td>
	    		</tr>
	    	</table>
    		<input  type="hidden"  name="userCode" value="${currentUser.userCode}">
    	</form>
    	<div id="dlg-buttons_edit">
    	<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="edit_save()">保存</a>
    	<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#edit_dlg').dialog('close')">取消</a>
    </div>
  </div>
    <script type="text/javascript">
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
	        });
	    	
	    	$('#dg').datagrid('load', params);
	    } 
	   }
	  
	  //  录入数据
        function add(){
        	var row = $('#dg').datagrid('getSelected');
        	if(row.reportItemValue!==null||row.textValue!==null){
        		$.messager.alert('提示框','数据已存在,若想修改数据,请选择编辑数据按钮','info');return  false}
            if (row){
            	$('#dlg').dialog('open').dialog('setTitle','录入数据');
            	$('#fm').form('load',row);
            	url = '${pageContext.request.contextPath}/itemdatamanual/add.do?rid='+row.reportItemCode;
            	}
	  }
	  // 编辑数据
	  function edit(){
        	var row = $('#dg').datagrid('getSelected');
        	if(row.outItemCode==null){
        		$.messager.alert('提示框','数据不存在,请先录入数据','info');
        		return  false}
            if (row){
            	$('#edit_dlg').dialog('open').dialog('setTitle','编辑数据');
            	$('#edit_fm').form('load',row);
            	var  array=row.outItemCode+'&comCode='+row.comCode+'&month='+row.month+'&quarter='+row.quarter+'&reportRate='+row.reportRate
            	url = '${pageContext.request.contextPath}/itemdatamanual/editor.do?outItemCode='+array;
            	}
    	}
        
        
        // 保存
        function edit_save(){
        	$('#edit_fm').form('submit',{
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
        				$('#edit_dlg').dialog('close');		
        				$('#dg').datagrid('reload');	
        			}
        		}
        	});
        }
        
        // 修改保存
        
        //  重置
        function  reset(){
            $('#serachFrom').form('reset');
        }
        
//   --------------------------
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
</body>
</html>