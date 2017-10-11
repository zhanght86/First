package com.sinosoft.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class HandsetStock_Id implements java.io.Serializable{
	@Column
	private String exchangemarket;
	@Column
	private String stockcode;
	@Column
	private String stockname;
	public String getExchangemarket() {
		return exchangemarket;
	}
	public void setExchangemarket(String exchangemarket) {
		this.exchangemarket = exchangemarket;
	}
	public String getStockcode() {
		return stockcode;
	}
	public void setStockcode(String stockcode) {
		this.stockcode = stockcode;
	}
	public String getStockname() {
		return stockname;
	}
	public void setStockname(String stockname) {
		this.stockname = stockname;
	}
	
}
