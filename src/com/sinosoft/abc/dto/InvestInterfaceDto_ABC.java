package com.sinosoft.abc.dto;

import java.math.BigDecimal;
import java.util.Date;

public class InvestInterfaceDto_ABC {
	private BigDecimal serialNo;  //主键自增
	private String itemCode;  //科目段
	private String year	;  //年度
	private String quarter;  //季度
	private Date importDate ;  //上传日期
	private String importDept;  //上传部门
	private String importOperator ;  //上传人员
    
    private String tableName ;
    private String itemName ;
    private String itemType ;
    private BigDecimal amount ;
    private String multiChoose ;
    private String issuer ;
    private String isHuShen ;
    private BigDecimal purchaseCost ;
    private BigDecimal recognitionValue;
    private BigDecimal interst ;
	private String externalRate; //--债券外部评级
	
	
	public String getExternalRate() {
		return externalRate;
	}
	public void setExternalRate(String externalRate) {
		this.externalRate = externalRate;
	}

	public BigDecimal getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(BigDecimal serialNo) {
		this.serialNo = serialNo;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
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

	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getMultiChoose() {
		return multiChoose;
	}
	public void setMultiChoose(String multiChoose) {
		this.multiChoose = multiChoose;
	}
	public String getIssuer() {
		return issuer;
	}
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}
	public String getIsHuShen() {
		return isHuShen;
	}
	public void setIsHuShen(String isHuShen) {
		this.isHuShen = isHuShen;
	}
	public BigDecimal getPurchaseCost() {
		return purchaseCost;
	}
	public void setPurchaseCost(BigDecimal purchaseCost) {
		this.purchaseCost = purchaseCost;
	}
	public BigDecimal getRecognitionValue() {
		return recognitionValue;
	}
	public void setRecognitionValue(BigDecimal recognitionValue) {
		this.recognitionValue = recognitionValue;
	}
	public BigDecimal getInterst() {
		return interst;
	}
	public void setInterst(BigDecimal interst) {
		this.interst = interst;
	}
    
    
}
