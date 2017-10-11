package com.sinosoft.zcfz.service.impl.reportdataimport;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sinosoft.dao.CfmapDao;
import com.sinosoft.zcfz.dao.reportdataimport.CfReportDataDAO;
import com.sinosoft.zcfz.dao.reportdataimport.CfReportItemCodeDescDAO;
import com.sinosoft.zcfz.dao.reportdataimport.CfReportRowAddDataDAO;
import com.sinosoft.zcfz.dao.reportdataimport.CfReportRowAddDescDAO;
import com.sinosoft.zcfz.dao.reportdataimport.CommonImportDAO;
import com.sinosoft.zcfz.dao.reportdatamamage.CfReportDataDao;
import com.sinosoft.zcfz.dao.reportdatamamage.RowAddDataDao;
import com.sinosoft.zcfz.dto.reportdataimport.UploadInfoDTO;
import com.sinosoft.entity.Cfmap;
import com.sinosoft.zcfz.service.reportdataimport.CommonImportDateService;
import com.sinosoft.util.ExcelUtil;
import com.sinosoft.util.UploadNumberFormatException;
import com.sinosoft.util.UploaderServlet;

@Service
public class CommonImportDateServiceImpl implements CommonImportDateService {
	private static final Logger LOGGER = LoggerFactory.getLogger(CommonImportDateServiceImpl.class);
	//private List<UploadInfoDTO> uploadInfoDTOs = new ArrayList<UploadInfoDTO>();//存放错误结果集
	
	@Resource
	private CommonImportDAO impDao;
	@Resource 
	private CfmapDao mapDao;
	@Resource
	private ExcelUtil eu;
	@Resource
	private UploaderServlet us;
	@Resource
	private CfReportDataDAO impcfReportDataDao;
	@Resource
	private CfReportRowAddDataDAO impcfReportRowAddDataDao;
	@Resource
	private CfReportItemCodeDescDAO impcfReportItemCodeDescDao;
	@Resource
	private CfReportRowAddDescDAO impcfReportRowAddDescDao;
	@Resource
	private CfReportDataDao impcfReportDatadao; 
	@Resource
	private RowAddDataDao impcfRowAddDao;
/*
 * 总的下载的专用
 * 
 * 
 */
	@Override
	public void downloadPath(String year, String quarter, String reportrate, String reporttype, String modelPath, String reportId) {
		// TODO Auto-generated method stub
		
		Map<String, Object> map = new HashMap<String, Object>();
		map = qryAllItemCodeData(year, quarter, reportrate, reporttype,reportId);
		
		//行可扩展数据
		List<Map<String, Object>> maplist = new ArrayList<Map<String,Object>>();
		maplist = qryAllRowData(year, quarter, reportrate, reporttype,reportId);
		Map<String, Object> tblmap = maplist.get(0);
		Map<String, Object> rowmap = maplist.get(1);

		//获取数值型指标的类型和精度
		List<Map<String, Object>> itemAcclist = new ArrayList<Map<String,Object>>();
		itemAcclist = qryItemAccuracy();
		Map<String, Object> itemacc = itemAcclist.get(0);
		Map<String, Object> itemtype = itemAcclist.get(1);
		
		FileInputStream fis = null;
		XSSFWorkbook wb = null;
		
		try {
			fis = new FileInputStream(modelPath);
			wb = new XSSFWorkbook(fis);
			//遍历所有sheet，统计不属于map中key的sheet名
			for(int i = 0; i < wb.getNumberOfSheets(); i++){
				String sheetName = wb.getSheetName(i);
				if(sheetName.equals("A01#") || sheetName.equals("A02#") || sheetName.equals("Database")){
					if(sheetName.equals("Database")){
						wb.setSheetHidden(i, true);
					}
					continue;
				}
				try {
					dealItemCodeData(wb.getSheetAt(i), map, itemacc, itemtype);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					LOGGER.error("固定因子数据处理异常", e);
				}
				
				try{
					if(sheetName.contains("CR09")){
						dealCR09(wb.getSheetAt(i), rowmap, itemacc, itemtype);
					}else{
						dealRowAddData(wb.getSheetAt(i), tblmap, rowmap, itemacc, itemtype);
					}
				}catch(Exception e){
					LOGGER.error("行可扩展数据处理异常", e);
				}
				
			}
			
			FileOutputStream fos = new FileOutputStream(modelPath);
			wb.write(fos);
			fos.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOGGER.error(e.getMessage());
		}finally{
			if(wb != null){
				try {
					wb.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					LOGGER.error(e.getMessage());
				}
			}
		}
	}
	
