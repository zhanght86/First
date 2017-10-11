<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>投资接口数据上传</title>
<meta http-equiv="Content-type" content="text/html;charset=UTF-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<%@include file="/commons/statics.jsp"%>
<link rel="stylesheet" type="text/css" href="<c:url value='/js/easyui/themes/color.css' />">
<script type="text/javascript" src="/js/validateyear.js"></script>
</head>

<div style="border:1px solid #95B8E7;width: auto;padding: 10px;">
		<form id="serachFrom" method="post">
			<table style="width: 100%;">
	    		<tr>
	    		    <td style="width:10%;" >
	    				<label>年&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;度:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input  id="yearmonth" name="yearmonth" class="easyui-textbox" validType="yearValidation['^[2][0-1][0-9][0-9]$','请输入合法的年份，例如：2016']" data-options="prompt: '年度'"  style="width:60%;">
	    			</td>
	    			
	    			<td style="width:10%;">
	    				<label>季&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;度:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input  id="quarter"   name="quarter"  class="easyui-combobox"  style="width:60%;"  
	    				data-options="
	    				prompt: '季度', 
	    				url:'<%=path%>/codeSelect.do?type=quarter',
	    				method: 'get',
						valueField:'value',
						editable:false,
						textField:'text',
						
						groupField:'group'" >
	    			</td>
	    			
	    			<td style="width:40%;" >
	    			    <input  type="hidden" style="width:60%;">
	    			</td>
	    		</tr>	    

	    		<tr>
	    			<td style="width:10%;">
	    				<label>表名选择:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input  id="investTable"   name="investTable"  class="easyui-combobox"  style="width:60%;"  
	    				data-options="
	    				prompt: '表名选择', 
	    				url:'<%=path%>/codeSelect.do?type=abc_investCode',
	    				method: 'get',
						valueField:'value',
						editable:false,
						textField:'text',
						required:true,
						groupField:'group'" ><font color="red">*</font>
	    			</td>
	    		   <td style="width:10%;" >
	    				<label>报&nbsp;&nbsp;送&nbsp;&nbsp;号:</label>
	    			</td>
	    			<td style="width:30%;">
	    				<input  id="reportId"   name="reportId"  class="easyui-combobox"  style="width:60%;"  
	    				data-options="
	    				prompt: '报送号选择', 
	    				url:'<%=path%>/codeSelect.do?type=invest_reportID',
	    				method: 'get',
						valueField:'value',
						editable:false,
						textField:'text',
						groupField:'group'" >
	    			</td>
	    		   
	    		   <td style="width:40%;" >
	    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="serach()"  data-options="iconCls:'icon-search'" style="width:80px;">查询</a>
					</td>	    			    			   			  			
	    		</tr>
	    			    		
	    	</table>
	</form>	
</div>

<!-- 展示部分CR01-政策性金融债-->
<div id="relation1">
	    <table id="dg1"  class="easyui-datagrid" style="width:100%;"
	        url=${pageContext.request.contextPath}/abc_invest/invest_list.do
			toolbar="#toolbar1" 
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
			style="width:100%;height:auto;"
			loadMsg="加载数据中..." 
			>
			
      	<thead>
    		<tr>      		    
     			<th field="year" width="5">年度</th>
    			<th field="quarter"  width="5">季度</th>  	
    			<th field="itemCode" width="10">证券代码</th>		
    			<th field="itemName" width="10">证券简称</th>  			    			
    			<th field="recognitionValue" width="10">净价市值</th> 	
    			<th field="amount" width="10">市价修正久期</th>
    			<th field="creditRate" width="10">发行人评级</th>   					
    	    </tr>
    	</thead>
    </table>
</div>

<!-- 展示部分CR01-其他债券-->
<div id="relation2">
	    <table id="dg2"  class="easyui-datagrid" style="width:100%;"
	        url=${pageContext.request.contextPath}/abc_invest/invest_list.do
			toolbar="#toolbar2" 
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
			style="width:100%;height:auto;"
			loadMsg="加载数据中..." 
			>
      	<thead>

    		<tr>      		    
     			<th field="year" width="5">年度</th>
    			<th field="quarter"  width="5">季度</th>  
    				
    			<th field="itemName" width="10">证券简称</th> 
    			<th field="itemCode" width="10">证券代码</th>		   			 			    			
    			<th field="recognitionValue" width="10">净价市值</th> 	
    			<th field="amount" width="10">市价修正久期</th>
    			<th field="creditRate" width="10">发行人评级</th> 
    			<th field="externalRate" width="10">债券外部评级</th>   					
    	    </tr>
    	</thead>
    </table>
</div>

<!-- 展示部分CR05-企业债-->
<div id="relation3">
	    <table id="dg3"  class="easyui-datagrid" style="width:100%;"
	        url=${pageContext.request.contextPath}/abc_invest/invest_list.do
			toolbar="#toolbar3" 
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
			style="width:100%;height:auto;"
			loadMsg="加载数据中..." 
			>
      	<thead>

    		<tr>      		    
     			<th field="year" width="5">年度</th>
    			<th field="quarter"  width="5">季度</th>  
    				
    			<th field="itemName" width="15">证券简称</th> 	   			 			    			
    			<th field="recognitionValue" width="10">净价市值</th> 
    			<th field="interst" width="10">应收利息</th>     				
    			<th field="amount" width="10">剩余年限</th>   			
    			<th field="creditRate" width="10">债券外部评级</th>  					
    	    </tr>
    	</thead>
    </table>
