package com.sinosoft.controller.zcfz.reportform;


import java.io.File;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sinosoft.common.Constant;
import com.sinosoft.common.InvokeResult;
import com.sinosoft.zcfz.dto.reportdatacheck.DataCheckDto;
import com.sinosoft.zcfz.dto.reportform.ReportCreateInfoDTO;
import com.sinosoft.entity.BranchInfo;
import com.sinosoft.entity.CalCfReportInfo;
import com.sinosoft.entity.UploadFile;
import com.sinosoft.service.BranchInfoService;
import com.sinosoft.zcfz.service.reportdatacheck.DataCheckService;
import com.sinosoft.zcfz.service.reportform.ExportXMLService;
import com.sinosoft.zcfz.service.reportform.ExportXbrlService;
import com.sinosoft.util.UploaderServlet;


@Controller
@RequestMapping("/reportcreate")
public class ExportXMLController {
	@Resource
	private ExportXMLService exportxmlService;
	@Resource
	private ExportXbrlService exportxbrlService;
	@Resource
	private UploaderServlet us;
	@Resource
	private DataCheckService dataCheckService;
	@Resource
	private BranchInfoService branchInfoService;
	
	private String organcode=Constant.ORGANCODE;
	
	private String licenseNumber=Constant.LICENSENUMBER;
	
	private String reportWork = Constant.UPLOADDIR;
	
	@RequestMapping(path="/reportcreate", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult reportcreate(ReportCreateInfoDTO info){
		try {			
			CalCfReportInfo calCfReportInfo=exportxbrlService.getReportInfo(info);
			info.setFileType("5");
			info.setYear(calCfReportInfo.getYear());
			info.setQuarter(calCfReportInfo.getQuarter());
			info.setReportType(calCfReportInfo.getReportType());
			info.setReportCateGory(calCfReportInfo.getReportCateGory());
			if("5".equals(info.getFileType())){
				return exportxbrlService.reportCreate(info); 
			}else{
				return exportxmlService.reportCreate(info); 
			}
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure("报送文件生成失败，原因是："+e.getMessage()+"。"); 
		}	
	}
	@RequestMapping(path="/download")
	@ResponseBody
	public InvokeResult download(ReportCreateInfoDTO info,HttpServletRequest request,HttpServletResponse response){	
		UploadFile u=new UploadFile();
		CalCfReportInfo calCfReportInfo=exportxbrlService.getReportInfo(info);
		info.setFileType("5");
		info.setYear(calCfReportInfo.getYear());
		info.setQuarter(calCfReportInfo.getQuarter());
		info.setReportType(calCfReportInfo.getReportType());
		if("1".equals(info.getFileType())){			
			u.setFileName("item.xml");
		}else if("2".equals(info.getFileType())){
			u.setFileName("row.xml");
		}else if("3".equals(info.getFileType())){
			u.setFileName("文件型因子上报.doc");
		}else if("4".equals(info.getFileType())){
			u.setFileName(info.getReportType()+"-"+info.getYear()+"-"+info.getQuarter()+".zip");
		}else if("5".equals(info.getFileType())){
			String reportType="";
			if(info.getReportId().substring(9,10).equals("1")){
				info.setReportCateGory("1");
			}
			if("1".equals(info.getReportType())){
				reportType="2";
			}else if("2".equals(info.getReportType())){
				reportType="1";
			}else{
				reportType=info.getReportType();
			}
			if("1".equals(info.getReportCateGory())){
				reportType="3";
				
			}
			System.out.println(reportType);
			String xbrlName=exportxbrlService.getOrganName(info.getQuarter(), info.getYear(), info.getReportType(), organcode,info.getReportId())+"-"+licenseNumber+"-"+reportType+"-"+exportxbrlService.GetPeriod("instant",info.getYear(),info.getQuarter())[1].replace("-", "");
			//String xbrlName=exportxbrlService.getOrganName(info.getQuarter(), info.getYear(), info.getReportType(), organcode,info.getReportId())+"-"+licenseNumber+"-"+reportType+"-"+info.getReportId().substring(20);
            System.out.println("525xbrl导出名："+info.getReportId().substring(20));
			if(info.getReportId().substring(9,10).equals("1")){
				BranchInfo b=branchInfoService.findBranchInfo(info.getCompany());
				xbrlName=b.getComName()+"-"+b.getLicensenumber()+"-"+reportType+"-"+exportxbrlService.GetPeriod("instant",info.getYear(),info.getQuarter())[1].replace("-", "");
				//xbrlName=b.getComName()+"-"+b.getLicensenumber()+"-"+reportType+"-"+info.getReportId().substring(20);
			}
			u.setFileName(xbrlName+".xml");
		}else{
			return InvokeResult.failure("文件下载失败，原因是：文件类型未输入。");
		}
		//File f = new File(reportWork + "/xbrl/" + info.getReportId() + "/"
		//		+ organcode);
		File f = new File(reportWork + "/xbrl/" + info.getReportId());
		if(!f.exists()){
			return InvokeResult.failure("文件下载失败，原因是：文件未找到。请先生成报送文件");
		}
		//u.setFilePath(reportWork + "/xbrl/" + info.getReportId() + "/"
		//		+ organcode + "/" + u.getFileName());
		u.setFilePath(reportWork + "/xbrl/" + info.getReportId() + "/" + u.getFileName());
		System.out.println("525----名："+info.getReportId());
		System.out.println("下载的路径=="+u.getFilePath());
		try {
			us.download(request,response,u);
			return InvokeResult.success();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return InvokeResult.failure("文件下载失败，原因是："+e.getMessage()+"。"); 
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
}
