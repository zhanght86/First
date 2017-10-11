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
@Table(name = "His_Cux_Interface_HS300Stock")
public class His_Cux_Interface_HS300Stock {
	
	@EmbeddedId
	@AttributeOverrides({
	    	@AttributeOverride(name = "year", column = @Column(name = "year", nullable = false, length = 20)),
		    @AttributeOverride(name = "quarter", column = @Column(name = "quarter", nullable = false, length = 20)),
			@AttributeOverride(name = "yearMonth", column = @Column(name = "YearMonth", nullable = false, length = 20)),
            @AttributeOverride(name = "code", column = @Column(name = "Code", nullable = false, length = 20)),
            @AttributeOverride(name = "name", column = @Column(name = "Name", nullable = false, length = 20)),
            @AttributeOverride(name = "datadate", column = @Column(name = "datadate", nullable = false, length = 20)),
            @AttributeOverride(name = "status", column = @Column(name = "status", nullable = false, length = 20))
	    	})
	private His_Cux_Interface_HS300StockId id;
	
	@Column
	private Date impDate;
	@Column
	private String impOperator;
	@Column
	private BigDecimal rank;
	@Column
	private BigDecimal close;
	@Column
	private BigDecimal weight;
	@Column
	private BigDecimal upOrDowm;
	@Column
	private BigDecimal changeFloat;
	@Column
	private BigDecimal indexContributePoint;
	@Column
	private BigDecimal volume;
	@Column
	private BigDecimal turnVolume;
	@Column
	private BigDecimal totalShares;
	@Column
	private BigDecimal flowShares;
	@Column
	private BigDecimal totalMarketValue;
	@Column
	private BigDecimal flowMarketValue;
	@Column
	private String commissionIndustry;
	@Column
	private String windIndustry;
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
	
	public His_Cux_Interface_HS300StockId getId() {
		return id;
	}
	public void setId(His_Cux_Interface_HS300StockId id) {
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
	public BigDecimal getRank() {
		return rank;
	}
	public void setRank(BigDecimal rank) {
		this.rank = rank;
	}
	public BigDecimal getClose() {
		return close;
	}
	public void setClose(BigDecimal close) {
		this.close = close;
	}
	public BigDecimal getWeight() {
		return weight;
	}
	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}
	public BigDecimal getUpOrDowm() {
		return upOrDowm;
	}
	public void setUpOrDowm(BigDecimal upOrDowm) {
		this.upOrDowm = upOrDowm;
	}
	public BigDecimal getChangeFloat() {
		return changeFloat;
	}
	public void setChangeFloat(BigDecimal changeFloat) {
		this.changeFloat = changeFloat;
	}
	public BigDecimal getIndexContributePoint() {
		return indexContributePoint;
	}
	public void setIndexContributePoint(BigDecimal indexContributePoint) {
		this.indexContributePoint = indexContributePoint;
	}
	public BigDecimal getVolume() {
		return volume;
	}
	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}
	public BigDecimal getTurnVolume() {
		return turnVolume;
	}
	public void setTurnVolume(BigDecimal turnVolume) {
		this.turnVolume = turnVolume;
	}
	public BigDecimal getTotalShares() {
		return totalShares;
	}
	public void setTotalShares(BigDecimal totalShares) {
		this.totalShares = totalShares;
	}
	public BigDecimal getFlowShares() {
		return flowShares;
	}
	public void setFlowShares(BigDecimal flowShares) {
		this.flowShares = flowShares;
	}
	public BigDecimal getTotalMarketValue() {
		return totalMarketValue;
	}
	public void setTotalMarketValue(BigDecimal totalMarketValue) {
		this.totalMarketValue = totalMarketValue;
	}
	public BigDecimal getFlowMarketValue() {
		return flowMarketValue;
	}
	public void setFlowMarketValue(BigDecimal flowMarketValue) {
		this.flowMarketValue = flowMarketValue;
	}
	public String getCommissionIndustry() {
		return commissionIndustry;
	}
	public void setCommissionIndustry(String commissionIndustry) {
		this.commissionIndustry = commissionIndustry;
	}
	public String getWindIndustry() {
		return windIndustry;
	}
	public void setWindIndustry(String windIndustry) {
		this.windIndustry = windIndustry;
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
