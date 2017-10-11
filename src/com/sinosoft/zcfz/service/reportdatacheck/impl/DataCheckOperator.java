package com.sinosoft.zcfz.service.reportdatacheck.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sinosoft.abc.entity.Invest_ABC;
import com.sinosoft.zcfz.dao.reportdatacheck.DataCheckDao;
import com.sinosoft.zcfz.dto.reportdatacheck.DataCheckDto;
import com.sinosoft.entity.CalCfReportInfo;
import com.sinosoft.entity.CfReportDataCheck;
import com.sinosoft.entity.CfReportErrInfo;
import com.sinosoft.entity.CfReportItemRowCheck;
import com.sinosoft.entity.CfReportPeriodDataCheck;
import com.sinosoft.entity.CfReportRowAddData;
import com.sinosoft.entity.CfReportRowDataCheck;
import com.sinosoft.util.Config;

/**
 * 数据检验相关操作
 *
 * @author liucw 2016年3月23日
 */
public class DataCheckOperator {
	@Resource
	public DataCheckDao dataCheckDao;
	private Log log = LogFactory.getLog(DataCheckOperator.class);

	private DecimalFormat decimalFormat = new DecimalFormat("0.00"); // 数字转换对象

	private static final String ERR_LEVEL = "error";
	private static final String WARN_LEVEL = "warn";
	private String quarter_Bq = "";  //本期月度
	private String quarter_Sq = "";  //上期月度
	private String yearMonth_Bq = ""; // 本期年度
	private String yearMonth_Sq = ""; // 上期年度 
	private String reportRate = ""; // 报送频度:1、季度快报； 2、季度报告； 3、临时报告
	private String isNeedChk = ""; // 校验公式标识：1、季度快报； 2、季度报告； 3、临时报告
	private String companyType = com.sinosoft.util.Config.getProperty("CompanyType");;// 校验公司标示：ALL-所有公司、L-寿险公司、P-财险公司、R-再保险公司
	private String comCode = com.sinosoft.util.Config.getProperty("OrganCode");
	private String reportId="";
	private Map<String, BigDecimal> hashMap_Item_Bq = new HashMap<String, BigDecimal>();// 本期因子指标数据
	private Map<String, BigDecimal> mHashMap_Item_Sq = new HashMap<String, BigDecimal>();// 上期因子指标数据

	public DataCheckOperator() {}

	/**
	 * 数据检验具体操作操作
	 * 
	 * @param dataCheckDto 前端条件参数，包含报告类型、年度、季度
	 */
	public boolean dataCheckOperator (DataCheckDto dataCheckDto) {
		reportId=dataCheckDto.getReportId();
		System.out.println("reportId-->"+reportId);
		CalCfReportInfo calCfReportInfo=dataCheckDao.get(CalCfReportInfo.class, reportId);
		if(calCfReportInfo==null||"".equals(calCfReportInfo)){
			log.error("报送号"+reportId+"不存在!");
			return false;
		}
		dataCheckDto.setReporttype(calCfReportInfo.getReportType());
		dataCheckDto.setYear(calCfReportInfo.getYear());
		dataCheckDto.setQuarter(calCfReportInfo.getQuarter());
		// 将前台传过来的参数转为程序真正需要处理的参数
		if (!getActualCheckParam(dataCheckDto)) {
			log.error("获取前端传过来的参数失败！！！");
			return false;
		}

		// 获取本期和上期因子指标数据
		if (!getFactorIndexData()) {
			log.error("获取本期或者上期因子指标数据出错！！！");
			return false;
		}

		// 删除对应的错误信息//删除按年和季度删
		if (!dataCheckDao.deleteErrInfo(dataCheckDto)) {
			log.error("删除当前条件下的错误信息出错！！！");
			return false;
		}


		// 校验因子指标数据
		try {
			checkItemCode();
		} catch (Exception e) {
			System.out.println("1");
			e.printStackTrace();
		}
		
		// 校验因子与行可扩展表数据
		try {
			checkItemRow();
		} catch (Exception e) {
			System.out.println("2");
			e.printStackTrace();
		}
		// 校验行可扩展表数据列
		try {
			checkRowData();
		} catch (Exception e) {
			System.out.println("3");
			e.printStackTrace();
		}
				
		// 校验跨期间因子指标数据
//		checkPeriodItem();  2.0版本暂时不检验期间
		
		//检验信用评级、修正久期与基础因子RFO
		
		try {
			checkRFO() ;
		} catch (Exception e) {
			System.out.println("4");
			e.printStackTrace();
		}
		
		
		return true;
	}
	/**
	 * 初步校验
	 */
	public boolean initialCheckOperate(DataCheckDto dataCheckDto) {
		String reportId=dataCheckDto.getReportId();
		CalCfReportInfo calCfReportInfo=dataCheckDao.get(CalCfReportInfo.class, reportId);
		if(calCfReportInfo==null||"".equals(calCfReportInfo)){
			log.error("报送号"+reportId+"不存在!");
			return false;
		}
		
		// 删除对应的错误信息
		if (!dataCheckDao.deleteInitErrInfo(dataCheckDto)) {
			log.error("删除当前条件下的错误信息出错！！！");
			return false;
		}
		
		//对接口数据进行检验
		List<Invest_ABC> MR09 = dataCheckDao.getMR09(dataCheckDto.getYear(), dataCheckDto.getQuarter());
		//系统对市值（认可价值＝市值）为0的进行报错及提示，之后手工维护；                                                                                                                   
		for (int i = 0; i < MR09.size(); i++) {
			BigDecimal numValue = MR09.get(i).getRecognitionValue() ;
			if (numValue.doubleValue() == 0.0) {
				
				String errorInfoString = "初步检验错误信息: 接口数据《MR09-股票》 这张表第 "+ (i+1) + " 行的市值为0，下载带数据的模板后会将该行数据剔除！";
				log.error(errorInfoString);
				PrepareErrLog1(ERR_LEVEL,errorInfoString,dataCheckDto.getYear(), dataCheckDto.getQuarter(),"2",reportId);
			}
		}
		
		List<CfReportRowAddData> MR20F00004 = dataCheckDao.getMR20F00004(dataCheckDto.getYear(), dataCheckDto.getQuarter(),"2");
		//系统对市值（认可价值＝市值）为0的进行报错及提示，之后手工维护；                                                                                                                   
		for (int i = 0; i < MR20F00004.size(); i++) {
			BigDecimal numValue = MR20F00004.get(i).getNumValue() ;
			if (numValue.doubleValue() == 0.0) {
				
				String errorInfoString ="初步检验错误信息: 季度报告 " + MR20F00004.get(i).getTableCode() + " 这张表第 "+ (i+1) + " 行的认可价值为0，请下载模板后手工维护！";
				log.error(errorInfoString);
				PrepareErrLog1(ERR_LEVEL,errorInfoString, dataCheckDto.getYear(), dataCheckDto.getQuarter(),"2",reportId);
			}
		}
		
		List<CfReportRowAddData> MR09F00007 = dataCheckDao.getMR09F00007(dataCheckDto.getYear(), dataCheckDto.getQuarter(),"2");
		//系统对市值（认可价值＝市值）为0的进行报错及提示，之后手工维护；                                                                                                                   
		for (int i = 0; i < MR09F00007.size(); i++) {
			BigDecimal numValue = MR09F00007.get(i).getNumValue() ;
			if (numValue.doubleValue() == 0.0) {
				
				String errorInfoString = "初步检验错误信息: 季度报告" +MR09F00007.get(i).getTableCode() + " 这张表第 "+ (i+1) + " 行的认可价值为0，请下载模板后手工维护！";
				log.error(errorInfoString);
				PrepareErrLog1(ERR_LEVEL,errorInfoString,dataCheckDto.getYear(), dataCheckDto.getQuarter(),"2",reportId);
			}
		}

		return true;
	}
	/**
	 * 检验信用评级、修正久期与基础因子RFO
	 * @return
	 */
	private boolean checkRFO() {
		log.info("------------------------开始检验信用评级、修正久期与基础因子RFO-----------------");
		//信用凭级的数据
		List<CfReportRowAddData> creditRating2  = dataCheckDao.getItemValue(reportId, yearMonth_Bq, quarter_Bq, reportRate, "CR01_02_F00003");
		List<CfReportRowAddData> creditRating3  = dataCheckDao.getItemValue(reportId, yearMonth_Bq, quarter_Bq, reportRate, "CR01_03_F00003");
		List<CfReportRowAddData> creditRating4  = dataCheckDao.getItemValue(reportId, yearMonth_Bq, quarter_Bq, reportRate, "CR01_04_F00003");
		List<CfReportRowAddData> creditRating5  = dataCheckDao.getItemValue(reportId, yearMonth_Bq, quarter_Bq, reportRate, "CR01_05_F00003");

		//修正久期
		List<CfReportRowAddData>  modifiedDuration1  = dataCheckDao.getItemValue(reportId,yearMonth_Bq, quarter_Bq, reportRate, "CR01_01_F00004");
		List<CfReportRowAddData>  modifiedDuration2  = dataCheckDao.getItemValue(reportId,yearMonth_Bq, quarter_Bq, reportRate, "CR01_02_F00004");
		List<CfReportRowAddData>  modifiedDuration3  = dataCheckDao.getItemValue(reportId,yearMonth_Bq, quarter_Bq, reportRate, "CR01_03_F00004");
		List<CfReportRowAddData>  modifiedDuration4  = dataCheckDao.getItemValue(reportId,yearMonth_Bq, quarter_Bq, reportRate, "CR01_04_F00004");
		List<CfReportRowAddData>  modifiedDuration5  = dataCheckDao.getItemValue(reportId,yearMonth_Bq, quarter_Bq, reportRate, "CR01_05_F00004");

		//RFO
		List<CfReportRowAddData>  RFOList1  = dataCheckDao.getItemValue(reportId,yearMonth_Bq, quarter_Bq, reportRate, "CR01_01_F00007");
		List<CfReportRowAddData>  RFOList2  = dataCheckDao.getItemValue(reportId,yearMonth_Bq, quarter_Bq, reportRate, "CR01_02_F00007");
		List<CfReportRowAddData>  RFOList3  = dataCheckDao.getItemValue(reportId,yearMonth_Bq, quarter_Bq, reportRate, "CR01_03_F00007");
		List<CfReportRowAddData>  RFOList4  = dataCheckDao.getItemValue(reportId,yearMonth_Bq, quarter_Bq, reportRate, "CR01_04_F00007");
		List<CfReportRowAddData>  RFOList5  = dataCheckDao.getItemValue(reportId,yearMonth_Bq, quarter_Bq, reportRate, "CR01_05_F00007");

		BigDecimal zero = new BigDecimal("0");
		DecimalFormat decimalFormat = new DecimalFormat("0.0000"); // 数字转换对象
		
		//CR01_01的关系 IF(E7="",0,(IF(E7<=5,E7*(-0.0012*E7+0.012),E7*0.006)))
		for (int i = 0; i < modifiedDuration1.size(); i++) {
			double H7 = Double.parseDouble(decimalFormat.format(RFOList1.get(i).getNumValue()));  //基础因子RF0
			double E7 = Double.parseDouble(decimalFormat.format(modifiedDuration1.get(i).getNumValue()))  ;  //修正久期

			if (modifiedDuration1.get(i).getNumValue() == null || "".equals(modifiedDuration1.get(i).getNumValue())) {				
				if (RFOList1.get(i).getNumValue() != zero) {
					log.error("检验信用评级、修正久期与基础因子RFO, 错误在CR01_01里的：第"+ (i+1)+"行");
					
					String errorInfoString = "检验信用评级、修正久期与基础因子RFO关系出错, 错误在CR01_01里的：第 "+ (i+1)+" 行。如果修正久期为空，则RFO应为0";
					PrepareErrLog(ERR_LEVEL,errorInfoString);
				}
				
			} else  {			
				if (E7 <= 5 ) {
					double result1 = -0.0012 * E7 + 0.012;
					double result = Double.parseDouble(decimalFormat.format(result1 * E7));
					if (H7 != result) {
						log.error("检验信用评级、修正久期与基础因子RFO, 错误在CR01_01里的：第"+ (i+1)+"行");
						
						String errorInfoString = "检验信用评级、修正久期与基础因子RFO关系出错, 错误在CR01_01里的：第 "+ (i+1)+" 行。如果修正久期值为："
						                           + modifiedDuration1.get(i).getNumValue() +",则RFO的值应为：" +result+", 而不是："+H7;
						PrepareErrLog(ERR_LEVEL,errorInfoString);
					}
					
				} else {
					double result1= Double.parseDouble(decimalFormat.format(E7 * 0.006));	
				    String result2 = new BigDecimal(result1).toPlainString();  //科学记数转数字
				    double result = Double.parseDouble(result2);
					if (H7 != result) {
						log.error("检验信用评级、修正久期与基础因子RFO, 错误在CR01_01里的：第"+ (i+1)+"行");
						
						String errorInfoString = "检验信用评级、修正久期与基础因子RFO关系出错, 错误在CR01_01里的：第 "+ (i+1)+" 行。如果修正久期值为："
						                           + modifiedDuration1.get(i).getNumValue() +",则RFO的值应为：" +result+", 而不是："+H7;
						PrepareErrLog(ERR_LEVEL,errorInfoString);
					}
				}

			}
		}
		
		//CR01_02
		checkCR01_02(creditRating2 , modifiedDuration2 , RFOList2, decimalFormat, "CR01_02");	
		checkCR01_02(creditRating3 , modifiedDuration3 , RFOList3, decimalFormat, "CR01_03");	
		checkCR01_02(creditRating4 , modifiedDuration4 , RFOList4, decimalFormat, "CR01_04");	
		checkCR01_02(creditRating5 , modifiedDuration5 , RFOList5, decimalFormat, "CR01_05");	
		
		log.info("------------------------结束检验信用评级、修正久期与基础因子RFO-----------------");

		return true;
	}
	
