package com.sinosoft.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class HandsetFund_Id implements java.io.Serializable{
	@Column
	private String fundcode;
	@Column
	private String fundname;
	public String getFundcode() {
		return fundcode;
	}
	public void setFundcode(String fundcode) {
		this.fundcode = fundcode;
	}
	public String getFundname() {
		return fundname;
	}
	public void setFundname(String fundname) {
		this.fundname = fundname;
	}
	
}
