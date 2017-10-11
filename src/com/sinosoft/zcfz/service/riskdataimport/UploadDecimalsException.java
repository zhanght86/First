package com.sinosoft.zcfz.service.riskdataimport;

import java.util.ArrayList;
import java.util.List;

import com.sinosoft.zcfz.dto.reportdataimport.UploadInfoDTO;

public class UploadDecimalsException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8261862033497984191L;
	
	public List<?> objs = new ArrayList<Object>();

	public UploadDecimalsException(List<?> fac) {
		super("捕获到精度异常2。。。");
		if (fac != null && fac.size() > 0) {
			this.objs = (List<?>) fac;
		}

	}


}
