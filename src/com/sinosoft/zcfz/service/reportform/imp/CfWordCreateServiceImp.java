package com.sinosoft.zcfz.service.reportform.imp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sinosoft.common.Constant;
import com.sinosoft.common.Dao;
import com.sinosoft.zcfz.dao.reportform.CfWordCreateDao;
import com.sinosoft.zcfz.dto.reportform.CfWordCreateDTO;
import com.sinosoft.entity.CfReportElement;
import com.sinosoft.zcfz.service.impl.reportformcompute.ComplexComputeImp;
import com.sinosoft.zcfz.service.reportform.CfWordCreateService;
import com.sinosoft.zcfz.service.reportform.ExportXbrlService;
import com.sinosoft.util.Config;
@Service
public class CfWordCreateServiceImp implements CfWordCreateService{
	@Resource
	private CfWordCreateDao cfWordCreateDao;
	
	@Resource
	private Dao dao;
	
	@Resource
	private ExportXbrlService exportXbrlService;
	
	private CfWordCreateDTO cfWordCreateDto;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CfWordCreateServiceImp.class);
	
	private String DBType = Constant.DBTYPE;// 系统使用数据库类型		
	private String CompanyType = "";// 报送公司所属类	
	private String organcode = Constant.ORGANCODE; // 机构代码	
	private String CfReportRate="";
	private List<?> listRowAddDesc;
	private List<?> listRowData;
	private List<?> listColData;
	//从数据库中查询出来的数据
	private List totalList=new ArrayList();
	//从数据库中查出来的数据格式化
	private HashMap mHashMap_Item=new HashMap();
	//好像是记录可扩展行数据用的
	private List trs_new = new ArrayList();
	// 行集合
	private List ssrs_rows = null;
	// 列集合
	private List ssrs_cols = null;
	//可扩展行所用的列数据
	private HashMap mHashMap_cols = new HashMap();
	
	public boolean CreateWord(CfWordCreateDTO wcdto){
		cfWordCreateDto=wcdto;
		//数据库中获取数据
		totalList=queryDateBySql();
		if (totalList != null) {
			// 初始化mHashMap_Item-------没有做完
			initmHashMap_Item();

			// System.out.println(mHashMap_Item.get("C01_00001"));
			// mHashMap_Item.get(var)可以以这种方式获取公司中英文名称。 var 表示在数据库中的代码字段
			cfWordCreateDto.setmHashMap_Const("中文公司名称", "英文公司名称");
			SAXReader saxReader = new SAXReader();
			Document document;
			try {
				File file = new File(cfWordCreateDto.getTemplatefile());
				document = saxReader.read(file);
				// 处理非报表变量
				document = dealConstVars(document);

				// 处理静态报表中变量(替换因子指标值)
				document = dealTabConstVars(document);

				// 处理动态报表(替换行可扩展值)
				document = deaTabDynamicVars(document);

				storeDoc(document, cfWordCreateDto.getOutputfile(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 通过freemarker模板来生成word报告
	 */
	public Map<String, Object> CreateWordByFreeMarker(CfWordCreateDTO wcdto){
		this.CfReportRate=wcdto.getmCfReportRate();
		if(!getCompanyType())
		{
			System.out.println("获取公司类型出错！");
		}
		//数据库中获取数据
		cfWordCreateDto=wcdto;
		//结果集
		Map<String, Object> paramMap = new HashMap<String, Object>();  
		//固定因子获取数据
		paramMap =getStaticData(paramMap);
		
		//行可扩展因子获取数据
		paramMap =getExtendsData(paramMap);
		
        return paramMap;
	}
	/**
	 * 信息披露自己的
	 * 通过freemarker模板来生成word报告
	 */
	public Map<String, Object> CreateWordByFreeMarker1(CfWordCreateDTO wcdto){
		this.CfReportRate=wcdto.getmCfReportRate();
		if(!getCompanyType())
		{
			System.out.println("获取公司类型出错！");
		}
		//数据库中获取数据
		cfWordCreateDto=wcdto;
		//结果集
		Map<String, Object> paramMap = new HashMap<String, Object>();  
		//固定因子获取数据
		paramMap =getStaticData1(paramMap);
		
		//行可扩展因子获取数据
		//paramMap =getExtendsData(paramMap);
		
        return paramMap;
	}
	public boolean downloadWord(CfWordCreateDTO wcdto,HttpServletResponse response,HttpServletRequest request){
		File file=new File(wcdto.getOutputfile());
		if(!file.exists()){
			return false;
		}else{
			return true;
		}
	}
	public boolean downloadWord(File file,HttpServletResponse response,HttpServletRequest request){
		if(!file.exists()){
			return false;
		}else{
			return true;
		}
	}
	private void storeDoc(Document doc, String filename, String aEncode) throws UnsupportedEncodingException, FileNotFoundException,IOException {
			Writer out = new OutputStreamWriter(new FileOutputStream(filename),aEncode);
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding(aEncode);
			XMLWriter writer = new XMLWriter(out, format);
			writer.write(doc);
			out.close();
	}
	
	
	
	public Map<String, Object> getStaticData(Map<String, Object> paramMap){
		NumberFormat nf=NumberFormat.getNumberInstance();
		totalList=queryStaticData();
		if (totalList!=null) {
			int totalCount=totalList.size();
			for(int i=0;i<totalCount;i++){
				Map map=(Map) totalList.get(i);
				String reportItemCode=(String) map.get("REPORTITEMCODE");
				String OutItemType = (String) map.get("OUTITEMTYPE");
				if(OutItemType==null||"".equals(OutItemType)){
					//List listocrrad= dao.queryWithJDBC("select ocrrad.coltype from rsys_CfReportRowAddDesc ocrrad where ocrrad.colCode='"+reportItemCode+"'");
					List listocrrad= dao.queryWithJDBC("select ocrrad.outitemtype from rsys_Cfreportitemcodedesc ocrrad where ocrrad.reportitemcode='"+reportItemCode+"'");
					if(listocrrad.size()>0){
						Map mapo=(Map) listocrrad.get(0);
						OutItemType=(String) mapo.get("coltype");
					}else{
						LOGGER.error(reportItemCode+"指标因子未找到!");
			        	throw new RuntimeException(reportItemCode+"指标因子未找到!");
					}
				}
				String DBDecimals="";
				//取精度
				List<CfReportElement> listCfReportElement=(List<CfReportElement>) dao.query("from CfReportElement where portItemCode='"+reportItemCode+"'");
				if(listCfReportElement!=null&&!"".equals(listCfReportElement)){
					if(listCfReportElement.size()>0){
						DBDecimals=listCfReportElement.get(0).getDecimals();
					}
				}
				String strValue = "";
				String strWValue = ""; 
				if("06".equals(OutItemType)){// 06 百分比型
					strValue = (String) map.get("REPORTITEMVALUE");
					strWValue = (String) map.get("REPORTITEMWANVALUE");
					if( null != strValue && !"".equals(strValue.trim())){
						if(DBDecimals!=null&&!"".equals(DBDecimals)){
								strValue =exportXbrlService.FormatData(strValue,DBDecimals); 
								strValue =String.valueOf(new BigDecimal(strValue).setScale(Integer.parseInt(DBDecimals),BigDecimal.ROUND_HALF_UP));
							}else{
								strValue =exportXbrlService.FormatData(strValue,"4"); 
								strValue =String.valueOf(new BigDecimal(strValue).setScale(4,BigDecimal.ROUND_HALF_UP));
							}
					}
					if( null != strWValue && !"".equals(strWValue.trim())){
						if(DBDecimals!=null&&!"".equals(DBDecimals)){
							strWValue =exportXbrlService.FormatData(strWValue,DBDecimals); 
							strWValue =String.valueOf(new BigDecimal(strWValue).setScale(Integer.parseInt(DBDecimals),BigDecimal.ROUND_HALF_UP));
						}else{
							strWValue =exportXbrlService.FormatData(strWValue,"4"); 
							strWValue =String.valueOf(new BigDecimal(strWValue).setScale(4,BigDecimal.ROUND_HALF_UP));
						}
					}
				}else if("05".equals(OutItemType)){//05 数量型
					strValue = (String) map.get("REPORTITEMVALUE");
					strWValue = (String) map.get("REPORTITEMWANVALUE");
					if( null != strValue && !"".equals(strValue.trim())){
						if(DBDecimals!=null&&!"".equals(DBDecimals)){
							strValue =exportXbrlService.FormatData(strValue,DBDecimals); 
							strValue =String.valueOf(new BigDecimal(strValue).setScale(Integer.parseInt(DBDecimals),BigDecimal.ROUND_HALF_UP));
						}else{
							strValue =exportXbrlService.FormatData(strValue,"0"); 
							strValue =String.valueOf(new BigDecimal(strValue).setScale(0,BigDecimal.ROUND_HALF_UP));
						}
					}
					if( null != strWValue && !"".equals(strWValue.trim())){
						if(DBDecimals!=null&&!"".equals(DBDecimals)){
							strWValue =exportXbrlService.FormatData(strWValue,DBDecimals); 
							strWValue =String.valueOf(new BigDecimal(strWValue).setScale(Integer.parseInt(DBDecimals),BigDecimal.ROUND_HALF_UP));
						}else{
							strWValue =exportXbrlService.FormatData(strWValue,"0"); 
							strWValue =String.valueOf(new BigDecimal(strWValue).setScale(0,BigDecimal.ROUND_HALF_UP));
						}
					}
				}else if("04".equals(OutItemType)){//04 数值型
					strValue = (String) map.get("REPORTITEMVALUE");
					strWValue = (String) map.get("REPORTITEMWANVALUE");
					if( null != strValue && !"".equals(strValue.trim())){
						if(DBDecimals!=null&&!"".equals(DBDecimals)){
							strValue =exportXbrlService.FormatData(strValue,DBDecimals); 
							strValue =String.valueOf(new BigDecimal(strValue).setScale(Integer.parseInt(DBDecimals),BigDecimal.ROUND_HALF_UP));
							nf.setMinimumFractionDigits(Integer.parseInt(DBDecimals));
						}else{
							strValue =exportXbrlService.FormatData(strValue,"2"); 
							strValue =String.valueOf(new BigDecimal(strValue).setScale(2,BigDecimal.ROUND_HALF_UP));
							nf.setMinimumFractionDigits(2);
						}
						strValue=nf.format(new BigDecimal(strValue));
					}
					if( null != strWValue && !"".equals(strWValue.trim())){
						if(DBDecimals!=null&&!"".equals(DBDecimals)){
							strWValue =exportXbrlService.FormatData(strWValue,DBDecimals); 
							strWValue =String.valueOf(new BigDecimal(strWValue).setScale(Integer.parseInt(DBDecimals),BigDecimal.ROUND_HALF_UP));
							nf.setMinimumFractionDigits(Integer.parseInt(DBDecimals));
						}else{
							strWValue =exportXbrlService.FormatData(strWValue,"2"); 
							strWValue =String.valueOf(new BigDecimal(strWValue).setScale(2,BigDecimal.ROUND_HALF_UP));
							nf.setMinimumFractionDigits(2);
						}
						strWValue=nf.format(new BigDecimal(strWValue));
					}
				}else if("03".equals(OutItemType)){//03 文件型，不包含在XML报文中
					continue;
				}else if("02".equals(OutItemType)){//02 描述型
					strValue=(String) map.get("DESTEXT");
					if(!"".equals(strValue)&&strValue!=null){
						strValue = "\n" + map.get("DESTEXT") + "\n";
					}				
				}else if("01".equals(OutItemType)){//01 短文本型
					strValue = (String) map.get("TEXTVALUE");
				}else if("07".equals(OutItemType)){//07  
					strValue = (String) map.get("TEXTVALUE");
				}else{
					System.out.println("未获取到因子值类型.");
				}
				if(strValue==null||"".equals(strValue)||("\n"+null+"\n").equals(strValue)){
					strValue="--";
				}
				if(strWValue==null||"".equals(strWValue)||("\n"+null+"\n").equals(strWValue)){
					strWValue="--";
				}
				paramMap.put(reportItemCode, strValue);
				paramMap.put("y_"+reportItemCode, strValue);
				paramMap.put("w_"+reportItemCode, strWValue);
			}
		}
		return paramMap;
	}
	
	/**
	 * 信息披露自己的
	 * @param paramMap
	 * @return
	 */
	public Map<String, Object> getStaticData1(Map<String, Object> paramMap){
		NumberFormat nf=NumberFormat.getNumberInstance();
		totalList=queryStaticData1();
		if (totalList!=null) {
			int totalCount=totalList.size();
			for(int i=0;i<totalCount;i++){
				Map map=(Map) totalList.get(i);
				String reportItemCode=(String) map.get("REPORTITEMCODE");
				String OutItemType = (String) map.get("OUTITEMTYPE");
				if(OutItemType==null||"".equals(OutItemType)){
					//List listocrrad= dao.queryWithJDBC("select ocrrad.coltype from rsys_CfReportRowAddDesc ocrrad where ocrrad.colCode='"+reportItemCode+"'");
					List listocrrad= dao.queryWithJDBC("select ocrrad.outitemtype from rsys_Cfreportitemcodedesc ocrrad where ocrrad.reportitemcode='"+reportItemCode+"'");
					if(listocrrad.size()>0){
						Map mapo=(Map) listocrrad.get(0);
						OutItemType=(String) mapo.get("coltype");
					}else{
						LOGGER.error(reportItemCode+"指标因子未找到!");
			        	throw new RuntimeException(reportItemCode+"指标因子未找到!");
					}
				}
				String DBDecimals="";
				//取精度
				List<CfReportElement> listCfReportElement=(List<CfReportElement>) dao.query("from CfReportElement where portItemCode='"+reportItemCode+"'");
				if(listCfReportElement!=null&&!"".equals(listCfReportElement)){
					if(listCfReportElement.size()>0){
						DBDecimals=listCfReportElement.get(0).getDecimals();
					}
				}
				String strValue = "";
				String strWValue = ""; 
				if("06".equals(OutItemType)){// 06 百分比型
					strValue = (String) map.get("REPORTITEMVALUE");
					strWValue = (String) map.get("REPORTITEMWANVALUE");
					if( null != strValue && !"".equals(strValue.trim())){
						if(DBDecimals!=null&&!"".equals(DBDecimals)){
								strValue =exportXbrlService.FormatData(strValue,DBDecimals); 
								strValue =String.valueOf(new BigDecimal(strValue).setScale(Integer.parseInt(DBDecimals),BigDecimal.ROUND_HALF_UP));
							}else{
								strValue =exportXbrlService.FormatData(strValue,"4"); 
								strValue =String.valueOf(new BigDecimal(strValue).setScale(4,BigDecimal.ROUND_HALF_UP));
							}
					}
					if( null != strWValue && !"".equals(strWValue.trim())){
						if(DBDecimals!=null&&!"".equals(DBDecimals)){
							strWValue =exportXbrlService.FormatData(strWValue,DBDecimals); 
							strWValue =String.valueOf(new BigDecimal(strWValue).setScale(Integer.parseInt(DBDecimals),BigDecimal.ROUND_HALF_UP));
						}else{
							strWValue =exportXbrlService.FormatData(strWValue,"4"); 
							strWValue =String.valueOf(new BigDecimal(strWValue).setScale(4,BigDecimal.ROUND_HALF_UP));
						}
					}
				}else if("05".equals(OutItemType)){//05 数量型
					strValue = (String) map.get("REPORTITEMVALUE");
					strWValue = (String) map.get("REPORTITEMWANVALUE");
					if( null != strValue && !"".equals(strValue.trim())){
						if(DBDecimals!=null&&!"".equals(DBDecimals)){
							strValue =exportXbrlService.FormatData(strValue,DBDecimals); 
							strValue =String.valueOf(new BigDecimal(strValue).setScale(Integer.parseInt(DBDecimals),BigDecimal.ROUND_HALF_UP));
						}else{
							strValue =exportXbrlService.FormatData(strValue,"0"); 
							strValue =String.valueOf(new BigDecimal(strValue).setScale(0,BigDecimal.ROUND_HALF_UP));
						}
					}
					if( null != strWValue && !"".equals(strWValue.trim())){
						if(DBDecimals!=null&&!"".equals(DBDecimals)){
							strWValue =exportXbrlService.FormatData(strWValue,DBDecimals); 
							strWValue =String.valueOf(new BigDecimal(strWValue).setScale(Integer.parseInt(DBDecimals),BigDecimal.ROUND_HALF_UP));
						}else{
							strWValue =exportXbrlService.FormatData(strWValue,"0"); 
							strWValue =String.valueOf(new BigDecimal(strWValue).setScale(0,BigDecimal.ROUND_HALF_UP));
						}
					}
				}else if("04".equals(OutItemType)){//04 数值型
					strValue = (String) map.get("REPORTITEMVALUE");
					strWValue = (String) map.get("REPORTITEMWANVALUE");
					if( null != strValue && !"".equals(strValue.trim())){
						if(DBDecimals!=null&&!"".equals(DBDecimals)){
							strValue =exportXbrlService.FormatData(strValue,DBDecimals); 
							strValue =String.valueOf(new BigDecimal(strValue).setScale(Integer.parseInt(DBDecimals),BigDecimal.ROUND_HALF_UP));
							nf.setMinimumFractionDigits(Integer.parseInt(DBDecimals));
						}else{
							strValue =exportXbrlService.FormatData(strValue,"2"); 
							strValue =String.valueOf(new BigDecimal(strValue).setScale(2,BigDecimal.ROUND_HALF_UP));
							nf.setMinimumFractionDigits(2);
						}
						BigDecimal b=new BigDecimal(strValue);
						strValue=nf.format(b);
					}
					if( null != strWValue && !"".equals(strWValue.trim())){
						if(DBDecimals!=null&&!"".equals(DBDecimals)){
							strWValue =exportXbrlService.FormatData(strWValue,DBDecimals); 
							strWValue =String.valueOf(new BigDecimal(strWValue).setScale(Integer.parseInt(DBDecimals),BigDecimal.ROUND_HALF_UP));
							nf.setMinimumFractionDigits(Integer.parseInt(DBDecimals));
						}else{
							strWValue =exportXbrlService.FormatData(strWValue,"2"); 
							strWValue =String.valueOf(new BigDecimal(strWValue).setScale(2,BigDecimal.ROUND_HALF_UP));
							nf.setMinimumFractionDigits(2);
						}
						strWValue=nf.format(new BigDecimal(strWValue));
					}
				}else if("03".equals(OutItemType)){//03 文件型，不包含在XML报文中
					continue;
				}else if("02".equals(OutItemType)){//02 描述型
					strValue=(String) map.get("DESTEXT");
					if(!"".equals(strValue)&&strValue!=null){
						strValue = "\n" + map.get("DESTEXT") + "\n";
					}				
				}else if("01".equals(OutItemType)){//01 短文本型
					strValue = (String) map.get("TEXTVALUE");
				}else if("07".equals(OutItemType)){//07  
					strValue = (String) map.get("TEXTVALUE");
				}else{
					System.out.println("未获取到因子值类型.");
				}
				if(strValue==null||"".equals(strValue)||("\n"+null+"\n").equals(strValue)){
					strValue="--";
				}
				if(strWValue==null||"".equals(strWValue)||("\n"+null+"\n").equals(strWValue)){
					strWValue="--";
				}
				paramMap.put(reportItemCode, strValue);
				paramMap.put("y_"+reportItemCode, strValue);
				paramMap.put("w_"+reportItemCode, strWValue);
			}
		}
		return paramMap;
	}
	
	public Map<String, Object> getExtendsData(Map<String, Object> paramMap){
		if(!getExtendTableCode()){
			return paramMap;
		}
		NumberFormat nf=NumberFormat.getNumberInstance();
		//系统频度值的设定与保监会报文频度设定不一致，需要调整
		String strIntervaltype = "";
		for(int nIndex=0;nIndex<listRowAddDesc.size();nIndex++){
			Map<?,?> m=(Map<?,?>)listRowAddDesc.get(nIndex);
			String strTableCode = (String) m.get("UPPER(cfr.tablecode)");
            // 通过表代码(tablecode)获取行号
			if(!getRowDataFromDB(strTableCode)){
				continue;
			}
			List list=new ArrayList(); //freemarker中的for循环
			int rowscount = listRowData.size();
			// 没有数据的行可扩展表，整表都不报送
			if( rowscount > 0 ){
				for(int rownum = 0 ; rownum < rowscount ; rownum ++ ){
					Map map=new HashMap();//freemarker一行的数据
					Map<?,?> mr=(Map<?,?>)listRowData.get(rownum);
					String rowno = (String) mr.get("LINE1");					
					map.put(strTableCode+"_rowno", rowno);
					// 通过行号获取该行，所有列的数据
					if(!getColDataFromDB(strTableCode,rowno)){
						//return staticXbrl;
						continue;
					}
					int colscount = listColData.size();
						for(int colnum = 0 ; colnum < colscount ; colnum ++ ){
							Map<?,?> mc=(Map<?,?>)listColData.get(colnum);
							String ColType = (String) mc.get("LINE2");
							String colcode = (String) mc.get("LINE3");
							if(ColType==null||"".equals(ColType)){
								List listocrrad= dao.queryWithJDBC("select ocrrad.coltype from rsys_CfReportRowAddDesc ocrrad where ocrrad.colCode='"+colcode+"'");
								if(listocrrad.size()>0){
									Map mapo=(Map) listocrrad.get(0);
									ColType=(String) mapo.get("coltype");
								}else{
									LOGGER.error(colcode+"指标因子未找到!");
						        	throw new RuntimeException(colcode+"指标因子未找到!");
								}
							}
							String DBDecimals="";
							//取精度
							List<CfReportElement> listCfReportElement=(List<CfReportElement>) dao.query("from CfReportElement where portItemCode='"+colcode+"'");
							if(listCfReportElement!=null&&!"".equals(listCfReportElement)){
								if(listCfReportElement.size()>0){
									DBDecimals=listCfReportElement.get(0).getDecimals();
								}
							}
							// 根据列类型取值
							String NumValue = "";
							String WNumValue="";
							if("06".equals(ColType)){// 06 百分比型
								NumValue = (String) mc.get("LINE4");
								WNumValue = (String) mc.get("LINE5");
								if( null != NumValue && !"".equals(NumValue.trim())){
									if(DBDecimals!=null&&!"".equals(DBDecimals)){
										NumValue =exportXbrlService.FormatData(NumValue,DBDecimals); 
										NumValue =String.valueOf(new BigDecimal(NumValue).setScale(Integer.parseInt(DBDecimals),BigDecimal.ROUND_HALF_UP));
									}else{
										NumValue =exportXbrlService.FormatData(NumValue,"4"); 
										NumValue =String.valueOf(new BigDecimal(NumValue).setScale(4,BigDecimal.ROUND_HALF_UP));
									}
								}
								if( null != WNumValue && !"".equals(WNumValue.trim())){
									if(DBDecimals!=null&&!"".equals(DBDecimals)){
										WNumValue =exportXbrlService.FormatData(WNumValue,DBDecimals); 
										WNumValue =String.valueOf(new BigDecimal(WNumValue).setScale(Integer.parseInt(DBDecimals),BigDecimal.ROUND_HALF_UP));
									}else{
										WNumValue =exportXbrlService.FormatData(WNumValue,"4"); 
										WNumValue =String.valueOf(new BigDecimal(WNumValue).setScale(4,BigDecimal.ROUND_HALF_UP));
									}
								}
							}else if("05".equals(ColType)){// 05 数量型
								NumValue = (String) mc.get("LINE4");
								WNumValue = (String) mc.get("LINE5");
								if( null != NumValue && !"".equals(NumValue.trim())){
									if(DBDecimals!=null&&!"".equals(DBDecimals)){
										NumValue =exportXbrlService.FormatData(NumValue,DBDecimals); 
										NumValue =String.valueOf(new BigDecimal(NumValue).setScale(Integer.parseInt(DBDecimals),BigDecimal.ROUND_HALF_UP));
									}else{
										NumValue =exportXbrlService.FormatData(NumValue,"0"); 
										NumValue =String.valueOf(new BigDecimal(NumValue).setScale(0,BigDecimal.ROUND_HALF_UP));
									}
								}
								if( null != WNumValue && !"".equals(WNumValue.trim())){
									if(DBDecimals!=null&&!"".equals(DBDecimals)){
										WNumValue =exportXbrlService.FormatData(WNumValue,DBDecimals); 
										WNumValue =String.valueOf(new BigDecimal(WNumValue).setScale(Integer.parseInt(DBDecimals),BigDecimal.ROUND_HALF_UP));
									}else{
										WNumValue =exportXbrlService.FormatData(WNumValue,"0"); 
										WNumValue =String.valueOf(new BigDecimal(WNumValue).setScale(0,BigDecimal.ROUND_HALF_UP));
									}
								}
							}else if("04".equals(ColType)){// 04 数值型
								NumValue = (String) mc.get("LINE4");
								WNumValue = (String) mc.get("LINE5");
								if( null != NumValue && !"".equals(NumValue.trim())){
									if(DBDecimals!=null&&!"".equals(DBDecimals)){
										NumValue =exportXbrlService.FormatData(NumValue,DBDecimals); 
										NumValue =String.valueOf(new BigDecimal(NumValue).setScale(Integer.parseInt(DBDecimals),BigDecimal.ROUND_HALF_UP));
										nf.setMinimumFractionDigits(Integer.parseInt(DBDecimals));
									}else{
										NumValue =exportXbrlService.FormatData(NumValue,"2"); 
										NumValue =String.valueOf(new BigDecimal(NumValue).setScale(2,BigDecimal.ROUND_HALF_UP));
										nf.setMinimumFractionDigits(2);
									}
									NumValue=nf.format(new BigDecimal(NumValue));
								}
								if( null != WNumValue && !"".equals(WNumValue.trim())){
									if(DBDecimals!=null&&!"".equals(DBDecimals)){
										WNumValue =exportXbrlService.FormatData(WNumValue,DBDecimals); 
										WNumValue =String.valueOf(new BigDecimal(WNumValue).setScale(Integer.parseInt(DBDecimals),BigDecimal.ROUND_HALF_UP));
										nf.setMinimumFractionDigits(Integer.parseInt(DBDecimals));
									}else{
										WNumValue =exportXbrlService.FormatData(WNumValue,"2"); 
										WNumValue =String.valueOf(new BigDecimal(WNumValue).setScale(2,BigDecimal.ROUND_HALF_UP));
										nf.setMinimumFractionDigits(2);
									}
									WNumValue=nf.format(new BigDecimal(WNumValue));
								}
							}else if("01".equals(ColType) || "02".endsWith(ColType)){// 01 短文本类型,02 描述型 		
								NumValue = (String) mc.get("LINE6");
							}else if("07".equals(ColType)){
								NumValue = (String) mc.get("LINE6");
							}else {
								NumValue = "";
							}
							if(NumValue==null||"".equals(NumValue)){
								NumValue="--";
							}
							if(WNumValue==null||"".equals(WNumValue)){
								WNumValue="--";
							}
							map.put(colcode, NumValue);
							map.put("w_"+colcode, WNumValue);
						}
						list.add(map);
						listColData.removeAll(listColData);
					}
				listRowData.removeAll(listRowData);
				}
			paramMap.put(strTableCode+"List", list);
			}
			listRowAddDesc.removeAll(listRowAddDesc);
		return paramMap;
		}
		//获取表代码
		private boolean getExtendTableCode(){
			/*--从行可扩展描述表中获取需要报送的表对应的表代码--*/
			String strSQL = "SELECT Distinct UPPER(cfr.tablecode) from cfreportrowadddesc cfr where 1 = 1";
	        //通过频度筛选指标描述表中需要报送的因子
			if("1".equals(CfReportRate)){//季度快报
				strSQL = strSQL + " AND (cfr.QuarterReport) = '1' ";
			}else if("2".equals(CfReportRate)){//季度报告
				strSQL = strSQL + " AND (cfr.HalfYearReport) = '1' ";
			}else if("3".equals(CfReportRate)){//临时报告
				strSQL = strSQL + " AND (cfr.YearReport) = '1' ";
			}
			// 根据系统分类判断哪些因子需要报送
			// 公司类型值就以系统分类字段名称为值
			strSQL = strSQL + " AND (cfr." + this.CompanyType + ") = '1' ";
			strSQL = strSQL + " ORDER BY UPPER(CFR.TABLECODE) ASC ";
			System.out.println(strSQL);
			listRowAddDesc = cfWordCreateDao.queryBysql(strSQL);
			if(listRowAddDesc.size()<=0){
				System.out.println("从cfreportrowadddesc获取报送因子获取失败");
				return false;
			}
			return true;
		}
	//因子查询				
	private List queryStaticData(){
		// 根据系统使用数据库类型，SQL语句不一致
			String strSQL = "";
			if("SqlServer".equals(this.DBType)){// SqlServer
				strSQL = " SELECT CFR.OUTITEMCODE,CFR.REPORTITEMCODE,CONVERT(char(50), CF.REPORTITEMVALUE) REPORTITEMVALUE,CONVERT(char(50), CF.REPORTITEMWANVALUE) REPORTITEMWANVALUE,CF.TEXTVALUE,CFR.OUTITEMTYPE,CFR.OUTITEMNOTE,CF.DESTEXT"
							+ " FROM CFREPORTITEMCODEDESC cfr left join (SELECT * FROM CFREPORTDATA WHERE 1 =1  ";
			}
			else if("Oracle".equals(this.DBType)){// Oracle
				strSQL = " SELECT CFR.OUTITEMCODE,CFR.REPORTITEMCODE,to_char(CF.REPORTITEMVALUE) REPORTITEMVALUE,to_char(CF.REPORTITEMWANVALUE) REPORTITEMWANVALUE,CF.TEXTVALUE,CFR.OUTITEMTYPE,CFR.OUTITEMNOTE"
					+ " FROM CFREPORTITEMCODEDESC cfr left join (SELECT * FROM CFREPORTDATA WHERE 1 =1  ";
			}
			else{
				System.out.println("系统使用数据库类型获取失败！");
			  	return null;
			}
//			if(cfWordCreateDto.getmQuarter() != null && !"".equals(cfWordCreateDto.getmQuarter())){
//				strSQL = strSQL + " AND quarter = '"+cfWordCreateDto.getmQuarter()+ "' ";
//			}
//			if(cfWordCreateDto.getmYearMonth() != null && !"".equals(cfWordCreateDto.getmYearMonth())){
//				strSQL = strSQL + " AND month = '"+cfWordCreateDto.getmYearMonth()+ "' ";
//			}
			
			if(cfWordCreateDto.getReportId() != null && !"".equals(cfWordCreateDto.getReportId())){
				strSQL = strSQL + " AND reportId = '"+cfWordCreateDto.getReportId()+ "' ";
			}
			strSQL = strSQL + " and  comcode='"+Config.getProperty("OrganCode") + "'";
			//按照频度筛选
			strSQL = strSQL + " and reportrate = '"+ cfWordCreateDto.getmCfReportRate() +"' "; 		
			strSQL = strSQL + ") cf  on cfr.REPORTITEMCODE = cf.Outitemcode  "
					+ "WHERE  1 = 1 ";
			//通过频度筛选指标描述表中需要报送的因子
			if("1".equals(cfWordCreateDto.getmCfReportRate())){//季度快报
				strSQL = strSQL + " AND cfr.quarterreport = '1' "; 
			}
			else if("2".equals(cfWordCreateDto.getmCfReportRate())){//季度报告
				strSQL = strSQL + " AND cfr.halfyearreport = '1' ";
			}
			else if("3".equals(cfWordCreateDto.getmCfReportRate())){//临时报告
				strSQL = strSQL + " AND cfr.yearreport = '1' ";
			}		
			// 根据系统分类判断哪些因子需要报送
			// 公司类型值就以系统分类字段名称为值
			strSQL = strSQL + " AND cfr." + this.CompanyType + " = '1' ";
					
			strSQL = strSQL + " ORDER BY CFR.OUTITEMCODE ASC ";		
			
			 
			System.out.println(strSQL);
			// 因子属性：01 公司偿付能力报告、02 分红险报告、03 公司财务报告
			List<?> listCfReportdata = cfWordCreateDao.queryBysql(strSQL);
			if(listCfReportdata.size()<=0){
				System.out.println("报送因子获取失败");
				return null;
			}
			if("1".equals(cfWordCreateDto.getmCfReportRate())){//季度快报
				for (int i = 0; i < listCfReportdata.size(); i++) {
					Map<String,Object> map = (Map<String, Object>) listCfReportdata.get(i);
					if ("C01_00000".equals(map.get("OUTITEMCODE"))) {
						map.put("TEXTVALUE", cfWordCreateDto.getmYear()+"年第"+cfWordCreateDto.getmQuarter()+"季度");
					}
				}
			}			
			return listCfReportdata;	
	}
	/**
	 * 信息披露自己的
	 * @return
	 */
	private List queryStaticData1(){
		// 根据系统使用数据库类型，SQL语句不一致
			String strSQL = "";
			if("SqlServer".equals(this.DBType)){// SqlServer
				strSQL = " SELECT CFR.OUTITEMCODE,CFR.REPORTITEMCODE,CONVERT(char(50), CF.REPORTITEMVALUE) REPORTITEMVALUE,CONVERT(char(50), CF.REPORTITEMWANVALUE) REPORTITEMWANVALUE,CF.TEXTVALUE,CFR.OUTITEMTYPE,CFR.OUTITEMNOTE,CF.DESTEXT"
							+ " FROM CFREPORTITEMCODEDESC cfr left join (SELECT * FROM CFREPORTDATA WHERE 1 =1  ";
			}
			else if("Oracle".equals(this.DBType)){// Oracle
				strSQL = " SELECT CFR.OUTITEMCODE,CFR.REPORTITEMCODE,to_char(CF.REPORTITEMVALUE) REPORTITEMVALUE,to_char(CF.REPORTITEMWANVALUE) REPORTITEMWANVALUE,CF.TEXTVALUE,CFR.OUTITEMTYPE,CFR.OUTITEMNOTE,CF.DESTEXT"
					+ " FROM CFREPORTITEMCODEDESC cfr left join (SELECT * FROM CFREPORTDATA WHERE 1 =1  ";
			}
			else{
				System.out.println("系统使用数据库类型获取失败！");
			  	return null;
			}
			if(cfWordCreateDto.getmQuarter() != null && !"".equals(cfWordCreateDto.getmQuarter())){
				strSQL = strSQL + " AND quarter = '"+cfWordCreateDto.getmQuarter()+ "' ";
			}
			if(cfWordCreateDto.getmYearMonth() != null && !"".equals(cfWordCreateDto.getmYearMonth())){
				strSQL = strSQL + " AND month = '"+cfWordCreateDto.getmYearMonth()+ "' ";
			}
			strSQL = strSQL + " and  comcode='"+Config.getProperty("OrganCode") + "'";
			//按照频度筛选
			strSQL = strSQL + " and reportrate = '"+ cfWordCreateDto.getmCfReportRate() +"' "; 		
			strSQL = strSQL + ") cf  on cfr.REPORTITEMCODE = cf.Outitemcode  "
					+ "WHERE  1 = 1 ";
			//通过频度筛选指标描述表中需要报送的因子
			if("1".equals(cfWordCreateDto.getmCfReportRate())){//季度快报
				strSQL = strSQL + " AND cfr.quarterreport = '1' "; 
			}
			else if("2".equals(cfWordCreateDto.getmCfReportRate())){//季度报告
				strSQL = strSQL + " AND cfr.halfyearreport = '1' ";
			}
			else if("3".equals(cfWordCreateDto.getmCfReportRate())){//临时报告
				strSQL = strSQL + " AND cfr.yearreport = '1' ";
			}		
			// 根据系统分类判断哪些因子需要报送
			// 公司类型值就以系统分类字段名称为值
			strSQL = strSQL + " AND cfr." + this.CompanyType + " = '1' and cfr.REPORTITEMCODE like 'C01%' or cfr.REPORTITEMCODE like 'S01%' or cfr.REPORTITEMCODE like 'S02%' or cfr.REPORTITEMCODE like 'S07%' or cfr.REPORTITEMCODE like '3110%'  ";
			strSQL = strSQL + " ORDER BY CFR.OUTITEMCODE ASC ";		 
			System.out.println(strSQL);
			// 因子属性：01 公司偿付能力报告、02 分红险报告、03 公司财务报告
			List listCfReportdata = cfWordCreateDao.queryBysql(strSQL);
			if(listCfReportdata.size()<=0){
				System.out.println("报送因子获取失败");
				return null;
			}
			return listCfReportdata;	
	}
	
	private boolean getCompanyType()
	{
		if(this.organcode.equals("000007")){//集团
	    	this.CompanyType="Group2";
	    }else if(this.organcode.equals("000008")){//财再
	    	this.CompanyType="Propertyinsurance1";//pro1
	    }else if(this.organcode.equals("000009")){//寿再
	    	this.CompanyType="Lifeinsurance1";//life1
	    }else if(this.organcode.equals("000000")){//寿再
	    	String commonCompanyType = Config.getProperty("CompanyType");
	    	if("L".equals(commonCompanyType)){
	    		this.CompanyType="Lifeinsurance1";
	    	}else if("P".equals(commonCompanyType)){
	    		this.CompanyType="Propertyinsurance1";
	    	}else if("R".equals(commonCompanyType)){
	    		this.CompanyType="Reinsurance";
	    	}
	    }else{
	    	this.CompanyType="Reinsurance";//资产
	    }
		  
		if(null == CompanyType || "".equals(CompanyType))
		{
	    return false;
		}
		return true;
	  }
	//从数据库获取数据
	private List queryDateBySql(){
		
		String	strSQL = " SELECT CFR.OUTITEMCODE line1,CFR.REPORTITEMCODE line2,to_char(CF.REPORTITEMVALUE) line3,to_char(CF.REPORTITEMWANVALUE) line4,CF.TEXTVALUE line5,CFR.OUTITEMTYPE line6, CFR.Reportitemname line7,CF.DESTEXT line8"
					+ " FROM CFREPORTITEMCODEDESC cfr left join (SELECT * FROM CFREPORTDATA WHERE 1 =1  ";
		String mysql = "SELECT * FROM CFREPORTDATA WHERE 1 =1 ";
		// 通过频度筛选指标数据表中数据
		if (cfWordCreateDto.getReportId() != null && !"".equals(cfWordCreateDto.getReportId())) {
			strSQL = strSQL + " AND reportId = '" + cfWordCreateDto.getReportId() + "' ";
			mysql = mysql + " AND reportId = '" + cfWordCreateDto.getReportId()
					+ "' ";
		}
		if (cfWordCreateDto.getmCfReportRate()!= null && !"".equals(cfWordCreateDto.getmCfReportRate())) {
			strSQL = strSQL + " AND reportrate = '" + cfWordCreateDto.getmCfReportRate() + "' ";
			mysql = mysql + " AND reportrate = '"
					+ cfWordCreateDto.getmCfReportRate() + "' ";
		}
//		if (cfWordCreateDto.getmYearMonth() != null && !"".equals(cfWordCreateDto.getmYearMonth())) {
//			strSQL = strSQL + " AND month = '" + cfWordCreateDto.getmYearMonth() + "' ";
//			mysql = mysql + " AND month = '" + cfWordCreateDto.getmYearMonth()
//					+ "' ";
//		}

		List<?> myList = cfWordCreateDao.queryBysql(mysql);
		if (myList.size() > 0) {
			strSQL = strSQL
					+ ") cf  on cfr.Outitemcode = cf.Outitemcode  WHERE  1 = 1 ";

			// 通过频度筛选指标描述表中需要报送的因子
			if ("1".equals(cfWordCreateDto.getmCfReportRate()))// 年报 modify lcw
																// 季度快报
			{
				strSQL = strSQL + " AND cfr.QuarterReport = '1' ";
			} else if ("2".equals(cfWordCreateDto.getmCfReportRate()))// 季报
			{
				strSQL = strSQL + " AND cfr.halfyearreport = '1' ";
			}
			// 只查询寿险因子
			strSQL = strSQL + " AND cfr.lifeinsurance1 = '1' ";
			strSQL = strSQL + " ORDER BY CFR.OUTITEMCODE ASC ";
			System.out.println("total----------------------------" + strSQL);
			return cfWordCreateDao.queryBysql(strSQL);
		} else {
			System.out.println("数据尚未上传");
			return null;
		}



	}
	/**
	 * 初始化mHashMap_Item
	 */
	private void initmHashMap_Item(){
		if (totalList != null) {
			for (int k = 0; k < totalList.size(); k++) {
				Map map= (Map) totalList.get(k);
				
				Vector vctValue = new Vector();
				String OutItemType = (String) map.get("line6");
				vctValue.add(OutItemType);// 集合中第一个是类型
				// 根据因子值类型取值
				String strValue = "";
				if ("06".equals(OutItemType))// 06 百分比型
				{
					strValue = (String) map.get("line3");
					if (null != strValue && !"".equals(strValue.trim())) {
						strValue = (new DecimalFormat("#0.0000")).format(Double
								.parseDouble(strValue));
					}
				} else if ("05".equals(OutItemType))// 05 数量型
				{
					strValue = (String) map.get("line3");
					if (null != strValue && !"".equals(strValue.trim())) {
						strValue = (new DecimalFormat("#0")).format(Double
								.parseDouble(strValue));
					}
				} else if ("04".equals(OutItemType))// 04 数值型
				{
					strValue = (String) map.get("line3");
					if (null != strValue && !"".equals(strValue.trim())) {
						strValue = (new DecimalFormat("#0.00")).format(Double
								.parseDouble(strValue));
					}
				} else if ("01".equals(OutItemType))// 01 短文本类型
				{
					strValue = (String) map.get("line5");
				} else if ("02".equals(OutItemType))// 02 描述型
				{
					strValue = (String) map.get("line8");
				} else if ("03".equals(OutItemType))// 文件型因子，输出因子描述
				{
					strValue = "该部分需要手工添加，因子说明：" + (String) map.get("line7");
				}
				vctValue.add(strValue);// 集合中第二个是值
				mHashMap_Item.put((String) map.get("line1"), vctValue);

			}
		}
	}
	/**
	 * 处理静态变量
	 * 
	 * @param document
	 * @return
	 */
	private Document dealConstVars(Document document) {
		List list = document.selectNodes("//w:p[contains(.,'&{')]");
		for (int i = 0; i < list.size(); i++) {
			StringBuffer content = new StringBuffer();
			Element e = (Element) list.get(i);
			List rs = e.elements("r");
			Element eb = null;
			int begin = -1;
			int end = rs.size();
			for (int j = 0; j < rs.size(); j++) {
				Element r = (Element) rs.get(j);

				if (r.element("t").getTextTrim().indexOf("&{") >= 0) {// 首节点
					begin = j;
					if (r.element("t").getTextTrim().indexOf("}") >= 0) {
						end = j;
					}
					eb = (Element) r;
					content.append(r.element("t").getTextTrim());
					// e.remove(r);
					if (begin == end) {
						if (eb != null) {
							eb.element("t").setText(getConstData(content.toString()));
							eb = null;
							begin = -1;
							end = rs.size();
							content = new StringBuffer();
							continue;
						}
					}
				}
				if (r.element("t").getTextTrim().indexOf("}") >= 0) {// 尾节点
					end = j;
					content.append(r.element("t").getTextTrim());
					e.remove(r);
				}
				if (begin >= 0 && j > begin && j < end) {// 中间节点
					content.append(r.element("t").getTextTrim());
					e.remove(r);
				}
			}
			if (eb != null) {
				eb.element("t").setText(getConstData(content.toString()));
			}
		}
		return document;
	}
	/**
	 * 获取表格中固定格式的内容
	 * @param var
	 * @return
	 */
	private String getConstData(String var) {
		if (var.indexOf("&{") < 0)
			return "";

		String constdata = "";
		int index_begin = var.indexOf("&{");
		int index_end = var.indexOf("}");
		String itemCode = var.substring(index_begin + 2, index_end);
		itemCode = itemCode.replaceAll(" ", "");

		constdata = (cfWordCreateDto.getmHashMap_Const().get(itemCode) == null) ? ""
				: (String) cfWordCreateDto.getmHashMap_Const().get(itemCode);

		return constdata;
	}
	/**
	 * 处理静态报表中变量(替换因子指标值)
	 * 
	 * @param document
	 * @return
	 */
	private Document dealTabConstVars(Document document) {
		List list = document.selectNodes("//w:p[contains(.,'#{')]");
		for (int i = 0; i < list.size(); i++) {
			Element e = (Element) list.get(i);
			replaceItemData(e);

		}

		return document;
	}
	/**
	 * 替换表格中动态的数据
	 * @param e
	 */
	private void replaceItemData(Element e) 
	{	
		StringBuffer content = new StringBuffer();
		List rs = e.elements("r");

		for (int j = 0; j < rs.size(); j++) {
			Element r = (Element) rs.get(j);
			content.append(r.element("t").getText());
			e.remove(r);
		}

		int index_begin = content.indexOf("#{");
		int index_end = content.indexOf("}");
		String temp_begin = content.substring(0, index_begin);// 截取 itemCode
																// 前面部分文字
		String temp_end = content.substring(index_end + 1, content.length());// 截取
																				// itemCode
																				// 后面部分文字
		String itemCode = content.substring(index_begin + 2, index_end);

		boolean bFlag_sz = false;// 是否有设置字体大小
		boolean bFlag_color = false;// 是否有设置字体颜色
		boolean bFlag_b = false;// 是否有设置字体加粗
		boolean bFlag_fn = false;// 是否有设置字体为 Times New Roman
		String var_sz = "";// 字体大小变量
		String var_color = "";// 字体颜色变量

		if (itemCode.indexOf("%") >= 0) {
			String var[] = itemCode.split("%");
			itemCode = var[0];
			for (int n = 0; n < var.length; n++) {
				if (var[n].indexOf("sz") >= 0) {
					bFlag_sz = bFlag_sz || true;
					var_sz = var[n].replaceAll("sz", "");
				}
				if (var[n].indexOf("cl") >= 0) {
					bFlag_color = bFlag_color || true;
					var_color = var[n].replaceAll("cl", "");
				}
				if (var[n].indexOf("b") >= 0) {
					bFlag_b = bFlag_b || true;
				}
				if (var[n].indexOf("fn") >= 0) {
					bFlag_fn = bFlag_fn || true;
				}
			}
		}

		String itemData = getItemData(itemCode);
		itemData = temp_begin + itemData + temp_end;
		Element r = e.addElement("w:r");
		Element rpr = r.addElement("w:rPr");
		
		if(bFlag_fn)
		{
			Element rFonts = rpr.addElement("w:rFonts");
			rFonts.addAttribute("w:ascii", "Times New Roman");
			rFonts.addAttribute("w:h-ansi", "Times New Roman");
			rpr.addElement("wx:font").addAttribute("wx:val", "Times New Roman");
		}
		else
		{
			rpr.addElement("w:rFonts").addAttribute("w:h-ansi", "宋体");
			rpr.addElement("wx:font").addAttribute("wx:val", "宋体");
		}
		
		if (bFlag_sz) {
			rpr.addElement("w:sz").addAttribute("w:val", var_sz);
		} else {
			rpr.addElement("w:sz").addAttribute("w:val", "21");// 默认五号字体
		}

		if (bFlag_color) {
			rpr.addElement("w:color").addAttribute("w:val", var_color);
		}

		if (bFlag_b) 
		{
			rpr.addElement("w:b").addAttribute("w:val", "on");
		}
		else
		{
			rpr.addElement("w:b").addAttribute("w:val", "off");
		}

		r.addElement("w:t").addText(itemData);
	}
	/**
	 * 替换固定因子变量
	 * 
	 * @param var
	 * @return
	 */
	private String getItemData(String itemCode) {
		String itemdata = "";
		String var = itemCode;
		itemCode = itemCode.replaceAll("if", "");// 去掉"是/否"符号
		itemCode = itemCode.replaceAll("have", "");// 去掉"有/无"符号
		itemCode = itemCode.replaceAll("w", "");
		itemCode = itemCode.replaceAll("p", "");
		itemCode = itemCode.replaceAll(" ", "");
		// 通过 itemCode 获得 对应值，并格式化
		String itemtype = "";
		if (mHashMap_Item.get(itemCode) != null) {
			Vector vctValue = (Vector)mHashMap_Item.get(itemCode);
			itemtype = (String) vctValue.get(0);
			itemdata = (String) vctValue.get(1);
		}

		if (var.indexOf("w") >= 0) {
			if (!"".equals(itemdata)) 
			{
				double dTemp = Double.parseDouble(itemdata==null?"0":itemdata);
				itemdata = String.valueOf(dTemp / 10000.0);
				itemdata = (new DecimalFormat("#0.00")).format(Double
						.parseDouble(itemdata));
			}
		} else if (var.indexOf("p") >= 0) {
			if (!"".equals(itemdata)) 
			{
				double dTemp = Double.parseDouble(itemdata);
				itemdata = String.valueOf(dTemp * 100.0);
				itemdata = (new DecimalFormat("#0.00")).format(Double
						.parseDouble(itemdata));
				itemdata = itemdata + "%";
			}
		}

		if ("04".equals(itemtype) && !"".equals(itemdata))// 数值型格式化
		{
			/*itemdata = (new DecimalFormat("#,##0.00")).format(Double
					.parseDouble(itemdata));*/
		}

		if (var.indexOf("if") >= 0)// "是/否"符号
		{
			if ("是".equals(itemdata.trim())) {
				itemdata = "（是■  否□）";
			} else 
			{
				itemdata = "（是□  否■）";
			}
		}
		if (var.indexOf("have") >= 0)// "有/无"符号
		{
			if ("有".equals(itemdata.trim())) {
				itemdata = "（有■  无□）";
			} else 
			{
				itemdata = "（有□  无■）";
			}
		}

		return itemdata;
	}
	/**
	 * 处理动态扩展行
	 * 
	 * @param document
	 * @return
	 */
	private Document deaTabDynamicVars(Document document) {
		// 获取所有行可扩展表 tbl
		List tbls = document.selectNodes("//w:tbl[contains(.,'${')]");
		for (int tblIndex = 0; tblIndex < tbls.size(); tblIndex++) {
			trs_new.clear();
			Element tbl = (Element) tbls.get(tblIndex);
		/*	
			Writer out;
			try {
				out = new OutputStreamWriter(new FileOutputStream("E:\\test123.txt"),"UTF-8");
				OutputFormat format = OutputFormat.createPrettyPrint();
				format.setEncoding("UTF-8");
				XMLWriter writer = new XMLWriter(out, format);
				writer.write(tbl);
				out.close();
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			// 判断是否为列可扩展表，并特殊处理
			if (dealColAddTable(tbl)) {
				continue;
			}
			List trs = tbl.elements("tr");
			// System.out.println("---trs.size = " + trs.size());
			dealTrs(trs);
			// 删除表中原有行
			for (int trIndex = 0; trIndex < trs.size(); trIndex++) {
				Element tr = (Element) trs.get(trIndex);
				tbl.remove(tr);
			}
			// 添加新的行
			for (int trIndex = 0; trIndex < trs_new.size(); trIndex++) {
				Element tr_new = (Element) trs_new.get(trIndex);
				tbl.add(tr_new);
			}
		}
		return document;
	}
	/**
	 * 判断是否为可扩展表
	 * @param tbl
	 * @return
	 */
	private boolean dealColAddTable(Element tbl) {
		if (isColAddTable(tbl, "${IL-1-1}")) {
			return dealTable_IL11(tbl, "${IL-1-1}");
		} else if (isColAddTable(tbl, "${IL-2-1}")) {
			return dealTable_IL21(tbl, "${IL-2-1}");
		} else if (isColAddTable(tbl, "${IL-3-1}")) {
			return dealTable_IL21(tbl, "${IL-3-1}");
		} else {
			return false;
		}

	}
	/**
	 * 判断是否为可扩展列
	 * @param tbl
	 * @return
	 */
	private boolean isColAddTable(Element tbl, String tblCode) {
		boolean bRet = false;
		List trs = tbl.elements("tr");
		for (int trIndex = 0; trIndex < trs.size(); trIndex++) {// 所有行
			Element tr = (Element) trs.get(trIndex);
			List tcs = tr.elements("tc");

			for (int tcIndex = 0; tcIndex < tcs.size(); tcIndex++) {
				Element tc = (Element) tcs.get(tcIndex);
				StringBuffer content = new StringBuffer();
				Element p = tc.element("p");
				List rs = p.elements("r");
				for (int k = 0; k < rs.size(); k++) {// 行中的单元格
					
					Element r = (Element) rs.get(k);
					if (r.element("t") == null) {  //modify by lcw 这里在取MR20_00015时为空，处理了一下，不太确定是否有问题
						System.out.println("*******null");
						continue;
					}
				    System.out.println("---- " + r.element("t").getText());
					content.append(r.element("t").getText());
				}

				// 判断
				if (content.indexOf(tblCode) >= 0) {
					bRet = bRet || true;
					break;
				}
			}
		}

		return bRet;
	}
	/**
	 * 
	 * @param tbl
	 * @return
	 */
	private boolean dealTable_IL11(Element tbl, String tblCode) {
		int index_begin = tblCode.indexOf("${");
		int index_end = tblCode.indexOf("}");
		tblCode = tblCode.substring(index_begin + 2, index_end).replaceAll(" ",
				"");

		List trs = tbl.elements("tr");
		List tcList = new ArrayList();
		// 通过表代码(tableid)获取行号
		if (!getRowDataFromDB(tblCode)) {
			return false;
		}
		int nRow = ssrs_rows.size();
		for (int rownum = 0; rownum < nRow; rownum++) {
			Map map=(Map) ssrs_rows.get(rownum);
			String rowno = (String) map.get("line1");
			// 通过行号获取该行，所有列的数据
			if (!getColDataFromDB(tblCode, rowno)) {
				return false;
			}
			List trList = new ArrayList();
			for (int rowIndex = 0; rowIndex < trs.size(); rowIndex++) {
				Element tr = (Element) trs.get(rowIndex);
				List tcs = tr.elements("tc");
				List colList = new ArrayList();
				for (int n = 3; n < tcs.size(); n++) {
					Element tc_cur = (Element) tcs.get(n);
					Element tc = tc_cur.createCopy();
					StringBuffer content = new StringBuffer();
					content = new StringBuffer();
					Element p = tc.element("p");
					List rs = p.elements("r");
					for (int k = 0; k < rs.size(); k++) {// 行中的单元格
						Element r = (Element) rs.get(k);
						content.append(r.element("t").getText());
						p.remove(r);
					}
//					System.out.println(" content = " + content);
					String colCode = content.toString();
					if (colCode.indexOf("}") >= 0) {
						colCode = colCode.substring(colCode.indexOf("}") + 1,
								colCode.length());
					}
					colCode = colCode.replaceAll(" ", "");
					Element r = p.addElement("w:r").addAttribute("wsp:rsidRPr",
							"004A35A6");
					Element rpr = r.addElement("w:rPr");
					rpr.addElement("w:rFonts").addAttribute("w:ascii", "宋体")
							.addAttribute("w:h-ansi", "宋体");
					// rpr.addElement("wx:font").addAttribute("wx:val", "宋体");
					rpr.addElement("w:kern").addAttribute("w:val", "0");
					rpr.addElement("w:sz-cs").addAttribute("w:val", "21");
					if (rowIndex == 2) {
						r.addElement("w:t").addText(
								String.valueOf(3 * (rownum - 1) + n));
					} else {
						String colValue = getColData(colCode);
						if (null == colValue || "".equals(colValue.trim())) {
							colValue = colCode;
						}
						r.addElement("w:t").addText(colValue);
					}
					colList.add(tc);

				}
				trList.add(colList);
			}
			tcList.add(trList);
		}
		// 去掉当前模板列，添加n列数据
		for (int rowIndex = 0; rowIndex < trs.size(); rowIndex++) {
			Element tr = (Element) trs.get(rowIndex);
			List tcs = tr.elements("tc");
			for (int n = 3; n < tcs.size(); n++) {
				Element tc = (Element) tcs.get(n);
				tr.remove(tc);
			}

			for (int tcIndex = 0; tcIndex < tcList.size(); tcIndex++) {
				List trList = (List) tcList.get(tcIndex);
				List colList = (List) trList.get(rowIndex);
				for (int colIndex = 0; colIndex < colList.size(); colIndex++) {
					tr.add((Element) colList.get(colIndex));
				}
			}
		}
		return true;
	}
	/**
	 * 从数据库中获取行数据
	 * 扩展行用
	 * @param tableid
	 * @return
	 */
	private boolean getRowDataFromDB(String tableid) {
		/*--通过表代码取行可扩展数据表中获取数据不为空的行的行号；数据全部为空的行，不报送--*/
		String strSQL = "";
			strSQL = "SELECT DISTINCT cf.rowno line1 "
					+ " FROM cfreportrowadddata cf WHERE 1= 1 and UPPER(cf.tablecode) = '"
					+ tableid
					+ "' "
					+ " AND (cf.yearmonth) = '"
					+ cfWordCreateDto.getmYearMonth()
					+ "' "
					+ " AND CF.QUARTER = '"
					+ cfWordCreateDto.getmQuarter()
					+ "' "
					+ " AND CF.REPORTRATE = '"
					+ cfWordCreateDto.getmCfReportRate()
					+ "' "
					+ " AND (nvl(CF.NUMVALUE, 0.00) <> 0.00 OR nvl(CF.WANVALUE, 0.00) <> 0.00 OR CF.TEXTVALUE IS NOT NULL OR CF.DESTEXT IS NOT NULL) "
					+ " ORDER BY to_number(RowNo) ASC ";
		
			System.out.println("row----------------------------"+strSQL);
		ssrs_rows = cfWordCreateDao.queryBysql(strSQL);
		listRowData = ssrs_rows;
		return true;
	}
	/**
	 * 从数据库中获取列数据
	 * 
	 * @param tableid
	 * @return
	 */
	private boolean getColDataFromDB(String tableid, String rowno) {

		String strSQL = "";
			strSQL = "select cf.ROWNO line1,CFR.COLTYPE line2,CFR.COLCODE line3,to_char(CF.NUMVALUE) line4,to_char(CF.WANVALUE) line5,CF.TEXTVALUE line6,CF.DESTEXT line7,CFR.OUTITEMNOTE line8"
					+ " FROM CFREPORTROWADDDESC CFR left join (SELECT * FROM CFREPORTROWADDDATA WHERE 1 = 1 "
					+ "  AND QUARTER = '"
					+ cfWordCreateDto.getmQuarter()
					+ "' "
					+ "  AND YEARMONTH = '"
					+ cfWordCreateDto.getmYearMonth()
					+ "' "
					+ "  AND REPORTRATE = '"
					+ cfWordCreateDto.getmCfReportRate()
					+ "' "
					+ "  AND ROWNO = '"
					+ rowno
					+ "') CF "
					+ "  on UPPER(CFR.TABLECODE) = UPPER(CF.TABLECODE)AND CFR.COLCODE = CF.COLCODE "
					+ "   WHERE 1=1  AND UPPER(CFR.TABLECODE) = '"
					+ tableid
					+ "'  ORDER BY cfr.colcode ASC ";
			System.out.println("col----------------------------"+strSQL);
		ssrs_cols = cfWordCreateDao.queryBysql(strSQL);
		listColData = ssrs_cols;
		// 以列代码为Key构建列值HashMap
		mHashMap_cols.clear();
		for (int nRow = 0; nRow < ssrs_cols.size(); nRow++) {
			Map map=(Map) ssrs_cols.get(nRow);
			String ColType = (String) map.get("line2");
			String colcode = (String) map.get("line3");
			Vector vctValue = new Vector();
			vctValue.add(ColType);
			// 根据列类型取值
			String NumValue = "";
			if ("06".equals(ColType))// 06 百分比型
			{
				NumValue = (String) map.get("line4");
				if (null != NumValue && !"".equals(NumValue.trim())) {

					NumValue = (new DecimalFormat("#0.0000")).format(Double
							.parseDouble(NumValue));
				} else {
					NumValue = "0.0000";
				}
			} else if ("05".equals(ColType))// 05 数量型
			{
				NumValue = (String) map.get("line4");
				if (null != NumValue && !"".equals(NumValue.trim())) {
					NumValue = (new DecimalFormat("#0")).format(Double
							.parseDouble(NumValue));
				} else {
					NumValue = "0";
				}
			} else if ("04".equals(ColType))// 04 数值型
			{
				NumValue = (String) map.get("line4");
				if (null != NumValue && !"".equals(NumValue.trim())) {
					NumValue = (new DecimalFormat("#0.00")).format(Double
							.parseDouble(NumValue));
				} else {
					NumValue = "0.00";
				}
			} else if ("01".equals(ColType) || "02".equals(ColType))// 01
																		// 短文本类型,02
																		// 描述型
			{
				NumValue = (String) map.get("line6");
			} else {
				NumValue = "";
			}
			vctValue.add(NumValue);
			mHashMap_cols.put(colcode, vctValue);
		}

		return true;
	}
	
	/**
	 * 
	 * @param colnum
	 * @return
	 */
	private String getColData(String var) {
		var = var.replaceAll(" ", "");
		String colcode =var;
		String unit = "";
		if (var.indexOf("%") >= 0) {
			colcode = var.split("%")[0];
			unit = var.split("%")[1];
		} else {
			colcode = var;
		}

		String strValue = "";
		String type = "";

		if (mHashMap_cols.get(colcode) != null) {
			Vector vctValue = (Vector) mHashMap_cols.get(colcode);
			type = (String) vctValue.get(0);
			strValue = (String) vctValue.get(1);
		}
//		System.out.println("@@@@@@@@@@@@@unit     :"+unit);
//		System.out.println("@@@@@@@@@@@@@colcode  :"+colcode);
//		System.out.println("@@@@@@@@@@@@@strValue :"+strValue);
		if ("p".equals(unit)) {
			double dTemp = Double.parseDouble(strValue.equals("")?"0":strValue);
			strValue = String.valueOf(dTemp * 100.0);
			strValue = (new DecimalFormat("#0.00")).format(Double
					.parseDouble(strValue));
			strValue = strValue + "%";
		} else if ("w".equals(unit)) {					
			double dTemp = Double.parseDouble(strValue.equals("")?"0":strValue);
			strValue = String.valueOf(dTemp / 10000.0);
			strValue = (new DecimalFormat("#0.00")).format(Double
					.parseDouble(strValue));
		}

		if ("04".equals(type) && !"".equals(strValue))// 数值型格式化
		{
			strValue = (new DecimalFormat("#,##0.00")).format(Double
					.parseDouble(strValue.equals("")?"0":strValue));
		}
		
		/*---处理'1' 到 '√' 的转换-------*/
		if("vote".equals(unit))
		{
			if("1".equals(strValue))
			{
				strValue = "√";
			}
		}
		return strValue;
	}
	/**
	 * 
	 * @param tbl
	 * @return
	 */
	private boolean dealTable_IL21(Element tbl, String tblCode) {
		int index_begin = tblCode.indexOf("${");
		int index_end = tblCode.indexOf("}");
		tblCode = tblCode.substring(index_begin + 2, index_end).replaceAll(" ",
				"");

		List trs = tbl.elements("tr");
		List tcList = new ArrayList();
		// 通过表代码(tableid)获取行号
		if (!getRowDataFromDB(tblCode)) {
			return false;
		}
		int nRow = ssrs_rows.size();
		for (int rownum = 0; rownum < nRow; rownum++) {
			Map map=(Map) ssrs_rows.get(rownum);
			String rowno = (String) map.get("line1");
			// 通过行号获取该行，所有列的数据
			if (!getColDataFromDB(tblCode, rowno)) {
				return false;
			}
			List trList = new ArrayList();
			for (int rowIndex = 0; rowIndex < trs.size(); rowIndex++) {
				Element tr = (Element) trs.get(rowIndex);
				List tcs = tr.elements("tc");

				Element tc_cur = (Element) tcs.get(3);
				Element tc = tc_cur.createCopy();
				StringBuffer content = new StringBuffer();
				content = new StringBuffer();
				Element p = tc.element("p");
				List rs = p.elements("r");
				for (int k = 0; k < rs.size(); k++) {// 行中的单元格
					Element r = (Element) rs.get(k);
					content.append(r.element("t").getText());
					p.remove(r);
				}
//				System.out.println(" content = " + content);
				String colCode = content.toString();
				if (colCode.indexOf("}") >= 0) {
					colCode = colCode.substring(colCode.indexOf("}") + 1,
							colCode.length());
				}
				colCode = colCode.replaceAll(" ", "");
				Element r = p.addElement("w:r").addAttribute("wsp:rsidRPr",
						"004A35A6");
				Element rpr = r.addElement("w:rPr");
				rpr.addElement("w:rFonts").addAttribute("w:ascii", "宋体")
						.addAttribute("w:h-ansi", "宋体");
				// rpr.addElement("wx:font").addAttribute("wx:val", "宋体");
				rpr.addElement("w:kern").addAttribute("w:val", "0");
				rpr.addElement("w:sz-cs").addAttribute("w:val", "21");
				if (rowIndex == 1) {
					r.addElement("w:t").addText(String.valueOf(rownum + 1));
				} else {
					r.addElement("w:t").addText(getColData(colCode));
				}

				trList.add(tc);
			}
			tcList.add(trList);
		}
		// 去掉当前模板列，添加n列数据
		for (int rowIndex = 0; rowIndex < trs.size(); rowIndex++) {
			Element tr = (Element) trs.get(rowIndex);
			List tcs = tr.elements("tc");
			Element tc = (Element) tcs.get(3);
			tr.remove(tc);

			for (int colIndex = 0; colIndex < tcList.size(); colIndex++) {
				List trList = (List) tcList.get(colIndex);
				tr.add((Element) trList.get(rowIndex));
			}
		}

		return true;
	}
	
	/**
	 * 可能是处理行数据用的
	 * @param trs
	 */
	private void dealTrs(List trs) {
		List trsAfter = new ArrayList();
		int trIndexCur = trs.size();
		String strContent = "";
		for (int trIndex = 0; trIndex < trs.size(); trIndex++) {// 所有行
			Element tr = (Element) trs.get(trIndex);
			List tcs = tr.elements("tc");
			// System.out.println("---tcs.size = " + tcs.size());

			// 获取该行中第一个单元格
			Element tc = (Element) tcs.get(0);
			StringBuffer content = new StringBuffer();
			Element p = tc.element("p");
			List rs = p.elements("r");
			for (int k = 0; k < rs.size(); k++) {// 行中的单元格
				Element r = (Element) rs.get(k);
				content.append(r.element("t").getText());
			}
//			System.out.println("content = " + content.toString());
			// 判断单元格中是否有 $
			if (content.indexOf("$") >= 0) {
				trIndexCur = trIndex;
				strContent = content.toString();
				break;
			} else {
				trs_new.add(tr);
			}
		}
//		System.out.println("-- trIndexCur = " + trIndexCur);

		// 如果剩下行中还有行可扩展代码，继续处理
		if (trIndexCur < trs.size()) {
			Element trCur = (Element) trs.get(trIndexCur);
			dealTrCur(trCur, strContent);// 生成动态行的内容
			trsAfter = trs.subList(trIndexCur + 1, trs.size());// 得到剩下的行
		}
		// 继续处理剩下的行
		if (trsAfter.size() > 0) {
			dealTrs(trsAfter);
		}
	}
	/**
	 * 
	 * @return
	 */
	private boolean dealTrCur(Element trCur, String strContent) {
		// 查询数据库生成该表对应可扩展行数据
		// System.out.println("-----strContent = " + strContent);
		if (strContent.indexOf("${") < 0) {
			return false;
		}
		int index_begin = strContent.indexOf("${");
		int index_end = strContent.indexOf("}");
		// String temp_begin = var.substring(0,index_begin);// 截取 itemCode
		// 前面部分文字
		// String temp_end = var.substring(index_end+1,var.length());// 截取
		// itemCode 后面部分文字
		String itemCode = strContent.substring(index_begin + 2, index_end);
		itemCode = itemCode.replaceAll(" ", "");
		String tableCode = "";
		String rowNo = "";
		if (itemCode.indexOf("%") >= 0) {
			tableCode = itemCode.split("%")[0];
			rowNo = itemCode.split("%")[1];
		} else {
			tableCode = itemCode;
		}

		// 通过表代码(tableid)获取行号
		if (!getRowDataFromDB(tableCode)) {
			return false;
		}

		int nRow = ssrs_rows.size();
		// 把数据放置到对应单元格中，并生成行
		for (int rownum = 0; rownum < nRow; rownum++) {
			Map map=(Map) ssrs_rows.get(rownum);
			String rowno = (String) map.get("line1");
			// 通过行号获取该行，所有列的数据
			if (!getColDataFromDB(tableCode, rowno)) {
				return false;
			}

			Element tr = trCur.createCopy();
			List tcs = tr.elements("tc");
			// System.out.println("-----tcs.size = " + tcs.size());
			/* 第一列为行号，生成行号---begin--- */
			Element tc1 = (Element) tcs.get(0);
			Element p1 = tc1.element("p");
			List rs1 = p1.elements("r");
			for (int k = 0; k < rs1.size(); k++) {// 行中的单元格
				Element r1 = (Element) rs1.get(k);
				p1.remove(r1);
			}
			
			Element r1 = p1.addElement("w:r");
			Element rpr1 = r1.addElement("w:rPr");
			rpr1.addElement("w:rFonts").addAttribute("w:h-ansi", "宋体");
			// rpr1.addElement("wx:font").addAttribute("wx:val", "宋体");

			rpr1.addElement("w:sz-cs").addAttribute("w:val", "21");
			r1.addElement("w:t").addText(rowNo.replaceAll("x", rowno));
			/* 第一列为行号，生成行号---end--- */
			for (int nCol = 1; nCol < tcs.size(); nCol++) {// 依次把数据库中列数据放到单元格中
				StringBuffer content = new StringBuffer();
				Element tc = (Element) tcs.get(nCol);
				Element p = tc.element("p");
				List rs = p.elements("r");
				for (int k = 0; k < rs.size(); k++) {// 行中的单元格
					Element r = (Element) rs.get(k);
					content.append(r.element("t").getText());
					p.remove(r);
				}
//				System.out.println("content = " + content.toString());
				Element r = p.addElement("w:r");
				Element rpr = r.addElement("w:rPr");
				rpr.addElement("w:rFonts").addAttribute("w:h-ansi", "宋体");
				// rpr.addElement("wx:font").addAttribute("wx:val", "宋体");
				rpr.addElement("w:sz-cs").addAttribute("w:val", "21");
				rpr.addElement("w:b").addAttribute("w:val", "off");//避免变加黑
				String cod=tableCode+"_"+content.toString();
				String dd=getColData(cod);
				r.addElement("w:t").addText(dd==null?"":dd);
			}
			trs_new.add(tr);
		}
		
		/*--添加空行--begin--*/
		Element tr = trCur.createCopy();
		List tcs = tr.elements("tc");
		for (int nCol = 0; nCol < tcs.size(); nCol++) {
			StringBuffer content = new StringBuffer();
			Element tc = (Element) tcs.get(nCol);
			Element p = tc.element("p");
			List rs = p.elements("r");
			for (int k = 0; k < rs.size(); k++) {// 行中的单元格
				Element r = (Element) rs.get(k);
				content.append(r.element("t").getText());
				p.remove(r);
			}
		}
		trs_new.add(tr);
		/*--添加空行--end--*/
		
		return true;
	}
	public static void main(String[] args) throws DocumentException{

		/*ApplicationContext ctx=new ClassPathXmlApplicationContext("spring\\application.xml");
		BaseDAO dao=(BaseDAO) ctx.getBean("dao");
		// System.out.println("---" + document.getRootElement().getName());
		CfWordCreateServiceImp crt = new CfWordCreateServiceImp();
//	crt.conn = DBConnPool.getConnection();
		CfWordCreateDTO cfdto=new CfWordCreateDTO();
		cfdto.setmCfReportRate("1");
		cfdto.setmYear("2010");
		cfdto.setmQuarter("4");
//			crt.dealTabConstVars(document);
//			crt.deaTabDynamicVars(document);
		// crt.storeDoc(document, "E:\\test012.doc", "UTF-8");
//			crt.conn.close();
//			crt.conn = null;
		crt.initmHashMap_Item();*/
		CfWordCreateServiceImp crt = new CfWordCreateServiceImp();
		SAXReader saxReader = new SAXReader();
		Document document;
		
		
		File file=new File("E:\\Ecl\\lunaW\\cf\\WebContent\\upload\\mould\\word\\寿险公司临时报告偿付能力报告模版2016.doc");
				document = saxReader.read(file);
		//		document = saxReader.read(new File("E:\\Ecl\\lunaW\\cf\\WebContent\\upload\\mould\\word\\偿二代季度报告word模板.docx"));
	/*	document = crt.dealConstVars(document);

		// 处理静态报表中变量(替换因子指标值)
		document = crt.dealTabConstVars(document);

		// 处理动态报表(替换行可扩展值)
		document = crt.deaTabDynamicVars(document);*/
		List list=document.selectNodes("//pkg:xmlData/w:document");
		System.out.println(list.size());
		//crt.storeDoc(document,"E:\\Ecl\\lunaW\\cf\\WebContent\\upload\\mould\\word\\3467.rtf", "UTF-8");
	}
}
