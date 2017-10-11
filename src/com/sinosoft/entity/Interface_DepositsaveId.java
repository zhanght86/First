package com.sinosoft.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Interface_DepositsaveId implements java.io.Serializable {
	@Column
	private String bankName;
	@Column
	private String bankAccount;
	@Column
	private String accountCode;
	@Column
	private BigDecimal amount;
	@Column
	private String saveType;

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getSaveType() {
		return saveType;
	}

	public void setSaveType(String saveType) {
		this.saveType = saveType;
	}

	public Interface_DepositsaveId(String bankName, String bankAccount, String accountCode, BigDecimal amount,
			String saveType) {
		super();
		this.bankName = bankName;
		this.bankAccount = bankAccount;
		this.accountCode = accountCode;
		this.amount = amount;
		this.saveType = saveType;
	}

	public Interface_DepositsaveId() {
		super();
		// TODO Auto-generated constructor stub
	}

}