</div>

<!-- 展示部分CR10-保险资管产品-->
<div id="relation4">
	    <table id="dg4"  class="easyui-datagrid" style="width:100%;"
	        url=${pageContext.request.contextPath}/abc_invest/invest_list.do
			toolbar="#toolbar4" 
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
			style="width:100%;height:auto;"
			loadMsg="加载数据中..." 
			>
      	<thead>

    		<tr>      		    
     			<th field="year" width="5">年度</th>
    			<th field="quarter"  width="5">季度</th>  
    				
    			<th field="creditRate" width="10">资产的信用评级</th>  
    			<th field="itemName" width="10">证券简称</th>	 			    			
    			<th field="recognitionValue" width="10">净价市值</th> 
    			<th field="interst" width="10">应收利息</th>     				    			 			
    			<th field="issuer" width="10">发行人</th>					
    	    </tr>
    	</thead>
    </table>
</div>

<!-- 展示部分MR09-股票-->
<div id="relation5">
	    <table id="dg5"  class="easyui-datagrid" style="width:100%;"
	        url=${pageContext.request.contextPath}/abc_invest/invest_list.do
			toolbar="#toolbar5" 
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
			style="width:100%;height:auto;"
			loadMsg="加载数据中..." 
			>
      	<thead>

    		<tr>      		    
     			<th field="year"  width="5">年度</th>
    			<th field="quarter"  width="5">季度</th>  
    				
    			<th field="itemCode" width="10">证券代码</th>		
    			<th field="itemName" width="10">证券名称</th>     				
    			<th field="amount" width="10">占流通股本比例%</th>
    			<th field="recognitionValue" width="10">市值</th> 
    			<th field="huShenValue" width="10">沪深300权重</th>
    			<th field="purchaseCost" width="10">累计成本</th>				
    	    </tr>
    	</thead>
    </table>
</div>
    
<!-- 展示部分MR12-股票-->
<div id="relation6">
	    <table id="dg6"  class="easyui-datagrid" style="width:100%;"
	        url=${pageContext.request.contextPath}/abc_invest/invest_list.do
			toolbar="#toolbar6" 
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
			style="width:100%;height:auto;"
			loadMsg="加载数据中..." 
			>
      	<thead>

    		<tr>      		    
     			<th field="year" width="5">年度</th>
    			<th field="quarter"  width="5">季度</th>  
    			
    			<th field="fundTypeA" width="10">基金类别</th>
    			<th field="issuer" width="10">发行方</th>	
    			<th field="itemCode" width="10">基金代码</th>		
    			<th field="itemName" width="15">基金名称</th>   
    			<th field="fundTypeB" width="10">基金类别B</th>  
    			<th field="amount" width="10">持有数量</th>
    			<th field="recognitionValue" width="10">市值</th> 
    			<th field="purchaseCost" width="10">累计成本</th>				
    	    </tr>
    	</thead>
    </table>
</div>

</body>
<script  type="text/javascript">

$(function(){
	$('#relation1').hide();
	$('#relation2').hide();
	$('#relation3').hide();
	$('#relation4').hide();
	$('#relation5').hide();
	$('#relation6').hide();
});

var params = {};

//查找
function serach(){
	$('#serachFrom').find('input').each(function(){
       var obj = $(this);
       var name = obj.attr('name');
       if(name){
           params[name] = obj.val();
           if(params[name] == 1){
           	$('#relation1').show();
           	$('#relation2').hide();
           	$('#relation3').hide();
           	$('#relation4').hide();          	
           	$('#relation5').hide();
        	$('#relation6').hide();
           	
           } else if(params[name] == 2) {
           	$('#relation1').hide();
           	$('#relation2').show();
           	$('#relation3').hide();
           	$('#relation4').hide();
           	$('#relation5').hide();
        	$('#relation6').hide();
           	
           }else if(params[name] == 3) {
           	$('#relation1').hide();
           	$('#relation2').hide();
           	$('#relation3').show();
           	$('#relation4').hide();
           	$('#relation5').hide();
        	$('#relation6').hide();
           	
           }else if(params[name] == 4) {
           	$('#relation1').hide();
           	$('#relation2').hide();
           	$('#relation3').hide();
           	$('#relation4').show();
           	$('#relation5').hide();
        	$('#relation6').hide();
           } else if(params[name] == 5) {
              	$('#relation1').hide();
               	$('#relation2').hide();
               	$('#relation3').hide();
               	$('#relation4').hide();
               	$('#relation5').show();
            	$('#relation6').hide();
          } else if(params[name] == 6) {
             	$('#relation1').hide();
               	$('#relation2').hide();
               	$('#relation3').hide();
               	$('#relation4').hide();
               	$('#relation5').hide();
            	$('#relation6').show();
          }
       }
   }); 

	$('#dg'+params.investTable).datagrid("load", params);
	
	$('#dg'+params.investTable).datagrid({
		//加载成功
		onLoadSuccess: function(data){
			if (data.total == 0){
				jsutil.msg.alert('没有符合条件的查询结果');
			} 			
		}
	
	}); 
}


</script>
</html>