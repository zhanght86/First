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
@Table(name = "Interface_Risk")
public class Interface_Risk {
	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "productCode", column = @Column(name = "productCode", nullable = false, length = 30)),
			@AttributeOverride(name = "productName", column = @Column(name = "productName", nullable = false, length = 60)),
			@AttributeOverride(name = "year", column = @Column(name = "year", nullable = false, length = 60)),
			@AttributeOverride(name = "quarter", column = @Column(name = "quarter", nullable = false, length = 60)),
			@AttributeOverride(name = "yearmonth", column = @Column(name = "yearmonth", nullable = false, length = 60))})
			
	private Interface_RiskId id;
	@Column
	private String riskCode;
	@Column
	private String riskName;
	@Column
	private int flag;
	@Column
	private String firstRiskType;
	@Column
	private String firstRiskTypeName;
	@Column
	private String secondRiskType;
	@Column
	private String secondRiskTypeName;
	@Column
	private String thirdRiskType;
	@Column
	private String thirdRiskTypeName;
	@Column
	private String payPeriod;
	@Column
	private String payType;
	@Column
	private String channel;
	@Column
	private String longOrShortRisk;
	@Column
	private String primaryOrAddRisk;
	@Column
	private BigDecimal rate;
	@Column
	private String accountType;
	@Column
	private int dutyFreeOrNot;
	@Column
	private Date dateTime;

	public Interface_RiskId getId() {
		return id;
	}

	public void setId(Interface_RiskId id) {
		this.id = id;
	}

	public String getRiskCode() {
		return riskCode;
	}

	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}

	public String getRiskName() {
		return riskName;
	}

	public void setRiskName(String riskName) {
		this.riskName = riskName;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getFirstRiskType() {
		return firstRiskType;
	}

	public void setFirstRiskType(String firstRiskType) {
		this.firstRiskType = firstRiskType;
	}

	public String getFirstRiskTypeName() {
		return firstRiskTypeName;
	}

	public void setFirstRiskTypeName(String firstRiskTypeName) {
		this.firstRiskTypeName = firstRiskTypeName;
	}

	public String getSecondRiskType() {
		return secondRiskType;
	}

	public void setSecondRiskType(String secondRiskType) {
		this.secondRiskType = secondRiskType;
	}

	public String getSecondRiskTypeName() {
		return secondRiskTypeName;
	}

	public void setSecondRiskTypeName(String secondRiskTypeName) {
		this.secondRiskTypeName = secondRiskTypeName;
	}

	public String getThirdRiskType() {
		return thirdRiskType;
	}

	public void setThirdRiskType(String thirdRiskType) {
		this.thirdRiskType = thirdRiskType;
	}

	public String getThirdRiskTypeName() {
		return thirdRiskTypeName;
	}

	public void setThirdRiskTypeName(String thirdRiskTypeName) {
		this.thirdRiskTypeName = thirdRiskTypeName;
	}

	public String getPayPeriod() {
		return payPeriod;
	}

	public void setPayPeriod(String payPeriod) {
		this.payPeriod = payPeriod;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getLongOrShortRisk() {
		return longOrShortRisk;
	}

	public void setLongOrShortRisk(String longOrShortRisk) {
		this.longOrShortRisk = longOrShortRisk;
	}

	public String getPrimaryOrAddRisk() {
		return primaryOrAddRisk;
	}

	public void setPrimaryOrAddRisk(String primaryOrAddRisk) {
		this.primaryOrAddRisk = primaryOrAddRisk;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public int getDutyFreeOrNot() {
		return dutyFreeOrNot;
	}

	public void setDutyFreeOrNot(int dutyFreeOrNot) {
		this.dutyFreeOrNot = dutyFreeOrNot;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

}
