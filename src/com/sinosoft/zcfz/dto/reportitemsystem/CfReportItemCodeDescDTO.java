package com.sinosoft.zcfz.dto.reportitemsystem;

import java.sql.Date;

import com.sinosoft.entity.CfReportItemCodeDesc;


public class CfReportItemCodeDescDTO {
	private String sort;
	private String order;
	private String rowComputePara;
	private String rowReportItemCode;
	private String rowReportItemName;
	private String colComputePara;
	private String colReportItemCode;
	private String colReportItemName;
	private String importSource;
	private String reportItemDesc;
	private String computeLevel;
	private String isDetailExits;
	private String isDataUpdate;
	private String reportItemType;
	private String composeFormula;
	private String composeFormulaName;
	private String composeFlag;
	private String temp1;
	private String temp2;
	private String temp3;
	private String reportItemDescForYear;
	
	
	
	
	
	private String reportItemCode;
	
	private String reportItemName;
	
	private String reportCode;
	
	private String outItemCode;
	
	private String outItemType;
	
	private String outItemTypeName;
	
	private String outItemState;
	
	private String outItemNote;
	
	private String outItemVersion;
	
	private Date outItemCreatTime;
	
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
	
	private String reportType;
	private String reportTypeName;
	
	private String beginTime;
	
	private String endTime;
	private String state;

