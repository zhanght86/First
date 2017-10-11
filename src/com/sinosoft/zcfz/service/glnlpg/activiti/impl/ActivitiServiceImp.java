package com.sinosoft.zcfz.service.glnlpg.activiti.impl;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sinosoft.common.Page;
import com.sinosoft.entity.UserInfo;
import com.sinosoft.util.activiti.HistoryProcessInstanceDiagramCmd;
import com.sinosoft.zcfz.service.glnlpg.activiti.ActivitiService;
@Service
public class ActivitiServiceImp implements ActivitiService{
	
	@Autowired
	private RepositoryService repositoryService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private FormService formService;
	
	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private ManagementService managementService;
	
	@Autowired
	private IdentityService identityService;

	private static final String DEFAULT_DATE_FORMAT="yyyy-MM-dd HH:mm:ss";  
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT); 

	/**部署流程定义*/
	@Override
	public void saveNewDeploye(MultipartFile file, String filename) {
		try {
			//2：将File类型的文件转化成ZipInputStream流
			ZipInputStream zipInputStream = new ZipInputStream(file.getInputStream());
			//ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(new File("D:/Desktop/leave.zip")));
			repositoryService.createDeployment()//创建部署对象
							.name(filename)//添加部署名称
							.addZipInputStream(zipInputStream)
							//.addClasspathResource("deploy/candidateUser.bpmn")
							.deploy();//完成部署
		} catch (Exception e) {
			throw new RuntimeException("部署出现错误，原因是："+e);
		}
	}
	
	/**查询部署对象信息，对应表（act_re_deployment）*/
	public List<Deployment> findDeploymentList() {
		List<Deployment> list = repositoryService.createDeploymentQuery()//创建部署对象查询
							.orderByDeploymenTime().asc()//
							.list();
		return list;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override	
	public Page<?> viewProcessDeploy(int page, int rows) {
		int start=(page-1)*rows;
		List<Deployment> list=repositoryService.createDeploymentQuery()
			.orderByDeploymentId().asc()
			.listPage(start, rows);
		List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
		for(Deployment d :list){
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("id", d.getId());
			map.put("name", d.getName());
			map.put("time", dateFormat.format(d.getDeploymentTime()));
			//String row="{id:"+d.getId()+",name:"+d.getName()+",time:"+d.getDeploymentTime()+"}";
			result.add(map);
		}
		long total=repositoryService.createDeploymentQuery().count();
		return new Page(total,result);
	}
	/**
	 * 流程定义查看
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Page<?> viewProdef(int page, int rows) {
		int start=(page-1)*rows;
		List<ProcessDefinition> list=repositoryService.createProcessDefinitionQuery()
				.orderByDeploymentId()
				.orderByProcessDefinitionVersion()
				.asc()
				.listPage(start, rows);
		List<Map<String,Object>> result=new ArrayList<Map<String,Object>>();
		for(ProcessDefinition d :list){
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("deploymentId", d.getDeploymentId());
			map.put("defineId", d.getId());
			map.put("name", d.getName());
			map.put("key", d.getKey());
			map.put("resourceName", d.getResourceName());
			map.put("diagramResourceName", d.getDiagramResourceName());
			map.put("version", d.getVersion());
			//String row="{id:"+d.getId()+",name:"+d.getName()+",time:"+d.getDeploymentTime()+"}";
			result.add(map);
		}
		long total=repositoryService.createProcessDefinitionQuery().count();
		return new Page(total,result);
	}
	
	
	/**查询流程定义的信息，对应表（act_re_procdef）*/
	public List<ProcessDefinition> findProcessDefinitionList() {
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery()//创建流程定义查询
							.orderByProcessDefinitionVersion().asc()//
							.list();
		return list;
	}
	
	/**使用部署对象ID，删除流程定义*/
	public void deleteProcessDefinitionByDeploymentId(String deploymentId) {
		repositoryService.deleteDeployment(deploymentId, true);
	}
	
	/**使用部署对象ID和资源图片名称，获取图片的输入流*/
	@Override
	public InputStream findImageInputStream(String deploymentId,
			String imageName) {
		return repositoryService.getResourceAsStream(deploymentId, imageName);
	}
	
	/**
	 * 启动流程
	 * @param 流程类型
	 * @param 业务号
	 * @param 流程变量
	 */
	public ProcessInstance startProcess(String processType,String id,Map<String, Object> variables) {
		//String key = processType.getClass().getSimpleName();
		//格式：LeaveBill.id的形式（使用流程变量）
		String businessKey = processType+"."+id;
		variables.put("businessKey", businessKey);
		//使用流程定义的key，启动流程实例，同时设置流程变量，同时向正在执行的执行对象表中的字段BUSINESS_KEY添加业务数据，同时让流程关联业务
		ProcessInstance processInstance=runtimeService.startProcessInstanceByKey(processType,businessKey,variables);
		return processInstance;
	}
	/**
	 * 根据流程定义号，所需资源类型取到资源的输出流
	 * @param 流程定义ID
	 * @param 所需资源类型
	 * @param response
	 * @throws IOException
	 */
	public void resourceRead(String processDefinitionId,String resourceType,HttpServletResponse response) throws IOException {
		ProcessDefinition processDefinition = repositoryService
				.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId).singleResult();
		String resourceName = "";
		if (resourceType.equals("image")) {
			resourceName = processDefinition.getDiagramResourceName();
		} else if (resourceType.equals("xml")) {
			resourceName = processDefinition.getResourceName();
		}
		InputStream resourceAsStream = repositoryService.getResourceAsStream(
				processDefinition.getDeploymentId(), resourceName);
		byte[] b = new byte[1024];
		int len = -1;

		while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
			response.getOutputStream().write(b, 0, len);
		}
	}
	public void resourceReadByprocessId(String processId,String resourceType,HttpServletResponse response) throws IOException{
		HistoricProcessInstance historicProcessInstance=historyService.createHistoricProcessInstanceQuery()
			.processInstanceId(processId)
			.singleResult();
		if(historicProcessInstance!=null){
			String processDefinitionId=historicProcessInstance.getProcessDefinitionId();
			resourceRead(processDefinitionId,resourceType,response);
		}
	}
	/**
	 * 带图片跟踪的流程实例
	 */
	public void traceImage(String processInstanceId,HttpServletResponse response) throws IOException{
		Command<InputStream> cmd = new HistoryProcessInstanceDiagramCmd(processInstanceId);

		InputStream is = managementService.executeCommand(cmd);
		int len = 0;
		byte[] b = new byte[1024];

		while ((len = is.read(b, 0, 1024)) != -1) {
			response.getOutputStream().write(b, 0, len);
		}
	}
	
	/**
	 * 查找个人任务，返回实例号
	 */
	public List<String> findMyTaskReturnProcessInstanceId(int page,int rows,UserInfo u){
		List<String> l=new ArrayList<String>();
		int start=(page-1)*rows;
		//Yusen
		Long userId=u.getId();
		List<Task> list=taskService.createTaskQuery()
			.taskCandidateOrAssigned(userId+"")
			.orderByTaskCreateTime()
			.asc()
			.listPage(start, rows);
		for(Task t:list){
			String processInstanceId=t.getProcessInstanceId();
			l.add(processInstanceId);
		}
		return l;
	}
	/**
	 * 查找个人任务，包括所在组的组任务，返回任务号
	 * @param page
	 * @param rows
	 * @param u
	 * @return 任务号
	 */
	public List<String> findMyTaskReturnTaskId(int page,int rows,UserInfo u){
		List<String> l=new ArrayList<String>();
		int start=(page-1)*rows;
		String userId=u.getId()+"";
		List<Task> list=taskService.createTaskQuery()
				.taskCandidateOrAssigned(userId)
				.orderByTaskCreateTime()
				.asc().list();
		for(Task t:list){
			l.add(t.getId());
		}
		return l;
	}
	public long findMyTaskReturnTaskIdCount(UserInfo u){
		//Yusen
		Long userId=u.getId();
		long l=taskService.createTaskQuery()
				.taskCandidateOrAssigned(userId+"")
				.count();
		return l;
	}
	
	/**
	 * 查找个人任务,不包括组任务，返回任务号
	 * @param page
	 * @param rows
	 * @param u
	 * @return 任务号
	 */
	public List<String> findMySelfTaskReturnTaskId(int page,int rows,UserInfo u){
		List<String> l=new ArrayList<String>();
		int start=(page-1)*rows;
		String userId=u.getId()+"";
		List<Task> list=taskService.createTaskQuery()
				//.taskAssignee(userId)
				.taskAssigneeLike("%"+userId+"%")
				//.taskCandidateOrAssigned(userId)
				.orderByTaskCreateTime()
				.asc().list();
		for(Task t:list){
			l.add(t.getId());
		}
		return l;
	}
	
	/**
	 * 查找个人组任务，返回实例号
	 */
	public List<String> findMyGroupTaskReturnProcessInstanceId(int page,int rows,UserInfo u){
		List<String> l=new ArrayList<String>();
		int start=(page-1)*rows;
		//Yusen
		Long userId=u.getId();
		List<Group> groups=identityService.createGroupQuery()
			.groupMember(userId+"")
			.list();
		List<String> groupIds=new ArrayList<String>();
		for(Group group:groups){
			groupIds.add(group.getId());
		}
		if(groupIds==null||"".equals(groupIds)||groupIds.size()==0){
			return null;
		}
		List<Task> list=taskService.createTaskQuery()
				.taskCandidateGroupIn(groupIds)
				.orderByTaskCreateTime()
				.asc()
				.listPage(start, rows);
		for(Task t:list){
			String processInstanceId=t.getProcessInstanceId();
			l.add(processInstanceId);
		}
		return l;
	}
	/**
	 * 查找个人组任务，返回任务ID
	 * @param page
	 * @param rows
	 * @param u
	 * @return 任务ID
	 */
	public List<String> findMyGroupTaskReturnTaskId(int page,int rows,UserInfo u){
		List<String> l=new ArrayList<String>();
		int start=(page-1)*rows;
		//Yusen
		Long userId=u.getId();
		List<Group> groups=identityService.createGroupQuery()
				.groupMember(userId+"")
				.list();
		List<String> groupIds=new ArrayList<String>();
		for(Group group:groups){
			groupIds.add(group.getId());
		}
		if(groupIds==null||"".equals(groupIds)||groupIds.size()==0){
			return null;
		}
		List<Task> list=taskService.createTaskQuery()
				.taskCandidateGroupIn(groupIds)
				.orderByTaskCreateTime()
				.asc()
				.listPage(start, rows);
		for(Task t:list){
			l.add(t.getId());
		}
		return l;
	}
	public long findMyGroupTaskReturnTaskIdCount(UserInfo u){
		//Yusen
		Long userId=u.getId();
		List<Group> groups=identityService.createGroupQuery()
				.groupMember(userId+"")
				.list();
		List<String> groupIds=new ArrayList<String>();
		for(Group group:groups){
			groupIds.add(group.getId());
		}
		if(groupIds==null||"".equals(groupIds)||groupIds.size()==0){
			return 0;
		}
		long l=taskService.createTaskQuery()
				.taskCandidateGroupIn(groupIds)
				.count();
		return l;
	}
	/**
	 * 查找个人已完成历史任务，返回实例号
	 */
	public List<String> findMyHiTask(int page,int rows,UserInfo u){
		List<String> l=new ArrayList<String>();
		int start=(page-1)*rows;
		//Yusen
		Long userId=u.getId();
		List<HistoricTaskInstance> list=historyService.createHistoricTaskInstanceQuery()
				.taskAssignee(userId+"")
				.finished()
				.orderByTaskCreateTime()
				.asc()
				.listPage(start, rows);
		for(HistoricTaskInstance t:list){
			String processInstanceId=t.getProcessInstanceId();
			l.add(processInstanceId);
		}
		return l;
	}
	/**
	 * 查找个人已完成历史任务，返回数量
	 */
	public long findMyHiTaskCount(UserInfo u){
		//Yusen
		Long userId=u.getId();
		Long l=historyService.createHistoricTaskInstanceQuery()
				.taskAssignee(userId+"")
				.finished()
				.count();
		return l;
	}
	/**
	 * 创建用户组
	 * @param 用户组id
	 * @param 用户组名称
	 * @param 用户组类型
	 */
	public void createGroup(String groupId,String groupName,String groupType){
		Group group=identityService.newGroup(groupId);
		group.setName(groupName);
		group.setType(groupType);
		identityService.saveGroup(group);
	}
	/**
	 * 更新用户组
	 * @param 用户组id
	 * @param 用户组名称
	 * @param 用户组类型
	 */
	public void updateGroup(String groupId,String groupName,String groupType){
		Group group=identityService.createGroupQuery().groupId(groupId).singleResult();
		if(group==null||"".equals(group)){
			group=identityService.newGroup(groupId);
		}
		group.setName(groupName);
		group.setType(groupType);
		identityService.saveGroup(group);
	}
	/**
	 * 删除用户组
	 * @param 用户组id
	 */
	public void deleteGroup(String groupId){
		identityService.deleteGroup(groupId);
	}
	/**
	 * 创建用户组关系
	 * @param 用户id
	 * @param 用户组id
	 */
	public void createGroupMemberShip(String userId,String groupId){
		identityService.createMembership(userId, groupId);
	}
	
	/**
	 * 获取连线名称和代码
	 */
	public List<String> findOutComeListByTaskId(String taskId) {
				//返回存放连线的名称集合
				List<String> list = new ArrayList<String>();
				//1:使用任务ID，查询任务对象
				Task task = taskService.createTaskQuery()//
							.taskId(taskId)//使用任务ID查询
							.singleResult();
				//2：获取流程定义ID
				String processDefinitionId = task.getProcessDefinitionId();
				//3：查询ProcessDefinitionEntiy对象
				ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId);
				//使用任务对象Task获取流程实例ID
				String processInstanceId = task.getProcessInstanceId();
				//使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
				ProcessInstance pi = runtimeService.createProcessInstanceQuery()//
							.processInstanceId(processInstanceId)//使用流程实例ID查询
							.singleResult();
				//获取当前活动的id
				String activityId = pi.getActivityId();
				//4：获取当前的活动
				ActivityImpl activityImpl = processDefinitionEntity.findActivity(activityId);
				//5：获取当前活动完成之后连线的名称
				List<PvmTransition> pvmList = activityImpl.getOutgoingTransitions();
				if(pvmList!=null && pvmList.size()>0){
					for(PvmTransition pvm:pvmList){
						String name = (String) pvm.getProperty("name");
						if(StringUtils.isNotBlank(name)){
							list.add(name);
						}
						else{
							list.add("默认提交");
						}
					}
				}
				return list;
	}
	/**
	 * 获取form key
	 */
	public String findFormValue(String taskId) {
		//Task task=taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
		TaskFormData td=formService.getTaskFormData(taskId);
		String x=td.getFormKey();
		return x;
	}
	
	/**
	 * 签收任务
	 * @param 流程实例ID
	 * @param 用户ID
	 */
	public void claimTaskByProcessInstanceId(String processInstanceId,String userId) {
		Task task=taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
		claimTaskByTaskId(task.getId(),userId);
	}
	/**
	 * 完成任务
	 * @param 流程实例ID
	 * @param 用户ID
	 */
	public void completeTaskByProcessInstanceId(String processInstanceId,String userId,Boolean flag) {
		Task task=taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();
		completeTaskByTaskId(task.getId(),userId,flag);
	}
	/**
	 * 签收任务
	 * @param 任务ID
	 * @param 用户ID
	 */
	public void claimTaskByTaskId(String taskId,String userId) {
		taskService.claim(taskId, userId);
	}
	/**
	 * 完成任务
	 * @param 任务ID
	 * @param 用户ID
	 */
	public void completeTaskByTaskId(String taskId,String userId,Boolean flag) {
		String variable=findFormValue(taskId);
		taskService.setAssignee(taskId, userId);
		if(StringUtils.isNotBlank(variable)){
			Map<String, Object> variables = new HashMap<String,Object>();
			//取到formkey的值来设置变量
			variables.put(variable, flag);
			//使用任务ID，完成当前人的个人任务，同时流程变量
			taskService.complete(taskId, variables);
		}else{
			taskService.complete(taskId);
		}
	}
	/**
	 * 添加备注信息
	 * @param taskId
	 * @param message
	 * @param userId
	 * @param type
	 */
	public void addCommentByTaskId(String taskId,String message,String userId,String type){
		Authentication.setAuthenticatedUserId(userId);
		//使用任务ID，查询任务对象，获取流程流程实例ID
		Task task = taskService.createTaskQuery()//
								.taskId(taskId)//使用任务ID查询
								.singleResult();
		//获取流程实例ID
		String processInstanceId = task.getProcessInstanceId();
		if(message!=null){
			taskService.addComment(taskId, processInstanceId, type,message);
		}else{
			taskService.addComment(taskId, processInstanceId, type,"");
		}
	}
	/**
	 * 添加备注信息
	 * @param processInstanceId
	 * @param message
	 * @param userId
	 * @param type
	 */
	public void addCommentByProcessInstanceId(String processInstanceId,String message,String userId,String type){
		//使用任务ID，查询任务对象，获取流程流程实例ID
		Task task = taskService.createTaskQuery()//
				.processInstanceId(processInstanceId)
				.singleResult();
		//获取流程实例ID
		String taskId = task.getId();
		Authentication.setAuthenticatedUserId(userId);
		if(message!=null){
			taskService.addComment(taskId, processInstanceId, type,message);
		}else{
			taskService.addComment(taskId, processInstanceId, type,"");
		}
	}
	/**
	 * 判断流程是否结束
	 * @param 流程实例ID
	 * @return 是否流程结束
	 */
	public boolean processIsEnd(String processInstanceId){
		ProcessInstance pi = runtimeService.createProcessInstanceQuery()//
				.processInstanceId(processInstanceId)//使用流程实例ID查询
				.singleResult();
		//流程结束了
		if(pi==null){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 获取批注信息，传递的是当前任务ID，获取历史任务ID对应的批注
	 * */
	public List<?> findCommentByTaskId(String taskId) {
		List<?> list = new ArrayList<Comment>();
		//使用当前的任务ID，查询当前流程对应的历史任务ID
		//使用当前任务ID，获取当前任务对象
		Task task = taskService.createTaskQuery()//
				.taskId(taskId)//使用任务ID查询
				.singleResult();
		//获取流程实例ID
		String processInstanceId = task.getProcessInstanceId();
		list = taskService.getProcessInstanceComments(processInstanceId);
		return list;
//		//使用流程实例ID，查询历史任务，获取历史任务对应的每个任务ID
//		List<HistoricTaskInstance> htiList = historyService.createHistoricTaskInstanceQuery()//历史任务表查询
//						.processInstanceId(processInstanceId)//使用流程实例ID查询
//						.list();
//		//遍历集合，获取每个任务ID
//		if(htiList!=null && htiList.size()>0){
//			for(HistoricTaskInstance hti:htiList){
//				//任务ID
//				String htaskId = hti.getId();
//				//获取批注信息
//				List<Comment> taskList = taskService.getTaskComments(htaskId);//对用历史完成后的任务ID
//				list.addAll(taskList);
//			}
//		}
	}
	/**
	 * 通过流程实例取备注
	 * @param processInstanceId
	 * @return 备注集合
	 */
	public List<?> findCommentByprocessInstanceId(String processInstanceId) {
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		List<Comment> list = new ArrayList<Comment>();
		list = taskService.getProcessInstanceComments(processInstanceId);
		for(Comment comment:list){
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("type",comment.getType());
			map.put("processInstanceId",comment.getProcessInstanceId());
			map.put("userId",comment.getUserId());
			map.put("message",comment.getFullMessage());
			map.put("time",dateFormat.format(comment.getTime()));
			result.add(map);
		}
		return result;
	}
	/**
	 * 通过任务id来找流程实例id
	 * @param taskId
	 * @return 流程实例id
	 */
	public String getProcessIdByTaskId(String taskId){
		 HistoricTaskInstance task=historyService.createHistoricTaskInstanceQuery().taskId(taskId)
		.singleResult();
		return task.getProcessInstanceId();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Page<?> findMyStartFinishProcess(int page, int rows, UserInfo u) {
		List<Map<String,Object>> l=new ArrayList<Map<String,Object>>();
		int start=(page-1)*rows;
		List<HistoricProcessInstance> list=historyService.createHistoricProcessInstanceQuery()
			.startedBy(u.getId()+"")//Yusen
			.finished()
			.orderByProcessInstanceStartTime()
			.asc()
			.listPage(start, rows);
		long total=historyService.createHistoricProcessInstanceQuery()
				.startedBy(u.getId()+"")//Yusen
				.finished()
				.count();
		for(HistoricProcessInstance hpi:list){
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("prodefId", hpi.getProcessDefinitionId());
			String prodefName=repositoryService.createProcessDefinitionQuery().processDefinitionId(hpi.getProcessDefinitionId()).singleResult().getName();
			map.put("prodefName", prodefName);
			map.put("bussinessKey", hpi.getBusinessKey());
			map.put("processInstanceId", hpi.getId());
			map.put("startUserId", hpi.getStartUserId());
			map.put("startTime", dateFormat.format(hpi.getStartTime()));
			map.put("endTime", dateFormat.format(hpi.getEndTime()));
			long day=hpi.getDurationInMillis()/(1000*3600*24);
			long hour=(hpi.getDurationInMillis()%(1000*3600*24))/(1000*3600);
			long second=((hpi.getDurationInMillis()%(1000*3600*24))%(1000*3600))/(60*1000);
			map.put("duration", day+"天"+hour+"时"+second+"分");
			l.add(map);
			//total++;
			//map.put("teach", "");
		}
		/*for(HistoricProcessInstance hpi:list){
			WorkflowProcessIstanceDTO wpi=new WorkflowProcessIstanceDTO();
			wpi.set
		}*/
		return new Page(total, l);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Page<?> findMyStartUnfinishProcess(int page, int rows, UserInfo u) {
		List<Map<String,Object>> l=new ArrayList<Map<String,Object>>();
		int start=(page-1)*rows;
		List<HistoricProcessInstance> list=historyService.createHistoricProcessInstanceQuery()
				.startedBy(u.getId()+"")//Yusen
				.unfinished()
				.orderByProcessInstanceStartTime()
				.asc()
				.listPage(start, rows);
		long total=historyService.createHistoricProcessInstanceQuery()
				.startedBy(u.getId()+"")//Yusen
				.unfinished()
				.count();
		for(HistoricProcessInstance hpi:list){
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("prodefId", hpi.getProcessDefinitionId());
			String prodefName=repositoryService.createProcessDefinitionQuery().processDefinitionId(hpi.getProcessDefinitionId()).singleResult().getName();
			List<Task> listtask=taskService.createTaskQuery().processInstanceId(hpi.getId()).list();
			map.put("prodefName", prodefName);
			map.put("bussinessKey", hpi.getBusinessKey());
			map.put("processInstanceId", hpi.getId());
			map.put("startUserId", hpi.getStartUserId());
			map.put("startTime", dateFormat.format(hpi.getStartTime()));
			String tech="";
			for(Task task:listtask){
				tech=tech+task.getName()+",";
			}
			if(!"".equals(tech)||tech!=null){
				tech=tech.substring(0, tech.length()-1);
			}
			map.put("tech", tech);
			l.add(map);
			//total++;
			//map.put("teach", "");
		}
		/*for(HistoricProcessInstance hpi:list){
			WorkflowProcessIstanceDTO wpi=new WorkflowProcessIstanceDTO();
			wpi.set
		}*/
		return new Page(total, l);
	}
	/**
	 * 通过流程实例id来删除实例
	 * @param processInstanceId
	 * @param reason
	 */
	public void destoryProcessIdByProcessInstanceId(String processInstanceId,String reason){
		runtimeService.deleteProcessInstance(processInstanceId, reason);
	}

	public void TransTaskByTaskId(String taskId, String userId) {
		taskService.setAssignee(taskId, userId);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Page<?> findAllStartProcess(int page, int rows) {
		List<Map<String,Object>> l=new ArrayList<Map<String,Object>>();
		int start=(page-1)*rows;
		List<Task> listtask=taskService.createTaskQuery()
			.orderByTaskCreateTime()
			.asc()
			.listPage(start, rows);
		long total=taskService.createTaskQuery()
				.count();
		for(Task task:listtask){
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("prodefId", task.getProcessDefinitionId());
			String prodefName=repositoryService.createProcessDefinitionQuery().processDefinitionId(task.getProcessDefinitionId()).singleResult().getName();
			map.put("prodefName", prodefName);
			ProcessInstance  p=runtimeService.createProcessInstanceQuery().processInstanceId(task.getProcessInstanceId()).singleResult();
			String businessKey=p.getBusinessKey();
			map.put("bussinessKey", businessKey);
			map.put("taskId", task.getId());
			map.put("processInstanceId", task.getProcessInstanceId());
			map.put("startUserId", p.getProcessVariables().get("applyUserId"));
			map.put("startTime", dateFormat.format(task.getCreateTime()));
			map.put("tech", task.getName());
			l.add(map);
		}
		return new Page(total, l);
	}
	/**
	 * 通过任务id来找流程定义id
	 * @param taskId
	 * @return 流程定义id
	 */
	@Override
	public String getProcessDefIdByTaskId(String taskId) {
		// TODO Auto-generated method stub
		HistoricTaskInstance task=historyService.createHistoricTaskInstanceQuery().taskId(taskId)
				.singleResult();
		return task.getProcessDefinitionId();
	}
	/**
	 * 查找个人任务，返回任务号
	 * @param page
	 * @param rows
	 * @param u
	 * @return 任务号
	 */
	public List<String> findMyTaskReturnTaskId(UserInfo u){
		List<String> l=new ArrayList<String>();
		String userId=u.getId()+"";
		List<Task> list=taskService.createTaskQuery()
				.taskCandidateOrAssigned(userId)
				.orderByTaskCreateTime()
				.asc().list();
		for(Task t:list){
			l.add(t.getId());
		}
		return l;
	}
	/**
	 * 根据流程定义的key，查找个人任务，返回任务号
	 * @param page
	 * @param rows
	 * @param u
	 * @return 任务号
	 */
	public List<String> findMyTaskReturnTaskIdByprocessDefinitionKey(int page,int rows,UserInfo u,String processDefinitionKey){
		List<String> l=new ArrayList<String>();
		int start=(page-1)*rows;
		String userId=u.getId()+"";
		List<Task> list=taskService.createTaskQuery()
				.processDefinitionKey(processDefinitionKey)
				.taskCandidateOrAssigned(userId)
				.orderByTaskCreateTime()
				.asc().listPage(start, rows);
		for(Task t:list){
			l.add(t.getId());
		}
		return l;
	}
	
}
