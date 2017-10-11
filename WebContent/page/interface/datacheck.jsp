<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>
<%@include file="/commons/statics.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>接口数据校验</title>

<script  type="text/javascript">
$('#year').combobox({
    disabled:false,
    prompt:'年度',
    valueField:'value',
    textField:'text',
    url:'${contextPath}/json/year.json'
});
$(function(){
	
	$('#source').combobox('setValue','');
	/* var date = new Date();
	var year = date.getFullYear();
	var opt = '';
	for(var i = year; i >= year - 10; i--){
		//alert(i);
		opt += '<option value="' + i + '">' + i + '</option>';
	}
	$('#year').html(opt); */
});
//重置
function reset(){
	$('#searchForm').form('clear');
};
function dataCheck(){
	$('#searchForm').form('submit',{
		url: '${pageContext.request.contextPath}/cux_sino/datacheck.do',
		onSubmit: function(){
			return checkinput();
						
		},
		
		success:function(result){
			$('#dlg').dialog('close');
			var result = eval('('+result+')');
			if (!result.success){//没有错误信息
				jsutil.msg.alert('数据校验失败');
			}else {
				var data = $('#searchForm').serialize();
				//alert(data);
				$('#datagrid').datagrid({
					url: '${pageContext.request.contextPath}/cux_sino/checkresult.do?' + data
				});
				jsutil.msg.alert('数据校验成功');
			} 
		}
		});
} 
function checkinput(){
	var year = $("#year").val().trim();
	//if(!/\s*\d{4}\s*/.test(year)) {
	//	jsutil.msg.alert('年度必须是4个数字！');
	//	return false;
	//}
	
	if(!$('#searchForm').form('validate')){
		return false;
	}
	
	$('#msg').text('正在检验，请稍后.');
	$('#dlg').dialog('open').dialog('setTitle','正在检验');
	return true;
}

var loading
function onOpen(){
	 loading=setInterval(showalert, 500); 
}

var i=2;
function showalert() 
{ 
		var text="";
		if(i==1){
			text='正在检验，请稍后.';
		}else if(i==2){
			text='正在检验，请稍后..';
		}else if(i==3){
			text='正在检验，请稍后...';
			i=0;
		}
		i++;
		$('#msg').text(text);
} 

function onClose(){
	clearInterval(loading); 
}

function search1(){
	
	if(!$('#searchForm').form('validate')){
		return false;
	}
	
	var data = $('#searchForm').serialize();
	//alert(data);
	$('#datagrid').datagrid({
		url: '${pageContext.request.contextPath}/cux_sino/checkresult.do?' + data,
		onDblClickRow: function(index,row){
			//alert(index);
			$('#detail').dialog('open');
			$('#errinfo').val(row.errinfo);
			$('#remark').val(row.remark);
		}
	});
}

function formatterdate(val, row) {
	if (val != null) {
	var date = new Date(val);
	return date.getFullYear() + '-' + formatterVal((date.getMonth() + 1)) + '-'
	+ formatterVal(date.getDate());
	}
}

function formatterdatetime(val, row) {
	if (val != null) {
	var date = new Date(val);
	
	return date.getFullYear() + '-' + formatterVal((date.getMonth() + 1)) + '-'
	+ date.getDate() + ' ' + formatterVal(date.getHours()) + ':' + formatterVal(date.getMinutes()) + ':' + formatterVal(date.getSeconds());
	}
}

function formatterVal(val){
	var _val = '';
	if(val < 10){
		_val = '0' + val;
	}else{
		_val = '' + val;
	}
	return _val;
}

function checkflag(val,row){
	if(val == '1'){ 
		return '<span style="color:yellow">警告</span>'; 
	}else{
		return '<span style="color:red">错误</span>';
	}
}
</script>
</head>
<%	String path = request.getContextPath();
	String basepath=request.getContextPath()+ request.getServletPath().substring(0,request.getServletPath().lastIndexOf("/") + 1); %>
