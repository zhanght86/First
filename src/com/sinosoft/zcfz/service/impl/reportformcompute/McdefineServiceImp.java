package com.sinosoft.zcfz.service.impl.reportformcompute;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dao.reportformcompute.McdefineDao;
import com.sinosoft.zcfz.dto.reportformcompute.McdefineDTO;
import com.sinosoft.entity.Mcdefine;
import com.sinosoft.zcfz.service.reportformcompute.McdefineService;

@Service
public class McdefineServiceImp implements McdefineService{
	@Resource
	private McdefineDao mcdefineDao;
	@Transactional
	public void saveMcdefine(McdefineDTO dto) {
		// TODO Auto-generated method stub
		Mcdefine br=new Mcdefine();
	    br.setMcCode(dto.getMcCode());
	    br.setRisktype1Code(dto.getRisktype1Code());
	    br.setRisktype1Name(dto.getRisktype1Name());
	    br.setRisktype2Code(dto.getRisktype2Code());
	    br.setRisktype2Name(dto.getRisktype2Name());
	    br.setRisktype3Code(dto.getRisktype3Code());
	    br.setRisktype3Name(dto.getRisktype3Name());
	    br.setMcDefine(dto.getMcDefine());
		mcdefineDao.save(br);
	}
	@Transactional
	public void updateMcdefine(Long id, McdefineDTO dto) {
		// TODO Auto-generated method stub
		Mcdefine br =mcdefineDao.get(Mcdefine.class,dto.getMcCode());
	    br.setMcCode(dto.getMcCode());
	    br.setRisktype1Code(dto.getRisktype1Code());
	    br.setRisktype1Name(dto.getRisktype1Name());
	    br.setRisktype2Code(dto.getRisktype2Code());
	    br.setRisktype2Name(dto.getRisktype2Name());
	    br.setRisktype3Code(dto.getRisktype3Code());
	    br.setRisktype3Name(dto.getRisktype3Name());
	    br.setMcDefine(dto.getMcDefine());
		mcdefineDao.update(br);
	}
	@Transactional
	public void deleteMcdefine(String[] idArr) {
		// TODO Auto-generated method stub
		mcdefineDao.removes(idArr,Mcdefine.class);
	}
	public Page<?> qryMcdefineOfPage(int page, int rows,McdefineDTO dto) {
		  StringBuilder querySql =new StringBuilder("select mcdefine.* from cal_MCDefine mcdefine where 1=1");
			if(!(dto.getMcCode()==null||"".equals(dto.getMcCode()))){
	    	    querySql.append("  and  McCode like '%"+dto.getMcCode()+"%'");
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
			if(!(dto.getMcDefine()==null||"".equals(dto.getMcDefine()))){
	    	    querySql.append("  and  McDefine like '%"+dto.getMcDefine()+"%'");
		    }
		return mcdefineDao.queryByPage(querySql.toString(),page,rows,McdefineDTO.class);
	}
	public List<?> qryMcdefine() {
		StringBuilder querySql =new StringBuilder("select mcdefine.* from cal_MCDefine mcdefine");
		List<?> list = mcdefineDao.queryBysql(querySql.toString(),McdefineDTO.class);
		return list;
	}
	public List<?> qryMcdefineAll() {
		List<?> list = mcdefineDao.queryBysql("select mcdefine.* from cal_MCDefine mcdefine",McdefineDTO.class);
		return list;
	}
	public Mcdefine findMcdefine(long id) {
		// TODO Auto-generated method stub
		return mcdefineDao.get(Mcdefine.class,id);
	}

}