	/**
	 * 固定因子数据封装
	 * @param year
	 * @param quarter
	 * @param reportrate
	 * @param reporttype
	 * @return
	 */
	public Map<String, Object> qryAllItemCodeData(String year, String quarter, String reportrate, String reporttype,String reportId){
		String qry = "select * from cfreportdata where reportId= '" + reportId
				+ "' and reportrate='" + reportrate 
				+ "' and reporttype='" + reporttype + "'";

		Map<String, Object> map = new HashMap<String, Object>();
		
		//文本对应关系 如AAA 1|AAA集合
		Map<String, String> doubleMaps = getMapInfo();
		
		try{
			List<?> list = impDao.queryBysql(qry);
			
			if(list !=null && list.size() > 0){
				for (Object object : list) {
					//一个_map对象对应数据库中的一条记录
					Map<String, Object> _map = (Map<String, Object>) object;
			
					String outitemtype = _map.get("outitemtype") == null ? "" : _map.get("outitemtype").toString();
					if(_map.get("outitemcode") != null && !_map.get("outitemcode").toString().equals("")){
						String code = _map.get("outitemcode").toString();
						
						if(outitemtype.equals("06") || outitemtype.equals("05") || outitemtype.equals("04") || outitemtype.equals("03") || outitemtype.equals("02") ){
							if(_map.get("reportitemvalue") != null){
								if (_map.get("isused")!=null) {
									map.put(code, String.valueOf("不适用"));
								}else {
									map.put(code, _map.get("reportitemvalue").toString());
								}
							}
						}else if(outitemtype.equals("07") || outitemtype.equals("01")){
							if(_map.get("textvalue") != null){
								//如果是公司类型的话，做带竖线的转换
								String txtString = _map.get("textvalue").toString();
								if(_map.get("outitemcode").toString().equals("C01_00003")){
									if(doubleMaps.containsKey(txtString)){
										map.put(code, doubleMaps.get(txtString).toString());
									}
								}if(_map.get("isused")!= null){
									if(_map.get("isused").toString().equals("0")){
										map.put(code, String.valueOf("不适用"));
									}
								}else{
									map.put(code, _map.get("textvalue").toString());
								}								
							}
						}else{
							map.put(code, "");
						}
					}
				}
			}
			
			return map;
		}catch(Exception e){
			LOGGER.error("固定因子数据封装异常", e);
			return map;
		}
	}
	
