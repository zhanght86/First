package com.sinosoft.zcfz.service.impl.interfaces;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.Row;
import org.freehep.graphicsio.swf.SWFAction.StringGreater;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sinosoft.common.Dao;
import com.sinosoft.dao.InterTabFinaDateInfoDao;
import com.sinosoft.dao.InterTabFinaEnValueDao;
import com.sinosoft.dao.InterTabFinaInfoDao;
import com.sinosoft.dao.InterTabInvestInfoDao;
import com.sinosoft.dao.InterfaceDealDao;
import com.sinosoft.zcfz.dao.interfaces.Cux_Sino_Interface_DAO;
import com.sinosoft.zcfz.dao.reportformcompute.CalCfReportInfoDao;
import com.sinosoft.zcfz.dto.interfase.ImpDataInfoDTO;
import com.sinosoft.entity.CalCfReportInfo;
import com.sinosoft.entity.InterTabFinaDateInfo;
import com.sinosoft.entity.InterTabFinaDateInfoId;
import com.sinosoft.entity.InterTabFinaEnValue;
import com.sinosoft.entity.InterTabFinaEnValueId;
import com.sinosoft.entity.InterTabFinaInfo;
import com.sinosoft.entity.InterTabFinaInfoId;
import com.sinosoft.entity.InterTabInvestInfo;
import com.sinosoft.entity.InterTabInvestInfoId;
import com.sinosoft.zcfz.service.impl.interfase.Cux_Sino_Interface_ServiceImpl;
import com.sinosoft.zcfz.service.interfaces.InterfaceService;
import com.sinosoft.util.ExcelUtil;
import com.sinosoft.util.UploaderServlet;
@Service
public class InterfaceServiceImpl  implements  InterfaceService{
	private static final Logger LOGGER = LoggerFactory
			.getLogger(Cux_Sino_Interface_ServiceImpl.class);
	private DateFormat  sdf2 = new SimpleDateFormat( "yyyy-MM-dd" );	
	private SimpleDateFormat sdf = new SimpleDateFormat( "yyyyMMdd" );	
	@Resource
	private UploaderServlet us;
	@Resource
	private ExcelUtil eu;
	@Resource
	private ExcelUtil eu2;
	@Resource
	private InterTabInvestInfoDao investInfoDao;
	@Resource
	private CalCfReportInfoDao calCfReportInfoDao;
	@Resource
	private InterTabFinaInfoDao finaInfoDao;
	@Resource
	private InterTabFinaEnValueDao finaEnValueDao;
	@Resource
	private InterTabFinaDateInfoDao finaDateInfoDao;
	@Resource
	private Dao dao;
	@Resource
    private InterfaceDealDao  interfaceDealDao;
	
