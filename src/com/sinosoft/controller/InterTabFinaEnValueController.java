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
import com.sinosoft.dto.InterTabFinaEnValueDTO;
import com.sinosoft.service.InterTabFinaEnValueService;

@Controller
@RequestMapping(path="/interTabFinaEnValue")
public class InterTabFinaEnValueController {
	@Resource
	private InterTabFinaEnValueService interTabFinaEnValueService;
	@RequestMapping(path="/add", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult saveInterTabFinaEnValue(InterTabFinaEnValueDTO dto){
		try {
			interTabFinaEnValueService.saveInterTabFinaEnValue(dto);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
		
	}
	@RequestMapping(path="/listpage")
	@ResponseBody
	public Page<?> qryInterTabFinaEnValueOfPage(@RequestParam int page,@RequestParam int rows,InterTabFinaEnValueDTO dto){
		return interTabFinaEnValueService.qryInterTabFinaEnValueOfPage(page, rows,dto);
	}
	@RequestMapping(path="/listall")
	@ResponseBody
	public List<?> qryInterTabFinaEnValueAll(){
		return interTabFinaEnValueService.qryInterTabFinaEnValueAll();
	}
	@RequestMapping(path="/list")
	@ResponseBody
	public List<?> qryInterTabFinaEnValue(){
		return interTabFinaEnValueService.qryInterTabFinaEnValue();
	}
	@RequestMapping(path="/update")
	@ResponseBody
	public InvokeResult updateInterTabFinaEnValue(@RequestParam long id,InterTabFinaEnValueDTO dto){
		try {
			interTabFinaEnValueService.updateInterTabFinaEnValue(id, dto);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
	@RequestMapping(path="/delete")
	@ResponseBody
	public InvokeResult deleteInterTabFinaEnValue(@RequestParam String ids){
		try {
			String[] idArr = ids.split(",");
			interTabFinaEnValueService.deleteInterTabFinaEnValue(idArr);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
}