	/**
	 * 行可扩展数据封装
	 * @param year
	 * @param quarter
	 * @param reportrate
	 * @param reporttype
	 * @return
	 */
	public List<Map<String, Object>> qryAllRowData(String year, String quarter, String reportrate, String reporttype,String reportId){
		
		//获取需要映射的指标
		Map<String, String> rowMapCode = getMapCode();
		//文本对应关系 如AAA 1|AAA集合
		Map<String, String> doubleMaps = getMapInfo();

		String qry = "select tablecode, colcode, rowno, coltype, numvalue, wanvalue, textvalue from cfreportrowadddata t"
				+ " where reportId= '" + reportId
				+ "' and reportrate = '" + reportrate
				+ "' and reporttype = '" + reporttype
				+ "' group by rowno,tablecode, colcode, coltype, numvalue, wanvalue, textvalue order by colcode";
		
		String qryrowno = "select tablecode,max(to_number(rowno)) maxrowno from cfreportrowadddata "
				+ " where reportId= '" + reportId
				+ "' and reportrate = '" +  reportrate
				+ "' and reporttype = '" + reporttype
				+ "' group by tablecode order by tablecode";
		List<Map<String, Object>> maplist = new ArrayList<Map<String,Object>>();
		Map<String, Object> rowmap = new HashMap<String, Object>();
		Map<String, Object> tblmap = new HashMap<String, Object>();
		try{
			List<?> list = impDao.queryBysql(qry);
			List<?> rowlist = impDao.queryBysql(qryrowno);
			
			if(list != null && list.size() > 0 && rowlist != null && rowlist.size() > 0){
				
				for (Object object : rowlist) {
					Map<String, Object> _colmap = (Map<String, Object>)object;;
					tblmap.put(_colmap.get("tablecode").toString(), _colmap.get("maxrowno"));
				}
				
				
				for (Object obj : list) {
					Map<String, Object> temp = (Map<String, Object>) obj;
					/*int maxrowno = Integer.parseInt(temp.get("maxrowno").toString());
					colmap.put(temp.get("colcode").toString(), maxrowno);*/
					
					String coltype = temp.get("coltype") != null ? temp.get("coltype").toString() : "";
					if(temp.get("colcode") != null && !temp.get("colcode").toString().equals("") && temp.get("rowno") != null && !temp.get("rowno").toString().equals("")){
						
						String _colcode = temp.get("colcode").toString();
						//表外数据指标代码处理
						if(_colcode.startsWith("O")){
							_colcode = _colcode.substring(1);
						}
						
						//String _key = temp.get("colcode").toString() + "_" + temp.get("rowno").toString();
						String _key = _colcode + "_" + temp.get("rowno").toString();
						if(coltype.equals("06") || coltype.equals("05") || coltype.equals("04") || coltype.equals("03") || coltype.equals("02") ){
							if(temp.get("numvalue") != null){
								rowmap.put(_key, temp.get("numvalue").toString());
							}
						}else if(coltype.equals("07") || coltype.equals("01")){
							String txtString = temp.get("textvalue").toString();	
							if(temp.get("textvalue") != null){
								//是否包含需要转换的带竖线的指标编码
								if(rowMapCode.containsKey(_colcode)){
									//如果需要做竖线转换，则转换成不带竖线的文本内容展示在页面上
									if(doubleMaps.containsKey(txtString)){
									rowmap.put(_key, doubleMaps.get(txtString).toString());
									}
								}else{
									rowmap.put(_key, temp.get("textvalue").toString());
								}								
							}
						}else{
							rowmap.put(_key, "");
						}
					}
				}
			}
			maplist.add(tblmap);
			maplist.add(rowmap);
			return maplist;
		}catch(Exception e){
			LOGGER.error("行可扩展数据封装异常", e);
			return maplist;
		}
	}
	
	/**
	 * 固定因子数据填充到模板
	 * @param sheet
	 * @param map
	 */
	public void dealItemCodeData(XSSFSheet sheet, Map<String, Object> map, Map<String, Object> itemacc, Map<String, Object> itemtype) throws Exception{
		XSSFRow row = null;
		XSSFCell cell = null;
		XSSFComment comment = null;
		String strItemCode = "";//因子代码
		String strFlag = "";//反填模板的标签
		//boolean wanFlag = false;//是否万元
		if(map != null && map.size() > 0){
			System.out.println("=========sheet:" + sheet.getSheetName() + "固定因子数据处理Go=======");
			System.out.println("起始行："+sheet.getFirstRowNum() + "， 结束行："+sheet.getLastRowNum() + "， 共" + (sheet.getLastRowNum() - sheet.getFirstRowNum() + 1) + "行------");
			
			for(int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++){
				System.out.println("----程序中对应行号：" + i + "，excel文件中对应行号：" + (i+1) + "----");
				row = sheet.getRow(i);
				if(row == null){
					continue;
				}
				
				if(row.getFirstCellNum() == -1){
					continue;
				}
				
				System.out.println("--起始列："+row.getFirstCellNum() + ", 结束列：" + row.getLastCellNum() + "， 一行共" + (row.getLastCellNum() - row.getFirstCellNum() + 1) + "列--");

				for(int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++){
					System.out.println("程序中对应列号：" + j + "，excel文件中对应列号：" + (j+1));
					cell = row.getCell(j);
					if(cell == null){
						continue;
					}
					
					comment = cell.getCellComment();
					if(comment == null){
						continue;
					}
					strFlag = comment.getString().toString();
					/*if(strFlag.indexOf("/wan") != -1){
						wanFlag = true;
					}*/
					if(!strFlag.contains("<g_item(") || strFlag.indexOf("(") == -1 || strFlag.lastIndexOf(")") == -1){
						continue;
					}
					
					strItemCode = strFlag.substring(strFlag.indexOf("(") + 1, strFlag.lastIndexOf(")"));
					//System.out.println("strItemCode:" + strItemCode);
					if(!strItemCode.equals("")){
						if(map.get(strItemCode) != null){
							String strvalue = map.get(strItemCode).toString();
							if(itemtype.get(strItemCode) != null){
								String _itemtype = itemtype.get(strItemCode).toString();
								BigDecimal bd;
								if(_itemtype.equals("06") || _itemtype.equals("05") || _itemtype.equals("04") || _itemtype.equals("03") || _itemtype.equals("02")){
									if (strvalue.equals("不适用")) {
										cell.setCellValue(strvalue);
										cell.getCellStyle().setWrapText(true);
									}else {
									int _itemacc = Integer.parseInt(itemacc.get(strItemCode) == null ? "0" : itemacc.get(strItemCode).toString());
									bd = new BigDecimal(strvalue);
									cell.setCellValue(bd.setScale(_itemacc, BigDecimal.ROUND_HALF_UP).doubleValue());
									cell.getCellStyle().setWrapText(true);
									cell.getCellStyle().setAlignment(XSSFCellStyle.ALIGN_RIGHT);
									}
								}else{
									cell.setCellValue(strvalue);
									cell.getCellStyle().setWrapText(true);
								}
							}else{
								System.out.println("指标" + strItemCode + "在cfreportelement表中未定义！");
								LOGGER.info("==========指标" + strItemCode + "在cfreportelement表中未定义！==========");
							}
							
						}else{
							System.out.println("指标" + strItemCode + "无数据！");
							LOGGER.info("==========指标" + strItemCode + "无数据！==========");
						}
					}/*else{
						cell.setCellValue("");
					}*/
					
					//wanFlag = false;
				}
			}
			System.out.println("=========sheet:" + sheet.getSheetName() + "固定因子数据处理End=======");
		}
		sheet.setForceFormulaRecalculation(true);
	}
	
