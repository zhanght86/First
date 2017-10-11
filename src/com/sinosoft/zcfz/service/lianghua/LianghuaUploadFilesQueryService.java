package com.sinosoft.zcfz.service.lianghua;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dto.reportform.QueryUploadFilesDTO;
import com.sinosoft.entity.CfUploadDocument;

public interface LianghuaUploadFilesQueryService {
	//查看文件上传信息
	public Page<?> getUploadFiles(int page, int rows, QueryUploadFilesDTO dto);

	// 根据Id查询单个文件上传的信息
	public CfUploadDocument getUploadByID(String uploadNo);
	
	public void downCheckResult(HttpServletRequest request, HttpServletResponse response,
			 String name, String queryConditions, String cols);
}
