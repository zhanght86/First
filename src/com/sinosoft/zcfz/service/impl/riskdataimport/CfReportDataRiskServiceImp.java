package com.sinosoft.zcfz.service.impl.riskdataimport;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dao.riskdataimport.CfReportDataRiskDao;
import com.sinosoft.zcfz.dto.riskdataimport.CfReportDataRiskDTO;
import com.sinosoft.entity.CfReportDataRisk;
import com.sinosoft.zcfz.service.riskdataimport.CfReportDataRiskService;

@Service
public class CfReportDataRiskServiceImp implements CfReportDataRiskService{
	@Resource
	private CfReportDataRiskDao cfReportDataRiskDao;
	@Transactional
	public void saveCfReportDataRisk(CfReportDataRiskDTO dto) {
		// TODO Auto-generated method stub
		CfReportDataRisk br=new CfReportDataRisk();
	    br.setComCode(dto.getComCode());
	    br.setOutItemType(dto.getOutItemType());
	    br.setOutItemCodeName(dto.getOutItemCodeName());
	    br.setReportItemCode(dto.getReportItemCode());
	    br.setReportItemValue(dto.getReportItemValue());
	    br.setReportItemWanValue(dto.getReportItemWanValue());
	    br.setTextValue(dto.getTextValue());
	    br.setDesText(dto.getDesText());
	    br.setFileText(dto.getFileText());
	    br.setMonth(dto.getMonth());
	    br.setQuarter(dto.getQuarter());
	    br.setSource(dto.getSource());
	    br.setReportType(dto.getReportType());
	    br.setReportRate(dto.getReportRate());
	    br.setComputeLevel(dto.getComputeLevel());
	    br.setReportItemValueSource(dto.getReportItemValueSource());
	    br.setOperator(dto.getOperator());
	    br.setOperDate(dto.getOperDate());
	    br.setTemp(dto.getTemp());
		cfReportDataRiskDao.save(br);
	}
	@Transactional
	public void updateCfReportDataRisk(long id, CfReportDataRiskDTO dto) {
		// TODO Auto-generated method stub
		CfReportDataRisk br =cfReportDataRiskDao.get(CfReportDataRisk.class,id);
	    br.setComCode(dto.getComCode());
	    br.setOutItemType(dto.getOutItemType());
	    br.setOutItemCodeName(dto.getOutItemCodeName());
	    br.setReportItemCode(dto.getReportItemCode());
	    br.setReportItemValue(dto.getReportItemValue());
	    br.setReportItemWanValue(dto.getReportItemWanValue());
	    br.setTextValue(dto.getTextValue());
	    br.setDesText(dto.getDesText());
	    br.setFileText(dto.getFileText());
	    br.setMonth(dto.getMonth());
	    br.setQuarter(dto.getQuarter());
	    br.setSource(dto.getSource());
	    br.setReportType(dto.getReportType());
	    br.setReportRate(dto.getReportRate());
	    br.setComputeLevel(dto.getComputeLevel());
	    br.setReportItemValueSource(dto.getReportItemValueSource());
	    br.setOperator(dto.getOperator());
	    br.setOperDate(dto.getOperDate());
	    br.setTemp(dto.getTemp());
		cfReportDataRiskDao.update(br);
	}
	@Transactional
	public void deleteCfReportDataRisk(String[] idArr) {
		// TODO Auto-generated method stub
		Long[] ids=new Long[idArr.length];
		for(int i=0;i<idArr.length;i++){
			ids[i]=Long.parseLong(idArr[i]);
		}
		cfReportDataRiskDao.removes(ids,CfReportDataRisk.class);
	}
	public Page<?> qryCfReportDataRiskOfPage(int page, int rows,CfReportDataRiskDTO dto) {
		  StringBuilder querySql =new StringBuilder("select cfReportDataRisk.* from CfReportDataRisk cfReportDataRisk where 1=1");
			if(!(dto.getOutItemCode()==null||"".equals(dto.getOutItemCode()))){
	    	    querySql.append("  and  OutItemCode like '%"+dto.getOutItemCode()+"%'");
		    }
			if(!(dto.getComCode()==null||"".equals(dto.getComCode()))){
	    	    querySql.append("  and  ComCode like '%"+dto.getComCode()+"%'");
		    }
			if(!(dto.getOutItemType()==null||"".equals(dto.getOutItemType()))){
	    	    querySql.append("  and  OutItemType like '%"+dto.getOutItemType()+"%'");
		    }
			if(!(dto.getOutItemCodeName()==null||"".equals(dto.getOutItemCodeName()))){
	    	    querySql.append("  and  OutItemCodeName like '%"+dto.getOutItemCodeName()+"%'");
		    }
			if(!(dto.getReportItemCode()==null||"".equals(dto.getReportItemCode()))){
	    	    querySql.append("  and  ReportItemCode like '%"+dto.getReportItemCode()+"%'");
		    }
			if(!(dto.getReportItemValue()==null||"".equals(dto.getReportItemValue()))){
	    	    querySql.append("  and  ReportItemValue like '%"+dto.getReportItemValue()+"%'");
		    }
			if(!(dto.getReportItemWanValue()==null||"".equals(dto.getReportItemWanValue()))){
	    	    querySql.append("  and  ReportItemWanValue like '%"+dto.getReportItemWanValue()+"%'");
		    }
			if(!(dto.getTextValue()==null||"".equals(dto.getTextValue()))){
	    	    querySql.append("  and  TextValue like '%"+dto.getTextValue()+"%'");
		    }
			if(!(dto.getDesText()==null||"".equals(dto.getDesText()))){
	    	    querySql.append("  and  DesText like '%"+dto.getDesText()+"%'");
		    }
			if(!(dto.getFileText()==null||"".equals(dto.getFileText()))){
	    	    querySql.append("  and  FileText like '%"+dto.getFileText()+"%'");
		    }
			if(!(dto.getMonth()==null||"".equals(dto.getMonth()))){
	    	    querySql.append("  and  Month like '%"+dto.getMonth()+"%'");
		    }
			if(!(dto.getQuarter()==null||"".equals(dto.getQuarter()))){
	    	    querySql.append("  and  Quarter like '%"+dto.getQuarter()+"%'");
		    }
			if(!(dto.getSource()==null||"".equals(dto.getSource()))){
	    	    querySql.append("  and  Source like '%"+dto.getSource()+"%'");
		    }
			if(!(dto.getReportType()==null||"".equals(dto.getReportType()))){
	    	    querySql.append("  and  ReportType like '%"+dto.getReportType()+"%'");
		    }
			if(!(dto.getReportRate()==null||"".equals(dto.getReportRate()))){
	    	    querySql.append("  and  ReportRate like '%"+dto.getReportRate()+"%'");
		    }
			if(!(dto.getComputeLevel()==null||"".equals(dto.getComputeLevel()))){
	    	    querySql.append("  and  ComputeLevel like '%"+dto.getComputeLevel()+"%'");
		    }
			if(!(dto.getReportItemValueSource()==null||"".equals(dto.getReportItemValueSource()))){
	    	    querySql.append("  and  ReportItemValueSource like '%"+dto.getReportItemValueSource()+"%'");
		    }
			if(!(dto.getOperator()==null||"".equals(dto.getOperator()))){
	    	    querySql.append("  and  Operator like '%"+dto.getOperator()+"%'");
		    }
			if(!(dto.getOperDate()==null||"".equals(dto.getOperDate()))){
	    	    querySql.append("  and  OperDate like '%"+dto.getOperDate()+"%'");
		    }
			if(!(dto.getTemp()==null||"".equals(dto.getTemp()))){
	    	    querySql.append("  and  Temp like '%"+dto.getTemp()+"%'");
		    }
			if(!(dto.getReportId()==null||"".equals(dto.getReportId()))){
	    	    querySql.append("  and  ReportId like '%"+dto.getReportId()+"%'");
		    }
		return cfReportDataRiskDao.queryByPage(querySql.toString(),page,rows,CfReportDataRiskDTO.class);
	}
	public List<?> qryCfReportDataRisk() {
		StringBuilder querySql =new StringBuilder("select cfReportDataRisk.* from CfReportDataRisk cfReportDataRisk");
		List<?> list = cfReportDataRiskDao.queryBysql(querySql.toString(),CfReportDataRiskDTO.class);
		return list;
	}
	public List<?> qryCfReportDataRiskAll() {
		List<?> list = cfReportDataRiskDao.queryBysql("select cfReportDataRisk.* from CfReportDataRisk cfReportDataRisk",CfReportDataRiskDTO.class);
		return list;
	}
	public CfReportDataRisk findCfReportDataRisk(long id) {
		// TODO Auto-generated method stub
		return cfReportDataRiskDao.get(CfReportDataRisk.class,id);
	}

}