package com.sinosoft.zcfz.service.impl.reportformcompute;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dao.reportformcompute.KdefineDao;
import com.sinosoft.zcfz.dto.reportformcompute.KdefineDTO;
import com.sinosoft.entity.Kdefine;
import com.sinosoft.zcfz.service.reportformcompute.KdefineService;

@Service
public class KdefineServiceImp implements KdefineService{
	@Resource
	private KdefineDao kdefineDao;
	@Transactional
	public void saveKdefine(KdefineDTO dto) {
		// TODO Auto-generated method stub
		Kdefine br=new Kdefine();
	    br.setKcode(dto.getKcode());
	    br.setRisktype1Code(dto.getRisktype1Code());
	    br.setRisktype1Name(dto.getRisktype1Name());
	    br.setRisktype2Code(dto.getRisktype2Code());
	    br.setRisktype2Name(dto.getRisktype2Name());
	    br.setRisktype3Code(dto.getRisktype3Code());
	    br.setRisktype3Name(dto.getRisktype3Name());
	    br.setDetailCode(dto.getDetailCode());
	    br.setDetailDescribe(dto.getDetailDescribe());
	    br.setKdefine(dto.getKdefine());
	    br.setKdefineFrom(dto.getKdefineFrom());
	    br.setKdefineTo(dto.getKdefineTo());
	    br.setKvalue(dto.getKvalue());
	    br.setKdefineVar(dto.getKdefineVar());
	    br.setKvalueRule(dto.getKvalueRule());
		kdefineDao.save(br);
	}
	@Transactional
	public void updateKdefine(int id, KdefineDTO dto) {
		// TODO Auto-generated method stub
		Kdefine br =kdefineDao.get(Kdefine.class,id);
	    br.setKcode(dto.getKcode());
	    br.setRisktype1Code(dto.getRisktype1Code());
	    br.setRisktype1Name(dto.getRisktype1Name());
	    br.setRisktype2Code(dto.getRisktype2Code());
	    br.setRisktype2Name(dto.getRisktype2Name());
	    br.setRisktype3Code(dto.getRisktype3Code());
	    br.setRisktype3Name(dto.getRisktype3Name());
	    br.setDetailCode(dto.getDetailCode());
	    br.setDetailDescribe(dto.getDetailDescribe());
	    br.setKdefine(dto.getKdefine());
	    br.setKdefineVar(dto.getKdefineVar());
	    br.setKdefineFrom(dto.getKdefineFrom());
	    br.setKdefineTo(dto.getKdefineTo());
	    br.setKvalue(dto.getKvalue());
	    br.setKvalueRule(dto.getKvalueRule());
		kdefineDao.update(br);
	}
	@Transactional
	public void deleteKdefine(String[] idArr) {
		// TODO Auto-generated method stub
		Integer[] ids=new Integer[idArr.length];
		for(int i=0;i<idArr.length;i++){
			ids[i]=Integer.parseInt(idArr[i]);
		}
		kdefineDao.removes(ids,Kdefine.class);
	}
	public Page<?> qryKdefineOfPage(int page, int rows,KdefineDTO dto) {
		  StringBuilder querySql =new StringBuilder("select kdefine.* from cal_Kdefine kdefine where 1=1");
			if(!(dto.getKcode()==null||"".equals(dto.getKcode()))){
	    	    querySql.append("  and  Kcode like '%"+dto.getKcode()+"%'");
		    }
			if(!(dto.getRisktype1Code()==null||"".equals(dto.getRisktype1Code()))){
	    	    querySql.append("  and  Risktype1Code like '%"+dto.getRisktype1Code()+"%'");
		    }
			if(!(dto.getRisktype1Name()==null||"".equals(dto.getRisktype1Name()))){
	    	    querySql.append("  and  Risktype1Name like '%"+dto.getRisktype1Name()+"%'");
		    }
			if(!(dto.getRisktype2Code()==null||"".equals(dto.getRisktype2Code()))){
	    	    querySql.append("  and  Risktype2Code like '%"+dto.getRisktype2Code()+"%'");
		    }
			if(!(dto.getRisktype2Name()==null||"".equals(dto.getRisktype2Name()))){
	    	    querySql.append("  and  Risktype2Name like '%"+dto.getRisktype2Name()+"%'");
		    }
			if(!(dto.getRisktype3Code()==null||"".equals(dto.getRisktype3Code()))){
	    	    querySql.append("  and  Risktype3Code like '%"+dto.getRisktype3Code()+"%'");
		    }
			if(!(dto.getRisktype3Name()==null||"".equals(dto.getRisktype3Name()))){
	    	    querySql.append("  and  Risktype3Name like '%"+dto.getRisktype3Name()+"%'");
		    }
			if(!(dto.getDetailCode()==null||"".equals(dto.getDetailCode()))){
	    	    querySql.append("  and  DetailCode like '%"+dto.getDetailCode()+"%'");
		    }
			if(!(dto.getDetailDescribe()==null||"".equals(dto.getDetailDescribe()))){
	    	    querySql.append("  and  DetailDescribe like '%"+dto.getDetailDescribe()+"%'");
		    }
			if(!(dto.getKdefine()==null||"".equals(dto.getKdefine()))){
	    	    querySql.append("  and  Kdefine like '%"+dto.getKdefine()+"%'");
		    }
			if(!(dto.getKdefineFrom()==null||"".equals(dto.getKdefineFrom()))){
	    	    querySql.append("  and  KdefineFrom like '%"+dto.getKdefineFrom()+"%'");
		    }
			if(!(dto.getKdefineTo()==null||"".equals(dto.getKdefineTo()))){
	    	    querySql.append("  and  KdefineTo like '%"+dto.getKdefineTo()+"%'");
		    }
			if(!(dto.getKvalue()==null||"".equals(dto.getKvalue()))){
	    	    querySql.append("  and  Kvalue like '%"+dto.getKvalue()+"%'");
		    }
		return kdefineDao.queryByPage(querySql.toString(),page,rows,KdefineDTO.class);
	}
	public List<?> qryKdefine() {
		StringBuilder querySql =new StringBuilder("select kdefine.* from cal_Kdefine kdefine");
		List<?> list = kdefineDao.queryBysql(querySql.toString(),KdefineDTO.class);
		return list;
	}
	public List<?> qryKdefineAll() {
		List<?> list = kdefineDao.queryBysql("select kdefine.* from cal_Kdefine kdefine",KdefineDTO.class);
		return list;
	}
	public Kdefine findKdefine(long id) {
		// TODO Auto-generated method stub
		return kdefineDao.get(Kdefine.class,id);
	}

}