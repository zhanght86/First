package com.sinosoft.entity;  

import java.math.BigDecimal;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="CfReportDataRisk")
public class CfReportDataRisk { 
	
	@EmbeddedId
	  @AttributeOverrides({
			@AttributeOverride(name = "reportId", column = @Column(name = "ReportId", nullable = false, length = 300)),
			@AttributeOverride(name = "outItemCode", column = @Column(name = "OutItemCode", nullable = false, length = 300))})
	

	private CfReportDataRiskId id;    
    
    @Column
    private String comCode; 
   
    @Column
    private String outItemType;  
   
    @Column
    private String outItemCodeName;  
   
    @Column
    private String reportItemCode;  
    
    @Column
    private BigDecimal reportItemValue;  
    
    @Column
    private BigDecimal reportItemWanValue;  
     
    @Column
    private String textValue;  
    
    @Column
    private String desText;  
     
    @Column
    private String fileText;  
    
    @Column
    private String month;  
     
    @Column
    private String quarter;  
    
    @Column
    private String source;  
     
    @Column
    private String reportType;  
    
    @Column
    private String reportRate;  
    
    @Column
    private String computeLevel;  
     
    @Column
    private String reportItemValueSource;  
     
    @Column
    private String operator;  
    
    @Column
    private String operDate;  
    
    @Column
    private String temp;  
    
    public CfReportDataRiskId getId() {
		return id;
	}

	public void setId(CfReportDataRiskId id) {
		this.id = id;
	}

	public void setComCode(String comCode) {  
        this.comCode = comCode;  
    }  
      
    public String getComCode() {  
        return this.comCode;  
    }  
    public void setOutItemType(String outItemType) {  
        this.outItemType = outItemType;  
    }  
      
    public String getOutItemType() {  
        return this.outItemType;  
    }  
    public void setOutItemCodeName(String outItemCodeName) {  
        this.outItemCodeName = outItemCodeName;  
    }  
      
    public String getOutItemCodeName() {  
        return this.outItemCodeName;  
    }  
    public void setReportItemCode(String reportItemCode) {  
        this.reportItemCode = reportItemCode;  
    }  
      
    public String getReportItemCode() {  
        return this.reportItemCode;  
    }  
   
    public void setTextValue(String textValue) {  
        this.textValue = textValue;  
    }  
      
    public String getTextValue() {  
        return this.textValue;  
    }  
    
    public BigDecimal getReportItemValue() {
		return reportItemValue;
	}

	public void setReportItemValue(BigDecimal reportItemValue) {
		this.reportItemValue = reportItemValue;
	}

	public BigDecimal getReportItemWanValue() {
		return reportItemWanValue;
	}

	public void setReportItemWanValue(BigDecimal reportItemWanValue) {
		this.reportItemWanValue = reportItemWanValue;
	}

	public String getDesText() {
		return desText;
	}

	public void setDesText(String desText) {
		this.desText = desText;
	}

	public String getFileText() {
		return fileText;
	}

	public void setFileText(String fileText) {
		this.fileText = fileText;
	}

	public void setMonth(String month) {  
        this.month = month;  
    }  
      
    public String getMonth() {  
        return this.month;  
    }  
    public void setQuarter(String quarter) {  
        this.quarter = quarter;  
    }  
      
    public String getQuarter() {  
        return this.quarter;  
    }  
    public void setSource(String source) {  
        this.source = source;  
    }  
      
    public String getSource() {  
        return this.source;  
    }  
    public void setReportType(String reportType) {  
        this.reportType = reportType;  
    }  
      
    public String getReportType() {  
        return this.reportType;  
    }  
    public void setReportRate(String reportRate) {  
        this.reportRate = reportRate;  
    }  
      
    public String getReportRate() {  
        return this.reportRate;  
    }  
    public void setComputeLevel(String computeLevel) {  
        this.computeLevel = computeLevel;  
    }  
      
    public String getComputeLevel() {  
        return this.computeLevel;  
    }  
    public void setReportItemValueSource(String reportItemValueSource) {  
        this.reportItemValueSource = reportItemValueSource;  
    }  
      
    public String getReportItemValueSource() {  
        return this.reportItemValueSource;  
    }  
    public void setOperator(String operator) {  
        this.operator = operator;  
    }  
      
    public String getOperator() {  
        return this.operator;  
    }  
    public void setOperDate(String operDate) {  
        this.operDate = operDate;  
    }  
      
    public String getOperDate() {  
        return this.operDate;  
    }  
    public void setTemp(String temp) {  
        this.temp = temp;  
    }  
      
    public String getTemp() {  
        return this.temp;  
    }  

 
}