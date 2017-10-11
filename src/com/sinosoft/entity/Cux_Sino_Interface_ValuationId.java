package com.sinosoft.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Cux_Sino_Interface_ValuationId generated by hbm2java
 */
@Embeddable
public class Cux_Sino_Interface_ValuationId  implements java.io.Serializable{
	@Column
	private String bookType;
	@Column
	private String yearMonth;
    @Column
	private String itemCode;
	@Column
	private String itemName;
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

	public Cux_Sino_Interface_ValuationId(String bookType, String yearMonth, String itemCode, String itemName) {
		super();
		this.bookType = bookType;
		this.yearMonth = yearMonth;
		this.itemCode = itemCode;
		this.itemName = itemName;
	}
	public Cux_Sino_Interface_ValuationId() {
		super();
	}
	
}
