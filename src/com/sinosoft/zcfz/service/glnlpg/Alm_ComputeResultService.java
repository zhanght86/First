package com.sinosoft.zcfz.service.glnlpg;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sinosoft.zcfz.dto.glnlpg.Alm_GatherInfoDTO;
public interface Alm_ComputeResultService {

	public List<?> qryComputeResult(String projectCode,String reportId);

	public String verify(String reportId);

	public void calculation(String reportId);

	public List<?> listnew(Alm_GatherInfoDTO sg);

	public List<?> listdept(Alm_GatherInfoDTO sg);

	public void download(HttpServletRequest request,HttpServletResponse response, String reportId);

	public String get();

	public String getyear();
	
}