	private boolean checkCR01_02(List<CfReportRowAddData> creditRating2 ,List<CfReportRowAddData> modifiedDuration2 ,
			List<CfReportRowAddData> RFOList2 ,DecimalFormat decimalFormat, String sheetName) {
		for (int i = 0; i < creditRating2.size(); i++) {
			String D6 = creditRating2.get(i).getTextValue(); //信用评级
			double E6 = Double.parseDouble(decimalFormat.format(modifiedDuration2.get(i).getNumValue()))  ;  //修正久期
			double H6 = Double.parseDouble(decimalFormat.format(RFOList2.get(i).getNumValue()));  //基础因子RF0
			
			if (D6.equals("1|AAA") && E6 <= 5) {
				double result1 = -0.0015 * E6+ 0.0175;
				double result = Double.parseDouble(decimalFormat.format(result1 * E6));
				if (H6 != result) {
					log.error("检验信用评级、修正久期与基础因子RFO, 错误在 " +sheetName+" 里的：第"+ (i+1)+"行");
					
					String errorInfoString = "检验信用评级、修正久期与基础因子RFO关系出错, 错误在 " +sheetName+" 里的：第 "+ (i+1)+" 行。"
							+ "如果信用评级为：" + D6 +", 修正久期值为："+ E6 +", 则RFO的值应为：" + result +", 而不是："+H6;
					PrepareErrLog(ERR_LEVEL,errorInfoString);
				}
				
			} else if (D6.equals("1|AAA") && E6 > 5) {				
				double result = Double.parseDouble(decimalFormat.format(0.01 * E6));
				if (H6 != result) {
					log.error("检验信用评级、修正久期与基础因子RFO, 错误在 " +sheetName+" 里的：第"+ (i+1)+"行");
					
					String errorInfoString = "检验信用评级、修正久期与基础因子RFO关系出错, 错误在 " +sheetName+" 里的：第 "+ (i+1)+" 行。"
							+ "如果信用评级为：" + D6 +", 修正久期值为："+ E6 +", 则RFO的值应为：" + result +", 而不是："+H6;
					PrepareErrLog(ERR_LEVEL,errorInfoString);
				}
				
			} else if (D6.equals("2|AA+") && E6 <= 5) {				
				double result1 = -0.0014 * E6 + 0.018;
				double result = Double.parseDouble(decimalFormat.format(result1 * E6));
				if (H6 != result) {
					log.error("检验信用评级、修正久期与基础因子RFO, 错误在 " +sheetName+" 里的：第"+ (i+1)+"行");
					
					String errorInfoString = "检验信用评级、修正久期与基础因子RFO关系出错, 错误在 " +sheetName+" 里的：第 "+ (i+1)+" 行。"
							+ "如果信用评级为：" + D6 +", 修正久期值为："+ E6 +", 则RFO的值应为：" + result +", 而不是："+H6;
					PrepareErrLog(ERR_LEVEL,errorInfoString);
				}
				
			} else if (D6.equals("2|AA+") && E6 > 5) {				
				double result = Double.parseDouble(decimalFormat.format(0.011 * E6));
				if (H6 != result) {
					log.error("检验信用评级、修正久期与基础因子RFO, 错误在 " +sheetName+" 里的：第"+ (i+1)+"行");
					
					String errorInfoString = "检验信用评级、修正久期与基础因子RFO关系出错, 错误在 " +sheetName+" 里的：第 "+ (i+1)+" 行。"
							+ "如果信用评级为：" + D6 +", 修正久期值为："+ E6 +", 则RFO的值应为：" + result +", 而不是："+H6;
					PrepareErrLog(ERR_LEVEL,errorInfoString);
				}
				
			} else if (D6.equals("3|AA") && E6 <= 5) {				
		    	double result1 = -0.0013 * E6 + 0.0195;
				double result = Double.parseDouble(decimalFormat.format(result1 * E6));
				if (H6 != result) {
					if (H6 == 0.0544) {
						System.out.println("H6= " +H6);

					}
					System.out.println("H6= " +H6);
					System.out.println("result:=" + result);

//					log.error("检验信用评级、修正久期与基础因子RFO, 错误在 " +sheetName+" 里的：第"+ (i+1)+"行");
					
					String errorInfoString = "检验信用评级、修正久期与基础因子RFO关系出错, 错误在 " +sheetName+" 里的：第 "+ (i+1)+" 行。"
							+ "如果信用评级为：" + D6 +", 修正久期值为："+ E6 +", 则RFO的值应为：" + result +", 而不是："+H6;
					PrepareErrLog(ERR_LEVEL,errorInfoString);
				}
				
			} else if (D6.equals("3|AA") && E6 > 5) {				
				double result = Double.parseDouble(decimalFormat.format(0.013 * E6));
				if (H6 != result) {
					log.error("检验信用评级、修正久期与基础因子RFO, 错误在 " +sheetName+" 里的：第"+ (i+1)+"行");
					
					String errorInfoString = "检验信用评级、修正久期与基础因子RFO关系出错, 错误在 " +sheetName+" 里的：第 "+ (i+1)+" 行。"
							+ "如果信用评级为：" + D6 +", 修正久期值为："+ E6 +", 则RFO的值应为：" + result +", 而不是："+H6;
					PrepareErrLog(ERR_LEVEL,errorInfoString);
				}
				
			} else if (D6.equals("4|AA-") && E6 <= 5) {				
				double result1 = -0.0012 * E6  + 0.022;
				double result = Double.parseDouble(decimalFormat.format(result1 * E6)) ;
				if (H6  != result) {
					log.error("检验信用评级、修正久期与基础因子RFO, 错误在 " +sheetName+" 里的：第"+ (i+1)+"行");
					
					String errorInfoString = "检验信用评级、修正久期与基础因子RFO关系出错, 错误在 " +sheetName+" 里的：第 "+ (i+1)+" 行。"
							+ "如果信用评级为：" + D6 +", 修正久期值为："+ E6 +", 则RFO的值应为：" + result +", 而不是："+H6;
					PrepareErrLog(ERR_LEVEL,errorInfoString);
				}
				
			} else if (D6.equals("4|AA-") && E6  > 5) {				
				double result = Double.parseDouble(decimalFormat.format(0.016 * E6)) ;
				if (H6  != result) {
					log.error("检验信用评级、修正久期与基础因子RFO, 错误在 " +sheetName+" 里的：第"+ (i+1)+"行");
					
					String errorInfoString = "检验信用评级、修正久期与基础因子RFO关系出错, 错误在 " +sheetName+" 里的：第 "+ (i+1)+" 行。"
							+ "如果信用评级为：" + D6 +", 修正久期值为："+ E6 +", 则RFO的值应为：" + result +", 而不是："+H6;
					PrepareErrLog(ERR_LEVEL,errorInfoString);
				}
				
			} else if (D6.equals("6|A") && E6  <= 5) {				
				double result1 = -0.0017 * E6  + 0.0285;
				double result = Double.parseDouble(decimalFormat.format(result1 * E6 ));
				if (H6  != result) {
					log.error("检验信用评级、修正久期与基础因子RFO, 错误在 " +sheetName+" 里的：第"+ (i+1)+"行");
					
					String errorInfoString = "检验信用评级、修正久期与基础因子RFO关系出错, 错误在 " +sheetName+" 里的：第 "+ (i+1)+" 行。"
							+ "如果信用评级为：" + D6 +", 修正久期值为："+ E6 +", 则RFO的值应为：" + result +", 而不是："+H6;
					PrepareErrLog(ERR_LEVEL,errorInfoString);
				}
				
			} else if (D6.equals("6|A") && E6  > 5) {				
				double result = Double.parseDouble(decimalFormat.format(0.02 * E6)) ;
				if (H6  != result) {
					log.error("检验信用评级、修正久期与基础因子RFO, 错误在 " +sheetName+" 里的：第"+ (i+1)+"行");
					
					String errorInfoString = "检验信用评级、修正久期与基础因子RFO关系出错, 错误在 " +sheetName+" 里的：第 "+ (i+1)+" 行。"
							+ "如果信用评级为：" + D6 +", 修正久期值为："+ E6 +", 则RFO的值应为：" + result +", 而不是："+H6;
					PrepareErrLog(ERR_LEVEL,errorInfoString);
				}

			} else if ((D6.equals("8|BBB+") || D6.equals("9|BBB") || D6.equals("10|BBB-") || D6.equals("11|无评级"))
					&& E6  <= 5){				
				double result1 = -0.0016 * E6  + 0.0304;
				double result = Double.parseDouble(decimalFormat.format(result1 * E6 ));
				if (H6  != result) {
					log.error("检验信用评级、修正久期与基础因子RFO, 错误在 " +sheetName+" 里的：第"+ (i+1)+"行");
					
					String errorInfoString = "检验信用评级、修正久期与基础因子RFO关系出错, 错误在 " +sheetName+" 里的：第 "+ (i+1)+" 行。"
							+ "如果信用评级为：" + D6 +", 修正久期值为："+ E6 +", 则RFO的值应为：" + result +", 而不是："+H6;
					PrepareErrLog(ERR_LEVEL,errorInfoString);
				}
				
			} else if ((D6.equals("8|BBB+") || D6.equals("9|BBB") || D6.equals("10|BBB-") || D6.equals("11|无评级"))
					&& E6  > 5){					
				double result = Double.parseDouble(decimalFormat.format(0.0224 * E6)) ;
				if (H6  != result) {
					log.error("检验信用评级、修正久期与基础因子RFO, 错误在 " +sheetName+" 里的：第"+ (i+1)+"行");
					
					String errorInfoString = "检验信用评级、修正久期与基础因子RFO关系出错, 错误在 " +sheetName+" 里的：第 "+ (i+1)+" 行。"
							+ "如果信用评级为：" + D6 +", 修正久期值为："+ E6 +", 则RFO的值应为：" + result +", 而不是："+H6;
					PrepareErrLog(ERR_LEVEL,errorInfoString);
				}
				
			} else {
				if (H6  != 0.0) {
					log.error("检验信用评级、修正久期与基础因子RFO, 错误在 " +sheetName+" 里的：第"+ (i+1)+"行");
					
					String errorInfoString = "检验信用评级、修正久期与基础因子RFO关系出错, 错误在 " +sheetName+" 里的：第 "+ (i+1)+" 行。"
							+ "如果信用评级为：" + D6 +", 修正久期值为："+ E6 +", 则RFO的值应为：0, 而不是："+H6;
					PrepareErrLog(ERR_LEVEL,errorInfoString);
				}
			}
		}
		return true;
	}