	@Transactional
	public boolean saveFinaData(ImpDataInfoDTO info) throws IOException, ParseException {
		// TODO Auto-generated method stub
		System.out.println(info.getFilePath()+"/"+us.getFileName());
		eu.setExcelPath(info.getFilePath()+"/"+us.getFileName());
		String interfacetype = info.getInterfacetype();
		if(interfacetype.equals("1")){//财务底稿数据
			//科目汇总表数据
			eu.setExcelPath(info.getFilePath()+"/"+us.getFileName());
			eu.setStartReadPos(5);
			eu.setEndReadPos(0);			
			eu.setSelectedSheetName("科目汇总表");
			List<Row> listFinanceItem=eu.readExcel();
			saveItemData(listFinanceItem,info);
			
			//资产负债表数据
			eu.setExcelPath(info.getFilePath()+"/"+us.getFileName());
			eu.setStartReadPos(9);
			eu.setEndReadPos(0);			
			eu.setSelectedSheetName("资产负债");
			List<Row> listFinanceFZ=eu.readExcel();
			saveFZData(listFinanceFZ,info);
			
			//其他资产表数据
			eu.setExcelPath(info.getFilePath()+"/"+us.getFileName());
			eu.setStartReadPos(1);
			eu.setEndReadPos(0);			
			eu.setSelectedSheetName("其他资产");
			List<Row> listFinanceOther=eu.readExcel();
			saveOtherData(listFinanceOther,info);
			
			//固定资产表数据
			eu.setExcelPath(info.getFilePath()+"/"+us.getFileName());
			eu.setStartReadPos(9);
			eu.setEndReadPos(0);			
			eu.setSelectedSheetName("固定资产");
			List<Row> listFinanceAsset=eu.readExcel();
			saveAssetData(listFinanceAsset,info);
			
			listFinanceItem.removeAll(listFinanceItem);
			return true;
		}else if(interfacetype.equals("2")||interfacetype.equals("4")||interfacetype.equals("5")){//全账套估值表数据
			//估值表数据
			eu.setExcelPath(info.getFilePath()+"/"+us.getFileName());
			eu.setStartReadPos(4);
			eu.setEndReadPos(0);			
			eu.setSelectedSheetName("估值表");
			List<Row> listFinaEnValue=eu.readExcel();
			saveEnValueData(listFinaEnValue,info);
			
			listFinaEnValue.removeAll(listFinaEnValue);
			return true;
			
		}else if(interfacetype.equals("3")){//到期日数据
			//到期日数据-为了获取剩余年限
			eu.setExcelPath(info.getFilePath()+"/"+us.getFileName());
			eu.setStartReadPos(1);
			eu.setEndReadPos(0);			
			eu.setSelectedSheetName("领息日");
			List<Row> listFinaDates=eu.readExcel();
			saveDateInfo(listFinaDates,info);
			
			listFinaDates.removeAll(listFinaDates);
			return true;
			
		}else{
			System.out.println("接口类别未识别。");
			return false;
		}
		
	}
	@Transactional//保存科目余额数据
    public void saveItemData(List<Row> list,ImpDataInfoDTO info) throws ParseException{
		//删除科目明细账报表中某年某季度的数据
		//String reportId = info.getRequest().getParameter("reportid");
		String reportId =info.getReportId();
		CalCfReportInfo ccri=new CalCfReportInfo();
		ccri=(CalCfReportInfo) dao.get(CalCfReportInfo.class, reportId);

		String year = ccri.getYear();
		String quarter = ccri.getQuarter();
		String reportDate = ccri.getDataDate();
		String delSql="delete from intertabfinainfo t where t.tabName='科目汇总表' "
		             +" and t.reportid='"+reportId+"'";
		
		finaInfoDao.excute(delSql);
		System.out.println("删除财务底稿表中报送号为"+reportId+"的科目余额表数据。");
		
		for(int i=0;i<list.size();i++){
			System.out.println("总共"+list.size()+"行，第"+(i+1)+"行：");
			
			Row row=list.get(i);
			//如果第一列没有科目，则自动跳过该行数据 
			if(ExcelUtil.getCellValue(row.getCell(0))==null||(ExcelUtil.getCellValue(row.getCell(0)).equals(""))){
				System.out.println("这一行无数据，自动跳过该行。");
				continue;
			}
			InterTabFinaInfo finaInfo = new InterTabFinaInfo();	
			InterTabFinaInfoId id = new InterTabFinaInfoId();		
			
			String itemCode = ExcelUtil.getCellValue(row.getCell(0));//证券代码
			String itemName = ExcelUtil.getCellValue(row.getCell(1)).trim();//证券名称	
			if(itemName==null || itemName.equals("")){
				itemName = itemCode;
			}
			String QCSum = ExcelUtil.getCellValue(row.getCell(2));
			if(QCSum==null || QCSum.equals("")){
				QCSum = "0";
			}
			String QMSum = ExcelUtil.getCellValue(row.getCell(5));
			if(QMSum==null || QMSum.equals("")){
				QMSum = "0";
			}
			
			id.setReportId(reportId);
			id.setItemCode(itemCode);
			id.setTabName("科目汇总表");
			id.setItemName(itemName);
			
			finaInfo.setYear(year);
			finaInfo.setQuarter(quarter);
			finaInfo.setReportDate(reportDate);
			finaInfo.setQcSum(new BigDecimal(QCSum));
			finaInfo.setQmSum(new BigDecimal(QMSum));
			finaInfo.setId(id);
			
			finaInfoDao.save(finaInfo);
			
		}
		
	}
	
