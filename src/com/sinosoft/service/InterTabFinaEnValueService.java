package com.sinosoft.service;  

import java.util.List;
import com.sinosoft.common.Page;
import com.sinosoft.dto.InterTabFinaEnValueDTO;
import com.sinosoft.entity.InterTabFinaEnValue;

public interface InterTabFinaEnValueService {  
    public void saveInterTabFinaEnValue(InterTabFinaEnValueDTO dto);
	public void updateInterTabFinaEnValue(long id,InterTabFinaEnValueDTO dto);
	public void deleteInterTabFinaEnValue(String[] idArr);
	public Page<?> qryInterTabFinaEnValueOfPage(int page,int rows,InterTabFinaEnValueDTO dto);
	public List<?> qryInterTabFinaEnValue();
	public List<?> qryInterTabFinaEnValueAll();
	public InterTabFinaEnValue findInterTabFinaEnValue(long id);
}