package com.sinosoft.entity;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

/**
 */
@Entity
public class UserBranch{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	private int id;
	
	@Column
	@JoinColumn(name="usercode",nullable=false)
	private String usercode;
	
	@Column
	@JoinColumn(name="branchcode",nullable=false)
	private String  branchcode;
	
	@Column
	@JoinColumn(name="deptcode",nullable=false)
	private String  deptcode;
	
    @Column
    private String remark;
    
    public UserBranch(){}
    
    public UserBranch(int id){this.id = id;}
    
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	

	public String getUsercode() {
		return usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}

	public String getBranchcode() {
		return branchcode;
	}

	public void setBranchcode(String branchcode) {
		this.branchcode = branchcode;
	}
	
	public String getDeptcode() {
		return deptcode;
	}

	public void setDeptcode(String deptcode) {
		this.deptcode = deptcode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
