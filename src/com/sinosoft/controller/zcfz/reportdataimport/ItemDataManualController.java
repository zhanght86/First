package com.sinosoft.controller.zcfz.reportdataimport;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sinosoft.common.InvokeResult;
import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dto.reportdataimport.ItemDataManualDTO;
import com.sinosoft.entity.CfReportDataId;
import com.sinosoft.zcfz.service.reportdataimport.ItemDataManualService;

@Controller
@RequestMapping(path="/itemdatamanual")
public class ItemDataManualController {
	 @Resource
     private     ItemDataManualService     itemservice;
	@RequestMapping(path="/list")
	@ResponseBody
     public  Page<?>  search(@RequestParam int  page,@RequestParam int rows,ItemDataManualDTO dto){
    	 return  itemservice.search(page,rows,dto);
     }
	@RequestMapping(path="/add")
	@ResponseBody
	public  InvokeResult  add(ItemDataManualDTO dto){
		try {
			itemservice.add(dto); 
		} catch (Exception e) {
			InvokeResult.failure("失败原因："+e.getMessage());
		}
		return  InvokeResult.success();
	}
	
	@RequestMapping(path="/editor")
	@ResponseBody
	public   InvokeResult    editor(CfReportDataId id,ItemDataManualDTO dto){
		try {
			itemservice.eidtor(id, dto);
		} catch (Exception e) {
			InvokeResult.failure("失败原因："+e.getMessage());
		}
		return  InvokeResult.success();
	}
}
