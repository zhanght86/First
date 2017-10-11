package com.sinosoft.controller;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sinosoft.common.InvokeResult;
import com.sinosoft.common.Page;

import com.sinosoft.dto.RoleInfoDTO;
import com.sinosoft.dto.UserRoleDTO;
import com.sinosoft.dto.UserinfoDTO;
import com.sinosoft.service.RoleInfoService;

@Controller
@RequestMapping("/role")
public class RoleInfoController {
	@Resource
	private RoleInfoService roleinfoService;
	private Log log  = LogFactory.getLog(RoleInfoController.class);
	
	@RequestMapping(path="/add", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult saveRoleinfo(RoleInfoDTO dto){
		log.info("id-->" + dto.getId() + ",remark-->" +dto.getRemark() + "RoleCode," + dto.getRoleCode() +", rolename" + dto.getRoleName());
		try {
			roleinfoService.saveRoleInfo(dto);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage()); 
		}	
	}
	@RequestMapping(value="/delete", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult saveRoleinfo(@RequestParam String ids){
		String[] idArr = ids.split(",");
		boolean flag = roleinfoService.deleteRoleInfo(idArr);
		if(flag){
			return InvokeResult.failure("超级角色不能删除"); 
		}else{
			return InvokeResult.success(); 
		}
	}
	/*@RequestMapping(path="/list")
	@ResponseBody
	public List<?> qryRoleinfo(){
		return RoleinfoService.qryRoleinfo();
	}*/
	@RequestMapping(path="/list")
	@ResponseBody
	public Page<?> qryRoleinfo(@RequestParam int page,@RequestParam int rows,RoleInfoDTO rid){
		return roleinfoService.qryRoleInfo(page,rows,rid);
	}
	@RequestMapping(path="/find")
	@ResponseBody
	public RoleInfoDTO Roleinfo(@RequestParam int id){
		return RoleInfoDTO.toDto(roleinfoService.findRoleInfo(id));
	}
	@RequestMapping(path="/update")
	@ResponseBody
	public InvokeResult updateRoleinfo(@RequestParam int id,RoleInfoDTO rid){
		roleinfoService.updateRoleInfo(id,rid);
		return InvokeResult.success(); 
	}
	
	@RequestMapping(path="/json")
	@ResponseBody
	public List<Map<String,Object>> findRoleInfo(String userId) {
		return roleinfoService.findRoleInfo(userId);
	}
	
	@RequestMapping(path="/roleToMenu")
	@ResponseBody
	public InvokeResult roleToMenu(String roleId , String menuId) {
		try{
			roleinfoService.saveUserToRole(roleId,menuId);
		}catch(Exception e){
			return InvokeResult.failure(e.getMessage());
		}
		
		return InvokeResult.success(); 
	}
	
}
