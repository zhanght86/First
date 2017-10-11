package com.sinosoft.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class His_Cux_Sino_Interface_DepositId implements java.io.Serializable {
	
	@Column
	private String year;
	@Column
	private String quarter;
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
	@Column
	private Date dataDate;
	public Date getDataDate() {
		return dataDate;
	}
	public void setDataDate(Date dataDate) {
		this.dataDate = dataDate;
	}
	@Column
	private String status;
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
	public String getBookType() {
		return bookType;
	}
	public void setBookType(String bookType) {
		this.bookType = bookType;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public His_Cux_Sino_Interface_DepositId(String year, String quarter,
			String depositType, String yearMonth, String bookType,
			Date startDate, Date endDate, String principalItemCode,
			String interestItem, String interestIncomeItem, Date dataDate,
			String status) {
		super();
		this.year = year;
		this.quarter = quarter;
		this.depositType = depositType;
		this.yearMonth = yearMonth;
		this.bookType = bookType;
		this.startDate = startDate;
		this.endDate = endDate;
		this.principalItemCode = principalItemCode;
		this.interestItem = interestItem;
		this.interestIncomeItem = interestIncomeItem;
		this.dataDate = dataDate;
		this.status = status;
	}
	public His_Cux_Sino_Interface_DepositId() {
		super();
	}
	

}
