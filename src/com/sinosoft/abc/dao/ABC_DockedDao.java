package com.sinosoft.abc.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.sinosoft.abc.entity.Invest_ABC;
import com.sinosoft.common.Dao;
import com.sinosoft.dao.BaseAbstractDao;
import com.sinosoft.zcfz.dto.reportdataimport.UploadInfoDTO;

@Repository
public class ABC_DockedDao extends BaseAbstractDao{
	@Resource(name = "dao")
	private Dao dao;

	public List<Map> getReportId(String year, String quarter) {
		String sqlString = "select reportId from CAL_CFREPORTINFO "
				+ "where reporttype='2' and year='" + year+"' and quarter ='"+quarter+"'";
		return  (List<Map>) dao.getList(sqlString);
	}
	public List<Invest_ABC> getMR12Invest(String year, String quarter) {
		String sql = " from abc_invest  "
				+ "where  year='"+ year +"' and quarter='"+quarter +"' ";

		return  (List<Invest_ABC>) dao.getList(sql);
	}
	
	//----------------------------------------------------------
	public boolean deleteCR19Data(String year, String quarter) {
		String sql = "delete from cfreportdata where month='" + year + 
				"' and quarter='" + quarter + "' and reportrate='2' and outitemcode like 'CR19%'  "  ;

		dao.excute(sql);
		return true;
	}
	public List<Map> getSubjectCR19Count(String year, String quarter) {
		String sql = "select  nvl(count(sum(closingBalance)),0) as count from abc_finance  "
				+ "where  subjectSegment in  ('1221001001','1221002001','1221003001','1221004001','1221005001','1221006001'"
				+ ",'1221007001','1221008001','1221009001','1221010001','1221011001','1221012001','1221013001','1221099001') "
				+ "and year='"+ year +"' and quarter='"+quarter +"' "
				+ "group by itemcode,details  order by sum(closingBalance) desc";

		return  (List<Map>) dao.getList(sql);
	}
	
	public List<Map>  getSubjectCR19Sum(String year, String quarter) {
		String sql = "select  nvl(sum(closingBalance),0) as value, details from abc_finance  "
				+ "where  subjectSegment in  ('1221001001','1221002001','1221003001','1221004001','1221005001','1221006001'"
				+ ",'1221007001','1221008001','1221009001','1221010001','1221011001','1221012001','1221013001','1221099001') "
				+ "and year='"+ year +"' and quarter='"+quarter +"' "
				+ "group by itemcode,details  order by sum(closingBalance) desc";

		return  (List<Map>) dao.getList(sql);
	}
	
	//----------------------------------------------------------
	/**
	 * 月度科目汇总账科目段及明细段表
	 */
	public boolean deleteSubjectData(UploadInfoDTO dto) {
		String sql = "delete from abc_finance where year='" + dto.getYearmonth() + 
				"' and quarter='" + dto.getQuarter() + "' and tablecode='F01' "  ;

		dao.excute(sql);
		return true;
	}
	
	/**
	 * 删除财务数据表里的数据
	 */
	public boolean deleteFinanceData(UploadInfoDTO dto) {
		String sql = "delete from abc_finance where year='" + dto.getYearmonth() + 
				"' and quarter='" + dto.getQuarter() + "' and not tablecode='F01' "  ;

		dao.excute(sql);
		return true;
	}

	/**********************************************************/
	
	/**
	 * 删除投资表表所有数据
	 */
	public boolean deleteInvestAllData(UploadInfoDTO dto) {
		String sql = "delete from abc_invest where year='" + dto.getYearmonth()+ 
				"' and quarter='" + dto.getQuarter() +"'";

		dao.excute(sql);
		return true;
	}
	
	/**
	 * 删除投资表表里的数据
	 */
	public boolean deleteInvestMR09Data(UploadInfoDTO dto) {
		String sql = "delete from abc_invest where year='" + dto.getYearmonth()+ 
				"' and quarter='" + dto.getQuarter() +
				"' and tablecode = 'MR09'";

		dao.excute(sql);
		return true;
	}
	
	public boolean deleteInvestMR12Data(UploadInfoDTO dto) {
		String sql = "delete from abc_invest where year='" + dto.getYearmonth() + 
				"' and quarter='" + dto.getQuarter() +
				"' and tablecode ='MR12'";

		dao.excute(sql);
		return true;
	}
	
	public boolean deleteInvestCR01PolicyData(UploadInfoDTO dto) {
		String sql = "delete from abc_invest where year='" + dto.getYearmonth() + 
				"' and quarter='" + dto.getQuarter() +
				"' and tablecode ='CR01_01' ";

		dao.excute(sql);
		return true;
	}
	
	public boolean deleteInvestCR01OtherData(UploadInfoDTO dto) {
		String sql = "delete from abc_invest where year='" + dto.getYearmonth() + 
				"' and quarter='" + dto.getQuarter() +
				"' and tablecode ='CR01_02' ";

		dao.excute(sql);
		return true;
	}
	
	public boolean deleteInvestCR05Data(UploadInfoDTO dto) {
		String sql = "delete from abc_invest where year='" + dto.getYearmonth() + 
				"' and quarter='" + dto.getQuarter() +
				"' and tablecode = 'CR05'";

		dao.excute(sql);
		return true;
	}
	
	public boolean deleteInvestCR10Data(UploadInfoDTO dto) {
		String sql = "delete from abc_invest where year='" + dto.getYearmonth() + 
				"' and quarter='" + dto.getQuarter() +
				"' and tablecode ='CR10'";

		dao.excute(sql);
		return true;
	}
}

