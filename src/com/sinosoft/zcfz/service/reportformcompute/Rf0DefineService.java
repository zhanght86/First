package com.sinosoft.zcfz.service.reportformcompute;  

import java.util.List;

import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dto.reportformcompute.Rf0DefineDTO;
import com.sinosoft.entity.Rf0Define;

public interface Rf0DefineService {  
    public void saveRf0Define(Rf0DefineDTO dto);
	public void updateRf0Define(int id,Rf0DefineDTO dto);
	public void deleteRf0Define(String[] idArr);
	public Page<?> qryRf0DefineOfPage(int page,int rows,Rf0DefineDTO dto);
	public List<?> qryRf0Define();
	public List<?> qryRf0DefineAll();
	public Rf0Define findRf0Define(long id);
}