package com.sinosoft.zcfz.service.glnlpg;

import java.util.List;

import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dto.glnlpg.Alm_AdjPlanDTO;


public interface Alm_AdjPlanService {

	public List<?> listAdjPlan();

	public void edit(Alm_AdjPlanDTO sev);

	public void submit(String[] idArr);

	public void send();

	public Page<?> listplan(int page, int rows, Alm_AdjPlanDTO dto, String dept);

	public Page<?> listpage(int page, int rows);

	public void tongguo(String[] idArr);

	public void fanhuixiugai(Alm_AdjPlanDTO srd);

	
}