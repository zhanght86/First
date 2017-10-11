package com.sinosoft.zcfz.service.glnlpg.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sinosoft.common.CurrentUser;
import com.sinosoft.common.InvokeResult;
import com.sinosoft.entity.CalCfReportInfo;
import com.sinosoft.entity.UserInfo;
import com.sinosoft.util.Config;
import com.sinosoft.util.EmailUtil;
import com.sinosoft.util.ExcelUtil;
import com.sinosoft.zcfz.dao.glnlpg.Alm_GatherInfoDao;
import com.sinosoft.zcfz.dao.glnlpg.Alm_ItemDefineDao;
import com.sinosoft.zcfz.dao.glnlpg.Alm_ReportDataDao;
import com.sinosoft.zcfz.dao.glnlpg.Alm_ReportInfoDao;
import com.sinosoft.zcfz.dao.glnlpg.Alm_ResultDao;
import com.sinosoft.zcfz.service.glnlpg.Alm_ResultService;
import com.sinosoft.zcfz.service.glnlpg.activiti.ActivitiService;
import com.sinosoft.zcfz.dao.glnlpg.Alm_AdjPlanDao;
import com.sinosoft.zcfz.dto.glnlpg.Alm_ComputeResultDTO;
import com.sinosoft.zcfz.dto.glnlpg.Alm_GatherInfoDTO;
import com.sinosoft.zcfz.dto.glnlpg.Alm_ItemDefineDTO;
import com.sinosoft.zcfz.dto.glnlpg.Alm_ReportDataDTO;
import com.sinosoft.zcfz.dto.glnlpg.Alm_ReportInfoDTO;
import com.sinosoft.zcfz.dto.glnlpg.Alm_ResultDTO;
import com.sinosoft.zcfz.entity.Alm_AdjPlan;
import com.sinosoft.zcfz.entity.Alm_ComputeResult;
import com.sinosoft.zcfz.entity.Alm_ComputeResultId;
import com.sinosoft.zcfz.entity.Alm_GatherInfo;
import com.sinosoft.zcfz.entity.Alm_ReportInfo;
import com.sinosoft.zcfz.entity.Alm_Result;
import com.sinosoft.zcfz.entity.Alm_ResultId;
@Service
public class Alm_ResultServiceImp implements Alm_ResultService {
	private static final Logger LOGGER = LoggerFactory.getLogger(Alm_ResultServiceImp.class);
	@Resource
	private Alm_ResultDao sev_ResultDao;
	@Resource
	private Alm_ItemDefineDao sev_ItemDefineDao;
	@Resource
	private Alm_GatherInfoDao sev_GatherInfoDao;
	@Resource
	private Alm_ReportInfoDao sev_ReportInfoDao;
	@Resource
	private Alm_ReportDataDao sev_ReportDataDao;
	@Resource
	private Alm_AdjPlanDao Sev_AdjPlanDao;
	@Autowired
	private IdentityService identityService;
	@Resource
	private ActivitiService activitiService;
	@Resource
	private TaskService taskService;

