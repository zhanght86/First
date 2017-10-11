package com.sinosoft.zcfz.service.glnlpg.activiti.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.IdentityService;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sinosoft.common.Page;
import com.sinosoft.entity.UserInfo;
import com.sinosoft.zcfz.dao.glnlpg.LeaveDao;
import com.sinosoft.zcfz.dto.glnlpg.LeaveDTO;
import com.sinosoft.zcfz.dto.glnlpg.backLogDTO;
import com.sinosoft.zcfz.entity.Leave;
import com.sinosoft.zcfz.service.glnlpg.activiti.ActivitiService;
import com.sinosoft.zcfz.service.glnlpg.activiti.LeaveService;


@Service
public class LeaveServiceImp implements LeaveService{
	@Resource
	private LeaveDao leaveDao;
	@Resource
	private ActivitiService activitiService;
	
	@Autowired
	private IdentityService identityService;
	@Transactional
	public void saveLeave(LeaveDTO dto) {
		// TODO Auto-generated method stub
		Leave br=new Leave();
	    br.setProcessInstanceId(dto.getProcessInstanceId());
	    br.setReason(dto.getReason());
	    br.setUserId(dto.getUserId());
	    br.setStatus("0"); //0代表暂存
		leaveDao.save(br);
	}
	@Transactional
	public void updateLeave(long id, LeaveDTO dto) {
		// TODO Auto-generated method stub
		Leave br =leaveDao.get(Leave.class,id);
	    br.setProcessInstanceId(dto.getProcessInstanceId());
	    br.setReason(dto.getReason());
	    br.setUserId(dto.getUserId());
		leaveDao.update(br);
	}
	
	@Transactional
	public void deleteLeave(String[] idArr) {
		// TODO Auto-generated method stub
		Long[] ids=new Long[idArr.length];
		for(int i=0;i<idArr.length;i++){
			ids[i]=Long.parseLong(idArr[i]);
		}
		leaveDao.removes(ids,Leave.class);
	}
	public Page<?> qryLeaveOfPage(int page, int rows,LeaveDTO dto) {
		  StringBuilder querySql =new StringBuilder("select * from act_Leave  where 1=1");
			if(!(dto.getId()==null||"".equals(dto.getId()))){
	    	    querySql.append("  and  Id like '%"+dto.getId()+"%'");
		    }
			if(!(dto.getProcessInstanceId()==null||"".equals(dto.getProcessInstanceId()))){
	    	    querySql.append("  and  ProcessInstanceId like '%"+dto.getProcessInstanceId()+"%'");
		    }
			if(!(dto.getReason()==null||"".equals(dto.getReason()))){
	    	    querySql.append("  and  Reason like '%"+dto.getReason()+"%'");
		    }
			if(!(dto.getUserId()==null||"".equals(dto.getUserId()))){
	    	    querySql.append("  and  UserId like '%"+dto.getUserId()+"%'");
		    }
		return leaveDao.queryByPage(querySql.toString(),page,rows,LeaveDTO.class);
	}
	public Page<?> qryMyLeaveOfPage(int page, int rows,LeaveDTO dto,UserInfo userInfo) {
		//Yusen
		StringBuilder querySql =new StringBuilder("select * from act_Leave  where 1=1 and userid='"+userInfo.getId()+"' ");
		if(!(dto.getId()==null||"".equals(dto.getId()))){
			querySql.append("  and  Id like '%"+dto.getId()+"%'");
		}
		if(!(dto.getProcessInstanceId()==null||"".equals(dto.getProcessInstanceId()))){
			querySql.append("  and  ProcessInstanceId like '%"+dto.getProcessInstanceId()+"%'");
		}
		if(!(dto.getReason()==null||"".equals(dto.getReason()))){
			querySql.append("  and  Reason like '%"+dto.getReason()+"%'");
		}
		return leaveDao.queryByPage(querySql.toString(),page,rows,LeaveDTO.class);
	}
	public List<?> qryLeave() {
		StringBuilder querySql =new StringBuilder("select leave.* from act_Leave leave");
		List<?> list = leaveDao.queryBysql(querySql.toString(),LeaveDTO.class);
		return list;
	}
	public List<?> qryLeaveAll() {
		List<?> list = leaveDao.queryBysql("select leave.* from act_Leave leave",LeaveDTO.class);
		return list;
	}
	public Leave findLeave(long id) {
		// TODO Auto-generated method stub
		return leaveDao.get(Leave.class,id);
	}
	