	/**
	 * 行可扩展数据处理
	 * @param sheet
	 * @param colmap
	 * @param rowmap
	 * @param itemacc
	 * @param itemtype
	 * @throws Exception
	 */
	public void dealRowAddData(XSSFSheet sheet , Map<String, Object> tblmap, Map<String, Object> rowmap, Map<String, Object> itemacc, Map<String, Object> itemtype) throws Exception{
		XSSFRow row = null;
		XSSFRow temprow = null;
		XSSFCell cell = null;
		XSSFCell tempcell = null;
		XSSFComment comment = null;
		String strItemCode = "";//因子代码
		String strFlag = "";//反填模板的标签
		String strTblCode = ""; //表代码
		String strColCode = ""; //列代码
		//boolean wanFlag = false;//是否万元
		
		if(rowmap != null && rowmap.size() > 0){
			System.out.println("=========sheet:" + sheet.getSheetName() + "行可扩展数据处理Go=======");
			System.out.println("------起始行："+sheet.getFirstRowNum() + "， 结束行："+sheet.getLastRowNum() + "， 共" + (sheet.getLastRowNum() - sheet.getFirstRowNum() + 1) + "行------");
			for(int rowNum = sheet.getFirstRowNum(); rowNum <= sheet.getLastRowNum(); rowNum++){
				System.out.println("----程序中对应行号：" + rowNum + "，excel文件中对应行号：" + (rowNum+1) + "----");
				row = sheet.getRow(rowNum);
				if(row == null){
					continue;
				}
				
				cell = row.getCell(0);
				if(cell == null){
					continue;
				}
				
				comment = cell.getCellComment();
				if(comment == null){
					continue;
				}
				
				strFlag = comment.getString().toString().trim();
				strTblCode = strFlag.substring(strFlag.indexOf("=") + 1, strFlag.indexOf("%"));
				
				System.out.println("--起始列："+row.getFirstCellNum() + ", 结束列：" + row.getLastCellNum() + "， 一行共" + (row.getLastCellNum() - row.getFirstCellNum() + 1) + "列--");
				for(int cellNum = 1; cellNum < row.getLastCellNum(); cellNum++){
					System.out.println("程序中对应列号：" + cellNum + "，excel文件中对应列号：" + (cellNum+1));
					cell = row.getCell(cellNum);
					if(cell == null){
						continue;
					}
					
					comment = cell.getCellComment();
					if(comment == null){
						continue;
					}
					
					strItemCode = comment.getString().toString();
					
					if(!strItemCode.equals("") || strItemCode.length() >= 6){
						/*if(strItemCode.contains("/10000") || strItemCode.contains("/wan")){
							wanFlag = true;
						}*/
						strItemCode = strItemCode.substring(0, 6);
						strColCode = strTblCode + "_" + strItemCode;
						if(tblmap.get(strTblCode) == null){
							continue;
						}
						
						//最大行号
						int maxrowno = Integer.parseInt(tblmap.get(strTblCode).toString());
						
						//遍历rowmap数据
						for(int dataRowNum = 1; dataRowNum <= maxrowno; dataRowNum++){
							//Map<String, Object> _rowmap = (Map<String, Object>) rowmap.get(dataRowNum + "");
							System.out.println("----程序中对应行号：" + (rowNum + dataRowNum - 1) + "，excel文件中对应行号：" + (rowNum + dataRowNum) + "----");
							temprow = sheet.getRow(rowNum + dataRowNum - 1);
							/*if(temprow == null){
								//插入新行
								POIUtil.insertRow(sheet.getWorkbook(), sheet, rowNum + dataRowNum - 1, 1);
								//System.out.println("新插入行，对应excel文件中的行号：" + rowNum + dataRowNum);
							}else{
								tempcell = temprow.getCell(0);
								if(tempcell == null || tempcell.getStringCellValue() == null || tempcell.getStringCellValue().equals("")){
									POIUtil.insertRow(sheet.getWorkbook(), sheet, rowNum + dataRowNum - 1, 1);
									//System.out.println("新插入行，对应excel文件中的行号：" + rowNum + dataRowNum);
								}
							}
							
							temprow = sheet.getRow(rowNum + dataRowNum - 1);
							temprow.getCell(0).setCellValue(strFlag.substring(strFlag.indexOf("%") + 1, strFlag.indexOf(">")).replace("x", dataRowNum + ""));
							tempcell = temprow.getCell(cellNum);*/
							XSSFRow upRow = sheet.getRow(rowNum);
							if(temprow == null){
								temprow = sheet.createRow(rowNum + dataRowNum - 1);
								temprow.createCell(0).setCellStyle(upRow.getCell(0).getCellStyle());
								temprow.createCell(cellNum);
								temprow.getCell(cellNum).setCellStyle(upRow.getCell(cellNum).getCellStyle());
							}else{
								if(temprow.getCell(0) == null){
									temprow.createCell(0).setCellStyle(upRow.getCell(0).getCellStyle());
								}
								if(temprow.getCell(cellNum) == null){
									temprow.createCell(0).setCellStyle(upRow.getCell(0).getCellStyle());
									temprow.createCell(cellNum);
									temprow.getCell(cellNum).setCellStyle(upRow.getCell(cellNum).getCellStyle());
								}
							}
							
							
							temprow.getCell(0).setCellValue(strFlag.substring(strFlag.indexOf("%") + 1, strFlag.indexOf(">")).replace("x", dataRowNum + ""));
							tempcell = temprow.getCell(cellNum);
							
							String colcode = strTblCode + "_" + strItemCode;
							String _colcode =  strColCode + "_" + dataRowNum;
							//判断map是否存在数据
							if(rowmap != null && rowmap.size() > 0){
								if(rowmap.get(_colcode) != null){
									String strValue = rowmap.get(_colcode).toString();
									
									if(itemtype.get(colcode) != null){
										String _itemtype = itemtype.get(colcode).toString();
										BigDecimal bd;
										if(_itemtype.equals("06") || _itemtype.equals("05") || _itemtype.equals("04") || _itemtype.equals("03") || _itemtype.equals("02")){
											int _itemacc = Integer.parseInt(itemacc.get(colcode) == null ? "0" : itemacc.get(colcode).toString());
											bd = new BigDecimal(strValue);
											tempcell.setCellValue(bd.setScale(_itemacc, BigDecimal.ROUND_HALF_UP).doubleValue());
											tempcell.getCellStyle().setWrapText(true);
											tempcell.getCellStyle().setAlignment(XSSFCellStyle.ALIGN_RIGHT);
										}else{
											tempcell.setCellValue(strValue);
											tempcell.getCellStyle().setWrapText(true);
										}
									}else{
										System.out.println("指标" + colcode + "在cfreportelement表中未定义！");
										LOGGER.info("==========指标" + colcode + "在cfreportelement表中未定义！==========");
									}
									
								}else{
									System.out.println("指标" + colcode + "无数据！");
									LOGGER.info("==========指标" + colcode + "无数据！==========");
								}
							}/*else{
								tempcell.setCellValue("");
							}*/
							
						}
					}
					
				}
			}
			System.out.println("=========sheet:" + sheet.getSheetName() + "行可扩展数据处理End=======");
		}
		
		sheet.setForceFormulaRecalculation(true);
	}
	
