package com.sinosoft.zcfz.service.reportdatamamage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestParam;
import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dto.reportdatamamage.CfReportDTO;
import com.sinosoft.zcfz.dto.reportdatamamage.ReportHistoryDTO;
import com.sinosoft.entity.CfReportDataId;

public interface CfReportDataService {
	public  Page<?>   qryIndexInfo(int page,int rows,CfReportDTO  dto);
	public  void      update(CfReportDataId id,CfReportDTO dto)throws Exception;
	public  void      insertTextBlock(CfReportDTO dto)throws Exception;
	public  void      deleteTextBlock(CfReportDTO dto)throws Exception;
	public  String      getTextBlock(CfReportDTO dto);
	public  Page<?>   reportHistory(@RequestParam int page,@RequestParam int rows,ReportHistoryDTO  dto);
	//固定因子数据管理导出
	public void downLoad(HttpServletRequest request, HttpServletResponse response,
			 String name, String queryConditions, String cols);
	
	
	
}
