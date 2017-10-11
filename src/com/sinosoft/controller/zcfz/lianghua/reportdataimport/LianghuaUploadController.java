package com.sinosoft.controller.zcfz.lianghua.reportdataimport;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.sinosoft.common.CurrentUser;
import com.sinosoft.common.Dao;
import com.sinosoft.common.InvokeResult;
import com.sinosoft.common.interceptor.Token;
import com.sinosoft.entity.BranchInfo;
import com.sinosoft.entity.CalCfReportInfo;
import com.sinosoft.entity.UploadFile;
import com.sinosoft.entity.UserInfo;
import com.sinosoft.util.Config;
import com.sinosoft.util.UploadNumberFormatException;
import com.sinosoft.util.UploaderServlet;
import com.sinosoft.zcfz.dto.reportdataimport.UploadInfoDTO;
import com.sinosoft.zcfz.service.lianghua.LianghuaUploadFileService;
import com.sinosoft.zcfz.service.lianghua.LianghuaUploadService;
import com.sinosoft.zcfz.service.riskdataimport.UploadDecimalsException;

@Controller
@RequestMapping(path="/lianghuaupload")
public class LianghuaUploadController {
		private static final String ALM_CfReportInfo = null;
		@Resource
		private UploaderServlet us;
		@Resource
		private LianghuaUploadService riskus;
		@Resource
		private LianghuaUploadFileService uf;
		@Resource(name = "dao")
		private Dao dao;
		
