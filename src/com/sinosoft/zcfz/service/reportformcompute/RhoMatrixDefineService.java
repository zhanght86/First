package com.sinosoft.zcfz.service.reportformcompute;  

import java.util.List;

import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dto.reportformcompute.RhoMatrixDefineDTO;
import com.sinosoft.entity.RhoMatrixDefine;

public interface RhoMatrixDefineService {  
    public void saveRhoMatrixDefine(RhoMatrixDefineDTO dto);
	public void updateRhoMatrixDefine(RhoMatrixDefineDTO dto);
	public void deleteRhoMatrixDefine(String[] idArr);
	public Page<?> qryRhoMatrixDefineOfPage(int page,int rows,RhoMatrixDefineDTO dto);
	public List<?> qryRhoMatrixDefine();
	public List<?> qryRhoMatrixDefineAll();
	public RhoMatrixDefine findRhoMatrixDefine(long id);
}