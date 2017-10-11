package com.sinosoft.controller.zcfz.riskdataimport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.sinosoft.common.InvokeResult;
import com.sinosoft.common.interceptor.Token;
import com.sinosoft.zcfz.dto.reportdataimport.UploadInfoDTO;
import com.sinosoft.entity.CalCfReportInfo;
import com.sinosoft.entity.UploadFile;
import com.sinosoft.entity.UserInfo;
import com.sinosoft.zcfz.service.impl.reportdataimport.CommonImportServiceImpl;
import com.sinosoft.zcfz.service.reportdataimport.CommonImportService;
import com.sinosoft.zcfz.service.reportdataimport.UploadFileService;
import com.sinosoft.zcfz.service.reportformcompute.CalCfReportInfoService;
import com.sinosoft.zcfz.service.riskdataimport.RiskDataImportService;
import com.sinosoft.util.Config;
import com.sinosoft.util.UploadNumberFormatException;
import com.sinosoft.util.UploaderServlet;

@Controller
@RequestMapping(path = "/RiskDataImport")
public class RiskDataImportController {
	private static final Logger LOGGER = LoggerFactory.getLogger(RiskDataImportController.class);
	@Resource
	UploaderServlet us;
	@Resource
	RiskDataImportService imps;
	@Resource
	private UploadFileService uf;
	@Resource
	private CalCfReportInfoService ccis;
	
	@RequestMapping(path = "/modeldownload", method = RequestMethod.POST)
	@Token
	@ResponseBody
	public InvokeResult downModelFile(HttpServletRequest request,
			HttpServletResponse response, UploadInfoDTO pld) throws IOException {
		
		//UserInfo userinfo=(UserInfo) request.getSession().getAttribute("currentUser");
		String modelType = request.getParameter("modelType");
		System.out.println(modelType);
		UploadFile u = new UploadFile();
		u.setFilePath(Config.getProperty("ReportWork") + "mould/excel/");
		
		String reportId = pld.getReportId();
		CalCfReportInfo ccri= ccis.findCalCfReportInfo(reportId);
		String type=ccri.getReportType();
		String name=pld.getReportname();

		if ("1".equals(name)) {
			if ("1".equals(type)) {
				u.setFileName("QuarterQuickReportMode.xlsm");
			} else if ("2".equals(type)) {
				u.setFileName("QuarterReportMode.xlsm");
			} else if ("3".equals(type)) {
				u.setFileName("TempReportMode.xlsm");
			} else {
				System.out.println("无法获取要下载excel模板信息。");
				return InvokeResult.failure("无法获取要下载excel模板信息。");
			}
		} else if ("2".equals(name)) {
			u.setFileName("WordReportMode.xlsm");
		} else if ("3".equals(name)) {
			if (Config.getProperty("CompanyType").equals("L")) {
				u.setFileName("LifeInsuranceCompanyCashflowStressTest.xlsm");
			} else if (Config.getProperty("CompanyType").equals("P")
					|| Config.getProperty("CompanyType").equals("R")) {
				u.setFileName("PropertyAndCasualtyInsuranceCompanyCashflowStressTest.xlsm");
			} else {
				System.out.println("无法获取要下载excel模板信息。");
				return InvokeResult.failure("无法获取要下载excel模板信息。");
			}

		} else {
			System.out.println("无法获取要下载excel模板信息。");
			return InvokeResult.failure("无法获取要下载excel模板信息。");
		}
		
		String year = ccri.getYear();
		String quarter = ccri.getQuarter();
		String deptno = pld.getDepartment();
		String deptName = "";
		
		if(deptno.equals("01")){
			deptName = "finance";
		}else if(deptno.equals("02")){
			deptName = "invest";
		}else if(deptno.equals("03")){
			deptName = "actuary";
		}else if(deptno.equals("04")){
			deptName = "resources";
		}
		/*switch (pld.getDepartment()) {
		case "01":
			deptName = "finance";
			break;
		case "02":
			deptName = "invest";
			break;
		case "03":
			deptName = "actuary";
			break;
		}*/
		
		try {
			String newFileName = deptName + "_" + u.getFileName();
			//服务器路径
			String serverPath =  request.getSession().getServletContext().getRealPath("/");
			//*******上行代码有问题由下行代码替换**********
			//String serverPath = request.getServletContext().getRealPath("/");
			
			String newFilePath = serverPath + Config.getProperty("downloaddir") + reportId+"/"+newFileName;
			//String newFilePath = Config.getProperty("downloaddir") + newFileName;
			File srcfile = new File(u.getFilePath() + "" + u.getFileName());
			File destfile = new File(newFilePath);
			if(!destfile.getParentFile().exists()){
				destfile.getParentFile().mkdirs();
			}
			
			FileUtils.copyFile(srcfile, destfile, false);
			/*u.setFilePath(Config.getProperty("downloaddir"));
			u.setFileName(newFileName);*/
			
			//if(!year.equals("") && !quarter.equals("") && !deptno.equals("")){
			imps.downloadPath(year, quarter, type, name, deptno, newFilePath, modelType,reportId );
			//}
			//us.download(request,response,u);
			
			String downloadPath = request.getScheme() + "://" + request.getServerName()
					+ ":" + request.getServerPort()
					+ request.getContextPath() 
					+ Config.getProperty("downloaddir") + reportId+"/"+newFileName;
			
			return InvokeResult.success(downloadPath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOGGER.error("分部门数据模板下载出错", e);
			return InvokeResult.failure("失败。。。");
		}
	}
	
	@RequestMapping(value="/upload", method=RequestMethod.POST)
	@ResponseBody
	public void reportupload(UploadInfoDTO pld,MultipartFile file,HttpServletRequest request,HttpServletResponse response){
		//UserInfo u=(UserInfo) request.getSession().getAttribute("currentUser");
		List<UploadInfoDTO> uploadInfoDTOs = new ArrayList<UploadInfoDTO>();//存储所有错误信息
		try {
			System.out.println("开始上传");
			System.out.println("department="+pld.getDepartment());
			String reportId=pld.getReportId();
			System.out.println(reportId+"====================");
			pld.setFilePath(Config.getProperty("uploaddir")+"/"+pld.getDepartment()+"dataupload");//文件上传地址
			pld.setRequest(request);
			pld.setMultipartFile(file);
			System.out.println(request);
			//上传文件			
		    us.upload(pld,response);
			//读取Excel数据并存入数据库中
		    String year=request.getParameter("year");
		    pld.setYearmonth(year);
			pld.setDate(us.getDate());
			pld.setReportId(reportId);
			//imps.saveData(pld);		
			//保存上传文件信息到数据库
			if(us.getFileName()!=null&&!"".equals(us.getFileName())){
				pld.setNewfileName(us.getFileName());//文件上传保存名称
				uf.saveUploadFile(pld);
				us.setFileName(null);
			}
			response.getWriter().write("{\"status\":true,\"newName\":\""+us.getFileName()+"\"}");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			try {
				response.getWriter().write("{\"status\":false}");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		} 
	}
	
}
