package com.sinosoft.controller.zcfz.reportitemsystem;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sinosoft.common.InvokeResult;
import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dto.reportitemsystem.CfReportRowAddDescDTO;
import com.sinosoft.entity.CfReportRowAddDescId;
import com.sinosoft.zcfz.service.reportitemsystem.CfReportRowAddDescService;

@Controller
@RequestMapping(value="/reportRowAdd")
public class CfReportRowAddDescController {
	@Resource
	private CfReportRowAddDescService cfReportRowAddService;
	@RequestMapping(value="/add", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult saveReportItem(CfReportRowAddDescDTO rid){
		try {
			
			cfReportRowAddService.saveCfReportRowAdd(rid);
			return InvokeResult.success();
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
		
	}
	@RequestMapping(value="/update")
	@ResponseBody
	public InvokeResult updateReportItem(CfReportRowAddDescId cfrid,CfReportRowAddDescDTO rid){
		try {
			if (StringUtils.isNotEmpty(rid.getOutItemNote())
					&& rid.getOutItemNote().length() > 500) {
				return InvokeResult.failure("您输入的指标说明超过了指定的长度，请确认后再次输入");
			}
			if (StringUtils.isNotEmpty(rid.getTableName())
					&& rid.getTableName().length() > 100) {
				return InvokeResult.failure("您输入表名称超过了指定的长度，请确认后再次输入");
			}
			if (StringUtils.isNotEmpty(rid.getColName())
					&& rid.getColName().length() > 100) {
				return InvokeResult.failure("您输入的列名称超过了指定的长度，请确认后再次输入");
			}
			cfReportRowAddService.updateCfReportRowAdd(cfrid, rid);
			return InvokeResult.success();
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
		
	}
	@RequestMapping(path="/listpage")
	@ResponseBody
	public Page<?> qryReportItemOfPage(@RequestParam int page,@RequestParam int rows,CfReportRowAddDescDTO rid){
		return cfReportRowAddService.qryCfReportRowAddOfPage(page, rows, rid);
	}
	@RequestMapping(path="/delete")
	@ResponseBody
	public InvokeResult deleteReportItem(CfReportRowAddDescId cfrid){
		try {
			cfReportRowAddService.deleteCfReportRowAdd(cfrid);
			return InvokeResult.success();
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
	@RequestMapping(path="/find")
	@ResponseBody
	public InvokeResult finReportItem(CfReportRowAddDescId cfrid){
		try {
			return InvokeResult.success(cfReportRowAddService.findReportItem(cfrid));
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
		
	}
}
