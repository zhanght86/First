package com.sinosoft.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Cux_Sino_Interface_Valuation generated by hbm2java
 */
@Entity
@Table(name = "Cux_Sino_Interface_Valuation")
public class Cux_Sino_Interface_Valuation {
	  @EmbeddedId
	  @AttributeOverrides({
			@AttributeOverride(name = "bookType", column = @Column(name = "BookType", nullable = false, length = 20)),
			@AttributeOverride(name = "yearMonth", column = @Column(name = "YearMonth", nullable = false, length = 20)),
			@AttributeOverride(name = "itemCode", column = @Column(name = "ItemCode", nullable = false, length = 50)),
			@AttributeOverride(name = "itemName", column = @Column(name = "ItemName", nullable = false, length = 100)) })
	  private Cux_Sino_Interface_ValuationId id;
	  @Column
	  private Date impDate;
	  @Column
	  private String impOperator;
	  @Column
	  private Date updateDate;
	  @Column
	  private String updateOperator;
	  @Column
	  private Date dataDate;
	  @Column
	  private BigDecimal amount;
	  @Column
	  private BigDecimal unitCost;
	  @Column
	  private BigDecimal cost;
	  @Column
	  private BigDecimal costForAccountPersent;
	  @Column
	  private BigDecimal marketPrice;
	  @Column
	  private BigDecimal marketValue;
	  @Column
	  private BigDecimal marketValueForAccountPersent;
	  @Column
	  private BigDecimal valuationAppreciation;
	  @Column
	  private String suspensionInfo;
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
	public Cux_Sino_Interface_ValuationId getId() {
		return id;
	}
	public void setId(Cux_Sino_Interface_ValuationId id) {
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
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getUpdateOperator() {
		return updateOperator;
	}
	public void setUpdateOperator(String updateOperator) {
		this.updateOperator = updateOperator;
	}
	public Date getDataDate() {
		return dataDate;
	}
	public void setDataDate(Date dataDate) {
		this.dataDate = dataDate;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public BigDecimal getUnitCost() {
		return unitCost;
	}
	public void setUnitCost(BigDecimal unitCost) {
		this.unitCost = unitCost;
	}
	public BigDecimal getCost() {
		return cost;
	}
	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}
	public BigDecimal getCostForAccountPersent() {
		return costForAccountPersent;
	}
	public void setCostForAccountPersent(BigDecimal costForAccountPersent) {
		this.costForAccountPersent = costForAccountPersent;
	}
	public BigDecimal getMarketPrice() {
		return marketPrice;
	}
	public void setMarketPrice(BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}
	public BigDecimal getMarketValue() {
		return marketValue;
	}
	public void setMarketValue(BigDecimal marketValue) {
		this.marketValue = marketValue;
	}
	public BigDecimal getMarketValueForAccountPersent() {
		return marketValueForAccountPersent;
	}
	public void setMarketValueForAccountPersent(BigDecimal marketValueForAccountPersent) {
		this.marketValueForAccountPersent = marketValueForAccountPersent;
	}
	public BigDecimal getValuationAppreciation() {
		return valuationAppreciation;
	}
	public void setValuationAppreciation(BigDecimal valuationAppreciation) {
		this.valuationAppreciation = valuationAppreciation;
	}
	public String getSuspensionInfo() {
		return suspensionInfo;
	}
	public void setSuspensionInfo(String suspensionInfo) {
		this.suspensionInfo = suspensionInfo;
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