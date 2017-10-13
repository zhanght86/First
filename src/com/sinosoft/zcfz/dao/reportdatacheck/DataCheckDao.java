package com.sinosoft.zcfz.dao.reportdatacheck;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.sinosoft.abc.entity.Invest_ABC;
import com.sinosoft.common.Dao;
import com.sinosoft.dao.BaseAbstractDao;
import com.sinosoft.zcfz.dto.reportdatacheck.DataCheckDto;
import com.sinosoft.entity.CfReportDataCheck;
import com.sinosoft.entity.CfReportErrInfo;
import com.sinosoft.entity.CfReportItemRowCheck;
import com.sinosoft.entity.CfReportPeriodDataCheck;
import com.sinosoft.entity.CfReportRowAddData;
import com.sinosoft.entity.CfReportRowDataCheck;
import com.sinosoft.entity.CfRiskRatingCheck;
import com.sinosoft.entity.CfRiskRatingErrInfo;

@Repository
public class DataCheckDao extends BaseAbstractDao {
	@Resource(name = "dao")
	private Dao dao;

	/**
	 * 批量删除初步检验错误信息
	 */
	public boolean deleteInitErrInfo(DataCheckDto dataCheckDto) {
		String sql = "delete from CfReportErrInfo where year='" + dataCheckDto.getYear() + "' and quarter='"
				+ dataCheckDto.getQuarter() + "'" + " and reportType='2'";

		dao.excute(sql);
		return true;
	}
	
	// 初步校验
	public List<Invest_ABC> getMR09( String year, String quarter) {
		String sql = " from Invest_ABC where tablecode like 'MR09%' " 
			    + " and QUARTER = '" + quarter + "' " + "  AND year = '" + year + "' order by serialNo" ;
		
	    return (List<Invest_ABC>) dao.query(sql);
	}
	
	//初步校验
	public List<CfReportRowAddData> getMR20F00004( String year, String quarter, String reportRate) {
		String sql = " from CfReportRowAddData where tablecode like 'MR20%' and colcode like '%F00004'" 
			    + " and QUARTER = '" + quarter + "' " + "  AND YEARMONTH = '" + year + "' "
				+ "  AND REPORTRATE = '" + reportRate + "' order by TO_NUMBER(rowno)" ;
		
	    return (List<CfReportRowAddData>) dao.query(sql);
	}
	
	// 初步校验
	public List<CfReportRowAddData> getMR09F00007( String year, String quarter, String reportRate) {
		String sql = " from CfReportRowAddData where tablecode like 'MR09%' and colcode like '%F00007'" 
			    + " and QUARTER = '" + quarter + "' " + "  AND YEARMONTH = '" + year + "' "
				+ "  AND REPORTRATE = '" + reportRate + "' order by TO_NUMBER(rowno)" ;
		
	    return (List<CfReportRowAddData>) dao.query(sql);
	}
	/***************************检验信用评级、修正久期与基础因子RFO**************************/
	public List<CfReportRowAddData> getItemValue(String reportId, String mYearMonth_Bq, String mQuarter, 
			String mReportRate,String colcode) {
		String sql = " from CfReportRowAddData " 
			    + "where  reportId = '" + reportId + "' and  QUARTER = '" + mQuarter + "' " + "  AND YEARMONTH = '" + mYearMonth_Bq + "' "
				+ "  AND REPORTRATE = '" + mReportRate + "' "  + " and colcode= '" + colcode +"' order by TO_NUMBER(rowno)" ;
		
	    return (List<CfReportRowAddData>) dao.query(sql);
	}
	
