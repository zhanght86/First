package com.sinosoft.zcfz.dto.glnlpg;  


public class LeaveDTO {  
      
    /**  
     */ 
    private Long id;  
    /**  
     */ 
    private String processInstanceId;  
    /**  
     */ 
    private String reason;  
    /**  
     */ 
    private String userId;  
    
    private String status;  
    
    private String taskId;
    
    private String message; //审批意见
 
    public void setId(Long id) {  
        this.id = id;  
    }  
      
    public Long getId() {  
        return this.id;  
    }  
    public void setProcessInstanceId(String processInstanceId) {  
        this.processInstanceId = processInstanceId;  
    }  
      
    public String getProcessInstanceId() {  
        return this.processInstanceId;  
    }  
    public void setReason(String reason) {  
        this.reason = reason;  
    }  
      
    public String getReason() {  
        return this.reason;  
    }  
    public void setUserId(String userId) {  
        this.userId = userId;  
    }  
      
    public String getUserId() {  
        return this.userId;  
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}  
 
}