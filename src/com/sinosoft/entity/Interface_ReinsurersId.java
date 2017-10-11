package com.sinosoft.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Interface_ReinsurersId implements java.io.Serializable{
	@Column
	private String itemcode;
	@Column
	private String itemname;
	@Column
	private String yearmonth;
	@Column
	private String remark;
	@Column
	private Date datetime;
	@Column
	private String year;
	@Column 
	private String quarter;
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
	public String getYearmonth() {
		return yearmonth;
	}
	public void setYearmonth(String yearmonth) {
		this.yearmonth = yearmonth;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public Interface_ReinsurersId(String itemcode, String itemname,
			String yearmonth, String remark, Date datetime, String year,
			String quarter) {
		super();
		this.itemcode = itemcode;
		this.itemname = itemname;
		this.yearmonth = yearmonth;
		this.remark = remark;
		this.datetime = datetime;
		this.year = year;
		this.quarter = quarter;
	}
	public Interface_ReinsurersId() {
		super();
	}
	
}
