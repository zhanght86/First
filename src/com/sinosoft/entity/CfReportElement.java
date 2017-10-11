package com.sinosoft.entity;  

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class CfReportElement {  
      
    /**  
     */ 
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
    @Column
    private Long id;  
    /**  
     */ 
    @Column
    private String cfReportType;  
    /**  
     */ 
    @Column
    private String portItemCode;  
    /**  
     */ 
    @Column
    private String elementCode;  
    /**  
     */ 
    @Column
    private String contextCode;  
    /**  
     */ 
    @Column
    private String unitCode;  
    /**  
     */ 
    @Column
    private String decimals;  
    /**  
     */ 
    @Column
    private String remark;
    
    @Column
    private String startOrEndType;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCfReportType() {
		return cfReportType;
	}
	public void setCfReportType(String cfReportType) {
		this.cfReportType = cfReportType;
	}
	public String getPortItemCode() {
		return portItemCode;
	}
	public void setPortItemCode(String portItemCode) {
		this.portItemCode = portItemCode;
	}
	public String getElementCode() {
		return elementCode;
	}
	public void setElementCode(String elementCode) {
		this.elementCode = elementCode;
	}
	public String getContextCode() {
		return contextCode;
	}
	public void setContextCode(String contextCode) {
		this.contextCode = contextCode;
	}
	public String getUnitCode() {
		return unitCode;
	}
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
	public String getDecimals() {
		return decimals;
	}
	public void setDecimals(String decimals) {
		this.decimals = decimals;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getStartOrEndType() {
		return startOrEndType;
	}
	public void setStartOrEndType(String startOrEndType) {
		this.startOrEndType = startOrEndType;
	}  
}