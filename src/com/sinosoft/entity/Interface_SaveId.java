package com.sinosoft.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Interface_SaveId implements java.io.Serializable{
	@Column
	private String tablecode;
	@Column
	private String accountcode;
	@Column
	private String yearmonth;
	@Column
	private Date datetime;
	@Column
	private String year;
	@Column 
	private String quarter;
	public String getTablecode() {
		return tablecode;
	}
	public void setTablecode(String tablecode) {
		this.tablecode = tablecode;
	}
	public String getAccountcode() {
		return accountcode;
	}
	public void setAccountcode(String accountcode) {
		this.accountcode = accountcode;
	}
	public String getYearmonth() {
		return yearmonth;
	}
	public void setYearmonth(String yearmonth) {
		this.yearmonth = yearmonth;
	}
	public Date getDatetime() {
		return datetime;
	}
	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}
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
	public Interface_SaveId(String tablecode, String accountcode,
			String yearmonth, Date datetime, String year, String quarter) {
		super();
		this.tablecode = tablecode;
		this.accountcode = accountcode;
		this.yearmonth = yearmonth;
		this.datetime = datetime;
		this.year = year;
		this.quarter = quarter;
	}
	public Interface_SaveId() {
		super();
	}
	
	
}
