package com.sinosoft.zcfz.service.glnlpg.activiti;  

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.sinosoft.common.Page;
import com.sinosoft.entity.UserInfo;
import com.sinosoft.zcfz.dto.glnlpg.LeaveDTO;
import com.sinosoft.zcfz.entity.Leave;

public interface LeaveService {  
    public void saveLeave(LeaveDTO dto);
	public void updateLeave(long id,LeaveDTO dto);
	public void deleteLeave(String[] idArr);
	public Page<?> qryLeaveOfPage(int page,int rows,LeaveDTO dto);
	public Page<?> qryMyLeaveOfPage(int page,int rows,LeaveDTO dto,UserInfo userInfo);
	public List<?> qryLeave();
	public List<?> qryLeaveAll();
	public Leave findLeave(long id);
	public void startLeaveProcess(long id);
	public void viewLeaveProcess(String processInstanceId,HttpServletResponse response);
	public Page<?> findMyLeaveTask(int page, int rows,UserInfo userInfo);
	public Page<?> findMyGroupLeaveTask(int page, int rows,UserInfo userInfo);
	public Page<?> findMyHiLeaveTask(int page, int rows,UserInfo userInfo);
	public void completeLeaveTask(LeaveDTO leavevo,UserInfo u,boolean flag);
	public void claimLeaveTask(Leave leave,UserInfo u,String taskId);
	public List<?> findLeaveComment(String string);
	public Page<?> findMyTask(int page, int rows, UserInfo userInfo);
}