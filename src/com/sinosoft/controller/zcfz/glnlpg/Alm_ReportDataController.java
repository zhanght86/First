package com.sinosoft.controller.zcfz.glnlpg;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.sinosoft.common.InvokeResult;
import com.sinosoft.common.Page;
import com.sinosoft.entity.UploadFile;
import com.sinosoft.entity.UserInfo;
import com.sinosoft.util.Config;
import com.sinosoft.zcfz.dto.glnlpg.DataImportDTO;
import com.sinosoft.zcfz.dto.glnlpg.Alm_ReportDataDTO;
import com.sinosoft.zcfz.service.glnlpg.Alm_ReportDataService;

@Controller
@RequestMapping(path="/alm_reportdata")
public class Alm_ReportDataController {
	private static final Logger LOGGER = LoggerFactory.getLogger(Alm_ReportDataController.class);
	@Resource
	private Alm_ReportDataService evaluateInfoService;
	/**
	 * 数据录入页面该部门需要填写的数据展示
	 * @return
	 */
	@RequestMapping(path="/data")
	@ResponseBody
	public List<?> queryDataByDeptNo(){
		return evaluateInfoService.queryDataByDeptNo();
	}
	/**
	 * 条目结果保存；
	 * 根据选择的符合程度计算出相应的得分
	 * @param sev
	 * @return
	 */
	@RequestMapping(path="/edit")
	@ResponseBody
	public InvokeResult edit(Alm_ReportDataDTO sev){
		//System.out.print("1111");
		evaluateInfoService.edit(sev);
		return InvokeResult.success();
	}
	/**
	 * 数据填写提交(目前没有审批流程后期可以添加)
	 * @param ids
	 * @return
	 */
	@RequestMapping(path="/submit")
	@ResponseBody
	public InvokeResult submit(@RequestParam String ids){
		System.out.println(ids);
		String[] idArr = ids.split(",");
		evaluateInfoService.submit(idArr);
		return InvokeResult.success(); 
	}
	
	
	@RequestMapping(path="/listpage")
	@ResponseBody
	public List<?> qrySev_ReportDataOfPage(){
		return evaluateInfoService.qrySev_ReportDataDaoOfPage();
	}
	/**
	 * 查询评估文件查询
	 * @param reportId
	 * @param itemCode
	 * @param number
	 * @return
	 */
	@RequestMapping(path="/file")
	@ResponseBody
	public List<?> file(String reportId,String itemCode,String number){
		System.out.println(reportId);
		System.out.println(itemCode);
		System.out.println(number);
		return evaluateInfoService.file(reportId,itemCode,number);
	}
	/**
	 * 评估文件下载
	 * @param id
	 * @param request
	 * @param response
	 */
	@RequestMapping(path = "/downloadURD")
	@ResponseBody
	public void downloadURD(@RequestParam long id, HttpServletRequest request,
			HttpServletResponse response) {
		// 查询要下载的文件的基本信息
		try {
			evaluateInfoService.downloadURD(id,request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@RequestMapping(path = "/downloadHTTP")
	@ResponseBody
	public String downloadHTTP(@RequestParam long id, HttpServletRequest request,HttpServletResponse response) {
		String str= evaluateInfoService.downloadHTTP(id,request,response);
		System.out.println(str);
		return str;
	}
	@RequestMapping(path="/send")
	@ResponseBody
	public InvokeResult send(){
		evaluateInfoService.send();
		return InvokeResult.success(); 
	}
	@RequestMapping(value="/cut", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult cut(@RequestParam String ids){
		System.out.println(ids);
		String[] idArr = ids.split(",");
		evaluateInfoService.cut(idArr);
		return InvokeResult.success(); 
	}
	@RequestMapping(path="/deptHistory")
	@ResponseBody
	public List<?> deptHistory(String deptNo,String reportId){
		System.out.println(reportId);
		System.out.println(deptNo);
		return evaluateInfoService.deptHistory(deptNo,reportId);
	}
	@RequestMapping(path="/listpageall")
	@ResponseBody
	public Page<?> qrySev_ReportDatanoallOfPage(@RequestParam int page,@RequestParam int rows,Alm_ReportDataDTO dto,@RequestParam String dept){
		return evaluateInfoService.qrySev_ReportDataDaoAllOfPage(page,rows,dto,dept);
	}
	@RequestMapping(value="/tongguo", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult tongguo(@RequestParam String ids){
		System.out.println(ids);
		String[] idArr = ids.split(",");
		evaluateInfoService.tongguo(idArr);
		return InvokeResult.success(); 
	}
	@RequestMapping(path="/fanhuixiugai")
	@ResponseBody
	public InvokeResult fanhuixiugai(Alm_ReportDataDTO srd,@RequestParam String reportId,@RequestParam String itemCode,@RequestParam String deptNo,@RequestParam String sevStatus){
		//为什么按照String型传参数 传后来就会多一个,出来 而int型不会出现加,的问题？？？？ 没有解决这个问题
		String[] id1 = reportId.split(",");
		String[] id2 = itemCode.split(",");
		String[] id3 = deptNo.split(",");
		String[] id4 = sevStatus.split(",");
		//System.out.println(srd.getInstanceId());
		//System.out.println(srd.getReportId()+"#"+srd.getItemCode()+"#"+srd.getDeptNo());
		//System.out.println(reportId+"###"+itemCode+"###"+deptNo);
		//System.out.println(id1[0]+"###"+id2[0]+"###"+id3[0]);
		evaluateInfoService.fanhuixiugai(srd,id1[0],id2[0],id3[0],id4[0]);
		return InvokeResult.success(); 
	}
	@RequestMapping(value="/jisuanpanduan", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult panduan(@RequestParam String reportId){
		System.out.println(reportId);
		evaluateInfoService.panduan(reportId);
		return InvokeResult.success(); 
	}
	@RequestMapping(path="/download")
	public void download(HttpServletRequest request, HttpServletResponse response,String deptCode){
		//System.out.println(deptCode);
		String str=deptCode.substring(0, 5);
		//System.out.println(str);
		evaluateInfoService.download(request,response,str);
	}
	//查看工作流
	@RequestMapping(path="/viewEvaluateEventProcess")
	@ResponseBody
	public InvokeResult viewLeaveProcessImage(@RequestParam String instanceId,HttpServletResponse response){
		try {
			evaluateInfoService.traceImage(instanceId, response);
			return InvokeResult.success(); 
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
	@RequestMapping(path="/findEvaluateEventComment")
	@ResponseBody
	public List<?> findEvaluateEventComment(@RequestParam String instanceId){
		System.out.println(instanceId);
		try {
			//System.out.println("++++++++++++++++++++++++++++++++++++++++++++++");
			List<?> result = null;
			//Sev_ReportData srd = evaluateInfoService.findEvaluateEventStore(reportId,itemCode,deptNo);
			result=evaluateInfoService.findEvaluateEventComment(instanceId);
			return result; 
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	/***
	 * 多文件上传
	 */
	@RequestMapping(path = "/multiupload", method = RequestMethod.POST)
	public synchronized void multiFileUpload(DataImportDTO dto, HttpServletRequest request,
			HttpServletResponse response) {

		try {
			request.setCharacterEncoding("UTF-8"); // 解决中文乱码问题
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		String itemCode = request.getParameter("itemCode");
		String reportId = request.getParameter("reportId");
		String deptNo = request.getParameter("deptNo");
		String number = request.getParameter("number");

		System.out.println(itemCode+ "-->" +reportId + "-->" +deptNo + "-->" +number);
		
		dto.setRequest(request);
		// 上传文件目录
		String serverPath =  request.getSession().getServletContext().getRealPath("/");
		System.out.println(serverPath);
		String uploaddir =serverPath + Config.getProperty("descripfile");
		System.out.println(uploaddir);
		File file = new File(uploaddir);
		//判断是否存在文件夹 如果不存在 创建文件夹
		if (!file.exists()) {
			file.mkdir();
		}
		
		response.setCharacterEncoding("UTF-8");
		String name = null;// 文件名
		String oname = null;
		try {
			DefaultMultipartHttpServletRequest dmhsr = (DefaultMultipartHttpServletRequest) dto.getRequest();
			MultiValueMap<String, MultipartFile> map = dmhsr.getMultiFileMap();
			String newFileName = null;
			Iterator<String> iter = map.keySet().iterator();
			while (iter.hasNext()) {
				String str = (String) iter.next();
				List<MultipartFile> fileList = map.get(str);
				for (MultipartFile multipartFile : fileList) {
					name = multipartFile.getOriginalFilename();
					oname = multipartFile.getName();
					//String baseName = name.substring(0, name.lastIndexOf("."));
					System.out.println(name); //上传文件名
					//System.out.println(FilenameUtils.getBaseName(name));
					//System.out.println(URLEncoder.encode(baseName, "utf-8"));
					// 文件重命名
					newFileName = System.currentTimeMillis()+ "_".concat(name);
					dto.setNewfileName(newFileName);
					System.out.println(newFileName);
				    //获取上传文件地址
				    String fileName = multipartFile.getOriginalFilename();
				    System.out.println("fileName=" + fileName);
				    dto.setFileName(fileName);
				    UserInfo u = (UserInfo) request.getSession().getAttribute("currentUser");
					if (oname != null) {
						String nFname = newFileName;
						File savedFile = new File(uploaddir, nFname);
						multipartFile.transferTo(savedFile); // 保存文件

						// 设置dto参数
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						dto.setNewfileName(nFname);
						dto.setFilePath(uploaddir);
						dto.setDate(sdf.format(new Date()));
						dto.setUploadTime(sdf.format(new Date()));
						dto.setFacYear(reportId.substring(0,4));
						dto.setDeptNo(u.getDeptCode());
						dto.setComCode(u.getComCode());
						dto.setUerId(u.getId());
						dto.setUploadUser(u.getUserCode());
						// 保存文件上传信息
						//riskDataFileService.saveUploadFile(dto);
					}
					//保存上传文件
					evaluateInfoService.save(dto,reportId,itemCode,number);
				}
			}
			response.getWriter().write("{\"status\":true,\"newName\":\"" + newFileName
							+ "\"}");				
			} catch (Exception e) {
				LOGGER.error("上传出错：" + e.getMessage());
				try {
					response.getWriter().write("{\"status\":false,\"errMessage\":\""+ "上传失败-->："+e.getMessage()+ "\"}");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			} 
	}
}
