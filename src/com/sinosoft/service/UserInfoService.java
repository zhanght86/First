package com.sinosoft.service;



import java.util.List;

import com.sinosoft.common.Page;
import com.sinosoft.dto.UserinfoDTO;
import com.sinosoft.entity.UserInfo;

public interface UserInfoService {
	public void saveUserInfo(UserinfoDTO u);
	public void updateUserInfo(long id,UserinfoDTO u);
	public boolean deleteUserInfo(long id);
	public Page<?> qryUserInfo(int page,int rows, UserinfoDTO dto);
	public UserInfo findUserInfo(long id);
	public List findUserInfo(UserinfoDTO dto);
	public void resetPassword(String newpass,UserInfo u);
	public void saveUserToRole(String roleIDs,String userIds) throws Exception  ;
}
