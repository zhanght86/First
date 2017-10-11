package com.sinosoft.zcfz.service.glnlpg;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sinosoft.common.InvokeResult;
import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dto.glnlpg.Alm_ReportDataDTO;
import com.sinosoft.zcfz.dto.glnlpg.Alm_ResultDTO;

public interface Alm_ResultService {

	public List<?> list(Alm_ResultDTO sr);

	public void edit(Alm_ResultDTO sr);

	public void submit(String reportId);

	public List<?> listhistroy(String projectCode, String reportId);

	public String get();

	public String gettype();

	public String get2();

	public List<?> listfeedback(Alm_ResultDTO sr);

	public void choosedept(Alm_ResultDTO sr);

	public void tixing(String reportId);

	public void download(HttpServletRequest request,HttpServletResponse response, String reportId);

	public String get3();

	public void entry(Alm_ResultDTO sr);

	public void tijiao(String reportId);

	public List<?> listfeedback2(Alm_ResultDTO sr);

	public List<?> histroy(String projectCode, String reportId);

	public List<?> listApproval(Alm_ResultDTO sr);

	public InvokeResult tongguo(String reportId);

	public InvokeResult fanhuixiugai(Alm_ReportDataDTO srd, String reportId);

	
}