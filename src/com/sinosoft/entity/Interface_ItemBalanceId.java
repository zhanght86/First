package com.sinosoft.entity;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;



@Embeddable
public class Interface_ItemBalanceId implements java.io.Serializable {
	
	@Column
	private String yearMonth;
	@Column
	private String itemcode;
	@Column
	private String itemname;
	@Column
	private Date datetime;
	@Column
	private String year;
	@Column 
	private String quarter;
	public String getYearMonth() {
		return yearMonth;
	}
	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}
	public String getItemcode() {
		return itemcode;
	}
	public void setItemcode(String itemcode) {
		this.itemcode = itemcode;
	}
	public String getItemname() {
		return itemname;
	}
	public void setItemname(String itemname) {
		this.itemname = itemname;
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
	public Interface_ItemBalanceId(String yearMonth, String itemcode,
			String itemname, Date datetime, String year, String quarter) {
		super();
		this.yearMonth = yearMonth;
		this.itemcode = itemcode;
		this.itemname = itemname;
		this.datetime = datetime;
		this.year = year;
		this.quarter = quarter;
	}
	public Interface_ItemBalanceId() {
		super();
	}
	
}
