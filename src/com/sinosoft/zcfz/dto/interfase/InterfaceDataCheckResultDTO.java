package com.sinosoft.zcfz.dto.interfase;

import java.util.Date;

public class InterfaceDataCheckResultDTO {
	private String year;
	private String quarter;
	private String yearmonth;
	private String datadate;
	private String source;
	private double leftvalue;
	private double rightvalue;
	private char flag;
	private String remark;
	private Date checktime;
	private String errinfo;
	
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
	public String getYearmonth() {
		return yearmonth;
	}
	public void setYearmonth(String yearmonth) {
		this.yearmonth = yearmonth;
	}
	public String getDatadate() {
		return datadate;
	}
	public void setDatadate(String datadate) {
		this.datadate = datadate;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public double getLeftvalue() {
		return leftvalue;
	}
	public void setLeftvalue(double leftvalue) {
		this.leftvalue = leftvalue;
	}
	public double getRightvalue() {
		return rightvalue;
	}
	public void setRightvalue(double rightvalue) {
		this.rightvalue = rightvalue;
	}
	public char getFlag() {
		return flag;
	}
	public void setFlag(char flag) {
		this.flag = flag;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getChecktime() {
		return checktime;
	}
	public void setChecktime(Date checktime) {
		this.checktime = checktime;
	}
	public String getErrinfo() {
		return errinfo;
	}
	public void setErrinfo(String errinfo) {
		this.errinfo = errinfo;
	}
	
	
}
