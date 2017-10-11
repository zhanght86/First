package com.sinosoft.controller.zcfz.reportdatamamage;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sinosoft.common.InvokeResult;
import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dto.reportdatamamage.CfReportDTO;
import com.sinosoft.zcfz.dto.reportdatamamage.ReportHistoryDTO;
import com.sinosoft.entity.CfReportDataId;
import com.sinosoft.zcfz.service.reportdatamamage.CfReportDataService;
import com.sinosoft.util.ExcelUtil;

@Controller
@RequestMapping("/indexCode")
public   class   CfReportDataController{
	@Resource
	private   CfReportDataService   service;
	@RequestMapping(path="/list")
	@ResponseBody
	//  查询
	public Page<?> qryRoleinfo(@RequestParam int page,@RequestParam int rows,CfReportDTO  dto){
		Page p = service.qryIndexInfo(page, rows, dto);
		return service.qryIndexInfo(page,rows,dto);
		
	} //  固定因子修改+添加修改轨迹
	@RequestMapping(path="/update")
	@ResponseBody
	public  InvokeResult  update( CfReportDataId id,CfReportDTO dto){
		
		try {
			service.update(id,dto);
		} catch (Exception e) {
			
			return InvokeResult.failure("修改失败，原因是："+e.getMessage());
		}
		return  InvokeResult.success();
	}
	@RequestMapping(path="/insertTextBlock",method=RequestMethod.POST)
	@ResponseBody
	public  InvokeResult  insertTextBlock(CfReportDTO dto){
		
		try {
			service.insertTextBlock(dto);
		} catch (Exception e) {
			
			return InvokeResult.failure("添加失败，原因是："+e.getMessage());
		}
		return  InvokeResult.success();
	}
	@RequestMapping(path="/getTextBlock")
	@ResponseBody
	public Map getTextBlock(CfReportDTO dto){
		//ModelAndView m=new ModelAndView();
		String textBlock=service.getTextBlock(dto);
		Map m=new HashMap();
		m.put("textBlock", textBlock);
		//m.addObject("textBlock", textBlock);
		//m.setViewName("reportdatamanage/reportTextBlockdata");
		return m;
	}
	@RequestMapping(path="/deleteTextBlock")
	@ResponseBody
	public InvokeResult deleteTextBlock(CfReportDTO dto){
		try {
			service.deleteTextBlock(dto);
		} catch (Exception e) {
			
			return InvokeResult.failure("删除失败，原因是："+e.getMessage());
		}
		return  InvokeResult.success();
	}
	//  查询修改轨迹
	@RequestMapping(path="/history")
	@ResponseBody
	public Page<?> reportHistory(@RequestParam int page,@RequestParam int rows,ReportHistoryDTO  dto){
		 return service.reportHistory(page,rows,dto);
		
	}
	
	@RequestMapping(path="/download",method=RequestMethod.POST)
	public void export(HttpServletRequest request, HttpServletResponse response, String name, String cols, String datas) {
		ExcelUtil eu=new ExcelUtil();
		try{
			eu.export(request, response, name, cols, datas);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	//固定因子数据管理导出
	@RequestMapping(path="/downloadAll",method=RequestMethod.POST)
	public void downloadAll(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String name,
			@RequestParam String queryConditions, @RequestParam String cols) {
		service.downLoad(request, response, name,queryConditions, cols);
	}

}
