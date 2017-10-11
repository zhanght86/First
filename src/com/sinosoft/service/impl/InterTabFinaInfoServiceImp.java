package com.sinosoft.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sinosoft.common.Page;
import com.sinosoft.dao.InterTabFinaInfoDao;
import com.sinosoft.dto.InterTabFinaInfoDTO;
import com.sinosoft.entity.InterTabFinaInfo;
import com.sinosoft.service.InterTabFinaInfoService;

@Service
public class InterTabFinaInfoServiceImp implements InterTabFinaInfoService{
	@Resource
	private InterTabFinaInfoDao interTabFinaInfoDao;
	@Transactional
	public void saveInterTabFinaInfo(InterTabFinaInfoDTO dto) {
		// TODO Auto-generated method stub
		InterTabFinaInfo br=new InterTabFinaInfo();
	    br.setYear(dto.getYear());
	    br.setQuarter(dto.getQuarter());
	    br.setReportDate(dto.getReportDate());	   
	    br.setQcSum(dto.getQcSum());
	    br.setQmSum(dto.getQmSum());
	    br.setTemp1(dto.getTemp1());
	    br.setTemp2(dto.getTemp2());
	    br.setTemp3(dto.getTemp3());
	    br.setTemp4(dto.getTemp4());
	    br.setTemp5(dto.getTemp5());
		interTabFinaInfoDao.save(br);
	}
	@Transactional
	public void updateInterTabFinaInfo(long id, InterTabFinaInfoDTO dto) {
		// TODO Auto-generated method stub
		InterTabFinaInfo br =interTabFinaInfoDao.get(InterTabFinaInfo.class,id);
	    br.setYear(dto.getYear());
	    br.setQuarter(dto.getQuarter());
	    br.setReportDate(dto.getReportDate());
	    br.setQcSum(dto.getQcSum());
	    br.setQmSum(dto.getQmSum());
	    br.setTemp1(dto.getTemp1());
	    br.setTemp2(dto.getTemp2());
	    br.setTemp3(dto.getTemp3());
	    br.setTemp4(dto.getTemp4());
	    br.setTemp5(dto.getTemp5());
		interTabFinaInfoDao.update(br);
	}
	@Transactional
	public void deleteInterTabFinaInfo(String[] idArr) {
		// TODO Auto-generated method stub
		Long[] ids=new Long[idArr.length];
		for(int i=0;i<idArr.length;i++){
			ids[i]=Long.parseLong(idArr[i]);
		}
		interTabFinaInfoDao.removes(ids,InterTabFinaInfo.class);
	}
	public Page<?> qryInterTabFinaInfoOfPage(int page, int rows,InterTabFinaInfoDTO dto) {
		  StringBuilder querySql =new StringBuilder("select interTabFinaInfo.* from InterTabFinaInfo interTabFinaInfo where 1=1");
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
			if(!(dto.getItemCode()==null||"".equals(dto.getItemCode()))){
	    	    querySql.append("  and  ItemCode like '%"+dto.getItemCode()+"%'");
		    }
			if(!(dto.getItemName()==null||"".equals(dto.getItemName()))){
	    	    querySql.append("  and  ItemName like '%"+dto.getItemName()+"%'");
		    }
			if(!(dto.getQcSum()==null||"".equals(dto.getQcSum()))){
	    	    querySql.append("  and  QcSum like '%"+dto.getQcSum()+"%'");
		    }
			if(!(dto.getQmSum()==null||"".equals(dto.getQmSum()))){
	    	    querySql.append("  and  QmSum like '%"+dto.getQmSum()+"%'");
		    }
			if(!(dto.getTabName()==null||"".equals(dto.getTabName()))){
	    	    querySql.append("  and  TabName like '%"+dto.getTabName()+"%'");
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
		return interTabFinaInfoDao.queryByPage(querySql.toString(),page,rows,InterTabFinaInfoDTO.class);
	}
	public List<?> qryInterTabFinaInfo() {
		StringBuilder querySql =new StringBuilder("select interTabFinaInfo.* from InterTabFinaInfo interTabFinaInfo");
		List<?> list = interTabFinaInfoDao.queryBysql(querySql.toString(),InterTabFinaInfoDTO.class);
		return list;
	}
	public List<?> qryInterTabFinaInfoAll() {
		List<?> list = interTabFinaInfoDao.queryBysql("select interTabFinaInfo.* from InterTabFinaInfo interTabFinaInfo",InterTabFinaInfoDTO.class);
		return list;
	}
	public InterTabFinaInfo findInterTabFinaInfo(long id) {
		// TODO Auto-generated method stub
		return interTabFinaInfoDao.get(InterTabFinaInfo.class,id);
	}

}