	/**
	 * 将错误纪录存放到数据库中
	 */
	private boolean PrepareErrLog1(String errLevel,String errInfo, String yearMonth_Bq, 
			String quarter_Bq,String reportRate,String reportId) {
		CfReportErrInfo cfReportErrInfo = new CfReportErrInfo();
		cfReportErrInfo.setYear(yearMonth_Bq);
		cfReportErrInfo.setQuarter(quarter_Bq);
		cfReportErrInfo.setReportType(reportRate);
		cfReportErrInfo.setCheckedCode(errLevel);  //错误级别
		cfReportErrInfo.setErrorInfo(errInfo.trim());
		cfReportErrInfo.setCheckdate(new Date());
		cfReportErrInfo.setTemp(null);
		cfReportErrInfo.setComCode(Config.getProperty("OrganCode"));
		cfReportErrInfo.setTolerance(new BigDecimal("0")); //容差
		cfReportErrInfo.setReportId(reportId);
		if (reportId==null) {
			System.out.println(errInfo);
		}
		if(!dataCheckDao.insertErrorInfo(cfReportErrInfo)) {
			log.error("插入数据到数据库出错！");
		}
	
		log.error("检验出错：" + errInfo + "\n");
		return true;
	}
	
	/**
	 * 将错误纪录存放到数据库中
	 */
	private boolean PrepareErrLog(String errLevel,String errInfo) {
		CfReportErrInfo cfReportErrInfo = new CfReportErrInfo();
		cfReportErrInfo.setYear(yearMonth_Bq);
		cfReportErrInfo.setQuarter(quarter_Bq);
		cfReportErrInfo.setReportType(reportRate);
		cfReportErrInfo.setCheckedCode(errLevel);  //错误级别
		cfReportErrInfo.setErrorInfo(errInfo.trim());
		cfReportErrInfo.setCheckdate(new Date());
		cfReportErrInfo.setTemp(null);
		cfReportErrInfo.setComCode(comCode);
		cfReportErrInfo.setTolerance(new BigDecimal("0")); //容差
		cfReportErrInfo.setReportId(reportId);
		if (reportId==null) {
			System.out.println(errInfo);
		}
		if(!dataCheckDao.insertErrorInfo(cfReportErrInfo)) {
			log.error("插入数据到数据库出错！");
		}
		
		log.error("检验出错：" + errInfo + "\n");
		return true;
	}
	/**
	 * 将前端数据检验参数，转为后台实际数据检验参数
	 *
	 * @param dataCheckDto 前端条件参数，包含报告类型、年度、季度
	 */
	private boolean getActualCheckParam(DataCheckDto dataCheckDto) {
		reportRate = dataCheckDto.getReporttype();
		String year = dataCheckDto.getYear().trim();
		quarter_Bq = dataCheckDto.getQuarter();

		// 得到本期月度
		yearMonth_Bq = year;

		// 得到上期月度
		if ("1".equals(quarter_Bq)) {
			int yearInt = Integer.parseInt(year) - 1;
			yearMonth_Sq = String.valueOf(yearInt);				

		} else {			
			yearMonth_Sq = year ;
		}

		quarter_Sq = String.valueOf(Integer.parseInt(quarter_Bq) - 1);
		
		// 1、季度快报； 2、季度报告； 3、临时报告
		isNeedChk = reportRate;

		if (comCode.equals("000007")) {
			companyType = "P";

		    } else if (comCode.equals("000008")) {
				companyType = "L";

			} else if (comCode.equals("000009")) {
				companyType = "R";

			}

		return true;
	}

