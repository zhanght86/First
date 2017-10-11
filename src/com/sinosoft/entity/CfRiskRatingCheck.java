package com.sinosoft.entity;

// Generated 2016-1-13 19:05:16 by Hibernate Tools 4.3.1

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * CfReportDataCheck generated by hbm2java
 */
@Entity
@Table(name = "CfRiskRatingCheck")
public class CfRiskRatingCheck {

	private int serialNo;  
	private String calFormula;
	private String calType;
	private String remark;
	private String isNeedChk;
	private BigDecimal tolerance;  //容差    
	private String companyFlag; // --z为总公司，f为分公司
	
	public CfRiskRatingCheck() {
	}

	public CfRiskRatingCheck(int serialNo, String calFormula) {
		this.serialNo = serialNo;
		this.calFormula = calFormula;
	}

	public CfRiskRatingCheck(int serialNo, String calFormula,
			String calType, String remark, String isNeedChk) {
		this.serialNo = serialNo;
		this.calFormula = calFormula;
		this.calType = calType;
		this.remark = remark;
		this.isNeedChk = isNeedChk;
	}

	
	@Column(name = "tolerance",  nullable = false)
	public BigDecimal getTolerance() {
		return tolerance;
	}

	public void setTolerance(BigDecimal tolerance) {
		this.tolerance = tolerance;
	}

	@Column(name = "CompanyFlag",  nullable = false)
	public String getCompanyFlag() {
		return companyFlag;
	}

	public void setCompanyFlag(String companyFlag) {
		this.companyFlag = companyFlag;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "SerialNo", unique = true, nullable = false, length = 10)
	public int getSerialNo() {
		return this.serialNo;
	}

	public void setSerialNo(int serialNo) {
		this.serialNo = serialNo;
	}

	
	@Column(name = "CalFormula", nullable = false, length = 100)
	public String getCalFormula() {
		return calFormula;
	}

	public void setCalFormula(String calFormula) {
		this.calFormula = calFormula;
	}

	@Column(name = "CalType", length = 20)
	public String getCalType() {
		return this.calType;
	}


	public void setCalType(String calType) {
		this.calType = calType;
	}

	@Column(name = "Remark", length = 2000)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "IsNeedChk", length = 3)
	public String getIsNeedChk() {
		return this.isNeedChk;
	}

	public void setIsNeedChk(String isNeedChk) {
		this.isNeedChk = isNeedChk;
	}

}