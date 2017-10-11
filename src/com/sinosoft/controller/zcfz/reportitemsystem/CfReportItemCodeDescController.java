package com.sinosoft.controller.zcfz.reportitemsystem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.ConcurrentUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sinosoft.common.CurrentUser;
import com.sinosoft.common.InvokeResult;
import com.sinosoft.common.Page;
import com.sinosoft.dao.UserInfoDao;
import com.sinosoft.zcfz.dao.reportitemsystem.CfReportItemCodeDescDao;
import com.sinosoft.dto.UserinfoDTO;
import com.sinosoft.zcfz.dto.reportform.CfWordCreateDTO;
import com.sinosoft.zcfz.dto.reportitemsystem.CfReportItemCodeDescDTO;
import com.sinosoft.entity.UserInfo;
import com.sinosoft.zcfz.service.reportitemsystem.CfReportItemCodeDescService;

import antlr.collections.List;

@Controller
@RequestMapping(value="/reportItem")
public class CfReportItemCodeDescController {
	@Resource
	private CfReportItemCodeDescService reportItemService;
	@Resource
	private CfReportItemCodeDescDao reportItemDao;
	@RequestMapping(value="/add", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult saveReportItem(CfReportItemCodeDescDTO rid){
		try {
			reportItemService.saveReportItem(rid);
			return InvokeResult.success();
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
		
	}
	@RequestMapping(value="/update")
	@ResponseBody
	public InvokeResult updateReportItem(@RequestParam String id,CfReportItemCodeDescDTO rid){
		try {
			if (StringUtils.isNotEmpty(rid.getOutItemNote())
					&& rid.getOutItemNote().length() > 500) {
				return InvokeResult.failure("您输入的指标说明超过了指定的长度，请确认后再次输入");
			}
			if (StringUtils.isNotEmpty(rid.getOutItemVersion())
					&& rid.getOutItemVersion().length() > 1) {
				return InvokeResult.failure("您输入的版本超过了指定的长度，请确认后再次输入");
			}
			if (StringUtils.isNotEmpty(rid.getReportCode())
					&& rid.getReportCode().length() > 10) {
				return InvokeResult.failure("您输入的报表代码超过了指定的长度，请确认后再次输入");
			}
			if (StringUtils.isNotEmpty(rid.getReportItemName())
					&& rid.getReportItemName().length() > 100) {
				return InvokeResult.failure("您输入的指标名称超过了指定的长度，请确认后再次输入");
			}
			reportItemService.updateReportItem(id, rid);
			return InvokeResult.success();
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
		
	}
	@RequestMapping(path="/listpage")
	@ResponseBody
	public Page<?> qryReportItemOfPage(@RequestParam int page,@RequestParam int rows,CfReportItemCodeDescDTO rid){
		return reportItemService.qryReportItemOfPage(page, rows,rid);
	}
	@RequestMapping(path="/listalltextblock")
	@ResponseBody
	public Page<?> qryReportItemTextBlockOfPage(@RequestParam int page,@RequestParam int rows,@RequestParam String reportId,@RequestParam String department){
		return reportItemService.qryReportItemTextBlockOfPage(page, rows,reportId,department);
	}
	@RequestMapping(path="/delete")
	@ResponseBody
	public InvokeResult deleteReportItem(@RequestParam String id){
		try {
			reportItemService.deleteReportItem(id);
			return InvokeResult.success();
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
	@RequestMapping(path="/find")
	@ResponseBody
	public InvokeResult finReportItem(@RequestParam String reportItemCode){
		try {
			return InvokeResult.success(reportItemService.findReportItem(reportItemCode));
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
	//文本块的复制
	@RequestMapping(path="/wordCopy")
	@ResponseBody
	public InvokeResult wordCopy(CfWordCreateDTO cfdto){		
	  try{
		String mYear=cfdto.getmYear();//年度
		String mquarter=cfdto.getmQuarter();//季度
		String reportid=cfdto.getReportId();//报送号
		String operator=CurrentUser.getCurrentUser().getUserCode();
		System.out.println("当前登录用户"+operator);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String operdate=df.format(new Date());
        System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
		int month=0;
		int quarter = Integer.valueOf(mquarter).intValue()-1;
		if(quarter==0){
			month = Integer.valueOf(mYear).intValue()-1;
			quarter=4;
		}else {
			month=Integer.valueOf(mYear).intValue();
			quarter=quarter;
		}
		int q=Integer.valueOf(mquarter).intValue();
		//查找当前年季度下的文本块中是否有数据
		String sql1="select * from CfReportData c where c.month='"+month+"' and c.quarter='"+q+"'";
		java.util.List<?>  list= reportItemDao.queryBysql(sql1);
		if(list.isEmpty()){
			String sql="insert into cfreportdata c	(OUTITEMCODE,COMCODE,OUTITEMTYPE,OUTITEMCODENAME,REPORTITEMCODE,REPORTITEMVALUE,REPORTITEMWANVALUE,TEXTVALUE,DESTEXT"+
					",FILETEXT,MONTH,QUARTER,SOURCE,REPORTTYPE,REPORTRATE,COMPUTELEVEL,REPORTITEMVALUESOURCE,OPERATOR,OPERDATE,TEMP,REPORTID,ISUSED)"+
					"(select OUTITEMCODE,COMCODE,OUTITEMTYPE,OUTITEMCODENAME,REPORTITEMCODE,REPORTITEMVALUE,REPORTITEMWANVALUE,TEXTVALUE,DESTEXT"+
					",FILETEXT,'"+mYear+"'"+","+"'"+mquarter+"',SOURCE,REPORTTYPE,REPORTRATE,COMPUTELEVEL,REPORTITEMVALUESOURCE,'"+
					operator+"'"+","+"'"+operdate+"',TEMP,"+"'"+reportid+"'"+",ISUSED "+
					"from cfreportdata where month="+"'"+month+"'"+" and quarter="+"'"+quarter+"'"+"and  (outitemcode like '"+3110200+
					"%'or outitemcode like '"+3110300+"%' or outitemcode like '"+3110412+"%' or outitemcode like '"+3110500+
					"%' or outitemcode like '"+3110600+"%' or outitemcode like '"+3110700+"%' or outitemcode like '"+3110800+
					"%' or outitemcode like '"+3110900+"%' or outitemcode like '"+3111000+"%' or outitemcode like '"+3111100+"%'))";
            reportItemDao.excute(sql);
		}else{
			return InvokeResult.failure("文本快中已存在数据");
		}
		
	  } catch (Exception e) {
		e.printStackTrace();
		return InvokeResult.failure(e.getMessage());
	  }
	return InvokeResult.success();
	}
}
