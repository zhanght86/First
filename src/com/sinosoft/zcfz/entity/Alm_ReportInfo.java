package com.sinosoft.zcfz.entity;

import java.io.Serializable;

import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Alm_ReportInfo implements Serializable{
	
	@Id
	private String reportId;
	@Column
    private String year;
	@Column
    private String quarter;
	@Column
    private String month;
	@Column
    private String Flag;
	@Column
    private Long oper_id;
	@Column
    private String oper_time;
	@Column
    private String backType;
	
	@Column
    private String instanceId;
	@Column
    private String sevstatus;
	
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	public String getSevstatus() {
		return sevstatus;
	}
	public void setSevstatus(String sevstatus) {
		this.sevstatus = sevstatus;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getQuarter() {
		return quarter;
	}
	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getOper_time() {
		return oper_time;
	}
	public void setOper_time(String oper_time) {
		this.oper_time = oper_time;
	}
	public String getFlag() {
		return Flag;
	}
	public void setFlag(String flag) {
		Flag = flag;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public String getBackType() {
		return backType;
	}
	public void setBackType(String backType) {
		this.backType = backType;
	}
	public Long getOper_id() {
		return oper_id;
	}
	public void setOper_id(Long oper_id) {
		this.oper_id = oper_id;
	}
}