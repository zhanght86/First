package com.sinosoft.controller.zcfz.reportdataimport;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.sinosoft.common.InvokeResult;
import com.sinosoft.common.interceptor.Token;
import com.sinosoft.zcfz.dao.reportdatamamage.CfReportDataDao;
import com.sinosoft.dto.PluploadDTO;
import com.sinosoft.zcfz.dto.reportdataimport.UploadInfoDTO;
import com.sinosoft.entity.CalCfReportInfo;
import com.sinosoft.entity.UploadFile;
import com.sinosoft.zcfz.service.reportdataimport.SaveDataService;
import com.sinosoft.zcfz.service.reportdataimport.UploadFileService;
import com.sinosoft.zcfz.service.reportformcompute.CalCfReportInfoService;
import com.sinosoft.zcfz.service.riskdataimport.UploadDecimalsException;
import com.sinosoft.util.Config;
import com.sinosoft.util.UploadNumberFormatException;
import com.sinosoft.util.UploaderServlet;

@Controller
@RequestMapping(path="/upload")
public class UploadController {
	@Resource
	private UploaderServlet us;
	@Resource
	private UploadFileService uf;
	@Resource
	private SaveDataService sd;
	@Resource
	private CalCfReportInfoService ccis;
	@Resource
	private CfReportDataDao impcfReportDatadao; 
	@RequestMapping(path="/upload",method=RequestMethod.POST)
	public void upload(PluploadDTO pld,HttpServletRequest request,HttpServletResponse response){
		try {
			pld.setRequest(request);
			us.upload(pld,response);
			if(us.getFileName()!=null&&!"".equals(us.getFileName())){
				pld.setFilePath(us.getUploadPath());
				pld.setFileName(us.getFileName());
				pld.setRemark("22222");
				uf.saveUploadFile(pld);
				us.setFileName(null);
			}
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 完整数据上传用
	 * @return
	 */
	@RequestMapping(value="/reportupload",method=RequestMethod.POST)
	@ResponseBody
	public synchronized Map<String, Object> upload(UploadInfoDTO pld,MultipartFile file,HttpServletRequest request,HttpServletResponse response){
		//List<UploadInfoDTO> uploadInfoDTOs = new ArrayList<UploadInfoDTO>();//存储所有错误信息
		Map<String , Object> resultMap=new HashMap<String, Object>();//存储返回的信息
		try {
			System.out.println("开始上传");
			System.out.println("department="+pld.getDepartment());
			String reportId=pld.getReportId();
			String year=request.getParameter("year");
			String quarter=pld.getQuarter();
			System.out.println("reportId"+pld.getReportId() +"//reporttype///"+pld.getReporttype()+"//reportname//"+pld.getReportname());
			String newFileType = file.getOriginalFilename().substring(
					file.getOriginalFilename().lastIndexOf("."));
			String newFileName = pld
					.getReportId()
					.concat("_")
					.concat(pld
							.getQuarter()
							.concat("_")
							.concat(System.currentTimeMillis() + ""));
			pld.setNewfileName(newFileName.concat(newFileType));// 重命名文件名
			pld.setFilePath(Config.getProperty("uploaddir")+"/"+pld.getDepartment()+"dataupload");//文件上传地址
			pld.setRequest(request);
			pld.setMultipartFile(file);
			System.out.println(request);
			//上传文件			
	    	us.upload(pld,response);
//	    	try {
			//读取Excel数据并存入数据库中
		    pld.setYearmonth(year);
		    pld.setQuarter(quarter);
			pld.setDate(us.getDate());
			pld.setReportId(reportId);
			sd.saveData(pld);
			//保存上传文件信息到数据库
			if(us.getFileName()!=null&&!"".equals(us.getFileName())){
				pld.setFileName(file.getOriginalFilename());// 文件上传保存名称
				uf.saveUploadFile(pld);
				us.setFileName(null);

			}
				//return InvokeResult.success();
				resultMap.put("status",true);
			/*} catch (Exception e) {
				//return InvokeResult.failure("文件上传失败");
				resultMap.put("status",false);
				resultMap.put("err","上传失败：原因为："+e.getMessage());
			}*/
		} catch (IOException e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			//return InvokeResult.failure("文件上传失败，原因是："+e.getMessage()+",IO异常。");
			resultMap.put("status",false);
			resultMap.put("err","上传失败：原因为："+e.getMessage());
		} catch (ServletException e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			//return InvokeResult.failure("文件上传失败，原因是："+e.getMessage());
			resultMap.put("status",false);
			resultMap.put("err","上传失败：原因为："+e.getMessage());
		} catch (NullPointerException e) {
			e.printStackTrace();
			// TODO Auto-generated catch block
			//return InvokeResult.failure("文件上传失败，原因是："+e.getMessage()+",请确认上传信息是否正确。");
			resultMap.put("status",false);
			resultMap.put("err","上传失败：原因为："+e.getMessage());
		} catch (UploadNumberFormatException e) {
			e.printStackTrace();
			resultMap.put("status",false);
			resultMap.put("err","上传失败：原因为：数据格式异常！");
			resultMap.put("data", e.objs);
		} catch (UploadDecimalsException e) {
			e.printStackTrace();
			resultMap.put("status",false);
			resultMap.put("err","上传失败：原因为：数据精度有问题！");
			resultMap.put("data", e.objs);
		}catch (Exception e) {
			//return InvokeResult.failure("文件上传失败");
			resultMap.put("status",false);
			resultMap.put("err","上传失败：原因为："+e.getMessage());
		}
		return resultMap;
	}
	@RequestMapping(path="/download")
	public void download(@RequestParam int id,String modelPath,HttpServletRequest request,HttpServletResponse response){
		UploadFile u=uf.findUploadFile(id);
		try {
			us.download(request,response,u);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@RequestMapping(path="/delete")
	public void delete(@RequestParam int id,HttpServletRequest request,HttpServletResponse response){
		try {
			UploadFile u=uf.findUploadFile(id);
			us.delete(request,response,u);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		uf.deleteUploadFile(id);
	}
	
	// 完整模板下载
	@RequestMapping(value="/modedownload",method=RequestMethod.POST)
	@Token
	@ResponseBody
	public InvokeResult modeDownload(UploadInfoDTO pld,HttpServletRequest request,HttpServletResponse response){
		// String reportWork = Constant.REPORTWORK;
		UploadFile u = new UploadFile();
		// u.setFilePath(reportWork + "mould/excel/");
		String reportId=pld.getReportId();
		CalCfReportInfo ccri= ccis.findCalCfReportInfo(reportId);
		String type=ccri.getReportType();
		String name=pld.getReportname();
		if("1".equals(name)){
			if("1".equals(type)){
				u.setFileName("QuarterQuickReportMode.xlsm");				
			}else if("2".equals(type)){
				u.setFileName("QuarterReportMode.xlsm");
			}else if("3".equals(type)){
				u.setFileName("TempReportMode.xlsm");
			}else{
				System.out.println("无法获取要下载excel模板信息。");
				return InvokeResult.failure("无法获取要下载excel模板信息。");
			}
		}else if("2".equals(name)){
			u.setFileName("WordReportMode.xlsm");
		}else if("3".equals(name)){
			if(Config.getProperty("CompanyType").equals("L")){
				u.setFileName("LifeInsuranceCompanyCashflowStressTest.xlsm");				
			}else if (Config.getProperty("CompanyType").equals("P")||Config.getProperty("CompanyType").equals("R")){
				u.setFileName("PropertyAndCasualtyInsuranceCompanyCashflowStressTest.xlsm");
			}else{
				System.out.println("无法获取要下载excel模板信息。");
				return InvokeResult.failure("无法获取要下载excel模板信息。");
			}
			//u.setFileName("LifeInsuranceCompanyCashflowStressTest.xlsm");
		}else{
			System.out.println("无法获取要下载excel模板信息。");
			return InvokeResult.failure("无法获取要下载excel模板信息。");
		}
		try {
			// String serverPath =
			// request.getSession().getServletContext().getRealPath("/");
			URL resource = this.getClass().getResource("/");
			String path = resource.toURI().getPath();
			String[] split = path.split("WEB-INF");
			String newFilePath = split[0].toString() + Config.getProperty("downloaddir") + u.getFileName();
			// File srcfile = new File(u.getFilePath() + "" + u.getFileName());
			// File destfile = new File(newFilePath);
			// if(!destfile.getParentFile().exists()){
			// destfile.getParentFile().mkdirs();
			// }
			// FileUtils.copyFile(srcfile, destfile, false);
			//us.download(request,response,u);
		/*	String downloadPath = request.getScheme() + "://" + request.getServerName()
					+ ":" + request.getServerPort()
					+ request.getContextPath() 
					+ Config.getProperty("downloaddir") + u.getFileName();*/
			return InvokeResult.success(newFilePath);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return InvokeResult.failure("失败。。。");
		}
	}
}
