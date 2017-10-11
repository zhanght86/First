package com.sinosoft.entity;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="reporthistory")
public class ReportHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	@Column
	private   String      oper_date;  
	@Column
	private   String      report_code;         
	@Column
	private   String      report_name;         
	@Column
	private   String      report_rate;
	@Column
	private   String      month;
	@Column
	private   String      quarter;
	@Column
	private   BigDecimal  report_mvalue;      
	@Column
	private   BigDecimal  report_mvalue_after; 
	@Column
	private   String      text_value;          
	@Column
	private   String      text_value_after;    
	@Column
	private   String      user_name;    
	@Column
	private   String      reportid;    
	         
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
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
	public BigDecimal getReport_mvalue() {
		return report_mvalue;
	}
	public void setReport_mvalue(BigDecimal report_mvalue) {
		this.report_mvalue = report_mvalue;
	}
	public BigDecimal getReport_mvalue_after() {
		return report_mvalue_after;
	}
	public void setReport_mvalue_after(BigDecimal report_mvalue_after) {
		this.report_mvalue_after = report_mvalue_after;
	}
	public String getText_value() {
		return text_value;
	}
	public void setText_value(String text_value) {
		this.text_value = text_value;
	}
	public String getText_value_after() {
		return text_value_after;
	}
	public void setText_value_after(String text_value_after) {
		this.text_value_after = text_value_after;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getOper_date() {
		return oper_date;
	}
	public void setOper_date(String oper_date) {
		this.oper_date = oper_date;
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
	public String getReportid() {
		return reportid;
	}
	public void setReportid(String reportid) {
		this.reportid = reportid;
	}
	
	
	
}
