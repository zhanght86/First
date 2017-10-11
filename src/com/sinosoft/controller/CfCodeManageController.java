package com.sinosoft.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sinosoft.dao.CfCodemanageDao;

@Controller
@RequestMapping(value = "/CfCodeManage")
public class CfCodeManageController {
	@Resource
	private CfCodemanageDao cfCodemanageDao;
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(CfCodeManageController.class);
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<?> codeSelect(@RequestParam String type) {
		List<?> result=new ArrayList<Object>();
		//报告类型
		if("reporttype".equals(type)){
			result=cfCodemanageDao.query("select codename as value,codename as text from cfcodemanage where codetype='"+type+"'");
		}
		//报表名称
		if("reportname".equals(type)){
			result=cfCodemanageDao.query("select codename as value,codename as text from cfcodemanage where codetype='"+type+"'");
		}
		//季度
		if("quarter".equals(type)){
			result=cfCodemanageDao.query("select codename as value,codename as text from cfcodemanage where codetype='"+type+"'");
		}
		if("datatype".equals(type)){
			result=cfCodemanageDao.query("select codecode as value,codename as text from cfcodemanage where codetype='"+type+"'");
		}
		if("cfreporttype".equals(type)){
			result=cfCodemanageDao.query("select codecode as value,codename as text from cfcodemanage where codetype='"+type+"'");
		}
		return result;
	}
}
