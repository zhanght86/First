package com.sinosoft.zcfz.service.reportformcompute;  

import java.util.List;

import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dto.reportformcompute.CalCfReportInfoDTO;
import com.sinosoft.entity.CalCfReportInfo;

public interface CalCfReportInfoService {  
    public boolean saveCalCfReportInfo(CalCfReportInfoDTO dto);
	public void updateCalCfReportInfo(String id,CalCfReportInfoDTO dto);
	public void deleteCalCfReportInfo(String[] idArr);
	public Page<?> qryCalCfReportInfoOfPage(int page,int rows,CalCfReportInfoDTO dto);
	public Page<?> qryCalCfReportInfoOfPage1(int page,int rows,CalCfReportInfoDTO dto);
	public Page<?> qryCalCfReportInfoOfPageByState(int page,int rows,CalCfReportInfoDTO dto,String type);
	public Page<?> qryCalCfReportInfoOfPageByType(int page,int rows,CalCfReportInfoDTO dto);
	public List<?> qryCalCfReportInfo();
	public List<?> qryCalCfReportInfoAll();
	public CalCfReportInfo findCalCfReportInfo(String id);
}