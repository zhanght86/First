package com.sinosoft.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sinosoft.common.InvokeResult;

@Controller
public class Login {
	private static final Logger LOGGER = LoggerFactory.getLogger(Login.class);
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(HttpServletRequest request) {
		 Subject currentUser = SecurityUtils.getSubject();
        if(currentUser.isAuthenticated()){
            return "redirect:index.do";
        }
		return "login";
	}
	
	@RequestMapping(path="/login",method=RequestMethod.POST)
	@ResponseBody
	public InvokeResult LoginIn(@RequestParam String username,@RequestParam String password){
		UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
		System.out.println("username================="+username);
		if("".equals(password.trim())||password==null){
			return InvokeResult.failure("密码不能为空");
		}
		try{
			SecurityUtils.getSubject().login(usernamePasswordToken);	
			return InvokeResult.success();
		}catch(UnknownAccountException e){
			LOGGER.error(e.getMessage(),e);
			return InvokeResult.failure("账号或密码不存在");
		}catch(LockedAccountException e){
			LOGGER.error(e.getMessage(),e);
			return InvokeResult.failure("该用户已禁用，请与管理员联系");
		}catch(AuthenticationException e){
			LOGGER.error(e.getMessage(),e);
			return InvokeResult.failure("用户密码失败");
		}catch(Exception e){
			LOGGER.error(e.getMessage(),e);
			return InvokeResult.failure("登录失败");
		}
	}
	@RequestMapping(value = "/loginOut",method=RequestMethod.POST)
	@ResponseBody
	public InvokeResult loginOut() {
		try{
			SecurityUtils.getSubject().logout();
			return InvokeResult.success();
		}catch(Exception e){
			LOGGER.error(e.getMessage(),e);
			return InvokeResult.success("退出失败");
		}
	}

}
