package com.sinosoft.controller.zcfz.glnlpg.activiti;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sinosoft.common.CurrentUser;
import com.sinosoft.common.InvokeResult;
import com.sinosoft.common.Page;
import com.sinosoft.entity.UserInfo;
import com.sinosoft.zcfz.dto.glnlpg.LeaveDTO;
import com.sinosoft.zcfz.entity.Leave;
import com.sinosoft.zcfz.service.glnlpg.activiti.ActivitiService;
import com.sinosoft.zcfz.service.glnlpg.activiti.LeaveService;
@Controller
@RequestMapping(path="/leave")
public class LeaveController {
	@Resource
	private LeaveService leaveService;
	@Resource
	private ActivitiService activitiService;
	@RequestMapping(path="/add", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult saveLeave(LeaveDTO dto){
		try {
			leaveService.saveLeave(dto);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
		
	}
	@RequestMapping(path="/listpage")
	@ResponseBody
	public Page<?> qryLeaveOfPage(@RequestParam int page,@RequestParam int rows,LeaveDTO dto){
		UserInfo userInfo=CurrentUser.getCurrentUser();
		return leaveService.qryMyLeaveOfPage(page, rows,dto,userInfo);
	}
	@RequestMapping(path="/listallpage")
	@ResponseBody
	public Page<?> qryLeaveOfAllPage(@RequestParam int page,@RequestParam int rows,LeaveDTO dto){
		return leaveService.qryLeaveOfPage(page, rows,dto);
	}
	@RequestMapping(path="/findLeave")
	@ResponseBody
	public Leave findLeave(@RequestParam long id){
		return leaveService.findLeave(id);
	}
	@RequestMapping(path="/listall")
	@ResponseBody
	public List<?> qryLeaveAll(){
		return leaveService.qryLeaveAll();
	}
	@RequestMapping(path="/list")
	@ResponseBody
	public List<?> qryLeave(){
		return leaveService.qryLeave();
	}
	@RequestMapping(path="/update")
	@ResponseBody
	public InvokeResult updateLeave(@RequestParam long id,LeaveDTO dto){
		try {
			leaveService.updateLeave(id, dto);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
	@RequestMapping(path="/delete")
	@ResponseBody
	public InvokeResult deleteLeave(@RequestParam String ids){
		try {
			String[] idArr = ids.split(",");
			leaveService.deleteLeave(idArr);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
	@RequestMapping(path="/startLeaveProcess")
	@ResponseBody
	public InvokeResult startLeaveProcess(@RequestParam long id){
		try {
			leaveService.startLeaveProcess(id);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
	@RequestMapping(path="/viewLeaveProcess")
	@ResponseBody
	public InvokeResult viewLeaveProcessImage(@RequestParam long id,HttpServletResponse response){
		try {
			Leave leave=leaveService.findLeave(id);
			leaveService.viewLeaveProcess(leave.getProcessInstanceId(),response);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
	
	@RequestMapping(path="/viewtraceImage")
	@ResponseBody
	public InvokeResult viewtraceImage(@RequestParam long id,HttpServletResponse response){
		try {
			Leave leave=leaveService.findLeave(id);
			activitiService.traceImage(leave.getProcessInstanceId(),response);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
	@RequestMapping(path="/findMyLeaveTask")
	@ResponseBody
	public Page<?> findMyLeaveTask(HttpServletRequest hsr,@RequestParam int page,@RequestParam int rows){
		try {
			UserInfo userInfo=(UserInfo)hsr.getSession().getAttribute("currentUser");
			Page<?> result=leaveService.findMyLeaveTask(page,rows,userInfo);
			return result; 
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@RequestMapping(path="/findMyTask")
	@ResponseBody
	public Page<?> findMyTask(HttpServletRequest hsr,@RequestParam int page,@RequestParam int rows){
		try {
			UserInfo userInfo=(UserInfo)hsr.getSession().getAttribute("currentUser");
			Page<?> result=leaveService.findMyTask(page,rows,userInfo);
			return result; 
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@RequestMapping(path="/findMyGroupLeaveTask")
	@ResponseBody
	public Page<?> findMyGroupTask(@RequestParam int page,@RequestParam int rows){
		try {
			UserInfo userInfo=CurrentUser.getCurrentUser();
			Page<?> result=leaveService.findMyGroupLeaveTask(page,rows,userInfo);
			return result; 
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@RequestMapping(path="/findMyHiLeaveTask")
	@ResponseBody
	public Page<?> findMyHiLeaveTask(@RequestParam int page,@RequestParam int rows){
		try {
			UserInfo userInfo=CurrentUser.getCurrentUser();
			Page<?> result=leaveService.findMyHiLeaveTask(page,rows,userInfo);
			return result; 
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@RequestMapping(path="/completeLeaveTask")
	@ResponseBody
	public InvokeResult completeLeaveTask(LeaveDTO leavevo,@RequestParam Boolean flag){
		try {
			UserInfo userInfo=CurrentUser.getCurrentUser();
			//Leave leave=leaveService.findLeave(leavevo.getId());
			leaveService.completeLeaveTask(leavevo,userInfo,flag);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
	@RequestMapping(path="/claimLeaveTask")
	@ResponseBody
	public InvokeResult claimLeaveTask(@RequestParam long id,@RequestParam String taskId){
		try {
			UserInfo userInfo=CurrentUser.getCurrentUser();
			Leave leave=leaveService.findLeave(id);
			leaveService.claimLeaveTask(leave,userInfo,taskId);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
	@RequestMapping(path="/findLeaveComment")
	@ResponseBody
	public List<?> findLeaveComment(@RequestParam long id){
		try {
			Leave leave=leaveService.findLeave(id);
			List<?> result=leaveService.findLeaveComment(leave.getProcessInstanceId());
			return result; 
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
