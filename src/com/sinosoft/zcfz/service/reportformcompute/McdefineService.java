package com.sinosoft.zcfz.service.reportformcompute;  

import java.util.List;

import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dto.reportformcompute.McdefineDTO;
import com.sinosoft.entity.Mcdefine;

public interface McdefineService {  
    public void saveMcdefine(McdefineDTO dto);
	public void updateMcdefine(Long id,McdefineDTO dto);
	public void deleteMcdefine(String[] idArr);
	public Page<?> qryMcdefineOfPage(int page,int rows,McdefineDTO dto);
	public List<?> qryMcdefine();
	public List<?> qryMcdefineAll();
	public Mcdefine findMcdefine(long id);
}