package com.sinosoft.zcfz.service.reportitemsystem.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dao.reportitemsystem.CfReportRowAddDescDao;
import com.sinosoft.zcfz.dto.reportitemsystem.CfReportRowAddDescDTO;
import com.sinosoft.entity.CfReportRowAddDesc;
import com.sinosoft.entity.CfReportRowAddDescId;
import com.sinosoft.zcfz.service.reportitemsystem.CfReportRowAddDescService;
@Service
public class CfReportRowAddDescServiceImp implements CfReportRowAddDescService{
	@Resource
	private CfReportRowAddDescDao cfReportRowAddDescDao;
	@Transactional
	@Override
	public void saveCfReportRowAdd(CfReportRowAddDescDTO rid) {
		// TODO Auto-generated method stub
		CfReportRowAddDesc re=rid.toCfReportRowAddDesc(null);
		cfReportRowAddDescDao.save(re);
	}
	@Transactional
	@Override
	public void updateCfReportRowAdd(CfReportRowAddDescId cfrid, CfReportRowAddDescDTO rid) {
		// TODO Auto-generated method stub
		CfReportRowAddDesc re=cfReportRowAddDescDao.get(CfReportRowAddDesc.class, cfrid);
		rid.toCfReportRowAddDesc(re);
		cfReportRowAddDescDao.update(re);
	}
	@Transactional
	@Override
	public void deleteCfReportRowAdd(CfReportRowAddDescId cfrid) {
		// TODO Auto-generated method stub
		if(cfrid!=null){
			String[] colCodes=cfrid.getColCode().split("\\|");
			String[] reportTypes=cfrid.getReportType().split("\\|");
			String[] tableCodes=cfrid.getTableCode().split("\\|");
			CfReportRowAddDescId[] cfrids=new CfReportRowAddDescId[tableCodes.length];
			for (int i = 0; i < tableCodes.length; i++) {
				CfReportRowAddDescId cfrowid=new CfReportRowAddDescId();
				cfrowid.setColCode(colCodes[i]);
				cfrowid.setReportType(reportTypes[i]);
				cfrowid.setTableCode(tableCodes[i]);
				cfrids[i]=cfrowid;
			}
			if(cfrids.length>1){
				cfReportRowAddDescDao.removes(cfrids,CfReportRowAddDesc.class);
			}else{
				CfReportRowAddDesc re=cfReportRowAddDescDao.get(CfReportRowAddDesc.class,cfrids[0]);
				cfReportRowAddDescDao.remove(re);
			}
		}
	}

