package com.sinosoft.zcfz.service.reportform;

import java.io.IOException;
import com.sinosoft.zcfz.dto.reportform.CfReportDownloadInfoDTO;

public interface CfReportDownloadService {
	public void reportDownload(CfReportDownloadInfoDTO info) throws IOException,NullPointerException;
}
