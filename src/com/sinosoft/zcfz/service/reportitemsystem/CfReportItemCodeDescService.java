package com.sinosoft.zcfz.service.reportitemsystem;

import java.util.List;

import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dto.reportitemsystem.CfReportItemCodeDescDTO;
import com.sinosoft.entity.CfReportItemCodeDesc;

public interface CfReportItemCodeDescService {
	public void saveReportItem(CfReportItemCodeDescDTO rid);
	public void updateReportItem(String id,CfReportItemCodeDescDTO rid);
	public void deleteReportItem(String id);
	public Page<?> qryReportItemOfPage(int page,int rows);
	public List<?> qryReportItem();
	public List<?> qryReportItemAll();
	public CfReportItemCodeDesc findReportItem(String id);
	public Page<?> qryReportItemOfPage(int page, int rows, CfReportItemCodeDescDTO rid);
	public Page<?> qryReportItemTextBlockOfPage(int page, int rows,String reportId,String department);
}
