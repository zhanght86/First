package com.sinosoft.zcfz.dto.reportdataimport;

import java.math.BigDecimal;

public class ItemDataManualDTO {                //  对应itemdataManual.jsp
	private    String    userCode;              //  对应的登录信息
    private    String    reportRate;            //  对应 报告类型
    private    String    reportType;            //  对应 报告名称
    private    String    month;                 //  对应年度
    private    String    quarter;               //  对应季度
    private    String    reportItemCode;        //  对应因子代码
    private    String    reportName;            //  对应表告名称
    private    String    sort;
    private    String    order;
    private    BigDecimal  reportItemValue;      
    private    String    TextValue;          
    private    String    rid;
	public String getRid() {
		return rid;
	}
	public void setRid(String rid) {
		this.rid = rid;
	}
	public BigDecimal getReportItemValue() {
		return reportItemValue;
	}
	public void setReportItemValue(BigDecimal reportItemValue) {
		this.reportItemValue = reportItemValue;
	}
	public String getTextValue() {
		return TextValue;
	}
	public void setTextValue(String textValue) {
		TextValue = textValue;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	public String getReportRate() {
		return reportRate;
	}
	public void setReportRate(String reportRate) {
		this.reportRate = reportRate;
	}
	public String getReportType() {
		return reportType;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getQuarter() {
		return quarter;
	}
	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}
	public String getReportItemCode() {
		return reportItemCode;
	}
	public void setReportItemCode(String reportItemCode) {
		this.reportItemCode = reportItemCode;
	}
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public   String    getSortName(String   sort){
		if("reportCode".equals(sort)){sort="des.reportCode";}
		if("reportName".equals(sort)){sort="des.reportName";}
		if("reportItemCode".equals(sort)){sort="de.reportItemCode";}
		if("reportItemName".equals(sort)){sort="de.reportItemName";}
		   return  sort;
	}
	
//	public  void  getYearMonth(String   quarter){
//		if(){
//			
//		}
//	}
}
