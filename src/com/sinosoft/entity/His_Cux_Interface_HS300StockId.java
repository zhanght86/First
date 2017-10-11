package com.sinosoft.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class His_Cux_Interface_HS300StockId implements java.io.Serializable {
	@Column
	private String year;
	@Column
	private String quarter;
	@Column
	private String yearMonth;
	@Column
	private String code;
	@Column
	private String name;
	@Column
	private Date datadate;
	@Column
	private String status;
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
	public String getYearMonth() {
		return yearMonth;
	}
	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getDatadate() {
		return datadate;
	}
	public void setDatadate(Date datadate) {
		this.datadate = datadate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public His_Cux_Interface_HS300StockId(String year, String quarter,
			String yearMonth, String code, String name, Date datadate,
			String status) {
		super();
		this.year = year;
		this.quarter = quarter;
		this.yearMonth = yearMonth;
		this.code = code;
		this.name = name;
		this.datadate = datadate;
		this.status = status;
	}
	public His_Cux_Interface_HS300StockId() {
		super();
	}
	
	
}
