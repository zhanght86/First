package com.sinosoft.zcfz.entity;

import java.io.Serializable;

import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Alm_AdjPlan implements Serializable{
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id", nullable=false, unique=true)
	private Long id;
	@Column
	private String reportId;
	@Column
	private String itemCode;
	@Column
	private String deptNo;
	@Column
	private String adjPlan;
	@Column
	private String adjStatus;
	@Column
	private String adjUser;
	@Column
	private String adjTime;
	@Column
	private String adjOpinion;
	@Column
    private String instanceId;
	
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public String getAdjOpinion() {
		return adjOpinion;
	}
	public void setAdjOpinion(String adjOpinion) {
		this.adjOpinion = adjOpinion;
	}
}