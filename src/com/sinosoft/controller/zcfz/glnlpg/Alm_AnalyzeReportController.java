package com.sinosoft.controller.zcfz.glnlpg;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sinosoft.zcfz.dto.glnlpg.Alm_GatherInfoDTO;
import com.sinosoft.zcfz.service.glnlpg.Alm_AnalyzeReportService;

@Controller
@RequestMapping(path="/analyzereport")
public class Alm_AnalyzeReportController {
	@Resource
	private Alm_AnalyzeReportService sev_ars;
	
	@RequestMapping(path="/qrydata")
	@ResponseBody
	public Map<?,?> qryAnalyzeData (Alm_GatherInfoDTO dto){
		return sev_ars.qryAnalyzeData(dto);
	}
}
