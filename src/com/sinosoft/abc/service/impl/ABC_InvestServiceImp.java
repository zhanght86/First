package com.sinosoft.abc.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ObjectUtils.Null;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sinosoft.abc.dao.ABC_DockedDao;
import com.sinosoft.abc.dto.InvestQueryDto_ABC;
import com.sinosoft.abc.entity.Invest_ABC;
import com.sinosoft.abc.service.ABC_InvestService;
import com.sinosoft.common.CurrentUser;
import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dto.reportdatacheck.DataCheckDto;
import com.sinosoft.zcfz.dto.reportdataimport.UploadInfoDTO;
import com.sinosoft.entity.CalCfReportInfo;
import com.sinosoft.entity.CfReportRowAddData;
import com.sinosoft.entity.CfReportRowAddDataId;
import com.sinosoft.entity.UserInfo;
import com.sinosoft.util.Config;
import com.sinosoft.util.ExcelUtil;
import com.sinosoft.util.UploaderServlet;


@Service("ABC_InvestService")
public class ABC_InvestServiceImp implements ABC_InvestService{
	private Log log = LogFactory.getLog(ABC_InvestServiceImp.class);
	
	@Resource
	private ABC_DockedDao dockedDao;
	@Resource
	private UploaderServlet uploaderServlet;
	

	/**
	 * 更新状态
	 */
	@Transactional
	public boolean updateState(UploadInfoDTO dto,String state) {
		String reportId = dto.getReportId();
		System.out.println(reportId);
		CalCfReportInfo calCfReportInfo=dockedDao.get(CalCfReportInfo.class, reportId);
		String reportStateString = calCfReportInfo.getReportState();
	
		//0.6','投资接口数据已导入，财务未导入'
		if (reportStateString.equals("0") || reportStateString.equals("0.6")) {
			calCfReportInfo.setReportState(state);
		} else {
			calCfReportInfo.setReportState("1");
		}
		
		try{
			dockedDao.update(calCfReportInfo);
		}catch(RuntimeException e){
			
			log.error("报送号"+reportId+"更新状态失败!");
			e.printStackTrace();
		}
		
		return true;
	}
	
	/**
	 * 上传投资表的《MR09-股票》表
	 */
	@Transactional
	public boolean MR09Import(UploadInfoDTO dto) {
		// 如果导入同样的表，并且年、季度一样，则删除之前的数据
		if (!dockedDao.deleteInvestMR09Data(dto)) {
			log.error("导入《MR09-股票》表时，删除当前条件下数据表数据出错！！！");
			return false;
		}
		
		//获取Excel文件数据
		List<Row> MR09List = getExcelData(dto, 0, 0);
		if (MR09List==null || MR09List.size()==0) {
			log.error("《MR09-股票》表没有数据");
			return false;
		}
		
		if(!saveMR09(dto, MR09List, "MR09-股票", "MR09")){
			return false;
		}
		return true;
	}

	/**
	 * 《MR12-基金》
	 */
	@Transactional
	public boolean MR12Import(UploadInfoDTO dto) {
		// 如果导入同样的表，并且年、季度一样，则删除之前的数据
		if (!dockedDao.deleteInvestMR12Data(dto)) {
			log.error("《MR12-基金》表时，删除当前条件下数据表数据出错！！！");
			return false;
		}
		
		//获取Excel文件数据
		List<Row> MR12List = getExcelData(dto, 0, 0);
		if (MR12List==null || MR12List.size()==0) {
			log.error("《MR12-基金》表没有数据");
			return false;
		}
		
		if(!saveMR12(dto, MR12List, "MR12-基金", "MR12")){
			return false;
		}
		return true;
	}

