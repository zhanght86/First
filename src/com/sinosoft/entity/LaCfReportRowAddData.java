package com.sinosoft.entity;

import java.math.BigDecimal;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "La_CfReportRowAddData")
public class LaCfReportRowAddData implements java.io.Serializable {

	private LaCfReportRowAddDataId id;
	private String tableCode;
	private String comCode;
	private String colType;
	private BigDecimal numValue;
	private BigDecimal wanValue;
	private String textValue;
	private String desText;
	private String reportType;
	private String source;
	private String operator2;
	private String operDate2;
	private String yearMonth;
	private String quarter;
	private String reportRate;

	public LaCfReportRowAddData() {
	}

	public LaCfReportRowAddData(LaCfReportRowAddDataId id, String colType,
			String operator2, String operDate2) {
		this.id = id;
		this.colType = colType;
		this.operator2 = operator2;
		this.operDate2 = operDate2;
	}

	public LaCfReportRowAddData(LaCfReportRowAddDataId id, String tableCode, String yearMonth, String quarter, String reportRate, String comCode,
			String colType, BigDecimal numValue, BigDecimal wanValue,
			String textValue, String desText, String reportType, String source,
			String operator2, String operDate2) {
		this.id = id;
		this.yearMonth = yearMonth;
		this.quarter = quarter;
		this.tableCode = tableCode;
		this.reportRate = reportRate;
		this.comCode = comCode;
		this.colType = colType;
		this.numValue = numValue;
		this.wanValue = wanValue;
		this.textValue = textValue;
		this.desText = desText;
		this.reportType = reportType;
		this.source = source;
		this.operator2 = operator2;
		this.operDate2 = operDate2;
	}

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "colCode", column = @Column(name = "ColCode", nullable = false, length = 20)),
			@AttributeOverride(name = "rowNo", column = @Column(name = "RowNo", nullable = false, length = 8)),
			@AttributeOverride(name = "reportid", column = @Column(name = "ReportId", nullable = false, length = 255)) })
	public LaCfReportRowAddDataId getId() {
		return this.id;
	}

	public void setId(LaCfReportRowAddDataId id) {
		this.id = id;
	}
	
	@Column(name = "TableCode", nullable = false, length = 20)
	public String getTableCode() {
		return this.tableCode;
	}

	public void setTableCode(String tableCode) {
		this.tableCode = tableCode;
	}
	
	@Column(name = "YearMonth", nullable = false, length = 6)
	public String getYearMonth() {
		return this.yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}

	@Column(name = "Quarter", nullable = false, length = 2)
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

	@Column(name = "ColType", nullable = false, length = 8)
	public String getColType() {
		return this.colType;
	}

	public void setColType(String colType) {
		this.colType = colType;
	}

	@Column(name = "NumValue", precision = 38, scale = 4)
	public BigDecimal getNumValue() {
		return this.numValue;
	}

	public void setNumValue(BigDecimal numValue) {
		this.numValue = numValue;
	}

	@Column(name = "WanValue", precision = 38, scale = 8)
	public BigDecimal getWanValue() {
		return this.wanValue;
	}

	public void setWanValue(BigDecimal wanValue) {
		this.wanValue = wanValue;
	}

	@Column(name = "TextValue", length = 4000)
	public String getTextValue() {
		return this.textValue;
	}

	public void setTextValue(String textValue) {
		this.textValue = textValue;
	}

	@Column(name = "DesText", length = 1000)
	public String getDesText() {
		return this.desText;
	}

	public void setDesText(String desText) {
		this.desText = desText;
	}

	@Column(name = "ReportType", length = 2)
	public String getReportType() {
		return this.reportType;
	}

	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	@Column(name = "Source", length = 2)
	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Column(name = "Operator2", nullable = false, length = 50)
	public String getOperator2() {
		return this.operator2;
	}

	public void setOperator2(String operator2) {
		this.operator2 = operator2;
	}

	@Column(name = "OperDate2", nullable = false, length = 10)
	public String getOperDate2() {
		return this.operDate2;
	}

	public void setOperDate2(String operDate2) {
		this.operDate2 = operDate2;
	}
}
