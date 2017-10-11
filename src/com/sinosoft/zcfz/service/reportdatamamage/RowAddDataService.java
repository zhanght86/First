package com.sinosoft.zcfz.service.reportdatamamage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dto.reportdatamamage.RowAddDTO;
import com.sinosoft.entity.CfReportRowAddDataId;

public interface RowAddDataService {
	public Page<?> qryIndexInfo(int page,int rows,RowAddDTO dto);
	public  void  update(CfReportRowAddDataId id,RowAddDTO dto);
	public Page<?> rowAddHistory(int page, int rows, RowAddDTO dto);
	//行可扩展数据管理导出
		public void downloadLine(HttpServletRequest request, HttpServletResponse response,
				 String name, String queryConditions, String cols);
}
