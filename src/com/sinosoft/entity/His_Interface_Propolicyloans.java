package com.sinosoft.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="His_Interface_Propolicyloans")
public class His_Interface_Propolicyloans {
	@EmbeddedId
	@AttributeOverrides({
		@AttributeOverride(name = "productcode", column = @Column(name="productcode",nullable = false, length = 20)),
		@AttributeOverride(name = "productname",column = @Column(name="productname",nullable=false,length=200)),
		@AttributeOverride(name = "yearmonth",column = @Column(name="yearmonth",nullable=false,length=20)),
		@AttributeOverride(name = "datetime",column = @Column(name="datetime",nullable=false,length=20)),
		@AttributeOverride(name = "year",column = @Column(name="year",nullable=false,length=20)),
		@AttributeOverride(name = "quarter",column = @Column(name="quarter",nullable=false,length=20)),
		@AttributeOverride(name = "status",column = @Column(name="status",nullable=false,length=20))
	})
	private His_Interface_PropolicyloansId id;
	@Column
	private BigDecimal premiumsreceivable;
	@Column
	private BigDecimal policyloans;
	@Column
	private BigDecimal interestreceivable;
	@Column
	private String accounttype;
	public His_Interface_PropolicyloansId getId() {
		return id;
	}
	public void setId(His_Interface_PropolicyloansId id) {
		this.id = id;
	}
	public BigDecimal getPremiumsreceivable() {
		return premiumsreceivable;
	}
	public void setPremiumsreceivable(BigDecimal premiumsreceivable) {
		this.premiumsreceivable = premiumsreceivable;
	}
	public BigDecimal getPolicyloans() {
		return policyloans;
	}
	public void setPolicyloans(BigDecimal policyloans) {
		this.policyloans = policyloans;
	}
	public BigDecimal getInterestreceivable() {
		return interestreceivable;
	}
	public void setInterestreceivable(BigDecimal interestreceivable) {
		this.interestreceivable = interestreceivable;
	}
	public String getAccounttype() {
		return accounttype;
	}
	public void setAccounttype(String accounttype) {
		this.accounttype = accounttype;
	}
}
