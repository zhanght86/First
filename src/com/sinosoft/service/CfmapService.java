package  com.sinosoft.service;  

import java.util.List;
import  com.sinosoft.common.Page;
import  com.sinosoft.dto.CfmapDTO;
import  com.sinosoft.entity.Cfmap;

public interface CfmapService {  
    public void saveCfmap(CfmapDTO dto);
	public void updateCfmap(long id,CfmapDTO dto);
	public void deleteCfmap(String[] idArr);
	public Page<?> qryCfmapOfPage(int page,int rows,CfmapDTO dto);
	public List<?> qryCfmap();
	public List<?> qryCfmapAll();
	public Cfmap findCfmap(long id);
}