package com.sinosoft.abc.service;


import com.sinosoft.abc.dto.InvestQueryDto_ABC;
import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dto.reportdataimport.UploadInfoDTO;

/**
 * 农银投资数据表处理业务
 * 
 * @author changWen
 */
public interface ABC_InvestService {

	boolean updateState(UploadInfoDTO dto,String state);
	
	//上传投资表的《MR09-股票》表
	boolean MR09Import(UploadInfoDTO dto);
	
	//上传投资表的《MR12-基金》表
	boolean MR12Import(UploadInfoDTO dto);
	
	//上传投资表的《CR01-政策性金融债 》 《CR01-其他债券》表
	boolean CR01Import(UploadInfoDTO dto);
		
	//上传投资表的《CR05-企业债 》表
	boolean CR05Import(UploadInfoDTO dto);
	
	//上传投资表的《CR10-保险资管产品》表
	boolean CR10Import(UploadInfoDTO dto);

	
    Page<?> getInfo(int page,int rows, InvestQueryDto_ABC dto);

}
