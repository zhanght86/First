package com.sinosoft.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Cux_Sino_Interface_StockId generated by hbm2java
 */
@Embeddable
public class Cux_Sino_Interface_StockId implements java.io.Serializable {
	@Column
	private String yearMonth;
	@Column
	private String securityCode;
	@Column
	private String securityName;
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
	public Cux_Sino_Interface_StockId(String yearMonth, String securityCode, String securityName) {
		super();
		this.yearMonth = yearMonth;
		this.securityCode = securityCode;
		this.securityName = securityName;
	}
	public Cux_Sino_Interface_StockId() {
		super();
	}
	
}
