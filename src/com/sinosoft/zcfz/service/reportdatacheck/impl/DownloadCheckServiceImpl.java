package com.sinosoft.zcfz.service.reportdatacheck.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinosoft.dao.DownloadCheckDao;
import com.sinosoft.dto.DownloadCheckDTO;
import com.sinosoft.zcfz.service.reportdatacheck.DownloadCheckService;
import com.sinosoft.util.ExcelUtil;



@Service
public class DownloadCheckServiceImpl implements DownloadCheckService{
	
@Resource
DownloadCheckDao downloadCheckDao;

DownloadCheckDTO downloadCheckDTO = new DownloadCheckDTO();

private Log log  = LogFactory.getLog(DownloadCheckServiceImpl.class);

	@Override
	public void downCheckResult(HttpServletRequest request,
			HttpServletResponse response, String name, DownloadCheckDTO dto,
			String cols) {
		ExcelUtil excelUtil = new ExcelUtil();
		try {
			StringBuilder sql = new StringBuilder();
			String getRepType = "";
			if (dto.getCheckedCode() != null
					&& dto.getReportType().length() > 0) {
				getRepType = "select codename from cfcodemanage where codetype='reporttype' and codecode='"
						+ dto.getReportType() + "'";

			} else {
				getRepType = "select codename from cfcodemanage where codetype='reporttype' and codecode= reportType ";
			}
			sql.append("select ("
					+ getRepType
					+ ") \"reportType\",year \"year\", quarter \"quarter\",comCode \"comCode\", checkedCode \"checkedCode\",temp \"temp\",errorInfo \"errorInfo\" ,tolerance \"tolerance\", to_char(checkdate,'yyyy-mm-dd') \"checkdate\"  from CfReportErrInfo  where 1=1 ");
			if (dto != null) {
				if (StringUtils.isNotEmpty(dto.getReportType())) {
				sql.append(" and reportType='"
 + dto.getReportType() + "'");
			}
				if (StringUtils.isNotEmpty(dto.getYear())) {
				sql.append(" and year='" + dto.getYear() + "'");
			}
				if (StringUtils.isNotEmpty(dto.getQuarter())) {
				sql.append(" and quarter='" + dto.getQuarter()
						+ "'");
			}
				if (StringUtils.isNotEmpty(dto.getCheckedCode())) {
				sql.append(" and checkedCode='"
 + dto.getCheckedCode() + "'");
			}
			}
			List<?> queryBysql = downloadCheckDao.queryBysql(sql.toString());
			String datas = new ObjectMapper().writeValueAsString(queryBysql);
			excelUtil.export(request, response, name, cols, datas);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}
@Transactional
public void downLoad1(HttpServletRequest request,
			HttpServletResponse response, String name, String queryConditions,
			String cols) {
		ExcelUtil excelUtil = new ExcelUtil();
		//1.查询数据sql
		String sql = "select t.calformula \"calFormula\",t.remark \"remark\", t.caltype \"calType\",c.codename \"isNeedChk\",t.tolerance \"tolerance\" from cfreportdatacheck t left join cfcodemanage c on c.codetype = 'reporttype' and c.codecode = t.isneedchk where 1=1 ";
		//DownloadCheckDTO downloadCheckDTO = new DownloadCheckDTO();;
		try {
			//2.将查询条件转换成DTO
			downloadCheckDTO = new ObjectMapper().readValue(queryConditions.replaceAll("&quot;", "\""), DownloadCheckDTO.class);
			List<?> jsonList = downloadCheckDao.queryBysql(spliceQuerySql1(downloadCheckDTO, sql));
			String datas = new ObjectMapper().writeValueAsString(jsonList);
			//3. 根据条件查询导出数据集
			excelUtil.export(request, response, name, cols, datas);
		} catch (Exception e) {
			log.error("导出失败，请查看具体原因！");
			e.printStackTrace();
		}
	}
	
@Transactional
public void downLoad2(HttpServletRequest request,
			HttpServletResponse response, String name, String queryConditions,
			String cols) {
		ExcelUtil excelUtil = new ExcelUtil();
		//1.查询数据sql
		String sql = "select t.tablecode \"tableCode\",t.calformula \"calFormula\",t.remark \"remark\", t.caltype \"calType\",c.codename \"isNeedChk\",t.tolerance \"tolerance\" from cfreportrowdatacheck t left join cfcodemanage c on c.codetype = 'reporttype' and c.codecode = t.isneedchk where 1=1  ";
		//DownloadCheckDTO downloadCheckDTO = new DownloadCheckDTO();;
		try {
			//2.将查询条件转换成DTO
			downloadCheckDTO = new ObjectMapper().readValue(queryConditions.replaceAll("&quot;", "\""), DownloadCheckDTO.class);
			List<?> jsonList = downloadCheckDao.queryBysql(spliceQuerySql2(downloadCheckDTO, sql));
			String datas = new ObjectMapper().writeValueAsString(jsonList);
			//3. 根据条件查询导出数据集
			excelUtil.export(request, response, name, cols, datas);
		} catch (Exception e) {
			log.error("导出失败，请查看具体原因！");
			e.printStackTrace();
		}
	}
@Transactional
public void downLoad3(HttpServletRequest request,
			HttpServletResponse response, String name, String queryConditions,
			String cols) {
		ExcelUtil excelUtil = new ExcelUtil();
		//1.查询数据sql
		String sql = "select t.reportitemcode \"reportItemCode\",t.tablecode \"tableCode\",t.colcode \"colCode\",t.remark  \"remark\", t.caltype \"calType\",c.codename \"isNeedChk\",t.tolerance \"tolerance\" from cfreportitemrowcheck t left join cfcodemanage c on c.codetype = 'reporttype' and c.codecode = t.isneedchk where 1=1 ";
		//DownloadCheckDTO downloadCheckDTO = new DownloadCheckDTO();
		try {
			//2.将查询条件转换成DTO
			downloadCheckDTO = new ObjectMapper().readValue(queryConditions.replaceAll("&quot;", "\""), DownloadCheckDTO.class);
			List<?> jsonList = downloadCheckDao.queryBysql(spliceQuerySql2(downloadCheckDTO, sql));
			String datas = new ObjectMapper().writeValueAsString(jsonList);
			//3. 根据条件查询导出数据集
			excelUtil.export(request, response, name, cols, datas);
		} catch (Exception e) {
			log.error("导出失败，请查看具体原因！");
			e.printStackTrace();
		}
	}
@Transactional
public void downLoad4(HttpServletRequest request,
			HttpServletResponse response, String name, String queryConditions,
			String cols) {
		ExcelUtil excelUtil = new ExcelUtil();
		//1.查询数据sql
		String sql = "select t.itemcodebq \"itemCodeBq\",t.itemcodesq \"itemCodeSq\",t.remark \"remark\", t.caltype \"calType\",c.codename \"isNeedChk\",t.tolerance \"tolerance\",t.relationOperator \"relationOperator\" from cfreportperioddatacheck t left join cfcodemanage c on c.codetype = 'reporttype' and c.codecode = t.isneedchk where 1=1 ";
		//DownloadCheckDTO downloadCheckDTO = new DownloadCheckDTO();;
		try {
			//2.将查询条件转换成DTO
			downloadCheckDTO = new ObjectMapper().readValue(queryConditions.replaceAll("&quot;", "\""), DownloadCheckDTO.class);
			List<?> jsonList = downloadCheckDao.queryBysql(spliceQuerySql4(downloadCheckDTO, sql));
			String datas = new ObjectMapper().writeValueAsString(jsonList);
			//3. 根据条件查询导出数据集
			excelUtil.export(request, response, name, cols, datas);
		} catch (Exception e) {
			log.error("导出失败，请查看具体原因！");
			e.printStackTrace();
		}
	}
	/**
	 * 拼接查询条件
	 * @param 
	 * @param sql
	 * @return
	 */
	public String spliceQuerySql1(DownloadCheckDTO downloadCheckDTO, String sql) {
		StringBuilder querySql = new StringBuilder(sql);
		querySql.append("  order by t.calformula");
		return querySql.toString();
	}
	
	public String spliceQuerySql2(DownloadCheckDTO downloadCheckDTO, String sql) {
		StringBuilder querySql = new StringBuilder(sql);
		querySql.append("  order by t.tablecode");
		return querySql.toString();
	}
	
	
	public String spliceQuerySql4(DownloadCheckDTO downloadCheckDTO, String sql) {
		StringBuilder querySql = new StringBuilder(sql);
		querySql.append("  order by t.itemcodebq");
		return querySql.toString();
	}
}
