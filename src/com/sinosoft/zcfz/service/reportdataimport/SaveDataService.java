package com.sinosoft.zcfz.service.reportdataimport;

import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;

import com.sinosoft.zcfz.dto.reportdataimport.UploadInfoDTO;
import com.sinosoft.util.UploadNumberFormatException;



public interface SaveDataService {
	public static final String STR = "不适用";
	
    public void saveDataA01(List<Row> list,UploadInfoDTO pld)throws UploadNumberFormatException,Exception;
    public void saveDataA02(List<Row> list,UploadInfoDTO pld)throws UploadNumberFormatException,Exception;
    public void saveData(UploadInfoDTO pld)throws UploadNumberFormatException,IOException,Exception;
}
