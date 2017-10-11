package com.sinosoft.zcfz.service.reportform;

import com.sinosoft.common.InvokeResult;
import com.sinosoft.zcfz.dto.reportform.ReportCreateInfoDTO;

public interface ExportXMLService {
	public InvokeResult reportCreate(ReportCreateInfoDTO info);
}
