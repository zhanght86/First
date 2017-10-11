package com.sinosoft.zcfz.service.interfase;

import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dto.interfase.CUX_SINO_INTERFACE_DTO;
import com.sinosoft.zcfz.dto.interfase.Cux_Sino_Interface_InvestDTO;
import com.sinosoft.zcfz.dto.interfase.ImpDataInfoDTO;

public interface Cux_Sino_Interface_Service {
    public Page<?> queryByPage(int page,int rows,CUX_SINO_INTERFACE_DTO  dto);
    public Page<?> queryByPage(int page,int rows,Cux_Sino_Interface_InvestDTO  dto);
	public boolean saveInterfaceData(ImpDataInfoDTO info) throws Exception;
	public String[] dealInterfaceData(String year, String quarter, String year_month, String impdate);
}
