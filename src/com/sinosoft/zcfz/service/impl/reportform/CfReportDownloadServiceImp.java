package com.sinosoft.zcfz.service.impl.reportform;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import com.sinosoft.zcfz.dao.reportform.ExportXMLQueryDAO;
import com.sinosoft.zcfz.dto.reportform.CfReportDownloadInfoDTO;
import com.sinosoft.entity.UploadFile;
import com.sinosoft.zcfz.service.reportform.CfReportDownloadService;
import com.sinosoft.util.Config;
import com.sinosoft.util.UploaderServlet;

@Service
public class CfReportDownloadServiceImp implements CfReportDownloadService{
	private final String comcode=Config.getProperty("OrganCode");
	//FileFlag 1:财务部 2：投资部 3：精算部 4：国际部 5：偿付能力
	private String fileFlag;
	private String yearCode;
    private String cfReportRate;
    private String cfReportType;
    private String quarter;
    private String mFilePath;
    private String mFileName;
    private String filePath;
    private String fileName;
    private String fileDirName;
    private String outputFile;
    private String mYearMonth;
    private String mTableDate;
    private String downAllMode = "";
    private HashMap mHashMap_Item = new HashMap();
    private HashMap mHashMap_rowadd = new HashMap();
    private List<?> listCfReportdata;
    private List<?> listReportdesc;
    private List<?> listRowAddData;
	private List<?> listRowNo;
	@Resource
	private ExportXMLQueryDAO querydao;
	@Resource
	private UploaderServlet us;
    
