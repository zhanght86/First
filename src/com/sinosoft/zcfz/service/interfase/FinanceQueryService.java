package com.sinosoft.zcfz.service.interfase;

import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dto.interfase.FinanceListDTO;

public interface FinanceQueryService {
    public Page<?> queryByPage(int page,int rows,FinanceListDTO  dto);
    public Page<?> queryByPage1(int page,int rows,FinanceListDTO  dto);
    public Page<?> queryByPage2(int page,int rows,FinanceListDTO  dto);
    public Page<?> queryByPage3(int page,int rows,FinanceListDTO  dto);
    public Page<?> queryByPage4(int page,int rows,FinanceListDTO  dto);
    public Page<?> queryByPage5(int page,int rows,FinanceListDTO  dto);
    public void updateBankInfo(String itemcode,String itemname,String yearmonth,String balance_qc_after,String DEBIT_BQ_after,String CREDIT_BQ_after,String BALANCE_QM_after,String year, String quarter,String datetime);
    public void updateBankInfo1(String tablecode,String itemcode,String yearmonth,String bookvalueqc_after,String amountbq_after,String amountqm_after,String bookvalueqm_after,String year, String quarter,String datetime);
    public void updateBankInfo2(String tablecode,String accountcode,String yearmonth,String balanceqc_after,String balanceqm_after,String debitbq_after,String creditbq_after,String interestvalue_after,String year, String quarter,String datetime);
    public void updateBankInfo4(String productcode,String productname,String yearmonth,String premiumsreceivable_after,String policyloans_after,String interestreceivable_after,String year, String quarter,String datetime);
    public void updateBankInfo5(String itemcode,String itemname,String yearmonth,String sumdebit_after,String suncredit_after,String remark,String year, String quarter,String datetime);

}
