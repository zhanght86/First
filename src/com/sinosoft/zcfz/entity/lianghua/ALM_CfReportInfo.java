package com.sinosoft.zcfz.entity.lianghua;  

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ALM_CfReportInfo")
public class ALM_CfReportInfo {  
      
    /**  
     */ 
    @Id
    private String reportId;  
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
    private String reportType;  
    /**  
     */ 
    @Column
    private String reportState;  
    /**  
     */ 
    @Column
    private String handleFlag;  
    /**  
     */ 
    @Column
    private String remark;
    
    @Column
    private String dataDate;
    @Column
    private String reportCateGory;  
    @Column
    private String comCode; 
    
    
    public String getComCode() {
		return comCode;
	}

	public void setComCode(String comCode) {
		this.comCode = comCode;
	}

	public String getReportCateGory() {
		return reportCateGory;
	}

	public void setReportCateGory(String reportCateGory) {
		this.reportCateGory = reportCateGory;
	}

	public String getDataDate() {
		return this.dataDate;
	}

	public void setDataDate(String datadate) {
		this.dataDate = datadate;
	}

	public void setReportId(String reportId) {  
        this.reportId = reportId;  
    }  
      
    public String getReportId() {  
        return this.reportId;  
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
    public void setReportType(String reportType) {  
        this.reportType = reportType;  
    }  
      
    public String getReportType() {  
        return this.reportType;  
    }  
    public void setReportState(String reportState) {  
        this.reportState = reportState;  
    }  
      
    public String getReportState() {  
        return this.reportState;  
    }  
    public void setHandleFlag(String handleFlag) {  
        this.handleFlag = handleFlag;  
    }  
      
    public String getHandleFlag() {  
        return this.handleFlag;  
    }  
    public void setRemark(String remark) {  
        this.remark = remark;  
    }  
      
    public String getRemark() {  
        return this.remark;  
    }  
 
    
}