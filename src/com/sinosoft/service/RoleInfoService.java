package com.sinosoft.service;



import java.util.List;
import java.util.Map;

import com.sinosoft.common.Page;
import com.sinosoft.dto.RoleInfoDTO;
import com.sinosoft.dto.UserRoleDTO;
import com.sinosoft.dto.UserinfoDTO;
import com.sinosoft.entity.RoleInfo;

public interface RoleInfoService {
	public void saveRoleInfo(RoleInfoDTO rid) throws Exception;
	public void updateRoleInfo(int id,RoleInfoDTO rid);
	public boolean deleteRoleInfo(String[] idArr);
	public Page<?> qryRoleInfo(int page,int rows,RoleInfoDTO rid);
	public RoleInfo findRoleInfo(int id);
	public List<Map<String,Object>> findRoleInfo(String userId);
	public void saveUserToRole(String roleId , String menuId) throws Exception;
	public UserinfoDTO findUserToRole(UserinfoDTO dto) throws Exception;
}
