package com.sinosoft.entity;

import java.math.BigDecimal;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name = "la_CfReportData")
public class LaCfReportData implements java.io.Serializable {
	private LaCfReportDataId id;
	private String month;
	private String quarter;
	private String reportRate;
	private String comCode;
	private String outItemType;
	private String outItemCodeName;
	private String reportItemCode;
	private BigDecimal reportItemValue;
	private BigDecimal reportItemWanValue;
	private String textValue;
	private String desText;
	private String fileText;
	private String source;
	private String reportType;
	private String computeLevel;
	private String reportItemValueSource;
	private String operator;
	private String operDate;
	private String temp;
	

	public LaCfReportData() {
	}

	public LaCfReportData(LaCfReportDataId id, String computeLevel,
			String operator, String operDate) {
		this.id = id;
		this.computeLevel = computeLevel;
		this.operator = operator;
		this.operDate = operDate;
	}

	

	public LaCfReportData(LaCfReportDataId id, String comCode, String outItemType,
			String outItemCodeName, String reportItemCode,
			BigDecimal reportItemValue, BigDecimal reportItemWanValue,
			String textValue, String desText, String fileText, String source,
			String reportType, String computeLevel,
			String reportItemValueSource, String operator, String operDate,
			String temp, String month, String quarter,String reportRate) {
		super();
		this.id = id;
		this.month = month;
		this.quarter = quarter;
		this.reportRate = reportRate;
		this.comCode = comCode;
		this.outItemType = outItemType;
		this.outItemCodeName = outItemCodeName;
		this.reportItemCode = reportItemCode;
		this.reportItemValue = reportItemValue;
		this.reportItemWanValue = reportItemWanValue;
		this.textValue = textValue;
		this.desText = desText;
		this.fileText = fileText;
		this.source = source;
		this.reportType = reportType;
		this.computeLevel = computeLevel;
		this.reportItemValueSource = reportItemValueSource;
		this.operator = operator;
		this.operDate = operDate;
		this.temp = temp;
	}

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "outItemCode", column = @Column(name = "OutItemCode", nullable = false, length = 20)),
			@AttributeOverride(name = "reportId", column = @Column(name = "ReportId", nullable = false, length = 50)) })
	public LaCfReportDataId getId() {
		return this.id;
	}

	public void setId(LaCfReportDataId id) {
		this.id = id;
	}

	@Column(name = "Month", nullable = false, length = 8)
	public String getMonth() {
		return this.month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	@Column(name = "Quarter", nullable = false, length = 1)
	public String getQuarter() {
		return this.quarter;
	}

	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}

	@Column(name = "ReportRate", nullable = false, length = 2)
	public String getReportRate() {
		return this.reportRate;
	}

	public void setReportRate(String reportRate) {
		this.reportRate = reportRate;
	}
	
	@Column(name = "ComCode", length = 10)
	public String getComCode() {
		return this.comCode;
	}

	public void setComCode(String comCode) {
		this.comCode = comCode;
	}


	@Column(name = "OutItemType", length = 2)
	public String getOutItemType() {
		return this.outItemType;
	}

	public void setOutItemType(String outItemType) {
		this.outItemType = outItemType;
	}

	@Column(name = "OutItemCodeName")
	public String getOutItemCodeName() {
		return this.outItemCodeName;
	}

	public void setOutItemCodeName(String outItemCodeName) {
		this.outItemCodeName = outItemCodeName;
	}

	@Column(name = "ReportItemCode", length = 20)
	public String getReportItemCode() {
		return this.reportItemCode;
	}

	public void setReportItemCode(String reportItemCode) {
		this.reportItemCode = reportItemCode;
	}

	@Column(name = "ReportItemValue", precision = 38)
	public BigDecimal getReportItemValue() {
		return this.reportItemValue;
	}

	public void setReportItemValue(BigDecimal reportItemValue) {
		this.reportItemValue = reportItemValue;
	}

	@Column(name = "ReportItemWanValue", precision = 38)
	public BigDecimal getReportItemWanValue() {
		return this.reportItemWanValue;
	}

	public void setReportItemWanValue(BigDecimal reportItemWanValue) {
		this.reportItemWanValue = reportItemWanValue;
	}

	@Column(name = "TextValue", length = 300)
	public String getTextValue() {
		return this.textValue;
	}

	public void setTextValue(String textValue) {
		this.textValue = textValue;
	}

	@Column(name = "DesText")
	@Lob
	public String getDesText() {
		return this.desText;
	}

	public void setDesText(String desText) {
		this.desText = desText;
	}

	@Column(name = "FileText")
	@Lob
	public String getFileText() {
		return this.fileText;
	}

	public void setFileText(String fileText) {
		this.fileText = fileText;
	}

	@Column(name = "Source", length = 1)
	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Column(name = "ReportType", length = 2)
	public String getReportType() {
		return this.reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	@Column(name = "ComputeLevel", nullable = false, length = 2)
	public String getComputeLevel() {
		return this.computeLevel;
	}

	public void setComputeLevel(String computeLevel) {
		this.computeLevel = computeLevel;
	}

	@Column(name = "ReportItemValueSource", length = 1)
	public String getReportItemValueSource() {
		return this.reportItemValueSource;
	}

	public void setReportItemValueSource(String reportItemValueSource) {
		this.reportItemValueSource = reportItemValueSource;
	}

	@Column(name = "Operator", nullable = false, length = 50)
	public String getOperator() {
		return this.operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Column(name = "OperDate", nullable = false, length = 10)
	public String getOperDate() {
		return this.operDate;
	}

	public void setOperDate(String operDate) {
		this.operDate = operDate;
	}

	@Column(name = "Temp", length = 25)
	public String getTemp() {
		return this.temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}
}
