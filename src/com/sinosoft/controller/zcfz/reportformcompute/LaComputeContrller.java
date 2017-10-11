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
import com.sinosoft.zcfz.service.reportformcompute.LaComputeService;

@Controller
@RequestMapping(path="/laCompute")
public class LaComputeContrller {
	@Resource
	private LaComputeService la;
	@RequestMapping(path="/laCompute", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult preCompute(@RequestParam String reportId){
		try {
			UserInfo user=CurrentUser.CurrentUser;
			la.laCompute(reportId,user);
			return InvokeResult.success("损失吸收效应计算完成！"); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
		
	}
}
