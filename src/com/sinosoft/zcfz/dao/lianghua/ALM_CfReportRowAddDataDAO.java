package com.sinosoft.zcfz.dao.lianghua;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.sinosoft.common.Dao;
import com.sinosoft.entity.CfReportRowAddData;
import com.sinosoft.entity.CfReportRowAddDataId;
import com.sinosoft.zcfz.entity.lianghua.ALM_CfReportRowAddData;
import com.sinosoft.zcfz.entity.lianghua.ALM_CfReportRowAddDataId;

@Repository
public class ALM_CfReportRowAddDataDAO {
	@Resource(name="dao")
	private Dao dao;
	public void save(ALM_CfReportRowAddData c){
		dao.create(c);
	}
	public void update(ALM_CfReportRowAddData c){
		dao.update(c);
	}
	public void delete(ALM_CfReportRowAddDataId id){
		ALM_CfReportRowAddData u=(ALM_CfReportRowAddData) dao.get(ALM_CfReportRowAddData.class, id);
		dao.delete(u);
	}
	public ALM_CfReportRowAddData find(ALM_CfReportRowAddDataId id){
		return (ALM_CfReportRowAddData) dao.get(ALM_CfReportRowAddData.class, id);
	}
	public List<?> queryById(ALM_CfReportRowAddDataId id){
		String sql="select * from ALM_cfReportrowaddData where colcode='"+id.getColCode()
		+"' and reportId='"+id.getReportId()
		+"' and rowno='"+id.getRowNo()
		+"' and colcode='"+id.getColCode()
		+"'";
		List<?> result=dao.queryWithJDBC(sql);
		return result;
	}
	public List<?> queryBySQL(String sql) {
		// TODO Auto-generated method stub
		List<?> result=dao.queryWithJDBC(sql);
		return result;
	}
}
