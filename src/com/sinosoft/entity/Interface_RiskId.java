package com.sinosoft.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Interface_RiskId implements java.io.Serializable {
	@Column
	private String productCode;
	@Column
	private String productName;
	@Column
	private String year;
	@Column
	private String quarter;
	@Column
	private String yearmonth;

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

	public String getYearmonth() {
		return yearmonth;
	}

	public void setYearmonth(String yearmonth) {
		this.yearmonth = yearmonth;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Interface_RiskId(String productCode, String productName,String year,String quarter,String yearmonth) {
		super();
		this.productCode = productCode;
		this.productName = productName;
		this.year=year;
		this.quarter=quarter;
		this.yearmonth=yearmonth;
	}

	public Interface_RiskId() {
		super();
		// TODO Auto-generated constructor stub
	}

}