	@Override
	public Page<?> qryCfReportRowAddOfPage(int page, int rows,CfReportRowAddDescDTO rid) {
		StringBuilder querySql=new StringBuilder("select c.*,cf1.codename reportTypeName from cfreportrowadddesc c,cfcodemanage cf1 where 1=1");
		querySql.append(" and  c.reporttype=cf1.codecode and cf1.codetype='cfreporttype' ");
		if(rid.getAsset()!=null&&!rid.getAsset().equals("")){
			querySql.append(" and asset like '%"+rid.getAsset()+"%'");
		}
		if(rid.getHalfYearReport()!=null&&!rid.getHalfYearReport().equals("")){
			querySql.append(" and HalfYearReport like '%"+rid.getHalfYearReport()+"%'");
		}
		if(rid.getYearReport()!=null&&!rid.getYearReport().equals("")){
			querySql.append(" and YearReport like '%"+rid.getYearReport()+"%'");
		}
		if(rid.getQuarterReport()!=null&&!rid.getQuarterReport().equals("")){
			querySql.append(" and QuarterReport like '%"+rid.getAsset()+"%'");
		}
		if(rid.getGroup1()!=null&&!rid.getGroup1().equals("")){
			querySql.append(" and Group1 like '%"+rid.getGroup1()+"%'");
		}
		if(rid.getGroup2()!=null&&!rid.getGroup2().equals("")){
			querySql.append(" and Group2 like '%"+rid.getGroup2()+"%'");
		}
		if(rid.getGroup3()!=null&&!rid.getGroup3().equals("")){
			querySql.append(" and Group3 like '%"+rid.getGroup3()+"%'");
		}
		if(rid.getLifeInsurance1()!=null&&!rid.getLifeInsurance1().equals("")){
			querySql.append(" and LifeInsurance1 like '%"+rid.getLifeInsurance1()+"%'");
		}
		if(rid.getLifeInsurance2()!=null&&!rid.getLifeInsurance2().equals("")){
			querySql.append(" and LifeInsurance2 like '%"+rid.getLifeInsurance2()+"%'");
		}
		if(rid.getLifeInsurance3()!=null&&!rid.getLifeInsurance3().equals("")){
			querySql.append(" and LifeInsurance3 like '%"+rid.getLifeInsurance3()+"%'");
		}
		if(rid.getLifeInsurance4()!=null&&!rid.getLifeInsurance4().equals("")){
			querySql.append(" and LifeInsurance4 like '%"+rid.getLifeInsurance4()+"%'");
		}
		if(rid.getReinsurance()!=null&&!rid.getReinsurance().equals("")){
			querySql.append(" and Reinsurance like '%"+rid.getReinsurance()+"%'");
		}
		if(rid.getPropertyInsurance1()!=null&&!rid.getPropertyInsurance1().equals("")){
			querySql.append(" and PropertyInsurance1 like '%"+rid.getPropertyInsurance1()+"%'");
		}
		if(rid.getPropertyInsurance2()!=null&&!rid.getPropertyInsurance2().equals("")){
			querySql.append(" and PropertyInsurance2 like '%"+rid.getPropertyInsurance2()+"%'");
		}
		if(rid.getPropertyInsurance3()!=null&&!rid.getPropertyInsurance3().equals("")){
			querySql.append(" and PropertyInsurance3 like '%"+rid.getPropertyInsurance3()+"%'");
		}
		if(rid.getOutItemNote()!=null&&!rid.getOutItemNote().equals("")){
			querySql.append(" and OutItemNote like '%"+rid.getOutItemNote()+"%'");
		}
		if(rid.getColCode()!=null&&!rid.getColCode().equals("")){
			querySql.append(" and colcode like '%"+rid.getColCode()+"%'");
		}
		if(rid.getColName()!=null&&!rid.getColName().equals("")){
			querySql.append(" and colname like '%"+rid.getColName()+"%'");
		}
		if(rid.getColName()!=null&&!rid.getColName().equals("")){
			querySql.append(" and colName like '%"+rid.getColName()+"%'");
		}
		if(rid.getTableCode()!=null&&!rid.getTableCode().equals("")){
			querySql.append(" and tableCode like '%"+rid.getTableCode()+"%'");
		}
		if(rid.getTableName()!=null&&!rid.getTableName().equals("")){
			querySql.append(" and tableName like '%"+rid.getTableName()+"%'");
		}
		if(rid.getSort()!=null&&!rid.getSort().equals("")){
			String[] sorts=rid.getSort().split(",");
			String[] orders=rid.getOrder().split(",");
			if(sorts.length>0){
				querySql.append(" order by "+sorts[0]+" "+orders[0]);
			}
			for(int i=1;i<orders.length;i++){
				querySql.append(" ,"+sorts[i]+" "+orders[i]);
			}
		}
		return cfReportRowAddDescDao.queryByPage(querySql.toString(), page, rows, CfReportRowAddDescDTO.class);
	}
	@Override
	public Page<?> qryCfReportRowAddOfPage(int page, int rows) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<?> qryCfReportRowAdd() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<?> qryCfReportRowAddAll() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public CfReportRowAddDesc findReportItem(CfReportRowAddDescId cfrid) {
		// TODO Auto-generated method stub
		return cfReportRowAddDescDao.get(CfReportRowAddDesc.class, cfrid);
	}

}
