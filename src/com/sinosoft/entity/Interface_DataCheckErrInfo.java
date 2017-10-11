package com.sinosoft.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Interface_DataCheckErrInfo")
public class Interface_DataCheckErrInfo implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String year;
	@Column
	private String quarter;
	@Column
	private String yearmonth;
	@Column
	private Date datadate;
	@Column
	private String source;
	@Column
	private BigDecimal leftvalue;
	@Column
	private BigDecimal rightvalue;
	@Column
	private char flag;
	@Column
	private String remark;
	@Column
	private Date checktime;
	@Column
	private String errInfo;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getYearmonth() {
		return yearmonth;
	}

	public void setYearmonth(String yearmonth) {
		this.yearmonth = yearmonth;
	}

	public Date getDatadate() {
		return datadate;
	}

	public void setDatadate(Date datadate) {
		this.datadate = datadate;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public BigDecimal getLeftvalue() {
		return leftvalue;
	}

	public void setLeftvalue(BigDecimal leftvalue) {
		this.leftvalue = leftvalue;
	}

	public BigDecimal getRightvalue() {
		return rightvalue;
	}

	public void setRightvalue(BigDecimal rightvalue) {
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

	public String getErrInfo() {
		return errInfo;
	}

	public void setErrInfo(String errInfo) {
		this.errInfo = errInfo;
	}
	
	
}
