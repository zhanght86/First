package com.sinosoft.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class His_Interface_BalanceId implements java.io.Serializable{
	@Column
	private String tablecode;
	@Column
	private String itemcode;
	@Column
	private String yearmonth;
	@Column
	private Date datetime;
	@Column
	private String year;
	@Column 
	private String quarter;
	@Column
	private String status;
	public String getTablecode() {
		return tablecode;
	}
	public void setTablecode(String tablecode) {
		this.tablecode = tablecode;
	}
	public String getItemcode() {
		return itemcode;
	}
	public void setItemcode(String itemcode) {
		this.itemcode = itemcode;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public His_Interface_BalanceId(String tablecode, String itemcode,
			String yearmonth, Date datetime, String year, String quarter,
			String status) {
		super();
		this.tablecode = tablecode;
		this.itemcode = itemcode;
		this.yearmonth = yearmonth;
		this.datetime = datetime;
		this.year = year;
		this.quarter = quarter;
		this.status = status;
	}
	public His_Interface_BalanceId() {
		super();
	}
	
	
}
