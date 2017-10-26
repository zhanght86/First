<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>
<html>
<head>
<title>报表</title>
<%@include file="/commons/statics.jsp"%>
<%@include file="/commons/highchartutil.jsp"%>
</head>
<body>
	<script>
		var options={
				chart: {
					type: 'line',
				    renderTo:'container1'
				},
				credits:{
					enabled: false
				},
				title: {
				    text: '评价指标统计分析',
				    x: -20 //center
				},
				tooltip: {
					  pointFormat: '{series.name}: <b>{point.y}分</b>'
					},
				xAxis:{
					type: 'category',
					labels:{
						rotation:45
					}
				},
				yAxis: {
		            title: {
		                text: ''
		            }
		        }
			};	
		var chart;
		 $(function () {
			 getMonth();
			 $('#quarter').combobox({url:'${pageContext.request.contextPath}/codeSelect.do?type=quarter'});
		 });
		 
		function reload(){
			var categories;
			var data;
			var params={};
			params['month'] = $('#year').combobox('getValue');
			params['quarter'] = $('#quarter').combobox('getValue');
			var result=jsutil.core.invoke('${pageContext.request.contextPath}/reportdataanalyse/evalFactorData.do',params);
			console.log(result);
			options.series=result;
			
			chart = new Highcharts.Chart(options);
		};
		
		function getMonth(){
            var url = '${pageContext.request.contextPath}/codeSelect/hasParam.do?type=dataanalyse&params=';
            $('#year').combobox('reload', url);
		}
		
		//汉化图表菜单
		Highcharts.setOptions({
			lang : {
				contextButtonTitle : "图表菜单",
				printChart : "打印图片",
				downloadJPEG : "下载JPEG 图片",
				downloadPDF : "下载PDF文档",
				downloadPNG : "下载PNG 图片",
				downloadSVG : "下载SVG 矢量图",
				exportButtonTitle : "导出图片"
			}
		});

		//重置
		function reset(){
			$('#serachFrom').form("clear");
		}

		//搜索
	    function serach(){
			reload();
		}
	</script>
			<div style="border:1px solid #95B8E7;width: auto;padding: 10px;">
				<form id="serachFrom">
					<table style="width: 100%;">
			    		<tr height="30">
			    			<td style="width:10%;" >
			    				<label>年度:</label>
			    			</td>
			    			<td style="width:20%">
			    				<input  id="year" class="easyui-combobox" name="month"  data-options="
					                method: 'get',
					                valueField:'value',
					                textField:'text',
					                required:true
					            " >
			    			</td>
			    			<td style="width:10%;" >
			    				<label>季度:</label>
			    			</td>
			    			<td style="width:20%">
			    				<input  id="quarter" class="easyui-combobox" name="quarter"  data-options="
					                method: 'get',
					                valueField:'value',
					                textField:'text',
					                required:true
					            " >
			    			</td>
			    			<td style="width:40%;" >
			    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="serach()"  data-options="iconCls:'icon-search'" style="width:80px;">查看</a>
			    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="reset()" data-options="iconCls:'icon-reload'" style="width:80px;">重置</a>
			    			</td>
			    		</tr>
			    	</table>
				</form>
			</div>
			<div class="menu-content" style="float: center;width:100%;height: 400px;margin:0 auto">
						<div class="box-title">
							<div style="float: right; padding-right: 10px;"></div>
						</div>
						<div class="box-content" style="height: 400px">
							<div id="container1" style="height: 90%; max-width: 100%"></div>
						</div>
			</div>
</body>
</html>