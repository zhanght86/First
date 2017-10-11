package com.sinosoft.zcfz.dto.reportdatamamage;

import java.math.BigDecimal;

public class CfReportDTO {                         //  对应reportdata.jsp
	private   String       userCode;               //  对应登着的信息
	private   String       sort;
	private   String       order;
    private   String       reportRate;             //  报表类型
    private   String       reportType;             //  报表名称
    private   String       month;                  //  年度
    private   String       quarter;                //  季度
	private   String       outItemCode; 	       //  因子代码
	private   BigDecimal   reportItemValue;        //  修改前的数值类型
	private   BigDecimal   reportItemValue_after;  //  修改后的数值类型
	private   String       textValue;              //  修改前的文本类型
	private   String       textValue_after; 	   //  修改后的文本类型
	private   String       reportItemName;         //  因子名称
	private   String       codeName;
	private   String       sheetCode;                  //  此属性具体名称待定，
	private   String       sheetName; 
	private   String       descText;                  //  此属性具体名称待定，
	private   String 	   reportId; //报送号
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
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
	public String getOutItemCode() {
		return outItemCode;
	}
	public void setOutItemCode(String outItemCode) {
		this.outItemCode = outItemCode;
	}
	
	public BigDecimal getReportItemValue() {
		return reportItemValue;
	}
	public void setReportItemValue(BigDecimal reportItemValue) {
		this.reportItemValue = reportItemValue;
	}
	public BigDecimal getReportItemValue_after() {
		return reportItemValue_after;
	}
	public void setReportItemValue_after(BigDecimal reportItemValue_after) {
		this.reportItemValue_after = reportItemValue_after;
	}
	public String getTextValue() {
		return textValue;
	}
	public void setTextValue(String textValue) {
		this.textValue = textValue;
	}
	public String getTextValue_after() {
		return textValue_after;
	}
	public void setTextValue_after(String textValue_after) {
		this.textValue_after = textValue_after;
	}
	public String getReportItemName() {
		return reportItemName;
	}
	public void setReportItemName(String reportItemName) {
		this.reportItemName = reportItemName;
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
	
	public String getCodeName() {
		return codeName;
	}
	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}
	

	public String getSheetCode() {
		return sheetCode;
	}
	public void setSheetCode(String sheetCode) {
		this.sheetCode = sheetCode;
	}
	public String getSheetName() {
		return sheetName;
	}
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	// 防止排序两张表的字段冲突
	public  String   getSortName(String   sort){
		if("codeName".equals(sort)){
			sort="reportrate";
		}
		if("outItemCode".equals(sort)){
			sort="da.outItemCode";
		}
		
		return  sort;
	}
	public String getDescText() {
		return descText;
	}
	public void setDescText(String descText) {
		this.descText = descText;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	
}
