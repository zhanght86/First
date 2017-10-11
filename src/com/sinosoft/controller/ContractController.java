package com.sinosoft.controller;


import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sinosoft.common.InvokeResult;
import com.sinosoft.common.Page;
import com.sinosoft.dto.ContractDTO;
import com.sinosoft.entity.Contract;
import com.sinosoft.service.ContractService;

@Controller
@RequestMapping("/contract")
public class ContractController {
	@Resource
	private ContractService contractService;
	@RequestMapping(path="/add", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult saveContract(ContractDTO c){
		contractService.saveConTract(c);
		return InvokeResult.success(); 
	}
	@RequestMapping(value="/delete", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult saveContract(@RequestParam String ids){
		String[] idArr = ids.split(",");
		contractService.deleteConTract(idArr);
		return InvokeResult.success(); 
	}
	/*@RequestMapping(path="/list")
	@ResponseBody
	public List<?> qryContract(){
		return contractService.qryConTract();
	}*/
	@RequestMapping(path="/list")
	@ResponseBody
	public Page<?> qryContract(@RequestParam int page,@RequestParam int rows,ContractDTO ctd){
		return contractService.qryConTract(page,rows,ctd);
	}
	@RequestMapping(path="/find")
	@ResponseBody
	public Contract Contract(@RequestParam int id){
		return contractService.findConTract(id);
	}
	@RequestMapping(path="/update")
	@ResponseBody
	public InvokeResult updateContract(@RequestParam int id,ContractDTO c){
		contractService.updateConTract(id,c);
		return InvokeResult.success(); 
	}
}
