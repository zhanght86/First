package com.sinosoft.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
@Embeddable
public class HandsetSecurity_Id implements java.io.Serializable{
	@Column
	private String exchangemarket;
	@Column
	private String securitycode;
	@Column
	private String securityname;
	public String getExchangemarket() {
		return exchangemarket;
	}
	public void setExchangemarket(String exchangemarket) {
		this.exchangemarket = exchangemarket;
	}
	public String getSecuritycode() {
		return securitycode;
	}
	public void setSecuritycode(String securitycode) {
		this.securitycode = securitycode;
	}
	public String getSecurityname() {
		return securityname;
	}
	public void setSecurityname(String securityname) {
		this.securityname = securityname;
	}
	
}
