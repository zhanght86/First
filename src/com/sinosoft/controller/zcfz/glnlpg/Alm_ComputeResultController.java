package com.sinosoft.controller.zcfz.glnlpg;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sinosoft.common.InvokeResult;
import com.sinosoft.common.Page;
import com.sinosoft.common.interceptor.Token;
import com.sinosoft.controller.zcfz.glnlpg.activiti.ActivitiController;
import com.sinosoft.zcfz.dto.glnlpg.DataImportDTO;
import com.sinosoft.zcfz.dto.glnlpg.Alm_ComputeResultDTO;
import com.sinosoft.zcfz.dto.glnlpg.Alm_GatherInfoDTO;
import com.sinosoft.entity.UploadFile;
import com.sinosoft.zcfz.service.glnlpg.Alm_ComputeResultService;
import com.sinosoft.util.Config;


@Controller
@RequestMapping(path="/alm_compute")
public class Alm_ComputeResultController {
	@Resource
	private Alm_ComputeResultService sev_ComputeResultService;
	
	/**
	 * 计算结果数据查询
	 * 查询汇总信息和基础信息
	 * @param projectCode
	 * @param reportId
	 * @return
	 */
	@RequestMapping(path="/list")
	@ResponseBody
	public List<?> qryThreshold(@RequestParam String projectCode,@RequestParam String reportId){
		
		System.out.println(projectCode);
		System.out.println(reportId);
		return sev_ComputeResultService.qryComputeResult(projectCode,reportId);
	}
	@RequestMapping(path="/listnew")
	@ResponseBody
	public List<?> listnew(Alm_GatherInfoDTO sg){
		return sev_ComputeResultService.listnew(sg);
	}
	@RequestMapping(path="/listdept")
	@ResponseBody
	public List<?> listdept(Alm_GatherInfoDTO sg){
		return sev_ComputeResultService.listdept(sg);
	}
	/**
	 * 指标计算前的校验
	 * 校验数据是否完整 如果有审批审批任务是否已都完结
	 * 是否允许重复计算
	 * @param reportId
	 * @return
	 */
	@RequestMapping(path="/verify")
	@ResponseBody
	public InvokeResult verify(@RequestParam String reportId){
		String returnResult = "";
		System.out.println(reportId);	
		try{
			returnResult= sev_ComputeResultService.verify(reportId);
			if(returnResult=="Y"){
				return InvokeResult.success();
			}else{
				return null;
			}
		}catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
	/**
	 * 汇总计算
	 * 根据录入数据计算总分把结果数据放入结果表内
	 * @param reportId
	 * @return
	 */
	@RequestMapping(path="/calculation")
	@ResponseBody
	public InvokeResult calculation(@RequestParam String reportId){
		System.out.println(reportId);		
		sev_ComputeResultService.calculation(reportId);
		return InvokeResult.success();
	}
	@RequestMapping(path="/download")
	public void download(HttpServletRequest request, HttpServletResponse response,String reportId){
		System.out.println(reportId);
		sev_ComputeResultService.download(request, response,reportId);
	}
	@RequestMapping(path="/get")
	@ResponseBody
	public String get(){
		String str=sev_ComputeResultService.get();
		System.out.println(str);
		return str;
	}
	@RequestMapping(path="/getyear")
	@ResponseBody
	public String getyear(){
		String str=sev_ComputeResultService.getyear();
		System.out.println(str);
		return str;
	}
	
}