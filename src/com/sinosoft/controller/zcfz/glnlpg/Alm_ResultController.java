package com.sinosoft.controller.zcfz.glnlpg;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sinosoft.common.InvokeResult;
import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dto.glnlpg.Alm_ReportDataDTO;
import com.sinosoft.zcfz.dto.glnlpg.Alm_ResultDTO;
import com.sinosoft.zcfz.service.glnlpg.Alm_ResultService;


@Controller
@RequestMapping(path="/alm_result")
public class Alm_ResultController {
	@Resource
	private Alm_ResultService sev_ResultService;
	/**
	 * 结果调整数据查询
	 * @param sr
	 * @return
	 */
	@RequestMapping(path="/list")
	@ResponseBody
	public List<?> list(Alm_ResultDTO sr){
		return sev_ResultService.list(sr);
	}
	@RequestMapping(path="/listfeedback")
	@ResponseBody
	public List<?> listfeedback(Alm_ResultDTO sr){
		return sev_ResultService.listfeedback(sr);
	}
	@RequestMapping(path="/listfeedback2")
	@ResponseBody
	public List<?> listfeedback2(Alm_ResultDTO sr){
		return sev_ResultService.listfeedback2(sr);
	}
	/**
	 * 计算结果查询
	 * 跟调整前的一样只是这次查的是调整后的数据
	 * @param projectCode
	 * @param reportId
	 * @return
	 */
	@RequestMapping(path="/history")
	@ResponseBody
	public List<?> history(@RequestParam String projectCode,@RequestParam String reportId){		
		System.out.println(projectCode);
		System.out.println(reportId);
		return sev_ResultService.histroy(projectCode,reportId);
	}
	/**
	 * 汇总调整条码信息编辑保存
	 * 跟据选择的符合程度设置相应的分数
	 * @param sr
	 * @return
	 */
	@RequestMapping(path="/edit")
	@ResponseBody
	public InvokeResult edit(Alm_ResultDTO sr){
		sev_ResultService.edit(sr);
		return InvokeResult.success();
	}
	@RequestMapping(path="/choosedept")
	@ResponseBody
	public InvokeResult choosedept(Alm_ResultDTO sr){
		sev_ResultService.choosedept(sr);
		return InvokeResult.success();
	}
	/**
	 * 汇总调整完成后提交修改数据进行报告的数据重新计算
	 * 如果有审批流程可以开启 没有即直接生成结果
	 * @param reportId
	 * @return
	 */
	@RequestMapping(path="/submit")
	@ResponseBody
	public InvokeResult submit(@RequestParam String reportId){
		sev_ResultService.submit(reportId);
		return InvokeResult.success();
	}
	@RequestMapping(path="/tongguo")
	@ResponseBody
	public InvokeResult tongguo(@RequestParam String reportId){
		
		return sev_ResultService.tongguo(reportId);
	}
	@RequestMapping(path="/fanhuixiugai")
	@ResponseBody
	public InvokeResult fanhuixiugai(Alm_ReportDataDTO srd,@RequestParam String reportId){
		
		return sev_ResultService.fanhuixiugai(srd,reportId);
	}
	@RequestMapping(path="/listApproval")
	@ResponseBody
	public List<?> listApproval(Alm_ResultDTO sr){
		return sev_ResultService.listApproval(sr);
	}
	@RequestMapping(path="/tixing")
	@ResponseBody
	public InvokeResult tixing(@RequestParam String reportId){
		sev_ResultService.tixing(reportId);
		return InvokeResult.success();
	}
	@RequestMapping(path="/listhistroy")
	@ResponseBody
	public List<?> listhistroy(@RequestParam String projectCode,@RequestParam String reportId){
		
		System.out.println(projectCode);
		System.out.println(reportId);
		return sev_ResultService.listhistroy(projectCode,reportId);
	}
	//获取汇总调整的单号
	@RequestMapping(path="/get")
	@ResponseBody
	public String get(){
		String str=sev_ResultService.get();
		System.out.println(str);
		return str;
	}
	//get结果反馈是自评还是监管
	@RequestMapping(path="/get2")
	@ResponseBody
	public String get2(){
		String str=sev_ResultService.get2();
		System.out.println(str);
		return str;
	}
	//get还没有监管录入的单号
	@RequestMapping(path="/get3")
	@ResponseBody
	public String get3(){
		String str=sev_ResultService.get3();
		System.out.println(str);
		return str;
	}
	//监管录入
	@RequestMapping(path="/entry")
	@ResponseBody
	public InvokeResult entry(Alm_ResultDTO sr){
		sev_ResultService.entry(sr);
		return InvokeResult.success();
	}
	@RequestMapping(path="/tijiao")
	@ResponseBody
	public InvokeResult tijiao(@RequestParam String reportId){
		sev_ResultService.tijiao(reportId);
		return InvokeResult.success();
	}
	@RequestMapping(path="/gettype")
	@ResponseBody
	public String gettype(){
		String str=sev_ResultService.gettype();
		System.out.println(str);
		return str;
	}
	@RequestMapping(path="/download")
	@ResponseBody
	public void download(HttpServletRequest request, HttpServletResponse response,String reportId){
		System.out.println(reportId);
		sev_ResultService.download(request, response,reportId);
	}
}