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
import com.sinosoft.zcfz.dto.reportformcompute.Rf0DefineDTO;
import com.sinosoft.zcfz.service.reportformcompute.Rf0DefineService;

@Controller
@RequestMapping(path="/rf0Define")
public class Rf0DefineController {
	@Resource
	private Rf0DefineService rf0DefineService;
	@RequestMapping(path="/add", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult saveRf0Define(Rf0DefineDTO dto){
		try {
			rf0DefineService.saveRf0Define(dto);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
		
	}
	@RequestMapping(path="/listpage")
	@ResponseBody
	public Page<?> qryRf0DefineOfPage(@RequestParam int page,@RequestParam int rows,Rf0DefineDTO dto){
		return rf0DefineService.qryRf0DefineOfPage(page, rows,dto);
	}
	@RequestMapping(path="/listall")
	@ResponseBody
	public List<?> qryRf0DefineAll(){
		return rf0DefineService.qryRf0DefineAll();
	}
	@RequestMapping(path="/list")
	@ResponseBody
	public List<?> qryRf0Define(){
		return rf0DefineService.qryRf0Define();
	}
	@RequestMapping(path="/update")
	@ResponseBody
	public InvokeResult updateRf0Define(@RequestParam int id,Rf0DefineDTO dto){
		try {
			rf0DefineService.updateRf0Define(id, dto);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
	@RequestMapping(path="/delete")
	@ResponseBody
	public InvokeResult deleteRf0Define(@RequestParam String ids){
		try {
			String[] idArr = ids.split(",");
			rf0DefineService.deleteRf0Define(idArr);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
}
