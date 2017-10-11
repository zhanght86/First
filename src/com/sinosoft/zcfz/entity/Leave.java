package com.sinosoft.zcfz.entity;  

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="act_leave")
public class Leave {  
      
    /**  
     */ 
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
    @Column
    private Long id;  
    /**  
     */ 
    @Column
    private String processInstanceId;  
    /**  
     */ 
    @Column
    private String reason;  
    /**  
     */ 
    @Column
    private String userId; 
    
    @Column
    private String status;  
 
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
 
}