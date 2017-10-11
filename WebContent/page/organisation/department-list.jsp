<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html >
<%@include file="/commons/taglibs.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@include file="/commons/statics.jsp"%>
<title>机构管理</title>
</head>
<body>
	<div class="easyui-layout" fit="true" >
		<div region="west" border="false"  style="width:35%;height:100%;margin:0 0 0 20px">
			<div class="easyui-panel" style="height:95%;width:80%; margin:20px 0 0 0 ;padding:10px 0 0 0;" >
				<ul class="easyui-tree" id="tt" >
				</ul>
			</div>
		</div>
		<div region="center" border="false" style="width:50% ;height:100%;">
			<div class="easyui-panel" style="height:95%;margin:20px 0 0 0;" align="center" > 
				<form id="showfm" method="post" style="height:90%;">
					<table cellpadding="5" >
			    		<tr>
			    			<td><label>机构编码:</label></td>
			    			<td>
								 <input class="easyui-textbox"  style="width:160px" name="comCode"  disabled="disabled">
							</td>
			    		</tr>
			    		<tr>
			    			<td><label>机构名称:</label></td>
			    			<td>
								 <input class="easyui-textbox" style="width:160px" name="comName" disabled="disabled">
							</td>
			    		</tr>
			    		<tr>
			    			<td><label>机构类别:</label></td>
			    			<td>
								 <input class="easyui-textbox" style="width:160px" name="comRankName" disabled="disabled">
							</td>
			    		</tr>
			    		<tr>
		    				<td><label>许可证号:</label></td>
		    				<td>
								 <input class="easyui-textbox"  style="width:160px" name="licensenumber" disabled="disabled">
							</td>
		    			</tr>
			    		<tr>
			    			<td><label>是否启用:</label></td>
			    			<td>
								 <select name="useFlag" disabled="disabled" style="width:80px">
								 	<option value="0">否</option>
								 	<option value="1">是</option>
								 </select>
							</td>
			    		</tr>
			    		<tr>
			    			<td><label>是否末级:</label></td>
			    			<td>
								  <select name="endFlag" disabled="disabled" style="width:80px">
								 	<option value="0">否</option>
								 	<option value="1">是</option>
								 </select>
							</td>
			    		</tr>
		    		</table>
		    	</form>
		    	<div align="right" style="padding-right:20px">
			    	<div id="toolbar">
				    	<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" onclick="newMenu(false)">新增机构</a>
				    	<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" onclick="editMenu()">编辑机构</a>
				    	<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" onclick="destroyMenu()">删除机构</a>
			   		 </div>
			    </div>
			</div>
		</div>
		<div region="east" border="false" style="width:3% ;">
		</div>
	</div>
	<div id="mm" class="easyui-menu" style="width:120px;">
		<div onclick="newMenu(true)" data-options="iconCls:'icon-add'">添加机构</div>
		<div onclick="editMenu()" data-options="iconCls:'icon-edit'">编辑机构</div>
		<div onclick="destroyMenu()" data-options="iconCls:'icon-remove'">删除机构</div>
	</div>
	<!-- 新建菜单弹出框 -->
		<div id="dlg" class="easyui-dialog" style="width:600px;height:400px;padding:10px 20px;"
	    		closed="true" buttons="#dlg-buttons" align="center" data-options="modal:true">
    	<form id="fm" method="post">
	    	<table cellpadding="5">
		    		<tr>
		    			<td><label>父机构:</label></td>
		    			<td>
							 <select  id="cc" class="easyui-combotree" style="width:200px"
        		 				data-options="prompt:'父机构',missingMessage:'请选择父机构'">
        					</select>
        					<input type="hidden" id="uperComCode" name="uperComCode">
						</td>
		    		</tr>
		    		<tr>
		    			<td><label>机构ID:</label></td>
		    			<td>
							 <input class="easyui-textbox easyui-validatebox"  style="width:200px" name="comCode" id="comCode" type="text" data-options="prompt:'机构ID',required:true,missingMessage:'请添加机构ID'">
						</td>
		    		</tr>
		    		<tr>
		    			<td><label>机构名称:</label></td>
		    			<td>
							 <input class="easyui-textbox easyui-validatebox" type="text" style="width:200px" name="comName" id="comName" data-options="prompt:'机构名称',required:true,missingMessage:'请添加机构名称'">
						</td>
		    		</tr>
		    		<tr>
		    			<td><label>机构类型:</label></td>
		    			<td>
		    			 <input  id="comRank" name="comRank" class="easyui-combobox" style="width:200px"
        		 				data-options="prompt:'请选择机构类别',
        		 				required:true,missingMessage:'请添加机构类别',
						  	    url:'${pageContext.request.contextPath}/branchinfo/comTypeSelect.do',
						   		method: 'get',
						   		editable:false,
						   		valueField:'value',
						   		textField:'text',
						   		groupField:'group'
        		 				">
        				
						</td>
		    		</tr>
		    		<tr>
		    			<td><label>许可证号:</label></td>
		    			<td>
							 <input class="easyui-textbox easyui-validatebox"  style="width:200px" type="text"  name="licensenumber" id="licensenumber" data-options="prompt:'许可证号',required:false,missingMessage:'请添加许可证号'">
						</td>
		    		</tr>
		    		<tr>
		    			<td><label>是否启用:</label></td>
		    			<td>
							 <select name="useFlag" style="width:100px">
							 	<option value="0">否</option>
							 	<option value="1">是</option>
							 </select>
						</td>
		    		</tr>
		    		<tr>
		    			<td><label>是否末级:</label></td>
		    			<td>
		    				<select name="endFlag" style="width:100px">
							 	<option value="0">否</option>
							 	<option value="1">是</option>
							 </select>
						</td>
		    		</tr>
	    	</table>
    	</form>
    </div>
     <div id="dlg-buttons">
    	<a href="#" class="easyui-linkbutton" iconCls="icon-ok" onclick="saveMenu()">保存</a>
    	<a href="#" class="easyui-linkbutton" iconCls="icon-cancel" onclick="javascript:$('#dlg').dialog('close')">取消</a>
    </div>