	@Transactional//保存资产负债表数据
    public void saveFZData(List<Row> list,ImpDataInfoDTO info) throws ParseException{
		//删除科目明细账报表中某年某季度的数据
		//String reportId = info.getRequest().getParameter("reportid");
		String reportId =info.getReportId();
		CalCfReportInfo ccri=new CalCfReportInfo();
		ccri=(CalCfReportInfo) dao.get(CalCfReportInfo.class, reportId);

		String year = ccri.getYear();
		String quarter = ccri.getQuarter();
		String reportDate = ccri.getDataDate();
		String delSql="delete from intertabfinainfo t where t.tabName='资产负债表' "
		             +" and t.reportid='"+reportId+"'";
		
		finaInfoDao.excute(delSql);
		System.out.println("删除财务底稿表中报送号为"+reportId+"的资产负债表数据。");
		for(int i=0;i<list.size();i++){
			System.out.println("总共"+list.size()+"行，第"+(i+1)+"行：");
			Row row=list.get(i);
			//如果第一列没有科目，则自动跳过该行数据 
			if(ExcelUtil.getCellValue(row.getCell(0))==null||(ExcelUtil.getCellValue(row.getCell(0)).equals(""))){
				System.out.println("这一行无数据，自动跳过该行。");
				continue;
			}
			InterTabFinaInfo finaInfo = new InterTabFinaInfo();	
			InterTabFinaInfoId id = new InterTabFinaInfoId();		
			
			String itemCode = ExcelUtil.getCellValue(row.getCell(0)).trim();//资产名称
			
			String QCSum = ExcelUtil.getCellValue(row.getCell(1));//期初数据
			if(QCSum==null || QCSum.equals("")){
				QCSum = "0";
			}
			String QMSum = ExcelUtil.getCellValue(row.getCell(2));//期末数据
			if(QMSum==null || QMSum.equals("")){
				QMSum = "0";
			}
			
			id.setReportId(reportId);
			id.setItemCode(itemCode);
			id.setItemName(itemCode);
			id.setTabName("资产负债表");
			
			finaInfo.setYear(year);
			finaInfo.setQuarter(quarter);
			finaInfo.setReportDate(reportDate);
			finaInfo.setQcSum(new BigDecimal(QCSum));
			finaInfo.setQmSum(new BigDecimal(QMSum));
			finaInfo.setId(id);
			
			finaInfoDao.save(finaInfo);
			
		}
	}
	@Transactional//保存其他资产表数据
    public void saveOtherData(List<Row> list,ImpDataInfoDTO info) throws ParseException{
		//删除科目明细账报表中某年某季度的数据
		//String reportId = info.getRequest().getParameter("reportid");
		String reportId =info.getReportId();
		CalCfReportInfo ccri=new CalCfReportInfo();
		ccri=(CalCfReportInfo) dao.get(CalCfReportInfo.class, reportId);

		String year = ccri.getYear();
		String quarter = ccri.getQuarter();
		String reportDate = ccri.getDataDate();
		String delSql="delete from intertabfinainfo t where t.tabName='其他资产表' "
		             +" and t.reportid='"+reportId+"'";
		
		finaInfoDao.excute(delSql);
		System.out.println("删除财务底稿表中报送号为"+reportId+"的其他资产表数据。");
		for(int i=0;i<list.size();i++){
			System.out.println("总共"+list.size()+"行，第"+(i+1)+"行：");
			Row row=list.get(i);
			//如果第一列没有科目，则自动跳过该行数据 
			if(ExcelUtil.getCellValue(row.getCell(0))==null||(ExcelUtil.getCellValue(row.getCell(0)).equals(""))){
				System.out.println("这一行无数据，自动跳过该行。");
				continue;
			}
			InterTabFinaInfo finaInfo = new InterTabFinaInfo();	
			InterTabFinaInfoId id = new InterTabFinaInfoId();		
			
			String itemCode = ExcelUtil.getCellValue(row.getCell(0)).trim();//资产名称
			String QCSum = ExcelUtil.getCellValue(row.getCell(1));//期初数据
			if(QCSum==null || QCSum.equals("")){
				QCSum = "0";
			}
			String QMSum = ExcelUtil.getCellValue(row.getCell(2));//期末数据
			if(QMSum==null || QMSum.equals("")){
				QMSum = "0";
			}
			
			id.setReportId(reportId);
			id.setItemCode(itemCode);
			id.setItemName(itemCode);
			id.setTabName("其他资产表");
			
			finaInfo.setYear(year);
			finaInfo.setQuarter(quarter);
			finaInfo.setReportDate(reportDate);
			finaInfo.setQcSum(new BigDecimal(QCSum));
			finaInfo.setQmSum(new BigDecimal(QMSum));
			finaInfo.setId(id);
			
			finaInfoDao.save(finaInfo);
			
		}
	}
	@Transactional//保存固定资产表数据
    public void saveAssetData(List<Row> list,ImpDataInfoDTO info) throws ParseException{
		//删除科目明细账报表中某年某季度的数据
		//String reportId = info.getRequest().getParameter("reportid");
		String reportId =info.getReportId();
		CalCfReportInfo ccri=new CalCfReportInfo();
		ccri=(CalCfReportInfo) dao.get(CalCfReportInfo.class, reportId);

		String year = ccri.getYear();
		String quarter = ccri.getQuarter();
		String reportDate = ccri.getDataDate();
		String delSql="delete from intertabfinainfo t where t.tabName='固定资产表' "
		             +" and t.reportid='"+reportId+"'";
		
		finaInfoDao.excute(delSql);
		System.out.println("删除财务底稿表中报送号为"+reportId+"的固定资产表数据。");
		for(int i=0;i<list.size();i++){
			System.out.println("总共"+list.size()+"行，第"+(i+1)+"行：");
			Row row=list.get(i);
			//如果第一列没有科目，则自动跳过该行数据 
			if(ExcelUtil.getCellValue(row.getCell(0))==null||(ExcelUtil.getCellValue(row.getCell(0)).equals(""))){
				System.out.println("这一行无数据，自动跳过该行。");
				continue;
			}
			InterTabFinaInfo finaInfo = new InterTabFinaInfo();	
			InterTabFinaInfoId id = new InterTabFinaInfoId();		
			
			String itemCode = ExcelUtil.getCellValue(row.getCell(0)).trim();//资产名称
			
			String QMSum = ExcelUtil.getCellValue(row.getCell(1));//期末数据
			if(QMSum==null || QMSum.equals("")){
				QMSum = "0";
			}
			
			id.setReportId(reportId);
			id.setItemCode(itemCode);
			id.setItemName(itemCode);
			id.setTabName("固定资产表");
			
			finaInfo.setYear(year);
			finaInfo.setQuarter(quarter);
			finaInfo.setReportDate(reportDate);
			finaInfo.setQmSum(new BigDecimal(QMSum));
			finaInfo.setId(id);
			
			finaInfoDao.save(finaInfo);
			
		}
	}
	
