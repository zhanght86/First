package com.sinosoft.controller.zcfz.reportformcompute;


import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sinosoft.common.CurrentUser;
import com.sinosoft.common.InvokeResult;
import com.sinosoft.entity.UserInfo;
import com.sinosoft.zcfz.service.reportformcompute.CalComplexComputeService;

@Controller
@RequestMapping(path="/calComplexCompute")
public class CalComplexComputeController {
	@Resource
	private CalComplexComputeService calComplexComputeService;
	@RequestMapping(path="/complexcompute", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult complexCompute(@RequestParam String reportId,@RequestParam String reporttype){
		try {
			UserInfo user=CurrentUser.CurrentUser;
			calComplexComputeService.ComplexCompute(reportId,user,reporttype);
			return InvokeResult.success("汇总计算完成！"); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
		
	}
	@RequestMapping(path="/backprecompute", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult backComplexCompute(@RequestParam String reportId,@RequestParam String reporttype){
		try {
			calComplexComputeService.backComplexCompute(reportId,reporttype);
			return InvokeResult.success("汇总计算完成！"); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
		
	}
	
}
