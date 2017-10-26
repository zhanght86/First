package com.sinosoft.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sinosoft.dao.CodeSelectDao;

@Controller
@RequestMapping(value = "/codeSelect")
public class CodeSelectController {
	@Resource
	private CodeSelectDao codeSeleceDao;
	private static final Logger LOGGER = LoggerFactory.getLogger(CodeSelectController.class);
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<?> codeSelect(@RequestParam String type) {
		List<?> result=new ArrayList<Object>();
		String sql="";
		if("contractName".equals(type)){
			sql="select ContractName as value,ContractName as text from Contract";
		}else if("reporttype".equals(type)){
			sql="select codecode as value,codename as text from cfcodemanage where codetype='reporttype'";
		}else if("reporttype1".equals(type)){
			sql="select codecode as value,codename as text from cfcodemanage where codetype='reporttype'and codecode='2'";
		}else if("reportname".equals(type)){
			sql="select codecode as value,codename as text from cfcodemanage where codetype='reportname'";
		}else if("quarter".equals(type)){
			sql="select codecode as value,codename as text from cfcodemanage where codetype='quarter' order by codecode";
		}else if("cfreporttype".equals(type)){
			sql="select codecode as value,codename as text from cfcodemanage where codetype='cfreporttype'";
		}else if("cfyzlx".equals(type)){
			sql="select codecode as value,codename as text from cfcodemanage where codetype='yzlx'";
		}else if("collectdepart".equals(type)){
			sql="select codecode as value,codename as text from cfcodemanage where codetype='collectdepart'";
		}else if("computetype".equals(type)){
			sql="select codecode as value,codename as text from cfcodemanage where codetype='computetype'";
		}else if("datafiletype".equals(type)){
			sql="select codecode as value,codename as text from cfcodemanage where codetype='datafiletype'";
		}else if("dataresource".equals(type)){
			sql="select codecode as value,codename as text from cfcodemanage where codetype='dataresource'";
		}else if("indextype".equals(type)){
			sql="select codecode as value,codename as text from cfcodemanage where codetype='indextype'";
		}else if("indextype1".equals(type)){
			sql="select codecode as value,codename as text from cfcodemanage where codetype='indextype1'";
		}else if("InvestType".equals(type)){
			sql="select codecode as value,codename as text from cfcodemanage where codetype='InvestType'";
		}else if("InvestTypeDetail".equals(type)){
			sql="select codecode as value,codename as text from cfcodemanage where codetype='InvestTypeDetail'";
		}else if("modifyState".equals(type)){
			sql="select codecode as value,codename as text from cfcodemanage where codetype='modifyState'";
		}else if("operator".equals(type)){
			sql="select codecode as value,codename as text from cfcodemanage where codetype='operator'";
		}else if("querydepart".equals(type)){
			sql="select codecode as value,codename as text from cfcodemanage where codetype='querydepart'";
		}else if("recognizecode".equals(type)){
			sql="select codecode as value,codename as text from cfcodemanage where codetype='recognizecode'";
		}else if("reportitemproperty".equals(type)){
			sql="select codecode as value,codename as text from cfcodemanage where codetype='reportitemproperty'";
		}else if("reportstate".equals(type)){
			sql="select codecode as value,codename as text from cfcodemanage where codetype='reportstate'";
		}else if("datatype".equals(type)){
			sql="select codecode as value,codename as text from cfcodemanage where codetype='datatype'";
		}else if("cfreportdata".equals(type)){
			sql="select distinct outitemcode as value,outitemcodename as text from cfReportData";
		}else if("reportdataanalyse".equals(type)){
			sql="select distinct reportitemcode as value,concat(outitemcode,concat('--',reportitemname)) as text from alm_cfreportitemcodedesc where reportitemcode like '%ALM_PJZB%' and sheetcol = '7' order by reportitemcode";
		}else if("reportdataanalyse1".equals(type)){
			sql="select distinct month as value,month as text,substr(month,0,4) team from cfReportData ";
		}else if("datacheck".equals(type)){ 
			sql="select codecode as value,codename as text from cfcodemanage where codetype='datacheck'";
		}else if("filetype".equals(type)){
			sql="select codecode as value,codename as text from cfcodemanage where codetype='filetype'";
		}else if("filetype1".equals(type)){
			sql="select codecode as value,codename as text from cfcodemanage where codetype='filetype' and remark='1'";
		}else if("sheet".equals(type)){
			sql="select distinct( re.reportcode) as value,re.reportname as text from cfreportdesc re, cfreportitemcodedesc de  where  re.reportcode=de.reportcode";
		}else if("reportName".equals(type)){
			sql="select distinct(re.reportcode) as value,re.reportname as text from cfreportdesc re, cfreportitemcodedesc de  where  re.reportcode=de.reportcode";
		}else if("investinterfacetype".equals(type)){
			sql="select codecode as value,codename as text from cfcodemanage where  codetype='investinterfacetype'";
		}else if("errLevel".equals(type)){
			sql="select codecode as value,codename as text from cfcodemanage where  codetype='errLevel'";
		}else if("cfreportstate".equals(type)){
			sql="select codecode as value,codename as text from cfcodemanage where  codetype='cfreportstate'";
		}else if("financeCode".equals(type)) {
			sql="select codecode as value,codename as text from cfcodemanage where  codetype='financeCode'";
		}else if("abc_investCode".equals(type)) {
			sql="select codecode as value,codename as text from cfcodemanage where  codetype='abc_investCode'";
		}else if ("finance_reportID".equals(type)) {
			sql="select distinct reportId as value,  reportId as text from abc_finance where reportid is not null  ";
		}else if ("invest_reportID".equals(type)) {
			sql="select distinct reportId as value,  reportId as text from abc_invest where reportid is not null  ";
		}else if("reportid".equals(type)) {
			sql="select reportid as value,reportid as text from cal_cfreportinfo order by reportid";
		}else if("reportid_import".equals(type)) {
			sql="select reportid as value,reportid as text from cal_cfreportinfo  where reportstate<2 order by reportid";
		} if("FinanceType".equals(type)){
			sql="select codecode as value,codename as text from cfcodemanage where  codetype='FinanceType'";
		}else if("wordType".equals(type)){
			sql="select codecode as value,codename as text from cfcodemanage where  codetype='wordType'";
		}else if("finadigaoType".equals(type)){
			sql="select codename as value,codename as text from cfcodemanage where  codetype='FinaInfoType'";
		}else if("reportCateGory".equals(type)){
			sql="select codecode as value,codename as text from cfcodemanage where  codetype='reportCateGory'";
		}else if (type.equalsIgnoreCase("deptno")){
			sql = "select comcode as value, comname as text from branchinfo a where a.comrank='4' and a.useflag='1' order by value";
		}else if(type.equalsIgnoreCase("companycode")){
			sql = "select comcode as value, comname as text from branchinfo a where a.useflag='1' and a.comrank<>'4' order by value";
		}else if (type.equalsIgnoreCase("deptnoall")){
			sql = "select comcode as value, comname as text from branchinfo a where a.comrank='4' and a.useflag='1' order by value";
		}else if (type.equalsIgnoreCase("UseFlag")){
			sql = "select codecode as value, codename as text from cfcodemanage a where a.codetype='UseFlag'";
		}else if("riskreportname".equals(type)){//风险综合评级报表名称
			sql="select codecode as value,codename as text from cfcodemanage where codetype='reportname' and codecode in('4','5')";
		}else if("projectCode1".equals(type)){//资产负债管理能力评估评估项目
			sql="select codecode as value,codename as text from cfcodemanage where codetype='projectCode'";
		}else if("projectCode2".equals(type)){//资产负债管理能力评估评估项目
			sql="select codecode as value,codename as text from cfcodemanage where codetype='projectCode' and codecode !='p00'";
		}else if("year".equals(type)){//资产负债管理能力评估评估项目单号年份
			sql="select distinct(c.year) as value,c.year as text from cal_cfreportinfo c where c.reportcategory='1' order by c.year";
		}else if("EvaluateResult".equals(type)){//资产负债管理能力评估评估项目
			sql="select codecode as value,codename as text from cfcodemanage where codetype='EvaluateResult'";
		}else if("EvaluateResult2".equals(type)){//资产负债管理能力评估评估项目
			sql="select codecode as value,codename as text from cfcodemanage where codetype='EvaluateResult' and codecode in ('1','4') ";
		}else if("quarter".equals(type)){
			sql="select codecode as value,codename as text from cfcodemanage where upper(codetype)= upper('" + type + "') order by to_number(value)";
		}
		try{
			result=codeSeleceDao.query(sql);
		}catch(Exception e){
			LOGGER.error("下拉框查询报错！sql为"+sql);
		}
		return result;
	}
	@RequestMapping(path = "/hasParam",method = RequestMethod.GET)
	@ResponseBody
	public List<?> codeSelect(@RequestParam String type,@RequestParam String params) {
		List<?> result=new ArrayList<Object>();
		String sql="";
		if("cfreportdataX".equals(type)){
			sql="select month as value,month as text,substr(month,0,4) team from cfReportData where outitemcode='"+params+"'";
		}else if("reportdataanalyse".equals(type)){
			sql="select distinct month as value,month as text,substr(month,0,4) team from alm_cfreportdata where outitemcode in ("+params+") order by team";
		}else if("dataanalyse".equals(type)){
			sql="select distinct month as value,month as text,substr(month,0,4) team from alm_cfreportdata order by team";
		}else if("reportdataanalyse1".equals(type)){
			sql="select distinct outitemcode as value,concat(outitemcode,concat('--', outitemcodename)) as text from cfreportdata where month='"+params + "'and outitemcode like 'S01%' or outitemcode like'S07%' order by outitemcode";
		}else if("reportname".equals(type)){
			if("1".equals(params)){
				sql="select distinct codecode as value,codename as text from cfCodeManage where codetype='reportname' and codecode in('1') order by codecode";
			}else if("2".equals(params)){
				sql="select distinct codecode as value,codename as text from cfCodeManage where codetype='reportname' and codecode in('1','2','3') order by codecode";				
			}else if("3".equals(params)){
				sql="select distinct codecode as value,codename as text from cfCodeManage where codetype='reportname' and codecode in('1','2','3') order by codecode";				
			}
		}else if("riskname".equals(type)){			
			sql="select distinct codecode as value,codename as text from cfCodeManage where codetype='riskname' and codecode in('2','3') order by codecode";				
		}else if("investinterfacetable".equals(type)){
			if("1".equals(params)){
				sql="select distinct codecode as value,codename as text from cfCodeManage where codetype='investinterfacetable1' order by codecode";
			}else if("2".equals(params)){
				sql="select distinct codecode as value,codename as text from cfCodeManage where codetype='investinterfacetable2' order by codecode";				
			}else if("3".equals(params)){
				sql="select distinct codecode as value,codename as text from cfCodeManage where codetype='investinterfacetable3' order by codecode";				
			}else if("4".equals(params)){
				sql="select distinct codecode as value,codename as text from cfCodeManage where codetype='investinterfacetable4' order by codecode";				
			}else if("5".equals(params)){
				sql="select distinct codecode as value,codename as text from cfCodeManage where codetype='investinterfacetable5' order by codecode";				
			}else if("6".equals(params)){
				sql="select distinct codecode as value,codename as text from cfCodeManage where codetype='investinterfacetable6' order by codecode";				
			}else if("7".equals(params)){
				sql="select distinct codecode as value,codename as text from cfCodeManage where codetype='investinterfacetable7' order by codecode";				
			}
		}else if("cfreportstate".equals(type)){
			sql="select codecode as value,codename as text from cfcodemanage where  codetype='cfreportstate' and codecode in ("+params+")";
		}
		else if("reportnameTy".equals(type)){
			 if("1".equals(params)){
				sql="select codecode as value,codename as text from cfcodemanage where codetype='reportname' and codecode in('1')";
			}else if("2".equals(params)){
				sql="select codecode as value,codename as text from cfcodemanage where codetype='reportname' and codecode in('1','3')";
			}else if("01".equals(params)||"02".equals(params)||"03".equals(params)){
				sql="select codecode as value,codename as text from cfcodemanage where codetype='reportname' and codecode in(select reportname_code from deptno_reportname_relation where department_code = '"+params+"')";
			}
		}
		else if("companycode".equals(type)){
			sql = "select comcode as value, comname as text from branchinfo a where a.useflag='1' and a.comrank<>'4' and a.comrank>=(select comrank from branchinfo where comcode='"+params+"') order by value";
		}else if("riskreportname".equals(type)){
			sql = "select codecode as value, codename as text from cfCodeManage where codetype='reportname' and codecode>= (select case when comrank='1' then '4' when comrank='2' or comrank='3' then '5' end from branchinfo where comcode='"+params+"') order by value";
		}else if("deptNo".equals(type)){
			sql = "select comcode as value, comname as text from branchinfo a where a.comrank='4' and a.useflag='1' and upercomcode='"+params+"'";
		}else if("comCode".equals(type)) {
			String sql1 = "select comRank as value from branchinfo where comcode='"+params+"'";
			List<?> comRankList = codeSeleceDao.query(sql1);
			
			String comRank =(String) ((Map)comRankList.get(0)).get("value");
			if (comRank.equals("1")) {
				sql = "select comcode as value, comname as text from branchinfo a where a.useflag='1' and a.comrank<>'4' and a.comrank>=(select comrank from branchinfo where comcode='"+params+"') order by value";
			} else {
				sql = "select comcode as value, comname as text from branchinfo a where a.useflag='1' and a.comcode='"+params+"'";
			}

		}else if("reportId".equals(type)){			
			sql="select c.reportid as value,c.reportid as text from cal_cfreportinfo c where c.year='"+params+"' and c.reportcategory='1' order by c.reportid";				
		}
		try{
			result=codeSeleceDao.query(sql);
		}catch(Exception e){
			LOGGER.error("下拉框查询报错！sql为"+sql);
		}
		return result;
	}
}
