package com.sinosoft.entity;  

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="HandSet")
public class HandSet implements java.io.Serializable{  
      
    /**  
     */ 
	@Id
	@Column(name = "tableCode", unique = true)
    private String tableCode;  
    /**  
     */ 
    @Column
    private String tableName;  
    /**  
     */ 
    @Column
    private String defendContent;  
    /**  
     */ 
    @Column
    private String defendResult;  
    /**  
     */ 
    @Column
    private String spare1;  
    /**  
     */ 
    @Column
    private String spare2;  
    /**  
     */ 
    @Column
    private String spare3;  
    /**  
     */ 
    @Column
    private String operator;  
    /**  
     */ 
    @Column
    private Date opraDate;  
    /**  
     */ 
    @Column
    private String remark;  
 
    public void setTableCode(String tableCode) {  
        this.tableCode = tableCode;  
    }  
      
    public String getTableCode() {  
        return this.tableCode;  
    }  
    public void setTableName(String tableName) {  
        this.tableName = tableName;  
    }  
      
    public String getTableName() {  
        return this.tableName;  
    }  
    public void setDefendContent(String defendContent) {  
        this.defendContent = defendContent;  
    }  
      
    public String getDefendContent() {  
        return this.defendContent;  
    }  
    public void setDefendResult(String defendResult) {  
        this.defendResult = defendResult;  
    }  
      
    public String getDefendResult() {  
        return this.defendResult;  
    }  
    public void setSpare1(String spare1) {  
        this.spare1 = spare1;  
    }  
      
    public String getSpare1() {  
        return this.spare1;  
    }  
    public void setSpare2(String spare2) {  
        this.spare2 = spare2;  
    }  
      
    public String getSpare2() {  
        return this.spare2;  
    }  
    public void setSpare3(String spare3) {  
        this.spare3 = spare3;  
    }  
      
    public String getSpare3() {  
        return this.spare3;  
    }  
    public void setOperator(String operator) {  
        this.operator = operator;  
    }  
      
    public String getOperator() {  
        return this.operator;  
    }  
    public void setOpraDate(Date opraDate) {  
        this.opraDate = opraDate;  
    }  
      
    public Date getOpraDate() {  
        return this.opraDate;  
    }  
    public void setRemark(String remark) {  
        this.remark = remark;  
    }  
      
    public String getRemark() {  
        return this.remark;  
    }  
 
}