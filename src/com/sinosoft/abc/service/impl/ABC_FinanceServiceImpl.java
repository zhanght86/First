package com.sinosoft.abc.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javassist.expr.NewArray;

import javax.annotation.Resource;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sinosoft.abc.dao.ABC_DockedDao;
import com.sinosoft.abc.entity.Finance_ABC;
import com.sinosoft.abc.service.ABC_FinanceService;
import com.sinosoft.common.CurrentUser;
import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dto.reportdataimport.UploadInfoDTO;
import com.sinosoft.entity.CalCfReportInfo;
import com.sinosoft.entity.CfReportData;
import com.sinosoft.entity.CfReportDataId;
import com.sinosoft.entity.UserInfo;
import com.sinosoft.util.Config;
import com.sinosoft.util.ExcelUtil;
import com.sinosoft.util.UploaderServlet;

@Service("ABC_FinanceService")
public class ABC_FinanceServiceImpl implements ABC_FinanceService{
	private Log log = LogFactory.getLog(ABC_FinanceServiceImpl.class);
	
	@Resource
	private ABC_DockedDao dockedDao;
	@Resource
	private UploaderServlet uploaderServlet;

	
	public void isHasReportId(UploadInfoDTO dto) {
		List<Map> list = dockedDao.getReportId(dto.getYearmonth(), dto.getQuarter());
		if(list == null|| list.size() == 0){
			log.error(dto.getYearmonth()+"年, 第" +dto.getQuarter()+" 季度, 季度报告的报送号不存在，请先新建!");
			throw new RuntimeException(dto.getYearmonth()+"年, 第" +dto.getQuarter()+" 季度, 季度报告的报送号不存在，请先新建!");
		}
		
//		String reportId = list.get(0).get("reportId").toString();
		
	}
	
	/**
	 * 更新状态
	 */
	@Transactional
	public void updateState(UploadInfoDTO dto,String state) {
		String reportId = dto.getReportId();
		CalCfReportInfo calCfReportInfo=dockedDao.get(CalCfReportInfo.class, reportId);
		String reportStateString = calCfReportInfo.getReportState();
		
		//'0.4','财务接口数据已导入，投资未导入'
		if (reportStateString.equals("0") || reportStateString.equals("0.4") ) {
			calCfReportInfo.setReportState(state);
		} else {
			calCfReportInfo.setReportState("1");
		}
		
		try{
			dockedDao.update(calCfReportInfo);
		}catch(RuntimeException e){
			log.error("报送号"+reportId+"更新状态失败!");
		}
	}
	
	/**
	 * 上传 科目表
	 */
	@Transactional
	public boolean subjectImport(UploadInfoDTO dto) {
		// 如果导入同样的表，并且年、季度一样，则删除之前的数据
		if (!dockedDao.deleteSubjectData(dto)) {
			log.error("导入科目表时，删除当前条件下科目表数据出错！！！");
			return false;
		}
		
		//获取Excel文件数据
		List<Row> subjectList = getExcelData(dto,  12, 0);
		if (subjectList==null || subjectList.size()==0) {
			log.error("月度科目汇总账科目段及明细段表没有数据");
			return false;
		}
		
		if(!saveSubjectData(dto, subjectList, dto.getFileName(), "F01")){
			return false;
		}
		
		return true;
	}

