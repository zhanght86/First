package com.sinosoft.service;

import java.util.List;
import java.util.Map;

import com.sinosoft.common.Page;
import com.sinosoft.dto.MenuInfoDTO;
import com.sinosoft.entity.MenuInfo;
import com.sinosoft.entity.UserInfo;

public interface MenuInfoService {
	public void saveMenuInfo(MenuInfoDTO mdt);
	public void updateMenuInfo(int id,MenuInfoDTO cdt);
	public void deleteMenuInfo(int id);
	public Page<?> qryMenuInfoOfPage(int page,int rows);
	public List<?> qryMenuInfo();
	public List<?> qryMenuInfoAll();
	public MenuInfo findMenuInfo(int id);
	public Map<?, ?> initMenuInfo(UserInfo u);
	public List<?> qryMenuInfoForCheck(String roleId);
	/**
	 * 实现三级菜单新加方法
	 */
	public List<?> initMenuInfo2(UserInfo u);
}