	public void dealCR09(XSSFSheet sheet , Map<String, Object> rowmap, Map<String, Object> itemacc, Map<String, Object> itemtype){
		XSSFRow row = null;
		XSSFCell cell = null;
		XSSFRow temprow = null;
		XSSFCell tempcell = null;
		XSSFComment comment = null;	//批注
		List<Integer> templist = new ArrayList<Integer>();
		int firCommRowNum = 0;  //第一列第一个包含批注的行号
		int secCommRowNum = 0;  //第一列第二个包含批注的行号
		String strValue = "";
		String strTblCode = "CR09";
		String strColCode = "";
		String colCode = "";
		
		
		//获取数据起始行
		for(int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++){
			row = sheet.getRow(i);
			if(row == null){
				continue;
			}
			
			cell = row.getCell(0);
			if(cell == null){
				continue;
			}
			
			comment = cell.getCellComment();
			if(comment != null){
				templist.add(i);
			}
		}
		
		firCommRowNum =  templist.get(0);
		secCommRowNum = templist.get(1);
		
		//处理第一个行可扩展项
		for(int rowNum = firCommRowNum; rowNum <= sheet.getLastRowNum(); rowNum++){
			row = sheet.getRow(rowNum);
			if(row == null){
				continue;
			}
			
			cell = row.getCell(0);
			if(cell == null){
				continue;
			}
			//如果是有一个.隔开的，则继续填充，否则不处理
			String  stre = cell.getStringCellValue();
			int pointCount = 0;
			for(int pointc=0;pointc<stre.length();pointc++){
				
				if(stre.substring(pointc, pointc+1).equals(".")){
					pointCount = pointCount+1;
				}
			}
			if(pointCount!=1 ){
				continue;
			}
			comment =  cell.getCellComment();
			if(comment == null){
				continue;
			}
			
			for(int cellNum = 1; cellNum < row.getLastCellNum(); cellNum++){
				cell = row.getCell(cellNum);
				if(cell == null){
					continue;
				}
				
				strValue = ExcelUtil.getCellValue(cell);
				if(strValue.contains("--")){
					continue;
				}
				
				comment = cell.getCellComment();
				strValue = comment.getString().toString();
				strColCode = strTblCode + "_" + strValue;
				
				for(int i = firCommRowNum; i <= sheet.getLastRowNum(); i++){
					temprow = sheet.getRow(i);
					
					if(temprow == null){
						continue;
					}
					
					cell = temprow.getCell(0);
					if(cell == null){
						continue;
					}
					
					strValue = cell.getStringCellValue();

					//判断字符串中有几个.,如果有一个则继续执行否则跳出
					pointCount = 0;
					for(int pointc=0;pointc<strValue.length();pointc++){
						
						if(strValue.substring(pointc, pointc+1).equals(".")){
							pointCount = pointCount+1;
						}
					}
					if(pointCount!=1){
						continue;
					}	
					
					tempcell = temprow.getCell(cellNum);
					colCode = strColCode + "_" + strValue.substring(2);
					if(rowmap != null && rowmap.size() > 0){
						if(rowmap.get(colCode) != null){
							String strVal = rowmap.get(colCode).toString();
							
							if(itemtype.get(strColCode) != null){
								String _itemtype = itemtype.get(strColCode).toString();
								BigDecimal bd;
								if(_itemtype.equals("06") || _itemtype.equals("05") || _itemtype.equals("04") || _itemtype.equals("03") || _itemtype.equals("02")){
									int _itemacc = Integer.parseInt(itemacc.get(strColCode) == null ? "0" : itemacc.get(strColCode).toString());
									bd = new BigDecimal(strVal);
									tempcell.setCellValue(bd.setScale(_itemacc, BigDecimal.ROUND_HALF_UP).doubleValue());
									tempcell.getCellStyle().setWrapText(true);
									tempcell.getCellStyle().setAlignment(XSSFCellStyle.ALIGN_RIGHT);
								}else{
									tempcell.setCellValue(strVal);
									tempcell.getCellStyle().setWrapText(true);
								}
							}else{
								System.out.println("指标" + strColCode + "在cfreportelement表中未定义！");
								LOGGER.info("==========指标" + strColCode + "在cfreportelement表中未定义！==========");
							}
						}
					}
				
				}
			}
		}
		
		//处理第二个行可扩展项
		for(int rowNum = secCommRowNum; rowNum <= sheet.getLastRowNum(); rowNum++){
			row = sheet.getRow(rowNum);
			if(row == null){
				continue;
			}
			
			cell = row.getCell(0);
			if(cell == null || cell.getStringCellValue().length() < 5){
				continue;
			}
			
			comment =  cell.getCellComment();
			if(comment == null){
				continue;
			}
			
			for(int cellNum = 1; cellNum < row.getLastCellNum(); cellNum++){
				cell = row.getCell(cellNum);
				if(cell == null){
					continue;
				}
				
				strValue = ExcelUtil.getCellValue(cell);
				if(strValue.contains("--")){
					continue;
				}
				
				comment = cell.getCellComment();
				strValue = comment.getString().toString();
				String _colCode = strValue;
				
				for(int j = secCommRowNum; j <= sheet.getLastRowNum(); j++){
					temprow = sheet.getRow(j);
					if(temprow == null){
						continue;
					}
					
					cell = temprow.getCell(0);
					if(cell == null){
						continue;
					}
					
					strValue = cell.getStringCellValue();
					if(strValue.length() < 5){
						continue;
					}
					
					tempcell = temprow.getCell(cellNum);
					//如何是超过9个大列的。则多出一位 如1.10.1
					if(strValue.length()==6){
						strColCode = strTblCode + "_0" + strValue.substring(5) + "_" + _colCode;
						colCode = strColCode + "_" + strValue.substring(2,4);
					}else{
						strColCode = strTblCode + "_0" + strValue.substring(4) + "_" + _colCode;
						colCode = strColCode + "_" + strValue.substring(2,3);
					}	
					
					if(rowmap != null && rowmap.size() > 0){
						if(rowmap.get(colCode) != null){
							String strVal = rowmap.get(colCode).toString();
							
							if(itemtype.get(strColCode) != null){
								String _itemtype = itemtype.get(strColCode).toString();
								BigDecimal bd;
								if(_itemtype.equals("06") || _itemtype.equals("05") || _itemtype.equals("04") || _itemtype.equals("03") || _itemtype.equals("02")){
									int _itemacc = Integer.parseInt(itemacc.get(strColCode) == null ? "0" : itemacc.get(strColCode).toString());
									bd = new BigDecimal(strVal);
									tempcell.setCellValue(bd.setScale(_itemacc, BigDecimal.ROUND_HALF_UP).doubleValue());
									tempcell.getCellStyle().setWrapText(true);
									tempcell.getCellStyle().setAlignment(XSSFCellStyle.ALIGN_RIGHT);
								}else{
									tempcell.setCellValue(strVal);
									tempcell.getCellStyle().setWrapText(true);
								}
							}else{
								System.out.println("指标" + strColCode + "在cfreportelement表中未定义！");
								LOGGER.info("==========指标" + strColCode + "在cfreportelement表中未定义！==========");
							}
						}
					}
				}
			}
		}
		
	}
	
