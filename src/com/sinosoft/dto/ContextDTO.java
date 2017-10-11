package com.sinosoft.dto;  


public class ContextDTO {  
      
    private String contextCode;  
    private String contextName;  
    private String contextType; //0 代表非维度 1代表明确维度 2代表类型化维度
    private String periodType; //0代表期点 1代表期间
    private String startPeriod;
    private String endPeriod;
    private String axis;  
    private String member;  
    private String lineMember;  
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
	public String getPeriodType() {
		return periodType;
	}
	public void setPeriodType(String periodType) {
		this.periodType = periodType;
	}
	public String getStartPeriod() {
		return startPeriod;
	}
	public void setStartPeriod(String startPeriod) {
		this.startPeriod = startPeriod;
	}
	public String getEndPeriod() {
		return endPeriod;
	}
	public void setEndPeriod(String endPeriod) {
		this.endPeriod = endPeriod;
	}
	public String getContextType() {
		return contextType;
	}
	public void setContextType(String contextType) {
		this.contextType = contextType;
	}  
	
}