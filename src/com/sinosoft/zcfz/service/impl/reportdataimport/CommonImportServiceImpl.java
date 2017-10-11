package com.sinosoft.zcfz.service.impl.reportdataimport;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sinosoft.common.Dao;
import com.sinosoft.controller.Login;
import com.sinosoft.dao.CfmapDao;
import com.sinosoft.zcfz.dao.reportdataimport.CfReportDataDAO;
import com.sinosoft.zcfz.dao.reportdataimport.CfReportItemCodeDescDAO;
import com.sinosoft.zcfz.dao.reportdataimport.CfReportRowAddDataDAO;
import com.sinosoft.zcfz.dao.reportdataimport.CfReportRowAddDescDAO;
import com.sinosoft.zcfz.dao.reportdataimport.CommonImportDAO;
import com.sinosoft.zcfz.dao.reportdatamamage.CfReportDataDao;
import com.sinosoft.zcfz.dao.reportdatamamage.RowAddDataDao;
import com.sinosoft.dto.UserinfoDTO;
import com.sinosoft.zcfz.dto.reportdataimport.UploadInfoDTO;
import com.sinosoft.entity.CfReportData;
import com.sinosoft.entity.CfReportDataId;
import com.sinosoft.entity.CfReportRowAddData;
import com.sinosoft.entity.CfReportRowAddDataId;
import com.sinosoft.entity.Cfmap;
import com.sinosoft.entity.ReportHistory;
import com.sinosoft.entity.RowAddHistory;
import com.sinosoft.entity.UserInfo;
import com.sinosoft.zcfz.service.reportdataimport.CommonImportService;
import com.sinosoft.util.Config;
import com.sinosoft.util.ExcelUtil;
import com.sinosoft.util.POIUtil;
import com.sinosoft.util.UploadNumberFormatException;
import com.sinosoft.util.UploaderServlet;
@Service
public class CommonImportServiceImpl implements CommonImportService {
	private static final Logger LOGGER = LoggerFactory.getLogger(CommonImportServiceImpl.class);
	private List<UploadInfoDTO> uploadInfoDTOs = new ArrayList<UploadInfoDTO>();//存放错误结果集
	private Map<String, String> codemap=new HashMap<String, String>();
	private Map<String, Object> hismap=new HashMap<String, Object>();
	private SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
	@Resource
	private CommonImportDAO impDao;
	@Resource 
	private CfmapDao mapDao;
	@Resource
	private Dao dao;
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
	/**
	 * 查询固定因子数据，结果集封装成Map
	 * @param year
	 * @param quarter
	 * @param reportCode 报表编号
	 * @param deptNo 部门编号
	 * @return
	 */
	public Map<String, Object> qryDataByDeptNo(String year, String quarter, String reportrate, String reporttype, String deptNo,String reportId) {
		// TODO Auto-generated method stub

		//文本对应关系 如AAA 1|AAA集合
		Map<String, String> doubleDownMaps = getMapInfoDown();		
		//获取固定因子数据
		String qry = "select * from cfreportdata where reportId= '" + reportId 
				+ "' and reportrate='" + reportrate 
				+ "' and reporttype='" + reporttype 
				+ "' and (outitemcode in (select outitemcode from cfreportitemcodedesc where reportitemtype='" + deptNo + "')"
				+ " or outitemcode in (select outitemcode from rsys_cfreportitemcodedesc where importsource=0 and reportitemtype='" + deptNo + "'))";

		Map<String, Object> map = new HashMap<String, Object>();
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
							if (_map.get("isused")!=null) {
								map.put(code, String.valueOf("不适用"));
							}else {
								map.put(code, _map.get("reportitemvalue").toString());
							}
						}else if(outitemtype.equals("07") || outitemtype.equals("01")){
							if(_map.get("textvalue") != null){
								//如果是公司类型的话，做带竖线的转换
								String txtString = _map.get("textvalue").toString();
								if(_map.get("outitemcode").toString().equals("C01_00003")){
									if(doubleDownMaps.containsKey(txtString)){
										map.put(code, doubleDownMaps.get(txtString).toString());
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
			LOGGER.error("固定因子数据封装出错", e);
			return map;
		}
	}

	/**
	 *  查询行可扩展数据，结果集封装成Map
	 * @param year	年度
	 * @param quarter 季度
	 * @param reportCode 报表编号
	 * @param deptNo 部门编号
	 * @return
	 */
	public List<Map<String, Object>> qryRowDataByDeptNo(String year, String quarter, String reportrate, String reporttype, String deptNo,String reportId){

		//获取需要映射的指标
		Map<String, String> rowMapCodes = getMapCode();
		//文本对应关系 如1|AAA = AAA  集合
		Map<String, String> doubleDownMaps = getMapInfoDown();		
		//获取数据
		String qry = "select colcode, rowno, coltype, numvalue, wanvalue, textvalue from cfreportrowadddata " 
				+ " where reportId= '" + reportId
				//	+ "' and quarter = '" + quarter
				+ "' and reportrate = '" +  reportrate
				+ "' and reporttype = '" + reporttype
				+ "' and colcode in (select colcode from cfreportrowadddesc where deptno = '" + deptNo + 
				"') group by rowno, colcode, coltype, numvalue, wanvalue, textvalue order by rowno";

		//获取tablecode中最大行号
		String qryrowno = "select tablecode,max(to_number(rowno)) maxrowno from cfreportrowadddata "
				+ " where reportId= '" + reportId
				//+ "' and quarter = '" + quarter
				+ "' and reportrate = '" +  reportrate
				+ "' and reporttype = '" + reporttype
				+ "' and colcode in (select colcode from cfreportrowadddesc where deptno = '" + deptNo  
				+ "') group by tablecode order by tablecode";

		List<Map<String, Object>> maplist = new ArrayList<Map<String,Object>>();
		Map<String, Object> rowmap = new HashMap<String, Object>();
		Map<String, Object> tblmap = new HashMap<String, Object>();

		try{
			List<?> list = impDao.queryBysql(qry);
			List<?> rowlist = impDao.queryBysql(qryrowno);

			if(list != null && list.size() > 0 && rowlist != null && rowlist.size() > 0){
				//对行号数据封装
				for (Object object : rowlist) {
					Map<String, Object> _colmap = (Map<String, Object>)object;;
					tblmap.put(_colmap.get("tablecode").toString(), _colmap.get("maxrowno"));
				}

				//对指标数据封装
				for (Object obj : list) {
					Map<String, Object> temp = (Map<String, Object>) obj;

					String coltype = temp.get("coltype") != null ? temp.get("coltype").toString() : "";
					if(temp.get("colcode") != null && !temp.get("colcode").toString().equals("") && temp.get("rowno") != null && !temp.get("rowno").toString().equals("")){

						String _colcode = temp.get("colcode").toString();
						if(_colcode.startsWith("O")){
							_colcode = _colcode.substring(1);
						}

						String _key = _colcode + "_" + temp.get("rowno").toString();
						if(coltype.equals("06") || coltype.equals("05") || coltype.equals("04") || coltype.equals("03") || coltype.equals("02") ){
							if(temp.get("numvalue") != null){
								rowmap.put(_key, temp.get("numvalue").toString());
							}
						}else if(coltype.equals("07") || coltype.equals("01")){
							String txtString = temp.get("textvalue").toString();	
							if(temp.get("textvalue") != null){
								//是否包含需要转换的带竖线的指标编码
								if(rowMapCodes.containsKey(_colcode)){
									//如果需要做竖线转换，则转换成不带竖线的文本内容展示在页面上
									if(doubleDownMaps.containsKey(txtString)){
										rowmap.put(_key, doubleDownMaps.get(txtString).toString());
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
/*
 * 分部门的下载专用
 */
	@Override
	public void downloadPath(String year, String quarter, String reportrate, String reporttype, String deptNo, String modelPath, String modelType,String reportId) {
		// TODO Auto-generated method stub

		FileInputStream fis = null;
		XSSFWorkbook wb = null;

		try {
			fis = new FileInputStream(modelPath);
			wb = new XSSFWorkbook(fis);

			//获取部门对应的sheet
			Map<String, Object>  sheetNames = qrySheetNames(deptNo);
			List<String> templist = new ArrayList<String>();

			//遍历所有sheet，统计不属于map中key的sheet名
			for(int i = 0; i < wb.getNumberOfSheets(); i++){
				String sheetName = wb.getSheetName(i);
				if(sheetName.equals("A01#") || sheetName.equals("A02#") || sheetName.equals("Database")){
					if(sheetName.equals("Database")){
						wb.setSheetHidden(i, true);
					}
					continue;
				}
				if(!sheetNames.containsKey(sheetName)){
					templist.add(sheetName);
				}
			}

			//wb删除sheet
			for (String str : templist) {
				wb.removeSheetAt(wb.getSheetIndex(str));
			}

			if(modelType.equals("1")){
				//固定因子数据
				Map<String, Object> map = qryDataByDeptNo(year, quarter, reportrate, reporttype, deptNo,reportId);

				//行可扩展数据
				List<Map<String, Object>> maplist = qryRowDataByDeptNo(year, quarter, reportrate, reporttype, deptNo,reportId);
				Map<String, Object> tblmap = maplist.get(0);
				Map<String, Object> rowmap = maplist.get(1);

				//获取数值型指标的类型和精度
				List<Map<String, Object>> itemAcclist = qryItemAccuracy();
				Map<String, Object> itemacc = itemAcclist.get(0);
				Map<String, Object> itemtype = itemAcclist.get(1);

				for(int i = 0; i < wb.getNumberOfSheets(); i++){
					String sheetName = wb.getSheetName(i);
					if(sheetName.equals("A01#") || sheetName.equals("A02#") || sheetName.equals("Database")){
						if(sheetName.equals("Database")){
							wb.setSheetHidden(i, true);
						}
						continue;
					}

					try {
						if(sheetName.contains("CR09")){
							dealCR09(wb.getSheetAt(i), rowmap, itemacc, itemtype);
						}else{
							dealItemCodeData(wb.getSheetAt(i), map, itemacc, itemtype);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						LOGGER.error("固定因子数据处理异常", e);
					}

					try{
						dealRowAddData(wb.getSheetAt(i), tblmap, rowmap, itemacc, itemtype);
					}catch(Exception e){
						LOGGER.error("行可扩展数据处理异常", e);
					}
				}
			}

			FileOutputStream fos = new FileOutputStream(modelPath);
			wb.write(fos);
			fos.flush();
			fos.close();
			System.gc();

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

		if(map != null && map.size() > 0){
			System.out.println("=========sheet:" + sheet.getSheetName() + "固定因子数据处理Go=======");
			System.out.println("------起始行："+sheet.getFirstRowNum() + "， 结束行："+sheet.getLastRowNum() + "， 共" + (sheet.getLastRowNum() - sheet.getFirstRowNum() + 1) + "行------");

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
					System.out.println("程序中对应列号：" + j + "，导入excel文件中对应列号：" + (j+1));

					cell = row.getCell(j);
					if(cell == null){
						continue;
					}

					//获取批注
					comment = cell.getCellComment();
					if(comment == null){
						continue;
					}

					strFlag = comment.getString().toString();
					if(!strFlag.contains("<g_item(") || strFlag.indexOf("(") == -1 || strFlag.lastIndexOf(")") == -1){
						continue;
					}

					//截取批注中的指标代码
					strItemCode = strFlag.substring(strFlag.indexOf("(") + 1, strFlag.lastIndexOf(")"));

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
							System.out.println("指标" + strItemCode + "未定义或不属于该部门！");
							LOGGER.info("==========指标" + strItemCode + "未定义或不属于该部门！==========");
						}
					}/*else{
						cell.setCellValue("");
					}*/
				}
			}
			System.out.println("=========sheet:" + sheet.getSheetName() + "固定因子数据处理End=======");
		}
		sheet.setForceFormulaRecalculation(true);
	}

	/**
	 * 行可扩展数据填充到模板
	 * @param sheet
	 * @param rowmap 说明：<行号,<指标代码,数据>>
	 */
	public void dealRowAddData(XSSFSheet sheet, Map<String, Object> tblmap, Map<String, Object> rowmap, Map<String, Object> itemacc, Map<String, Object> itemtype) throws Exception{
		XSSFRow row = null;
		XSSFRow temprow = null;
		XSSFCell cell = null;
		XSSFCell tempcell = null;
		XSSFComment comment = null;
		String strItemCode = "";//因子代码
		String strFlag = "";//反填模板的标签
		String strTblCode = ""; //表代码
		String strColCode = ""; //列代码

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

						strItemCode = strItemCode.substring(0, 6);
						strColCode = strTblCode + "_" + strItemCode;
						if(tblmap.get(strTblCode) == null){
							continue;
						}

						//最大行号 
						int maxrowno = Integer.parseInt(tblmap.get(strTblCode).toString());

						//遍历rowmap数据
						for(int dataRowNum = 1; dataRowNum <= maxrowno; dataRowNum++){
							System.out.println("----程序中对应行号：" + (rowNum + dataRowNum - 1) + "，excel文件中对应行号：" + (rowNum + dataRowNum) + "----");
							temprow = sheet.getRow(rowNum + dataRowNum - 1);

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
									System.out.println("指标" + colcode + "未定义或不属于该部门！");
									LOGGER.info("==========指标" + colcode + "未定义或不属于该部门！==========");
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
			if(cell == null || cell.getStringCellValue().length() > 3){
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
					if(strValue.length() > 3){
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
			if(cell == null || cell.getStringCellValue().length() < 4){
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
					if(strValue.length() < 4){
						continue;
					}

					tempcell = temprow.getCell(cellNum);
					strColCode = strTblCode + "_0" + strValue.substring(4) + "_" + _colCode;
					colCode = strColCode + "_" + strValue.substring(2,3);

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

	/**
	 * 查询部门对应的报表名称
	 * @param deptNo 部门编号
	 * @return
	 */
	public Map<String, Object> qrySheetNames(String deptNo){

		String qry = "select reportcode,remark from cfreportdesc where remark is not null and department like '%" + deptNo + "%'";
		List<?> list = impDao.queryBysql(qry);
		Map<String, Object> map = new HashMap<String, Object>();
		if(list != null && list.size() > 0){
			for (Object obj : list) {
				Map<String, Object> _map = (Map<String, Object>) obj;
				map.put(_map.get("remark").toString(), _map.get("remark").toString());
			}
		}

		return map;
	}



	@Transactional
	public void saveData(UploadInfoDTO pld) throws UploadNumberFormatException,
	IOException,Exception {
		UserInfo userinfo = (UserInfo) pld.getRequest().getSession().getAttribute("currentUser");
		uploadInfoDTOs.clear();
		eu.setExcelPath(pld.getFilePath()+"/"+us.getFileName());
		if("1".equals(pld.getReporttype())&&"1".equals(pld.getReportname())){//季度快报->偿付能力报告	
			eu.setStartReadPos(1);
			eu.setEndReadPos(0);
			eu.setSelectedSheetName("A01#");
			List<Row> listA01=eu.readExcel();
			saveDataA01(listA01,pld, userinfo.getUserCode());
			LOGGER.error("固定因子数据保存异常这是分部门的——————————季度快报->偿付能力报告——————————————————");
			listA01.removeAll(listA01);
		}
		if("2".equals(pld.getReporttype())||"3".equals(pld.getReporttype())){//季度报告偿付能力报告或临时报告
			if("1".equals(pld.getReportname())){//偿付能力报告
				eu.setStartReadPos(1);
				eu.setEndReadPos(0);
				eu.setSelectedSheetName("A01#");
				List<Row> listA01=eu.readExcel();
				saveDataA01(listA01,pld, userinfo.getUserCode());
				LOGGER.error("固定因子数据保存异常这是分部门的——————————季度报告偿付能力报告或临时报告——————————————————");
				listA01.removeAll(listA01);
				eu.setSelectedSheetName("A02#");
				List<Row> listA02=eu.readExcel();
				saveDataA02(listA02,pld, userinfo.getUserCode());
				LOGGER.error("行可扩展数据保存异常——————————季度报告偿付能力报告或临时报告——————————————————");
				listA02.removeAll(listA02);				
			}else if("2".equals(pld.getReportname())||"3".equals(pld.getReportname())){//Word报告或现金流压力测试报告
				eu.setStartReadPos(1);
				eu.setEndReadPos(0);
				eu.setSelectedSheetName("A01#");
				List<Row> listA01=eu.readExcel();
				saveDataA01(listA01,pld, userinfo.getUserCode());
				LOGGER.error("固定因子数据保存异常------Word报告或现金流压力测试报告-");
				listA01.removeAll(listA01);
			}
		}
	}

	@Transactional
	public void saveDataA01(List<Row> list,UploadInfoDTO pld, String userCode)throws UploadNumberFormatException,NullPointerException,Exception{
		// TODO Auto-generated method stub

		//文本对应关系 如AAA 1|AAA集合
		Map<String, String> doubleMap = getMapInfo();

		//删除历史数据且只删除对应部门的手工录入数据
		cfReportItemCodeHis(pld);
		//根据部门获取报表，然后根据报表和来源获取手工录入指标
		getCfReportItemCodeDesc(pld.getDepartment());

		int k=0;
		for(int i=0;i<list.size();i++){
			Row row=list.get(i);
			System.out.println("row.getLastCellNum()="+row.getLastCellNum());
			//判断list中的记录是否在手工录入指标集
			if(!codemap.containsKey(row.getCell(0).getStringCellValue())){
				continue;
			}

			String strTemp=(String)codemap.get(row.getCell(0).getStringCellValue());//可能空指针异常 
			if(strTemp==null){
				System.out.println("指标描述表未查到该因子代码");
				continue;
			}
			CfReportDataId cfReportDataId=new CfReportDataId();
			CfReportData cfReportData=new CfReportData();
			cfReportDataId.setOutItemCode(row.getCell(0).getStringCellValue());//因子代码
			cfReportData.setMonth(pld.getYearmonth());//月度
			cfReportData.setQuarter(pld.getQuarter());//季度
			cfReportData.setReportRate(pld.getReporttype());//报送频度
			cfReportDataId.setReportId(pld.getReportId());
			cfReportData.setId(cfReportDataId);
			cfReportData.setComCode(Config.getProperty("OrganCode"));//机构代码
			cfReportData.setOutItemCodeName(row.getCell(1).getStringCellValue());//因子名称
			cfReportData.setReportItemCode(row.getCell(0).getStringCellValue());//内部码
			System.out.println(row.getCell(3).getStringCellValue());
			System.out.println(ExcelUtil.getCellValue(row.getCell(2)));

			String temp[]=strTemp.split("_");
			String strOutItemType=temp[0];
			String yinziCode=row.getCell(0).getStringCellValue();//获得因子的代码
			//String yinziValue=ExcelUtil.getCellValue(row.getCell(2));
			if(strOutItemType.equals("06")){//百分比型
				try {
					if("".equals(ExcelUtil.getCellValue(row.getCell(2)))||"--".equals(ExcelUtil.getCellValue(row.getCell(2)))||"-".equals(ExcelUtil.getCellValue(row.getCell(2)))){		    		   
						System.out.println("无值可取！！！");
						cfReportData.setReportItemValue(new BigDecimal(0).setScale(8,BigDecimal.ROUND_HALF_UP));
						cfReportData.setReportItemWanValue(new BigDecimal(0).setScale(12,BigDecimal.ROUND_HALF_UP));
					}else if(ExcelUtil.getCellValue(row.getCell(2)).trim().contains("不适用")){
						cfReportData.setReportItemValue(new BigDecimal(0).setScale(8,BigDecimal.ROUND_HALF_UP));
						cfReportData.setReportItemWanValue(new BigDecimal(0).setScale(12,BigDecimal.ROUND_HALF_UP));
						cfReportData.setIsused("0");
					}
					else{
						cfReportData.setReportItemValue(new BigDecimal(ExcelUtil.getCellValue(row.getCell(2))).setScale(8,BigDecimal.ROUND_HALF_UP));
						cfReportData.setReportItemWanValue(new BigDecimal(0).setScale(12,BigDecimal.ROUND_HALF_UP));
					}
				} catch (NumberFormatException e) {
					//e.printStackTrace();
					storeA01UploadInfoDTOs(row,strOutItemType);
					//continue;
					throw new  RuntimeException(row.getCell(0).getStringCellValue()+"格式不对");

				}
				cfReportData.setOutItemType("06");
				cfReportData.setTextValue("");
				cfReportData.setDesText(null);//描述性型
				cfReportData.setFileText(null);//文件型
			}else if(strOutItemType.equals("05")){//数量型
				try {
					if("".equals(ExcelUtil.getCellValue(row.getCell(2)))||"--".equals(ExcelUtil.getCellValue(row.getCell(2)))||"-".equals(ExcelUtil.getCellValue(row.getCell(2)))){		    		   
						System.out.println("无值可取！！！");
						cfReportData.setReportItemValue(new BigDecimal(0).setScale(8,BigDecimal.ROUND_HALF_UP));
						cfReportData.setReportItemWanValue(new BigDecimal(0).setScale(12,BigDecimal.ROUND_HALF_UP));
					}else if(ExcelUtil.getCellValue(row.getCell(2)).trim().contains("不适用")){
						cfReportData.setReportItemValue(new BigDecimal(0).setScale(8,BigDecimal.ROUND_HALF_UP));
						cfReportData.setReportItemWanValue(new BigDecimal(0).setScale(12,BigDecimal.ROUND_HALF_UP));
						cfReportData.setIsused("0");
					}
					else{
						cfReportData.setReportItemValue(new BigDecimal(ExcelUtil.getCellValue(row.getCell(2))).setScale(8,BigDecimal.ROUND_HALF_UP));
						cfReportData.setReportItemWanValue(new BigDecimal(ExcelUtil.getCellValue(row.getCell(2))).divide(new BigDecimal("10000"), 12, RoundingMode.HALF_UP));
					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
					storeA01UploadInfoDTOs(row,strOutItemType);
					// continue;
					throw new  RuntimeException(row.getCell(0).getStringCellValue()+"格式不对");

				}
				cfReportData.setOutItemType("05");
				cfReportData.setTextValue("");
				cfReportData.setDesText(null);//描述性型
				cfReportData.setFileText(null);//文件型
			}else if(strOutItemType.equals("04")){//数值型
				try {
					if("".equals(ExcelUtil.getCellValue(row.getCell(2)))||"--".equals(ExcelUtil.getCellValue(row.getCell(2)))
							||"--".equals(ExcelUtil.getCellValue(row.getCell(2))) ){		
						System.out.println("无值可取！！！");
						cfReportData.setReportItemValue(new BigDecimal(0).setScale(8,BigDecimal.ROUND_HALF_UP));
						cfReportData.setReportItemWanValue(new BigDecimal(0).setScale(12,BigDecimal.ROUND_HALF_UP));
					}else if(ExcelUtil.getCellValue(row.getCell(2)).trim().contains("不适用")){
						cfReportData.setReportItemValue(new BigDecimal(0).setScale(8,BigDecimal.ROUND_HALF_UP));
						cfReportData.setReportItemWanValue(new BigDecimal(0).setScale(12,BigDecimal.ROUND_HALF_UP));
						cfReportData.setIsused("0");
					}
					else{
						cfReportData.setReportItemValue(new BigDecimal(ExcelUtil.getCellValue(row.getCell(2))).setScale(8,BigDecimal.ROUND_HALF_UP));
						cfReportData.setReportItemWanValue(new BigDecimal(ExcelUtil.getCellValue(row.getCell(2))).divide(new BigDecimal("10000"), 12, RoundingMode.HALF_UP));
					}
					/*
					 * 判断这几个数是否有值，如果是在A01中的值是0，就把这几个数设为空，不然下载数据时IR01会出现多余的RF0和K
					 */
					if(turnNull(yinziCode, ExcelUtil.getCellValue(row.getCell(2)))){
						cfReportData.setReportItemValue(null);
						cfReportData.setReportItemWanValue(null);
					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
					storeA01UploadInfoDTOs(row,strOutItemType);
					//continue;
					throw new  RuntimeException(row.getCell(0).getStringCellValue()+"格式不对");

				}
				cfReportData.setOutItemType("04");
				cfReportData.setTextValue("");
				cfReportData.setDesText(null);//描述性型
				cfReportData.setFileText(null);//文件型
			}else if(strOutItemType.equals("03")){//文件型
				try {
					cfReportData.setOutItemType("03");
					cfReportData.setReportItemValue(new BigDecimal(0).setScale(4,BigDecimal.ROUND_HALF_UP));
					cfReportData.setReportItemWanValue(new BigDecimal(0).setScale(8,BigDecimal.ROUND_HALF_UP));
				} catch (NumberFormatException e) {
					e.printStackTrace();
					storeA01UploadInfoDTOs(row,strOutItemType);
					// continue;
					throw new  RuntimeException(row.getCell(0).getStringCellValue()+"格式不对");

				}
				cfReportData.setTextValue("");
				cfReportData.setDesText(null);//描述性型
				cfReportData.setFileText(null);//文件型
			}else if(strOutItemType.equals("02")){
				try {
					cfReportData.setOutItemType("02");
					cfReportData.setReportItemValue(new BigDecimal(0).setScale(4,BigDecimal.ROUND_HALF_UP));
					cfReportData.setReportItemWanValue(new BigDecimal(0).setScale(8,BigDecimal.ROUND_HALF_UP));
				} catch (NumberFormatException e) {
					e.printStackTrace();
					storeA01UploadInfoDTOs(row,strOutItemType);
					// continue;
					throw new  RuntimeException(row.getCell(0).getStringCellValue()+"格式不对");

				}
				cfReportData.setTextValue("");
				cfReportData.setDesText(null);//描述性型
				cfReportData.setFileText(null);//文件型
			}else if(strOutItemType.equals("01")){//短文本类型
				//因子代码
				String outItemCode=row.getCell(0).getStringCellValue().trim();

				row.getCell(2).setCellType(1);//设置单元格格式为文本型
				cfReportData.setOutItemType("01");
				cfReportData.setReportItemValue(new BigDecimal(0).setScale(4,BigDecimal.ROUND_HALF_UP));
				cfReportData.setReportItemWanValue(new BigDecimal(0).setScale(8,BigDecimal.ROUND_HALF_UP));
				try {
					String textValue = row.getCell(2).getStringCellValue();
					//如果是公司类型,则针对填写内容转换成带竖线的
					if(outItemCode.equals("C01_00003")){
						if(doubleMap.containsKey(textValue)){
							textValue = doubleMap.get(textValue).toString();
						}
					}
					if("0".equals(textValue)){
						cfReportData.setTextValue("");
					}else if(textValue.trim().contains("不适用")){
						cfReportData.setIsused("0");  
						cfReportData.setTextValue("nil");
					}else{		    			   
						cfReportData.setTextValue(textValue);
					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
					storeA01UploadInfoDTOs(row,strOutItemType);
					//continue;
					throw new  RuntimeException(row.getCell(0).getStringCellValue()+"格式不对");

				}
				cfReportData.setDesText(null);//描述性型
				cfReportData.setFileText(null);//文件型
			}
			else if(strOutItemType.equals("07")){//日期型
				cfReportData.setOutItemType("07");
				cfReportData.setReportItemValue(new BigDecimal(0).setScale(8,BigDecimal.ROUND_HALF_UP));
				cfReportData.setReportItemWanValue(new BigDecimal(0).setScale(12,BigDecimal.ROUND_HALF_UP));
				try {
					if(ExcelUtil.getCellValue(row.getCell(2)).trim().contains("不适用")){
						cfReportData.setIsused("0");
						cfReportData.setTextValue("nil");
					}else{			    		   
						cfReportData.setTextValue(sdf.format(row.getCell(2).getDateCellValue()));
					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
					storeA01UploadInfoDTOs(row,strOutItemType);
					//continue;
					throw new  RuntimeException(row.getCell(0).getStringCellValue()+"格式不对");

				}
				cfReportData.setDesText(null);//描述性型
				cfReportData.setFileText(null);//文件型
			}else{
				System.out.println("导入因子值类型有误！！！");
			}
			cfReportData.setSource("");//源1：指标导入；源2：自动提数业务；源3：自动提数财务；源4：精算模板导入；源5：投资模板导入；源6：计算
			cfReportData.setReportType(pld.getReportname());//报表类别
			cfReportData.setComputeLevel("0");//计算级别
			cfReportData.setReportItemValueSource("0");//指标值来源：1-初步计算数据手工修改；2-尾数自动调整
			cfReportData.setOperator(userCode);//默认（操作人）
			cfReportData.setOperDate(pld.getDate());//操作日期
			cfReportData.setTemp("");

			//保存新数据
			impcfReportDatadao.save(cfReportData);
			System.out.println("编码是：+++++++++++++++++"+cfReportDataId.getOutItemCode());
			//添加修改轨迹
			if(strOutItemType.equals("04")||strOutItemType.equals("05")||strOutItemType.equals("06")){		    	   
				BigDecimal numvalue=(BigDecimal)hismap.get(row.getCell(0).getStringCellValue());//可能空指针异常
				if(numvalue==null){
					numvalue=new BigDecimal(0);
				}
				ReportHistory history = new ReportHistory();
				history.setReportid(cfReportDataId.getReportId());
				history.setMonth(cfReportData.getMonth());
				history.setOper_date(cfReportData.getOperDate());
				history.setQuarter(cfReportData.getQuarter());
				history.setReport_code(cfReportData.getId().getOutItemCode());
				history.setReport_mvalue(numvalue);
				history.setReport_mvalue_after(cfReportData.getReportItemValue());
				history.setReport_name(cfReportData.getOutItemCodeName());
				history.setReport_rate(cfReportData.getReportRate());
				history.setUser_name(cfReportData.getOperator());
				impcfReportDatadao.save(history);
			}else if(strOutItemType.equals("01")||strOutItemType.equals("07")){
				String strvalue=(String)hismap.get(row.getCell(0).getStringCellValue());//可能空指针异常
				if(strvalue==null){
					strvalue="";
				}
				ReportHistory history = new ReportHistory();
				history.setReportid(cfReportDataId.getReportId());
				history.setMonth(cfReportData.getMonth());
				history.setOper_date(cfReportData.getOperDate());
				history.setQuarter(cfReportData.getQuarter());
				history.setReport_code(cfReportData.getId().getOutItemCode());
				history.setText_value(strvalue);
				history.setText_value_after(cfReportData.getTextValue());
				history.setReport_name(cfReportData.getOutItemCodeName());
				history.setReport_rate(cfReportData.getReportRate());
				history.setUser_name(cfReportData.getOperator());
				impcfReportDatadao.save(history);
			}else if(strOutItemType.equals("03")||strOutItemType.equals("02")){
				System.out.println("描述性型和文件型暂无法保存修改轨迹。");
			}
			k=k+1;
			System.out.println(k);
		}
		if(uploadInfoDTOs.size()!=0){
			throw new UploadNumberFormatException(uploadInfoDTOs);
		}

	}

	@Transactional
	public void saveDataA02(List<Row> list,UploadInfoDTO pld, String userCode)throws UploadNumberFormatException,NullPointerException,Exception {
		// TODO Auto-generated method stub
		cfReportRowAddHis(pld);
		getCfReportRowAddDesc(pld.getDepartment());

		//获取需要映射的指标
		Map<String, String> rowMap = getMapCode();
		//文本对应关系 如AAA 1|AAA集合
		Map<String, String> doubleMap = getMapInfo();

		int k=0;
		for(int i=0;i<list.size();i++){
			Row row=list.get(i);

			if(row.getCell(0) != null && !row.getCell(0).getStringCellValue().equals("") && row.getCell(2) != null && !row.getCell(2).getStringCellValue().equals("")){
				if(!codemap.containsKey(row.getCell(0).getStringCellValue()+"_"+row.getCell(2).getStringCellValue())){
					continue;
				}
			}else{
				continue;
			}

			System.out.println("row.getLastCellNum()="+row.getLastCellNum());
			for(int j=22;j<row.getLastCellNum();j++){
				System.out.println(ExcelUtil.getCellValue(row.getCell(j)));
				if("".equals(ExcelUtil.getCellValue(row.getCell(j)))||"0".equals(ExcelUtil.getCellValue(row.getCell(j)))||"0.0".equals(ExcelUtil.getCellValue(row.getCell(j)))||"-".equals(ExcelUtil.getCellValue(row.getCell(j)))||"--".equals(ExcelUtil.getCellValue(row.getCell(j)))||"#REF!".equals(ExcelUtil.getCellValue(row.getCell(j)))||"<g_idiv>".equals(ExcelUtil.getCellValue(row.getCell(j)))||"#VALUE!".equals(ExcelUtil.getCellValue(row.getCell(j)))){
					continue;
				}
				System.out.println("第"+(i+2)+"行，第"+(j+1)+"列。");
				CfReportRowAddDataId cfReportRowAddDataId=new CfReportRowAddDataId();
				CfReportRowAddData cfReportRowAddData=new CfReportRowAddData();
				cfReportRowAddDataId.setReportId(pld.getReportId());
				cfReportRowAddData.setTableCode(row.getCell(0).getStringCellValue());//表代码
				cfReportRowAddDataId.setColCode(row.getCell(2).getStringCellValue());//列代码
				cfReportRowAddDataId.setRowNo(String.valueOf(j-21));//行数
				cfReportRowAddData.setYearMonth(pld.getYearmonth());//会计与月度
				cfReportRowAddData.setQuarter(pld.getQuarter());//季度
				cfReportRowAddData.setReportRate(pld.getReporttype());//报送频度
				cfReportRowAddData.setId(cfReportRowAddDataId);
				cfReportRowAddData.setReportType(pld.getReportname());//报表类别
				cfReportRowAddData.setSource("1");//源1：指标导入；源2：自动提数业务；源3：自动提数财务；源4：精算模板导入；源5：投资模板导入；源6：计算
				cfReportRowAddData.setOperator2(userCode);//操作人
				cfReportRowAddData.setOperDate2(pld.getDate());//操作日期
				cfReportRowAddData.setComCode(Config.getProperty("OrganCode"));
				System.out.println(row.getCell(0).getStringCellValue()+"_"+row.getCell(2).getStringCellValue());
				String strColtype = codemap.get(row.getCell(0).getStringCellValue()+"_"+row.getCell(2).getStringCellValue());
				if(strColtype==null){
					System.out.println("行可扩展因子代码未定义");
				}
				System.out.println("coltype="+strColtype);
				String position = "第"+(i+2)+"行，第"+(j+1)+"列,";
				if(strColtype.equals("06")){//百分比型
					try{
						String strValue = ExcelUtil.getCellValue(row.getCell(j));
						if("".equals(strValue.trim())||"--".equals(strValue.trim())||"-".equals(strValue.trim())||"#REF!".equals(strValue.trim())||"<g_idiv>".equals(strValue.trim())||"#VALUE!".equals(strValue.trim())){
							strValue="0";
						}
						cfReportRowAddData.setColType("06");
						new BigDecimal(strValue);
						cfReportRowAddData.setNumValue(new BigDecimal(strValue).setScale(8,BigDecimal.ROUND_HALF_UP));
						cfReportRowAddData.setWanValue(new BigDecimal("0").setScale(12,BigDecimal.ROUND_HALF_UP));
					}catch(NumberFormatException e){
						e.printStackTrace();
						storeA02UploadInfoDTOs(row, strColtype, position);
						//continue;
						throw new  RuntimeException("表代码:"+row.getCell(0).getStringCellValue()+"列代码:"+row.getCell(2).getStringCellValue()+"格式不对");

					}
					cfReportRowAddData.setTextValue("");
					cfReportRowAddData.setDesText("");
				}else if(strColtype.equals("05")){//数量型
					try{
						String strValue = ExcelUtil.getCellValue(row.getCell(j));
						if("".equals(strValue.trim())||"--".equals(strValue.trim())||"-".equals(strValue.trim())||"#REF!".equals(strValue.trim())||"<g_idiv>".equals(strValue.trim())||"#VALUE!".equals(strValue.trim())){
							strValue="0";
						}
						cfReportRowAddData.setColType("05");
						new BigDecimal(strValue);
						cfReportRowAddData.setNumValue(new BigDecimal(strValue).setScale(8,BigDecimal.ROUND_HALF_UP));
						cfReportRowAddData.setWanValue(new BigDecimal(strValue).divide(new BigDecimal("10000"), 12, RoundingMode.HALF_UP));
					}catch(NumberFormatException e){
						System.out.println("数据转换出错！");
						e.printStackTrace();
						storeA02UploadInfoDTOs(row, strColtype, position);
						//continue;
						throw new  RuntimeException("表代码:"+row.getCell(0).getStringCellValue()+"列代码:"+row.getCell(2).getStringCellValue()+"格式不对");

					}
					cfReportRowAddData.setTextValue("");
					cfReportRowAddData.setDesText("");
				}else if(strColtype.equals("04")){//数值型
					try{
						String strValue = ExcelUtil.getCellValue(row.getCell(j));
						if("".equals(strValue.trim())||"--".equals(strValue.trim())||"-".equals(strValue.trim())||"#REF!".equals(strValue.trim())||"<g_idiv>".equals(strValue.trim())||"#VALUE!".equals(strValue.trim())){
							strValue="0";
						}
						cfReportRowAddData.setColType("04");
						new BigDecimal(strValue);
						cfReportRowAddData.setNumValue(new BigDecimal(strValue).setScale(8,BigDecimal.ROUND_HALF_UP));
						cfReportRowAddData.setWanValue(new BigDecimal(strValue).divide(new BigDecimal("10000"), 12, RoundingMode.HALF_UP));
					}catch(NumberFormatException e){
						e.printStackTrace();
						storeA02UploadInfoDTOs(row, strColtype, position);
						//continue;
						throw new  RuntimeException("表代码:"+row.getCell(0).getStringCellValue()+"列代码:"+row.getCell(2).getStringCellValue()+"格式不对");

					}
					cfReportRowAddData.setTextValue("");
					cfReportRowAddData.setDesText("");		        	
				}else if(strColtype.equals("02")){//描述型
					try {
						String strValue = ExcelUtil.getCellValue(row.getCell(j));
						cfReportRowAddData.setColType("02");
						cfReportRowAddData.setNumValue(new BigDecimal("0").setScale(4,BigDecimal.ROUND_HALF_UP));
						cfReportRowAddData.setWanValue(new BigDecimal("0").setScale(8,BigDecimal.ROUND_HALF_UP));
						cfReportRowAddData.setTextValue(strValue);
					} catch (NumberFormatException e) {
						e.printStackTrace();
						storeA02UploadInfoDTOs(row, strColtype, position);
						//continue;
						throw new  RuntimeException("表代码:"+row.getCell(0).getStringCellValue()+"列代码:"+row.getCell(2).getStringCellValue()+"格式不对");

					}
					cfReportRowAddData.setDesText("");		        	
				}else if(strColtype.equals("01")){//短文本类型
					try {
						row.getCell(j).setCellType(1);//设置单元格格式为文本型
						String strValue = row.getCell(j).getStringCellValue();

						//指标代码
						String colCode=row.getCell(2).getStringCellValue();
						//判断该指标内容是否需要做带竖线的转换
						if(rowMap.containsKey(colCode)){
							System.out.println("需要转换成带竖线的指标是：----------"+colCode);
							if(doubleMap.containsKey(strValue)){
								strValue = doubleMap.get(strValue);
								System.out.println("转换后的文本内容是：----"+strValue);
							} 							
						}

						cfReportRowAddData.setColType("01");
						cfReportRowAddData.setNumValue(new BigDecimal("0").setScale(4,BigDecimal.ROUND_HALF_UP));
						cfReportRowAddData.setWanValue(new BigDecimal("0").setScale(8,BigDecimal.ROUND_HALF_UP));
						cfReportRowAddData.setTextValue(strValue);
					} catch (NumberFormatException e) {
						e.printStackTrace();
						storeA02UploadInfoDTOs(row, strColtype, position);
						//continue;
						throw new  RuntimeException("表代码:"+row.getCell(0).getStringCellValue()+"列代码:"+row.getCell(2).getStringCellValue()+"格式不对");

					}
					cfReportRowAddData.setDesText("");		        	
				}else if(strColtype.equals("07")){//日期型
					try {
						String strValue = sdf.format(row.getCell(j).getDateCellValue());
						cfReportRowAddData.setColType("07");
						cfReportRowAddData.setNumValue(new BigDecimal("0").setScale(8,BigDecimal.ROUND_HALF_UP));
						cfReportRowAddData.setWanValue(new BigDecimal("0").setScale(12,BigDecimal.ROUND_HALF_UP));
						cfReportRowAddData.setTextValue(strValue);
						cfReportRowAddData.setDesText("");		        	
					} catch (NumberFormatException e) {
						e.printStackTrace();
						storeA02UploadInfoDTOs(row, strColtype, position);
						//continue;
						throw new  RuntimeException("表代码:"+row.getCell(0).getStringCellValue()+"列代码:"+row.getCell(2).getStringCellValue()+"格式不对");

					}
				}else{
					System.out.println("导入因子值类型有误！！！");
				}

				//保存新数据
				impcfRowAddDao.save(cfReportRowAddData);
				//添加修改轨迹
				if(strColtype.equals("04")||strColtype.equals("05")||strColtype.equals("06")){		    	   
					BigDecimal numvalue=(BigDecimal)hismap.get(row.getCell(0).getStringCellValue());//可能空指针异常
					if(numvalue==null){
						numvalue=new BigDecimal(0);
					}
					RowAddHistory history = new RowAddHistory();
					history.setReportid(cfReportRowAddDataId.getReportId());
					history.setOper_date(cfReportRowAddData.getOperDate2());
					history.setTable_code(cfReportRowAddData.getTableCode());
					history.setTable_name(row.getCell(1).getStringCellValue());
					history.setCol_code(cfReportRowAddData.getId().getColCode());
					history.setCol_name(row.getCell(3).getStringCellValue());
					history.setRow_no(cfReportRowAddData.getId().getRowNo());
					history.setReport_rate(cfReportRowAddData.getReportRate());
					history.setYear_month(cfReportRowAddData.getYearMonth());
					history.setQuarter(cfReportRowAddData.getQuarter());
					history.setReport_mvalue(numvalue);
					history.setReport_mvalue_after(cfReportRowAddData.getNumValue());
					history.setUser_name(cfReportRowAddData.getOperator2());
					impcfRowAddDao.save(history);
				}else if(strColtype.equals("01")||strColtype.equals("07")){
					String strvalue=(String)hismap.get(row.getCell(0).getStringCellValue());//可能空指针异常
					if(strvalue==null){
						strvalue="";
					}
					RowAddHistory history = new RowAddHistory();
					history.setReportid(cfReportRowAddData.getId().getReportId());
					history.setOper_date(cfReportRowAddData.getOperDate2());
					history.setTable_code(cfReportRowAddData.getTableCode());
					history.setTable_name(row.getCell(1).getStringCellValue());
					history.setCol_code(cfReportRowAddData.getId().getColCode());
					history.setCol_name(row.getCell(3).getStringCellValue());
					history.setRow_no(cfReportRowAddData.getId().getRowNo());
					history.setReport_rate(cfReportRowAddData.getReportRate());
					history.setYear_month(cfReportRowAddData.getYearMonth());
					history.setQuarter(cfReportRowAddData.getQuarter());
					history.setText_value(strvalue);
					history.setText_value_after(cfReportRowAddData.getTextValue());
					history.setUser_name(cfReportRowAddData.getOperator2());
					impcfRowAddDao.save(history);
				}else if(strColtype.equals("03")||strColtype.equals("02")){
					System.out.println("描述性型和文件型暂无法保存修改轨迹。");
				}
			}
			k=k+1;
			System.out.println(k);
		}
		if(uploadInfoDTOs.size()!=0){
			throw new UploadNumberFormatException(uploadInfoDTOs);
		}
	}

	/**
	 * A01 存储数据格式转换异常结果集
	 * @param row
	 * @param strOutItemType
	 */
	private void storeA01UploadInfoDTOs(Row row, String strOutItemType){
		UploadInfoDTO uploadInfoDTO = new UploadInfoDTO();
		uploadInfoDTO.setReportcode(row.getCell(0).getStringCellValue());//因子代码
		uploadInfoDTO.setReportname(row.getCell(1).getStringCellValue());//因子名称
		uploadInfoDTO.setFileName(row.getCell(4).getStringCellValue());//表名称
		if(strOutItemType.equals("01")){
			uploadInfoDTO.setRemark("数据类型不为多文本型");
		} else if(strOutItemType.equals("02")){
			uploadInfoDTO.setRemark("数据类型不为描述型");
		} else if(strOutItemType.equals("03")){
			uploadInfoDTO.setRemark("数据类型不为文件型");
		} else if(strOutItemType.equals("04")){
			uploadInfoDTO.setRemark("数据类型不为数值型");
		} else if(strOutItemType.equals("05")){
			uploadInfoDTO.setRemark("数据类型不为数量型");
		} else if(strOutItemType.equals("06")){
			uploadInfoDTO.setRemark("数据类型不为百分比型");
		} else if(strOutItemType.equals("07")){
			uploadInfoDTO.setRemark("数据类型不为日期型");
		} else {
			uploadInfoDTO.setRemark("未知类型：导入因子值类型有误！");//错误信息
		}

		this.uploadInfoDTOs.add(uploadInfoDTO);
	}

	/**
	 * A02存储数据格式转换异常结果集
	 * @param row
	 * @param strColtype
	 * @param position
	 */
	private void storeA02UploadInfoDTOs(Row row, String strColtype, String position){
		UploadInfoDTO uploadInfoDTO = new UploadInfoDTO();
		uploadInfoDTO.setReportcode(row.getCell(2).getStringCellValue());//因子代码
		uploadInfoDTO.setReportname(row.getCell(3).getStringCellValue());//因子名称
		uploadInfoDTO.setFileName(row.getCell(5).getStringCellValue());//表名称
		if(strColtype.equals("01")){
			uploadInfoDTO.setRemark(position+"数据类型不为多文本型");
		} else if(strColtype.equals("02")){
			uploadInfoDTO.setRemark(position+"数据类型不为描述型");
		} else if(strColtype.equals("03")){
			uploadInfoDTO.setRemark(position+"数据类型不为文件型");
		} else if(strColtype.equals("04")){
			uploadInfoDTO.setRemark(position+"数据类型不为数值型");
		} else if(strColtype.equals("05")){
			uploadInfoDTO.setRemark(position+"数据类型不为数量型");
		} else if(strColtype.equals("06")){
			uploadInfoDTO.setRemark(position+"数据类型不为百分比型");
		} else if(strColtype.equals("07")){
			uploadInfoDTO.setRemark(position+"数据类型不为日期型");
		} else {
			uploadInfoDTO.setRemark("未知类型：行可扩展因子代码未定义！");//错误信息
		}

		this.uploadInfoDTOs.add(uploadInfoDTO);
	}

	@Transactional
	public void getCfReportItemCodeDesc(String deptNo) {
		// TODO Auto-generated method stub
		//根据部门和录入类型获取指标
		//表内指标
		String sql="select * from cfreportitemcodedesc where importsource='0' and  reportitemtype='" + deptNo + "' order by outitemcode";
		List<?> result=impcfReportItemCodeDescDao.queryBysql(sql);
		codemap.clear();
		for(int i=0;i<result.size();i++){
			Map<?,?> m=(Map<?,?>)result.get(i);
			System.out.println(m.get("reportItemCode"));
			codemap.put((String) m.get("reportItemCode"), m.get("outItemType")+"_"+m.get("reportItemCode"));
		}
		//表外指标
		String sql_out="select * from rsys_CfReportItemCodeDesc where importsource='0' and  reportitemtype='" + deptNo + "' order by outitemcode";
		List<?> result_out=impcfReportItemCodeDescDao.queryBysql(sql_out);
		for(int i=0;i<result_out.size();i++){
			Map<?,?> m=(Map<?,?>)result_out.get(i);
			System.out.println(m.get("reportItemCode"));
			codemap.put((String) m.get("reportItemCode"), m.get("outItemType")+"_"+m.get("reportItemCode"));
		}
	}

	/**
	 * 删除历史数据且只删除对应部门的手工录入数据
	 * @param pld
	 */
	@Transactional
	private void cfReportItemCodeHis(UploadInfoDTO pld) {
		// TODO Auto-generated method stub
		String sqlhis="select * from cfreportdata where month='"+
				pld.getYearmonth()+"' and quarter='"+
				pld.getQuarter()+"' and reportid='"+
				pld.getReportId()+"' and reportrate='"+
				pld.getReporttype()+"' and reporttype='"+
				pld.getReportname()+"' and (outitemcode in (select outitemcode from cfreportitemcodedesc where importsource='0' and  reportitemtype='" + pld.getDepartment() + "') "
				+ "or outitemcode in (select outitemcode from rsys_CfReportItemCodeDesc where importsource='0' and  reportitemtype='" + pld.getDepartment() + "')) ";
		System.out.println(sqlhis);
		List<?> hisresult=impcfReportDatadao.queryBysql(sqlhis);
		hismap.clear();			
		if(hisresult.size()>0){
			for(int i=0;i<hisresult.size();i++){
				Map<?,?> m=(Map<?,?>)hisresult.get(i);
				System.out.println(m.get("outItemType"));
				if(m.get("outItemType").equals("06")||m.get("outItemType").equals("05")||m.get("outItemType").equals("04")){					
					hismap.put((String) m.get("reportItemCode"), (BigDecimal)m.get("reportItemValue"));
				}else if(m.get("outItemType").equals("01")||m.get("outItemType").equals("07")){
					hismap.put((String) m.get("reportItemCode"), (String)m.get("textValue"));
				}else if(m.get("outItemType").equals("02")||m.get("outItemType").equals("03")){
					continue;
				}
			}
		}
		//删除原数据
		String lsql="delete from cfreportdata where month='"+pld.getYearmonth()
				+"' and reportid='"+pld.getReportId()
				+"' and quarter='"+pld.getQuarter()+"' and reportrate='"+
				pld.getReporttype()+"' and reporttype='"+
				pld.getReportname()+"' and outitemtype<>'02' and (outitemcode in (select outitemcode from cfreportitemcodedesc where importsource='0' and  reportitemtype='" + pld.getDepartment() + "') "
				+ "or outitemcode in (select outitemcode from rsys_CfReportItemCodeDesc where importsource='0' and  reportitemtype='" + pld.getDepartment() + "')) ";
		System.out.println("---------------------删除原数据--------------------------------");
		System.out.println(lsql);
		try{
			dao.excute(lsql);
		}catch(Exception e){
			LOGGER.error("删除历史数据出错！sql="+lsql);
			throw new RuntimeException("删除历史数据出错！");
		}
	}
	@Transactional
	public void getCfReportRowAddDesc(String deptNo) {
		// TODO Auto-generated method stub
		//表内指标
		String sql="select * from cfreportrowadddesc where source='0' and deptno='" + deptNo + "'";
		List<?> result=impcfReportRowAddDescDao.queryBysql(sql);
		codemap.clear();
		for(int i=0;i<result.size();i++){
			Map<?,?> m=(Map<?,?>)result.get(i);
			System.out.println(m.get("tableCode")+"_"+m.get("colCode")+"_"+m.get("colType"));
			codemap.put((String) m.get("tableCode")+"_"+m.get("colCode"),(String) m.get("colType"));
		}
		//表外指标
		String sql_out="select * from rsys_CfReportRowAddDesc where source='0' and deptno='" + deptNo + "'";
		List<?> result_out=impcfReportRowAddDescDao.queryBysql(sql_out);
		for(int i=0;i<result_out.size();i++){
			Map<?,?> m=(Map<?,?>)result_out.get(i);
			System.out.println(m.get("tableCode")+"_"+m.get("colCode")+"_"+m.get("colType"));
			codemap.put((String) m.get("tableCode")+"_"+m.get("colCode"),(String) m.get("colType"));
		}
	}

	//删除历史数据且只删除对应部门的手工录入数据
	@Transactional
	private void cfReportRowAddHis(UploadInfoDTO pld) {
		// TODO Auto-generated method stub
		String sqlhis="select * from cfreportrowadddata where yearmonth='"+
				pld.getYearmonth()+"' and quarter='"+
				pld.getQuarter()+"' and reportrate='"+
				pld.getReporttype()+"' and reporttype='"+ pld.getReportname()
				+ "' and (colcode in (select colcode from cfreportrowadddesc where source='0' and deptno='" + pld.getDepartment() + "') "
				+ "  or colcode in (select colcode from rsys_CfReportRowAddDesc where source='0' and deptno='" + pld.getDepartment() + "'))";
		System.out.println(sqlhis);
		List<?> hisresult=impcfRowAddDao.queryBysql(sqlhis);
		hismap.clear();			
		if(hisresult.size()>0){
			for(int i=0;i<hisresult.size();i++){
				Map<?,?> m=(Map<?,?>)hisresult.get(i);
				System.out.println(m.get("colType"));
				if(m.get("colType").equals("06")||m.get("colType").equals("05")||m.get("colType").equals("04")){					
					hismap.put((String) m.get("colCode"), (BigDecimal)m.get("numValue"));
				}else if(m.get("colType").equals("01")||m.get("colType").equals("07")){
					hismap.put((String) m.get("colCode"), (String)m.get("textValue"));
				}
			}
		}
		//删除原数据
		String lsql="delete from cfreportrowadddata where yearmonth='"+pld.getYearmonth()+"' and quarter='"+pld.getQuarter()+"' and reportrate='"+
				pld.getReporttype()+"' and reporttype='"+
				pld.getReportname()+"' and (colcode in (select colcode from cfreportrowadddesc where source='0' and deptno='" + pld.getDepartment() + "') "
				+ "  or colcode in (select colcode from rsys_CfReportRowAddDesc where source='0' and deptno='" + pld.getDepartment() + "'))";
		try{
			dao.excute(lsql);
		}catch(Exception e){
			LOGGER.error("删除历史数据出错！sql="+lsql);
			throw new RuntimeException("删除历史数据出错！");
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
		for (Object obj : list) {
			_map = (Map<String, Object>) obj;
			mapitemacc.put(_map.get("code").toString(), _map.get("decimals"));
			mapitemtype.put(_map.get("code").toString(), _map.get("itemtype"));
		}
		maplist.add(mapitemacc);
		maplist.add(mapitemtype);

		String osql = "select code, itemtype, decimals from (select t1.outitemcode code, t1.outitemtype itemtype, t1.decimals decimals from rsys_cfreportitemcodedesc t1 union select t2.colcode code, t2.coltype itemtype, t2.decimals decimals from rsys_cfreportrowadddesc t2)";
		list = impDao.queryBysql(osql);
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
	 * 如 AAA = 1|AAA	 *  
	 * 用于上传数据时转换
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
					map.put(oriName, sysName);
				}					
			}
		}			
		return map;
	}

	/**********************
	 * 获取带竖线内容的映射对应关系
	 * 如1|AAA = AAA * 
	 * 用于下载数据时的转换
	 *********************/	
	@SuppressWarnings("unchecked")
	@Transactional
	public Map<String, String> getMapInfoDown(){
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
	/**********************
	 * 
	 * 将因子代码的值为0的，改为值为空
	 * 
	 *********************/	
	@SuppressWarnings("unchecked")
	@Transactional
	public boolean turnNull(String id,String value){
		if((id.equals("IR01_00056")&&(value.equals("0")))
				||(id.equals("IR01_00057")&&(value.equals("0")))
				||(id.equals("IR01_00058")&&(value.equals("0")))
				||(id.equals("IR01_00059")&&(value.equals("0")))
				||(id.equals("IR01_00060")&&(value.equals("0")))
				||(id.equals("IR01_00061")&&(value.equals("0")))
				||(id.equals("IR01_00067")&&(value.equals("0")))){
			System.out.println(value);
			System.out.println("++++++++++++++++++++++++++++++++++++++");
			/*
			   cfReportData.setReportItemValue(null);
			   cfReportData.setReportItemWanValue(null);
			 */
			return true;
		}
		return false;

	}
}
