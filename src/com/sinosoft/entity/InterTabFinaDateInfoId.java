package com.sinosoft.entity;  

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class InterTabFinaDateInfoId implements java.io.Serializable{  
      
    
    @Column
    private String reportId;      
    @Column
    private String accountType; 
    @Column
    private String properCode;  
    
    public void setReportId(String reportId) {  
        this.reportId = reportId;  
    }  
      
    public String getReportId() {  
        return this.reportId;  
    }  
    public void setAccountType(String accountType) {  
        this.accountType = accountType;  
    }  
      
    public String getAccountType() {  
        return this.accountType;  
    }  
    public void setProperCode(String properCode) {  
        this.properCode = properCode;  
    }  
      
    public String getProperCode() {  
        return this.properCode;  
    } 
    public InterTabFinaDateInfoId() {
		super();
	}
    public InterTabFinaDateInfoId(String reportId,String accountType,String properCode) {
  		super();
  		this.reportId = reportId;
  		this.accountType = accountType;
  		this.properCode = properCode;
  	}
 
}