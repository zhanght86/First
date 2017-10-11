package com.sinosoft.zcfz.service.reportform;

import com.sinosoft.common.InvokeResult;
import com.sinosoft.zcfz.dto.reportform.ReportCreateInfoDTO;
import com.sinosoft.entity.CalCfReportInfo;

public interface ExportXbrlService {
	public InvokeResult reportCreate(ReportCreateInfoDTO info);
	public CalCfReportInfo getReportInfo(ReportCreateInfoDTO info);
	public String[] GetPeriod(String periodType,String year,String Quarter);
	public String getOrganName(String Quarter,String year,String CfReportRate,String organcode,String reportId);
	public String FormatData(String strValue,String decimals);
	public void test();
}
