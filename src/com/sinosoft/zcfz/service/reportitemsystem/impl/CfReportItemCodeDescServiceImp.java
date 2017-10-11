package com.sinosoft.zcfz.service.reportitemsystem.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sinosoft.common.CurrentUser;
import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dao.reportitemsystem.CfReportItemCodeDescDao;
import com.sinosoft.zcfz.dto.reportitemsystem.CfReportItemCodeDescDTO;
import com.sinosoft.entity.CfReportItemCodeDesc;
import com.sinosoft.entity.UserInfo;
import com.sinosoft.zcfz.service.reportitemsystem.CfReportItemCodeDescService;
import com.sinosoft.util.Config;
@Service
public class CfReportItemCodeDescServiceImp implements CfReportItemCodeDescService{
	@Resource
	private CfReportItemCodeDescDao reportItemDao;
	@Transactional
	@Override
	public void saveReportItem(CfReportItemCodeDescDTO rid) {
		// TODO Auto-generated method stub
		CfReportItemCodeDesc re=rid.toCfReportItemCodeDesc(new CfReportItemCodeDesc());
		reportItemDao.save(re);	
	}
	@Transactional
	@Override
	public void updateReportItem(String id, CfReportItemCodeDescDTO rid) {
		// TODO Auto-generated method stub
		CfReportItemCodeDesc re=reportItemDao.get(CfReportItemCodeDesc.class, id);
		// rid.setOutItemCode(re.getOutItemCode());
		re=rid.toCfReportItemCodeDesc(re);
		reportItemDao.update(re);
	}
	@Transactional
	@Override
	public void deleteReportItem(String id) {
		String[] ids=id.split("\\|");
		if(ids.length>1){
			reportItemDao.removes(ids,CfReportItemCodeDesc.class);
		}else{
			CfReportItemCodeDesc re=reportItemDao.get(CfReportItemCodeDesc.class, ids[0]);
			reportItemDao.remove(re);
		}
		// TODO Auto-generated method stub
	}

