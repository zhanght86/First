package com.sinosoft.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UploadNumberFormatException extends NumberFormatException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8261862033497984191L;
	
	public List<?> objs = new  ArrayList<Object>();
	
	public UploadNumberFormatException(List<?> uploadInfoDTOs){
		super("捕获到自定义异常。。。");
		this.objs = uploadInfoDTOs;
	}

}