	@SuppressWarnings("resource")
	public void reportDownload(CfReportDownloadInfoDTO info) throws IOException,NullPointerException {
		// TODO Auto-generated method stub
		getInputData(info);

		FileInputStream is = new FileInputStream(mFilePath);
		XSSFWorkbook modelBook = new XSSFWorkbook(is);
		//将初步计算的值通过反添模板加到excel中
		modelBook = getPrepareValue(modelBook);
		filePath = Config.getProperty("ReportWork")+"createExcelFile/";
		fileName = fileDirName + "_"+ new Random().nextInt(9999) + ".xlsm";
		outputFile = filePath + fileName;
		FileOutputStream newFile = new FileOutputStream(outputFile);
		modelBook.write(newFile);
		newFile.flush();
		newFile.close();	
			
		
/*		if("1".equals(cfReportRate)){//季度快报  若要下载完整数据模板，则传递all参数，否则flag=""
			BookModelImpl book = new BookModelImpl();
			book.initWorkbook();
			book.read(outputfile, new ReadParams());
 			book = getDownYearDisplay(book, mFilePath);//将模版sheet删除到只有此部门需要的sheet
			book.write(outputfile, new WriteParams(BookModelImpl.eFileExcel97)); // 拷贝新的文件
		}else if("2".equals(cfReportRate)){//季度报告 若要下载完整数据模板，则传递all参数，否则flag=""
			BookModelImpl book = new BookModelImpl();
			book.initWorkbook();
			book.read(outputfile, new ReadParams());
 			book = getDownDisplay(book, mFilePath);//将模版sheet删除到只有此部门需要的sheet
			book.write(outputfile, new WriteParams(BookModelImpl.eFileExcel97)); // 拷贝新的文件
		}else if("3".equals(cfReportRate) ){//临时报告 若要下载完整数据模板，则传递all参数，否则flag=""
			BookModelImpl book = new BookModelImpl();
			book.initWorkbook();
			book.read(outputfile, new ReadParams());
 			book = getDownDisplay(book, mFilePath);//将模版sheet删除到只有此部门需要的sheet
			book.write(outputfile, new WriteParams(BookModelImpl.eFileExcel97)); // 拷贝新的文件
		}*/
//		else{
//			BookModelImpl book = new BookModelImpl();
//			book.initWorkbook()y;
//			book.read(outputfile, new ReadParams());
//			book.write(outputfile, new WriteParams(BookModelImpl.eFileExcel97)); // 拷贝新的文件
//		}
/*		UploadFile u=new UploadFile();
		u.setFileName(fileName);
		u.setFilePath(filePath);	
		try {
			us.download(request, response, u);			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	
	}
    private XSSFWorkbook getPrepareValue(XSSFWorkbook modelBook) {
		// TODO Auto-generated method stub
    	if(getItemMap()){
//   	getReportDesc();
    		modelBook = getValue(modelBook);//赋值操作
    		return modelBook;   		
    	}else{
    		return null;
    	}
	}
	private XSSFWorkbook getValue(XSSFWorkbook modelBook) {
		try {			
			/*---处理固定因子值---begin---*/
			modelBook = dealItemCodeData(modelBook);
			/*---处理行可扩展数据---begin---*/
			modelBook = dealRowAddData(modelBook);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return modelBook;
	}
	private XSSFWorkbook dealRowAddData(XSSFWorkbook modelBook) {
		try {
			// 获取行可扩展表所有数据
			if (!getRowAddMap()) {
				return modelBook;
			}

			XSSFSheet sheet;
			XSSFRow row;
			XSSFRow temprow;
			XSSFCell cell;
			XSSFCell tempcell;
			XSSFComment comment;
			String sheetname= "";
			String strDes = "";
			String strTableId = "";
			String strRowNo = "";
			String colcode = "";
			boolean wanFlag = false;//是否万元
			int sheetNum = modelBook.getNumberOfSheets();
			System.out.println(sheetNum);//表个数
			for(int i=0;i<sheetNum;i++){
				sheet = modelBook.getSheetAt(i);
				sheetname = sheet.getSheetName();
				System.out.println("sheetname="+sheetname);
				//以下7个sheet不再需要了
				if("A01#".equals(sheetname)||"A02#".equals(sheetname)||"Database".equals(sheetname)||"目录".equals(sheetname)||"说明".equals(sheetname))
					continue;
				System.out.println("FirstRowNum="+sheet.getFirstRowNum());
				System.out.println("LastRowNum="+sheet.getLastRowNum());
				for (int nRowNumBook = sheet.getFirstRowNum(); nRowNumBook <= sheet.getLastRowNum();) {
					// 取每行第一列，判断是否有行可扩展表代码
					System.out.println("row:"+nRowNumBook);
					row = sheet.getRow(nRowNumBook);
					if(row==null){
						nRowNumBook++;
						continue;
					}	
					cell = row.getCell(0);
					if(cell==null){
						nRowNumBook++;
						continue;
					}
					//System.out.println(cell);
					comment = cell.getCellComment();
					if(comment==null){
						nRowNumBook++;
						continue;
					}
					strDes = comment.getString().getString().trim();
                    //	if("<tableid=IA-11-2%2.x>".equals(strDes)){
		            System.out.println("strDes:"+strDes);
					if (strDes.indexOf("<tableid=") != -1) {
						strTableId = strDes.substring(9, strDes.length() - 1);						
						if (strTableId.indexOf("%") >= 0) {
							String strTemp[] = strTableId.split("%");
							strTableId = strTemp[0];
							strRowNo = strTemp[1];
						}
						
						// 通过表代码(tableid)获取行号
//						if("IA-12-1".equals(strTableId)){
//							System.out.println("comment:"+comment);
//						}
						if (!getRowDataFromDB(strTableId)) {
							nRowNumBook++;
							continue;
						}
						int nRowData = listRowNo.size();
						if (nRowData > 0) {
							for (int nColNumBook = 1; nColNumBook <= row.getLastCellNum(); nColNumBook++) {
								cell = row.getCell(nColNumBook);
								if(cell==null)
									continue;
								comment = cell.getCellComment();
								if(comment==null)
									continue;
								colcode = comment.getString().getString().trim();
								System.out.println("colcode ="+colcode);
								if (colcode != null && !"".equals(colcode)&& colcode.length() >= 6) {
									wanFlag = false;
									if (colcode.indexOf("/10000") != -1) {
										wanFlag = true;
									}
									colcode = colcode.substring(0, 6);

									String strValue = "";
									String type = "";
									
									for (int nRowNumData = 0; nRowNumData < nRowData; nRowNumData++) {
//										if("IA-11-2".equals(strTableId)){
//											if(colcode.equals("F00112") &&nRowNumData==43){
//												System.out.println("comment:"+comment);
//											}
//										}
//										if("IA-12".equals(strTableId)){
//											System.out.println("comment:"+comment);
//										}
										Map<?,?> m = (Map<?, ?>) listRowNo.get(nRowNumData);
										String rowno = (String) m.get("rowNo");
										temprow = sheet.getRow(nRowNumBook+nRowNumData);
										if(temprow==null){
											temprow = sheet.createRow(nRowNumBook+nRowNumData);
											tempcell = temprow.createCell(nColNumBook);
											temprow.createCell(0).setCellValue(strRowNo.replaceAll("x", rowno));// 此过程会重复多次
										}else{
											tempcell = temprow.getCell(nColNumBook);
											if(tempcell==null){
												tempcell = temprow.createCell(0);
												tempcell = temprow.createCell(nColNumBook);
											}												
											if(temprow.getCell(0)==null)
												continue;
											temprow.getCell(0).setCellValue(strRowNo.replaceAll("x", rowno));// 此过程会重复多次
										}										
										strValue="";
										if (mHashMap_rowadd.get(strTableId + "_"+ rowno + "_" + strTableId+"_"+colcode) != null) {
											Vector vctValue = (Vector) mHashMap_rowadd.get(strTableId + "_" + rowno+ "_" + strTableId+ "_" + colcode);
											type = (String) vctValue.get(0);
											strValue = (String) vctValue.get(1);
										}
										if (strValue == null || strValue =="") {
											strValue = "0";  
										}
										if ("04".equals(type) || "05".equals(type)|| "06".equals(type)) {
											if (("".equals(strValue.trim())||strValue == null)&&"05".equals(type)) 
												strValue = "0";
											else if (("".equals(strValue.trim())||strValue == null)&&"04".equals(type)) 
												strValue = "0.00";
											else if (("".equals(strValue.trim())||strValue == null)&&"06".equals(type)) 
												strValue = "0.00";

											if (wanFlag) 
												tempcell.setCellValue(Double.parseDouble(strValue) / 10000.0);
											else
												tempcell.setCellValue(Double.parseDouble(strValue));
											
										} else {
											tempcell.setCellValue(strValue);
										}
									}
								}
							}
							nRowNumBook = nRowNumBook + nRowData;
					}else
						nRowNumBook++;
					}else
						nRowNumBook++;				
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return modelBook;
	}
	private XSSFWorkbook dealItemCodeData(XSSFWorkbook modelBook) {
		try {
			XSSFSheet sheet;
			XSSFRow row;
			XSSFCell cell;
			XSSFComment comment;
			String sheetname= "";
			String strItemCode = "";//因子代码
			String strFlag = "";//反填模板的标签
			boolean wanFlag = false;//是否万元
			int sheetNum = modelBook.getNumberOfSheets();//表个数
			System.out.println("sheetNum="+sheetNum);
			for(int i=0;i<sheetNum;i++){
				sheet = modelBook.getSheetAt(i);
				sheetname = sheet.getSheetName();
				System.out.println("sheet="+sheet);
				System.out.println("sheetname="+sheetname);//表名称
				if("A01#".equals(sheetname)||"A02#".equals(sheetname)||"Database".equals(sheetname)||"目录".equals(sheetname)||"说明".equals(sheetname))
					continue;
				System.out.println("FirstRowNum="+sheet.getFirstRowNum());
				System.out.println("LastRowNum="+sheet.getLastRowNum());
				for (int nRow = sheet.getFirstRowNum(); nRow <= sheet.getLastRowNum(); nRow++) {
					System.out.println("row:"+nRow);
					row = sheet.getRow(nRow);
					if(row==null)//跳过空行
						continue;
					System.out.println("第一个单元格索引："+row.getFirstCellNum());
					System.out.println("最后一个单元格索引："+row.getLastCellNum());
					if(row.getFirstCellNum()==-1)//跳过空行
						continue;
					for (int cellnum = row.getFirstCellNum(); cellnum < row.getLastCellNum(); cellnum++) {
						System.out.println("cell:"+cellnum);
						cell = row.getCell(cellnum);
						if(cell==null)//跳过空单元格
							continue;
						comment = cell.getCellComment();
						System.out.println("comment="+comment);
						if(comment==null)//跳过无批注单元格
							continue;
						strFlag = comment.getString().toString();
							System.out.println("strFlag="+strFlag);					
						if (strFlag.indexOf("/wan") != -1) {
							wanFlag = true;
						}
//if(strFlag.equals("<g_idiv(8000463)>")){
//	System.out.println(strFlag);
//}						
						strItemCode="";
						int substart = strFlag.indexOf("(")+1;
						int subend = strFlag.indexOf(")");
						if (strFlag.indexOf("<g_idiv(") != -1) {
							strItemCode = strFlag.trim().substring(substart, subend);								
						} else if (strFlag.indexOf("<g_item(") != -1) {
							strItemCode = strFlag.trim().substring(substart, subend);
   					   }
//if(strItemCode.equals("6000138")){
//	System.out.println(strFlag);
//}						
							System.out.println("strItemCode="+strItemCode);				
						if (strItemCode != null && !"".equals(strItemCode)) {
							Vector vctValue = (Vector) mHashMap_Item.get(strItemCode);
							String srtItemType = "";
							String strValue = "";
							if (null == vctValue) {
								srtItemType = "";
								strValue = "0";   //为了防止下载的模板中出现#value！情况，vctValue=null的strValue="0.00"
							} else {
								strValue = (String) vctValue.get(1);
								srtItemType = (String) vctValue.get(0);
							}
							System.out.println("strValue="+strValue);
							System.out.println("srtItemType="+srtItemType);
							if (strValue == null || strValue =="") {
								strValue = "0";
							}
							if ("04".equals(srtItemType)|| "05".equals(srtItemType)|| "06".equals(srtItemType)) {
								if (("".equals(strValue.trim())||strValue == null)&&"05".equals(srtItemType)) 
									strValue = "0";
								else if (("".equals(strValue.trim())||strValue == null)&&"04".equals(srtItemType)) 
									strValue = "0.00";
								else if (("".equals(strValue.trim())||strValue == null)&&"06".equals(srtItemType)) 
									strValue = "0.00";
								if (wanFlag){ 
									cell.setCellValue(Double.parseDouble(strValue) / 10000.0);
								}else{ 
									cell.setCellValue(Double.parseDouble(strValue) );
								}
							} else {
								cell.setCellValue(strValue);
							}
						}
						wanFlag = false;
						/*---设置公司名称和日期--begin--*/
						/*if (strFlag.indexOf("<g_corp(") != -1) 
							cell.setCellValue((String) mHashMap_Const.get("cname"));

						if (strFlag.indexOf("<g_titletime(") != -1) 
							cell.setCellValue((String) mHashMap_Const.get("tabledate"));
						*/
						/*---设置公司名称和日期--end--*/			
					}
				}
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return modelBook;
	}
	private boolean getRowAddMap() {
		if (!getRowAddDataFromDB()) {
			return false;
		}
		mHashMap_rowadd.clear();
		if (listRowAddData.size()>0) {
			for (int k = 0; k < listRowAddData.size(); k++) {
				Map<?,?> m=(Map<?, ?>) listRowAddData.get(k);
				String strTableCode = (String) m.get("tableCode");
				String strColCode = (String) m.get("colCode");
				String strRowNo = (String) m.get("rowNo");
				String strColType = (String) m.get("colType");
				Vector vctValue = new Vector();

				vctValue.add(strColType);// 集合中第一个是类型
				// 根据因子值类型取值
				String strValue = "";
				if ("06".equals(strColType))// 06 百分比型
				{
					strValue = String.valueOf(m.get("numValue"));
					if (null != strValue && !"".equals(strValue.trim())) {
						strValue = (new DecimalFormat("#0.00")).format(Double.parseDouble(strValue));
					}
				} else if ("05".equals(strColType))// 05 数量型
				{
					strValue = String.valueOf(m.get("numValue"));
					if (null != strValue && !"".equals(strValue.trim())) {
						strValue = (new DecimalFormat("#0.00")).format(Double.parseDouble(strValue));
					}
				} else if ("04".equals(strColType))// 04 数值型
				{
					strValue = String.valueOf(m.get("numValue"));
					if (null != strValue && !"".equals(strValue.trim())) {
						strValue = (new DecimalFormat("#0.0000")).format(Double
								.parseDouble(strValue));
					}
				} else if ("01".equals(strColType)||"07".equals(strColType))// 01 短文本类型,07日期型
				{
					strValue = (String) m.get("textValue");
				} else if ("02".equals(strColType))// 02 描述型
				{
					strValue = (String) m.get("desText");
				} else if ("03".equals(strColType))// 文件型因子，输出因子描述
				{
					strValue = "";
				}
				vctValue.add(strValue);// 集合中第二个是值
				mHashMap_rowadd.put(strTableCode + "_" + strRowNo + "_"+ strColCode, vctValue);
			}
		}
		return true;
	}
	private boolean getRowAddDataFromDB() {
		String strSQL = "";
		strSQL = "SELECT cf.* "
				+ " FROM cfreportrowadddata cf WHERE 1= 1 "
				// + " and UPPER(cf.tablecode) = '" + tableid + "' "
				+ " AND (cf.yearmonth) = '"
				+ this.mYearMonth
				+ "' "
				+ " AND CF.QUARTER = '"
				+ this.quarter
				+ "' "
				+ " AND CF.REPORTRATE = '"
				+ this.cfReportRate
				+ "' "
				+ " and comcode='"
				+ comcode
				+ "'"
				+ " AND (ifnull(CF.NUMVALUE, 0.00) <> 0.00 OR ifnull(CF.WANVALUE, 0.00) <> 0.00 OR CF.TEXTVALUE IS NOT NULL OR CF.DESTEXT IS NOT NULL) "
				+ "  ORDER BY tablecode ASC, TO_NUMBER(ROWNO) ASC,colcode ASC ";
        System.out.println("strSQL="+strSQL);
		listRowAddData = querydao.queryBysql(strSQL);
		if (listRowAddData.size()<=0) {
			System.out.println("查询行可扩展数据表出错");
			return false;
		}
		return true;
	}
	private boolean getReportDesc() {
		String strSQL = "SELECT c.reportcode,c.reportname,c.rulenature,c.remark,c.DEALWAY FROM CFREPORTDESC C WHERE 1 = 1 AND reportcompany1 = '1' ";
/*		if ("1".equals(cfReportRate))// 季度快报
		{
			strSQL = strSQL + " AND c.frequency = '1' ";
		} else if ("2".equals(cfReportRate))// 季度报告
		{
			strSQL = strSQL + " AND c.frequency2 = '1' ";
		} else if ("3".equals(cfReportRate))// 临时报告
		{
			strSQL = strSQL + " AND c.frequency3 = '1' ";
		}*/
		
		// temp1: 集团，temp2:财再，temp3:寿再，temp4:资再，temp1-->temp4值：1：财务部 2：投资部 3：精算部 4：国际部(集团)
		//FileFlag 1:财务部 2：投资部 3：精算部 4：国际部 5：偿付能力
		if(comcode.equals("000007")){
			if(fileFlag.equals("1")){
				strSQL = strSQL + "AND c.temp1='1' ";
			}else if(fileFlag.equals("2")){
				strSQL = strSQL + "AND c.temp1='2' ";
			}else if(fileFlag.equals("3")){
				strSQL = strSQL + "AND c.temp1='3' ";
			}else if(fileFlag.equals("4")){
				strSQL = strSQL + "AND c.temp1='4' ";
			}			
		}else if(comcode.equals("000008")){
			if(fileFlag.equals("1")){
				strSQL = strSQL + "AND c.temp2='1' ";
			}else if(fileFlag.equals("2")){
				strSQL = strSQL + "AND c.temp2='2' ";
			}else if(fileFlag.equals("3")){
				strSQL = strSQL + "AND c.temp2='3' ";
			}
		}else if(comcode.equals("000009")){
			if(fileFlag.equals("1")){
				strSQL = strSQL + "AND c.temp3='1' ";
			}else if(fileFlag.equals("2")){
				strSQL = strSQL + "AND c.temp3='2' ";
			}else if(fileFlag.equals("3")){
				strSQL = strSQL + "AND c.temp3='3' ";
			}
		}else if(comcode.equals("000089")){
			if(fileFlag.equals("1")){
				strSQL = strSQL + "AND c.temp4='1' ";
			}
		}
 		strSQL = strSQL + " ORDER BY c.reportorder DESC ";
 		System.out.println("strSQL="+strSQL);
 		listReportdesc = querydao.queryBysql(strSQL);
 		if(listReportdesc.size()<=0){
 			System.out.println("查询cfreportdesc表数据为空。");
 			return false;
 		}
		return true;
	}
	private boolean getItemMap() {
		// TODO Auto-generated method stub

		if (!getItemDataFromDB()) {
			return false;
		}
		mHashMap_Item.clear();
		if (listCfReportdata.size()>0) {
			for (int k = 0; k < listCfReportdata.size(); k++) {
				Vector vctValue = new Vector();
				Map<?,?> m=(Map<?,?>)listCfReportdata.get(k);
				String OutItemType = (String) m.get("OUTITEMTYPE");
				vctValue.add(OutItemType);// 集合中第一个是类型
				// 根据因子值类型取值
				String strValue = "";
				if ("06".equals(OutItemType))// 06 百分比型
				{
					strValue = (String) m.get("to_char(CF.REPORTITEMVALUE)");
					if( null != strValue && !"".equals(strValue.trim())){
						strValue = (new DecimalFormat("#0.00")).format(Double.parseDouble(strValue));
					}
				} else if ("05".equals(OutItemType))// 05 数量型
				{
					strValue = (String) m.get("to_char(CF.REPORTITEMVALUE)");
					if( null != strValue && !"".equals(strValue.trim())){
						strValue = (new DecimalFormat("#0.00")).format(Double.parseDouble(strValue));
					}
				} else if ("04".equals(OutItemType))// 04 数值型
				{
					strValue = (String) m.get("to_char(CF.REPORTITEMVALUE)");
					if( null != strValue && !"".equals(strValue.trim())){
						strValue = (new DecimalFormat("#0.0000")).format(Double.parseDouble(strValue));
					}
				} else if ("01".equals(OutItemType))// 01 短文本类型
				{
					strValue = (String) m.get("TEXTVALUE");
				} else if ("02".equals(OutItemType))// 02 描述型
				{
					strValue = "\n" + m.get("DESTEXT") + "\n";
				} else if ("03".equals(OutItemType))// 文件型因子，输出因子描述
				{
					strValue = "该部分需要手工添加，因子说明：" + m.get("Reportitemname");
				}
				vctValue.add(strValue);// 集合中第二个是值 
				mHashMap_Item.put(m.get("OUTITEMCODE"), vctValue);

			}
		}
		return true;	
	}
	private boolean getItemDataFromDB() {
		String strSQL = "";
		strSQL = " SELECT CFR.OUTITEMCODE,CFR.REPORTITEMCODE,to_char(CF.REPORTITEMVALUE),to_char(CF.REPORTITEMWANVALUE),CF.TEXTVALUE,CFR.OUTITEMTYPE,CFR.Reportitemname,CF.DESTEXT"
				+ " FROM CFREPORTITEMCODEDESC cfr left join (SELECT * FROM CFREPORTDATA WHERE 1 =1  ";
		// 通过频度筛选指标数据表中数据
		if (quarter != null && !"".equals(quarter)) {
			strSQL = strSQL + " AND quarter = '" + quarter + "' ";
		}
		if (cfReportRate != null && !"".equals(cfReportRate)) {
			strSQL = strSQL + " AND reportrate = '" + cfReportRate + "' ";
		}
		if (mYearMonth != null && !"".equals(mYearMonth)) {
			strSQL = strSQL + " AND month = '" + mYearMonth + "' ";
		}
		strSQL = strSQL + " and comcode='"+comcode+"'";
		if(this.downAllMode.equals("all")){
			strSQL = strSQL
			+ ") cf  on cfr.Outitemcode = cf.Outitemcode  WHERE  1 = 1  and (cfr.importsource='1' or cfr.importsource='9' or cfr.importsource='8')";
		}else{
			strSQL = strSQL
			+ ") cf  on cfr.Outitemcode = cf.Outitemcode  WHERE  1 = 1";
    
		}
		// 通过频度筛选指标描述表中需要报送的因子
		if("1".equals(cfReportRate)){//季度快报
			strSQL = strSQL + " AND cfr.quarterreport = '1' "; 
		}
		else if("2".equals(cfReportRate)){//季度报告
			strSQL = strSQL + " AND cfr.halfyearreport = '1' ";
		}
		else if("3".equals(cfReportRate)){//临时报告
			strSQL = strSQL + " AND cfr.yearreport = '1' ";
		}

		// 只查询寿险因子
		//strSQL = strSQL + " AND cfr.lifeinsurance1 = '1' ";
		strSQL = strSQL + " ORDER BY CFR.OUTITEMCODE ASC ";
		System.out.println("strSQL="+strSQL);
		listCfReportdata = querydao.queryBysql(strSQL);
		if(listCfReportdata.size()<=0){
			System.out.println("查询cfreportdata数据为空。");
			return false;
		}
		return true;
	}
	//获取前台信息
	private void getInputData(CfReportDownloadInfoDTO info) {
		// TODO Auto-generated method stub
		yearCode = info.getYear();
		cfReportRate = info.getReportType();
	    cfReportType = info.getReportName();
	    quarter = info.getQuarter();
	    fileDirName = yearCode + "-" + cfReportRate + "-" + quarter;
	    if("1".equals(info.getReportType())){
	    	mFilePath = Config.getProperty("ReportWork")+"mould/excel/QuarterQuickReportMode.xlsm";
	    	mFileName =  "QuarterQuickReportMode.xlsm";				
		}else if("2".equals(info.getReportType())){//保监会一样模版完整模版下载
	    	mFilePath = Config.getProperty("ReportWork")+"mould/excel/QuarterReportModeCirc.xlsm";
	    	mFileName =  "QuarterReportMode.xlsm";	
		}else if("3".equals(info.getReportType())){
	    	mFilePath = Config.getProperty("ReportWork")+"mould/excel/TempReportMode.xlsm";
	    	mFileName =  "TempReportMode.xlsm";	
		}
	    setMonth();
	}
	private boolean getRowDataFromDB(String tableid) {
		/*--通过表代码取行可扩展数据表中获取数据不为空的行的行号；数据全部为空的行，不报送--*/
		String strSQL = "";

		strSQL = "SELECT DISTINCT cf.rowno "
				+ " FROM cfreportrowadddata cf WHERE 1= 1 and UPPER(cf.tablecode) = '"
				+ tableid
				+ "' "
				+ " AND (cf.yearmonth) = '"
				+ this.mYearMonth
				+ "' "
				+ " AND CF.QUARTER = '"
				+ this.quarter
				+ "' "
				+ " AND CF.REPORTRATE = '"
				+ this.cfReportRate
				+ "' "
				+ " and comcode='"
				+ comcode
				+ "'"
				+ " AND (ifnull(CF.NUMVALUE, 0.00) <> 0.00 OR ifnull(CF.WANVALUE, 0.00) <> 0.00 OR CF.TEXTVALUE IS NOT NULL OR CF.DESTEXT IS NOT NULL) "
				+ " ORDER BY to_number(RowNo) ASC ";
        System.out.println(strSQL);
		listRowNo = querydao.queryBysql(strSQL);
		if (listRowNo.size()<=0) {
			System.out.println("查询行可扩展表数据为空");
			return false;
		}
		return true;
	}
	private void setMonth() {
		if ("1".equals(this.quarter)) {
			this.mYearMonth = this.yearCode;
			this.mTableDate = this.yearCode + "-03-31";
		} else if ("2".equals(this.quarter)) {
			this.mYearMonth = this.yearCode;
			this.mTableDate = this.yearCode + "-06-30";
		} else if ("3".equals(this.quarter)) {
			this.mYearMonth = this.yearCode;
			this.mTableDate = this.yearCode + "-09-30";
		} else if ("4".equals(this.quarter)) {
			this.mYearMonth = this.yearCode;
			this.mTableDate = this.yearCode + "-12-31";
		}
		mTableDate = mTableDate.replaceFirst("-", "年");
		mTableDate = mTableDate.replaceFirst("-", "月");
		mTableDate = mTableDate + "日";
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	
}
