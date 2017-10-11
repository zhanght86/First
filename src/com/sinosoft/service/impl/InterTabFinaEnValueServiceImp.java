package com.sinosoft.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sinosoft.common.Page;
import com.sinosoft.dao.InterTabFinaEnValueDao;
import com.sinosoft.dto.InterTabFinaEnValueDTO;
import com.sinosoft.entity.InterTabFinaEnValue;
import com.sinosoft.service.InterTabFinaEnValueService;

@Service
public class InterTabFinaEnValueServiceImp implements InterTabFinaEnValueService{
	@Resource
	private InterTabFinaEnValueDao interTabFinaEnValueDao;
	@Transactional
	public void saveInterTabFinaEnValue(InterTabFinaEnValueDTO dto) {
		// TODO Auto-generated method stub
		InterTabFinaEnValue br=new InterTabFinaEnValue();
	    br.setYear(dto.getYear());
	    br.setQuarter(dto.getQuarter());
	    br.setReportDate(dto.getReportDate());
	    br.setItemName(dto.getItemName());
	    br.setEnCounts(dto.getEnCounts());
	    br.setEnCosts(dto.getEnCosts());
	    br.setEnValue(dto.getEnValue());
	    br.setTemp1(dto.getTemp1());
	    br.setTemp3(dto.getTemp3());
	    br.setTemp4(dto.getTemp4());
	    br.setTemp5(dto.getTemp5());
		interTabFinaEnValueDao.save(br);
	}
	@Transactional
	public void updateInterTabFinaEnValue(long id, InterTabFinaEnValueDTO dto) {
		// TODO Auto-generated method stub
		InterTabFinaEnValue br =interTabFinaEnValueDao.get(InterTabFinaEnValue.class,id);
	    br.setYear(dto.getYear());
	    br.setQuarter(dto.getQuarter());
	    br.setReportDate(dto.getReportDate());
	    br.setItemName(dto.getItemName());
	    br.setEnCounts(dto.getEnCounts());
	    br.setEnCosts(dto.getEnCosts());
	    br.setEnValue(dto.getEnValue());
	    br.setTemp1(dto.getTemp1());
	    br.setTemp3(dto.getTemp3());
	    br.setTemp4(dto.getTemp4());
	    br.setTemp5(dto.getTemp5());
		interTabFinaEnValueDao.update(br);
	}
	@Transactional
	public void deleteInterTabFinaEnValue(String[] idArr) {
		// TODO Auto-generated method stub
		Long[] ids=new Long[idArr.length];
		for(int i=0;i<idArr.length;i++){
			ids[i]=Long.parseLong(idArr[i]);
		}
		interTabFinaEnValueDao.removes(ids,InterTabFinaEnValue.class);
	}
	public Page<?> qryInterTabFinaEnValueOfPage(int page, int rows,InterTabFinaEnValueDTO dto) {
		  StringBuilder querySql =new StringBuilder("select interTabFinaEnValue.* from InterTabFinaEnValue interTabFinaEnValue where 1=1");
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
			if(!(dto.getItemCode()==null||"".equals(dto.getItemCode()))){
	    	    querySql.append("  and  ItemCode like '%"+dto.getItemCode()+"%'");
		    }
			if(!(dto.getItemName()==null||"".equals(dto.getItemName()))){
	    	    querySql.append("  and  ItemName like '%"+dto.getItemName()+"%'");
		    }
			if(!(dto.getEnCounts()==null||"".equals(dto.getEnCounts()))){
	    	    querySql.append("  and  EnCounts like '%"+dto.getEnCounts()+"%'");
		    }
			if(!(dto.getEnCosts()==null||"".equals(dto.getEnCosts()))){
	    	    querySql.append("  and  EnCosts like '%"+dto.getEnCosts()+"%'");
		    }
			if(!(dto.getEnValue()==null||"".equals(dto.getEnValue()))){
	    	    querySql.append("  and  EnValue like '%"+dto.getEnValue()+"%'");
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
		return interTabFinaEnValueDao.queryByPage(querySql.toString(),page,rows,InterTabFinaEnValueDTO.class);
	}
	public List<?> qryInterTabFinaEnValue() {
		StringBuilder querySql =new StringBuilder("select interTabFinaEnValue.* from InterTabFinaEnValue interTabFinaEnValue");
		List<?> list = interTabFinaEnValueDao.queryBysql(querySql.toString(),InterTabFinaEnValueDTO.class);
		return list;
	}
	public List<?> qryInterTabFinaEnValueAll() {
		List<?> list = interTabFinaEnValueDao.queryBysql("select interTabFinaEnValue.* from InterTabFinaEnValue interTabFinaEnValue",InterTabFinaEnValueDTO.class);
		return list;
	}
	public InterTabFinaEnValue findInterTabFinaEnValue(long id) {
		// TODO Auto-generated method stub
		return interTabFinaEnValueDao.get(InterTabFinaEnValue.class,id);
	}

}