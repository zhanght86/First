package com.sinosoft.service;

import java.util.List;

import com.sinosoft.common.Page;
import com.sinosoft.dto.BranchInfoDTO;
import com.sinosoft.entity.BranchInfo;

public interface BranchInfoService {
	public void saveBranchInfo(BranchInfoDTO mdt);
	public void updateBranchInfo(String id,BranchInfoDTO cdt);
	public void deleteBranchInfo(String id);
	public Page<?> qryBranchInfoOfPage(int page,int rows);
	public List<?> qryBranchInfo();
	public List<?> qryBranchInfoo();
	public List<?> qryBranchInfoAll();
	/**
	 * 查询公司及子公司
	 * @param organize
	 * @return
	 */
	public List<String> qryBranchInfoAndChild(String organize);
	public BranchInfo findBranchInfo(String id);
	public List<?> listByUperCompany(String company);
	/**
	 * 返回公司及下级公司
	 * @param company
	 * @return 公司及下级公司list
	 */
	public List<?> listCompanyByUperCompany(String company);
}
