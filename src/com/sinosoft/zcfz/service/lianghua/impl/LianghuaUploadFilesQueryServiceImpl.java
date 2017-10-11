package com.sinosoft.zcfz.service.lianghua.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dao.reportform.UploadFilesQueryDao;
import com.sinosoft.zcfz.dto.reportform.QueryUploadFilesDTO;
import com.sinosoft.entity.CfUploadDocument;
import com.sinosoft.zcfz.service.lianghua.LianghuaUploadFilesQueryService;
import com.sinosoft.zcfz.service.reportdatacheck.impl.DownloadCheckServiceImpl;
import com.sinosoft.util.ExcelUtil;

@Service("uploadFilesQueryService")
public class LianghuaUploadFilesQueryServiceImpl implements LianghuaUploadFilesQueryService{
	@Resource
	private UploadFilesQueryDao uploadFilesQueryDao;
	QueryUploadFilesDTO rowadd=new QueryUploadFilesDTO();
	private Log log  = LogFactory.getLog(DownloadCheckServiceImpl.class);
	@Override
	public CfUploadDocument getUploadByID(String uploadNo) {
		Integer id = Integer.parseInt(uploadNo);
		return uploadFilesQueryDao.get(CfUploadDocument.class, id);
	}
	
	//业务：获取校验错误信息
	@Transactional
	public Page<?> getUploadFiles(int page, int rows, QueryUploadFilesDTO dto) {		
		//获取对应的季度快报或者报告等
		String getRepType = "";
		if(dto.getReporttype() != null && dto.getReporttype().length() > 0) {
			getRepType = "select codename from cfcodemanage where codetype='reporttype' and codecode='" + dto.getReporttype() + "'";
		
		} else {
			getRepType = "select codename from cfcodemanage where codetype='reporttype' and codecode=reportrate ";
		}
		
		//获取对应的reportname
		String getRepName = "";
		if(dto.getReportname() != null && !"".equals(dto.getReportname())){
			getRepName = "select codename from cfcodemanage where codetype='reportname' and codecode='" + dto.getReportname() + "'";
			
		}else {
			getRepName = "select codename from cfcodemanage where codetype='reportname' and codecode= reporttype ";
		}
		
		StringBuilder sql = new StringBuilder();
		sql.append("select  uploadno \"uploadNo\", ("
				+ getRepType
				+ ") \"reportType\" ,  quarter \"quarter\",("
				+ getRepName
				+ ") \"reportName\", yearmonth \"yearMonth\" , fileName  \"fileName\", filepath \"filepath\" , remark \"remark\" , operator \"operator\",operatdate \"operatdate\"   from CfUploadDocument  where 1=1 ");
		
		
		if(dto.getQuarter() != null && !"".equals(dto.getQuarter()) ){
			sql.append(" and quarter='" + dto.getQuarter() + "'");
		}
		
		if(dto.getReporttype() != null && !"".equals(dto.getReporttype()) ) {
			sql.append(" and reportrate='" + dto.getReporttype() + "'");
		}
		
		if(dto.getReportname() != null && !"".equals(dto.getReportname())) {
			sql.append(" and reporttype='" + dto.getReportname() + "'");
		}
		
		if(dto.getYear() != null && !"".equals(dto.getYear().trim()) ) {
			if(!"".equals(dto.getQuarter()) ) {
				sql.append(" and yearmonth='" + dto.getYear() + "'");
				System.out.println(dto.getYear());
			}else {
				sql.append(" and yearmonth like '" + dto.getYear().trim() + "%'");
				System.out.println("like"+dto.getYear().trim());
			}
		}
        //新加operatdate desc，按操作时间排序
		sql.append(" order by yearmonth desc,quarter desc,operatdate desc ");
		return uploadFilesQueryDao.queryByPage(sql.toString(),page,rows);
	}
	
	private String getYearMoth(QueryUploadFilesDTO dto) {
		String mQuarter = dto.getQuarter();
		String mYear = dto.getYear().trim();
		
		// 得到本期月度
		if (!mQuarter.equals("4")) {
			int month = 3 * Integer.parseInt(mQuarter);
			return mYear + "0" + new Integer(month).toString();
			
		} else {
			return mYear + 3 * Integer.parseInt(mQuarter);
		}
	}
	//上传文件清单导出
	@Override
	public void downCheckResult(HttpServletRequest request, HttpServletResponse response, String name,
			String queryConditions, String cols) {
		// TODO Auto-generated method stub
		ExcelUtil excelUtil = new ExcelUtil();
		//1.查询数据sql
		try {
			//2.将查询条件转换成DTO
			rowadd = new ObjectMapper().readValue(queryConditions.replaceAll("&quot;", "\""), QueryUploadFilesDTO.class);
			String getRepType = "";
			getRepType = "select codename from cfcodemanage where codetype='reporttype' and codecode=reportrate";
			String getRepName = "";
			getRepName = "select codename from cfcodemanage where codetype='reportname' and codecode=reporttype";
			StringBuilder sql = new StringBuilder();
			sql.append("select  uploadno \"uploadNo\", "
					+ "("+ getRepType+ ") \"reportType\" ,  "
							+ "quarter \"quarter\","
							+ "("+ getRepName + ") \"reportName\", "
									+ "yearmonth \"yearMonth\" , fileName  \"fileName\", filepath \"filepath\" , remark \"remark\" , operator \"operator\",operatdate \"operatdate\"   from CfUploadDocument  where 1=1 ");		
			List<?> jsonList = uploadFilesQueryDao.queryBysql(spliceQuerySql(rowadd, sql));				
			String datas = new ObjectMapper().writeValueAsString(jsonList);
			//3. 根据条件查询导出数据集
			excelUtil.export(request, response, name, cols, datas);
		} catch (Exception e) {
			log.error("导出失败，请查看具体原因！");
			e.printStackTrace();
		}
		
	}	
	public String spliceQuerySql(QueryUploadFilesDTO queryup,StringBuilder sql){
		StringBuilder querySql = new StringBuilder(sql);
		if(rowadd.getQuarter() != null && !"".equals(rowadd.getQuarter()) ){
			querySql.append(" and quarter='" + rowadd.getQuarter() + "'");
		}
		
		if(rowadd.getReporttype() != null && !"".equals(rowadd.getReporttype()) ) {
			querySql.append(" and reportrate='" + rowadd.getReporttype() + "'");
		}
		
		if(rowadd.getReportname() != null && !"".equals(rowadd.getReportname())) {
			querySql.append(" and reporttype='" + rowadd.getReportname() + "'");
		}
		
		if(rowadd.getYear() != null && !"".equals(rowadd.getYear().trim()) ) {
			querySql.append(" and yearmonth='" + rowadd.getYear() + "'");
		}
		//新加operatdate desc，按操作时间排序
			querySql.append(" order by yearmonth desc,quarter desc，operatdate desc ");
	
		return querySql.toString();
	}
}
