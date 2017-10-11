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
@Table(name="His_Interface_Save")
public class His_Interface_Save implements java.io.Serializable {
	@EmbeddedId
	@AttributeOverrides({
		@AttributeOverride(name = "tablecode", column = @Column(name="tablecode",nullable = false, length = 20)),
		@AttributeOverride(name = "accountcode",column = @Column(name="accountcode",nullable=false,length=30)),
		@AttributeOverride(name = "yearmonth",column = @Column(name="yearmonth",nullable=false,length=200)),
		@AttributeOverride(name = "datetime",column = @Column(name="datetime",nullable=false,length=20)),
		@AttributeOverride(name = "year",column = @Column(name="year",nullable=false,length=20)),
		@AttributeOverride(name = "quarter",column = @Column(name="quarter",nullable=false,length=20)),
		@AttributeOverride(name = "status",column = @Column(name="status",nullable=false,length=20))
	})
	private His_Interface_SaveId id;
	
	@Column
	private String tablename;
	@Column
	private String accountname;
	@Column
	private BigDecimal balance_qc;
	@Column
	private BigDecimal debit_bq;
	@Column
	private BigDecimal credit_bq;
	@Column
	private BigDecimal balance_qm;
	@Column
	private BigDecimal interestvalue;
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
	@Column
	private String spare11;
	@Column
	private String spare12;
	@Column
	private String spare13;
	@Column
	private String spare14;
	@Column
	private String spare15;
	@Column
	public His_Interface_SaveId getId() {
		return id;
	}
	public void setId(His_Interface_SaveId id) {
		this.id = id;
	}
	public String getTablename() {
		return tablename;
	}
	public void setTablename(String tablename) {
		this.tablename = tablename;
	}
	public String getAccountname() {
		return accountname;
	}
	public void setAccountname(String accountname) {
		this.accountname = accountname;
	}
	public BigDecimal getBalance_qc() {
		return balance_qc;
	}
	public void setBalance_qc(BigDecimal balance_qc) {
		this.balance_qc = balance_qc;
	}
	public BigDecimal getDebit_bq() {
		return debit_bq;
	}
	public void setDebit_bq(BigDecimal debit_bq) {
		this.debit_bq = debit_bq;
	}
	public BigDecimal getCredit_bq() {
		return credit_bq;
	}
	public void setCredit_bq(BigDecimal credit_bq) {
		this.credit_bq = credit_bq;
	}
	public BigDecimal getBalance_qm() {
		return balance_qm;
	}
	public void setBalance_qm(BigDecimal balance_qm) {
		this.balance_qm = balance_qm;
	}
	public BigDecimal getInterestvalue() {
		return interestvalue;
	}
	public void setInterestvalue(BigDecimal interestvalue) {
		this.interestvalue = interestvalue;
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
	public String getSpare11() {
		return spare11;
	}
	public void setSpare11(String spare11) {
		this.spare11 = spare11;
	}
	public String getSpare12() {
		return spare12;
	}
	public void setSpare12(String spare12) {
		this.spare12 = spare12;
	}
	public String getSpare13() {
		return spare13;
	}
	public void setSpare13(String spare13) {
		this.spare13 = spare13;
	}
	public String getSpare14() {
		return spare14;
	}
	public void setSpare14(String spare14) {
		this.spare14 = spare14;
	}
	public String getSpare15() {
		return spare15;
	}
	public void setSpare15(String spare15) {
		this.spare15 = spare15;
	}
	
}
