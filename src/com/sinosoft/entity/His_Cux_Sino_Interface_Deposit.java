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
@Table(name = "His_Cux_Sino_Interface_Deposit")
public class His_Cux_Sino_Interface_Deposit {
	@EmbeddedId
	@AttributeOverrides({
		    @AttributeOverride(name = "year", column = @Column(name = "year", nullable = false, length = 20)),
    		@AttributeOverride(name = "quarter", column = @Column(name = "quarter", nullable = false, length = 20)),
			@AttributeOverride(name = "depositType", column = @Column(name = "DepositType", nullable = false, length = 20)),
			@AttributeOverride(name = "yearMonth", column = @Column(name = "YearMonth", nullable = false, length = 20)), 
			@AttributeOverride(name = "bookType", column = @Column(name = "BookType", nullable = false, length = 100)),
			@AttributeOverride(name = "startDate", column = @Column(name = "StartDate", nullable = false)),
			@AttributeOverride(name = "endDate", column = @Column(name = "EndDate", nullable = false)),			
			@AttributeOverride(name = "principalItemCode", column = @Column(name = "PrincipalItemCode", nullable = false, length = 200)),
            @AttributeOverride(name = "interestItem", column = @Column(name = "InterestItem", nullable = false, length = 200)),
            @AttributeOverride(name = "interestIncomeItem", column = @Column(name = "InterestIncomeItem", nullable = false, length = 200)),
            @AttributeOverride(name = "status", column = @Column(name = "status", nullable = false, length = 20)),
		    })
	private His_Cux_Sino_Interface_DepositId id;
	@Column
	private Date impDate;
	@Column
	private String impOperator;
	
	@Column
	private String depositState;
	@Column
	private BigDecimal principal;
	@Column
	private String bankCode;
	@Column
	private String insideOrOutside;
	@Column
	private String accountCode;
	@Column
	private String accountName;
	@Column
	private BigDecimal perHundredRate;
	@Column
	private String depositsTerm;
	@Column
	private Date interestStartDate;
	@Column
	private String interestTateType;
	@Column
	private String interestType;
	@Column
	private String interestWay;
	@Column
	private String interestFrequency;
	@Column
	private String interestSettleWay;
	@Column
	private Date firstInterestSettleDate;
	@Column
	private String baseInterestRateType;
	@Column
	private BigDecimal basicInterestRateDiffer;
	@Column
	private BigDecimal floatRatio;
	@Column
	private String pranayamaTimeType;
	@Column
	private String remark;
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
	public His_Cux_Sino_Interface_DepositId getId() {
		return id;
	}
	public void setId(His_Cux_Sino_Interface_DepositId id) {
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
	public String getDepositState() {
		return depositState;
	}
	public void setDepositState(String depositState) {
		this.depositState = depositState;
	}
	public BigDecimal getPrincipal() {
		return principal;
	}
	public void setPrincipal(BigDecimal principal) {
		this.principal = principal;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getInsideOrOutside() {
		return insideOrOutside;
	}
	public void setInsideOrOutside(String insideOrOutside) {
		this.insideOrOutside = insideOrOutside;
	}
	public String getAccountCode() {
		return accountCode;
	}
	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public BigDecimal getPerHundredRate() {
		return perHundredRate;
	}
	public void setPerHundredRate(BigDecimal perHundredRate) {
		this.perHundredRate = perHundredRate;
	}
	public String getDepositsTerm() {
		return depositsTerm;
	}
	public void setDepositsTerm(String depositsTerm) {
		this.depositsTerm = depositsTerm;
	}
	public Date getInterestStartDate() {
		return interestStartDate;
	}
	public void setInterestStartDate(Date interestStartDate) {
		this.interestStartDate = interestStartDate;
	}
	public String getInterestTateType() {
		return interestTateType;
	}
	public void setInterestTateType(String interestTateType) {
		this.interestTateType = interestTateType;
	}
	public String getInterestType() {
		return interestType;
	}
	public void setInterestType(String interestType) {
		this.interestType = interestType;
	}
	public String getInterestWay() {
		return interestWay;
	}
	public void setInterestWay(String interestWay) {
		this.interestWay = interestWay;
	}
	public String getInterestFrequency() {
		return interestFrequency;
	}
	public void setInterestFrequency(String interestFrequency) {
		this.interestFrequency = interestFrequency;
	}
	public String getInterestSettleWay() {
		return interestSettleWay;
	}
	public void setInterestSettleWay(String interestSettleWay) {
		this.interestSettleWay = interestSettleWay;
	}
	public Date getFirstInterestSettleDate() {
		return firstInterestSettleDate;
	}
	public void setFirstInterestSettleDate(Date firstInterestSettleDate) {
		this.firstInterestSettleDate = firstInterestSettleDate;
	}
	public String getBaseInterestRateType() {
		return baseInterestRateType;
	}
	public void setBaseInterestRateType(String baseInterestRateType) {
		this.baseInterestRateType = baseInterestRateType;
	}
	public BigDecimal getBasicInterestRateDiffer() {
		return basicInterestRateDiffer;
	}
	public void setBasicInterestRateDiffer(BigDecimal basicInterestRateDiffer) {
		this.basicInterestRateDiffer = basicInterestRateDiffer;
	}
	public BigDecimal getFloatRatio() {
		return floatRatio;
	}
	public void setFloatRatio(BigDecimal floatRatio) {
		this.floatRatio = floatRatio;
	}
	public String getPranayamaTimeType() {
		return pranayamaTimeType;
	}
	public void setPranayamaTimeType(String pranayamaTimeType) {
		this.pranayamaTimeType = pranayamaTimeType;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
