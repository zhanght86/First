package com.sinosoft.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.sinosoft.common.BaseDAO;
import com.sinosoft.common.Page;
import com.sinosoft.dao.MenuInfoDao;
import com.sinosoft.dto.MenuInfoDTO;
import com.sinosoft.entity.MenuInfo;
import com.sinosoft.entity.RoleInfo;
import com.sinosoft.entity.UserInfo;
import com.sinosoft.entity.UserRole;
import com.sinosoft.service.MenuInfoService;
@Service
public class MenuInfoServiceImp implements MenuInfoService {
	
	@Resource
	private MenuInfoDao menuInfoDao;
	@Transactional
	public void saveMenuInfo(MenuInfoDTO mdt) {
		// TODO Auto-generated method stub
		MenuInfo m=new MenuInfo();
		Assert.isNull(menuInfoDao.get(MenuInfo.class,mdt.getId()), "主键重复，请重新选择主键");
		m.setChildCount(mdt.getChildCount());
		m.setId(mdt.getId());
		m.setMenuCode(mdt.getMenuCode());
		m.setMenuIcon(mdt.getMenuIcon());
		m.setMenuName(mdt.getMenuName());
		m.setScript(mdt.getScript());
		m.setRemark(mdt.getRemark());
		m.setSuperMenu(mdt.getSuperMenu());
		menuInfoDao.save(m);
	}
	@Transactional
	public void updateMenuInfo(int id, MenuInfoDTO mdt) {
		MenuInfo m=menuInfoDao.get(MenuInfo.class,id);
		m.setChildCount(mdt.getChildCount());
		//m.setId(mdt.getId()); id不能修改
		m.setMenuCode(mdt.getMenuCode());
		m.setMenuIcon(mdt.getMenuIcon());
		m.setMenuName(mdt.getMenuName());
		m.setScript(mdt.getScript());
		m.setRemark(mdt.getRemark());
		m.setSuperMenu(mdt.getSuperMenu());
		menuInfoDao.update(m);
	}
	@Transactional
	public void deleteMenuInfo(int id) {
		MenuInfo m=menuInfoDao.get(MenuInfo.class, id);
		menuInfoDao.remove(m);
	}
	public MenuInfo findMenuInfo(int id) {
		// TODO Auto-generated method stub
		return menuInfoDao.get(MenuInfo.class, id);
	}
	public Page<?> qryMenuInfoOfPage(int page, int rows) {
		// TODO Auto-generated method stub
		String sql="select id \"id\",childCount \"childCount\",menuCode \"menuCode\",menuName \"menuName\",remark \"remark\",script \"script\",superMenu \"_parentId\",menuIcon \"menuIcon\" from menuinfo ";
		return menuInfoDao.queryByPage(sql, page, rows);
	}
	public List<?> qryMenuInfoAll() {
		// TODO Auto-generated method stub
		String sql="select id \"id\",childCount \"childCount\",menuCode \"menuCode\",menuName \"menuName\",remark \"remark\",script \"script\",superMenu \"_parentId\",menuIcon \"menuIcon\" from menuinfo where superMenu is null ";
		List<?> list = menuInfoDao.queryBysql(sql);
		List resultList=new ArrayList();
		//遍历查询父菜单的子菜单
		for (Object obj : list) {
			Map map=(Map) obj;
			List list2=qryChildren( (BigDecimal)map.get("id")," id \"id\",childCount \"childCount\",menuCode \"menuCode\",menuName \"menuName\",remark \"remark\",script \"script\",superMenu \"_parentId\",menuIcon \"menuIcon\"" );
			if(list2!=null&&list2.size()>0&&!list2.isEmpty()){
				map.put("children",list2);
				map.put("state","closed");
			}
			resultList.add(map);
		}
		return resultList;
	}
	/**
	 * 获取全部的菜单信息
	 */
	public List<?> qryMenuInfo() {
		// TODO Auto-generated method stub
		//获取所有父菜单为空也就是最外层的父菜单
		List<?> list = menuInfoDao.queryBysql("select id \"id\",menuName \"text\" from menuinfo where superMenu is null");
		List resultList=new ArrayList();
		//遍历查询父菜单的子菜单
		for (Object obj : list) {
			Map map=(Map) obj;
			List list2=qryChildren( (BigDecimal)map.get("id")," id \"id\",menuName \"text\" " );
			if(list2!=null){
				map.put("children",list2);
			}
			resultList.add(map);
		}
		return resultList;
	}
	public Map initMenuInfo(UserInfo u) {
		List<?> roleList=menuInfoDao.queryBysql("select role_id from userrole where user_id='"+u.getId()+"'");
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<roleList.size();i++){
			Map m=(Map) roleList.get(i);
			int roleid=Integer.parseInt(m.get("role_id").toString());
			List<?> menulist=menuInfoDao.queryBysql("select menu_id from rolemenu where role_id='"+roleid+"'");
			for(int j=0;j<menulist.size();j++){
				Map m1=(Map) menulist.get(j);
				int menuid=Integer.parseInt( m1.get("menu_id").toString());
				sb.append(menuid);
				sb.append(",");
			}
		}
		String hasMenu="";
		if(sb.toString()!=null&&!"".equals(sb.toString())){
			hasMenu=sb.toString().substring(0, sb.length()-1);
		}else{
			hasMenu="''";
		}
		// TODO Auto-generated method stub
		//获取所有父菜单为空也就是最外层的父菜单
		List<?> list = menuInfoDao.queryBysql("select id \"menuid\",menuName \"menuname\",menuicon \"icon\",script \"url\" from menuinfo where superMenu is null and id in ("+hasMenu+") order by to_number(menucode)");
		List childList=new ArrayList();
		List resultList=new ArrayList();
		//遍历查询父菜单的子菜单
		for (Object obj : list) {
			Map map=(Map) obj;
			List list2=qryChildren(Integer.parseInt( map.get("menuid").toString())," id \"menuid\",menuName \"menuname\",menuicon \"icon\",script \"url\" " ,hasMenu);
			if(list2!=null){
				map.put("menus",list2);
			}
			childList.add(map);
		}
		Map map=new HashMap();
		map.put("menus",childList);
		return map;
	}
	//递归查询子菜单
		private List<MenuInfoDTO> qryChildren(int id,String sql,String hasMenu){
			List list1=new ArrayList();
			List<?> list= menuInfoDao.queryBysql("select "+sql+" from menuinfo where superMenu='"+id+"' and id in ("+hasMenu+") order by to_number(menucode)");
			if(list!=null&&list.size()>0&&!list.isEmpty()){
				for (Object obj : list) {
					Map map=(Map) obj;
					List list2=qryChildren(Integer.parseInt(map.get("menuid").toString()),sql,hasMenu);
					if(list2!=null&&list2.size()>0&&!list2.isEmpty()){
						map.put("menus",list2);
					}
					list1.add(map);
				}
			}
			return list1;
		}
	//递归查询子菜单
	private List<MenuInfoDTO> qryChildren(BigDecimal id,String sql){
		List list1=new ArrayList();
		List<?> list= menuInfoDao.queryBysql("select "+sql+" from menuinfo where superMenu='"+id+"'");
		if(list!=null&&list.size()>0&&!list.isEmpty()){
			for (Object obj : list) {
				Map map=(Map) obj;
				List list2=qryChildren( (BigDecimal) map.get("id"),sql);
				if(list2!=null&&list2.size()>0&&!list2.isEmpty()){
					map.put("children",list2);
					map.put("state","closed");
				}
				list1.add(map);
			}
		}
		return list1;
	}
	

	
	/**
	 * 获取角色对应的菜单信息
	 */
	public List<?> qryMenuInfoForCheck(String roleId) {
		// TODO Auto-generated method stub
		//获取所有父菜单为空也就是最外层的父菜单
		List<?> list = menuInfoDao.queryBysql("select m.id AS id,m.menuName as text ,r.role_id as mid from menuinfo m Left outer join rolemenu r on r.menu_id = m.id and r.role_id="+roleId+" where superMenu is null order by m.id" );
		List resultList=new ArrayList();
		//遍历查询父菜单的子菜单
		for (Object obj : list) {
			Map map=(Map) obj;
			List list2=qryChildrenForCheck( (BigDecimal)map.get("id")," m.id  ,m.menuName as text" ,roleId);
			map.put("id",map.get("id"));
			map.put("text",map.get("text"));
			if(list2!=null){
				map.put("children",list2);
			}
			resultList.add(map);
		}
		return resultList;
	}
	
	/**
	 * 获取角色对应的子菜单
	 * @param id
	 * @param sql
	 * @return
	 */
	private List<MenuInfoDTO> qryChildrenForCheck(BigDecimal id,String sql,String roleId){
		List list1=new ArrayList();
		List<?> list= menuInfoDao.queryBysql("select "+sql+" ,r.role_id as mid from menuinfo m Left outer join rolemenu r on r.menu_id = m.id and r.role_id="+roleId+" where superMenu='"+id+"'");
		if(list!=null&&list.size()>0&&!list.isEmpty()){
			for (Object obj : list) {
				Map map=(Map) obj;
				List list2=qryChildrenForCheck( (BigDecimal) map.get("id"),sql,roleId);
				if(list2!=null&&list2.size()>0&&!list2.isEmpty()){
					map.put("id",map.get("id"));
					map.put("text",map.get("text"));
					map.put("children",list2);
					map.put("state","closed");
					map.put("checked", map.get("mid")!=null&&!map.get("mid").equals("")?true:false);
				}
				map.put("id",map.get("id"));
				map.put("text",map.get("text"));
				map.put("checked", map.get("mid")!=null&&!map.get("mid").equals("")?true:false);
				list1.add(map);
			}
		}
		return list1;
	}
	
	@Override
	public List<?> initMenuInfo2(UserInfo u) {
		// TODO Auto-generated method stub
		List<?> roleList=menuInfoDao.queryBysql("select role_id from userrole where user_id='"+u.getId()+"'");
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<roleList.size();i++){
			Map m=(Map) roleList.get(i);
			int roleid=Integer.parseInt(m.get("role_id").toString());
			List<?> menulist=menuInfoDao.queryBysql("select menu_id from rolemenu where role_id='"+roleid+"'");
			for(int j=0;j<menulist.size();j++){
				Map m1=(Map) menulist.get(j);
				Long menuid=Long.parseLong( m1.get("menu_id").toString());
				sb.append(menuid);
				sb.append(",");
			}
		}
		String hasMenu="";
		if(sb.toString()!=null&&!"".equals(sb.toString())){
			hasMenu=sb.toString().substring(0, sb.length()-1);
		}else{
			hasMenu="''";
		}
		
		List resultList=new ArrayList();
		for(int i=0;i<roleList.size();i++){
			Map m=(Map) roleList.get(i);
			int roleid=Integer.parseInt(m.get("role_id").toString());
			//获取所有父菜单为空也就是最外层的父菜单!!!下面的list为空（2017/8/2）
			List<?> list = menuInfoDao.queryBysql("select m.id AS ,m.menuName as text  from menuinfo m  where superMenu is null and id in (" + hasMenu +")  order by m.id" );//and length(m.id) > 10
				
			//遍历查询父菜单的子菜单
			for (Object obj : list) {
				Map map=(Map) obj;
				List list2=qryChildrenForCheck2( (BigDecimal)map.get("id")," m.id  ,m.menuName as text" , hasMenu);
				map.put("id",map.get("id"));
				map.put("text",map.get("text"));
				if(list2!=null){
					map.put("children",list2);
				}
				if(!resultList.contains(map)){
					resultList.add(map);
				}

			}
		}
				return resultList;
	}
	/**
	 * 获取角色对应的子菜单
	 * @param id
	 * @param sql
	 * @return
	 */
	private List<MenuInfoDTO> qryChildrenForCheck2(BigDecimal id,String sql,String hasMenu){
		List list1=new ArrayList();
		Map<String, Object> attributes = null;
		List<?> list= menuInfoDao.queryBysql("select "+sql+" ,m.script as url from menuinfo m  where m.id in (" + hasMenu + ") and  superMenu='"+id+"' order by m.id");//length(m.id) > 10 and
		if(list!=null&&list.size()>0&&!list.isEmpty()){
			for (Object obj : list) {
				attributes = new HashMap<String, Object>();
				Map map=(Map) obj;
				List list2=qryChildrenForCheck2( (BigDecimal) map.get("id"),sql, hasMenu);
				if(list2!=null&&list2.size()>0&&!list2.isEmpty()){
					map.put("id",map.get("id"));
					map.put("text",map.get("text"));
					map.put("children",list2);
					map.put("state","closed");
				}
				
				attributes.put("url", map.get("url"));
				map.put("id",map.get("id"));
				map.put("text",map.get("text"));
				map.put("attributes", attributes);
				list1.add(map);
			}
		}
		return list1;
	}
	
}
