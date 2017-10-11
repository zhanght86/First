package com.sinosoft.entity;

// Generated 2016-1-13 19:05:16 by Hibernate Tools 4.3.1

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * CfReportRowAddDesc generated by hbm2java
 */
@Entity
@Table(name = "CfReportRowAddDesc")
public class CfReportRowAddDesc implements java.io.Serializable {

	private CfReportRowAddDescId id;
	private String tableName;
	private String colName;
	private String colType;
	private String outItemNote;
	private String outItemCreatTime;
	private String propertyInsurance1;
	private String propertyInsurance2;
	private String propertyInsurance3;
	private String reinsurance;
	private String lifeInsurance1;
	private String lifeInsurance2;
	private String lifeInsurance3;
	private String lifeInsurance4;
	private String asset;
	private String group1;
	private String group2;
	private String group3;
	private String quarterReport;
	private String halfYearReport;
	private String yearReport;
	private String colDesc;
	private String deptNo;
	private String source;

	public CfReportRowAddDesc() {
	}

	public CfReportRowAddDesc(CfReportRowAddDescId id) {
		this.id = id;
	}

	public CfReportRowAddDesc(CfReportRowAddDescId id, String tableName,
			String colName, String colType, String outItemNote,
			String outItemCreatTime, String propertyInsurance1,
			String propertyInsurance2, String propertyInsurance3,
			String reinsurance, String lifeInsurance1, String lifeInsurance2,
			String lifeInsurance3, String lifeInsurance4, String asset,
			String group1, String group2, String group3, String quarterReport,
			String halfYearReport, String yearReport, String colDesc,
			String deptNo, String source) {
		this.id = id;
		this.tableName = tableName;
		this.colName = colName;
		this.colType = colType;
		this.outItemNote = outItemNote;
		this.outItemCreatTime = outItemCreatTime;
		this.propertyInsurance1 = propertyInsurance1;
		this.propertyInsurance2 = propertyInsurance2;
		this.propertyInsurance3 = propertyInsurance3;
		this.reinsurance = reinsurance;
		this.lifeInsurance1 = lifeInsurance1;
		this.lifeInsurance2 = lifeInsurance2;
		this.lifeInsurance3 = lifeInsurance3;
		this.lifeInsurance4 = lifeInsurance4;
		this.asset = asset;
		this.group1 = group1;
		this.group2 = group2;
		this.group3 = group3;
		this.quarterReport = quarterReport;
		this.halfYearReport = halfYearReport;
		this.yearReport = yearReport;
		this.colDesc = colDesc;
		this.deptNo = deptNo;
		this.source = source;
	}

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "tableCode", column = @Column(name = "TableCode", nullable = false, length = 20)),
			@AttributeOverride(name = "colCode", column = @Column(name = "ColCode", nullable = false, length = 20)),
			@AttributeOverride(name = "reportType", column = @Column(name = "ReportType", nullable = false, length = 2)) })
	public CfReportRowAddDescId getId() {
		return this.id;
	}

	public void setId(CfReportRowAddDescId id) {
		this.id = id;
	}

	@Column(name = "TableName", length = 200)
	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	@Column(name = "ColName", length = 200)
	public String getColName() {
		return this.colName;
	}

	public void setColName(String colName) {
		this.colName = colName;
	}

	@Column(name = "ColType", length = 8)
	public String getColType() {
		return this.colType;
	}

	public void setColType(String colType) {
		this.colType = colType;
	}

	@Column(name = "OutItemNote", length = 1000)
	public String getOutItemNote() {
		return this.outItemNote;
	}

	public void setOutItemNote(String outItemNote) {
		this.outItemNote = outItemNote;
	}

	@Column(name = "OutItemCreatTime", length = 10)
	public String getOutItemCreatTime() {
		return this.outItemCreatTime;
	}

	public void setOutItemCreatTime(String outItemCreatTime) {
		this.outItemCreatTime = outItemCreatTime;
	}

	@Column(name = "PropertyInsurance1", length = 1)
	public String getPropertyInsurance1() {
		return this.propertyInsurance1;
	}

	public void setPropertyInsurance1(String propertyInsurance1) {
		this.propertyInsurance1 = propertyInsurance1;
	}

	@Column(name = "PropertyInsurance2", length = 1)
	public String getPropertyInsurance2() {
		return this.propertyInsurance2;
	}

	public void setPropertyInsurance2(String propertyInsurance2) {
		this.propertyInsurance2 = propertyInsurance2;
	}

	@Column(name = "PropertyInsurance3", length = 1)
	public String getPropertyInsurance3() {
		return this.propertyInsurance3;
	}

	public void setPropertyInsurance3(String propertyInsurance3) {
		this.propertyInsurance3 = propertyInsurance3;
	}

	@Column(name = "Reinsurance", length = 1)
	public String getReinsurance() {
		return this.reinsurance;
	}

	public void setReinsurance(String reinsurance) {
		this.reinsurance = reinsurance;
	}

	@Column(name = "LifeInsurance1", length = 1)
	public String getLifeInsurance1() {
		return this.lifeInsurance1;
	}

	public void setLifeInsurance1(String lifeInsurance1) {
		this.lifeInsurance1 = lifeInsurance1;
	}

	@Column(name = "LifeInsurance2", length = 1)
	public String getLifeInsurance2() {
		return this.lifeInsurance2;
	}

	public void setLifeInsurance2(String lifeInsurance2) {
		this.lifeInsurance2 = lifeInsurance2;
	}

	@Column(name = "LifeInsurance3", length = 1)
	public String getLifeInsurance3() {
		return this.lifeInsurance3;
	}

	public void setLifeInsurance3(String lifeInsurance3) {
		this.lifeInsurance3 = lifeInsurance3;
	}

	@Column(name = "LifeInsurance4", length = 1)
	public String getLifeInsurance4() {
		return this.lifeInsurance4;
	}

	public void setLifeInsurance4(String lifeInsurance4) {
		this.lifeInsurance4 = lifeInsurance4;
	}

	@Column(name = "asset", length = 1)
	public String getAsset() {
		return this.asset;
	}

	public void setAsset(String asset) {
		this.asset = asset;
	}

	@Column(name = "Group1", length = 1)
	public String getGroup1() {
		return this.group1;
	}

	public void setGroup1(String group1) {
		this.group1 = group1;
	}

	@Column(name = "Group2", length = 1)
	public String getGroup2() {
		return this.group2;
	}

	public void setGroup2(String group2) {
		this.group2 = group2;
	}

	@Column(name = "Group3", length = 1)
	public String getGroup3() {
		return this.group3;
	}

	public void setGroup3(String group3) {
		this.group3 = group3;
	}

	@Column(name = "QuarterReport", length = 1)
	public String getQuarterReport() {
		return this.quarterReport;
	}

	public void setQuarterReport(String quarterReport) {
		this.quarterReport = quarterReport;
	}

	@Column(name = "HalfYearReport", length = 1)
	public String getHalfYearReport() {
		return this.halfYearReport;
	}

	public void setHalfYearReport(String halfYearReport) {
		this.halfYearReport = halfYearReport;
	}

	@Column(name = "YearReport", length = 1)
	public String getYearReport() {
		return this.yearReport;
	}

	public void setYearReport(String yearReport) {
		this.yearReport = yearReport;
	}

	@Column(name = "ColDesc", length = 200)
	public String getColDesc() {
		return this.colDesc;
	}

	public void setColDesc(String colDesc) {
		this.colDesc = colDesc;
	}
	
	@Column(name = "DeptNo", length = 2)
	public String getDeptNo() {
		return this.deptNo;
	}

	public void setDeptNo(String deptNo) {
		this.deptNo = deptNo;
	}
	
	@Column(name = "Source", length = 1)
	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

}
