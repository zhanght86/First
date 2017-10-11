package com.sinosoft.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sinosoft.common.Dao;
import com.sinosoft.common.Page;
import com.sinosoft.controller.RoleInfoController;
import com.sinosoft.dao.RoleInfoDao;
import com.sinosoft.dao.UserRoleDao;
import com.sinosoft.dto.RoleInfoDTO;
import com.sinosoft.dto.UserinfoDTO;
import com.sinosoft.entity.MenuInfo;
import com.sinosoft.entity.RoleInfo;
import com.sinosoft.entity.RoleMenu;
import com.sinosoft.entity.UserInfo;
import com.sinosoft.entity.UserRole;
import com.sinosoft.service.RoleInfoService;

@Service
public class RoleInfoServiceImp implements RoleInfoService{
	@Resource
	private RoleInfoDao roleInfoDao;
	@Resource
	private UserRoleDao userRoleDao;
	@Resource
	private Dao dao;
	private Log log  = LogFactory.getLog(RoleInfoServiceImp.class);

	
	@Transactional
	public void saveRoleInfo(RoleInfoDTO dto) throws Exception  {
		log.info("1");
		RoleInfo info = new RoleInfo();
		
		if(dto.getId()!=null) {
			if (dto.getId().equals("")) {
				log.info("2");
				info.setId(0);
			} else {
				log.info("3");
				info.setId(Integer.parseInt(dto.getId()));
			}
		}
		info.setRemark(dto.getRemark());
		info.setRoleCode(dto.getRoleCode());
		info.setRoleName(dto.getRoleName());
		
	
		log.info("id-->" + dto.getId() + ",remark-->" +dto.getRemark() + "RoleCode," + dto.getRoleCode() +", rolename" + dto.getRoleName());

		String err = checkRoleInfo(info);
		log.info("--" + err+"--");
		if(err.trim().length()>0){
			throw new Exception(err);
		}
		log.info("4");
		roleInfoDao.save(info);
		//roleInfoDao.save();
	}
	@Transactional
	public boolean deleteRoleInfo(String[] idArr) {
		boolean flag = false; //0 没有超级管理 1有超管理员
		for (String id : idArr) {
			RoleInfo r=roleInfoDao.get(RoleInfo.class, Integer.parseInt(id));
			if(!r.getRoleCode().equals("admin")){
				roleInfoDao.remove(r);
			}else{
				flag = true;
				continue;
			}
		}
		return flag;
		//roleInfoDao.save();
	}
	
	public Page<?> qryRoleInfo(int page,int rows,RoleInfoDTO cdt) {
		RoleInfo roleInfo = RoleInfoDTO.toEntity(cdt);
		StringBuilder querySql =new StringBuilder("select roleinfo.id,roleinfo.roleCode,roleinfo.roleName,roleinfo.remark from roleinfo roleinfo where 1=1");
		  if(roleInfo.getRoleCode()!=null && !roleInfo.getRoleCode().equals("")){
			  querySql.append(" and roleinfo.roleCode like '%"+roleInfo.getRoleCode()+"%'");
		  }
		  if(roleInfo.getRoleName()!=null && !roleInfo.getRoleName().equals("")){
			  querySql.append(" and roleinfo.roleName like '%"+roleInfo.getRoleName()+"%'");
		  }
		  if(roleInfo.getRemark()!=null && !roleInfo.getRemark().equals("")){
			  querySql.append(" and roleinfo.remark like '%"+roleInfo.getRemark()+"%'");
		  }
		  querySql.append(" order by "+cdt.getSort()+" "+cdt.getOrder());//排序
		return roleInfoDao.queryByPage(querySql.toString(),page,rows,RoleInfoDTO.class);
		//roleInfoDao.save();
	}
	public RoleInfo findRoleInfo(int id) {
		return roleInfoDao.get(RoleInfo.class,id);
		//roleInfoDao.save();
	}
	@Transactional
	public void updateRoleInfo(int id, RoleInfoDTO dto) {
		RoleInfo c =roleInfoDao.get(RoleInfo.class,id);
		c.setRemark(dto.getRemark());
		c.setRoleCode(dto.getRoleCode());
		c.setRoleName(dto.getRoleName());
		roleInfoDao.update(c);
		
	}
	