	/**
	 * CR01_01-政策性金融债、《CR01_02-其他债券》表
	 */
	@Transactional
	public boolean CR01Import(UploadInfoDTO dto) {		
		List<Row> CR01List = null;
		
		if (dto.getFileName().contains("CR01-政策性金融债")) {
			// 如果导入同样的表，并且年、季度一样，则删除之前的数据
			if (!dockedDao.deleteInvestCR01PolicyData(dto)) {
				log.error("《CR01-政策性金融债 》表时，删除当前条件下数据表数据出错！！！");
				return false;
			}
			
			//获取Excel《CR01-政策性金融债》文件数据
			CR01List = getExcelData(dto, 0,0);
			if (CR01List==null || CR01List.size()==0) {
				log.error("《CR01-政策性金融债》表没有数据");
				return false;
			}
			
			if(!saveCR01PolicyList(dto, CR01List, "CR01-政策性金融债", "CR01_01")){
				return false;
			}
			
		} else if (dto.getFileName().contains("CR01-其他债券")) {
			// 如果导入同样的表，并且年、季度一样，则删除之前的数据
			if (!dockedDao.deleteInvestCR01OtherData(dto)) {
				log.error("《CR01-其他债券 》表时，删除当前条件下数据表数据出错！！！");
				return false;
			}
			
			//获取Excel《CR01-其他债券》文件数据
			CR01List = getExcelData(dto, 0, 0);
			if (CR01List==null || CR01List.size()==0) {
				log.error("《CR01-其他债券》表没有数据");
				return false;
			}
			
			if(!saveCR01OtherList(dto, CR01List, "CR01-其他债券", "CR01_02")){
				return false;
			}
		} else {
			throw new RuntimeException("CR01只需要上传《CR01-政策性金融债》和《CR01-其他债券 》");
		}
		

		return true;
	}
	
	/**
	 * 《CR05-企业债 》
	 */
	@Transactional
	public boolean CR05Import(UploadInfoDTO dto) {
		// 如果导入同样的表，并且年、季度一样，则删除之前的数据
		if (!dockedDao.deleteInvestCR05Data(dto)) {
			log.error("《CR05-企业债 》表时，删除当前条件下数据表数据出错！！！");
			return false;
		}
		
		//获取Excel文件数据
		List<Row> CR05List = getExcelData(dto, 0, 0);
		if (CR05List==null || CR05List.size()==0) {
			log.error("《CR05-企业债 》表没有数据");
			return false;
		}
		
		if(!saveCR05(dto, CR05List, "CR05-企业债", "CR05")){
			return false;
		}
		return true;
	}
	
	/**
	 * 《CR10-保险资管产品》
	 */
	@Transactional
	public boolean CR10Import(UploadInfoDTO dto) {
		// 如果导入同样的表，并且年、季度一样，则删除之前的数据
		if (!dockedDao.deleteInvestCR10Data(dto)) {
			log.error("《CR10-保险资管产品》表时，删除当前条件下数据表数据出错！！！");
			return false;
		}
		
		//获取Excel文件数据
		List<Row> CR10List = getExcelData(dto, 0, 0);
		if (CR10List==null || CR10List.size()==0) {
			log.error("《CR10-保险资管产品》表没有数据");
			return false;
		}
		
		if(!saveCR10(dto, CR10List, "CR10-保险资管产品", "CR10")){
			return false;
		}
		return true;
	}
	