	@Transactional//保存全账套-估值表数据
    public void saveEnValueData(List<Row> list,ImpDataInfoDTO info) throws ParseException{
		//删除科目明细账报表中某年某季度的数据
		String reportId = info.getReportId();
		
		CalCfReportInfo ccri=new CalCfReportInfo();
		ccri=(CalCfReportInfo) dao.get(CalCfReportInfo.class, reportId);

		String year = ccri.getYear();
		String quarter = ccri.getQuarter();
		String reportDate = ccri.getDataDate();
		
		String enValueType=info.getInterfacetype();
		//删除全账套、万能险、分红险下估值表数据
		if(enValueType=="2"||enValueType.equals("2")){
			String delSql="delete from intertabfinaenvalue t where t.accountType='全账套' "
		             +" and t.reportid='"+reportId+"'";
			finaInfoDao.excute(delSql);
			System.out.println("删除财务估值表中报送号为"+reportId+"的全账套数据。");

		}else if(enValueType=="4"||enValueType.equals("4")){
			String delSql="delete from intertabfinaenvalue t where t.accountType='万能险' "
		             +" and t.reportid='"+reportId+"'";
			finaInfoDao.excute(delSql);
			System.out.println("删除财务估值表中报送号为"+reportId+"的万能险数据。");
			
		}else if(enValueType=="5"||enValueType.equals("5")){
			String delSql="delete from intertabfinaenvalue t where t.accountType='分红险' "
		             +" and t.reportid='"+reportId+"'";
			finaInfoDao.excute(delSql);
			System.out.println("删除财务估值表中报送号为"+reportId+"的分红险数据。");

		}	
		
		//定义map用来保存添加市场类型的匹配代码
		Map<String, String> mmpCode = new HashMap<String, String>();

		for(int i=0;i<list.size();i++){
			System.out.println("总共"+list.size()+"行，第"+(i+1)+"行：");
			Row row=list.get(i);
			//如果第一列没有科目，则自动跳过该行数据 
			if(ExcelUtil.getCellValue(row.getCell(0))==null||(ExcelUtil.getCellValue(row.getCell(0)).equals(""))){
				System.out.println("这一行无数据，自动跳过该行。");
				continue;
			}
			InterTabFinaEnValue finaEnValue = new InterTabFinaEnValue();	
			InterTabFinaEnValueId id = new InterTabFinaEnValueId();		
			
			String itemCode = ExcelUtil.getCellValue(row.getCell(0)).trim();//科目编码
			
			String itemName = ExcelUtil.getCellValue(row.getCell(1)).trim();//科目名称
			System.out.println("--------------"+itemName);
			//市场类型分为：上海、深圳、银行间 如果包含类似的字样，则添加到map中
			if(itemName.contains("上海")){
				mmpCode.put(itemCode, "上海");
			}else if(itemName.contains("深圳")){
				mmpCode.put(itemCode, "深圳");
			}else if(itemName.contains("银行间")){
				mmpCode.put(itemCode, "银行间");
			}
			String enCounts = ExcelUtil.getCellValue(row.getCell(2));//数量
			if(enCounts==null || enCounts.equals("")){
				enCounts="0";
			}
			String enCosts = ExcelUtil.getCellValue(row.getCell(4));//成本
			if(enCosts==null || enCosts.equals("")){
				enCosts = "0";
			}
			String enValue = ExcelUtil.getCellValue(row.getCell(7));//市值
			if(enValue==null || enValue.equals("")){
				enValue = "0";
			}			
			//报送号
			id.setReportId(reportId);
			id.setItemCode(itemCode);
			//险种属性
			if(enValueType=="2"||enValueType.equals("2")){
				id.setAccountType("全账套");
			}else if(enValueType=="4"||enValueType.equals("4")){
				id.setAccountType("万能险");				
			}else if(enValueType=="5"||enValueType.equals("5")){
				id.setAccountType("分红险");
			}				
			
			finaEnValue.setYear(year);
			finaEnValue.setQuarter(quarter);
			finaEnValue.setReportDate(reportDate);
			finaEnValue.setItemName(itemName);
			finaEnValue.setEnCounts(new BigDecimal(enCounts));
			finaEnValue.setEnCosts(new BigDecimal(enCosts));
			finaEnValue.setEnValue(new BigDecimal(enValue));
			//如果科目编码长度大于12位，则添加市场类型
			String mapItemCode = "12";
			if(itemCode.length()>12){
				mapItemCode=itemCode.substring(0, 13);				
			}
			if(mmpCode.containsKey(mapItemCode)){
				id.setTemp2(mmpCode.get(mapItemCode).toString());
			}else{
				id.setTemp2("0");
			}
			
			//取科目编码的后六位做产品编码，如遇到A-转换-10 B-转换-11..等等...
			String properCodeSix = null;
			if(itemCode.length()>14){
				properCodeSix=itemCode.substring(13);
				properCodeSix = replaceABC(properCodeSix);
			}
			finaEnValue.setTemp1(properCodeSix);

			finaEnValue.setId(id);
			
			finaEnValueDao.save(finaEnValue);
			
		}
	}
	
