 <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>
<%@include file="/commons/statics.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>精算数据查询</title>
</head>
<%	String path = request.getContextPath();
	String basepath=request.getContextPath()+ request.getServletPath().substring(0,request.getServletPath().lastIndexOf("/") + 1); %>
<body>
	<!-- 操作部分 -->
	<div class="easyui-panel" style="padding: 10px 0px 10px 0px;">
		<form  id="serachFrom">
		
		<table cellpadding="5"  width="100%"  >		
	    		<tr>
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

	    <table id="dg"  class="easyui-datagrid" style="width:100%;"
	        url=${pageContext.request.contextPath}/cux_sino/list.do
			toolbar="#toolbar" 
			rownumbers="true" 
			fitColumns="true"
			singleSelect="true" 
			pagination="true" 
			striped="true"
			nowrap="true"
			striped='true'
			loadMsg="加载数据中..." >
    	<thead>
    		<tr> 
    		    <th field="ck"     checkbox="true"></th> 
    		    <th field="period"          width="50"  data-options="sortable:true">会计月度</th>
    		    <th field="account_code"    width="40"  data-options="sortable:true">科目</th>
    		    <th field="account_name"    width="100" data-options="sortable:true">科目明细</th>
    			<th field="product_code"    width="30"  data-options="sortable:true">险种代码</th>
    			<th field="product_name"    width="50"  data-options="sortable:true">险种名称</th>
    			<th field="period_balance"  width="50"  data-options="sortable:true" align="right" formatter="jsutil.tool.formatCurrency">余额</th>
    	</tr>
    	</thead>
    </table>
    <div id="toolbar">
    	<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="edit()">修改</a>
    </div>
    <div id="dlg" class="easyui-dialog" style="width: 600px; padding: 10px 20px" align="center"  data-options="modal:true,closed:true,resizable:true,collapsible:true,minimizable:true,maximizable:true"  buttons="#dlg-buttons">
    	<form id="fm" method="post">
    		<table cellpadding="5" >
	    		<tr>
	    			<td><label>表名:</label></td>
	    			<td>
						 <input class="easyui-textbox" type="text"  id="codeName" name="codeName" class="easyui-validatebox"data-options="prompt:'报告类型'"  readonly="readonly">
					</td>
					
					<td><label>会计月度:</label></td>
	    			<td>
						<input class="easyui-textbox" type="text"  id="reportItemName" name="reportItemName" class="easyui-validatebox textbox" data-options="prompt:'因子名称'" readonly="readonly">
					</td>
	    		</tr>
	    		<tr>
					<td><label>起始日期:</label></td>
	    			<td>
						<input  type="text"  name="reportItemValue" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2"   readonly="readonly">
					</td>					
					<td><label>截止日期：</label></td>
	    			<td>
						<input  type="text"  name="reportItemValue_after" class="easyui-numberbox" data-options="prompt:'数值型值',precision:2" >
					</td>
	    		</tr>	    		
	    	</table>
    		<input  type="hidden"  name="token"  value="${token}"/>
    		<input  type="hidden"  name="userCode"  value="${currentUser.userCode}"/>
    	</form>
    <div id="dlg-buttons">
    	<a href="#" class="easyui-linkbutton" iconCls="icon-ok"     onclick="save()">保存</a>
    	<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">取消</a>
    </div>
  </div>
</body>
<script  type="text/javascript">
function serach(){
	var params = {};
	$('#serachFrom').find('input').each(function(){
        var obj = $(this);
        var name = obj.attr('name');
        if(name){
        	params[name] = obj.val();
        }
    }) 
	  $('#dg').datagrid('load',params);
      $('#dg').datagrid({
			onLoadSuccess:function(params){
				if(params.total>0) return  false;
				$("#dg").datagrid('appendRow',{period:'没有相关记录'});
				}
		} );
	  
	}

 function reset(){
	 $('#serachFrom').form('clear')
	
	
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
//修改
  function edit(){
         var row = $('#dg').datagrid('getSelected');
         if (row){
         	$('#dlg').dialog('open').dialog('setTitle','修改信息'); // OUTITEMCODE, COMCODE, MONTH, QUARTER, REPORTRATE
         	$('#fm').form('load',row);
         	var  array=row.outItemCode+'&comCode='+row.comCode+'&month='+row.month+'&quarter='+row.quarter+'&reportRate='+row.reportRate
         	url = '${pageContext.request.contextPath}/indexCode/update.do?outItemCode='+array;
         }
     } 
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
   				
   				$('#dg').datagrid('reload');
   				$('#dlg').dialog('close');	
   				jsutil.msg.alert("修改成功");
   			}
   		}
   	});
    }
</script>


</html>