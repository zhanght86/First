package com.sinosoft.dto;

import com.sinosoft.entity.Contract;

public class ContractDTO {
	private String contractId;
	private String contractName;
	private String applyNo;
	private String remark;
	public String getContractId() {
		return contractId;
	}
	public void setContractId(String contractId) {
		this.contractId = contractId;
	}
	public String getContractName() {
		return contractName;
	}
	public void setContractName(String contractName) {
		this.contractName = contractName;
	}
	public String getApplyNo() {
		return applyNo;
	}
	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	/**
	 * 将DTO转换成实体
	 * @param ctd
	 * @return
	 */
	public static Contract toEntity(ContractDTO ctd){
		Contract ct = new Contract();
		ct.setApplyNo(ctd.getApplyNo());
		ct.setContractId(ctd.getContractId());
		ct.setContractName(ctd.getContractName());
		ct.setRemark(ctd.getRemark());
		return ct;
	}
}
