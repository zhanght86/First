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
import com.sinosoft.zcfz.dto.riskdataimport.CfReportRowAddDataRiskDTO;
import com.sinosoft.zcfz.service.riskdataimport.CfReportRowAddDataRiskService;

@Controller
@RequestMapping(path="/cfReportRowAddDataRisk")
public class CfReportRowAddDataRiskController {
	@Resource
	private CfReportRowAddDataRiskService cfReportRowAddDataRiskService;
	@RequestMapping(path="/add", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult saveCfReportRowAddDataRisk(CfReportRowAddDataRiskDTO dto){
		try {
			cfReportRowAddDataRiskService.saveCfReportRowAddDataRisk(dto);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
		
	}
	@RequestMapping(path="/listpage")
	@ResponseBody
	public Page<?> qryCfReportRowAddDataRiskOfPage(@RequestParam int page,@RequestParam int rows,CfReportRowAddDataRiskDTO dto){
		return cfReportRowAddDataRiskService.qryCfReportRowAddDataRiskOfPage(page, rows,dto);
	}
	@RequestMapping(path="/listall")
	@ResponseBody
	public List<?> qryCfReportRowAddDataRiskAll(){
		return cfReportRowAddDataRiskService.qryCfReportRowAddDataRiskAll();
	}
	@RequestMapping(path="/list")
	@ResponseBody
	public List<?> qryCfReportRowAddDataRisk(){
		return cfReportRowAddDataRiskService.qryCfReportRowAddDataRisk();
	}
	@RequestMapping(path="/update")
	@ResponseBody
	public InvokeResult updateCfReportRowAddDataRisk(@RequestParam long id,CfReportRowAddDataRiskDTO dto){
		try {
			cfReportRowAddDataRiskService.updateCfReportRowAddDataRisk(id, dto);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
	@RequestMapping(path="/delete")
	@ResponseBody
	public InvokeResult deleteCfReportRowAddDataRisk(@RequestParam String ids){
		try {
			String[] idArr = ids.split(",");
			cfReportRowAddDataRiskService.deleteCfReportRowAddDataRisk(idArr);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
}
