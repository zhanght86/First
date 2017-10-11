<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%
	String path=request.getContextPath()+ request.getServletPath().substring(0,request.getServletPath().lastIndexOf("/") + 1); 
%>
<title>偿二代报告单报送号管理</title>
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
		<form id="serachFrom" method="post" style="margin-bottom: 0">
			<table cellpadding="5" border='0' style="width: 100%; overflow: hidden;table-layout: fixed;">						
		    		<tr>
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
		    				<input style="width: 100%;"  name="year" class="easyui-textbox" data-options="  editable:false , disabled:false , missingMessage:'请输入年份' "/>
		    			</td>
		    			<td style="width:8%;text-align: right;" >
		    				<label>报送季度:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input  name="quarter" class="easyui-combobox"  style="width:100%;"
		    				 data-options=" prompt: '报送季度',editable:false, missingMessage:'请添加季度',
						    url: '${pageContext.request.contextPath}/codeSelect.do?type=quarter',
						    method: 'get',valueField:'value', textField:'text', groupField:'group'">
		    			</td>
			    		<td style="width:8%;text-align: right;" >
		    				<label>报送类型:</label>
		    			</td>
		    			<td style="width:10%;">
		    				<input id="reportType" name="reportType" class="easyui-combobox"  style="width:100%;"
		    				data-options=" prompt: '报表类别',editable:false,
						    url: '${pageContext.request.contextPath}/codeSelect.do?type=reporttype',
						    method: 'get',valueField:'value', textField:'text',groupField:'group'">
		    			</td>	
				    </tr>
				    <tr>
				    <!-- 
				     <td style="width:8%;text-align: right;" >
		    				<label>报送状态:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<input id="reportState" name="reportState" class="easyui-combobox"  style="width:100%;"
		    				data-options="prompt: '报送状态',editable:false,
						    url: '${pageContext.request.contextPath}/codeSelect.do?type=cfreportstate',
						    method: 'get', valueField:'value',textField:'text',groupField:'group'">
		    			</td>
			    		<td style="width:10%;text-align: right;" >
		    				<label>是否完成手工导入:</label>
		    			</td>
		    			<td style="width:13%;">
		    				<select id="handleFlag" name="handleFlag" class="easyui-combobox" data-options="prompt: '是否完成手工导入',editable:false"   style="width:100%;">
		    					<option value=""></option>
		    					<option value="0">否</option>
		    					<option value="1">是</option>
		    				</select>
		    			</td>
		    			 -->
				    </tr>
				    <tr>
		    			<td colspan="8" style="text-align: right;padding-right: 20px" >
	    					<a href="javascript:void(0)" class="easyui-linkbutton" onclick="serach()"  data-options="iconCls:'icon-search'" style="width:8%;">查询</a>
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
<!-- 	操作，展示部分 -->
	 <div class="easyui-panel" style="border: 0;width: 100%">
		<table id="dg" class="easyui-datagrid"
			toolbar="#toolbar" 
			rownumbers="true" 
			fitColumns="true"
			singleSelect="false" 
			pagination="true" 
			striped="true"
			nowrap="false"
			autoRowHeight="false"
			style="table-layout:fixed;word-break:break-all;width:100%;height:auto;"		
			loadMsg="加载数据中..." 
			>
			<div id="toolbar">
				<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newCalCfReportInfo()">新建</a> 
				<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" plain="true" onclick="destroyCalCfReportInfo()">删除</a>
			</div>
			<thead>
				<tr>
					<th field="ck" checkbox=true></th>
					<th field="reportId" width="22%" align="center" halign="center">报送号</th>
					<th field="year" width="15%" align="center" halign="center">报送年度</th>
					<th field="quarter" width="8%" hidden>报送季度</th>
					<th field="quarterName" width="15%" align="center" halign="center">报送季度</th>
					<th field="reportType" width="12%" hidden>报送类型</th>
					<th field="reportTypeName" width="15%" align="center" halign="center">报送类型</th>
					<th field="reportCateGory" width="12%" hidden>报告类型</th>
					<th field="reportCateGoryName" width="15%" align="center" halign="center">报告类型</th>
					<th field="comCode" width="15%" align="center" halign="center">机构编码</th>
					
					<!--  
					<th field="reportState" width="12%" hidden>报送状态</th>
					<th field="reportStateName" width="10%" align="center" halign="center">报送状态</th>
					<th field="handleFlag" width="12%" formatter="isYON" align="center" halign="center">是否完成手工导入</th>
					
					-->
				</tr>
			</thead>
		</table>
	</div>
	<!--  新增弹框界面-->
	<div id="dlg" class="easyui-dialog"
		style="width: 400px; height: 350px; padding: 10px 20px" closed="true" align="center" 
		data-options="modal:true,closed:true,resizable:true,collapsible:true,minimizable:true,maximizable:true" 
		buttons="#dlg-buttons">
		<form id="fm" method="post">
			<table cellpadding="5">
				<tr>
				    <td style="width:20%;" >
	    				<label>报送年度:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="year" class="easyui-textbox" name="year"  data-options="prompt: '报送年度',required:true,editable:false,"  style="width:60%;">
	    			</td>
	    		</tr>
				<tr>
				    <td style="width:20%;" >
	    				<label>报送季度:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="quarter" type="text" name="quarter" class="easyui-combobox" editable="false"  required style="width:60%;"
	    				data-options="
					        prompt: '报送季度',
					        missingMessage:'请添加季度',
					        editable:false,
						    url: '${pageContext.request.contextPath}/codeSelect.do?type=quarter',
						    method: 'get',
						    valueField:'value',
						    textField:'text',
						    groupField:'group'
					    	">
	    			</td>
	    		</tr>
				<tr>
				    <td style="width:20%;" >
	    				<label>报送类型:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="reportType" type="text" name="reportType" class="easyui-combobox" editable="false" required style="width:60%;"
	    				data-options="
					        prompt: '报表类别',
					        missingMessage:'请添加报表类别',
						    url: '${pageContext.request.contextPath}/codeSelect.do?type=reporttype',
						    method: 'get',
						    valueField:'value',
						    textField:'text',
						    groupField:'group',
						    onSelect: function(rec){
						    if(rec.value==2){
						    	$('#needHidden').show();
						    	$('#reportCateGory').combobox('setValue','0');
						    }else{
						    	$('#needHidden').hide();
						    }
						    }">
	    			</td>	    			
	    		</tr>
	    		<tr id='needHidden'>
				    <td style="width:20%;" >
	    				<label>报告类型:</label>
	    			</td>
	    			<td style="width:30%;">
	    			<!-- 	<input id="reportCateGory" type="text" name="reportCateGory" class="easyui-combobox" editable="false" style="width:60%;"
	    				>  -->
	    			<select id="reportCateGory" type="text" name="reportCateGory" class="easyui-combobox" editable="false" style="width:60%;"> 
					</select>
	    				
	    			</td>
	    		</tr>
	    		<tr>
				    <td style="width:20%;" >
	    				<label>报送日期:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input id="reportType" type="text" name="dataDate" class="easyui-datebox" editable="false" required style="width:60%;">
	    			</td>
	    		</tr>
	    	</table>
		</form>
		<div id="dlg-buttons">
	    	<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveCalCfReportInfo()">保存</a>
	    	<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">取消</a>
	    </div>
    </div>
