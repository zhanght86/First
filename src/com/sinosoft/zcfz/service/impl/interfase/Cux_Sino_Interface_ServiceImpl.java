package com.sinosoft.zcfz.service.impl.interfase;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dao.interfaces.Cux_Sino_Interface_DAO;
import com.sinosoft.zcfz.dto.interfase.CUX_SINO_INTERFACE_DTO;
import com.sinosoft.zcfz.dto.interfase.Cux_Sino_Interface_InvestDTO;
import com.sinosoft.zcfz.dto.interfase.ImpDataInfoDTO;
import com.sinosoft.entity.Cux_Sino_Interface_BondInfo;
import com.sinosoft.entity.Cux_Sino_Interface_BondInfoId;
import com.sinosoft.entity.Cux_Sino_Interface_Deposit;
import com.sinosoft.entity.Cux_Sino_Interface_DepositId;
import com.sinosoft.entity.Cux_Sino_Interface_Finance;
import com.sinosoft.entity.Cux_Sino_Interface_FinanceId;
import com.sinosoft.entity.Cux_Sino_Interface_Fund;
import com.sinosoft.entity.Cux_Sino_Interface_FundId;
import com.sinosoft.entity.Cux_Sino_Interface_HS300Stock;
import com.sinosoft.entity.Cux_Sino_Interface_HS300StockId;
import com.sinosoft.entity.Cux_Sino_Interface_Stock;
import com.sinosoft.entity.Cux_Sino_Interface_StockId;
import com.sinosoft.entity.Cux_Sino_Interface_Valuation;
import com.sinosoft.entity.Cux_Sino_Interface_ValuationId;
import com.sinosoft.zcfz.service.interfase.Cux_Sino_Interface_Service;
import com.sinosoft.util.ExcelUtil;
import com.sinosoft.util.UploaderServlet;
@Service
public class Cux_Sino_Interface_ServiceImpl  implements  Cux_Sino_Interface_Service{
	private static final Logger LOGGER = LoggerFactory
			.getLogger(Cux_Sino_Interface_ServiceImpl.class); 
	private String yearmonth;
	private SimpleDateFormat sdf1 = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
	private SimpleDateFormat sdf2 = new SimpleDateFormat( "yyyy-MM-dd" );
	@Resource
	private UploaderServlet us;
	@Resource
	private ExcelUtil eu;
	@Resource
    private Cux_Sino_Interface_DAO  cuxdao;
	@Transactional
	public Page<?> queryByPage(int page, int rows, CUX_SINO_INTERFACE_DTO dto) {
        
		StringBuilder   sql=new  StringBuilder("select product_code \"product_code\", product_name \"product_name\" ,account_code \"account_code\" ,account_name \"account_name\" , period \"period\" ,  period_balance \"period_balance\"  from  Cux_Sino_Interface_All  where  1=1 ");
		if(!(dto.getAccount_code()==""||dto.getAccount_code()==null)){
			sql.append(" and  account_code = '"+dto.getAccount_code().trim()+"'");
		}if(!(dto.getPeriod()==""||dto.getPeriod()==null)){
			sql.append(" and  period = '"+dto.getPeriod().trim()+"'");
		}
		if(!(dto.getSort()==""||dto.getOrder()==""||dto.getSort()==null||dto.getOrder()==null)){
			sql.append(" order by  "+dto.getSort()+" "+dto.getOrder());
		}
		return cuxdao.queryByPage(sql.toString(), page, rows);
	}
	@Transactional
	public Page<?> queryByPage(int page, int rows, Cux_Sino_Interface_InvestDTO dto) {
        
		StringBuilder   sql=new  StringBuilder("select product_code \"product_code\", product_name \"product_name\" ,account_code \"account_code\" ,account_name \"account_name\" , period \"period\" ,  period_balance \"period_balance\"  from  Cux_Sino_Interface_All  where  1=1 ");
		return cuxdao.queryByPage(sql.toString(), page, rows);
	}
	@Transactional
	public boolean saveInterfaceData(ImpDataInfoDTO info) throws IOException, ParseException {
		// TODO Auto-generated method stub
		getYearmonth(info);
		System.out.println(info.getFilePath()+"/"+us.getFileName());
		eu.setExcelPath(info.getFilePath()+"/"+us.getFileName());
		String interfacetype = info.getInterfacetype();
		if(interfacetype.equals("1")){//存款
			eu.setStartReadPos(3);
			eu.setEndReadPos(0);			
			eu.setSelectedSheetName("Sheet1");
			List<Row> listDeposit=eu.readExcel();
			saveDataDeposit(listDeposit,info);
			listDeposit.removeAll(listDeposit);
			return true;
		}else if(interfacetype.equals("2")){//理财
			eu.setStartReadPos(1);
			eu.setEndReadPos(0);			
			eu.setSelectedSheetName("Sheet1");
			List<Row> listFinance=eu.readExcel();
			saveDataFinance(listFinance,info);
			listFinance.removeAll(listFinance);
			return true;
		}else if(interfacetype.equals("3")){//估值
			eu.setStartReadPos(4);
			eu.setEndReadPos(-20);			
			eu.setSelectedSheetName("Sheet1");
			List<Row> listValuation=eu.readExcel();
			saveDataValuation(listValuation,info);
			listValuation.removeAll(listValuation);
			return true;
		}else if(interfacetype.equals("4")){//股票
			eu.setStartReadPos(1);
			eu.setEndReadPos(-1);			
			eu.setSelectedSheetName("Wind资讯");
			List<Row> listStock=eu.readExcel();
			saveDataStock(listStock,info);
			listStock.removeAll(listStock);
			return true;
		}else if(interfacetype.equals("5")){//基金
			eu.setStartReadPos(1);
			eu.setEndReadPos(-1);			
			eu.setSelectedSheetName("Wind资讯");
			List<Row> listFund=eu.readExcel();
			saveDataFund(listFund,info);
			listFund.removeAll(listFund);
			return true;
		}else if(interfacetype.equals("6")){//沪深300
			eu.setStartReadPos(1);
			eu.setEndReadPos(0);			
			eu.setSelectedSheetName("成份数据--成份及权重");
			List<Row> listHS300=eu.readExcel();
			saveDataHS300(listHS300,info);
			listHS300.removeAll(listHS300);
			return true;
		}else if(interfacetype.equals("7")){//债券基础信息
			eu.setStartReadPos(1);
			eu.setEndReadPos(-1);			
			eu.setSelectedSheetName("Wind资讯");
			List<Row> listBond=eu.readExcel();
			saveDataBond(listBond,info);
			listBond.removeAll(listBond);
			return true;
		}else{
			System.out.println("接口类别未识别。");
			return false;
		}
	}
	@Transactional
	public void saveDataDeposit(List<Row> list,ImpDataInfoDTO info) throws ParseException{
		for(int i=0;i<list.size();i++){
			System.out.println("总共"+list.size()+"行，第"+(i+1)+"行：");
			Row row=list.get(i);
			if(row.getCell(0).getStringCellValue()==null||"".equals(row.getCell(0).getStringCellValue())){
				System.out.println("这一行无数据，自动跳过该行。");
				continue;
			}
			Cux_Sino_Interface_Deposit deposit = new Cux_Sino_Interface_Deposit();
			Cux_Sino_Interface_DepositId id = new Cux_Sino_Interface_DepositId();
			id.setDepositType(info.getInterfacetable());
			id.setYearMonth(yearmonth);
			id.setBookType(row.getCell(0).getStringCellValue());
			id.setStartDate(row.getCell(1).getDateCellValue());
			id.setEndDate(row.getCell(2).getDateCellValue());
			id.setPrincipalItemCode(row.getCell(5).getStringCellValue());
			id.setInterestItem(row.getCell(10).getStringCellValue());
			id.setInterestIncomeItem(row.getCell(11).getStringCellValue());
			deposit.setId(id);
			System.out.println(us.getDate());
			deposit.setImpDate(sdf1.parse(us.getDate()));
			deposit.setImpOperator("admin");//默认
			deposit.setUpdateDate(null);
			deposit.setUpdateOperator(null);
			if(info.getDatadate()==null||"".equals(info.getDatadate())){
				System.out.println("取数截止日期未录入。");
			}else{
				deposit.setDataDate(sdf2.parse(info.getDatadate()));							
			}
			deposit.setDepositState(row.getCell(3).getStringCellValue());
			if(ExcelUtil.getCellValue(row.getCell(4))!=""){				
				deposit.setPrincipal(new BigDecimal(ExcelUtil.getCellValue(row.getCell(4))));
			}
			deposit.setBankCode(row.getCell(6).getStringCellValue());
			deposit.setInsideOrOutside(row.getCell(7).getStringCellValue());
			deposit.setAccountCode(row.getCell(8).getStringCellValue());
			deposit.setAccountName(row.getCell(9).getStringCellValue());
			if(ExcelUtil.getCellValue(row.getCell(12))!=""){				
				deposit.setPerHundredRate(new BigDecimal(ExcelUtil.getCellValue(row.getCell(12))));
			}
			deposit.setDepositsTerm(row.getCell(13).getStringCellValue());
			deposit.setInterestStartDate(row.getCell(14).getDateCellValue());
			deposit.setInterestTateType(row.getCell(15).getStringCellValue());
			deposit.setInterestType(row.getCell(16).getStringCellValue());
			deposit.setInterestWay(row.getCell(17).getStringCellValue());
			deposit.setInterestFrequency(row.getCell(18).getStringCellValue());
			deposit.setInterestSettleWay(row.getCell(19).getStringCellValue());
			deposit.setFirstInterestSettleDate(row.getCell(20).getDateCellValue());
			deposit.setBaseInterestRateType(row.getCell(21).getStringCellValue());
			if(ExcelUtil.getCellValue(row.getCell(22))!=""){				
				deposit.setBasicInterestRateDiffer(new BigDecimal(ExcelUtil.getCellValue(row.getCell(22))));
			}
			if(ExcelUtil.getCellValue(row.getCell(23))!=""){				
				deposit.setFloatRatio(new BigDecimal(ExcelUtil.getCellValue(row.getCell(23))));
			}
			deposit.setPranayamaTimeType(row.getCell(24).getStringCellValue());
			deposit.setRemark(row.getCell(25).getStringCellValue());
			//根据id查询
			Cux_Sino_Interface_Deposit adeposit=(Cux_Sino_Interface_Deposit) cuxdao.queryByID(deposit.getClass(), id);
			if(adeposit!=null){
				//删除旧的数据
				System.out.println("删除");
				cuxdao.remove(adeposit);
				//添加新数据
				System.out.println("新增");
				cuxdao.save(deposit);
			}else{
				System.out.println("新增");
				cuxdao.save(deposit);				
			}
		}
	}
	@Transactional
    public void saveDataFinance(List<Row> list,ImpDataInfoDTO info) throws ParseException{
		if("1".equals(info.getInterfacetable())){//保险理财特殊处理
			for(int i=0;i<list.size();i++){
				System.out.println("总共"+list.size()+"行，第"+(i+1)+"行：");
				Row row=list.get(i);
				if(row.getCell(0).getStringCellValue()==null||"".equals(row.getCell(0).getStringCellValue())||"证券代码".equals(row.getCell(0).getStringCellValue())){
					System.out.println("这一行无数据，自动跳过该行。");
					continue;
				}
				Cux_Sino_Interface_Finance finance = new Cux_Sino_Interface_Finance();
				Cux_Sino_Interface_FinanceId id = new Cux_Sino_Interface_FinanceId();
				id.setFinanceType(info.getInterfacetable());
				id.setYearMonth(yearmonth);
				id.setSecuritiesCode(row.getCell(0).getStringCellValue());
				id.setSecurityName(row.getCell(1).getStringCellValue());
				finance.setId(id);
				finance.setImpDate(sdf1.parse(us.getDate()));
				finance.setImpOperator("admin");//默认
				finance.setUpdateDate(null);
				finance.setUpdateOperator(null);
				if(info.getDatadate()==null||"".equals(info.getDatadate())){
					System.out.println("取数截止日期未录入。");
				}else{
					finance.setDataDate(sdf2.parse(info.getDatadate()));							
				}
				finance.setIssuers(row.getCell(2).getStringCellValue());
				finance.setCurrency(row.getCell(4).getStringCellValue());
				finance.setStartDate(row.getCell(5).getDateCellValue());
				finance.setEndDate(row.getCell(6).getDateCellValue());
				finance.setSimpleOrCompound(row.getCell(7).getStringCellValue());
				if(ExcelUtil.getCellValue(row.getCell(8))!=""){				
					finance.setPerDenomination(new BigDecimal(ExcelUtil.getCellValue(row.getCell(8))));
				}
				finance.setProfitType(row.getCell(9).getStringCellValue());
				finance.setDetailType(row.getCell(10).getStringCellValue());
				finance.setInterestStartDate(row.getCell(11).getDateCellValue());
				finance.setInterestFristPayDate(row.getCell(12).getDateCellValue());
				finance.setInterestPayRate(row.getCell(13).getStringCellValue());
				finance.setInterestComputeWay(row.getCell(14).getStringCellValue());
				finance.setUpdateOpera(row.getCell(15).getStringCellValue());
				finance.setConfirmOpera(row.getCell(16).getStringCellValue());
				finance.setRemark(row.getCell(17).getStringCellValue());
				finance.setRiskType(row.getCell(3).getStringCellValue());
				
				//根据id查询
				Cux_Sino_Interface_Finance afinance=(Cux_Sino_Interface_Finance) cuxdao.queryByID(finance.getClass(), id);
				if(afinance!=null){
					//删除旧的数据
					System.out.println("删除");
					cuxdao.remove(afinance);
					//添加新数据
					System.out.println("新增");
					cuxdao.save(finance);
				}else{
					System.out.println("新增");
					cuxdao.save(finance);				
				}
			}					
		}else{
			for(int i=0;i<list.size();i++){
				System.out.println("总共"+list.size()+"行，第"+(i+1)+"行：");
				Row row=list.get(i);
				if(row.getCell(0).getStringCellValue()==null||"".equals(row.getCell(0).getStringCellValue())||"证券代码".equals(row.getCell(0).getStringCellValue())){
					System.out.println("这一行无数据，自动跳过该行。");
					continue;
				}
				Cux_Sino_Interface_Finance finance = new Cux_Sino_Interface_Finance();
				Cux_Sino_Interface_FinanceId id = new Cux_Sino_Interface_FinanceId();
				id.setFinanceType(info.getInterfacetable());
				id.setYearMonth(yearmonth);
				id.setSecuritiesCode(row.getCell(0).getStringCellValue());
				id.setSecurityName(row.getCell(1).getStringCellValue());
				finance.setId(id);
				finance.setImpDate(sdf1.parse(us.getDate()));
				finance.setImpOperator("admin");//默认
				finance.setUpdateDate(null);
				finance.setUpdateOperator(null);
				if(info.getDatadate()==null||"".equals(info.getDatadate())){
					System.out.println("取数截止日期未录入。");
				}else{
					finance.setDataDate(sdf2.parse(info.getDatadate()));							
				}
				finance.setIssuers(row.getCell(2).getStringCellValue());
				finance.setCurrency(row.getCell(3).getStringCellValue());
				finance.setStartDate(row.getCell(4).getDateCellValue());
				finance.setEndDate(row.getCell(5).getDateCellValue());
				finance.setSimpleOrCompound(row.getCell(6).getStringCellValue());
				if(ExcelUtil.getCellValue(row.getCell(7))!=""){				
					finance.setPerDenomination(new BigDecimal(ExcelUtil.getCellValue(row.getCell(7))));
				}
				finance.setProfitType(row.getCell(8).getStringCellValue());
				finance.setDetailType(row.getCell(9).getStringCellValue());
				finance.setInterestStartDate(row.getCell(10).getDateCellValue());
				finance.setInterestFristPayDate(row.getCell(11).getDateCellValue());
				finance.setInterestPayRate(row.getCell(12).getStringCellValue());
				finance.setInterestComputeWay(row.getCell(13).getStringCellValue());
				finance.setUpdateOpera(row.getCell(14).getStringCellValue());
				finance.setConfirmOpera(row.getCell(15).getStringCellValue());
				finance.setRemark(row.getCell(16).getStringCellValue());
				finance.setRiskType(null);

				//根据id查询
				Cux_Sino_Interface_Finance afinance=(Cux_Sino_Interface_Finance) cuxdao.queryByID(finance.getClass(), id);
				if(afinance!=null){
					//删除旧的数据
					System.out.println("删除");
					cuxdao.remove(afinance);
					//添加新数据
					System.out.println("新增");
					cuxdao.save(finance);
				}else{
					System.out.println("新增");
					cuxdao.save(finance);				
				}
			}			
		}
	}
	@Transactional
    public void saveDataValuation(List<Row> list,ImpDataInfoDTO info) throws ParseException{
		for(int i=0;i<list.size();i++){
			System.out.println("总共"+list.size()+"行，第"+(i+1)+"行：");
			Row row=list.get(i);
			if(row.getCell(0).getStringCellValue()==null||"".equals(row.getCell(0).getStringCellValue())){
				System.out.println("这一行无数据，自动跳过该行。");
				continue;
			}
			Cux_Sino_Interface_Valuation valuation = new Cux_Sino_Interface_Valuation();
			Cux_Sino_Interface_ValuationId id = new Cux_Sino_Interface_ValuationId();
			id.setBookType(info.getInterfacetable());
			id.setYearMonth(yearmonth);
			id.setItemCode(row.getCell(0).getStringCellValue());
			id.setItemName(row.getCell(1).getStringCellValue());
			valuation.setId(id);
			valuation.setImpDate(sdf1.parse(us.getDate()));
			valuation.setImpOperator("admin");//默认
			valuation.setUpdateDate(null);
			valuation.setUpdateOperator(null);
			if(info.getDatadate()==null||"".equals(info.getDatadate())){
				System.out.println("取数截止日期未录入。");
			}else{
				valuation.setDataDate(sdf2.parse(info.getDatadate()));							
			}
			if(ExcelUtil.getCellValue(row.getCell(2))!=""){
				valuation.setAmount(new BigDecimal(ExcelUtil.getCellValue(row.getCell(2))));				
			}
			if(ExcelUtil.getCellValue(row.getCell(3))!=""){				
				valuation.setUnitCost(new BigDecimal(ExcelUtil.getCellValue(row.getCell(3))));
			}
			if(ExcelUtil.getCellValue(row.getCell(4))!=""){				
				valuation.setCost(new BigDecimal(ExcelUtil.getCellValue(row.getCell(4))));
			}
			if(ExcelUtil.getCellValue(row.getCell(5))!=""){				
				valuation.setCostForAccountPersent(new BigDecimal(ExcelUtil.getCellValue(row.getCell(5))));
			}
			if(ExcelUtil.getCellValue(row.getCell(6))!=""){				
				valuation.setMarketPrice(new BigDecimal(ExcelUtil.getCellValue(row.getCell(6))));
			}
			if(ExcelUtil.getCellValue(row.getCell(7))!=""){				
				valuation.setMarketValue(new BigDecimal(ExcelUtil.getCellValue(row.getCell(7))));
			}
			if(ExcelUtil.getCellValue(row.getCell(8))!=""){				
				valuation.setMarketValueForAccountPersent(new BigDecimal(ExcelUtil.getCellValue(row.getCell(8))));
			}
			if(ExcelUtil.getCellValue(row.getCell(9))!=""){				
				valuation.setValuationAppreciation(new BigDecimal(ExcelUtil.getCellValue(row.getCell(9))));
			}
			valuation.setSuspensionInfo(row.getCell(10).getStringCellValue());

			//根据id查询
			Cux_Sino_Interface_Valuation avaluation=(Cux_Sino_Interface_Valuation) cuxdao.queryByID(valuation.getClass(), id);
			if(avaluation!=null){
				//删除旧的数据
				System.out.println("删除");
				cuxdao.remove(avaluation);
				//添加新数据
				System.out.println("新增");
				cuxdao.save(valuation);
			}else{
				System.out.println("新增");
				cuxdao.save(valuation);				
			}
		}
    }
	@Transactional
	public void saveDataFund(List<Row> list,ImpDataInfoDTO info) throws ParseException{
		for(int i=0;i<list.size();i++){
			System.out.println("总共"+list.size()+"行，第"+(i+1)+"行：");
			Row row=list.get(i);
			if(row.getCell(0).getStringCellValue()==null||"".equals(row.getCell(0).getStringCellValue())){
				System.out.println("这一行无数据，自动跳过该行。");
				continue;
			}
			Cux_Sino_Interface_Fund fund = new Cux_Sino_Interface_Fund();
			Cux_Sino_Interface_FundId id = new Cux_Sino_Interface_FundId();
			id.setYearMonth(yearmonth);
			id.setSecurityCode(row.getCell(0).getStringCellValue());
			id.setSecurityName(row.getCell(1).getStringCellValue());
			fund.setId(id);
			fund.setImpDate(sdf1.parse(us.getDate()));
			fund.setImpOperator("admin");
			fund.setUpdateOperator(null);
			fund.setUpdateOperator(null);
			if(info.getDatadate()==null||"".equals(info.getDatadate())){
				System.out.println("取数截止日期未录入。");
			}else{
				fund.setDataDate(sdf2.parse(info.getDatadate()));							
			}
			fund.setFundPromoter(row.getCell(2).getStringCellValue());
			fund.setInvestType(row.getCell(3).getStringCellValue());
			fund.setGradingFund(row.getCell(4).getStringCellValue());
			fund.setGradingFundType(row.getCell(5).getStringCellValue());
			
			//根据id查询
			Cux_Sino_Interface_Fund afund=(Cux_Sino_Interface_Fund) cuxdao.queryByID(fund.getClass(), id);
			if(afund!=null){
				//删除旧的数据
				System.out.println("删除");
				cuxdao.remove(afund);
				//添加新数据
				System.out.println("新增");
				cuxdao.save(fund);
			}else{
				System.out.println("新增");
				cuxdao.save(fund);				
			}
		}
	}
	@Transactional
	public void saveDataStock(List<Row> list,ImpDataInfoDTO info) throws ParseException{
		for(int i=0;i<list.size();i++){
			System.out.println("总共"+list.size()+"行，第"+(i+1)+"行：");
			Row row=list.get(i);
			if(row.getCell(0).getStringCellValue()==null||"".equals(row.getCell(0).getStringCellValue())){
				System.out.println("这一行无数据，自动跳过该行。");
				continue;
			}
			Cux_Sino_Interface_Stock stock = new Cux_Sino_Interface_Stock();
			Cux_Sino_Interface_StockId id = new Cux_Sino_Interface_StockId();
			id.setYearMonth(yearmonth);
			id.setSecurityCode(row.getCell(0).getStringCellValue());
			id.setSecurityName(row.getCell(1).getStringCellValue());
			stock.setId(id);
			stock.setImpDate(sdf1.parse(us.getDate()));
			stock.setImpOperator("admin");//默认
			stock.setUpdateDate(null);
			stock.setUpdateOperator(null);
			if(info.getDatadate()==null||"".equals(info.getDatadate())){
				System.out.println("取数截止日期未录入。");
			}else{
				stock.setDataDate(sdf2.parse(info.getDatadate()));							
			}
			stock.setMarketBoard(row.getCell(2).getStringCellValue());
			if(ExcelUtil.getCellValue(row.getCell(3))!=""){				
				stock.setTotalMarketValue(new BigDecimal(ExcelUtil.getCellValue(row.getCell(3))));
			}
			//根据id查询
			Cux_Sino_Interface_Stock astock=(Cux_Sino_Interface_Stock) cuxdao.queryByID(stock.getClass(), id);
			if(astock!=null){
				//删除旧的数据
				System.out.println("删除");
				cuxdao.remove(astock);
				//添加新数据
				System.out.println("新增");
				cuxdao.save(stock);
			}else{
				System.out.println("新增");
				cuxdao.save(stock);				
			}
		}
	}
	@Transactional
	public void saveDataHS300(List<Row> list,ImpDataInfoDTO info) throws ParseException{
		for(int i=0;i<list.size();i++){
			System.out.println("总共"+list.size()+"行，第"+(i+1)+"行：");
			Row row=list.get(i);
			if(ExcelUtil.getCellValue(row.getCell(0))==null||"".equals(ExcelUtil.getCellValue(row.getCell(0)))){
				System.out.println("这一行无数据，自动跳过该行。");
				continue;
			}
			Cux_Sino_Interface_HS300Stock hs300stock = new Cux_Sino_Interface_HS300Stock();
			Cux_Sino_Interface_HS300StockId id = new Cux_Sino_Interface_HS300StockId();
			id.setYearMonth(yearmonth);
			id.setCode(row.getCell(1).getStringCellValue());
			id.setName(row.getCell(2).getStringCellValue());
			hs300stock.setId(id);
			hs300stock.setImpDate(sdf1.parse(us.getDate()));
			hs300stock.setImpOperator("admin");//默认
			hs300stock.setUpdateDate(null);
			hs300stock.setUpdateOperator(null);
			if(info.getDatadate()==null||"".equals(info.getDatadate())){
				System.out.println("取数截止日期未录入。");
			}else{
				hs300stock.setDataDate(sdf2.parse(info.getDatadate()));							
			}
			hs300stock.setRank(new BigDecimal(ExcelUtil.getCellValue(row.getCell(0)).replaceAll(",", "")));
			hs300stock.setClose(new BigDecimal(ExcelUtil.getCellValue(row.getCell(3)).replaceAll(",", "")));
			hs300stock.setWeight(new BigDecimal(ExcelUtil.getCellValue(row.getCell(4)).replaceAll(",", "")));
			if(!"--".equals(ExcelUtil.getCellValue(row.getCell(5)).replaceAll(",", ""))){//特殊处理				
				hs300stock.setUpOrDowm(new BigDecimal(ExcelUtil.getCellValue(row.getCell(5)).replaceAll(",", "")));
			}
			if(!"--".equals(ExcelUtil.getCellValue(row.getCell(6)).replaceAll(",", ""))){				
				hs300stock.setChangeFloat(new BigDecimal(ExcelUtil.getCellValue(row.getCell(6)).replaceAll(",", "")));
			}
			hs300stock.setIndexContributePoint(new BigDecimal(ExcelUtil.getCellValue(row.getCell(7)).replaceAll(",", "")));
			hs300stock.setVolume(new BigDecimal(ExcelUtil.getCellValue(row.getCell(8)).replaceAll(",", "")));
			hs300stock.setTurnVolume(new BigDecimal(ExcelUtil.getCellValue(row.getCell(9)).replaceAll(",", "")));
			hs300stock.setTotalShares(new BigDecimal(ExcelUtil.getCellValue(row.getCell(10)).replaceAll(",", "")));
			hs300stock.setFlowShares(new BigDecimal(ExcelUtil.getCellValue(row.getCell(11)).replaceAll(",", "")));
			hs300stock.setTotalMarketValue(new BigDecimal(ExcelUtil.getCellValue(row.getCell(12)).replaceAll(",", "")));
			hs300stock.setFlowMarketValue(new BigDecimal(ExcelUtil.getCellValue(row.getCell(13)).replaceAll(",", "")));
			hs300stock.setCommissionIndustry(row.getCell(14).getStringCellValue());
			hs300stock.setWindIndustry(row.getCell(15).getStringCellValue());

			//根据id查询
			Cux_Sino_Interface_HS300Stock ahs300stock=(Cux_Sino_Interface_HS300Stock) cuxdao.queryByID(hs300stock.getClass(), id);
			if(ahs300stock!=null){
				//删除旧的数据
				System.out.println("删除");
				cuxdao.remove(ahs300stock);
				//添加新数据
				System.out.println("新增");
				cuxdao.save(hs300stock);
			}else{
				System.out.println("新增");
				cuxdao.save(hs300stock);				
			}		
		}
	}
	@Transactional
	public void saveDataBond(List<Row> list,ImpDataInfoDTO info) throws ParseException{
		for(int i=0;i<list.size();i++){
			System.out.println("总共"+list.size()+"行，第"+(i+1)+"行：");
			Row row=list.get(i);
			if(row.getCell(0).getStringCellValue()==null||"".equals(row.getCell(0).getStringCellValue())){
				System.out.println("这一行无数据，自动跳过该行。");
				continue;
			}
			Cux_Sino_Interface_BondInfo bondinfo = new Cux_Sino_Interface_BondInfo();
			Cux_Sino_Interface_BondInfoId id = new Cux_Sino_Interface_BondInfoId();
			id.setBondType(info.getInterfacetable());
			id.setYearMonth(yearmonth);
			id.setSecurityCode(row.getCell(0).getStringCellValue());
			id.setSecurityName(row.getCell(1).getStringCellValue());
			bondinfo.setId(id);
			bondinfo.setImpDate(sdf1.parse(us.getDate()));
			bondinfo.setImpOperator("admin");
			bondinfo.setUpdateDate(null);
			bondinfo.setUpdateOperator(null);
			if(info.getDatadate()==null||"".equals(info.getDatadate())){
				System.out.println("取数截止日期未录入。");
			}else{
				bondinfo.setDataDate(sdf2.parse(info.getDatadate()));							
			}
			bondinfo.setIssuerCname(row.getCell(2).getStringCellValue());
			bondinfo.setDebtLevel(row.getCell(3).getStringCellValue());
			if(ExcelUtil.getCellValue(row.getCell(4))!=""){				
				bondinfo.setClosingPriceCorrectDuration(new BigDecimal(ExcelUtil.getCellValue(row.getCell(4))));
			}
			if(ExcelUtil.getCellValue(row.getCell(5))!=""){				
				bondinfo.setCouponRate(new BigDecimal(ExcelUtil.getCellValue(row.getCell(5))));
			}
			bondinfo.setInterestStartDate(row.getCell(6).getDateCellValue());
			bondinfo.setDeadlineForInterest(row.getCell(7).getDateCellValue());
			if(ExcelUtil.getCellValue(row.getCell(8))!=""){				
				bondinfo.setInterestPayRateOfYear(new BigDecimal(ExcelUtil.getCellValue(row.getCell(8))));
			}
			bondinfo.setRateType(row.getCell(9).getStringCellValue());
			bondinfo.setBaseInterestRateName(row.getCell(10).getStringCellValue());
			bondinfo.setRightDebt(row.getCell(11).getStringCellValue());
            if(row.getCell(12).getCellType()==0){//数值型
            	bondinfo.setNextExerciseDate(sdf2.format(row.getCell(12).getDateCellValue()));
            }else if(row.getCell(12).getCellType()==1){//字符串           	
            	bondinfo.setNextExerciseDate(ExcelUtil.getCellValue(row.getCell(12)));
            }else if(row.getCell(12).getCellType()==3){//空值
            	bondinfo.setNextExerciseDate(row.getCell(12).getStringCellValue());
            }

          //根据id查询
            Cux_Sino_Interface_BondInfo abondinfo=(Cux_Sino_Interface_BondInfo) cuxdao.queryByID(bondinfo.getClass(), id);
			if(abondinfo!=null){
				//删除旧的数据
				System.out.println("删除");
				cuxdao.remove(abondinfo);
				//添加新数据
				System.out.println("新增");
				cuxdao.save(bondinfo);
			}else{
				System.out.println("新增");
				cuxdao.save(bondinfo);				
			}		
		}
	}
	public void getYearmonth(ImpDataInfoDTO info) {
		String year = info.getYear();
		String quarter = info.getQuarter();
		if("1".equals(quarter)){
			this.yearmonth=year+"03";
		}else if("2".equals(quarter)){
			this.yearmonth=year+"06";
		}else if("3".equals(quarter)){
			this.yearmonth=year+"09";
		}else if("4".equals(quarter)){
			this.yearmonth=year+"12";
		}
	}
	
