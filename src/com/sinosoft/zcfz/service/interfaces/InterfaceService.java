package com.sinosoft.zcfz.service.interfaces;

import com.sinosoft.zcfz.dto.interfase.ImpDataInfoDTO;

public interface InterfaceService {
	public boolean saveFinaData(ImpDataInfoDTO info) throws Exception;
	public boolean saveInvestData(ImpDataInfoDTO info) throws Exception;
	public String[] dealInterfaceData(String reportid, String impdate);
}
