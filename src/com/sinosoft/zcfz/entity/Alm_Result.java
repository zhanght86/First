package com.sinosoft.zcfz.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class Alm_Result implements Serializable{
	
	@EmbeddedId
	private Alm_ResultId  id;
	@Column
    private String projectCode;
	@Column
    private Integer integritySevResult;
	@Column
    private BigDecimal integrityScore;
	@Column
    private BigDecimal adjIntegrityScore;
	@Column
    private Integer efficiencySevResult;
	@Column
    private BigDecimal efficiencyScore;
	@Column
    private BigDecimal adjEfficiencyScore;
	@Column
    private BigDecimal subtotalScore;
	@Column
    private BigDecimal adjSubtotalScore;
	@Column
    private BigDecimal adjustCoefficient;
	@Column
    private String sevBase;
	@Column
    private Double sev_Total;
	@Column
    private Integer sev_Order;
	@Column
    private Integer itemType;
	@Column
	private String adjustOpinion;
	
	public String getAdjustOpinion() {
		return adjustOpinion;
	}
	public void setAdjustOpinion(String adjustOpinion) {
		this.adjustOpinion = adjustOpinion;
	}
	public Alm_ResultId getId() {
		return id;
	}
	public void setId(Alm_ResultId id) {
		this.id = id;
	}
	public String getProjectCode() {
		return projectCode;
	}
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	public Integer getIntegritySevResult() {
		return integritySevResult;
	}
	public void setIntegritySevResult(Integer integritySevResult) {
		this.integritySevResult = integritySevResult;
	}
	public BigDecimal getIntegrityScore() {
		return integrityScore;
	}
	public void setIntegrityScore(BigDecimal integrityScore) {
		this.integrityScore = integrityScore;
	}
	public BigDecimal getAdjIntegrityScore() {
		return adjIntegrityScore;
	}
	public void setAdjIntegrityScore(BigDecimal adjIntegrityScore) {
		this.adjIntegrityScore = adjIntegrityScore;
	}
	public Integer getEfficiencySevResult() {
		return efficiencySevResult;
	}
	public void setEfficiencySevResult(Integer efficiencySevResult) {
		this.efficiencySevResult = efficiencySevResult;
	}
	public BigDecimal getEfficiencyScore() {
		return efficiencyScore;
	}
	public void setEfficiencyScore(BigDecimal efficiencyScore) {
		this.efficiencyScore = efficiencyScore;
	}
	public BigDecimal getAdjEfficiencyScore() {
		return adjEfficiencyScore;
	}
	public void setAdjEfficiencyScore(BigDecimal adjEfficiencyScore) {
		this.adjEfficiencyScore = adjEfficiencyScore;
	}
	public BigDecimal getSubtotalScore() {
		return subtotalScore;
	}
	public void setSubtotalScore(BigDecimal subtotalScore) {
		this.subtotalScore = subtotalScore;
	}
	public BigDecimal getAdjSubtotalScore() {
		return adjSubtotalScore;
	}
	public void setAdjSubtotalScore(BigDecimal adjSubtotalScore) {
		this.adjSubtotalScore = adjSubtotalScore;
	}
	public BigDecimal getAdjustCoefficient() {
		return adjustCoefficient;
	}
	public void setAdjustCoefficient(BigDecimal adjustCoefficient) {
		this.adjustCoefficient = adjustCoefficient;
	}
	public String getSevBase() {
		return sevBase;
	}
	public void setSevBase(String sevBase) {
		this.sevBase = sevBase;
	}
	public Double getSev_Total() {
		return sev_Total;
	}
	public void setSev_Total(Double sev_Total) {
		this.sev_Total = sev_Total;
	}
	public Integer getSev_Order() {
		return sev_Order;
	}
	public void setSev_Order(Integer sev_Order) {
		this.sev_Order = sev_Order;
	}
	public Integer getItemType() {
		return itemType;
	}
	public void setItemType(Integer itemType) {
		this.itemType = itemType;
	}
	
}