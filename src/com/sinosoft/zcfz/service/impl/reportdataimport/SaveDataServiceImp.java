package com.sinosoft.zcfz.service.impl.reportdataimport;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sinosoft.common.Dao;
import com.sinosoft.dao.CfmapDao;
import com.sinosoft.zcfz.dao.reportdataimport.CfReportDataDAO;
import com.sinosoft.zcfz.dao.reportdataimport.CfReportItemCodeDescDAO;
import com.sinosoft.zcfz.dao.reportdataimport.CfReportRowAddDataDAO;
import com.sinosoft.zcfz.dao.reportdataimport.CfReportRowAddDescDAO;
import com.sinosoft.zcfz.dao.reportdatamamage.CfReportDataDao;
import com.sinosoft.zcfz.dao.reportdatamamage.RowAddDataDao;
import com.sinosoft.zcfz.dto.reportdataimport.UploadInfoDTO;
import com.sinosoft.entity.CfReportData;
import com.sinosoft.entity.CfReportDataId;
import com.sinosoft.entity.CfReportRowAddData;
import com.sinosoft.entity.CfReportRowAddDataId;
import com.sinosoft.entity.Cfmap;
import com.sinosoft.entity.ReportHistory;
import com.sinosoft.entity.RowAddHistory;
import com.sinosoft.zcfz.service.impl.reportformcompute.PreComputeImp;
import com.sinosoft.zcfz.service.reportdataimport.SaveDataService;
import com.sinosoft.util.Config;
import com.sinosoft.util.ExcelUtil;
import com.sinosoft.util.UploadNumberFormatException;
import com.sinosoft.util.UploaderServlet;

@Service
public class SaveDataServiceImp implements SaveDataService {
	private SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
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
	@Resource 
	private CfmapDao mapDao;
	@Resource
	private ExcelUtil eu;
	@Resource
	private UploaderServlet us;
	private Map<String, String> map=new HashMap<String, String>();
	private Map<String, String> mapDecimals=new HashMap<String, String>();
	private Map<String, Object> hismap=new HashMap<String, Object>();
	@Resource
	private Dao dao;
	private List<Map<String, Object>> uploadInfoDTOs = new ArrayList<Map<String, Object>>();//存放错误结果集
	private static final Logger LOGGER = LoggerFactory.getLogger(PreComputeImp.class);