	private String departmentName;
	
	
	public String getDepartmentName() {
		return departmentName;
	}
	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getReportItemCode() {
		return reportItemCode;
	}
	public void setReportItemCode(String reportItemCode) {
		this.reportItemCode = reportItemCode;
	}
	public String getReportItemName() {
		return reportItemName;
	}
	public void setReportItemName(String reportItemName) {
		this.reportItemName = reportItemName;
	}
	public String getReportCode() {
		return reportCode;
	}
	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}
	public String getOutItemCode() {
		return outItemCode;
	}
	public void setOutItemCode(String outItemCode) {
		this.outItemCode = outItemCode;
	}
	public String getOutItemType() {
		return outItemType;
	}
	public void setOutItemType(String outItemType) {
		this.outItemType = outItemType;
	}
	public String getOutItemState() {
		return outItemState;
	}
	public void setOutItemState(String outItemState) {
		this.outItemState = outItemState;
	}
	public String getOutItemNote() {
		return outItemNote;
	}
	public void setOutItemNote(String outItemNote) {
		this.outItemNote = outItemNote;
	}
	public String getOutItemVersion() {
		return outItemVersion;
	}
	public void setOutItemVersion(String outItemVersion) {
		this.outItemVersion = outItemVersion;
	}
	public Date getOutItemCreatTime() {
		return outItemCreatTime;
	}
	public void setOutItemCreatTime(Date outItemCreatTime) {
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
		this.propertyInsurance2Name = returnName(propertyInsurance2);
	}
	public String getPropertyInsurance3() {
		return propertyInsurance3;
	}
	public void setPropertyInsurance3(String propertyInsurance3) {
		this.propertyInsurance3 = propertyInsurance3;
		this.propertyInsurance3Name = returnName(propertyInsurance3);
	}
	public String getReinsurance() {
		return reinsurance;
	}
	public void setReinsurance(String reinsurance) {
		this.reinsurance = reinsurance;
		this.reinsuranceName = returnName(reinsurance);
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
	public String getReportType() {
		return reportType;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}
	public String getOutItemTypeName() {
		return outItemTypeName;
	}
	public void setOutItemTypeName(String outItemTypeName) {
		this.outItemTypeName = outItemTypeName;
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
	public String getRowComputePara() {
		return rowComputePara;
	}
	public void setRowComputePara(String rowComputePara) {
		this.rowComputePara = rowComputePara;
	}
	public String getRowReportItemCode() {
		return rowReportItemCode;
	}
	public void setRowReportItemCode(String rowReportItemCode) {
		this.rowReportItemCode = rowReportItemCode;
	}
	public String getRowReportItemName() {
		return rowReportItemName;
	}
	public void setRowReportItemName(String rowReportItemName) {
		this.rowReportItemName = rowReportItemName;
	}
	public String getColComputePara() {
		return colComputePara;
	}
	public void setColComputePara(String colComputePara) {
		this.colComputePara = colComputePara;
	}
	public String getColReportItemCode() {
		return colReportItemCode;
	}
	public void setColReportItemCode(String colReportItemCode) {
		this.colReportItemCode = colReportItemCode;
	}
	public String getColReportItemName() {
		return colReportItemName;
	}
	public void setColReportItemName(String colReportItemName) {
		this.colReportItemName = colReportItemName;
	}
	public String getImportSource() {
		return importSource;
	}
	public void setImportSource(String importSource) {
		this.importSource = importSource;
	}
	public String getReportItemDesc() {
		return reportItemDesc;
	}
	public void setReportItemDesc(String reportItemDesc) {
		this.reportItemDesc = reportItemDesc;
	}
	public String getComputeLevel() {
		return computeLevel;
	}
	public void setComputeLevel(String computeLevel) {
		this.computeLevel = computeLevel;
	}
	public String getIsDetailExits() {
		return isDetailExits;
	}
	public void setIsDetailExits(String isDetailExits) {
		this.isDetailExits = isDetailExits;
	}
	public String getIsDataUpdate() {
		return isDataUpdate;
	}
	public void setIsDataUpdate(String isDataUpdate) {
		this.isDataUpdate = isDataUpdate;
	}
	public String getReportItemType() {
		return reportItemType;
	}
	public void setReportItemType(String reportItemType) {
		this.reportItemType = reportItemType;
	}
	public String getComposeFormula() {
		return composeFormula;
	}
	public void setComposeFormula(String composeFormula) {
		this.composeFormula = composeFormula;
	}
	public String getComposeFormulaName() {
		return composeFormulaName;
	}
	public void setComposeFormulaName(String composeFormulaName) {
		this.composeFormulaName = composeFormulaName;
	}
	public String getComposeFlag() {
		return composeFlag;
	}
	public void setComposeFlag(String composeFlag) {
		this.composeFlag = composeFlag;
	}
	public String getTemp1() {
		return temp1;
	}
	public void setTemp1(String temp1) {
		this.temp1 = temp1;
	}
	public String getTemp2() {
		return temp2;
	}
	public void setTemp2(String temp2) {
		this.temp2 = temp2;
	}
	public String getTemp3() {
		return temp3;
	}
	public void setTemp3(String temp3) {
		this.temp3 = temp3;
	}
	public String getReportItemDescForYear() {
		return reportItemDescForYear;
	}
	public void setReportItemDescForYear(String reportItemDescForYear) {
		this.reportItemDescForYear = reportItemDescForYear;
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
	public CfReportItemCodeDesc toCfReportItemCodeDesc(CfReportItemCodeDesc re){
		if(re==null){
			re=new CfReportItemCodeDesc();
		}
		if(re.getReportItemCode()==null){
			re.setReportItemCode(this.getReportItemCode());
		}
		re.setReportItemType(this.getReportItemType());
		re.setAsset(this.getAsset());
		re.setReportItemName(this.getReportItemName());
		re.setHalfYearReport(this.getHalfYearReport());
		re.setYearReport(this.getYearReport());
		re.setReportType(this.getReportType());
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
		re.setOutItemCode(this.getOutItemCode());
		re.setOutItemType(this.getOutItemType());
		re.setOutItemState(this.getOutItemState());
		re.setOutItemNote(this.getOutItemNote());
		re.setOutItemVersion(this.getOutItemVersion());
		re.setOutItemCreatTime(this.getOutItemCreatTime());
		re.setReportCode(this.getReportCode());
		re.setRowComputePara(this.getRowComputePara());
		re.setRowReportItemCode(this.getRowReportItemCode());
		re.setRowReportItemName(this.getRowReportItemName());
		re.setColComputePara(this.getColComputePara());
		re.setColReportItemCode(this.getColReportItemCode());
		re.setColReportItemName(this.getColReportItemName());
		re.setImportSource(this.getImportSource());
		re.setReportItemDesc(this.getReportItemDesc());
		re.setComputeLevel(this.getComputeLevel());
		re.setIsDetailExits(this.getIsDetailExits());
		re.setIsDataUpdate(this.getIsDataUpdate());
		re.setComposeFormula(this.getComposeFormula());
		re.setComposeFormulaName(this.getComposeFormulaName());
		re.setComposeFlag(this.getComposeFlag());
		re.setTemp1(this.getTemp1());
		re.setTemp2(this.getTemp2());
		re.setTemp3(this.getTemp3());
		re.setReportItemDescForYear(this.getReportItemDescForYear());
		return re;
		
	}
}

