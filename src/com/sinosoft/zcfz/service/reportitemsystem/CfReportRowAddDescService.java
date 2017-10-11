package com.sinosoft.zcfz.service.reportitemsystem;

import java.util.List;

import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dto.reportitemsystem.CfReportItemCodeDescDTO;
import com.sinosoft.zcfz.dto.reportitemsystem.CfReportRowAddDescDTO;
import com.sinosoft.entity.CfReportItemCodeDesc;
import com.sinosoft.entity.CfReportRowAddDesc;
import com.sinosoft.entity.CfReportRowAddDescId;

public interface CfReportRowAddDescService {
	public void saveCfReportRowAdd(CfReportRowAddDescDTO rid);
	public void updateCfReportRowAdd(CfReportRowAddDescId cfrid,CfReportRowAddDescDTO rid);
	public void deleteCfReportRowAdd(CfReportRowAddDescId cfrid);
	public Page<?> qryCfReportRowAddOfPage(int page,int rows);
	public List<?> qryCfReportRowAdd();
	public List<?> qryCfReportRowAddAll();
	public CfReportRowAddDesc findReportItem(CfReportRowAddDescId cfrid);
	public Page<?> qryCfReportRowAddOfPage(int page, int rows, CfReportRowAddDescDTO rid);
}
