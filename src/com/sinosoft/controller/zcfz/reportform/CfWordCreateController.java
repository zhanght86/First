package com.sinosoft.controller.zcfz.reportform;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import com.sinosoft.common.Constant;
import com.sinosoft.common.InvokeResult;
import com.sinosoft.zcfz.dto.reportform.CfWordCreateDTO;
import com.sinosoft.zcfz.service.reportform.CfWordCreateService;
import com.sinosoft.util.Config;
import com.sinosoft.util.FreeMarkerUtil;

@Controller
@RequestMapping(value="/cfwordCreate")
public class CfWordCreateController {
	private final String FREEMARKER_PATH=Constant.UPLOADDIR+"/doc";
	private final String companyType=Constant.COMPANYTYPE;
	@Autowired
	private FreeMarkerConfig freeMarkerConfig;//获取FreemarkerConfig的实例 
	@Resource
	private CfWordCreateService cfWordCreateService;
	@RequestMapping(value="/create", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult CreateWord(CfWordCreateDTO cfdto){
		try {
			if(cfWordCreateService.CreateWord(cfdto)){
				return InvokeResult.success();
			}else{
				return InvokeResult.failure("没有可生成报送文件的数据，请确认季度数据已上传");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
	@RequestMapping(value="/createWordByFreeMarker", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult CreateWordByFreeMarker(CfWordCreateDTO cfdto,HttpServletRequest hsr){
		try {
			String mYear=cfdto.getmYear();//年度
			String quarter=cfdto.getmQuarter();//季度
			String mCfReportType=cfdto.getmCfReportType();//报告类型
			String wordType=cfdto.getWordType();//模板类型
			String fileName ="";
			String model="";
			Map<String, Object> mapParam=cfWordCreateService.CreateWordByFreeMarker(cfdto);
			if("1".equals(mCfReportType)){
				if("0".equals(wordType)){
					fileName=mYear+"-"+quarter+"-"+mCfReportType+"-QuarterQuickReportMode.doc";
					model="QuarterQuickReportMode.ftl";
					/*if("4".equals(quarter)){
						model="QuarterQuickReportMode4.ftl";
					}*/
				}else{  
					fileName=mYear+"-"+quarter+"-"+mCfReportType+"-WQuarterQuickReportMode.doc";
					model="WQuarterQuickReportMode.ftl";
					/*if("4".equals(quarter)){
						model="WQuarterQuickReportMode4.ftl";
					}*/
				}
			}else if("2".equals(mCfReportType)){
				if("0".equals(wordType)){
					fileName=mYear+"-"+quarter+"-"+mCfReportType+"-QuarterReportMode.doc";
					model="QuarterReportMode.ftl";
					/*if("4".equals(quarter)){
						model="QuarterReportMode4.ftl";
					}*/
				}else{
					fileName=mYear+"-"+quarter+"-"+mCfReportType+"-WQuarterReportMode.doc";
					model="WQuarterReportMode.ftl";
					/*if("4".equals(quarter)){
						model="WQuarterReportMode4.ftl";
					}*/
				}
			}
			model=companyType+"_"+model;
			try {
				FreeMarkerUtil.createHtml(freeMarkerConfig, model, hsr, mapParam, FREEMARKER_PATH+"/"+mYear+"-"+quarter+"-"+mCfReportType+"/"+companyType, fileName);//根据模板生成静态页面
			} catch (Exception e) {
				return InvokeResult.failure("生成word失败。。。。");
			}
			return InvokeResult.success();
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
	@RequestMapping(value="/createWordxinxipilu", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult CreateWordxinxipilu(CfWordCreateDTO cfdto,HttpServletRequest hsr){
		try {
			String mYear=cfdto.getmYear();//年度
			String quarter=cfdto.getmQuarter();//季度
			String mCfReportType=cfdto.getmCfReportType();//报告类型
			String wordType=cfdto.getWordType();//模板类型
			String fileName ="";
			String model="";
			Map<String, Object> mapParam=cfWordCreateService.CreateWordByFreeMarker1(cfdto);
			if("1".equals(mCfReportType)){
				if("0".equals(wordType)){
					fileName=mYear+"-"+quarter+"-"+mCfReportType+"-QuarterQuickReportMode.doc";
					model="QuarterQuickReportMode.ftl";
					/*if("4".equals(quarter)){
						model="QuarterQuickReportMode4.ftl";
					}*/
				}else{  
					fileName=mYear+"-"+quarter+"-"+mCfReportType+"-WQuarterQuickReportMode.doc";
					model="WQuarterQuickReportMode.ftl";
					/*if("4".equals(quarter)){
						model="WQuarterQuickReportMode4.ftl";
					}*/
				}
			}else if("2".equals(mCfReportType)){
				if("0".equals(wordType)){
					fileName=mYear+"-"+quarter+"-"+mCfReportType+"-ReportSummaries.doc";
					model="ReportSummaries.ftl";
					/*if("4".equals(quarter)){
						model="ReportSummaries4.ftl";
					}*/
				}else{
					fileName=mYear+"-"+quarter+"-"+mCfReportType+"-WReportSummaries.doc";
					model="WReportSummaries.ftl";
					/*if("4".equals(quarter)){
						model="WReportSummaries4.ftl";
					}*/
				}
			}
			model="ABC"+"_"+model;
			FreeMarkerUtil.createHtml(freeMarkerConfig, model, hsr, mapParam, FREEMARKER_PATH+"/"+mYear+"-"+quarter+"-"+mCfReportType+"/"+"ABC", fileName);//根据模板生成静态页面
			 return InvokeResult.success();
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
	@RequestMapping(value="/downloadxinxipilu", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult downloadWordxinxipilu(CfWordCreateDTO cfdto,HttpServletResponse response,HttpServletRequest request){
		
		
		try {
			String mYear=cfdto.getmYear();//年度
			String quarter=cfdto.getmQuarter();//季度
			String mCfReportType=cfdto.getmCfReportType();//报告类型
			String wordType=cfdto.getWordType();//模板类型
			String name="";
			if("1".equals(mCfReportType)){
				if("0".equals(wordType)){
					name=mYear+"-"+quarter+"-"+mCfReportType+"-QuarterQuickReportMode.doc";
				}else{
					name=mYear+"-"+quarter+"-"+mCfReportType+"-WQuarterQuickReportMode.doc";
				}
			}else if("2".equals(mCfReportType)){
				if("0".equals(wordType)){
					name=mYear+"-"+quarter+"-"+mCfReportType+"-ReportSummaries.doc";
				}else{
					name=mYear+"-"+quarter+"-"+mCfReportType+"-WReportSummaries.doc";
				}
			}
	        //name=companyType+"/"+name;
			String path=FREEMARKER_PATH+"/"+mYear+"-"+quarter+"-"+mCfReportType+"/"+"ABC"+"/"+name;
			File file=new File(path);
			boolean flag=cfWordCreateService.downloadWord(file, response, request);
			//System.out.println(path);
			if(flag){
				String contentType="application/octet-stream";
		        response.setContentType("text/html;charset=UTF-8");  
		        request.setCharacterEncoding("UTF-8");  
		        BufferedInputStream bis = null;  
		        BufferedOutputStream bos = null;  
		        System.out.println(path);
		        long fileLength = new File(path).length();  
		        response.setContentType(contentType);  
		        response.setHeader("Content-disposition", "attachment; filename="+URLEncoder.encode(name,"UTF-8"));  
		        response.setHeader("Content-Length", String.valueOf(fileLength));  
		        bis = new BufferedInputStream(new FileInputStream(path));  
		        bos = new BufferedOutputStream(response.getOutputStream());  
		        byte[] buff = new byte[2048];  
		        int bytesRead;  
		        while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {  
		            bos.write(buff, 0, bytesRead);  
		        }  
		        bis.close();  
		        bos.close();  
		        response.getWriter().write("{\"success\":true}");
				return InvokeResult.success();
			}else{
				return InvokeResult.failure("文件不存在");
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.getWriter().write("{\"success\":false}");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return InvokeResult.failure(e.getMessage());
		}
	}
	@RequestMapping(value="/download", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult downloadWord(CfWordCreateDTO cfdto,HttpServletResponse response,HttpServletRequest request){
		try {
			String reportId=cfdto.getReportId();//获取ID
			String mYear=reportId.substring(0,4);//年度
			String quarter=reportId.substring(5,6);//季度
			String mCfReportType=cfdto.getmCfReportType();//报告类型
			String wordType=cfdto.getWordType();//模板类型
			String name="";
			if("1".equals(mCfReportType)){
				if("0".equals(wordType)){
					name=mYear+"-"+quarter+"-"+mCfReportType+"-QuarterQuickReportMode.doc";
				}else{
					name=mYear+"-"+quarter+"-"+mCfReportType+"-WQuarterQuickReportMode.doc";
				}
			}else if("2".equals(mCfReportType)){
				if("0".equals(wordType)){
					name=mYear+"-"+quarter+"-"+mCfReportType+"-QuarterReportMode.doc";
				}else{
					name=mYear+"-"+quarter+"-"+mCfReportType+"-WQuarterReportMode.doc";
				}
			}
	        //name=companyType+"/"+name;
			String path=FREEMARKER_PATH+"/"+mYear+"-"+quarter+"-"+mCfReportType+"/"+companyType+"/"+name;
			File file=new File(path);
			boolean flag=cfWordCreateService.downloadWord(file, response, request);
			//System.out.println(path);
			if(flag){
				String contentType="application/octet-stream";
		        response.setContentType("text/html;charset=UTF-8");  
		        request.setCharacterEncoding("UTF-8");  
		        BufferedInputStream bis = null;  
		        BufferedOutputStream bos = null;  
		        System.out.println(path);
		        long fileLength = new File(path).length();  
		        response.setContentType(contentType);  
		        response.setHeader("Content-disposition", "attachment; filename="+URLEncoder.encode(name,"UTF-8"));  
		        response.setHeader("Content-Length", String.valueOf(fileLength));  
		        bis = new BufferedInputStream(new FileInputStream(path));  
		        bos = new BufferedOutputStream(response.getOutputStream());  
		        byte[] buff = new byte[2048];  
		        int bytesRead;  
		        while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {  
		            bos.write(buff, 0, bytesRead);  
		        }  
		        bis.close();  
		        bos.close();  
		        //response.getWriter().write("{\"success\":true}");
				return InvokeResult.success();
			}else{
				return InvokeResult.failure("文件不存在");
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				response.getWriter().write("{\"success\":false}");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return InvokeResult.failure(e.getMessage());
		}
	}
	
	
}
