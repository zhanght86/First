package com.sinosoft.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Cux_Sino_Interface_DepositId generated by hbm2java
 */
@Embeddable
public class Cux_Sino_Interface_DepositId implements java.io.Serializable{
	@Column
	private String depositType;
	@Column
	private String yearMonth;
	@Column
	private String bookType;
	@Column
	private Date startDate;
	@Column
	private Date endDate;
	@Column
	private String principalItemCode;
	@Column
	private String interestItem;
	@Column
	private String interestIncomeItem;
	public String getDepositType() {
		return depositType;
	}
	public void setDepositType(String depositType) {
		this.depositType = depositType;
	}
	public String getYearMonth() {
		return yearMonth;
	}
	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}
	public Cux_Sino_Interface_DepositId(String depositType, String yearMonth, String bookType, Date startDate,
			Date endDate, String principalItemCode, String interestItem, String interestIncomeItem) {
		super();
		this.depositType = depositType;
		this.yearMonth = yearMonth;
		this.bookType = bookType;
		this.startDate = startDate;
		this.endDate = endDate;
		this.principalItemCode = principalItemCode;
		this.interestItem = interestItem;
		this.interestIncomeItem = interestIncomeItem;
	}
	public Cux_Sino_Interface_DepositId() {
		super();
	}
	public String getBookType() {
		return bookType;
	}
	public void setBookType(String bookType) {
		this.bookType = bookType;
	}
	public String getPrincipalItemCode() {
		return principalItemCode;
	}
	public void setPrincipalItemCode(String principalItemCode) {
		this.principalItemCode = principalItemCode;
	}
	public String getInterestItem() {
		return interestItem;
	}
	public void setInterestItem(String interestItem) {
		this.interestItem = interestItem;
	}
	public String getInterestIncomeItem() {
		return interestIncomeItem;
	}
	public void setInterestIncomeItem(String interestIncomeItem) {
		this.interestIncomeItem = interestIncomeItem;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
}
