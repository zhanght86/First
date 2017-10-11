package com.sinosoft.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sinosoft.common.Page;
import com.sinosoft.dao.InterTabFinaDateInfoDao;
import com.sinosoft.dto.InterTabFinaDateInfoDTO;
import com.sinosoft.entity.InterTabFinaDateInfo;
import com.sinosoft.service.InterTabFinaDateInfoService;

@Service
public class InterTabFinaDateInfoServiceImp implements InterTabFinaDateInfoService{
	@Resource
	private InterTabFinaDateInfoDao interTabFinaDateInfoDao;
	@Transactional
	public void saveInterTabFinaDateInfo(InterTabFinaDateInfoDTO dto) {
		// TODO Auto-generated method stub
		InterTabFinaDateInfo br=new InterTabFinaDateInfo();
	    br.setYear(dto.getYear());
	    br.setQuarter(dto.getQuarter());
	    br.setReportDate(dto.getReportDate());	    
	    br.setProperName(dto.getProperName());
	    br.setStartDate(dto.getStartDate());
	    br.setEndDate(dto.getEndDate());
	    br.setCurrDate(dto.getCurrDate());
	    br.setTemp1(dto.getTemp1());
	    br.setTemp2(dto.getTemp2());
	    br.setTemp3(dto.getTemp3());
	    br.setTemp4(dto.getTemp4());
	    br.setTemp5(dto.getTemp5());
		interTabFinaDateInfoDao.save(br);
	}
	@Transactional
	public void updateInterTabFinaDateInfo(long id, InterTabFinaDateInfoDTO dto) {
		// TODO Auto-generated method stub
		InterTabFinaDateInfo br =interTabFinaDateInfoDao.get(InterTabFinaDateInfo.class,id);
	    br.setYear(dto.getYear());
	    br.setQuarter(dto.getQuarter());
	    br.setReportDate(dto.getReportDate());	    
	    br.setProperName(dto.getProperName());
	    br.setStartDate(dto.getStartDate());
	    br.setEndDate(dto.getEndDate());
	    br.setCurrDate(dto.getCurrDate());
	    br.setTemp1(dto.getTemp1());
	    br.setTemp2(dto.getTemp2());
	    br.setTemp3(dto.getTemp3());
	    br.setTemp4(dto.getTemp4());
	    br.setTemp5(dto.getTemp5());
		interTabFinaDateInfoDao.update(br);
	}
	@Transactional
	public void deleteInterTabFinaDateInfo(String[] idArr) {
		// TODO Auto-generated method stub
		Long[] ids=new Long[idArr.length];
		for(int i=0;i<idArr.length;i++){
			ids[i]=Long.parseLong(idArr[i]);
		}
		interTabFinaDateInfoDao.removes(ids,InterTabFinaDateInfo.class);
	}
	public Page<?> qryInterTabFinaDateInfoOfPage(int page, int rows,InterTabFinaDateInfoDTO dto) {
		  StringBuilder querySql =new StringBuilder("select interTabFinaDateInfo.* from InterTabFinaDateInfo interTabFinaDateInfo where 1=1");
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
			if(!(dto.getAccountType()==null||"".equals(dto.getAccountType()))){
	    	    querySql.append("  and  AccountType like '%"+dto.getAccountType()+"%'");
		    }
			if(!(dto.getProperCode()==null||"".equals(dto.getProperCode()))){
	    	    querySql.append("  and  ProperCode like '%"+dto.getProperCode()+"%'");
		    }
			if(!(dto.getProperName()==null||"".equals(dto.getProperName()))){
	    	    querySql.append("  and  ProperName like '%"+dto.getProperName()+"%'");
		    }
			if(!(dto.getStartDate()==null||"".equals(dto.getStartDate()))){
	    	    querySql.append("  and  StartDate like '%"+dto.getStartDate()+"%'");
		    }
			if(!(dto.getEndDate()==null||"".equals(dto.getEndDate()))){
	    	    querySql.append("  and  EndDate like '%"+dto.getEndDate()+"%'");
		    }
			if(!(dto.getCurrDate()==null||"".equals(dto.getCurrDate()))){
	    	    querySql.append("  and  CurrDate like '%"+dto.getCurrDate()+"%'");
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
		return interTabFinaDateInfoDao.queryByPage(querySql.toString(),page,rows,InterTabFinaDateInfoDTO.class);
	}
	public List<?> qryInterTabFinaDateInfo() {
		StringBuilder querySql =new StringBuilder("select interTabFinaDateInfo.* from InterTabFinaDateInfo interTabFinaDateInfo");
		List<?> list = interTabFinaDateInfoDao.queryBysql(querySql.toString(),InterTabFinaDateInfoDTO.class);
		return list;
	}
	public List<?> qryInterTabFinaDateInfoAll() {
		List<?> list = interTabFinaDateInfoDao.queryBysql("select interTabFinaDateInfo.* from InterTabFinaDateInfo interTabFinaDateInfo",InterTabFinaDateInfoDTO.class);
		return list;
	}
	public InterTabFinaDateInfo findInterTabFinaDateInfo(long id) {
		// TODO Auto-generated method stub
		return interTabFinaDateInfoDao.get(InterTabFinaDateInfo.class,id);
	}

}