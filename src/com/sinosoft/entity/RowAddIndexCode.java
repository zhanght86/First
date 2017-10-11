package com.sinosoft.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table
public class RowAddIndexCode {  //  这个实体类对应的是行可扩展数据表  CFREPORTROWADDDATA
		 @Id
		 @GeneratedValue(strategy = GenerationType.AUTO)
		 @Column(name = "reportitemcode",  nullable = false)
	     private   String     reportitemcode;
		 @Column(nullable=false)
		 private   String     tablecode;               
		 @Column(nullable=false)
		 private   String     colcode;                  
		 @Column(nullable=false)
		 private   String     comcode;                
		 @Column(nullable=false)
		 private   String     rowno;                   
		 @Column(nullable=false)
		 private   String     coltype;                 
		 @Column
		 private   double     numvalue;               
		 @Column
		 private   double     wanvalue;                
		 @Column
		 private   String     textvalue; 
		 @Column
		 private   String     destext;   
		 @Column
		 private   String     reporttype;
		 @Column(nullable=false)
		 private   String     yearmonth;               
		 @Column(nullable=false)
		 private   String     quarter;                  
		 @Column(nullable=false)
		 private   String     reportrate;               
		 @Column
		 private   String     source;    
		 @Column(nullable=false)
		 private   String     operator2;                
		 @Column(nullable=false)
		 private   String     operdate2;               
		public String getReportitemcode() {
			return reportitemcode;
		}
		public void setReportitemcode(String reportitemcode) {
			this.reportitemcode = reportitemcode;
		}
		public String getTablecode() {
			return tablecode;
		}
		public void setTablecode(String tablecode) {
			this.tablecode = tablecode;
		}
		public String getColcode() {
			return colcode;
		}
		public void setColcode(String colcode) {
			this.colcode = colcode;
		}
		public String getComcode() {
			return comcode;
		}
		public void setComcode(String comcode) {
			this.comcode = comcode;
		}
		public String getRowno() {
			return rowno;
		}
		public void setRowno(String rowno) {
			this.rowno = rowno;
		}
		public String getColtype() {
			return coltype;
		}
		public void setColtype(String coltype) {
			this.coltype = coltype;
		}
		public double getNumvalue() {
			return numvalue;
		}
		public void setNumvalue(double numvalue) {
			this.numvalue = numvalue;
		}
		public double getWanvalue() {
			return wanvalue;
		}
		public void setWanvalue(double wanvalue) {
			this.wanvalue = wanvalue;
		}
		public String getTextvalue() {
			return textvalue;
		}
		public void setTextvalue(String textvalue) {
			this.textvalue = textvalue;
		}
		public String getDestext() {
			return destext;
		}
		public void setDestext(String destext) {
			this.destext = destext;
		}
		public String getReporttype() {
			return reporttype;
		}
		public void setReporttype(String reporttype) {
			this.reporttype = reporttype;
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
		public String getReportrate() {
			return reportrate;
		}
		public void setReportrate(String reportrate) {
			this.reportrate = reportrate;
		}
		public String getSource() {
			return source;
		}
		public void setSource(String source) {
			this.source = source;
		}
		public String getOperator2() {
			return operator2;
		}
		public void setOperator2(String operator2) {
			this.operator2 = operator2;
		}
		public String getOperdate2() {
			return operdate2;
		}
		public void setOperdate2(String operdate2) {
			this.operdate2 = operdate2;
		}
}
