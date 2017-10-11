package com.sinosoft.zcfz.service.glnlpg;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sinosoft.common.InvokeResult;
import com.sinosoft.common.Page;
import com.sinosoft.dto.RoleInfoDTO;
import com.sinosoft.dto.UserinfoDTO;
import com.sinosoft.zcfz.dto.glnlpg.DataImportDTO;
import com.sinosoft.zcfz.dto.glnlpg.Alm_ReportDataDTO;

public interface Alm_ReportDataService {
	
	public List<?> queryDataByDeptNo();
	
	public List<?> qrySev_ReportDataDaoOfPage();
	
	public Page<?> qrySev_ReportDataDaoAllOfPage(int page,int rows,Alm_ReportDataDTO dto, String dept);

	public void tongguo(String[] idArr);

	public String panduan(String reportId);

	public void edit(Alm_ReportDataDTO sev);

	public void submit(String[] idArr);

	public void fanhuixiugai(Alm_ReportDataDTO srd, String reportId, String itemCode, String deptNo,String sevStatus);

	public void download(HttpServletRequest request,HttpServletResponse response, String deptNo);

	public void save(DataImportDTO dto, String reportId, String itemCode,String number);

	public List<?> file(String reportId, String itemCode, String number);

	public void downloadURD(long id, HttpServletRequest request,
			HttpServletResponse response) throws IOException;

	public void cut(String[] idArr);

	public String downloadHTTP(long id, HttpServletRequest request,
			HttpServletResponse response);

	public void send();

	public void traceImage(String instanceId, HttpServletResponse response);

	public List<?> findEvaluateEventComment(String instanceId);

	public List<?> deptHistory(String deptNo, String reportId);

}
