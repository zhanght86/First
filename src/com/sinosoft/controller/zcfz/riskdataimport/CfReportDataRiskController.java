package com.sinosoft.controller.zcfz.riskdataimport;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sinosoft.common.InvokeResult;
import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dto.riskdataimport.CfReportDataRiskDTO;
import com.sinosoft.zcfz.service.riskdataimport.CfReportDataRiskService;

@Controller
@RequestMapping(path="/cfReportDataRisk")
public class CfReportDataRiskController {
	@Resource
	private CfReportDataRiskService cfReportDataRiskService;
	@RequestMapping(path="/add", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult saveCfReportDataRisk(CfReportDataRiskDTO dto){
		try {
			cfReportDataRiskService.saveCfReportDataRisk(dto);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
		
	}
	@RequestMapping(path="/listpage")
	@ResponseBody
	public Page<?> qryCfReportDataRiskOfPage(@RequestParam int page,@RequestParam int rows,CfReportDataRiskDTO dto){
		return cfReportDataRiskService.qryCfReportDataRiskOfPage(page, rows,dto);
	}
	@RequestMapping(path="/listall")
	@ResponseBody
	public List<?> qryCfReportDataRiskAll(){
		return cfReportDataRiskService.qryCfReportDataRiskAll();
	}
	@RequestMapping(path="/list")
	@ResponseBody
	public List<?> qryCfReportDataRisk(){
		return cfReportDataRiskService.qryCfReportDataRisk();
	}
	@RequestMapping(path="/update")
	@ResponseBody
	public InvokeResult updateCfReportDataRisk(@RequestParam long id,CfReportDataRiskDTO dto){
		try {
			cfReportDataRiskService.updateCfReportDataRisk(id, dto);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
	@RequestMapping(path="/delete")
	@ResponseBody
	public InvokeResult deleteCfReportDataRisk(@RequestParam String ids){
		try {
			String[] idArr = ids.split(",");
			cfReportDataRiskService.deleteCfReportDataRisk(idArr);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
}
