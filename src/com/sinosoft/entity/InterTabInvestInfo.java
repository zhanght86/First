package com.sinosoft.entity; 

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="interTabInvestInfo")
public class InterTabInvestInfo {  
	
	@EmbeddedId
	  @AttributeOverrides({
			@AttributeOverride(name = "reportId", column = @Column(name = "ReportId", nullable = false, length = 300)),
			@AttributeOverride(name = "properCode", column = @Column(name = "ProperCode", nullable = false, length = 300)),
			@AttributeOverride(name = "accounts", column = @Column(name = "Accounts", nullable = false, length = 300))})
	  private InterTabInvestInfoId id;
      
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
    private String issurer;  
    /**  
     */ 
    @Column
    private String creditLevel;  
    /**  
     */ 
    @Column
    private String fixedDuration;      
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
 
    public InterTabInvestInfoId getId() {
		return id;
	}

	public void setId(InterTabInvestInfoId id) {
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
  
    public String getReportDate() {
		return reportDate;
	}

	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}

	public void setProperName(String properName) {  
        this.properName = properName;  
    }  
      
    public String getProperName() {  
        return this.properName;  
    }  
    public void setIssurer(String issurer) {  
        this.issurer = issurer;  
    }  
      
    public String getIssurer() {  
        return this.issurer;  
    }  
    public void setCreditLevel(String creditLevel) {  
        this.creditLevel = creditLevel;  
    }  
      
    public String getCreditLevel() {  
        return this.creditLevel;  
    }  
    public void setFixedDuration(String fixedDuration) {  
        this.fixedDuration = fixedDuration;  
    }  
      
    public String getFixedDuration() {  
        return this.fixedDuration;  
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