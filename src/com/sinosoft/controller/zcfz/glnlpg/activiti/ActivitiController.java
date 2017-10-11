package com.sinosoft.controller.zcfz.glnlpg.activiti;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.sinosoft.common.CurrentUser;
import com.sinosoft.common.InvokeResult;
import com.sinosoft.common.Page;
import com.sinosoft.entity.UserInfo;
import com.sinosoft.zcfz.dto.glnlpg.WorkflowDTO;
import com.sinosoft.zcfz.service.glnlpg.activiti.ActivitiService;

/**
 * @Description: TODO(工作流程定义与实例等资源处理类)
 * @author zh
 *
 */
@Controller
@RequestMapping("/activitiController")
public class ActivitiController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiController.class);

	@Resource
	private ActivitiService activitiService;

	// 部署流程
	@RequestMapping(path = "processDeploy", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult deployprocessDefine(WorkflowDTO workflowDTO, MultipartFile file) {
		try {
			// 获取页面传递的值
			// 1：获取页面上传递的zip格式的文件，格式是File类型
			// 文件名称
			String filename = workflowDTO.getFilename();
			// 完成部署
			activitiService.saveNewDeploye(file, filename);
			return InvokeResult.success();
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure("部署失败，原因为:" + e);
		}
	}

	/**
	 * 流程部署查看
	 * 
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(path = "viewProcessDeploy")
	@ResponseBody
	public Page<?> viewProcessDeploy(@RequestParam int page, @RequestParam int rows) {
		Page<?> pagelist = activitiService.viewProcessDeploy(page, rows);
		return pagelist;
	}

	/**
	 * 流程定义查看
	 * 
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(path = "viewProdef")
	@ResponseBody
	public Page<?> viewProdef(@RequestParam int page, @RequestParam int rows) {
		Page<?> pagelist = activitiService.viewProdef(page, rows);
		return pagelist;
	}

	/**
	 * 删除部署的流程，级联删除流程实例
	 * 
	 * @param deploymentId
	 *            流程部署ID
	 */
	@RequestMapping(path = "delDeploy", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult delDeploy(@RequestParam("deploymentId") String deploymentId) {
		try {
			activitiService.deleteProcessDefinitionByDeploymentId(deploymentId);
			return InvokeResult.success();
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("删除失败，原因为:" + e);
			return InvokeResult.failure("删除失败，原因为:" + e);
		}
	}

	@RequestMapping(path = "/viewTraceImage")
	@ResponseBody
	public InvokeResult viewTraceImage(@RequestParam String processInstanceId, HttpServletResponse response) {
		try {
			activitiService.traceImage(processInstanceId, response);
			return InvokeResult.success();
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}

	@RequestMapping(path = "/findMyTask")
	@ResponseBody
	public InvokeResult findMyTask(@RequestParam int page, @RequestParam int rows) {
		UserInfo userInfo = CurrentUser.getCurrentUser();
		try {
			activitiService.findMyTaskReturnTaskId(page, rows, userInfo);
			return InvokeResult.success();
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}

	@RequestMapping(path = "findMyStartProcess")
	@ResponseBody
	public Page<?> findMyStartProcess(@RequestParam int page, @RequestParam int rows,@RequestParam String taskType) {
		UserInfo userInfo = CurrentUser.getCurrentUser();
		if("1".equals(taskType)){
			Page<?> result=activitiService.findMyStartFinishProcess(page,rows,userInfo);
			return result;
		}else if("2".equals(taskType)){
			Page<?> result=activitiService.findMyStartUnfinishProcess(page,rows,userInfo);
			return result;
		}
		return null;
	}
	
	@RequestMapping(path="/viewtraceImage")
	@ResponseBody
	public InvokeResult viewtraceImage(@RequestParam String processInstanceId,HttpServletResponse response){
		try {
			activitiService.traceImage(processInstanceId,response);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
	/**
	 * 返回业务单界面
	 * @param bussinessKey
	 * @return 业务单界面
	 */
	@RequestMapping(path="/viewbussiness")
	@ResponseBody
	public List<String> viewbussiness(@RequestParam String bussinessKey){
		try {
			List<String> list=new ArrayList<String>();
			String[] view=bussinessKey.split("\\.");
			list.add(view[0]+"-view.jsp");
			list.add(view[1]);
			return list; 
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(path="/findComment")
	@ResponseBody
	public List<?> findComment(@RequestParam String processInstanceId){
		try {
			List<?> result=activitiService.findCommentByprocessInstanceId(processInstanceId);
			return result; 
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@RequestMapping(path = "/resource/process-def")
	@ResponseBody
	public InvokeResult viewResourceByDef(@RequestParam String processDefId,@RequestParam String type,HttpServletResponse response) {
		try {
			activitiService.resourceRead(processDefId,type, response);
			return InvokeResult.success();
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
	/*
	 * //** 我的流程定义
	 */
	/*
	 * @RequestMapping(params = "myProcessList") public ModelAndView
	 * myProcessList(HttpServletRequest request) { return new
	 * ModelAndView("jeecg/activiti/my/myProcessList"); }
	 *//**
		 * 流程启动表单选择
		 */
	/*
	 * @RequestMapping(params = "startPageSelect") public ModelAndView
	 * startPageSelect(@RequestParam("startPage") String
	 * startPage,HttpServletRequest request) {
	 * 
	 * return new ModelAndView("jeecg/activiti/my/"+startPage.substring(0,
	 * startPage.lastIndexOf("."))); }
	 *//**
		 * easyui 运行中流程列表页面
		 * 
		 * @param request
		 * @param response
		 * @param dataGrid
		 */
	/*
	 * 
	 * @RequestMapping(params = "runningProcessList") public ModelAndView
	 * runningProcessList(HttpServletRequest request, HttpServletResponse
	 * response, DataGrid dataGrid) { return new
	 * ModelAndView("jeecg/activiti/process/runninglist"); }
	 *//**
		 * easyui 运行中流程列表数据
		 * 
		 * @param request
		 * @param response
		 * @param dataGrid
		 */
	/*
	 * 
	 * @RequestMapping(params = "runningProcessDataGrid") public void
	 * runningProcessDataGrid(HttpServletRequest request, HttpServletResponse
	 * response, DataGrid dataGrid) {
	 * 
	 * List<HistoricProcessInstance> historicProcessInstances = historyService
	 * .createHistoricProcessInstanceQuery() .unfinished().list();
	 * ProcessInstanceQuery processInstanceQuery =
	 * runtimeService.createProcessInstanceQuery(); List<ProcessInstance> list =
	 * processInstanceQuery.list();
	 * 
	 * StringBuffer rows = new StringBuffer(); for(ProcessInstance hi : list){
	 * rows.append("{'id':"+hi.getId()+",'processDefinitionId':'"+hi.
	 * getProcessDefinitionId()
	 * +"','processInstanceId':'"+hi.getProcessInstanceId
	 * ()+"','activityId':'"+hi.getActivityId()+"'},"); }
	 * 
	 * 
	 * String rowStr = StringUtils.substringBeforeLast(rows.toString(), ",");
	 * 
	 * JSONObject jObject =
	 * JSONObject.fromObject("{'total':"+list.size()+",'rows':["+rowStr+"]}");
	 * responseDatagrid(response, jObject); }
	 *//**
		 * 读取工作流定义的图片或xml
		 * 
		 * @throws Exception
		 */
	/*
	 * @RequestMapping(params = "resourceRead") public void
	 * resourceRead(@RequestParam("processDefinitionId") String
	 * processDefinitionId, @RequestParam("resourceType") String resourceType,
	 * HttpServletResponse response) throws Exception { ProcessDefinition
	 * processDefinition =
	 * repositoryService.createProcessDefinitionQuery().processDefinitionId
	 * (processDefinitionId).singleResult(); String resourceName = ""; if
	 * (resourceType.equals("image")) { resourceName =
	 * processDefinition.getDiagramResourceName(); } else if
	 * (resourceType.equals("xml")) { resourceName =
	 * processDefinition.getResourceName(); } InputStream resourceAsStream =
	 * repositoryService
	 * .getResourceAsStream(processDefinition.getDeploymentId(), resourceName);
	 * byte[] b = new byte[1024]; int len = -1;
	 * 
	 * while ((len = resourceAsStream.read(b, 0, 1024)) != -1) {
	 * response.getOutputStream().write(b, 0, len); } }
	 *//**
		 * 读取带跟踪的流程图片
		 * 
		 * @throws Exception
		 */
	/*
	 * @RequestMapping(params = "traceImage") public void
	 * traceImage(@RequestParam("processInstanceId") String processInstanceId,
	 * HttpServletResponse response) throws Exception {
	 * 
	 * Command<InputStream> cmd = new HistoryProcessInstanceDiagramCmd(
	 * processInstanceId);
	 * 
	 * ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	 * InputStream is = processEngine.getManagementService().executeCommand(
	 * cmd);
	 * 
	 * int len = 0; byte[] b = new byte[1024];
	 * 
	 * while ((len = is.read(b, 0, 1024)) != -1) {
	 * response.getOutputStream().write(b, 0, len); } }
	 *//**
		 * easyui 流程历史页面
		 * 
		 * @param request
		 * @param response
		 * @param dataGrid
		 */
	/*
	 * 
	 * @RequestMapping(params = "viewProcessInstanceHistory") public
	 * ModelAndView
	 * viewProcessInstanceHistory(@RequestParam("processInstanceId") String
	 * processInstanceId, HttpServletRequest request, HttpServletResponse
	 * respone,Model model) {
	 * 
	 * model.addAttribute("processInstanceId", processInstanceId);
	 * 
	 * return new
	 * ModelAndView("jeecg/activiti/process/viewProcessInstanceHistory"); }
	 *//**
		 * easyui 流程历史数据获取
		 * 
		 * @param request
		 * @param response
		 * @param dataGrid
		 */
	/*
	 * 
	 * @RequestMapping(params = "taskHistoryList") public void
	 * taskHistoryList(@RequestParam("processInstanceId") String
	 * processInstanceId, HttpServletRequest request, HttpServletResponse
	 * response,DataGrid dataGrid) {
	 * 
	 * List<HistoricTaskInstance> historicTasks = historyService
	 * .createHistoricTaskInstanceQuery()
	 * .processInstanceId(processInstanceId).list();
	 * 
	 * StringBuffer rows = new StringBuffer(); for(HistoricTaskInstance hi :
	 * historicTasks){
	 * rows.append("{'name':'"+hi.getName()+"','processInstanceId':'"
	 * +hi.getProcessInstanceId()
	 * +"','startTime':'"+hi.getStartTime()+"','endTime':'"
	 * +hi.getEndTime()+"','assignee':'"
	 * +hi.getAssignee()+"','deleteReason':'"+hi.getDeleteReason()+"'},");
	 * //System
	 * .out.println(hi.getName()+"@"+hi.getAssignee()+"@"+hi.getStartTime
	 * ()+"@"+hi.getEndTime()); }
	 * 
	 * String rowStr = StringUtils.substringBeforeLast(rows.toString(), ",");
	 * 
	 * JSONObject jObject =
	 * JSONObject.fromObject("{'total':"+historicTasks.size(
	 * )+",'rows':["+rowStr+"]}"); responseDatagrid(response, jObject); }
	 *//**
		 * easyui AJAX请求数据
		 * 
		 * @param request
		 * @param response
		 * @param dataGrid
		 */
	/*
	 * 
	 * @RequestMapping(params = "datagrid") public void
	 * datagrid(HttpServletRequest request, HttpServletResponse response,
	 * DataGrid dataGrid) {
	 * 
	 * ProcessDefinitionQuery query =
	 * repositoryService.createProcessDefinitionQuery(); List<ProcessDefinition>
	 * list = query.list();
	 * 
	 * StringBuffer rows = new StringBuffer(); int i = 0; for(ProcessDefinition
	 * pi : list){ i++;
	 * rows.append("{'id':"+i+",'processDefinitionId':'"+pi.getId()
	 * +"','startPage':'"
	 * +pi.getDescription()+"','resourceName':'"+pi.getResourceName
	 * ()+"','deploymentId':'"
	 * +pi.getDeploymentId()+"','key':'"+pi.getKey()+"','name':'"
	 * +pi.getName()+"','version':'"
	 * +pi.getVersion()+"','isSuspended':'"+pi.isSuspended()+"'},"); } String
	 * rowStr = StringUtils.substringBeforeLast(rows.toString(), ",");
	 * 
	 * JSONObject jObject =
	 * JSONObject.fromObject("{'total':"+query.count()+",'rows':["+rowStr+"]}");
	 * responseDatagrid(response, jObject); }
	 *//**
		 * easyui 待领任务页面
		 * 
		 * @param request
		 * @param response
		 * @param dataGrid
		 */
	/*
	 * @RequestMapping(params = "waitingClaimTask") public ModelAndView
	 * waitingClaimTask() {
	 * 
	 * return new ModelAndView("jeecg/activiti/process/waitingClaimTask"); }
	 *//**
		 * easyui AJAX请求数据 待领任务
		 * 
		 * @param request
		 * @param response
		 * @param dataGrid
		 */
	/*
	 * @RequestMapping(params = "waitingClaimTaskDataGrid") public void
	 * waitingClaimTaskDataGrid(HttpServletRequest request, HttpServletResponse
	 * response, DataGrid dataGrid) {
	 * 
	 * String userId = "hruser"; TaskService taskService =
	 * processEngine.getTaskService(); List<Task> tasks =
	 * taskService.createTaskQuery
	 * ().taskCandidateUser(userId).active().list();//
	 * .taskCandidateGroup("hr").active().list();
	 * 
	 * StringBuffer rows = new StringBuffer(); for(Task t : tasks){
	 * rows.append("{'name':'"+t.getName()
	 * +"','taskId':'"+t.getId()+"','processDefinitionId':'"
	 * +t.getProcessDefinitionId()+"'},"); } String rowStr =
	 * StringUtils.substringBeforeLast(rows.toString(), ",");
	 * 
	 * JSONObject jObject =
	 * JSONObject.fromObject("{'total':"+tasks.size()+",'rows':["+rowStr+"]}");
	 * responseDatagrid(response, jObject); }
	 *//**
		 * easyui 待办任务页面
		 * 
		 * @param request
		 * @param response
		 * @param dataGrid
		 */
	/*
	 * @RequestMapping(params = "claimedTask") public ModelAndView claimedTask()
	 * {
	 * 
	 * return new ModelAndView("jeecg/activiti/process/claimedTask"); }
	 *//**
		 * easyui AJAX请求数据 待办任务
		 * 
		 * @param request
		 * @param response
		 * @param dataGrid
		 */
	/*
	 * @RequestMapping(params = "claimedTaskDataGrid") public void
	 * claimedTaskDataGrid(HttpServletRequest request, HttpServletResponse
	 * response, DataGrid dataGrid) {
	 * 
	 * String userId = "leaderuser"; TaskService taskService =
	 * processEngine.getTaskService(); List<Task> tasks =
	 * taskService.createTaskQuery().taskAssignee(userId).list();
	 * 
	 * StringBuffer rows = new StringBuffer(); for(Task t : tasks){
	 * rows.append("{'name':'"+t.getName()
	 * +"','description':'"+t.getDescription(
	 * )+"','taskId':'"+t.getId()+"','processDefinitionId':'"
	 * +t.getProcessDefinitionId
	 * ()+"','processInstanceId':'"+t.getProcessInstanceId()+"'},"); } String
	 * rowStr = StringUtils.substringBeforeLast(rows.toString(), ",");
	 * 
	 * JSONObject jObject =
	 * JSONObject.fromObject("{'total':"+tasks.size()+",'rows':["+rowStr+"]}");
	 * responseDatagrid(response, jObject); }
	 *//**
		 * easyui 已办任务页面
		 * 
		 * @param request
		 * @param response
		 * @param dataGrid
		 */
	/*
	 * @RequestMapping(params = "finishedTask") public ModelAndView
	 * finishedTask() {
	 * 
	 * return new ModelAndView("jeecg/activiti/process/finishedTask"); }
	 *//**
		 * easyui AJAX请求数据 已办任务
		 * 
		 * @param request
		 * @param response
		 * @param dataGrid
		 */
	/*
	 * @RequestMapping(params = "finishedTaskDataGrid") public void
	 * finishedTask(HttpServletRequest request, HttpServletResponse response,
	 * DataGrid dataGrid) {
	 * 
	 * String userId = "leaderuser"; List<HistoricTaskInstance> historicTasks =
	 * historyService .createHistoricTaskInstanceQuery().taskAssignee(userId)
	 * .finished().list();
	 * 
	 * StringBuffer rows = new StringBuffer(); for(HistoricTaskInstance t :
	 * historicTasks){ rows.append("{'name':'"+t.getName()
	 * +"','description':'"+t
	 * .getDescription()+"','taskId':'"+t.getId()+"','processDefinitionId':'"
	 * +t.getProcessDefinitionId
	 * ()+"','processInstanceId':'"+t.getProcessInstanceId()+"'},"); } String
	 * rowStr = StringUtils.substringBeforeLast(rows.toString(), ",");
	 * 
	 * JSONObject jObject =
	 * JSONObject.fromObject("{'total':"+historicTasks.size(
	 * )+",'rows':["+rowStr+"]}"); responseDatagrid(response, jObject); }
	 *//**
		 * 签收任务
		 * 
		 * @param taskId
		 */
	/*
	 * @RequestMapping(params = "claimTask")
	 * 
	 * @ResponseBody public AjaxJson claimTask(@RequestParam("taskId") String
	 * taskId, HttpServletRequest request) { AjaxJson j = new AjaxJson();
	 * 
	 * String userId = "leaderuser";
	 * 
	 * TaskService taskService = processEngine.getTaskService();
	 * taskService.claim(taskId, userId);
	 * 
	 * String message = "签收成功"; j.setMsg(message); return j; }
	 * 
	 * //
	 * ------------------------------------------------------------------------
	 * ----------- // 以下各函数可以提成共用部件 (Add by Quainty) //
	 * --------------------------
	 * --------------------------------------------------------- public void
	 * responseDatagrid(HttpServletResponse response, JSONObject jObject) {
	 * response.setContentType("application/json");
	 * response.setHeader("Cache-Control", "no-store"); try { PrintWriter
	 * pw=response.getWriter(); pw.write(jObject.toString()); pw.flush(); }
	 * catch (IOException e) { e.printStackTrace(); } }
	 */
}