	/**
	 * 获取因子指标数据
	 */
	public List<?> getFactorIndexData(String reportId, String reportRate, String yearMonth, String quarter, String comCode) {
		String sql = "select  outitemcode, decode(round(reportitemvalue, decimals) ,null, 0,round(reportitemvalue, decimals)) reportitemvalue   from cfreportdata join cfreportelement  on outitemcode = portitemcode"
			 		+ " where  reportid='"+reportId+"'  and reportRate='" + reportRate + "' and month='" + yearMonth 
					+ "' and quarter='" + quarter + "' and comCode='" + comCode + "' and reportItemValue is not null and decimals is not null ";

		

		System.out.println(sql);
		return dao.queryWithJDBC(sql);
	}
	
	

	/************************************ checkRowData校验行可扩展表数据列 ***************************************/

/*	public List<?> getRightMinre(String tablecode, String mQuarter, String mYearMonth_Bq, String mReportRate,
			String tRightmin, String comCode) {
		String sql = "select rowno,sum(numvalue) from CfReportRowAddData " + "where tablecode='" + tablecode + "'"
				+ "  AND QUARTER = '" + mQuarter + "' " + "  AND YEARMONTH = '" + mYearMonth_Bq + "' "
				+ "  AND REPORTRATE = '" + mReportRate + "' " + " and comCode='" + comCode + "'" + " and colcode in ("
				+ tRightmin + ")" + " group by rowno order by rowno ";

		return dao.queryWithJDBC(sql);

	}

	public List<?> getRightPlusre(String tablecode, String mQuarter, String mYearMonth_Bq, String mReportRate,
			String tRightplus, String comCode) {
		String sql = "select rowno,sum(numvalue) from CfReportRowAddData " + "where tablecode='" + tablecode + "'"
				+ "  AND QUARTER = '" + mQuarter + "' " + "  AND YEARMONTH = '" + mYearMonth_Bq + "' "
				+ "  AND REPORTRATE = '" + mReportRate + "' " + " and comCode='" + comCode + "'" + " and colcode in ("
				+ tRightplus + ")" + " group by rowno order by rowno ";

		return dao.queryWithJDBC(sql);
	}

	public List<?> getLeftMinre(String tablecode, String mQuarter, String mYearMonth_Bq, String mReportRate,
			String tLeftmin, String comCode) {
		String sql = "select rowno ,sum(numvalue) from CfReportRowAddData " + "where tablecode='" + tablecode + "'"
				+ "  AND QUARTER = '" + mQuarter + "' " + "  AND YEARMONTH = '" + mYearMonth_Bq + "' "
				+ "  AND REPORTRATE = '" + mReportRate + "' " + " and comCode='" + comCode + "'" + " and colcode in ("
				+ tLeftmin + ")" + " group by rowno order by rowno ";
		return dao.queryWithJDBC(sql);
	}

	public List<?> getLeftCount(String tablecode, String mQuarter, String mYearMonth_Bq, String mReportRate,
			String tleftplus, String comCode) {
		String sql = "select rowno,sum(numvalue) from CfReportRowAddData " + "where tablecode='" + tablecode + "'"
				+ "  AND QUARTER = '" + mQuarter + "' " + "  AND YEARMONTH = '" + mYearMonth_Bq + "' "
				+ "  AND REPORTRATE = '" + mReportRate + "' " + " and colcode in (" + tleftplus + ")" + " and comCode='"
				+ comCode + "'" + " group by rowno order by rowno ";

		return dao.queryWithJDBC(sql);
	}*/

	public List<CfReportRowAddData> getSingleRowItemValue( String reportId,String mQuarter, String mYearMonth_Bq, 
			String mReportRate,String colcode, int rowNo, String comCode) {
		String sql = " from CfReportRowAddData  where reportid='"+reportId+"' and QUARTER = '" + mQuarter + "' " + "  AND YEARMONTH = '" + mYearMonth_Bq + "' "
				+ "  AND REPORTRATE = '" + mReportRate + "' "  + " and colcode= '" + colcode +"' " + "  AND rowNo = '" + rowNo + "' " 
				+ " and comCode='"+ comCode + "'" ;
		
	    return (List<CfReportRowAddData>) dao.query(sql);
	}
	
