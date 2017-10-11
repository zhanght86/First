package com.sinosoft.zcfz.service.impl.reportformcompute;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dao.reportformcompute.RhoMatrixDefineDao;
import com.sinosoft.zcfz.dto.reportformcompute.RhoMatrixDefineDTO;
import com.sinosoft.entity.RhoMatrixDefine;
import com.sinosoft.zcfz.service.reportformcompute.RhoMatrixDefineService;

@Service
public class RhoMatrixDefineServiceImp implements RhoMatrixDefineService{
	@Resource
	private RhoMatrixDefineDao rhoMatrixDefineDao;
	@Transactional
	public void saveRhoMatrixDefine(RhoMatrixDefineDTO dto) {
		// TODO Auto-generated method stub
		RhoMatrixDefine br=new RhoMatrixDefine();
	    br.setRhomatrixCode(dto.getRhomatrixCode());
	    br.setRhomatrixName(dto.getRhomatrixName());
	    br.setReportitemcode(dto.getReportitemcode());
	    br.setColumnLine(dto.getColumnLine());
	    br.setRhomatrixValue(dto.getRhomatrixValue());
	    br.setRemark(dto.getRemark());
		rhoMatrixDefineDao.save(br);
	}
	@Transactional
	public void updateRhoMatrixDefine( RhoMatrixDefineDTO dto) {
		RhoMatrixDefine br =rhoMatrixDefineDao.get(RhoMatrixDefine.class,dto.getRhomatrixCode());
//		System.out.println(dto.getRhomatrixName());
//		System.out.println(dto.getReportitemcode());
	    br.setRhomatrixName(dto.getRhomatrixName());
	    br.setReportitemcode(dto.getReportitemcode());
	    br.setColumnLine(dto.getColumnLine());
	    br.setRhomatrixValue(dto.getRhomatrixValue());
		rhoMatrixDefineDao.update(br);
	}
	@Transactional
	public void deleteRhoMatrixDefine(String[] idArr) {
		// TODO Auto-generated method stub
		rhoMatrixDefineDao.removes(idArr,RhoMatrixDefine.class);
	}
	public Page<?> qryRhoMatrixDefineOfPage(int page, int rows,RhoMatrixDefineDTO dto) {
		  StringBuilder querySql =new StringBuilder("select rhoMatrixDefine.* from cal_RhoMatrixDefine rhoMatrixDefine where 1=1");
			if(!(dto.getRhomatrixCode()==null||"".equals(dto.getRhomatrixCode()))){
	    	    querySql.append("  and  RhomatrixCode like '%"+dto.getRhomatrixCode()+"%'");
		    }
			if(!(dto.getRhomatrixName()==null||"".equals(dto.getRhomatrixName()))){
	    	    querySql.append("  and  RhomatrixName like '%"+dto.getRhomatrixName()+"%'");
		    }
			if(!(dto.getReportitemcode()==null||"".equals(dto.getReportitemcode()))){
	    	    querySql.append("  and  Reportitemcode like '%"+dto.getReportitemcode()+"%'");
		    }
			if(!(dto.getColumnLine()==null||"".equals(dto.getColumnLine()))){
	    	    querySql.append("  and  ColumnLine like '%"+dto.getColumnLine()+"%'");
		    }
			if(!(dto.getRhomatrixValue()==null||"".equals(dto.getRhomatrixValue()))){
	    	    querySql.append("  and  RhomatrixValue like '%"+dto.getRhomatrixValue()+"%'");
		    }
			if(!(dto.getRemark()==null||"".equals(dto.getRemark()))){
	    	    querySql.append("  and  Remark like '%"+dto.getRemark()+"%'");
		    }
		return rhoMatrixDefineDao.queryByPage(querySql.toString(),page,rows,RhoMatrixDefineDTO.class);
	}
	public List<?> qryRhoMatrixDefine() {
		StringBuilder querySql =new StringBuilder("select rhoMatrixDefine.* from cal_RhoMatrixDefine rhoMatrixDefine");
		List<?> list = rhoMatrixDefineDao.queryBysql(querySql.toString(),RhoMatrixDefineDTO.class);
		return list;
	}
	public List<?> qryRhoMatrixDefineAll() {
		List<?> list = rhoMatrixDefineDao.queryBysql("select rhoMatrixDefine.* from cal_RhoMatrixDefine rhoMatrixDefine",RhoMatrixDefineDTO.class);
		return list;
	}
	public RhoMatrixDefine findRhoMatrixDefine(long id) {
		// TODO Auto-generated method stub
		return rhoMatrixDefineDao.get(RhoMatrixDefine.class,id);
	}

}