 <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>
<%@include file="/commons/statics.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>财务数据接口</title>
</head>
<%	String path = request.getContextPath();
	String basepath=request.getContextPath()+ request.getServletPath().substring(0,request.getServletPath().lastIndexOf("/") + 1); %>
<body>
	<!-- 操作部分 -->
	<div class="easyui-panel" style="padding: 10px 0px 10px 0px;">
		<form  id="serachFrom">
		
		<table cellpadding="6"  width="100%"  >		
	    		<tr>
					<td style="width: 80px; text-align: right; padding-right: 40px;">
						<label>年&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;度:</label>
					</td>
					<td style="width: 300px;">
					     <input class="easyui-textbox" editable="false" id="year" name="year" style="width: 60%;" data-options="prompt: '年    度'" readonly> 
					</td>
					<td style="width: 80px; text-align: right; padding-right: 40px;">
						<label>季&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;度:</label>
					</td>
					<td>
						<input class="easyui-textbox" editable="false" id="quartername" name="quartername" style="width: 200px;" data-options="prompt: '季    度'" readonly>
						<input class="hidden"  id="quarter" name="quarter" style="display: none;">
					 </td>
				</tr>
	    		<tr>
					<td style="width: 80px; text-align: right; padding-right: 40px;">
						<label>会计期间:</label>
					</td>
					<td>
						<input name="yearmonth" id="yearmonth" class="easyui-textbox"  data-options="prompt: '会计期间'" style="width: 200px;" readonly>
					</td>
					<td style="width: 80px; text-align: right; padding-right: 40px;">数据来源:</td>
	    			<td>
	    				<select class="easyui-combobox" name="source" id="source" data-options="prompt: '数据来源',editable:false,required:true,
					        missingMessage:'请选择数据来源'" style="width: 200px;">
					        <option value=""></option>
					        <option value="1">投资部</option>
					        <option value="2">财务部</option>
					    </select>
	    			</td>
				</tr>
	    		<tr>
	    			<td style="width:80px;text-align: right;padding-right: 40px;" ></td>
	    			<td style="width:300px;text-align: right;padding-right: 40px;" ></td>
	    			<td style="width:80px;text-align: right;padding-right: 40px;" ></td>
	    			<td colspan="3" style="width:400px;text-align: right;padding-right: 40px;" >
	    				<a href="javascript:search();" class="easyui-linkbutton"   data-options="iconCls:'icon-print'" style="width:80px;">科目校验</a>
					</td>
	    		</tr>
	    </table>
		
	</form>	
</div>  <!-- 展示部分 -->    
	    <table id="dg"  class="easyui-datagrid" style="width:100%;"
	        url=${pageContext.request.contextPath}/newitemcheck/list.do
			toolbar="#toolbar" 
			rownumbers="true" 
			fitColumns="true"
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
    			<th field="ck"    checkbox="true"></th>
    			<th field="source"   width="150px"  data-options="sortable:true">数据来源</th>
    			<th field="itemcode"   width="150px"  data-options="sortable:true">科目代码</th>
    			<th field="itemname"   width="150px"  data-options="sortable:true">科目名称</th>
    			<th field="checkinfo"  width="70px"  data-options="sortable:true">校验信息</th>
        	</tr>
    	</thead>
    </table>    
</body>
<script  type="text/javascript">
$(function(){
	
	var date= new Date();
	date.getFullYear();
	$("#year").textbox('setValue',date.getFullYear());
	console.log(date.getMonth());
	if(date.getMonth()<3){
		$("#yearmonth").textbox('setValue',date.getFullYear()+"03");
		$("#quartername").textbox('setValue',"第一季度");
		$("#quarter").val("1");
	}else if(date.getMonth()>2&&date.getMonth()<6){
		console.log(1);
		$("#yearmonth").textbox('setValue',date.getFullYear()+"06");
		$("#quartername").textbox('setValue',"第二季度");
		$("#quarter").val("2");
	}else if(date.getMonth()>5&&date.getMonth()<9){
		$("#yearmonth").textbox('setValue',date.getFullYear()+"09");
		$("#quartername").textbox('setValue',"第三季度");
		$("#quarter").val("3");
	}else if(date.getMonth()>8){
		$("#yearmonth").textbox('setValue',date.getFullYear()+"12");
		$("#quartername").textbox('setValue',"第四季度");
		$("#quarter").val("4");
	}
	
	
});


function search(){
	var params = {};
	$('#serachFrom').find('input').each(function(){
        var obj = $(this);
        var name = obj.attr('name');
        if(name){
        	params[name] = obj.val();
        }
    }); 
	  $('#dg').datagrid('load',params);
      $('#dg').datagrid({
			onLoadSuccess:function(params){
				if(params.total>0) return  false;
				$("#dg").datagrid('appendRow',{period:'没有相关记录'});
				}
		});    	
}


 function reset(){
	 $('#serachFrom').form('clear');
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