	/**
	 * 获取本期和上期因子指标数据,将因子代码 对应的 reportItemValue 值存放到map里
	 */
	private boolean getFactorIndexData() {
		// 每次检验前都清空数据，防止数据存储过大，耗时耗内存
		hashMap_Item_Bq.clear();
		mHashMap_Item_Sq.clear();
		
		// 获取本期因子指标数据
		List<?> bqList = dataCheckDao.getFactorIndexData(reportId,reportRate, yearMonth_Bq, quarter_Bq, comCode);
		if (bqList == null || bqList.size() <= 0) {
			log.info("没有当前条件下的的本期因子指标数据！");
			return false;
		}

		// 获取的本期因子指标数据,其中key为 因子代码 ，value为 reportItemValue
		for (int k = 0; k < bqList.size(); k++) {
			hashMap_Item_Bq.put((String)((Map) bqList.get(k)).get("outitemcode"),(BigDecimal) ((Map) bqList.get(k)).get("reportitemvalue"));
		}

		
		// 获取上期因子指标数据
		List<?> sqList = dataCheckDao.getFactorIndexData(reportId,reportRate, yearMonth_Sq, quarter_Sq ,comCode);
		if (sqList == null || sqList.size() <= 0) {
			log.info("提示：没有当前条件下的的上期因子指标数据。");
			//可能出现的情况：只有本期数据，所有不能返回false
			return true;
		}

		for (int k = 0; k < sqList.size(); k++) {
			mHashMap_Item_Sq.put((String)((Map) sqList.get(k)).get("outitemcode"),(BigDecimal) ((Map) sqList.get(k)).get("reportitemvalue"));
		}

		return true;
	}
	/**
	 * 获取算术表达式里运算符
	 *
	 * @param arithmeticExp 算术表达式
	 */
	private String getOperator(String arithmeticExp) {
		Pattern pattern = Pattern.compile("=|>|<|≥|≤");
		Matcher matcher = pattern.matcher(arithmeticExp);

		if (!matcher.find()) {
			log.error("核验等式里没有运行符=、<、≤、>、≥ ");
			return null;
		}

		// 返回运行体符
		return matcher.group();
	}

	/**
	 * 处理计算公式为连等，形如a=b=c
	 * 思路：
	 *    截取运行符之间的固定因子，将第一个固定因子的值作为比较值，之后的值都与之比较，如果有一个不同，则有错误
	 * @return
	 */
	private boolean equalsCheck(String calculateFormula, String relationOperator, String remark,BigDecimal tolerance) {
		String calculateFormula1=calculateFormula.replaceAll("#","");
		int outItemCodeNum = calculateFormula1.split(relationOperator).length; //固定因子的个数
		String[] outItemCodes = calculateFormula1.split(relationOperator);  //截取公式
		double tempValue = getSingleItemValue(outItemCodes[0], "Bq");  //将数组里的第一个值作为比较值
		
		for (int i = 1; i < outItemCodes.length; i++) {
			//获取每一个固定因子对应的值
			double outItemCodeValue = getSingleItemValue(outItemCodes[i], "Bq");
			
			//只要有一个值 不相等就有错
			if (outItemCodeValue != tempValue) {
				log.error("错误：校验因子指标数据: 请查看cfreportdatacheck表里的数据！ "+ calculateFormula);
				PrepareErrLog("校验因子指标数据出错:" , ERR_LEVEL, String.valueOf(tempValue), outItemCodes[i] + "对应的值为： " +String.valueOf(outItemCodeValue), 
						outItemCodeValue-tempValue, calculateFormula, remark ,tolerance);
				return false;
			}
		}
		return true;
		
	}
	/**
	 * 校验因子指标数据:对应cfReportDataCheck
	 */
	private boolean checkItemCode() {
		log.info("---------------开始校验因子指标数据，即cfReportDataCheck-------------------");
		List<CfReportDataCheck> list = dataCheckDao.getRepDataChecked(isNeedChk, companyType);
		if (list == null || list.size() <= 0) {
			log.info("提示：当前检验条件下，cfreportdatacheck表里没有检验数据,不进行检验");
			return false;
		}

		for (int i = 0; i < list.size(); i++) {
			// 获取计算公式
			String calculateFormula = list.get(i).getCalFormula();
			String remark = list.get(i).getRemark();
			BigDecimal tolerance = list.get(i).getTolerance();  //容差

			// 获取关系运算符
			String relationOperator = getOperator(calculateFormula);
			if (relationOperator == null) {
				log.error("请修改cfreportdatacheck表里的检验式： "+ calculateFormula );
				return false;
			}
			//计算公式是否为连等
			if (calculateFormula.split(relationOperator).length >2) {
				equalsCheck(calculateFormula,  relationOperator, remark, tolerance);
				continue;
			}

			// 获取公式左边计算公式
			String leftCalculateFormula = calculateFormula.substring(0, calculateFormula.indexOf(relationOperator));
			// 获取左边计算式的值
			double leftValue = getValue(leftCalculateFormula.replaceAll(" ", ""), "Bq");

			// 获取公式右边
			String rightCalculateFormula = calculateFormula.substring(calculateFormula.indexOf(relationOperator) + 1);
			double rightValue = getValue(rightCalculateFormula.replaceAll(" ", ""), "Bq");			

			String tLeftValue = decimalFormat.format(leftValue);
			String tRightValue = decimalFormat.format(rightValue);
			//得到左值 减 右值 的差值
			double leftMinusRight = Double.parseDouble(decimalFormat.format(leftValue - rightValue));
		
			// 将错误信息存放到数据库里
			if ( Math.abs(leftMinusRight) > tolerance.doubleValue()) {
				log.error("错误：校验因子指标数据: 请查看cfreportdatacheck表里的数据！ "+ calculateFormula);
				PrepareErrLog("校验因子指标数据出错：" , ERR_LEVEL, tLeftValue, tRightValue, leftMinusRight,
						calculateFormula, list.get(i).getRemark(),tolerance);
			
				//警告信息
			} else if((Math.abs(leftMinusRight) > 0 )&& (Math.abs(leftMinusRight) <= tolerance.doubleValue())){
				log.warn("警告：校验因子指标数据: 请查看cfreportdatacheck表里的数据！ "+ calculateFormula);
				PrepareErrLog("校验因子指标数据警告：" ,WARN_LEVEL, tLeftValue, tRightValue, leftMinusRight,
						calculateFormula, list.get(i).getRemark(),tolerance);
			}

		}

		log.info("----------------------校验因子指标数据结束--------------------------");
		return true;
	}

