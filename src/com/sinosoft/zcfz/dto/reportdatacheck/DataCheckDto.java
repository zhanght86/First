package com.sinosoft.zcfz.dto.reportdatacheck;

//数据校验页面的参数
public class DataCheckDto {

	private String year;
	private String quarter;
	private String reporttype;
	private String checkedCode;
	private String temp;
	private String errorInfo;
	private String checkdate;
	private String reportId;
	private String errLevel; //错误级别 ：  error、warn 
	private String comCode;  //机构代码 1为总公司
	
	public String getErrLevel() {
		return errLevel;
	}
	public void setErrLevel(String errLevel) {
		this.errLevel = errLevel;
	}
	public String getComCode() {
		return comCode;
	}
	public void setComCode(String comCode) {
		this.comCode = comCode;
	}
	public String getTemp() {
		return temp;
	}
	public void setTemp(String temp) {
		this.temp = temp;
	}
	public String getErrorInfo() {
		return errorInfo;
	}
	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}
	public String getCheckdate() {
		return checkdate;
	}
	public void setCheckdate(String checkdate) {
		this.checkdate = checkdate;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getQuarter() {
		return quarter;
	}
	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}

	public String getReporttype() {
		return reporttype;
	}
	public void setReporttype(String reporttype) {
		this.reporttype = reporttype;
	}
	public String getCheckedCode() {
		return checkedCode;
	}
	public void setCheckedCode(String checkedCode) {
		this.checkedCode = checkedCode;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	
}

