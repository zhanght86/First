package com.sinosoft.zcfz.service.glnlpg;

import java.util.Map;

import com.sinosoft.zcfz.dto.glnlpg.Alm_GatherInfoDTO;


public interface Alm_AnalyzeReportService {
	public Map<?, ?> qryAnalyzeData(Alm_GatherInfoDTO dto);
}
