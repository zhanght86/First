package com.sinosoft.entity;  

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class InterTabFinaEnValueId implements java.io.Serializable{  
      
	
    @Column
    private String reportId;
    @Column
    private String accountType; 
    @Column
    private String itemCode;
    @Column
    private String temp2; 
    
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	
	public String getTemp2() {
		return temp2;
	}
	public void setTemp2(String temp2) {
		this.temp2 = temp2;
	}
	public InterTabFinaEnValueId(){
		super();
	}
	public InterTabFinaEnValueId(String reportId,String itemCode,String accountType,String temp2){
		super();
		this.reportId = reportId;
		this.itemCode = itemCode;
		this.accountType = accountType;
		this.temp2 = temp2;
	} 
 
}