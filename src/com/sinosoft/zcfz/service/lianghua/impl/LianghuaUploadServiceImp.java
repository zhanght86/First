package com.sinosoft.zcfz.service.lianghua.impl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bouncycastle.jce.provider.JDKDSASigner.stdDSA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;












import com.sinosoft.common.Dao;
import com.sinosoft.dao.CfmapDao;
import com.sinosoft.zcfz.dao.lianghua.ALM_CfReportDataDAO;
import com.sinosoft.zcfz.dao.lianghua.ALM_CfReportItemCodeDescDAO;
import com.sinosoft.zcfz.dao.lianghua.ALM_CfReportRowAddDataDAO;
import com.sinosoft.zcfz.dao.lianghua.ALM_CfReportRowAddDescDAO;
import com.sinosoft.zcfz.dao.reportdataimport.CfReportItemCodeDescDAO;
import com.sinosoft.zcfz.dto.reportdataimport.UploadInfoDTO;
import com.sinosoft.zcfz.entity.lianghua.ALM_CfReportData;
import com.sinosoft.zcfz.entity.lianghua.ALM_CfReportDataId;
import com.sinosoft.zcfz.entity.lianghua.ALM_CfReportItemCodeDesc;
import com.sinosoft.zcfz.entity.lianghua.ALM_CfReportRowAddData;
import com.sinosoft.zcfz.entity.lianghua.ALM_CfReportRowAddDataId;
import com.sinosoft.entity.Cfmap;
import com.sinosoft.entity.ReportHistory;
import com.sinosoft.entity.UserInfo;
import com.sinosoft.zcfz.service.impl.reportformcompute.PreComputeImp;
import com.sinosoft.zcfz.service.lianghua.LianghuaUploadService;
import com.sinosoft.zcfz.service.reportform.RiskUploadService;
import com.sinosoft.util.Config;
import com.sinosoft.util.ExcelUtil;
import com.sinosoft.util.UploadNumberFormatException;
import com.sinosoft.util.UploaderServlet;