	@Transactional
	public void saveData(UploadInfoDTO pld) throws NullPointerException, Exception {
		uploadInfoDTOs.clear();
		// TODO Auto-generated method stub
		eu.setExcelPath(pld.getFilePath()+"/"+us.getFileName());
		
		if("1".equals(pld.getReporttype())&&"1".equals(pld.getReportname())){//季度快报->偿付能力报告	
			eu.setStartReadPos(1);
			eu.setEndReadPos(0);
			eu.setSelectedSheetName("A01#");
			List<Row> listA01=eu.readExcel();
			saveDataA01(listA01,pld);
			listA01.removeAll(listA01);
		}
		if("2".equals(pld.getReporttype())||"3".equals(pld.getReporttype())){//季度报告偿付能力报告或临时报告
			if("1".equals(pld.getReportname())){//偿付能力报告
				eu.setStartReadPos(1);
				eu.setEndReadPos(0);
				eu.setSelectedSheetName("A01#");
				List<Row> listA01=eu.readExcel();
				saveDataA01(listA01,pld);
				listA01.removeAll(listA01);
				eu.setSelectedSheetName("A02#");
				List<Row> listA02=eu.readExcel();
				saveDataA02(listA02,pld);
				listA02.removeAll(listA02);				
			}else if("2".equals(pld.getReportname())||"3".equals(pld.getReportname())){//Word报告或现金流压力测试报告
				eu.setStartReadPos(1);
				eu.setEndReadPos(0);
				eu.setSelectedSheetName("A01#");
				List<Row> listA01=eu.readExcel();
				saveDataA01(listA01,pld);
				listA01.removeAll(listA01);
			}
		}
	}
	@Transactional
	public void saveDataA01(List<Row> list,UploadInfoDTO pld)throws UploadNumberFormatException,NullPointerException,Exception {
		// TODO Auto-generated method stub
	//	String reportId=pld.getReportId();
		 //校验上传的文件是否正确，偿付能力上传时，是否是季报模板，现金流上传，就是现金流
		System.out.println("------------------开始校验上传文件是否正确------------------");
			Row row1=list.get(0);
			//获取因子代码
			String outItemCode1 = row1.getCell(0).getStringCellValue().trim();
			String leixingString ="select c.reporttype reporttype from cfreportitemcodedesc c where c.reportitemcode='"+outItemCode1+"'";
			List<?> listleixing=impcfReportItemCodeDescDao.queryBysql(leixingString);
			String leixing="";
			for (int j = 0; j < listleixing.size(); j++) {
				leixing=((Map<String, String>) listleixing.get(j)).get("reporttype");
				System.out.println(leixing);
			}
			//cfreportitemcodedesc里季报=1，现金流为3，
			//reportid里面，pld.getReportId().substring(7, 8)
			if (!leixing.equals(pld.getReportname())) {
				System.out.println(pld.getReportname());
				System.out.println("上传的文件不匹配！！！！！");
				throw new Exception("上传的文件不匹配！！！！！");
				
			}
		
		
		//文本对应关系 如AAA 1|AAA集合
		Map<String, String> doubleMap = getMapInfo();
				
		cfReportItemCodeHis(pld);
		getCfReportItemCodeDesc(pld.getDepartment());
		
		getCfReportItemCodeDescDecimals(pld.getDepartment());
		validateNumberFormat(list, pld);//校验数据格式
		if (uploadInfoDTOs.size() != 0) {
			throw new UploadNumberFormatException(uploadInfoDTOs);
		}
		
		/*
		//判断如果是快报的话，添加期间信息（C01_10000）
		if("1".equals(pld.getReportId().substring(7, 8))){
			CfReportData data=new CfReportData();
			CfReportDataId dataId=new CfReportDataId();
			
			dataId.setOutItemCode("C01_10000");//因子代码
			dataId.setReportId(pld.getReportId());
			data.setId(dataId);
			
			data.setMonth(pld.getYearmonth());//月度
			data.setQuarter(pld.getQuarter());//季度
			data.setReportRate(pld.getReporttype());//报送频度
			data.setComCode(Config.getProperty("OrganCode"));//机构代码
			data.setOutItemCodeName("报告期间");//因子名称
			data.setReportItemCode("C01_10000");//内部码	
			data.setTextValue(pld.getReportId().substring(0, 4)+"年第"+pld.getReportId().substring(5, 6)+"季度");
			data.setDesText(null);//描述性型
			data.setFileText(null);//文件型
			data.setOutItemType("01");
			data.setReportItemValue(new BigDecimal(0).setScale(4,BigDecimal.ROUND_HALF_UP));
			data.setReportItemWanValue(new BigDecimal(0).setScale(8,BigDecimal.ROUND_HALF_UP));
			data.setSource("1");//源1：指标导入；源2：自动提数业务；源3：自动提数财务；源4：精算模板导入；源5：投资模板导入；源6：计算
			data.setReportType(pld.getReportname());//报表类别
			data.setComputeLevel("0");//计算级别
			data.setReportItemValueSource("0");//指标值来源：1-初步计算数据手工修改；2-尾数自动调整
			data.setOperator("cf");//默认（操作人）
			data.setOperDate(pld.getDate());//操作日期
			data.setTemp("");
			
			impcfReportDataDao.save(data);
		}
		*/	
		//validateDecimals(list, pld);//校验精度
		// if (uploadInfoDTOs.size() != 0) {
		// throw new UploadDecimalsException(uploadInfoDTOs);
		// }
		
		int k=0;
		for(int i=0;i<list.size();i++){
		       Row row=list.get(i);
		       System.out.println("listsize="+list.size()+"    row.getcurrentrow()="+(i+1));
		       System.out.println("row.getLastCellNum()="+row.getLastCellNum());
		       String strTemp=(String)map.get(row.getCell(0).getStringCellValue());//可能空指针异常 
		       System.out.println(row.getCell(0).getStringCellValue());//因子代码
		       if(strTemp==null){
		    	   System.out.println("指标描述表未查到该因子代码");
		    	  continue;
		       }
		       CfReportData cfReportData=new CfReportData();
		       CfReportDataId cfReportDataId=new CfReportDataId();
		       //因子代码
		       String outItemCode = row.getCell(0).getStringCellValue().trim();
		       //因子名称
		       String OutItemCodeName = row.getCell(1).getStringCellValue().trim();
		       //指标数值
		       String itemValue = ExcelUtil.getCellValue(row.getCell(2)).trim();

		       cfReportDataId.setOutItemCode(outItemCode);//因子代码
		       cfReportDataId.setReportId(pld.getReportId());
		       cfReportData.setId(cfReportDataId);
		       
		       cfReportData.setMonth(pld.getYearmonth());//月度
		       cfReportData.setQuarter(pld.getQuarter());//季度
		       cfReportData.setReportRate(pld.getReporttype());//报送频度
		       cfReportData.setComCode(Config.getProperty("OrganCode"));//机构代码
		       cfReportData.setOutItemCodeName(OutItemCodeName);//因子名称
		       cfReportData.setReportItemCode(outItemCode);//内部码
		       System.out.println(itemValue);

		       String temp[]=strTemp.split("_");
		       String strOutItemType=temp[0];
		       String yinziCode=row.getCell(0).getStringCellValue();
		       if(strOutItemType.equals("06")){//百分比型
		    	   try {
		    		   if("".equals(itemValue)||"--".equals(itemValue)||"-".equals(itemValue)){		    		   
			    		   System.out.println("无值可取！！！");
			    		   cfReportData.setReportItemValue(new BigDecimal(0).setScale(8,BigDecimal.ROUND_HALF_UP));
			    		   cfReportData.setReportItemWanValue(new BigDecimal(0).setScale(12,BigDecimal.ROUND_HALF_UP));
			    	   }else if(ExcelUtil.getCellValue(row.getCell(2)).trim().contains("不适用")){
							cfReportData.setReportItemValue(new BigDecimal(0).setScale(8,BigDecimal.ROUND_HALF_UP));
							cfReportData.setReportItemWanValue(new BigDecimal(0).setScale(12,BigDecimal.ROUND_HALF_UP));
							cfReportData.setIsused("0");
						}else{
			    		   cfReportData.setReportItemValue(new BigDecimal(itemValue).setScale(8,BigDecimal.ROUND_HALF_UP));
			    		   cfReportData.setReportItemWanValue(new BigDecimal(0).setScale(12,BigDecimal.ROUND_HALF_UP));
			    	   }
			       } catch (NumberFormatException e) {
			    	   e.printStackTrace();
			    	   //storeA01UploadInfoDTOs(row,strOutItemType);
			    	   //continue;
			    	  //throw new  RuntimeException(row.getCell(0).getStringCellValue()+"格式不对");
			       }
		    	   cfReportData.setOutItemType("06");
		    	   cfReportData.setTextValue("");
		    	   cfReportData.setDesText(null);//描述性型
		    	   cfReportData.setFileText(null);//文件型
		       }else if(strOutItemType.equals("05")){//数量型
		    	   try {
		    		   if("".equals(itemValue)||"--".equals(itemValue)||"-".equals(itemValue)){		    		   
		    			   System.out.println("无值可取！！！");
		    			   cfReportData.setReportItemValue(new BigDecimal(0).setScale(8,BigDecimal.ROUND_HALF_UP));
		    			   cfReportData.setReportItemWanValue(new BigDecimal(0).setScale(12,BigDecimal.ROUND_HALF_UP));
		    		   }else if(ExcelUtil.getCellValue(row.getCell(2)).trim().contains("不适用")){
							cfReportData.setReportItemValue(new BigDecimal(0).setScale(8,BigDecimal.ROUND_HALF_UP));
							cfReportData.setReportItemWanValue(new BigDecimal(0).setScale(12,BigDecimal.ROUND_HALF_UP));
							cfReportData.setIsused("0");
						}else{
		    			   cfReportData.setReportItemValue(new BigDecimal(itemValue).setScale(8,BigDecimal.ROUND_HALF_UP));
		    			   cfReportData.setReportItemWanValue(new BigDecimal(itemValue).divide(new BigDecimal("10000"), 12, RoundingMode.HALF_UP));
		    		   }
			       } catch (NumberFormatException e) {
			    	   e.printStackTrace();
			    	   //storeA01UploadInfoDTOs(row,strOutItemType);
			    	   //continue;
			    	   //throw new  RuntimeException(row.getCell(0).getStringCellValue()+"格式不对");
			       }
		    	   cfReportData.setOutItemType("05");
		    	   cfReportData.setTextValue("");
		    	   cfReportData.setDesText(null);//描述性型
		    	   cfReportData.setFileText(null);//文件型
		       }else if(strOutItemType.equals("04")){//数值型
		    	   try {
		    		   if("".equals(itemValue)||"--".equals(itemValue)||"--".equals(itemValue)){		    		   
		    			   System.out.println("无值可取！！！");
		    			   cfReportData.setReportItemValue(new BigDecimal(0).setScale(8,BigDecimal.ROUND_HALF_UP));
		    			   cfReportData.setReportItemWanValue(new BigDecimal(0).setScale(12,BigDecimal.ROUND_HALF_UP));
		    		   }else if(ExcelUtil.getCellValue(row.getCell(2)).trim().contains("不适用")){
							cfReportData.setReportItemValue(new BigDecimal(0).setScale(8,BigDecimal.ROUND_HALF_UP));
							cfReportData.setReportItemWanValue(new BigDecimal(0).setScale(12,BigDecimal.ROUND_HALF_UP));
							cfReportData.setIsused("0");
						}else{
		    			   cfReportData.setReportItemValue(new BigDecimal(itemValue).setScale(8,BigDecimal.ROUND_HALF_UP));
		    			   cfReportData.setReportItemWanValue(new BigDecimal(itemValue).divide(new BigDecimal("10000"), 12, RoundingMode.HALF_UP));
		    		   }
		    		   /*
		    		    * 判断这几个数是否有值，如果是在A01中的值是0，就把这几个数设为空，不然下载数据时IR01会出现多余的RF0和K
		    		    */
		    		   if(turnNull(yinziCode, itemValue)){
		    			   //cfReportData.setReportItemValue(null);
		    			   //cfReportData.setReportItemWanValue(null);
		    		   }
			       } catch (NumberFormatException e) {
			    	   e.printStackTrace();
			    	   //storeA01UploadInfoDTOs(row,strOutItemType);
			    	   //continue;
			    	   //throw new  RuntimeException(row.getCell(0).getStringCellValue()+"格式不对");
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
			    	  // storeA01UploadInfoDTOs(row,strOutItemType);
			    	  // continue;
			    	  // throw new RuntimeException(row.getCell(0).getStringCellValue()+"格式不对");
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
			    	   //storeA01UploadInfoDTOs(row,strOutItemType);
			    	  // continue;
			    	   //throw new  RuntimeException(row.getCell(0).getStringCellValue()+"格式不对");
			       }
		    	   cfReportData.setTextValue("");
		    	   cfReportData.setDesText(null);//描述性型
		    	   cfReportData.setFileText(null);//文件型
		       }else if(strOutItemType.equals("01")){//短文本类型

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
			    	  // storeA01UploadInfoDTOs(row,strOutItemType);
			    	  // continue;
			    	   //throw new  RuntimeException(row.getCell(0).getStringCellValue()+"格式不对");
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
			    	  // storeA01UploadInfoDTOs(row,strOutItemType);
			    	  // continue;
			    	  // throw new  RuntimeException(row.getCell(0).getStringCellValue()+"格式不对");
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
		       cfReportData.setOperator("cf");//默认（操作人）
		       cfReportData.setOperDate(pld.getDate());//操作日期
		       cfReportData.setTemp("");		       
		       
		       
		       
/*		       List<?> result=impcfReportDataDao.queryById(cfReportDataId);
		       if(result.size()>0){		    	   
		    	   //删除原有数据
		    	   impcfReportDataDao.delete(cfReportDataId);		       
		    	   //固定因子修改添加修改轨迹
		    	   Map<?,?> m=(Map<?,?>)result.get(0);
		    	   if(strOutItemType.equals("06")||strOutItemType.equals("05")||strOutItemType.equals("04")){
		    		   if(!cfReportData.getReportItemValue().equals((BigDecimal)m.get("reportItemValue"))){
		    			   ReportHistory history = new ReportHistory();
		    			   history.setMonth(cfReportData.getId().getMonth());
		    			   history.setOper_date(cfReportData.getOperDate());
		    			   history.setQuarter(cfReportData.getId().getQuarter());
		    			   history.setReport_code(cfReportData.getId().getOutItemCode());
		    			   history.setReport_mvalue((BigDecimal)m.get("reportItemValue"));
		    			   history.setReport_mvalue_after(cfReportData.getReportItemValue());
		    			   history.setReport_name(cfReportData.getOutItemCodeName());
		    			   history.setReport_rate(cfReportData.getId().getReportRate());
		    			   history.setUser_name(cfReportData.getOperator());
		    			   impcfReportDatadao.save(history);
		    		   }
		    	   }else if(strOutItemType.equals("01")||strOutItemType.equals("07")){
		    		   if(!cfReportData.getTextValue().equals((String)m.get("textValue"))){
		    			   ReportHistory history = new ReportHistory();
		    			   history.setMonth(cfReportData.getId().getMonth());
		    			   history.setOper_date(cfReportData.getOperDate());
		    			   history.setQuarter(cfReportData.getId().getQuarter());
		    			   history.setReport_code(cfReportData.getId().getOutItemCode());
		    			   history.setText_value((String)m.get("textValue"));
		    			   history.setText_value_after(cfReportData.getTextValue());
		    			   history.setReport_name(cfReportData.getOutItemCodeName());
		    			   history.setReport_rate(cfReportData.getId().getReportRate());
		    			   history.setUser_name(cfReportData.getOperator());
		    			   impcfReportDatadao.save(history);
		    		   }
		    	   }else if(strOutItemType.equals("03")||strOutItemType.equals("02")){
		    		   System.out.println("描述性型和文件型暂无法保存修改轨迹。");
		    	   }
		       }else{		    	   
		    	   if(strOutItemType.equals("06")||strOutItemType.equals("05")||strOutItemType.equals("04")){			   		
		    		   ReportHistory history = new ReportHistory();
		    		   history.setMonth(cfReportData.getId().getMonth());
		    		   history.setOper_date(cfReportData.getOperDate());
		    		   history.setQuarter(cfReportData.getId().getQuarter());
		    		   history.setReport_code(cfReportData.getId().getOutItemCode());
		    		   history.setReport_mvalue(new BigDecimal(0).setScale(4,BigDecimal.ROUND_HALF_UP));
		    		   history.setReport_mvalue_after(cfReportData.getReportItemValue());
		    		   history.setReport_name(cfReportData.getOutItemCodeName());
		    		   history.setReport_rate(cfReportData.getId().getReportRate());
		    		   history.setUser_name(cfReportData.getOperator());
		    		   impcfReportDatadao.save(history);
		    	   }else if(strOutItemType.equals("01")||strOutItemType.equals("07")){
		    		   ReportHistory history = new ReportHistory();
		    		   history.setMonth(cfReportData.getId().getMonth());
		    		   history.setOper_date(cfReportData.getOperDate());
		    		   history.setQuarter(cfReportData.getId().getQuarter());
		    		   history.setReport_code(cfReportData.getId().getOutItemCode());
		    		   history.setText_value("");
		    		   history.setText_value_after(cfReportData.getTextValue());
		    		   history.setReport_name(cfReportData.getOutItemCodeName());
		    		   history.setReport_rate(cfReportData.getId().getReportRate());
		    		   history.setUser_name(cfReportData.getOperator());
		    		   impcfReportDatadao.save(history);
		    	   }else if(strOutItemType.equals("03")||strOutItemType.equals("02")){
		    		   System.out.println("描述性型和文件型暂无法保存修改轨迹。");
		    	   }
		       }*/
		       //保存新数据
		       impcfReportDataDao.save(cfReportData);
		       //添加修改轨迹
		       if(strOutItemType.equals("04")||strOutItemType.equals("05")||strOutItemType.equals("06")){		    	   
		    	   BigDecimal numvalue=(BigDecimal)hismap.get(row.getCell(0).getStringCellValue());//可能空指针异常
		    	   if(numvalue==null){
		    		   numvalue=new BigDecimal(0);
		    	   }
	    			ReportHistory history = new ReportHistory();
	    			history.setReportid(cfReportData.getId().getReportId());
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
	    			history.setReportid(cfReportData.getId().getReportId());
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
		// if(uploadInfoDTOs.size()!=0){
		// throw new UploadNumberFormatException(uploadInfoDTOs);
		// }
		
	}

	@Transactional
	public void saveDataA02(List<Row> list,UploadInfoDTO pld)throws UploadNumberFormatException,NullPointerException,Exception {
		// TODO Auto-generated method stub
		
		//获取需要映射的指标
		Map<String, String> rowMap = getMapCode();
		//文本对应关系 如AAA 1|AAA集合
		Map<String, String> doubleMap = getMapInfo();
		
		cfReportRowAddHis(pld);
		getCfReportRowAddDesc();
		int k=0;
		for(int i=0;i<list.size();i++){
			Row row=list.get(i);
			System.out.println("row.getLastCellNum()="+row.getLastCellNum());
			for(int j=22;j<row.getLastCellNum();j++){
				System.out.println(ExcelUtil.getCellValue(row.getCell(j)));
				if("".equals(ExcelUtil.getCellValue(row.getCell(j)))||"0".equals(ExcelUtil.getCellValue(row.getCell(j)))||"0.0".equals(ExcelUtil.getCellValue(row.getCell(j)))||"-".equals(ExcelUtil.getCellValue(row.getCell(j)))||"--".equals(ExcelUtil.getCellValue(row.getCell(j)))||"#REF!".equals(ExcelUtil.getCellValue(row.getCell(j)))||"<g_idiv>".equals(ExcelUtil.getCellValue(row.getCell(j)))||"#VALUE!".equals(ExcelUtil.getCellValue(row.getCell(j)))){
					continue;
				}
				System.out.println("第"+(i+2)+"行，第"+(j+1)+"列。");
				CfReportRowAddDataId cfReportRowAddDataId=new CfReportRowAddDataId();
				CfReportRowAddData cfReportRowAddData=new CfReportRowAddData();
				cfReportRowAddData.setTableCode(row.getCell(0).getStringCellValue());//表代码
				cfReportRowAddDataId.setColCode(row.getCell(2).getStringCellValue());//列代码
				cfReportRowAddDataId.setRowNo(String.valueOf(j-21));//行数
				cfReportRowAddData.setYearMonth(pld.getYearmonth());//会计与月度
				cfReportRowAddData.setQuarter(pld.getQuarter());//季度
				cfReportRowAddData.setReportRate(pld.getReporttype());//报送频度
				cfReportRowAddData.setId(cfReportRowAddDataId);
				cfReportRowAddData.setReportType(pld.getReportname());//报表类别
				cfReportRowAddData.setSource("1");//源1：指标导入；源2：自动提数业务；源3：自动提数财务；源4：精算模板导入；源5：投资模板导入；源6：计算
				cfReportRowAddData.setOperator2("cf");//操作人
				cfReportRowAddData.setOperDate2(pld.getDate());//操作日期
				cfReportRowAddData.setComCode(Config.getProperty("OrganCode"));
				cfReportRowAddDataId.setReportId(pld.getReportId());
				System.out.println(row.getCell(0).getStringCellValue()+"_"+row.getCell(2).getStringCellValue());
				String strColtype = map.get(row.getCell(0).getStringCellValue()+"_"+row.getCell(2).getStringCellValue());
				if(strColtype==null){
					System.out.println("行可扩展因子代码未定义");
					continue;
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
						//storeA02UploadInfoDTOs(row, strColtype, position);
					//	continue;
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
						//storeA02UploadInfoDTOs(row, strColtype, position);
					//	continue;
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
						//storeA02UploadInfoDTOs(row, strColtype, position);
					//	continue;
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
						//storeA02UploadInfoDTOs(row, strColtype, position);
					//	continue;
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
						//storeA02UploadInfoDTOs(row, strColtype, position);
					//	continue;
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
						//storeA02UploadInfoDTOs(row, strColtype, position);
					//	continue;
						throw new  RuntimeException("表代码:"+row.getCell(0).getStringCellValue()+"列代码:"+row.getCell(2).getStringCellValue()+"格式不对");

					}
				}else{
					System.out.println("导入因子值类型有误！！！");
				}
/*				List<?> result=impcfReportRowAddDataDao.queryById(cfReportRowAddDataId);
				if(result.size()>0){		    	   
					//删除原有数据
					impcfReportRowAddDataDao.delete(cfReportRowAddDataId);
					//行可扩展因子修改添加修改轨迹
					Map<?,?> m=(Map<?,?>)result.get(0);
					if(strColtype.equals("06")||strColtype.equals("05")||strColtype.equals("04")){
						if(!cfReportRowAddData.getNumValue().equals((BigDecimal)m.get("numValue"))){
							RowAddHistory history = new RowAddHistory();
							history.setOper_date(cfReportRowAddData.getOperDate2());
							history.setTable_code(cfReportRowAddData.getId().getTableCode());
							history.setTable_name(row.getCell(1).getStringCellValue());
							history.setCol_code(cfReportRowAddData.getId().getColCode());
							history.setCol_name(row.getCell(3).getStringCellValue());
							history.setRow_no(cfReportRowAddData.getId().getRowNo());
							history.setReport_rate(cfReportRowAddData.getId().getReportRate());
							history.setYear_month(cfReportRowAddData.getId().getYearMonth());
							history.setQuarter(cfReportRowAddData.getId().getQuarter());
							history.setReport_mvalue((BigDecimal)m.get("numValue"));
							history.setReport_mvalue_after(cfReportRowAddData.getNumValue());
							history.setUser_name(cfReportRowAddData.getOperator2());
							impcfRowAddDao.save(history);
						}
					}else if(strColtype.equals("01")||strColtype.equals("07")){
						if(!cfReportRowAddData.getTextValue().equals((String)m.get("textValue"))){
							RowAddHistory history = new RowAddHistory();
							history.setOper_date(cfReportRowAddData.getOperDate2());
							history.setTable_code(cfReportRowAddData.getId().getTableCode());
							history.setTable_name(row.getCell(1).getStringCellValue());
							history.setCol_code(cfReportRowAddData.getId().getColCode());
							history.setCol_name(row.getCell(3).getStringCellValue());
							history.setRow_no(cfReportRowAddData.getId().getRowNo());
							history.setReport_rate(cfReportRowAddData.getId().getReportRate());
							history.setYear_month(cfReportRowAddData.getId().getYearMonth());
							history.setQuarter(cfReportRowAddData.getId().getQuarter());
							history.setText_value((String)m.get("textValue"));
							history.setText_value_after(cfReportRowAddData.getTextValue());
							history.setUser_name(cfReportRowAddData.getOperator2());
							impcfRowAddDao.save(history);
						}
					}else if(strColtype.equals("03")||strColtype.equals("02")){
						System.out.println("描述性型和文件型暂无法保存修改轨迹。");
					}			       
				}else{			    	   
					if(strColtype.equals("06")||strColtype.equals("05")||strColtype.equals("04")){			   		
						RowAddHistory history = new RowAddHistory();
						history.setOper_date(cfReportRowAddData.getOperDate2());
						history.setTable_code(cfReportRowAddData.getId().getTableCode());
						history.setTable_name(row.getCell(1).getStringCellValue());
						history.setCol_code(cfReportRowAddData.getId().getColCode());
						history.setCol_name(row.getCell(3).getStringCellValue());
						history.setRow_no(cfReportRowAddData.getId().getRowNo());
						history.setReport_rate(cfReportRowAddData.getId().getReportRate());
						history.setYear_month(cfReportRowAddData.getId().getYearMonth());
						history.setQuarter(cfReportRowAddData.getId().getQuarter());
						history.setReport_mvalue(new BigDecimal("0").setScale(4,BigDecimal.ROUND_HALF_UP));
						history.setReport_mvalue_after(cfReportRowAddData.getNumValue());
						history.setUser_name(cfReportRowAddData.getOperator2());
						impcfRowAddDao.save(history);
					}else if(strColtype.equals("01")||strColtype.equals("07")){
						RowAddHistory history = new RowAddHistory();
						history.setOper_date(cfReportRowAddData.getOperDate2());
						history.setTable_code(cfReportRowAddData.getId().getTableCode());
						history.setTable_name(row.getCell(1).getStringCellValue());
						history.setCol_code(cfReportRowAddData.getId().getColCode());
						history.setCol_name(row.getCell(3).getStringCellValue());
						history.setRow_no(cfReportRowAddData.getId().getRowNo());
						history.setReport_rate(cfReportRowAddData.getId().getReportRate());
						history.setYear_month(cfReportRowAddData.getId().getYearMonth());
						history.setQuarter(cfReportRowAddData.getId().getQuarter());
						history.setText_value("");
						history.setText_value_after(cfReportRowAddData.getTextValue());
						history.setUser_name(cfReportRowAddData.getOperator2());
						impcfRowAddDao.save(history);
					}else if(strColtype.equals("03")||strColtype.equals("02")){
						System.out.println("描述性型和文件型暂无法保存修改轨迹。");
					}			       
				}*/		        	       
				//保存新数据
				impcfReportRowAddDataDao.save(cfReportRowAddData);
				//添加修改轨迹
			       if(strColtype.equals("04")||strColtype.equals("05")||strColtype.equals("06")){		    	   
			    	   BigDecimal numvalue=(BigDecimal)hismap.get(row.getCell(2).getStringCellValue());//可能空指针异常
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
			}
			k=k+1;
			System.out.println(k);
		}
		if(uploadInfoDTOs.size()!=0){
			//throw new UploadNumberFormatException(uploadInfoDTOs);
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
	
	/**
	 * A02存储数据格式转换异常结果集
	 * @param row
	 * @param strColtype
	 * @param position
	 */
	private void storeA02UploadInfoDTOs(Row row, String strColtype, String position){
		/*UploadInfoDTO uploadInfoDTO = new UploadInfoDTO();
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
		
		this.uploadInfoDTOs.add(uploadInfoDTO);*/
	}
	
	@Transactional
	public void getCfReportItemCodeDesc(String dpt) {
		// TODO Auto-generated method stub
		String str="";
		if(dpt.equals("account")){
			str+="01";
		}else if(dpt.equals("investment")){
			str+="02";
		}else if(dpt.equals("actuarial")){
			str+="03";
		}else if(dpt.equals("all")){
			str+="01,02,03";
		}
        //表内指标
		String sql="select * from cfreportitemcodedesc where outitemtype!='02' and outitemtype!='03'  order by reportItemCode";
		List<?> result=impcfReportItemCodeDescDao.queryBysql(sql);
		map.clear();
	    for(int i=0;i<result.size();i++){
	    	Map<?,?> m=(Map<?,?>)result.get(i);
			System.out.println(m.get("reportItemCode"));
			map.put((String) m.get("reportItemCode"), m.get("outItemType")+"_"+m.get("reportItemCode"));
		}
	    //表外指标
		String sql_out="select * from rsys_CfReportItemCodeDesc where outitemtype!='02' and outitemtype!='03' order by reportItemCode";
		List<?> result_out=impcfReportItemCodeDescDao.queryBysql(sql_out);
	    for(int i=0;i<result_out.size();i++){
	    	Map<?,?> m=(Map<?,?>)result_out.get(i);
			System.out.println(m.get("reportItemCode"));
			map.put((String) m.get("reportItemCode"), m.get("outItemType")+"_"+m.get("reportItemCode"));
		}
	}
	
	/**
	 * 获取需要校验#A01中指标的精度和格式
	 */
	@Transactional
	public void getCfReportItemCodeDescDecimals(String dpt) {
		// TODO Auto-generated method stub
				String str="";
				if(dpt.equals("account")){
					str+="01";
				}else if(dpt.equals("investment")){
					str+="02";
				}else if(dpt.equals("actuarial")){
					str+="03";
				}else if(dpt.equals("all")){
					str+="01,02,03";
				}
		        //表内指标
				String sql = "select a.reportItemCode,a.outItemType,b.decimals\n" +
						"  from cfreportitemcodedesc a\n" + 
						"  left join cfreportelement b\n" + 
						"    on a.reportitemcode = b.portitemcode\n" + 
						" where outitemtype != '02'\n" + 
						"   and outitemtype != '03' order by a.reportItemCode";

				List<?> result=impcfReportItemCodeDescDao.queryBysql(sql);
				mapDecimals.clear();
			    for(int i=0;i<result.size();i++){
			    	Map<?,?> m=(Map<?,?>)result.get(i);
					System.out.println(m.get("reportItemCode"));
					mapDecimals.put((String) m.get("reportItemCode"), m.get("outItemType")+"_"+m.get("decimals"));
				}
			    //表外指标
			    String sql_out = "select a.reportItemCode,a.outItemType,b.decimals\n" +
						"  from rsys_CfReportItemCodeDesc a\n" + 
						"  left join cfreportelement b\n" + 
						"    on a.reportitemcode = b.portitemcode\n" + 
						" where outitemtype != '02'\n" + 
						"   and outitemtype != '03' order by a.reportItemCode";
				List<?> result_out=impcfReportItemCodeDescDao.queryBysql(sql_out);
			    for(int i=0;i<result_out.size();i++){
			    	Map<?,?> m=(Map<?,?>)result_out.get(i);
					System.out.println(m.get("reportItemCode"));
					mapDecimals.put((String) m.get("reportItemCode"), m.get("outItemType")+"_"+m.get("decimals"));
				}
	}
	
	@Transactional
	private void cfReportItemCodeHis(UploadInfoDTO pld) {
		// TODO Auto-generated method stub
		String sqlhis="select * from cfreportdata where month='"+
		              pld.getYearmonth()+"' and quarter='"+
				      pld.getQuarter()+"' and reportrate='"+
		              pld.getReporttype()+"' and reporttype='"+
				      pld.getReportname()+"' and reportid='"+
 pld.getReportId() + "'";
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
		String lsql="delete from cfreportdata where month='"+pld.getYearmonth()
				+"' and quarter='"+pld.getQuarter()
				+"' and reportrate='"+pld.getReporttype()
				+"' and reporttype='"+pld.getReportname()
				+"' and reportid='"+pld.getReportId()
				+ "'  and outitemtype<>'02'";
		try{
			dao.excute(lsql);
		}catch(Exception e){
			LOGGER.error("删除历史数据出错！sql="+lsql);
			throw new RuntimeException("删除历史数据出错！");
		}
	}
	@Transactional
	public void getCfReportRowAddDesc() {
		// TODO Auto-generated method stub
		String sql="select * from cfreportrowadddesc ";
		List<?> result=impcfReportRowAddDescDao.queryBysql(sql);
		//表内指标
		map.clear();
	    for(int i=0;i<result.size();i++){
	    	Map<?,?> m=(Map<?,?>)result.get(i);
			System.out.println(m.get("tableCode")+"_"+m.get("colCode")+"_"+m.get("colType"));
			map.put((String) m.get("tableCode")+"_"+m.get("colCode"),(String) m.get("colType"));
		}
	    //表外指标
		String sql_out="select * from rsys_CfReportRowAddDesc ";
		List<?> result_out=impcfReportRowAddDescDao.queryBysql(sql_out);
	    for(int i=0;i<result_out.size();i++){
	    	Map<?,?> m=(Map<?,?>)result_out.get(i);
			System.out.println(m.get("tableCode")+"_"+m.get("colCode")+"_"+m.get("colType"));
			map.put((String) m.get("tableCode")+"_"+m.get("colCode"),(String) m.get("colType"));
		}
	}
	@Transactional
	private void cfReportRowAddHis(UploadInfoDTO pld) {
		// TODO Auto-generated method stub
		String sqlhis="select * from cfreportrowadddata where yearmonth='"+
		              pld.getYearmonth()+"' and quarter='"+
				      pld.getQuarter()+"' and reportrate='"+
		              pld.getReporttype()+"' and reporttype='"+
				      pld.getReportname()+"' and reportid='"+
 pld.getReportId() + "'";
		System.out.println(sqlhis);
		List<?> hisresult=impcfReportRowAddDataDao.queryBySQL(sqlhis);
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
		String lsql="delete from cfreportrowadddata where yearmonth='"+pld.getYearmonth()
					+"' and quarter='"+pld.getQuarter()
					+"' and reportrate='"+pld.getReporttype()
					+"' and reporttype='"+pld.getReportname()
					+"' and reportid='"+pld.getReportId()
 + "'";
		try{
			dao.excute(lsql);
		}catch(Exception e){
			LOGGER.error("删除历史数据出错！sql="+lsql);
        	throw new RuntimeException("删除历史数据出错！");
		}
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
	
	private void validateNumberFormat(List<Row> list, UploadInfoDTO pld) {
		for (int i = 0; i < list.size(); i++) {
			Row row = list.get(i);
			if (row.getCell(0) == null) {
				System.out.println("空单元格");
				continue;
			}
			String strTemp = (String) mapDecimals.get(row.getCell(0)
					.getStringCellValue());// 可能空指针异常
			
			if (strTemp == null) {
				System.out.println("指标描述表未查到该因子代码");
				continue;
			}
			// 输出数据类型和值
			System.out.println("---------数据格式校验1---------"+row.getCell(3).getStringCellValue());
			System.out.println("---------数据格式校验2----------"+ExcelUtil.getCellValue(row.getCell(2)));
			
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
							|| SaveDataService.STR.equals(cellValue)) {
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
							|| SaveDataService.STR.equals(cellValue)
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
							|| SaveDataService.STR.equals(cellValue)
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
			String strTemp = (String) mapDecimals.get(row.getCell(0)
					.getStringCellValue());// 可能空指针异常
			if (strTemp == null) {
				System.out.println("指标描述表未查到该因子代码");
				continue;
			}
			if(row.getCell(0).getStringCellValue().equals("CR21_00018")){
				System.out.println("");
			}
			String cellValue = ExcelUtil.getCellValue(row.getCell(2));
			// 输出数据类型和值
			System.out.println("---------精度校验1----------"+row.getCell(3).getStringCellValue());
			System.out.println("---------精度校验2----------"+cellValue);
			
			String temp[] = strTemp.split("_",-1);
			String strOutItemType = temp[0];
			String decimalsStr = temp[1];
			int decimals = Integer.parseInt(StringUtils.isEmpty(decimalsStr) || "null".equals(decimalsStr)? "0": decimalsStr);//精度
			// 百分比型 ，数量型  ，数值型
			if (strOutItemType.equals("04")||strOutItemType.equals("05")||strOutItemType.equals("06")) {
				if ("".equals(cellValue)
						|| "--".equals(cellValue)
								|| "-".equals(cellValue)
								|| SaveDataService.STR.equals(cellValue)) {
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
