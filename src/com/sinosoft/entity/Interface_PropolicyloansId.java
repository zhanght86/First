package com.sinosoft.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Interface_PropolicyloansId implements java.io.Serializable {

	@Column
	private String productcode;
	@Column
	private String productname;
	@Column
	private String yearmonth;
	@Column
	private Date datetime;
	@Column
	private String year;
	@Column 
	private String quarter;
	public String getProductcode() {
		return productcode;
	}
	public void setProductcode(String productcode) {
		this.productcode = productcode;
	}
	public String getProductname() {
		return productname;
	}
	public void setProductname(String productname) {
		this.productname = productname;
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
	public Interface_PropolicyloansId(String productcode, String productname,
			String yearmonth, Date datetime, String year, String quarter) {
		super();
		this.productcode = productcode;
		this.productname = productname;
		this.yearmonth = yearmonth;
		this.datetime = datetime;
		this.year = year;
		this.quarter = quarter;
	}
	public Interface_PropolicyloansId() {
		super();
	}
	
	
}
