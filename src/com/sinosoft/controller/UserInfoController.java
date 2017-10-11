package com.sinosoft.controller;




import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sinosoft.common.InvokeResult;
import com.sinosoft.common.Page;
import com.sinosoft.common.interceptor.Token;
import com.sinosoft.dto.UserinfoDTO;
import com.sinosoft.entity.UserInfo;
import com.sinosoft.service.UserInfoService;

@Controller
@RequestMapping(value="userinfo")
public class UserInfoController {
	@Resource
	private UserInfoService userInfoService;
	@RequestMapping(path="/add", method = RequestMethod.POST)
	@Token
	@ResponseBody
	public InvokeResult saveUserInfo(UserinfoDTO u){
		try{
			userInfoService.saveUserInfo(u);
			return InvokeResult.success(); 
		}catch(Exception e){
			return InvokeResult.failure("用户新建失败，原因是：用户代码已存在"); 
		}
	}
	@RequestMapping(value="/delete", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult deleteUserInfo(@RequestParam long id){
		boolean flag = userInfoService.deleteUserInfo(id);
		if(flag){
			return InvokeResult.success(); 
		}else{
			return InvokeResult.failure("超级管理员不能删除");
		}
	}
	/*@RequestMapping(path="/list")
	@ResponseBody
	public List<?> qryContract(){
		return contractService.qryConTract();
	}*/
	@RequestMapping(path="/list")
	@ResponseBody
	public Page<?> qryUserInfo(@RequestParam int page,@RequestParam int rows,UserinfoDTO dto){
		return userInfoService.qryUserInfo(page,rows,dto);
	}
	@RequestMapping(path="/find")
	@ResponseBody
	public UserInfo userInfo(@RequestParam long id){
		return userInfoService.findUserInfo(id);
	}
	@RequestMapping(path="/findByUserCode")
	@ResponseBody
	public InvokeResult qryUserInfo(UserinfoDTO dto){
		try {
			return InvokeResult.success(userInfoService.findUserInfo(dto));
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
	@RequestMapping(path="/update")
	@ResponseBody
	public InvokeResult updateUserInfo(@RequestParam long id,UserinfoDTO u){
		try {
			userInfoService.updateUserInfo(id, u);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			if(e.getMessage().contains("constraint")){
				return InvokeResult.failure("用户编码已存在");
			}
			return InvokeResult.failure(e.getMessage());
		}
	}
	@RequestMapping(path="/newpass",method=RequestMethod.POST)
	@ResponseBody
	public String resetPassword(@RequestParam String newpass,HttpServletRequest hrq){
		UserInfo u=(UserInfo) hrq.getSession().getAttribute("currentUser");
		try{
			userInfoService.resetPassword(newpass,u);
		}catch(Exception e){
			newpass="错误！";
			e.printStackTrace();
		}
		return newpass;
	}
	@RequestMapping(path="/resetpwd",method=RequestMethod.POST)
	@ResponseBody
	public InvokeResult resetUserPassword(@RequestParam long userId,HttpServletRequest hrq){
		try{
			UserInfo u=userInfoService.findUserInfo(userId);
			userInfoService.resetPassword("12345678",u);
		}catch(Exception e){
			e.printStackTrace();
			return InvokeResult.failure("密码重置失败,原因为:"+e.getMessage());
		}
		return InvokeResult.success();
	}
	@RequestMapping(path="/UMmap",method=RequestMethod.POST)
	@ResponseBody
	public InvokeResult addUserToRole(String roleIDs,String userID){
		try{
			userInfoService.saveUserToRole(roleIDs,userID);			
		}catch(Exception e){
			return InvokeResult.failure(e.getMessage());
		}
		return InvokeResult.success();
	}
}