	@Transactional
	public void saveDateInfo(List<Row> list,ImpDataInfoDTO info) throws ParseException{
		
		Map<String, String> codMap = new HashMap<String, String>();
		//删除科目明细账报表中某年某季度的数据
		String reportId = info.getReportId();

		CalCfReportInfo ccri=new CalCfReportInfo();
		ccri=(CalCfReportInfo) dao.get(CalCfReportInfo.class, reportId);

		String year = ccri.getYear();
		String quarter = ccri.getQuarter();
		String reportDate = ccri.getDataDate();
		String delSql="delete from interTabFinaDateInfo t where t.accountType='全账套' "
		             +" and t.reportid='"+reportId+"'";
		
		finaInfoDao.excute(delSql);
		System.out.println("删除财务到期日中报送号为"+reportId+"的全账套数据。");
		
		String currDate = null;
		String currDate1 = null;
		if(quarter.equals("1")){
			currDate="0331";
			currDate1="03-31";
		}else if(quarter.equals("2")){
			currDate="0630";
			currDate1="06-30";
		}else if(quarter.equals("3")){
			currDate="0930";
			currDate1="09-30";
		}else if(quarter.equals("4")){
			currDate="1231";
			currDate1="12-31";
		}
		currDate = year+currDate;
		currDate1 = year+"-"+currDate1;
		Date currDateF = sdf2.parse(currDate1);
		
		for(int i=0;i<list.size();i++){
			System.out.println("总共"+list.size()+"行，第"+(i+1)+"行：");
			Row row=list.get(i);
			//如果第一列没有科目，则自动跳过该行数据 
			if(ExcelUtil.getCellValue(row.getCell(0))==null||(ExcelUtil.getCellValue(row.getCell(0)).equals(""))){
				System.out.println("这一行无数据，自动跳过该行。");
				continue;
			}
			InterTabFinaDateInfo finaDateInfo = new InterTabFinaDateInfo();	
			InterTabFinaDateInfoId id = new InterTabFinaDateInfoId();				
			
			String properCode = ExcelUtil.getCellValue(row.getCell(2)).trim();//债券编码
			String properName = ExcelUtil.getCellValue(row.getCell(3)).trim();//债券名称
			String endDate = null;//到期日
			Date endDateF = row.getCell(5).getDateCellValue();
			if(endDateF==null){
				continue;
			}
			endDate = sdf.format(endDateF);
			
			if(codMap.containsKey(properCode)){
				continue;
			}else{
				codMap.put(properCode, properCode);
			}
			
			id.setReportId(reportId);
			id.setProperCode(properCode);
			id.setAccountType("全账套");
			
			finaDateInfo.setYear(year);
			finaDateInfo.setQuarter(quarter);
			finaDateInfo.setReportDate(reportDate);
			finaDateInfo.setEndDate(endDate);
			finaDateInfo.setCurrDate(currDate);
			finaDateInfo.setProperName(properName);
			finaDateInfo.setId(id);
			
			int days = calInterval(currDateF, endDateF, "D");
			
			finaDateInfo.setTemp1(String.valueOf(days));
			
			finaDateInfoDao.save(finaDateInfo);
			
		}
	}
	
