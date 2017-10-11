package com.sinosoft.zcfz.service.reportdatacheck;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sinosoft.dto.DownloadCheckDTO;



public interface DownloadCheckService {
	
	/**
	 * 导出符合条件的表的全部公式
	 * @param request
	 * @param response
	 * @param name 导出excel表格名称
	 * @param queryConditions 导出条件
	 * @param cols 列名称
	 */
	public void downLoad1(HttpServletRequest request, HttpServletResponse response,
		 String name, String queryConditions, String cols);
	public void downLoad2(HttpServletRequest request, HttpServletResponse response,
			 String name, String queryConditions, String cols);
	public void downLoad3(HttpServletRequest request, HttpServletResponse response,
			 String name, String queryConditions, String cols);
	public void downLoad4(HttpServletRequest request, HttpServletResponse response,
			 String name, String queryConditions, String cols);

	/**
	 * 导出校验结果
	 * 
	 * @param request
	 * @param response
	 * @param name
	 * @param dto
	 * @param cols
	 */
	public void downCheckResult(HttpServletRequest request,
			HttpServletResponse response, String name, DownloadCheckDTO dto,
			String cols);
}
