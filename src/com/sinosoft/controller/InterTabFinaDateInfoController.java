package com.sinosoft.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sinosoft.common.InvokeResult;
import com.sinosoft.common.Page;
import com.sinosoft.dto.InterTabFinaDateInfoDTO;
import com.sinosoft.service.InterTabFinaDateInfoService;

@Controller
@RequestMapping(path="/interTabFinaDateInfo")
public class InterTabFinaDateInfoController {
	@Resource
	private InterTabFinaDateInfoService interTabFinaDateInfoService;
	@RequestMapping(path="/add", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult saveInterTabFinaDateInfo(InterTabFinaDateInfoDTO dto){
		try {
			interTabFinaDateInfoService.saveInterTabFinaDateInfo(dto);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
		
	}
	@RequestMapping(path="/listpage")
	@ResponseBody
	public Page<?> qryInterTabFinaDateInfoOfPage(@RequestParam int page,@RequestParam int rows,InterTabFinaDateInfoDTO dto){
		return interTabFinaDateInfoService.qryInterTabFinaDateInfoOfPage(page, rows,dto);
	}
	@RequestMapping(path="/listall")
	@ResponseBody
	public List<?> qryInterTabFinaDateInfoAll(){
		return interTabFinaDateInfoService.qryInterTabFinaDateInfoAll();
	}
	@RequestMapping(path="/list")
	@ResponseBody
	public List<?> qryInterTabFinaDateInfo(){
		return interTabFinaDateInfoService.qryInterTabFinaDateInfo();
	}
	@RequestMapping(path="/update")
	@ResponseBody
	public InvokeResult updateInterTabFinaDateInfo(@RequestParam long id,InterTabFinaDateInfoDTO dto){
		try {
			interTabFinaDateInfoService.updateInterTabFinaDateInfo(id, dto);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
	@RequestMapping(path="/delete")
	@ResponseBody
	public InvokeResult deleteInterTabFinaDateInfo(@RequestParam String ids){
		try {
			String[] idArr = ids.split(",");
			interTabFinaDateInfoService.deleteInterTabFinaDateInfo(idArr);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
}