    @Transactional
	public boolean saveInvestData(ImpDataInfoDTO info) throws IOException, ParseException {
		// TODO Auto-generated method stub
		System.out.println(info.getFilePath()+"/"+us.getFileName());
		eu.setExcelPath(info.getFilePath()+"/"+us.getFileName());
		String interfacetype = info.getInterfacetype();
		interfacetype = "1";
		if(interfacetype.equals("1")){//投资明细表
			eu.setExcelPath(info.getFilePath()+"/"+us.getFileName());
			eu.setStartReadPos(1);
			eu.setEndReadPos(0);			
			eu.setSelectedSheetName("投资中间表");
			List<Row> listFinance=eu.readExcel();
			saveInvestInfo(listFinance, info);//处理投资中间表数据			
			listFinance.removeAll(listFinance);
			return true;
		}else{
			System.out.println("接口类别未识别。");
			return false;
		}
	}
    @Transactional
    public void saveInvestInfo(List<Row> list,ImpDataInfoDTO info) throws ParseException{
		
		//删除科目明细账报表中某年某季度的数据
		String reportId = info.getReportId();
		CalCfReportInfo ccri=new CalCfReportInfo();
		ccri=(CalCfReportInfo) dao.get(CalCfReportInfo.class, reportId);

		String year = ccri.getYear();
		String quarter = ccri.getQuarter();
		String reportDate = ccri.getDataDate();
		String delSql="delete from intertabinvestinfo t where t.year='"+year+"'"
		             +" and t.quarter='"+quarter+"'"
		             +" and t.reportid='"+reportId+"'";
		
		investInfoDao.excute(delSql);
		System.out.println("删除投资资产明细表中报送号为"+reportId+"的数据。");
		for(int i=0;i<list.size();i++){
			System.out.println("总共"+list.size()+"行，第"+(i+1)+"行：");
			Row row=list.get(i);
			//如果第一列没有科目，则自动跳过该行数据 
			if(ExcelUtil.getCellValue(row.getCell(0))==null||(ExcelUtil.getCellValue(row.getCell(0)).equals(""))){
				System.out.println("这一行无数据，自动跳过该行。");
				continue;
			}
			InterTabInvestInfo investDetailInfo = new InterTabInvestInfo();	
			InterTabInvestInfoId id = new InterTabInvestInfoId();		
			
			String properCode = ExcelUtil.getCellValue(row.getCell(0));//证券代码
			String ProperName = ExcelUtil.getCellValue(row.getCell(1));//证券名称			
			String issurer = ExcelUtil.getCellValue(row.getCell(2));//发行方
			String fixedDuration = ExcelUtil.getCellValue(row.getCell(3));//修正久期
			String properCreditLevel = ExcelUtil.getCellValue(row.getCell(4));//资产的信用评级
			String marketCategory = ExcelUtil.getCellValue(row.getCell(5));//市场类型
			//调整信用评级，AAA 转换成 1|AAA 
			if(properCreditLevel!=null && !properCreditLevel.equals("")){
				properCreditLevel = replaceAA(properCreditLevel);
			}
			
			id.setProperCode(properCode);
			id.setReportId(reportId);
			id.setAccounts("全账户");
			id.setTemp1(marketCategory);

			
			investDetailInfo.setYear(year);
			investDetailInfo.setQuarter(quarter);
			investDetailInfo.setReportDate(reportDate);
			investDetailInfo.setId(id);
			investDetailInfo.setProperName(ProperName);
			investDetailInfo.setCreditLevel(properCreditLevel);
			investDetailInfo.setIssurer(issurer);
			investDetailInfo.setFixedDuration(fixedDuration);
			investInfoDao.save(investDetailInfo);
			
		}
    }
    @Transactional
    /***********************
     * 系统自动将 信用评级替换成带序号的
     * **********************/
    public String replaceAA(String para) throws ParseException{
    	
    	String oldString  = para;
    	if(oldString.equals("AAA")){
    		oldString="1|AAA";
    	}else if(oldString.equals("AA+")){
    		oldString="2|AA+";
    	}else if(oldString.equals("AA")){
    		oldString="3|AA";
    	}else if(oldString.equals("AA-")){
    		oldString="4|AA-";
    	}else if(oldString.equals("A+")){
    		oldString="5|A+";
    	}else if(oldString.equals("A")){
    		oldString="6|A";
    	}else if(oldString.equals("A-")){
    		oldString="7|A-";
    	}else if(oldString.equals("BBB+")){
    		oldString="8|BBB+";
    	}else if(oldString.equals("BBB")){
    		oldString="9|BBB";
    	}else if(oldString.equals("BBB-")){
    		oldString="10|BBB-";
    	}else {
    		oldString="11|无评级";
    	}
    	return oldString;
    }
    @Transactional
    /***********************
     * 系统自动将 股票类型替换成带序号的
     * **********************/
    public String replaceHS(String para) throws ParseException{
    	
    	String oldString  = para;
    	if(oldString.equals("沪深主板股")){
    		oldString="1|沪深主板股";
    	}else if(oldString.equals("中小板股")){
    		oldString="2|中小板股";
    	}else if(oldString.equals("创业板股")){
    		oldString="3|创业板股";
    	}
    	return oldString;
    }
    @Transactional
    public String replaceABC(String para){
    	String itemCode  = para;
    	String code = itemCode.substring(0, 1);
    	String endCode = itemCode.substring(1);
    	Map<String,String> ABCMap = new HashMap<String, String>();
    	ABCMap.put("A", "10");
    	ABCMap.put("B", "11");
    	ABCMap.put("C", "12");
    	ABCMap.put("D", "13");
    	ABCMap.put("E", "14");
    	ABCMap.put("F", "15");
    	ABCMap.put("G", "16");
    	ABCMap.put("H", "17");
    	ABCMap.put("I", "18");
    	ABCMap.put("J", "19");
    	ABCMap.put("K", "20");
    	ABCMap.put("L", "21");
    	ABCMap.put("M", "22");
    	ABCMap.put("N", "23");
    	ABCMap.put("O", "24");
    	
    	if(ABCMap.containsKey(code)){
    		code = ABCMap.get(code).toString();
    	}
    	itemCode=code+endCode;
    	return itemCode;
    }
    
