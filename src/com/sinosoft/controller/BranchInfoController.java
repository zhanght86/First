package com.sinosoft.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sinosoft.dao.CodeSelectDao;
import java.util.ArrayList;
import com.sinosoft.common.InvokeResult;
import com.sinosoft.common.Page;
import com.sinosoft.dto.BranchInfoDTO;
import com.sinosoft.service.BranchInfoService;

@Controller
@RequestMapping(path="/branchinfo")
public class BranchInfoController {
	@Resource
	private BranchInfoService branchInfoService;
	@Resource
	private CodeSelectDao codeSeleceDao;
	private static final Logger LOGGER = LoggerFactory.getLogger(CodeSelectController.class);

	@RequestMapping(path="/add", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult saveBranchInfo(BranchInfoDTO m){
		try {
			branchInfoService.saveBranchInfo(m);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
		
	}
	@RequestMapping(path="/listpage")
	@ResponseBody
	public Page<?> qryBranchInfoOfPage(@RequestParam int page,@RequestParam int rows){
		return branchInfoService.qryBranchInfoOfPage(page, rows);
	}
	@RequestMapping(path="/listall")
	@ResponseBody
	public List<?> qryBranchInfoAll(){
		return branchInfoService.qryBranchInfoAll();
	}
	@RequestMapping(path="/list")
	@ResponseBody
	public List<?> qryBranchInfo(){
		return branchInfoService.qryBranchInfo();
	}
	@RequestMapping(path="/list1")
	@ResponseBody
	public List<?> qryBranchInfo1(){
		return branchInfoService.qryBranchInfoo();
	}
	//新加，从渤海那的
	@RequestMapping(path="/listCompanyByUperCompany")
    @ResponseBody
    public List<?> listCompanyByUperCompany(@RequestParam String company){
    	try {
    		List<?> list=branchInfoService.listCompanyByUperCompany(company);
    		return list; 
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return null;
    }
	@RequestMapping(path="/update")
	@ResponseBody
	public InvokeResult updateBranchInfo(@RequestParam String id,BranchInfoDTO c){
		try {
			branchInfoService.updateBranchInfo(id, c);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
	@RequestMapping(path="/delete")
	@ResponseBody
	public InvokeResult deleteBranchInfo(@RequestParam String id){
		try {
			branchInfoService.deleteBranchInfo(id);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
	@RequestMapping(path = "/comTypeSelect",method = RequestMethod.GET)
	@ResponseBody
	public List<?> codeSelect() {
		List<?> result=new ArrayList<Object>();
		String sql="select codecode as value ,codename as text from cfcodemanage where codetype = 'comtype'";
		try{
			result=codeSeleceDao.query(sql);
		}catch(Exception e){
			LOGGER.error("下拉框查询报错！sql为"+sql);
		}
		return result;
	}
}
