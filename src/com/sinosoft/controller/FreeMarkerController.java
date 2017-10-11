package com.sinosoft.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import com.sinosoft.util.Config;
import com.sinosoft.util.FreeMarkerUtil;


@Controller
public class FreeMarkerController {
	final String FREEMARKER_PATH=Config.getProperty("uploaddir");
	@Autowired
	private FreeMarkerConfig freeMarkerConfig;//获取FreemarkerConfig的实例 
	@RequestMapping("/freemarker")
	public String ttt(HttpServletRequest request,HttpServletResponse response,ModelMap mv) throws Exception{
		String fileName ="JDKB.doc";
	    Boolean flag =(Boolean)FreeMarkerUtil.htmlFileHasExist(request, FREEMARKER_PATH, fileName).get("exist");
	    if(!flag){//如何静态文件不存在，重新生成
	    	Map<String, Object> paramMap = new HashMap<String, Object>();  
            paramMap.put("description", "我正在学习使用Freemarker生成静态文件！");  
              
            List<String> nameList = new ArrayList<String>();  
            nameList.add("陈靖仇");  
            nameList.add("玉儿");  
            nameList.add("宇文拓");  
            paramMap.put("nameList", nameList);  
              
            Map<String, Object> weaponMap = new HashMap<String, Object>();  
            weaponMap.put("first", "<table border=1><tr><td>1111111</td></tr></table>");  
            weaponMap.put("second", "崆峒印");  
            weaponMap.put("third", "女娲石");  
            weaponMap.put("fourth", "神农鼎");  
            weaponMap.put("fifth", "伏羲琴");  
            weaponMap.put("sixth", "昆仑镜");  
            weaponMap.put("seventh", null);  
            paramMap.put("weaponMap", weaponMap);  
            
	        mv.addAllAttributes(paramMap);
	        FreeMarkerUtil.createHtml(freeMarkerConfig, "KBMB2016.ftl", request, paramMap, FREEMARKER_PATH, fileName);//根据模板生成静态页面
	    }
	    return FREEMARKER_PATH+"/"+fileName;//始终返回生成的HTML页面
	}
}
