package com.sinosoft.zcfz.service.reportformcompute;  

import java.util.List;

import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dto.reportformcompute.KdefineDTO;
import com.sinosoft.entity.Kdefine;

public interface KdefineService {  
    public void saveKdefine(KdefineDTO dto);
	public void updateKdefine(int id,KdefineDTO dto);
	public void deleteKdefine(String[] idArr);
	public Page<?> qryKdefineOfPage(int page,int rows,KdefineDTO dto);
	public List<?> qryKdefine();
	public List<?> qryKdefineAll();
	public Kdefine findKdefine(long id);
}