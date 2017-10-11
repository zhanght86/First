package com.sinosoft.service;  

import java.util.List;
import com.sinosoft.common.Page;
import com.sinosoft.dto.InterTabInvestInfoDTO;
import com.sinosoft.entity.InterTabInvestInfo;

public interface InterTabInvestInfoService {  
    public void saveInterTabInvestInfo(InterTabInvestInfoDTO dto);
	public void updateInterTabInvestInfo(long id,InterTabInvestInfoDTO dto);
	public void deleteInterTabInvestInfo(String[] idArr);
	public Page<?> qryInterTabInvestInfoOfPage(int page,int rows,InterTabInvestInfoDTO dto);
	public List<?> qryInterTabInvestInfo();
	public List<?> qryInterTabInvestInfoAll();
	public InterTabInvestInfo findInterTabInvestInfo(long id);
}