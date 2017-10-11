package com.sinosoft.zcfz.dto.reportformcompute;  


public class RhoMatrixDefineDTO {  
      
    /**  
     */ 
    private String rhomatrixCode;  
    /**  
     */ 
    private String rhomatrixName;  
    /**  
     */ 
    private String reportitemcode;  
    /**  
     */ 
    private Integer columnLine;  
    /**  
     */ 
    private String rhomatrixValue;  
    /**  
     */ 
    private String remark;  
 
    public void setRhomatrixCode(String rhomatrixCode) {  
        this.rhomatrixCode = rhomatrixCode;  
    }  
      
    public String getRhomatrixCode() {  
        return this.rhomatrixCode;  
    }  
    public void setRhomatrixName(String rhomatrixName) {  
        this.rhomatrixName = rhomatrixName;  
    }  
      
    public String getRhomatrixName() {  
        return this.rhomatrixName;  
    }  
    public void setReportitemcode(String reportitemcode) {  
        this.reportitemcode = reportitemcode;  
    }  
      
    public String getReportitemcode() {  
        return this.reportitemcode;  
    }  

	public Integer getColumnLine() {
		return columnLine;
	}

	public void setColumnLine(Integer columnLine) {
		this.columnLine = columnLine;
	}

	public void setRhomatrixValue(String rhomatrixValue) {  
        this.rhomatrixValue = rhomatrixValue;  
    }  
      
    public String getRhomatrixValue() {  
        return this.rhomatrixValue;  
    }  
    public void setRemark(String remark) {  
        this.remark = remark;  
    }  
      
    public String getRemark() {  
        return this.remark;  
    }  
 
}