		@RequestMapping(value="/upload",method=RequestMethod.POST)
		@ResponseBody
		public synchronized Map<String, Object> upload(UploadInfoDTO pld,MultipartFile file,HttpServletRequest request,HttpServletResponse response) throws Exception{
			Map<String , Object> resultMap=new HashMap<String, Object>();//存储返回的信息

			String reportId= pld.getReportId();
			CalCfReportInfo  ccri=new CalCfReportInfo();
			ccri=(CalCfReportInfo) dao.get(CalCfReportInfo.class, reportId);		
			//根据报送号获取相关信息
			pld.setYearmonth(ccri.getYear());
			pld.setQuarter(ccri.getQuarter());
			pld.setReportcategory("1");
			pld.setReporttype(ccri.getReportType());
			pld.setCompanycode(ccri.getComCode());
			
			//报表名称
			BranchInfo branchInfo = new BranchInfo();
			branchInfo = (BranchInfo)dao.get(BranchInfo.class, ccri.getComCode());
			String comType=branchInfo.getComRank();
			/*if(comType.equals("1")){//总公司
				pld.setReportname("1");
			}else{//分公司
				pld.setReportname("5");
			}*/
			System.out.println(pld);
			//判断报送号完毕
			try {
				String deptflag="1";
				System.out.println("开始上传");
				// 文件上传目录
				String newFileType = file.getOriginalFilename().substring(
						file.getOriginalFilename().lastIndexOf("."));
				String newFileName = pld
						.getReportId()
						.concat("_")
						.concat(pld.getQuarter());//.concat("_")
						//	.concat(System.currentTimeMillis() + ""));
				pld.setNewfileName(newFileName.concat(newFileType));// 重命名文件名
				pld.setFilePath(Config.getProperty("uploaddir")+"/LianghuaDataUpload");//文件上传地址
				File uplaodfile = new File(Config.getProperty("uploaddir")
						+ "/LianghuaDataUpload");
				if (!uplaodfile.exists()) {
					uplaodfile.mkdir();
				}
				pld.setRequest(request);
				pld.setMultipartFile(file);
				System.out.println(request);
				//上传文件			
			    us.upload(pld,response);
				//读取Excel数据并存入数据库中
				pld.setDate(us.getDate());
				UserInfo user = CurrentUser.getCurrentUser();
				
				riskus.saveData(pld,user,deptflag);		
				//保存上传文件信息到数据库
				if(us.getFileName()!=null&&!"".equals(us.getFileName())){
					pld.setFileName(file.getOriginalFilename());// 文件上传保存名称
					uf.saveUploadFile(pld);
					us.setFileName(null);
				}
				 
//				   if(data.size()>0){  // 返回精度错误报告
//					   return data;
//				   }
				//return InvokeResult.success();
				resultMap.put("status",true);
			} catch (IOException e) {
				e.printStackTrace();
				// TODO Auto-generated catch block
				resultMap.put("status",false);
				resultMap.put("err","上传失败：原因为："+e.getMessage());
//				return InvokeResult.failure("文件上传失败，原因是："+e.getMessage()+",IO异常。");
			} catch (ServletException e) {
				e.printStackTrace();
				// TODO Auto-generated catch block
				resultMap.put("status",false);
				resultMap.put("err","上传失败：原因为："+e.getMessage());
//				return InvokeResult.failure("文件上传失败，原因是："+e.getMessage());
				
			} catch (NullPointerException e) {
				e.printStackTrace();
				// TODO Auto-generated catch block
				resultMap.put("status",false);
				resultMap.put("err","上传失败：原因为："+e.getMessage());
//				return InvokeResult.failure("文件上传失败，原因是："+e.getMessage()+",请确认上传信息是否正确。");
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
				e.printStackTrace();
				resultMap.put("status",false);
				resultMap.put("err",e.getMessage());
			}
			return resultMap;
		}                       
		@RequestMapping(value="/modedownload",method=RequestMethod.POST)
		@Token
		@ResponseBody
		public InvokeResult modeDownload(UploadInfoDTO pld,HttpServletRequest request,HttpServletResponse response){
			try {
			String param = request.getParameter("param");
			String deptflag = request.getParameter("deptflag");//是否分部门:yes是no否
			UserInfo user = CurrentUser.getCurrentUser();
			String reportId= pld.getReportId();
			
			CalCfReportInfo ccri=new CalCfReportInfo();
			ccri=(CalCfReportInfo) dao.get(CalCfReportInfo.class, reportId);		
			//根据报送号获取相关信息
			pld.setYearmonth(ccri.getYear());
			pld.setQuarter(ccri.getQuarter());
			pld.setReportcategory(ccri.getReportCateGory());
			pld.setReporttype(ccri.getReportType());
			pld.setCompanycode(ccri.getComCode());
			//报表名称
			BranchInfo branchInfo = new BranchInfo();
			branchInfo = (BranchInfo)dao.get(BranchInfo.class, ccri.getComCode());
			String comType=branchInfo.getComRank();
			if("1".equals(comType)){//量化
				pld.setReportname("0");
			}

			//获取部门信息
			 
			String dept = user.getDeptCode();
			if("1-data".equals(param)||"2-data".equals(param)){
				String year = pld.getYearmonth();
				String quarter = pld.getQuarter();
				String reportrate = pld.getReporttype();
				String reporttype = pld.getReportname();
				String companycode = pld.getCompanycode();
				if("".equals(reportrate)||reportrate==null){
					return InvokeResult.failure("报表类别未录入。");
				}
				if("".equals(reporttype)||reporttype==null){
					return InvokeResult.failure("报表名称未录入。");
				}
				if("".equals(year)||year==null){
					return InvokeResult.failure("年度未录入。");
				}
				if("".equals(quarter)||quarter==null){
					return InvokeResult.failure("季度未录入。");
				}
				if("".equals(companycode)||companycode==null){
					return InvokeResult.failure("机构名称未录入。");
				}
			}
			
			    UploadFile u=new UploadFile();
				URL resource = this.getClass().getResource("/");
				String path = resource.toURI().getPath();
				String[] split = path.split("WEB-INF");
				u.setFilePath(split[0].toString()
						+ Config.getProperty("downloaddir"));
				    u.setFileName("QuantitativeAssessmentOfAssetLiabilityManagementOfLifeInsuranceCompanies.xlsx");
			
				String newFileName = "";
				 String downloadPath ="";
				//是分部门
				 if("yes".equals(deptflag)){
						if("all".equals(param)){//完整模板下载(分部门)
							newFileName = newFileName + dept + "_" + u.getFileName();
						}/*else if("1".equals(param)){//法人机构模板下载(分部门)
							newFileName = newFileName + "head_" + dept + "_" + u.getFileName();
						}*/else if("2".equals(param)){//分部门模板下载(分部门)
							newFileName = newFileName + "branch_" + dept + "_" + u.getFileName();
						}else if("1-data".equals(param)){//数据模板下载(分部门)
							newFileName = newFileName + "headdata_" + pld.getCompanycode() + "_" + dept + u.getFileName();
						}else if("2-data".equals(param)){//分支机构数据模板下载(分部门)
							newFileName = newFileName + "branchdata_" + pld.getCompanycode() + "_" + dept + "_" + u.getFileName();
						}
					}else{//不是分部门	
						System.out.println("no----"+param+"no-----"+deptflag+" 不是分部门的");
						if("all".equals(param)){//完整模板下载
							newFileName =  newFileName +""+ u.getFileName();
						}else if("1".equals(param)){//法人机构模板下载
							newFileName = newFileName + "head_" + u.getFileName();
							System.out.println("1======"+newFileName);
						}else if("2".equals(param)){//分支机构模板下载
							newFileName = newFileName + "branch_" + u.getFileName();
						}else if("1-data".equals(param)){//法人机构数据模板下载
							newFileName = newFileName + "headdata_" + pld.getCompanycode() + "_" + u.getFileName();
						}else if("2-data".equals(param)){//分支机构数据模板下载
							newFileName = newFileName + "branchdata_" + pld.getCompanycode() + "_" + u.getFileName();
						}
					/*if("all".equals(param)){//完整模板下载
						 newFileName = "QuantitativeAssessmentOfAssetLiabilityManagementOfLifeInsuranceCompanies.xlsx";
						//newFileName =  newFileName +""+ u.getFileName();
						 downloadPath = Config.getProperty("uploaddir") + Config.getProperty("downloaddir")  + newFileName;
						 System.out.println(downloadPath+"----------------");
					}else{ 
					    newFileName = pld.getReportId().concat("_").concat(pld.getQuarter()+".xlsx");
					    System.out.println(1+newFileName);
					    downloadPath =Config.getProperty("uploaddir")	+ "/LianghuaDataUpload/"+newFileName;
					    System.out.println(2+downloadPath);
					}*/
					
				}
				//服务器路径
				//String serverPath =  request.getSession().getServletContext().getRealPath("/");			
				//String newFilePath = serverPath + Config.getProperty("downloaddir") + u.getFileName();
				String newFilePath = Config.getProperty("uploaddir") + Config.getProperty("downloaddir") + newFileName;
				File srcfile = new File(u.getFilePath() + "" + u.getFileName());
				File destfile = new File(newFilePath);
				if(!destfile.getParentFile().exists()){
					destfile.getParentFile().mkdirs();
				}			
				FileUtils.copyFile(srcfile, destfile, false);
				riskus.downloadPath(pld, dept, pld.getCompanycode(), newFilePath, param, deptflag);
				/*String downloadPath = request.getScheme() + "://" + request.getServerName()
								+ ":" + request.getServerPort()
								+ request.getContextPath()+"/upload"
						    	+ Config.getProperty("downloaddir") + newFileName;*/
					
				return InvokeResult.success(newFilePath);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return InvokeResult.failure("失败。。。");
			}
		}
	

	
}