	/**
	 * 校验因子与行可扩展表数据:对应CfReportItemRowCheck
	 */
	private boolean checkItemRow() {
		log.info("------------------开始校验因子与行可扩展表数据，即CfReportItemRowCheck！------------------");
		List<CfReportItemRowCheck> list = dataCheckDao.getRepItemRowChecked(isNeedChk, companyType);
		if (list == null || list.size() <= 0) {
			log.info("-----------提示：当前检验条件下，CfReportItemRowCheck表里没有检验数据,不进行检验---------------");
			return false;
		}

		for (int i = 0; i < list.size(); i++) {
			CfReportItemRowCheck cfschema = list.get(i);
			String tableCode = cfschema.getTableCode();//  表代码
			String colCode = cfschema.getColCode(); // 列代码
			BigDecimal tolerance = list.get(i).getTolerance();  //容差

			// cfschema.getReportItemCode的因子代码和其对应的值放在cfreportdata表里
			double itemCodeValue = getSingleItemValue(cfschema.getReportItemCode(), "Bq");
			if (itemCodeValue == -1) {
				log.error("数据表CfReportItemRowCheck中没有条件下的固定因子: " + cfschema.getReportItemCode());
				continue;
			}
			
			// 如果rowDataValue为null，需要考虑异常
			Map rowDataValue = (Map) dataCheckDao.
					getSum(reportId, quarter_Bq, yearMonth_Bq, reportRate, colCode, tableCode, comCode).get(0);
			if (rowDataValue.get("SUM(NUMVALUE)") == null || rowDataValue.size() <= 0) {
				log.info("CfReportRowAddData中缺少数据,求和为null!!!");
				continue;
				//return false;
			}
			
			BigDecimal rowdata1 = (BigDecimal) rowDataValue.get("SUM(NUMVALUE)");
			double rowdata = rowdata1.doubleValue();

			double differ = Double.parseDouble(decimalFormat.format(itemCodeValue - rowdata));
			
			// 将错误信息存放到数据库里
			if (Math.abs(differ) > tolerance.doubleValue()) {
				
				log.error("错误： 校验因子与行可扩展表数据: 请查看CfReportItemRowCheck表. 该表里的固定因子：" + cfschema.getReportItemCode() + "在的cfreportdata值是：" +itemCodeValue
						+ "。行可扩展表数据的查询条件为：colCode: " + colCode + ", tableCode: " + tableCode + "等条件的求cfreportrowadddata表的和：" + rowdata);
				PrepareErrLog("校验因子与行可扩展表数据出错:" , ERR_LEVEL, decimalFormat.format(itemCodeValue),
						decimalFormat.format(rowdata), differ, cfschema.getReportItemCode() + "因子=∑"
								+ cfschema.getTableCode() + "表" + cfschema.getColCode() + "列",
						cfschema.getRemark(),tolerance);
				
				//警告信息
			}else if((Math.abs(differ) > 0) && (Math.abs(differ) <= tolerance.doubleValue())) {
				log.error("警告：校验因子与行可扩展表数据 : 请查看CfReportItemRowCheck表. 该表里的固定因子：" + cfschema.getReportItemCode() + "在的cfreportdata值是：" +itemCodeValue
						+ "。行可扩展表数据的查询条件为：colCode: " + colCode + ", tableCode: " + tableCode + "等条件的求cfreportrowadddata表的和：" + rowdata);
				PrepareErrLog("校验因子与行可扩展表数据警告:" ,WARN_LEVEL, decimalFormat.format(itemCodeValue),
						decimalFormat.format(rowdata), differ, cfschema.getReportItemCode() + "因子=∑"
								+ cfschema.getTableCode() + "表" + cfschema.getColCode() + "列",
						cfschema.getRemark(),tolerance);
			}
		}
		log.info("------------------------校验因子与行可扩展表数据结束！---------------------");
		return true;
	}

	/**这是现在的检验！！！（这里计算结果 如果有误差，而且误差很小，原因是Excel文件的数据的实际数据与数据库保留的数据不一样引起的。）
	 * 校验行可扩展表数据列：CfReportRowDataCheck 
	 * 这个检验的算法思想： 
	 * 1、根据关系运算符"=、<、≤、>、≥",将计算式分成左右
	 * 2、然后根据因子代码一个一个来求值w
	 */
	private boolean checkRowData() {		
		List<CfReportRowDataCheck> list = dataCheckDao.getRepRowDataChecked(isNeedChk, companyType);
		if (list == null || list.size() <= 0) {
			log.info("-----------提示：当前检验条件下，CfReportRowDataCheck表里没有检验数据,不进行检验---------------");
			return false;
		}

		log.info("------------------开始校验行可扩展表数据列，即CfReportRowDataCheck！-----------------------");
		for (int i = 0; i < list.size(); i++) {
			CfReportRowDataCheck cfschema = list.get(i);
			String tableCode = cfschema.getTableCode();
			String calculateFormula = cfschema.getCalFormula();
			BigDecimal tolerance = list.get(i).getTolerance();  //容差

			// 获取关系运算符
			String relationOperator = getOperator(calculateFormula);
			if (relationOperator == null) {
				log.error("请修改CfReportRowDataCheck表里的检验式： "+ calculateFormula );
				return false;
			}			
			
			// 获取关系运算符在表达式的位置
			int operatorIndex = calculateFormula.indexOf(relationOperator);
			
			// 获取关系运算符左边的表达式
			String leftCalculateFormula = calculateFormula.substring(0, operatorIndex);
			String[] leftFormula = leftCalculateFormula.replaceAll(" ", "").split("\\*" );
			
			// 获取关系运算符右边的表达式
			String rightCalculateFormula = calculateFormula.substring(operatorIndex + 1);
			String[] rightFormula = rightCalculateFormula.replaceAll(" ", "").split("\\*" );		
		
			
			List<?> rowNoCount = dataCheckDao.getRowCount(reportId,tableCode, quarter_Bq, yearMonth_Bq, reportRate, comCode);
			
			long rows = (Long) rowNoCount.get(0);
			if (rows == 0) {
				log.info("没有当前检验公式下的固定因子,即行可扩展行没有数据！查看表代码为："+tableCode);
				continue;
			}
			for (int rowNo = 1; rowNo <= rows; rowNo++) {
				BigDecimal leftSum = new BigDecimal("1") ; 
				BigDecimal rightSum = new BigDecimal("1") ;
			
				//求等式左边的和
				leftSum = getRowDataFormulaSum(leftSum, leftFormula, rowNo);
				//保留2位小数
				double  leftSumDouble  =  leftSum.setScale(2,  BigDecimal.ROUND_HALF_UP).doubleValue(); 
				
				if(calculateFormula.contains("/")){
					getDivFormulaSum(rightCalculateFormula, rightSum, rowNo);
					continue;
				}
				
				
				//求等式右边的和
				rightSum = getRowDataFormulaSum(rightSum, rightFormula, rowNo);
				//保留2位小数
				double  rightSumDouble  =  rightSum.setScale(2,  BigDecimal.ROUND_HALF_UP).doubleValue(); 
				
				double differ = leftSumDouble - rightSumDouble;
				
				//BigDecimal differ = leftSum.subtract(rightSum);				

				// 将错误信息存放到数据库里
				if (Math.abs(differ) > tolerance.doubleValue()) {					
					log.error("校验行可扩展表数据列出错：请查看CfReportRowDataCheck表里的检验关系 ： " + calculateFormula +" 表代码为："+tableCode + " 行可扩展第 " + rowNo + "行比较出错");
					PrepareErrLog("校验行可扩展表数据列出错:", ERR_LEVEL, decimalFormat.format(leftSum),
							decimalFormat.format(rightSum), differ, cfschema.getCalFormula(),
							cfschema.getRemark(),tolerance);
					
					//警告信息
				} else if((Math.abs(differ) > 0) && (Math.abs(differ) <= tolerance.doubleValue()))  {
					log.error("警告： 校验行可扩展表数据列：请查看CfReportRowDataCheck表里的检验关系 ： " + calculateFormula +" 表代码为："+tableCode + " 行可扩展第 " + rowNo + "行比较出错");
					PrepareErrLog("校验行可扩展表数据列警告:" ,WARN_LEVEL, decimalFormat.format(leftSum),
							decimalFormat.format(rightSum), differ, cfschema.getCalFormula(),
							cfschema.getRemark(),tolerance);
				}
			}
			
		
	}
		log.info("---------------------校验行可扩展表数据列结束！-------------------------------");
		return true;
	}

    //求CfReportRowDataCheck检验公式的左或右一边的值
    private BigDecimal getRowDataFormulaSum(BigDecimal rightSum, String[] rightFormula,  int rowNo) {
        for (int rightRow = 0; rightRow< rightFormula.length; rightRow++) {
        	
            if (rightFormula[rightRow].contains("+")) {
                String valueStr = rightFormula[rightRow].replaceAll("\\(|\\)||\\（||\\）", "");
                String[] addFormula = valueStr.split("\\+");
                BigDecimal singleValueSum = new BigDecimal("0");
             
                //求括号里的+
                for (int a = 0; a < addFormula.length; a++) {
                    if (addFormula[a].equals("1")) {
                        singleValueSum = singleValueSum.add(new BigDecimal("1"));
                    } else{
                        List<CfReportRowAddData> valueList = dataCheckDao.getSingleRowItemValue(reportId,quarter_Bq, yearMonth_Bq, reportRate, addFormula[a],rowNo, comCode);
                        BigDecimal value1;
                        if(valueList==null || valueList.size()==0){
                            value1 = new BigDecimal("0");
                        }else {
                            value1 = valueList.get(0).getNumValue();
                        }
                        singleValueSum = singleValueSum.add(value1);
                    }
                }
                //相乘
                rightSum = rightSum.multiply(singleValueSum);
            } else{
                List<CfReportRowAddData> valueList = dataCheckDao.getSingleRowItemValue(reportId, quarter_Bq, yearMonth_Bq, reportRate, rightFormula[rightRow],rowNo, comCode);
                BigDecimal value;
                if(valueList==null || valueList.size()==0){
                    value = new BigDecimal("0");;
                }else {
                    value = valueList.get(0).getNumValue();
                }

                rightSum = rightSum.multiply(value);
            }
        }
        return rightSum;
    }
    