	public List<Map<String,Object>> qryItemAccuracy(){
		String sql = "select * from (select t.portitemcode code, i.outitemtype itemtype, t.decimals from cfreportelement t, cfreportitemcodedesc i "
				+ "where t.portitemcode = i.outitemcode"
				+ " union  "
				+ "select t.portitemcode code, r.coltype itemtype, t.decimals from cfreportelement t, cfreportrowadddesc r "
				+ "where t.portitemcode = r.colcode)";
		List<?> list = impDao.queryBysql(sql);
		List<Map<String,Object>> maplist = new ArrayList<Map<String,Object>>();
		Map<String, Object> _map = new HashMap<String, Object>();
		Map<String, Object> mapitemacc = new HashMap<String, Object>();
		Map<String, Object> mapitemtype = new HashMap<String, Object>();
		if(list != null && list.size() > 0){
			for (Object obj : list) {
				_map = (Map<String, Object>) obj;
				mapitemacc.put(_map.get("code").toString(), _map.get("decimals"));
				mapitemtype.put(_map.get("code").toString(), _map.get("itemtype"));
			}
			maplist.add(mapitemacc);
			maplist.add(mapitemtype);
		}
		
		
		/*String osql = "select code, itemtype, decimals from (select t1.outitemcode code, t1.outitemtype itemtype, t1.decimals decimals from rsys_cfreportitemcodedesc t1 union select t2.colcode code, t2.coltype itemtype, t2.decimals decimals from rsys_cfreportrowadddesc t2)";
		list = impDao.queryBysql(osql);
		if(list != null && list.size() > 0){
			for (Object obj : list) {
				_map = (Map<String, Object>) obj;
				
				mapitemacc.put(_map.get("code").toString().substring(1), _map.get("decimals"));
				mapitemtype.put(_map.get("code").toString().substring(1), _map.get("itemtype"));
			}
			maplist.add(mapitemacc);
			maplist.add(mapitemtype);
		}*/
		
		//表外行可扩展
		String osql_item= "select code, itemtype, decimals from (select t2.colcode code, t2.coltype itemtype, t2.decimals decimals from rsys_cfreportrowadddesc t2)";
		list = impDao.queryBysql(osql_item);
		if(list != null && list.size() > 0){
			for (Object obj : list) {
				_map = (Map<String, Object>) obj;
				
				mapitemacc.put(_map.get("code").toString().substring(1), _map.get("decimals"));
				mapitemtype.put(_map.get("code").toString().substring(1), _map.get("itemtype"));
			}
			maplist.add(mapitemacc);
			maplist.add(mapitemtype);
		}
		
		//表外固定因子
		String osql_rowadd = "select code, itemtype, decimals from (select t1.outitemcode code, t1.outitemtype itemtype, t1.decimals decimals from rsys_cfreportitemcodedesc t1)";
		list = impDao.queryBysql(osql_rowadd);
		if(list != null && list.size() > 0){
			for (Object obj : list) {
				_map = (Map<String, Object>) obj;
				
				mapitemacc.put(_map.get("code").toString(), _map.get("decimals"));
				mapitemtype.put(_map.get("code").toString(), _map.get("itemtype"));
			}
			maplist.add(mapitemacc);
			maplist.add(mapitemtype);
		}
		
		return maplist;
		
		
	}

