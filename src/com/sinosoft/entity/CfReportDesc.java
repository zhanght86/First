package com.sinosoft.entity;

// Generated 2016-1-13 19:05:16 by Hibernate Tools 4.3.1

import java.math.BigDecimal;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * CfReportDesc generated by hbm2java
 */
@Entity
@Table(name = "CfReportDesc")
public class CfReportDesc implements java.io.Serializable {

	private CfReportDescId id;
	private String reportName;
	private String frequency;
	private String frequency2;
	private String reportCompany1;
	private String reportCompany2;
	private String reportCompany3;
	private BigDecimal reportOrder;
	private String dealWay;
	private String remark;
	private String department;
	private String temp1;
	private String temp2;
	private String temp3;
	private String temp4;
	private String reportCompany0;
	private String frequency3;

	public CfReportDesc() {
	}

	public CfReportDesc(CfReportDescId id) {
		this.id = id;
	}

	public CfReportDesc(CfReportDescId id, String reportName, String frequency,
			String frequency2, String reportCompany1, String reportCompany2,
			String reportCompany3, BigDecimal reportOrder, String dealWay,
			String remark, String department, String temp1, String temp2,
			String temp3, String temp4, String reportCompany0, String frequency3) {
		this.id = id;
		this.reportName = reportName;
		this.frequency = frequency;
		this.frequency2 = frequency2;
		this.reportCompany1 = reportCompany1;
		this.reportCompany2 = reportCompany2;
		this.reportCompany3 = reportCompany3;
		this.reportOrder = reportOrder;
		this.dealWay = dealWay;
		this.remark = remark;
		this.department = department;
		this.temp1 = temp1;
		this.temp2 = temp2;
		this.temp3 = temp3;
		this.temp4 = temp4;
		this.reportCompany0 = reportCompany0;
		this.frequency3 = frequency3;
	}

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "reportCode", column = @Column(name = "ReportCode", nullable = false, length = 20)),
			@AttributeOverride(name = "ruleNature", column = @Column(name = "RuleNature", nullable = false, length = 1)),
			@AttributeOverride(name = "reportMode", column = @Column(name = "ReportMode", nullable = false, length = 1)) })
	public CfReportDescId getId() {
		return this.id;
	}

	public void setId(CfReportDescId id) {
		this.id = id;
	}

	@Column(name = "ReportName", length = 100)
	public String getReportName() {
		return this.reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	@Column(name = "Frequency", length = 20)
	public String getFrequency() {
		return this.frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	@Column(name = "Frequency2", length = 20)
	public String getFrequency2() {
		return this.frequency2;
	}

	public void setFrequency2(String frequency2) {
		this.frequency2 = frequency2;
	}

	@Column(name = "ReportCompany1", length = 1)
	public String getReportCompany1() {
		return this.reportCompany1;
	}

	public void setReportCompany1(String reportCompany1) {
		this.reportCompany1 = reportCompany1;
	}

	@Column(name = "ReportCompany2", length = 1)
	public String getReportCompany2() {
		return this.reportCompany2;
	}

	public void setReportCompany2(String reportCompany2) {
		this.reportCompany2 = reportCompany2;
	}

	@Column(name = "ReportCompany3", length = 1)
	public String getReportCompany3() {
		return this.reportCompany3;
	}

	public void setReportCompany3(String reportCompany3) {
		this.reportCompany3 = reportCompany3;
	}

	@Column(name = "ReportOrder", precision = 22, scale = 0)
	public BigDecimal getReportOrder() {
		return this.reportOrder;
	}

	public void setReportOrder(BigDecimal reportOrder) {
		this.reportOrder = reportOrder;
	}

	@Column(name = "DealWay", length = 1)
	public String getDealWay() {
		return this.dealWay;
	}

	public void setDealWay(String dealWay) {
		this.dealWay = dealWay;
	}

	@Column(name = "Remark")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "Department", length = 10)
	public String getDepartment() {
		return this.department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	@Column(name = "Temp1")
	public String getTemp1() {
		return this.temp1;
	}

	public void setTemp1(String temp1) {
		this.temp1 = temp1;
	}

	@Column(name = "Temp2")
	public String getTemp2() {
		return this.temp2;
	}

	public void setTemp2(String temp2) {
		this.temp2 = temp2;
	}

	@Column(name = "Temp3")
	public String getTemp3() {
		return this.temp3;
	}

	public void setTemp3(String temp3) {
		this.temp3 = temp3;
	}

	@Column(name = "Temp4")
	public String getTemp4() {
		return this.temp4;
	}

	public void setTemp4(String temp4) {
		this.temp4 = temp4;
	}

	@Column(name = "ReportCompany0", length = 1)
	public String getReportCompany0() {
		return this.reportCompany0;
	}

	public void setReportCompany0(String reportCompany0) {
		this.reportCompany0 = reportCompany0;
	}

	@Column(name = "Frequency3", length = 20)
	public String getFrequency3() {
		return this.frequency3;
	}

	public void setFrequency3(String frequency3) {
		this.frequency3 = frequency3;
	}

}