	@Override
	public Page<?> qryReportItemOfPage(int page, int rows,CfReportItemCodeDescDTO rid) {
		StringBuilder querySql=new StringBuilder("select c.*,cf1.codename reportTypeName,cf2.codename outItemTypeName from cfreportItemcodedesc c,cfcodemanage cf1,cfcodemanage cf2 where 1=1");
		querySql.append(" and  c.reporttype=cf1.codecode and cf1.codetype='cfreporttype' and c.outitemtype=cf2.codecode and cf2.codetype='datatype'");
		if(rid.getAsset()!=null&&!rid.getAsset().equals("")){
			querySql.append(" and asset like '%"+rid.getAsset()+"%'");
		}
		if(rid.getReportItemCode()!=null&&!rid.getReportItemCode().equals("")){
			querySql.append(" and ReportItemCode like '%"+rid.getReportItemCode()+"%'");
		}
		if(rid.getReportItemName()!=null&&!rid.getReportItemName().equals("")){
			querySql.append(" and ReportItemName like '%"+rid.getReportItemName()+"%'");
		}
		if(rid.getHalfYearReport()!=null&&!rid.getHalfYearReport().equals("")){
			querySql.append(" and HalfYearReport like '%"+rid.getHalfYearReport()+"%'");
		}
		if(rid.getYearReport()!=null&&!rid.getYearReport().equals("")){
			querySql.append(" and YearReport like '%"+rid.getYearReport()+"%'");
		}
		if(rid.getReportType()!=null&&!rid.getReportType().equals("")){
			querySql.append(" and ReportType like '%"+rid.getReportType()+"%'");
		}
		if(rid.getQuarterReport()!=null&&!rid.getQuarterReport().equals("")){
			querySql.append(" and QuarterReport like '%"+rid.getAsset()+"%'");
		}
		if(rid.getGroup1()!=null&&!rid.getGroup1().equals("")){
			querySql.append(" and Group1 like '%"+rid.getGroup1()+"%'");
		}
		if(rid.getGroup2()!=null&&!rid.getGroup2().equals("")){
			querySql.append(" and Group2 like '%"+rid.getGroup2()+"%'");
		}
		if(rid.getGroup3()!=null&&!rid.getGroup3().equals("")){
			querySql.append(" and Group3 like '%"+rid.getGroup3()+"%'");
		}
		if(rid.getLifeInsurance1()!=null&&!rid.getLifeInsurance1().equals("")){
			querySql.append(" and LifeInsurance1 like '%"+rid.getLifeInsurance1()+"%'");
		}
		if(rid.getLifeInsurance2()!=null&&!rid.getLifeInsurance2().equals("")){
			querySql.append(" and LifeInsurance2 like '%"+rid.getLifeInsurance2()+"%'");
		}
		if(rid.getLifeInsurance3()!=null&&!rid.getLifeInsurance3().equals("")){
			querySql.append(" and LifeInsurance3 like '%"+rid.getLifeInsurance3()+"%'");
		}
		if(rid.getLifeInsurance4()!=null&&!rid.getLifeInsurance4().equals("")){
			querySql.append(" and LifeInsurance4 like '%"+rid.getLifeInsurance4()+"%'");
		}
		if(rid.getReinsurance()!=null&&!rid.getReinsurance().equals("")){
			querySql.append(" and Reinsurance like '%"+rid.getReinsurance()+"%'");
		}
		if(rid.getPropertyInsurance1()!=null&&!rid.getPropertyInsurance1().equals("")){
			querySql.append(" and PropertyInsurance1 like '%"+rid.getPropertyInsurance1()+"%'");
		}
		if(rid.getPropertyInsurance2()!=null&&!rid.getPropertyInsurance2().equals("")){
			querySql.append(" and PropertyInsurance2 like '%"+rid.getPropertyInsurance2()+"%'");
		}
		if(rid.getPropertyInsurance3()!=null&&!rid.getPropertyInsurance3().equals("")){
			querySql.append(" and PropertyInsurance3 like '%"+rid.getPropertyInsurance3()+"%'");
		}
		if(rid.getOutItemCode()!=null&&!rid.getOutItemCode().equals("")){
			querySql.append(" and OutItemCode like '%"+rid.getOutItemCode()+"%'");
		}
		if(rid.getOutItemType()!=null&&!rid.getOutItemType().equals("")){
			querySql.append(" and OutItemType like '%"+rid.getOutItemType()+"%'");
		}
		if(rid.getOutItemState()!=null&&!rid.getOutItemState().equals("")){
			querySql.append(" and OutItemState like '%"+rid.getOutItemState()+"%'");
		}
		if(rid.getOutItemNote()!=null&&!rid.getOutItemNote().equals("")){
			querySql.append(" and OutItemNote like '%"+rid.getOutItemNote()+"%'");
		}
		if(rid.getOutItemVersion()!=null&&!rid.getOutItemVersion().equals("")){
			querySql.append(" and OutItemVersion like '%"+rid.getOutItemVersion()+"%'");
		}
		if(rid.getTemp2()!=null&&!rid.getTemp2().equals("")){
			querySql.append(" and temp2 like '%"+rid.getTemp2()+"%'");
		}
		String endtime=rid.getEndTime();
		String begintime=rid.getBeginTime();
		if(begintime!=null&&!begintime.trim().equals("")){
			begintime=rid.getBeginTime().replaceAll("-0","/").replaceAll("-","/");
			querySql.append(" and outitemcreattime >= to_date('"+begintime+"','yyyy/mm/dd')");
		}
		if(endtime!=null&&!endtime.trim().equals("")){
			endtime=rid.getEndTime().replaceAll("-0","/").replaceAll("-","/");
			querySql.append(" and outitemcreattime <= to_date('"+endtime+"','yyyy/mm/dd')");
		}
		if(rid.getReportCode()!=null&&!rid.getReportCode().equals("")){
			querySql.append(" and ReportCode like '%"+rid.getReportCode()+"%'");
		}
		
		if(rid.getSort()!=null&&!rid.getSort().equals("")){
			String[] sorts=rid.getSort().split(",");
			String[] orders=rid.getOrder().split(",");
			if(sorts.length>0){
				querySql.append(" order by "+sorts[0]+" "+orders[0]);
			}
			for(int i=1;i<orders.length;i++){
				querySql.append(" ,"+sorts[i]+" "+orders[i]);
			}
		}
		return reportItemDao.queryByPage(querySql.toString(), page, rows, CfReportItemCodeDescDTO.class);
	}
	//查询指标文本块
	public Page<?> qryReportItemTextBlockOfPage(int page, int rows,String reportId,String department) {
		StringBuilder querySql=new StringBuilder("select c.*,(select codename  from cfcodemanage cc,cfreportitemcodedesc cd where codetype='collectdepart' and codecode =cd.reportitemtype and cd.reportitemcode=c.reportitemcode)as departmentName ,cf1.codename reportTypeName,cf2.codename outItemTypeName,u.state state from cfreportItemcodedesc c,cfcodemanage cf1,cfcodemanage cf2,");
		querySql.append("((Select reportitemcode,'否' state From cfreportItemcodedesc Where reportitemcode Not In(Select reportitemcode From cfreportdata where reportId='"+reportId+"' ) and outitemtype = '02') union  (Select reportitemcode,'是' state From cfreportItemcodedesc Where reportitemcode In(Select reportitemcode From cfreportdata where reportId='"+reportId+"' ) and outitemtype = '02') ) u");
		querySql.append(" where 1=1 and c.reporttype=cf1.codecode and cf1.codetype='cfreporttype' and c.outitemtype=cf2.codecode and cf2.codetype='datatype' and c.outitemtype='02' and c.reportitemcode=u.reportitemcode");
		//querySql=new StringBuilder("select c.*,(select codename  from cfcodemanage where codetype='collectdepart' and codecode='"+department+"') as departmentName ,cf1.codename reportTypeName,cf2.codename outItemTypeName,u.state state from cfreportItemcodedesc c,cfcodemanage cf1,cfcodemanage cf2,");
		//querySql.append("((Select reportitemcode,'否' state From cfreportItemcodedesc Where reportitemcode Not In(Select reportitemcode From cfreportdata where reportId='"+reportId+"' ) and outitemtype = '02') union  (Select reportitemcode,'是' state From cfreportItemcodedesc Where reportitemcode In(Select reportitemcode From cfreportdata where reportId='"+reportId+"' ) and outitemtype = '02') ) u");
		//querySql.append(" where 1=1 and c.reporttype=cf1.codecode and cf1.codetype='cfreporttype' and c.outitemtype=cf2.codecode and cf2.codetype='datatype' and c.outitemtype='02' and c.reportitemcode=u.reportitemcode");
		if("01".equals(department)||"02".equals(department)||"03".equals(department)||"04".equals(department)||"05".equals(department)||"06".equals(department)||"07".equals(department)||"08".equals(department)||"09".equals(department)){
		//判断是否是管理员--true是管理员，false不是管理员
		if (!isAdmin()) {
				querySql.append(" and c.reportitemtype='"+department+"'");
			}
		}
		System.out.println(isAdmin());
		String companyType=Config.getProperty("CompanyType");
		if("L".equals(companyType.trim())){
			querySql.append(" and c.lifeinsurance1='1'");
		}else if("P".equals(companyType.trim())){
			querySql.append(" and c.propertyinsurance1='1'");
		}else if("R".equals(companyType.trim())){
			querySql.append(" and c.reinsurance='1'");
		}
		return reportItemDao.queryByPage(querySql.toString(), page, rows, CfReportItemCodeDescDTO.class);
		}
	//判断是否是管理员
	public boolean isAdmin() {
		UserInfo user=CurrentUser.CurrentUser;
		String userCode=user.getUserCode();
		String sql="select rr.rolecode from roleinfo rr where rr.id=(select uu.role_id from Userrole uu where uu.user_id=(select ui.id from userinfo ui where ui.usercode='"+userCode+"'))";
		List<?> list= reportItemDao.queryBysql(sql);
		String u=list.get(0).toString();
		if (u.equals("{ROLECODE=admin}")) {
			return true;
		}
 		return false;
		}
	@Override
	public List<?> qryReportItem() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<?> qryReportItemAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CfReportItemCodeDesc findReportItem(String id) {
		// TODO Auto-generated method stub
		return reportItemDao.get(CfReportItemCodeDesc.class, id);
	}
	@Override
	public Page<?> qryReportItemOfPage(int page, int rows) {
		// TODO Auto-generated method stub
		return null;
	}
}
