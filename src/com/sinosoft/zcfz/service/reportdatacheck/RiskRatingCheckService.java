package com.sinosoft.zcfz.service.reportdatacheck;

import java.util.List;

import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dto.reportdatacheck.DataCheckDto;

public interface RiskRatingCheckService {
	public boolean dataCheckOperator (DataCheckDto dataCheckDto);
	
	public List<?> getErrorInfo(DataCheckDto dataCheckDto);
	public Page<?> getErrorInfo(int page, int rows, DataCheckDto dataCheckDto);
}