	/**
	 * 上传财务数据表
	 */
	@Transactional
	public boolean financeImport(UploadInfoDTO dto) {
		// 如果导入同样的表，并且年、季度一样，则删除财务数据表之前的数据
		if (!dockedDao.deleteFinanceData(dto)) {
			log.error("导入科目表时，删除当前条件下财务数据表数据出错！！！");
			return false;
		}
		
		
		//获取Excel：<保户质押贷款-分险种>sheet表的数据
		List<Row> finaceList0 = getExcelData(dto, 0, "保户质押贷款-分险种");		
		if (finaceList0==null || finaceList0.size()==0) {
			log.error("<保户质押贷款-分险种>表没有数据");
			return false;
		}
		
		if(!saveFinanceData1(dto, finaceList0, "保户质押贷款-分险种", "F02")){
			log.error("保存<保存保户质押贷款-分险种>数据出错！");
			return false;
		}

		
		//获取Excel：<认可资产>sheet表的数据
		List<Row> finaceList1 = getExcelData(dto, 0, "资产负债");		
		if (finaceList1==null || finaceList1.size()==0) {
			log.error("<资产负债>表没有数据");
			return false;
		}
		
		if(!saveFinanceData2(dto, finaceList1, "资产负债", "F03")){
			log.error("保存<资产负债>数据出错！");
			return false;
		}

		
		//获取Excel：认可资产表（sheetName=3）
		List<Row> finaceList3 = getExcelData(dto, 0, "认可资产");		
		if (finaceList3==null || finaceList3.size()==0) {
			log.error("<认可资产表>表没有数据");
			return false;
		}
		
		if(!saveFinanceData3(dto, finaceList3, "认可资产", "F04")){
			log.error("<认可资产表>数据出错！");
			return false;
		}
		
		//认可负债表（sheetName=4）
		List<Row> finaceList4 = getExcelData(dto, 0,"认可负债");		
		if (finaceList4==null || finaceList4.size()==0) {
			log.error("<认可负债表>表没有数据");
			return false;
		}
		
		if(!saveFinanceData3(dto, finaceList4, "认可负债", "F05")){
			log.error("<认可负债表>数据出错！");
			return false;
		}
		
		
		return true;
	}
	 
	@Transactional
	public  boolean singleCalCR19(String year, String quarter){
		List<Map> list = dockedDao.getReportId(year, quarter);
		String reportId = list.get(0).get("reportId").toString();
		
		singleCalCR19(year, quarter, reportId);
		return true;
	}
	
	//CR19初步计算单独处理
	@Transactional
	public  boolean singleCalCR19(String year, String quarter,String id) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy");
		String operDate = simpleDateFormat.format(new Date());
		dockedDao.deleteCR19Data(year,quarter);
		//交易对手名称
		String[] tradeName  = {"CR19_00061","CR19_00062","CR19_00063", "CR19_00064", "CR19_00065",
				"CR19_00066","CR19_00067","CR19_00068", "CR19_00069", "CR19_00070"};
		//债权净额
		String[] valueStrings = {"CR19_00071","CR19_00072","CR19_00073", "CR19_00074", "CR19_00075",
				"CR19_00076","CR19_00077","CR19_00078", "CR19_00079", "CR19_00080"};
		List<Map>  countList = dockedDao.getSubjectCR19Count(year, quarter);		
		List<Map> CR19List = dockedDao.getSubjectCR19Sum(year, quarter);
		BigDecimal count =  (BigDecimal) countList.get(0).get("count");
		UserInfo userInfo = CurrentUser.CurrentUser;
		
