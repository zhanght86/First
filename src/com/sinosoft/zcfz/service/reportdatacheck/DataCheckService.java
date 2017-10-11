package com.sinosoft.zcfz.service.reportdatacheck;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dto.reportdatacheck.CheckRelationDTO;
import com.sinosoft.zcfz.dto.reportdatacheck.DataCheckDto;
import com.sinosoft.entity.UserInfo;

/**
 * 数据校验的业务
 */
public interface DataCheckService {
	//初步检验
	boolean initialCheck(DataCheckDto dataCheckDto);
	// 数据校验
	public boolean dataCheck(DataCheckDto dataCheckDto);
	
	public void updateState(DataCheckDto dataCheckDto,String state);
	
	// 获取校验错误信息
	public Page<?> getErrorInfo(int page, int rows, DataCheckDto dataCheckDto);
	// 根据条件获取校验错误信息
	public List<?> getErrorInfoByCondition(DataCheckDto dataCheckDto);
	
	// 校验关系管理
	public Page<?> getCheckRelationshipInfo(int page, int rows, CheckRelationDTO dto);
	public boolean deleteCheckRelaInfo(String[] idArr, String checkSchemaCode);
	public boolean saveCheckRelaInfo(CheckRelationDTO dto);
	public boolean updateCheckRelaInfo(int id,CheckRelationDTO dto);
	// 根据计算规则得到RF0指标数据---行可扩展的
	public Map<String, BigDecimal> GetRF0Data(String id,UserInfo user,String reporttypes) throws Exception;
	// 根据计算规则得到RF0指标数据---固定因子的
	public Map<String, BigDecimal> GetRF0GDData(String id,UserInfo user,String reporttypes) throws Exception;
	//根据报送号筛选当前数据库中的数据--行可扩展的
	public Map<String, BigDecimal> GetDatas(String id) throws Exception;
	//根据报送号筛选当前数据库中的数据--固定因子的
		public Map<String, BigDecimal> GetGDDatas(String id) throws Exception;
	//用来校验两个Map中数据是否一致---行可扩展的
	public boolean checkRF0Data(Map<String, BigDecimal> rowDatas,Map<String, BigDecimal> alDatas, DataCheckDto dataCheckDto) throws Exception;
	//用来校验两个Map中数据是否一致---固定因子的
	public boolean checkRF0GDData(Map<String, BigDecimal> rowDatas,Map<String, BigDecimal> alDatas, DataCheckDto dataCheckDto) throws Exception;

	//记录错误信息
	public boolean PrepareErrLog(DataCheckDto dataCheckDto,String errInfo);
}
