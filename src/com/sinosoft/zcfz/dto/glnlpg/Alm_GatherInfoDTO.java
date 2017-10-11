package com.sinosoft.zcfz.dto.glnlpg;

import javax.persistence.Column;

public class Alm_GatherInfoDTO {

	private Integer id;

	private String reportId;

	private String projectCode;

    private Double jcstandardScore;
    
    private Double tsstandardScore;

    private Double integrityScore;

    private Double efficiencyScore;

    private Double subtotalScore;

    private Double adjSubtotalScore;
    private Double adjScore;
    private String weight;

    private Double jcscore;
    private Double tsscore;
    
	private Double rate;

	private String adjustType;
	
	private String projectName;
	
	private String rateName;

	public Double getJcstandardScore() {
		return jcstandardScore;
	}

	public void setJcstandardScore(Double jcstandardScore) {
		this.jcstandardScore = jcstandardScore;
	}

	public Double getTsstandardScore() {
		return tsstandardScore;
	}

	public void setTsstandardScore(Double tsstandardScore) {
		this.tsstandardScore = tsstandardScore;
	}

	public Double getAdjScore() {
		return adjScore;
	}

	public void setAdjScore(Double adjScore) {
		this.adjScore = adjScore;
	}

	public Double getJcscore() {
		return jcscore;
	}

	public void setJcscore(Double jcscore) {
		this.jcscore = jcscore;
	}

	public Double getTsscore() {
		return tsscore;
	}

	public void setTsscore(Double tsscore) {
		this.tsscore = tsscore;
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

	public Double getIntegrityScore() {
		return integrityScore;
	}

	public void setIntegrityScore(Double integrityScore) {
		this.integrityScore = integrityScore;
	}

	public Double getEfficiencyScore() {
		return efficiencyScore;
	}

	public void setEfficiencyScore(Double efficiencyScore) {
		this.efficiencyScore = efficiencyScore;
	}

	public Double getSubtotalScore() {
		return subtotalScore;
	}

	public void setSubtotalScore(Double subtotalScore) {
		this.subtotalScore = subtotalScore;
	}

	public Double getAdjSubtotalScore() {
		return adjSubtotalScore;
	}

	public void setAdjSubtotalScore(Double adjSubtotalScore) {
		this.adjSubtotalScore = adjSubtotalScore;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public String getAdjustType() {
		return adjustType;
	}

	public void setAdjustType(String adjustType) {
		this.adjustType = adjustType;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getRateName() {
		return rateName;
	}

	public void setRateName(String rateName) {
		this.rateName = rateName;
	}

}
