package com.sinosoft.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class His_Cux_Interface_BondInfoId implements java.io.Serializable{
	@Column
	private String year;
	@Column
	private String quarter;
	@Column
	private String BondType;	
	@Column
	private String yearMonth;
	@Column
	private String securityCode;
	@Column
	private String securityName;
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
	public String getBondType() {
		return BondType;
	}
	public void setBondType(String bondType) {
		BondType = bondType;
	}
	public String getYearMonth() {
		return yearMonth;
	}
	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}
	public String getSecurityCode() {
		return securityCode;
	}
	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}
	public String getSecurityName() {
		return securityName;
	}
	public void setSecurityName(String securityName) {
		this.securityName = securityName;
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
	public His_Cux_Interface_BondInfoId(String year, String quarter,
			String bondType, String yearMonth, String securityCode,
			String securityName, Date datadate, String status) {
		super();
		this.year = year;
		this.quarter = quarter;
		BondType = bondType;
		this.yearMonth = yearMonth;
		this.securityCode = securityCode;
		this.securityName = securityName;
		this.datadate = datadate;
		this.status = status;
	}
	public His_Cux_Interface_BondInfoId() {
		super();
	}
	
}