    //求CfReportRowDataCheck检验公式的左或右一边的值 CR09_F00004/(CR09_F00002+CR09_F00003)
    private BigDecimal getDivFormulaSum(String rightCalculateFormula, BigDecimal rightSum, int rowNo) {
    	String[] rightFormula = rightCalculateFormula.replaceAll(" ", "").split("/" );
    	
    	//求CR09_F00004的值 
    	List<CfReportRowAddData> valueList = dataCheckDao.getSingleRowItemValue(reportId,quarter_Bq, yearMonth_Bq, reportRate, rightFormula[0],rowNo, comCode);
        BigDecimal value1, addv0,addv1;
        if(valueList==null || valueList.size()==0){
            value1 = new BigDecimal("0");
        }else {
            value1 = valueList.get(0).getNumValue();
        }
        
        //(CR09_F00002+CR09_F00003)
    	String valueStr = rightFormula[1].replaceAll("\\(|\\)||\\（||\\）", "");
        String[] addFormula = valueStr.split("\\+");
        
        List<CfReportRowAddData> addFormula0 = dataCheckDao.getSingleRowItemValue(reportId, quarter_Bq, yearMonth_Bq, reportRate, addFormula[0],rowNo, comCode);
        if(addFormula0==null || addFormula0.size()==0){
        	addv0 = new BigDecimal("0");
        }else {
        	addv0 = addFormula0.get(0).getNumValue();
        }
        
        List<CfReportRowAddData> addFormula1 = dataCheckDao.getSingleRowItemValue(reportId, quarter_Bq, yearMonth_Bq, reportRate, addFormula[1],rowNo, comCode);
        if(addFormula1==null || addFormula1.size()==0){
        	addv1 = new BigDecimal("0");
        }else {
        	addv1 = addFormula1.get(0).getNumValue();
        }
        
        
        BigDecimal addValueSum = addv0.add(addv1);
        try {
            rightSum=value1.divide(addValueSum, 4, BigDecimal.ROUND_HALF_UP);

		} catch (Exception e) {
			log.error("出现无限不循环小数");	
			e.printStackTrace();
				
			}
        		
        return rightSum;
    }
	
	/**这是以前的检验！！！
	 * 校验行可扩展表数据列：CfReportRowDataCheck 
	 * 这个检验的算法思想： 
	 * 1、根据关系运算符"=、<、≤、>、≥",将计算式分成左右
	 * 2、然后分别求算式两边，根据"+","-"，将式子拆开，相加的放一个数组，被减的放一个数组。左右相同
	 * 3、然后将数组里的因子代码拼接，使用in,并在数据库里里查询求和 
	 * 4、左右再边的值相比较时，需要注意，左右的行数可能不同，需要处理
	 */
/*	private boolean checkRowData() {
		log.info("开始校验行可扩展表数据列，即CfReportRowDataCheck！");
		List<CfReportRowDataCheck> list = dataCheckDao.getRepRowDataChecked(isNeedChk, companyType);
		if (list == null || list.size() == 0) {
			log.error("提示：当前检验条件下，CfReportRowDataCheck表里没有检验数据,不进行检验");
			return false;
		}

		for (int i = 0; i < list.size(); i++) {
			CfReportRowDataCheck cfschema = list.get(i);
			String tableCode = cfschema.getTableCode();
			String calculateFormula = cfschema.getCalFormula();

			// 获取关系运算符
			String relationOperator = getOperator(calculateFormula);
			if (relationOperator == null) {
				log.error("请修改CfReportRowDataCheck表里的检验式： "+ calculateFormula );
				return false;
			}
			
			// 获取关系运算符在表达式的位置
			int operatorIndex = calculateFormula.indexOf(relationOperator);

			// 获取关系运算符左边的表达式
			String leftCalculateFormula = calculateFormula.substring(0, operatorIndex);
			String[] leftAdd = getFormula(leftCalculateFormula, "+"); // 这里存放的拆开左边表达式后用于进行相加的多个因子代码
			String[] leftMinus = getFormula(leftCalculateFormula, "-"); // ...相减...（需要注意，这里多个因子代码前面没有负号）

			// 获取关系运算符右边的表达式
			String rightCalculateFormula = calculateFormula.substring(operatorIndex + 1);
			String[] tRightAdd = getFormula(rightCalculateFormula, "+");
			String[] tRightMun = getFormula(rightCalculateFormula, "-");

			// 获取多个因子对应的值的和，key是ROWNO，value是SUM(NUMVALUE)
			Map<Integer, BigDecimal> tleftplusre = getOutItemCodesSum(leftAdd, tableCode);
			Map<Integer, BigDecimal> tLeftminre = getOutItemCodesSum(leftMinus, tableCode); // 虽然是求减的和，但这里是正数
			Map<Integer, BigDecimal> tRightplusre = getOutItemCodesSum(tRightAdd, tableCode);
			Map<Integer, BigDecimal> tRightminre = getOutItemCodesSum(tRightMun, tableCode);

			if (tleftplusre.size() == 0 && tLeftminre.size() == 0 && tRightplusre.size() == 0
					&& tRightminre.size() == 0)
				continue;

			int m = 1;// 定义右边的计数器tRightplusre
			int n = 1;// 定义右边的计数器tRightminre

			// 这里定义max的目的是为了考虑，如果存放数据的左右两边的行不相等时，会计算达不预期
			int max = 0;
			if (tleftplusre.size() >= tRightplusre.size()) {
				max = tleftplusre.size();
			} else {
				max = tRightplusre.size();
			}

			// 左右两边相比较
			for (int rowNo = 0; rowNo < max; rowNo++) {
				double leftplusvalue = 0d, leftminvalue = 0d, rightplusvalue = 0d, rightminvalue = 0d, leftsum = 0d,
						rightsum = 0d;

				if (tleftplusre.size() != 0 && tleftplusre.containsKey(rowNo + 1)) {
					leftplusvalue = tleftplusre.get(rowNo + 1).doubleValue();
				}

				if (tLeftminre.size() != 0 && tLeftminre.containsKey(rowNo + 1)) {
					leftminvalue = tLeftminre.get(rowNo + 1).doubleValue();
				}

				if (tRightplusre.size() != 0 && tRightplusre.containsKey(rowNo + 1)) {
					rightplusvalue = tRightplusre.get(rowNo + 1).doubleValue();

					if (m < tRightplusre.size()) {
						m = m + 1;
					}

				} else {
					rightplusvalue = 0;
				}

				if (tRightminre.size() != 0 && tRightminre.containsKey(rowNo + 1)) {
					rightminvalue = tRightminre.get(rowNo + 1).doubleValue();

					if (n < tRightminre.size()) {
						n = n + 1;
					}

				} else {
					rightminvalue = 0;
				}

				leftsum = leftplusvalue - leftminvalue;
				rightsum = rightplusvalue - rightminvalue;

				Double differ = new Double(Arith.round(leftsum - rightsum, 4));

				if (differ.doubleValue() != 0.0) {
					
					log.error("校验行可扩展表数据列出错：请查看CfReportRowDataCheck表里的数据 ");
					PrepareErrLog(String.valueOf(cfschema.getSerialNo()), decimalFormat.format(leftsum),
							decimalFormat.format(rightsum), differ.doubleValue(), cfschema.getCalFormula(),
							cfschema.getRemark());
				}

			}
		}

		log.info("校验行可扩展表数据列结束！");
		return true;
	}*/

	/**
	 * 获取当前所有因子代码对应值的和
	 *
	 * @param outItemCodesArray 存放因子代码的数组
	 * @param tablecode 表代码
	 */
	private Map<Integer, BigDecimal> getOutItemCodesSum(String[] outItemCodesArray, String tablecode) {
		// 存放拆开表达式后的多个因子代码
		String outItemCodesStr = "";
		Map<Integer, BigDecimal> countValueMap = new HashMap<Integer, BigDecimal>();

		// 将存放数据里的因子代码拼接，用于sql查询
		if (outItemCodesArray != null && outItemCodesArray.length != 0
				&& !(outItemCodesArray.length == 1 && outItemCodesArray[0] == null)) {
			// join方法是将outItemCodesArray里数据拼接成字符串，并在两个数之间加','
			outItemCodesStr = (new StringBuffer("'")).append(StringUtils.join(outItemCodesArray, "','")).append("'")
					.toString();
		}

		if (outItemCodesStr != null && !(outItemCodesStr.trim().length() == 0)) {
			// 求当前所有因子代码对应值的和
			List<?> list = dataCheckDao.
					getOutItemCodesSum(reportId, tablecode, quarter_Bq, yearMonth_Bq, reportRate, outItemCodesStr,comCode);

			// 将从数据库读取的数据进行转换，将数据表里的rowNo对应的值当作转换后map的key值,SUM(NUMVALUE)当作转换后的value
			for (int i = 0; i < list.size(); i++) {
				countValueMap.put(Integer.parseInt((String) ((Map) list.get(i)).get("ROWNO")),
						(BigDecimal) ((Map) list.get(i)).get("SUM(NUMVALUE)"));
			}

		}
		return countValueMap;

	}

