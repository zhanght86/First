package com.sinosoft.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class His_Cux_Interface_ValuationId implements java.io.Serializable {
	@Column
	private String year;
	@Column
	private String quarter;
	@Column
	private String bookType;
	@Column
	private String yearMonth;
    @Column
	private String itemCode;
	@Column
	private String itemName;
	@Column
	private Date dataDate;
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
	public String getBookType() {
		return bookType;
	}
	public void setBookType(String bookType) {
		this.bookType = bookType;
	}
	public String getYearMonth() {
		return yearMonth;
	}
	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public Date getDataDate() {
		return dataDate;
	}
	public void setDataDate(Date dataDate) {
		this.dataDate = dataDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public His_Cux_Interface_ValuationId(String year, String quarter,
			String bookType, String yearMonth, String itemCode,
			String itemName, Date dataDate, String status) {
		super();
		this.year = year;
		this.quarter = quarter;
		this.bookType = bookType;
		this.yearMonth = yearMonth;
		this.itemCode = itemCode;
		this.itemName = itemName;
		this.dataDate = dataDate;
		this.status = status;
	}
	public His_Cux_Interface_ValuationId() {
		super();
	}
	
}
