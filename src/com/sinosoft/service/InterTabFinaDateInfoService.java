package com.sinosoft.service;  

import java.util.List;
import com.sinosoft.common.Page;
import com.sinosoft.dto.InterTabFinaDateInfoDTO;
import com.sinosoft.entity.InterTabFinaDateInfo;

public interface InterTabFinaDateInfoService {  
    public void saveInterTabFinaDateInfo(InterTabFinaDateInfoDTO dto);
	public void updateInterTabFinaDateInfo(long id,InterTabFinaDateInfoDTO dto);
	public void deleteInterTabFinaDateInfo(String[] idArr);
	public Page<?> qryInterTabFinaDateInfoOfPage(int page,int rows,InterTabFinaDateInfoDTO dto);
	public List<?> qryInterTabFinaDateInfo();
	public List<?> qryInterTabFinaDateInfoAll();
	public InterTabFinaDateInfo findInterTabFinaDateInfo(long id);
}