	/**
	 * 期间数据校验：CfReportPeriodDataCheck
	 */
	private boolean checkPeriodItem() {
		log.info("开始校验期间数据，即CfReportPeriodDataCheck！");
		if (mHashMap_Item_Sq.size() <= 0) {
			log.info("---------------提示：没有当前条件下的的上期因子指标数据，不进行期间数据校验---------------");
			return false;
		}
		
		List<CfReportPeriodDataCheck> list = dataCheckDao.getRepPeriodDataChecked(isNeedChk, companyType);
		if (list == null || list.size() <= 0) {
			log.info("------------期间数据校验检验时提示：CfReportPeriodDataCheck表里没有当前条件下的数据，不进行检验！--------------");
			return false;
		}

		for (int i = 0; i < list.size(); i++) {
			String itemCodeBq = list.get(i).getItemCodeBq(); // 获取本期因子
			String itemCodeSq = list.get(i).getItemCodeSq(); // 获取上期因子
			String relationOperator = list.get(i).getRelationOperator(); // 获取关系运算符
			BigDecimal tolerance = list.get(i).getTolerance();  //容差

			// 等式左边为本期数，右边为上期数
			double leftValue = getValue(itemCodeBq.replaceAll(" ", ""), "Bq");
			double rightValue = getValue(itemCodeSq.replaceAll(" ", ""), "Sq");

			double leftMinusRight = Double.parseDouble(decimalFormat.format(leftValue - rightValue));

			// 将检验出错的信息存放到数据库对应的表里
			if (Math.abs(leftMinusRight) > tolerance.doubleValue()) {
				String tCalFomula = (itemCodeBq + relationOperator + itemCodeSq).replaceAll(" ", "");
				String tLeftValue = decimalFormat.format(leftValue);
				String tRightValue = decimalFormat.format(rightValue);

				log.error("期间数据校验出错：请查看CfReportPeriodDataCheck里的相应数据！本期因子代码：" + itemCodeBq + ", 上期因子 代码： " + itemCodeSq);
				PrepareErrLog("期间数据校验出错:" , ERR_LEVEL, tLeftValue, tRightValue, leftMinusRight,
						tCalFomula, list.get(i).getRemark(),tolerance);

				//警告信息
			} else if((Math.abs(leftMinusRight) > 0) && (Math.abs(leftMinusRight) <= tolerance.doubleValue()))  {
				String tCalFomula = (itemCodeBq + relationOperator + itemCodeSq).replaceAll(" ", "");
				String tLeftValue = decimalFormat.format(leftValue);
				String tRightValue = decimalFormat.format(rightValue);

				log.error("警告： 期间数据校验：请查看CfReportPeriodDataCheck里的相应数据！本期因子代码：" + itemCodeBq + ", 上期因子 代码： " + itemCodeSq);
				PrepareErrLog("期间数据校验警告: ", WARN_LEVEL, tLeftValue, tRightValue, leftMinusRight,
						tCalFomula, list.get(i).getRemark(),tolerance);
			}

		}

		log.info("-----------------------校验期间数据结束！---------------------");
		return true;
	}

	/**
	 * 将错误纪录存放到数据库中
	 */
	private boolean PrepareErrLog(String checkInfo, String errLevel, String leftValue, String rightValue, double differ,
			String calculateFormula, String remark, BigDecimal tolerance) {
		String errInfo = checkInfo + "公式：" + remark.trim() + "，" + "等式左边为：" + leftValue + ",等式右边为："
				+ rightValue.trim() + ",差额为：" + decimalFormat.format(differ)+ "。容差为："+ tolerance.doubleValue();

		CfReportErrInfo cfReportErrInfo = new CfReportErrInfo();
		cfReportErrInfo.setYear(yearMonth_Bq);
		cfReportErrInfo.setQuarter(quarter_Bq);
		cfReportErrInfo.setReportType(reportRate);
		cfReportErrInfo.setCheckedCode(errLevel);  //错误级别
		cfReportErrInfo.setErrorInfo(errInfo.trim());
		cfReportErrInfo.setCheckdate(new Date());
		cfReportErrInfo.setTemp(calculateFormula.trim());
		cfReportErrInfo.setComCode(comCode);
		cfReportErrInfo.setTolerance(tolerance); //容差
		cfReportErrInfo.setReportId(reportId);
		
		if (reportId==null) {
			System.out.println(errInfo);
		}
		if(!dataCheckDao.insertErrorInfo(cfReportErrInfo)) {
			log.error("插入数据到数据库出错！");
		}
		
		log.error("检验出错：" + errInfo + "\n");
		return true;
	}

	/**
	 * 获取表达里的"+","-"运算符两边的所有值
	 *
	 * @param calculateFormula
	 *            表达式
	 * @param operator
	 *            表达式里的运算符
	 */
	private String[] getFormula(String calculateFormula, String operator) {
		String[] itemcodes = calculateFormula.replaceAll(" ", "").split("\\" + operator);

		// countMatches取得某字符串在另一字符串中出现的次数
		String[] tResult = new String[StringUtils.countMatches(calculateFormula, operator) + 1];

		for (int j = 1; j < itemcodes.length; j++) {
			if (operator.equals("-")) {
				tResult[j - 1] = itemcodes[j].split("\\-|\\+")[0];
			} else {
				tResult[j] = itemcodes[j].split("\\-|\\+")[0];
			}
		}

		return tResult;
	}

	/**
	 * 求表达式的结果值
	 *
	 * @param calculateFormula
	 * @param BqOrSqFlag 本期或上期的的标志，即是"Bq"还是"Sq"字符串
	 */
	private double getValue(String calculateFormula, String BqOrSqFlag) {
		double dSum = 0.0;

		// 表达式中含有max的公式解析
		if (calculateFormula.contains("max")) {
			dSum = getMaxValue(dSum, calculateFormula, BqOrSqFlag);

		} else if (calculateFormula.contains("if")) {// 表达式中含有if的公式解析
			calculateFormula = calculateFormula.substring(3, calculateFormula.length());
			calculateFormula = calculateFormula.substring(0, calculateFormula.length() - 1);
			String[] temp_des = calculateFormula.split(",");
			String temp_des0 = temp_des[0];
			String temp_des1 = temp_des[1];
			String temp_des2 = temp_des[2];
			boolean value_des0 = getIudgeResult(temp_des0, BqOrSqFlag);
			String value_des1 = getCalResult(temp_des1, BqOrSqFlag);
			String value_des2 = getCalResult(temp_des2, BqOrSqFlag);
			if (value_des0) {
				dSum = Double.parseDouble(value_des1);
			} else {
				dSum = Double.parseDouble(value_des2);
			}

		} else {
			String retValue = getCalResult(calculateFormula, BqOrSqFlag);
			dSum = Double.parseDouble(retValue);
		}
		
		if (dSum==0) {
			return 0.00;
		}
		dSum = Arith.round(dSum, 2);// 四舍五入保留2位小数
		return dSum;
	}

	/***********************************************************************/
	/**
	 * 
	 * @param tCalFomula
	 * @param BqOrSqFlag 本期或上期的的标志，即是"Bq"还是"Sq"字符串
	 * @return
	 */
	private boolean getIudgeResult(String tCalFomula, String BqOrSqFlag) {
		boolean bRet = true;

		// 获取关系运算符
		String relationOperator = getOperator(tCalFomula);

		// 获取公式左边
		String tLeftCalFomula = tCalFomula.substring(0, tCalFomula.indexOf(relationOperator));
		double tDLeftValue = getValue(tLeftCalFomula.replaceAll(" ", ""), "Bq");
		
		// 获取公式右边
		String tRightCalFomula = tCalFomula.substring(tCalFomula.indexOf(relationOperator) + 1);
		double tDRightValue = getValue(tRightCalFomula.replaceAll(" ", ""), "Bq");

		double leftMinusRight = Double.parseDouble(decimalFormat.format(tDLeftValue - tDRightValue));
		
		if ((relationOperator.equals("=") && leftMinusRight != 0)
				|| (relationOperator.equals(">") && leftMinusRight <= 0)
				|| (relationOperator.equals("<") && leftMinusRight >= 0)
				|| (relationOperator.equals("≥") && leftMinusRight < 0)
				|| (relationOperator.equals("≤") && leftMinusRight > 0) ) {
			
			log.error("getIudgeResult方法计算出错！！！");
			bRet = false;
		} 
		
		return bRet;
	}

