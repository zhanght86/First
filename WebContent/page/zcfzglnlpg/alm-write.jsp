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
<title>数据录入</title>

</head>
<body>
	<%@include file="/commons/statics.jsp"%>
	<!-- 	操作，展示部分 -->
	<div>
		<table id="dg" class="easyui-datagrid"
			url=${pageContext.request.contextPath}/alm_reportdata/data.do
			title="数据录入" 
			toolbar="#toolbar" 
			striped="true"
			nowrap="false"
			autoRowHeight="true"
			style="width:100%;height:420px;"
			loadMsg="加载数据中..." 
			method="get"
			>
			<thead style="height:50px;">
				<tr>
					<th halign="center" align="center" field="ck"checkbox=true></th>
					<th field="itemNum" >序号</th>
					<th field="projectCode" width="120px">评估项目</th>
					<th field="itemName" width="450px">评估标准</th>
					<th field="temp"width="300px">备注</th> 
					<th field="integritySevResultName">制度健全性结果</th>
					<th field="integrityScore">制度健全性得分</th>
					<th align="center" data-options="field:'integrityFile',formatter:ingo" id="infilePath">制度健全性文件</th>					
					<th field="efficiencySevResultName">遵循有效性结果</th>
					<th field="efficiencyScore">遵循有效性得分</th>
					<th align="center" data-options="field:'efficiencyFile',formatter:efgo" id="effilePath">遵循有效性文件</th>
					<th field="subtotalScore">得分小计</th>
					<th field="auditorOpinion"width="100px">返回意见</th>
					<th field="sevBase"width="200px">评分依据</th>					
					<th field="sevStatus">评估状态</th>
				</tr>
				
			</thead>
		</table>
		</div>
		<div id="toolbar">
		<table cellspacing="0" cellpadding="0">
		        <tr>
		            <td>
		                <a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editManage()">填写</a>
	    			</td>
		            <td>
		                <div class="datagrid-btn-separator"></div>
		            </td>
		            <td>
		               <a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="submit()">提交</a>
	    			</td>
    				<!-- <td>
		                <div class="datagrid-btn-separator"></div>
		            </td>
		            <td>
		               <a href="#" class="easyui-linkbutton" iconCls="icon-large-picture" plain="true" onclick="viewProcess()">查看自评估事件流程</a>
	    			</td>
    				<td>
		                <div class="datagrid-btn-separator"></div>
		            </td>
		            <td>
		             	<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="modeldownload()">模板导出</a>
	    			</td> -->						    			
		        </tr>
		    </table>
	    	</div>
	    <!--  新增弹框界面-->
	<div id="dlg" class="easyui-dialog" style="width: 600px; padding: 10px 20px" align="center"  data-options="modal:true,closed:true,resizable:false,maximizable:true,closable:true"  buttons="#dlg-buttons">
	   	<form id="fm" method="post">
	   		<table cellpadding="5">	   			
	   			<input type="hidden" name="itemCode">
	   			<input type="hidden" name="reportId">	
	   			<input type="hidden" name="deptNo">	
	   			<input type="hidden" name="sysIntegrityScore">	
	   			<input type="hidden" name="folEfficiencyScore">	
	   			<input type="hidden" name="standardScore">	
	   			<tr>
	   				<td>制度健全性评分结果:</td>
	   				<td><input class="easyui-combobox" 
							   name="integritySevResult"
							   style="width:200px;" 
			                   data-options="
					                 url:'${pageContext.request.contextPath}/codeSelect.do?type=EvaluateResult',
					                 method:'get',
					                 valueField:'value',
					                 textField:'text',required:true,
					                 panelHeight:'auto'
			         "></td>
	   			</tr>
	   			<tr>
	   				<td>遵循有效性评分结果:</td>
	   				<td><input class="easyui-combobox" 
							   name="efficiencySevResult"
							   style="width:200px;" 
			                   data-options="
					                 url:'${pageContext.request.contextPath}/codeSelect.do?type=EvaluateResult',
					                 method:'get',
					                 valueField:'value',
					                 textField:'text',required:true,
					                 panelHeight:'auto'
			         "></td>
	   			</tr>
	   			<tr>
	   				<td>评估依据:</td>
	   				<td><textarea name="sevBase" class="easyui-validatebox" style="width: 200px; height: 60px;"></textarea></td>
	   			</tr>
	   			<tr>
	   				<td>制度健全性文件:</td>
	   				<td><a href="#" class="easyui-linkbutton" plain="true" onclick="makerUpload(false,1)" data-options="iconCls:'icon-arrows-up'"
							style="width: 80px;">文件上传</a></td>
	   			</tr>
	   			<tr>
	   				<td>遵循有效性文件:</td>
	   				<td><a href="#" class="easyui-linkbutton" plain="true" onclick="makerUpload(false,3)" data-options="iconCls:'icon-arrows-up'"
							style="width: 80px;">文件上传</a></td>
	   			</tr>
	   		</table>
	   	</form>
	    <div id="dlg-buttons">
    	<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveManage()">保存</a>
    	<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">取消</a>
    	</div>	
    </div>
    	</div>
	    <!--  新增弹框界面-->
	<div id="dlg2" class="easyui-dialog" style="width: 600px; padding: 10px 20px" align="center"  data-options="modal:true,closed:true,resizable:false,maximizable:true,closable:true"  buttons="#dlg2-buttons">
	   	<form id="fm2" method="post">
	   		<table cellpadding="5">	   			
	   			<input type="hidden" name="itemCode">
	   			<input type="hidden" name="reportId">	
	   			<input type="hidden" name="deptNo">	
	   			<input type="hidden" name="sysIntegrityScore">	
	   			<input type="hidden" name="folEfficiencyScore">	
	   			<input type="hidden" name="standardScore">	
	   			<tr>
	   				<td>制度健全性评分结果:</td>
	   				<td><input class="easyui-combobox" 
							   name="integritySevResult"
							   style="width:200px;" 
			                   data-options="
					                 url:'${pageContext.request.contextPath}/codeSelect.do?type=EvaluateResult2',
					                 method:'get',
					                 valueField:'value',
					                 textField:'text',required:true,
					                 panelHeight:'auto'
			         "></td>
	   			</tr>
	   			<tr>
	   				<td>遵循有效性评分结果:</td>
	   				<td><input class="easyui-combobox" 
							   name="efficiencySevResult"
							   style="width:200px;" 
			                   data-options="
					                 url:'${pageContext.request.contextPath}/codeSelect.do?type=EvaluateResult2',
					                 method:'get',
					                 valueField:'value',
					                 textField:'text',required:true,
					                 panelHeight:'auto'
			         "></td>
	   			</tr>
	   			<tr>
	   				<td>评估依据:</td>
	   				<td><textarea name="sevBase" class="easyui-validatebox" style="width: 200px; height: 60px;"></textarea></td>
	   			</tr>
	   			<tr>
	   				<td>制度健全性文件:</td>
	   				<td><a href="#" class="easyui-linkbutton" plain="true" onclick="makerUpload(false,1)" data-options="iconCls:'icon-arrows-up'"
							style="width: 80px;">文件上传</a></td>
	   			</tr>
	   			<tr>
	   				<td>遵循有效性文件:</td>
	   				<td><a href="#" class="easyui-linkbutton" plain="true" onclick="makerUpload(false,3)" data-options="iconCls:'icon-arrows-up'"
							style="width: 80px;">文件上传</a></td>
	   			</tr>
	   		</table>
	   	</form>
	    <div id="dlg2-buttons">
    	<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveManage2()">保存</a>
    	<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">取消</a>
    	</div>	说
    </div>
      <!-- 评估文件查看 -->
   <div id="dlg1" class="easyui-dialog" style="width: 800px; padding: 10px 20px" align="center"  data-options="modal:true,closed:true,resizable:true,maximizable:true"  buttons="#dlg1-buttons">
			<div >
			<table id="dg1" class="easyui-datagrid">
				<thead>
					<tr>
						
						<th field="fileName"  width="20%" halign="center">文件名称</th>
						<th field="uploadTime" width="20%" align="left" halign="center">上传日期</th>
						<th field="uploadUser"  width="20%" align="center" >操作人</th>	
						<th field="deptNo" width="20%" align="center" >操作人所属部门</th>	
						<th width="20%" data-options="field:'filePath',formatter:goo" id="filePath">下载查看</th>
					</tr>
				</thead>
			</table>	
			</div>
			<div id="toolbar1">
			<table cellspacing="0" cellpadding="0">
		        <tr>		         
		            <td>
		               <a href="#" class="easyui-linkbutton" iconCls="icon-cut" onclick="cut()">删除</a>
					</td>						    			
		        </tr>
		    </table>
			</div>
    </div>
    <!-- 流程图片 -->
  <div id="imagediv" class="easyui-dialog"
		style="width: 80%; height: 500px; padding: 1px" closed="true" align="center" 
		data-options="modal:true,closed:true,resizable:true,maximizable:true" >
