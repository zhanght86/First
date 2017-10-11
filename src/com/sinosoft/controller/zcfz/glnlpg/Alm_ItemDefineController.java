package com.sinosoft.controller.zcfz.glnlpg;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.sinosoft.common.InvokeResult;
import com.sinosoft.common.Page;
import com.sinosoft.entity.UserInfo;
import com.sinosoft.util.Config;
import com.sinosoft.zcfz.dto.glnlpg.DataImportDTO;
import com.sinosoft.zcfz.dto.glnlpg.Alm_ItemDefineDTO;
import com.sinosoft.zcfz.dto.reportformcompute.CalCfReportInfoDTO;
import com.sinosoft.zcfz.service.glnlpg.Alm_ItemDefineService;

@Controller
@RequestMapping(path="/alm_itemdefine")
public class Alm_ItemDefineController {
	
	@Resource
	private Alm_ItemDefineService sev_ItemDefineService;
	
	/**
	 * 规则管理数据查询
	 * @param sid
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(path="/data")
	@ResponseBody
	public Page<?> qrySev_ItemDefineOfPage(Alm_ItemDefineDTO sid,@RequestParam int page,@RequestParam int rows){
		return sev_ItemDefineService.qrySev_ItemDefineOfPage(sid,page, rows);
	}
	/**
	 * 编辑规则管理，可以修改条目的所属部门以每个部门的权重
	 * 两个判断
	 * 1:判断部门个数跟权重个数是否相同
	 * 2:权重和是否为1
	 * @param sid
	 * @return
	 */
	@RequestMapping(path="/editManage")
	@ResponseBody
	public InvokeResult updateManage(Alm_ItemDefineDTO sid){
		try{
			sev_ItemDefineService.updateManage(sid);	
			return InvokeResult.success();
		}catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
	/**
	 * 单号新增
	 * 创建新的单号，并设置单号状态
	 * @param dto
	 * @return
	 */
	@RequestMapping(path="/add", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult saveCalCfReportInfo(CalCfReportInfoDTO dto){
		try {
			boolean flag=sev_ItemDefineService.saveCalCfReportInfo(dto);//false表示数据库中没有所填的id，true表示数据库中存在id
			if(!flag){
			return InvokeResult.success(); 
			}else{
			return InvokeResult.failure("报送号已经存在！！！"); 
			}
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
		
	}
	@RequestMapping(path="/listAll")
	@ResponseBody
	public Page<?> listAll(@RequestParam int page,@RequestParam int rows){
		return sev_ItemDefineService.listAll(page,rows);
	}
	/**
	 * 需求发送
	 * 把规则管理内设置的条目按照部门拆分，存放在数据表内进行数据录入
	 * @param sid
	 * @return
	 */
	@RequestMapping(path="/send")
	@ResponseBody
	public InvokeResult send(CalCfReportInfoDTO sid){
		try {
			sev_ItemDefineService.send(sid);
			return InvokeResult.success(); 
		}catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
	@RequestMapping(path="/get")
	@ResponseBody
	public String get(){
		return sev_ItemDefineService.get(); 
	}
	@RequestMapping(path="/fileList")
	@ResponseBody
	public List<?> fileList(){
		return sev_ItemDefineService.fileList(); 
	}
	@RequestMapping(path = "/downloadHTTP")
	@ResponseBody
	public String downloadHTTP(@RequestParam long id, HttpServletRequest request,HttpServletResponse response) {
		String str= sev_ItemDefineService.downloadHTTP(id,request,response);
		System.out.println(str);
		return str;
	}
	@RequestMapping(value = "/saveFile", method = RequestMethod.POST)
	@ResponseBody
	public synchronized InvokeResult pressUpload(DataImportDTO dto, MultipartFile file,
			HttpServletRequest request, HttpServletResponse response) {
		System.out.println("开始上传");
		System.out.println(file);
		String serverPath =  request.getSession().getServletContext().getRealPath("/");
		System.out.println(serverPath);
		String filePath =serverPath + Config.getProperty("descripfile");
		System.out.println(filePath);
		File file1 = new File(filePath);
		//判断是否存在文件夹 如果不存在 创建文件夹
		if (!file1.exists()) {
			file1.mkdir();
		}
		String name = file.getOriginalFilename();
		String baseName = name.substring(0, name.lastIndexOf("."));
		String type = name.substring(name.lastIndexOf("."),name.length());
		String newFileName="";
		try {
			newFileName = URLEncoder.encode(baseName, "utf-8")+type;
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String oname = file.getName();
		System.out.println(name); //上传文件名
		//获取上传文件地址
		UserInfo u = (UserInfo) request.getSession().getAttribute("currentUser");
		if (oname != null) {			
			try {
				File savedFile = new File(filePath,newFileName);
				file.transferTo(savedFile);
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // 保存文件
		}
		//设置上传文件记录
		dto.setNewfileName(newFileName);
		dto.setFileName(name);
		dto.setFilePath(filePath);
		dto.setDeptNo(u.getDeptCode());
		dto.setComCode(u.getComCode());
		dto.setUerId(u.getId());
		dto.setUploadUser(u.getUserCode());   
		sev_ItemDefineService.saveFile(dto);
	    
		return InvokeResult.success(); 
	}
}