<body>

		<div class="easyui-panel" style="width: auto;padding: 10px;">
		<form id="searchForm" method="post">
		<table cellpadding="5">
	    		<tr>
					<td style="width: 80px; text-align: right; padding-right: 40px;">
						<label>年&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;度:</label>
					</td>
					<td style="width: 300px;">
					<!-- 修改年度为下拉
						<input id="year" name="year" class="easyui-textbox" validType="length[4,4]" data-options="prompt: '年    度',required:true,
					        missingMessage:'请添加年度'"  style="width: 200px;" maxlength="4">-->
		    				<input style="width: 100%;" id="year" name="year" class="easyui-textbox" data-options="  editable:false , disabled:false , missingMessage:'请输入年份' "/>
					</td>
					<td style="width: 80px; text-align: right; padding-right: 40px;">
						<label>季&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;度:</label>
					</td>
					<td>
						<input class="easyui-combobox" editable="false" id="quarter" name="quarter" style="width: 200px;"
						    data-options="
						        prompt: '季    度',	
						        required:true,
						        missingMessage:'请添加季度',		        
							    url: '<%=path%>/codeSelect.do?type=quarter',
							    method: 'get',
							    valueField:'value',
							    textField:'text',
							    groupField:'group'
						    ">
					 </td>
				</tr>
	    		<tr>
					<td style="width: 80px; text-align: right; padding-right: 40px;">
						<label>会计期间:</label>
					</td>
					<td>
						<input name="yearmonth" id="yearmonth" class="easyui-textbox" validType="length[4,6]"
						data-options="prompt: '会计期间',required:true,missingMessage:'请添加季度'," style="width: 200px;">
					</td>
					<td style="width: 80px; text-align: right; padding-right: 40px;">
						<label>日&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;期:</label>
					</td>
					<td><input id="datadate" name="datadate" class="easyui-datebox"
						data-options="prompt: '取数日期',editable:false,required:true,missingMessage:'请添加季度',"
						style="width: 200px;"></td>
				</tr>
	    		<tr>
	    			
	    			<td style="width: 80px; text-align: right; padding-right: 40px;">数据来源:</td>
	    			<td>
	    				<select class="easyui-combobox" name="source" id="source" data-options="prompt: '数据来源',editable:false,required:true,
					        missingMessage:'请选择数据来源'" style="width: 200px;">
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
	    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="dataCheck()"  data-options="iconCls:'icon-print'" style="width:80px;">数据校验</a>
	    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="search1()"  data-options="iconCls:'icon-print'" style="width:110px;">校验结果查询</a>
	    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="reset()" data-options="iconCls:'icon-reload'" style="width:80px;">重置</a>
					</td>
	    		</tr>
	    	</table>
	</form>	
</div>
<!-- 展示部分 -->
<table id="datagrid"  class="easyui-datagrid" style="width:100%;height:300px;"
			rownumbers="true" 
			fitColumns="true"
			singleSelect="true" 
			pagination="true" 
			striped="true"
			nowrap="true"
			striped='true'
			loadMsg="加载数据中..."
			 >
    	<thead>
    		<tr> 
    		    <th field="ck"    checkbox="true"></th> 
    		    <th field="id" width="6" hidden>id</th>
    		    <th field="year"          width="80" >年度</th>
    		    <th field="quarter"    width="80"  >季度</th>
    		    <th field="yearmonth"    width="120" >会计期间</th>
    			<th field="datadate"    width="150"  data-options="formatter:formatterdate">数据日期</th>
    			<th field="flag"  width="120"  data-options="formatter:checkflag">校验状态</th>
    			<th field="errinfo"  width="500" >校验信息</th>
    			<th field="remark"    width="500" >校验公式</th>
    			<th field="checktime"  width="150"  data-options="formatter:formatterdatetime">校验时间</th>
    			<th field="source"  width="120" data-options="formatter:function(val,row){if(val == '1'){ return '投资部'; }else{return '财务部';}}">数据来源</th>
    		</tr>
    	</thead>
    	<thead>
    	</thead>
    </table>
	<div id="dlg" class="easyui-dialog"
		style="width: 300px; height:100px; padding: 10px 20px" closed="true" align="center" 
		data-options="modal:true,closed:true,onOpen:onOpen,onClose:onClose">
		<div id="msg"></div>
    </div>
    
    <div id="detail" class="easyui-dialog" title="详细信息"
		style="width: 500px; height:280px; padding: 10px 10px" closed="true" align="center"  buttons="#dlg-buttons"
		data-options="modal:true,closable:false,resizable:true">
		<table>
			<tr>
				<td>校验信息:</td>
				<td>
					<textarea rows="4" cols="60" id="errinfo"></textarea>
				</td>
				
			</tr>
			<tr>
				<td>校验公式:</td>
				<td>
					<textarea rows="8" cols="60" id="remark"></textarea>
				</td>
			</tr>
		</table>
	<div id="dlg-buttons">
    	<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#detail').dialog('close')">关闭</a>
    </div>
    </div>
 
</body>
</html>