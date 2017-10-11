package com.sinosoft.controller.zcfz.reportdatacheck;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sinosoft.common.InvokeResult;
import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dto.reportdatacheck.DataCheckDto;
import com.sinosoft.zcfz.service.reportdatacheck.DataCheckService;
import com.sinosoft.zcfz.service.reportdatacheck.RiskRatingCheckService;

@Controller
@RequestMapping("/RiskRatingCheck")
public class RiskRatingCheckController {
	@Resource
	private RiskRatingCheckService riskRatingCheckService;
	
	@Resource
	private DataCheckService dataCheckService;
	
	
	@ResponseBody
	@RequestMapping(path="/check")
	public InvokeResult dataCheck(DataCheckDto dataCheckDto) {
		
		System.out.println(dataCheckDto.getYear()+",--->"+dataCheckDto.getQuarter());
		//数据检验
		String reportId=dataCheckDto.getReportId();
		String year=reportId.substring(0,4);
		String quarter = reportId.substring(5, 6);
		String reportCateGory = reportId.substring(9,10);
		String comCode = reportId.substring(20);
		dataCheckDto.setComCode(comCode);
		dataCheckDto.setYear(year);
		dataCheckDto.setQuarter(quarter);
	
		riskRatingCheckService.dataCheckOperator(dataCheckDto);
	
		//获取检验错误信息
		@SuppressWarnings("unchecked")
		List<DataCheckDto> errorInfoList = (List<DataCheckDto>) riskRatingCheckService.getErrorInfo(dataCheckDto);
//		dataCheckDto.setCheckedCode("error");
		@SuppressWarnings("unchecked")
		List<DataCheckDto> errorInfoListerror = (List<DataCheckDto>) riskRatingCheckService.getErrorInfo(dataCheckDto);
		if(errorInfoListerror.size()==0){
			if(errorInfoList.size()==0){
				return InvokeResult.success();
			}else{
				return InvokeResult.failure("数据校验通过但出现警告！",errorInfoList);
			}
		}else{
			return InvokeResult.failure("数据校验未通过！",errorInfoList);
		}
	}
	

	@ResponseBody
	@RequestMapping(path="/list")
	public Page<?> getErrorInfo(@RequestParam int page,@RequestParam int rows,DataCheckDto dataCheckDto){
		return riskRatingCheckService.getErrorInfo(page, rows, dataCheckDto);
	}
	
}
