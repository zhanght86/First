package com.sinosoft.abc.service;


import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dto.reportdataimport.UploadInfoDTO;


/**
 * 农银对接业务，包括财务和投资，精算是导入模板
 * 
 * @author changWen
 */
public interface ABC_FinanceService {

	void updateState(UploadInfoDTO dto,String state);
	void isHasReportId(UploadInfoDTO dto);
	//方案一：使用模板导入
//	boolean financeModelImport(UploadInfoDTO dto);
	 
	//方案二：
	//财务数据导入
	boolean financeImport(UploadInfoDTO dto);	
	//科目表数据导入
    boolean subjectImport(UploadInfoDTO dto);
    
    ////初步 计算CR19单个文件，无报送号）
    boolean singleCalCR19(String year, String quarter);
    
    //初步 计算CR19(多文件，也就是有报送号）
    boolean singleCalCR19(String year, String quarter,String id);

    Page<?> getInfo(int page,int rows, UploadInfoDTO dto);

}
