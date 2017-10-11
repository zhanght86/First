package com.sinosoft.entity;  

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="cal_RhoMatrixDefine")
public class RhoMatrixDefine {  
      
    /**  
     */ 
    @Id
    private String rhomatrixCode;  
    /**  
     */ 
    @Column
    private String rhomatrixName;  
    /**  
     */ 
    @Column
    private String reportitemcode;  
    /**  
     */ 
    @Column
    private int columnLine;  
    /**  
     */ 
    @Column
    private String rhomatrixValue;  
    /**  
     */ 
    @Column
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
    
    public int getColumnLine() {
		return columnLine;
	}

	public void setColumnLine(int columnLine) {
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