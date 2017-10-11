package com.sinosoft.entity;  

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Embeddable
public class InterTabFinaInfoId implements java.io.Serializable {  
      
	@Column
    private String reportId;      
    @Column
    private String itemCode;  
    @Column
    private String tabName;
    @Column
    private String itemName;  
    
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
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
	public String getTabName() {
		return tabName;
	}
	public void setTabName(String tabName) {
		this.tabName = tabName;
	} 
	public InterTabFinaInfoId(String reportId, String itemCode,String tabName,String itemName) {
		super();
		this.reportId = reportId;
		this.itemCode = itemCode;	
		this.tabName = tabName;
		this.itemName = itemName;
	}
	public InterTabFinaInfoId() {
		super();
	}
 
}