package com.sinosoft.zcfz.dto.reportform;

public class ReportCreateInfoDTO {
    private String reportType;
    private String year;
    private String quarter;
    private String fileType;
    private String reportId;
    private String isAllFlag; //1是
    private String company;
    private String reportCateGory;//0 公司偿付能力报告和风险流动性报告	1 风险评估报告
	public String getReportType() {
		return reportType;
	}
	public void setReportType(String reportType) {
		this.reportType = reportType;
	}

	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
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
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public String getIsAllFlag() {
		return isAllFlag;
	}
	public void setIsAllFlag(String isAllFlag) {
		this.isAllFlag = isAllFlag;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getReportCateGory() {
		return reportCateGory;
	}
	public void setReportCateGory(String reportCateGory) {
		this.reportCateGory = reportCateGory;
	}
	
}
