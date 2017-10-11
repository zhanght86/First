package com.sinosoft.zcfz.service.impl.reportformcompute;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dao.reportformcompute.CalCfReportRelationDao;
import com.sinosoft.zcfz.dto.reportformcompute.CalCfReportRelationDTO;
import com.sinosoft.entity.CalCfReportRelation;
import com.sinosoft.zcfz.service.reportformcompute.CalCfReportRelationService;

@Service
public class CalCfReportRelationServiceImp implements CalCfReportRelationService{
	@Resource
	private CalCfReportRelationDao calCfReportRelationDao;
	@Transactional
	public void saveCalCfReportRelation(CalCfReportRelationDTO dto) {
		// TODO Auto-generated method stub
		CalCfReportRelation br=new CalCfReportRelation();
	    br.setPortItemCode(dto.getPortItemCode());
	    br.setPortItemType(dto.getPortItemType());
	    br.setPortItemHome(dto.getPortItemHome());
	    br.setInterfaceType(dto.getInterfaceType());
	    br.setInterfacePriority(dto.getInterfacePriority());
	    br.setInterfaceGetData(dto.getInterfaceGetData());
	    br.setCalculateType(dto.getCalculateType());
	    br.setCalculateRule(dto.getCalculateRule());
	    br.setVarFlag(dto.getVarFlag());
	    br.setComputeVar(dto.getComputeVar());
	    br.setComputeVarRule(dto.getComputeVarRule());
	    br.setInterfaceGetDataVar(dto.getInterfaceGetDataVar());
	    br.setRemark(dto.getRemark());
		calCfReportRelationDao.save(br);
	}
	@Transactional
	public void updateCalCfReportRelation(Integer id, CalCfReportRelationDTO dto) {
		// TODO Auto-generated method stub
		CalCfReportRelation br =calCfReportRelationDao.get(CalCfReportRelation.class,dto.getPortItemCode());
	    br.setPortItemType(dto.getPortItemType());
	    br.setPortItemHome(dto.getPortItemHome());
	    br.setInterfaceType(dto.getInterfaceType());
	    br.setInterfacePriority(dto.getInterfacePriority());
	    br.setInterfaceGetData(dto.getInterfaceGetData());
	    br.setCalculateType(dto.getCalculateType());
	    br.setCalculateRule(dto.getCalculateRule());
	    br.setVarFlag(dto.getVarFlag());
	    br.setComputeVar(dto.getComputeVar());
	    br.setInterfaceGetDataVar(dto.getInterfaceGetDataVar());
	    br.setComputeVarRule(dto.getComputeVarRule());
	    br.setRemark(dto.getRemark());
		calCfReportRelationDao.update(br);
	}
	@Transactional
	public void deleteCalCfReportRelation(String[] idArr) {
		// TODO Auto-generated method stub
		calCfReportRelationDao.removes(idArr,CalCfReportRelation.class);
	}
	public Page<?> qryCalCfReportRelationOfPage(int page, int rows,CalCfReportRelationDTO dto) {
		  StringBuilder querySql =new StringBuilder("select calCfReportRelation.* from Cal_CfReportRelation calCfReportRelation where 1=1");
			if(!(dto.getPortItemCode()==null||"".equals(dto.getPortItemCode()))){
	    	    querySql.append("  and  PortItemCode like '%"+dto.getPortItemCode()+"%'");
		    }
			if(!(dto.getPortItemType()==null||"".equals(dto.getPortItemType()))){
	    	    querySql.append("  and  PortItemType like '%"+dto.getPortItemType()+"%'");
		    }
			if(!(dto.getInterfaceType()==null||"".equals(dto.getInterfaceType()))){
	    	    querySql.append("  and  InterfaceType like '%"+dto.getInterfaceType()+"%'");
		    }
			if(!("".equals(dto.getInterfacePriority())||dto.getInterfacePriority()==null)){
	    	    querySql.append("  and  InterfacePriority like '%"+dto.getInterfacePriority()+"%'");
		    }
			if(!(dto.getInterfaceGetData()==null||"".equals(dto.getInterfaceGetData()))){
	    	    querySql.append("  and  InterfaceGetData like '%"+dto.getInterfaceGetData()+"%'");
		    }
			if(!(dto.getCalculateType()==null||"".equals(dto.getCalculateType()))){
	    	    querySql.append("  and  CalculateType like '%"+dto.getCalculateType()+"%'");
		    }
			if(!(dto.getCalculateRule()==null||"".equals(dto.getCalculateRule()))){
	    	    querySql.append("  and  CalculateRule like '%"+dto.getCalculateRule()+"%'");
		    }
			if(!(dto.getVarFlag()==null||"".equals(dto.getVarFlag()))){
	    	    querySql.append("  and  VarFlag like '%"+dto.getVarFlag()+"%'");
		    }
			if(!(dto.getComputeVar()==null||"".equals(dto.getComputeVar()))){
	    	    querySql.append("  and  ComputeVar like '%"+dto.getComputeVar()+"%'");
		    }
			if(!(dto.getComputeVarRule()==null||"".equals(dto.getComputeVarRule()))){
	    	    querySql.append("  and  ComputeVarRule like '%"+dto.getComputeVarRule()+"%'");
		    }
			if(!(dto.getRemark()==null||"".equals(dto.getRemark()))){
	    	    querySql.append("  and  Remark like '%"+dto.getRemark()+"%'");
		    }
		return calCfReportRelationDao.queryByPage(querySql.toString(),page,rows,CalCfReportRelationDTO.class);
	}
	public List<?> qryCalCfReportRelation() {
		StringBuilder querySql =new StringBuilder("select calCfReportRelation.* from Cal_CfReportRelation calCfReportRelation");
		List<?> list = calCfReportRelationDao.queryBysql(querySql.toString(),CalCfReportRelationDTO.class);
		return list;
	}
	public List<?> qryCalCfReportRelationAll() {
		List<?> list = calCfReportRelationDao.queryBysql("select calCfReportRelation.* from Cal_CfReportRelation calCfReportRelation",CalCfReportRelationDTO.class);
		return list;
	}
	public CalCfReportRelation findCalCfReportRelation(String id) {
		// TODO Auto-generated method stub
		return calCfReportRelationDao.get(CalCfReportRelation.class,id);
	}

}