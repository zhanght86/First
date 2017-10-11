package com.sinosoft.controller.zcfz.lianghua.reportformcompute;


import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sinosoft.common.CurrentUser;
import com.sinosoft.common.InvokeResult;
import com.sinosoft.entity.UserInfo;
import com.sinosoft.zcfz.service.lianghua.LianghuaComputeService;


@Controller
@RequestMapping(path="/lianghuaCompute")
public class LianghuaComputeController {
	@Resource
	private LianghuaComputeService lianghuaComputeService;
	@RequestMapping(path="/complexcompute", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult complexCompute(@RequestParam String reportId){
		try {
			UserInfo user=CurrentUser.getCurrentUser();
			lianghuaComputeService.ComplexCompute(reportId,user);
			return InvokeResult.success("汇总计算完成！"); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
		
	}
	@RequestMapping(path="/backprecompute", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult backComplexCompute(@RequestParam String reportId){
		try {
			lianghuaComputeService.backComplexCompute(reportId);
			return InvokeResult.success("汇总计算完成！"); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
		
	}
	
}
