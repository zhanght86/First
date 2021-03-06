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
@Table(name = "His_Cux_Sino_Interface_Stock")
public class His_Cux_Sino_Interface_Stock {
	@EmbeddedId
	@AttributeOverrides({
		    @AttributeOverride(name = "year", column = @Column(name = "Year", nullable = false, length = 20)),
		    @AttributeOverride(name = "quarter", column = @Column(name = "quarter", nullable = false, length = 20)),
			@AttributeOverride(name = "yearMonth", column = @Column(name = "YearMonth", nullable = false, length = 20)),
            @AttributeOverride(name = "securityCode", column = @Column(name = "SecurityCode", nullable = false, length = 20)),
            @AttributeOverride(name = "securityName", column = @Column(name = "SecurityName", nullable = false, length = 20)),
            @AttributeOverride(name = "datadate", column = @Column(name = "datadate", nullable = false, length = 20)),
            @AttributeOverride(name = "status", column = @Column(name = "status", nullable = false, length = 20))
	})
	private His_Cux_Sino_Interface_StockId id;
	@Column
	private Date impDate;
	@Column
	private String impOperator;
	@Column
	private String marketBoard;
	@Column
	private BigDecimal totalMarketValue;
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
	public His_Cux_Sino_Interface_StockId getId() {
		return id;
	}
	public void setId(His_Cux_Sino_Interface_StockId id) {
		this.id = id;
	}
	public Date getImpDate() {
		return impDate;
	}
	public void setImpDate(Date impDate) {
		this.impDate = impDate;
	}
	public String getImpOperator() {
		return impOperator;
	}
	public void setImpOperator(String impOperator) {
		this.impOperator = impOperator;
	}
	public String getMarketBoard() {
		return marketBoard;
	}
	public void setMarketBoard(String marketBoard) {
		this.marketBoard = marketBoard;
	}
	public BigDecimal getTotalMarketValue() {
		return totalMarketValue;
	}
	public void setTotalMarketValue(BigDecimal totalMarketValue) {
		this.totalMarketValue = totalMarketValue;
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
	
	
}
