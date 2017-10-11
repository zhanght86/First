package com.sinosoft.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class InterTabInvestInfoId implements java.io.Serializable {
	@Column
    private String reportId; 
	@Column
    private String properCode;     
    @Column
    private String accounts; 
    @Column
    private String temp1;  
	
    public String getAccounts() {
		return accounts;
	}
	public void setAccounts(String accounts) {
		this.accounts = accounts;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public String getProperCode() {
		return properCode;
	}
	public void setProperCode(String properCode) {
		this.properCode = properCode;
	}
	public String getTemp1() {
		return temp1;
	}
	public void setTemp1(String temp1) {
		this.temp1 = temp1;
	}
	public InterTabInvestInfoId(String reportId, String properCode,String accounts,String temp1) {
		super();
		this.reportId = reportId;
		this.properCode = properCode;	
		this.accounts = accounts;
		this.temp1 = temp1;
	}
	public InterTabInvestInfoId() {
		super();
	}

}
