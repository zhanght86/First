package com.sinosoft.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.sinosoft.entity.BranchInfo;

public class BranchInfoDTO {
	private String comCode;
	
    private String comName;
	
    private String comType;
    private String uperComCode;
	
    private String endFlag;
	
    private String inAccount;
	
    private String outAccount;
	
    private String payee;
	
    private String currItemCode;
	
    private String currItemName;
	
    private BigDecimal budgetPercent;
	
    private BigDecimal budgetDiffer;
	
    private String useFlag;
	
    private String remark;
	
    private String uperDeptCode;
	
    private String moneyOut;
	
    private String deptTypeCode;
	
    private String modifier;
	
    private Date modificationDate;
	
    private String menuCode;
	
    private String comRank;
    
    private String licensenumber;
    
    private String comRankName;
    
    public String getComRankName() {
		return comRankName;
	}

	public void setComRankName(String comRankName) {
		this.comRankName = comRankName;
	}

	public String getLicensenumber() {
		return licensenumber;
	}

	public void setLicensenumber(String licensenumber) {
		this.licensenumber = licensenumber;
	}

	public String getComCode() {
		return comCode;
	}

	public void setComCode(String comCode) {
		this.comCode = comCode;
	}

	public String getComName() {
		return comName;
	}

	public void setComName(String comName) {
		this.comName = comName;
	}

	public String getComType() {
		return comType;
	}

	public void setComType(String comType) {
		this.comType = comType;
	}

	public String getUperComCode() {
		return uperComCode;
	}

	public void setUperComCode(String uperComCode) {
		this.uperComCode = uperComCode;
	}

	public String getEndFlag() {
		return endFlag;
	}

	public void setEndFlag(String endFlag) {
		this.endFlag = endFlag;
	}

	public String getInAccount() {
		return inAccount;
	}

	public void setInAccount(String inAccount) {
		this.inAccount = inAccount;
	}

	public String getOutAccount() {
		return outAccount;
	}

	public void setOutAccount(String outAccount) {
		this.outAccount = outAccount;
	}

	public String getPayee() {
		return payee;
	}

	public void setPayee(String payee) {
		this.payee = payee;
	}

	public String getCurrItemCode() {
		return currItemCode;
	}

	public void setCurrItemCode(String currItemCode) {
		this.currItemCode = currItemCode;
	}

	public String getCurrItemName() {
		return currItemName;
	}

	public void setCurrItemName(String currItemName) {
		this.currItemName = currItemName;
	}

	public BigDecimal getBudgetPercent() {
		return budgetPercent;
	}

	public void setBudgetPercent(BigDecimal budgetPercent) {
		this.budgetPercent = budgetPercent;
	}

	public BigDecimal getBudgetDiffer() {
		return budgetDiffer;
	}

	public void setBudgetDiffer(BigDecimal budgetDiffer) {
		this.budgetDiffer = budgetDiffer;
	}

	public String getUseFlag() {
		return useFlag;
	}

	public void setUseFlag(String useFlag) {
		this.useFlag = useFlag;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getUperDeptCode() {
		return uperDeptCode;
	}

	public void setUperDeptCode(String uperDeptCode) {
		this.uperDeptCode = uperDeptCode;
	}

	public String getMoneyOut() {
		return moneyOut;
	}

	public void setMoneyOut(String moneyOut) {
		this.moneyOut = moneyOut;
	}

	public String getDeptTypeCode() {
		return deptTypeCode;
	}

	public void setDeptTypeCode(String deptTypeCode) {
		this.deptTypeCode = deptTypeCode;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public Date getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}

	public String getMenuCode() {
		return menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	public String getComRank() {
		return comRank;
	}

	public void setComRank(String comRank) {
		this.comRank = comRank;
	}
    
}
