package com.sinosoft.zcfz.service.impl.reportformcompute;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dao.reportformcompute.Rf0DefineDao;
import com.sinosoft.zcfz.dto.reportformcompute.Rf0DefineDTO;
import com.sinosoft.entity.Rf0Define;
import com.sinosoft.zcfz.service.reportformcompute.Rf0DefineService;

@Service
public class Rf0DefineServiceImp implements Rf0DefineService{
	@Resource
	private Rf0DefineDao rf0DefineDao;
	@Transactional
	public void saveRf0Define(Rf0DefineDTO dto) {
		// TODO Auto-generated method stub
		Rf0Define br=new Rf0Define();
	    br.setRf0Code(dto.getRf0Code());
	    br.setRisktype1Code(dto.getRisktype1Code());
	    br.setRisktype1Name(dto.getRisktype1Name());
	    br.setRisktype2Code(dto.getRisktype2Code());
	    br.setRisktype2Name(dto.getRisktype2Name());
	    br.setRisktype3Code(dto.getRisktype3Code());
	    br.setRisktype3Name(dto.getRisktype3Name());
	    br.setDetailCode(dto.getDetailCode());
	    br.setDetailDescribe(dto.getDetailDescribe());
	    br.setRf0Define(dto.getRf0Define());
	    br.setRf0DefineFrom(dto.getRf0DefineFrom());
	    br.setRf0DefineTo(dto.getRf0DefineTo());
	    br.setRf0Value(dto.getRf0Value());
	    br.setRf0DefineVar(dto.getRf0DefineVar());
	    br.setRf0ValueRule(dto.getRf0ValueRule());
		rf0DefineDao.save(br);
	}
	@Transactional
	public void updateRf0Define(int id, Rf0DefineDTO dto) {
		// TODO Auto-generated method stub
		Rf0Define br =rf0DefineDao.get(Rf0Define.class,id);
	    br.setRf0Code(dto.getRf0Code());
	    br.setRisktype1Code(dto.getRisktype1Code());
	    br.setRisktype1Name(dto.getRisktype1Name());
	    br.setRisktype2Code(dto.getRisktype2Code());
	    br.setRisktype2Name(dto.getRisktype2Name());
	    br.setRisktype3Code(dto.getRisktype3Code());
	    br.setRisktype3Name(dto.getRisktype3Name());
	    br.setDetailCode(dto.getDetailCode());
	    br.setDetailDescribe(dto.getDetailDescribe());
	    br.setRf0Define(dto.getRf0Define());
	    br.setRf0DefineFrom(dto.getRf0DefineFrom());
	    br.setRf0DefineTo(dto.getRf0DefineTo());
	    br.setRf0Value(dto.getRf0Value());
	    br.setRf0DefineVar(dto.getRf0DefineVar());
	    br.setRf0ValueRule(dto.getRf0ValueRule());
		rf0DefineDao.update(br);
	}
	@Transactional
	public void deleteRf0Define(String[] idArr) {
		// TODO Auto-generated method stub
		Integer[] ids=new Integer[idArr.length];
		for(int i=0;i<idArr.length;i++){
			ids[i]=Integer.parseInt(idArr[i]);
		}
		rf0DefineDao.removes(ids,Rf0Define.class);
	}
	public Page<?> qryRf0DefineOfPage(int page, int rows,Rf0DefineDTO dto) {
		  StringBuilder querySql =new StringBuilder("select rf0Define.* from cal_Rf0Define rf0Define where 1=1");
			if(!(dto.getRf0Code()==null||"".equals(dto.getRf0Code()))){
	    	    querySql.append("  and  Rf0Code like '%"+dto.getRf0Code()+"%'");
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
			if(!(dto.getRf0Define()==null||"".equals(dto.getRf0Define()))){
	    	    querySql.append("  and  Rf0Define like '%"+dto.getRf0Define()+"%'");
		    }
			if(!(dto.getRf0DefineFrom()==null||"".equals(dto.getRf0DefineFrom()))){
	    	    querySql.append("  and  Rf0DefineFrom like '%"+dto.getRf0DefineFrom()+"%'");
		    }
			if(!(dto.getRf0DefineTo()==null||"".equals(dto.getRf0DefineTo()))){
	    	    querySql.append("  and  Rf0DefineTo like '%"+dto.getRf0DefineTo()+"%'");
		    }
			if(!(dto.getRf0Value()==null||"".equals(dto.getRf0Value()))){
	    	    querySql.append("  and  Rf0Value like '%"+dto.getRf0Value()+"%'");
		    }
		return rf0DefineDao.queryByPage(querySql.toString(),page,rows,Rf0DefineDTO.class);
	}
	public List<?> qryRf0Define() {
		StringBuilder querySql =new StringBuilder("select rf0Define.* from cal_Rf0Define rf0Define");
		List<?> list = rf0DefineDao.queryBysql(querySql.toString(),Rf0DefineDTO.class);
		return list;
	}
	public List<?> qryRf0DefineAll() {
		List<?> list = rf0DefineDao.queryBysql("select rf0Define.* from cal_Rf0Define rf0Define",Rf0DefineDTO.class);
		return list;
	}
	public Rf0Define findRf0Define(long id) {
		// TODO Auto-generated method stub
		return rf0DefineDao.get(Rf0Define.class,id);
	}

}