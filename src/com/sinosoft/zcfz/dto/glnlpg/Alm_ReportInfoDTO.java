package com.sinosoft.zcfz.dto.glnlpg;

public class Alm_ReportInfoDTO {
	
    private String ReportId;

    private String year;

    private String quarter;

    private String month;

    private Integer Flag;

    private Integer oper_id;

    private String oper_time;
    
    private String backType;
	
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
	public Integer getFlag() {
		return Flag;
	}
	public void setFlag(Integer flag) {
		Flag = flag;
	}
	public Integer getOper_id() {
		return oper_id;
	}
	public void setOper_id(Integer oper_id) {
		this.oper_id = oper_id;
	}
	public String getOper_time() {
		return oper_time;
	}
	public void setOper_time(String oper_time) {
		this.oper_time = oper_time;
	}
	public String getReportId() {
		return ReportId;
	}
	public void setReportId(String reportId) {
		ReportId = reportId;
	}
	public String getBackType() {
		return backType;
	}
	public void setBackType(String backType) {
		this.backType = backType;
	}
}