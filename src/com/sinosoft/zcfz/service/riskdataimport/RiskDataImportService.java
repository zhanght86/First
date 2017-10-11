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
public interface RiskDataImportService {
	
	
	/**
	 * 判断cfreportdata, cfreportrowadddata是否存在相应年度和季度数据
	 * @param month 年度
	 * @param quarter 季度
	 * @param deptNo 部门编号
	 * @return
	 */
	//public boolean isHasData(String year, String quarter, String reportrate, String reporttype, String deptNo);
	
	/**
	 * 返回新文件地址
	 * @param year
	 * @param quarter
	 * @param modePath 模板路径
	 * @param modelType 模板类型 0-空模板 1-数据模板
	 * @return
	 */
	public void downloadPath(String year, String quarter, String reportrate, String reporttype, String deptNo, String modelPath, String modelType,String reportId);
	
	/**
	 * 保存上传文件数据
	 * @param pld
	 * @throws UploadNumberFormatException
	 * @throws IOException
	 */
	public void saveData(UploadInfoDTO pld)throws UploadNumberFormatException,IOException;
}
