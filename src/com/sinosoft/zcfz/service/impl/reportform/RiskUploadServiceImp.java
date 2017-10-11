package com.sinosoft.zcfz.service.impl.reportform;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fr.third.org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator.CellValue;
import com.sinosoft.common.Dao;
import com.sinosoft.dao.CfmapDao;
import com.sinosoft.zcfz.dao.reportdataimport.CfReportDataDAO;
import com.sinosoft.zcfz.dao.reportdataimport.CfReportItemCodeDescDAO;
import com.sinosoft.zcfz.dao.reportdatamamage.CfReportDataDao;
import com.sinosoft.zcfz.dto.reportdataimport.UploadInfoDTO;
import com.sinosoft.entity.CfReportData;
import com.sinosoft.entity.CfReportDataId;
import com.sinosoft.entity.Cfmap;
import com.sinosoft.entity.ReportHistory;
import com.sinosoft.entity.UserInfo;
import com.sinosoft.zcfz.service.impl.reportformcompute.PreComputeImp;
import com.sinosoft.zcfz.service.reportform.RiskUploadService;
import com.sinosoft.zcfz.service.riskdataimport.UploadDecimalsException;
import com.sinosoft.util.Config;
import com.sinosoft.util.ExcelUtil;
import com.sinosoft.util.UploadNumberFormatException;
import com.sinosoft.util.UploaderServlet;

