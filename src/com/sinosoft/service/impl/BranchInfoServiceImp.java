package com.sinosoft.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sinosoft.common.Constant;
import com.sinosoft.common.Page;
import com.sinosoft.dao.BranchInfoDao;
import com.sinosoft.dto.BranchInfoDTO;
import com.sinosoft.entity.BranchInfo;
import com.sinosoft.service.BranchInfoService;
@Service
public class BranchInfoServiceImp implements BranchInfoService{
	@Resource
	private BranchInfoDao branchInfoDao;
	@Transactional
	public void saveBranchInfo(BranchInfoDTO brt) {
		// TODO Auto-generated method stub
		BranchInfo br=new BranchInfo();
		br.setComCode(brt.getComCode());
		br.setComName(brt.getComName());
		//add wuchao comRank许可证号的id
		br.setLicensenumber(brt.getLicensenumber());
		//end
		br.setEndFlag(brt.getEndFlag());
		br.setUseFlag(brt.getUseFlag());
		br.setComRank(brt.getComRank());
		//添加公司类型
		br.setComType(Constant.COMPANYTYPE);
		br.setUperComCode(branchInfoDao.get(BranchInfo.class, brt.getUperComCode()));
		branchInfoDao.save(br);
	}
	@Transactional
	public void updateBranchInfo(String id, BranchInfoDTO brt) {
		// TODO Auto-generated method stub
		BranchInfo br=new BranchInfo();
		br=branchInfoDao.get(BranchInfo.class, id);
	//	br.setComCode(id+"");
		br.setComName(brt.getComName());
		//add wuchao comRank许可证号的id
		br.setLicensenumber(brt.getLicensenumber());
		//end
		br.setEndFlag(brt.getEndFlag());
		br.setUseFlag(brt.getUseFlag());
		br.setComRank(brt.getComRank());
		br.setUperComCode(branchInfoDao.get(BranchInfo.class, brt.getUperComCode()));
		branchInfoDao.update(br);
	}
	@Transactional
	public void deleteBranchInfo(String id) {
		// TODO Auto-generated method stub
		BranchInfo br=branchInfoDao.get(BranchInfo.class, id);
		branchInfoDao.remove(br);
	}
	public Page<?> qryBranchInfoOfPage(int page, int rows) {
		// TODO Auto-generated method stub
		return null;
	}
	public BranchInfo qryBranchInfoByCode(String organizeCode) {
		@SuppressWarnings("unchecked")
		List<BranchInfo> list=(List<BranchInfo>) branchInfoDao.queryByhsql("from BranchInfo where comCode='"+organizeCode+"'");
		if(list!=null&&list.size()>0){
			BranchInfo b=list.get(0);
			return b;
		}
		return null;
	}
	public List<?> qryBranchInfo() {
		List<?> list = branchInfoDao.queryBysql("select t.*,(select codename from cfcodemanage where codetype='comtype' and codecode=t.comrank) as comrankname from branchinfo t where t.upercomcode is null",BranchInfoDTO.class);
		List resultList=new ArrayList();
		//遍历查询父菜单的子菜单
		for (Object obj : list) {
			Map map=new HashMap();
			BranchInfoDTO bd=(BranchInfoDTO) obj;
			map.put("id", bd.getComCode());
			map.put("text", bd.getComName());
			map.put("comCode", bd.getComCode());
			map.put("comName", bd.getComName());
			map.put("comRank", bd.getComRank());
			map.put("comRankName", bd.getComRankName());
			map.put("licensenumber", bd.getLicensenumber());
			map.put("endFlag", bd.getEndFlag());
			map.put("useFlag", bd.getUseFlag());
			map.put("comType", bd.getComType());
			map.put("_parentId", bd.getUperComCode());
			List list2=qryChildren(bd.getComCode(),"0");
			if(list2!=null&&list2.size()>0&&!list2.isEmpty()){
				map.put("children",list2);
			}
			resultList.add(map);
		}
		return resultList;
	}
	public List<?> qryBranchInfoo() {
		List<?> list = branchInfoDao.queryBysql("select t.*,(select codename from cfcodemanage where codetype='comtype' and codecode=t.comrank) as comrankname from branchinfo t where t.upercomcode is null",BranchInfoDTO.class);
		List resultList=new ArrayList();
		//遍历查询父菜单的子菜单
		for (Object obj : list) {
			Map map=new HashMap();
			BranchInfoDTO bd=(BranchInfoDTO) obj;
				map.put("id", bd.getComCode());
				map.put("text", bd.getComName());
				map.put("comCode", bd.getComCode());
				map.put("comName", bd.getComName());
				map.put("comRank", bd.getComRank());
				map.put("comRankName", bd.getComRankName());
				map.put("licensenumber", bd.getLicensenumber());
				map.put("endFlag", bd.getEndFlag());
				map.put("useFlag", bd.getUseFlag());
				map.put("comType", bd.getComType());
				map.put("_parentId", bd.getUperComCode());
				List list2=qryChildren1(bd.getComCode(),"0");
				if(list2!=null&&list2.size()>0&&!list2.isEmpty()){
					map.put("children",list2);
				}
				resultList.add(map);			
		}
		return resultList;
	}
	public List<?> qryBranchInfoAll() {
		return null;
	}
	public BranchInfo findBranchInfo(String id) {
		BranchInfo b=branchInfoDao.get(BranchInfo.class, id);
		return b;
	}
	/**
	 * 递归查询机构树
	 * @param id
	 * @param organType 0 全部 1 公司 2 部门
	 * @return
	 */
	private List<?> qryChildren(String id,String organType){ 
		List list1=new ArrayList();
		String sql="select t.*,(select codename from cfcodemanage where codetype='comtype' and codecode=t.comrank) as comrankname from branchinfo t where t.UperComCode='"+id+"'";
		if("0".equals(organType)){
			sql="select t.*,(select codename from cfcodemanage where codetype='comtype' and codecode=t.comrank) as comrankname from branchinfo t where t.UperComCode='"+id+"'";
		}else if("1".equals(organType)){
			sql="select t.*,(select codename from cfcodemanage where codetype='comtype' and codecode=t.comrank) as comrankname from branchinfo t where t.UperComCode='"+id+"' and t.comRank in ('0','1','2','3')";
		}else if("2".equals(organType)){
			sql="select t.*,(select codename from cfcodemanage where codetype='comtype' and codecode=t.comrank) as comrankname from branchinfo t where t.UperComCode='"+id+"' and t.comRank not in ('0','1','2','3')";
		}
		List<?> list= branchInfoDao.queryBysql(sql,BranchInfoDTO.class);
		if(list!=null&&list.size()>0&&!list.isEmpty()){
			for (Object obj : list) {
				Map map=new HashMap();
				BranchInfoDTO bd=(BranchInfoDTO) obj;
				map.put("id", bd.getComCode());
				map.put("text", bd.getComName());
				map.put("comCode", bd.getComCode());
				map.put("comName", bd.getComName());
				map.put("comRank", bd.getComRank());
				map.put("comRankName", bd.getComRankName());
				map.put("licensenumber", bd.getLicensenumber());
				map.put("endFlag", bd.getEndFlag());
				map.put("useFlag", bd.getUseFlag());
				map.put("comType", bd.getComType());
				map.put("_parentId", bd.getUperComCode());
				List list2=qryChildren(bd.getComCode(),organType);
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
	 * 递归查询机构树
	 * @param id
	 * @param organType 0 全部 1 公司 2 部门
	 * @return
	 */
	private List<?> qryChildren1(String id,String organType){ 
		List list1=new ArrayList();
		String sql="select t.*,(select codename from cfcodemanage where codetype='comtype' and codecode=t.comrank) as comrankname from branchinfo t where t.UperComCode='"+id+"'";
		if("0".equals(organType)){
			sql="select t.*,(select codename from cfcodemanage where codetype='comtype' and codecode=t.comrank) as comrankname from branchinfo t where  t.endflag='0' and  t.UperComCode='"+id+"'";
		}else if("1".equals(organType)){
			sql="select t.*,(select codename from cfcodemanage where codetype='comtype' and codecode=t.comrank) as comrankname from branchinfo t where t.UperComCode='"+id+"' and t.comRank in ('0','1','2','3')";
		}else if("2".equals(organType)){
			sql="select t.*,(select codename from cfcodemanage where codetype='comtype' and codecode=t.comrank) as comrankname from branchinfo t where t.UperComCode='"+id+"' and t.comRank not in ('0','1','2','3')";
		}
		List<?> list= branchInfoDao.queryBysql(sql,BranchInfoDTO.class);
		if(list!=null&&list.size()>0&&!list.isEmpty()){
			for (Object obj : list) {
				Map map=new HashMap();
				BranchInfoDTO bd=(BranchInfoDTO) obj;
				map.put("id", bd.getComCode());
				map.put("text", bd.getComName());
				map.put("comCode", bd.getComCode());
				map.put("comName", bd.getComName());
				map.put("comRank", bd.getComRank());
				map.put("comRankName", bd.getComRankName());
				map.put("licensenumber", bd.getLicensenumber());
				map.put("endFlag", bd.getEndFlag());
				map.put("useFlag", bd.getUseFlag());
				map.put("comType", bd.getComType());
				map.put("_parentId", bd.getUperComCode());
				List list2=qryChildren(bd.getComCode(),organType);
				if(list2!=null&&list2.size()>0&&!list2.isEmpty()){
					map.put("children",list2);
					map.put("state","closed");
				}
				list1.add(map);
			}
		}
		return list1;
	}
	
	
	
	
	public List<String> qryBranchInfoAndChild(String organize) {
		List<String> list=new ArrayList<String>();
		list.add(organize);
		List<String> list1=qryChildren(list,organize);
		return list1;
	}
	//递归查询机构树
	private List<String> qryChildren(List<String> list,String id){
		List<?> list1= branchInfoDao.queryBysql("select comCode from branchinfo where UperComCode='"+id+"' and comrank in ('0','1','2')");
		if(list1!=null&&list1.size()>0&&!list1.isEmpty()){
			for (Object obj : list1) {
				Map map=(Map) obj;
				list.add((String) map.get("comCode"));
				List<String> list2=qryChildren(list,(String) map.get("comCode"));
				if(list2!=null&&list2.size()>0&&!list2.isEmpty()){
					/*for(String o2:list2){
						//Map map2=(Map)o2;
						list.add(o2);
					}*/
					return list2;
				}
			}
		}
		return list;
	}
	@Override
	public List<?> listByUperCompany(String company) {
		List list=new ArrayList();
		BranchInfo b=findBranchInfo(company);
		Map<String,String> map=new HashMap<String,String>();
		map.put("id", b.getComCode());
		map.put("text",b.getComName());
		list.add(map);
		List<?> list1=qryChildren(company,"0");
		for(Object bd:list1){
			list.add((Map)bd);
		}
		return list;
	}
	public List<?> listCompanyByUperCompany(String company) {
		List list=new ArrayList();
		BranchInfo b=findBranchInfo(company);
		Map<String,String> map=new HashMap<String,String>();
		map.put("id", b.getComCode());
		map.put("text",b.getComName());
		list.add(map);
		List<?> list1=qryChildren(company,"1");
		for(Object bd:list1){
			list.add((Map)bd);
		}
		return list;
	}
}
