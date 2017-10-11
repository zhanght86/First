package com.sinosoft.zcfz.service.glnlpg;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dto.glnlpg.DataImportDTO;
import com.sinosoft.zcfz.dto.glnlpg.Alm_ItemDefineDTO;
import com.sinosoft.zcfz.dto.reportformcompute.CalCfReportInfoDTO;

public interface Alm_ItemDefineService {
	public Page<?> qrySev_ItemDefineOfPage(Alm_ItemDefineDTO sid, int page,int rows);
	
	public void updateManage(Alm_ItemDefineDTO sid);

	public Page<?> listAll(int page, int rows);

	public String get();

	public void send(CalCfReportInfoDTO sid);

	public void saveFile(DataImportDTO dto);

	public List<?> fileList();

	public String downloadHTTP(long id, HttpServletRequest request,
			HttpServletResponse response);

	public boolean saveCalCfReportInfo(CalCfReportInfoDTO dto);

}