@Service
public class RiskUploadServiceImp implements RiskUploadService{
	private SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
	@Resource
	private ExcelUtil eu;
	@Resource
	private UploaderServlet us;
	@Resource 
	private CfmapDao mapDao;
	@Resource
	private CfReportDataDAO impcfReportDataDao;
	@Resource
	private Dao dao;
	private static final Logger LOGGER = LoggerFactory.getLogger(PreComputeImp.class);
	private Map<String, Object> hismap=new HashMap<String, Object>();
	@Resource
	private CfReportItemCodeDescDAO impcfReportItemCodeDescDao;
	private Map<String, String> map=new HashMap<String, String>();
	private List<Map<String, Object>> uploadInfoDTOs = new ArrayList<Map<String, Object>>();//存放错误结果集
	@Resource
	private CfReportDataDao impcfReportDatadao; 
	@Transactional
	public void saveData(UploadInfoDTO pld,UserInfo user,String deptflag) throws UploadNumberFormatException,IOException,Exception{
		uploadInfoDTOs.clear();
		eu.setExcelPath(pld.getFilePath()+"/"+us.getFileName());
		/*String filename = us.getFileName();
		if(filename.contains("财产险法人机构")){//1.财产险法人机构表样
			
		}else if(filename.contains("财产险分支机构")){//2.财产险分支机构表样
			
		}else if(filename.contains("人身险法人机构")){//3.人身险法人机构表样
			
		}else if(filename.contains("人身险分支机构")){//4.人身险分支机构表样
			
		}else if(filename.contains("再保险")){//5.再保险表样
			
		}else if(filename.contains("所有")){//6.所有公司类型的表样
			
		}*/
		eu.setStartReadPos(1);//第一行开始
		eu.setEndReadPos(0);//最后一行结束
		eu.setSelectedSheetName("A01#");
		List<Row> listA01=eu.readExcel();
		saveDataA01(listA01,pld,user,deptflag);
		listA01.removeAll(listA01);
	}
	@Transactional
	public void saveDataA01(List<Row> list,UploadInfoDTO pld,UserInfo user,String deptflag)throws UploadNumberFormatException,NullPointerException,Exception {
				
		cfReportItemCodeHis(pld, user,deptflag);
		getCfReportItemCodeDesc(pld, user,deptflag);
		
		validateNumberFormat(list, pld);//校验数据格式
		if (uploadInfoDTOs.size() != 0) {
			throw new UploadNumberFormatException(uploadInfoDTOs);
		}
		/*validateDecimals(list, pld);//校验精度
		if (uploadInfoDTOs.size() != 0) {
			throw new UploadDecimalsException(uploadInfoDTOs);
		}*/
		
		
		int k=0;
		String reportId=pld.getReportId();
		for(int i=0;i<list.size();i++){
		       Row row=list.get(i);
		       System.out.println("listsize="+list.size()+"    row.getcurrentrow()="+(i+1));
		       System.out.println("row.getLastCellNum()="+row.getLastCellNum());
		       if(row.getCell(0)==null){
		    	   System.out.println("空单元格");
		    	   continue;
		       }
		       String strTemp=(String)map.get(row.getCell(0).getStringCellValue());//可能空指针异常 
		       if(strTemp==null){
		    	   System.out.println("指标描述表未查到该因子代码");
		    	   continue;
		       }
		      
		       CfReportDataId cfReportDataId=new CfReportDataId();
		       CfReportData cfReportData=new CfReportData();
		       cfReportDataId.setOutItemCode(row.getCell(0).getStringCellValue());//因子代码
		       
		       cfReportDataId.setReportId(reportId);
		       cfReportData.setMonth(pld.getYearmonth());//月度
		       cfReportData.setQuarter(pld.getQuarter());//季度
		       cfReportData.setReportRate(pld.getReporttype());//报送频度
		       cfReportData.setId(cfReportDataId);
		       cfReportData.setComCode(pld.getCompanycode());//机构代码
		       cfReportData.setOutItemCodeName(row.getCell(1).getStringCellValue());//因子名称
		       cfReportData.setReportItemCode(row.getCell(0).getStringCellValue());//内部码
		       
		       //输出数据类型和值
		       System.out.println(row.getCell(3).getStringCellValue());
		       System.out.println(ExcelUtil.getCellValue(row.getCell(2)));

		       String temp[]=strTemp.split("_");
		       String strOutItemType=temp[0];
		       System.out.println("数据类型-----"+strOutItemType);
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
			    	   }else{
			    		   cfReportData.setReportItemValue(new BigDecimal(ExcelUtil.getCellValue(row.getCell(2))).setScale(8,BigDecimal.ROUND_HALF_UP));
			    		   cfReportData.setReportItemWanValue(new BigDecimal(0).setScale(12,BigDecimal.ROUND_HALF_UP));
			    	   }
			       } catch (NumberFormatException e) {
			    	   //e.printStackTrace();
			       }
		    	   cfReportData.setOutItemType("06");
		    	   cfReportData.setTextValue("");
		    	   cfReportData.setDesText(null);//描述性型
		    	   cfReportData.setFileText(null);//文件型
		       }else if(strOutItemType.equals("05")){//数量型
		    	  
		    	   try {
		    		  String outItemCode=ExcelUtil.getCellValue(row.getCell(0)).trim();
		    		  if (outItemCode.equals("3320111_1_00008")||
								outItemCode.equals("3320111_1_00011")||outItemCode.equals("3320111_1_00014")||
								outItemCode.equals("3320111_1_00022")||outItemCode.equals("3320111_1_00049")||
								outItemCode.equals("3320212_1_00004")||outItemCode.equals("3320212_1_00006")||
								outItemCode.equals("3320212_1_00015")||outItemCode.equals("3320212_1_00018")||
								outItemCode.equals("3320212_1_00023")||outItemCode.equals("3320311_2_00006")||
								outItemCode.equals("3320311_2_00015")||outItemCode.equals("3320311_2_00018")||
								outItemCode.equals("3320311_2_00030")||outItemCode.equals("3320311_2_00037")||
								outItemCode.equals("3320311_2_00047")||outItemCode.equals("3320311_2_00050")||
								outItemCode.equals("3320412_2_00006")||outItemCode.equals("3320412_2_00015")||
								outItemCode.equals("3320412_2_00018")||outItemCode.equals("3320412_2_00021")||
								outItemCode.equals("3320412_2_00024")||outItemCode.equals("3320412_2_00034")||
								outItemCode.equals("3320412_2_00044")||outItemCode.equals("3320412_2_00059")||
								outItemCode.equals("3320412_2_00062")||outItemCode.equals("3320511_1_00007")||
								outItemCode.equals("3320511_1_00014")||outItemCode.equals("3320511_1_00020")||
								outItemCode.equals("3320511_1_00026")||outItemCode.equals("3320511_1_00029")||
								outItemCode.equals("3320511_1_00032")||outItemCode.equals("3320511_1_00035")||
								outItemCode.equals("3320511_1_00050")||outItemCode.equals("3320612_1_00003")||
								outItemCode.equals("3320612_1_00007")||outItemCode.equals("3320711_2_00003")||
								outItemCode.equals("3320711_2_00013")||outItemCode.equals("3320711_2_00016")||
								outItemCode.equals("3320711_2_00019")||outItemCode.equals("3320711_2_00022")||
								outItemCode.equals("3320711_2_00025")||outItemCode.equals("3320711_2_00028")||
								outItemCode.equals("3320812_2_00003")||outItemCode.equals("3320812_2_00012")||
								outItemCode.equals("3320812_2_00015")||outItemCode.equals("3320812_2_00018")||
								outItemCode.equals("3320812_2_00021")||outItemCode.equals("3321000_1_00008")||
								outItemCode.equals("3321000_1_00017")||outItemCode.equals("3321200_1_00010")||
								outItemCode.equals("3321200_1_00024")||outItemCode.equals("3321200_1_00035")||
								outItemCode.equals("3321300_2_00008")||outItemCode.equals("3321300_2_00011")||
								outItemCode.equals("3321300_2_00031")||outItemCode.equals("3321300_2_00039")||
								outItemCode.equals("3321300_2_00042")||outItemCode.equals("3321411_1_00011")||
								outItemCode.equals("3321411_1_00037")||outItemCode.equals("3321512_1_00003")||
								outItemCode.equals("3321512_1_00008")) {
			    		       // int textValue = Integer.parseInt(ExcelUtil.getCellValue(row.getCell(2)));
		    			     String textValue = ExcelUtil.getCellValue(row.getCell(2)).trim();
							if (textValue.equals("0")) {
								throw new Exception("上传失败---表中批注是"+outItemCode+"的单元格是分母不能是空或'0',应填写'不适用'，请重新填写并上传！");
							}
						}
		    		   //-------分母是两个数相加的
		    		  if (outItemCode.equals("3320311_2_00003")||outItemCode.equals("3320311_2_00010")||
		    			  outItemCode.equals("3320412_2_00003")||outItemCode.equals("3320412_2_00010")||
		    			  outItemCode.equals("3320412_2_00048")||outItemCode.equals("3320412_2_00053")||
		    			  outItemCode.equals("3320711_2_00007")||outItemCode.equals("3320812_2_00007")||
		    			  outItemCode.equals("3321000_1_00012")||outItemCode.equals("3321200_1_00007")||
		    			  outItemCode.equals("3321300_2_00005")||outItemCode.equals("3321300_2_00028")){	
			    		 // int textValue = Integer.parseInt(ExcelUtil.getCellValue(row.getCell(2)));
			    		  String textValue = ExcelUtil.getCellValue(row.getCell(2)).trim();

		    			  Row row1= list.get(i-1);
			    		   //int textValue1 =  Integer.parseInt(ExcelUtil.getCellValue(row1.getCell(2)));
			    		   String textValue1 = ExcelUtil.getCellValue(row1.getCell(2)).trim();
			    		   String outItemCode1=ExcelUtil.getCellValue(row1.getCell(0)).trim();
			    		   if (textValue.equals("0")&&textValue1.equals("0")){
								throw new Exception("上传失败---表中批注是"+outItemCode+"和"+outItemCode1+"的单元格是分母不能是空或'0',应填写'不适用'，请重新填写并上传！");
						}
					  }//---------分母是三个数相加的
		    		  else if (outItemCode.equals("3321300_2_00024")) {	
			    		  //int textValue = Integer.parseInt(ExcelUtil.getCellValue(row.getCell(2)));
		    			  String textValue = ExcelUtil.getCellValue(row.getCell(2)).trim();
		    			   Row row1= list.get(i-1);
		    			   Row row2= list.get(i-2);
			    		   //int textValue1 =  Integer.parseInt(ExcelUtil.getCellValue(row1.getCell(2)));
			    		  // int textValue2 =  Integer.parseInt(ExcelUtil.getCellValue(row2.getCell(2)));
		    			   String textValue1 = ExcelUtil.getCellValue(row1.getCell(2)).trim();
		    			   String textValue2 = ExcelUtil.getCellValue(row2.getCell(2)).trim();
			    		   String outItemCode1=ExcelUtil.getCellValue(row1.getCell(0)).trim();
			    		   String outItemCode2=ExcelUtil.getCellValue(row2.getCell(0)).trim();

			    		if (textValue.equals("0")&&textValue1.equals("0")&&textValue2.equals("0")){
								throw new Exception("上传失败---表中批注是"+outItemCode+"和"+outItemCode1+"和"+outItemCode2+"的单元格是分母不能是空或'0',应填写'不适用'，请重新填写并上传！");
								}
					  }
		    		   if("".equals(ExcelUtil.getCellValue(row.getCell(2)))||"--".equals(ExcelUtil.getCellValue(row.getCell(2)))||"-".equals(ExcelUtil.getCellValue(row.getCell(2)))){		    		   
		    			   System.out.println("无值可取！！！");
		    			   cfReportData.setReportItemValue(new BigDecimal(0).setScale(8,BigDecimal.ROUND_HALF_UP));
		    			   cfReportData.setReportItemWanValue(new BigDecimal(0).setScale(12,BigDecimal.ROUND_HALF_UP));
		    		   }else if(ExcelUtil.getCellValue(row.getCell(2)).trim().contains("不适用")){
			    	       System.out.println("============不适用 ");
		    			   cfReportData.setReportItemValue(new BigDecimal(0).setScale(8,BigDecimal.ROUND_HALF_UP));
			    		   cfReportData.setReportItemWanValue(new BigDecimal(0).setScale(12,BigDecimal.ROUND_HALF_UP));
			    		   cfReportData.setIsused("0");
			    	       System.out.println("------------不适用 ");
			    	   }else{
		    			   cfReportData.setReportItemValue(new BigDecimal(ExcelUtil.getCellValue(row.getCell(2))).setScale(8,BigDecimal.ROUND_HALF_UP));
		    			   cfReportData.setReportItemWanValue(new BigDecimal(ExcelUtil.getCellValue(row.getCell(2))).divide(new BigDecimal("10000"), 12, RoundingMode.HALF_UP));
		    		   }
			      } catch (NumberFormatException e) {
			    	   e.printStackTrace();
			    	  
			       }
		    	   cfReportData.setOutItemType("05");
		    	   cfReportData.setTextValue("");
		    	   cfReportData.setDesText(null);//描述性型
		    	   cfReportData.setFileText(null);//文件型
		       }else if(strOutItemType.equals("04")){//数值型
		    	   try {
		    		   if("".equals(ExcelUtil.getCellValue(row.getCell(2)))||"--".equals(ExcelUtil.getCellValue(row.getCell(2)))||"--".equals(ExcelUtil.getCellValue(row.getCell(2)))){		    		   
		    			   System.out.println("无值可取！！！");
		    			   cfReportData.setReportItemValue(new BigDecimal(0).setScale(8,BigDecimal.ROUND_HALF_UP));
		    			   cfReportData.setReportItemWanValue(new BigDecimal(0).setScale(12,BigDecimal.ROUND_HALF_UP));
		    		   }else if(ExcelUtil.getCellValue(row.getCell(2)).trim().contains("不适用")){
			    		   cfReportData.setReportItemValue(new BigDecimal(0).setScale(8,BigDecimal.ROUND_HALF_UP));
			    		   cfReportData.setReportItemWanValue(new BigDecimal(0).setScale(12,BigDecimal.ROUND_HALF_UP));
			    		   cfReportData.setIsused("0");
			    	   }else{
		    			   cfReportData.setReportItemValue(new BigDecimal(ExcelUtil.getCellValue(row.getCell(2))).setScale(8,BigDecimal.ROUND_HALF_UP));
		    			   cfReportData.setReportItemWanValue(new BigDecimal(ExcelUtil.getCellValue(row.getCell(2))).divide(new BigDecimal("10000"), 12, RoundingMode.HALF_UP));
		    		   }
			       } catch (NumberFormatException e) {
			    	   e.printStackTrace();
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
		       cfReportData.setOperator(user.getUserCode());//（操作人）
		       cfReportData.setOperDate(pld.getDate());//操作日期
		       cfReportData.setTemp(pld.getRemark());

		       //保存新数据
		       impcfReportDataDao.save(cfReportData);
		       //添加修改轨迹
		       if(strOutItemType.equals("04")||strOutItemType.equals("05")||strOutItemType.equals("06")){		    	   
		    	   BigDecimal numvalue=(BigDecimal)hismap.get(row.getCell(0).getStringCellValue());//可能空指针异常
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
	    			history.setUser_name(cfReportData.getOperator());
	    			history.setReportid(reportId);
	    			impcfReportDatadao.save(history);
		       }else if(strOutItemType.equals("01")||strOutItemType.equals("07")){
		    	   String strvalue=(String)hismap.get(row.getCell(0).getStringCellValue());//可能空指针异常
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
		       }else if(strOutItemType.equals("03")||strOutItemType.equals("02")){
		    	    System.out.println("描述性型和文件型暂无法保存修改轨迹。");
		       }
		       k=k+1;
		       System.out.println(k);
		}
//		if(uploadInfoDTOs.size()!=0){
//			throw new UploadNumberFormatException(uploadInfoDTOs);
//		}
		
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
		List<?> hisresult=impcfReportDataDao.queryBySQL(sqlhis);
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
					pld.setReportname("4");
				}else if(param.equals("2-data")){
					pld.setReportname("5");
				}
				//固定因子数据
				Map<String, Object> map = new HashMap<String, Object>();
				map = qryDataByDeptNo(pld.getYearmonth(), pld.getQuarter(), pld.getReporttype(), pld.getReportname(), comcode, dept, deptflag);
				
				//获取数值型指标的类型和精度
				List<Map<String, Object>> itemAcclist = new ArrayList<Map<String,Object>>();
				itemAcclist = qryItemAccuracy(pld.getReportname());
				Map<String, Object> itemacc = itemAcclist.get(0);
				Map<String, Object> itemtype = itemAcclist.get(1);
				
				for(int i = 0; i < wb.getNumberOfSheets(); i++){
					String sheetName = wb.getSheetName(i);
					if(sheetName.equals("A01#") || sheetName.equals("Database") || templist.contains(sheetName)){
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
				qry = "select reportcode,remark from cfreportdesc where remark is not null and temp1 like '%" + Config.getProperty("CompanyType") + "%' and temp2='1'";
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
		String qry = "select outitemcode,outitemtype, reportitemvalue,textvalue,isused  from cfreportdata where month= '" + year 
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
	public List<Map<String,Object>> qryItemAccuracy(String reporttype){
		String sql = "select * from (select t.portitemcode code, i.outitemtype itemtype, t.decimals from cfreportelement t, cfreportitemcodedesc i "
				+ "where t.portitemcode = i.outitemcode and i.reporttype ='"+reporttype+"' union all "
				+ " select rs.reportitemcode code, rs.outitemtype itemtype, rs.decimals from  rsys_cfreportitemcodedesc rs "
				+ " where  rs.reporttype ='"+reporttype+"')";
		List<?> list = mapDao.queryBysql(sql);
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
		return maplist;
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