</body>
<script  type="text/javascript">

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
$(function(){
	$('#reportCateGory').combobox({ 
		 	 prompt: '报告类别',
		     required:true,
		     url :'${pageContext.request.contextPath}/codeSelect.do?type=reportCateGory',
		     missingMessage:'请添加报告类别',
		     method: 'get',
		     valueField:'value',
		     textField:'text',
		     groupField:'group',
		    });	
	//$('#reportCateGory').combobox('setValue','0');	
    $('#reportCateGory').combobox('disable');  
   });
function isYON(value){
	if(value=='0'){
		return '否';
	}else{
		return '是';
		} 
}
// 新建
    function newCalCfReportInfo(){   	
    	$('#dlg').dialog('open').dialog('setTitle','新增');  	
    	$('#fm').form('clear');
    	$('#needHidden').hide();
    	url='${pageContext.request.contextPath}/calCfReportInfo/add.do';
	}
//保存
function saveCalCfReportInfo(){
	//$('#quarter').val($('#quarterCombox').combobox('getValue'));
	$('#fm').form('submit',{
		url: url,
		onSubmit: function(){
			return $(this).form('validate');
		},
		success: function(result){
			var result = eval('('+result+')');
			if (result.errorMsg){
				jsutil.msg.alert(result.errorMsg);
			} else {
				$('#dlg').dialog('close');		// close the dialog
				$('#dg').datagrid('reload');	// reload the user data
				jsutil.msg.alert("保存成功");
			}
		}
	});
}
//修改
function editCalCfReportInfo(){
    var rows = $('#dg').datagrid('getSelections');
    
    if(!rows){$.messager.alert('消息提示','请选择要编辑的数据信息!','error');return ;}
    if(rows.length>1){$.messager.alert('消息提示','请选择一条数据进行编辑!','error');return ;}
	$('#dlg').dialog('open').dialog('setTitle','编辑');
	$('#fm').form('load',rows[0]);
	url = '${pageContext.request.contextPath}/calCfReportInfo/update.do?id='+rows[0].reportId;
	
}

