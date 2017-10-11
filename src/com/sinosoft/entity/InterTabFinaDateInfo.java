package com.sinosoft.entity;  

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
@Table(name="interTabFinaDateInfo")
public class InterTabFinaDateInfo {  
	
	@EmbeddedId
	  @AttributeOverrides({
			@AttributeOverride(name = "reportId", column = @Column(name = "ReportId", nullable = false, length = 300)),
			@AttributeOverride(name = "properCode", column = @Column(name = "ProperCode", nullable = false, length = 300)),
			@AttributeOverride(name = "accountType", column = @Column(name = "AccountType", nullable = false, length = 300))})
	  private InterTabFinaDateInfoId id;  
      
    /**  
     */ 
    @Column
    private String year;  
    /**  
     */ 
    @Column
    private String quarter;  
    /**  
     */ 
    @Column
    private String reportDate;     
    /**  
     */ 
    @Column
    private String properName;  
    /**  
     */ 
    @Column
    private String startDate;  
    /**  
     */ 
    @Column
    private String endDate;  
    /**  
     */ 
    @Column
    private String currDate;  
    /**  
     */ 
    @Column
    private String temp1;  
    /**  
     */ 
    @Column
    private String temp2;  
    /**  
     */ 
    @Column
    private String temp3;  
    /**  
     */ 
    @Column
    private String temp4;  
    /**  
     */ 
    @Column
    private String temp5;  
 
    public InterTabFinaDateInfoId getId() {
		return id;
	}

	public void setId(InterTabFinaDateInfoId id) {
		this.id = id;
	}

	public void setYear(String year) {  
        this.year = year;  
    }  
      
    public String getYear() {  
        return this.year;  
    }  
    public void setQuarter(String quarter) {  
        this.quarter = quarter;  
    }  
      
    public String getQuarter() {  
        return this.quarter;  
    }  
    public void setReportDate(String reportDate) {  
        this.reportDate = reportDate;  
    }  
      
    public String getReportDate() {  
        return this.reportDate;  
    }  
    
    public void setProperName(String properName) {  
        this.properName = properName;  
    }  
      
    public String getProperName() {  
        return this.properName;  
    }  
    public void setStartDate(String startDate) {  
        this.startDate = startDate;  
    }  
      
    public String getStartDate() {  
        return this.startDate;  
    }  
    public void setEndDate(String endDate) {  
        this.endDate = endDate;  
    }  
      
    public String getEndDate() {  
        return this.endDate;  
    }  
    public void setCurrDate(String currDate) {  
        this.currDate = currDate;  
    }  
      
    public String getCurrDate() {  
        return this.currDate;  
    }  
    public void setTemp1(String temp1) {  
        this.temp1 = temp1;  
    }  
      
    public String getTemp1() {  
        return this.temp1;  
    }  
    public void setTemp2(String temp2) {  
        this.temp2 = temp2;  
    }  
      
    public String getTemp2() {  
        return this.temp2;  
    }  
    public void setTemp3(String temp3) {  
        this.temp3 = temp3;  
    }  
      
    public String getTemp3() {  
        return this.temp3;  
    }  
    public void setTemp4(String temp4) {  
        this.temp4 = temp4;  
    }  
      
    public String getTemp4() {  
        return this.temp4;  
    }  
    public void setTemp5(String temp5) {  
        this.temp5 = temp5;  
    }  
      
    public String getTemp5() {  
        return this.temp5;  
    }  
 
}