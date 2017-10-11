package com.sinosoft.zcfz.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Alm_ItemDefine implements Serializable  {
	@Id
	private String itemCode;
	@Column
	private String itemNum;
	@Column
	private String itemName;
	@Column
	private String standardScore;
	@Column
	private Double sysIntegrityScore;
	@Column
	private Double sysIntegrityWeight;
	@Column
	private Double folEfficiencyScore;
	@Column
	private Double folEfficiencyWeight;
	@Column
	private Double itemWeight;
	@Column
	private String projectCode;
	@Column
	private String deptNo;
	@Column
	private String deptWeight;
	@Column
	private String companyCode;
	@Column
	private String temp;
	@Column
    private Integer itemType;

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemNum() {
		return itemNum;
	}

	public void setItemNum(String itemNum) {
		this.itemNum = itemNum;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getStandardScore() {
		return standardScore;
	}

	public void setStandardScore(String standardScore) {
		this.standardScore = standardScore;
	}

	public Double getSysIntegrityScore() {
		return sysIntegrityScore;
	}

	public void setSysIntegrityScore(Double sysIntegrityScore) {
		this.sysIntegrityScore = sysIntegrityScore;
	}

	public Double getSysIntegrityWeight() {
		return sysIntegrityWeight;
	}

	public void setSysIntegrityWeight(Double sysIntegrityWeight) {
		this.sysIntegrityWeight = sysIntegrityWeight;
	}

	public Double getFolEfficiencyScore() {
		return folEfficiencyScore;
	}

	public void setFolEfficiencyScore(Double folEfficiencyScore) {
		this.folEfficiencyScore = folEfficiencyScore;
	}

	public Double getFolEfficiencyWeight() {
		return folEfficiencyWeight;
	}

	public void setFolEfficiencyWeight(Double folEfficiencyWeight) {
		this.folEfficiencyWeight = folEfficiencyWeight;
	}

	public Double getItemWeight() {
		return itemWeight;
	}

	public void setItemWeight(Double itemWeight) {
		this.itemWeight = itemWeight;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getDeptNo() {
		return deptNo;
	}

	public void setDeptNo(String deptNo) {
		this.deptNo = deptNo;
	}

	public String getDeptWeight() {
		return deptWeight;
	}

	public void setDeptWeight(String deptWeight) {
		this.deptWeight = deptWeight;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	public Integer getItemType() {
		return itemType;
	}

	public void setItemType(Integer itemType) {
		this.itemType = itemType;
	}
	
}
