package com.sinosoft.zcfz.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class Alm_GatherInfo implements Serializable{
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id", nullable=false, unique=true)
	private Integer id;
	@Column
	private String reportId;
	@Column
	private String projectCode;
	@Column
    private BigDecimal jcstandardScore;
	@Column
    private BigDecimal tsstandardScore;
	@Column
    private BigDecimal integrityScore;
	@Column
    private BigDecimal efficiencyScore;
	@Column
    private BigDecimal subtotalScore;
	@Column
    private BigDecimal adjSubtotalScore;
	@Column
    private BigDecimal adjScore;
	@Column
    private String weight;
	@Column
    private BigDecimal jcscore;
	@Column
    private BigDecimal tsscore;
	@Column
	private BigDecimal rate;
	@Column
	private String adjustType;

	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public String getProjectCode() {
		return projectCode;
	}
	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}
	public String getAdjustType() {
		return adjustType;
	}
	public void setAdjustType(String adjustType) {
		this.adjustType = adjustType;
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
	public BigDecimal getRate() {
		return rate;
	}
	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}
	public BigDecimal getJcstandardScore() {
		return jcstandardScore;
	}
	public void setJcstandardScore(BigDecimal jcstandardScore) {
		this.jcstandardScore = jcstandardScore;
	}
	public BigDecimal getTsstandardScore() {
		return tsstandardScore;
	}
	public void setTsstandardScore(BigDecimal tsstandardScore) {
		this.tsstandardScore = tsstandardScore;
	}
	public BigDecimal getJcscore() {
		return jcscore;
	}
	public void setJcscore(BigDecimal jcscore) {
		this.jcscore = jcscore;
	}
	public BigDecimal getTsscore() {
		return tsscore;
	}
	public void setTsscore(BigDecimal tsscore) {
		this.tsscore = tsscore;
	}
	public BigDecimal getAdjScore() {
		return adjScore;
	}
	public void setAdjScore(BigDecimal adjScore) {
		this.adjScore = adjScore;
	}
}