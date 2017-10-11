package com.sinosoft.abc.dto;

import java.math.BigDecimal;
import java.util.Date;


public class FinanceInterfaceDto_ABC {
	private BigDecimal serialNo	;  //主键自增
	private String sheetName; //sheet表名称,第一张为1
	private String rowNo;  //行次
	private String itemCode	;  //科目或者小计
	private BigDecimal openingBalance;  //期初余额勒或者本期发生数
	private BigDecimal closingBalance;  //期末余额或者本年累计数
							
	private String year	;  //年度
	private String quarter;  //季度

	private Date importDate ;   //上传日期
	private String importDept;   //上传部门
	private String importOperator;  //操作人

	private String details;  //明细段

	private BigDecimal currentDebit	;  //本期借方
	private BigDecimal currentCredit;  //本期贷方
	
	private String financeTable;
	
	
	public String getFinanceTable() {
		return financeTable;
	}
	public void setFinanceTable(String financeTable) {
		this.financeTable = financeTable;
	}
	public BigDecimal getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(BigDecimal serialNo) {
		this.serialNo = serialNo;
	}
	public String getSheetName() {
		return sheetName;
	}
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	public String getRowNo() {
		return rowNo;
	}
	public void setRowNo(String rowNo) {
		this.rowNo = rowNo;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public BigDecimal getOpeningBalance() {
		return openingBalance;
	}
	public void setOpeningBalance(BigDecimal openingBalance) {
		this.openingBalance = openingBalance;
	}
	public BigDecimal getClosingBalance() {
		return closingBalance;
	}
	public void setClosingBalance(BigDecimal closingBalance) {
		this.closingBalance = closingBalance;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getQuarter() {
		return quarter;
	}
	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}
	public Date getImportDate() {
		return importDate;
	}
	public void setImportDate(Date importDate) {
		this.importDate = importDate;
	}
	public String getImportDept() {
		return importDept;
	}
	public void setImportDept(String importDept) {
		this.importDept = importDept;
	}
	public String getImportOperator() {
		return importOperator;
	}
	public void setImportOperator(String importOperator) {
		this.importOperator = importOperator;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public BigDecimal getCurrentDebit() {
		return currentDebit;
	}
	public void setCurrentDebit(BigDecimal currentDebit) {
		this.currentDebit = currentDebit;
	}
	public BigDecimal getCurrentCredit() {
		return currentCredit;
	}
	public void setCurrentCredit(BigDecimal currentCredit) {
		this.currentCredit = currentCredit;
	}
	@Override
	public String toString() {
		return "FinanceDto_ABC [serialNo=" + serialNo + ", sheetName="
				+ sheetName + ", rowNo=" + rowNo + ", itemCode=" + itemCode
				+ ", openingBalance=" + openingBalance + ", closingBalance="
				+ closingBalance + ", year=" + year + ", quarter=" + quarter
				+ ", importDate=" + importDate + ", importDept=" + importDept
				+ ", importOperator=" + importOperator + ", details=" + details
				+ ", currentDebit=" + currentDebit + ", currentCredit="
				+ currentCredit + "]";
	}
	
	
}
