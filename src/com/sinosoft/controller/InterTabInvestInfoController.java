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
import com.sinosoft.dto.InterTabInvestInfoDTO;
import com.sinosoft.service.InterTabInvestInfoService;

@Controller
@RequestMapping(path="/interTabInvestInfo")
public class InterTabInvestInfoController {
	@Resource
	private InterTabInvestInfoService interTabInvestInfoService;
	@RequestMapping(path="/add", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult saveInterTabInvestInfo(InterTabInvestInfoDTO dto){
		try {
			interTabInvestInfoService.saveInterTabInvestInfo(dto);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
		
	}
	@RequestMapping(path="/listpage")
	@ResponseBody
	public Page<?> qryInterTabInvestInfoOfPage(@RequestParam int page,@RequestParam int rows,InterTabInvestInfoDTO dto){
		return interTabInvestInfoService.qryInterTabInvestInfoOfPage(page, rows,dto);
	}
	@RequestMapping(path="/listall")
	@ResponseBody
	public List<?> qryInterTabInvestInfoAll(){
		return interTabInvestInfoService.qryInterTabInvestInfoAll();
	}
	@RequestMapping(path="/list")
	@ResponseBody
	public List<?> qryInterTabInvestInfo(){
		return interTabInvestInfoService.qryInterTabInvestInfo();
	}
	@RequestMapping(path="/update")
	@ResponseBody
	public InvokeResult updateInterTabInvestInfo(@RequestParam long id,InterTabInvestInfoDTO dto){
		try {
			interTabInvestInfoService.updateInterTabInvestInfo(id, dto);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
	@RequestMapping(path="/delete")
	@ResponseBody
	public InvokeResult deleteInterTabInvestInfo(@RequestParam String ids){
		try {
			String[] idArr = ids.split(",");
			interTabInvestInfoService.deleteInterTabInvestInfo(idArr);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
}
