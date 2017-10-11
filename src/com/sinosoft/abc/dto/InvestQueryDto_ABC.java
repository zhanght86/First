package com.sinosoft.abc.dto;

public class InvestQueryDto_ABC {
	private String yearmonth;
	private String quarter;
	private String investTable;
	private String subject;
	private String reportId;
	
	
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public String getYearmonth() {
		return yearmonth;
	}
	public void setYearmonth(String yearmonth) {
		this.yearmonth = yearmonth;
	}
	public String getQuarter() {
		return quarter;
	}
	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}
	public String getInvestTable() {
		return investTable;
	}
	public void setInvestTable(String investTable) {
		this.investTable = investTable;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	
}