<!-- 		<img id=image style="position: absolute;top: 50px;left: 50px;" src=""/>
 -->  </div>
</body>
	<script type="text/javascript">
	function ingo(value,row,index){
		//return '<a  href="${pageContext.request.contextPath}/page/evaluate/evaluate_file.jsp?reportId='+row.reportId+'&itemCode='+row.itemCode+'">查看</a>';
		return '<a  href="#" onclick="chakan('+index+',1)">查看</a>';
	}
	function efgo(value,row,index){
		//return '<a  href="${pageContext.request.contextPath}/page/evaluate/evaluate_file.jsp?reportId='+row.reportId+'&itemCode='+row.itemCode+'">查看</a>';
		return '<a  href="#" onclick="chakan('+index+',3)">查看</a>';
	}
	function  goo(val,row){
    	return '<a   href="${pageContext.request.contextPath}/alm_reportdata/downloadURD.do?id='+row.id+'" >下载</a>';
    };
	function chakan(index,number){
		var row = $('#dg').datagrid('getData').rows[index];
		$('#dlg1').dialog('open').dialog('setTitle','评分依据文件查看');
		$("#dg1").datagrid({
			url:'${pageContext.request.contextPath}/alm_reportdata/file.do?reportId='+row.reportId+'&itemCode='+row.itemCode+'&number='+number+'&deptNo='+row.deptNo,
			toolbar:'#toolbar1',
			fitColumns:true,
			singleSelect:true,
			striped:true,
			nowrap:false,
			method:'get',
			loadMsg:'加载数据中...',
		});
	}
	function cut(){
		var rows = $('#dg1').datagrid('getSelections');
		var idArr = new Array();
		for(var i=0;i<rows.length;i++){
			idArr[i] =rows[i].id;
		}
		var data =[{name:'ids',value:idArr.join(',') }];
		console.log(rows);	
		if(rows.length==0){$.messager.alert('消息提示','请选择要删除的评估文件!','error');return ;}
			$.messager.confirm('删除','确认删除选中评估文件吗?',function(r){
				if (r){
					$.post('${pageContext.request.contextPath}/alm_reportdata/cut.do',data,function(result){
						if (result.success){
							$('#dg1').datagrid('reload');
							$.messager.show({
								title:'提示',
								msg:'已经删除',
								showType:'show'
							});
						} else {
							$.messager.show({	// show error message
								title: 'Error',
								msg: result.errorMsg
							});
						}
					},'json');
				}
			});
	}
    function xiazai(){
    	//alert("下载");
    	var row = $('#dg1').datagrid('getSelected');
    	if(row.length==0){$.messager.alert('消息提示','请选择文件下载!','error');return ;}
		if(row.length>1){$.messager.alert('消息提示','请选择一个文件进行下载!','error');return ;}
    	$.get('${pageContext.request.contextPath}/alm_reportdata/downloadHTTP.do?id='+row.id,
				function(data){
					location.href=data;
    			}
    	);
    }
	var itemCode;
	var reportId;
	var deptNo;
	function makerUpload(chunk,number){
		var itemCode1 =itemCode;
		var reportId1 =reportId;
		var deptNo1 =deptNo;
		var _data = 'itemCode=' + itemCode1 + '&reportId=' + reportId1 +'&deptNo=' + deptNo1 +'&number=' + number ;
	 	
		Uploader(chunk,_data,itemCode1,reportId1,deptNo1,function(files){});
	}
	function Uploader(chunk, data, itemCode1,reportId1 ,deptNo1, callBack){
		var addWin = $('<div style="overflow: hidden;top:20px;"/>');
		var upladoer = $('<iframe/>');
		upladoer.attr({'src':'${pageContext.request.contextPath}/page/zcfzglnlpg/abc_investMultiupload.jsp?chunk2='+ chunk + '&' + data,width:'100%',height:'100%',frameborder:'0',scrolling:'no'});
		addWin.window({
			title:"上传文件",
			height:370,
			width:550,
			minimizable:false,
			modal:true,
			collapsible:false,
			maximizable:false,
			resizable:false,
			content:upladoer,
			onClose:function(){
				var fw = GetFrameWindow(upladoer[0]);
				var files = fw.files;
				$(this).window('destroy');
				callBack.call(this,files);
			},
			onOpen:function(){
				var target = $(this);
				setTimeout(function(){
					var fw = GetFrameWindow(upladoer[0]);
					fw.target = target;
				},100);
			}
		});
	}
	function GetFrameWindow(frame){
		 return frame && typeof(frame)=='object' && frame.tagName == 'IFRAME' && frame.contentWindow;
	}
	//填写自评估数据
    function editManage(){
	    var rows = $('#dg').datagrid('getSelections');
	    if(rows.length==0){$.messager.alert('消息提示','请选择要填写的明细项!','error');return ;}
	    if(rows.length>1){$.messager.alert('消息提示','请选择一条明细项进行填写!','error');return ;}
	    var row = $('#dg').datagrid('getSelected');
	    //如果为0则下拉框为五个情况 如果为3则只有完全符合和不符合两种情况
	    if(row.itemType==0){
	    	$('#dlg').dialog('open').dialog('setTitle','填写');
	 		$('#fm').form('load',row);
	 		itemCode=row.itemCode;
	 		reportId=row.reportId;
	 		deptNo=row.deptNo;
	 		url='${pageContext.request.contextPath}/alm_reportdata/edit.do';
	    }else{
	    	$('#dlg2').dialog('open').dialog('setTitle','填写');
	 		$('#fm2').form('load',row);
	 		itemCode=row.itemCode;
	 		reportId=row.reportId;
	 		deptNo=row.deptNo;
	 		url='${pageContext.request.contextPath}/alm_reportdata/edit.do';
	    }
	    //需要做一个判断只有是未填写和编辑中的才能进行编辑
//	    if(row.sevStatus=="es1"){
	    	
//	    }else{
//	    	$.messager.alert('提示',"状态下的不允许编辑",'warning');
//	    	
//	    }
		
	}
 	//保存编辑好的信息
    function saveManage(){
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
					$('#dlg').dialog('close');		// close the dialog
					$('#dg').datagrid('reload');	// reload the user data
				}
			}
		});
    }
  //保存编辑好的信息
    function saveManage2(){
		$('#fm2').form('submit',{
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
					$('#dlg2').dialog('close');		// close the dialog
					$('#dg').datagrid('reload');	// reload the user data
				}
			}
		});
    }
  	//提交自评估填写
	function submit(){
		var rows = $('#dg').datagrid('getSelections');
		var idArr = new Array();
		for(var i=0;i<rows.length;i++){
			idArr[i] =rows[i].reportId+"#"+rows[i].itemCode+"#"+rows[i].deptNo;			
		}
		var data =[{name:'ids',value:idArr.join(',') }];
		console.log(rows);	
		if(rows.length==0){$.messager.alert('消息提示','请选择要提交的明细项!','error');return ;}
			
			$.messager.confirm('提交','确认要提交选中的明细项么?',function(r){
				if (r){
					$.post('${pageContext.request.contextPath}/alm_reportdata/submit.do',data,function(result){
						if (result.success){
							$('#dg').datagrid('reload');	// reload the user data
						} else {
							$.messager.show({	// show error message
								title: 'Error',
								msg: result.errorMsg
							});
						}
					},'json');
				}
			});
	}
	//导出数据以excel文件格式导出
	function modeldownload(){
		var param={deptNo:"###"};
    	jsutil.core.download("${pageContext.request.contextPath}/alm_reportdata/download.do?deptCode=${currentUser.deptCode}",param);
	}
	 //查看事件流程
    function viewProcess(){
    var rows = $('#dg').datagrid('getSelections');
    if(rows.length==0){$.messager.alert('消息提示','请选择一条自评估进行查看!','error');return ;}
    if(rows.length>1){$.messager.alert('消息提示','请选择一条自评估进行查看!','error');return ;}
    if(rows[0].instanceId==null){$.messager.alert('消息提示','事件未提交，无审批流!','warning');return ;}
    var timestamp = Date.parse(new Date());
    var title = "查看自评估流程图片";
	var url = "${pageContext.request.contextPath}/alm_reportdata/viewEvaluateEventProcess.do?instanceId="+rows[0].instanceId+"&timestamp="+timestamp;
	$('#imagediv').dialog('open').dialog('setTitle',title);
	var imagediv = "<img style='width:auto;height:auto;max-width: 100%;max-height: 100%;' src='"+url+"'/>";
	//$('#image')[0].src=url;
	$('#imagediv').html(imagediv);
        $.ajax( { 
	        url: '${pageContext.request.contextPath}/alm_reportdata/viewEvaluateEventProcess.do',
	        type:'post',
	        data:{"instanceId":rows[0].instanceId},
	        success:function(data) {             	           
	         }
	    }); 
    }
	</script>
</html>