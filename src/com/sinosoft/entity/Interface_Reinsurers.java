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
@Table(name="Interface_Reinsurers")
public class Interface_Reinsurers {
	@EmbeddedId
	@AttributeOverrides({
		@AttributeOverride(name = "itemcode", column = @Column(name="itemcode",nullable = false, length = 20)),
		@AttributeOverride(name = "itemname",column = @Column(name="itemname",nullable=false,length=200)),
		@AttributeOverride(name = "yearmonth",column = @Column(name="yearmonth",nullable=false,length=20)),
		@AttributeOverride(name = "remark",column = @Column(name="remark",nullable=false,length=20)),
		@AttributeOverride(name = "datetime",column = @Column(name="datetime",nullable=false,length=20)),
		@AttributeOverride(name = "year",column = @Column(name="year",nullable=false,length=20)),
		@AttributeOverride(name = "quarter",column = @Column(name="quarter",nullable=false,length=20))
	})
	private Interface_ReinsurersId id;
	
	@Column
	private BigDecimal sumdebit;
	@Column
	private BigDecimal sumcredit;
	@Column
	private String spare1;
	@Column
	private String spare2;
	@Column
	private String spare3;
	@Column
	private String spare4;
	@Column
	private String spare5;
	@Column
	private String spare6;
	@Column
	private String spare7;
	@Column
	private String spare8;
	@Column
	private String spare9;
	@Column
	private String spare10;
	public Interface_ReinsurersId getId() {
		return id;
	}
	public void setId(Interface_ReinsurersId id) {
		this.id = id;
	}
	public BigDecimal getSumdebit() {
		return sumdebit;
	}
	public void setSumdebit(BigDecimal sumdebit) {
		this.sumdebit = sumdebit;
	}
	public BigDecimal getSumcredit() {
		return sumcredit;
	}
	public void setSumcredit(BigDecimal sumcredit) {
		this.sumcredit = sumcredit;
	}
	public String getSpare1() {
		return spare1;
	}
	public void setSpare1(String spare1) {
		this.spare1 = spare1;
	}
	public String getSpare2() {
		return spare2;
	}
	public void setSpare2(String spare2) {
		this.spare2 = spare2;
	}
	public String getSpare3() {
		return spare3;
	}
	public void setSpare3(String spare3) {
		this.spare3 = spare3;
	}
	public String getSpare4() {
		return spare4;
	}
	public void setSpare4(String spare4) {
		this.spare4 = spare4;
	}
	public String getSpare5() {
		return spare5;
	}
	public void setSpare5(String spare5) {
		this.spare5 = spare5;
	}
	public String getSpare6() {
		return spare6;
	}
	public void setSpare6(String spare6) {
		this.spare6 = spare6;
	}
	public String getSpare7() {
		return spare7;
	}
	public void setSpare7(String spare7) {
		this.spare7 = spare7;
	}
	public String getSpare8() {
		return spare8;
	}
	public void setSpare8(String spare8) {
		this.spare8 = spare8;
	}
	public String getSpare9() {
		return spare9;
	}
	public void setSpare9(String spare9) {
		this.spare9 = spare9;
	}
	public String getSpare10() {
		return spare10;
	}
	public void setSpare10(String spare10) {
		this.spare10 = spare10;
	}
	
	
}
