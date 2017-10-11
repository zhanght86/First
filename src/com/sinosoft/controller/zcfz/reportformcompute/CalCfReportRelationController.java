package com.sinosoft.controller.zcfz.reportformcompute;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sinosoft.common.InvokeResult;
import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dto.reportformcompute.CalCfReportRelationDTO;
import com.sinosoft.zcfz.service.reportformcompute.CalCfReportRelationService;

@Controller
@RequestMapping(path="/calCfReportRelation")
public class CalCfReportRelationController {
	@Resource
	private CalCfReportRelationService calCfReportRelationService;
	@RequestMapping(path="/add", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult saveCalCfReportRelation(CalCfReportRelationDTO dto){
		try {
			calCfReportRelationService.saveCalCfReportRelation(dto);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
		
	}
	@RequestMapping(path="/listpage")
	@ResponseBody
	public Page<?> qryCalCfReportRelationOfPage(@RequestParam int page,@RequestParam int rows,CalCfReportRelationDTO dto){
		return calCfReportRelationService.qryCalCfReportRelationOfPage(page, rows,dto);
	}
	@RequestMapping(path="/listall")
	@ResponseBody
	public List<?> qryCalCfReportRelationAll(){
		return calCfReportRelationService.qryCalCfReportRelationAll();
	}
	@RequestMapping(path="/list")
	@ResponseBody
	public List<?> qryCalCfReportRelation(){
		return calCfReportRelationService.qryCalCfReportRelation();
	}
	@RequestMapping(path="/update")
	@ResponseBody
	public InvokeResult updateCalCfReportRelation(@RequestParam Integer id,CalCfReportRelationDTO dto){
		try {
			calCfReportRelationService.updateCalCfReportRelation(id, dto);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
	@RequestMapping(path="/delete")
	@ResponseBody
	public InvokeResult deleteCalCfReportRelation(@RequestParam String ids){
		System.out.println(ids);
		try {
			String[] idArr = ids.split(",");
			calCfReportRelationService.deleteCalCfReportRelation(idArr);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
}