	//接口数据处理
    @Transactional
  	public String[] dealInterfaceData(String year, String quarter,
  			String year_month, String impdate) {
  		// TODO Auto-generated method stub
  		String[] result = new String[2];
  		result[0] = "0";
  		
  		List<?> sqlList =  cuxdao.queryBysql("select tablecode, rulesql, delsql, params from listrule where delsql is not null and rulesql is not null");
  		
  		Map<String, Object> map;
  		String delsql = "";
  		String insql = "";
  		
  		for (Object obj : sqlList) {
  			map = (Map<String, Object>) obj;
  			delsql = map.get("delsql").toString();
  			insql = map.get("rulesql").toString();
  			String _params = map.get("params") == null ? "" :  map.get("params").toString();
  			System.out.println("=========接口表" + map.get("tablecode").toString() + "处理Go=========");
  			if(!_params.equals("")){
  				Object[] params = sortParams(year, quarter, year_month, impdate, map.get("params").toString().split(","));
  				
  				int delparamcount = getParamsNum(delsql);
  				int inparamcount = getParamsNum(delsql);
  				if(delparamcount != params.length){
  					result[1] = "sql实际需要参数个数与传递参数个数不一致";
  					return result;
  				}
  				
  				if(inparamcount != params.length){
  					result[1] = "sql实际需要参数个数与传递参数个数不一致";
  					return result;
  				}
  				
  				
  				try{
  					cuxdao.commonUpdate(delsql, params);
  				}catch(Exception e){
  					LOGGER.error("============接口数据处理删除中间表数据出现异常============");
  					LOGGER.error(e.getMessage());
  					result[1] = "接口数据处理失败";
  					return result;
  				}
  				
  				try{
  					cuxdao.commonUpdate(insql, params);
  				}catch(Exception e){
  					LOGGER.error("============接口数据处理从接口表取数到中间表出现异常============");
  					LOGGER.error(e.getMessage());
  					result[1] = "接口数据处理失败";
  					return result;
  				}
  			}
  			
  			System.out.println("=========接口表" + map.get("tablecode").toString() + "处理End========");
  		}
  		result[0] = "1";
  		result[1] = "接口数据处理成功";
  		return result;
  	}
  //接口数据处理，配置参数整理
  	public Object[] sortParams(String year, String quarter, String year_month, String impdate, String[] srcparams){
  		Object[] params = new Object[srcparams.length];
  		for (int i = 0; i < srcparams.length; i++) {
  			if(srcparams[i].equals("year")){
  				params[i] = year;
  			}else if(srcparams[i].equals("quarter")){
  				params[i] = quarter;
  			}else if(srcparams[i].equals("yearmonth")){
  				params[i] = year_month;
  			}else if(srcparams[i].equals("datadate") ||  srcparams[i].equals("datetime")){
  				params[i] = impdate;
  			}
  		}
  		
  		return params;
  	}
  	
