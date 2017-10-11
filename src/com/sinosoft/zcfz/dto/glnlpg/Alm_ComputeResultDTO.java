package com.sinosoft.zcfz.dto.glnlpg;

public class Alm_ComputeResultDTO {
	
	private String reportId;
	 
	private String itemCode;

    private String projectCode;

    private Integer integritySevResult;

    private Double integrityScore;

    private Double adjIntegrityScore;

    private Integer efficiencySevResult;

    private Double efficiencyScore;

    private Double adjEfficiencyScore;

    private Double subtotalScore;

    private Double adjSubtotalScore;

    private String SevBase;

    private Double Sev_Total;

    private Integer Sev_Order;
    
    private String standardScore;
    
    private Double sysIntegrityScore;
    
    private Double folEfficiencyScore;
    
    private Integer itemType;
    
    private String itemNum;
	
	private String itemName;
	
	private String intResult;
	
	private String effResult;
	
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
	public Double getIntegrityScore() {
		return integrityScore;
	}
	public void setIntegrityScore(Double integrityScore) {
		this.integrityScore = integrityScore;
	}
	public Double getAdjIntegrityScore() {
		return adjIntegrityScore;
	}
	public void setAdjIntegrityScore(Double adjIntegrityScore) {
		this.adjIntegrityScore = adjIntegrityScore;
	}
	public Integer getEfficiencySevResult() {
		return efficiencySevResult;
	}
	public void setEfficiencySevResult(Integer efficiencySevResult) {
		this.efficiencySevResult = efficiencySevResult;
	}
	public Double getEfficiencyScore() {
		return efficiencyScore;
	}
	public void setEfficiencyScore(Double efficiencyScore) {
		this.efficiencyScore = efficiencyScore;
	}
	public Double getAdjEfficiencyScore() {
		return adjEfficiencyScore;
	}
	public void setAdjEfficiencyScore(Double adjEfficiencyScore) {
		this.adjEfficiencyScore = adjEfficiencyScore;
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
	public String getSevBase() {
		return SevBase;
	}
	public void setSevBase(String sevBase) {
		SevBase = sevBase;
	}
	public Double getSev_Total() {
		return Sev_Total;
	}
	public void setSev_Total(Double sev_Total) {
		Sev_Total = sev_Total;
	}
	public Integer getSev_Order() {
		return Sev_Order;
	}
	public void setSev_Order(Integer sev_Order) {
		Sev_Order = sev_Order;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getStandardScore() {
		return standardScore;
	}
	public void setStandardScore(String standardScore) {
		this.standardScore = standardScore;
	}
	public Double getSysIntegrityScore() {
		return sysIntegrityScore;
	}
	public void setSysIntegrityScore(Double sysIntegrityScore) {
		this.sysIntegrityScore = sysIntegrityScore;
	}
	public Double getFolEfficiencyScore() {
		return folEfficiencyScore;
	}
	public void setFolEfficiencyScore(Double folEfficiencyScore) {
		this.folEfficiencyScore = folEfficiencyScore;
	}
	public Integer getItemType() {
		return itemType;
	}
	public void setItemType(Integer itemType) {
		this.itemType = itemType;
	}
	public String getItemNum() {
		return itemNum;
	}
	public void setItemNum(String itemNum) {
		this.itemNum = itemNum;
	}
//	public String getItemName() {
//		return itemName;
//	}
//	public void setItemName(String itemName) {
//		this.itemName = itemName;
//	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getIntResult() {
		return intResult;
	}
	public void setIntResult(String intResult) {
		this.intResult = intResult;
	}
	public String getEffResult() {
		return effResult;
	}
	public void setEffResult(String effResult) {
		this.effResult = effResult;
	}
}