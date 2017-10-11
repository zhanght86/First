package com.sinosoft.controller;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sinosoft.util.ExcelUtil;

@Controller
public class ExcelExportController {
	@RequestMapping(path="/downloadExcel",method=RequestMethod.POST)
	public void export(HttpServletRequest request, HttpServletResponse response, String name, String cols, String datas) {
		ExcelUtil eu=new ExcelUtil();
		try{
			eu.export(request, response, name, cols, datas);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
