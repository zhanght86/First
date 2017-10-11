package com.sinosoft.zcfz.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Alm_ReportData implements Serializable{
	
	@EmbeddedId
	private Alm_ReportDataId  id;
	@Column
    private String projectCode;
	@Column
    private String SevBase;
	@Column
    private String integrityAdjPlan;
	@Column
    private Integer integritySevResult;
	@Column
    private BigDecimal integrityScore;
	@Column
    private String SevBaseFile;
	@Column
    private String efficiencyAdjPlan;
	@Column
    private Integer efficiencySevResult;
	@Column
    private BigDecimal efficiencyScore;
	@Column
    private String sevStatus;
	@Column
    private BigDecimal deptWeight;
	@Column
    private String oper_Id;
	@Column
    private String oper_Datetime;
	@Column
    private String auditor_Id;
	@Column
    private String auditor_Datetime;
	@Column
    private String auditorOpinion;
	@Column
    private String temp;
	@Column
    private Integer itemType;
	@Column
    private String instanceId;
	@Column
    private BigDecimal subtotalScore;
	

	public BigDecimal getSubtotalScore() {
		return subtotalScore;
	}

	public void setSubtotalScore(BigDecimal subtotalScore) {
		this.subtotalScore = subtotalScore;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public Alm_ReportDataId getId() {
		return this.id;
	}

	public void setId(Alm_ReportDataId id) {
		this.id = id;
	}
	public String getProjectCode() {
		return projectCode;
	}
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	public String getIntegrityAdjPlan() {
		return integrityAdjPlan;
	}
	public void setIntegrityAdjPlan(String integrityAdjPlan) {
		this.integrityAdjPlan = integrityAdjPlan;
	}
	public Integer getIntegritySevResult() {
		return integritySevResult;
	}
	public void setIntegritySevResult(Integer integritySevResult) {
		this.integritySevResult = integritySevResult;
	}
	public String getEfficiencyAdjPlan() {
		return efficiencyAdjPlan;
	}
	public void setEfficiencyAdjPlan(String efficiencyAdjPlan) {
		this.efficiencyAdjPlan = efficiencyAdjPlan;
	}
	public Integer getEfficiencySevResult() {
		return efficiencySevResult;
	}
	public void setEfficiencySevResult(Integer efficiencySevResult) {
		this.efficiencySevResult = efficiencySevResult;
	}
	public String getSevStatus() {
		return sevStatus;
	}
	public void setSevStatus(String sevStatus) {
		this.sevStatus = sevStatus;
	}
	public BigDecimal getIntegrityScore() {
		return integrityScore;
	}

	public void setIntegrityScore(BigDecimal integrityScore) {
		this.integrityScore = integrityScore;
	}

	public BigDecimal getEfficiencyScore() {
		return efficiencyScore;
	}

	public void setEfficiencyScore(BigDecimal efficiencyScore) {
		this.efficiencyScore = efficiencyScore;
	}

	public BigDecimal getDeptWeight() {
		return deptWeight;
	}

	public void setDeptWeight(BigDecimal deptWeight) {
		this.deptWeight = deptWeight;
	}

	public String getOper_Id() {
		return oper_Id;
	}
	public void setOper_Id(String oper_Id) {
		this.oper_Id = oper_Id;
	}
	public String getOper_Datetime() {
		return oper_Datetime;
	}
	public void setOper_Datetime(String oper_Datetime) {
		this.oper_Datetime = oper_Datetime;
	}
	public String getAuditor_Id() {
		return auditor_Id;
	}
	public void setAuditor_Id(String auditor_Id) {
		this.auditor_Id = auditor_Id;
	}
	public String getAuditor_Datetime() {
		return auditor_Datetime;
	}
	public void setAuditor_Datetime(String auditor_Datetime) {
		this.auditor_Datetime = auditor_Datetime;
	}
	public String getAuditorOpinion() {
		return auditorOpinion;
	}
	public void setAuditorOpinion(String auditorOpinion) {
		this.auditorOpinion = auditorOpinion;
	}
	public String getTemp() {
		return temp;
	}
	public void setTemp(String temp) {
		this.temp = temp;
	}

	public String getSevBase() {
		return SevBase;
	}

	public void setSevBase(String sevBase) {
		SevBase = sevBase;
	}

	public String getSevBaseFile() {
		return SevBaseFile;
	}

	public void setSevBaseFile(String sevBaseFile) {
		SevBaseFile = sevBaseFile;
	}

	public Integer getItemType() {
		return itemType;
	}

	public void setItemType(Integer itemType) {
		this.itemType = itemType;
	}

}