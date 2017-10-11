package com.sinosoft.zcfz.dto.reportitemsystem;

import com.sinosoft.entity.CfReportRowAddDesc;
import com.sinosoft.entity.CfReportRowAddDescId;

public class CfReportRowAddDescDTO {
	private CfReportRowAddDescId id;
	private String sort;
	private String order;
	private String tableCode;
	private String colCode;
	private String reportType;
	private String tableName;
	private String colName;
	private String colType;
	private String outItemNote;
	private String outItemCreatTime;
	private String propertyInsurance1;
	private String propertyInsurance1Name;
	private String propertyInsurance2;
	private String propertyInsurance2Name;
	private String propertyInsurance3;
	private String propertyInsurance3Name;
	private String reinsurance;
	private String reinsuranceName;
	private String lifeInsurance1;
	private String lifeInsurance1Name;
	private String lifeInsurance2;
	private String lifeInsurance2Name;
	private String lifeInsurance3;
	private String lifeInsurance3Name;
	private String lifeInsurance4;
	private String lifeInsurance4Name;
	private String asset;
	private String assetName;
	private String group1;
	private String group1Name;
	private String group2;
	private String group2Name;
	private String group3;
	private String group3Name;
	private String quarterReport;
	private String quarterReportName;
	private String halfYearReport;
	private String halfYearReportName;
	private String yearReport;
	private String yearReportName;
	private String colDesc;
	private String reportTypeName;
	
	
	
	
	
	
	public CfReportRowAddDescId getId() {
		return id;
	}
	public void setId(CfReportRowAddDescId id) {
		this.id = id;
	}
	public String getTableCode() {
		return tableCode;
	}
	public void setTableCode(String tableCode) {
		this.tableCode = tableCode;
	}
	public String getColCode() {
		return colCode;
	}
	public void setColCode(String colCode) {
		this.colCode = colCode;
	}
	public String getReportType() {
		return reportType;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getColName() {
		return colName;
	}
	public void setColName(String colName) {
		this.colName = colName;
	}
	public String getColType() {
		return colType;
	}
	public void setColType(String colType) {
		this.colType = colType;
	}
	public String getOutItemNote() {
		return outItemNote;
	}
	public void setOutItemNote(String outItemNote) {
		this.outItemNote = outItemNote;
	}
	public String getOutItemCreatTime() {
		return outItemCreatTime;
	}
	public void setOutItemCreatTime(String outItemCreatTime) {
		this.outItemCreatTime = outItemCreatTime;
	}
	public String getPropertyInsurance1() {
		return propertyInsurance1;
	}
	public void setPropertyInsurance1(String propertyInsurance1) {
		this.propertyInsurance1 = propertyInsurance1;
		this.propertyInsurance1Name=returnName(propertyInsurance1);
	}
	public String getPropertyInsurance2() {
		return propertyInsurance2;
	}
	public void setPropertyInsurance2(String propertyInsurance2) {
		this.propertyInsurance2 = propertyInsurance2;
		this.propertyInsurance2Name=returnName(propertyInsurance2);
	}
	public String getPropertyInsurance3() {
		return propertyInsurance3;
	}
	public void setPropertyInsurance3(String propertyInsurance3) {
		this.propertyInsurance3 = propertyInsurance3;
		this.propertyInsurance3Name=returnName(propertyInsurance3);
	}
	public String getReinsurance() {
		return reinsurance;
	}
	public void setReinsurance(String reinsurance) {
		this.reinsurance = reinsurance;
		this.reinsuranceName=returnName(reinsurance);
	}
	public String getLifeInsurance1() {
		return lifeInsurance1;
	}
	public void setLifeInsurance1(String lifeInsurance1) {
		this.lifeInsurance1 = lifeInsurance1;
		this.lifeInsurance1Name = returnName(lifeInsurance1);
	}
	public String getLifeInsurance2() {
		return lifeInsurance2;
	}
	public void setLifeInsurance2(String lifeInsurance2) {
		this.lifeInsurance2 = lifeInsurance2;
		this.lifeInsurance2Name = returnName(lifeInsurance2);
	}
	public String getLifeInsurance3() {
		return lifeInsurance3;
	}
	public void setLifeInsurance3(String lifeInsurance3) {
		this.lifeInsurance3 = lifeInsurance3;
		this.lifeInsurance3Name = returnName(lifeInsurance3);
	}
	public String getLifeInsurance4() {
		return lifeInsurance4;
	}
	public void setLifeInsurance4(String lifeInsurance4) {
		this.lifeInsurance4 = lifeInsurance4;
		this.lifeInsurance4Name = returnName(lifeInsurance4);
	}
	public String getAsset() {
		return asset;
	}
	public void setAsset(String asset) {
		this.asset = asset;
		this.assetName = returnName(asset);
	}
	public String getGroup1() {
		return group1;
	}
	public void setGroup1(String group1) {
		this.group1 = group1;
		this.group1Name = returnName(group1);
	}
	public String getGroup2() {
		return group2;
	}
	public void setGroup2(String group2) {
		this.group2 = group2;
		this.group2Name = returnName(group2);
	}
	public String getGroup3() {
		return group3;
	}
	public void setGroup3(String group3) {
		this.group3 = group3;
		this.group3Name = returnName(group3);
	}
	public String getQuarterReport() {
		return quarterReport;
	}
	public void setQuarterReport(String quarterReport) {
		this.quarterReport = quarterReport;
		this.quarterReportName = returnName(quarterReport);
	}
	public String getHalfYearReport() {
		return halfYearReport;
	}
	public void setHalfYearReport(String halfYearReport) {
		this.halfYearReport = halfYearReport;
		this.halfYearReportName = returnName(halfYearReport);
	}
	public String getYearReport() {
		return yearReport;
	}
	public void setYearReport(String yearReport) {
		this.yearReport = yearReport;
		this.yearReportName = returnName(yearReport);
	}
	public String getColDesc() {
		return colDesc;
	}
	public void setColDesc(String colDesc) {
		this.colDesc = colDesc;
	}
	public String getPropertyInsurance1Name() {
		return propertyInsurance1Name;
	}
	public void setPropertyInsurance1Name(String propertyInsurance1Name) {
		this.propertyInsurance1Name = propertyInsurance1Name;
	}
	public String getPropertyInsurance2Name() {
		return propertyInsurance2Name;
	}
	public void setPropertyInsurance2Name(String propertyInsurance2Name) {
		this.propertyInsurance2Name = propertyInsurance2Name;
	}
	public String getPropertyInsurance3Name() {
		return propertyInsurance3Name;
	}
	public void setPropertyInsurance3Name(String propertyInsurance3Name) {
		this.propertyInsurance3Name = propertyInsurance3Name;
	}
	public String getReinsuranceName() {
		return reinsuranceName;
	}
	public void setReinsuranceName(String reinsuranceName) {
		this.reinsuranceName = reinsuranceName;
	}
	public String getLifeInsurance1Name() {
		return lifeInsurance1Name;
	}
	public void setLifeInsurance1Name(String lifeInsurance1Name) {
		this.lifeInsurance1Name = lifeInsurance1Name;
	}
	public String getLifeInsurance2Name() {
		return lifeInsurance2Name;
	}
	public void setLifeInsurance2Name(String lifeInsurance2Name) {
		this.lifeInsurance2Name = lifeInsurance2Name;
	}
	public String getLifeInsurance3Name() {
		return lifeInsurance3Name;
	}
	public void setLifeInsurance3Name(String lifeInsurance3Name) {
		this.lifeInsurance3Name = lifeInsurance3Name;
	}
	public String getLifeInsurance4Name() {
		return lifeInsurance4Name;
	}
	public void setLifeInsurance4Name(String lifeInsurance4Name) {
		this.lifeInsurance4Name = lifeInsurance4Name;
	}
	public String getAssetName() {
		return assetName;
	}
	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}
	public String getGroup1Name() {
		return group1Name;
	}
	public void setGroup1Name(String group1Name) {
		this.group1Name = group1Name;
	}
	public String getGroup2Name() {
		return group2Name;
	}
	public void setGroup2Name(String group2Name) {
		this.group2Name = group2Name;
	}
	public String getGroup3Name() {
		return group3Name;
	}
	public void setGroup3Name(String group3Name) {
		this.group3Name = group3Name;
	}
	public String getQuarterReportName() {
		return quarterReportName;
	}
	public void setQuarterReportName(String quarterReportName) {
		this.quarterReportName = quarterReportName;
	}
	public String getHalfYearReportName() {
		return halfYearReportName;
	}
	public void setHalfYearReportName(String halfYearReportName) {
		this.halfYearReportName = halfYearReportName;
	}
	public String getYearReportName() {
		return yearReportName;
	}
	public void setYearReportName(String yearReportName) {
		this.yearReportName = yearReportName;
	}
	public String getReportTypeName() {
		return reportTypeName;
	}
	public void setReportTypeName(String reportTypeName) {
		this.reportTypeName = reportTypeName;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	private String returnName(String pro){
		if("1".equals(pro)){
			return "是";
		}else if("0".equals(pro)){
			return "否";
		}else{
			return "";
		}
		
	}
	public CfReportRowAddDesc toCfReportRowAddDesc(CfReportRowAddDesc re){
		if(re==null){
			re=new CfReportRowAddDesc();
		}
		if(re.getId()==null){
			CfReportRowAddDescId reid=new CfReportRowAddDescId();
			reid.setColCode(this.getColCode());
			reid.setReportType(this.getReportType());
			reid.setTableCode(this.getTableCode());
			re.setId(reid);
		}
		re.setAsset(this.getAsset());
		re.setHalfYearReport(this.getHalfYearReport());
		re.setYearReport(this.getYearReport());
		re.setQuarterReport(this.getQuarterReport());
		re.setGroup1(this.getGroup1());
		re.setGroup2(this.getGroup2());
		re.setGroup3(this.getGroup3());
		re.setLifeInsurance1(this.getLifeInsurance1());
		re.setLifeInsurance2(this.getLifeInsurance2());
		re.setLifeInsurance3(this.getLifeInsurance3());
		re.setLifeInsurance4(this.getLifeInsurance4());
		re.setReinsurance(this.getReinsurance());
		re.setPropertyInsurance1(this.getPropertyInsurance1());
		re.setPropertyInsurance2(this.getPropertyInsurance2());
		re.setPropertyInsurance3(this.getPropertyInsurance3());
		re.setOutItemNote(this.getOutItemNote());
		re.setOutItemCreatTime(this.getOutItemCreatTime());
		re.setColDesc(this.colDesc);
		re.setColType(this.colType);
		re.setColName(this.colName);
		re.setTableName(this.tableName);
		return re;
		
	}
}
