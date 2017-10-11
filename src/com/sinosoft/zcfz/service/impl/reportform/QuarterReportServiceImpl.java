package com.sinosoft.zcfz.service.impl.reportform;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import com.sinosoft.zcfz.dao.reportform.QuarterReportDao;
import com.sinosoft.zcfz.service.reportform.QuarterReportService;
import com.sinosoft.util.Config;
import com.sinosoft.util.ExcelUtil;

@Service
public class  QuarterReportServiceImpl implements QuarterReportService{
	@Resource
    private  QuarterReportDao   daoReportDao;
	@Override
	// 总方法
	public String Loda(HttpServletRequest request,HttpServletResponse response) {
		 FileInputStream       fis = null;
		 FileOutputStream outputStream = null;
		try {   
				Map<String, String>   map_CodeType = ReadCodeType();         // 根据固定因子代码得到数据类型
		        Map<String, String>   map_rowType = ReadRowCodeType();       // 根据行可扩展因子代码得到数据类型
			    String                path = Config.getProperty("ReportWork")+"mould/excel/QuarterReportModeCirc.xlsm";
			    fis = new FileInputStream(path);
				XSSFWorkbook          workBook = new XSSFWorkbook(fis);
				int                   ToatleSheet =workBook.getNumberOfSheets();
				System.out.println("开始读取。。。。");
				Map<String, String>   MapData = getMapData(request);          // 得到行可扩展数据
				Map<String, String>   dataMap = getData(request);             // 得到固定因子数据
				Map<String, String>   rowNoMap = getMaxRowNo(request);        // 得到可扩展的最大行号
	            for (int i = 0; i < ToatleSheet; i++) {  
	            	XSSFSheet   sheet = workBook.getSheetAt(i);
	            	int         lastRowNum = sheet.getLastRowNum();
	            	int         firRowNum = sheet.getFirstRowNum();
	            	DataLoad(request, firRowNum, lastRowNum ,sheet,map_CodeType,dataMap);  // 固定因子填数
	            	getRowDataLoad(MapData,request,firRowNum,lastRowNum,sheet,map_rowType,rowNoMap,workBook);// 行可扩展填数
                }	
	            String  str = Config.getProperty("ReportWork")+"temp"; 
	            File    saveFile = new File(str);
	            if(!saveFile.exists()){saveFile.mkdirs();}
	            	                      
	            String  filepath = saveFile+"/QuarterReportModeCirc.xlsm";
	            
//	            try {  
	                // 重新将数据写入Excel中  
	            outputStream = new FileOutputStream(filepath);  
	                workBook.write(outputStream);
	                System.out.println("完成！"); 
	                outputStream.flush();  
//	                outputStream.close();  
//	            }  
//	            fis.close();
	            return  filepath;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}catch (Exception e) {  
            System.out.println("写入Excel时发生错误！ ");  
            e.printStackTrace();  
            return null;
        }finally {
			if(outputStream!=null){
				try {
					outputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	//CR091 
    public  void  CR091(XSSFSheet sheet,int firRowNum,int lastRowNum,Map<String, String>   rowNoMap,Map<String, String>   MapData,String ColCode){
    	F_QuarterReportDTO    fdto = firstRow(sheet,firRowNum,lastRowNum,rowNoMap,MapData);
    	int                   startRowNum = fdto.getRowNo();
    	String                rowNo = rowNoMap.get(ColCode+"_F00001");  // CR09 一共有几行
    	int                   lastCellNum = sheet.getRow(startRowNum).getLastCellNum();
    	String                commentValue0 = sheet.getRow(fdto.getRowNo()).getCell(0).getCellComment().getString().getString().trim();
    	String                H_RowNo = getH_RowNo(commentValue0);
        if(rowNo!=null){
        	int       in_rowNo = Integer.parseInt(rowNo);
    		XSSFRow   row_new=null;
    		XSSFRow   startRow = sheet.getRow(startRowNum);
    		for (int j = startRowNum; j < 2*(in_rowNo-1)+startRowNum; j++) {
    			sheet.shiftRows(j+2, lastRowNum,1, true,false);
    			row_new = sheet.createRow(j+2);
    			for (int j2 = 0; j2 <lastCellNum; j2++) {
    				XSSFCellStyle cellStyle = startRow.getCell(j2).getCellStyle();
    				cellStyle.setWrapText(false);    
    				row_new.createCell(j2).setCellStyle(cellStyle);
				}
    			j++;
			}
    		XSSFRow       row = sheet.getRow(fdto.getRowNo());
        		for (int j = 1; j < row.getLastCellNum(); j++) {
					XSSFCell       cell = row.getCell(j);
					XSSFComment    comment = cell.getCellComment();
					if(comment==null){continue;}
					String         value = comment.getString().getString().trim();
					int m=0;
					for (int k = fdto.getRowNo(); k < 2*in_rowNo+fdto.getRowNo(); k++) {
						m++;
						String         cell_value = MapData.get(ColCode+"_"+value+"_"+(m));
						XSSFRow        row_2 = sheet.getRow(k);
						XSSFCell       cell_0 = row_2.getCell(0);
						XSSFCell       cell_2 = row_2.getCell(j);
						cell_0.setCellValue(H_RowNo+m);
						cell_2.setCellValue(cell_value);
						if(m>in_rowNo){m=1;}
						k++;
					}
				}
             }
    }
    // CR092
    public  void  CR092(HttpServletRequest request,XSSFSheet sheet,int firRowNum,int lastRowNum,Map<String, String>   rowNoMap,Map<String, String>   MapData,String ColCode){
    	F_QuarterReportDTO    fdto = firstRow(sheet,firRowNum,lastRowNum,rowNoMap,MapData);
    	int                   startRowNum = fdto.getRowNo();
    	String                rowNo = rowNoMap.get(ColCode+"_F00001");  // CR09 一共有几行
    	int                   lastCellNum = sheet.getRow(fdto.getRowNo()).getLastCellNum();
    	XSSFRow               row_1 = sheet.getRow(startRowNum+1);
    	if(rowNo!=null){
    		int m=0;
    		for (int i = startRowNum+1; i < 100; i++) {
    			m++;
    			int in = getCR09_RoNum(request,m);
    			if(in==0){continue;}
    			if(in>1){
    				sheet.shiftRows(i+1,lastRowNum,in-1, true,false);                // 增加行
    			}
    			for (int j = i+1; j < in+i; j++) {
    				XSSFRow             row_2 = sheet.createRow(j);
    				for (int j2 = 0; j2 <lastCellNum ; j2++) {
    					XSSFCellStyle cellStyle = row_1.getCell(j2).getCellStyle();
    					row_2.createCell(j2).setCellStyle(cellStyle);
    				}
				}
    			int a = i+in;
    			i=a;
			}
    		XSSFRow       row = sheet.getRow(startRowNum+1);
    		for (int i = 1; i < lastCellNum; i++) {              // cell 循环
				XSSFCell      cell = row.getCell(i);
				XSSFComment   comment = cell.getCellComment();
				if(comment==null){continue;}
				String        commentValue = comment.getString().getString().trim();
				int b=0;
				for (int j = startRowNum+1; j < 100; j++) {     // row循环
					b++;
					int       in = getCR09_RoNum(request,b);
        			if(in==0){continue;}
        			int d=0;
        			XSSFRow    row_j = sheet.getRow(j-1);
        			for (int k = j; k < in+j+1 ; k++) {       // 小循环
        				XSSFRow    row_k = sheet.getRow(k);
        				if(row_k==null){continue;}
        				d++;
        				String     commentValue_j = ColCode+"_0"+d+"_"+commentValue;
    	    			String     cell_value = MapData.get(commentValue_j+"_"+(b));
    	    			if(cell_value==null){continue;}
    					XSSFCell   cell_i = row_k.getCell(i);
    					XSSFCell   cell_0 = row_j.getCell(0);
    					String     cell_0Value =  ExcelUtil.getCellValue(cell_0);
    					XSSFCell   cell_01 = row_k.getCell(0);
    					if(cell_i==null){continue;}
    					cell_i.setCellValue(cell_value);
    					if(d>in){continue;}
   					    cell_01.setCellValue(cell_0Value+"."+d);
					}
	    			int c=j+in;
	    			j=c;
				}
			}
    	}
    }
	// first Row
	public F_QuarterReportDTO firstRow(XSSFSheet sheet,int firRowNum,int lastRowNum,Map<String, String>   rowNoMap,Map<String, String>   MapData){
		    F_QuarterReportDTO  fdto = new F_QuarterReportDTO();
		    int  n=0;
		    for (int i = firRowNum; i < lastRowNum; i++) {                // i 代表sheet的行号
		    	XSSFRow          row = sheet.getRow(i);
		    	if(row==null||"".equals(row)){continue;}
		    	XSSFCell         cell_0 = row.getCell(0);
		    	String           value = ExcelUtil.getCellValue(cell_0);
		    	if(value.contains("行次")){
		    		n=i;
		    	}
		    	if(cell_0==null){continue;}
		    	XSSFComment          comment_0= cell_0.getCellComment();
		    	if(comment_0==null){continue;}
		    	XSSFRichTextString   rts_0 = comment_0.getString();
		    	String               commentValue_0 = rts_0.getString().trim();
			    if(!(commentValue_0.contains("tableid"))){continue;
			    }else{
				int     in = commentValue_0.replace("<tableid=", "").trim().indexOf("%");
			    String  ColCode_0 = commentValue_0.replace("<tableid=", "").substring(0, in);
			    String  map_ColCode_0 = ColCode_0+"_F00001";
			    String  rowNo_0 = rowNoMap.get(map_ColCode_0);
			    if(rowNo_0==null){continue;}
			    if(Integer.parseInt(rowNo_0)>0){
				   for (int j = row.getFirstCellNum(); j < row.getLastCellNum(); j++) {  // j 代表循环单元格
					XSSFCell              cell_j = row.getCell(j);
					XSSFComment           comment_j = cell_j.getCellComment();
					if(comment_j==null){continue;}
					XSSFRichTextString    rts_j = comment_j.getString();
					String                cell_j_value = rts_j.getString().trim();
                	if(cell_j_value.contains("tableid")){
                		fdto.setRowNo(i);
                	}
				  }	
			    }
			}
			  if(n+1==i){break;}
		   }
		    return fdto;
	}
	// 固定因子数据填数
	public  void  DataLoad(HttpServletRequest request,int firRowNum,int lastRowNum ,XSSFSheet sheet,Map<String, String>  map_CodeType,Map<String,String> dataMap){
		for (int j = firRowNum; j <= lastRowNum; j++) {
			XSSFRow row = sheet.getRow(j);
			if(row==null||"".equals(row)){continue;}
			int    lastCellNum = row.getLastCellNum();
			for (int k = 0; k < lastCellNum; k++) {
				XSSFCell  cell = row.getCell(k);
				if(cell==null||"".equals(cell)){continue;}
				XSSFComment  comment= cell.getCellComment();
				if(comment==null||"".equals(comment)){continue;}
				XSSFRichTextString    rts = comment.getString();
			    if(rts==null||"".equals(rts)){continue;}
				String  commentValue = rts.getString().trim();
				if(commentValue.contains("g_item")){
					if(commentValue.contains("/wan")){
						String   reportItemCode = commentValue.replace("<g_item(", "").replace(")>/wan", "").trim();
					    String   outItemValue = dataMap.get(reportItemCode);  // 根据类型可到数据
					    String   code_type = map_CodeType.get(reportItemCode);
					    if("0".equals(outItemValue)||".00".equals(outItemValue)||"0.00".equals(outItemValue)||"0.0000".equals(outItemValue)){
					    	 cell.getCellStyle().setAlignment(XSSFCellStyle.ALIGN_RIGHT);
					    	 cell.setCellValue("-");
					    }else{
					    	if("01".equals(code_type)||"02".equals(code_type)||"03".equals(code_type)){
					    		cell.getCellStyle().setAlignment(XSSFCellStyle.ALIGN_LEFT);
					    		cell.setCellValue(outItemValue);
					    	}else{
					    		cell.getCellStyle().setAlignment(XSSFCellStyle.ALIGN_RIGHT);
					    		cell.setCellValue(outItemValue);
					    	}
					    }
					}else{
						String  reportItemCode = commentValue.replace("<g_item(", "").replace(")>", "").trim();
						String   outItemValue = dataMap.get(reportItemCode);
						String   code_type = map_CodeType.get(reportItemCode);
						 if("0".equals(outItemValue)||".00".equals(outItemValue)||"0.00".equals(outItemValue)||"0.0000".equals(outItemValue)){
					    	 cell.getCellStyle().setAlignment(XSSFCellStyle.ALIGN_RIGHT);
					    	 cell.setCellValue("-");
					    }else{
					    	if("01".equals(code_type)||"02".equals(code_type)||"03".equals(code_type)){
					    		cell.getCellStyle().setAlignment(XSSFCellStyle.ALIGN_LEFT);
					    		cell.setCellValue(outItemValue);
					    	}else{
					    		cell.getCellStyle().setAlignment(XSSFCellStyle.ALIGN_RIGHT);
					    		cell.setCellValue(outItemValue);
					    	}
					    }
					}
				}
			}
		}
	}
	// 行可扩展填数
	public  void  getRowDataLoad(Map<String, String> MapData ,HttpServletRequest request,int firRowNum,int lastRowNum ,XSSFSheet sheet,Map<String, String>   map_rowType,Map<String, String>   rowNoMap,XSSFWorkbook  workBook ){
		for (int i = firRowNum; i < lastRowNum; i++) {
			XSSFRow  row = sheet.getRow(i);
			if(row==null||"".equals(row)){continue;}
			XSSFCell  cell = row.getCell(0);
			if(cell==null||"".equals(cell)){continue;}
			XSSFComment  comment= cell.getCellComment();
			if(comment==null||"".equals(comment)){continue;}
			XSSFRichTextString    rts = comment.getString();
			String  commentValue = rts.getString().trim();
			if(commentValue.contains("tableid")){                                     
				int  in = commentValue.replace("<tableid=", "").trim().indexOf("%");
			    String  ColCode = commentValue.replace("<tableid=", "").substring(0, in);
			   if(("<tableid=CR09%1.x>").equals(commentValue)){
				   F_QuarterReportDTO    fdto = firstRow(sheet,firRowNum,lastRowNum,rowNoMap,MapData);
				   if(fdto.getRowNo()==0){continue;}
				   CR091(sheet,firRowNum,lastRowNum,rowNoMap,MapData, ColCode);
				   CR092(request,sheet,firRowNum,lastRowNum,rowNoMap,MapData, ColCode);
			   }else{
				if(commentValue.contains("<tableid=CR09%1.1.x>")){continue;}
			    String  rowNo = rowNoMap.get(ColCode+"_F00001");
			    if(rowNo==null){continue;}
			    int in_rowNo = Integer.parseInt(rowNo);
			    if(in_rowNo>0){
			    	String  H_RowNo = getH_RowNo(commentValue);
			    	if(in_rowNo>1){
			    		 sheet.shiftRows(i+1, lastRowNum,in_rowNo-1, true, false);   //  增加一行
			    	}
				    for (int j = i+1; j < i+in_rowNo; j++) {
						XSSFRow    new_row = sheet.createRow(j);
						for (int k = 0; k < row.getLastCellNum(); k++) {
							XSSFCell   old_cell = row.getCell(k);
							if(old_cell==null){
								continue;
							}
							XSSFCellStyle   style = old_cell.getCellStyle();
							XSSFCell        new_cell = new_row.createCell(k);
							new_cell.setCellStyle(style);
						}
					}
			    	for (int j = 1; j < row.getLastCellNum(); j++) {
				    	XSSFCell              cell_j = row.getCell(j);
				    	if(cell_j==null||"".equals(cell_j)){continue;}
						XSSFComment           comment_j= cell_j.getCellComment();
						if(comment_j==null||"".equals(comment_j)){continue;}
						XSSFRichTextString    rts_j = comment_j.getString();
						String                commentValue_j =  rts_j.getString().trim();
						String                commentValue_k=null;
						if(commentValue_j.contains("/10000")){
						   String  comment0 = commentValue_j.replace("/10000", "");	
						   commentValue_k = ColCode+"_"+comment0;
						}else{commentValue_k = ColCode+"_"+commentValue_j;}
					//	if(MapData.get(commentValue_k+"_1")==null){continue;}
						for (int k = 0; k < in_rowNo; k++) {                   // k 代表行号rowNo
							XSSFRow row_i = sheet.getRow(i+k);
								XSSFCell cell_k = row_i.getCell(j);
								String colType= map_rowType.get(commentValue_k);
								if("04".equals(colType)||"05".equals(colType)||"06".equals(colType)){
									cell_k.getCellStyle().setAlignment(XSSFCellStyle.ALIGN_RIGHT);
									cell_k.setCellValue(MapData.get(commentValue_k+"_"+(k+1)));
								}else{
									cell_k.getCellStyle().setAlignment(XSSFCellStyle.ALIGN_LEFT);
									row_i.getCell(0).getCellStyle().setAlignment(XSSFCellStyle.ALIGN_LEFT);
									row_i.getCell(0).setCellValue(H_RowNo+(k+1));
									cell_k.setCellValue(MapData.get(commentValue_k+"_"+(k+1)));
							}
					    }
					 }
			      }
			   }
			}
		  }
	}
    // 获取最大行数
	public  Map<String, String>  getMaxRowNo(HttpServletRequest request){
		String   hsql="select colcode, max(to_number(rowNo)) from CfReportRowAddData des  where  reportid='"+request.getParameter("id")+"' group by colcode";
		List<?>  list = daoReportDao.queryBysql(hsql);
		Map<String,String>  rowNo_Map = new  HashMap<String,String>();
		rowNo_Map.clear();
		for (int i = 0; i < list.size(); i++) {
			Map  data = (Map) list.get(i);
			String  colCode = (String) data.get("colCode");
			String  rowNo =  data.get("max(to_number(rowNo))").toString();
			rowNo_Map.put(colCode, rowNo);
		}
		 return rowNo_Map;
	}
	// 行号
    public  String   getH_RowNo(String   commentValue){  //<tableid=CR09%1.1.x>
		System.out.println("commentValue="+commentValue);
		String  H_RowNo = commentValue.substring(commentValue.indexOf("%")+1, commentValue.indexOf("x"));
		if(H_RowNo.contains(".")){
			return  H_RowNo;
		}else{
			return  "";
		}
	}
    // 得到固定因子数据
	public  Map<String, String>  getData(HttpServletRequest request){
		DecimalFormat df0 = new DecimalFormat("###,###");
		DecimalFormat df2 = new DecimalFormat("###,##0.00");
		DecimalFormat df4 = new DecimalFormat("###,##0.0000");
		DecimalFormat df8 = new DecimalFormat("###,##0.00000000");
		Map<String, String>  map_decimal= getCodeDecimal();
		Map<String, String>  map = ReadCodeType();
		Map<String, String>  dataMap = new HashMap<String, String>();
		String   sql = "select reportItemCode,textValue,reportItemValue from CfReportData data  where data.reportid='"+request.getParameter("id")+"' ";
		List<?>  list =	daoReportDao.queryBysql(sql);
		for(int i=0;i<list.size();i++){
			Map  data = (Map) list.get(i);
			String  reportItemCode = (String) data.get("reportItemCode");
			if(reportItemCode==null ){continue;}
			if("01".equals(map.get(reportItemCode))||"02".equals(map.get(reportItemCode))||"03".equals(map.get(reportItemCode))){
				if (data.get("textValue") == null) {continue;}
					dataMap.put(reportItemCode, data.get("textValue").toString());
			}else if("2".equals(map_decimal.get(reportItemCode))){
					 String   value = new BigDecimal(data.get("reportItemValue").toString()).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
					 double   cellvalue = Double.parseDouble(value);
					 dataMap.put(reportItemCode, df2.format(cellvalue));
			}else if("4".equals(map_decimal.get(reportItemCode))){
				 String   value = new BigDecimal(data.get("reportItemValue").toString()).setScale(4, BigDecimal.ROUND_HALF_UP).toString();
				 double   cellvalue = Double.parseDouble(value);
				 dataMap.put(reportItemCode, df4.format(cellvalue));
			}else if("8".equals(map_decimal.get(reportItemCode))){
				 String   value = new BigDecimal(data.get("reportItemValue").toString()).setScale(8, BigDecimal.ROUND_HALF_UP).toString();
				 double   cellvalue = Double.parseDouble(value);
				 dataMap.put(reportItemCode, df8.format(cellvalue));
			}else{
				String value = data.get("reportItemValue").toString();
				double   cellvalue = Double.parseDouble(value);
				 dataMap.put(reportItemCode, df0.format(cellvalue));
			 }
 		}
		return  dataMap;
	}
	// 得到固定因子数据类型
	public  Map<String, String>  ReadCodeType(){
		String   hsql = "select reportItemCode,outItemType  from CfReportItemCodeDesc ";
	    List<?>  list =	 daoReportDao.queryBysql(hsql);
	    Map<String,String>      map  =  new HashMap<String,String>();
	    for (int i = 0; i < list.size(); i++) {
	    	Map desc = (Map) list.get(i);
			map.put(desc.get("reportItemCode").toString(), desc.get("outItemType").toString());
		}
	    return  map;
	}
	// 得到行可扩展数据
	public   Map<String,String>  getMapData(HttpServletRequest request){
		DecimalFormat df0 = new DecimalFormat("###,###");
		DecimalFormat df2 = new DecimalFormat("###,##0.00");
		DecimalFormat df4 = new DecimalFormat("###,##0.0000");
		DecimalFormat df8 = new DecimalFormat("###,##0.00000000");
		 Map<String, String>  map_decimal= getCodeDecimal();
		 String   sql = "select rowdata.textValue,rowdata.numValue,rowdata.colcode,rowdata.rowno,des.coltype from CfReportRowAddData rowdata,cfreportrowadddesc des where rowdata.colcode=des.colcode and rowdata.reportid='"+request.getParameter("id")+"'";
		 List<?>  list = daoReportDao.queryBysql(sql);
		 if(list.size()==0){ return  null; }
		 Map<String,String> map = new HashMap<String,String>();
		 for (int i = 0; i < list.size(); i++) {
			 Map      rowMap =  (Map) list.get(i);
			 String   colType = (rowMap.get("coltype")).toString();         // 得到数据类型   
			 String   colCode = (rowMap.get("colcode")).toString();         // 得到指标代码
			 String   rowNo = (rowMap.get("rowno")).toString();             // 得到指标代码
			 if(rowMap.get("textValue")==null&&rowMap.get("numValue")==null){continue;}
			 //  01 短文本 02 描述 03 文件 04 数值 05 数量 06 百分比
			 if("CR09_F00001".equals(colCode)&&"1".equals(rowNo)){
				 System.out.println();
			 }
			 if("01".equals(colType)||"02".equals(colType)||"03".equals(colType)){
				 if(rowMap.get("textValue")!=null){
					 map.put(colCode+"_"+rowNo, rowMap.get("textValue").toString());
				 }
			 }else{
				 if(rowMap.get("numValue")!=null&&"2".equals(map_decimal.get(colCode))){
					 String   value = new BigDecimal(rowMap.get("numValue").toString()).setScale(2, BigDecimal.ROUND_HALF_UP).toString();
					 double   cellvalue = Double.parseDouble(value);
					 map.put(colCode+"_"+rowNo, df2.format(cellvalue));
				 }
				 if(rowMap.get("numValue")!=null&&"4".equals(map_decimal.get(colCode))){
					 String   value = new BigDecimal(rowMap.get("numValue").toString()).setScale(4, BigDecimal.ROUND_HALF_UP).toString();
					 double   cellvalue = Double.parseDouble(value);
					 map.put(colCode+"_"+rowNo, df4.format(cellvalue));
				 }
				 else if(rowMap.get("numValue")!=null&&"8".equals(map_decimal.get(colCode))){
					 String   value = new BigDecimal(rowMap.get("numValue").toString()).setScale(8, BigDecimal.ROUND_HALF_UP).toString();
					 double   cellvalue = Double.parseDouble(value);
					 map.put(colCode+"_"+rowNo, df8.format(cellvalue));
				 }else{
					 String value = rowMap.get("numValue").toString();
					 double   cellvalue = Double.parseDouble(value);
					 map.put(colCode+"_"+rowNo, df2.format(cellvalue));
				 }
			 }
		}
		return map;
	}
    // 得到行可扩展数据的数据类型
	public  Map<String, String>   ReadRowCodeType(){
		String   sql = "select colcode,coltype from cfreportrowadddesc";
		List     list = daoReportDao.queryBysql(sql);
		Map<String,String>      hashMap = new HashMap<String,String>();
		for (int i = 0; i < list.size(); i++) {
			Map  map = (Map) list.get(i);
			if(map.get("colcode")==null||map.get("coltype")==null){continue;}
			String   colcode = (String) map.get("colcode");
			String   coltype = (String) map.get("coltype");
			hashMap.put(colcode, coltype);
		}
		return  hashMap;
	}
	// 获取所有因子代码精度
	public  Map<String, String>   getCodeDecimal(){
		String   sql="select el.portitemcode,el.decimals from cfreportelement el";
		List     list = daoReportDao.queryBysql(sql);
		Map<String,String>      map_type = new HashMap<String,String>();
		for (int i = 0; i < list.size(); i++) {
			Map      map = (Map) list.get(i);
			String   portitemcode = map.get("portitemcode").toString();
			if(map.get("decimals")==null){continue;}
			String  decimals =	map.get("decimals").toString();
			map_type.put(portitemcode, decimals);
		}		
		return map_type;
	}
	//  特殊CR09取行数
	public  int  getCR09_RoNum(HttpServletRequest request,int in){
		String       sql = "select count(*) from cfreportrowadddata r where  r.tablecode like 'CR09_%' and r.colcode like'%F00001%'and r.reportid='"+request.getParameter("id")+"'  and r.rowno='"+in+"'";
		    List     list = daoReportDao.queryBysql(sql);
			Map      map = (Map)list.get(0);
			Integer   count = Integer.parseInt(map.get("count(*)").toString());  // 得到数据类型
			return count;
	}
}
