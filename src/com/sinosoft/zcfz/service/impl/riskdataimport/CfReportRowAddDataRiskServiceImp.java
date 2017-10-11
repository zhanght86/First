package com.sinosoft.zcfz.service.impl.riskdataimport;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dao.riskdataimport.CfReportRowAddDataRiskDao;
import com.sinosoft.zcfz.dto.riskdataimport.CfReportRowAddDataRiskDTO;
import com.sinosoft.entity.CfReportRowAddDataRisk;
import com.sinosoft.zcfz.service.riskdataimport.CfReportRowAddDataRiskService;

@Service
public class CfReportRowAddDataRiskServiceImp implements CfReportRowAddDataRiskService{
	@Resource
	private CfReportRowAddDataRiskDao cfReportRowAddDataRiskDao;
	@Transactional
	public void saveCfReportRowAddDataRisk(CfReportRowAddDataRiskDTO dto) {
		// TODO Auto-generated method stub
		CfReportRowAddDataRisk br=new CfReportRowAddDataRisk();
	    br.setTableCode(dto.getTableCode());
	    br.setComCode(dto.getComCode());
	    br.setColType(dto.getColType());
	    br.setNumValue(dto.getNumValue());
	    br.setWanValue(dto.getWanValue());
	    br.setTextValue(dto.getTextValue());
	    br.setDesText(dto.getDesText());
	    br.setReportType(dto.getReportType());
	    br.setYearMonth(dto.getYearMonth());
	    br.setQuarter(dto.getQuarter());
	    br.setReportRate(dto.getReportRate());
	    br.setSource(dto.getSource());
	    br.setOperator2(dto.getOperator2());
	    br.setOperDate2(dto.getOperDate2());
		cfReportRowAddDataRiskDao.save(br);
	}
	@Transactional
	public void updateCfReportRowAddDataRisk(long id, CfReportRowAddDataRiskDTO dto) {
		// TODO Auto-generated method stub
		CfReportRowAddDataRisk br =cfReportRowAddDataRiskDao.get(CfReportRowAddDataRisk.class,id);
	    br.setTableCode(dto.getTableCode());
	    br.setComCode(dto.getComCode());
	    br.setColType(dto.getColType());
	    br.setNumValue(dto.getNumValue());
	    br.setWanValue(dto.getWanValue());
	    br.setTextValue(dto.getTextValue());
	    br.setDesText(dto.getDesText());
	    br.setReportType(dto.getReportType());
	    br.setYearMonth(dto.getYearMonth());
	    br.setQuarter(dto.getQuarter());
	    br.setReportRate(dto.getReportRate());
	    br.setSource(dto.getSource());
	    br.setOperator2(dto.getOperator2());
	    br.setOperDate2(dto.getOperDate2());
		cfReportRowAddDataRiskDao.update(br);
	}
	@Transactional
	public void deleteCfReportRowAddDataRisk(String[] idArr) {
		// TODO Auto-generated method stub
		Long[] ids=new Long[idArr.length];
		for(int i=0;i<idArr.length;i++){
			ids[i]=Long.parseLong(idArr[i]);
		}
		cfReportRowAddDataRiskDao.removes(ids,CfReportRowAddDataRisk.class);
	}
	public Page<?> qryCfReportRowAddDataRiskOfPage(int page, int rows,CfReportRowAddDataRiskDTO dto) {
		  StringBuilder querySql =new StringBuilder("select cfReportRowAddDataRisk.* from CfReportRowAddDataRisk cfReportRowAddDataRisk where 1=1");
			if(!(dto.getTableCode()==null||"".equals(dto.getTableCode()))){
	    	    querySql.append("  and  TableCode like '%"+dto.getTableCode()+"%'");
		    }
			if(!(dto.getColCode()==null||"".equals(dto.getColCode()))){
	    	    querySql.append("  and  ColCode like '%"+dto.getColCode()+"%'");
		    }
			if(!(dto.getComCode()==null||"".equals(dto.getComCode()))){
	    	    querySql.append("  and  ComCode like '%"+dto.getComCode()+"%'");
		    }
			if(!(dto.getRowNo()==null||"".equals(dto.getRowNo()))){
	    	    querySql.append("  and  RowNo like '%"+dto.getRowNo()+"%'");
		    }
			if(!(dto.getColType()==null||"".equals(dto.getColType()))){
	    	    querySql.append("  and  ColType like '%"+dto.getColType()+"%'");
		    }
			if(!(dto.getNumValue()==null||"".equals(dto.getNumValue()))){
	    	    querySql.append("  and  NumValue like '%"+dto.getNumValue()+"%'");
		    }
			if(!(dto.getWanValue()==null||"".equals(dto.getWanValue()))){
	    	    querySql.append("  and  WanValue like '%"+dto.getWanValue()+"%'");
		    }
			if(!(dto.getTextValue()==null||"".equals(dto.getTextValue()))){
	    	    querySql.append("  and  TextValue like '%"+dto.getTextValue()+"%'");
		    }
			if(!(dto.getDesText()==null||"".equals(dto.getDesText()))){
	    	    querySql.append("  and  DesText like '%"+dto.getDesText()+"%'");
		    }
			if(!(dto.getReportType()==null||"".equals(dto.getReportType()))){
	    	    querySql.append("  and  ReportType like '%"+dto.getReportType()+"%'");
		    }
			if(!(dto.getYearMonth()==null||"".equals(dto.getYearMonth()))){
	    	    querySql.append("  and  YearMonth like '%"+dto.getYearMonth()+"%'");
		    }
			if(!(dto.getQuarter()==null||"".equals(dto.getQuarter()))){
	    	    querySql.append("  and  Quarter like '%"+dto.getQuarter()+"%'");
		    }
			if(!(dto.getReportRate()==null||"".equals(dto.getReportRate()))){
	    	    querySql.append("  and  ReportRate like '%"+dto.getReportRate()+"%'");
		    }
			if(!(dto.getSource()==null||"".equals(dto.getSource()))){
	    	    querySql.append("  and  Source like '%"+dto.getSource()+"%'");
		    }
			if(!(dto.getOperator2()==null||"".equals(dto.getOperator2()))){
	    	    querySql.append("  and  Operator2 like '%"+dto.getOperator2()+"%'");
		    }
			if(!(dto.getOperDate2()==null||"".equals(dto.getOperDate2()))){
	    	    querySql.append("  and  OperDate2 like '%"+dto.getOperDate2()+"%'");
		    }
			if(!(dto.getReportId()==null||"".equals(dto.getReportId()))){
	    	    querySql.append("  and  ReportId like '%"+dto.getReportId()+"%'");
		    }
		return cfReportRowAddDataRiskDao.queryByPage(querySql.toString(),page,rows,CfReportRowAddDataRiskDTO.class);
	}
	public List<?> qryCfReportRowAddDataRisk() {
		StringBuilder querySql =new StringBuilder("select cfReportRowAddDataRisk.* from CfReportRowAddDataRisk cfReportRowAddDataRisk");
		List<?> list = cfReportRowAddDataRiskDao.queryBysql(querySql.toString(),CfReportRowAddDataRiskDTO.class);
		return list;
	}
	public List<?> qryCfReportRowAddDataRiskAll() {
		List<?> list = cfReportRowAddDataRiskDao.queryBysql("select cfReportRowAddDataRisk.* from CfReportRowAddDataRisk cfReportRowAddDataRisk",CfReportRowAddDataRiskDTO.class);
		return list;
	}
	public CfReportRowAddDataRisk findCfReportRowAddDataRisk(long id) {
		// TODO Auto-generated method stub
		return cfReportRowAddDataRiskDao.get(CfReportRowAddDataRisk.class,id);
	}

}