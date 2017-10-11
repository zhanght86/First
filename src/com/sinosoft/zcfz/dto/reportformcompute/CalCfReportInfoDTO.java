package com.sinosoft.zcfz.dto.reportformcompute;  


public class CalCfReportInfoDTO {  
      
    /**  
     */ 
    private String reportId;  
    /**  
     */ 
    private String year;  
    /**  
     */ 
    private String quarter;  
    /**  
     */ 
    private String quarterName;  
    /**  
     */ 
    private String reportType;  
    /**  
     */ 
    private String reportTypeName;  
    /**  
     */ 
    private String reportState;  
    /**  
     */ 
    private String reportStateName;  
    /**  
     */ 
    private String handleFlag;  
    /**  
     */ 
    private String remark;  
    
    private String dataDate;
    
    private String reportCateGory;  

    private String reportCateGoryName; 
    
    private String comCode;   
    
    private String comName;      

    
    public String getComName() {
		return comName;
	}

	public void setComName(String comName) {
		this.comName = comName;
	}

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

	public String getReportCateGoryName() {
		return reportCateGoryName;
	}

	public void setReportCateGoryName(String reportCateGoryName) {
		this.reportCateGoryName = reportCateGoryName;
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

	public String getQuarterName() {
		return quarterName;
	}

	public void setQuarterName(String quarterName) {
		this.quarterName = quarterName;
	}

	public String getReportTypeName() {
		return reportTypeName;
	}

	public void setReportTypeName(String reportTypeName) {
		this.reportTypeName = reportTypeName;
	}

	public String getReportStateName() {
		return reportStateName;
	}

	public void setReportStateName(String reportStateName) {
		this.reportStateName = reportStateName;
	}

	public String getDataDate() {
		return dataDate;
	}

	public void setDataDate(String dataDate) {
		this.dataDate = dataDate;
	}  
 
	
}