package com.sinosoft.zcfz.service.glnlpg.activiti;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.web.multipart.MultipartFile;

import com.sinosoft.common.Page;
import com.sinosoft.entity.UserInfo;

public interface ActivitiService {
	
	void saveNewDeploye(MultipartFile file, String filename);
	
	
	public Page<?> viewProcessDeploy(int page,int rows);
	
	public Page<?> viewProdef(int page,int rows);

	//List<Deployment> findDeploymentList();

	//List<ProcessDefinition> findProcessDefinitionList();

	public InputStream findImageInputStream(String deploymentId, String imageName);
	
	public void resourceRead(String processDefinitionId,String resourceType,HttpServletResponse response) throws IOException;
	
	public void resourceReadByprocessId(String processId,String resourceType,HttpServletResponse response) throws IOException;
	
	public void traceImage(String processInstanceId,HttpServletResponse response) throws IOException;

	public void deleteProcessDefinitionByDeploymentId(String deploymentId);
	
	public List<String> findMyTaskReturnProcessInstanceId(int page,int rows,UserInfo u);
	
	public List<String> findMyTaskReturnTaskId(int page,int rows,UserInfo u);
	
	public List<String> findMySelfTaskReturnTaskId(int page,int rows,UserInfo u);
	
	public long findMyTaskReturnTaskIdCount(UserInfo u);
	
	public List<String> findMyTaskReturnTaskId(UserInfo u);
	
	public List<String> findMyGroupTaskReturnProcessInstanceId(int page,int rows,UserInfo u);
	
	public List<String> findMyTaskReturnTaskIdByprocessDefinitionKey(int page,int rows,UserInfo u,String processDefinitionKey);
	
	public List<String> findMyGroupTaskReturnTaskId(int page,int rows,UserInfo u);
	
	public long findMyGroupTaskReturnTaskIdCount(UserInfo u);
	
	public List<String> findMyHiTask(int page,int rows,UserInfo u);
	/**
	 * 查询个人完成任务数量
	 * @param u
	 * @return 数量
	 */
	public long findMyHiTaskCount(UserInfo u);

	public ProcessInstance startProcess(String processType,String id,Map<String, Object> variables);
	
	public void createGroup(String groupId,String groupName,String groupType);
	
	public void updateGroup(String groupId,String groupName,String groupType);
	
	public void deleteGroup(String groupId);
	
	public void createGroupMemberShip(String userId,String groupId);
	
	public List<String> findOutComeListByTaskId(String taskId);
	
	public String findFormValue(String processId);
	
	public void claimTaskByProcessInstanceId(String processInstanceId,String userId);
	
	public void completeTaskByProcessInstanceId(String processInstanceId,String userId,Boolean flag);
	
	public void claimTaskByTaskId(String taskId,String userId);
	/**
	 * 转移任务
	 * @param taskId
	 * @param 转移到人的id
	 */
	public void TransTaskByTaskId(String taskId,String userId);
	
	public void completeTaskByTaskId(String taskId,String userId,Boolean flag);
	
	/**
	 * 添加备注信息
	 * @param taskId
	 * @param message
	 * @param userId
	 * @param type
	 */
	public void addCommentByTaskId(String taskId,String message,String userId,String type);
	
	public void addCommentByProcessInstanceId(String processInstanceId,String message,String userId,String type);
	
	public boolean processIsEnd(String processInstanceId);
	
	public List<?> findCommentByTaskId(String taskId);
	
	public List<?> findCommentByprocessInstanceId(String processInstanceId);
	
	/**
	 * 通过任务id来找流程实例id
	 * @param taskId
	 * @return 流程实例id
	 */
	public String getProcessIdByTaskId(String taskId);
	
	/**
	 * 通过任务id来找流程定义id
	 * @param taskId
	 * @return 流程定义id
	 */
	public String getProcessDefIdByTaskId(String taskId);
	
	/**
	 * 查找我发起已完成的流程
	 * @param page
	 * @param rows
	 * @param u
	 * @return
	 */
	public  Page<?> findMyStartFinishProcess(int page,int rows,UserInfo u);
	/**
	 * 查找我发起未完成的流程
	 * @param page
	 * @param rows
	 * @param u
	 * @return
	 */
	public  Page<?> findMyStartUnfinishProcess(int page,int rows,UserInfo u);
	
	/**
	 * 查找所有的已经启动的流程
	 * @param page
	 * @param rows
	 * @return
	 */
	public Page<?> findAllStartProcess(int page, int rows);
	
	/**
	 * 通过流程实例id来删除实例
	 * @param processInstanceId
	 * @param reason
	 */
	public void destoryProcessIdByProcessInstanceId(String processInstanceId,String reason);
	
	//List<Task> findTaskListByName(String name);

	//String findTaskFormKeyByTaskId(String taskId);

	//LeaveBill findLeaveBillByTaskId(String taskId);

	//List<String> findOutComeListByTaskId(String taskId);

	//void saveSubmitTask(WorkflowDTO workflowBean);

	//List<Comment> findCommentByTaskId(String taskId);

	//List<Comment> findCommentByLeaveBillId(Long id);

	//ProcessDefinition findProcessDefinitionByTaskId(String taskId);

	//Map<String, Object> findCoordingByTask(String taskId);


}
