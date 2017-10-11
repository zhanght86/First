package com.sinosoft.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="lacal_CfReportRelation")
public class LaCalCfReportRelation  {  
    
  /**  
   */ 
	@Id
	private String portItemCode;  
  /**  
   */ 
  @Column
  private String portItemType;  
  /**  
   */ 
  @Column
  private String portItemHome;  
  /**  
   */ 
  @Column
  private String interfaceType;  
  /**  
   */ 
  @Column
  private Integer interfacePriority;  
  /**  
   */ 
  @Column
  private String interfaceGetData;  
  
  @Column
  private String interfaceGetDataVar;  
  /**  
   */ 
  @Column
  private String calculateType;  
  /**  
   */ 
  @Column
  private String calculateRule;  
  /**  
   */ 
  @Column
  private String varFlag;  
  /**  
   */ 
  @Column
  private String computeVar;  
  /**  
   */ 
  @Column
  private String computeVarRule;  
  /**  
   */ 
  @Column
  private String remark;  

  public void setPortItemCode(String portItemCode) {  
      this.portItemCode = portItemCode;  
  }  
    
  public String getPortItemCode() {  
      return this.portItemCode;  
  }  
  public void setPortItemType(String portItemType) {  
      this.portItemType = portItemType;  
  }  
    
  public String getPortItemType() {  
      return this.portItemType;  
  }  
  public void setInterfaceType(String interfaceType) {  
      this.interfaceType = interfaceType;  
  }  
    
  public String getInterfaceType() {  
      return this.interfaceType;  
  }  

	public Integer getInterfacePriority() {
		return interfacePriority;
	}

	public void setInterfacePriority(Integer interfacePriority) {
		this.interfacePriority = interfacePriority;
	}

	public void setInterfaceGetData(String interfaceGetData) {  
      this.interfaceGetData = interfaceGetData;  
  }  
    
  public String getInterfaceGetData() {  
      return this.interfaceGetData;  
  }  
  public void setCalculateType(String calculateType) {  
      this.calculateType = calculateType;  
  }  
    
  public String getCalculateType() {  
      return this.calculateType;  
  }  
  public void setCalculateRule(String calculateRule) {  
      this.calculateRule = calculateRule;  
  }  
    
  public String getCalculateRule() {  
      return this.calculateRule;  
  }  
  public void setVarFlag(String varFlag) {  
      this.varFlag = varFlag;  
  }  
    
  public String getVarFlag() {  
      return this.varFlag;  
  }  
  public void setComputeVar(String computeVar) {  
      this.computeVar = computeVar;  
  }  
    
  public String getComputeVar() {  
      return this.computeVar;  
  }  
  public void setComputeVarRule(String computeVarRule) {  
      this.computeVarRule = computeVarRule;  
  }  
    
  public String getComputeVarRule() {  
      return this.computeVarRule;  
  }  
  public void setRemark(String remark) {  
      this.remark = remark;  
  }  
    
  public String getRemark() {  
      return this.remark;  
  }

	public String getInterfaceGetDataVar() {
		return interfaceGetDataVar;
	}

	public void setInterfaceGetDataVar(String interfaceGetDataVar) {
		this.interfaceGetDataVar = interfaceGetDataVar;
	}

	public String getPortItemHome() {
		return portItemHome;
	}

	public void setPortItemHome(String portItemHome) {
		this.portItemHome = portItemHome;
	}  

}
