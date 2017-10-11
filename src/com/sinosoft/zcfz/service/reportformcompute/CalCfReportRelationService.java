package com.sinosoft.zcfz.service.reportformcompute;  

import java.util.List;

import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dto.reportformcompute.CalCfReportRelationDTO;
import com.sinosoft.entity.CalCfReportRelation;

public interface CalCfReportRelationService {  
    public void saveCalCfReportRelation(CalCfReportRelationDTO dto);
	public void updateCalCfReportRelation(Integer id,CalCfReportRelationDTO dto);
	public void deleteCalCfReportRelation(String[] idArr);
	public Page<?> qryCalCfReportRelationOfPage(int page,int rows,CalCfReportRelationDTO dto);
	public List<?> qryCalCfReportRelation();
	public List<?> qryCalCfReportRelationAll();
	public CalCfReportRelation findCalCfReportRelation(String id);
}