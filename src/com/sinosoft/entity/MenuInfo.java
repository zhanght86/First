package com.sinosoft.entity;


import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.GenericGenerator;


/**
 */
@Entity
public class MenuInfo{
	@Id
	@GenericGenerator(name="menuinfo",strategy="auto")
	private int id;
	
	@Column(nullable=false,unique=true)
	private String menuCode;
	
    @Column
    private String menuName;
    
    @Column
    private Integer childCount;
    
    @Column
    private String menuIcon;
    
    @Column
    private String script;
    
    @Column
    private String remark;
    
    @Column(name="superMenu")
    private String superMenu;
    
   /* @OneToMany(mappedBy = "parent",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
    private Set<MenuInfo> children = new HashSet<MenuInfo>();*/
    
    @OneToMany(mappedBy = "menuInfo",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
    private Set<RoleMenu> rolemenu = new HashSet<RoleMenu>();

    public MenuInfo() {}
    
    public MenuInfo(int id) {
    	this.id = id;
    }
    
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMenuCode() {
		return menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public Integer getChildCount() {
		return childCount;
	}

	public void setChildCount(Integer childCount) {
		this.childCount = childCount;
	}

	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSuperMenu() {
		return superMenu;
	}

	public void setSuperMenu(String superMenu) {
		this.superMenu = superMenu;
	}
/*
	public Set<MenuInfo> getChildren() {
		return children;
	}

	public void setChildren(Set<MenuInfo> children) {
		this.children = children;
	}*/

	public String getMenuIcon() {
		return menuIcon;
	}

	public void setMenuIcon(String menuIcon) {
		this.menuIcon = menuIcon;
	}

}