	@Override
	public void saveData(UploadInfoDTO pld) throws UploadNumberFormatException,
			IOException {
		// TODO Auto-generated method stub
		
	}
	/**********************
	 * 获得行可扩展描述表中
	 * 需要做竖线转换的的指标代码
	 * *******************/
	@SuppressWarnings("unchecked")
	@Transactional
	public Map<String, String> getMapCode(){
		Map<String, String> map = new HashMap<String, String>();
		String getRowAddCode="select * from cfreportrowadddesc cf where cf.group1=1";
		
		List<?> list = (List<?>) impcfReportRowAddDescDao.queryBysql(getRowAddCode);
		if(list.size()>0){
			for(int m=0;m<list.size();m++){
				
				Map<String, Object> rowMap = (Map<String, Object>) list.get(m);
				String rowCode =  rowMap.get("colCode").toString();
				if(rowCode!=null && !rowCode.equals("")){
					map.put(rowCode, rowCode);
				}					
			}
		}			
		return map;
	}
	/**********************
	 * 获取带竖线内容的映射对应关系
	 * 如1|AAA = AAA * 
	 *********************/	
	@SuppressWarnings("unchecked")
	@Transactional
	public Map<String, String> getMapInfo(){
		Map<String, String> map = new HashMap<String, String>();
		String getRowMapSQl=" select oriname,sysname from cfmap t ";
		
		List<Cfmap> list = (List<Cfmap>) mapDao.queryBysql(getRowMapSQl);
		if(list.size()>0){
			for(int m=0;m<list.size();m++){
				Map<String, Object> rowMap = (Map<String, Object>) list.get(m);
				
				String oriName =  rowMap.get("oriName").toString();
				String sysName =  rowMap.get("sysName").toString();
				if(oriName!=null && !oriName.equals("")){
					map.put(sysName,oriName);
				}					
			}
		}			
		return map;
	}
}
