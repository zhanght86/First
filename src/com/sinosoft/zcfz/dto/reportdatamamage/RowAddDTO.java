package com.sinosoft.zcfz.dto.reportdatamamage;

import java.math.BigDecimal;

public class RowAddDTO {
	private   String      userCode;
	private   String      sort;
	private   String      order;
    private   String      reportRate;
    private   String      reportType;
    private   String      yearMonth;
    private   String      quarter;
    private   String      tableCode;
    private   BigDecimal  numValue_after;
    private   String      textValue_after;
    private   String      colCode;
    private   String      tableName;
    private   String      colName;
    private   String      rowNo;
    private   String      codeName;
    private   BigDecimal  numValue;
    private   String      textValue;
    private	  String 	  reportId;
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
	public String getYearMonth() {
		return yearMonth;
	}
	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}
	public String getQuarter() {
		return quarter;
	}
	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}
	
	public BigDecimal getNumValue_after() {
		return numValue_after;
	}
	public void setNumValue_after(BigDecimal numValue_after) {
		this.numValue_after = numValue_after;
	}
	public String getTextValue_after() {
		return textValue_after;
	}
	public void setTextValue_after(String textValue_after) {
		this.textValue_after = textValue_after;
	}
	
	public String getTableCode() {
		return tableCode;
	}
	public void setTableCode(String tableCode) {
		this.tableCode = tableCode;
	}
	
	public String getColCode() {
		return colCode;
	}
	public void setColCode(String colCode) {
		this.colCode = colCode;
	}
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public String getColName() {
		return colName;
	}
	public void setColName(String colName) {
		this.colName = colName;
	}
	
	public String getRowNo() {
		return rowNo;
	}
	public void setRowNo(String rowNo) {
		this.rowNo = rowNo;
	}
	public String getCodeName() {
		return codeName;
	}
	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}
	public BigDecimal getNumValue() {
		return numValue;
	}
	public void setNumValue(BigDecimal numValue) {
		this.numValue = numValue;
	}
	public String getTextValue() {
		return textValue;
	}
	public void setTextValue(String textValue) {
		this.textValue = textValue;
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
	
    public  String    getSortName(String   sort){
    	if("tableCode".equals(sort)){
    		sort="da.tableCode";
    	}
    	if("colCode".equals(sort)){
    		sort="da.colCode";  
    	}
    	if("codeName".equals(sort)){
    		sort="reportrate";
    	}
    	return  sort;
    }
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	
    
}
