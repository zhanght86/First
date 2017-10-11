<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>
<html>
<head>
<title>偿付能力重要指标分析(同比)</title>
<%@include file="/commons/statics.jsp"%>
<%@include file="/commons/highchartutil.jsp"%>
<style>
button.change
{
    background-color: green;
    color: #794A4A;
	border-radius: 5px;
    /* font-size: 20px; */
    /* text-align: center; */
    /* text-decoration: none; */
    /* display: inline-block; */
    cursor: pointer;
    /* float: left; */
    border: none;
    color: white;
    padding: 7px 16px;
    text-align: center;
    text-decoration: none;
    display: inline-block;
    font-size: 16px;
    box-sizing: border-box;
    -webkit-transition-duration: 0.4s; /* Safari */
    transition-duration: 0.4s;
}
button.change:hover
{
	background-color: gray; 
    color: white;
}
</style>
</head>
<body>
	<script>
		var options={
				chart: {
				    plotBackgroundColor: null,
				    plotBorderWidth: null,
				    plotShadow: false,
				    renderTo:'container1'
				},
				credits:{
					enabled: false
				},
				title: {
				    text: '固定因子统计分析',
				    x: -20 //center
				},
				tooltip: {
				  pointFormat: '{series.name}: <b>{point.y}</b>'
				},
				xAxis:{
					
				},
				yAxis: {
		            title: {
		                text: '金额'
		            },
		           plotLines: [{
		                value: 0,
		                width: 1,
		                color: '#808080'
		            }],
		            type:'linear',
		        },
				labels:{
					items:[{
						/* html:'<a href="http://www.52wulian.org" target="_blank">HighCharts</a>',
						style: {
							left:'532px',
							top:'160px',				
						} */
					}],
					style:{
						color:'red',
						fontSize:45,
						fontWeight:'bold',
						zIndex:1000
					}
				},
				series: [
				         
				  ]
			};	
		var type="column";
		var chart;
		 $(function () {
			 reload();
			 $('.change').on('click',function(){
				type = $(this).val();
					reload();
			}); 
		 });
		 
		 function loadData(){
				var params = {};
				var outitem=new Array();
				var outitemCode="";
				outitem=$("#cc2").combobox('getValues');
				for(var key in outitem){
					outitemCode=outitemCode+","+outitem[key]; 
	            }
				outitemCode=outitemCode.substr(1);
			    params["outItemCodes"]=outitemCode;
			    var quarterValues=new Array();
			    var quarter="";
			    quarterValues=$("#cc3").combobox('getValues');
			    for(var key in quarterValues){
					quarter=quarter+","+quarterValues[key]; 
	            }
				quarter=quarter.substr(1);
			    params["quarters"]=quarter;
				/* $('#serachFrom').find('input').each(function(){
			        var obj = $(this);
			        var name = obj.attr('name');
			        if(name){
				        if(name=="month"){ */
		        			params["month"]=$("#cc1").combobox('getValue');
				        /* }
			        }
			    }); */
				return params;
			}
		 
		function reload(){
			var categories;
			var data;
			var params=loadData();
			var result=jsutil.core.invoke('${pageContext.request.contextPath}/reportdataanalyse/getdata1.do',params);
			if(type == "pie") {
				options.tooltip.pointFormat = '{series.name}: <b>{point.percentage:.1f}%</b>';
			}else {
				options.tooltip.pointFormat = '{series.name}: <b>{point.y}</b>';
			}
			options.chart.type = type;
			options.series=result;
			//options.series[0].data=result.data;
			//options.series[0].name=result.name;
			var results=new Array();
			var x=new Array();
			
			for(var key in result.list){
				results.list[key].data= result.list[key].data; //给X轴赋值
				for(var keyx in results){
					x[keyx] = results.data[keyx][0]; //给X轴赋值
					
	            }
            }
			options.yAxis.categories=x;
			chart = new Highcharts.Chart(options);
		};
		
		function getItemCode(rec){
			var month= $("#cc1").combobox('getValues');
			
            var url = '${pageContext.request.contextPath}/codeSelect/hasParam.do?type=reportdataanalyse1&params='+month;
            $('#cc2').combobox('reload', url);
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
					<!-- 日期的 -->
			    		<tr height="30">
			    			<td style="width:10%;" >
			    				<label>年度:</label>
			    			</td>
			    			<td style="width:40%">
			    				<input  id="cc1" class="easyui-combobox" name="outItemCodes" style="width:60%;" data-options="
			    					url: '${pageContext.request.contextPath}/codeSelect.do?type=reportdataanalyse1',
					                method: 'get',
					                valueField:'value',
					                textField:'text',
					                required:true,
					                multiple:false,
					                multiline:false,
					                onSelect: getItemCode
					            " >
			    			</td>
			    			<td style="width:10%;" >
			    				<label>季度:</label>
			    			</td>
			    			<td style="width:40%">
			    				<input  id="cc3" class="easyui-combobox" name="quarters"  style="width:60%;" data-options="
			    					url: '${pageContext.request.contextPath}/codeSelect.do?type=quarter',
					                method: 'get',
					                valueField:'value',
					                textField:'text',
					                groupField:'group',
					                required:true,
					                multiple:false,
					                multiline:false,
					                editable:false
					            " >
			    			</td>
			    		</tr>
			    		<!-- 固定因子的 -->
			    		<tr height="30">
			    			<td style="width:10%;" >
			    				<label>固定因子:</label>
			    			</td>
			    			<td style="width:40%;">
			    				<input id="cc2" class="easyui-combobox" name="month" style="width:60%;height:50px" data-options="
					                method: 'get',
					                valueField:'value',
					                textField:'text',
					                groupField:'group',
					                required:true,
					                multiple:true,
					                multiline:true
					            " >
			    			</td>
			    			<td style="width:10%;" >
			    				
			    			</td>
			    			<td align="right" style="padding-right: 160px;">
			    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="serach()"  data-options="iconCls:'icon-search'" style="width:80px;">查看</a>
			    				<a href="javascript:void(0)" class="easyui-linkbutton" onclick="reset()" data-options="iconCls:'icon-reload'" style="width:80px;">重置</a>
			    			</td>
			    		</tr>
			    	</table>
				</form>
			</div>
			<div class="menu-content" style="float: center;width:100%;height: 400px;margin:0 auto">
						<div class="box-title">
							<div style="float: left">
								<img src="${pageContext.request.contextPath}/js/easyui/themes/icons/man.png" alt="" width="20"
									height="20" /> 图表1
							</div>
							<div style="float: right; padding-right: 10px;"></div>
						</div>
						<div class="box-content" style="height: 400px">
							<div id="container1" style="height: 90%; max-width: 100%"></div>
						</div>
			</div>
			<div style="margin:5px 0px 10px 20px;">
			      点击按钮切换图表：
			      <button class="change" value="line">直线图</button>
			      <button class="change" value="spline">曲线图</button>
			      <button class="change" value="pie">饼图</button>
			      <button class="change" value="area">面积图</button>
			      <button class="change" value="column" >柱状图</button>
			      <button class="change" value="areaspline">曲线面积范围图</button>
			      <button class="change" value="bar">条形图</button>
			      <button class="change" value="scatter">散点图</button>
			 </div>
</body>
</html>