    /**
     * 通过起始日期和终止日期计算以时间间隔单位为计量标准的时间间隔 author: HST
     */
    @Transactional
    public static int calInterval(Date startDate, Date endDate, String unit) {
        int interval = 0;

        GregorianCalendar sCalendar = new GregorianCalendar();
        sCalendar.setTime(startDate);
        int sYears = sCalendar.get(Calendar.YEAR);
        int sMonths = sCalendar.get(Calendar.MONTH);
        int sDays = sCalendar.get(Calendar.DAY_OF_MONTH);
        int sDaysOfYear = sCalendar.get(Calendar.DAY_OF_YEAR);

        GregorianCalendar eCalendar = new GregorianCalendar();
        eCalendar.setTime(endDate);
        int eYears = eCalendar.get(Calendar.YEAR);
        int eMonths = eCalendar.get(Calendar.MONTH);
        int eDays = eCalendar.get(Calendar.DAY_OF_MONTH);
        int eDaysOfYear = eCalendar.get(Calendar.DAY_OF_YEAR);

        if (unit.equals("Y")) {
            interval = eYears - sYears;
            if (eMonths < sMonths) {
                interval--;
            } else {
                if (eMonths == sMonths && eDays < sDays) {
                    interval--;
                    if (eMonths == 1) { //如果同是2月，校验润年问题
                        if ((sYears % 4) == 0 && (eYears % 4) != 0) { //如果起始年是润年，终止年不是润年
                            if (eDays == 28) { //如果终止年不是润年，且2月的最后一天28日，那么补一
                                interval++;
                            }
                        }
                    }
                }
            }
        }
        if (unit.equals("M")) {
            interval = eYears - sYears;
            interval = interval * 12;

            interval = eMonths - sMonths + interval;
            if (eDays < sDays) {
                interval--;
                //eDays如果是月末，则认为是满一个月
                int maxDate = eCalendar.getActualMaximum(Calendar.DATE);
                if (eDays == maxDate) {
                    interval++;
                }
            }
        }
        if (unit.equals("D")) {
            interval = eYears - sYears;
            interval = interval * 365;
            interval = eDaysOfYear - sDaysOfYear + interval;

            // 处理润年
            int n = 0;
            eYears--;
            if (eYears > sYears) {
                int i = sYears % 4;
                if (i == 0) {
                    sYears++;
                    n++;
                }
                int j = (eYears) % 4;
                if (j == 0) {
                    eYears--;
                    n++;
                }
                n += (eYears - sYears) / 4;
            }
            if (eYears == sYears) {
                int i = sYears % 4;
                if (i == 0) {
                    n++;
                }
            }
            interval += n;
        }
        return interval;
    }
    