	public List<?> getRowCount(String reportId, String tablecode, String mQuarter, String mYearMonth_Bq, String mReportRate,String comCode) {
		String sql = "select COUNT(DISTINCT rowno) from CfReportRowAddData where reportid='"+reportId+"' and tablecode='" + tablecode + "'"
				+ "  AND QUARTER = '" + mQuarter + "' " + "  AND YEARMONTH = '" + mYearMonth_Bq + "' "
				+ "  AND REPORTRATE = '" + mReportRate + "' "  + " and comCode='"+ comCode + "'";
		
		return dao.query(sql);
/*		String sql2 =  "select COUNT(DISTINCT rowno) from CfReportRowAddData  where tablecode= ?  AND QUARTER = ?  AND YEARMONTH = ? AND REPORTRATE = ? and comCode=?";
		
		return dao.getCount(sql2, tablecode,  mQuarter,  mYearMonth_Bq,  mReportRate, comCode);*/
	}
	
	public List<?> getOutItemCodesSum(String reportId,String tablecode, String mQuarter, String mYearMonth_Bq, String mReportRate,
			String outItemCodes, String comCode) {
		String sql = "select rowno,sum(numvalue) from CfReportRowAddData  where  reportId = '" + reportId + "' and tablecode='" + tablecode + "'"
				+ "  AND QUARTER = '" + mQuarter + "' " + "  AND YEARMONTH = '" + mYearMonth_Bq + "' "
				+ "  AND REPORTRATE = '" + mReportRate + "' " + " and colcode in (" + outItemCodes + ")" + " and comCode='"
				+ comCode + "'" + " group by rowno order by rowno ";

		return dao.queryWithJDBC(sql);
	}
	
	@SuppressWarnings("unchecked")
	public List<CfReportRowDataCheck> getRepRowDataChecked(String isNeedChk, String companyType) {
		String hql = " from CfReportRowDataCheck c where c.isNeedChk like '%" + isNeedChk + "%' and (c.calType like '%" + companyType
				+ "%' or c.calType='ALL')";
/*		if (!"4".equals(isNeedChk)) {
			hql += " or c.isNeedChk = '3'";
		}*/

		return (List<CfReportRowDataCheck>) dao.query(hql);
	}

	/*********************************** checkPeriodItem校验跨期间因子指标数据 *************************************/
	@SuppressWarnings("unchecked")
	public List<CfReportPeriodDataCheck> getRepPeriodDataChecked(String isNeedChk, String companyType) {
		String hql = " from CfReportPeriodDataCheck c where c.isNeedChk like '%" + isNeedChk + "%' and (c.calType like '%"
				+ companyType + "%' or c.calType='ALL')";
/*		if (!"4".equals(isNeedChk)) {
			hql += " or c.isNeedChk = '3'";
		}*/

		return (List<CfReportPeriodDataCheck>) dao.query(hql);
	}

	/************************** checkItemRow ****************************************/
	/**
	 * 求和
	 */
	public List<?> getSum(String reportId, String mQuarter, String mYearMonth_Bq, String mReportRate, String colcode, String tablecode,
			String comCode) {
		String sql = "select sum(numvalue) from cfreportrowadddata where 1=1 and reportId = '" + reportId + "'  AND QUARTER = '" + mQuarter + "' "
				+ "  AND YEARMONTH = '" + mYearMonth_Bq + "' " + "  AND REPORTRATE = '" + mReportRate + "' "
				+ " and colcode = '" + colcode + "'" + " and tablecode='" + tablecode + "'" + " and comCode='" + comCode
				+ "'";
		return dao.queryWithJDBC(sql);
	}

