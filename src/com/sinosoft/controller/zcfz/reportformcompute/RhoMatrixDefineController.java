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
import com.sinosoft.zcfz.dto.reportformcompute.RhoMatrixDefineDTO;
import com.sinosoft.zcfz.service.reportformcompute.RhoMatrixDefineService;

@Controller
@RequestMapping(path="/rhoMatrixDefine")
public class RhoMatrixDefineController {
	@Resource
	private RhoMatrixDefineService rhoMatrixDefineService;
	@RequestMapping(path="/add", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult saveRhoMatrixDefine(RhoMatrixDefineDTO dto){
		try {
			rhoMatrixDefineService.saveRhoMatrixDefine(dto);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
		
	}
	@RequestMapping(path="/listpage")
	@ResponseBody
	public Page<?> qryRhoMatrixDefineOfPage(@RequestParam int page,@RequestParam int rows,RhoMatrixDefineDTO dto){
		return rhoMatrixDefineService.qryRhoMatrixDefineOfPage(page, rows,dto);
	}
	@RequestMapping(path="/listall")
	@ResponseBody
	public List<?> qryRhoMatrixDefineAll(){
		return rhoMatrixDefineService.qryRhoMatrixDefineAll();
	}
	@RequestMapping(path="/list")
	@ResponseBody
	public List<?> qryRhoMatrixDefine(){
		return rhoMatrixDefineService.qryRhoMatrixDefine();
	}
	@RequestMapping(path="/update")
	@ResponseBody
	public InvokeResult updateRhoMatrixDefine(RhoMatrixDefineDTO dto){
		try {
			rhoMatrixDefineService.updateRhoMatrixDefine(dto);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
	@RequestMapping(path="/delete")
	@ResponseBody
	public InvokeResult deleteRhoMatrixDefine(@RequestParam String ids){
		try {
			String[] idArr = ids.split(",");
			rhoMatrixDefineService.deleteRhoMatrixDefine(idArr);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
}
