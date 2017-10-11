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
@Table(name="CfReportRowAddDataRisk")
public class CfReportRowAddDataRisk {  
	
	@EmbeddedId
	  @AttributeOverrides({
			@AttributeOverride(name = "reportId", column = @Column(name = "ReportId", nullable = false, length = 300)),
			@AttributeOverride(name = "rowNo", column = @Column(name = "RowNo", nullable = false, length = 300)),
			@AttributeOverride(name = "colCode", column = @Column(name = "ColCode", nullable = false, length = 300))})
	private CfReportRowAddDataRiskId id;
      
    /**  
     */ 
    @Column
    private String tableCode;       
    /**  
     */ 
    @Column
    private String comCode;  
   
    /**  
     */ 
    @Column
    private String colType;  
    /**  
     */ 
    @Column
    private BigDecimal numValue;  
    /**  
     */ 
    @Column
    private BigDecimal wanValue;  
    /**  
     */ 
    @Column
    private String textValue;  
    /**  
     */ 
    @Column
    private String desText;  
    /**  
     */ 
    @Column
    private String reportType;  
    /**  
     */ 
    @Column
    private String yearMonth;  
    /**  
     */ 
    @Column
    private String quarter;  
    /**  
     */ 
    @Column
    private String reportRate;  
    /**  
     */ 
    @Column
    private String source;  
    /**  
     */ 
    @Column
    private String operator2;  
    /**  
     */ 
    @Column
    private String operDate2;  
    /**  
     */ 
 
    public CfReportRowAddDataRiskId getId() {
		return id;
	}

	public void setId(CfReportRowAddDataRiskId id) {
		this.id = id;
	}

	public void setTableCode(String tableCode) {  
        this.tableCode = tableCode;  
    }  
      
    public String getTableCode() {  
        return this.tableCode;  
    }  
   
    public void setComCode(String comCode) {  
        this.comCode = comCode;  
    }  
      
    public String getComCode() {  
        return this.comCode;  
    }  
   
    public void setColType(String colType) {  
        this.colType = colType;  
    }  
      
    public String getColType() {  
        return this.colType;  
    }  
    
    public BigDecimal getNumValue() {
		return numValue;
	}

	public void setNumValue(BigDecimal numValue) {
		this.numValue = numValue;
	}

	public BigDecimal getWanValue() {
		return wanValue;
	}

	public void setWanValue(BigDecimal wanValue) {
		this.wanValue = wanValue;
	}

	public void setTextValue(String textValue) {  
        this.textValue = textValue;  
    }  
      
    public String getTextValue() {  
        return this.textValue;  
    }  
    public void setDesText(String desText) {  
        this.desText = desText;  
    }  
      
    public String getDesText() {  
        return this.desText;  
    }  
    public void setReportType(String reportType) {  
        this.reportType = reportType;  
    }  
      
    public String getReportType() {  
        return this.reportType;  
    }  
    public void setYearMonth(String yearMonth) {  
        this.yearMonth = yearMonth;  
    }  
      
    public String getYearMonth() {  
        return this.yearMonth;  
    }  
    public void setQuarter(String quarter) {  
        this.quarter = quarter;  
    }  
      
    public String getQuarter() {  
        return this.quarter;  
    }  
    public void setReportRate(String reportRate) {  
        this.reportRate = reportRate;  
    }  
      
    public String getReportRate() {  
        return this.reportRate;  
    }  
    public void setSource(String source) {  
        this.source = source;  
    }  
      
    public String getSource() {  
        return this.source;  
    }  
    public void setOperator2(String operator2) {  
        this.operator2 = operator2;  
    }  
      
    public String getOperator2() {  
        return this.operator2;  
    }  
    public void setOperDate2(String operDate2) {  
        this.operDate2 = operDate2;  
    }  
      
    public String getOperDate2() {  
        return this.operDate2;  
    }  
     
}