	/**
	 * 校验角色信息
	 * @param roleinfo
	 * @return
	 */
	public String checkRoleInfo(RoleInfo roleinfo){
		StringBuffer sql= new StringBuffer();
		String err="";
		sql= new StringBuffer("select 1 from roleinfo r where r.roleCode='"+roleinfo.getRoleCode()+"'");		//校验角色代码
		if (roleInfoDao.queryBysql(sql.toString()) == null) {
			log.info("根据角色代码查找为null");
			return err;
		}
		if(roleInfoDao.queryBysql(sql.toString()).size()>0){
			err="角色代码不能重复!";
			return err;
		}
		sql= new StringBuffer("select 1 from roleinfo r where r.roleName='"+roleinfo.getRoleName()+"'");		//校验角色名称
		if (roleInfoDao.queryBysql(sql.toString()) == null) {
			log.info("根据角色名称查找为null");
			return err;
		}
		if(roleInfoDao.queryBysql(sql.toString()).size()>0){err="角色名称不能重复!";return err;}		
		else{err="";return err;}		//返回空，说明校验通过
	}
	
	public List<Map<String,Object>> findRoleInfo(String userId){
		List<Map<String,Object>> jsonD = new ArrayList<Map<String,Object>>();
		String sql="select r.id,r.rolecode,r.rolename,u.id as mid from roleinfo r Left outer join userrole u on r.id = u.role_id and u.user_id="+userId+"";
		List<?> listD = roleInfoDao.queryBysql(sql);
		for(Object obj :  listD){
			Map<String, Object> map = (Map<String, Object>)obj;
			map.put("id", map.get("id"));
			map.put("text", map.get("roleName"));
			map.put("checked", map.get("mid")!=null&&!map.get("mid").equals("")?true:false);
			jsonD.add(map);
		}
		return jsonD;
	}
	
	@Transactional
	public void saveUserToRole(String roleId , String menuId) throws Exception  {
		try{
			//删除当前用户下所有的角色
			List<?> userRoles  = (List<?>)roleInfoDao.queryBysql("select r.* from rolemenu r where r.role_id="+roleId);
			for(Object obj : userRoles){
				int roleMenuId = Integer.parseInt(((Map)obj).get("id").toString());
				roleInfoDao.remove(roleInfoDao.get(RoleMenu.class, roleMenuId));
			}
			String[] menu_ids = menuId.split(",");
			//去掉重复项
	        List<String> list = new ArrayList<String>();
	        for(String a:menu_ids){
	            if(!list.contains(a)){
	            	list.add(a);
	            }
	        }
	        //添加角色菜单信息
			for(String mid : list){
				RoleMenu rolemenu= new RoleMenu();
				rolemenu.setRoleInfo(roleInfoDao.get(RoleInfo.class, Integer.parseInt(roleId)));
				rolemenu.setMenuInfo(roleInfoDao.get(MenuInfo.class, Integer.parseInt(mid)));
				roleInfoDao.save(rolemenu);
//				dao.flush();
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("角色关联菜单出错!");
		}
	} 
	
	@Transactional
	public UserinfoDTO findUserToRole(UserinfoDTO dto) throws Exception  {
		UserinfoDTO dto2 = new UserinfoDTO();
		try{
			dto2 = UserinfoDTO.toDTO(roleInfoDao.get(UserInfo.class, dto.getId()));
		}catch(Exception e){
//			e.printStackTrace();
			throw new Exception("用户关联角色出错!");
		}
		return dto2;
	} 
	
}
