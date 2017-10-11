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
@Table(name = "abc_invest")
public class Invest_ABC {
	private BigDecimal serialNo;
	private String tableCode;   //表代码
	private String tableName ;  //Excel表名
	private String itemName;  //股票、基金、优先股、证券、债券、产品等名称
	private String itemType ;  //行可扩展名称的类别
	private String itemCode ;  //股票、基金、证券等代码
	private BigDecimal amount;  //持股比例/持股数量（股）/持仓数量（份）/修正久期（年））/剩余年限（年）
	private String creditRate ;  //--信用评级
	private String stockType ; //--股票类型
	private String fundType ;  //--基金类别
	private String issuer ;  //发行方、发行机构
	private String isHuShen ;  //是否为沪深300指数成份股(计算后的结果）
	private String huShenValue ;
	private BigDecimal purchaseCost ;  //购买成本
	private BigDecimal recognitionValue ;  //认可价值
	private BigDecimal interst;  //应收利息
	private BigDecimal holdingQuantity;  //持有数量
	
	private String year	;  //年度
	private String quarter;  //季度
	private Date importDate ;  //上传日期
	private String importDept;  //上传部门
	private String importOperator ;  //上传人员
	
	private String externalRate; //--债券外部评级
	private String fundTypeB;  //金类别B
	private String fundTypeA ;  // --基金类别（手工维护）的数据
	
	private String colcode;
	
	private String reportId;
	
	@Column(name = "ReportId",  length = 20)
	public String getReportId() {
		return reportId;
	}


	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	
	@Column(name = "HoldingQuantity")
	public BigDecimal getHoldingQuantity() {
		return holdingQuantity;
	}
	public void setHoldingQuantity(BigDecimal holdingQuantity) {
		this.holdingQuantity = holdingQuantity;
	}
	@Column(name = "Colcode", length = 20)
	public String getColcode() {
		return colcode;
	}
	public void setColcode(String colcode) {
		this.colcode = colcode;
	}
	@Column(name = "HuShenValue", length = 20)
	public String getHuShenValue() {
		return huShenValue;
	}
	public void setHuShenValue(String huShenValue) {
		this.huShenValue = huShenValue;
	}
	@Column(name = "TableCode", length = 10)
	public String getTableCode() {
		return tableCode;
	}
	public void setTableCode(String tableCode) {
		this.tableCode = tableCode;
	}
	
	@Column(name = "CreditRate", length = 10)
	public String getCreditRate() {
		return creditRate;
	}
	public void setCreditRate(String creditRate) {
		this.creditRate = creditRate;
	}
	
	@Column(name = "StockType", length = 20)
	public String getStockType() {
		return stockType;
	}
	public void setStockType(String stockType) {
		this.stockType = stockType;
	}
	
	@Column(name = "FundType", length = 30)
	public String getFundType() {
		return fundType;
	}
	public void setFundType(String fundType) {
		this.fundType = fundType;
	}
	
	@Column(name = "FundTypeA", length = 30)
	public String getFundTypeA() {
		return fundTypeA;
	}
	public void setFundTypeA(String fundTypeA) {
		this.fundTypeA = fundTypeA;
	}
	@Column(name = "FundTypeB", length = 30)
	public String getFundTypeB() {
		return fundTypeB;
	}
	public void setFundTypeB(String fundTypeB) {
		this.fundTypeB = fundTypeB;
	}
	public String getExternalRate() {
		return externalRate;
	}
	public void setExternalRate(String externalRate) {
		this.externalRate = externalRate;
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
	
	@Column(name = "TableName", length = 100)
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}


	@Column(name = "ItemName", length = 100)
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	
	@Column(name = "ItemType", length = 50)
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	
	@Column(name = "ItemCode", length = 50)
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	
	@Column(name = "Amount")
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
	
	@Column(name = "Issuer", length = 100)
	public String getIssuer() {
		return issuer;
	}
	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}
	
	@Column(name = "IsHuShen", length = 20)
	public String getIsHuShen() {
		return isHuShen;
	}
	public void setIsHuShen(String isHuShen) {
		this.isHuShen = isHuShen;
	}
	
	@Column(name = "PurchaseCost")
	public BigDecimal getPurchaseCost() {
		return purchaseCost;
	}
	public void setPurchaseCost(BigDecimal purchaseCost) {
		this.purchaseCost = purchaseCost;
	}
	
	@Column(name = "RecognitionValue")
	public BigDecimal getRecognitionValue() {
		return recognitionValue;
	}
	public void setRecognitionValue(BigDecimal recognitionValue) {
		this.recognitionValue = recognitionValue;
	}
	
	@Column(name = "Interst")
	public BigDecimal getInterst() {
		return interst;
	}
	public void setInterst(BigDecimal interst) {
		this.interst = interst;
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
		
	@Column(name = "ImportDate")
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
	
	@Column(name = "ImportOperator", length = 20)
	public String getImportOperator() {
		return importOperator;
	}
	public void setImportOperator(String importOperator) {
		this.importOperator = importOperator;
	}
	
}
