package com.sinosoft.entity;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;


/**
 */
@Entity
public class RoleMenu{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	private int id;
	
	@ManyToOne
	@JoinColumn(name="Role_id",nullable=false)
	private RoleInfo roleInfo;
	
	@ManyToOne
	@JoinColumn(name="Menu_id",nullable=false)
	private MenuInfo menuInfo;
	
    @Column
    private String remark;
    
    public RoleMenu(){}
    
    public RoleMenu(int id){this.id = id;}
    
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public RoleInfo getRoleInfo() {
		return roleInfo;
	}

	public void setRoleInfo(RoleInfo roleInfo) {
		this.roleInfo = roleInfo;
	}

	public MenuInfo getMenuInfo() {
		return menuInfo;
	}

	public void setMenuInfo(MenuInfo menuInfo) {
		this.menuInfo = menuInfo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
