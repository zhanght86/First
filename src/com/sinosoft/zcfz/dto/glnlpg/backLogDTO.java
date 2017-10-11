package com.sinosoft.zcfz.dto.glnlpg;

public class backLogDTO {
	
	private String menuLink;//页面链接
	private String backLogName;//待办事项名称
	private int type;//代办类型   1、损失事件 2、损失事件整改 3、自评估
	private String menuName;//页面名称
	public String getMenuLink() {
		return menuLink;
	}
	public void setMenuLink(String menuLink) {
		this.menuLink = menuLink;
	}
	public String getBackLogName() {
		return backLogName;
	}
	public void setBackLogName(String backLogName) {
		this.backLogName = backLogName;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public backLogDTO(String menuLink, String backLogName,int type,String menuName) {
		super();
		this.menuLink = menuLink;
		this.backLogName = backLogName;
		this.type = type;
		this.menuName = menuName;
	}
	
	public backLogDTO() {
		
	}
	
}
