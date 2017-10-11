package com.sinosoft.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sinosoft.common.Page;
import com.sinosoft.dao.InterTabInvestInfoDao;
import com.sinosoft.dto.InterTabInvestInfoDTO;
import com.sinosoft.entity.InterTabInvestInfo;
import com.sinosoft.service.InterTabInvestInfoService;

@Service
public class InterTabInvestInfoServiceImp implements InterTabInvestInfoService{
	@Resource
	private InterTabInvestInfoDao interTabInvestInfoDao;
	@Transactional
	public void saveInterTabInvestInfo(InterTabInvestInfoDTO dto) {
		// TODO Auto-generated method stub
		InterTabInvestInfo br=new InterTabInvestInfo();
	    br.setYear(dto.getYear());
	    br.setQuarter(dto.getQuarter());
	    br.setReportDate(dto.getReportDate());
	    br.setProperName(dto.getProperName());
	    br.setIssurer(dto.getIssurer());
	    br.setCreditLevel(dto.getCreditLevel());
	    br.setFixedDuration(dto.getFixedDuration());
	    br.setTemp2(dto.getTemp2());
	    br.setTemp3(dto.getTemp3());
	    br.setTemp4(dto.getTemp4());
	    br.setTemp5(dto.getTemp5());
		interTabInvestInfoDao.save(br);
	}
	@Transactional
	public void updateInterTabInvestInfo(long id, InterTabInvestInfoDTO dto) {
		// TODO Auto-generated method stub
		InterTabInvestInfo br =interTabInvestInfoDao.get(InterTabInvestInfo.class,id);
	    br.setYear(dto.getYear());
	    br.setQuarter(dto.getQuarter());
	    br.setReportDate(dto.getReportDate());
	    br.setProperName(dto.getProperName());
	    br.setIssurer(dto.getIssurer());
	    br.setCreditLevel(dto.getCreditLevel());
	    br.setFixedDuration(dto.getFixedDuration());
	    br.setTemp2(dto.getTemp2());
	    br.setTemp3(dto.getTemp3());
	    br.setTemp4(dto.getTemp4());
	    br.setTemp5(dto.getTemp5());
		interTabInvestInfoDao.update(br);
	}
	@Transactional
	public void deleteInterTabInvestInfo(String[] idArr) {
		// TODO Auto-generated method stub
		Long[] ids=new Long[idArr.length];
		for(int i=0;i<idArr.length;i++){
			ids[i]=Long.parseLong(idArr[i]);
		}
		interTabInvestInfoDao.removes(ids,InterTabInvestInfo.class);
	}
	public Page<?> qryInterTabInvestInfoOfPage(int page, int rows,InterTabInvestInfoDTO dto) {
		  StringBuilder querySql =new StringBuilder("select interTabInvestInfo.* from InterTabInvestInfo interTabInvestInfo where 1=1");
			if(!(dto.getYear()==null||"".equals(dto.getYear()))){
	    	    querySql.append("  and  Year like '%"+dto.getYear()+"%'");
		    }
			if(!(dto.getQuarter()==null||"".equals(dto.getQuarter()))){
	    	    querySql.append("  and  Quarter like '%"+dto.getQuarter()+"%'");
		    }
			if(!(dto.getReportDate()==null||"".equals(dto.getReportDate()))){
	    	    querySql.append("  and  ReportDate like '%"+dto.getReportDate()+"%'");
		    }
			if(!(dto.getReportId()==null||"".equals(dto.getReportId()))){
	    	    querySql.append("  and  ReportId like '%"+dto.getReportId()+"%'");
		    }			
			if(!(dto.getProperCode()==null||"".equals(dto.getProperCode()))){
	    	    querySql.append("  and  ProperCode like '%"+dto.getProperCode()+"%'");
		    }
			if(!(dto.getProperName()==null||"".equals(dto.getProperName()))){
	    	    querySql.append("  and  ProperName like '%"+dto.getProperName()+"%'");
		    }
			if(!(dto.getIssurer()==null||"".equals(dto.getIssurer()))){
	    	    querySql.append("  and  Issurer like '%"+dto.getIssurer()+"%'");
		    }
			if(!(dto.getCreditLevel()==null||"".equals(dto.getCreditLevel()))){
	    	    querySql.append("  and  CreditLevel like '%"+dto.getCreditLevel()+"%'");
		    }
			if(!(dto.getFixedDuration()==null||"".equals(dto.getFixedDuration()))){
	    	    querySql.append("  and  FixedDuration like '%"+dto.getFixedDuration()+"%'");
		    }
			if(!(dto.getTemp1()==null||"".equals(dto.getTemp1()))){
	    	    querySql.append("  and  Temp1 like '%"+dto.getTemp1()+"%'");
		    }
			if(!(dto.getTemp2()==null||"".equals(dto.getTemp2()))){
	    	    querySql.append("  and  Temp2 like '%"+dto.getTemp2()+"%'");
		    }
			if(!(dto.getTemp3()==null||"".equals(dto.getTemp3()))){
	    	    querySql.append("  and  Temp3 like '%"+dto.getTemp3()+"%'");
		    }
			if(!(dto.getTemp4()==null||"".equals(dto.getTemp4()))){
	    	    querySql.append("  and  Temp4 like '%"+dto.getTemp4()+"%'");
		    }
			if(!(dto.getTemp5()==null||"".equals(dto.getTemp5()))){
	    	    querySql.append("  and  Temp5 like '%"+dto.getTemp5()+"%'");
		    }
		return interTabInvestInfoDao.queryByPage(querySql.toString(),page,rows,InterTabInvestInfoDTO.class);
	}
	public List<?> qryInterTabInvestInfo() {
		StringBuilder querySql =new StringBuilder("select interTabInvestInfo.* from InterTabInvestInfo interTabInvestInfo");
		List<?> list = interTabInvestInfoDao.queryBysql(querySql.toString(),InterTabInvestInfoDTO.class);
		return list;
	}
	public List<?> qryInterTabInvestInfoAll() {
		List<?> list = interTabInvestInfoDao.queryBysql("select interTabInvestInfo.* from InterTabInvestInfo interTabInvestInfo",InterTabInvestInfoDTO.class);
		return list;
	}
	public InterTabInvestInfo findInterTabInvestInfo(long id) {
		// TODO Auto-generated method stub
		return interTabInvestInfoDao.get(InterTabInvestInfo.class,id);
	}

}