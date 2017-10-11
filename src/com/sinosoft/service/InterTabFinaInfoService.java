package com.sinosoft.service;  

import java.util.List;
import com.sinosoft.common.Page;
import com.sinosoft.dto.InterTabFinaInfoDTO;
import com.sinosoft.entity.InterTabFinaInfo;

public interface InterTabFinaInfoService {  
    public void saveInterTabFinaInfo(InterTabFinaInfoDTO dto);
	public void updateInterTabFinaInfo(long id,InterTabFinaInfoDTO dto);
	public void deleteInterTabFinaInfo(String[] idArr);
	public Page<?> qryInterTabFinaInfoOfPage(int page,int rows,InterTabFinaInfoDTO dto);
	public List<?> qryInterTabFinaInfo();
	public List<?> qryInterTabFinaInfoAll();
	public InterTabFinaInfo findInterTabFinaInfo(long id);
}