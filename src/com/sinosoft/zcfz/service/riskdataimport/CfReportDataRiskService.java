package com.sinosoft.zcfz.service.riskdataimport;  

import java.util.List;

import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dto.riskdataimport.CfReportDataRiskDTO;
import com.sinosoft.entity.CfReportDataRisk;

public interface CfReportDataRiskService {  
    public void saveCfReportDataRisk(CfReportDataRiskDTO dto);
	public void updateCfReportDataRisk(long id,CfReportDataRiskDTO dto);
	public void deleteCfReportDataRisk(String[] idArr);
	public Page<?> qryCfReportDataRiskOfPage(int page,int rows,CfReportDataRiskDTO dto);
	public List<?> qryCfReportDataRisk();
	public List<?> qryCfReportDataRiskAll();
	public CfReportDataRisk findCfReportDataRisk(long id);
}