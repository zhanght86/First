package com.sinosoft.zcfz.dao.reportdataimport;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.sinosoft.common.Dao;
import com.sinosoft.entity.CfReportRowAddData;
import com.sinosoft.entity.CfReportRowAddDataId;

@Repository
public class CfReportRowAddDataDAO {
	@Resource(name="dao")
	private Dao dao;
	public void save(CfReportRowAddData c){
		dao.create(c);
	}
	public void update(CfReportRowAddData c){
		dao.update(c);
	}
	public void delete(CfReportRowAddDataId id){
		CfReportRowAddData u=(CfReportRowAddData) dao.get(CfReportRowAddData.class, id);
		dao.delete(u);
	}
	public CfReportRowAddData find(CfReportRowAddDataId id){
		return (CfReportRowAddData) dao.get(CfReportRowAddData.class, id);
	}
	public List<?> queryById(CfReportRowAddDataId id){
		String sql="select * from cfReportrowaddData where colcode='"+id.getColCode()
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
