package com.sinosoft.zcfz.service.glnlpg.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.IdentityService;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sinosoft.common.Constant;
import com.sinosoft.common.CurrentUser;
import com.sinosoft.common.Page;
import com.sinosoft.entity.UserInfo;
import com.sinosoft.util.EmailUtil;
import com.sinosoft.zcfz.dao.glnlpg.Alm_AdjPlanDao;
import com.sinosoft.zcfz.dao.glnlpg.Alm_ReportInfoDao;
import com.sinosoft.zcfz.dto.glnlpg.Alm_AdjPlanDTO;
import com.sinosoft.zcfz.dto.glnlpg.Alm_ReportDataDTO;
import com.sinosoft.zcfz.dto.glnlpg.Alm_ReportInfoDTO;
import com.sinosoft.zcfz.entity.Alm_AdjPlan;
import com.sinosoft.zcfz.service.glnlpg.Alm_AdjPlanService;
import com.sinosoft.zcfz.service.glnlpg.activiti.ActivitiService;
@Service
public class Alm_AdjPlanServiceImp implements Alm_AdjPlanService {
	private Logger LOGGER= Logger.getLogger(Alm_AdjPlanServiceImp.class);
	@Resource
	private Alm_AdjPlanDao sev_AdjPlanDao;
	@Resource
	private Alm_ReportInfoDao sev_ReportInfoDao;
	@Autowired
	private IdentityService identityService;
	@Resource
	private ActivitiService activitiService;
	
