package com.sinosoft.controller.zcfz.reportDataAnalyse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sinosoft.common.Dao;
import com.sinosoft.entity.CfReportData;

@Controller
@RequestMapping(value="/cfreportdata")
public class CfReportItemCodeController {
	@Resource
	private Dao dao;
	@RequestMapping(value="/getdata", method = RequestMethod.POST)
	@ResponseBody
	public Map savReportItem(@RequestParam Map<?,?> param){
		try {
			String outItemCode="";
			String outItemCodeName="";
			String month="";
			if(param.get("outItemCode")!=null&&!"".equals(param.get("outItemCode"))){
				outItemCode=param.get("outItemCode").toString();
				//outItemCodeName=PubFun.getCfReportData(outItemCode).getOutItemCodeName();
			}
			String hsql="from CfReportData where id.outItemCode='"+param.get("outItemCode")+"'";
			if(param.get("month")!=null&&!"".equals(param.get("month"))){
				month=String.valueOf(param.get("month"));
				hsql="from CfReportData where id.outItemCode='"+param.get("outItemCode")+"' and id.month in("+month+")";
			}
			List list=dao.query(hsql);
			int len=list.size();
			Object[] y= new Object[len];
			for(int i=0;i<len;i++){
				Object[] x = new Object[2];
				x[0]=((CfReportData)list.get(i)).getMonth();
				x[1]=((CfReportData)list.get(i)).getReportItemValue();
				outItemCodeName=((CfReportData)list.get(0)).getOutItemCodeName();
				y[i]=x;
			}
			Map map=new HashMap();
			
			map.put("data", y);
			map.put("name", outItemCode+"("+outItemCodeName+")");
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
