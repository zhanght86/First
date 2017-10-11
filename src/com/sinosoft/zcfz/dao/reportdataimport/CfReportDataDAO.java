package com.sinosoft.zcfz.dao.reportdataimport;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.sinosoft.common.Dao;
import com.sinosoft.entity.CfReportData;
import com.sinosoft.entity.CfReportDataId;

@Repository
public class CfReportDataDAO {
	@Resource(name="dao")
	private Dao dao;
	public void save(CfReportData c){
		dao.create(c);
	}
	public void update(CfReportData c){
		dao.update(c);
	}
	public void delete(CfReportDataId id){
		CfReportData u=(CfReportData) dao.get(CfReportData.class, id);
		dao.delete(u);
	}
	public CfReportData find(CfReportDataId id){
		return (CfReportData) dao.get(CfReportData.class, id);
	}
	public List<?> queryById(CfReportDataId id){
		String sql="select * from CfReportData where =reportId'"+id.getReportId()
		+"' and outitemcode='"+id.getOutItemCode()
		+"'";
		List<?> result=dao.queryWithJDBC(sql);
		return result;
	}	
	public List<?> queryBySQL(String sql){		
		List<?> result=dao.queryWithJDBC(sql);
		return result;
	}
		
}