	@Transactional
	public List<?> listAdjPlan(){
		try{
			StringBuffer sql=new StringBuffer();
			System.out.println(CurrentUser.getUserdept());
			sql.append("select s.id,s.reportId,s.adjplan,s.adjOpinion,s.instanceId,");
			sql.append(" (select t.comname from branchinfo t where t.comcode=s.deptno) as deptNo ,");
			sql.append(" (select a.itemname from sev_itemdefine a where a.itemcode=s.itemcode) as itemCode ,");
			sql.append(" (select t.codename from codemanage t where  t.codecode=s.adjStatus) as adjStatus ");
			sql.append(" from sev_adjplan s where s.deptno='"+CurrentUser.getUserdept()+"'");
			System.out.println(sql);
			return sev_AdjPlanDao.queryBysql(sql.toString(),Alm_AdjPlanDTO.class);
		}catch(Exception e){
			e.printStackTrace();
			LOGGER.error("改进方案查询失败：", e);
			return null;
		}
	}
	@Transactional
	public void edit(Alm_AdjPlanDTO sev) {
		try{
			Alm_AdjPlan sap=sev_AdjPlanDao.get(Alm_AdjPlan.class, sev.getId());
			System.out.println(sap);
			sap.setAdjPlan(sev.getAdjPlan());
			sap.setAdjUser(CurrentUser.getUserAccount());
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date = df.format(new Date());
			sap.setAdjTime(date);
			sap.setAdjStatus("es2");
			sev_AdjPlanDao.update(sap);
		}catch(Exception e){
			e.printStackTrace();
			LOGGER.error("改进方案填写失败：", e);
		}
	}
	@SuppressWarnings("unchecked")
	@Transactional
	public void submit(String[] idArr) {
		try{
			for (String id : idArr) {
				System.out.println(id);
				
				String businessKey =id;
			    ProcessInstance processInstance = null;
			    identityService.setAuthenticatedUserId(CurrentUser.getUserId()+"");
			    //设置参数 是在绘制流程的时候设置的参数 ${dept} 
	            Map<String, Object> variables = new HashMap<String, Object>();           
	            variables.put("dept",CurrentUser.getUserdept());
	            //开启流 传三个参数 第一个流的名称 第二个 流的唯一确定的id 第三个设置的参数 （此处是设置的部门编号）
	           
				List<String> recipients=new ArrayList<String>();
				Alm_AdjPlan sap=sev_AdjPlanDao.get(Alm_AdjPlan.class,Long.parseLong(id));
				System.out.println(sap);
				if(CurrentUser.getUserdept().equals(Constant.DEPTNO)){
					sap.setAdjStatus("es8");
					processInstance = activitiService.startProcess("evaluation2", businessKey, variables);
					String processInstanceId = processInstance.getId();//工作流ID       
			        System.out.println(processInstanceId+"保存流程id到数据表");
			        sap.setInstanceId(processInstanceId);
					String sql="select s.email as deptNo from userinfo s left join act_id_membership a on a.user_id_=s.id where a.group_id_='zrs'";
					List<Alm_ReportDataDTO> srd=(List<Alm_ReportDataDTO>) sev_AdjPlanDao.queryBysql(sql.toString(),Alm_ReportDataDTO.class);
					for(Alm_ReportDataDTO srdd:srd){
						recipients.add(srdd.getDeptNo());
					}
					System.out.println(recipients);
					EmailUtil eu=new EmailUtil();
					String subject = "11号文自评估审批提醒";
					String msg = "您有需要审批的的自评估,请登录系统审批";		
					//eu.sendEmail(recipients,msg,subject);
				}else{
					sap.setAdjStatus("es3");
					processInstance = activitiService.startProcess("evaluationbm", businessKey, variables);
					String processInstanceId = processInstance.getId();//工作流ID       
			        System.out.println(processInstanceId+"保存流程id到数据表");
			        sap.setInstanceId(processInstanceId);
					String sql="select s.email as deptNo from userinfo s left join act_id_membership a on a.user_id_=s.id where a.group_id_ like '%e02' and s.deptcode='"+CurrentUser.getUserdept()+"'";
					List<Alm_ReportDataDTO> srd=(List<Alm_ReportDataDTO>) sev_AdjPlanDao.queryBysql(sql.toString(),Alm_ReportDataDTO.class);
					for(Alm_ReportDataDTO srdd:srd){
						recipients.add(srdd.getDeptNo());
					}
					System.out.println(recipients);
					EmailUtil eu=new EmailUtil();
					String subject = "11号文自评估审批提醒";
					String msg = "您有需要审批的的自评估,请登录系统审批";		
					//eu.sendEmail(recipients,msg,subject);
				}
				//如果修改后再提交那么把之前的审批意见制空
				sap.setAdjOpinion(null);
				sev_AdjPlanDao.update(sap);
			}
		}catch(Exception e){
			e.printStackTrace();
			LOGGER.error("改进方案提交失败", e);
		}
	}
	@SuppressWarnings("unchecked")
	@Transactional
	public Page<?> listplan(int page, int rows,Alm_AdjPlanDTO dto,String dept) {
		try{
			StringBuilder querySql = new StringBuilder();
			System.out.println(dto.getDeptNo());
			String sql="select t.reportid from sev_reportinfo t where t.flag in('2','3') and t.backtype=1";
			List<Alm_AdjPlanDTO> list=(List<Alm_AdjPlanDTO>) sev_AdjPlanDao.queryBysql(sql,Alm_AdjPlanDTO.class);
			if(list.size()!=0){
				System.out.println(list.get(0).getReportId());
				querySql.append("select s.id,s.reportId,s.adjplan,s.instanceId,");
				querySql.append(" (select t.comname from branchinfo t where t.comcode=s.deptno) as deptNo ,");
				querySql.append(" (select a.itemname from sev_itemdefine a where a.itemcode=s.itemcode) as itemCode ,");
				querySql.append(" (select t.codename from codemanage t where  t.codecode=s.adjStatus) as adjStatus ");
				querySql.append(" from sev_adjplan s where s.reportid='"+list.get(0).getReportId()+"'");
				if(dto.getDeptNo()!=null && !dto.getDeptNo().equals("")){
					querySql.append(" and s.deptno  like '%"+dto.getDeptNo()+"%'");
				}else{
					querySql.append(" and s.deptno  like '%"+dept+"%'");
				}
				System.out.println(querySql);
				return sev_AdjPlanDao.queryByPage(querySql.toString(),page,rows,Alm_AdjPlanDTO.class);
			}
			return null;
		}catch(Exception e){
			e.printStackTrace();
			LOGGER.error("改进方案进度查询失败", e);
			return null;
		}
	}
	@SuppressWarnings("unchecked")
	@Transactional
	public void send(){	
		try{
			List<String> recipients=new ArrayList<String>();	
			String querySql="select reportId from sev_reportinfo t order by reportId desc ";
			List<Alm_ReportInfoDTO> sc=(List<Alm_ReportInfoDTO>) sev_ReportInfoDao.queryBysql(querySql,Alm_ReportInfoDTO.class);			
			String sql="select s.deptno,s.sevstatus from sev_reportdata s where s.sevstatus!='es12' and s.reportid='"+sc.get(0).getReportId()+"'";
			List<Alm_ReportDataDTO> srd=(List<Alm_ReportDataDTO>) sev_AdjPlanDao.queryBysql(sql,Alm_ReportDataDTO.class);
			for(int i=0;i<srd.size();i++){
				if(srd.get(i).getDeptNo().equals(Constant.DEPTNO)){
					if(srd.get(i).getSevStatus().equals("es8")){
						String sql1="select s.email as deptNo from userinfo s  left join act_id_membership a on a.user_id_=s.id where a.group_id_='zrs'";
						List<Alm_ReportDataDTO> sr=(List<Alm_ReportDataDTO>) sev_AdjPlanDao.queryBysql(sql1,Alm_ReportDataDTO.class);
						for(Alm_ReportDataDTO srdd:sr){
							if(!recipients.contains(srd.get(0).getDeptNo())){
								recipients.add(srdd.getDeptNo());
							}						
						}
					}else if(srd.get(i).getSevStatus().equals("es10")){
						String sql1="select s.email as deptNo from userinfo s  left join act_id_membership a on a.user_id_=s.id where a.group_id_='zrm'";
						List<Alm_ReportDataDTO> sr=(List<Alm_ReportDataDTO>) sev_AdjPlanDao.queryBysql(sql1,Alm_ReportDataDTO.class);
						for(Alm_ReportDataDTO srdd:sr){
							if(!recipients.contains(srd.get(0).getDeptNo())){
								recipients.add(srdd.getDeptNo());
							}						
						}
					}else{
						String sql1="select s.email as deptNo from userinfo s  left join act_id_membership a on a.user_id_=s.id where a.group_id_='zre'";
						List<Alm_ReportDataDTO> sr=(List<Alm_ReportDataDTO>) sev_AdjPlanDao.queryBysql(sql1,Alm_ReportDataDTO.class);
						for(Alm_ReportDataDTO srdd:sr){
							if(!recipients.contains(srd.get(0).getDeptNo())){
								recipients.add(srdd.getDeptNo());
							}						
						}
					}
				}else{
					if(srd.get(i).getSevStatus().equals("es3")){
						String sql1="select s.email as deptNo from userinfo s  left join act_id_membership a on a.user_id_=s.id where a.group_id_ like '%e02' and s.deptcode='"+srd.get(i).getDeptNo()+"'";
						List<Alm_ReportDataDTO> sr=(List<Alm_ReportDataDTO>) sev_AdjPlanDao.queryBysql(sql1,Alm_ReportDataDTO.class);
						for(Alm_ReportDataDTO srdd:sr){
							if(!recipients.contains(srd.get(0).getDeptNo())){
								recipients.add(srdd.getDeptNo());
							}						
						}
					}else if(srd.get(i).getSevStatus().equals("es4")){
						String sql1="select s.email as deptNo from userinfo s  left join act_id_membership a on a.user_id_=s.id where a.group_id_ like '%e03' and s.deptcode='"+srd.get(i).getDeptNo()+"'";
						List<Alm_ReportDataDTO> sr=(List<Alm_ReportDataDTO>) sev_AdjPlanDao.queryBysql(sql1,Alm_ReportDataDTO.class);
						for(Alm_ReportDataDTO srdd:sr){
							if(!recipients.contains(srd.get(0).getDeptNo())){
								recipients.add(srdd.getDeptNo());
							}						
						}
					}else if(srd.get(i).getSevStatus().equals("es6")){
						String sql1="select s.email as deptNo from userinfo s  left join act_id_membership a on a.user_id_=s.id where a.group_id_='zre'";
						List<Alm_ReportDataDTO> sr=(List<Alm_ReportDataDTO>) sev_AdjPlanDao.queryBysql(sql1,Alm_ReportDataDTO.class);
						for(Alm_ReportDataDTO srdd:sr){
							if(!recipients.contains(srd.get(0).getDeptNo())){
								recipients.add(srdd.getDeptNo());
							}						
						}
					}else{
						String sql1="select s.email as deptNo from userinfo s  left join act_id_membership a on a.user_id_=s.id where a.group_id_ like '%e01' and s.deptcode='"+srd.get(i).getDeptNo()+"'";
						List<Alm_ReportDataDTO> sr=(List<Alm_ReportDataDTO>) sev_AdjPlanDao.queryBysql(sql1,Alm_ReportDataDTO.class);
						for(Alm_ReportDataDTO srdd:sr){
							if(!recipients.contains(srd.get(0).getDeptNo())){
								recipients.add(srdd.getDeptNo());
							}						
						}
					}
				}				
			}		
			EmailUtil eu=new EmailUtil();
			String subject = "11号文自评估改进方案提醒";
			String msg = "您有需要处理的改进方案,请登录系进行操作";		
			//eu.sendEmail(recipients,msg,subject);
		}catch(Exception e){
			LOGGER.error("改进方案发送邮件提醒失败！", e);	
		}	
	}
	@Transactional
	public Page<?> listpage(int page, int rows){
		try{
			StringBuilder querySql = new StringBuilder();
//			String sql1="select t.group_id_ as backType  from act_id_membership t where t.user_id_='"+CurrentUser.getUserAccount()+"'";
//			List<Sev_ReportInfoDTO> sr=(List<Sev_ReportInfoDTO>) sev_AdjPlanDao.queryBysql(sql1,Sev_ReportInfoDTO.class);
//			String code;
//			if(sr.size()!=0){
//				code=sr.get(0).getBackType().substring(sr.get(0).getBackType().length()-3, sr.get(0).getBackType().length());
//			}else{
//				code="e02";
//			}
			String instanceId="";
			UserInfo userInfo = (UserInfo) CurrentUser.getCurrentUser();			
			List<String> list1=activitiService.findMyTaskReturnTaskId(page, rows, userInfo);
			if(list1.size()!=0){
				for(String taskId:list1){
					//查处该用户所有属于他的流Id进行数据检索
					String processInstanceId=activitiService.getProcessIdByTaskId(taskId);
					instanceId=instanceId+processInstanceId+"','";
					System.out.println(instanceId+"####流ID");
				}
				instanceId=instanceId.substring(0, instanceId.length()-3);
				System.out.println(instanceId);
			}	
			//String deptId=CurrentUser.getUserdept();
			String sql="select t.reportid from sev_reportinfo t where t.flag in('2','3') and t.backtype=1";
			List<Alm_AdjPlanDTO> list=(List<Alm_AdjPlanDTO>) sev_AdjPlanDao.queryBysql(sql,Alm_AdjPlanDTO.class);
			if(list.size()!=0){
				querySql.append("select s.id,s.reportId,s.adjplan,s.adjStatus,");
				querySql.append(" (select t.id_ from act_ru_task t where t.proc_inst_id_=s.instanceId ) as taskId,");
				querySql.append(" (select t.comname from branchinfo t where t.comcode=s.deptno) as deptNo ,");
				querySql.append(" (select a.itemname from sev_itemdefine a where a.itemcode=s.itemcode) as itemCode ,");
				querySql.append(" (select t.codename from codemanage t where  t.codecode=s.adjStatus) as adjStatusName ");
				querySql.append(" from sev_adjplan s where s.reportid='"+list.get(0).getReportId()+"'");
				querySql.append(" and s.instanceId in ('"+instanceId+"')");
//				if(deptId.equals(Constant.DEPTNO)){
//					if(code.equals("zre")){
//						querySql.append(" and s.adjStatus ='es6'");
//					}else if(code.equals("zrs")){
//						querySql.append(" and s.adjStatus ='es8'");
//					}else if(code.equals("zrm")){
//						querySql.append(" and s.adjStatus ='es10'");
//					}
//				}else{
//					if(code.equals("e02")){
//						querySql.append(" and s.adjStatus ='es3' and s.deptNo= '"+deptId+"'");
//					}else if(code.equals("e03")){
//						querySql.append(" and s.adjStatus ='es4' and s.deptNo= '"+deptId+"'");
//					}
//				}
				System.out.println(querySql);
				return sev_AdjPlanDao.queryByPage(querySql.toString(),page,rows,Alm_AdjPlanDTO.class);
			}
			return null;
		}catch(Exception e){
			e.printStackTrace();
			LOGGER.error("改进方案审批页面查询失败", e);
			return null;
		}
	}
	@SuppressWarnings("unchecked")
	@Transactional
	public void tongguo(String[] idArr) {
		try{
//			String sql1="select t.group_id_ as backType  from act_id_membership t where t.user_id_='"+CurrentUser.getUserAccount()+"'";
//			List<Sev_ReportInfoDTO> si=(List<Sev_ReportInfoDTO>) sev_AdjPlanDao.queryBysql(sql1,Sev_ReportInfoDTO.class);
//			String code;
//			if(si.size()!=0){
//				code=si.get(0).getBackType().substring(si.get(0).getBackType().length()-3, si.get(0).getBackType().length());
//			}else{
//				code="e02";
//			}
			for (String id : idArr) {
				String[] ids = id.split("#");
				String option="";
				//第一个参数 事先传到前台的taskid 第二个 参数是意见 第三个是登录人的code 第四个是流名称
				activitiService.addCommentByTaskId(ids[1],option,CurrentUser.getUserAccount(),"自评估审批事件");
				//设置流继续前进 最后一个true 代表是同意
				activitiService.completeTaskByTaskId(ids[1],CurrentUser.getUserAccount(),true);
				List<String> recipients=new ArrayList<String>();
				Alm_AdjPlan sap=sev_AdjPlanDao.get(Alm_AdjPlan.class,Long.parseLong(ids[0]));
				if(CurrentUser.getUserdept().equals(Constant.DEPTNO)){
					if(ids[2].equals("es6")){
						sap.setAdjStatus("es8");
						String sql="select s.email as deptNo from userinfo s left join act_id_membership a on a.user_id_=s.id where a.group_id_='zrs'";
						List<Alm_ReportDataDTO> srd=(List<Alm_ReportDataDTO>) sev_AdjPlanDao.queryBysql(sql.toString(),Alm_ReportDataDTO.class);
						for(Alm_ReportDataDTO srdd:srd){
							recipients.add(srdd.getDeptNo());
						}
						System.out.println(recipients);
						EmailUtil eu=new EmailUtil();
						String subject = "11号文自评估审批提醒";
						String msg = "您有需要审批的的自评估,请登录系统审批";		
						//eu.sendEmail(recipients,msg,subject);
					}else if(ids[2].equals("es8")){
						sap.setAdjStatus("es10");
						String sql="select s.email as deptNo from userinfo s left join act_id_membership a on a.user_id_=s.id where a.group_id_='zrm'";
						List<Alm_ReportDataDTO> srd=(List<Alm_ReportDataDTO>) sev_AdjPlanDao.queryBysql(sql.toString(),Alm_ReportDataDTO.class);
						for(Alm_ReportDataDTO srdd:srd){
							recipients.add(srdd.getDeptNo());
						}
						System.out.println(recipients);
						EmailUtil eu=new EmailUtil();
						String subject = "11号文自评估审批提醒";
						String msg = "您有需要审批的的自评估,请登录系统审批";		
						//eu.sendEmail(recipients,msg,subject);
					}else if(ids[2].equals("es10")){
						sap.setAdjStatus("es12");
					}
				}else{
					if(ids[2].equals("es3")){
						sap.setAdjStatus("es4");
						String sql="select s.email as deptNo from userinfo s left join act_id_membership a on a.user_id_=s.id where a.group_id_ like '%e03' and s.deptcode='"+CurrentUser.getUserdept()+"'";
						List<Alm_ReportDataDTO> srd=(List<Alm_ReportDataDTO>) sev_AdjPlanDao.queryBysql(sql.toString(),Alm_ReportDataDTO.class);
						for(Alm_ReportDataDTO srdd:srd){
							recipients.add(srdd.getDeptNo());
						}
						System.out.println(recipients);
						EmailUtil eu=new EmailUtil();
						String subject = "11号文自评估审批提醒";
						String msg = "您有需要审批的的自评估,请登录系统审批";		
						//eu.sendEmail(recipients,msg,subject);
					}else if(ids[2].equals("es4")){
						sap.setAdjStatus("es6");
						String sql="select s.email as deptNo from userinfo s left join act_id_membership a on a.user_id_=s.id where a.group_id_='zre'";
						List<Alm_ReportDataDTO> srd=(List<Alm_ReportDataDTO>) sev_AdjPlanDao.queryBysql(sql.toString(),Alm_ReportDataDTO.class);
						for(Alm_ReportDataDTO srdd:srd){
							recipients.add(srdd.getDeptNo());
						}
						System.out.println(recipients);
						EmailUtil eu=new EmailUtil();
						String subject = "11号文自评估审批提醒";
						String msg = "您有需要审批的的自评估,请登录系统审批";		
						//eu.sendEmail(recipients,msg,subject);
					}
				}
				sev_AdjPlanDao.update(sap);
			}
		}catch(Exception e){
			e.printStackTrace();
			LOGGER.error("改进方案通过失败", e);
		}
	}
	@SuppressWarnings("unchecked")
	@Transactional
	public void fanhuixiugai(Alm_AdjPlanDTO srd) {
		try{
//			String sql1="select t.group_id_ as backType  from act_id_membership t where t.user_id_='"+CurrentUser.getUserAccount()+"'";
//			List<Sev_ReportInfoDTO> si=(List<Sev_ReportInfoDTO>) sev_AdjPlanDao.queryBysql(sql1,Sev_ReportInfoDTO.class);
//			String code;
//			if(si.size()!=0){
//				code=si.get(0).getBackType().substring(si.get(0).getBackType().length()-3, si.get(0).getBackType().length());
//			}else{
//				code="e02";
//			}
			//第一个参数 事先传到前台的taskid 第二个 参数是意见 第三个是登录人的code 第四个是流名称
			activitiService.addCommentByTaskId(srd.getTaskId(),srd.getAdjOpinion(),CurrentUser.getUserAccount(),"自评估审批事件");
			//设置false表示
			activitiService.completeTaskByTaskId(srd.getTaskId(),CurrentUser.getUserAccount(),false);
			
			Alm_AdjPlan sap=sev_AdjPlanDao.get(Alm_AdjPlan.class,srd.getId());
			System.out.println(sap);
			sap.setAdjOpinion(srd.getAdjOpinion());
			if(CurrentUser.getUserdept().equals(Constant.DEPTNO)){
				if(srd.getAdjStatus().equals("es6")){
					sap.setAdjStatus("es9");
				}else if(srd.getAdjStatus().equals("es8")){
					sap.setAdjStatus("es11");
				}else if(srd.getAdjStatus().equals("es10")){
					sap.setAdjStatus("es13");
				}
			}else{
				if(srd.getAdjStatus().equals("es3")){
					sap.setAdjStatus("es5");
				}else if(srd.getAdjStatus().equals("es4")){
					sap.setAdjStatus("es7");
				}
			}
			List<String> recipients=new ArrayList<String>();
			if(CurrentUser.getUserdept().equals(Constant.DEPTNO)){
				String sql="select s.email as deptNo from userinfo s left join act_id_membership a on a.user_id_=s.id where a.group_id_='zre'";
				List<Alm_ReportDataDTO> srdd=(List<Alm_ReportDataDTO>) sev_AdjPlanDao.queryBysql(sql.toString(),Alm_ReportDataDTO.class);
				for(Alm_ReportDataDTO srddd:srdd){
					recipients.add(srddd.getDeptNo());
				}
				System.out.println(recipients);
				EmailUtil eu=new EmailUtil();
				String subject = "11号文自评估修改提醒";
				String msg = "您提交的自评填写被退回来了，请查看原因进行修改后再提交";		
				//eu.sendEmail(recipients,msg,subject);
			}else{
				String sql="select s.email as deptNo from userinfo s left join act_id_membership a on a.user_id_=s.id where a.group_id_ like '%e01' and s.deptcode='"+CurrentUser.getUserdept()+"'";
				List<Alm_ReportDataDTO> srdd=(List<Alm_ReportDataDTO>) sev_AdjPlanDao.queryBysql(sql.toString(),Alm_ReportDataDTO.class);
				for(Alm_ReportDataDTO srddd:srdd){
					recipients.add(srddd.getDeptNo());
				}
				System.out.println(recipients);
				EmailUtil eu=new EmailUtil();
				String subject = "11号文自评估修改提醒";
				String msg = "您提交的自评填写被退回来了，请查看原因进行修改后再提交";		
				//eu.sendEmail(recipients,msg,subject);
			}
			sev_AdjPlanDao.update(sap);
		}catch(Exception e){
			e.printStackTrace();
			LOGGER.error("改进方案返回修改失败", e);
		}
	}
	
}
