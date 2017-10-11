package com.sinosoft.entity;  

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ContextTable {  
      
    /**  
     */ 
    @Id
    private String contextCode;  
    /**  
     */ 
    @Column
    private String contextName;  
    /**  
     */ 
    @Column
    private String axis;  
    /**  
     */ 
    @Column
    private String member;  
    /**  
     */ 
    @Column
    private String lineMember;  
    /**  
     */ 
    @Column
    private String remark;
	public String getContextCode() {
		return contextCode;
	}
	public void setContextCode(String contextCode) {
		this.contextCode = contextCode;
	}
	public String getContextName() {
		return contextName;
	}
	public void setContextName(String contextName) {
		this.contextName = contextName;
	}
	public String getAxis() {
		return axis;
	}
	public void setAxis(String axis) {
		this.axis = axis;
	}
	public String getMember() {
		return member;
	}
	public void setMember(String member) {
		this.member = member;
	}
	public String getLineMember() {
		return lineMember;
	}
	public void setLineMember(String lineMember) {
		this.lineMember = lineMember;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}  
 
    
 
}