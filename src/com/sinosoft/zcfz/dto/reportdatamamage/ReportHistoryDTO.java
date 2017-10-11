package com.sinosoft.zcfz.dto.reportdatamamage;

public class ReportHistoryDTO {
	private   String    sort;
	private   String    order;
	private   String    report_code;         
	private   String    report_name;         
	private   String    report_rate;         
	private   String    month;
	private   String    quarter;
	public String getReport_code() {
		return report_code;
	}
	public void setReport_code(String report_code) {
		this.report_code = report_code;
	}
	public String getReport_name() {
		return report_name;
	}
	public void setReport_name(String report_name) {
		this.report_name = report_name;
	}

	public String getReport_rate() {
		return report_rate;
	}
	public void setReport_rate(String report_rate) {
		this.report_rate = report_rate;
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

}
