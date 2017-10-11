package com.sinosoft.entity;  

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="cal_Kdefine")
public class Kdefine {  
	/**  
     */ 
	@Id
	@SequenceGenerator(name = "generator",sequenceName="k_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "generator")
	private int id;

    /**  
     */ 
	@Column
    private String kcode;  
	
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
    private String kdefine;  
    /**  
     */ 
    @Column
    private String kdefineVar;  
    /**  
     */ 
    @Column
    private String kdefineFrom;  
    /**  
     */ 
    @Column
    private String kdefineTo;  
    /**  
     */ 
    @Column
    private String kvalue;  
    /**  
     */ 
    @Column
    private String kvalueRule;  
    /**  
     */ 
    @Column
    private String remark;  
 
    public void setKcode(String kcode) {  
        this.kcode = kcode;  
    }  
      
    public String getKcode() {  
        return this.kcode;  
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
    public void setKdefine(String kdefine) {  
        this.kdefine = kdefine;  
    }  
      
    public String getKdefine() {  
        return this.kdefine;  
    }  
    public void setKdefineFrom(String kdefineFrom) {  
        this.kdefineFrom = kdefineFrom;  
    }  
      
    public String getKdefineFrom() {  
        return this.kdefineFrom;  
    }  
    public void setKdefineTo(String kdefineTo) {  
        this.kdefineTo = kdefineTo;  
    }  
      
    public String getKdefineTo() {  
        return this.kdefineTo;  
    }  
    public void setKvalue(String kvalue) {  
        this.kvalue = kvalue;  
    }  
      
    public String getKvalue() {  
        return this.kvalue;  
    }


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKvalueRule() {
		return kvalueRule;
	}

	public void setKvalueRule(String kvalueRule) {
		this.kvalueRule = kvalueRule;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getKdefineVar() {
		return kdefineVar;
	}

	public void setKdefineVar(String kdefineVar) {
		this.kdefineVar = kdefineVar;
	}
 
}