//删除
function destroyCalCfReportInfo(){
	var rows = $('#dg').datagrid('getSelections');
	var idArr = new Array();
	for(var i=0;i<rows.length;i++){
		idArr[i] = rows[i].reportId;
	}
	var data =[{name:'ids',value:idArr.join(',') }];
	if(rows.length==0){$.messager.alert('消息提示','请选择要删除的数据信息!','error');return ;}
		$.messager.confirm('删除','确认删除吗?',function(r){
			if (r){
				$.post('${pageContext.request.contextPath}/calCfReportInfo/delete.do',data,function(result){
					if (result.success){
						$('#dg').datagrid('reload');	// reload the user data
						jsutil.msg.alert("删除成功");
					} else {
						jsutil.msg.alert(result.errorMsg);
					}
				},'json');
			}
		});
}
//	重置
function reset(){
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
	params["reportCateGory"]='0';
	//$('#dg').datagrid("load", params); 
	  $("#dg").datagrid({
		     url:'${pageContext.request.contextPath}/calCfReportInfo/listpage.do',		
		     	queryParams: params,
				onBeforeLoad: function(){
					if(!$('#serachFrom').form('validate')) return false;
				},
				onLoadSuccess: function(data){
					if(data.total>0) {
		 				return ;
		 			}
		 			$.messager.alert("提示","未查询到相关数据","info");
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
			reset();
		}
}


//修改
function edit(){
       var row = $('#dg').datagrid('getSelected');
       if(!row){$.messager.alert('消息提示','请选择要编辑的数据信息!','error');return ;}
       if (row){
       	$('#dlg').dialog('open').dialog('setTitle','修改信息');
       	$('#fm').form('load',row);
       	var   array=row.tableCode+'&&colCode='+row.colCode+'&&yearMonth='+row.yearMonth+'&&quarter='+row.quarter+'&&reportRate='+row.reportRate    
       	url = '${pageContext.request.contextPath}/RowAdd/update.do?tableCode='+array;
       }
   } 
   //保存
function save(){
 	$('#fm').form(	'submit',{
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
 				$('#dlg').dialog('close');		// close the dialog
 				$('#dg').datagrid('reload');	// reload the user data
 				jsutil.msg.alert("修改成功");
 			}
 		}
 	});
  }
  //  导出 
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