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
import com.sinosoft.zcfz.dto.reportformcompute.KdefineDTO;
import com.sinosoft.zcfz.service.reportformcompute.KdefineService;

@Controller
@RequestMapping(path="/kdefine")
public class KdefineController {
	@Resource
	private KdefineService kdefineService;
	@RequestMapping(path="/add", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult saveKdefine(KdefineDTO dto){
		try {
			kdefineService.saveKdefine(dto);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
		
	}
	@RequestMapping(path="/listpage")
	@ResponseBody
	public Page<?> qryKdefineOfPage(@RequestParam int page,@RequestParam int rows,KdefineDTO dto){
		return kdefineService.qryKdefineOfPage(page, rows,dto);
	}
	@RequestMapping(path="/listall")
	@ResponseBody
	public List<?> qryKdefineAll(){
		return kdefineService.qryKdefineAll();
	}
	@RequestMapping(path="/list")
	@ResponseBody
	public List<?> qryKdefine(){
		return kdefineService.qryKdefine();
	}
	@RequestMapping(path="/update")
	@ResponseBody
	public InvokeResult updateKdefine(@RequestParam int id,KdefineDTO dto){
		try {
			kdefineService.updateKdefine(id, dto);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
	@RequestMapping(path="/delete")
	@ResponseBody
	public InvokeResult deleteKdefine(@RequestParam String ids){
		try {
			String[] idArr = ids.split(",");
			kdefineService.deleteKdefine(idArr);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
}