	@Transactional
	public void startLeaveProcess(long id) {
        Leave leave=findLeave(id);
        String businessKey = String.valueOf(leave.getId());
        ProcessInstance processInstance = null;
        try {
            // 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
            identityService.setAuthenticatedUserId(leave.getUserId());
            Map<String, Object> variables = new HashMap<String, Object>();
            processInstance = activitiService.startProcess("leave", businessKey, variables);//leave
            String processInstanceId = processInstance.getId();//工作流ID
            leave.setProcessInstanceId(processInstanceId);
        } finally {
            identityService.setAuthenticatedUserId(null);
        }
        leave.setStatus("1"); //已提交
        leaveDao.update(leave);
	}
	
	public void viewLeaveProcess(String processInstanceId,HttpServletResponse response) {
		try {
			activitiService.resourceReadByprocessId(processInstanceId, "image", response);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("读取图片失败！原因是："+e);
		}
	}
	/**
	 * 查找个人请假任务
	 */
	public Page<?> findMyLeaveTask(int page, int rows,UserInfo u) {
		List resultList=new ArrayList();
		long total=0;
		List<String> list=activitiService.findMyTaskReturnTaskId(page, rows, u);
		for(String taskId:list){
			String processInstanceId=activitiService.getProcessIdByTaskId(taskId);
			StringBuilder querySql =new StringBuilder("select * from act_Leave  where processInstanceId='"+processInstanceId+"'");
			List<?> leaveList=leaveDao.queryBysql(querySql.toString(),LeaveDTO.class);
			//total++;
			LeaveDTO map=(LeaveDTO) leaveList.get(0);
			map.setTaskId(taskId);
			resultList.add(map);
		}
		total=activitiService.findMyTaskReturnTaskIdCount(u);
		return new Page(total, resultList);
	}
	/**
	 * 查找组任务
	 */
	public Page<?> findMyGroupLeaveTask(int page, int rows,UserInfo u) {
		List resultList=new ArrayList();
		long total=0;
		List<String> list=activitiService.findMyGroupTaskReturnTaskId(page, rows, u);
		if(list!=null&&list.size()>0){
			for(String taskId:list){
				String processInstanceId=activitiService.getProcessIdByTaskId(taskId);
				StringBuilder querySql =new StringBuilder("select * from act_Leave where processInstanceId='"+processInstanceId+"'");
				List<?> leaveList=leaveDao.queryBysql(querySql.toString(),LeaveDTO.class);
				//total++;
				LeaveDTO map=(LeaveDTO) leaveList.get(0);
				map.setTaskId(taskId);
				resultList.add(map);
			}
		}
		total=activitiService.findMyGroupTaskReturnTaskIdCount(u);
		return new Page(total, resultList);
	}
	/**
	 * 查找历史任务
	 */
	public Page<?> findMyHiLeaveTask(int page, int rows,UserInfo u) {
		List resultList=new ArrayList();
		long total=0;
		List<String> list=activitiService.findMyHiTask(page, rows, u);
		if(list!=null&&list.size()>0){
			for(String processInstanceId:list){
				StringBuilder querySql =new StringBuilder("select * from act_Leave where processInstanceId='"+processInstanceId+"'");
				List<?> leaveList=leaveDao.queryBysql(querySql.toString(),LeaveDTO.class);
				resultList.add(leaveList.get(0));
			}
		}
		total=activitiService.findMyHiTaskCount(u);
		return new Page(total, resultList);
	}
	/**
	 * 完成个人任务
	 */
	@Transactional
	public void completeLeaveTask(LeaveDTO leavevo, UserInfo u,boolean flag) {
		try{
			String taskId=leavevo.getTaskId();
			Leave leave=findLeave(leavevo.getId());
			String processInstanceId=leavevo.getProcessInstanceId();
			//activitiService.addCommentByProcessInstanceId(processInstanceId, "审批"+u.getUserName(),u.getUserCode());
			//activitiService.completeTaskByProcessInstanceId(processInstanceId, u.getUserCode(), flag);
			activitiService.addCommentByTaskId(taskId,leavevo.getMessage(),u.getUserCode(),"请假单");
			activitiService.completeTaskByTaskId(taskId, u.getUserCode(), flag);
			if(!flag){
				leave.setStatus("6"); //流程退回
				leaveDao.update(leave);
			}
			boolean isEnd=activitiService.processIsEnd(processInstanceId);
			if(isEnd){
				leave.setStatus("5"); //流程结束状态
				leaveDao.update(leave);
			}
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	/**
	 * 签收个人任务
	 */
	@Transactional
	public void claimLeaveTask(Leave leave, UserInfo u,String taskId) {
		//String processInstanceId=leave.getProcessInstanceId();
		//activitiService.claimTaskByProcessInstanceId(processInstanceId, u.getUserCode());
		activitiService.claimTaskByTaskId(taskId, u.getUserCode());
		leave.setStatus("3"); //已签收状态
		leaveDao.update(leave);
	}
	@Override
	/**
	 * 查找流程历史
	 */
	public List<?> findLeaveComment(String processInstanceId) {
		List<?> list=activitiService.findCommentByprocessInstanceId(processInstanceId);
		return list;
	}
	
	
	
	@Override
	/**
	 * 查找个人任务
	 */
	public Page<?> findMyTask(int page, int rows,UserInfo u) {
		List<backLogDTO> backLogs = new ArrayList<backLogDTO>();
		List<String> list=activitiService.findMySelfTaskReturnTaskId(page, rows, u);
		int i = 0;
		int j = 0;
		int k = 0;
		int l = 0;
		int o = 0;
		int p = 0;
		int q = 0;
		int s = 0;
		int m = 0;
		for(String taskId:list){
			String processInstanceType = activitiService.getProcessDefIdByTaskId(taskId);//根据任务ID得到流ID
			String str = processInstanceType.substring(0,processInstanceType.indexOf(":"));//截取流类型的前端，用来判断是事件的流还是方案的流
			if(str.equals("xghhLossEvent") && i==0){
				backLogDTO b = new backLogDTO("page/t_losseventstore/t_losseventstore-verify.jsp", "损失事件审核",1,"损失事件审核");
				backLogs.add(b);
				i++;
				m++;
			}else if(str.equals("xghhEventRectify") && j==0){
				backLogDTO b = new backLogDTO("page/t_losseventstore/t_losseventstore-verify.jsp", "损失事件整改方案审核",2,"损失事件整改方案审核");
				backLogs.add(b);
				j++;
				m++;
			}else if((str.equals("evaluationbm") || str.equals("evaluationhg"))&&k==0){
				backLogDTO b = new backLogDTO("page/evaluate/evaluate-approval.jsp", "自评估审核",3,"自评估审批");
				backLogs.add(b);
				k++;
				m++;
			}else if((str.equals("riskReport")) &&l ==0){
				backLogDTO b = new backLogDTO("page/riskreports/riskreportApproval.jsp", "风险报告审批",4,"风险报告审批");
				backLogs.add(b);
				l++;
				m++;
			}else if((str.equals("Warnbm") || str.equals("Warnhg"))&&o==0){//风险预警
				backLogDTO b = new backLogDTO("page/riskwarn/indexfeedbackbymanage.jsp", "风险预警审批",5,"风险预警审批");
				backLogs.add(b);
				o++;
				m++;
			}else if((str.equals("reportForm") || str.equals("evaluation10"))&&p==0){//风险综合评级
				backLogDTO b = new backLogDTO("page/reportform/approval.jsp", "风险综合评级审批",6,"风险综合评级审批");
				backLogs.add(b);
				p++;
				m++;
			}else if((str.equals("adjust")) && q ==0){//自评估汇总调整
				backLogDTO b = new backLogDTO("page/evaluate/evaluate-adjustapproval.jsp", "自评估汇总调整审批",7,"自评估汇总调整审批");
				backLogs.add(b);
				q++;
				m++;
			}else if((str.equals("adjustSubmitTen")) && s ==0){//风险综合评级汇总调整审批
				backLogDTO b = new backLogDTO("page/reportform/adjustapproval.jsp", "风险综合评级汇总调整审批",8,"风险综合评级汇总调整审批");
				backLogs.add(b);
				s++;
				m++;
			}
			
		}
		
		return new Page(m, backLogs);
	}
	
	
	
}