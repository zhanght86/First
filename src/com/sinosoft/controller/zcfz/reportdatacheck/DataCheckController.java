package com.sinosoft.controller.zcfz.reportdatacheck;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sinosoft.common.CurrentUser;
import com.sinosoft.common.Dao;
import com.sinosoft.common.InvokeResult;
import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dto.reportdatacheck.CheckRelationDTO;
import com.sinosoft.zcfz.dto.reportdatacheck.DataCheckDto;
import com.sinosoft.entity.CalCfReportInfo;
import com.sinosoft.entity.UserInfo;
import com.sinosoft.zcfz.service.reportdatacheck.DataCheckService;


@Controller
@RequestMapping("/dataCheck")
public class DataCheckController {

	@Resource
	private DataCheckService dataCheckService;
	@Resource
	private Dao dao;
	
	//初步检验
	@ResponseBody
	@RequestMapping(path="/initialCheck")
	public InvokeResult initialCheck(DataCheckDto dataCheckDto) {
		//数据检验
		dataCheckService.initialCheck(dataCheckDto);
	
		//获取检验错误信息
		@SuppressWarnings("unchecked")
		List<DataCheckDto> errorInfoList = (List<DataCheckDto>) dataCheckService.getErrorInfoByCondition(dataCheckDto);
		if( errorInfoList.size() == 0){
			dataCheckService.updateState(dataCheckDto,"3"); //更新报送单的状态
			return InvokeResult.failure("校验完毕：校验无错误数据");
		}
		dataCheckService.updateState(dataCheckDto,"3"); //更新报送单的状态
		return InvokeResult.success(errorInfoList);
	}
	
	
	@ResponseBody
	@RequestMapping(path="/check")
	public InvokeResult dataCheck(DataCheckDto dataCheckDto) {
		//数据检验
		dataCheckService.dataCheck(dataCheckDto);
		
		UserInfo user = CurrentUser.getCurrentUser();
		String reportId=dataCheckDto.getReportId();
		CalCfReportInfo ccri=new CalCfReportInfo();
		ccri=(CalCfReportInfo) dao.get(CalCfReportInfo.class, reportId);
		dataCheckDto.setYear(ccri.getYear());
		dataCheckDto.setQuarter(ccri.getQuarter());
		dataCheckDto.setComCode(ccri.getComCode());
		dataCheckDto.setReporttype(ccri.getReportType());		
		
		try {
			//获取系统存储的风险暴露、RF0、K、最低资本数据---行可扩展的
			Map<String, BigDecimal> alDatas = dataCheckService.GetDatas(reportId);
			//获取系统存储的风险暴露、RF0、K、最低资本数据---固定因子的
			Map<String, BigDecimal> alGDDatas = dataCheckService.GetGDDatas(reportId);			
			//获取在系统计算规则下的风险暴露、RF0、K、最低资本数据---行可扩展的
			Map<String, BigDecimal> rowDatas = dataCheckService.GetRF0Data(reportId, user, "1");
			//获取在系统计算规则下的风险暴露、RF0、K、最低资本数据---固定因子的
			Map<String, BigDecimal> rowGDDatas = dataCheckService.GetRF0GDData(reportId, user, "1");
			String dsql="delete from cfreportdata c where c.outitemcode like ('O%') and c.reportid='1' ";
			dao.excute(dsql);
			//进行数据比对---行可扩展的
			dataCheckService.checkRF0Data(rowDatas,alDatas,dataCheckDto);
			//进行数据比对---固定因子的
			dataCheckService.checkRF0GDData(rowGDDatas,alGDDatas,dataCheckDto);
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		//获取检验错误信息
		@SuppressWarnings("unchecked")
		List<DataCheckDto> errorInfoList = (List<DataCheckDto>) dataCheckService.getErrorInfoByCondition(dataCheckDto);
		dataCheckDto.setCheckedCode("error");
		@SuppressWarnings("unchecked")
		List<DataCheckDto> errorInfoListerror = (List<DataCheckDto>) dataCheckService.getErrorInfoByCondition(dataCheckDto);
		if(errorInfoListerror.size()==0){
			dataCheckService.updateState(dataCheckDto,"5"); //更新报送单的状态
			if(errorInfoList.size()==0){
				return InvokeResult.success();
			}else{
				return InvokeResult.failure("数据校验通过但出现警告！",errorInfoList);
			}
		}else{
			dataCheckService.updateState(dataCheckDto,"4.5"); //更新报送单的状态
			return InvokeResult.failure("数据校验未通过！",errorInfoList);
		}
	}
	@ResponseBody
	@RequestMapping(path="/updateState")
	public InvokeResult updateState(DataCheckDto dataCheckDto) {
		try{
			dataCheckService.updateState(dataCheckDto,"4"); //更新报送单的状态
			return InvokeResult.success("退回状态成功");
		}catch(Exception e){
			return InvokeResult.failure("状态更新错误"+e.getMessage());
		}
	}
	
	@ResponseBody
	@RequestMapping(path="/list")
	public Page<?> getErrorInfo(@RequestParam int page,@RequestParam int rows,DataCheckDto dataCheckDto){
		return dataCheckService.getErrorInfo(page, rows, dataCheckDto);
	}
	
	/*********************************** 校验关系管理  ********************************/
	@RequestMapping(path="/add", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult saveCheckRelaInfo(CheckRelationDTO dto){
		System.out.println("++++" + dto);
		if(!dataCheckService.saveCheckRelaInfo(dto)){
			return InvokeResult.failure("保存校验关系出错");
		}
				
		return InvokeResult.success(); 
	}
	
	@RequestMapping(value="/delete", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult deleteCheckRelaInfo(@RequestParam String ids, @RequestParam String checkSchemaCode){
		System.out.println("/////" + checkSchemaCode);
		String[] idArr = ids.split(",");
		
		if(!dataCheckService.deleteCheckRelaInfo(idArr, checkSchemaCode)){
			return InvokeResult.failure("删除校验关系出错");
		}
				
		return InvokeResult.success(); 
	}
	
	@RequestMapping(path="/update")
	@ResponseBody
	public InvokeResult updateCheckRelaInfo(@RequestParam int id,CheckRelationDTO dto){
		System.out.println("******" + id +" : " + dto.getIsNeedChk() + "tempReport: " + dto.getTempReport() + 
				",quarterReport: " + dto.getQuarterReport() + " quarterFalshReport: " + dto.getQuarterFalshReport());
		if(!dataCheckService.updateCheckRelaInfo(id , dto)){
			return InvokeResult.failure("修改校验关系出错");
		}
				
		return InvokeResult.success(); 
	}
	
	@ResponseBody
	@RequestMapping(path="/relationList")
	public Page<?> getCheckRelationshipInfo(@RequestParam int page,@RequestParam int rows, CheckRelationDTO dto){
		return dataCheckService.getCheckRelationshipInfo(page, rows, dto);
	}
	
}
