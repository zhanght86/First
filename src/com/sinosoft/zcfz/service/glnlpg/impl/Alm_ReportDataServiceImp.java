package com.sinosoft.zcfz.service.glnlpg.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sinosoft.common.Constant;
import com.sinosoft.common.CurrentUser;
import com.sinosoft.common.InvokeResult;
import com.sinosoft.common.Page;
import com.sinosoft.entity.UserInfo;
import com.sinosoft.util.Config;
import com.sinosoft.util.EmailUtil;
import com.sinosoft.util.ExcelUtil;
import com.sinosoft.zcfz.dao.glnlpg.Alm_ReportDataDao;
import com.sinosoft.zcfz.dao.glnlpg.Alm_ReportInfoDao;
import com.sinosoft.zcfz.dao.glnlpg.Alm_SevBaseDao;
import com.sinosoft.zcfz.dto.glnlpg.DataImportDTO;
import com.sinosoft.zcfz.dto.glnlpg.Alm_ReportDataDTO;
import com.sinosoft.zcfz.dto.glnlpg.Alm_ReportInfoDTO;
import com.sinosoft.zcfz.dto.glnlpg.Alm_SevBaseDTO;
import com.sinosoft.zcfz.dto.reportformcompute.CalCfReportInfoDTO;
import com.sinosoft.zcfz.entity.Alm_ReportData;
import com.sinosoft.zcfz.entity.Alm_ReportDataId;
import com.sinosoft.zcfz.entity.Alm_SevBase;
import com.sinosoft.zcfz.service.glnlpg.Alm_ReportDataService;
import com.sinosoft.zcfz.service.glnlpg.activiti.ActivitiService;
@Service
public class Alm_ReportDataServiceImp implements Alm_ReportDataService{
	private static final Logger LOGGER = LoggerFactory.getLogger(Alm_ReportDataServiceImp.class);
	@Resource
	private Alm_ReportDataDao evaluateInfoDao;
	@Resource
	private Alm_ReportInfoDao sev_ReportInfoDao;
	@Resource
	private Alm_SevBaseDao sev_SevBaseDao;
	@Autowired
	private IdentityService identityService;
	@Resource
	private ActivitiService activitiService;
	@Resource
	private TaskService taskService;
	//自评估审页面展示
	@SuppressWarnings("unchecked")
	@Transactional
	public List<?> qrySev_ReportDataDaoOfPage() {
		//System.out.println(CurrentUser.getUserAccount());
		//System.out.println(CurrentUser.getUserdept()); 获取部门id
		
		try{
			StringBuffer sb = new StringBuffer();
			//把登陆用户user对象传入可以查到隶属于他的流节点 从而方便查找 （但是其实已经有了逻辑查出来也可以不用这个）
			//List<String> list=activitiService.findMyTaskReturnTaskId(page, rows, CurrentUser.getCurrentUser());
			//System.out.println(list+"###taskId");
			//会查出很多 需要连接在一起 在sql语句中用in 还需要把taskid塞进list中 这样就不需要等通过或不通过时再查找taskid
//			for(String taskId:list){
//				String processInstanceId=activitiService.getProcessIdByTaskId(taskId);
//				sb.append(processInstanceId);
//			}
			String instanceId="";
			int page=1;
			int rows=1;
			UserInfo userInfo = (UserInfo) CurrentUser.getCurrentUser();			
			List<String> list=activitiService.findMySelfTaskReturnTaskId(page, rows, userInfo);
			if(list.size()!=0){
				for(String taskId:list){
					//查处该用户所有属于他的流Id进行数据检索
					String processInstanceId=activitiService.getProcessIdByTaskId(taskId);
					instanceId=instanceId+processInstanceId+"','";
					System.out.println(instanceId+"####流ID");
				}
				instanceId=instanceId.substring(0, instanceId.length()-3);
				System.out.println(instanceId);
			}	
			String deptId=CurrentUser.getUserdept();
			String querySql="select reportId from sev_reportinfo t where t.flag=0 order by reportId desc ";
			List<Alm_ReportInfoDTO> sc=(List<Alm_ReportInfoDTO>) sev_ReportInfoDao.queryBysql(querySql,Alm_ReportInfoDTO.class);
			if(sc!=null&&!sc.isEmpty()&&sc.size()>0){
				StringBuilder sql = new StringBuilder();
				sql.append("select s.ReportId             reportId,");
				sql.append("       s.ItemCode             itemCode,");
				sql.append("       s.IntegritySevResult   integritySevResult,");
				sql.append("       s.integrityscore       integrityScore,");
				sql.append("       s.EfficiencySevResult  efficiencySevResult,");
				sql.append("       s.EfficiencyScore      efficiencyScore,");
				sql.append("       (select c.codename from codemanage c");
				sql.append("       where c.codetype = 'EvaluateStatus' and c.codecode=s.sevstatus)                 sevStatusName,");
				sql.append("       s.Temp                 temp,");
				sql.append("       s.sevStatus            sevStatus,");
				sql.append("       (select i.itemname from sev_itemdefine i where i.itemcode=s.itemcode)           itemName,");
				sql.append("       (select i.itemnum from sev_itemdefine i where i.itemcode=s.itemcode)            itemNum,");
				sql.append("       s.deptno               deptNo,");
				sql.append("       (select b.comname from branchinfo b where b.comcode=s.deptno)     as           deptName,");
				sql.append("       s.sevbase              sevBase, ");
				sql.append("       s.instanceId      	  instanceId,");
				sql.append("       (select t.id_ from act_ru_task t where t.proc_inst_id_=s.instanceId )      	  taskId,");
				sql.append("       (select c. codename from codemanage c");
				sql.append("       where c.codetype = 'EvaluateResult' and c.codecode=s.integritysevresult)        integritySevResultName,");
				sql.append("       (select c. codename from codemanage c");
				sql.append("       where c.codetype = 'EvaluateResult' and c.codecode=s.efficiencysevresult)       efficiencySevResultName,");
				sql.append("       (select c.codename from codemanage c");
				sql.append("       where c.codetype = 'evaluateSearchType' and c.codecode=s.projectcode)           projectCode");
				sql.append(" from sev_reportdata s");
				sql.append(" where s.reportid='"+sc.get(0).getReportId()+"'");
				sql.append(" and s.instanceId in ('"+instanceId+"')");
				if(deptId.equals(Constant.DEPTNO)){}else{
					sql.append(" and  s.deptno='"+deptId+"' ");
				}
				//sql.append(" and s.instanceId ='"+sb+"'");
//				if(CurrentUser.getUserdept().equals(Constant.DEPTNO)){
//					sql.append(" and s.sevStatus ='es5'");
//				}else{
//					sql.append(" and s.sevStatus ='es3' and s.deptNo= '"+CurrentUser.getUserdept()+"'");
//				}
				System.out.println(sql);
				return evaluateInfoDao.queryBysql(sql.toString(), Alm_ReportDataDTO.class);
			}else{
				return null;
			}			
		}catch(Exception e){
			LOGGER.error(e.getMessage());
			return null;
		}	
	}
	//进度管理页面
	@SuppressWarnings("unchecked")
	@Transactional
	public Page<?> qrySev_ReportDataDaoAllOfPage(int page, int rows,Alm_ReportDataDTO dto,String dept) {
		try{
			StringBuilder querySql = new StringBuilder();
			System.out.println(dto.getDeptNo());
			String sql="select distinct t.reportid reportId from sev_reportdata t order by reportId desc ";
			List<Alm_ReportDataDTO> list=(List<Alm_ReportDataDTO>) evaluateInfoDao.queryBysql(sql,Alm_ReportDataDTO.class);
			System.out.println(list.get(0).getReportId());
			querySql.append("select c.CODENAME sevStatus ,b.comname deptNo,s.instanceId, ");
			querySql.append("d.codename projectCode,si.itemnum itemNum from sev_reportdata s ");
			querySql.append("LEFT JOIN branchinfo b ON s.deptNo = b.comcode ");
			querySql.append("LEFT JOIN codemanage c ON c.CODECODE=s.SevStatus ");
			querySql.append("left join codemanage d on s.projectcode=d.codecode ");
			querySql.append("left join sev_itemdefine si on si.itemcode=s.itemcode ");
			querySql.append("where s.reportid='"+list.get(0).getReportId()+"' ");
			if(dto.getDeptNo()!=null && !dto.getDeptNo().equals("")){
				querySql.append(" and s.deptno  like '%"+dto.getDeptNo()+"%'");
			}else{
				querySql.append(" and s.deptno  like '%"+dept+"%'");
			}
			querySql.append(" order by si.itemnum");
			return evaluateInfoDao.queryByPage(querySql.toString(),page,rows,Alm_ReportDataDTO.class);
		}catch(Exception e){
			LOGGER.error(e.getMessage());
			return null;
		}	
	}
	//自评估填写提交	
	@SuppressWarnings("unchecked")
	@Transactional
	public void submit(String[] idArr) {
		try{
			StringBuffer message = new StringBuffer("");
			List<String> recipients=new ArrayList<String>();
			String deptName="";
			String year="";
			for (String id : idArr) {
				String[] ids = id.split("#");
				Alm_ReportDataId  srId=new Alm_ReportDataId(ids[0],ids[1],ids[2]);
				Alm_ReportData sr=evaluateInfoDao.get(Alm_ReportData.class,srId);
				sr.setSevStatus("es3");
				evaluateInfoDao.update(sr);
//				year=ids[0].substring(0, 4);
//				String dep="select b.comname as deptNo from branchinfo b where b.comcode='"+ids[2]+"'";
//				List<Alm_ReportDataDTO> dept=(List<Alm_ReportDataDTO>) evaluateInfoDao.queryBysql(dep,Alm_ReportDataDTO.class);
//				deptName=dept.get(0).getDeptNo();		
//				if(CurrentUser.getUserdept().equals(Constant.DEPTNO)){
//					sr.setSevStatus("es7");
//					
//					//设置key确定该流程的唯一性 相当于主键
//					String businessKey =id;
//				    ProcessInstance processInstance = null;
//				    identityService.setAuthenticatedUserId(CurrentUser.getUserId()+"");
//				    //设置参数 是在绘制流程的时候设置的参数 ${dept} 
//		            Map<String, Object> variables = new HashMap<String, Object>();           
//		            //variables.put("dept",CurrentUser.getUserdept());
//		            //开启流 传三个参数 第一个流的名称 第二个 流的唯一确定的id 第三个设置的参数 （此处是设置的部门编号）
//		            processInstance = activitiService.startProcess("evaluationhg", businessKey, variables);//leave
//		            
//					
//					//获取流的id 存到数据库里 这个id是跟流连接的纽带 可以通过流id查到taskid
//					String processInstanceId = processInstance.getId();//工作流ID       
//			        System.out.println(processInstanceId+"保存流程id到数据表");	
//					sr.setInstanceId(processInstanceId);
//					//发送邮件提醒					
//					String sql1="select u.email as deptNo,u.id as oper_Id from userinfo u left join act_id_membership a on a.user_id_=u.id where a.group_id_='riskhead'";
//					List<Alm_ReportDataDTO> srd=(List<Alm_ReportDataDTO>) evaluateInfoDao.queryBysql(sql1,Alm_ReportDataDTO.class);
//					if(srd.size()!=0){
//						StringBuffer str=new StringBuffer();
//						for(Alm_ReportDataDTO srdd:srd){
//							if(!recipients.contains(srdd.getDeptNo())){
//								recipients.add(srdd.getDeptNo());
//							}
//							str.append(srdd.getOper_Id());
//							str.append(",");
//						}
//						setTaskAssign(processInstanceId,str.toString().substring(0,str.length()-1));
//					}					
//				}else{
//					sr.setSevStatus("es3");
//					
//					//设置key确定该流程的唯一性 相当于主键
//					String businessKey =id;
//				    ProcessInstance processInstance = null;
//				    identityService.setAuthenticatedUserId(CurrentUser.getUserId()+"");
//				    //设置参数 是在绘制流程的时候设置的参数 ${dept} 
//		            Map<String, Object> variables = new HashMap<String, Object>();           
//		            //variables.put("dept",CurrentUser.getUserdept());
//		            //开启流 传三个参数 第一个流的名称 第二个 流的唯一确定的id 第三个设置的参数 （此处是设置的部门编号）
//		            processInstance = activitiService.startProcess("evaluationbm", businessKey, variables);//leave
//		            					
//					//获取流的id 存到数据库里 这个id是跟流连接的纽带 可以通过流id查到taskid
//					String processInstanceId = processInstance.getId();//工作流ID       
//			        System.out.println(processInstanceId+"保存流程id到数据表");	
//					sr.setInstanceId(processInstanceId);
//					//发送邮件提醒
//					
//					String sql1="select u.email as deptNo,u.id as oper_Id from userinfo u left join act_id_membership a on a.user_id_=u.id where u.deptcode='"+CurrentUser.getUserdept()+"' and a.group_id_='bmhead'";
//					List<Alm_ReportDataDTO> srd=(List<Alm_ReportDataDTO>) evaluateInfoDao.queryBysql(sql1,Alm_ReportDataDTO.class);
//					if(srd.size()!=0){
//						StringBuffer str=new StringBuffer();
//						for(Alm_ReportDataDTO srdd:srd){
//							if(!recipients.contains(srdd.getDeptNo())){
//								recipients.add(srdd.getDeptNo());
//							}
//							str.append(srdd.getOper_Id());
//							str.append(",");
//						}
//						setTaskAssign(processInstanceId,str.toString().substring(0,str.length()-1));					
//					}					
//				}
//				evaluateInfoDao.update(sr);
//				System.out.println("流程开启");
			}
//			System.out.println(recipients);
//			String subject = "11号文自评估";
//			String url = Config.getProperty("sysUrl");
//			message.append("<html>"+"\n"+"<table align='center' cellpadding='3' cellspacing='0'>");			
//			message.append("<tr><td align='center' width='1000' colspan=5><h3>"+year+"年度风险管理自评估审批通知</h3></td></tr>"+"\n");			
//			message.append("<tr><td colspan=5 width='1000'>"+deptName+"负责人：</td></tr>"+"\n");
//			message.append("<tr><td colspan=5 width='1000'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您有需要处理的自评估。</td></tr>"+"\n");
//			message.append("<tr><td colspan=5 width='1000'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请点击如下链接，登录风险管理系统进行审核。</td></tr>"+"\n");
//			message.append("<tr><td colspan=5 width='1000'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='"+url+"?emailType=5'>"+url+"</a></td></tr>"+"\n");
//			message.append("</table>"+"\n"+"</html>");
//			//EmailUtil.sendHtmlMail(recipients,subject,message.toString());
		}catch(Exception e){
			LOGGER.error(e.getMessage());
		}	
	}
	@Transactional
	public void tongguo(String[] idArr) {
		String option="";
		String year="";
		StringBuffer message = new StringBuffer("");
		List<String> recipients=new ArrayList<String>();
		for (String id : idArr) {
			String[] ids = id.split("#");
			Alm_ReportDataId  srId=new Alm_ReportDataId(ids[0],ids[1],ids[2]);
			
			//第一个参数 事先传到前台的taskid 第二个 参数是意见 第三个是登录人的code 第四个是流名称
			activitiService.addCommentByTaskId(ids[3],option,CurrentUser.getUserId()+"","自评估审批事件");
			//设置流继续前进 最后一个true 代表是同意
			activitiService.completeTaskByTaskId(ids[3],CurrentUser.getUserId()+"",true);
			
			Alm_ReportData sr=evaluateInfoDao.get(Alm_ReportData.class,srId);
			year=ids[0].substring(0, 4);
			String dep="select b.comname as deptNo from branchinfo b where b.comcode='"+ids[2]+"'";
			List<Alm_ReportDataDTO> dept=(List<Alm_ReportDataDTO>) evaluateInfoDao.queryBysql(dep,Alm_ReportDataDTO.class);
			if(CurrentUser.getUserdept().equals(Constant.DEPTNO)){
				if(ids[4].equals("es5")){
					sr.setSevStatus("es7");	
					String sql1="select u.email as deptNo,u.id as oper_Id from userinfo u left join act_id_membership a on a.user_id_=u.id where a.group_id_='riskhead'";
					List<Alm_ReportDataDTO> srd=(List<Alm_ReportDataDTO>) evaluateInfoDao.queryBysql(sql1,Alm_ReportDataDTO.class);
					if(srd.size()!=0){
						StringBuffer str=new StringBuffer();
						for(Alm_ReportDataDTO srdd:srd){
							if(!recipients.contains(srdd.getDeptNo())){
								recipients.add(srdd.getDeptNo());
							}
							str.append(srdd.getOper_Id());
							str.append(",");
						}
						setTaskAssign(sr.getInstanceId(),str.toString().substring(0,str.length()-1));	
					}
				}else if(ids[4].equals("es7")){
					sr.setSevStatus("es9");
				}
			}else{				
				sr.setSevStatus("es5");
				//发送邮件提醒
				
				String sql1="select u.email as deptNo,u.id as oper_Id from userinfo u left join act_id_membership a on a.user_id_=u.id where a.group_id_='riskcontact'";
				List<Alm_ReportDataDTO> srd=(List<Alm_ReportDataDTO>) evaluateInfoDao.queryBysql(sql1,Alm_ReportDataDTO.class);
				if(srd.size()!=0){
					StringBuffer str=new StringBuffer();
					for(Alm_ReportDataDTO srdd:srd){
						if(!recipients.contains(srdd.getDeptNo())){
							recipients.add(srdd.getDeptNo());
						}
						str.append(srdd.getOper_Id());
						str.append(",");
					}
					setTaskAssign(sr.getInstanceId(),str.toString().substring(0,str.length()-1));	
				}			
			}			
			evaluateInfoDao.update(sr);
		}
		String subject = "11号文自评估";	
		System.out.println(recipients);
		String url = Config.getProperty("sysUrl");
		message.append("<html>"+"\n"+"<table align='center' cellpadding='3' cellspacing='0'>");			
		message.append("<tr><td align='center' width='1000' colspan=5><h3>"+year+"年度风险管理自评估审批通知</h3></td></tr>"+"\n");			
		message.append("<tr><td colspan=5 width='1000'>您好：</td></tr>"+"\n");
		message.append("<tr><td colspan=5 width='1000'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您有需要处理的自评估。</td></tr>"+"\n");
		message.append("<tr><td colspan=5 width='1000'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请点击如下链接，登录风险管理系统进行审核。</td></tr>"+"\n");
		message.append("<tr><td colspan=5 width='1000'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='"+url+"?emailType=5'>"+url+"</a></td></tr>"+"\n");
		message.append("</table>"+"\n"+"</html>");
		if(recipients.size()!=0){
			//EmailUtil.sendHtmlMail(recipients,subject,message.toString());
		}
	}
	@Transactional
	public void fanhuixiugai(Alm_ReportDataDTO srd,String reportId, String itemCode, String deptNo,String sevStatus) {
//		String sql1="select t.group_id_ as backType  from act_id_membership t where t.user_id_='"+CurrentUser.getUserAccount()+"'";
//		List<Sev_ReportInfoDTO> si=(List<Sev_ReportInfoDTO>) sev_ReportInfoDao.queryBysql(sql1,Sev_ReportInfoDTO.class);
//		String code;
//		if(si.size()!=0){
//			code=si.get(0).getBackType();
//		}else{
//			code="e02";
//		}
		System.out.println(srd.getTaskId());
		System.out.println(srd.getAuditorOpinion());
		System.out.println(CurrentUser.getUserAccount());
		
		activitiService.addCommentByTaskId(srd.getTaskId(),srd.getAuditorOpinion(),CurrentUser.getUserId()+"","自评估审批事件");
		activitiService.completeTaskByTaskId(srd.getTaskId(),CurrentUser.getUserId()+"",false);
		
		Alm_ReportDataId  srId=new Alm_ReportDataId(reportId,itemCode,deptNo);
		System.out.println(srId);
		Alm_ReportData sr=evaluateInfoDao.get(Alm_ReportData.class,srId);
		System.out.println(sr);
		sr.setAuditorOpinion(srd.getAuditorOpinion());
		if(CurrentUser.getUserdept().equals(Constant.DEPTNO)){
			if(sevStatus.equals("es5")){
				sr.setSevStatus("es6");	
			}else{
				sr.setSevStatus("es8");
			}		
		}else{			
			sr.setSevStatus("es4");
		}
		String year=reportId.substring(0, 4);
		String dep="select b.comname as deptNo from branchinfo b where b.comcode='"+deptNo+"'";
		List<Alm_ReportDataDTO> dept=(List<Alm_ReportDataDTO>) evaluateInfoDao.queryBysql(dep,Alm_ReportDataDTO.class);
		String deptName=dept.get(0).getDeptNo();
		//发送邮件提醒
		List<String> recipients=new ArrayList<String>();
		String sql1="select u.email as deptNo from userinfo u left join act_id_membership a on a.user_id_=u.id where u.deptcode='"+deptNo+"' and a.group_id_='bmcontact'";
		List<Alm_ReportDataDTO> srdd=(List<Alm_ReportDataDTO>) evaluateInfoDao.queryBysql(sql1,Alm_ReportDataDTO.class);
		if(srdd.size()!=0){
			for(Alm_ReportDataDTO srddd:srdd){
				recipients.add(srddd.getDeptNo());
			}
			System.out.println(recipients);
			String url = Config.getProperty("sysUrl");
			EmailUtil eu=new EmailUtil();
			StringBuffer message = new StringBuffer("");
			String subject = "11号文自评估";	
			message.append("<html>"+"\n"+"<table align='center' cellpadding='3' cellspacing='0'>");			
			message.append("<tr><td align='center' width='1000' colspan=5><h3>"+year+"年度风险管理自评估填写通知</h3></td></tr>"+"\n");			
			message.append("<tr><td colspan=5 width='1000'>"+deptName+"联系人：</td></tr>"+"\n");
			message.append("<tr><td colspan=5 width='1000'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您有需要填写的自评估。</td></tr>"+"\n");
			message.append("<tr><td colspan=5 width='1000'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请点击如下链接，登录风险管理系统进行填写。</td></tr>"+"\n");
			message.append("<tr><td colspan=5 width='1000'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='"+url+"?emailType=6'>"+url+"</a></td></tr>"+"\n");
			message.append("</table>"+"\n"+"</html>");
			//eu.sendHtmlMail(recipients,subject,message.toString());
		}		
		evaluateInfoDao.update(sr);
	}
	//自评估填写页面数据查询
	@SuppressWarnings("unchecked")
	@Transactional
	public List<?> queryDataByDeptNo() {
		List<Alm_ReportDataDTO> list=new ArrayList<Alm_ReportDataDTO>();
		String deptNo=CurrentUser.getUserdept();
		StringBuffer sql=new StringBuffer();
		try{
			String file="select t.reportId from cal_cfreportinfo t where t.reportCateGory='1' order by t.reportid desc";
			List<CalCfReportInfoDTO> sc=(List<CalCfReportInfoDTO>) sev_ReportInfoDao.queryBysql(file,CalCfReportInfoDTO.class);			
			if(sc!=null&&!sc.isEmpty()&&sc.size()>0){
				sql.append("select s.ReportId             reportId,");
				sql.append("       s.ItemCode             itemCode,");
				sql.append("       s.IntegritySevResult   integritySevResult,");
				sql.append("       s.instanceId   		  instanceId,");
				sql.append("       s.integrityscore       integrityScore,");
				sql.append("       s.EfficiencySevResult  efficiencySevResult,");
				sql.append("       s.EfficiencyScore      efficiencyScore,");
				sql.append("       s.Temp                 temp,");
				sql.append("       i.sysintegrityscore   sysIntegrityScore,");
				sql.append("       i.folefficiencyscore  folEfficiencyScore,");
				sql.append("       i.standardscore  standardScore,");
				sql.append("       i.itemname       itemName,");
				sql.append("       i.itemnum       itemNum,");
				sql.append("       s.deptno               deptNo,");
				sql.append("       s.sevStatus              sevStatus, ");
				sql.append("       s.sevbase              sevBase, ");
				sql.append("       s.auditorOpinion       auditorOpinion, ");
				sql.append("       s.subtotalScore       subtotalScore, ");
				sql.append("       s.itemType       itemType, ");
				sql.append("       (select c. codename from cfcodemanage c");
				sql.append("       where c.codetype = 'EvaluateResult' and c.codecode=s.integritysevresult)        integritySevResultName,");
				sql.append("       (select c. codename from cfcodemanage c");
				sql.append("       where c.codetype = 'EvaluateResult' and c.codecode=s.efficiencysevresult)       efficiencySevResultName,");
				sql.append("       (select c.codename from cfcodemanage c");
				sql.append("       where c.codetype = 'projectCode' and c.codecode=s.projectcode)           projectCode");
				sql.append(" from alm_reportdata s");
				sql.append(" left join alm_itemdefine i on i.itemcode=s.itemcode");
				sql.append(" where s.DeptNo='"+deptNo+"'and s.reportid='"+sc.get(0).getReportId()+"' order by s.itemcode ");
				System.out.println(sql);
				list=(List<Alm_ReportDataDTO>) evaluateInfoDao.queryBysql(sql.toString(),Alm_ReportDataDTO.class);
				return list;
			}else{
				return list;
			}
		}catch(Exception e){
			LOGGER.error("填写数据查询失败", e);
			return list;	
		}
	}
	@Transactional
	public String panduan(String reportId) {
		String str=null;
		System.out.println(reportId);
		return str;
	}
	//自评估填写保存
	@Transactional
	public void edit(Alm_ReportDataDTO sev) {
		try{
			Alm_ReportDataId  srId=new Alm_ReportDataId(sev.getReportId(),sev.getItemCode(),sev.getDeptNo());
			Alm_ReportData sr=evaluateInfoDao.get(Alm_ReportData.class,srId);
			sr.setIntegritySevResult(sev.getIntegritySevResult());
			sr.setEfficiencySevResult(sev.getEfficiencySevResult());
			sr.setSevBase(sev.getSevBase());
			sr.setSevStatus("es2");
			if(sr.getItemType()==0){
				if(sr.getIntegritySevResult()==1){
					sr.setIntegrityScore(BigDecimal.valueOf(sev.getSysIntegrityScore()).multiply(sr.getDeptWeight()));	
				}else if(sr.getIntegritySevResult()==2){
					BigDecimal uu1=BigDecimal.valueOf(sev.getSysIntegrityScore()).multiply(sr.getDeptWeight());
					sr.setIntegrityScore(uu1.multiply(BigDecimal.valueOf(0.8)));
				}else if(sr.getIntegritySevResult()==3){
					BigDecimal uu1=BigDecimal.valueOf(sev.getSysIntegrityScore()).multiply(sr.getDeptWeight());
					sr.setIntegrityScore(uu1.multiply(BigDecimal.valueOf(0.5)));
				}else if(sr.getIntegritySevResult()==4){
					sr.setIntegrityScore(BigDecimal.valueOf(0));
				}else{
					sr.setIntegrityScore(BigDecimal.valueOf(0));
				}
				if(sr.getEfficiencySevResult()==1){
					sr.setEfficiencyScore(BigDecimal.valueOf(sev.getFolEfficiencyScore()).multiply(sr.getDeptWeight()));	
				}else if(sr.getEfficiencySevResult()==2){
					BigDecimal uu1=BigDecimal.valueOf(sev.getFolEfficiencyScore()).multiply(sr.getDeptWeight());
					sr.setEfficiencyScore(uu1.multiply(BigDecimal.valueOf(0.8)));
				}else if(sr.getEfficiencySevResult()==3){
					BigDecimal uu1=BigDecimal.valueOf(sev.getFolEfficiencyScore()).multiply(sr.getDeptWeight());
					sr.setEfficiencyScore(uu1.multiply(BigDecimal.valueOf(0.5)));
				}else if(sr.getEfficiencySevResult()==4){
					sr.setEfficiencyScore(BigDecimal.valueOf(0));
				}else{
					sr.setEfficiencyScore(BigDecimal.valueOf(0));
				}
				sr.setSubtotalScore(sr.getIntegrityScore().add(sr.getEfficiencyScore()));
			}else{
				if(sev.getIntegritySevResult()==1&&sev.getEfficiencySevResult()==1){
					sr.setSubtotalScore(BigDecimal.valueOf(sev.getStandardScore()).multiply(sr.getDeptWeight()));
				}else{
					sr.setSubtotalScore(BigDecimal.valueOf(0));
				}
			}
			
			evaluateInfoDao.update(sr);
		}catch(Exception e){
			LOGGER.error("自评估填写保存失败", e);	
		}	
	}
	//获取用户角色 然后根据角色查询需要填写的明细项
	@SuppressWarnings("unchecked")
	@Override
	public void download(HttpServletRequest request, HttpServletResponse response, String deptNo) {
		try{
			ExcelUtil excelUtil = new ExcelUtil();
			StringBuffer sql=new StringBuffer();
			String querySql="select reportId from sev_reportinfo t where t.flag=0 order by reportId desc ";
			List<Alm_ReportInfoDTO> sc=(List<Alm_ReportInfoDTO>) sev_ReportInfoDao.queryBysql(querySql,Alm_ReportInfoDTO.class);
			if(sc!=null&&!sc.isEmpty()&&sc.size()>0){
				sql.append("select t.itemcode,(select c.codename from codemanage c where c.codecode=t.projectcode)as projectCode, ");
				sql.append("s.itemnum,s.itemname,t.temp from sev_reportdata t  left join sev_itemdefine s on s.itemcode=t.itemcode  ");		
				sql.append(" where t.deptNo='"+deptNo+"' and t.reportid='"+sc.get(0).getReportId()+"'");
				System.out.println(sql);
				List<Alm_ReportDataDTO> datas=(List<Alm_ReportDataDTO>) evaluateInfoDao.queryBysql(sql.toString(),Alm_ReportDataDTO.class);
				//excelUtil.export4(request, response, datas);
			}
		}catch(Exception e){
			LOGGER.error("自评估填写模板下载失败", e);	
		}		
	}
	@Transactional
	public void save(DataImportDTO dto,String reportId,String itemCode,String number) {
		Alm_SevBase ssb=new Alm_SevBase();
		System.out.println(ssb);
		ssb.setReportId(reportId);
		ssb.setItemCode(itemCode);
		ssb.setDeptNo(dto.getDeptNo());
	    ssb.setNewFileName(dto.getNewfileName());
	    ssb.setFileName(dto.getFileName());
	    ssb.setFilePath(dto.getFilePath());	    
	    ssb.setCompanyCode(dto.getComCode());
	    ssb.setUploadUser(dto.getUploadUser());
	    ssb.setType(number);
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    ssb.setUploadTime(sdf.format(new Date()));   
	    sev_SevBaseDao.save(ssb);   
	}
	@SuppressWarnings("unchecked")
	@Transactional
	public List<?> file(String reportId,String itemCode,String number) {
		String sql="select t.id,t.filename,t.uploaduser,(select b.comname from branchinfo b where b.comcode=t.deptno) as deptNo,t.uploadtime from alm_sevbase t where t.reportid='"+reportId+"' and t.itemcode='"+itemCode+"' and t.type='"+number+"'";
		System.out.println(sql);
		List<Alm_SevBaseDTO> list=(List<Alm_SevBaseDTO>) sev_SevBaseDao.queryBysql(sql,Alm_SevBaseDTO.class);
		return list;
	}
	//进度查询页面发送邮件提醒部门联系人填写
	@SuppressWarnings("static-access")
	@Transactional
	public void send(){	
		try{
			List<String> recipients1=new ArrayList<String>();//进入审批页面
			List<String> recipients2=new ArrayList<String>();//进度填写页面
			String querySql="select reportId from sev_reportinfo t where t.flag='0' order by reportId desc ";
			List<Alm_ReportInfoDTO> sc=(List<Alm_ReportInfoDTO>) sev_ReportInfoDao.queryBysql(querySql,Alm_ReportInfoDTO.class);			
			String sql="";
			if(CurrentUser.getUserdept().equals(Constant.DEPTNO)){
				sql="select s.deptno,s.sevstatus from sev_reportdata s where s.sevstatus!='es9' and s.reportid='"+sc.get(0).getReportId()+"'";				
			}else{
				sql="select s.deptno,s.sevstatus from sev_reportdata s where s.sevstatus!='es9' and s.reportid='"+sc.get(0).getReportId()+"' and s.deptNo='"+CurrentUser.getUserdept()+"'";								
			}List<Alm_ReportDataDTO> srd=(List<Alm_ReportDataDTO>) evaluateInfoDao.queryBysql(sql,Alm_ReportDataDTO.class);
			for(int i=0;i<srd.size();i++){
				if(srd.get(i).getSevStatus().equals("es3")){
					String sql1="select s.email as deptNo from userinfo s  left join act_id_membership a on a.user_id_=s.id where a.group_id_ = 'bmhead' and s.deptcode='"+srd.get(i).getDeptNo()+"'";
					List<Alm_ReportDataDTO> sr=(List<Alm_ReportDataDTO>) evaluateInfoDao.queryBysql(sql1,Alm_ReportDataDTO.class);
					for(Alm_ReportDataDTO srdd:sr){
						if(!recipients1.contains(srdd.getDeptNo())){
							recipients1.add(srdd.getDeptNo());
						}						
					}
				}else if(srd.get(i).getSevStatus().equals("es5")){
					String sql1="select s.email as deptNo from userinfo s  left join act_id_membership a on a.user_id_=s.id where a.group_id_='riskcontact'";
					List<Alm_ReportDataDTO> sr=(List<Alm_ReportDataDTO>) evaluateInfoDao.queryBysql(sql1,Alm_ReportDataDTO.class);
					for(Alm_ReportDataDTO srdd:sr){
						if(!recipients1.contains(srdd.getDeptNo())){
							recipients1.add(srdd.getDeptNo());
						}						
					}
				}else if(srd.get(i).getSevStatus().equals("es7")){
					String sql1="select s.email as deptNo from userinfo s  left join act_id_membership a on a.user_id_=s.id where a.group_id_='riskhead'";
					List<Alm_ReportDataDTO> sr=(List<Alm_ReportDataDTO>) evaluateInfoDao.queryBysql(sql1,Alm_ReportDataDTO.class);
					for(Alm_ReportDataDTO srdd:sr){
						if(!recipients1.contains(srdd.getDeptNo())){
							recipients1.add(srdd.getDeptNo());
						}						
					}
				}else if(srd.get(i).getSevStatus().equals("es8")&&srd.get(i).getDeptNo().equals(Constant.DEPTNO)){
					String sql1="select s.email as deptNo from userinfo s  left join act_id_membership a on a.user_id_=s.id where a.group_id_='riskcontact'";
					List<Alm_ReportDataDTO> sr=(List<Alm_ReportDataDTO>) evaluateInfoDao.queryBysql(sql1,Alm_ReportDataDTO.class);
					for(Alm_ReportDataDTO srdd:sr){
						if(!recipients2.contains(srdd.getDeptNo())){
							recipients2.add(srdd.getDeptNo());
						}						
					}
				}else{
					String sql1="select s.email as deptNo from userinfo s  left join act_id_membership a on a.user_id_=s.id where a.group_id_ = 'bmcontact' and s.deptcode='"+srd.get(i).getDeptNo()+"'";
					List<Alm_ReportDataDTO> sr=(List<Alm_ReportDataDTO>) evaluateInfoDao.queryBysql(sql1,Alm_ReportDataDTO.class);
					for(Alm_ReportDataDTO srdd:sr){
						if(!recipients2.contains(srdd.getDeptNo())){
							recipients2.add(srdd.getDeptNo());
						}						
					}
				}
			}
			String year=sc.get(0).getReportId().substring(0, 4);
			EmailUtil eu=new EmailUtil();
			String url = Config.getProperty("sysUrl");
			String subject1 = "11号文自评估审批提醒";
			StringBuffer message1 = new StringBuffer("");
			message1.append("<html>"+"\n"+"<table align='center' cellpadding='3' cellspacing='0'>");			
			message1.append("<tr><td align='center' width='1000' colspan=5><h3>"+year+"年度风险管理自评估审批通知</h3></td></tr>"+"\n");			
			message1.append("<tr><td colspan=5 width='1000'>您好：</td></tr>"+"\n");
			message1.append("<tr><td colspan=5 width='1000'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您有需要处理的自评估。</td></tr>"+"\n");
			message1.append("<tr><td colspan=5 width='1000'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请点击如下链接，登录风险管理系统进行审核。</td></tr>"+"\n");
			message1.append("<tr><td colspan=5 width='1000'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='"+url+"?emailType=5'>"+url+"</a></td></tr>"+"\n");
			message1.append("</table>"+"\n"+"</html>");
			if(recipients1.size()!=0){
				//eu.sendHtmlMail(recipients1,subject1,message1.toString());
			}
			String subject2 = "11号文自评估填写提醒";
			StringBuffer message2 = new StringBuffer("");
			message2.append("<html>"+"\n"+"<table align='center' cellpadding='3' cellspacing='0'>");			
			message2.append("<tr><td align='center' width='1000' colspan=5><h3>"+year+"年度风险管理自评估填写通知</h3></td></tr>"+"\n");			
			message2.append("<tr><td colspan=5 width='1000'>您好：</td></tr>"+"\n");
			message2.append("<tr><td colspan=5 width='1000'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您有需要处理的自评估。</td></tr>"+"\n");
			message2.append("<tr><td colspan=5 width='1000'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请点击如下链接，登录风险管理系统进行填写。</td></tr>"+"\n");
			message2.append("<tr><td colspan=5 width='1000'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='"+url+"?emailType=6'>"+url+"</a></td></tr>"+"\n");
			message2.append("</table>"+"\n"+"</html>");
			if(recipients2.size()!=0){
				//eu.sendHtmlMail(recipients2,subject2,message2.toString());
			}
		}catch(Exception e){
			LOGGER.error("发送邮件提醒失败！", e);	
		}	
	}
	//文件下载
	@Transactional
	public void downloadURD(long id,HttpServletRequest request,HttpServletResponse response) throws IOException {
		System.out.println(id);
		Alm_SevBase ss=sev_SevBaseDao.get(Alm_SevBase.class, id);
		BufferedInputStream bis = null;  
		BufferedOutputStream bos = null;  
		try{
			String contentType="application/octet-stream";
	        response.setContentType("text/html;charset=UTF-8");  
	        request.setCharacterEncoding("UTF-8");  
	        String name=ss.getNewFileName();
	        String serverPath =  request.getSession().getServletContext().getRealPath("/");
	        System.out.println("项目服务路径："+serverPath);
			String uploaddir =serverPath + Config.getProperty("descripfile");
			System.out.println("文件存储路径："+uploaddir);
	        String path=uploaddir+'/'+name;
	        System.out.println(path);
	        long fileLength = new File(path).length();  
	        
	        System.out.println("========================"+URLEncoder.encode(name,"UTF-8"));
	        
	        response.setContentType(contentType);  
	        response.setHeader("Content-disposition", "attachment; filename="+URLEncoder.encode(name,"UTF-8"));  
	        response.setHeader("Content-Length", String.valueOf(fileLength));  
	  
	        bis = new BufferedInputStream(new FileInputStream(path));  
	        bos = new BufferedOutputStream(response.getOutputStream());  
	        byte[] buff = new byte[2048];  
	        int bytesRead;  
	        while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {  
	            bos.write(buff, 0, bytesRead);  
	        }  
	        System.out.println("===========================================1");
	        bis.close(); 
	        bos.flush();
			bos.close();
		}catch(Exception e){
			e.printStackTrace();
			LOGGER.error("利用流方法文件下载失败", e);	
		}finally {
			bis.close(); 
			bos.close();			 
		}
	}
	@Transactional
	public String downloadHTTP(long id,HttpServletRequest request,HttpServletResponse response) {
		try{
			System.out.println(id);
			Alm_SevBase ss=sev_SevBaseDao.get(Alm_SevBase.class, id);
			String newFileName =ss.getNewFileName();
			String path=Config.getProperty("descripfile")+URLEncoder.encode(newFileName, "utf-8");
			String downloadPath = request.getScheme() + "://" + request.getServerName()
					+ ":" + request.getServerPort()
					+ request.getContextPath() 
					+path;
			System.out.println(downloadPath);
			return downloadPath;
		}catch(Exception e){
			LOGGER.error("利用http方法文件下载失败", e);	
		}
		return null;
	}
	@Transactional
	public List<?> deptHistory(String deptNo,String reportId) {
		StringBuffer sql=new StringBuffer();
		sql.append("select s.ReportId             reportId,");
		sql.append("       s.ItemCode             itemCode,");
		sql.append("       s.IntegritySevResult   integritySevResult,");
		sql.append("       s.instanceId   		  instanceId,");
		sql.append("       s.integrityscore       integrityScore,");
		sql.append("       s.EfficiencySevResult  efficiencySevResult,");
		sql.append("       s.EfficiencyScore      efficiencyScore,");
		sql.append("       (select c.codename from codemanage c");
		sql.append("       where c.codetype = 'EvaluateStatus' and c.codecode=s.sevstatus)                 sevStatus,");
		sql.append("       s.Temp                 temp,");
		sql.append("       i.sysintegrityscore   sysIntegrityScore,");
		sql.append("       i.folefficiencyscore  folEfficiencyScore,");
		sql.append("       i.itemname       itemName,");
		sql.append("       i.itemnum       itemNum,");
		sql.append("       s.deptno               deptNo,");
		sql.append("       s.sevbase              sevBase, ");
		sql.append("       s.auditorOpinion       auditorOpinion, ");
		sql.append("       (select c. codename from codemanage c");
		sql.append("       where c.codetype = 'EvaluateResult' and c.codecode=s.integritysevresult)        integritySevResultName,");
		sql.append("       (select c. codename from codemanage c");
		sql.append("       where c.codetype = 'EvaluateResult' and c.codecode=s.efficiencysevresult)       efficiencySevResultName,");
		sql.append("       (select c.codename from codemanage c");
		sql.append("       where c.codetype = 'evaluateSearchType' and c.codecode=s.projectcode)           projectCode");
		sql.append(" from sev_reportdata s");
		sql.append(" left join sev_itemdefine i on i.itemcode=s.itemcode");
		sql.append(" where s.DeptNo='"+deptNo+"'and s.reportid='"+reportId+"'");
		sql.append(" order by s.itemcode");	
		System.out.println(sql);
		return  evaluateInfoDao.queryBysql(sql.toString(),Alm_ReportDataDTO.class);
	}
	@Transactional
	public void cut(String[] idArr) {
		for (String id : idArr) {
			System.out.println(id);
			Alm_SevBase ss=sev_SevBaseDao.get(Alm_SevBase.class,Long.parseLong(id));
			System.out.println(ss);
			sev_SevBaseDao.remove(ss);
		}
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
	/**
	 * 查看流程信息
	 */
	@Override
	public List<?> findEvaluateEventComment(String instanceId) {
		List<?> list = null;
		try{
			list=activitiService.findCommentByprocessInstanceId(instanceId);
		}catch(Exception e){
			LOGGER.error(e.getMessage());
		}
		return list;
	}
	/**
	 * 带图片跟踪的流程实例
	 */
	@Override
	public void traceImage(String instanceId,HttpServletResponse response){
		System.out.println("instanceId+++++++++++++++++++++++++++"+instanceId);
		try {
			activitiService.traceImage(instanceId, response);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/*@Override
	public Page<?> qrySev_ReportDataDaoAllOfPage(int page, int rows,
			Sev_ReportDataDTO dto, String dept) {
		// TODO Auto-generated method stub
		return null;
	}*/
	
	
	
	
	
}










