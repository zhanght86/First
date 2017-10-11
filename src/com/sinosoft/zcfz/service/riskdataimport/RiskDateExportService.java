package com.sinosoft.zcfz.service.riskdataimport;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.sinosoft.zcfz.dto.reportdataimport.UploadInfoDTO;
import com.sinosoft.util.UploadNumberFormatException;
/**
 * @author hanchuanxing
 * @date 2016年5月28日 下午6:12:47
 */
public interface RiskDateExportService {
	
	/**
	 * 保存上传文件数据
	 * @param pld
	 * @throws UploadNumberFormatException
	 * @throws IOException
	 */
	public void saveData(UploadInfoDTO pld)throws UploadNumberFormatException,IOException;

	
	public void downloadPath(String year, String quarter, String reportrate, String reporttype, String modelPath,String reportId);
}
