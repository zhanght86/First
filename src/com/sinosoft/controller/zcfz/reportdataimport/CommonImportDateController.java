package com.sinosoft.controller.zcfz.reportdataimport;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sinosoft.common.InvokeResult;
import com.sinosoft.common.interceptor.Token;
import com.sinosoft.dao.CodeSelectDao;
import com.sinosoft.zcfz.dto.reportdataimport.UploadInfoDTO;
import com.sinosoft.entity.UploadFile;
import com.sinosoft.zcfz.service.reportdataimport.CommonImportDateService;
import com.sinosoft.zcfz.service.reportdataimport.UploadFileService;
import com.sinosoft.util.Config;

@Controller
@RequestMapping(path = "/commonImportDate")
public class CommonImportDateController {
	private static final Logger LOGGER = LoggerFactory.getLogger(CommonImportDateController.class);
	@Resource
	CommonImportDateService imps;
	@Resource
	private UploadFileService uf;
	@Resource
	private CodeSelectDao codeSeleceDao;
	
	@RequestMapping(path = "/modeldownloaddate", method = RequestMethod.POST)
	@Token
	@ResponseBody
	public InvokeResult downModelFile(HttpServletRequest request,
			HttpServletResponse response, UploadInfoDTO pld) throws IOException {
		
		//UserInfo userinfo=(UserInfo) request.getSession().getAttribute("currentUser");
		
		UploadFile u = new UploadFile();
		String type = pld.getReporttype();
		String name = pld.getReportname();
		if(type=="季度报告"){
			type="2";
		}
		if(type=="临时报告"){
			type="3";
		}
		if(type=="季度快报"){
			type="1";
		}
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
		String year = pld.getYearmonth();
		String quarter = pld.getQuarter();
		String reportId=pld.getReportId();
		
		try {
			//String serverPath =  request.getSession().getServletContext().getRealPath("/");
			URL resource = this.getClass().getResource("/");
			String path = resource.toURI().getPath();
			String[] split = path.split("WEB-INF");
			String newFilePath = Config.getProperty("uploaddir")
					+ Config.getProperty("downloaddir") + reportId + "/"
					+ u.getFileName();
			u.setFilePath(split[0].toString()
					+ Config.getProperty("downloaddir"));
			File srcfile = new File(u.getFilePath() + "" + u.getFileName());
			File destfile = new File(newFilePath);
			if (!destfile.getParentFile().exists()) {
				destfile.getParentFile().mkdirs();
			}
			FileUtils.copyFile(srcfile, destfile, false);
			imps.downloadPath(year, quarter, type, name, newFilePath ,reportId);
			/*String downloadPath = request.getScheme() + "://" + request.getServerName()
					+ ":" + request.getServerPort()
					+ request.getContextPath() 
					+ Config.getProperty("downloaddir") +reportId+"/"+ u.getFileName();*/
			
			return InvokeResult.success(newFilePath);
		} catch (Exception e) {
			LOGGER.error("完成数据模板下载出错", e);
			return InvokeResult.failure("失败。。。");
		}
	}
}
