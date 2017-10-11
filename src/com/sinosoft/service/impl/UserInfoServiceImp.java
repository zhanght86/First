package com.sinosoft.service.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import antlr.StringUtils;

import com.sinosoft.common.Page;
import com.sinosoft.dao.BaseAbstractDao;
import com.sinosoft.dao.UserInfoDao;
import com.sinosoft.dto.UserinfoDTO;
import com.sinosoft.entity.RoleInfo;
import com.sinosoft.entity.UserBranch;
import com.sinosoft.entity.UserInfo;
import com.sinosoft.entity.UserRole;
import com.sinosoft.service.UserInfoService;
import com.sinosoft.util.MD5Encrypt;

@Service
public class UserInfoServiceImp implements UserInfoService{
	@Resource
	private UserInfoDao userInfoDao;

	@Transactional
	public void saveUserInfo(UserinfoDTO ud) {
		UserInfo u=new UserInfo(ud.getUserCode(),ud.getPassword());
		u.setUserName(ud.getUserName());
		u.setBankCode(ud.getBankCode());
		u.setBankInfo(ud.getBankInfo());
		u.setComCode(ud.getComCode());
		u.setDeptCode(ud.getDeptCode());
		u.setEmail(ud.getEmail());
		u.setPhone(ud.getPhone());
		u.setUseFlag(ud.getUseFlag());
		userInfoDao.save(u);
		String userCode = ud.getUserCode();
		String branchs = ud.getBranchs();
		String depts = ud.getDepts();
        try{
			//删除当前用户下所有的机构
			List<?> userBranchs  = (List<?>)userInfoDao.queryBysql("select u.* from userbranch u where u.usercode='"+userCode+"'");
			for(Object obj : userBranchs){
				int userBranchId = Integer.parseInt(((Map)obj).get("id").toString());
				userInfoDao.remove(new UserBranch(userBranchId));
			}
			//添加
					UserBranch ub = new UserBranch();
					ub.setUsercode(userCode);
					ub.setDeptcode(depts);
					ub.setBranchcode(branchs);
					userInfoDao.save(ub);
		}catch(Exception e){
			e.printStackTrace();
			try {
				throw new Exception("用户关联机构或部门出错!");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
        
	}

	@Transactional
	public void updateUserInfo(long id, UserinfoDTO ud) {
		UserInfo u=userInfoDao.get(UserInfo.class, id);
		u.setUserCode(ud.getUserCode());
		u.setUserName(ud.getUserName());
		u.setBankCode(ud.getBankCode());
		u.setBankInfo(ud.getBankInfo());
		u.setComCode(ud.getComCode());
		u.setDeptCode(ud.getDeptCode());
		u.setEmail(ud.getEmail());
		u.setPhone(ud.getPhone());
		u.setUseFlag(ud.getUseFlag());
		userInfoDao.update(u);
		String userCode = ud.getUserCode();
		String branchs = ud.getBranchs();
		String depts = ud.getDepts();
        try{
			//删除当前用户下所有的机构
			List<?> userBranchs  = (List<?>)userInfoDao.queryBysql("select u.* from userbranch u where u.usercode='"+userCode+"'");
			for(Object obj : userBranchs){
				int userBranchId = Integer.parseInt(((Map)obj).get("id").toString());
				userInfoDao.remove(new UserBranch(userBranchId));
			}
			//添加
					UserBranch ub = new UserBranch();
					ub.setBranchcode(branchs);
					ub.setDeptcode(depts);
					ub.setUsercode(userCode);
					userInfoDao.update(ub);
					
		}catch(Exception e){
			e.printStackTrace();
			try {
				throw new Exception("用户关联机构或部门出错!");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	@Transactional
	public boolean deleteUserInfo(long id) {
		try {			
			UserInfo u=userInfoDao.get(UserInfo.class, id);
			List<?> userBranchs  = (List<?>)userInfoDao.queryBysql("select u.* from userbranch u where u.usercode="+id);
			for(Object obj : userBranchs){
				int userBranchId = Integer.parseInt(((Map)obj).get("id").toString());
				userInfoDao.remove(new UserBranch(userBranchId));
			}
			userInfoDao.remove(u);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}

	public Page<?> qryUserInfo(int page, int rows,UserinfoDTO dto) {
		StringBuilder querySql =new StringBuilder("select u.id,u.userCode,u.userName,u.bankCode,u.bankInfo,u.comCode,u.deptcode,(b.comname) comCodeName,(bi.comname) deptCodeName,u.email,u.idCardNo,u.phone,(ub.branchcode)branchs,(ub.deptcode)depts,u.useflag,(c.codename) useFlagName from userinfo u,branchinfo b,branchinfo bi,cfcodemanage c,userbranch ub "
							   + " where 1=1 and u.comcode = b.comcode  and u.deptcode = bi.comcode  and u.useflag = c.codecode and u.usercode = ub.usercode  and c.codetype = 'UseFlag'");
		if(dto.getUserCodeS()!=null && !dto.getUserCodeS().equals("")){
			  querySql.append(" and u.userCode like '%"+dto.getUserCodeS()+"%'");
		}
		if(dto.getUserName()!=null && !dto.getUserName().equals("")){
			  querySql.append(" and u.userName like '%"+dto.getUserName()+"%'");
		}
		if(dto.getBankCode()!=null && !dto.getBankCode().equals("")){
			  querySql.append(" and u.bankCode like '%"+dto.getBankCode()+"%'");
		}
		if(dto.getBankInfo()!=null && !dto.getBankInfo().equals("")){
			  querySql.append(" and u.bankInfo like '%"+dto.getBankInfo()+"%'");
		}
		if(dto.getComName()!=null && !dto.getComName().equals("")){
			  querySql.append(" and u.comCode  like '%"+dto.getComName()+"%'");
		}
		if(dto.getDeptName()!=null && !dto.getDeptName().equals("")){
			querySql.append(" and u.deptCode  like '%"+dto.getDeptName()+"%'");
			}
		
		if(dto.getEmail()!=null && !dto.getEmail().equals("")){
			  querySql.append(" and u.email like '%"+dto.getEmail()+"%'");
		}
		if(dto.getIdCardNo()!=null && !dto.getIdCardNo().equals("")){
			  querySql.append(" and u.idCardNo like '%"+dto.getIdCardNo()+"%'");
		}
		if(dto.getPhone()!=null && !dto.getPhone().equals("")){
			  querySql.append(" and u.phone like '%"+dto.getPhone()+"%'");
		}
		querySql.append(" order by "+dto.getSort()+" "+dto.getOrder());//排序
		Page<?> result = userInfoDao.queryByPage(querySql.toString(), page, rows,UserinfoDTO.class);
		return result;
	}

	public UserInfo findUserInfo(long id) {
		// TODO Auto-generated method stub
		return userInfoDao.get(UserInfo.class, id);
	}
	@Transactional
	public void resetPassword(String newpass,UserInfo u) {
		// TODO Auto-generated method stub
		u.setPassword(MD5Encrypt.encryptPassword(newpass));
		userInfoDao.update(u);
	}
	
	@Transactional
	public void saveUserToRole(String roleIDs,String userIds) throws Exception  {
		try{
			//删除当前用户下所有的角色
			List<?> userRoles  = (List<?>)userInfoDao.queryBysql("select u.* from userrole u where u.user_id="+userIds);
			for(Object obj : userRoles){
				int userRoleId = Integer.parseInt(((Map)obj).get("id").toString());
				userInfoDao.remove(new UserRole(userRoleId));
			}
			//添加
			if(!roleIDs.equals(""))
				for(String roleId : roleIDs.split(",")){
					UserRole userRole = new UserRole();
					userRole.setRoleInfo(new RoleInfo(Integer.parseInt(roleId)));
					userRole.setUserInfo(new UserInfo(Integer.parseInt(userIds)));
					userInfoDao.save(userRole);
				}
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("用户关联角色出错!");
		}
	}

	@Override
	public List<?> findUserInfo(UserinfoDTO dto) {
		StringBuilder querySql =new StringBuilder("select * from userinfo u where 1=1");
		if(dto.getUserCode()!=null && !dto.getUserCode().equals("")){
			  querySql.append(" and u.userCode like '%"+dto.getUserCode()+"%'");
		}
		if(dto.getUserName()!=null && !dto.getUserName().equals("")){
			  querySql.append(" and u.userName like '%"+dto.getUserName()+"%'");
		}
		if(dto.getBankCode()!=null && !dto.getBankCode().equals("")){
			  querySql.append(" and u.bankCode like '%"+dto.getBankCode()+"%'");
		}
		if(dto.getBankInfo()!=null && !dto.getBankInfo().equals("")){
			  querySql.append(" and u.bankInfo like '%"+dto.getBankInfo()+"%'");
		}
		if(dto.getComCode()!=null && !dto.getComCode().equals("")){
			  querySql.append(" and u.comCode in ('select b.comcode from branchinfo b where b.comname like '%"+dto.getComCode()+"%' and b.comtype='公司'')'");
		}
		if(dto.getDeptCode()!=null && !dto.getDeptCode().equals("")){
			  querySql.append(" and u.deptCode in ('select b.comcode from branchinfo b where b.comname like '%"+dto.getDeptCode()+"%' and b.comtype='部门'')'");
		}
		if(dto.getEmail()!=null && !dto.getEmail().equals("")){
			  querySql.append(" and u.email like '%"+dto.getEmail()+"%'");
		}
		if(dto.getIdCardNo()!=null && !dto.getIdCardNo().equals("")){
			  querySql.append(" and u.idCardNo like '%"+dto.getIdCardNo()+"%'");
		}
		if(dto.getPhone()!=null && !dto.getPhone().equals("")){
			  querySql.append(" and u.phone like '%"+dto.getPhone()+"%'");
		}
		return userInfoDao.queryBysql(querySql.toString());
	} 
	
}