  //接口数据处理
    @Transactional
  	public String[] dealInterfaceData(String reportid,String impdate) {
  		// TODO Auto-generated method stub
  		String[] result = new String[2];
  		result[0] = "0";
  		
  		List<?> sqlList =  interfaceDealDao.queryBysql("select tablecode, rulesql, delsql, params,remark from listrule where delsql is not null and rulesql is not null");
  		
  		Map<String, Object> map;
  		String delsql = "";
  		String insql = "";
  		
  		for (Object obj : sqlList) {
  			map = (Map<String, Object>) obj;
  			delsql = map.get("delsql").toString();
  			insql = map.get("rulesql").toString();
  			String _params = map.get("params") == null ? "" :  map.get("params").toString();
  			String _params_del = map.get("remark") == null ? "" :  map.get("remark").toString();
  			System.out.println("=========接口表" + map.get("tablecode").toString() + "处理Go=========");
  			if(!_params.equals("")){
  				Object[] params = sortParams(reportid, impdate, map.get("params").toString().split(","));
  				Object[] params_del = sortParams(reportid, impdate, map.get("remark").toString().split(","));
  				
  				int delparamcount = getParamsNum(delsql);
  				int inparamcount = getParamsNum(insql);
  				if(delparamcount != params_del.length){
  					result[1] = "sql实际需要参数个数与传递参数个数不一致";
  					return result;
  				}
  				
  				if(inparamcount != params.length){
  					result[1] = "sql实际需要参数个数与传递参数个数不一致";
  					return result;
  				}
  				
  				
  				try{
  					interfaceDealDao.commonUpdate(delsql, params_del);
  				}catch(Exception e){
  					LOGGER.error("============接口数据处理删除中间表数据出现异常============");
  					LOGGER.error(e.getMessage());
  					result[1] = "接口数据处理失败";
  					return result;
  				}
  				
  				try{
  					interfaceDealDao.commonUpdate(insql, params);
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
  	public Object[] sortParams(String reportid,  String impdate, String[] srcparams){
  		Object[] params = new Object[srcparams.length];
  		for (int i = 0; i < srcparams.length; i++) {
  			if(srcparams[i].equals("reportid")){
  				params[i] = reportid;
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
   
}
