package com.sinosoft.entity;  

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ElementInfo {  
      
    /**  
     */ 
    @Id
    private String elementCode;  
    /**  
     */ 
    @Column
    private String elementName;  
    /**  
     */ 
    @Column
    private String type;  
    /**  
     */ 
    @Column
    private String periodType;  
    /**  
     */ 
    @Column
    private String balance;  
    /**  
     */ 
    @Column
    private String subStitutionGroup;  
    /**  
     */ 
    @Column
    private String abstractType;  
    
    
    @Column
    private String labelzh;  
    /**  
     */ 
    @Column
    private String terseLabelzh;  
    /**  
     */ 
    @Column
    private String labelen;  
    /**  
     */ 
    @Column
    private String terseLabelen;
	public String getElementCode() {
		return elementCode;
	}
	public void setElementCode(String elementCode) {
		this.elementCode = elementCode;
	}
	public String getElementName() {
		return elementName;
	}
	public void setElementName(String elementName) {
		this.elementName = elementName;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPeriodType() {
		return periodType;
	}
	public void setPeriodType(String periodType) {
		this.periodType = periodType;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getSubStitutionGroup() {
		return subStitutionGroup;
	}
	public void setSubStitutionGroup(String subStitutionGroup) {
		this.subStitutionGroup = subStitutionGroup;
	}
	public String getAbstractType() {
		return abstractType;
	}
	public void setAbstractType(String abstractType) {
		this.abstractType = abstractType;
	}
	public String getLabelzh() {
		return labelzh;
	}
	public void setLabelzh(String labelzh) {
		this.labelzh = labelzh;
	}
	public String getTerseLabelzh() {
		return terseLabelzh;
	}
	public void setTerseLabelzh(String terseLabelzh) {
		this.terseLabelzh = terseLabelzh;
	}
	public String getLabelen() {
		return labelen;
	}
	public void setLabelen(String labelen) {
		this.labelen = labelen;
	}
	public String getTerseLabelen() {
		return terseLabelen;
	}
	public void setTerseLabelen(String terseLabelen) {
		this.terseLabelen = terseLabelen;
	}
}