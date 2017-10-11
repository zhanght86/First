<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>
<%@include file="/commons/statics.jsp"%>
<%	String path = request.getContextPath();
	String basepath=request.getContextPath()+ request.getServletPath().substring(0,request.getServletPath().lastIndexOf("/") + 1); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="/js/validateyear.js"></script>
<title>固定因子修改轨迹查询</title>
<style type="text/css">
.datagrid-header-row td {
	height: 40px;
	font-weight: bold;
}
</style>
</head>

<body>
	<!-- 操作部分 -->
	<div class="easyui-panel" style="height: auto; width:100%; padding: 8px 0px 0px 0px;overflow: hidden;">
		<form  id="serachFrom" style="margin-bottom:0;">		
			<table cellpadding="5" border='0' style="width: 100%; overflow: hidden;table-layout: fixed;">		
	    		<tr>
	    			<td style="width:8%;text-align:right;" >
	    				<label>报告类型：</label>
	    			</td>
	    			<td style="width:13%;">
	    				<input class="easyui-combobox"  name="report_rate" style="width:100%" data-options="url: '<%=path%>/codeSelect.do?type=reporttype',prompt:'报表类型', editable:false,method: 'get',valueField:'value',textField:'text' ">	 
	    			</td>
	    			<td style="width:8%;text-align: right;" >
	    				<label>报告名称：</label>
	    			</td>
	    			<td style="width:13%;">
	    				<input  class="easyui-combobox"  name="report_name" style="width: 100%;" data-options="prompt: '报表名称',editable:false,url:'<%=path%>/codeSelect.do?type=reportname',method: 'get',valueField:'value',textField:'text' ">
	    			</td>
	    			<td style="width:8%;text-align: right;" >
	    				<label>年度：</label>
	    			</td>
	    			<td style="width:13%;">
	    				<!-- <input  id="month" name="month" class="easyui-textbox" data-options="prompt: '年度'"  validType="yearValidation['^[2][0-1][0-9][0-9]$','请输入合法的年份，例如：2016']" style="width:100%;"> -->
	    				<input style="width: 100%;" id="month" name="month" class="easyui-textbox" data-options="  editable:false , disabled:false , missingMessage:'请输入年份' "/>
	    			</td>
	    			<td style="width:8%;text-align: right;"  >
	    				<label>季度：</label>
	    			</td>
	    			<td style="width:13%;">
	    				<input  id="quarter"   name="quarter"  class="easyui-combobox"  style="width: 100%;" data-options="prompt: '季度', editable:false,url:'<%=path%>/codeSelect.do?type=quarter',method: 'get',valueField:'value',textField:'text',groupField:'group'" >
	    			</td>	    			
	    		</tr>
	    		<tr>
	    			<td style="width:8%;text-align:right;" >
	    				<label>因子代码：</label>
	    			</td>
	    			<td style="width:13%;">
	    				<input name="report_code"  class="easyui-textbox" data-options="prompt: '因子代码'" style="width: 100%;">
	    			</td>
	    		</tr>
	    		<tr>
	    			<td colspan="8" style="text-align: right; padding-right: 20px" >
						<a href="javascript:void(0)" class="easyui-linkbutton" onclick="serach()"  data-options="iconCls:'icon-search'" style="width:8%;">查询</a>
	    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="reset()"   data-options="iconCls:'icon-reload'" style="width:8%;">重置</a>
					</td>
	    		</tr>
	    	</table>
		
	</form>	
</div>  
<!-- 展示部分 -->
	 <div class="easyui-panel" style="border: 0;width: 100%">
	    <table id="dg"  class="easyui-datagrid" style="width:100%;"
			toolbar="#toolbar" 
			rownumbers="true" 
			fitColumns="false"
			singleSelect="true" 
			pagination="true" 
			sortName="oper_date"
			sortOrder="desc"
			striped="true"	
			nowrap="false"
			style="table-layout:fixed;word-break:break-all;width:100%;height:auto;"	
			loadMsg="加载数据中..." >
    	<thead>
    		<tr>  
    		    <th field="report_rate"    align="center" halign="center" width="6%"   data-options="sortable:true">报告类型</th>
    		    <th field="report_code"    align="center" halign="center" width="12%"   data-options="sortable:true">因子代码</th>
    		    <th field="report_name"    align="left" halign="center"  width="20%"  data-options="sortable:true">因子名称</th>
    			<th field="report_mvalue"   align="center" halign="center" width="10%"  data-options="sortable:true,precision:2" class="easyui-numberbox" formatter="jsutil.tool.formatCurrency" align="right">数值型值</th>
    			<th field="report_mvalue_after" width="10%"  align="center" halign="center"  data-options="sortable:true,precision:2" class="easyui-numberbox" formatter="jsutil.tool.formatCurrency" align="right">数值型值（修改后）</th>
    			<th field="text_value"       align="center" halign="center"  width="10%"  data-options="sortable:true">文本型值</th>
    			<th field="text_value_after"  align="center" halign="center" width="10%"  data-options="sortable:true">文本型值（修改后）</th>
    			<th field="oper_date"        align="center" halign="center" width="12%"  data-options="sortable:true">修改日期</th>
    			<th field="user_name"     align="center" halign="center" width="8%"  data-options="sortable:true" align="center">修改人</th>
    	    </tr>
    	</thead>
    </table>
 </div>
</body>
<script  type="text/javascript">
$(function(){
	$('#month').combobox({
	    disabled:false,
	    prompt:'年度',
	    valueField:'value',
	    textField:'text',
	    url:'${contextPath}/json/year.json'
	});
	serach();
});
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
	//$('#dg').datagrid("load", params);
   	$("#dg").datagrid({
   	    url:'${pageContext.request.contextPath}/indexCode/history.do',
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
	}	
	



// 重置
function reset(){
	$('#serachFrom').form("clear");
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