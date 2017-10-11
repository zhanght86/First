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
 * CfReportItemRowCheck generated by hbm2java
 */
@Entity
@Table(name = "CfReportItemRowCheck")
public class CfReportItemRowCheck implements java.io.Serializable {
	private int serialNo;    //由String类型修改为int类型 ,并让其自动增长。 modify by liucw
	private String reportItemCode;
	private String tableCode;
	private String colCode;
	private String calType;
	private String remark;
	private String isNeedChk;
	private BigDecimal tolerance;  //容差      add by liucw
	
	@Column(name = "tolerance",  nullable = false)
	public BigDecimal getTolerance() {
		return tolerance;
	}

	public void setTolerance(BigDecimal tolerance) {
		this.tolerance = tolerance;
	}

	public CfReportItemRowCheck() {
	}

	public CfReportItemRowCheck(int serialNo, String reportItemCode) {
		this.serialNo = serialNo;
		this.reportItemCode = reportItemCode;
	}

	public CfReportItemRowCheck(int serialNo, String reportItemCode,
			String tableCode, String colCode, String calType, String remark,
			String isNeedChk) {
		this.serialNo = serialNo;
		this.reportItemCode = reportItemCode;
		this.tableCode = tableCode;
		this.colCode = colCode;
		this.calType = calType;
		this.remark = remark;
		this.isNeedChk = isNeedChk;
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

	@Column(name = "ReportItemCode", nullable = false, length = 10)
	public String getReportItemCode() {
		return this.reportItemCode;
	}

	public void setReportItemCode(String reportItemCode) {
		this.reportItemCode = reportItemCode;
	}

	@Column(name = "TableCode", length = 20)
	public String getTableCode() {
		return this.tableCode;
	}

	public void setTableCode(String tableCode) {
		this.tableCode = tableCode;
	}

	@Column(name = "ColCode", length = 20)
	public String getColCode() {
		return this.colCode;
	}

	public void setColCode(String colCode) {
		this.colCode = colCode;
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