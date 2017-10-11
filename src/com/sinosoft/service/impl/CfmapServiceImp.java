package  com.sinosoft.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import  com.sinosoft.common.Page;
import  com.sinosoft.dao.CfmapDao;
import  com.sinosoft.dto.CfmapDTO;
import  com.sinosoft.entity.Cfmap;
import  com.sinosoft.service.CfmapService;

@Service
public class CfmapServiceImp implements CfmapService{
	@Resource
	private CfmapDao cfmapDao;
	@Transactional
	public void saveCfmap(CfmapDTO dto) {
		// TODO Auto-generated method stub
		Cfmap br=new Cfmap();
	    br.setMapid(dto.getMapid());
	    br.setOriname(dto.getOriname());
	    br.setSysname(dto.getSysname());
		cfmapDao.save(br);
	}
	@Transactional
	public void updateCfmap(long id, CfmapDTO dto) {
		// TODO Auto-generated method stub
		Cfmap br =cfmapDao.get(Cfmap.class,id);
	    br.setMapid(dto.getMapid());
	    br.setOriname(dto.getOriname());
	    br.setSysname(dto.getSysname());
		cfmapDao.update(br);
	}
	@Transactional
	public void deleteCfmap(String[] idArr) {
		// TODO Auto-generated method stub
		Long[] ids=new Long[idArr.length];
		for(int i=0;i<idArr.length;i++){
			ids[i]=Long.parseLong(idArr[i]);
		}
		cfmapDao.removes(ids,Cfmap.class);
	}
	public Page<?> qryCfmapOfPage(int page, int rows,CfmapDTO dto) {
		  StringBuilder querySql =new StringBuilder("select cfmap.* from Cfmap cfmap where 1=1");
			if(!(dto.getMapid()==null||"".equals(dto.getMapid()))){
	    	    querySql.append("  and  Mapid like '%"+dto.getMapid()+"%'");
		    }
			if(!(dto.getOriname()==null||"".equals(dto.getOriname()))){
	    	    querySql.append("  and  Oriname like '%"+dto.getOriname()+"%'");
		    }
			if(!(dto.getSysname()==null||"".equals(dto.getSysname()))){
	    	    querySql.append("  and  Sysname like '%"+dto.getSysname()+"%'");
		    }
		return cfmapDao.queryByPage(querySql.toString(),page,rows,CfmapDTO.class);
	}
	public List<?> qryCfmap() {
		StringBuilder querySql =new StringBuilder("select cfmap.* from Cfmap cfmap");
		List<?> list = cfmapDao.queryBysql(querySql.toString(),CfmapDTO.class);
		return list;
	}
	public List<?> qryCfmapAll() {
		List<?> list = cfmapDao.queryBysql("select cfmap.* from Cfmap cfmap",CfmapDTO.class);
		return list;
	}
	public Cfmap findCfmap(long id) {
		// TODO Auto-generated method stub
		return cfmapDao.get(Cfmap.class,id);
	}

}