package com.sinosoft.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sinosoft.dto.DownloadCheckDTO;
import com.sinosoft.zcfz.service.reportdatacheck.DownloadCheckService;

@Controller
@RequestMapping("/downloadCheck")
public class DownloadCheckController {
	
	@Resource
	private DownloadCheckService downloadCheckService ;
	
	@RequestMapping(value = "/downCheckResult")
	public void downLoadcheckResult(HttpServletRequest request,
			HttpServletResponse response,
 @RequestParam String name,
			@RequestParam String queryConditions, @RequestParam String cols,
			String checkedCode, String reporttype, String year, String quarter) {
		DownloadCheckDTO dto = new DownloadCheckDTO();
		dto.setYear(year);
		dto.setQuarter(quarter);
		dto.setCheckedCode(checkedCode);
		dto.setReportType(reporttype);
		downloadCheckService.downCheckResult(request, response, name,
 dto, cols);
	}

	@RequestMapping(value = "/download1")
	public void downLoad1(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String name,
			@RequestParam String queryConditions, @RequestParam String cols) {
		downloadCheckService.downLoad1(request, response, name,
				queryConditions, cols);
	}
	
	@RequestMapping(value="/download2")
	public void downLoad2(HttpServletRequest request, HttpServletResponse response,
			@RequestParam String name, @RequestParam String queryConditions, @RequestParam String cols){
		downloadCheckService.downLoad2(request, response, name, queryConditions, cols);
	}
	
	@RequestMapping(value="/download3")
	public void downLoad3(HttpServletRequest request, HttpServletResponse response,
			@RequestParam String name, @RequestParam String queryConditions, @RequestParam String cols){
		downloadCheckService.downLoad3(request, response, name, queryConditions, cols);
	}
	
	@RequestMapping(value="/download4")
	public void downLoad4(HttpServletRequest request, HttpServletResponse response,
			@RequestParam String name, @RequestParam String queryConditions, @RequestParam String cols){
		downloadCheckService.downLoad4(request, response, name, queryConditions, cols);
	}
}
