package com.sinosoft.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sinosoft.common.InvokeResult;
import com.sinosoft.common.Page;
import com.sinosoft.dto.MenuInfoDTO;
import com.sinosoft.entity.UserInfo;
import com.sinosoft.service.MenuInfoService;

@Controller
@RequestMapping(path="/menuinfo")
public class MenuInfoController {
	@Resource
	private MenuInfoService menuInfoService;
	@RequestMapping(path="/add", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult saveMenuInfo(MenuInfoDTO m){
		try {
			menuInfoService.saveMenuInfo(m);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
	@RequestMapping(path="/listpage")
	@ResponseBody
	public Page<?> qryMenuInfoOfPage(@RequestParam int page,@RequestParam int rows){
		return menuInfoService.qryMenuInfoOfPage(page, rows);
	}
	@RequestMapping(path="/listall")
	@ResponseBody
	public List<?> qryMenuInfoAll(){
		return menuInfoService.qryMenuInfoAll();
	}
	@RequestMapping(path="/list")
	@ResponseBody
	public List<?> qryMenuInfo(){
		return menuInfoService.qryMenuInfo();
	}
	@RequestMapping(path="/update")
	@ResponseBody
	public InvokeResult updateMenuInfo(@RequestParam int id,MenuInfoDTO c){
		try {
			menuInfoService.updateMenuInfo(id, c);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
	@RequestMapping(path="/delete")
	@ResponseBody
	public InvokeResult deleteMenuInfo(@RequestParam int id){
		try {
			menuInfoService.deleteMenuInfo(id);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
	@RequestMapping(path="/initMenu")
	@ResponseBody
	public Map<?,?> initMenuInfo(HttpServletRequest hsr){
		UserInfo u=(UserInfo) hsr.getSession().getAttribute("currentUser");
		return menuInfoService.initMenuInfo(u);
	}
	/**
	 * 实现三级菜单新加
	 * @param hsr
	 * @return
	 */
	@RequestMapping(path="/initMenu2")
	@ResponseBody
	public List<?> initMenuInfo2(HttpServletRequest hsr){
		UserInfo u=(UserInfo) hsr.getSession().getAttribute("currentUser");
		List<?> list =menuInfoService.initMenuInfo2(u);
		return list;
	}
	@RequestMapping(path="/getCheckData")
	@ResponseBody
	public List<?> getCheckData(String roleId){
		return menuInfoService.qryMenuInfoForCheck(roleId);
	}
	@RequestMapping(path="/find")
	@ResponseBody
	public InvokeResult find(int id){
		try {
			return InvokeResult.success(menuInfoService.findMenuInfo(id)); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
	
}
