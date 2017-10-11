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
import com.sinosoft.zcfz.service.reportformcompute.CalPreComputeService;

@Controller
@RequestMapping(path="/calPreCompute")
public class CalPreComputeController {
	@Resource
	private CalPreComputeService calPreComputeService;
	@RequestMapping(path="/precompute", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult preCompute(@RequestParam String reportId,@RequestParam String reporttype){
		try {
			UserInfo user=CurrentUser.CurrentUser;
			calPreComputeService.preCompute(reportId,user,reporttype);
			return InvokeResult.success("初步计算完成！"); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
		
	}
	@RequestMapping(path="/backprecompute", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult backPreCompute(@RequestParam String reportId,@RequestParam String reporttype){
		try {
			calPreComputeService.backPreCompute(reportId,reporttype);
			return InvokeResult.success("初步计算完成！"); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
		
	}
	
}
