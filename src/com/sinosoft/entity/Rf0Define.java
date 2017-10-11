package com.sinosoft.entity;  

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="cal_rf0Define")
public class Rf0Define {  
    @Id
    @SequenceGenerator(name = "generator",sequenceName="RF0_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
	private int id;
    /**  
     */ 
    @Column
    private String rf0Code;  
    /**  
     */ 
    @Column
    private String risktype1Code;  
    /**  
     */ 
    @Column
    private String risktype1Name;  
    /**  
     */ 
    @Column
    private String risktype2Code;  
    /**  
     */ 
    @Column
    private String risktype2Name;  
    /**  
     */ 
    @Column
    private String risktype3Code;  
    /**  
     */ 
    @Column
    private String risktype3Name;  
    /**  
     */ 
    @Column
    private String detailCode;  
    /**  
     */ 
    @Column
    private String detailDescribe;  
    /**  
     */ 
    @Column
    private String rf0Define;  
    /**  
     */ 
    @Column
    private String rf0DefineVar;  
    /**  
     */ 
    @Column
    private String rf0DefineFrom;  
    /**  
     */ 
    @Column
    private String rf0DefineTo;  
    /**  
     */ 
    @Column
    private String rf0ValueRule;  
    /**  
     */ 
    @Column
    private String rf0Value;  
    /**  
     */ 
    @Column
    private String remark;  
 
    public void setRf0Code(String rf0Code) {  
        this.rf0Code = rf0Code;  
    }  
      
    public String getRf0Code() {  
        return this.rf0Code;  
    }  
    public void setRisktype1Code(String risktype1Code) {  
        this.risktype1Code = risktype1Code;  
    }  
      
    public String getRisktype1Code() {  
        return this.risktype1Code;  
    }  
    public void setRisktype1Name(String risktype1Name) {  
        this.risktype1Name = risktype1Name;  
    }  
      
    public String getRisktype1Name() {  
        return this.risktype1Name;  
    }  
    public void setRisktype2Code(String risktype2Code) {  
        this.risktype2Code = risktype2Code;  
    }  
      
    public String getRisktype2Code() {  
        return this.risktype2Code;  
    }  
    public void setRisktype2Name(String risktype2Name) {  
        this.risktype2Name = risktype2Name;  
    }  
      
    public String getRisktype2Name() {  
        return this.risktype2Name;  
    }  
    public void setRisktype3Code(String risktype3Code) {  
        this.risktype3Code = risktype3Code;  
    }  
      
    public String getRisktype3Code() {  
        return this.risktype3Code;  
    }  
    public void setRisktype3Name(String risktype3Name) {  
        this.risktype3Name = risktype3Name;  
    }  
      
    public String getRisktype3Name() {  
        return this.risktype3Name;  
    }  
    public void setDetailCode(String detailCode) {  
        this.detailCode = detailCode;  
    }  
      
    public String getDetailCode() {  
        return this.detailCode;  
    }  
    public void setDetailDescribe(String detailDescribe) {  
        this.detailDescribe = detailDescribe;  
    }  
      
    public String getDetailDescribe() {  
        return this.detailDescribe;  
    }  
    public void setRf0Define(String rf0Define) {  
        this.rf0Define = rf0Define;  
    }  
      
    public String getRf0Define() {  
        return this.rf0Define;  
    }  
    public void setRf0DefineFrom(String rf0DefineFrom) {  
        this.rf0DefineFrom = rf0DefineFrom;  
    }  
      
    public String getRf0DefineFrom() {  
        return this.rf0DefineFrom;  
    }  
    public void setRf0DefineTo(String rf0DefineTo) {  
        this.rf0DefineTo = rf0DefineTo;  
    }  
      
    public String getRf0DefineTo() {  
        return this.rf0DefineTo;  
    }  
    public void setRf0Value(String rf0Value) {  
        this.rf0Value = rf0Value;  
    }  
      
    public String getRf0Value() {  
        return this.rf0Value;  
    }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRf0ValueRule() {
		return rf0ValueRule;
	}

	public void setRf0ValueRule(String rf0ValueRule) {
		this.rf0ValueRule = rf0ValueRule;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRf0DefineVar() {
		return rf0DefineVar;
	}

	public void setRf0DefineVar(String rf0DefineVar) {
		this.rf0DefineVar = rf0DefineVar;
	}  
 
}