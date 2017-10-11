package com.sinosoft.entity;  

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="UnitTable")
public class Unit {  
      
    /**  
     */ 
    @Id
    private String unitCode;  
    /**  
     */ 
    @Column
    private String unitName;  
    /**  
     */ 
    @Column
    private String numerator;  
    /**  
     */ 
    @Column
    private String denominator;
	public String getUnitCode() {
		return unitCode;
	}
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public String getNumerator() {
		return numerator;
	}
	public void setNumerator(String numerator) {
		this.numerator = numerator;
	}
	public String getDenominator() {
		return denominator;
	}
	public void setDenominator(String denominator) {
		this.denominator = denominator;
	}  
    
	
}