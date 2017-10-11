package com.sinosoft.controller.zcfz.reportdatamamage;

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
import com.sinosoft.zcfz.dto.reportdatamamage.RowAddDTO;
import com.sinosoft.entity.CfReportRowAddDataId;
import com.sinosoft.zcfz.service.reportdatamamage.RowAddDataService;

@Controller
@RequestMapping("/RowAdd")
public class RowAddDataController {
	@Resource
	private    RowAddDataService   rowadddataservice;
   @RequestMapping(path="/list")
   @ResponseBody
   //  查询
   public  Page<?> qryRoleinfo(@RequestParam int page,@RequestParam int rows,RowAddDTO dto){
	   return rowadddataservice.qryIndexInfo(page, rows, dto);
   }
   //固定因子扩张修改+修改轨迹添加
   @RequestMapping(path="/update")
   @ResponseBody
   public  InvokeResult  update(CfReportRowAddDataId id,RowAddDTO dto){
	   rowadddataservice.update(id,dto);
	  return InvokeResult.success();
   }
   
// 查询修改轨迹
	@RequestMapping(path="/history")
	@ResponseBody
	public Page<?> reportHistory(@RequestParam int page,@RequestParam int rows, RowAddDTO dto){
		 return rowadddataservice.rowAddHistory(page,rows,dto);		
	}
	//行可扩展数据管理导出
	@RequestMapping(path="/downloadLine",method=RequestMethod.POST)
	public void downloadLine(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String name,
			@RequestParam String queryConditions, @RequestParam String cols) {
		rowadddataservice.downloadLine(request, response, name,queryConditions, cols);
	}
	
	
}
