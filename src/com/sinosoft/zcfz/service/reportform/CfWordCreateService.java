package com.sinosoft.zcfz.service.reportform;

import java.io.File;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sinosoft.zcfz.dto.reportform.CfWordCreateDTO;

public interface CfWordCreateService {
	public boolean CreateWord(CfWordCreateDTO cfdto);
	
	public Map<String, Object> CreateWordByFreeMarker(CfWordCreateDTO cfdto);
	public Map<String, Object> CreateWordByFreeMarker1(CfWordCreateDTO cfdto);
	public boolean downloadWord(CfWordCreateDTO cfdto,HttpServletResponse resp,HttpServletRequest req);
	public boolean downloadWord(File file,HttpServletResponse response,HttpServletRequest request);
}
