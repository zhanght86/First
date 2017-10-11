package com.sinosoft.controller.zcfz.glnlpg;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sinosoft.common.InvokeResult;
import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dto.glnlpg.Alm_AdjPlanDTO;
import com.sinosoft.zcfz.dto.glnlpg.Alm_ComputeResultDTO;
import com.sinosoft.zcfz.dto.glnlpg.Alm_GatherInfoDTO;
import com.sinosoft.zcfz.dto.glnlpg.Alm_ReportDataDTO;
import com.sinosoft.zcfz.dto.glnlpg.Alm_ResultDTO;
import com.sinosoft.zcfz.service.glnlpg.Alm_AdjPlanService;
import com.sinosoft.zcfz.service.glnlpg.Alm_ComputeResultService;
import com.sinosoft.zcfz.service.glnlpg.Alm_ResultService;


@Controller
@RequestMapping(path="/adjPlan")
public class Alm_AdjPlanController {
	private static final Logger LOGGER = LoggerFactory.getLogger(Alm_AdjPlanController.class);
	@Resource
	private Alm_AdjPlanService sev_AdjPlanService;
	
	@RequestMapping(path="/list")
	@ResponseBody
	public List<?> listAdjPlan(){
		return sev_AdjPlanService.listAdjPlan();
	}
	@RequestMapping(path="/edit")
	@ResponseBody
	public InvokeResult edit(Alm_AdjPlanDTO sev){
		sev_AdjPlanService.edit(sev);
		return InvokeResult.success();
	}
	@RequestMapping(path="/submit")
	@ResponseBody
	public InvokeResult submit(@RequestParam String ids){
		System.out.println(ids);
		String[] idArr = ids.split(",");
		sev_AdjPlanService.submit(idArr);
		return InvokeResult.success(); 
	}
	@RequestMapping(path="/listplan")
	@ResponseBody
	public Page<?> listplan(@RequestParam int page,@RequestParam int rows,Alm_AdjPlanDTO dto,String dept){
		return sev_AdjPlanService.listplan(page,rows,dto,dept);
	}
	@RequestMapping(path="/send")
	@ResponseBody
	public InvokeResult send(){
		sev_AdjPlanService.send();
		return InvokeResult.success(); 
	}
	@RequestMapping(path="/listpage")
	@ResponseBody
	public Page<?> listpage(@RequestParam int page,@RequestParam int rows){
		return sev_AdjPlanService.listpage(page,rows);
	}
	@RequestMapping(value="/tongguo", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult tongguo(@RequestParam String ids){
		System.out.println(ids);
		String[] idArr = ids.split(",");
		sev_AdjPlanService.tongguo(idArr);
		return InvokeResult.success(); 
	}
	@RequestMapping(path="/fanhuixiugai")
	@ResponseBody
	public InvokeResult fanhuixiugai(Alm_AdjPlanDTO srd){
		System.out.println(srd.getId()+"#"+srd.getTaskId());
		sev_AdjPlanService.fanhuixiugai(srd);
		return InvokeResult.success(); 
	}
}