//<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
//<%@include file="/commons/taglibs.jsp"%>
//<%@include file="/commons/statics.jsp"%>
//<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
//<html>
//<head>
//<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
//<title>数据校验</title>
//</head>
//<%	String path = request.getContextPath();
//	String basepath=request.getContextPath()+ request.getServletPath().substring(0,request.getServletPath().lastIndexOf("/") + 1); %>
//<body>
//	<!-- 操作部分 -->
//	<table  title="数据校验条件" class="easyui-datagrid" style="width:100%;">	</table>
//	<div class="easyui-panel" style="padding: 10px 0px 10px 0px;">
//		<form  id="serachFrom">
//		
//		<table cellpadding="5"  width="100%">
//		
//	    		<tr>
//	    			<td style="width:80px;text-align:right;padding-right: 40px;" >
//	    				<label>报表类型</label>
//	    			</td>
//	    			<td style="width:300px;">
//	    				<input class="easyui-combobox" id="reporttype" name="reporttype" style="width:200px"
//					data-options="
//						url: '<%=path%>/codeSelect.do?type=reporttype',
//						prompt:'报表类型',
//						method: 'get',
//						valueField:'value',
//						textField:'text' ">
//						 
//	    			</td>
//	    		   <td style="width:80px;text-align: right;padding-right: 40px;" >
//	    				<label>校验代码</label>
//	    			</td>
//	    			<td>
//	    				<input  id="checkedCode"   name="checkedCode" class="easyui-textbox" data-options="prompt: '校验代码',required:true"  style="width: 200px;">
//	    			</td>
//	
//	    		</tr>
//	    		
//	    		<tr>
//	    			
//	    			<td style="width:80px;text-align: right;padding-right: 40px;" >
//	    				<label>年度<font  color="#ff0000">*</font></label>
//	    			</td>
//	    			<td>
//	    				<input  id="year" name="year" class="easyui-textbox" data-options="prompt: '年度',required:true"  style="width: 200px;">
//	    			</td>
//	    			
//	    			<td style="width:80px;text-align: right;   padding-right: 40px;"  >
//	    				<label>季度<font  color="#ff0000">*</font></label>
//	    			</td>
//	    			<td>
//	    				<input  id="quarter"   name="quarter"  class="easyui-combobox"  style="width: 200px;"  
//	    				data-options="
//	    				prompt: '季度', 
//	    				url:'<%=path%>/codeSelect.do?type=quarter',
//	    				method: 'get',
//						valueField:'value',
//						textField:'text',
//						groupField:'group',
//	    				required:true" >
//	    			</td>
//	    			
//	    		</tr>
//	    		<tr>
//	 
//	    			<td style="width:80px;text-align: right;padding-right: 40px;" >
//	    			</td>
//	    			<td style="width:280px; padding-right: 40px;" align="left" >
//						<a href="javascript:void(0)" class="easyui-linkbutton" onclick="check()"  data-options="iconCls:'icon-search'" style="width:80px;">校验</a>
//						<a href="javascript:void(0)" class="easyui-linkbutton" onclick="search()"  data-options="iconCls:'icon-search'" style="width:80px;">查询</a>
//	    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="reset()"   data-options="iconCls:'icon-print'"  style="width:80px;">导出</a>
//					</td>
//	    		</tr>
//	    	</table>
//		
//	</form>	
//</div>  <!-- 展示部分 -->
//
//	    <table id="dg" title="数据校验错误结果" class="easyui-datagrid" style="width:100%;"
//	        url=${pageContext.request.contextPath}/dataCheck/errorList.do
//			toolbar="#toolbar" 
//			rownumbers="true" 
//			fitColumns="true"
//			singleSelect="false" 
//			pagination="true" 
//			striped="true"
//			nowrap="true"	
//			loadMsg="加载数据中..." >
//      	<thead>
//    		<tr>  
//    			<th field="reportType" width="15">报表类型</th>
//     			<th field="year"  width="10">年度</th>
//    			<th field="quarter"  width="8">季度</th>
//    			<th field="comCode" width="15">机构代码</th>
//    			<th field="checkedCode" width="15">校验代码</th>
//    			<th field="temp" width="50">校验公式</th>
//    			<th field="errorInfo" idth="50">错误信息</th>
//    	</tr>
//    	</thead>
//    </table>
//</body>
//<script  type="text/javascript">
///* //校验
//function check(){
//       var row = $('#dg').datagrid('getSelected');
//       if (row){
//       	$('#dlg').dialog('open').dialog('setTitle','修改信息'); 
//       	$('#fm').form('load',row);
//       	var  array = row.outItemCode+'&&checkedCode='+row.checkedCode+'&&month='+row.year+'&&quarter='+row.quarter+'&&reporttype='+row.reporttype
//       	url = '${pageContext.request.contextPath}/dataCheck/check.do?outItemCode='+array;
//       }
//   }  */
//   
////查找校验错误信息
//function search(){
//	var  year=document.getElementById("year").value;
//	var  quarter=$("#quarter").combobox('getValue')
//	if(year==""||quarter==""){
//		alert("带*为必填项")
//	}else{
//	 var params = {};
//	$('#serachFrom').find('input').each(function(){
//        var obj = $(this);
//        var name = obj.attr('name');
//        if(name){
//            params[name] = obj.val();
//            
//        }
//    }); 
//	$('#dg').datagrid("load", params);
//	}		
//	
//}
// $(function (){
//	$('#dg').datagrid({
//		//加载成功
//		onLoadSuccess: function(data){
//			if(data.total>0)return;
//			$('#dg').datagrid('appendRow',{
//				roleName: '没有相关记录'
//			});
//		}
//	}); 
//	//自定义验证
//	//条件：,required:true,novalidate:false(默认false)
//	$.extend($.fn.validatebox.defaults.rules, {    
//        roleCode: {    
//            validator: function(value){    
//                var flag = false;  
//                console.log($(this).val().length);
//                  return flag;  
//            },  
//           message: '您输入的用户名已存在，请更换。'    
//        }
//	})
//});
//
// 
///* 
//    
// function save(){
//	  	$('#fm').form(	'submit',{
//	  		url: url,
//	  		onSubmit: function(){
//	  			return $(this).form('validate');
//	  		},
//	  		success: function(result){
//	  			var result = eval('('+result+')');
//	  			if (result.errorMsg){
//	  				$.messager.show({
//	  					title: 'Error',
//	  					msg: result.errorMsg
//	  				});
//	  			} else {
//	  				$('#dlg').dialog('close');		// close the dialog
//	  				//$('#dg').datagrid('reload');	// reload the user data
//	  			}
//	  		}
//	  	});
// } */
//
//</script>
//</html>