package com.sinosoft.controller.zcfz.reportDataAnalyse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sinosoft.common.Dao;
import com.sinosoft.zcfz.entity.lianghua.ALM_CfReportData;
import com.sun.jna.platform.win32.Kernel32.OVERLAPPED_COMPLETION_ROUTINE;


@Controller
@RequestMapping(value="/reportdataanalyse")
public class ReportDataAnalyse {
	@Resource
	private Dao dao;

	@RequestMapping(value = "/getdata", method = RequestMethod.POST)
	@ResponseBody
	public List<?> savReportItem(@RequestParam String month,@RequestParam List outItemCodes) {
		System.out.println("-->"+month +"--->"+outItemCodes);
		try {
			List list = new ArrayList();
			String outItemCode = "";
			String outItemCodeName = "";
			String quarter="";
			if(outItemCodes!=null&&!"".equals(outItemCodes)){
				//String[] outitemcodes = outItemCodes.split(",");
				for (int i = 0; i < outItemCodes.size(); i++) {
					outItemCode = outItemCodes.get(i).toString();
					String hsql = "from ALM_CfReportData where id.outItemCode='"
							+ outItemCode + "'";
					if (month != null
							&& !"".equals(month)) {
						hsql = "from ALM_CfReportData where id.outItemCode='"
								+ outItemCode
								+ "' and month in(" + month + ")";
					}
					List listResult = dao.query(hsql);
					int len = listResult.size();
					Object[] y = new Object[len];
					Map map = new HashMap();
					
					//Object[] quarter = new Object[len];s
					for (int j = 0; j < len; j++) {
						Object[] x = new Object[2];
						quarter = ((ALM_CfReportData) listResult.get(j)).getQuarter();
						x[0] = ((ALM_CfReportData) listResult.get(j)).getMonth()+ "-" + "Q" + quarter;
						x[1] = ((ALM_CfReportData) listResult.get(j))
								.getReportItemValue();
						outItemCodeName = ((ALM_CfReportData) listResult.get(0))
								.getOutItemCodeName();
						System.out.println(x[0] + "," +x[1]);
						System.out.println(outItemCodeName);

						y[j] = x;
					}
					map.put("data", y);
					map.put("name", outItemCode + "(" + outItemCodeName + ")");
					list.add(map);
					
					System.out.println("环比--------------->");
					
					System.out.println(list);
				}
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@RequestMapping(value = "/getdata1", method = RequestMethod.POST)
	@ResponseBody
	public List<?> savReportItem1(@RequestParam String month,@RequestParam List outItemCodes,@RequestParam List quarters) {
		try {
			List list = new ArrayList();
			String outItemCode = "";
			String outItemCodeName = "";
			String quarter="";
			//String[] quarters = quarters.split(",");
			if(quarters!=null&&!"".equals(quarters)){
				if(outItemCodes!=null&&!"".equals(outItemCodes)){
					for(int h=0;h<quarters.size();h++){
						quarter=quarters.get(h).toString();
						for (int i = 0; i < outItemCodes.size(); i++) {
							outItemCode = outItemCodes.get(i).toString();
							String hsql = "from ALM_CfReportData where id.outItemCode='"
									+ outItemCode + "'";
							if (month != null
									&& !"".equals(month)&& quarter!=null &&!"".equals(quarter)) {
								hsql = "from ALM_CfReportData where id.outItemCode='"
										+ outItemCode
										+ "' and id.month in(" + month + ")"
										+ " and id.quarter ='"+ quarter+"' ";
							}
							List listResult = dao.query(hsql);
							 
							int len = listResult.size();
							Object[] y = new Object[len];
							for (int j = 0; j < len; j++) {
								Object[] x = new Object[3];
								x[0] = ((ALM_CfReportData) listResult.get(j)).getMonth();
								x[1] = ((ALM_CfReportData) listResult.get(j))
										.getReportItemValue();
								x[2] = ((ALM_CfReportData) listResult.get(j)).getQuarter();
								outItemCodeName = ((ALM_CfReportData) listResult.get(0))
										.getOutItemCodeName();
								y[j] = x;
							}
							Map map = new HashMap();
							map.put("data", y);
							map.put("name", outItemCode + "(" + outItemCodeName + ")"+"第"+quarter+"季度");
							list.add(map);
							System.out.println("同比---------->" );
							System.out.println( list);
						}
					}
				}
			}
			
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	@RequestMapping(value = "/getdata3", method = RequestMethod.POST)
	@ResponseBody
	public List<?> savReportItem3(@RequestParam String month,@RequestParam List outItemCodes) {
		try {
			List list = new ArrayList();
			String outItemCode = "";
			String outItemCodeName = "";
			String quarter="";
			if(outItemCodes!=null&&!"".equals(outItemCodes)){
				//String[] outitemcodes = outItemCodes.split(",");
				for (int i = 0; i < outItemCodes.size(); i++) {
					outItemCode = outItemCodes.get(i).toString();
					String hsql = "from ALM_CfReportData where id.outItemCode='"
							+ outItemCode + "'";
					if (month != null
							&& !"".equals(month)) {
						hsql = "from ALM_CfReportData where id.outItemCode='"
								+ outItemCode
								+ "' and id.month in(" + month + ")";
					}
					List listResult = dao.query(hsql);
					int len = listResult.size();
					
					
					
					//Object[] quarter = new Object[len];
					for (int j = 0; j < len; j++) {
						Object[] y = new Object[len];
						Object[] x = new Object[2];
						
						quarter = ((ALM_CfReportData) listResult.get(j)).getQuarter();
//						x[0] = ((CfReportData) listResult.get(j)).getOutItemCodeName();
						x[0] = ((ALM_CfReportData) listResult.get(j)).getReportItemValue();
						System.out.println(x[0] + "," +x[1]);
						y[0] = x;
						Map map = new HashMap();
						map.put("data", x);	
						//						map.put("name", outItemCode );
						map.put("name", ((ALM_CfReportData) listResult.get(j)).getMonth()+ "-" + "Q" + quarter);
						list.add(map);
					}


					System.out.println("柱状分析--------------->");
					System.out.println(list);
				}
			}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/getdata4", method = RequestMethod.POST)
	@ResponseBody
	public List<?> savReportItem4(@RequestParam String month,@RequestParam String quarters) {
		try {
			
			List list = new ArrayList();
			if(quarters!=null&&!"".equals(quarters)){
				
					String sql = "select   case     when b.outitemcode= 'S07_00011' then '市场风险'"
							+ "   when b.outitemcode= 'S07_00019' then '信用风险'  when b.outitemcode= 'S07_00023' then '风险分散效应'  when b.outitemcode= 'S07_00024' then '损失吸收效应' when b.outitemcode= 'S07_00033' then '最低资本' "
							+ " else '其他' end   as outitemcode  ,   round((select  a.reportitemvalue/(select reportitemvalue from cfreportdata where outitemcode='S07_00033' and quarter='1' and month='2016') "
							+ "from CfReportData a where a.outitemcode = b.outitemcode  and a.quarter='1' and a.month='2016') *100,1) as value from cfreportdata b where b.outItemCode in ('S07_00011','S07_00019','S07_00023','S07_00024','S07_00033')  "
							+ " and b.quarter='"+quarters+"' and b.month='"+month+"'";
									 									 									
					List<Map> listResult = (List<Map>) dao.getList(sql);
					
					String sql2= " select round(( ( b.reportitemvalue + a.reportitemvalue)/(select reportitemvalue from cfreportdata where outitemcode='S07_00033' and quarter='"+quarters+"' and month='"+month+"') ) *100,1) as value , '保险风险' as outitemcode from "
							+ "(select ifnull(reportitemvalue ,0) reportitemvalue from CfReportData where outItemCode = 'S07_00002' and quarter='"+quarters+"' and month='"+month+"' ) a,"
							+ "(select ifnull(reportitemvalue ,0) reportitemvalue from CfReportData where outItemCode = 'S07_00007' and quarter='"+quarters+"' and month='"+month+"') b ";
					List<Map> listResult1 = (List<Map>) dao.getList(sql2);
					listResult.add(0,listResult1.get(0));
					
					int len = listResult.size();
					Object[] y = new Object[len];		
					Map map = new HashMap();
					
					//Object[] quarter = new Object[len];
					for (int j = 0; j < len; j++) {
						Object[] x = new Object[2];
						x[0] = listResult.get(j).get("outitemcode");
						x[1] = listResult.get(j).get("value");
						System.out.println(x[0] + "," +x[1]);
						y[j] = x;
					}
					map.put("data", y);
					map.put("name", "最低资本表");
					list.add(map);
					
					
					
					System.out.println("最低资本表--------------->");										
					System.out.println(list);
			
			
			
				}
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	
	
}
