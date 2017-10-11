package com.sinosoft.entity;



import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;



/**
 */
@Entity
public class RoleInfo{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	private int id;
	
	@Column(nullable=false,unique=true)
	private String roleCode;
	
    @Column(nullable=false,unique=true)
    private String roleName;
    
    @Column
    private String remark;

    @OneToMany(mappedBy = "roleInfo",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
    private Set<RoleMenu> rolemenu = new HashSet<RoleMenu>();

    @OneToMany(mappedBy = "roleInfo",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
    private Set<UserRole> userRole= new HashSet<UserRole>();
    
    public RoleInfo() {}
    
    public RoleInfo(int id) {
		this.id = id;
	}
    
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Set<RoleMenu> getRolemenu() {
		return rolemenu;
	}

	public void setRolemenu(Set<RoleMenu> rolemenu) {
		this.rolemenu = rolemenu;
	}

	public Set<UserRole> getUserRole() {
		return userRole;
	}

	public void setUserRole(Set<UserRole> userRole) {
		this.userRole = userRole;
	}
}
