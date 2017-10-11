package com.sinosoft.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.sinosoft.common.Dao;


@Repository
public class CfCodemanageDao extends BaseAbstractDao{
	@Resource(name="dao")
	private Dao dao;
	public List<?> query(String sql){
		System.out.println(sql);
		List<?> list=dao.queryWithJDBC(sql);
		int length=list.size();
		List<Map<?,?>> result=new ArrayList<Map<?,?>>();
		for(int i=0;i<length;i++){
			Map<String,String> map= new HashMap<String,String>();
			@SuppressWarnings("unchecked")
			Map<String,String> mapcode=(Map<String,String>) list.get(i);
			map.put("value", mapcode.get("value"));
			map.put("text", mapcode.get("text"));
			String group=mapcode.get("mapcode");
			if(group!=null&&!"".equals(group)){
				map.put("group", mapcode.get("group"));
			}
			result.add(map);
		}
		return result;
	}
}
