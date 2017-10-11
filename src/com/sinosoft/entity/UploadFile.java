package com.sinosoft.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
//@IdClass(UploadFileId.class)
@Table(name="upload")
public class UploadFile {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int uploadNo;
	@Column
    private String reportcode;
	@Column
    private String reporttype;
	@Column
    private String reportname;
	@Column
    private String yearmonth;
	@Column
    private String quarter;
	@Column
    private String fileName;
	@Column
    private String filePath;
	@Column
    private String remark;
	@Column
    private String operator;
	@Column
    private String operatdate;

	public int getUploadNo() {
		return uploadNo;
	}
	public void setUploadNo(int uploadNo) {
		this.uploadNo = uploadNo;
	}
	public String getReportcode() {
		return reportcode;
	}
	public void setReportcode(String reportcode) {
		this.reportcode = reportcode;
	}
	public String getReporttype() {
		return reporttype;
	}
	public void setReporttype(String reporttype) {
		this.reporttype = reporttype;
	}
	public String getReportname() {
		return reportname;
	}
	public void setReportname(String reportname) {
		this.reportname = reportname;
	}
	public String getYearmonth() {
		return yearmonth;
	}
	public void setYearmonth(String yearmonth) {
		this.yearmonth = yearmonth;
	}
	public String getQuarter() {
		return quarter;
	}
	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getOperatdate() {
		return operatdate;
	}
	public void setOperatdate(String operatdate) {
		this.operatdate = operatdate;
	}
	
}
