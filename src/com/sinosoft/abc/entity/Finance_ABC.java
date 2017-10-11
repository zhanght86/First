package com.sinosoft.abc.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "abc_finance")
public class Finance_ABC {
	private BigDecimal serialNo	;  //主键自增
	private String tableName ;
	private String tableCode ;
	
	private String subjectSegment;  //科目段
	private String details;  //明细段
	
	private String rowNo;  //行次
	private String itemCode	;  //科目或者小计
	private BigDecimal openingBalance;  //期初余额勒或者本期发生数
	private BigDecimal closingBalance;  //期末余额或者本年累计数
							
	private String year	;  //年度
	private String quarter;  //季度

	private Date importDate ;   //上传日期
	private String importDept;   //上传部门
	private String importOperator;  //操作人
	
	private String reportId;
	
	@Column(name = "ReportId",  length = 20)
	public String getReportId() {
		return reportId;
	}


	public void setReportId(String reportId) {
		this.reportId = reportId;
	}


	public Finance_ABC(){
	}
	
	
	@Column(name = "Details",  length = 100)
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}

	@Column(name = "SubjectSegment",  length = 10)
	public String getSubjectSegment() {
		return subjectSegment;
	}
	public void setSubjectSegment(String subjectSegment) {
		this.subjectSegment = subjectSegment;
	}

	@Column(name = "TableName",  length = 50)
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	@Column(name = "TableCode", nullable = false, length = 10)
	public String getTableCode() {
		return tableCode;
	}
	public void setTableCode(String tableCode) {
		this.tableCode = tableCode;
	}


	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "SerialNo", unique = true, nullable = false, scale = 0)
	public BigDecimal getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(BigDecimal serialNo) {
		this.serialNo = serialNo;
	}
	

	@Column(name = "RowNo",  length = 10)
	public String getRowNo() {
		return rowNo;
	}
	public void setRowNo(String rowNo) {
		this.rowNo = rowNo;
	}
	
	@Column(name = "ItemCode",  length = 100)
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	
	@Column(name = "OpeningBalance")
	public BigDecimal getOpeningBalance() {
		return openingBalance;
	}

	public void setOpeningBalance(BigDecimal openingBalance) {
		this.openingBalance = openingBalance;
	}
	
	@Column(name = "ClosingBalance")
	public BigDecimal getClosingBalance() {
		return closingBalance;
	}
	public void setClosingBalance(BigDecimal closingBalance) {
		this.closingBalance = closingBalance;
	}
	
	@Column(name = "Year", nullable = false, length = 4)
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	
	@Column(name = "Quarter", nullable = false, length = 2)
	public String getQuarter() {
		return quarter;
	}
	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}
	
	@Column(name = "ImportDate", nullable = false)
	public Date getImportDate() {
		return importDate;
	}
	public void setImportDate(Date importDate) {
		this.importDate = importDate;
	}
	
	@Column(name = "ImportDept", length = 20)
	public String getImportDept() {
		return importDept;
	}
	public void setImportDept(String importDept) {
		this.importDept = importDept;
	}
	
	@Column(name = "ImportOperator",  length = 20)
	public String getImportOperator() {
		return importOperator;
	}
	public void setImportOperator(String importOperator) {
		this.importOperator = importOperator;
	}

	
}
