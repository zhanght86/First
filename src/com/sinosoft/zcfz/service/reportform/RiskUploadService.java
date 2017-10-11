package com.sinosoft.zcfz.service.reportform;

import java.io.IOException;

import com.sinosoft.zcfz.dto.reportdataimport.UploadInfoDTO;
import com.sinosoft.entity.UserInfo;
import com.sinosoft.util.UploadNumberFormatException;

public interface RiskUploadService {
	
	public static final String STR = "不适用";

	public void saveData(UploadInfoDTO pld, UserInfo user, String department) throws UploadNumberFormatException,IOException,Exception;
    public void downloadPath(UploadInfoDTO pld, String dept,String comcode, String newFilePath, String param, String deptflag);
}
