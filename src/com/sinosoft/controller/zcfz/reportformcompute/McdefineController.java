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
import com.sinosoft.zcfz.dto.reportformcompute.McdefineDTO;
import com.sinosoft.zcfz.service.reportformcompute.McdefineService;

@Controller
@RequestMapping(path="/mcdefine")
public class McdefineController {
	@Resource
	private McdefineService mcdefineService;
	@RequestMapping(path="/add", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult saveMcdefine(McdefineDTO dto){
		try {
			mcdefineService.saveMcdefine(dto);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
		
	}
	@RequestMapping(path="/listpage")
	@ResponseBody
	public Page<?> qryMcdefineOfPage(@RequestParam int page,@RequestParam int rows,McdefineDTO dto){
		return mcdefineService.qryMcdefineOfPage(page, rows,dto);
	}
	@RequestMapping(path="/listall")
	@ResponseBody
	public List<?> qryMcdefineAll(){
		return mcdefineService.qryMcdefineAll();
	}
	@RequestMapping(path="/list")
	@ResponseBody
	public List<?> qryMcdefine(){
		return mcdefineService.qryMcdefine();
	}
	@RequestMapping(path="/update")
	@ResponseBody
	public InvokeResult updateMcdefine(@RequestParam Long id,McdefineDTO dto){
		try {
			mcdefineService.updateMcdefine(id, dto);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
	@RequestMapping(path="/delete")
	@ResponseBody
	public InvokeResult deleteMcdefine(@RequestParam String ids){
		try {
			String[] idArr = ids.split(",");
			mcdefineService.deleteMcdefine(idArr);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
}