	//获取sql中参数个数
  	public int getParamsNum(String sql){
  		char[] ch = sql.toCharArray();
  		int i = 0;
  		for (char c : ch) {
  			if(c == '?'){
  				i++;
  			}
  		}
  		return i;
  	}
    public static void main(String[] args) {
    	List<Row> list;
    	ExcelUtil eu = new ExcelUtil(); 
		try {
			eu.setExcelPath("C:/Users/Administrator/Desktop/华夏2016一季度季报数据.xlsm");
			eu.setStartReadPos(0);
			//eu.setEndReadPos(0);			
			eu.setSelectedSheetName("A02#");
			list = eu.readExcel();

			for(int i=0;i<list.size();i++){
				Row row = list.get(i);
				System.out.println("第"+(i+1)+"行,最后一个单元格："+(row.getLastCellNum()+1));
				//System.out.println("==="+ExcelUtil.getCellValue(list.get(i).getCell(0))+"===");
				/*for(int j=0;j<row.getLastCellNum();j++){
					System.out.println("cell="+j);
					System.out.println(ExcelUtil.getCellValue(row.getCell(j)));
					System.out.println("单元格类型="+row.getCell(j).getCellType());
		            if(row.getCell(j).getCellType()==0){//数值型
		            	System.out.println(ExcelUtil.getCellValue(row.getCell(j)));	
		            }else if(row.getCell(j).getCellType()==1){//字符串           	
		            	System.out.println(ExcelUtil.getCellValue(row.getCell(j)));	
		            }
				}*/
			}
			list.removeAll(list);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