<script type="text/javascript">

$('#tt').tree({
	 url:'${pageContext.request.contextPath}/branchinfo/list.do'
});

//右键菜单功能函数触发
$('#tt').tree({
	onContextMenu: function(e, node){
		e.preventDefault();
		$('#tt').tree('select', node.target);
		$('#mm').menu('show',{
			left: e.pageX,
			top: e.pageY
		});
	}
});

//单击机构树时触发函数
$('#tt').tree({
	onSelect:function(node){
		//点击机构时，在将机构的详细信息显示在右侧明细栏;
		var node=$('#tt').tree('getSelected');
		$('#showfm').form('load',node);
	}
});

//新建机构选择部门时许可证号不可填写
 $(document).ready(function(){
	$("#comRank").combobox({
	onChange: function(){
	   var getvalue = $("#comRank").combobox("getValue");
	   //var inputValue = getvalue.substr(0,2);
		   if(getvalue==4){
			  $('#licensenumber').textbox('disable');
			  $('#licensenumber').textbox({
				  required: false
			  });
		   }else {
			   $('#licensenumber').textbox({disabled:false});
			   $('#licensenumber').textbox({
					  required: true
				  });
		   } 
		}
	});
}); 

//新建机构
function newMenu(flag){
	$('#dlg').dialog('open').dialog('setTitle','添加子机构');
	$('#fm').form('clear');
	url='${pageContext.request.contextPath}/branchinfo/add.do';
	$.ajax({
		url:'${pageContext.request.contextPath}/branchinfo/list1.do',
		type:'get',
		success:function(result){
			$('#cc').combotree('loadData',result);			
		}
	});
	$('#comCode').textbox('enable');
	var node=$('#tt').tree('getSelected');
	if(node&&flag)
	$('#cc').combotree('setValue', node.id);
}
function editMenu(){
    	var node=$('#tt').tree('getSelected');
    	if(node){
    		$('#dlg').dialog('open').dialog('setTitle','编辑机构');
    		$('#fm').form('clear');
        	$.ajax({
        		url:'${pageContext.request.contextPath}/branchinfo/list.do',
        		type:'get',
        		success:function(result){
        			$('#cc').combotree('loadData',result);
        		}
        	});
        	$('#comCode').textbox('disable');
        	if(node._parentId!=null){
         		  $('#cc').combotree('setValue', node._parentId);
         		 
         	    }else{
         		  $('#cc').combotree('setValue', '');
         		 
       
         	}
        	$('#fm').form('load',node);
        	url = '${pageContext.request.contextPath}/branchinfo/update.do?id='+node.id;
    	}else{
    		$.messager.show({
    			 title: '提示信息',  
                 msg: '请先选择所要编辑的机构',  
                 showType: 'show'  
    		});
    	}
}
function saveMenu(){
	var t = $('#cc').combotree('tree');	// get the tree object
	 var n = t.tree('getSelected');
	if(n)
	 $('#uperComCode').val(n.id);
		$.ajax({
		url:url,
		data:$('#fm').serialize(),
		type:'post',
		beforeSend: function(){
			/*  var flag=$('#fm').form('validate');
			if(!flag){
				$.messager.show({
					title: '提示',
					msg:"请填写带*信息栏"
				});	
			}
			return flag;  */
		},
		success: function(result){
			if (result.success){
				$('#tt').tree('reload');	// reload the user data
				$('#showfm').form("clear");
				$('#dlg').dialog('close');		// close the dialog
				$.messager.show({
					title: '成功',
					msg:"操作成功"
				});		
			}else if (result.errorMsg){
				$.messager.show({
					title: '错误',
					msg: result.errorMsg
				});
			} 
		}
	}); 
}
function destroyMenu(){
	var node=$('#tt').tree('getSelected');
   	if (node){
   		$.messager.confirm('删除','确认删除该机构信息吗?',function(r){
   			if (r){
   				 $.post('${pageContext.request.contextPath}/branchinfo/delete.do',{id:node.id},function(result){
   					if (result.success){
   						$('#tt').tree('reload');	// reload the user data
   						//jsutil.msg.info("删除成功");
   						$.messager.show({
   							title: '成功',
   							msg:"删除成功"
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
   	}else{
		$.messager.show({
			 title: '提示信息',  
            msg: '请先选择所要删除的机构',  
            showType: 'show'  
		});
	}
}
</script>
</body>
</html>