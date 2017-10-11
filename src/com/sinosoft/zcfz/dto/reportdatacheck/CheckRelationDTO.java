package com.sinosoft.zcfz.dto.reportdatacheck;

import java.math.BigDecimal;

import com.sinosoft.entity.CfReportDataCheck;
import com.sinosoft.entity.CfReportItemRowCheck;
import com.sinosoft.entity.CfReportPeriodDataCheck;
import com.sinosoft.entity.CfReportRowDataCheck;

public class CheckRelationDTO {
	private String checkSchemaCode; //校验关系表的代码
	
	private String id;
	private String calFormula;
	private String remark;
	private String calType;
	private String isNeedChk;
	private String tempReport; //临时报告3
	private String quarterReport; //季度报告2
	private String quarterFalshReport; //季度快报1
	
	private String tableCode; //表名
	
	private String colCode; //列名 -- CfReportItemRowCheck
	private String reportItemCode;  //因子指标 -- CfReportItemRowCheck
	
	private String itemCodeBq;   //指标代码本期数 -- CfReportPeriodDataCheck
	private String itemCodeSq;   //指标代码上期数 -- CfReportPeriodDataCheck
	private String relationOperator;   //相关操作人 -- CfReportPeriodDataCheck
	
	private BigDecimal tolerance;  //容差
	
	
	public BigDecimal getTolerance() {
		return tolerance;
	}

	public void setTolerance(BigDecimal tolerance) {
		this.tolerance = tolerance;
	}

	public String getTempReport() {
		return tempReport;
	}

	public void setTempReport(String tempReport) {
		this.tempReport = tempReport;
	}

	public String getQuarterReport() {
		return quarterReport;
	}

	public void setQuarterReport(String quarterReport) {
		this.quarterReport = quarterReport;
	}

	public String getQuarterFalshReport() {
		return quarterFalshReport;
	}

	public void setQuarterFalshReport(String quarterFalshReport) {
		this.quarterFalshReport = quarterFalshReport;
	}

	public String getItemCodeBq() {
		return itemCodeBq;
	}

	public void setItemCodeBq(String itemCodeBq) {
		this.itemCodeBq = itemCodeBq;
	}

	public String getItemCodeSq() {
		return itemCodeSq;
	}

	public void setItemCodeSq(String itemCodeSq) {
		this.itemCodeSq = itemCodeSq;
	}

	public String getRelationOperator() {
		return relationOperator;
	}

	public void setRelationOperator(String relationOperator) {
		this.relationOperator = relationOperator;
	}

	public String getReportItemCode() {
		return reportItemCode;
	}

	public void setReportItemCode(String reportItemCode) {
		this.reportItemCode = reportItemCode;
	}

	public String getColCode() {
		return colCode;
	}

	public void setColCode(String colCode) {
		this.colCode = colCode;
	}

	public String getTableCode() {
		return tableCode;
	}

	public void setTableCode(String tableCode) {
		this.tableCode = tableCode;
	}

	public String getCheckSchemaCode() {
		return checkSchemaCode;
	}

