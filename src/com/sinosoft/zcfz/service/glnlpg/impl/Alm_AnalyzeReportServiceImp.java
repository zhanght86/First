package com.sinosoft.zcfz.service.glnlpg.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.sinosoft.zcfz.dao.glnlpg.Alm_ComputeResultDao;
import com.sinosoft.zcfz.dto.glnlpg.Alm_GatherInfoDTO;
import com.sinosoft.zcfz.service.glnlpg.Alm_AnalyzeReportService;

@Service
public class Alm_AnalyzeReportServiceImp implements Alm_AnalyzeReportService {
	private Logger LOGGER= Logger.getLogger(Alm_AnalyzeReportServiceImp.class);
	@Resource
	private Alm_ComputeResultDao sev_crd;
	@Override
	public Map<?, ?> qryAnalyzeData(Alm_GatherInfoDTO dto) {
		// TODO Auto-generated method stub
		Map<String, Object> result = new HashMap<String, Object>();
		
		String sql = "select c.codename  \"itemname\", t.rate \"rate\" from codemanage c left join sev_gatherinfo t on c.codecode = t.projectcode and c.codetype='evaluateSearchType' and c.codecode not in ('p00','p01','p10')  where t.projectcode is not null and t.type = 1 and t.adjusttype = 'at1' and t.reportid = " + dto.getReportId();
		try{
			List<?> list = sev_crd.queryBysql(sql);
			if(list != null && list.size() > 0 && !list.isEmpty()){
				String[] categories = new String[list.size()];
				Double[] data = new Double[list.size()];
				for(int i = 0; i < list.size(); i++){
					Map map = (Map) list.get(i);
					categories[i] = map.get("itemname").toString();
					data[i] = map.get("rate") == null ? 0 : (Double.parseDouble(map.get("rate").toString()) * 100);
				}
				result.put("categories", categories);
				result.put("data", data);	
			}
		}catch(Exception e){
			e.printStackTrace();
			LOGGER.error("评估结果分析报告取数异常：", e);
			return result;
		}
		return result;
	}

}
