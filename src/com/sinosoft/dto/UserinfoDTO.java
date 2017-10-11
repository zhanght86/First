package com.sinosoft.dto;

import com.sinosoft.entity.UserInfo;

public class UserinfoDTO {
	
	private String id;
	private String userCode;
	private String userCodeS;
	private String userName;
	private String bankCode;
	private String bankInfo;
	private String comCode;
	private String deptCode;
	private String comCodeName;
	private String deptCodeName;
	private String comName;
	private String deptName;
	private String email;
	private String idCardNo;
	private String password;
	private String phone;
	private String roleId;
	private String branchs;
	private String depts;
	private String useFlag;
	private String useFlagName;
	
	private String sort;
	
	private String order;
	
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
	
	public String getUserCodeS() {
		return userCodeS;
	}
	public void setUserCodeS(String userCodeS) {
		this.userCodeS = userCodeS;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getBankInfo() {
		return bankInfo;
	}
	public void setBankInfo(String bankInfo) {
		this.bankInfo = bankInfo;
	}
	public String getComCode() {
		return comCode;
	}
	public void setComCode(String comCode) {
		this.comCode = comCode;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	
	
	public String getComCodeName() {
		return comCodeName;
	}
	public void setComCodeName(String comCodeName) {
		this.comCodeName = comCodeName;
	}
	public String getDeptCodeName() {
		return deptCodeName;
	}
	public void setDeptCodeName(String deptCodeName) {
		this.deptCodeName = deptCodeName;
	}
	public String getComName() {
		return comName;
	}
	public void setComName(String comName) {
		this.comName = comName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getIdCardNo() {
		return idCardNo;
	}
	public void setIdCardNo(String idCardNo) {
		this.idCardNo = idCardNo;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getBranchs() {
		return branchs;
	}
	public void setBranchs(String branchs) {
		this.branchs = branchs;
			
	}
	public String getDepts() {
		return depts;
	}
	public void setDepts(String depts) {
		this.depts = depts;
	}
	
	public String getUseFlag() {
		return useFlag;
	}
	public void setUseFlag(String useFlag) {
		this.useFlag = useFlag;
	}
	public String getUseFlagName() {
		return useFlagName;
	}
	public void setUseFlagName(String useFlagName) {
		this.useFlagName = useFlagName;
	}
	public static UserinfoDTO toDTO(UserInfo userinfo){
		UserinfoDTO dto =  new UserinfoDTO();
		dto.setId(String.valueOf(userinfo.getId()));
		dto.setUserCode(userinfo.getUserCode());
		dto.setUserName(userinfo.getUserName());
		dto.setBankCode(userinfo.getBankCode());
		dto.setBankInfo(userinfo.getBankInfo());
		dto.setComCode(userinfo.getComCode());
		dto.setDeptCode(userinfo.getDeptCode());
		dto.setEmail(userinfo.getEmail());
		dto.setIdCardNo(userinfo.getIdCardNo());
		return dto;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}

}
