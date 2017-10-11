package com.sinosoft.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fr.web.core.A.d;
import com.sinosoft.common.InvokeResult;
import com.sinosoft.common.Page;
import com.sinosoft.dto.InterTabFinaInfoDTO;
import com.sinosoft.service.InterTabFinaInfoService;

@Controller
@RequestMapping(path="/interTabFinaInfo")
public class InterTabFinaInfoController {
	@Resource
	private InterTabFinaInfoService interTabFinaInfoService;
	@RequestMapping(path="/add", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult saveInterTabFinaInfo(InterTabFinaInfoDTO dto){
		try {
			interTabFinaInfoService.saveInterTabFinaInfo(dto);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
		
	}
	@RequestMapping(path="/listpage")
	@ResponseBody
	public Page<?> qryInterTabFinaInfoOfPage(@RequestParam int page,@RequestParam int rows,@RequestParam String type1,@RequestParam String reportId, InterTabFinaInfoDTO dto){
		//System.out.println(type1);
		//System.out.println(reportId);
		String type=type1;
		String id=reportId;
		dto.setReportId(id);
		dto.setTabName(type);
		return interTabFinaInfoService.qryInterTabFinaInfoOfPage(page, rows,dto);
	}
	@RequestMapping(path="/listall")
	@ResponseBody
	public List<?> qryInterTabFinaInfoAll(){
		return interTabFinaInfoService.qryInterTabFinaInfoAll();
	}
	@RequestMapping(path="/list")
	@ResponseBody
	public List<?> qryInterTabFinaInfo(){
		return interTabFinaInfoService.qryInterTabFinaInfo();
	}
	@RequestMapping(path="/update")
	@ResponseBody
	public InvokeResult updateInterTabFinaInfo(@RequestParam long id,InterTabFinaInfoDTO dto){
		try {
			interTabFinaInfoService.updateInterTabFinaInfo(id, dto);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
	@RequestMapping(path="/delete")
	@ResponseBody
	public InvokeResult deleteInterTabFinaInfo(@RequestParam String ids){
		try {
			String[] idArr = ids.split(",");
			interTabFinaInfoService.deleteInterTabFinaInfo(idArr);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
}