	@SuppressWarnings("unchecked")
	@Transactional
	public String get() {
		String sql="select distinct t.reportid reportId from sev_result t left join sev_reportinfo s on t.reportid=s.reportid where s.flag=1 order by reportId desc ";
		List<Alm_ResultDTO> sc=(List<Alm_ResultDTO>)sev_ResultDao.queryBysql(sql,Alm_ResultDTO.class);	
		if(sc!=null&&!sc.isEmpty()&&sc.size()>0){
			return sc.get(0).getReportId();
		}else{
			return null;
		}
	}
	//监管反馈页面获取单号
	@SuppressWarnings("unchecked")
	@Transactional
	public String get2() {
		String sql="select distinct t.reportid reportId from sev_result t  order by reportId desc ";
		List<Alm_ResultDTO> sc=(List<Alm_ResultDTO>)sev_ResultDao.queryBysql(sql,Alm_ResultDTO.class);	
		String sql1="select s.flag from sev_reportinfo s where s.reportid='"+sc.get(0).getReportId()+"' and s.backtype=0";
		List<Alm_ReportInfoDTO> si=(List<Alm_ReportInfoDTO>)sev_ReportInfoDao.queryBysql(sql1,Alm_ReportInfoDTO.class);	
		if(si.get(0).getFlag()==2||si.get(0).getFlag()==3){
			return sc.get(0).getReportId();
		}else{
			return null;
		}
	}
	//获取监管录入的单号
	@SuppressWarnings("unchecked")
	@Transactional
	public String get3() {
		String sql="select distinct t.reportid reportId from sev_result t  order by reportId desc ";
		List<Alm_ResultDTO> sc=(List<Alm_ResultDTO>)sev_ResultDao.queryBysql(sql,Alm_ResultDTO.class);	
		String sql1="select s.flag from sev_reportinfo s where s.reportid='"+sc.get(0).getReportId()+"'";
		List<Alm_ReportInfoDTO> si=(List<Alm_ReportInfoDTO>)sev_ReportInfoDao.queryBysql(sql1,Alm_ReportInfoDTO.class);	
		if(si.get(0).getFlag()==2){
			return sc.get(0).getReportId();
		}else{
			return null;
		}
	}
	//保存监管录入
	@Transactional
	public void entry(Alm_ResultDTO sr){
		System.out.println(sr.getReportId()+"###"+sr.getItemCode());
		Alm_ResultId  srId=new Alm_ResultId(sr.getReportId(),sr.getItemCode());
		Alm_Result sc =sev_ResultDao.get(Alm_Result.class, srId);
		System.out.println(sc);
		//sc.setRegulatoryScore(sr.getRegulatoryScore());
		//sc.setRegulatoryTemp(sr.getRegulatoryTemp());
		sev_ResultDao.update(sc);
	}
	//提交 修改单号表的里flag
	@Transactional
	public void tijiao(String reportId){
		Alm_ReportInfo sc =sev_ReportInfoDao.get(Alm_ReportInfo.class, reportId);
		System.out.println(sc);
		sc.setFlag("3");
		sev_ReportInfoDao.update(sc);
	}
	@SuppressWarnings("unchecked")
	@Transactional
	public String gettype() {
		String sql="select distinct t.reportid reportId from sev_result t  order by reportId desc ";
		List<Alm_ResultDTO> sc=(List<Alm_ResultDTO>)sev_ResultDao.queryBysql(sql,Alm_ResultDTO.class);	
		String sql1="select s.flag from sev_reportinfo s where s.reportid='"+sc.get(0).getReportId()+"' and s.backtype=0";
		List<Alm_ReportInfoDTO> si=(List<Alm_ReportInfoDTO>)sev_ReportInfoDao.queryBysql(sql1,Alm_ReportInfoDTO.class);	
		if(si.get(0).getFlag()==2){
			return "##";
		}else if(si.get(0).getFlag()==3){
			return "###";
		}else{
			return null;
		}
	}
	//汇总调整页面数据展示
	@SuppressWarnings("unchecked")
	@Transactional
	public List<?> list(Alm_ResultDTO sr) {
		StringBuffer querySql = new StringBuffer();
		List<Alm_ResultDTO> list= new ArrayList<Alm_ResultDTO>();
		try{			
			querySql.append("select t.itemcode ,s.reportid,s.adjustOpinion,t.itemnum itemNum,t.itemname itemName,t.standardscore standardScore,t.sysintegrityscore sysIntegrityScore,");
			querySql.append("s.integritysevresult integritySevResult,s.integrityscore integrityScore,t.folefficiencyscore folEfficiencyScore,t.itemType, ");
			querySql.append("s.efficiencysevresult efficiencySevResult,s.efficiencyscore efficiencyScore,s.subtotalscore subtotalScore,s.sevbase SevBase, ");
			querySql.append(" (select c.codename from cfcodemanage c where c.codecode=s.integritysevresult and c.codetype='EvaluateResult') as intResult, ");
			querySql.append(" (select c.codename from cfcodemanage c where c.codecode=s.efficiencysevresult and c.codetype='EvaluateResult') as effResult ");			
			querySql.append(" from alm_itemdefine t ");		
			querySql.append("left join (select c.reportid,c.integritysevresult integritySevResult, c.integrityscore integrityScore,c.itemcode itemcode,");
			querySql.append("c.efficiencysevresult efficiencySevResult,c.efficiencyscore efficiencyScore,c.subtotalscore subtotalScore,c.sevbase SevBase,c.adjustOpinion  ");
			querySql.append("from alm_result c where c.reportid='"+sr.getReportId()+"') s on t.itemcode=s.itemcode ");
			querySql.append(" where t.projectcode='"+sr.getProjectCode()+"' and t.itemType in('0','3')");
			querySql.append(" order by t.itemcode");
			System.out.println(querySql);
			list=(List<Alm_ResultDTO>) sev_ResultDao.queryBysql(querySql.toString(),Alm_ResultDTO.class);
			return list;		
		}catch(Exception e){
			LOGGER.error("汇总调整数据查询失败", e);
			return list;
		}
		
	}
	//结果反馈根据展示信息选择部门进行调整信息录入
	@SuppressWarnings("unchecked")
	@Transactional
	public List<?> listfeedback(Alm_ResultDTO sr) {
		StringBuffer querySql = new StringBuffer();
		List<Alm_ResultDTO> list =new ArrayList<Alm_ResultDTO>();
		try{
			//判断需tag=2才是可以修改的报告单号
			//System.out.println(sr.getReportId());
			String sql="select reportId from sev_reportinfo t where t.flag in ('2','3') and t.backtype=0  order by reportId desc ";
			List<Alm_ReportInfoDTO> sc=(List<Alm_ReportInfoDTO>) sev_ReportInfoDao.queryBysql(sql,Alm_ReportInfoDTO.class);
			if(sc!=null&&!sc.isEmpty()&&sc.size()>0){
				if(sr.getProjectCode()!=null){
					querySql.append("select t.itemcode ,s.reportid,t.itemnum itemNum,t.itemname itemName,t.standardscore standardScore,t.sysintegrityscore sysIntegrityScore,");
					querySql.append("s.integritysevresult integritySevResult,s.integrityscore integrityScore,t.folefficiencyscore folEfficiencyScore,");
					querySql.append("s.efficiencysevresult efficiencySevResult,s.efficiencyscore efficiencyScore,s.subtotalscore subtotalScore,");
					querySql.append("t.deptno deptNo,s.adjustopinion adjustOpinion,s.regulatoryScore regulatoryScore,s.regulatoryTemp regulatoryTemp ");
					querySql.append(" from sev_itemdefine t ");
					querySql.append("left join (select c.reportid,c.integritysevresult integritySevResult, c.integrityscore integrityScore,c.itemcode itemcode,");
					querySql.append("c.efficiencysevresult efficiencySevResult,c.efficiencyscore efficiencyScore,c.subtotalscore subtotalScore,");
					querySql.append("c.adjustopinion adjustOpinion,c.regulatoryScore regulatoryScore,c.regulatoryTemp regulatoryTemp ");
					querySql.append(" from sev_result c where c.reportid='"+sc.get(0).getReportId()+"') s on t.itemcode=s.itemcode ");
					querySql.append(" where t.projectcode='"+sr.getProjectCode()+"' and t.itemType=0");
					querySql.append(" order by t.itemcode");
				}else{
					querySql.append("select t.itemcode ,s.reportid,t.itemnum itemNum,t.itemname itemName,t.standardscore standardScore,t.sysintegrityscore sysIntegrityScore,");
					querySql.append("s.integritysevresult integritySevResult,s.integrityscore integrityScore,t.folefficiencyscore folEfficiencyScore,");
					querySql.append("s.efficiencysevresult efficiencySevResult,s.efficiencyscore efficiencyScore,s.subtotalscore subtotalScore,");
					querySql.append("t.deptno deptNo,s.adjustopinion adjustOpinion,s.regulatoryScore regulatoryScore,s.regulatoryTemp regulatoryTemp ");
					querySql.append(" from sev_itemdefine t ");
					querySql.append("left join (select c.reportid,c.integritysevresult integritySevResult, c.integrityscore integrityScore,c.itemcode itemcode,");
					querySql.append("c.efficiencysevresult efficiencySevResult,c.efficiencyscore efficiencyScore,c.subtotalscore subtotalScore,");
					querySql.append("c.adjustopinion adjustOpinion,c.regulatoryScore regulatoryScore,c.regulatoryTemp regulatoryTemp ");
					querySql.append(" from sev_result c where c.reportid='"+sc.get(0).getReportId()+"') s on t.itemcode=s.itemcode");
					querySql.append(" where t.projectcode='p01' and t.itemType=0");
					querySql.append("order by t.itemcode");
				}
				System.out.println(querySql);
				list=(List<Alm_ResultDTO>) sev_ResultDao.queryBysql(querySql.toString(),Alm_ResultDTO.class);
				return list;
			}else{
				return list;
			}
		}catch(Exception e){
			LOGGER.error("结果反馈页面查询失败", e);
			return list;
		}
	}
	@SuppressWarnings("unchecked")
	@Transactional
	public List<?> listfeedback2(Alm_ResultDTO sr) {
		StringBuffer querySql = new StringBuffer();
		List<Alm_ResultDTO> list =new ArrayList<Alm_ResultDTO>();
		//判断需tag=2才是可以修改的报告单号
		//System.out.println(sr.getReportId());
		String sql="select reportId from sev_reportinfo t where t.flag =2 order by reportId desc ";
		List<Alm_ReportInfoDTO> sc=(List<Alm_ReportInfoDTO>) sev_ReportInfoDao.queryBysql(sql,Alm_ReportInfoDTO.class);
		if(sc!=null&&!sc.isEmpty()&&sc.size()>0){
			if(sr.getProjectCode()!=null){
				querySql.append("select t.itemcode ,s.reportid,t.itemnum itemNum,t.itemname itemName,t.standardscore standardScore,t.sysintegrityscore sysIntegrityScore,");
				querySql.append("s.integritysevresult integritySevResult,s.integrityscore integrityScore,t.folefficiencyscore folEfficiencyScore,");
				querySql.append("s.efficiencysevresult efficiencySevResult,s.efficiencyscore efficiencyScore,s.subtotalscore subtotalScore,");
				querySql.append("t.deptno deptNo,s.adjustopinion adjustOpinion,s.regulatoryScore regulatoryScore,s.regulatoryTemp regulatoryTemp ");
				querySql.append(" from sev_itemdefine t ");
				querySql.append("left join (select c.reportid,c.integritysevresult integritySevResult, c.integrityscore integrityScore,c.itemcode itemcode,");
				querySql.append("c.efficiencysevresult efficiencySevResult,c.efficiencyscore efficiencyScore,c.subtotalscore subtotalScore,");
				querySql.append("c.adjustopinion adjustOpinion,c.regulatoryScore regulatoryScore,c.regulatoryTemp regulatoryTemp ");
				querySql.append(" from sev_result c where c.reportid='"+sc.get(0).getReportId()+"') s on t.itemcode=s.itemcode ");
				querySql.append(" where t.projectcode='"+sr.getProjectCode()+"' and t.itemType=0");
				querySql.append(" order by t.itemcode");
			}else{
				querySql.append("select t.itemcode ,s.reportid,t.itemnum itemNum,t.itemname itemName,t.standardscore standardScore,t.sysintegrityscore sysIntegrityScore,");
				querySql.append("s.integritysevresult integritySevResult,s.integrityscore integrityScore,t.folefficiencyscore folEfficiencyScore,");
				querySql.append("s.efficiencysevresult efficiencySevResult,s.efficiencyscore efficiencyScore,s.subtotalscore subtotalScore,");
				querySql.append("t.deptno deptNo,s.adjustopinion adjustOpinion,s.regulatoryScore regulatoryScore,s.regulatoryTemp regulatoryTemp ");
				querySql.append(" from sev_itemdefine t ");
				querySql.append("left join (select c.reportid,c.integritysevresult integritySevResult, c.integrityscore integrityScore,c.itemcode itemcode,");
				querySql.append("c.efficiencysevresult efficiencySevResult,c.efficiencyscore efficiencyScore,c.subtotalscore subtotalScore,");
				querySql.append("c.adjustopinion adjustOpinion,c.regulatoryScore regulatoryScore,c.regulatoryTemp regulatoryTemp ");
				querySql.append(" from sev_result c where c.reportid='"+sc.get(0).getReportId()+"') s on t.itemcode=s.itemcode");
				querySql.append(" where t.projectcode='p01' and t.itemType=0");
				querySql.append("order by t.itemcode");
			}
			System.out.println(querySql);
			list=(List<Alm_ResultDTO>) sev_ResultDao.queryBysql(querySql.toString(),Alm_ResultDTO.class);
			return list;
		}else{
			return list;
		}
	}
	//根据评估单号和项目编号来查询所有的指标明细
	@SuppressWarnings("unchecked")
	@Transactional
	public List<?> histroy(String projectCode,String reportId) {
		StringBuffer querySql = new StringBuffer();
		try{
		if(projectCode.equals("p00")){
			querySql.append("select t.*,(select c.codename from cfcodemanage c where c.codecode=t.projectcode and c.codetype='projectCode') as projectName ");
			querySql.append("from alm_gatherinfo t where t.reportid='"+reportId+"' and t.adjusttype='2' order by t.projectcode");
			System.out.println(querySql);
			List<Alm_GatherInfoDTO> list =(List<Alm_GatherInfoDTO>) sev_GatherInfoDao.queryBysql(querySql.toString(),Alm_GatherInfoDTO.class);
			list.get(list.size()-1).setProjectName("分值合计");
			return list;
		}else{
			querySql.append("select t.itemnum itemNum,t.itemname itemName,t.standardscore standardScore,t.sysintegrityscore sysIntegrityScore,");
			querySql.append("s.integritysevresult integritySevResult,s.integrityscore integrityScore,t.folefficiencyscore folEfficiencyScore,");
			querySql.append("s.efficiencysevresult efficiencySevResult,s.efficiencyscore efficiencyScore,s.subtotalscore subtotalScore,s.sevbase SevBase, ");
			querySql.append(" (select c.codename from cfcodemanage c where c.codecode=s.integritysevresult and c.codetype='EvaluateResult') as intResult, ");
			querySql.append(" (select c.codename from cfcodemanage c where c.codecode=s.efficiencysevresult and c.codetype='EvaluateResult') as effResult ");
			querySql.append(" from alm_itemdefine t ");
			querySql.append("left join (select c.integritysevresult integritySevResult, c.integrityscore integrityScore,c.itemcode itemcode,");
			querySql.append("c.efficiencysevresult efficiencySevResult,c.efficiencyscore efficiencyScore,c.subtotalscore subtotalScore,c.sevbase SevBase ");
			querySql.append("from alm_result c where c.reportid='"+ reportId+"') s on t.itemcode=s.itemcode");
			querySql.append(" where t.projectcode='" + projectCode+ "'");
			querySql.append(" order by t.itemcode");	
			System.out.println(querySql);
			List<Alm_ComputeResultDTO> list= (List<Alm_ComputeResultDTO>) sev_ResultDao.queryBysql(querySql.toString(),Alm_ComputeResultDTO.class);
			return list;
		}
		}catch(Exception e){
			LOGGER.error("查询失败", e);	
			return null;
		}
	}
		//进行调整修改结果
		@Transactional
		public void edit(Alm_ResultDTO sev) {
			try{
				Alm_ResultId  srId=new Alm_ResultId(sev.getReportId(),sev.getItemCode());
				Alm_Result sr =sev_ResultDao.get(Alm_Result.class, srId);
				sr.setIntegritySevResult(sev.getIntegritySevResult());
				sr.setEfficiencySevResult(sev.getEfficiencySevResult());
				sr.setAdjustOpinion(sev.getAdjustOpinion());
				if(sr.getItemType()==0){
					if(sr.getIntegritySevResult()==1){
						sr.setIntegrityScore(BigDecimal.valueOf(sev.getSysIntegrityScore()));	
					}else if(sr.getIntegritySevResult()==2){
						BigDecimal uu1=BigDecimal.valueOf(sev.getSysIntegrityScore());
						sr.setIntegrityScore(uu1.multiply(BigDecimal.valueOf(0.8)));
					}else if(sr.getIntegritySevResult()==3){
						BigDecimal uu1=BigDecimal.valueOf(sev.getSysIntegrityScore());
						sr.setIntegrityScore(uu1.multiply(BigDecimal.valueOf(0.5)));
					}else if(sr.getIntegritySevResult()==4){
						sr.setIntegrityScore(BigDecimal.valueOf(0));
					}else{
						sr.setIntegrityScore(BigDecimal.valueOf(0));
					}
					if(sr.getEfficiencySevResult()==1){
						sr.setEfficiencyScore(BigDecimal.valueOf(sev.getFolEfficiencyScore()));	
					}else if(sr.getEfficiencySevResult()==2){
						BigDecimal uu1=BigDecimal.valueOf(sev.getFolEfficiencyScore());
						sr.setEfficiencyScore(uu1.multiply(BigDecimal.valueOf(0.8)));
					}else if(sr.getEfficiencySevResult()==3){
						BigDecimal uu1=BigDecimal.valueOf(sev.getFolEfficiencyScore());
						sr.setEfficiencyScore(uu1.multiply(BigDecimal.valueOf(0.5)));
					}else if(sr.getEfficiencySevResult()==4){
						sr.setEfficiencyScore(BigDecimal.valueOf(0));
					}else{
						sr.setEfficiencyScore(BigDecimal.valueOf(0));
					}
					sr.setSubtotalScore(sr.getIntegrityScore().add(sr.getEfficiencyScore()));
				}else{
					if(sev.getIntegritySevResult()==1&&sev.getEfficiencySevResult()==1){
						sr.setSubtotalScore(BigDecimal.valueOf(sev.getStandardScore()));
					}else{
						sr.setSubtotalScore(BigDecimal.valueOf(0));
					}
				}			
				sev_ResultDao.update(sr);
			}catch(Exception e){
				LOGGER.error("进行调整修改结果失败", e);
			}
		}
	@Transactional
	public void choosedept(Alm_ResultDTO sr) {
		System.out.println(sr.getReportId()+"###"+sr.getItemCode());
		Alm_ResultId  srId=new Alm_ResultId(sr.getReportId(),sr.getItemCode());
		Alm_Result sc =sev_ResultDao.get(Alm_Result.class, srId);
		System.out.println(sc);
		//sc.setAdjustOpinion(sr.getAdjustOpinion());
		//sc.setDeptNo(sr.getDeptNo());
		sev_ResultDao.update(sc);
	}
	//导出数据以excel文件的格式
	@SuppressWarnings("unchecked")
	@Override
	public void download(HttpServletRequest request, HttpServletResponse response, String reportId) {
		try{
			ExcelUtil excelUtil = new ExcelUtil();
			StringBuffer querySql1 = new StringBuffer();
			querySql1.append("select t.integrityscore integrityScore,t.efficiencyscore efficiencyScore,t.subtotalscore subtotalScore,t.adjsubtotalscore adjSubtotalScore,t.score score "); 
			querySql1.append("from sev_gatherinfo t left join codemanage c on c.codecode=t.projectcode where t.reportid='"+reportId+"'and t.type=1 and t.adjusttype='at2' order by t.projectcode");
			System.out.println(querySql1);
			List<Alm_GatherInfoDTO> datas1 =(List<Alm_GatherInfoDTO>) sev_GatherInfoDao.queryBysql(querySql1.toString(),Alm_GatherInfoDTO.class);
			StringBuffer querySql2=new StringBuffer();
			querySql2.append("select t.itemcode,(select c.codename from codemanage c where c.codecode=t.integritysevresult and c.codetype='EvaluateResult') as intResult,");
			querySql2.append("t.integrityscore,(select c.codename from codemanage c where c.codecode=t.efficiencysevresult and c.codetype='EvaluateResult') as effResult, ");
			querySql2.append("t.efficiencyscore,t.subtotalscore,t.sevbase from sev_result t where t.reportid='"+reportId+"' order by t.itemcode");
			System.out.println(querySql2);
			List<Alm_ComputeResultDTO> datas2= (List<Alm_ComputeResultDTO>) sev_ResultDao.queryBysql(querySql2.toString(),Alm_ComputeResultDTO.class);
			//excelUtil.export3(request, response,datas1,datas2);
		}catch(Exception e){
			LOGGER.error("结果导出失败", e);
		}
	}
	//发送提醒给部门联系人
	@Transactional
	public void tixing(String reportId) {
		try{
			String list = deptNo();
			System.out.println(list);
			List<String> recipients=new ArrayList<String>();
			String sql="select s.email as deptNo from userinfo s left join act_id_membership a on a.user_id_=s.id where a.group_id_='e01' and s.deptcode in('"+list+"')";
			List<Alm_ReportDataDTO> srd=(List<Alm_ReportDataDTO>) sev_ReportDataDao.queryBysql(sql,Alm_ReportDataDTO.class);
			for(Alm_ReportDataDTO srdd:srd){
				recipients.add(srdd.getDeptNo());
			}
			System.out.println(recipients);
			EmailUtil eu=new EmailUtil();
			String subject = "11号文反馈结果";
			String msg = "您有改进方案需要填写，请登录系统填写";		
			//eu.sendEmail(recipients,msg,subject);
			
			//设置单号表反馈类型 
			Alm_ReportInfo sr=sev_ReportInfoDao.get(Alm_ReportInfo.class,reportId);
			sr.setBackType("1");
			sev_ReportInfoDao.update(sr);
			
			//把需要改进的信息传到评估改进方案表中 进行改进方案录入
			String sql1="select t.reportid,t.itemcode,t.deptno from sev_result t where t.deptno is not null and t.reportid='"+reportId+"'";
			List<Alm_ResultDTO> srdto=(List<Alm_ResultDTO>) sev_ResultDao.queryBysql(sql1,Alm_ResultDTO.class);
			for(Alm_ResultDTO sd:srdto){
				String[] str=sd.getDeptNo().split(",");
				for(int i=0;i<str.length;i++){
					Alm_AdjPlan sap=new Alm_AdjPlan();
					sap.setReportId(sd.getReportId());
					sap.setItemCode(sd.getItemCode());
					sap.setDeptNo(str[i]);
					sap.setAdjStatus("es1");
					sev_ReportDataDao.save(sap);
				}
			}
		}catch(Exception e){
			LOGGER.error("发起改进方案录入失败", e);
		}
	}
	@Transactional
	public String deptNo() {
		String sql = " select DeptNo from sev_result ";
		//List<Sev_ItemDefine> list= (List<Sev_ItemDefine>) sev_ItemDefineDao.queryByhsql(sql);
		List<?> list= sev_ResultDao.queryBysql(sql);
		StringBuffer strBuffer = new StringBuffer();
		for(int i=0;i<list.size();i++){
			//System.out.println(list.get(i).toString().indexOf("null"));
			if(list.get(i).toString().indexOf("null")==-1){
				//System.out.println(list.get(i).toString());
				String str =list.get(i).toString().substring(8,list.get(i).toString().length()-1);
				//System.out.println(str);
				strBuffer.append(str);
				strBuffer.append(',');
			}
		}
		//System.out.println(strBuffer);
		return strBuffer.substring(0, strBuffer.length()-1).replaceAll(",","','");	
	}
	//进行提交 重新计算
	@SuppressWarnings("unchecked")
	@Transactional
	public void submit(String reportId) {
		LOGGER.error("计算开始");
		String[] strs1={"p0106","p0107","p0108"};
		calculationItem(reportId,0,33,strs1,"p01",0.2,3);
		//计算风险管理能力评估表 - 控制与流程
		String[] strs2={"p0203","p0204","p0205"};
		calculationItem(reportId,34,59,strs2,"p02",0.4,0);
		//计算风险管理能力评估表 - 模型与工具
		String[] strs3={"p0303","p0304","p0305"};
		calculationItem(reportId,60,85,strs3,"p03",0.2,4);
		//计算风险管理能力评估表 - 绩效考核
		String[] strs4={"p0402","p0403","p0404"};
		calculationItem(reportId,86,96,strs4,"p04",0.1,3);
		//计算风险管理能力评估表 - 资产负债管理报告
		String[] strs5={"p0502","p0503","p0504"};
		calculationItem(reportId,97,104,strs5,"p05",0.1,0);
		LOGGER.error("计算结束");
		//评估结果分析页面数据计算
		sev_ResultDao.flush();
		
		String querySql="select * from alm_gatherinfo a where a.reportid='"+reportId+"' and a.adjusttype='2'";
		List<Alm_GatherInfoDTO> scyx=(List<Alm_GatherInfoDTO>)sev_ResultDao.queryBysql(querySql,Alm_GatherInfoDTO.class);
		System.out.println(scyx.size());
		BigDecimal dou1=BigDecimal.valueOf(0);//基础能力得分小计
		BigDecimal dou2=BigDecimal.valueOf(0);//提升能力得分小计
		BigDecimal dou3=BigDecimal.valueOf(0);//最终得分小计
		for(Alm_GatherInfoDTO yx:scyx){
			dou1=dou1.add(BigDecimal.valueOf(yx.getJcscore()));
			dou2=dou2.add(BigDecimal.valueOf(yx.getTsscore()));
			dou3=dou3.add(BigDecimal.valueOf(yx.getRate()));
		}
		Alm_GatherInfo sg =new Alm_GatherInfo();
		sg.setReportId(reportId);
		sg.setWeight("100%");
		sg.setJcscore(dou1);
		sg.setTsscore(dou2);
		sg.setRate(dou3);
		sg.setAdjustType("2");
		sev_GatherInfoDao.save(sg);
		
		//修改单号状态
		CalCfReportInfo cal = sev_ReportDataDao.get(CalCfReportInfo.class,reportId);
		cal.setFlag("4");
		sev_ReportDataDao.update(cal);
		System.out.println("报告单号状态修改完成");
	}
	@SuppressWarnings("unchecked")
	@Transactional
	public InvokeResult tongguo(String reportId) {
		try{
			String sql="select t.id_ as taskId,(select sevStatus from sev_reportInfo where reportId='"+reportId+"') as sevStatus from act_ru_task t where t.proc_inst_id_=(select instanceId from sev_reportInfo where reportId='"+reportId+"')";
			List<Alm_ReportDataDTO> si=(List<Alm_ReportDataDTO>)sev_ReportDataDao.queryBysql(sql,Alm_ReportDataDTO.class);
			String option="";
			//第一个参数 事先传到前台的taskid 第二个 参数是意见 第三个是登录人的code 第四个是流名称1
			activitiService.addCommentByTaskId(si.get(0).getTaskId(),option,CurrentUser.getUserId()+"","汇总调整审批");
			//设置流继续前进 最后一个true 代表是同意
			activitiService.completeTaskByTaskId(si.get(0).getTaskId(),CurrentUser.getUserId()+"",true);
			
			//设置报告单表的报送状态为已生成报告
			System.out.println(reportId);
			Alm_ReportInfo sev = sev_ReportInfoDao.get(Alm_ReportInfo.class,reportId);
			System.out.println(sev);
			sev.setFlag("2");
			System.out.println(sev.getFlag());
			sev_ReportInfoDao.update(sev);	
			System.out.println("报告单号类型修改完成");
			return InvokeResult.success("审核完成");
		}catch(Exception e){
			e.printStackTrace();
		}
		return InvokeResult.success("审批完成");

	}
	@SuppressWarnings("unchecked")
	@Transactional
	public InvokeResult fanhuixiugai(Alm_ReportDataDTO srd,String reportId) {
		try{
			System.out.println(srd.getAuditorOpinion());
			String sql="select t.id_ as taskId,(select sevStatus from sev_reportInfo where reportId='"+reportId+"') as sevStatus from act_ru_task t where t.proc_inst_id_=(select instanceId from sev_reportInfo where reportId='"+reportId+"')";
			List<Alm_ReportDataDTO> si=(List<Alm_ReportDataDTO>)sev_ReportDataDao.queryBysql(sql,Alm_ReportDataDTO.class);
			//第一个参数 事先传到前台的taskid 第二个 参数是意见 第三个是登录人的code 第四个是流名称1
			activitiService.addCommentByTaskId(si.get(0).getTaskId(),srd.getAuditorOpinion(),CurrentUser.getUserId()+"","汇总调整审批");
			//设置流继续前进 最后一个true 代表是同意
			activitiService.completeTaskByTaskId(si.get(0).getTaskId(),CurrentUser.getUserId()+"",false);
			
			Alm_ReportInfo sev = sev_ReportInfoDao.get(Alm_ReportInfo.class,reportId);
			
			List<String> recipients=new ArrayList<String>();
			String em="select s.email as deptNo from userinfo s left join act_id_membership a on a.user_id_=to_char(s.id) where a.group_id_='riskcontact' and s.useflag='1'";
			List<Alm_ReportDataDTO> srdd=(List<Alm_ReportDataDTO>) sev_ItemDefineDao.queryBysql(em,Alm_ReportDataDTO.class);
			if(srdd.size()!=0&&!srdd.get(0).getDeptNo().equals("")){
				recipients.add(srdd.get(0).getDeptNo());
				String url = Config.getProperty("sysUrl");
				EmailUtil eu=new EmailUtil();
				StringBuffer message = new StringBuffer("");
				String subject = "11号文自评估";	
				message.append("<html>"+"\n"+"<table align='center' cellpadding='3' cellspacing='0'>");			
				message.append("<tr><td align='center' width='1000' colspan=5><h3>"+sev.getYear()+"年度风险管理自评估汇总调整</h3></td></tr>"+"\n");			
				message.append("<tr><td colspan=5 width='1000'>风险管理部联系人：</td></tr>"+"\n");
				message.append("<tr><td colspan=5 width='1000'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您提交的汇总调整被打回。</td></tr>"+"\n");
				message.append("<tr><td colspan=5 width='1000'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;打回原因为:"+srd.getAuditorOpinion()+"</td></tr>"+"\n");	
				message.append("<tr><td colspan=5 width='1000'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请点击如下链接，登录风险管理系统重新调整。</td></tr>"+"\n");
				message.append("<tr><td colspan=5 width='1000'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='"+url+"?emailType=#'>"+url+"</a></td></tr>"+"\n");
				message.append("</table>"+"\n"+"</html>");
				//eu.sendHtmlMail(recipients,subject,message.toString());
			}else{
				return InvokeResult.success("审核完成,但是未查询到提醒者邮箱！");
			}
			Alm_ReportInfo ser = sev_ReportInfoDao.get(Alm_ReportInfo.class,reportId);
			System.out.println("获取到单号信息"+ser);
			ser.setSevstatus("0");
			sev_ReportInfoDao.update(ser);	
			return InvokeResult.success("审核完成");
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return InvokeResult.success("审批完成");
	}
	/**
	 * 为待办的任务设置办理人
	 */
	@Transactional
	public void setTaskAssign(String processInstanceId,String userCode) {
		try {
			List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
        	if(tasks!=null && tasks.size()>0){
        		activitiService.TransTaskByTaskId(tasks.get(0).getId(),userCode);
        	}
		}catch(Exception e){
        	e.printStackTrace();
        	LOGGER.error(e.getMessage());
        } 
	}
	@SuppressWarnings("unchecked")
	@Transactional
	public void calculationItem(String reportId,int begin,int end,String[] strs,String projectCode,Double weight,int score){
		try{
			String sql="select t.itemcode from alm_result t where t.reportid='"+reportId+"' and t.projectCode='"+projectCode+"' and t.itemtype in ('0','3') order by t.itemcode";
			List<Alm_ResultDTO> sev=(List<Alm_ResultDTO>)sev_ResultDao.queryBysql(sql,Alm_ResultDTO.class);
			BigDecimal sum1=BigDecimal.valueOf(0);//统计不适用的分数
			BigDecimal sum2=BigDecimal.valueOf(0);//统计健全性所得分数
			BigDecimal sum3=BigDecimal.valueOf(0);//统计有效性所得分数
			BigDecimal sum4=BigDecimal.valueOf(0);//统提升能力所得分数
			for (Alm_ResultDTO dto:sev) {
				Alm_ResultId  scId=new Alm_ResultId(reportId,dto.getItemCode());
				Alm_Result sc =sev_ResultDao.get(Alm_Result.class, scId);
				if(sc.getItemType()==0){
					if(sc.getIntegritySevResult()==5){
						String sql1="select t.sysintegrityscore sysIntegrityScore from alm_itemdefine t where t.itemcode='"+dto.getItemCode()+"'";
						List<Alm_ItemDefineDTO> si=(List<Alm_ItemDefineDTO>)sev_ItemDefineDao.queryBysql(sql1,Alm_ItemDefineDTO.class);
						sum1=sum1.add(BigDecimal.valueOf(si.get(0).getSysIntegrityScore()));
					}else{
						sum2=sum2.add(sc.getIntegrityScore());
					}
					if(sc.getEfficiencySevResult()==5){
						String sql1="select t.folefficiencyscore folEfficiencyScore from alm_itemdefine t where t.itemcode='"+dto.getItemCode()+"'";
						List<Alm_ItemDefineDTO> si=(List<Alm_ItemDefineDTO>)sev_ItemDefineDao.queryBysql(sql1,Alm_ItemDefineDTO.class);
						sum1=sum1.add(BigDecimal.valueOf(si.get(0).getFolEfficiencyScore()));
					}else{
						sum3=sum3.add(sc.getEfficiencyScore());
					}
				}
				else{
					sum4=sum4.add(sc.getSubtotalScore());
				}
			}
			System.out.println("不适用得分："+sum1+"健全性得分："+sum2+"有效性得分："+sum3+"提升能力得分："+sum4);
			
			//对最后的三列总结项进行储存
			Alm_ResultId  scId=new Alm_ResultId(reportId,strs[0]);
			Alm_Result sc =sev_ResultDao.get(Alm_Result.class, scId);
			sc.setIntegrityScore(sum2);
			sc.setEfficiencyScore(sum3);
			sc.setSubtotalScore(sum2.add(sum3));
			sev_ResultDao.update(sc);
			Alm_ResultId  scId1=new Alm_ResultId(reportId,strs[1]);
			Alm_Result sc1 =sev_ResultDao.get(Alm_Result.class, scId1);
			sc1.setSubtotalScore(sum4);
			sev_ResultDao.update(sc1);
			Alm_ResultId  scId2=new Alm_ResultId(reportId,strs[2]);
			Alm_Result sc2 =sev_ResultDao.get(Alm_Result.class, scId2);
			sc2.setSubtotalScore(sum1);
			sev_ResultDao.update(sc2);
			
			//给结果分析表内存数据
			Alm_GatherInfo sg =new Alm_GatherInfo();
			sg.setReportId(reportId);
			sg.setProjectCode(projectCode);
			sg.setJcstandardScore(BigDecimal.valueOf(100));
			sg.setIntegrityScore(sum2);
			sg.setEfficiencyScore(sum3);
			sg.setSubtotalScore(sum2.add(sum3));
			sg.setAdjScore(sum1);
			//汇总调整后得分计算公式：(得分小计)/(100-不适用得分)*100
			//100
			BigDecimal uu1=BigDecimal.valueOf(100);
			//100-不适用得分
			BigDecimal uu2=BigDecimal.valueOf(100).subtract(sum1);
			//调整系数=(100)/(100-不适用得分)
			BigDecimal uu3=uu1.divide(uu2,4,BigDecimal.ROUND_HALF_UP);
			sg.setAdjSubtotalScore(sg.getSubtotalScore().multiply(uu3));
			if(weight==0.1){
				sg.setWeight("10%");
				sg.setJcscore(sg.getAdjSubtotalScore().multiply(BigDecimal.valueOf(0.1)));
			}else if(weight==0.2){
				sg.setWeight("20%");
				sg.setJcscore(sg.getAdjSubtotalScore().multiply(BigDecimal.valueOf(0.2)));
			}else if(weight==0.4){
				sg.setWeight("40%");
				sg.setJcscore(sg.getAdjSubtotalScore().multiply(BigDecimal.valueOf(0.4)));
			}
			sg.setTsstandardScore(BigDecimal.valueOf(score));
			sg.setTsscore(sum4);
			sg.setRate(sg.getJcscore().add(sg.getTsscore()));
			sg.setAdjustType("2");
			sev_GatherInfoDao.save(sg);
			System.out.println("该表计算完成");
		}catch(Exception e){
			LOGGER.error("数据计算失败", e);
		}
	}
	//根据评估单号和项目编号来查询所有的指标明细
	@SuppressWarnings("unchecked")
	@Transactional
	public List<?> listhistroy(String projectCode,String reportId) {
		StringBuffer querySql = new StringBuffer();
		//需要判断两个type 一个是type 还有一个是adjustType
		try{
			if(projectCode.equals("p00")){
				querySql.append("select c.codename projectName,t.standardscore standardScore,t.integrityscore integrityScore,t.efficiencyscore efficiencyScore,t.subtotalscore subtotalScore,t.adjsubtotalscore adjSubtotalScore,t.weight weight,t.score score "); 
				querySql.append("from sev_gatherinfo t left join codemanage c on c.codecode=t.projectcode where t.reportid='"+reportId+"'and t.type=1 and t.adjusttype='at2' order by t.projectcode");
				System.out.println(querySql);
				List<Alm_GatherInfoDTO> list =(List<Alm_GatherInfoDTO>) sev_GatherInfoDao.queryBysql(querySql.toString(),Alm_GatherInfoDTO.class);
				if(list!=null&&!list.isEmpty()&&list.size()>0){
					list.get(list.size()-1).setProjectName("汇总");
				}
				return list;
			}else{
				querySql.append("select t.itemnum itemNum,t.itemname itemName,t.standardscore standardScore,t.sysintegrityscore sysIntegrityScore,");
				querySql.append("s.integritysevresult integritySevResult,s.integrityscore integrityScore,t.folefficiencyscore folEfficiencyScore,");
				querySql.append("s.efficiencysevresult efficiencySevResult,s.efficiencyscore efficiencyScore,s.subtotalscore subtotalScore,s.sevbase SevBase ");
				querySql.append(" from sev_itemdefine t ");
				querySql.append("left join (select c.integritysevresult integritySevResult, c.integrityscore integrityScore,c.itemcode itemcode,");
				querySql.append("c.efficiencysevresult efficiencySevResult,c.efficiencyscore efficiencyScore,c.subtotalscore subtotalScore,c.sevbase SevBase ");
				querySql.append("from sev_result c where c.reportid='"+ reportId+"') s on t.itemcode=s.itemcode");
				querySql.append(" where t.projectcode='" + projectCode+ "'");
				querySql.append(" order by t.itemcode");	
				System.out.println(querySql);
				List<Alm_ResultDTO> list= (List<Alm_ResultDTO>) sev_ResultDao.queryBysql(querySql.toString(),Alm_ResultDTO.class);
				return list;
			}		
		}catch(Exception e){
			LOGGER.error("根据评估单号和项目编号来查询所有的指标明细失败", e);
			return null;	
		}
	}
	//汇总调整审批页面信息查询
	@SuppressWarnings("unchecked")
	@Transactional
	public List<?> listApproval(Alm_ResultDTO sr) {
		String instanceId="";
		int page=1;
		int rows=10;
		UserInfo userInfo = (UserInfo) CurrentUser.getCurrentUser();			
		List<String> list1=activitiService.findMyTaskReturnTaskId(page, rows, userInfo);
		if(list1.size()!=0){
			for(String taskId:list1){
				//查处该用户所有属于他的流Id进行数据检索d
				String processInstanceId=activitiService.getProcessIdByTaskId(taskId);
				instanceId=instanceId+processInstanceId+"','";
				System.out.println(instanceId+"####流ID");
			}
			instanceId=instanceId.substring(0, instanceId.length()-3);
			System.out.println(instanceId);
		}
		StringBuffer querySql = new StringBuffer();
		List<Alm_ResultDTO> list= new ArrayList<Alm_ResultDTO>();
		//判断需tag=1才是可以修改的报告单号
		String sql="select reportId from sev_reportinfo t  where t.flag=1 order by reportId desc ";
		List<Alm_ReportInfoDTO> sc=(List<Alm_ReportInfoDTO>) sev_ReportInfoDao.queryBysql(sql,Alm_ReportInfoDTO.class);
		try{
			if(sc!=null&&!sc.isEmpty()&&sc.size()>0){
				if(sr.getProjectCode()!=null){
					querySql.append("select t.itemcode ,s.reportid,t.itemnum itemNum,t.itemname itemName,t.standardscore standardScore,t.sysintegrityscore sysIntegrityScore,");
					querySql.append("s.integritysevresult integritySevResult,s.integrityscore integrityScore,t.folefficiencyscore folEfficiencyScore,");
					querySql.append("s.efficiencysevresult efficiencySevResult,s.efficiencyscore efficiencyScore,s.subtotalscore subtotalScore,s.sevbase SevBase ");
					querySql.append(" from sev_itemdefine t ");
					querySql.append("left join (select c.reportid,c.integritysevresult integritySevResult, c.integrityscore integrityScore,c.itemcode itemcode,");
					querySql.append("c.efficiencysevresult efficiencySevResult,c.efficiencyscore efficiencyScore,c.subtotalscore subtotalScore,c.sevbase SevBase ");
					querySql.append("from sev_result c where c.reportid='"+sc.get(0).getReportId()+"') s on t.itemcode=s.itemcode ");
					querySql.append(" where t.projectcode='"+sr.getProjectCode()+"' and t.itemType=0");
					querySql.append(" and (select instanceId from sev_reportInfo where reportId='"+sc.get(0).getReportId()+"') in ('"+instanceId+"')");
					querySql.append(" order by t.itemcode");
				}else{
					querySql.append("select t.itemcode ,s.reportid,t.itemnum itemNum,t.itemname itemName,t.standardscore standardScore,t.sysintegrityscore sysIntegrityScore,");
					querySql.append("s.integritysevresult integritySevResult,s.integrityscore integrityScore,t.folefficiencyscore folEfficiencyScore,");
					querySql.append("s.efficiencysevresult efficiencySevResult,s.efficiencyscore efficiencyScore,s.subtotalscore subtotalScore,s.sevbase SevBase ");
					querySql.append(" from sev_itemdefine t ");
					querySql.append("left join (select c.reportid,c.integritysevresult integritySevResult, c.integrityscore integrityScore,c.itemcode itemcode,");
					querySql.append("c.efficiencysevresult efficiencySevResult,c.efficiencyscore efficiencyScore,c.subtotalscore subtotalScore,c.sevbase SevBase ");
					querySql.append(" from sev_result c where c.reportid='"+sc.get(0).getReportId()+"') s on t.itemcode=s.itemcode");
					querySql.append(" where t.projectcode='p01' and t.itemType=0");
					querySql.append(" and (select instanceId from sev_reportInfo where reportId='"+sc.get(0).getReportId()+"') in ('"+instanceId+"')");
					querySql.append("order by t.itemcode");
				}
				System.out.println("####结果调整审批sql####"+querySql);
				list=(List<Alm_ResultDTO>) sev_ResultDao.queryBysql(querySql.toString(),Alm_ResultDTO.class);
				return list;
			}else{
				return list;
			}
		}catch(Exception e){
			LOGGER.error("汇总调整数据查询失败", e);
			return list;
		}
	}
}