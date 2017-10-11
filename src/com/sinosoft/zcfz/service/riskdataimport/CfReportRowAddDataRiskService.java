package com.sinosoft.zcfz.service.riskdataimport;  

import java.util.List;

import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dto.riskdataimport.CfReportRowAddDataRiskDTO;
import com.sinosoft.entity.CfReportRowAddDataRisk;

public interface CfReportRowAddDataRiskService {  
    public void saveCfReportRowAddDataRisk(CfReportRowAddDataRiskDTO dto);
	public void updateCfReportRowAddDataRisk(long id,CfReportRowAddDataRiskDTO dto);
	public void deleteCfReportRowAddDataRisk(String[] idArr);
	public Page<?> qryCfReportRowAddDataRiskOfPage(int page,int rows,CfReportRowAddDataRiskDTO dto);
	public List<?> qryCfReportRowAddDataRisk();
	public List<?> qryCfReportRowAddDataRiskAll();
	public CfReportRowAddDataRisk findCfReportRowAddDataRisk(long id);
}