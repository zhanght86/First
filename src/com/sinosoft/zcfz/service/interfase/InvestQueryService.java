package com.sinosoft.zcfz.service.interfase;
import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dto.interfase.InvestListDTO;

public interface InvestQueryService {
	 public Page<?> queryByPage(int page,int rows,InvestListDTO  dto);
	 public Page<?> queryByPage1(int page,int rows,InvestListDTO  dto);
	 public Page<?> queryByPage2(int page,int rows,InvestListDTO  dto);
	 public Page<?> queryByPage3(int page,int rows,InvestListDTO  dto);
	 public Page<?> queryByPage4(int page,int rows,InvestListDTO  dto);
	 public Page<?> queryByPage5(int page,int rows,InvestListDTO  dto);
	 public Page<?> queryByPage6(int page,int rows,InvestListDTO  dto);
	 public void updateBankInfo(String booktype,String interestincodeitem,String yearmonth,String interestitem,String principalitemcode,String startdate,String enddate,String principal_after,String year,String quarter,String deposittype,String datadate);
	 public void updateBankInfo2(String booktype,String itemcode,String itemname,String yearmonth,String year,String quarter,String amount_after,String cost_after,String marketvalue_after,String datadate);
	 public void updateBankInfo3( String bondtype, String yearmonth, String securitycode, String securityname, String closingpricecorrectduration_after, String year, String quarter, String couponrate_after,String datadate);
	 public void updateBankInfo4( String yearmonth, String securitycode, String securityname,  String year, String quarter, String totalmarrketvalue_after, String datadate);
	 public void updateBankInfo6(String yearmonth, String code, String name, String year, String quarter, String close_after, String weight_after,String upordowm_after, String changefloat_after, String indexcontributepoint_after, String volume_after, String turnvolume_after, String totalshares_after,String flowshares_after, String totalmarketvalue_after, String folwmarketvalue_after,String datadate);

}