	/**
	 * 表达式中含有max的公式解析,
	 * @param BqOrSqFlag 本期或上期的的标志，即是"Bq"还是"Sq"字符串
	 */
	private double getMaxValue(double dSum, String calculateFormula, String BqOrSqFlag) {
		double Sum1 = 0.0;
		double Sum2 = 0.0;
		double Sum3 = 0.0;
		String value = "";// max 公式 "]"后的字符串
		String sign = ""; // "]"后的运算符号
		String temp3 = "";// "]"后的因子值

		int length = calculateFormula.length();
		int start = calculateFormula.indexOf("]");
		if ((length) > (start + 1)) {// 判断max公式后是否还有运算
			value = calculateFormula.substring(start + 1);
			sign = value.substring(0, 1);// 确定运算符号
			temp3 = value.substring(1);
		}
		calculateFormula = calculateFormula.substring(calculateFormula.indexOf("[") + 1, calculateFormula.indexOf("]"));
		int a = calculateFormula.indexOf(",");// 确定max公式里","的位置
		String temp1 = calculateFormula.substring(0, a);// ","前边的因子
		String temp2 = calculateFormula.substring(a + 1);// ","后边的因子
		String[] temp11 = temp1.split("#");
		String[] temp22 = temp2.split("#");
		String[] temp33 = temp3.split("#");// max公式之后部分去掉“#” 后的因子值

		for (String aTemp11 : temp11) {
			if (!"".equals(aTemp11.trim()) && !",".equals(aTemp11.trim()) && !"+".equals(aTemp11.trim())) {
				Sum1 = Sum1 + getSingleItemValue(aTemp11, BqOrSqFlag);//
			}
		}

		for (int i = 0; i < temp22.length; i++) {
			if (!"".equals(temp22[i].trim()) && !",".equals(temp22[i].trim()) && !"+".equals(temp22[i].trim())) {
				Sum2 = Sum2 + getSingleItemValue(temp22[i], BqOrSqFlag);//
			}
		}

		for (int i = 0; i < temp33.length; i++) {
			if (!"".equals(temp33[i].trim()) && !",".equals(temp33[i].trim()) && !"+".equals(temp33[i].trim())) {
				Sum3 = getSingleItemValue(temp33[i], BqOrSqFlag);// max公式之后部分去掉“#”
				// 后的因子对应的值
			}
		}

		dSum = Math.max(Sum1, Sum2);
		if ((length) > (start + 1)) {// 判断max公式后是否还有运算
			if (sign.equals("+")) {
				dSum = dSum + Sum3;
			} else if (sign.equals("-")) {
				dSum = dSum - Sum3;
			} else if (sign.equals("*")) {
				dSum = dSum * Sum3;
			} else {
				dSum = dSum / Sum3;
			}
		}

		return dSum;
	}

	/**
	 * 获取存放在map里的单个固定因子的值
	 *
	 * @param outItemcode 固定因子
	 * @param BqOrSqFlag 本期或上期的的标志，即是"Bq"还是"Sq"字符串
	 */
	private double getSingleItemValue(String outItemCode, String BqOrSqFlag) {
		BigDecimal value = new BigDecimal(0);

		if ("Sq".equals(BqOrSqFlag)) {
			value = mHashMap_Item_Sq.get(outItemCode); 
		} else {
			value = hashMap_Item_Bq.get(outItemCode);
		}

		if (value == null) {
			return -1.0;
		}

		return value.doubleValue();
	}

	/*************************************************************************/

	
	/**
	 * 对表达式进行求值
	 * 
	 * @param BqOrSqFlag 本期或上期的的标志，即是"Bq"还是"Sq"字符串
	 */
	private String getCalResult(String calculateFormula, String BqOrSqFlag) {
		List<String> expression = new ArrayList<String>();// 存储中序表达式
		List<String> right = new ArrayList<String>();// 存储右序表达式

		StringTokenizer st = new StringTokenizer(calculateFormula, "+-*/()", true);
		while (st.hasMoreElements()) {
			expression.add(st.nextToken());
		}

		// 将中序表达式转换为右序表达式
		toRight(expression, right);

//		Stacks myStack = new Stacks();
		Stack<String> stack = new Stack<String>();

		String op1, op2, is = null;
		Iterator<String> it = right.iterator(); //[(, #S01_00004#, #S01_00005#, ), #S01_00008#, /, +]

		while (it.hasNext()) {
			is = (String) it.next();
			if (Calculate.isOperator(is)) {
				if (is.equals("+") || is.equals("-") || is.equals("*") || is.equals("/")) {
//					op1 = (String) myStack.pop();
//					op2 = (String) myStack.pop();
					op1 = stack.pop();
					op2 = stack.pop();
//					myStack.push(Calculate.twoResult(is, op1, op2));
					stack.push(Calculate.twoResult(is, op1, op2));
				}
			} else {// is #s03_00184# Bq
				
				is = getValueQuerySQL(is, BqOrSqFlag);// 解析表达式中的查询条件，并查询返回值
//				myStack.push(is);
				stack.push(is);
			}
		}
		// 获取计算结果
//		String calculateResult = (String) myStack.pop();
		String calculateResult = stack.pop();

		if ("Infinity".equals(calculateResult) || "NaN".equals(calculateResult)) {
			calculateResult = "0";
		}

		return calculateResult;
	}

	/**
	 * 将中序表达式转换为右序表达式
	 */
	private void toRight(List<String> expression, List<String> right) {
//		Stacks aStack = new Stacks();
		Stack<String> stack = new Stack<String>();

		String operator;
		int position = 0;
		while (true) {
			if (Calculate.isOperator((String) expression.get(position))) {
//				if (aStack.top == -1 || ((String) expression.get(position)).equals("[")) {
//					aStack.push(expression.get(position));
				if (stack.isEmpty() || ((String) expression.get(position)).equals("(")) {
					stack.push(expression.get(position));
				} else {
					if (((String) expression.get(position)).equals(")")) {
//						if (!((String) aStack.top()).equals("[")) {
//							operator = (String) aStack.pop();
						if (!(stack.peek()).equals("(")) {
							operator = stack.pop();
							right.add(operator);
						}
						// ---这里一定要注意了，千万不能漏，漏掉就有可能在括号的地方出错
//						operator = (String) aStack.pop();
						operator = stack.pop();
						// ---这里一定要注意了，千万不能漏，漏掉就有可能在括号的地方出错
					} else {
//						if (Calculate.priority((String) expression.get(position)) <= Calculate
//								.priority((String) aStack.top()) && aStack.top != -1) {
//							if (!((String) aStack.top()).equals("["))
						if (Calculate.priority((String) expression.get(position)) <= Calculate
								.priority(stack.peek()) && !stack.empty()) {
							if (!(stack.peek()).equals("("))
							// 这个条件很重要，看到很多网上面都没有，结果求：(5*8-(6-3))*5 就会出错
							{
//								operator = (String) aStack.pop();
								operator = stack.pop();
								right.add(operator);
							}

						}
//						aStack.push(expression.get(position));
						stack.push(expression.get(position));
					}
				}
			} else
				right.add(expression.get(position));
			position++;
			if (position >= expression.size())
				break;
		}
//		while (aStack.top != -1) {
//			operator = (String) aStack.pop();
		while (!stack.empty()) {
			operator = stack.pop();
			right.add(operator);
		}
	}

	/**
	 * 
	 * @param outItemCode 固定因子
	 * @param BqOrSqFlag 本期或上期的的标志，即是"Bq"还是"Sq"字符串
	 */
	private String getValueQuerySQL(String outItemCode, String BqOrSqFlag) {
		String outItemCodeValue = ""; //固定因子对应的值
		// 判断outItemCode是否为数字
		boolean isNumber = true;
		try {
			Double.parseDouble(outItemCode);
		} catch (NumberFormatException ex) {
			isNumber = false;
		}

		if (isNumber) {// 是数字，直接返回原值
			return outItemCode;

		} else {
			// 判断是否为百分比值
			if (outItemCode.indexOf("%") != -1) {
				// 去掉百分号
				outItemCode = outItemCode.replaceAll("%", "");
				outItemCode = outItemCode.replaceAll(" ", "");
				
				double dtemp = Double.parseDouble(outItemCode);
				dtemp = dtemp / 100;
				outItemCodeValue = String.valueOf(dtemp);
				
			} else {
				//去除固定因子的#
				outItemCode = outItemCode.substring(1, outItemCode.length() - 1);
				outItemCode = outItemCode.replaceAll(" ", "");

				//固定因子对应的值,就double类型
				double outItemcodeValueDouble = getSingleItemValue(outItemCode, BqOrSqFlag);
				if (outItemcodeValueDouble == -1) {
					log.info("getValueQuerySQL方法下，没有该因子： " + outItemCode);
					outItemcodeValueDouble = 0.0;
				}
		
				outItemCodeValue = String.valueOf(outItemcodeValueDouble);
			}
		}

		return outItemCodeValue;
	}

}