	/**
	 * 获取科目表信息
	 */
	@Transactional
	public Page<?> getInfo(int page,int rows, InvestQueryDto_ABC dto) {
		if (dto.getInvestTable() == null) {
			return null;
		}
		String sql="";

		//CR01-政策性金融债
		if (dto.getInvestTable().equals("1") ) {
			sql = "select   year \"year\", quarter \"quarter\", "
					+ "itemCode \"itemCode\", itemName \"itemName\", "
					+ "recognitionValue \"recognitionValue\", "
					+ "amount \"amount\",creditRate \"creditRate\" "
					+ " from abc_invest where 1=1 and tableCode='CR01_01' ";
		 		
			//CR01-其他债券
		} else if (dto.getInvestTable().equals("2") ){
			sql = "select   year \"year\", quarter \"quarter\", "
					+ "itemCode \"itemCode\",itemName \"itemName\", "
					+ "externalRate \"externalRate\", recognitionValue \"recognitionValue\", "
					+ "amount \"amount\",creditRate \"creditRate\" "
					+ " from abc_invest where 1=1 and tableCode='CR01_02' ";
	
			//CR05-企业债
		} else if (dto.getInvestTable().equals("3") ){
			sql = "select   year \"year\", quarter \"quarter\", "
					+ "itemName \"itemName\",  interst \"interst\","
					+ " recognitionValue \"recognitionValue\", "
					+ "amount \"amount\",creditRate \"creditRate\" "
					+ " from abc_invest where 1=1 and tableCode='CR05' ";				    			 			

			//CR10-保险资管产品
		} else if (dto.getInvestTable().equals("4") ){
			sql = "select   year \"year\", quarter \"quarter\", "
					+ "itemName \"itemName\", "
					+ "issuer \"issuer\", interst \"interst\","
					+ "recognitionValue \"recognitionValue\", "
					+ "creditRate \"creditRate\" "
					+ " from abc_invest where 1=1 and tableCode='CR10' ";

			//MR09-股票
		} else if (dto.getInvestTable().equals("5") ){
			sql = "select   year \"year\", quarter \"quarter\", "
					+ "itemCode \"itemCode\",itemName \"itemName\", "
					+ "huShenValue \"huShenValue\","
					+ "purchaseCost \"purchaseCost\", recognitionValue \"recognitionValue\", "
					+ "amount \"amount\" "
					+ " from abc_invest where 1=1 and tableCode='MR09' ";
	
			//MR12-基金
		} else if (dto.getInvestTable().equals("6") ){
			sql = "select   year \"year\", quarter \"quarter\", "
					+ "itemCode \"itemCode\",itemName \"itemName\", "
					+ "issuer \"issuer\",fundTypeB \"fundTypeB\","
					+ "purchaseCost \"purchaseCost\", recognitionValue \"recognitionValue\", "
					+ "amount \"amount\",fundTypeA \"fundTypeA\" "
					+ " from abc_invest where 1=1 and tableCode='MR12' ";
		}
					
			
		if (dto.getYearmonth() != null && !"".equals(dto.getYearmonth())) {
			sql += " and year='" +dto.getYearmonth() + "'" ;
		}		

		if (dto.getQuarter() != null && !"".equals(dto.getQuarter())) {
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
	private List<Row>  getExcelData(UploadInfoDTO dto, int startReadPos, int sheetIndex)  {
		ExcelUtil excelUtil = new ExcelUtil();
		String absolutePathString = dto.getFilePath() + "/" + dto.getNewfileName();		
		
		//设置文件路径
		excelUtil.setExcelPath(absolutePathString);
		excelUtil.setStartReadPos(startReadPos);  //设置从几行开始
		excelUtil.setSelectedSheetIdx(sheetIndex);
		
		try {
			return excelUtil.readExcel();
		} catch (IOException e) {
			log.error("获取Excel数据出错");
			e.printStackTrace();
		}
		
		return null;
	}

	/**
     * 获取指定的字段属于Excel文件在第几列
     */
    private int index(Row firstRow, String dimString) {
        int columns = firstRow.getPhysicalNumberOfCells();
        
        for (int columnIndex = 0; columnIndex < columns; columnIndex++) {
            //如果当前列的值==配置文件中该列对应的值
            if (ExcelUtil.getCellValue(firstRow.getCell(columnIndex)).contains(dimString)) {
                //返回当前列在第几列
                return columnIndex;
            }
        }
      
        log.error("字段 " + dimString +" 在Excel文件中不存在,请在配置文件中重新设置");
        return -1;
    }
	/*******************************保存数据*******************************/
	/**
	 * 保存《MR09-股票》表的数据
	 * 
	 * 从第
	 */
	private boolean saveMR09(UploadInfoDTO dto, List<Row> list,String tableName, String tableCode) {		
		Row firstRow = list.get(0);
	
		String itemType = ExcelUtil.getCellValue(list.get(1).getCell(0));
		for (int i = 1; i < list.size(); i++) {
			Row row = list.get(i);
			String fisrtCellData = ExcelUtil.getCellValue(row.getCell(1));  //新增一列，将0修改为1
			if (fisrtCellData == null || fisrtCellData.length() ==0) {
				log.info("《MR09-股票》证券代码为空,下面的数据不在获取！");
				break;
			}
			
			Invest_ABC invest  = new Invest_ABC();
			
			//遍历每一行，通过第一行的字段名获取相应的数据（注意，这里的数字是000,000,风格）
			//从第2列开始读取！！！！！第一列只有一个数
			for (int j = 1; j < row.getPhysicalNumberOfCells(); j++) {
				String cellData = ExcelUtil.getCellValue(row.getCell(j));
				if (cellData.contains(",")|| cellData.contains("，")) {
					cellData = new   String(cellData);
					cellData = cellData.replace( ",", "")   ;
				}
				
				if (cellData == null || cellData.length() ==0) {
					break;
				}				
				
				//股票名称：＝底稿中的证券名称
				if (cellData != null && j == index(firstRow, "证券名称")) {
					invest.setItemName(cellData);
					
					//股票代码=证券代码；
				} else if ( j == index(firstRow, "证券代码")) {
					invest.setItemCode(cellData);
			        
					//股票类型：证券代码002开头是中小板，300开头是创业板，其他都是沪深主板；                                                                                                                    
					if (cellData.matches("^002\\w+")) {
						invest.setStockType("2|中小板股");
						
					} else if (cellData.matches("^300\\w+")) {
						invest.setStockType("3|创业板股");
						
					} else {
						invest.setStockType("1|沪深主板股");
					}
					
					//持股比例 ：＝底稿中的占流通股本比例％，只填列大与5%的。小于5%的股票该项不填列(初步计算时处理）； 
				} else if (j == index(firstRow, "占流通股本比例")) {
					if (cellData.contains("NAN")) {
						invest.setAmount(null); //怎么处理？？？？
						continue;
					}
					if (Double.parseDouble(cellData) > 0.05) {						
						invest.setAmount(cellData==null || "".equals(cellData.trim())?
								new BigDecimal("0"):new BigDecimal(cellData));
						
					} else {
						invest.setAmount(null);
					}					
					
					//购买成本＝底稿中的累计成本；
				} else if ( j == index(firstRow, "累计成本")) {
					invest.setPurchaseCost(cellData==null || "".equals(cellData.trim())?
							new BigDecimal("0"):new BigDecimal(cellData));
					
					//对市值（认可价值＝市值）为0的进行报错及提示
				} else if (j == index(firstRow, "市值")) {
/*					if (new BigDecimal(cellData).compareTo(new BigDecimal("0"))==0) {
						log.error("MR09的市值不能为0,需要报错及提示");
						throw new RuntimeException("： 《MR09-股票》表里的的市值不可以为0！");
					} else {
						invest.setRecognitionValue(cellData==null || "".equals(cellData.trim())?
								new BigDecimal("0"):new BigDecimal(cellData));
					}	*/	
					invest.setRecognitionValue(cellData==null || "".equals(cellData.trim())?
							new BigDecimal("0"):new BigDecimal(cellData));
					
				} else if ( j == index(firstRow, "沪深300权重")) {
					invest.setHuShenValue(cellData);

					if (new BigDecimal(cellData).doubleValue() == 0) {
						invest.setIsHuShen("否");
					} else {
						invest.setIsHuShen("是");
					}

				} else if ( j == index(firstRow, "持有数量")) {
					invest.setHoldingQuantity(cellData==null || "".equals(cellData.trim())?
							new BigDecimal("0"):new BigDecimal(cellData));
				}
				
			}
			
			invest.setTableName(tableName);
			invest.setTableCode(tableCode);
			invest.setItemType(itemType);
			invest.setIssuer(null);
			invest.setInterst(null);
			
			invest.setYear(dto.getYearmonth());
			invest.setQuarter(dto.getQuarter());
			invest.setImportDate(new Date());
			invest.setImportDept("投资部");
			UserInfo userInfo = CurrentUser.CurrentUser;
			invest.setImportOperator(userInfo.getUserName());
			invest.setReportId(dto.getReportId());
			
			dockedDao.save(invest);
			//对于不再引用的对象，及时把它的引用赋为null
			invest = null; //回收
			
			//如果内存确实很紧张，调用System.gc()  方法来建议垃圾回收器开始回收垃圾。
			//System.gc()  
		}
		return true;
	}
 
	/**
	 * 保存《MR12-基金》表的数据(直接插入到cfreportrowadddata)
	 * 
	 * 从第一行开始取值，通过第一行的字段名来判断后面行的数据，“基金名称”为空取值结束，后面即使有数据也读取
	 */
	private boolean saveMR12(UploadInfoDTO dto, List<Row> list,String tableName, String tableCode) {
		Row firstRow = list.get(0);
		
		for (int i = 1; i < list.size(); i++) {
			Row row = list.get(i);
			String fisrtCellData = ExcelUtil.getCellValue(row.getCell(index(firstRow, "基金名称")));
			if (fisrtCellData == null || fisrtCellData.length() ==0) {
				log.info("《MR12-基金》基金名称不能为空");
				break;
			}
			
			Invest_ABC invest = new Invest_ABC();
			
			//遍历每一行，通过第一行的字段名获取相应的数据
			for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
				String cellData = ExcelUtil.getCellValue(row.getCell(j));
				if (cellData.contains(",")|| cellData.contains("，")) {
					cellData = new   String(cellData);
					cellData = cellData.replace( ",", "")   ;
				}
		    	
		    	
				if (cellData.contains(",") || cellData.contains("，")) {
					cellData = new   String(cellData);
					cellData   =   cellData.replace( ",", "");
				}

				//基金名称：＝底稿中的基金名称；
				if (cellData != null && j == index(firstRow, "基金名称")) {
					invest.setItemName(cellData);
				
					//基金代码：＝底稿中的基金代码；                                                                                                                                                     
				} else if (j == index(firstRow, "基金代码")) {
					invest.setItemCode(cellData);
					
					//发行方：在底稿第二列，手工维护；
				}else if (j == index(firstRow, "发行方")) {
					invest.setIssuer(cellData);	

					//持仓数量（份）：＝底稿中的持有数量； 
				} else if (j == index(firstRow, "持有数量")) {
					invest.setAmount(cellData==null || "".equals(cellData.trim())?
							new BigDecimal("0"):new BigDecimal(cellData));							
					
					//购买成本：＝底稿中的累计成本；
				} else if ( j == index(firstRow, "累计成本")) {
					invest.setPurchaseCost(cellData==null || "".equals(cellData.trim())?
							new BigDecimal("0"):new BigDecimal(cellData));
					
					//认可价值：＝底稿中的市值；
				} else if (j == index(firstRow, "市值")) {
					invest.setRecognitionValue(cellData==null || "".equals(cellData.trim())?
							new BigDecimal("0"):new BigDecimal(cellData));
					
				} else if (j == index(firstRow, "基金类别B")) {
					invest.setFundTypeB(cellData);
					
					//基金类别A 手工维护那一列
				} else if (j == index(firstRow, "基金类别")) {  //一定要放最后面
					invest.setFundTypeA(cellData);					
				}
				
			}
			
			/**
			 * 基金类别：货币型／混合基金通过底稿中的“基金类别b”判断，基金类别通过同样方式进行判断，
			 * 如果为债券基金或者股票基金，则根据第一列“基金类别A判断”
			 *  混合基金＝底稿中的混合型， 货币市场基金＝底稿中的货币市场型； 其他资产类别在底稿第一列中维护；                                                      
			 */
   		    //基金类别(手工维护那一列
			String fundTypeDataA = invest.getFundTypeA();
			
			//基金类别B
			String fundTypeDataB = invest.getFundTypeB();

			
			if (fundTypeDataB.contains("货币市场型") ) {
				invest.setFundType(fundTypeDataB);
				invest.setColcode("MR12_08");
				
			}  else if (fundTypeDataB.contains("混合型") ){
				invest.setFundType(fundTypeDataB);
				invest.setColcode("MR12_07");
				
			}  else if (fundTypeDataB.contains("股票型") ){
				if (fundTypeDataA.contains("分级股票基金-劣后级") ){
					invest.setFundType(fundTypeDataA);
					invest.setColcode("MR12_06");
					
				} else if (fundTypeDataA.contains("分级股票基金-优先级") ){
					invest.setFundType(fundTypeDataA);
					invest.setColcode("MR12_05");
					
				} else if (fundTypeDataA.contains("普通股票基金") ){
					invest.setFundType(fundTypeDataA);
					invest.setColcode("MR12_04");					
				} 
				
			} else if (fundTypeDataB.contains("债券型") ){
				if (fundTypeDataA.contains("分级债券基金-劣后级") ){
					invest.setFundType(fundTypeDataA);
					invest.setColcode("MR12_03");		
				} else if (fundTypeDataA.contains("分级债券基金-优先级") ){
					invest.setFundType(fundTypeDataA);
					invest.setColcode("MR12_02");
					
				} else if (fundTypeDataA.contains("普通债券基金") ){
					invest.setFundType(fundTypeDataA);
					invest.setColcode("MR12_01");
				} 
				
			}
			
			

			invest.setTableName(tableName);
			invest.setTableCode(tableCode);
			invest.setIsHuShen(null);
			invest.setInterst(null);
			
			invest.setYear(dto.getYearmonth());
			invest.setQuarter(dto.getQuarter());
			invest.setImportDate(new Date());
			invest.setImportDept("投资部");
			UserInfo userInfo = CurrentUser.CurrentUser;
			invest.setImportOperator(userInfo.getUserName());
			invest.setReportId(dto.getReportId());
			dockedDao.save(invest);
			//对于不再引用的对象，及时把它的引用赋为null
			invest = null; //回收
			
			//如果内存确实很紧张，调用System.gc()  方法来建议垃圾回收器开始回收垃圾。
			//System.gc()  
		}
		return true;
	}
	

	/**
	 * 保存《CR01-政策性金融债 》
	 * 
	 * 从第一行开始取值，通过第一行的字段名来判断后面行的数据，“证券简称”为空取值结束，后面即使有数据也读取
	 */
	private boolean saveCR01PolicyList(UploadInfoDTO dto, List<Row> CR01PolicyList, String tableName, String tableCode) {                                                                                                                               
		Row firstRow = CR01PolicyList.get(0);
		
		for (int i = 1; i < CR01PolicyList.size(); i++) {
			Row row = CR01PolicyList.get(i);
			String fisrtCellData = ExcelUtil.getCellValue(row.getCell(index(firstRow, "证券简称")));
			if (fisrtCellData == null || fisrtCellData.length() ==0) {
				log.info("《CR01-政策性金融债 》证券简称不能为空");
				break;
			}

			Invest_ABC invest = new Invest_ABC();
			
			//遍历每一行，通过第一行的字段名获取相应的数据
			for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
				String cellData = ExcelUtil.getCellValue(row.getCell(j));			
				
				//证券名称=底稿中的证券简称
				if (cellData != null && j == index(firstRow, "证券简称")) {
					invest.setItemName(cellData);				
				
					//修正久期（年）=底稿中的市价修正久期
				} else if (j == index(firstRow, "市价修正久期")) {
					 //保留4位小数  
					BigDecimal result = new BigDecimal(cellData).setScale(4, BigDecimal.ROUND_HALF_UP);
					invest.setAmount(cellData==null || "".equals(cellData)?
							new BigDecimal("0"): result);							
				
					//信用评级=底稿中的发行人评级
				} else if (j == index(firstRow, "发行人评级")) {
					cellData = creditRateToString(cellData);
					invest.setCreditRate(cellData);	
					
					//证券代码=底稿中的证券代码
				} else if ( j == index(firstRow, "证券代码")) {
					invest.setItemCode(cellData);
					
					//认可价值=底稿中的净价市值 
				} else if (j == index(firstRow, "净价市值")) {
					invest.setRecognitionValue(cellData==null || "".equals(cellData)?
							new BigDecimal("0"):new BigDecimal(cellData));
					
				}
	
			}
			
			invest.setTableCode(tableCode);
			invest.setTableName(tableName);	

			invest.setInterst(null);
			invest.setIssuer(null);	
			invest.setIsHuShen(null);
			invest.setPurchaseCost(null);
			
			invest.setYear(dto.getYearmonth());
			invest.setQuarter(dto.getQuarter());
			invest.setImportDate(new Date());
			invest.setImportDept("投资部");
			UserInfo userInfo = CurrentUser.CurrentUser;
			invest.setImportOperator(userInfo.getUserName());
			invest.setReportId(dto.getReportId());
			
			dockedDao.save(invest);
			//对于不再引用的对象，及时把它的引用赋为null
			invest = null; //回收
			
			//如果内存确实很紧张，调用System.gc()  方法来建议垃圾回收器开始回收垃圾。
			//System.gc()  
		}
		return true;
	}