		if (count.doubleValue() <= 10) {
			for (int i = 0; i < CR19List.size(); i++) {  //后面没有的数据为0		
				if (new BigDecimal(CR19List.get(i).get("value").toString()).doubleValue() == 0) {
					continue;
				}
				CfReportData cfReportData = new CfReportData();	
				CfReportDataId cfReportDataId=new CfReportDataId();		
				
			    cfReportDataId.setOutItemCode(tradeName[i]);//指标代码
			    cfReportData.setMonth(year);//年度
			    cfReportData.setQuarter(quarter);//季度
			    cfReportData.setReportRate("2");//报送类型
		        							
				cfReportData.setId(cfReportDataId);
			    cfReportData.setComCode(Config.getProperty("OrganCode"));//机构代码
			    cfReportData.setOutItemType("01"); //交易对手名称是1
			    cfReportData.setOutItemCodeName("");//因子名称			    
			    cfReportData.setReportItemCode(tradeName[i]);//因子代码
			    cfReportData.setReportItemValue(new BigDecimal("0"));
			    cfReportData.setReportItemWanValue(new BigDecimal("0"));
			    
				cfReportData.setTextValue(CR19List.get(i).get("details").toString());

			    			
			    cfReportData.setOperator(userInfo.getUserName());
			    cfReportData.setReportType("1");
			    cfReportData.setOperDate(operDate);			   
		       	cfReportData.setSource("");		       
		       	cfReportDataId.setReportId(id);
		       	cfReportData.setComputeLevel("1");
		       	dockedDao.save(cfReportData);
		       	
				CfReportData cfReportData1 = new CfReportData();	
				CfReportDataId cfReportDataId1=new CfReportDataId();		
				
			    cfReportDataId1.setOutItemCode(valueStrings[i]);//指标代码
			    cfReportData1.setMonth(year);//年度
			    cfReportData1.setQuarter(quarter);//季度
			    cfReportData1.setReportRate("2");//报送类型
		        							
				cfReportData1.setId(cfReportDataId1);
			    cfReportData1.setComCode(Config.getProperty("OrganCode"));//机构代码
			    cfReportData1.setOutItemType("04");
			    cfReportData1.setOutItemCodeName("");//因子名称			    
			    cfReportData1.setReportItemCode(valueStrings[i]);//因子代码
			    

			    BigDecimal itemValue = new BigDecimal(CR19List.get(i).get("value").toString());
				cfReportData1.setReportItemValue(itemValue);
				
				BigDecimal itemWanValue = itemValue.divide(new BigDecimal("10000"));
			    cfReportData1.setReportItemWanValue(itemWanValue);
			    cfReportData1.setTextValue("");
		
			    cfReportData1.setOperator(userInfo.getUserName());
			    cfReportData1.setReportType("1");
			    cfReportData1.setOperDate(operDate);		   //!!!!!!!!!	    
			    cfReportData.setSource("");	       
			    cfReportDataId1.setReportId(id);
		       	cfReportData1.setComputeLevel("1");
		       	dockedDao.save(cfReportData1);

			}

			
		} else {
			for (int i = 0; i < 10; i++) {
				CfReportData cfReportData = new CfReportData();	
				CfReportDataId cfReportDataId=new CfReportDataId();		
				
			    cfReportDataId.setOutItemCode(tradeName[i]);//指标代码
			    cfReportData.setMonth(year);//年度
			    cfReportData.setQuarter(quarter);//季度
			    cfReportData.setReportRate("2");//报送类型
		        							
				cfReportData.setId(cfReportDataId);
			    cfReportData.setComCode(Config.getProperty("OrganCode"));//机构代码
			    cfReportData.setOutItemType("01");
			    cfReportData.setOutItemCodeName("");//因子名称			    
			    cfReportData.setReportItemCode(tradeName[i]);//因子代码
			    cfReportData.setReportItemValue(new BigDecimal("0"));
			    cfReportData.setReportItemWanValue(new BigDecimal("0"));
			    cfReportData.setTextValue(CR19List.get(i).get("details").toString());
			    cfReportData.setOperator(userInfo.getUserName());
			    cfReportData.setReportType("1");
			    cfReportData.setOperDate(operDate);		   //!!!!!!!!!				    
			    cfReportData.setSource("");	       
			    cfReportDataId.setReportId(id);
		       	dockedDao.save(cfReportData);
		       	
				CfReportData cfReportData1 = new CfReportData();	
				CfReportDataId cfReportDataId1=new CfReportDataId();		
				
			    cfReportDataId1.setOutItemCode(valueStrings[i]);//指标代码
			    cfReportData1.setMonth(year);//年度
			    cfReportData1.setQuarter(quarter);//季度
			    cfReportData1.setReportRate("2");//报送类型
		        							
				cfReportData1.setId(cfReportDataId1);
			    cfReportData1.setComCode(Config.getProperty("OrganCode"));//机构代码
			    cfReportData1.setOutItemType("04");
			    cfReportData1.setOutItemCodeName("");//因子名称			    
			    cfReportData1.setReportItemCode(valueStrings[i]);//因子代码
			    
			    BigDecimal itemValue = new BigDecimal(CR19List.get(i).get("value").toString());
				cfReportData1.setReportItemValue(itemValue);
				
				BigDecimal itemWanValue = itemValue.divide(new BigDecimal("10000"));
			    cfReportData1.setReportItemWanValue(itemWanValue);
			    
			    cfReportData1.setTextValue("");
			    cfReportData1.setOperator(userInfo.getUserName());
			    cfReportData1.setReportType("1");
			    cfReportData1.setOperDate(operDate);		   //!!!!!!!!!			    
			    cfReportData1.setSource("");	       
			    cfReportDataId1.setReportId(id);
		       	cfReportData1.setComputeLevel("1");
		       	dockedDao.save(cfReportData1);

			}
			
			Integer sum = 0;
			for (int i = 10; i < CR19List.size(); i++) {
				Integer sum2 =  (Integer) CR19List.get(i).get("value");
				sum += sum2;
			}
			
			CfReportData cfReportData = new CfReportData();	
			CfReportDataId cfReportDataId=new CfReportDataId();		
			
		    cfReportDataId.setOutItemCode("CR19_00081");//指标代码
		    cfReportData.setMonth(year);//年度
		    cfReportData.setQuarter(quarter);//季度
		    cfReportData.setReportRate("2");//报送类型
	        							
			cfReportData.setId(cfReportDataId);
		    cfReportData.setComCode(Config.getProperty("OrganCode"));//机构代码
		    cfReportData.setOutItemType("04");
		    cfReportData.setOutItemCodeName("");//因子名称			    
		    cfReportData.setReportItemCode("CR19_00081");//因子代码
		    
		    BigDecimal itemValue = new BigDecimal(sum.toString());
		    cfReportData.setReportItemValue(itemValue);
			
			BigDecimal itemWanValue = itemValue.divide(new BigDecimal("10000"));
			cfReportData.setReportItemWanValue(itemWanValue);
		    
		    cfReportData.setTextValue("");
		    cfReportData.setOperator(userInfo.getUserName());
		    cfReportData.setReportType("1");
		    cfReportData.setOperDate(operDate);		   //!!!!!!!!!			    
		    cfReportData.setSource("");		       
		    cfReportDataId.setReportId(id);
	       	cfReportData.setComputeLevel("1");
	       	dockedDao.save(cfReportData);
		}
		return true;
	}
	
	/**
	 * 获取财务表信息
	 */
	@Transactional
	public Page<?> getInfo(int page,int rows, UploadInfoDTO dto) {
		if(dto.getFinanceTable() == null) {
			return null;
		}
		System.out.println(dto.getFinanceTable());
		String sql="";
		//F01-月度科目汇总账科目段及明细段
		if (dto.getFinanceTable().equals("1")) {
			sql = "select  year \"year\", quarter \"quarter\", "
					+ "subjectSegment \"subjectSegment\",openingBalance \"openingBalance\", "
					+ "details \"details\",closingBalance \"closingBalance\" "
					+ " from abc_finance where 1=1 and tableCode='F01' ";
			
			//F02-保户质押贷款-分险种
		} else if (dto.getFinanceTable().equals("2")){ 
			sql = "select  year \"year\", quarter \"quarter\", "
					+ " openingBalance \"openingBalance\", "
					+ " itemCode \"itemCode\", "
					+ "closingBalance \"closingBalance\" "
					+ " from  abc_finance where 1=1 and tableCode='F02'  ";

			//F03-资产负债
		} else if (dto.getFinanceTable().equals("3")){ 
			sql = "select  year \"year\", quarter \"quarter\", "
					+ " openingBalance \"openingBalance\", "
					+ "itemCode \"itemCode\", "
					+ "closingBalance \"closingBalance\" "
					+ " from  abc_finance where 1=1 and tableCode='F03' ";

			//F04-认可资产
		} else if (dto.getFinanceTable().equals("4")){ 
			sql = "select  year \"year\", quarter \"quarter\", "
					+ " openingBalance \"openingBalance\", "
					+ "rowNo \"rowNo\", itemCode \"itemCode\", "
					+ "closingBalance \"closingBalance\" "
					+ " from  abc_finance where 1=1 and tableCode='F04' ";

			//F05-认可负债
		} else if (dto.getFinanceTable().equals("5")){ 
			sql = "select  year \"year\", quarter \"quarter\", "
					+ " openingBalance \"openingBalance\", "
					+ "rowNo \"rowNo\", itemCode \"itemCode\", "
					+ "closingBalance \"closingBalance\" "
					+ " from  abc_finance where 1=1 and tableCode='F05' ";

		} 

		if (dto.getYearmonth() != null && dto.getYearmonth().trim().length() >0) {
			sql += " and year='" +dto.getYearmonth() + "'" ;
		}		

		if (dto.getQuarter() != null && dto.getQuarter() .trim().length() >0) {
			sql += " and quarter='" +dto.getQuarter() + "'" ;
		}		


		if (dto.getReportId() != null && dto.getReportId().trim().length() > 0) {
			sql  += " and reportId like'%" + dto.getReportId() +"%'"; 
		}

		sql += "order by serialNo";
		

		return dockedDao.queryByPage(sql, page, rows);
	}
	
	/**
	 * 获取Excel文件里的数据
	 */
	private List<Row>  getExcelData(UploadInfoDTO dto, int startReadPos , int sheetIndex) {
		ExcelUtil excelUtil = new ExcelUtil();
		String absolutePathString = dto.getFilePath() + "/" + dto.getNewfileName();
		System.out.println("文件路径：" + absolutePathString);
		//设置文件路径
		excelUtil.setExcelPath(absolutePathString);
		excelUtil.setStartReadPos(startReadPos);  //从第二行开始获取数据
		excelUtil.setSelectedSheetIdx(sheetIndex);
//		excelUtil.setSelectedSheetName(selectedSheetName);  //通过sheet名读取Excel文件
		
		try {
			return excelUtil.readExcel();
		} catch (IOException e) {
			log.error("getExcelData方法出现异常：");
			e.printStackTrace();
		}
		log.error("获取Excel数据出错");
		return null;
	}
	
	/**
	 * 获取Excel文件里的数据
	 */
	private List<Row>  getExcelData(UploadInfoDTO dto, int startReadPos , String selectedSheetName) {
		ExcelUtil excelUtil = new ExcelUtil();
		String absolutePathString = dto.getFilePath() + "/" + dto.getNewfileName();
		
		//设置文件路径
		excelUtil.setExcelPath(absolutePathString);
		excelUtil.setStartReadPos(startReadPos);  //从第二行开始获取数据
//		excelUtil.setSelectedSheetIdx(sheetIndex);
		excelUtil.setSelectedSheetName(selectedSheetName);  //通过sheet名读取Excel文件
		
		try {
			return excelUtil.readExcel();
		} catch (IOException e) {
			e.printStackTrace();
		}
		log.error("获取Excel数据出错");
		return null;
	}

	
	/**
	 * 保存财务的《月度科目汇总账科目段及明细段表》
	 * 
	 * 从第13行开始取值，直到科目段为空
	 */
	private boolean saveSubjectData(UploadInfoDTO dto, List<Row> list, String tableName, String tableCode) {
		for(int i=0; i<list.size(); i++){
			Row row=list.get(i);
			String cellData = ExcelUtil.getCellValue(row.getCell(0));
					
			if (cellData == null || cellData.length() == 0) {
				log.info("月度科目表科目段出现空，下面数据不在读取");
				break;
			}
			Finance_ABC finance = new Finance_ABC();

			//科目代码如果是数字为科学计数
			if (row.getCell(0).getCellType() == Cell.CELL_TYPE_NUMERIC) {
				finance.setSubjectSegment(new BigDecimal(cellData).toPlainString());  //科目段
			} else {
				//如果科目代码时有字母
				finance.setSubjectSegment(cellData);  //科目代码
			}

			finance.setDetails(row.getCell(1).getStringCellValue());  //明细段
		    
		    String cell4 = ExcelUtil.getCellValue(row.getCell(4));
		    finance.setOpeningBalance(cell4 == null || "".equals(cell4.trim()) ?
		    		new BigDecimal("0") : new BigDecimal(cell4));  //期初余额
		    
		    
		    String cell7 = ExcelUtil.getCellValue(row.getCell(7));
		    finance.setClosingBalance(cell7 == null || "".equals(cell7.trim()) ?
		    		new BigDecimal("0") : new BigDecimal(cell7));  //期末余额
		    
		    finance.setTableCode(tableCode);
		    finance.setTableName(tableName);
		    finance.setRowNo(null);
		    finance.setItemCode(null);
		    finance.setYear(dto.getYearmonth());
		    finance.setQuarter(dto.getQuarter());
		    finance.setImportDate(new Date());
		    finance.setImportDept("财务部");
		    finance.setReportId(dto.getReportId());
		    UserInfo userInfo = CurrentUser.CurrentUser;			
			finance.setImportOperator(userInfo.getUserName());
			
			try {
				//保存新数据
			    dockedDao.save(finance);
			} catch (Exception e) {
				e.printStackTrace();
			}
		    
		}
		return true;
	}

	/**
	 * 保存财务数据表：<保户质押贷款-分险种>表适合这个方法
	 * 通过Excel表的第二列的‘汇总’和‘小计’来获取数据，没有的不取值，
	 */
	private boolean saveFinanceData1(UploadInfoDTO dto, List<Row> list,String tableName, String tableCode) {
		//财务数据表里有些表有分页，需要注意
		for(int i=0; i<list.size(); i++){
			Row row=list.get(i);
			String cellData = ExcelUtil.getCellValue(row.getCell(1));
			
			if (cellData ==null) {
				continue;
			}
			if (cellData.contains("汇总") || 
					cellData.contains("小计") ) {
				Finance_ABC finance = new Finance_ABC();
				
				finance.setTableName(tableName);
				finance.setTableCode(tableCode);
				
				//这个表没必要保存行次,设置为0
//				finance.setRowNo("0");
				//科目(对应科目、小计）
				finance.setItemCode(ExcelUtil.getCellValue(row.getCell(1)));

				
				String cell2 = ExcelUtil.getCellValue(row.getCell(2));
				cell2 = new String(cell2);
				cell2 = cell2.replace( ",", "") ;
				finance.setOpeningBalance(cell2 == null || "".equals(cell2.trim()) ?
			    		new BigDecimal("0") : new BigDecimal(cell2));  //期初余额

				String cell3 = ExcelUtil.getCellValue(row.getCell(3));
				cell3 = new String(cell3);
				cell3 = cell3.replace( ",", "") ;
				finance.setClosingBalance(cell3 == null || "".equals(cell3.trim()) ?
			    		new BigDecimal("0") : new BigDecimal(cell3));  //期末余额
				
				finance.setRowNo(null);

				finance.setYear(dto.getYearmonth());
				finance.setQuarter(dto.getQuarter());
				finance.setImportDate(new Date());
				finance.setImportDept("财务部");
				UserInfo userInfo = CurrentUser.CurrentUser;			
				finance.setImportOperator(userInfo.getUserName());
			    finance.setReportId(dto.getReportId());
				dockedDao.save(finance); 
				
			} else {
				continue;
			}
			
		}
		return true;
	}
		
	/**
	 * 保存财务数据表：
	 * 适用资产负债（sheetName=2）
	 * 
	 * 从Excel表的第二列的单元格含有‘--’下一行开始读取，直到数据读取完
	 */
	private boolean saveFinanceData2(UploadInfoDTO dto, List<Row> list,  String tableName, String tableCode) {
		int hasNumberRow = 0; //需要存入数据库的数据开始行
		
		//这个循环用于知道Excel文件里需要存入数据库的数据是从第几行开始的
		for(int i=0; i<list.size(); i++) {
			Row row=list.get(i);
			
			if (row.getCell(1)!=null && 
					ExcelUtil.getCellValue(row.getCell(1)).contains("--")) {
				hasNumberRow++;
				break;
			}
			
			hasNumberRow++;
			continue;
		}
		
		//正式获取有用的数据
		for(int i=hasNumberRow; i<list.size(); i++){
			Row row=list.get(i);
			
			Finance_ABC finance = new Finance_ABC();
			
			finance.setTableName(tableName);
			finance.setTableCode(tableCode);
			
			//这个表没必要保存行次,设置为0
//			finance.setRowNo("0");
			//科目(对应科目、小计）
			finance.setItemCode(ExcelUtil.getCellValue(row.getCell(0)));
			
			//期初余额,单元格有可能为空
			String cell1 = ExcelUtil.getCellValue(row.getCell(1));
			finance.setOpeningBalance(cell1==null || "".equals(cell1.trim())?
					new BigDecimal("0"):new BigDecimal(cell1));
			//期末余额
			String cell2 = ExcelUtil.getCellValue(row.getCell(2));
			finance.setClosingBalance(cell2==null || "".equals(cell2.trim())?
					new BigDecimal("0"):new BigDecimal(cell2));
			
			finance.setYear(dto.getYearmonth());
			finance.setQuarter(dto.getQuarter());
			finance.setImportDate(new Date());
			finance.setImportDept("财务部");
			
			UserInfo userInfo = CurrentUser.CurrentUser;			
			finance.setImportOperator(userInfo.getUserName());
		    finance.setReportId(dto.getReportId());
		    
			dockedDao.save(finance);
		}
		return true;
	}
	
	
	/**
	 * 保存财务数据表：
	 * 适用F04-认可资产、F05-认可负债
	 * 
	 * 从Excel文件的第一列含有数字的那一行开始，第一列没有含有数字那一行不保存到数据库中
	 */
	private boolean saveFinanceData3(UploadInfoDTO dto, List<Row> list,  String tableName, String tableCode) {
		//财务数据表里有些表有分页，需要注意
		for(int i=0; i<list.size(); i++){
			Row row=list.get(i);
			String cellData = ExcelUtil.getCellValue(row.getCell(0));
			
			Pattern pattern = Pattern.compile("\\d+"); 
			Matcher matcher = pattern.matcher(cellData);
			
			//lookingAt()对前面的字符串进行匹配,只有匹配到的字符串在最前面才返回true
			if (row.getCell(0)==null || 
					!matcher.lookingAt()) {
				continue;
			}
			
			Finance_ABC finance = new Finance_ABC();
			
			finance.setTableName(tableName);
			finance.setTableCode(tableCode);
			//行次
			finance.setRowNo(ExcelUtil.getCellValue(row.getCell(0)));
			//科目(对应科目、小计）
			finance.setItemCode(ExcelUtil.getCellValue(row.getCell(1)));
			//期初余额,单元格有可能为空
			String cell2 = ExcelUtil.getCellValue(row.getCell(2));
			finance.setOpeningBalance(cell2==null || "".equals(cell2.trim())?
					new BigDecimal("0"):new BigDecimal(cell2));
			//期末余额
			String cell3 = ExcelUtil.getCellValue(row.getCell(3));
			finance.setClosingBalance(cell3==null || "".equals(cell3.trim())?
					new BigDecimal("0"):new BigDecimal(cell3));
			
			finance.setYear(dto.getYearmonth());
			finance.setQuarter(dto.getQuarter());
			finance.setImportDate(new Date());
			finance.setImportDept("财务部");
			UserInfo userInfo = CurrentUser.CurrentUser;	
			finance.setImportOperator(userInfo.getUserName());
		    finance.setReportId(dto.getReportId());
		    
			dockedDao.save(finance);
		}
		return true;
	}

}


