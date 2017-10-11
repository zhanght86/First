package com.sinosoft.zcfz.service.interfase;

import java.util.List;

import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dto.interfase.AccountInterfaceDTO;

public interface AccountInterfaceService {
	 public Page<?> queryByPage(int page,int rows,AccountInterfaceDTO  dto);
	/**
	* 投资新增科目校验
	*/
	public List<?> investItemCheck(String year, String quarter, String yearmonth, String datadate)throws Exception;
	/**
	* 投资新增科目一键维护
	 * @throws Exception 
	*/
	public void investItemHandset(String year, String quarter, String yearmonth, String datadate) throws Exception;
}