@Service
public class LianghuaUploadServiceImp implements LianghuaUploadService{
	private SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
	@Resource
	private ExcelUtil eu;
	@Resource
	private UploaderServlet us;
	@Resource 
	private CfmapDao mapDao;
	@Resource
	private ALM_CfReportItemCodeDescDAO impcfReportDataDao;
	@Resource
	private ALM_CfReportRowAddDescDAO alm_CfReportRowAddDescDAO;
	@Resource
	private Dao dao;
	private static final Logger LOGGER = LoggerFactory.getLogger(PreComputeImp.class);
	private Map<String, Object> hismap=new HashMap<String, Object>();
	@Resource
	private CfReportItemCodeDescDAO impcfReportItemCodeDescDao;
	private Map<String, String> map=new HashMap<String, String>();
	private List<Map<String, Object>> uploadInfoDTOs = new ArrayList<Map<String, Object>>();//存放错误结果集
	@Resource
	private ALM_CfReportDataDAO impcfReportDatadao;
	@Resource
	private ALM_CfReportRowAddDataDAO alm_CfReportRowAddDataDAO;
	@Transactional
	public void saveData(UploadInfoDTO pld,UserInfo user,String deptflag) throws UploadNumberFormatException,IOException,Exception{
		    uploadInfoDTOs.clear();
		    try {
		    	saveDataA01(pld,user,deptflag);
		    	saveDataA02(user, pld);
		    } catch (Exception e) {
		    	// TODO: handle exception
		    }
	}
	@SuppressWarnings("resource")
	@Transactional
	public void saveDataA01(UploadInfoDTO pld,UserInfo user,String deptflag)throws UploadNumberFormatException,NullPointerException,Exception {
		
		String reportId=pld.getReportId();
		//删除对应报送号的数据
		System.out.println("删除对应报送号的数据");
		impcfReportDataDao.excute("delete  from ALM_cfreportdata where reportid='"+reportId+"'");
		System.out.println("删除完毕！！！！");
		//查询reportitemcodedesc表中的数据的位置
		String sql="select * from ALM_CfReportItemCodeDesc";
		List<?> rowlist=impcfReportDataDao.queryBysql(sql);
		
		//获取文件路径
		String path=pld.getFilePath()+"/"+us.getFileName();
		
		
		FileInputStream fis = null;
	    XSSFWorkbook wb = null;
		XSSFSheet sheet = null;
		XSSFRow row = null;
		XSSFCell cell = null;
		String valueString =null;
		try {			
			fis = new FileInputStream(path);
			wb = new XSSFWorkbook(fis);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		for (int i = 0; i < rowlist.size(); i++) {
			System.out.println("正在执行-固定因子-第"+i+"行，还有"+(rowlist.size()-i)+"行");
			Map<String, String> map= (Map<String, String>) rowlist.get(i);
			//获取定位
			String itemCode=map.get("reportitemcode").toString();//因子代码
			String rowNum=map.get("sheetrow").toString();//行号
			String cellNum=map.get("sheetcol").toString();//列号
			String sheetName=map.get("outitemnote").toString();//表名
			String dataType = map.get("outitemtype").toString();//数据类型
			
			System.out.println("代码"+itemCode+"--行数--"+rowNum+",--列数--"+cellNum);
			//读取数据
			sheet = wb.getSheet(sheetName);
			if(sheet == null) continue;
		    row = sheet.getRow(Integer.parseInt(rowNum)-1);
		    if(row == null) continue;
		    cell = row.getCell(Integer.parseInt(cellNum)-1);
		    if(cell == null) continue;
		    
		    try {
		    	valueString= eu.getCellValue(cell);
			} catch (Exception e) {
				e.printStackTrace();
			}
		    
		    //把数据填到实例里
		       ALM_CfReportDataId cfReportDataId=new ALM_CfReportDataId();
		       ALM_CfReportData cfReportData=new ALM_CfReportData();
		       cfReportDataId.setOutItemCode(itemCode);//因子代码		       
		       cfReportDataId.setReportId(pld.getReportId());
		       cfReportData.setMonth(pld.getYearmonth());//月度
		       cfReportData.setQuarter(pld.getQuarter());//季度
		       cfReportData.setReportRate(pld.getReporttype());//报送频度
		       cfReportData.setId(cfReportDataId);
		       cfReportData.setComCode(pld.getCompanycode());//机构代码
		       cfReportData.setOutItemCodeName(map.get("reportitemname").toString());//因子名称
		       cfReportData.setReportItemCode(itemCode);//内部码
		  
		//cfReportItemCodeHis(pld, user,deptflag);
		//getCfReportItemCodeDesc(pld, user,deptflag);
		
		/*validateNumberFormat(list, pld);//校验数据格式
		if (uploadInfoDTOs.size() != 0) {
			throw new UploadNumberFormatException(uploadInfoDTOs);
		}*/
		/*validateDecimals(list, pld);//校验精度
		if (uploadInfoDTOs.size() != 0) {
			throw new UploadDecimalsException(uploadInfoDTOs);
		}*/
		       //输出数据类型和值
		      
		       if(dataType.equals("06")){//百分比型
		    	   try {
		    		   if("".equals(valueString)||"--".equals(valueString)||"-".equals(valueString)){		    		   
			    		   System.out.println("无值可取！！！");
			    		   cfReportData.setReportItemValue(new BigDecimal(0).setScale(8,BigDecimal.ROUND_HALF_UP));
			    		   cfReportData.setReportItemWanValue(new BigDecimal(0).setScale(12,BigDecimal.ROUND_HALF_UP));
			    	   }else if(valueString.trim().contains("不适用")){
			    		   cfReportData.setReportItemValue(new BigDecimal(0).setScale(8,BigDecimal.ROUND_HALF_UP));
			    		   cfReportData.setReportItemWanValue(new BigDecimal(0).setScale(12,BigDecimal.ROUND_HALF_UP));
			    		   cfReportData.setIsused("0");
			    	   }else{
			    		   cfReportData.setReportItemValue(new BigDecimal(valueString).setScale(8,BigDecimal.ROUND_HALF_UP));
			    		   cfReportData.setReportItemWanValue(new BigDecimal(0).setScale(12,BigDecimal.ROUND_HALF_UP));
			    	   }
			       } catch (NumberFormatException e) {
			    	   //e.printStackTrace();
			       }
		    	   cfReportData.setOutItemType("06");
		    	   cfReportData.setTextValue("");
		    	   cfReportData.setDesText(null);//描述性型
		    	   cfReportData.setFileText(null);//文件型
		       }else if(dataType.equals("05")){//数量型		    	   
		    	   try {
		    		   if("".equals(valueString)||"--".equals(valueString)||"--".equals(valueString)){		    		   
		    			   System.out.println("无值可取！！！");
		    			   cfReportData.setReportItemValue(new BigDecimal(0).setScale(8,BigDecimal.ROUND_HALF_UP));
		    			   cfReportData.setReportItemWanValue(new BigDecimal(0).setScale(12,BigDecimal.ROUND_HALF_UP));
		    		   }else if(valueString.trim().contains("不适用")){
			    		   cfReportData.setReportItemValue(new BigDecimal(0).setScale(8,BigDecimal.ROUND_HALF_UP));
			    		   cfReportData.setReportItemWanValue(new BigDecimal(0).setScale(12,BigDecimal.ROUND_HALF_UP));
			    		   cfReportData.setIsused("0");
			    	   }else{
		    			   cfReportData.setReportItemValue(new BigDecimal(valueString).setScale(8,BigDecimal.ROUND_HALF_UP));
		    			   cfReportData.setReportItemWanValue(new BigDecimal(valueString).divide(new BigDecimal("10000"), 12, RoundingMode.HALF_UP));
		    		   }
			       } catch (NumberFormatException e) {
			    	   e.printStackTrace();
			       }cfReportData.setOutItemType("05");
		    	   cfReportData.setTextValue("");
		    	   cfReportData.setDesText(null);//描述性型
		    	   cfReportData.setFileText(null);//文件型
		       }else if(dataType.equals("04")){//数值型
		    	   try {
		    		   if("".equals(valueString)||"--".equals(valueString)||"--".equals(valueString)){		    		   
		    			   System.out.println("无值可取！！！");
		    			   cfReportData.setReportItemValue(new BigDecimal(0).setScale(8,BigDecimal.ROUND_HALF_UP));
		    			   cfReportData.setReportItemWanValue(new BigDecimal(0).setScale(12,BigDecimal.ROUND_HALF_UP));
		    		   }else if(valueString.trim().contains("不适用")){
			    		   cfReportData.setReportItemValue(new BigDecimal(0).setScale(8,BigDecimal.ROUND_HALF_UP));
			    		   cfReportData.setReportItemWanValue(new BigDecimal(0).setScale(12,BigDecimal.ROUND_HALF_UP));
			    		   cfReportData.setIsused("0");
			    	   }else{
			    		   System.out.println("211行值为"+valueString);
		    			   cfReportData.setReportItemValue(new BigDecimal(valueString).setScale(8,BigDecimal.ROUND_HALF_UP));
		    			   cfReportData.setReportItemWanValue(new BigDecimal(valueString).divide(new BigDecimal("10000"), 12, RoundingMode.HALF_UP));
		    		   }
			       } catch (NumberFormatException e) {
			    	   e.printStackTrace();
			       }
		    	   cfReportData.setOutItemType("04");
		    	   cfReportData.setTextValue("");
		    	   cfReportData.setDesText(null);//描述性型
		    	   cfReportData.setFileText(null);//文件型
		       }else if(dataType.equals("03")){//文件型
		    	   try {
		    		   cfReportData.setOutItemType("03");
		    		   cfReportData.setReportItemValue(new BigDecimal(0).setScale(4,BigDecimal.ROUND_HALF_UP));
		    		   cfReportData.setReportItemWanValue(new BigDecimal(0).setScale(8,BigDecimal.ROUND_HALF_UP));
			       } catch (NumberFormatException e) {
			    	   e.printStackTrace();
			       }
		    	   cfReportData.setTextValue("");
		    	   cfReportData.setDesText(null);//描述性型
		    	   cfReportData.setFileText(null);//文件型
		       }else if(dataType.equals("02")){
		    	   try {
		    		   cfReportData.setOutItemType("02");
		    		   cfReportData.setReportItemValue(new BigDecimal(0).setScale(4,BigDecimal.ROUND_HALF_UP));
		    		   cfReportData.setReportItemWanValue(new BigDecimal(0).setScale(8,BigDecimal.ROUND_HALF_UP));
			       } catch (NumberFormatException e) {
			    	   e.printStackTrace();
			       }
		    	   cfReportData.setTextValue("");
		    	   cfReportData.setDesText(null);//描述性型
		    	   cfReportData.setFileText(null);//文件型
		       }else if(dataType.equals("01")){//短文本类型
		    	   cfReportData.setOutItemType("01");
		    	   cfReportData.setReportItemValue(new BigDecimal(0).setScale(4,BigDecimal.ROUND_HALF_UP));
		    	   cfReportData.setReportItemWanValue(new BigDecimal(0).setScale(8,BigDecimal.ROUND_HALF_UP));
		    	   try {
		    		   //String textValue = row.getCell(2).getStringCellValue();
		    		   
		    		   if("0".equals(valueString)){
		    			   cfReportData.setTextValue("");
		    		   }else if(valueString.trim().contains("不适用")){
		    			   cfReportData.setIsused("0");
		    			   cfReportData.setTextValue("nil");
		    		   }else{		    			   
		    			   cfReportData.setTextValue(valueString);
		    		   }
			       } catch (NumberFormatException e) {
			    	   e.printStackTrace();
			       }
		    	   cfReportData.setDesText(null);//描述性型
		    	   cfReportData.setFileText(null);//文件型
		       }
		       else if(dataType.equals("07")){//日期型
		    	   cfReportData.setOutItemType("07");
		    	   cfReportData.setReportItemValue(new BigDecimal(0).setScale(8,BigDecimal.ROUND_HALF_UP));
		    	   cfReportData.setReportItemWanValue(new BigDecimal(0).setScale(12,BigDecimal.ROUND_HALF_UP));
		    	   try {
		    		   if(valueString.trim().contains("不适用")){
			    		   cfReportData.setIsused("0");
			    		   cfReportData.setTextValue("nil");
			    	   }else{			    		   
			    		   cfReportData.setTextValue(sdf.format(valueString));
			    	   }
			       } catch (NumberFormatException e) {
			    	   e.printStackTrace();
			       }
		    	   cfReportData.setDesText(null);//描述性型
		    	   cfReportData.setFileText(null);//文件型
		       }else{
		    	   System.out.println("导入因子值类型有误！！！");
		       }
		       cfReportData.setSource("1");//源1：指标导入；源2：自动提数业务；源3：自动提数财务；源4：精算模板导入；源5：投资模板导入；源6：计算
		       cfReportData.setReportType(pld.getReportname());//报表类别
		       cfReportData.setComputeLevel("0");//计算级别
		       cfReportData.setReportItemValueSource("0");//指标值来源：1-初步计算数据手工修改；2-尾数自动调整
		       try {
		    	   cfReportData.setOperator(user.getUserCode());//（操作人）
			   } catch (Exception e) {
				throw new Exception("登录时间已过期，请重新登录！！！");
			   }
		       cfReportData.setOperDate(pld.getDate());//操作日期
		       cfReportData.setTemp(pld.getRemark());

		       //保存新数据
		       impcfReportDataDao.save(cfReportData);
		       //添加修改轨迹
		       if(dataType.equals("04")||dataType.equals("05")||dataType.equals("06")){		    	   
		    	   BigDecimal numvalue=(BigDecimal)hismap.get(valueString);//可能空指针异常
		    	   if(numvalue==null){
		    		   numvalue=new BigDecimal(0);
		    	   }
	    			ReportHistory history = new ReportHistory();
	    			history.setMonth(cfReportData.getMonth());
	    			history.setOper_date(cfReportData.getOperDate());
	    			history.setQuarter(cfReportData.getQuarter());
	    			history.setReport_code(cfReportData.getId().getOutItemCode());
	    			history.setReport_mvalue(numvalue);
	    			history.setReport_mvalue_after(cfReportData.getReportItemValue());
	    			history.setReport_name(cfReportData.getOutItemCodeName());
	    			history.setReport_rate(cfReportData.getReportRate());
	    			try {
	    				history.setUser_name(cfReportData.getOperator());
					} catch (Exception e) {
						throw new Exception("登录时间已过期，请重新登录！！！");
					}
	    			history.setReportid(reportId);
	    			impcfReportDatadao.save(history);
		       }else if(dataType.equals("01")||dataType.equals("07")){
		    	   String strvalue=(String)hismap.get(valueString);//可能空指针异常
		    	   if(strvalue==null){
		    		   strvalue="";
		    	   }
		    	    ReportHistory history = new ReportHistory();
		    		history.setMonth(cfReportData.getMonth());
		    		history.setOper_date(cfReportData.getOperDate());
		    		history.setQuarter(cfReportData.getQuarter());
		    		history.setReport_code(cfReportData.getId().getOutItemCode());
		    		history.setText_value(strvalue);
		    		history.setText_value_after(cfReportData.getTextValue());
		    		history.setReport_name(cfReportData.getOutItemCodeName());
		    		history.setReport_rate(cfReportData.getReportRate());
		    		history.setUser_name(cfReportData.getOperator());
		    		history.setReportid(reportId);
		    		impcfReportDatadao.save(history);
		       }else if(dataType.equals("03")||dataType.equals("02")){
		    	    System.out.println("描述性型和文件型暂无法保存修改轨迹。");
		       }
		       
		}
//		if(uploadInfoDTOs.size()!=0){
//			throw new UploadNumberFormatException(uploadInfoDTOs);
//		}
		
	}
	@Transactional
	public void saveDataA02(UserInfo user,UploadInfoDTO pld)throws UploadNumberFormatException,NullPointerException,Exception {
		String reportId=pld.getReportId();
		//删除对应报送号的数据
		System.out.println("删除行可扩展对应报送号的数据");
		alm_CfReportRowAddDescDAO.excute("delete  from alm_cfreportrowadddata where reportid='"+reportId+"'");
		System.out.println("删除完毕！！！！");
		//查询reportitemcodedesc表中的数据的位置
		String sql="select * from ALM_cfreportrowadddesc";
		List<?> rowlist2=alm_CfReportRowAddDescDAO.queryBysql(sql);
		
		//获取文件路径
		String path=pld.getFilePath()+"/"+us.getFileName();

		FileInputStream fis = null;
		XSSFWorkbook wb = null;
		XSSFSheet sheet = null;
		XSSFRow row = null;
		XSSFCell cell = null;
		String valueString = null;
		try {			
			fis = new FileInputStream(path);
			wb = new XSSFWorkbook(fis);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		for (int i = 0; i < rowlist2.size(); i++) {
			System.out.println("正在执行-行可扩展-第"+i+"行，还有"+(rowlist2.size()-i)+"行");
			Map<String, String> map= (Map<String, String>) rowlist2.get(i);
			//获取定位
			String tableCode=map.get("tablecode").toString();//表代码
			String itemCode=map.get("colcode").toString();//因子代码
			String sheetName=map.get("tablename").toString();//表名
			String masterKey=map.get("masterfactor").toString();//决定因子
			String cellNum=map.get("sheetcol").toString();//列号
			String rowNum=map.get("sheetrow").toString();//从哪一行开始读
			String dataType = map.get("coltype").toString();//数据类型
					
			//读取数据
			sheet = wb.getSheet(sheetName);
			if(sheet == null) continue;
			int startRow = Integer.parseInt(rowNum)-1;
			if(startRow > sheet.getLastRowNum()) continue;
			for(int j = startRow; j <= sheet.getLastRowNum(); j++){
				row = sheet.getRow(j);
				if(row == null) continue;
				cell = row.getCell(0);//决定因子
				if(cell == null) continue;
				String text = cell.getStringCellValue();
				if(text.startsWith(masterKey)){
					cell = row.getCell(Integer.parseInt(cellNum)-1);//单元格位置
					if(cell == null) continue;
					valueString = eu.getCellValue(cell);
					
					
					//保存
					//判断是否为空
					if("".equals(valueString)||"0".equals(valueString)||"0.0".equals(valueString)||"-".equals(valueString)||"--".equals(valueString)||"#REF!".equals(valueString)||"<g_idiv>".equals(valueString)||"#VALUE!".equals(valueString)){
						continue;
					}
					//实体类存值
					System.out.println("第"+(j)+"行，第"+(cellNum)+"列。");
					ALM_CfReportRowAddDataId cfReportRowAddDataId=new ALM_CfReportRowAddDataId();
					ALM_CfReportRowAddData cfReportRowAddData=new ALM_CfReportRowAddData();
					cfReportRowAddData.setTableCode(tableCode);//表代码
					cfReportRowAddDataId.setColCode(itemCode);//列代码
					cfReportRowAddDataId.setRowNo(String.valueOf(j));//行数
					cfReportRowAddData.setYearMonth(pld.getYearmonth());//会计与月度
					cfReportRowAddData.setQuarter(pld.getQuarter());//季度
					cfReportRowAddData.setReportRate(pld.getReporttype());//报送频度
					cfReportRowAddData.setId(cfReportRowAddDataId);
					cfReportRowAddData.setReportType(pld.getReportname());//报表类别
					cfReportRowAddData.setSource("1");//源1：指标导入；源2：自动提数业务；源3：自动提数财务；源4：精算模板导入；源5：投资模板导入；源6：计算
					 try {
						 cfReportRowAddData.setOperator2(user.getUserCode());//操作人
					   } catch (Exception e) {
						throw new Exception("登录时间已过期，请重新登录！！！");
					   }
					cfReportRowAddData.setOperDate2(pld.getDate());//操作日期
					cfReportRowAddData.setComCode(Config.getProperty("OrganCode"));
					cfReportRowAddDataId.setReportId(pld.getReportId());
										
					if(dataType.equals("06")){//百分比型
						try{
							String strValue = valueString;
							if("".equals(strValue.trim())||"--".equals(strValue.trim())||"-".equals(strValue.trim())||"#REF!".equals(strValue.trim())||"<g_idiv>".equals(strValue.trim())||"#VALUE!".equals(strValue.trim())){
								strValue="0";
							}
							cfReportRowAddData.setColType("06");
							new BigDecimal(strValue);
							cfReportRowAddData.setNumValue(new BigDecimal(strValue).setScale(8,BigDecimal.ROUND_HALF_UP));
							cfReportRowAddData.setWanValue(new BigDecimal("0").setScale(12,BigDecimal.ROUND_HALF_UP));
						}catch(NumberFormatException e){
							e.printStackTrace();
							//storeA02UploadInfoDTOs(row, strColtype, position);
						//	continue;
							throw new  RuntimeException("表代码:"+row.getCell(0).getStringCellValue()+"列代码:"+row.getCell(2).getStringCellValue()+"格式不对");
						}
						cfReportRowAddData.setTextValue("");
						cfReportRowAddData.setDesText("");
					}else if(dataType.equals("05")){//数量型
						try{
							String strValue = valueString;
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
							//storeA02UploadInfoDTOs(row, strColtype, position);
						//	continue;
							throw new  RuntimeException("表代码:"+row.getCell(0).getStringCellValue()+"列代码:"+row.getCell(2).getStringCellValue()+"格式不对");

						}
						cfReportRowAddData.setTextValue("");
						cfReportRowAddData.setDesText("");
					}else if(dataType.equals("04")){//数值型
						try{
							String strValue = valueString;
							if("".equals(strValue.trim())||"--".equals(strValue.trim())||"-".equals(strValue.trim())||"#REF!".equals(strValue.trim())||"<g_idiv>".equals(strValue.trim())||"#VALUE!".equals(strValue.trim())){
								strValue="0";
							}
							cfReportRowAddData.setColType("04");
							new BigDecimal(strValue);
							cfReportRowAddData.setNumValue(new BigDecimal(strValue).setScale(8,BigDecimal.ROUND_HALF_UP));
							cfReportRowAddData.setWanValue(new BigDecimal(strValue).divide(new BigDecimal("10000"), 12, RoundingMode.HALF_UP));
						}catch(NumberFormatException e){
							e.printStackTrace();
							//storeA02UploadInfoDTOs(row, strColtype, position);
						//	continue;
							throw new  RuntimeException("表代码:"+row.getCell(0).getStringCellValue()+"列代码:"+row.getCell(2).getStringCellValue()+"格式不对");

						}
						cfReportRowAddData.setTextValue("");
						cfReportRowAddData.setDesText("");		        	
					}else if(dataType.equals("02")){//描述型
						try {
							String strValue = valueString;
							cfReportRowAddData.setColType("02");
							cfReportRowAddData.setNumValue(new BigDecimal("0").setScale(4,BigDecimal.ROUND_HALF_UP));
							cfReportRowAddData.setWanValue(new BigDecimal("0").setScale(8,BigDecimal.ROUND_HALF_UP));
							cfReportRowAddData.setTextValue(strValue);
						} catch (NumberFormatException e) {
							e.printStackTrace();
							//storeA02UploadInfoDTOs(row, strColtype, position);
						//	continue;
							throw new  RuntimeException("表代码:"+row.getCell(0).getStringCellValue()+"列代码:"+row.getCell(2).getStringCellValue()+"格式不对");

						}
						cfReportRowAddData.setDesText("");		        	
					}else if(dataType.equals("01")){//短文本类型
						try {
							String strValue = valueString;							
							//指标代码
							String colCode=itemCode;
							//判断该指标内容是否需要做带竖线的转换
							/*if(rowMap.containsKey(colCode)){
								System.out.println("需要转换成带竖线的指标是：----------"+colCode);
								if(doubleMap.containsKey(strValue)){
									strValue = doubleMap.get(strValue);
									System.out.println("转换后的文本内容是：----"+strValue);
								} 							
							}	*/	
							
							cfReportRowAddData.setColType("01");
							cfReportRowAddData.setNumValue(new BigDecimal("0").setScale(4,BigDecimal.ROUND_HALF_UP));
							cfReportRowAddData.setWanValue(new BigDecimal("0").setScale(8,BigDecimal.ROUND_HALF_UP));
							cfReportRowAddData.setTextValue(strValue);
						} catch (NumberFormatException e) {
							e.printStackTrace();
							//storeA02UploadInfoDTOs(row, strColtype, position);
						//	continue;
							throw new  RuntimeException("表代码:"+row.getCell(0).getStringCellValue()+"列代码:"+row.getCell(2).getStringCellValue()+"格式不对");

						}
						cfReportRowAddData.setDesText("");		        	
					}else if(dataType.equals("07")){//日期型
						try {
							String strValue = sdf.format(row.getCell(j).getDateCellValue());
							cfReportRowAddData.setColType("07");
							cfReportRowAddData.setNumValue(new BigDecimal("0").setScale(8,BigDecimal.ROUND_HALF_UP));
							cfReportRowAddData.setWanValue(new BigDecimal("0").setScale(12,BigDecimal.ROUND_HALF_UP));
							cfReportRowAddData.setTextValue(strValue);
							cfReportRowAddData.setDesText("");		        	
						} catch (NumberFormatException e) {
							e.printStackTrace();
							//storeA02UploadInfoDTOs(row, strColtype, position);
						//	continue;
							throw new  RuntimeException("表代码:"+row.getCell(0).getStringCellValue()+"列代码:"+row.getCell(2).getStringCellValue()+"格式不对");

						}
					}else{
						System.out.println("导入因子值类型有误！！！");
					}
					
					//保存新数据
					alm_CfReportRowAddDataDAO.save(cfReportRowAddData);
					/*//添加修改轨迹
				       if(dataType.equals("04")||dataType.equals("05")||dataType.equals("06")){		    	   
				    	   BigDecimal numvalue=(BigDecimal)hismap.get(valueString);//可能空指针异常
				    	   if(numvalue==null){
				    		   numvalue=new BigDecimal(0);
				    	   }
				    		RowAddHistory history = new RowAddHistory();
				    		history.setOper_date(cfReportRowAddData.getOperDate2());
				    		history.setTable_code(cfReportRowAddData.getTableCode());
				    		history.setReportid(cfReportRowAddData.getId().getReportId());
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
				    	   String strvalue=(String)hismap.get(row.getCell(2).getStringCellValue());//可能空指针异常
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

					 */


				}
			}

		}


	}
	/**********************
	 * 获取带竖线内容的映射对应关系
	 * 如 AAA = 1|AAA	 * 
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
	@Transactional
	private void cfReportItemCodeHis(UploadInfoDTO pld,UserInfo user,String deptflag) {
		// TODO Auto-generated method stub
		String sqlhis="select * from cfreportdata where month='"+
	              pld.getYearmonth()+"' and quarter='"+
			      pld.getQuarter()+"' and reportrate='"+
	              pld.getReporttype()+"' and reporttype='"+
			      pld.getReportname()+"' and comcode='"+
	              pld.getCompanycode()+"' and reportId='"+
			      pld.getReportId()+"'";
		if("yes".equals(deptflag)){//分部门删除数据
			sqlhis=sqlhis+" and outitemcode in (select reportitemcode from cfreportitemcodedesc where reportitemtype like '%"+user.getDeptCode()+"%')";
		}
		System.out.println(sqlhis);
		List<?> hisresult=impcfReportDataDao.queryBysql(sqlhis);
		//将要覆盖的数据存放到hismap中，用于保存修改轨迹
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
		String lsql="delete from cfreportdata where month='"+pld.getYearmonth()+"' and quarter='"+pld.getQuarter()+"' and reportrate='"+
				pld.getReporttype()+"' and reporttype='"+
				pld.getReportname()+"' and outitemtype<>'02' and comcode='"+
			    pld.getCompanycode()+"' and reportId='"+pld.getReportId()+"'";
		if("yes".equals(deptflag)){//分部门删除数据
		    lsql=lsql+" and outitemcode in (select reportitemcode from cfreportitemcodedesc where reportitemtype like '%"+user.getDeptCode()+"%'"
		    		+ " union all select reportitemcode from rsys_cfreportitemcodedesc where reportitemtype like '%"+user.getDeptCode()+"%')";
		}
		try{
			dao.excute(lsql);
		}catch(Exception e){
			LOGGER.error("删除历史数据出错！sql="+lsql);
			throw new RuntimeException("删除历史数据出错！");
		}
	}
	@Transactional
	public void getCfReportItemCodeDesc(UploadInfoDTO pld,UserInfo user,String deptflag) {
		// TODO Auto-generated method stub
		String sql="select cf.reportitemcode reportitemcode, cf.outitemtype,b.decimals " + 
				"from cfreportitemcodedesc cf " +
				"  left join cfreportelement b\n" + 
				"    on cf.reportitemcode = b.portitemcode\n" +
				"where outitemtype<>'02' and outitemtype<>'03'";
		String comtype = Config.getProperty("CompanyType");
		String reporttype = pld.getReportname();
		if("L".equals(comtype)){
			sql = sql + " and LIFEINSURANCE1='1' and LIFEINSURANCE2='1' and LIFEINSURANCE3='1' and LIFEINSURANCE4='1' and reporttype='"+reporttype+"'";
		}else if("P".equals(comtype)){
			sql = sql + " and PROPERTYINSURANCE1='1' and PROPERTYINSURANCE2='1' and PROPERTYINSURANCE3='1' and reporttype='"+reporttype+"'";
		}else if("R".equals(comtype)){
			sql = sql + " and REINSURANCE='1'";
		}
		if("yes".equals(deptflag)){//分部门删除数据
		    sql=sql+" and reportitemtype like '%"+user.getDeptCode()+"%'";
		}
		
		sql = sql +" union all ";
		sql = sql +" select rs.reportitemcode reportitemcode, rs.outitemtype,b.decimals from rsys_cfreportitemcodedesc rs " + 
				"  left join cfreportelement b\n" + 
				"    on rs.reportitemcode = b.portitemcode\n" +
				"where outitemtype<>'02' and outitemtype<>'03' ";
		if("L".equals(comtype)){
			sql = sql + " and rs.LIFEINSURANCE1='1' and rs.LIFEINSURANCE2='1' and rs.LIFEINSURANCE3='1' and rs.LIFEINSURANCE4='1' and rs.reporttype='"+reporttype+"'";
		}else if("P".equals(comtype)){
			sql = sql + " and rs.PROPERTYINSURANCE1='1' and rs.PROPERTYINSURANCE2='1' and rs.PROPERTYINSURANCE3='1' and rs.reporttype='"+reporttype+"'";
		}else if("R".equals(comtype)){
			sql = sql + " and rs.REINSURANCE='1'";
		}
		if("yes".equals(deptflag)){//分部门删除数据
		    sql=sql+" and rs.reportitemtype like '%"+user.getDeptCode()+"%'";
		}
		sql = sql + " order by reportItemCode";

		
		List<?> result=impcfReportItemCodeDescDao.queryBysql(sql);
		map.clear();
	    for(int i=0;i<result.size();i++){
	    	Map<?,?> m=(Map<?,?>)result.get(i);
			System.out.println(m.get("reportItemCode"));
			map.put((String) m.get("reportItemCode"), m.get("outItemType")+"_"+m.get("decimals"));
		}
	}
	
	/**
	 * A01 存储数据格式转换异常结果集
	 * @param row
	 * @param strOutItemType
	 * @param result
	 */
	private void storeA01UploadInfoDTOs(Row row, String strOutItemType,String result){
		
		/*UploadInfoDTO uploadInfoDTO = new UploadInfoDTO();
		uploadInfoDTO.setReportcode(row.getCell(0).getStringCellValue());//因子代码
		uploadInfoDTO.setReportname(row.getCell(1).getStringCellValue());//因子名称
		uploadInfoDTO.setFileName(row.getCell(4).getStringCellValue());//表名称
*/		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("reportcode", row.getCell(0).getStringCellValue());
		map.put("reportname", row.getCell(1).getStringCellValue());
		map.put("filename", row.getCell(4).getStringCellValue());
		map.put("result", result);
		
		String remark = "";
		if(strOutItemType.equals("01")){
			remark = "数据类型不为多文本型";
		} else if(strOutItemType.equals("02")){
			remark = "数据类型不为描述型";
		} else if(strOutItemType.equals("03")){
			remark = "数据类型不为文件型";
		} else if(strOutItemType.equals("04")){
			remark = "数据类型不为数值型";
		} else if(strOutItemType.equals("05")){
			remark = "数据类型不为数量型";
		} else if(strOutItemType.equals("06")){
			remark = "数据类型不为百分比型";
		} else if(strOutItemType.equals("07")){
			remark = "数据类型不为日期型";
		} else {
			remark = "未知类型：导入因子值类型有误！";//错误信息
		}
		map.put("remark", remark);
		
		this.uploadInfoDTOs.add(map);
	}
	public void downloadPath(UploadInfoDTO pld, String dept, String comcode, String newFilePath, String param, String deptflag) {
		// TODO Auto-generated method stub
		FileInputStream fis = null;
		XSSFWorkbook wb = null;
		
		try {
			fis = new FileInputStream(newFilePath);
			wb = new XSSFWorkbook(fis);
			
			//获取部门对应的sheet
			Map<String, String>  sheetNames = qrySheetNames(param,dept,deptflag);
			List<String> templist = new ArrayList<String>();
			
			//遍历所有sheet，统计不属于map中key的sheet名
			for(int i = 0; i < wb.getNumberOfSheets(); i++){
				String sheetName = wb.getSheetName(i);
				if(sheetName.equals("A01#") ||sheetName.equals("目录")){
					continue;
				}
				if(!sheetNames.containsKey(sheetName)){
					templist.add(sheetName);
				}
			}
			if(param.equals("1-data")||param.equals("2-data")){//数据模板下载
				if(param.equals("1-data")){
					pld.setReportname("0");
				}
				//固定因子数据
				Map<String, Object> map = new HashMap<String, Object>();
				map = qryDataByDeptNo(pld.getYearmonth(), pld.getQuarter(), pld.getReporttype(), pld.getReportname(), comcode, dept, deptflag);
				
				//行可扩展数据
				List<Map<String, Object>> maplist = new ArrayList<Map<String,Object>>();
				maplist = qryAllRowData(pld.getYearmonth(), pld.getQuarter(),pld.getReporttype(), pld.getReportname(),pld.getReportId());
				Map<String, Object> tblmap = maplist.get(0);
				Map<String, Object> rowmap = maplist.get(1);
				
				
				//获取数值型指标的类型和精度和
				//List<Map<String, Object>> itemAcclist = new ArrayList<Map<String,Object>>();
				/*List<?> itemAcclist=new ArrayList();
				itemAcclist = qryItemAccuracy(pld.getReportname());
				Map<String, Object> itemacc = itemAcclist.get(0);
				Map<String, Object> itemtype = itemAcclist.get(1);*/
				
				for(int i = 0; i < wb.getNumberOfSheets(); i++){
					String sheetName = wb.getSheetName(i);
					if(sheetName.equals("A01#") || sheetName.equals("Database") || templist.contains(sheetName)){
						if(sheetName.equals("Database")){
							wb.setSheetHidden(i, true);
						}
						continue;
					}
					
					try {
						dealItemCodeData(wb.getSheetAt(i), map);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						LOGGER.error("固定因子数据处理异常", e);
					}
					try{						
						dealRowAddData(wb.getSheetAt(i), tblmap, rowmap);
						
					}catch(Exception e){
						LOGGER.error("行可扩展数据处理异常", e);
					}
				}
			}
			XSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
			//wb删除sheet
			for (String str : templist) {
				wb.removeSheetAt(wb.getSheetIndex(str));
			}
			FileOutputStream fos = new FileOutputStream(newFilePath);
			wb.write(fos);
			fos.flush();
			fos.close();
			System.gc();
	    }catch (IOException e) {
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
	/*
	 * 行可扩展数据查询
	 */
        public List<Map<String, Object>> qryAllRowData(String year, String quarter, String reportrate, String reporttype,String reportId){
		
		//获取需要映射的指标
		//Map<String, String> rowMapCode = getMapCode();
		//文本对应关系 如AAA 1|AAA集合
		//Map<String, String> doubleMaps = getMapInfo();

		String qry = "select tablecode, colcode, rowno, coltype, numvalue, wanvalue, textvalue from alm_cfreportrowadddata t"
				+ " where reportId= '" + reportId
				+ "' and reportrate = '" + reportrate
				+ "' and reporttype = '" + reporttype
				+ "' group by rowno,tablecode, colcode, coltype, numvalue, wanvalue, textvalue order by colcode";
		
		String qryrowno = "select tablecode,max(to_number(rowno)) maxrowno from alm_cfreportrowadddata "
				+ " where reportId= '" + reportId
				+ "' and reportrate = '" +  reportrate
				+ "' and reporttype = '" + reporttype
				+ "' group by tablecode order by tablecode";
		List<Map<String, Object>> maplist = new ArrayList<Map<String,Object>>();
		Map<String, Object> rowmap = new HashMap<String, Object>();
		Map<String, Object> tblmap = new HashMap<String, Object>();
		try{
			List<?> list = alm_CfReportRowAddDataDAO.queryBySQL(qry);
			List<?> rowlist = alm_CfReportRowAddDataDAO.queryBySQL(qryrowno);
			
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
								/*//是否包含需要转换的带竖线的指标编码
								if(rowMapCode.containsKey(_colcode)){
									//如果需要做竖线转换，则转换成不带竖线的文本内容展示在页面上
									if(doubleMaps.containsKey(txtString)){
									rowmap.put(_key, doubleMaps.get(txtString).toString());
									}
								}else{*/
									rowmap.put(_key, temp.get("textvalue").toString());
								/*}	*/							
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
	 * 查询部门对应的报表名称
	 * @param comtype 
	 * @param dept 部门编号
	 * @return
	 */
	public Map<String, String> qrySheetNames(String param, String dept, String deptflag){
		String qry = "";
		if("no".equals(deptflag)){
			if("all".equals(param)){//完整模板下载
				qry = "select reportcode,remark from cfreportdesc where remark is not null";
			}else if("1".equals(param)||"1-data".equals(param)){//法人机构模板下载
				qry = "select reportcode,remark from cfreportdesc where remark is not null ";
			}else if("2".equals(param)||"2-data".equals(param)){//分支机构模板下载
				qry = "select reportcode,remark from cfreportdesc where remark is not null and temp1 like '%" + Config.getProperty("CompanyType") + "%' and temp2='2'";
			}else if("3".equals(param)){//再保险机构模板下载
				qry = "select reportcode,remark from cfreportdesc where remark is not null and temp1 like '%" + Config.getProperty("CompanyType") + "%'";
			}			
		}else if("yes".equals(deptflag)){
			if("all".equals(param)){//完整模板下载(分部门)
				qry = "select reportcode,remark from cfreportdesc where remark is not null and department like '%" + dept + "%'";
			}else if("1".equals(param)||"1-data".equals(param)){//法人机构模板下载(分部门)
				qry = "select reportcode,remark from cfreportdesc where remark is not null and temp1 like '%" + Config.getProperty("CompanyType") + "%' and temp2='1' and department like '%" + dept + "%'";
			}else if("2".equals(param)||"2-data".equals(param)){//分支机构模板下载(分部门)
				qry = "select reportcode,remark from cfreportdesc where remark is not null and temp1 like '%" + Config.getProperty("CompanyType") + "%' and temp2='2' and department like '%" + dept + "%'";
			}else if("3".equals(param)){//再保险机构模板下载(分部门)
				qry = "select reportcode,remark from cfreportdesc where remark is not null and temp1 like '%" + Config.getProperty("CompanyType") + "%' and department like '%" + dept + "%'";
			}
		}
		@SuppressWarnings("unchecked")
		List<Map<String, String>> list = (List<Map<String, String>>) mapDao.queryBysql(qry);
		Map<String, String> map = new HashMap<String, String>();
		if(list != null && list.size() > 0){
			for (int i=0;i<list.size();i++) {
				Map<String, String> _map = (Map<String, String>) list.get(i);
				map.put(_map.get("remark").toString(), _map.get("remark").toString());
			}
		}		
		return map;
	}
	/**
	 * 查询固定因子数据，结果集封装成Map
	 * @param year
	 * @param quarter
	 * @param reportCode 报表编号
	 * @param deptNo 部门编号
	 * @return
	 */
	public Map<String, Object> qryDataByDeptNo(String year, String quarter, String reportrate, String reporttype, String comcode, String dept, String deptflag) {
		// TODO Auto-generated method stub
		//获取固定因子数据
		String qry = "select outitemcode,outitemtype, reportitemvalue,textvalue,isused  from ALM_cfreportdata where month= '" + year 
				+ "' and quarter = '" + quarter + "' and reportrate='" + reportrate 
				+ "' and reporttype='" + reporttype +"'";
		//分部门下载数据的时候，需要根据不同部门显示数据。 完整下载的时候，不需要区分部门。
		if("yes".equals(deptflag)){
			qry = qry + "and outitemcode in ("
					+ " select reportitemcode from cfreportitemcodedesc where "
					+ " reporttype ='"+reporttype+"' and reportitemtype like '%"+dept+"%' "
					+ " union all select reportitemcode from rsys_cfreportitemcodedesc c where  "
					+ " reporttype ='"+reporttype+"' and reportitemtype like '%"+dept+"%') ";
			qry = qry + "' and comcode='" + comcode + "'";
		}

		Map<String, Object> map = new HashMap<String, Object>();
		try{
			System.out.println(qry);
			List<?> list = mapDao.queryBysql(qry);

			if(list !=null && list.size() > 0){
				for (Object object : list) {
					//一个_map对象对应数据库中的一条记录
					@SuppressWarnings("unchecked")
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
								if (_map.get("isused")!=null) {
									map.put(code, String.valueOf("不适用"));
								}else {
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
	@SuppressWarnings("unchecked")
	public List<?> qryItemAccuracy(String reporttype){
		/*String sql = "select * from (select t.portitemcode code, i.outitemtype itemtype, t.decimals,i.sheetrow,i.sheetcol from cfreportelement t, cfreportitemcodedesc i "
				+ "where t.portitemcode = i.outitemcode and i.reporttype ='"+reporttype+"' union all "
				+ " select rs.reportitemcode code, rs.outitemtype itemtype, rs.decimals from  rsys_cfreportitemcodedesc rs "
				+ " where  rs.reporttype ='"+reporttype+"')";*/
		String sql = "select * from (select i.reportitemcode code, i.outitemtype itemtype,i.sheetrow,i.sheetcol,i.outitemnote from  ALM_cfreportitemcodedesc i order by i.outitemnote)";
		List<?> list = mapDao.queryBysql(sql);
		/*List<Map<String,Object>> maplist = new ArrayList<Map<String,Object>>();
		Map<String, Object> _map = new HashMap<String, Object>();
		Map<String, Object> mapitemacc = new HashMap<String, Object>();
		Map<String, Object> mapitemtype = new HashMap<String, Object>();
		Map<String, Object> mapitemrow = new HashMap<String, Object>();
		Map<String, Object> mapitemcol = new HashMap<String, Object>();
		for (Object obj : list) {
			_map = (Map<String, Object>) obj;
			mapitemacc.put(_map.get("code").toString(), _map.get("decimals"));
			mapitemtype.put(_map.get("code").toString(), _map.get("itemtype"));
			mapitemrow.put(_map.get("code").toString(), _map.get("sheetrow"));
			mapitemcol.put(_map.get("code").toString(), _map.get("sheetcol"));
		}
		maplist.add(mapitemacc);
		maplist.add(mapitemtype);
		maplist.add(mapitemrow);
		maplist.add(mapitemcol);*/
		return list;
	}
	/**
	 * 固定因子数据填充到模板
	 * @param sheet
	 * @param map  数据表查询
	 * @param itemAcclist 描述表查询
	 */
	public void dealItemCodeData(XSSFSheet sheet, Map<String, Object> map) throws Exception{
		XSSFRow row = null;
		XSSFCell cell = null;
		XSSFComment comment = null;
		String strItemCode = "";//因子代码
		String strFlag = "";//反填模板的标签
		
		if(map != null && map.size() > 0){
			System.out.println("=========sheet:" + sheet.getSheetName() + "固定因子数据处理Go=======");
			//查询描述表获取位置
			String sql = "select * from (select i.reportitemcode code, i.outitemtype itemtype,i.sheetrow,i.sheetcol,i.outitemnote from  ALM_cfreportitemcodedesc i where i.outitemnote='"+ 
						sheet.getSheetName()+"')";
			System.out.println("描述表查询"+sql);
			List<?> list = alm_CfReportRowAddDescDAO.queryBysql(sql);				
			for(int i = 0; i < list.size(); i++){	
				Map<String, Object> mapitem=(Map<String, Object>) list.get(i);
				String rowloa=mapitem.get("sheetrow").toString();
				String colloa=mapitem.get("sheetcol").toString();
				System.out.println("行数===="+rowloa+"列数====="+colloa);
				try {
					row = sheet.getRow(Integer.valueOf(rowloa)-1);
					cell = row.getCell(Integer.valueOf(colloa)-1);
				} catch (Exception e) {
					e.printStackTrace();
				}
			
				//获取代码
				strItemCode=mapitem.get("code").toString();
				//获取类型
				String itemtype = mapitem.get("itemtype").toString();
				if(row == null){
					continue;
				}
				if(!strItemCode.equals("")){
					if(map.get(strItemCode) != null){
						String strvalue = map.get(strItemCode).toString();
						if(itemtype != null){
							BigDecimal bd;
							if(itemtype.equals("06") || itemtype.equals("05") || itemtype.equals("04") || itemtype.equals("03") || itemtype.equals("02")){
								if (strvalue.equals("不适用")) {
									cell.setCellValue(strvalue);
									cell.getCellStyle().setWrapText(true);
								}else {
									//int _itemacc = Integer.parseInt(itemacc.get(strItemCode) == null ? "0" : itemacc.get(strItemCode).toString());
									bd = new BigDecimal(strvalue);
									System.out.println(bd);
									cell.setCellValue(bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
									cell.getCellStyle().setWrapText(true);
									cell.getCellStyle().setAlignment(XSSFCellStyle.ALIGN_RIGHT);
								}
							}else{
								cell.setCellValue(strvalue);
								cell.getCellStyle().setWrapText(true);
							}	
						}else{
							System.out.println("指标" + strItemCode + "在cfreportelement表或表外数据描述表中未定义！");
							LOGGER.info("==========指标" + strItemCode + "在cfreportelement表或表外数据描述表中未定义！==========");
						}
					}else{
						System.out.println("指标" + strItemCode + "未定义或不属于该部门！");
						LOGGER.info("==========指标" + strItemCode + "未定义或不属于该部门！==========");
					}
					}
				}
			}
			System.out.println("=========sheet:" + sheet.getSheetName() + "固定因子数据处理End=======");
		
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
	public void dealRowAddData(XSSFSheet sheet , Map<String, Object> tblmap, Map<String, Object> rowmap) throws Exception{
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
		
		//获取数据位置
		String sql="select c.tablecode,c.tablename,c.colcode,c.colname,c.coltype,c.outitemnote,c.sheetcol,d.rowno from ALM_cfreportrowadddesc c,alm_cfreportrowadddata d where c.colcode=d.colcode and c.outitemnote='"+ 
						sheet.getSheetName()+"'";
		List<?> listItem = alm_CfReportRowAddDataDAO.queryBySQL(sql);
		
		if(listItem != null && listItem.size() > 0){
			System.out.println("=========sheet:" + sheet.getSheetName() + "行可扩展数据处理Go=======");			
			for(int i = 0; i < listItem.size(); i++){	
				Map<String, Object> mapitem=(Map<String, Object>) listItem.get(i);
				String rowloa=mapitem.get("rowno").toString();
				String colloa=mapitem.get("sheetcol").toString();
				try {
					row = sheet.getRow(Integer.valueOf(rowloa));
					cell = row.getCell(Integer.valueOf(colloa)-1);
					System.out.println("行数===="+(Integer.valueOf(rowloa)+1)+"列数====="+colloa);
				} catch (Exception e) {
					e.printStackTrace();
				}			
				//获取代码
				strItemCode=mapitem.get("colcode").toString()+"_"+rowloa;
				//获取类型
				String itemtype = mapitem.get("coltype").toString();
				if(row == null){
					continue;
				}
				if(!strItemCode.equals("")){
					if(rowmap.get(strItemCode) != null){
						String strvalue = rowmap.get(strItemCode).toString();
						if(itemtype != null){
							BigDecimal bd;
							if(itemtype.equals("06") || itemtype.equals("05") || itemtype.equals("04") || itemtype.equals("03") || itemtype.equals("02")){
								if (strvalue.equals("不适用")) {
									cell.setCellValue(strvalue);
									cell.getCellStyle().setWrapText(true);
								}else {
									//int _itemacc = Integer.parseInt(itemacc.get(strItemCode) == null ? "0" : itemacc.get(strItemCode).toString());
									bd = new BigDecimal(strvalue);
									System.out.println(bd);
									cell.setCellValue(bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
									cell.getCellStyle().setWrapText(true);
									cell.getCellStyle().setAlignment(XSSFCellStyle.ALIGN_RIGHT);
								}
							}else{
								cell.setCellValue(strvalue);
								cell.getCellStyle().setWrapText(true);
							}	
						}else{
							System.out.println("指标" + strItemCode + "在cfreportelement表或表外数据描述表中未定义！");
							LOGGER.info("==========指标" + strItemCode + "在cfreportelement表或表外数据描述表中未定义！==========");
						}
					}else{
						System.out.println("指标" + strItemCode + "未定义或不属于该部门！");
						LOGGER.info("==========指标" + strItemCode + "未定义或不属于该部门！==========");
					}
					}
				}
			}
			System.out.println("=========sheet:" + sheet.getSheetName() + "行可扩展数据处理End=======");
		
		
		
		
		
	}
	
	
	
	
	private void validateNumberFormat(List<Row> list, UploadInfoDTO pld) {
		for (int i = 0; i < list.size(); i++) {
			Row row = list.get(i);
			if (row.getCell(0) == null) {
				System.out.println("空单元格");
				continue;
			}
			String strTemp = (String) map.get(row.getCell(0)
					.getStringCellValue());// 可能空指针异常
			
			if (strTemp == null) {
				System.out.println("指标描述表未查到该因子代码");
				continue;
			}
			// 输出数据类型和值
//			System.out.println("---------数据格式校验1---------"+row.getCell(3).getStringCellValue());
//			System.out.println("---------数据格式校验2----------"+ExcelUtil.getCellValue(row.getCell(2)));
			System.out.println("---------数据格式校验2----------"+row.getCell(0).getStringCellValue());
			
//			if(row.getCell(0).getStringCellValue().equals("3320212_1_00032")){
//				System.out.println(row.getCell(0).getStringCellValue());
//			}
			
			String temp[] = strTemp.split("_",-1);
			String strOutItemType = temp[0];
			String decimalsStr = temp[1];
			int decimals = Integer.parseInt(StringUtils.isEmpty(decimalsStr) ||"null".equals(decimalsStr)? "0": decimalsStr);//精度
			int decimalsWan = decimals+4;
			String cellValue = ExcelUtil.getCellValue(row.getCell(2));
			if (strOutItemType.equals("06")) {// 百分比型
				try {
					if ("".equals(cellValue)
							|| "--".equals(cellValue)
							|| "-".equals(cellValue)
							|| RiskUploadService.STR.equals(cellValue)) {
						System.out.println("无值可取！！！");
					} else {
						new BigDecimal(cellValue)
						.setScale(decimals, BigDecimal.ROUND_HALF_UP);
					}
				} catch (NumberFormatException e) {
					// e.printStackTrace();
					storeA01UploadInfoDTOs(row, strOutItemType,cellValue);
					continue;
				}
			} else if (strOutItemType.equals("05")) {// 数量型
				try {
					if ("".equals(cellValue)
							|| RiskUploadService.STR.equals(cellValue)
							|| "--".equals(cellValue)
							|| "-".equals(cellValue)) {
						System.out.println("无值可取！！！");
					} else {
						new BigDecimal(cellValue)
								.setScale(decimals, BigDecimal.ROUND_HALF_UP);
						new BigDecimal(cellValue).divide(
								new BigDecimal("10000"), decimalsWan,
								RoundingMode.HALF_UP);
					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
					storeA01UploadInfoDTOs(row, strOutItemType,cellValue);
					continue;
				}
			} else if (strOutItemType.equals("04")) {// 数值型
				try {
					if ("".equals(cellValue)
							|| RiskUploadService.STR.equals(cellValue)
							|| "--".equals(cellValue)
							|| "--".equals(cellValue)) {
						System.out.println("无值可取！！！");
					} else {
						new BigDecimal(cellValue)
								.setScale(decimals, BigDecimal.ROUND_HALF_UP);
						new BigDecimal(cellValue).divide(
								new BigDecimal("10000"), decimalsWan,
								RoundingMode.HALF_UP);
					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
					storeA01UploadInfoDTOs(row, strOutItemType,cellValue);
					continue;
				}
			} else if (strOutItemType.equals("03")) {// 文件型
				
			} else if (strOutItemType.equals("02")) {
				
			} else if (strOutItemType.equals("01")) {// 短文本类型
				row.getCell(2).setCellType(1);// 设置单元格格式为文本型
				try {
					row.getCell(2).getStringCellValue();
				} catch (NumberFormatException e) {
					e.printStackTrace();
					storeA01UploadInfoDTOs(row, strOutItemType,cellValue);
					continue;
				}
			} else if (strOutItemType.equals("07")) {// 日期型
				try {
					sdf.format(row.getCell(2)
							.getDateCellValue());
				} catch (NumberFormatException e) {
					e.printStackTrace();
					storeA01UploadInfoDTOs(row, strOutItemType,cellValue);
					continue;
				}
			} else {
				System.out.println("导入因子值类型有误！！！");
			}
		}
	}
	private void validateDecimals(List<Row> list, UploadInfoDTO pld) {
		for (int i = 0; i < list.size(); i++) {
			Row row = list.get(i);
			if (row.getCell(0) == null) {
				System.out.println("空单元格");
				continue;
			}
			String strTemp = (String) map.get(row.getCell(0)
					.getStringCellValue());// 可能空指针异常
			if (strTemp == null) {
				System.out.println("指标描述表未查到该因子代码");
				continue;
			}
			/*if(row.getCell(0).getStringCellValue().equals("3320111_1_00022")){
				System.out.println("");
			}*/
			// 输出数据类型和值
			System.out.println("---------精度校验1----------"+row.getCell(3).getStringCellValue());
			System.out.println("---------精度校验2----------"+ExcelUtil.getCellValue(row.getCell(2)));
			
			String temp[] = strTemp.split("_",-1);
			String strOutItemType = temp[0];
			String decimalsStr = temp[1];
			int decimals = Integer.parseInt(StringUtils.isEmpty(decimalsStr) || "null".equals(decimalsStr)? "0": decimalsStr);//精度
			String cellValue = ExcelUtil.getCellValue(row.getCell(2));
			// 百分比型 ，数量型  ，数值型
			if (strOutItemType.equals("04")||strOutItemType.equals("05")||strOutItemType.equals("06")) {
				if ("".equals(cellValue)
						|| "--".equals(cellValue)
								|| "-".equals(cellValue)
								|| RiskUploadService.STR.equals(cellValue)) {
					System.out.println("无值可取！！！");
				} else {
					BigDecimal bg = new BigDecimal(cellValue);
					BigDecimal bdNew = new BigDecimal(String.format("%."+decimals+"f", bg));
					if(bg.compareTo(bdNew)!=0){
						storeDecimasUploadInfoDTOs(row,decimals,cellValue);
						continue;
					}
				}
			}
		}
	}
	private void storeDecimasUploadInfoDTOs(Row row, int decimals,String result) {
		/*UploadInfoDTO uploadInfoDTO = new UploadInfoDTO();
		uploadInfoDTO.setReportcode(row.getCell(0).getStringCellValue());// 因子代码
		uploadInfoDTO.setReportname(row.getCell(1).getStringCellValue());// 因子名称
		uploadInfoDTO.setFileName(row.getCell(4).getStringCellValue());// 表名称
		uploadInfoDTO.setRemark("精度不为"+ decimals +"位");*/
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("reportcode", row.getCell(0).getStringCellValue());
		map.put("reportname", row.getCell(1).getStringCellValue());
		map.put("filename", row.getCell(4).getStringCellValue());
		map.put("remark", "精度不为"+ decimals +"位");
		map.put("result", result);
		this.uploadInfoDTOs.add(map);
	}
}