	public void setCheckSchemaCode(String checkSchemaCode) {
		this.checkSchemaCode = checkSchemaCode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCalFormula() {
		return calFormula;
	}

	public void setCalFormula(String calFormula) {
		this.calFormula = calFormula;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCalType() {
		return calType;
	}

	public void setCalType(String calType) {
		this.calType = calType;
	}

	public String getIsNeedChk() {
		return isNeedChk;
	}

	public void setIsNeedChk(String isNeedChk) {
		this.isNeedChk  = isNeedChk;
//		this.isNeedChk  = quarterFalshReport+quarterReport+tempReport;
	}

	/**
	 * 通过dto转换成CfReportDataCheck实体类
	 */
	public static CfReportDataCheck toCfReportDataCheckEntity(CheckRelationDTO dto) {
		CfReportDataCheck checkSchema = new CfReportDataCheck();
		
		if(dto.getId() != null){
			checkSchema.setSerialNo(dto.getId()==""?0:Integer.parseInt(dto.getId()));
		}
		checkSchema.setCalFormula(dto.getCalFormula());
		checkSchema.setCalType(dto.getCalType());
//		checkSchema.setCalType("ALL");
		checkSchema.setIsNeedChk(dto.getIsNeedChk());
		checkSchema.setRemark(dto.getRemark());
		checkSchema.setTolerance(dto.getTolerance());
		
		return checkSchema;
	}

	/**
	 * 通过dto转换成CfReportRowDataCheck实体类
	 */
	public static CfReportRowDataCheck toCfReportRowDataCheckEntity(CheckRelationDTO dto) {
		CfReportRowDataCheck schema = new CfReportRowDataCheck();
		
		if(dto.getId() != null){
			schema.setSerialNo(dto.getId()==""?0:Integer.parseInt(dto.getId()));
		}
		schema.setCalFormula(dto.getCalFormula());
		schema.setCalType(dto.getCalType());
//		schema.setCalType("ALL");
		schema.setIsNeedChk(dto.getIsNeedChk());
		schema.setRemark(dto.getRemark());
		schema.setTableCode(dto.getTableCode());
		schema.setTolerance(dto.getTolerance());
		
		return schema;
	}
	
	/**
	 * 通过dto转换成CfReportItemRowCheck实体类
	 */
	public static CfReportItemRowCheck toCfReportItemRowCheckEntity(CheckRelationDTO dto) {
		CfReportItemRowCheck schema = new CfReportItemRowCheck();
		
		if(dto.getId() != null){
			schema.setSerialNo(dto.getId()==""?0:Integer.parseInt(dto.getId()));
		}
		schema.setCalType(dto.getCalType());
//		schema.setCalType("ALL");
		schema.setIsNeedChk(dto.getIsNeedChk());
		schema.setRemark(dto.getRemark());
		schema.setTableCode(dto.getTableCode());
		schema.setColCode(dto.getColCode());
		schema.setReportItemCode(dto.getReportItemCode());
		schema.setTolerance(dto.getTolerance());
		
		return schema;
	}

	/**
	 * 通过dto转换成CfReportPeriodDataCheck实体类
	 */
	public static CfReportPeriodDataCheck toCfReportPeriodDataCheckEntity(CheckRelationDTO dto) {
		CfReportPeriodDataCheck schema = new CfReportPeriodDataCheck();
		
		if(dto.getId() != null){
			schema.setSerialNo(dto.getId()==""?0:Integer.parseInt(dto.getId()));
		}
		schema.setCalType(dto.getCalType());
//		schema.setCalType("ALL");
		schema.setIsNeedChk(dto.getIsNeedChk());
		schema.setRemark(dto.getRemark());
		schema.setItemCodeBq(dto.getItemCodeBq());
		schema.setItemCodeSq(dto.getItemCodeSq());
		schema.setRelationOperator(dto.getRelationOperator());
		schema.setTolerance(dto.getTolerance());
		
		return schema;
	}

	@Override
	public String toString() {
		return "CheckRelationDTO [checkSchemaCode=" + checkSchemaCode + ", id="
				+ id + ", calFormula=" + calFormula + ", remark=" + remark
				+ ", calType=" + calType + ", isNeedChk=" + isNeedChk
				+ ", tempReport=" + tempReport + ", quarterReport="
				+ quarterReport + ", quarterFalshReport=" + quarterFalshReport
				+ ", tableCode=" + tableCode + ", colCode=" + colCode
				+ ", reportItemCode=" + reportItemCode + ", itemCodeBq="
				+ itemCodeBq + ", itemCodeSq=" + itemCodeSq
				+ ", relationOperator=" + relationOperator + ", tolerance="
				+ tolerance + "]";
	}
	
	
	
}
