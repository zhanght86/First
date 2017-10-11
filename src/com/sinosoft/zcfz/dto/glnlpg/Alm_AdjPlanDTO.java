package com.sinosoft.zcfz.dto.glnlpg;

public class Alm_AdjPlanDTO {
	
	private Long id;
	
	private String reportId;

	private String itemCode;

	private String adjPlan;

	private String adjStatus;
	
	private String adjStatusName;

	private String deptNo;

	private String adjUser;

	private String adjTime;
	
	private String adjOpinion;
	
    private String instanceId;
    
    private String taskId;
	
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getDeptNo() {
		return deptNo;
	}
	public void setDeptNo(String deptNo) {
		this.deptNo = deptNo;
	}
	public String getAdjPlan() {
		return adjPlan;
	}
	public void setAdjPlan(String adjPlan) {
		this.adjPlan = adjPlan;
	}
	public String getAdjStatus() {
		return adjStatus;
	}
	public void setAdjStatus(String adjStatus) {
		this.adjStatus = adjStatus;
	}
	public String getAdjUser() {
		return adjUser;
	}
	public void setAdjUser(String adjUser) {
		this.adjUser = adjUser;
	}
	public String getAdjTime() {
		return adjTime;
	}
	public void setAdjTime(String adjTime) {
		this.adjTime = adjTime;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAdjOpinion() {
		return adjOpinion;
	}
	public void setAdjOpinion(String adjOpinion) {
		this.adjOpinion = adjOpinion;
	}
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getAdjStatusName() {
		return adjStatusName;
	}
	public void setAdjStatusName(String adjStatusName) {
		this.adjStatusName = adjStatusName;
	}
}