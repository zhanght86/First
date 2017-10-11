package com.sinosoft.dto;

/**
 * @author Administrator
 * 
 */
public class DownloadCheckDTO {
	
	//1表中的公式
	private String calFormula;
	
	//1表中的公式说明
	private String remark;
	
	//判断导出那张表
	private String checkSchemaCode;
	
	//2表中的
	private String tableCode;
	
	//3表中的因子指标
	private String reportItemCode;
	
	//3表中的
	private String colCode;
	
	//4表中的
	private String itemCodeBq;
	private String itemCodeSq;
	
	private String calType;
	private String isNeedChk;
	private String tolerance;
	private String relationOperator;
	private String year;
	private String quarter;
	private String reportType;
	private String checkedCode;
	private String temp;
	private String errorInfo;
	private String checkdate;
	
	
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getQuarter() {
		return quarter;
	}

	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}

	public String getReportType() {
		return reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getCheckedCode() {
		return checkedCode;
	}

	public void setCheckedCode(String checkedCode) {
		this.checkedCode = checkedCode;
	}

	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	public String getErrorInfo() {
		return errorInfo;
	}

	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}

	public String getCheckdate() {
		return checkdate;
	}

	public void setCheckdate(String checkdate) {
		this.checkdate = checkdate;
	}
	public String getRelationOperator() {
		return relationOperator;
	}
	public void setRelationOperator(String relationOperator) {
		this.relationOperator = relationOperator;
	}
	public String getCalType() {
		return calType;
	}
	public void setCalType(String calType) {
		this.calType = calType;
	}
	public String getIsNeedChk() {
		return isNeedChk;
	}
	public void setIsNeedChk(String isNeedChk) {
		this.isNeedChk = isNeedChk;
	}
	public String getTolerance() {
		return tolerance;
	}
	public void setTolerance(String tolerance) {
		this.tolerance = tolerance;
	}
	public String getCalFormula() {
		return calFormula;
	}
	public void setCalFormula(String calFormula) {
		this.calFormula = calFormula;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCheckSchemaCode() {
		return checkSchemaCode;
	}
	public void setCheckSchemaCode(String checkSchemaCode) {
		this.checkSchemaCode = checkSchemaCode;
	}
	public String getTableCode() {
		return tableCode;
	}
	public void setTableCode(String tableCode) {
		this.tableCode = tableCode;
	}
	public String getReportItemCode() {
		return reportItemCode;
	}
	public void setReportItemCode(String reportItemCode) {
		this.reportItemCode = reportItemCode;
	}
	public String getColCode() {
		return colCode;
	}
	public void setColCode(String colCode) {
		this.colCode = colCode;
	}
	public String getItemCodeBq() {
		return itemCodeBq;
	}
	public void setItemCodeBq(String itemCodeBq) {
		this.itemCodeBq = itemCodeBq;
	}
	public String getItemCodeSq() {
		return itemCodeSq;
	}
	public void setItemCodeSq(String itemCodeSq) {
		this.itemCodeSq = itemCodeSq;
	}
	@Override
	public String toString() {
		return "DownloadCheckDTO [calFormula=" + calFormula + ", remark="
				+ remark + ", checkSchemaCode=" + checkSchemaCode
				+ ", tableCode=" + tableCode + ", reportItemCode="
				+ reportItemCode + ", colCode=" + colCode + ", itemCodeBq="
				+ itemCodeBq + ", itemCodeSq=" + itemCodeSq + ", calType="
				+ calType + ", isNeedChk=" + isNeedChk + ", tolerance="
				+ tolerance + ", relationOperator=" + relationOperator + "]";
	}



	
	
}
