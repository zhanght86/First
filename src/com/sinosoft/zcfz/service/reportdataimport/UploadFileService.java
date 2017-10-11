package com.sinosoft.zcfz.service.reportdataimport;



import com.sinosoft.common.Page;
import com.sinosoft.dto.PluploadDTO;
import com.sinosoft.zcfz.dto.reportdataimport.UploadInfoDTO;
import com.sinosoft.zcfz.dto.reportformcompute.CalCfReportInfoDTO;
import com.sinosoft.entity.UploadFile;

public interface UploadFileService {
	public void saveUploadFile(PluploadDTO p);
	public void saveUploadFile(UploadInfoDTO p);
	public void updateUploadFile(int id,PluploadDTO p);
	public void deleteUploadFile(int id);
	public Page<?> qryUploadFile(int page,int rows,int id);
	public UploadFile findUploadFile(int id);
}
