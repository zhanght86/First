<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/commons/taglibs.jsp"%>
<html>
<head>
<title>报表</title>
<%@include file="/commons/statics.jsp"%>
<script src="<c:url value='/js/highchart/highcharts.js' />"></script>
<script src="<c:url value='/js/highchart/modules/exporting.js' />"></script>
<script src="<c:url value='/js/highchart/themes/grid.js' />"></script>
<script src="<c:url value='/js/highchart/highcharts-more.js' />"></script>
<script src="<c:url value='/js/highchart/highcharts-3d.js' />"></script>
</head>
<body>
	<script>
		$(function() {
			$('#container1')
					.highcharts(
							{
								chart : {
									plotBackgroundColor : null,
									plotBorderWidth : null,
									plotShadow : false
								},
								title : {
									text : '饼状图'
								},
								tooltip : {
									pointFormat : '{series.name}: <b>{point.percentage:.1f}%</b>'
								},
								plotOptions : {
									pie : {
										allowPointSelect : true,
										cursor : 'pointer',
										dataLabels : {
											enabled : true,
											format : '<b>{point.name}</b>: {point.percentage:.1f} %',
											style : {
												color : (Highcharts.theme && Highcharts.theme.contrastTextColor)
														|| 'black'
											}
										}
									}
								},
								series : [ {
									type : 'pie',
									name : 'Browser share',
									data : [ [ 'Firefox', 45.0 ],
											[ 'IE', 26.8 ], {
												name : 'Chrome',
												y : 12.8,
												sliced : true,
												selected : true
											}, [ 'Safari', 8.5 ],
											[ 'Opera', 6.2 ], [ 'Others', 0.7 ] ]
								} ]
							});
		});
		$(function () {
		    $('#container2').highcharts({
		        title: {
		            text: '折线图',
		            x: -20 //center
		        },
		        subtitle: {
		            text: 'Source: WorldClimate.com',
		            x: -20
		        },
		        xAxis: {
		            categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun','Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
		        },
		        yAxis: {
		            title: {
		                text: 'Temperature (°C)'
		            },
		            plotLines: [{
		                value: 0,
		                width: 1,
		                color: '#808080'
		            }]
		        },
		        tooltip: {
		            valueSuffix: '°C'
		        },
		        legend: {
		            layout: 'vertical',
		            align: 'right',
		            verticalAlign: 'middle',
		            borderWidth: 0
		        },
		        series: [{
		            name: 'Tokyo',
		            data: [7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6]
		        }, {
		            name: 'New York',
		            data: [-0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5]
		        }, {
		            name: 'Berlin',
		            data: [-0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0]
		        }, {
		            name: 'London',
		            data: [3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8]
		        }]
		    });
		});
						
		$(function () {
		    $('#container3').highcharts({
		        chart: {
		            type: 'column'
		        },
		        title: {
		            text: '柱状图'
		        },
		        subtitle: {
		            text: 'Source: WorldClimate.com'
		        },
		        xAxis: {
		            categories: [
		                'Jan',
		                'Feb',
		                'Mar',
		                'Apr',
		                'May',
		                'Jun',
		                'Jul',
		                'Aug',
		                'Sep',
		                'Oct',
		                'Nov',
		                'Dec'
		            ]
		        },
		        yAxis: {
		            min: 0,
		            title: {
		                text: 'Rainfall (mm)'
		            }
		        },
		        tooltip: {
		            headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
		            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
		                '<td style="padding:0"><b>{point.y:.1f} mm</b></td></tr>',
		            footerFormat: '</table>',
		            shared: true,
		            useHTML: true
		        },
		        plotOptions: {
		            column: {
		                pointPadding: 0.2,
		                borderWidth: 0
		            }
		        },
		        series: [{
		            name: 'Tokyo',
		            data: [49.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4]

		        }, {
		            name: 'New York',
		            data: [83.6, 78.8, 98.5, 93.4, 106.0, 84.5, 105.0, 104.3, 91.2, 83.5, 106.6, 92.3]

		        }, {
		            name: 'London',
		            data: [48.9, 38.8, 39.3, 41.4, 47.0, 48.3, 59.0, 59.6, 52.4, 65.2, 59.3, 51.2]

		        }, {
		            name: 'Berlin',
		            data: [42.4, 33.2, 34.5, 39.7, 52.6, 75.5, 57.4, 60.4, 47.6, 39.1, 46.8, 51.1]

		        }]
		    });
		});		
		﻿$(function () {
		    // Set up the chart
		    var chart = new Highcharts.Chart({
		        chart: {
		            renderTo: 'container4',
		            type: 'column',
		            margin: 75,
		            options3d: {
		                enabled: true,
		                alpha: 15,
		                beta: 15,
		                depth: 50,
		                viewDistance: 25
		            }
		        },
		        title: {
		            text: '3D图'
		        },
		        subtitle: {
		            text: 'Test options by dragging the sliders below'
		        },
		        plotOptions: {
		            column: {
		                depth: 25
		            }
		        },
		        series: [{
		            data: [29.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4]
		        }]
		    });
		    

		    // Activate the sliders
		    $('#R0').on('change', function(){
		        chart.options.chart.options3d.alpha = this.value;
		        showValues();
		        chart.redraw(false);
		    });
		    $('#R1').on('change', function(){
		        chart.options.chart.options3d.beta = this.value;
		        showValues();
		        chart.redraw(false);
		    });

		    function showValues() {
		        $('#R0-value').html(chart.options.chart.options3d.alpha);
		        $('#R1-value').html(chart.options.chart.options3d.beta);
		    }
		    showValues();
		});				
		// Apply the theme
		var highchartsOptions = Highcharts.setOptions(Highcharts.theme);

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
	</script>
	<form>
			<div class="menu-content" style="float: left;width:400px;height: 400px;">
						<div class="box-title">
							<div style="float: left">
								<img src="../js/easyui/themes/icons/man.png" alt="" width="20"
									height="20" /> 图表1
							</div>
							<div style="float: right; padding-right: 10px;">更多</div>
						</div>
						<div class="box-content" style="height: 310px">
							<div id="container1" style="height: 300px; max-width: 500px"></div>
						</div>
			</div>
			<div class="menu-content"  style="float: right;width:400px;height: 400px;">
					<div class="easyui-box">
						<div class="box-title">
							<div style="float: left">
								<img src="../js/easyui/themes/icons/man.png" alt="" width="20"
									height="20" /> 图表2
							</div>
							<div style="float: right; padding-right: 10px;">更多</div>
						</div>
						<div class="box-content" style="height: 310px">
							<div id="container2" style="height: 300px; max-width: 500px"></div>
						</div>
					</div>
			</div>
			<div class="menu-content"  style="float: left;width:400px;height: 400px;">
					<div class="easyui-box">
						<div class="box-title">
							<div style="float: left">
								<img src="../js/easyui/themes/icons/man.png" alt="" width="20"
									height="20" /> 图表3
							</div>
							<div style="float: right; padding-right: 10px;">更多</div>
						</div>
						<div class="box-content" style="height: 310px">
							<div id="container3" style="height: 300px; max-width: 500px"></div>
						</div>
					</div>
			</div>
			<div class="menu-content"  style="float: right;width:400px;height: 400px;">
					<div class="easyui-box">
						<div class="box-title">
							<div style="float: left">
								<img src="../js/easyui/themes/icons/man.png" alt="" width="20"
									height="20" /> 图表4
							</div>
							<div style="float: right; padding-right: 10px;">更多</div>
						</div>
						<div class="box-content" style="height: 310px">
							<div id="container4" style="height: 300px; max-width: 500px"></div>
						</div>
					</div>
			</div>
	</form>
</body>
</html>