	/**
	 * 获取CfReportItemRowCheck需要校验的数据
	 */
	@SuppressWarnings("unchecked")
	public List<CfReportItemRowCheck> getRepItemRowChecked(String isNeedChk, String companyType) {
		String hql = " from CfReportItemRowCheck c where c.isNeedChk like '%" + isNeedChk + "%' and (c.calType  like'%" + companyType
				+ "%' or c.calType='ALL')";
/*		if (!"4".equals(isNeedChk)) {
			hql += " or c.isNeedChk = '3'";
		}*/

		return (List<CfReportItemRowCheck>) dao.query(hql);
	}
	/**************************************************************************************/

	/**
	 * 获取cfReportDataCheck需要校验的数据
	 */
	@SuppressWarnings("unchecked")
	public List<CfReportDataCheck> getRepDataChecked(String isNeedChk, String companyType) {
		String hql = " from CfReportDataCheck c where c.isNeedChk like '%" + isNeedChk + "%' and (c.calType like'%" + companyType
				+ "%' or c.calType='ALL')";

		return (List<CfReportDataCheck>) dao.query(hql);
	}

	/**
	 * 添加错误信息到数据库
	 */
	public boolean insertErrorInfo(CfReportErrInfo tCfReportErrInfo) {
		try {
			dao.create(tCfReportErrInfo);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 批量删除错误信息//删除按年和季度删除
	 */
	public boolean deleteErrInfo(DataCheckDto dataCheckDto) {
		String sql = "delete from CfReportErrInfo where  year='" + dataCheckDto.getYear() + "' and quarter='"
				+ dataCheckDto.getQuarter() + "'" + " and reportType='" + dataCheckDto.getReporttype() + "'";

		if (dataCheckDto.getCheckedCode() != null && !dataCheckDto.getCheckedCode().trim().equals("")) {
			sql += " and checkedCode='" + dataCheckDto.getCheckedCode() + "'";
		}

		dao.excute(sql);
		return true;
	}
	/**
	 * 批量删除风险评级的错误信息---需要根据公司代码删除
	 */
	public boolean deleteRiskRatingErrInfo(DataCheckDto dataCheckDto,String comCode) {
		String sql = "delete from CfRiskRatingErrInfo where year='" + dataCheckDto.getYear() + "' and quarter='"
				+ dataCheckDto.getQuarter() + "'" + " and reportType='2'"
				+ " and comCode='"+dataCheckDto.getComCode()+"'";

		dao.excute(sql);
		return true;
	}
	//------------------------------------------------------------风险评级
		public List<?> getRiskRatingDataByDecimals(String reportRate, String yearMonth, String quarter, String comCode) {
			String sql="";
			
				 sql = "select cf1.outitemcode as outitemcode,round(cf1.reportitemvalue,ifnull(cf2.decimals,0)) as reportitemvalue  ,ifnull(cf2.decimals,0) as decimals  from CfReportData cf1 ,cfreportelement cf2 "
				 		+ " where cf1.outitemcode=cf2.portitemcode and cf1.reportRate='" + reportRate + "' and cf1.month='" + yearMonth 
						+ "' and cf1.quarter='" + quarter + "' and cf1.comCode='" + comCode + "' and cf1.reportItemValue is not null";
			System.out.println("sql--->"+sql);
			return (List<?>) dao.queryWithJDBC(sql);
		}
		public List<CfRiskRatingCheck> getRiskRatingChecked(String isNeedChk, String companyType,String companyFlag) {
			String hql = " from CfRiskRatingCheck c where c.companyFlag='"+companyFlag+"' and c.isNeedChk like '%" + isNeedChk + "%' and (c.calType like '%"
					+ companyType + "%' or c.calType='ALL') ";
	/*		if (!"4".equals(isNeedChk)) {
				hql += " or c.isNeedChk = '3'";
			}*/

			return (List<CfRiskRatingCheck>) dao.query(hql);
		}
		/*
		 * 添加错误信息到风险评估错误信息表里
		 */
		public boolean insertErrorToRiskRating(CfRiskRatingErrInfo cfRiskRatingErrinfo) {
			try {
				dao.create(cfRiskRatingErrinfo);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

}
