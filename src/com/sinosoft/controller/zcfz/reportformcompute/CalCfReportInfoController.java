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
import com.sinosoft.zcfz.dto.reportformcompute.CalCfReportInfoDTO;
import com.sinosoft.zcfz.service.reportformcompute.CalCfReportInfoService;

@Controller
@RequestMapping(path="/calCfReportInfo")
public class CalCfReportInfoController {
	@Resource
	private CalCfReportInfoService calCfReportInfoService;
	@RequestMapping(path="/add", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult saveCalCfReportInfo(CalCfReportInfoDTO dto){
		try {
			boolean flag=calCfReportInfoService.saveCalCfReportInfo(dto);//false表示数据库中没有所填的id，true表示数据库中存在id
			if(!flag){
			return InvokeResult.success(); 
			}else{
			return InvokeResult.failure("报送号已经存在！！！"); 
			}
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
		
	}
	@RequestMapping(path="/listpage")
	@ResponseBody
	public Page<?> qryCalCfReportInfoOfPage(@RequestParam int page,@RequestParam int rows,CalCfReportInfoDTO dto){
		return calCfReportInfoService.qryCalCfReportInfoOfPage(page, rows,dto);
	}
	/**
	 * 财务，投资上传时，报送号选取汇总计算之前的
	 * @param page
	 * @param rows
	 * @param dto
	 * @return
	 */
	@RequestMapping(path="/listpage1")
	@ResponseBody
	public Page<?> qryCalCfReportInfoOfPage1(@RequestParam int page,@RequestParam int rows,CalCfReportInfoDTO dto){
		return calCfReportInfoService.qryCalCfReportInfoOfPage1(page, rows,dto);
	}
	
	@RequestMapping(path="/listpagehasparam")
	@ResponseBody
	public Page<?> qryCalCfReportInfoOfPageByState(@RequestParam int page,@RequestParam int rows,CalCfReportInfoDTO dto,@RequestParam String type){
		return calCfReportInfoService.qryCalCfReportInfoOfPageByState(page, rows,dto,type);
	}
	//只查询季度报告类型的报送单号
	@RequestMapping(path="/listpagebytype")
	@ResponseBody
	public Page<?> qryCalCfReportInfoOfPageByType(@RequestParam int page,@RequestParam int rows,CalCfReportInfoDTO dto){
		return calCfReportInfoService.qryCalCfReportInfoOfPageByType(page, rows,dto);
	}
	
	@RequestMapping(path="/listall")
	@ResponseBody
	public List<?> qryCalCfReportInfoAll(){
		return calCfReportInfoService.qryCalCfReportInfoAll();
	}
	@RequestMapping(path="/list")
	@ResponseBody
	public List<?> qryCalCfReportInfo(){
		return calCfReportInfoService.qryCalCfReportInfo();
	}
	@RequestMapping(path="/update")
	@ResponseBody
	public InvokeResult updateCalCfReportInfo(@RequestParam String id,CalCfReportInfoDTO dto){
		try {
			calCfReportInfoService.updateCalCfReportInfo(id, dto);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
	@RequestMapping(path="/delete")
	@ResponseBody
	public InvokeResult deleteCalCfReportInfo(@RequestParam String ids){
		try {
			String[] idArr = ids.split(",");
			calCfReportInfoService.deleteCalCfReportInfo(idArr);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
}