	/**
	 * 保存《CR01-其他债券》
	 * 
	 * 从第一行开始取值，通过第一行的字段名来判断后面行的数据，“证券代码”为空取值结束，后面即使有数据也读取
	 */
	private boolean saveCR01OtherList (UploadInfoDTO dto, List<Row> CR01OtherList,String tableName, String tableCode) {                                                                                                                               
		Row firstRow = CR01OtherList.get(0);
                                                                                                                                                             
		for (int i = 1; i < CR01OtherList.size(); i++) {
			Row row = CR01OtherList.get(i);
			String fisrtCellData = ExcelUtil.getCellValue(row.getCell(index(firstRow, "证券简称")));
			if (fisrtCellData == null || fisrtCellData.length() ==0) {
				log.info("《CR01-其他债券》证券简称不能为空");
				break;
			}

			Invest_ABC invest = new Invest_ABC();
			
			//遍历每一行，通过第一行的字段名获取相应的数据
			for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
				String cellData = ExcelUtil.getCellValue(row.getCell(j));			
				
				//证券名称=底稿中的证券简称，铁道债不取，根据关键字“铁道”判断(这个在数据库初步计算时获取）
				if (cellData != null && j == index(firstRow, "证券简称")) {
					invest.setItemName(cellData);				
				
					//修正久期（年）=底稿中的市价修正久期
				} else if (j == index(firstRow, "市价修正久期")) {
					BigDecimal result = new BigDecimal(cellData).setScale(4, BigDecimal.ROUND_HALF_UP);
					invest.setAmount(cellData==null || "".equals(cellData)?
							new BigDecimal("0"): result);							
				
					/*信用评级：根据证券简称判断，含cp／scp的就是短期融资券，
					 * 短期融资券的债券评级取底稿中的发行人评级，不是短融的取底稿中的债券外部评级 ，(这个在数据库初步计算时获取）*/
				} else if (j == index(firstRow, "发行人评级")) {
					cellData = creditRateToString(cellData);
					invest.setCreditRate(cellData);	
					
					//证券代码=底稿中的证券代码
				} else if ( j == index(firstRow, "证券代码")) {
					invest.setItemCode(cellData);
					
					//认可价值=底稿中的净价市值 
				} else if (j == index(firstRow, "净价市值")) {
					invest.setRecognitionValue(cellData==null || "".equals(cellData.trim())?
							new BigDecimal("0"):new BigDecimal(cellData));
					
				} else if (j == index(firstRow, "债券外部评级")) {
					cellData = creditRateToString(cellData);
					invest.setExternalRate(cellData);
					
				}
	
			}
			
			invest.setTableCode(tableCode);
			invest.setTableName(tableName);
			invest.setInterst(null);
			invest.setIssuer(null);	
			invest.setIsHuShen(null);
			invest.setPurchaseCost(null);
			
			invest.setYear(dto.getYearmonth());
			invest.setQuarter(dto.getQuarter());
			invest.setImportDate(new Date());
			invest.setImportDept("投资部");
			UserInfo userInfo = CurrentUser.CurrentUser;
			invest.setImportOperator(userInfo.getUserName());
			invest.setReportId(dto.getReportId());
			
			dockedDao.save(invest);
			//对于不再引用的对象，及时把它的引用赋为null
			invest = null; //回收
			
			//如果内存确实很紧张，调用System.gc()  方法来建议垃圾回收器开始回收垃圾。
			//System.gc()  
		}
		return true;
	}
	
	//将信息评级转化成对应的评级
	private String creditRateToString(String creditRate) {
		String resultString = "";
		if (creditRate == null || creditRate.equals("") || creditRate.equals("11|无评级")) {
			resultString = "11|无评级";			
		} else if (creditRate.equals("AAA") || creditRate.equals("1|AAA")) {
			resultString = "1|AAA";			
		} else if (creditRate.equals("AA+") || creditRate.equals("2|AA+")) {
			resultString = "2|AA+";			
		}else if (creditRate.equals("AA") || creditRate.equals("3|AA")) {
			resultString = "3|AA";			
		}else if (creditRate.equals("AA-") || creditRate.equals("4|AA-")) {
			resultString = "4|AA-";			
		}else if (creditRate.equals("A+") || creditRate.equals("5|A+")) {
			resultString = "5|A+";			
		} else if (creditRate.equals("A") || creditRate.equals("6|A")) {
			resultString = "6|A";			
		} else if (creditRate.equals("A-") || creditRate.equals("7|A-")) {
			resultString = "7|A-";			
		}else if (creditRate.equals("BBB+") || creditRate.equals("8|BBB+")) {
			resultString = "8|BBB+";			
		}else if (creditRate.equals("BBB") || creditRate.equals("9|BBB")) {
			resultString = "9|BBB";			
		}else if (creditRate.equals("BBB-") || creditRate.equals("10|BBB-")) {
			resultString = "10|BBB-";			
		}
		return resultString;
	}
	/**
	 * 保存《CR05-企业债 》表的数据
	 * 
	 * 从第一行开始取值，通过第一行的字段名来判断后面行的数据，“证券简称”为空取值结束，后面即使有数据也读取
	 */
	private boolean saveCR05(UploadInfoDTO dto, List<Row> list, String tableName, String tableCode) {
		Row firstRow = list.get(0);
		
		for (int i = 1; i < list.size(); i++) {
			Row row = list.get(i);
			String fisrtCellData = ExcelUtil.getCellValue(row.getCell(index(firstRow, "证券简称")));
			if (fisrtCellData == null || fisrtCellData.length() ==0) {
				log.info("《CR05-企业债 》证券简称不能为空");
				break;
			}
			
			Invest_ABC invest = new Invest_ABC();
			
			//遍历每一行，通过第一行的字段名获取相应的数据
			for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
				String cellData = ExcelUtil.getCellValue(row.getCell(j));			
				
				//债券名称：＝底稿中的证券简称，铁道债和三峡债根据关键字“铁道”和“三峡债”进行剔除，其他数据填列在本表中,在数据库里处理；
				if (cellData != null && j == index(firstRow, "证券简称")) {
					invest.setItemName(cellData);				
				
					//剩余年限（年）：=底稿中的剩余年限；
				} else if (j == index(firstRow, "剩余年限")) {
					BigDecimal result = new BigDecimal(cellData).setScale(4, BigDecimal.ROUND_HALF_UP);
					invest.setAmount(cellData==null || "".equals(cellData)?
							new BigDecimal("0"): result);							
				
					//资产的信用评级=底稿中的债券外部评级；
				} else if (j == index(firstRow, "债券外部评级")) {
					cellData = creditRateToString(cellData);
					invest.setCreditRate(cellData);	
					
					//应收利息=底稿中的应收利息
				} else if ( j == index(firstRow, "应收利息")) {
					invest.setInterst(cellData==null || "".equals(cellData.trim())?
							new BigDecimal("0"):new BigDecimal(cellData));
					
					//认可价值=底稿中的净价市值；
				} else if (j == index(firstRow, "净价市值")) {
					invest.setRecognitionValue(cellData==null || "".equals(cellData.trim())?
							new BigDecimal("0"):new BigDecimal(cellData));
					
				}
	
			}
			
			invest.setTableName(tableName);
			invest.setTableCode(tableCode);
			invest.setItemCode(null);
			invest.setItemType(null);
			invest.setIssuer(null);	
			invest.setIsHuShen(null);
			invest.setPurchaseCost(null);
			
			invest.setYear(dto.getYearmonth());
			invest.setQuarter(dto.getQuarter());
			invest.setImportDate(new Date());
			invest.setImportDept("投资部");
			UserInfo userInfo = CurrentUser.CurrentUser;
			invest.setImportOperator(userInfo.getUserName());
			invest.setReportId(dto.getReportId());
			
			dockedDao.save(invest);
			//对于不再引用的对象，及时把它的引用赋为null
			invest = null; //回收
			
			//如果内存确实很紧张，调用System.gc()  方法来建议垃圾回收器开始回收垃圾。
			//System.gc()  
		}
		return true;
	}
	
	/**
	 * 保存《CR10-保险资管产品》表的数据
	 * 
	 * 从第一行开始取值，通过第一行的字段名来判断后面行的数据，“证券简称”为空取值结束，后面即使有数据也读取
	 */
	private boolean saveCR10(UploadInfoDTO dto, List<Row> list, String tableName, String tableCode) {
		Row firstRow = list.get(0);
		
		for (int i = 1; i < list.size(); i++) {
			Row row = list.get(i);
			String fisrtCellData = ExcelUtil.getCellValue(row.getCell(index(firstRow, "证券简称")));
			if (fisrtCellData == null || fisrtCellData.length() ==0) {
				log.info("《CR10-保险资管产品》证券简称不能为空");
				break;
			}
			
			Invest_ABC invest = new Invest_ABC();
			
			//遍历每一行，通过第一行的字段名获取相应的数据
			for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
				String cellData = ExcelUtil.getCellValue(row.getCell(j));			
				
				//名称：＝底稿中的证券简称，根据关键字“信托”，将含有信托的产品剔除；
				if (cellData != null && j == index(firstRow, "证券简称")) {
					invest.setItemName(cellData);				
				
					//发行机构=底稿中的发行人；
				} else if (j == index(firstRow, "发行人")) {
					invest.setIssuer(cellData);							
				
					//资产的信用评级，取底稿中的资产的信用评级，（手工维护在底稿中）；  
				} else if (j == index(firstRow, "资产的信用评级")) {
					cellData = creditRateToString(cellData);
					invest.setCreditRate(cellData);	
					
					//应收利息=底稿中的应收利息
				} else if ( j == index(firstRow, "应收利息")) {
					invest.setInterst(cellData==null || "".equals(cellData.trim())?
							new BigDecimal("0"):new BigDecimal(cellData));
					
					//认可价值=底稿中的净价市值；
				} else if (j == index(firstRow, "净价市值")) {
					invest.setRecognitionValue(cellData==null || "".equals(cellData.trim())?
							new BigDecimal("0"):new BigDecimal(cellData));
					
				}
	
			}
								
			invest.setTableName(tableName);
			invest.setTableCode(tableCode);
			invest.setItemCode(null);
			invest.setItemType(null);
			invest.setAmount(null);	
			invest.setIsHuShen(null);
			invest.setPurchaseCost(null);
			
			invest.setYear(dto.getYearmonth());
			invest.setQuarter(dto.getQuarter());
			invest.setImportDate(new Date());
			invest.setImportDept("投资部");
			UserInfo userInfo = CurrentUser.CurrentUser;
			invest.setImportOperator(userInfo.getUserName());
			invest.setReportId(dto.getReportId());
			
			dockedDao.save(invest);
			//对于不再引用的对象，及时把它的引用赋为null
			invest = null; //回收
			
			//如果内存确实很紧张，调用System.gc()  方法来建议垃圾回收器开始回收垃圾。
			//System.gc()  
		}
		return true;
	}
	

}
