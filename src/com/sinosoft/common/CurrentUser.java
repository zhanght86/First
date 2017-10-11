package com.sinosoft.common;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;

import com.sinosoft.entity.UserInfo;

/**
 * 系统常量
 *
 */
public class CurrentUser {
	public static Session session =SecurityUtils.getSubject().getSession();

    public static UserInfo CurrentUser;

	public static UserInfo getCurrentUser() {
		return CurrentUser;
	}
	public static String getUserdept() {
		return getCurrentUser().getDeptCode();
	}
	public static String getUserAccount() {
		return getCurrentUser().getUserCode();
	}
	public static void setCurrentUser(UserInfo currentUser) {
		CurrentUser = currentUser;
	}
	public static long getUserId() {
		return getCurrentUser().getId();
	}


    
}