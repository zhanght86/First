package com.sinosoft.zcfz.service.impl.reportformcompute;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sinosoft.common.Constant;
import com.sinosoft.common.Dao;
import com.sinosoft.common.PubFun;
import com.sinosoft.entity.CfReportData;
import com.sinosoft.entity.CfReportDataId;
import com.sinosoft.entity.CfReportDataRisk;
import com.sinosoft.entity.CfReportDataRiskId;
import com.sinosoft.entity.CfReportRowAddDataRisk;
import com.sinosoft.entity.CfReportRowAddDataRiskId;
import com.sinosoft.entity.LaCalCfReportRelation;
import com.sinosoft.entity.CalCfReportInfo;
import com.sinosoft.entity.CfReportElement;
import com.sinosoft.entity.CfReportItemCodeDesc;
import com.sinosoft.entity.OCfReportItemCodeDesc;
import com.sinosoft.entity.RhoMatrixDefine;
import com.sinosoft.entity.UserInfo;
import com.sinosoft.zcfz.service.reportform.ExportXbrlService;
import com.sinosoft.zcfz.service.reportformcompute.LaComputeService;

import Jama.Matrix;

@Service
public class LaComputeServiceImp implements LaComputeService{
	@Resource
	private Dao dao;
	@Resource
	private ExportXbrlService exportXbrlService;
	private String organCode=Constant.ORGANCODE;
	private static final Logger LOGGER = LoggerFactory.getLogger(LaComputeServiceImp.class);
	/**
	 * 获取前N季度的季度和年
	 * @param 年度
	 * @param 季度
	 * @param 前几季度
	 * @return
	 */
	private String[] getquarter(String year,String quarter,int how){
		int intyear=Integer.parseInt(year);
		int intquarter=Integer.parseInt(quarter);
		return getquarter(intyear,intquarter,how);
		
	}
	private String[] getquarter(int year,int quarter,int how){
		String[] result=new String[2];
		int resultquart=quarter-how;
		int resultyear=year;
		if(quarter-how<=0){
			resultquart=resultquart+4;
			resultyear=year-1;
		}
		result[0]=String.valueOf(resultyear);
		result[1]=String.valueOf(resultquart);
		return result;
		
	}
	/**
	 * la计算的逻辑
	 * @param id
	 * @throws Exception 
	 */
	@Transactional
	public void laCompute(String id, UserInfo user) throws Exception {
		//la初步计算
		laPreCompute(id,user);
		//la汇总计算
		laComplexCompute(id,user);
		//将计算得出的损失吸收效应调整上限保存到全账套数据中
		putLoseInAll(id,user);
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional
    public void laPreCompute(String id, UserInfo user){
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd  HH:mm:ss" );
		CalCfReportInfo ccri=new CalCfReportInfo();
		ccri=(CalCfReportInfo) dao.get(CalCfReportInfo.class, id);
		String year=ccri.getYear();
		String quarter=ccri.getQuarter();
		String day=ccri.getDataDate();
		//la初步计算前删除数据防止唯一主键冲突
		String lsql1="delete from cfreportrowadddatarisk where reportid='"+id+"'  and colcode in (select portitemcode from lacal_cfreportrelation where portitemtype='1' and interfacetype='0')";  
		String lsql2="delete from cfreportdatarisk where reportid='"+id+"' and outitemcode in (select portitemcode from lacal_cfreportrelation where portitemtype='0' and interfacetype='0')";
		try{
			dao.excute(lsql1);
			dao.excute(lsql2);
		}catch(Exception e){
			LOGGER.error("初步计算删除la数据出错！");
        	throw new RuntimeException("初步计算删除la数据出错！");
		}
		//报送类型
		String reportType=ccri.getReportType();
		List<LaCalCfReportRelation> list=(List<LaCalCfReportRelation>) dao.query("from LaCalCfReportRelation where interfaceType='0' order by portitemcode");
		for(int i=0;i<list.size();i++){
			LaCalCfReportRelation ccrr=list.get(i);
			//指标代码
			String portItemCode=ccrr.getPortItemCode();
			//取数规则
			String interfaceGetData=ccrr.getInterfaceGetData();
			if(interfaceGetData==null||"".equals(interfaceGetData)){
				continue;
			}
			//指标类型
			String portItemType=ccrr.getPortItemType();
			//取数规则参数
			String InterfaceGetDataVar=ccrr.getInterfaceGetDataVar();
			if(InterfaceGetDataVar!=null&&!"".equals(InterfaceGetDataVar)){
				if(InterfaceGetDataVar.contains("firstquarterago")){
					String[] fistquarterago=getquarter(year,quarter,1);
					InterfaceGetDataVar=InterfaceGetDataVar.replace("year", fistquarterago[0]);
					InterfaceGetDataVar=InterfaceGetDataVar.replace("firstquarterago", fistquarterago[1]);
				}else if(InterfaceGetDataVar.contains("twoquarterago")){
					String[] twoquarterago=getquarter(year,quarter,1);
					InterfaceGetDataVar=InterfaceGetDataVar.replace("year", twoquarterago[0]);
					InterfaceGetDataVar=InterfaceGetDataVar.replace("twoquarterago", twoquarterago[1]);
				}else if(InterfaceGetDataVar.contains("threequarterago")){
					String[] threequarterago=getquarter(year,quarter,1);
					InterfaceGetDataVar=InterfaceGetDataVar.replace("year", threequarterago[0]);
					InterfaceGetDataVar=InterfaceGetDataVar.replace("threequarterago", threequarterago[1]);
				}else{
					try{
						InterfaceGetDataVar=InterfaceGetDataVar.replace("year", year);
					}catch(Exception e){
						//说明参数中没有year或者quarter，继续执行
					}
					try{
						InterfaceGetDataVar=InterfaceGetDataVar.replace("quarter", quarter);
					}catch(Exception e){
						//说明参数中没有year或者quarter，继续执行
					}
					try{
						InterfaceGetDataVar=InterfaceGetDataVar.replace("day", day);
					}catch(Exception e){
						//说明参数中没有year或者quarter，继续执行
					}
					try{
						InterfaceGetDataVar=InterfaceGetDataVar.replace("reportId", id);
					}catch(Exception e){
						//说明参数中没有year或者quarter或day，继续执行
					}
				}
			}
			String DBDecimals="";
			//取精度
			List<CfReportElement> listCfReportElement=(List<CfReportElement>) dao.query("from CfReportElement where portItemCode='"+portItemCode+"'");
			if(listCfReportElement!=null&&!"".equals(listCfReportElement)){
				if(listCfReportElement.size()>0){
					DBDecimals=listCfReportElement.get(0).getDecimals();
				}
			}
			if("0".equals(portItemType)){
				try{
					List<Map> listData=null;
					String value="";
					//取到值
					if(InterfaceGetDataVar!=null&&!"".equals(InterfaceGetDataVar)){
						listData= (List<Map>) dao.getList(interfaceGetData,InterfaceGetDataVar.split(","));
					}else{
						listData=(List<Map>) dao.getList(interfaceGetData);
					}
					if(listData.size()==0){
						value="0";
					}else{
						value=String.valueOf(listData.get(0).get("value"));
					}
					System.out.println(portItemCode+"的值为："+value);
					CfReportDataRiskId lacfReportDataId=new CfReportDataRiskId();
				    CfReportDataRisk lacfReportData=new CfReportDataRisk();
				    lacfReportDataId.setOutItemCode(portItemCode);//指标代码
				    lacfReportDataId.setReportId(id);
				    lacfReportData.setId(lacfReportDataId);
				    lacfReportData.setMonth(year);//年度
				    lacfReportData.setQuarter(quarter);//季度
				    lacfReportData.setReportRate(reportType);//报送类型
				    lacfReportData.setComCode(organCode);//机构代码
				    lacfReportData.setOutItemCodeName("");//因子名称
				    lacfReportData.setReportItemCode(portItemCode);//因子代码
			        CfReportItemCodeDesc cricd=new CfReportItemCodeDesc();
			        OCfReportItemCodeDesc ocricd=new OCfReportItemCodeDesc();
			        cricd=(CfReportItemCodeDesc) dao.get(CfReportItemCodeDesc.class, portItemCode);
			        String strOutItemType="";
			        if(cricd==null||"".equals(cricd)){
			        	ocricd=(OCfReportItemCodeDesc) dao.get(OCfReportItemCodeDesc.class, portItemCode);
			        	if(ocricd==null||"".equals(ocricd)){
			        		LOGGER.error(portItemCode+"指标因子未找到!");
			        		throw new Exception(portItemCode+"指标因子未找到!");
			        	}else{
			        		strOutItemType=ocricd.getOutItemType();
			        	}
			        }else{
			        	strOutItemType=cricd.getOutItemType();
			        }
			        if(strOutItemType.equals("06")){//百分比型
				    	   try {
				    		   if("".equals(value)||value==null){		    		   
					    		   System.out.println(portItemCode+"无值可取！！！");
					    		   continue;
					    	   }else{
					    		   if(DBDecimals!=null&&!"".equals(DBDecimals)){
					    			   value =exportXbrlService.FormatData(value,DBDecimals); 
									}else{
										value =exportXbrlService.FormatData(value,"4"); 
									}
					    		   lacfReportData.setReportItemValue(new BigDecimal(value).setScale(8,BigDecimal.ROUND_HALF_UP));
					    		   lacfReportData.setReportItemWanValue(new BigDecimal(0).setScale(12,BigDecimal.ROUND_HALF_UP));
					    	   }
					       } catch (NumberFormatException e) {
					    	   LOGGER.error(portItemCode+"指标因子取值不是百分比类型!");
					    	   e.printStackTrace();
					    	   continue;
					       }
				    	   lacfReportData.setOutItemType("06");
				    	   lacfReportData.setTextValue("");
				    	   lacfReportData.setDesText(null);//描述性型
				    	   lacfReportData.setFileText(null);//文件型
				       }else if(strOutItemType.equals("05")){//数量型
				    	   try {
				    		   if("".equals(value)||value==null){		    		   
				    			   System.out.println(portItemCode+"无值可取！！！");
				    		   }else{
				    			   if(DBDecimals!=null&&!"".equals(DBDecimals)){
					    			   value =exportXbrlService.FormatData(value,DBDecimals); 
									}else{
										value =exportXbrlService.FormatData(value,"0"); 
									}
				    			   lacfReportData.setReportItemValue(new BigDecimal(value).setScale(8,BigDecimal.ROUND_HALF_UP));
				    			   lacfReportData.setReportItemWanValue(new BigDecimal(value).divide(new BigDecimal("10000"), 12, RoundingMode.HALF_UP));
				    		   }
					       } catch (NumberFormatException e) {
					    	   LOGGER.error(portItemCode+"指标因子取值不是数量类型!");
					    	   e.printStackTrace();
					    	   continue;
					       }
				    	   lacfReportData.setOutItemType("05");
				    	   lacfReportData.setTextValue("");
				    	   lacfReportData.setDesText(null);//描述性型
				    	   lacfReportData.setFileText(null);//文件型
				       }else if(strOutItemType.equals("04")){//数值型
				    	   try {
				    		   if("".equals(value)||value==null){		    		    		   
				    			   System.out.println(portItemCode+"无值可取！！！");
				    		   }else{
				    			   if(DBDecimals!=null&&!"".equals(DBDecimals)){
					    			   value =exportXbrlService.FormatData(value,DBDecimals); 
									}else{
										value =exportXbrlService.FormatData(value,"2"); 
									}
				    			   lacfReportData.setReportItemValue(new BigDecimal(value).setScale(8,BigDecimal.ROUND_HALF_UP));
				    			   lacfReportData.setReportItemWanValue(new BigDecimal(value).divide(new BigDecimal("10000"), 12, RoundingMode.HALF_UP));
				    		   }
					       } catch (NumberFormatException e) {
					    	   LOGGER.error(portItemCode+"指标因子取值不是数值类型!");
					    	   e.printStackTrace();
					       }
				    	   lacfReportData.setOutItemType("04");
				    	   lacfReportData.setTextValue("");
				    	   lacfReportData.setDesText(null);//描述性型
				    	   lacfReportData.setFileText(null);//文件型
				       }else if(strOutItemType.equals("03")){//文件型
				    	   try {
				    		   lacfReportData.setOutItemType("03");
				    		   lacfReportData.setReportItemValue(new BigDecimal(0).setScale(4,BigDecimal.ROUND_HALF_UP));
				    		   lacfReportData.setReportItemWanValue(new BigDecimal(0).setScale(8,BigDecimal.ROUND_HALF_UP));
					       } catch (NumberFormatException e) {
					    	   LOGGER.error(portItemCode+"指标因子取值不是文件类型!");
					    	   e.printStackTrace();
					    	   continue;
					       }
				    	   lacfReportData.setTextValue("");
				    	   lacfReportData.setDesText(null);//描述性型
				    	   lacfReportData.setFileText(null);//文件型
				       }else if(strOutItemType.equals("02")){ //描述
				    	   try {
				    		   lacfReportData.setOutItemType("02");
				    		   lacfReportData.setReportItemValue(new BigDecimal(0).setScale(4,BigDecimal.ROUND_HALF_UP));
				    		   lacfReportData.setReportItemWanValue(new BigDecimal(0).setScale(8,BigDecimal.ROUND_HALF_UP));
					       } catch (NumberFormatException e) {
					    	   LOGGER.error(portItemCode+"指标因子取值不是描述类型!");
					    	   e.printStackTrace();
					    	   continue;
					       }
				    	   lacfReportData.setTextValue(null);
				    	   lacfReportData.setDesText(null);//描述性型
				    	   lacfReportData.setFileText(null);//文件型
				       }else if(strOutItemType.equals("01")){//短文本类型
				    	   lacfReportData.setOutItemType("01");
				    	   lacfReportData.setReportItemValue(new BigDecimal(0).setScale(4,BigDecimal.ROUND_HALF_UP));
				    	   lacfReportData.setReportItemWanValue(new BigDecimal(0).setScale(8,BigDecimal.ROUND_HALF_UP));
				    	   try {
				    		   lacfReportData.setTextValue(value);
				    		   if(value==null||"null".equals(value)){
				    			   lacfReportData.setTextValue("");
				    		   }
					       } catch (NumberFormatException e) {
					    	   LOGGER.error(portItemCode+"指标因子取值不是短文本类型!");
					    	   e.printStackTrace();
					    	   continue;
					       }
				    	   lacfReportData.setDesText(null);//描述性型
				    	   lacfReportData.setFileText(null);//文件型
				       }
				       else if(strOutItemType.equals("07")){//日期型
				    	   lacfReportData.setOutItemType("07");
				    	   lacfReportData.setReportItemValue(new BigDecimal(0).setScale(8,BigDecimal.ROUND_HALF_UP));
				    	   lacfReportData.setReportItemWanValue(new BigDecimal(0).setScale(12,BigDecimal.ROUND_HALF_UP));
				    	   try {
				    		   lacfReportData.setTextValue(sdf.format(value));
					       } catch (NumberFormatException e) {
					    	   lacfReportData.setTextValue(value);
					       }
				    	   lacfReportData.setDesText(null);//描述性型
				    	   lacfReportData.setFileText(null);//文件型
				       }else{
				    	   System.out.println("导入因子值类型有误！！！");
				       }
			        lacfReportData.setSource("8");//源1：指标导入；源2：自动提数业务；源3：自动提数财务；源4：精算模板导入；源5：投资模板导入；源6：计算
			        lacfReportData.setReportType("1");//报表类别
			        lacfReportData.setComputeLevel("0");//计算级别
			        lacfReportData.setReportItemValueSource("1");//指标值来源：1-初步计算数据手工修改；2-尾数自动调整
			        lacfReportData.setOperator(user.getUserCode());//默认（操作人）
			        lacfReportData.setOperDate(sdf.format(new Date()));//操作日期
			        lacfReportData.setTemp("");
				    dao.create(lacfReportData);
				    dao.flush();
				}catch(Exception e){
					e.printStackTrace();
					LOGGER.error("请检查"+portItemCode+"配置的取数规则是否正确！");
					throw new RuntimeException("请检查"+portItemCode+"配置的取数规则是否正确！");
				}
			}else if("1".equals(portItemType)){
				List<Map> listData=null;
				//取到值
				if(InterfaceGetDataVar!=null&&!"".equals(InterfaceGetDataVar)){
					listData= (List<Map>) dao.getList(interfaceGetData,InterfaceGetDataVar.split(","));
				}else{
					listData=(List<Map>) dao.getList(interfaceGetData);
				}
				if(listData.size()==0){
					continue;
				}
				for(int j=0;j<listData.size();j++){
					Map resultMap=listData.get(j);
					String value=String.valueOf(resultMap.get("value"));
					String rowno=String.valueOf(j+1);
					if(resultMap.get("rowno")!=null&&!"".equals(resultMap.get("rowno"))&&!"null".equals(resultMap.get("rowno"))){
						rowno=String.valueOf(resultMap.get("rowno"));
					}
					CfReportRowAddDataRiskId lacfReportRowAddDataId=new CfReportRowAddDataRiskId();
					CfReportRowAddDataRisk lacfReportRowAddData=new CfReportRowAddDataRisk();
					lacfReportRowAddDataId.setColCode(portItemCode);//列代码
					lacfReportRowAddDataId.setRowNo(rowno);//行数
					lacfReportRowAddDataId.setReportId(id);
					lacfReportRowAddData.setId(lacfReportRowAddDataId);
					lacfReportRowAddData.setTableCode(portItemCode.split("_").length==2?portItemCode.split("_")[0]:portItemCode.split("_")[0]+"_"+portItemCode.split("_")[1]);//表代码
					lacfReportRowAddData.setYearMonth(year);//年度
					lacfReportRowAddData.setQuarter(quarter);//季度
					lacfReportRowAddData.setReportRate(reportType);//报送类型
					lacfReportRowAddData.setReportType("1");//报表类别
					lacfReportRowAddData.setSource("8");//源1：指标导入；源2：自动提数业务；源3：自动提数财务；源4：精算模板导入；源5：投资模板导入；源6：初步计算 7.汇总计算
					lacfReportRowAddData.setOperator2(user.getUserCode());//操作人
					lacfReportRowAddData.setOperDate2(sdf.format(new Date()));//操作日期
					lacfReportRowAddData.setComCode(organCode);
					System.out.println(portItemCode+"的值为："+value);
					//CfReportRowAddDesc cricd=new CfReportRowAddDesc();
					List listcrrad= dao.queryWithJDBC("select crrad.coltype from CfReportRowAddDesc crrad where crrad.colCode='"+portItemCode+"'");
					List listocrrad= dao.queryWithJDBC("select ocrrad.coltype from rsys_CfReportRowAddDesc ocrrad where ocrrad.colCode='"+portItemCode+"'");
					String strOutItemType="";
			        if(listcrrad.size()>0){
			        	Map map=(Map) listcrrad.get(0);
			        	strOutItemType=(String) map.get("coltype");
			        }else{
			        	if(listocrrad.size()>0){
				        	Map mapo=(Map) listocrrad.get(0);
				        	strOutItemType=(String) mapo.get("coltype");
				        }else{
				        	LOGGER.error(portItemCode+"指标因子未找到!");
				        	throw new RuntimeException(portItemCode+"指标因子未找到!");
				        }
			        }
					if(strOutItemType==null||"".equals(strOutItemType)){
						System.out.println("行可扩展因子代码未定义");
					}
					System.out.println("coltype="+strOutItemType);
					String strColtype=strOutItemType;
					if(strColtype.equals("06")){//百分比型
						try{
							if("".equals(value)||value==null){							
								value="0";
							}
							lacfReportRowAddData.setColType("06");
							if(DBDecimals!=null&&!"".equals(DBDecimals)){
				    			   value =exportXbrlService.FormatData(value,DBDecimals); 
								}else{
									value =exportXbrlService.FormatData(value,"4"); 
								}
							lacfReportRowAddData.setNumValue(new BigDecimal(value).setScale(8,BigDecimal.ROUND_HALF_UP));
							lacfReportRowAddData.setWanValue(new BigDecimal("0").setScale(12,BigDecimal.ROUND_HALF_UP));
						}catch(NumberFormatException e){
							 LOGGER.error(portItemCode+"指标因子取值不是百分比类型!");
							e.printStackTrace();
							continue;
						}
						lacfReportRowAddData.setTextValue("");
						lacfReportRowAddData.setDesText("");
					}else if(strColtype.equals("05")){//数量型
						try{
							if("".equals(value)||value==null){								
								value="0";
							}
							lacfReportRowAddData.setColType("05");
							if(DBDecimals!=null&&!"".equals(DBDecimals)){
				    			   value =exportXbrlService.FormatData(value,DBDecimals); 
								}else{
									value =exportXbrlService.FormatData(value,"0"); 
								}
							lacfReportRowAddData.setNumValue(new BigDecimal(value).setScale(8,BigDecimal.ROUND_HALF_UP));
							lacfReportRowAddData.setWanValue(new BigDecimal(value).divide(new BigDecimal("10000"), 12, RoundingMode.HALF_UP));
						}catch(NumberFormatException e){
							 LOGGER.error(portItemCode+"指标因子取值不是数量类型!");
							e.printStackTrace();
							continue;
						}
						lacfReportRowAddData.setTextValue("");
						lacfReportRowAddData.setDesText("");
					}else if(strColtype.equals("04")){//数值型
						try{
							if("".equals(value)||value==null){
								value="0";
							}
							lacfReportRowAddData.setColType("04");
							if(DBDecimals!=null&&!"".equals(DBDecimals)){
				    			   value =exportXbrlService.FormatData(value,DBDecimals); 
								}else{
									value =exportXbrlService.FormatData(value,"2"); 
								}
							lacfReportRowAddData.setNumValue(new BigDecimal(value).setScale(8,BigDecimal.ROUND_HALF_UP));
							lacfReportRowAddData.setWanValue(new BigDecimal(value).divide(new BigDecimal("10000"), 12, RoundingMode.HALF_UP));
						}catch(NumberFormatException e){
							 LOGGER.error(portItemCode+"指标因子取值不是数值类型!");
							e.printStackTrace();
							continue;
						}
						lacfReportRowAddData.setTextValue("");
						lacfReportRowAddData.setDesText("");		        	
					}else if(strColtype.equals("02")){//描述型
						try {
							lacfReportRowAddData.setColType("02");
							lacfReportRowAddData.setNumValue(new BigDecimal("0").setScale(4,BigDecimal.ROUND_HALF_UP));
							lacfReportRowAddData.setWanValue(new BigDecimal("0").setScale(8,BigDecimal.ROUND_HALF_UP));
							lacfReportRowAddData.setTextValue(value);
							if(value==null||"null".equals(value)){
								lacfReportRowAddData.setTextValue("");
							}
						} catch (NumberFormatException e) {
							 LOGGER.error(portItemCode+"指标因子取值不是描述类型!");
							e.printStackTrace();
							continue;
						}
						lacfReportRowAddData.setDesText("");		        	
					}else if(strColtype.equals("01")){//短文本类型
						try {
							lacfReportRowAddData.setColType("01");
							lacfReportRowAddData.setNumValue(new BigDecimal("0").setScale(4,BigDecimal.ROUND_HALF_UP));
							lacfReportRowAddData.setWanValue(new BigDecimal("0").setScale(8,BigDecimal.ROUND_HALF_UP));
							lacfReportRowAddData.setTextValue(value);
						} catch (NumberFormatException e) {
							 LOGGER.error(portItemCode+"指标因子取值不是短文本类型!");
							e.printStackTrace();
							continue;
						}
						lacfReportRowAddData.setDesText("");		        	
					}else if(strColtype.equals("07")){//日期型
						try {
							lacfReportRowAddData.setColType("07");
							lacfReportRowAddData.setNumValue(new BigDecimal("0").setScale(8,BigDecimal.ROUND_HALF_UP));
							lacfReportRowAddData.setWanValue(new BigDecimal("0").setScale(12,BigDecimal.ROUND_HALF_UP));
							//TODO
							lacfReportRowAddData.setTextValue(value);
							lacfReportRowAddData.setDesText("");		        	
						} catch (NumberFormatException e) {
							 LOGGER.error(portItemCode+"指标因子取值不是日期类型!");
							e.printStackTrace();
							continue;
						}
					}else{
						System.out.println("导入因子值类型有误！！！");
					}
					dao.create(lacfReportRowAddData);
					dao.flush();
				}
			}
		}
		
		
    }
	/**
	 * 汇总计算的逻辑
	 * @param id
	 * @throws Exception 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional
	public void laComplexCompute(String id,UserInfo user) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd  HH:mm:ss" );
		CalCfReportInfo ccri=new CalCfReportInfo();
		ccri=(CalCfReportInfo) dao.get(CalCfReportInfo.class, id);
		String year=ccri.getYear();
		String quarter=ccri.getQuarter();
		String day=ccri.getDataDate();
		//报送类型
		String reportType=ccri.getReportType();
		//汇总计算前删除汇总计算的数据防止唯一主键冲突
		String lsql1="delete from cfreportrowadddatarisk where reportid='"+id+"' and colcode in (select portitemcode from lacal_cfreportrelation where portitemtype='1' and interfacetype='1')";  
		String lsql2="delete from cfreportdatarisk where reportid='"+id+"' and outitemcode in (select portitemcode from lacal_cfreportrelation where portitemtype='0' and interfacetype='1')";
		try{
			dao.excute(lsql1);
			dao.excute(lsql2);
		}catch(Exception e){
			LOGGER.error("汇总计算删除la数据出错！");
        	throw new RuntimeException("汇总计算删除la数据出错！");
		}
		int ifpc=dao.getCount("select count(*) from (select distinct interfacePriority from laCal_CfReportRelation where interfaceType=?)", "1");
		List<Map> interfacePrioritys=(List<Map>) dao.getList("select distinct interfacePriority as interfacePriority from laCal_CfReportRelation where interfaceType=? order by interfacePriority", "1");
		for(int c=1;c<=ifpc;c++){
			String interfacePriority="";
			try{
				interfacePriority=String.valueOf(interfacePrioritys.get(c-1).get("interfacePriority"));
				System.out.println("开始计算优先级为："+interfacePriority+"的汇总数据----------------");
				
			}catch(Exception e){
				LOGGER.error("请检查优先级的配置是否正确！");
	        	throw new RuntimeException("请检查优先级的配置是否正确！");
			}
			List<LaCalCfReportRelation> list=(List<LaCalCfReportRelation>) dao.query("from LaCalCfReportRelation where interfaceType='1' and interfacePriority='"+interfacePriority+"' order by portitemcode");
			for(int i=0;i<list.size();i++){
				LaCalCfReportRelation ccrr=list.get(i);
				//指标代码
				String portItemCode=ccrr.getPortItemCode();
				if(portItemCode.equals("S07_00024")){
					System.out.println("1223");
				}
				//取数规则
				String interfaceGetData=ccrr.getInterfaceGetData();
				//指标类型
				String portItemType=ccrr.getPortItemType();
				//取数规则参数
				String InterfaceGetDataVar=ccrr.getInterfaceGetDataVar();
				//计算规则类型
				String calculateType=ccrr.getCalculateType();
				//计算规则
				String calculateRule=ccrr.getCalculateRule();
				//参数标志
				String varFlag=ccrr.getVarFlag();
				//计算参数
				String computeVar=ccrr.getComputeVar();
				//计算参数取数规则
				String computeVarRule=ccrr.getComputeVarRule();
				//对于如果规则中加号的处理
				boolean flag=false; //没有加号
				if(calculateRule!=null&&!"".equals(calculateRule)){
					if(calculateRule.contains("+")||calculateRule.contains("-")){
						flag=true;
					}
				}
				String DBDecimals="";
				//取精度
				List<CfReportElement> listCfReportElement=(List<CfReportElement>) dao.query("from CfReportElement where portItemCode='"+portItemCode+"'");
				if(listCfReportElement!=null&&!"".equals(listCfReportElement)){
					if(listCfReportElement.size()>0){
						DBDecimals=listCfReportElement.get(0).getDecimals();
					}
				}
				if(InterfaceGetDataVar!=null&&!"".equals(InterfaceGetDataVar)){
					if(InterfaceGetDataVar.contains("firstquarterago")){
						String[] fistquarterago=getquarter(year,quarter,1);
						InterfaceGetDataVar=InterfaceGetDataVar.replace("year", fistquarterago[0]);
						InterfaceGetDataVar=InterfaceGetDataVar.replace("firstquarterago", fistquarterago[1]);
					}else if(InterfaceGetDataVar.contains("twoquarterago ")){
						String[] twoquarterago=getquarter(year,quarter,1);
						InterfaceGetDataVar=InterfaceGetDataVar.replace("year", twoquarterago[0]);
						InterfaceGetDataVar=InterfaceGetDataVar.replace("twoquarterago", twoquarterago[1]);
					}else if(InterfaceGetDataVar.contains("threequarterago ")){
						String[] threequarterago=getquarter(year,quarter,1);
						InterfaceGetDataVar=InterfaceGetDataVar.replace("year", threequarterago[0]);
						InterfaceGetDataVar=InterfaceGetDataVar.replace("threequarterago", threequarterago[1]);
					}else{
						try{
							InterfaceGetDataVar=InterfaceGetDataVar.replace("year", year);
						}catch(Exception e){
							//说明参数中没有year或者quarter或day，继续执行
						}
						try{
							InterfaceGetDataVar=InterfaceGetDataVar.replace("quarter", quarter);
						}catch(Exception e){
							//说明参数中没有year或者quarter或day，继续执行
						}
						try{
							InterfaceGetDataVar=InterfaceGetDataVar.replace("day", day);
						}catch(Exception e){
							//说明参数中没有year或者quarter或day，继续执行
						}
						try{
							InterfaceGetDataVar=InterfaceGetDataVar.replace("reportId", id);
						}catch(Exception e){
							//说明参数中没有year或者quarter或day，继续执行
						}
					}
				}
				List<Map> listData=null;
				//取到值
				if(interfaceGetData!=null&&!"".equals(interfaceGetData)){
					if(interfaceGetData.contains("select")){

						if(InterfaceGetDataVar!=null&&!"".equals(InterfaceGetDataVar)){
							try{
								listData= (List<Map>) dao.getList(interfaceGetData,InterfaceGetDataVar.split(","));
							}catch(Exception e){
								LOGGER.error("请检查"+portItemCode+"的配置中取数sql是否正确");
								//throw new RuntimeException("请检查"+portItemCode+"的配置中取数sql是否正确");
								continue;
							}
						}else{
							try{
								listData=(List<Map>) dao.getList(interfaceGetData);
							}catch(Exception e){
								LOGGER.error("请检查"+portItemCode+"的配置中取数sql是否正确");
								throw new RuntimeException("请检查"+portItemCode+"的配置中取数sql是否正确");
							}
						}
					}else{
						if("0".equals(portItemType)){
							listData=(List<Map>) dao.getList("select case when outitemtype in ('01','07') then  textvalue else to_char(reportitemvalue) end as value from cfreportdatarisk where reportid=? and month=? and quarter=? and outitemcode=?",id,year,quarter,interfaceGetData);
						}else{
							//如果根据rowno来决定行数取上面注释的
							//listData=(List<Map>) dao.getList("select rowno as rowno,case when coltype in ('01','07') then  textvalue else to_char(numvalue) end as value from cfreportrowadddata where colcode=?  and yearmonth=? and quarter=? order by to_number(rowno)",interfaceGetData,year,quarter);
							listData=(List<Map>) dao.getList("select case when coltype in ('01','07') then  textvalue else to_char(numvalue) end as value from cfreportrowadddatarisk where colcode=?  and yearmonth=? and quarter=? order by to_number(rowno)",interfaceGetData,year,quarter);
						}
					}
				}
				if("0".equals(portItemType)){//固定因子
					String value="0";
					if(listData!=null&&!"".equals(listData)){
						if(listData.size()>0){
							value=String.valueOf(listData.get(0).get("value"));
						}
					}
					if("1".equals(calculateType)){
						ScriptEngineManager manager = new ScriptEngineManager();
						ScriptEngine engine = manager.getEngineByName("js");
						if("0".equals(varFlag)){
							try{
								BigDecimal args=new BigDecimal(value);
								engine.put("x", args);
							}catch(Exception e){
								LOGGER.warn(portItemCode+"配置的未知数x不是一个数值类型----"+value);
								engine.put("x", value);
							}
						}else if("1".equals(varFlag)){
							int len=computeVar.split(",").length;
							String[] computeVars=computeVar.split(",");
							String[] computeVarRules=computeVarRule.split(";");
							ScriptEngine engineMatrix = manager.getEngineByName("js");
							for(int l=0;l<len;l++){
								if(computeVarRules[l].contains("select")){
									try{
										List<Map> listVarData=(List<Map>) dao.getList(computeVarRules[l],year,quarter);
										if(listVarData.size()==0){
											LOGGER.warn("请检查"+portItemCode+"的配置中参数取数"+computeVarRules[l]+"是否正确");
											if(flag){
												Map map=new HashMap();
												map.put("value", "0");
												listVarData.add(0, map);
											}else{
												continue;
											}
											//throw new RuntimeException("请检查"+portItemCode+"的配置中参数取数"+computeVarRules[l]+"是否正确");
										}
										//engine.put(computeVars[l],String.valueOf(listVarData.get(0).get("value")));
										try{
											BigDecimal args=new BigDecimal(String.valueOf(listVarData.get(0).get("value")));
											engine.put(computeVars[l], args);
										}catch(Exception e){
											LOGGER.warn(portItemCode+"配置的未知数"+computeVars[l]+"不是一个数值类型----"+String.valueOf(listVarData.get(0).get("value")));
											engine.put(computeVars[l], String.valueOf(listVarData.get(0).get("value")));
										}
									}catch(Exception e){
										LOGGER.error("请检查"+portItemCode+"的配置中取数"+computeVarRules[l]+"配置的sql是否正确");
										throw new RuntimeException("请检查"+portItemCode+"的配置中取数"+computeVarRules[l]+"配置的sql是否正确");
									}
								}else if(computeVarRules[l].contains("zb_")){
									String outitemCode=computeVarRules[l].substring(3);
									List<Map> listVarData=(List<Map>) dao.getList("select case when outitemtype in ('01','07') then  textvalue else to_char(reportitemvalue) end as value from cfreportdatarisk where reportid=? and month=? and quarter=? and outitemcode=?",id,year,quarter,outitemCode);
									if(listVarData.size()==0){
										LOGGER.warn("请检查"+portItemCode+"的配置中参数取数"+computeVarRules[l]+"是否正确");
										if(flag){
											Map map=new HashMap();
											map.put("value", "0");
											listVarData.add(0, map);
										}else{
											continue;
										}
										//throw new RuntimeException("请检查"+portItemCode+"的配置中参数取数"+computeVarRules[l]+"是否正确");
									}
									//engine.put(computeVars[l],String.valueOf(listVarData.get(0).get("value")));
									try{
										BigDecimal args=new BigDecimal(String.valueOf(listVarData.get(0).get("value")));
										engine.put(computeVars[l], args);
									}catch(Exception e){
										LOGGER.warn(portItemCode+"配置的未知数"+computeVars[l]+"不是一个数值类型----"+String.valueOf(listVarData.get(0).get("value")));
										engine.put(computeVars[l], String.valueOf(listVarData.get(0).get("value")));
									}
								}else if(computeVarRules[l].contains("RF0_")){ //RF0 矩阵的逻辑
									if(computeVarRules[l].contains("|")){
										String[]  rf0computecode=computeVarRules[l].split("\\|");
										List<Map> listRF0computecode=(List<Map>) dao.getList("select case when outitemtype in ('01','07') then  textvalue else to_char(reportitemvalue) end as value from cfreportdatarisk where reportid=? and month=? and quarter=? and outitemcode=?",id,year,quarter,rf0computecode[1]);
										if(listRF0computecode.size()==0){
											LOGGER.error("请检查"+portItemCode+"的配置中参数取数"+computeVarRules[l]+"是否正确");
											//continue;
											throw new RuntimeException("请检查"+portItemCode+"的配置中参数取数"+computeVarRules[l]+"是否正确");
										}
										computeVarRules[l]=computeVarRules[l].replace(rf0computecode[1], String.valueOf(listRF0computecode.get(0).get("value")));
									}
									List<Map> listRF0Define=(List<Map>) dao.getList("select distinct rf0Define as value from cal_RF0Define where rf0Code=?",computeVarRules[l]);
									if(listRF0Define.size()==0){
										LOGGER.error("请检查"+portItemCode+"的配置中参数取数"+computeVarRules[l]+"是否正确");
										//continue;
										throw new RuntimeException("请检查"+portItemCode+"的配置中参数取数"+computeVarRules[l]+"是否正确");
									}
									List<Map> listRF0DefineVar=(List<Map>) dao.getList("select distinct rf0DefineVar as value from cal_RF0Define where rf0Code=?",computeVarRules[l]);
									String RF0Define=String.valueOf( listRF0Define.get(0).get("value"));
									String RF0DefineVar=String.valueOf( listRF0DefineVar.get(0).get("value"));
									List<Map> listRF0DefineValue=null;
									if(RF0Define.contains("select")){ //说明决定rf0的不是指标因子
										try{
											RF0Define=RF0Define.replace("cfreportdata", "cfreportdatarisk");
										}catch(Exception e){
											throw new RuntimeException("cfreportdatarisk替换cfreportdata报错。");
										}
										try{
											RF0Define=RF0Define.replace("cfreportrowadddata", "cfreportrowadddatarisk");
										}catch(Exception e){
											throw new RuntimeException("cfreportrowadddatarisk替换cfreportrowadddata报错。");
										}										if(RF0DefineVar==null||"".equals(RF0DefineVar)){
											try{
												listRF0DefineValue=(List<Map>) dao.getList(RF0Define);
												if(listRF0DefineValue.size()==0){
													LOGGER.warn("请检查"+computeVarRules[l]+"的配置中配置的决定因子是否正确");
													continue;
													//throw new RuntimeException("请检查"+computeVarRules[l]+"的配置中配置的决定因子是否正确");
												}
												RF0Define=String.valueOf( listRF0DefineValue.get(0).get("value"));
											}catch(Exception e){
												LOGGER.error("请检查"+portItemCode+"的配置中参数取数"+computeVarRules[l]+"的配置中配置的决定因子的sql是否正确");
												throw new RuntimeException("请检查"+portItemCode+"的配置中参数取数"+computeVarRules[l]+"的配置中配置的决定因子的sql是否正确");
											}
										}else{
											try{
												RF0DefineVar=RF0DefineVar.replace("year", year);
											}catch(Exception e){
												//说明参数中没有year或者quarter，继续执行
											}
											try{
												RF0DefineVar=RF0DefineVar.replace("quarter", quarter);
											}catch(Exception e){
												//说明参数中没有year或者quarter，继续执行
											}
											try{
												RF0DefineVar=RF0DefineVar.replace("day", day);
											}catch(Exception e){
												//说明参数中没有year或者quarter，继续执行
											}
											try{
												RF0DefineVar=RF0DefineVar.replace("reportId", id);
											}catch(Exception e){
												//说明参数中没有year或者quarter，继续执行
											}
											try{
												listRF0DefineValue=(List<Map>) dao.getList(RF0Define,RF0DefineVar.split(","));
												if(listRF0DefineValue.size()==0){
													LOGGER.warn("请检查"+computeVarRules[l]+"的配置中配置的决定因子是否正确");
													continue;
													//throw new RuntimeException("请检查"+computeVarRules[l]+"的配置中配置的决定因子是否正确");
												}
												RF0Define=String.valueOf( listRF0DefineValue.get(0).get("value"));
											}catch(Exception e){
												LOGGER.error("请检查"+portItemCode+"的配置中参数取数"+computeVarRules[l]+"的配置中配置的决定因子的sql是否正确");
												throw new RuntimeException("请检查"+portItemCode+"的配置中参数取数"+computeVarRules[l]+"的配置中配置的决定因子的sql是否正确");
											}
										}
									}else if(RF0Define.contains("GD")){
										RF0Define=String.valueOf(RF0Define.split("_")[1]);
									}else{ //决定RF0的是指标因子
										//指标取数不一样，从不同的字段取 TODO
										listRF0DefineValue=(List<Map>) dao.getList("select case when outitemtype in ('01','07') then  textvalue else to_char(reportitemvalue) end as value from cfreportdatarisk where reportid=? and month=? and quarter=? and outitemcode=?",id,year,quarter,RF0Define);
										if(listRF0DefineValue.size()==0){
											LOGGER.warn("请检查"+computeVarRules[l]+"的配置中配置的决定因子是否正确");
											continue;
											//throw new RuntimeException("请检查"+computeVarRules[l]+"的配置中配置的决定因子是否正确");
										}
										RF0Define=String.valueOf(listRF0DefineValue.get(0).get("value"));
									}
									//List<Map> listRF0Value=(List<Map>) dao.getList("select distinct rf0value as value from cal_RF0Define where rf0Code=? and (rf0DefineFrom<? and rf0DefineTo>=?) or (rf0DefineTo is null and rf0DefineFrom=?)",computeVarRules[l],RF0Define,RF0Define,RF0Define);
									List<Map> listRF0Value=null;
									String sql1="select distinct rf0value as value from cal_RF0Define where rf0Code=? and remark='1' and ((TO_NUMBER(rf0DefineFrom)<TO_NUMBER(?) and TO_NUMBER(rf0DefineTo)>=TO_NUMBER(?)) or (rf0DefineTo is null and TO_NUMBER(rf0DefineFrom)<TO_NUMBER(?)) or (TO_NUMBER(rf0DefineTo)>=TO_NUMBER(?) and rf0DefineFrom is null))";
									sql1=sql1+" union select distinct rf0value as value from cal_RF0Define where rf0Code=? and remark='0' and ((TO_NUMBER(rf0DefineFrom)<=TO_NUMBER(?) and TO_NUMBER(rf0DefineTo)>TO_NUMBER(?)) or (rf0DefineTo is null and TO_NUMBER(rf0DefineFrom)<=TO_NUMBER(?)) or (TO_NUMBER(rf0DefineTo)>TO_NUMBER(?) and rf0DefineFrom is null))";
									sql1=sql1+" union select distinct rf0value as value from cal_RF0Define where rf0Code = ? and remark <>'0' and remark <>'1' and TO_NUMBER(rf0DefineFrom)=TO_NUMBER(?)";
									String sql2="select distinct rf0value as value from cal_RF0Define where rf0Code=? and rf0DefineTo is null and rf0DefineFrom=?";
									try{
										listRF0Value=(List<Map>) dao.getList(sql1,computeVarRules[l],RF0Define,RF0Define,RF0Define,RF0Define,computeVarRules[l],RF0Define,RF0Define,RF0Define,RF0Define,computeVarRules[l],RF0Define);
										if(listRF0Value.size()==0){
											listRF0Value=(List<Map>) dao.getList(sql2,computeVarRules[l],RF0Define);
										}
									}catch(Exception e){
										listRF0Value=(List<Map>) dao.getList(sql2,computeVarRules[l],RF0Define);
									}
									String RF0Value="";
									try{
										RF0Value=(String) listRF0Value.get(0).get("value");
									}catch(Exception e){
										LOGGER.error(portItemCode+"配置的未知数"+computeVars[l]+"中RF0根据决定因子未取到值！");
										//throw new RuntimeException(portItemCode+"配置的未知数"+computeVars[l]+"中RF0根据决定因子未取到值！");
										continue;
									}
									List<Map> listRF0ValueRule=(List<Map>) dao.getList("select distinct rf0valuerule as value from cal_RF0Define where rf0Code=? ",computeVarRules[l]);
									String RF0ValueRule=(String) listRF0ValueRule.get(0).get("value");
									if("1".equals(RF0ValueRule)){
										//ScriptEngineManager manager1 = new ScriptEngineManager();
										ScriptEngine engine1 = manager.getEngineByName("js");
										//engine1.put("x", RF0Define);
										try{
											int xCount=PubFun.subCount(RF0Value, "x");
											BigDecimal args=new BigDecimal(RF0Define);
											for(int x=0;x<xCount;x++){
												engine1.put("x", args);
											}
										}catch(Exception e){
											LOGGER.warn(portItemCode+"配置的未知数"+computeVars[l]+"中的未知数x不是一个数值类型----"+RF0Define);
											engine1.put("x", RF0Define);
										}
										try{
											RF0Value=String.valueOf(engine1.eval(RF0Value));
										}catch(Exception e){
											LOGGER.error(portItemCode+"指标因子"+computeVarRules[l]+"参数的计算规则错误!");
								        	throw new RuntimeException(portItemCode+"指标因子"+computeVarRules[l]+"参数的计算规则错误!");
										}
									}
									//engine.put(computeVars[l],RF0Value);
									try{
										BigDecimal args=new BigDecimal(RF0Value);
										engine.put(computeVars[l], args);
									}catch(Exception e){
										LOGGER.warn(portItemCode+"配置的未知数"+computeVars[l]+"不是一个数值类型----"+RF0Value);
										engine.put(computeVars[l], RF0Value);
									}
								}else if(computeVarRules[l].contains("K_")){ //K 矩阵的逻辑
									if(computeVarRules[l].contains("|")){
										String[]  kcomputecode=computeVarRules[l].split("\\|");
										List<Map> listKcomputecode=(List<Map>) dao.getList("select case when outitemtype in ('01','07') then  textvalue else to_char(reportitemvalue) end as value from cfreportdatarisk where reportid=? and month=? and quarter=? and outitemcode=?",id,year,quarter,kcomputecode[1]);
										if(listKcomputecode.size()==0){
											LOGGER.error("请检查"+portItemCode+"的配置中参数取数"+computeVarRules[l]+"是否正确");
											//continue;
											throw new RuntimeException("请检查"+portItemCode+"的配置中参数取数"+computeVarRules[l]+"是否正确");
										}
										computeVarRules[l]=computeVarRules[l].replace(kcomputecode[1], String.valueOf(listKcomputecode.get(0).get("value")));
									}
									List<Map> listKDefine=(List<Map>) dao.getList("select distinct KDefine as value from cal_KDefine where kCode=?",computeVarRules[l]);
									if(listKDefine.size()==0){
										LOGGER.error("请检查"+portItemCode+"的配置中参数取数"+computeVarRules[l]+"是否正确");
										//continue;
										throw new RuntimeException("请检查"+portItemCode+"的配置中参数取数"+computeVarRules[l]+"是否正确");
									}
									List<Map> listKDefineVar=(List<Map>) dao.getList("select distinct KDefineVar as value from cal_KDefine where KCode=?",computeVarRules[l]);
									String KDefine=String.valueOf( listKDefine.get(0).get("value"));
    								String KDefineVar=String.valueOf( listKDefineVar.get(0).get("value"));
									List<Map> listKDefineValue=null;
									if(KDefine.contains("select")){ //说明决定rf0的不是指标因子
										try{
											KDefine=KDefine.replace("cfreportdata", "cfreportdatarisk");
										}catch(Exception e){
											throw new RuntimeException("cfreportdatarisk替换cfreportdata报错。");
										}
										try{
											KDefine=KDefine.replace("cfreportrowadddata", "cfreportrowadddatarisk");
										}catch(Exception e){
											throw new RuntimeException("cfreportrowadddatarisk替换cfreportrowadddata报错。");
										}
										if(KDefineVar==null||"".equals(KDefineVar)){
											try{
												listKDefineValue=(List<Map>) dao.getList(KDefine);
												if(listKDefineValue.size()==0){
													LOGGER.warn("请检查"+computeVarRules[l]+"的配置中配置的决定因子是否正确");
													continue;
													//throw new RuntimeException("请检查"+computeVarRules[l]+"的配置中配置的决定因子是否正确");
												}
												KDefine=String.valueOf( listKDefineValue.get(0).get("value"));
											}catch(Exception e){
												LOGGER.error("请检查"+portItemCode+"的配置中参数取数"+computeVarRules[l]+"决定因子sql配置是否正确");
												throw new RuntimeException("请检查"+portItemCode+"的配置中参数取数"+computeVarRules[l]+"决定因子sql配置是否正确");
											}
										}else{
											try{
												KDefineVar=KDefineVar.replace("year", year);
											}catch(Exception e){
												//说明参数中没有year或者quarter，继续执行
											}
											try{
												KDefineVar=KDefineVar.replace("quarter", quarter);
											}catch(Exception e){
												//说明参数中没有year或者quarter，继续执行
											}
											try{
												KDefineVar=KDefineVar.replace("day", day);
											}catch(Exception e){
												//说明参数中没有year或者quarter，继续执行
											}
											try{
												KDefineVar=KDefineVar.replace("reportId", id);
											}catch(Exception e){
												//说明参数中没有year或者quarter，继续执行
											}
											try{
												listKDefineValue=(List<Map>) dao.getList(KDefine,KDefineVar.split(","));
												if(listKDefineValue.size()==0){
													LOGGER.warn("请检查"+computeVarRules[l]+"的配置中配置的决定因子是否正确");
													continue;
													//throw new RuntimeException("请检查"+computeVarRules[l]+"的配置中配置的决定因子是否正确");
												}
												KDefine=String.valueOf( listKDefineValue.get(0).get("value"));
											}catch(Exception e){
												LOGGER.error("请检查"+portItemCode+"的配置中参数取数"+computeVarRules[l]+"决定因子sql配置是否正确");
												throw new RuntimeException("请检查"+portItemCode+"的配置中参数取数"+computeVarRules[l]+"决定因子sql配置是否正确");
											}
										}
									}else if(KDefine.contains("GD")){
										KDefine=String.valueOf(KDefine.split("_")[1]);
									}else{ //决定K的是指标因子
										listKDefineValue=(List<Map>) dao.getList("select case when outitemtype in ('01','07') then  textvalue else to_char(reportitemvalue) end as value from cfreportdatarisk where reportid=? and month=? and quarter=? and outitemcode=?",id,year,quarter,KDefine);
										if(listKDefineValue.size()==0){
											LOGGER.warn("请检查"+computeVarRules[l]+"的配置中配置的决定因子是否正确");
											continue;
											//throw new RuntimeException("请检查"+computeVarRules[l]+"的配置中配置的决定因子是否正确");
										}
										KDefine=String.valueOf(listKDefineValue.get(0).get("value"));
									}
									//List<Map> listKValue=(List<Map>) dao.getList("select distinct kvalue as value from cal_kDefine where kCode=? and (TO_NUMBER(kDefineFrom)<TO_NUMBER(?) and TO_NUMBER(kDefineTo)>=TO_NUMBER(?)) or (kDefineTo is null and kDefineFrom=?)",computeVarRules[l],KDefine,KDefine,KDefine);
									List<Map> listKValue=null;
									String sql1="select distinct kvalue as value from cal_kDefine where kCode=? and remark='1' and ((TO_NUMBER(kDefineFrom)<TO_NUMBER(?) and TO_NUMBER(kDefineTo)>=TO_NUMBER(?)) or (kDefineTo is null and TO_NUMBER(kDefineFrom)<TO_NUMBER(?)) or (TO_NUMBER(kDefineTo)>=TO_NUMBER(?) and kDefineFrom is null))";
									sql1=sql1+" union select distinct kvalue as value from cal_kDefine where kCode=? and remark='0' and ((TO_NUMBER(kDefineFrom)<=TO_NUMBER(?) and TO_NUMBER(kDefineTo)>TO_NUMBER(?)) or (kDefineTo is null and TO_NUMBER(kDefineFrom)<=TO_NUMBER(?)) or (TO_NUMBER(kDefineTo)>TO_NUMBER(?) and kDefineFrom is null))";
									sql1=sql1+" union select distinct kvalue as value from cal_kDefine where kCode = ? and remark <> '0' and remark<> '1' and TO_NUMBER(kDefineFrom) = TO_NUMBER(?)";
									String sql2="select distinct kvalue as value from cal_kDefine where kCode=? and kDefineTo is null and kDefineFrom=?";
									try{
										listKValue=(List<Map>) dao.getList(sql1,computeVarRules[l],KDefine,KDefine,KDefine,KDefine,computeVarRules[l],KDefine,KDefine,KDefine,KDefine,computeVarRules[l],KDefine);
										if(listKValue.size()==0){
											listKValue=(List<Map>) dao.getList(sql2,computeVarRules[l],KDefine);
										}
									}catch(Exception e){
										listKValue=(List<Map>) dao.getList(sql2,computeVarRules[l],KDefine);
									}
									String KValue="";
									try{
										KValue=(String) listKValue.get(0).get("value");
									}catch(Exception e){
										LOGGER.error(portItemCode+"配置的未知数"+computeVars[l]+"中K根据决定因子未取到值！");
										//throw new RuntimeException(portItemCode+"配置的未知数"+computeVars[l]+"中K根据决定因子未取到值！");
									    continue;    
									}
									List<Map> listKValueRule=(List<Map>) dao.getList("select distinct kvaluerule as value from cal_kDefine where kCode=? ",computeVarRules[l]);
									String KValueRule=(String) listKValueRule.get(0).get("value");
									if("1".equals(KValueRule)){
										ScriptEngine engine1 = manager.getEngineByName("js");
										//engine1.put("x", KDefine);
										try{
											int xCount=PubFun.subCount(KValue, "x");
											BigDecimal args=new BigDecimal(KDefine);
											for(int x=0;x<xCount;x++){
												engine1.put("x", args);
											}
										}catch(Exception e){
											LOGGER.warn(portItemCode+"配置的未知数"+computeVars[l]+"中的未知数x不是一个数值类型----"+KDefine);
											engine1.put("x", KDefine);
										}
										try{
											KValue=String.valueOf(engine1.eval(KValue));
										}catch(Exception e){
											LOGGER.error(portItemCode+"指标因子"+computeVarRules[l]+"参数的计算规则错误!");
								        	throw new RuntimeException(portItemCode+"指标因子"+computeVarRules[l]+"参数的计算规则错误!");
										}
									}
									//engine.put(computeVars[l],KValue);
									try{
										BigDecimal args=new BigDecimal(KValue);
										engine.put(computeVars[l], args);
									}catch(Exception e){
										LOGGER.warn(portItemCode+"配置的未知数"+computeVars[l]+"不是一个数值类型----"+KValue);
										engine.put(computeVars[l], KValue);
									}
								}else if(computeVarRules[l].contains("M_")){ //矩阵逻辑
									RhoMatrixDefine rhoMatrixDefine =(RhoMatrixDefine) dao.get(RhoMatrixDefine.class, computeVarRules[l]);
									if(rhoMatrixDefine==null||"".equals(rhoMatrixDefine)){
										throw new RuntimeException("请检查"+portItemCode+"的配置中参数取数"+computeVarRules[l]+"是否正确");
									}
									int columnLine=rhoMatrixDefine.getColumnLine();
									String rhomatrixValue=rhoMatrixDefine.getRhomatrixValue();
									if(columnLine==1||columnLine==-1){ // 1行向量 -1 列向量
										String[] rowOrColMatrix=rhomatrixValue.split(",");
										int roclen=rowOrColMatrix.length;
										double[][] RowValue=new double[1][roclen];
										double[] ColValue=new double[roclen];
										for(int roc=0;roc<roclen;roc++){
											BigDecimal MDefineValue= new BigDecimal(0);
											List<Map> listMDefineValue=null;
											if(rowOrColMatrix[roc].contains("+")){
												String[]  rowOrColMatrixs=rowOrColMatrix[roc].split("\\+");
												ScriptEngine enginexroc = manager.getEngineByName("js");
												for(int rocm=0;rocm<rowOrColMatrixs.length;rocm++){
													if(rowOrColMatrixs[rocm].contains("-")){
														listMDefineValue=(List<Map>) dao.getList("select case when outitemtype in ('01','07') then  textvalue else to_char(reportitemvalue) end as value from cfreportdatarisk where reportid=? and month=? and quarter=? and outitemcode=?",id,year,quarter,rowOrColMatrixs[rocm].substring(1));
														if(listMDefineValue.size()==0){
															LOGGER.warn("请检查"+computeVarRules[l]+"的配置中配置的"+rowOrColMatrix[roc]+"中"+rowOrColMatrixs[rocm]+"指标代码是否正确");
															Map map=new HashMap();
															map.put("value", "0");
															listMDefineValue.add(0, map);
															//continue;
															//throw new RuntimeException("请检查"+computeVarRules[l]+"的配置中配置的"+rowOrColMatrix[roc]+"中"+rowOrColMatrixs[rocm]+"指标代码是否正确");
														}
														try{
															enginexroc.put(rowOrColMatrixs[rocm].substring(1), new BigDecimal(String.valueOf(listMDefineValue.get(0).get("value"))));
														}catch(Exception e){
															throw new RuntimeException("请检查"+computeVarRules[l]+"的配置中配置的"+rowOrColMatrix[roc]+"中"+rowOrColMatrixs[rocm]+"指标代码所取到的值是否是数值类型");
														}
													}else{
														listMDefineValue=(List<Map>) dao.getList("select case when outitemtype in ('01','07') then  textvalue else to_char(reportitemvalue) end as value from cfreportdatarisk where reportid=? and month=? and quarter=? and outitemcode=?",id,year,quarter,rowOrColMatrixs[rocm]);
														if(listMDefineValue.size()==0){
															LOGGER.warn("请检查"+computeVarRules[l]+"的配置中配置的"+rowOrColMatrix[roc]+"中"+rowOrColMatrixs[rocm]+"指标代码是否正确");
															Map map=new HashMap();
															map.put("value", "0");
															listMDefineValue.add(0, map);
															//continue;
															//throw new RuntimeException("请检查"+computeVarRules[l]+"的配置中配置的"+rowOrColMatrix[roc]+"中"+rowOrColMatrixs[rocm]+"指标代码是否正确");
														}
														try{
															enginexroc.put(rowOrColMatrixs[rocm], new BigDecimal(String.valueOf(listMDefineValue.get(0).get("value"))));
														}catch(Exception e){
															throw new RuntimeException("请检查"+computeVarRules[l]+"的配置中配置的"+rowOrColMatrix[roc]+"中"+rowOrColMatrixs[rocm]+"指标代码所取到的值是否是数值类型");
														}
													}
												}
												Object result=enginexroc.eval(rowOrColMatrix[roc]);
												MDefineValue=new BigDecimal(String.valueOf(result));
											}else{
												listMDefineValue=(List<Map>) dao.getList("select case when outitemtype in ('01','07') then  textvalue else to_char(reportitemvalue) end as value from cfreportdatarisk where reportid=? and month=? and quarter=? and outitemcode=?",id,year,quarter,rowOrColMatrix[roc]);
												if(listMDefineValue.size()==0){
													LOGGER.warn("请检查"+computeVarRules[l]+"的配置中配置的"+rowOrColMatrix[roc]+"指标代码是否正确");
													Map map=new HashMap();
													map.put("value", "0");
													listMDefineValue.add(0, map);
													//continue;
													//throw new RuntimeException("请检查"+computeVarRules[l]+"的配置中配置的"+rowOrColMatrix[roc]+"指标代码是否正确");
												}
												MDefineValue= new BigDecimal(String.valueOf(listMDefineValue.get(0).get("value")));
											}
											//rhomatrixValue.replace(rowOrColMatrix[i], MDefineValue);
											if(columnLine==1){
												RowValue[0][roc]=MDefineValue.doubleValue();
											}else{
												ColValue[roc]=MDefineValue.doubleValue();
											}
										}
										if(columnLine==1){
											Matrix RowM=new Matrix(RowValue);
											//engine.put(computeVars[l],RowM);
											engineMatrix.put(computeVars[l],RowM);
										}else{
											Matrix ColM=new Matrix(ColValue,roclen);
											//engine.put(computeVars[l],ColM);
											engineMatrix.put(computeVars[l],ColM);
										}
									}else{
										String[] CrossMatrix=rhomatrixValue.split(",");
										double[][] CrossValue=new double[columnLine][ columnLine];
										try{
										for(int Row=0;Row<columnLine;Row++){
												for(int Col=0;Col<columnLine;Col++){
													CrossValue[Row][Col]=new BigDecimal(CrossMatrix[Row*columnLine+Col]).doubleValue();
												}
											}
											Matrix CrossM=new Matrix(CrossValue);
											//engine.put(computeVars[l],CrossM);
											engineMatrix.put(computeVars[l],CrossM);
										}catch(Exception e){
											throw new RuntimeException("请检查"+computeVarRules[l]+"的配置中配置的的值是否有无效数字");
										}
									}
								}
							}
							if(calculateRule.contains("A.times(B).times(C)")){
								Object resultMatrix=engineMatrix.eval("A.times(B).times(C)");
								if("Jama.Matrix".equals(resultMatrix.getClass().getName())){
									BigDecimal Matrixresult=new BigDecimal(((Matrix)resultMatrix).get(0, 0));
									calculateRule=calculateRule.replace("A.times(B).times(C)", "Matrix");
									engine.put("Matrix", Matrixresult);
								}
							}
						}
						System.out.println(calculateRule+"------------------");
						Object result=null;
						try{
							result=engine.eval(calculateRule);
						}catch(ScriptException e){
							LOGGER.error(portItemCode+"指标因子参数的计算规则错误!");
				        	//throw new RuntimeException(portItemCode+"指标因子参数的计算规则错误!");
							continue;
						}
						value=String.valueOf(result);
						if("Jama.Matrix".equals(result.getClass().getName())){
							value=String.valueOf(((Matrix)result).get(0,0));
						}
					}
					System.out.println(portItemCode+"的值为："+value);
					CfReportDataRiskId lacfReportDataId=new CfReportDataRiskId();				
				    CfReportDataRisk lacfReportData=new CfReportDataRisk();
				    lacfReportDataId.setOutItemCode(portItemCode);//指标代码
				    lacfReportDataId.setReportId(id);
				    lacfReportData.setId(lacfReportDataId);
				    lacfReportData.setMonth(year);//年度
				    lacfReportData.setQuarter(quarter);//季度
				    lacfReportData.setReportRate(reportType);//报送类型
				    lacfReportData.setComCode(organCode);//机构代码
				    lacfReportData.setOutItemCodeName("");//因子名称
				    lacfReportData.setReportItemCode(portItemCode);//因子代码
			        CfReportItemCodeDesc cricd=new CfReportItemCodeDesc();
			        OCfReportItemCodeDesc ocricd=new OCfReportItemCodeDesc();
			        cricd=(CfReportItemCodeDesc) dao.get(CfReportItemCodeDesc.class, portItemCode);
			        String strOutItemType="";
			        if(cricd==null||"".equals(cricd)){
			        	ocricd=(OCfReportItemCodeDesc) dao.get(OCfReportItemCodeDesc.class, portItemCode);
			        	if(ocricd==null||"".equals(ocricd)){
			        		LOGGER.error(portItemCode+"指标因子未找到!");
			        		//throw new Exception(portItemCode+"指标因子未找到!");
			        		continue;
			        	}else{
			        		strOutItemType=ocricd.getOutItemType();
			        		DBDecimals=ocricd.getDecimals();
			        		System.out.println("================"+DBDecimals);
			        	}
			        }else{
			        	strOutItemType=cricd.getOutItemType();
			        }
			        
			        if(strOutItemType.equals("06")){//百分比型
				    	   try {
				    		   if("".equals(value)||value==null||"null".equals(value)){		    		
				    			   value="0";
					    		   System.out.println(portItemCode+"无值可取,默认取0！！！");
					    		   //continue;
				    		   }
				    		   if(DBDecimals!=null&&!"".equals(DBDecimals)){
				    			   value =exportXbrlService.FormatData(value,DBDecimals); 
								}else{
									value =exportXbrlService.FormatData(value,"4"); 
								}
				    		   lacfReportData.setReportItemValue(new BigDecimal(value).setScale(8,BigDecimal.ROUND_HALF_UP));
				    		   lacfReportData.setReportItemWanValue(new BigDecimal(0).setScale(12,BigDecimal.ROUND_HALF_UP));
					       } catch (NumberFormatException e) {
					    	   LOGGER.error(portItemCode+"指标因子取值不是百分比类型!");
					    	   e.printStackTrace();
					    	   continue;
					       }
				    	   lacfReportData.setOutItemType("06");
				    	   lacfReportData.setTextValue("");
				    	   lacfReportData.setDesText(null);//描述性型
				    	   lacfReportData.setFileText(null);//文件型
				       }else if(strOutItemType.equals("05")){//数量型
				    	   try {
				    		   if("".equals(value)||value==null||"null".equals(value)){		
				    			   value="0";
				    			   System.out.println(portItemCode+"无值可取,默认取0！！！");
				    			   //continue;
				    		   }
				    		   if(DBDecimals!=null&&!"".equals(DBDecimals)){
				    			   value =exportXbrlService.FormatData(value,DBDecimals); 
								}else{
									value =exportXbrlService.FormatData(value,"0"); 
								}
				    		   //}else{
				    		   lacfReportData.setReportItemValue(new BigDecimal(value).setScale(8,BigDecimal.ROUND_HALF_UP));
				    		   lacfReportData.setReportItemWanValue(new BigDecimal(value).divide(new BigDecimal("10000"), 12, RoundingMode.HALF_UP));
				    		   //}
					       } catch (NumberFormatException e) {
					    	   LOGGER.error(portItemCode+"指标因子取值不是数量类型!");
					    	   e.printStackTrace();
					    	   continue;
					       }
				    	   lacfReportData.setOutItemType("05");
				    	   lacfReportData.setTextValue("");
				    	   lacfReportData.setDesText(null);//描述性型
				    	   lacfReportData.setFileText(null);//文件型
				       }else if(strOutItemType.equals("04")){//数值型
				    	   try {
				    		   if("".equals(value)||value==null||"null".equals(value)){		    
				    			   value="0";
				    			   System.out.println(portItemCode+"无值可取,默认取0！！！");
				    			   //continue;
				    		   }
				    		   if(DBDecimals!=null&&!"".equals(DBDecimals)){
				    			   value =exportXbrlService.FormatData(value,DBDecimals); 
								}else{
									value =exportXbrlService.FormatData(value,"2"); 
								}
				    		   lacfReportData.setReportItemValue(new BigDecimal(value).setScale(8,BigDecimal.ROUND_HALF_UP));
				    		   lacfReportData.setReportItemWanValue(new BigDecimal(value).divide(new BigDecimal("10000"), 12, RoundingMode.HALF_UP));
					       } catch (NumberFormatException e) {
					    	   LOGGER.error(portItemCode+"指标因子取值不是数值类型!");
					    	   e.printStackTrace();
					    	   continue;
					       }
				    	   lacfReportData.setOutItemType("04");
				    	   lacfReportData.setTextValue("");
				    	   lacfReportData.setDesText(null);//描述性型
				    	   lacfReportData.setFileText(null);//文件型
				       }else if(strOutItemType.equals("03")){//文件型
				    	   try {
				    		   lacfReportData.setOutItemType("03");
				    		   lacfReportData.setReportItemValue(new BigDecimal(0).setScale(4,BigDecimal.ROUND_HALF_UP));
				    		   lacfReportData.setReportItemWanValue(new BigDecimal(0).setScale(8,BigDecimal.ROUND_HALF_UP));
					       } catch (NumberFormatException e) {
					    	   LOGGER.error(portItemCode+"指标因子取值不是文件类型!");
					    	   e.printStackTrace();
					    	   continue;
					       }
				    	   lacfReportData.setTextValue("");
				    	   lacfReportData.setDesText(null);//描述性型
				    	   lacfReportData.setFileText(null);//文件型
				       }else if(strOutItemType.equals("02")){ //描述
				    	   try {
				    		   lacfReportData.setOutItemType("02");
				    		   lacfReportData.setReportItemValue(new BigDecimal(0).setScale(4,BigDecimal.ROUND_HALF_UP));
				    		   lacfReportData.setReportItemWanValue(new BigDecimal(0).setScale(8,BigDecimal.ROUND_HALF_UP));
					       } catch (NumberFormatException e) {
					    	   LOGGER.error(portItemCode+"指标因子取值不是描述类型!");
					    	   e.printStackTrace();
					    	   continue;
					       }
				    	   lacfReportData.setTextValue(null);
				    	   lacfReportData.setDesText(null);//描述性型
				    	   lacfReportData.setFileText(null);//文件型
				       }else if(strOutItemType.equals("01")){//短文本类型
				    	   lacfReportData.setOutItemType("01");
				    	   lacfReportData.setReportItemValue(new BigDecimal(0).setScale(4,BigDecimal.ROUND_HALF_UP));
				    	   lacfReportData.setReportItemWanValue(new BigDecimal(0).setScale(8,BigDecimal.ROUND_HALF_UP));
				    	   try {
				    		   lacfReportData.setTextValue(value);
					       } catch (NumberFormatException e) {
					    	   LOGGER.error(portItemCode+"指标因子取值不是短文本类型!");
					    	   e.printStackTrace();
					    	   continue;
					       }
				    	   lacfReportData.setDesText(null);//描述性型
				    	   lacfReportData.setFileText(null);//文件型
				       }
				       else if(strOutItemType.equals("07")){//日期型
				    	   lacfReportData.setOutItemType("07");
				    	   lacfReportData.setReportItemValue(new BigDecimal(0).setScale(8,BigDecimal.ROUND_HALF_UP));
				    	   lacfReportData.setReportItemWanValue(new BigDecimal(0).setScale(12,BigDecimal.ROUND_HALF_UP));
				    	   try {
				    		   lacfReportData.setTextValue(sdf.format(value));
					       } catch (NumberFormatException e) {
					    	   lacfReportData.setTextValue(value);
					       }
				    	   lacfReportData.setDesText(null);//描述性型
				    	   lacfReportData.setFileText(null);//文件型
				       }else{
				    	   System.out.println("导入因子值类型有误！！！");
				       }
			           lacfReportData.setSource("9");//源1：指标导入；源2：自动提数业务；源3：自动提数财务；源4：精算模板导入；源5：投资模板导入；源6：计算
			           lacfReportData.setReportType("1");//报表类别
			           lacfReportData.setComputeLevel("0");//计算级别
			           lacfReportData.setReportItemValueSource("1");//指标值来源：1-初步计算数据手工修改；2-尾数自动调整
			           lacfReportData.setOperator(user.getUserCode());//默认（操作人）
			           lacfReportData.setOperDate(sdf.format(new Date()));//操作日期
			           lacfReportData.setTemp("");
				       dao.create(lacfReportData);
				       dao.flush();
				}else if("1".equals(portItemType)){//行可扩展数据
					for(int j=0;j<listData.size();j++){
						Map resultMap=listData.get(j);
						String rowno=String.valueOf(j+1);
						//CR09的特殊处理
						if(resultMap.get("rowno")!=null&&!"".equals(resultMap.get("rowno"))&&!"null".equals(resultMap.get("rowno"))){
							rowno=String.valueOf(resultMap.get("rowno"));
						}
						String value=String.valueOf(resultMap.get("value"));
						if("1".equals(calculateType)){
							ScriptEngineManager manager = new ScriptEngineManager();
							ScriptEngine engine = manager.getEngineByName("js");
							if("0".equals(varFlag)){
								//engine.put("x", value);
								try{
									BigDecimal args=new BigDecimal(value);
									engine.put("x", args);
								}catch(Exception e){
									LOGGER.warn(portItemCode+"配置的未知数x不是一个数值类型----"+value);
									engine.put("x", value);
								}
							}else if("1".equals(varFlag)){
								int len=computeVar.split(",").length;
								String[] computeVars=computeVar.split(",");
								String[] computeVarRules=computeVarRule.split(";");
								for(int l=0;l<len;l++){
									if(computeVarRules[l].contains("select")){
										try{
											List<Map> listVarData=(List<Map>) dao.getList(computeVarRules[l],year,quarter);
											if(listVarData.size()==0){
												LOGGER.warn("请检查"+portItemCode+"的配置中参数取数"+computeVarRules[l]+"是否正确");
												if(flag){
													Map map=new HashMap();
													map.put("value", "0");
													listVarData.add(0, map);
												}else{
													continue;
												}
											}
											//engine.put(computeVars[l],String.valueOf(listVarData.get(0).get("value")));
											try{
												BigDecimal args=new BigDecimal(String.valueOf(listVarData.get(0).get("value")));
												engine.put(computeVars[l], args);
											}catch(Exception e){
												LOGGER.warn(portItemCode+"配置的未知数"+computeVars[l]+"不是一个数值类型----"+String.valueOf(listVarData.get(0).get("value")));
												engine.put(computeVars[l], String.valueOf(listVarData.get(0).get("value")));
											}
										}catch(Exception e){
											LOGGER.error("请检查"+portItemCode+"的配置中参数取数"+computeVarRules[l]+"配置的sql是否正确");
											throw new RuntimeException("请检查"+portItemCode+"的配置中参数取数"+computeVarRules[l]+"配置的sql是否正确");
										}
									}else if(computeVarRules[l].contains("zb_")){
										String outitemCode=computeVarRules[l].substring(3);
										List<Map> listVarData=(List<Map>) dao.getList("select case when coltype in ('01','07') then  textvalue else to_char(numvalue) end as value from cfreportrowadddatarisk where reportid=? and colcode=? and rowno=? and yearmonth=? and quarter=? ",id,outitemCode,rowno,year,quarter);
										if(listVarData.size()==0){
											LOGGER.warn("请检查"+portItemCode+"--"+rowno+"的配置中参数取数"+computeVarRules[l]+"是否正确");
											if(flag){
												Map map=new HashMap();
												map.put("value", "0");
												listVarData.add(0, map);
											}else{
												continue;
											}
											//throw new RuntimeException("请检查"+portItemCode+"--"+rowno+"的配置中参数取数"+computeVarRules[l]+"是否正确");
										}
										//engine.put(computeVars[l],String.valueOf(listVarData.get(0).get("value")));
										try{
											BigDecimal args=new BigDecimal(String.valueOf(listVarData.get(0).get("value")));
											engine.put(computeVars[l], args);
										}catch(Exception e){
											LOGGER.warn(portItemCode+"配置的未知数"+computeVars[l]+"不是一个数值类型----"+String.valueOf(listVarData.get(0).get("value")));
											engine.put(computeVars[l], String.valueOf(listVarData.get(0).get("value")));
										}
									}else if(computeVarRules[l].contains("RF0_")){ //RF0 矩阵的逻辑
										if(computeVarRules[l].contains("|")){ //RF0决定因子是两个的情况
											String[]  rf0computecode=computeVarRules[l].split("\\|");
											List<Map> listRF0computecode=(List<Map>) dao.getList("select case when coltype in ('01','07') then  textvalue else to_char(numvalue) end as value from cfreportrowadddatarisk where reportid=? and colcode=? and rowno=? and yearmonth=? and quarter=? ",id,rf0computecode[1],rowno,year,quarter);
											if(listRF0computecode.size()==0){
												//throw new RuntimeException("请检查"+portItemCode+"的配置中参数取数"+computeVarRules[l]+"是否正确");
												continue;
											}
											computeVarRules[l]=computeVarRules[l].replace(rf0computecode[1], String.valueOf(listRF0computecode.get(0).get("value")));
										}
										List<Map> listRF0Define=(List<Map>) dao.getList("select distinct rf0Define as value from cal_RF0Define where rf0Code=?",computeVarRules[l]);
										if(listRF0Define.size()==0){
											LOGGER.error("请检查"+portItemCode+"--"+rowno+"的配置中参数取数"+computeVarRules[l]+"是否正确");
											throw new RuntimeException("请检查"+portItemCode+"--"+rowno+"的配置中参数取数"+computeVarRules[l]+"是否正确");
										}
										List<Map> listRF0DefineVar=(List<Map>) dao.getList("select distinct rf0DefineVar as value from cal_RF0Define where rf0Code=?",computeVarRules[l]);
										String RF0Define=String.valueOf( listRF0Define.get(0).get("value"));
										String RF0DefineVar=String.valueOf( listRF0DefineVar.get(0).get("value"));
										List<Map> listRF0DefineValue=null;
										if(RF0Define.contains("select")){ //说明决定rf0的不是指标因子
											try{
												RF0Define=RF0Define.replace("cfreportdata", "cfreportdatarisk");
											}catch(Exception e){
												throw new RuntimeException("cfreportdatarisk替换cfreportdata报错。");
											}
											try{
												RF0Define=RF0Define.replace("cfreportrowadddata", "cfreportrowadddatarisk");
											}catch(Exception e){
												throw new RuntimeException("cfreportrowadddatarisk替换cfreportrowadddata报错。");
											}		
											if(RF0DefineVar==null||"".equals(RF0DefineVar)){
												try{
													listRF0DefineValue=(List<Map>) dao.getList(RF0Define);
													if(listRF0DefineValue.size()==0){
														LOGGER.warn("请检查"+computeVarRules[l]+"的配置中配置的决定因子是否正确");
														continue;
														//throw new RuntimeException("请检查"+computeVarRules[l]+"的配置中配置的决定因子是否正确");
													}
													RF0Define=String.valueOf( listRF0DefineValue.get(0).get("value"));
												}catch(Exception e){
													LOGGER.error("请检查"+portItemCode+"的配置中参数取数"+computeVarRules[l]+"的配置中配置的决定因子的sql是否正确");
													throw new RuntimeException("请检查"+portItemCode+"的配置中参数取数"+computeVarRules[l]+"的配置中配置的决定因子的sql是否正确");
												}
											}else{
												try{
													RF0DefineVar=RF0DefineVar.replace("year", year);
												}catch(Exception e){
													//说明参数中没有year或者quarter，继续执行
												}
												try{
													RF0DefineVar=RF0DefineVar.replace("quarter", quarter);
												}catch(Exception e){
													//说明参数中没有year或者quarter，继续执行
												}
												try{
													RF0DefineVar=RF0DefineVar.replace("rowno", rowno);
												}catch(Exception e){
													//说明参数中没有year或者quarter，继续执行
												}
												try{
													RF0DefineVar=RF0DefineVar.replace("day", day);
												}catch(Exception e){
													//说明参数中没有year或者quarter，继续执行
												}
												try{
													RF0DefineVar=RF0DefineVar.replace("reportId", id);
												}catch(Exception e){
													//说明参数中没有year或者quarter，继续执行
												}
												listRF0DefineValue=(List<Map>) dao.getList(RF0Define,RF0DefineVar.split(","));
												if(listRF0DefineValue.size()==0){
													LOGGER.warn("请检查"+computeVarRules[l]+"的配置中配置的决定因子是否正确");
													continue;
													//throw new RuntimeException("请检查"+computeVarRules[l]+"的配置中配置的决定因子是否正确");
												}
												RF0Define=String.valueOf( listRF0DefineValue.get(0).get("value"));
											}
										}else if(RF0Define.contains("GD")){
											RF0Define=String.valueOf(RF0Define.split("_")[1]);
										}else{ //决定RF0的是指标因子
											//指标取数不一样，从不同的字段取 TODO
											listRF0DefineValue=(List<Map>) dao.getList("select case when coltype in ('01','07') then  textvalue else to_char(numvalue) end as value from cfreportrowadddatarisk where reportid=? and colcode=? and rowno=? and yearmonth=? and quarter=? ",id,RF0Define,rowno,year,quarter);
											if(listRF0DefineValue.size()==0){
												LOGGER.warn("请检查"+computeVarRules[l]+"的配置中配置的决定因子是否正确");
												continue;
												//throw new RuntimeException("请检查"+computeVarRules[l]+"的配置中配置的决定因子是否正确");
											}
											RF0Define=String.valueOf(listRF0DefineValue.get(0).get("value"));
										}
										//List<Map> listRF0Value=(List<Map>) dao.getList("select distinct rf0value as value from cal_RF0Define where rf0Code=? and (rf0DefineFrom<? and rf0DefineTo>=?) or (rf0DefineTo is null and rf0DefineFrom=?)",computeVarRules[l],RF0Define,RF0Define,RF0Define);
										List<Map> listRF0Value=null;
										String sql1="select distinct rf0value as value from cal_RF0Define where rf0Code=? and remark='1' and ((TO_NUMBER(rf0DefineFrom)<TO_NUMBER(?) and TO_NUMBER(rf0DefineTo)>=TO_NUMBER(?)) or (rf0DefineTo is null and TO_NUMBER(rf0DefineFrom)<TO_NUMBER(?)) or (TO_NUMBER(rf0DefineTo)>=TO_NUMBER(?) and rf0DefineFrom is null))";
										sql1=sql1+" union select distinct rf0value as value from cal_RF0Define where rf0Code=? and remark='0' and ((TO_NUMBER(rf0DefineFrom)<=TO_NUMBER(?) and TO_NUMBER(rf0DefineTo)>TO_NUMBER(?)) or (rf0DefineTo is null and TO_NUMBER(rf0DefineFrom)<=TO_NUMBER(?)) or (TO_NUMBER(rf0DefineTo)>TO_NUMBER(?) and rf0DefineFrom is null))";
										sql1=sql1+" union select distinct rf0value as value from cal_RF0Define where rf0Code = ? and remark <>'0' and remark <>'1' and TO_NUMBER(rf0DefineFrom)=TO_NUMBER(?)";
										String sql2="select distinct rf0value as value from cal_RF0Define where rf0Code=? and rf0DefineTo is null and rf0DefineFrom=?";
										try{
											listRF0Value=(List<Map>) dao.getList(sql1,computeVarRules[l],RF0Define,RF0Define,RF0Define,RF0Define,computeVarRules[l],RF0Define,RF0Define,RF0Define,RF0Define,computeVarRules[l],RF0Define);
											if(listRF0Value.size()==0){
												listRF0Value=(List<Map>) dao.getList(sql2,computeVarRules[l],RF0Define);
											}
										}catch(Exception e){
											listRF0Value=(List<Map>) dao.getList(sql2,computeVarRules[l],RF0Define);
										}
										String RF0Value="";
										try{
											RF0Value=(String) listRF0Value.get(0).get("value");
										}catch(Exception e){
											LOGGER.error(portItemCode+"配置的未知数"+computeVars[l]+"中RF0根据决定因子未取到值！");
											throw new RuntimeException(portItemCode+"配置的未知数"+computeVars[l]+"中RF0根据决定因子未取到值！");
										}
										List<Map> listRF0ValueRule=(List<Map>) dao.getList("select distinct rf0valuerule as value from cal_RF0Define where rf0Code=? ",computeVarRules[l]);
										String RF0ValueRule=(String) listRF0ValueRule.get(0).get("value");
										if("1".equals(RF0ValueRule)){
											//ScriptEngineManager manager1 = new ScriptEngineManager();
											//engine1.put("x", RF0Define);
											ScriptEngine engine1 = manager.getEngineByName("js");
											try{
												int xCount=PubFun.subCount(RF0Value, "x");
												BigDecimal args=new BigDecimal(RF0Define);
												for(int x=0;x<xCount;x++){
													engine1.put("x", args);
												}
											}catch(Exception e){
												LOGGER.warn(portItemCode+"配置的未知数"+computeVars[l]+"中的未知数x不是一个数值类型----"+RF0Define);
												engine1.put("x", RF0Define);
											}
											try{
												RF0Value=String.valueOf(engine1.eval(RF0Value));
											}catch(Exception e){
												LOGGER.error(portItemCode+"指标因子"+computeVarRules[l]+"参数的计算规则错误!");
									        	throw new RuntimeException(portItemCode+"指标因子"+computeVarRules[l]+"参数的计算规则错误!");
											}
										}
										//engine.put(computeVars[l],RF0Value);
										try{
											BigDecimal args=new BigDecimal(RF0Value);
											engine.put(computeVars[l], args);
										}catch(Exception e){
											LOGGER.warn(portItemCode+"配置的未知数"+computeVars[l]+"不是一个数值类型----"+RF0Value);
											engine.put(computeVars[l], RF0Value);
										}
									}else if(computeVarRules[l].contains("K_")){ //K 矩阵的逻辑
										if(computeVarRules[l].contains("|")){ //K决定因子是两个的情况
											String[]  kcomputecode=computeVarRules[l].split("\\|");
											List<Map> listKcomputecode=(List<Map>) dao.getList("select case when coltype in ('01','07') then  textvalue else to_char(numvalue) end as value from cfreportrowadddatarisk where reportid=? and colcode=? and rowno=? and yearmonth=? and quarter=? ",id,kcomputecode[1],rowno,year,quarter);
											if(listKcomputecode.size()==0){
												throw new RuntimeException("请检查"+portItemCode+"的配置中参数取数"+computeVarRules[l]+"是否正确");
											}
											computeVarRules[l]=computeVarRules[l].replace(kcomputecode[1], String.valueOf(listKcomputecode.get(0).get("value")));
										}
										List<Map> listKDefine=(List<Map>) dao.getList("select distinct KDefine as value from cal_KDefine where kCode=?",computeVarRules[l]);
										if(listKDefine.size()==0){
											throw new RuntimeException("请检查"+portItemCode+"的配置中参数取数"+computeVarRules[l]+"是否正确");
										}
										List<Map> listKDefineVar=(List<Map>) dao.getList("select distinct KDefineVar as value from cal_KDefine where KCode=?",computeVarRules[l]);
										String KDefine=String.valueOf( listKDefine.get(0).get("value"));
										String KDefineVar=String.valueOf( listKDefineVar.get(0).get("value"));
										List<Map> listKDefineValue=null;
										if(KDefine.contains("select")){ //说明决定k的不是指标因子
											try{
												KDefine=KDefine.replace("cfreportdata", "cfreportdatarisk");
											}catch(Exception e){
												throw new RuntimeException("cfreportdatarisk替换cfreportdata报错。");
											}
											try{
												KDefine=KDefine.replace("cfreportrowadddata", "cfreportrowadddatarisk");
											}catch(Exception e){
												throw new RuntimeException("cfreportrowadddatarisk替换cfreportrowadddata报错。");
											}
											if(KDefineVar==null||"".equals(KDefineVar)){
												try{
													listKDefineValue=(List<Map>) dao.getList(KDefine);
													if(listKDefineValue.size()==0){
														LOGGER.warn("请检查"+computeVarRules[l]+"的配置中配置的决定因子是否正确");
														continue;
														//throw new RuntimeException("请检查"+computeVarRules[l]+"的配置中配置的决定因子是否正确");
													}
													KDefine=String.valueOf( listKDefineValue.get(0).get("value"));
												}catch(Exception e){
													LOGGER.error("请检查"+portItemCode+"的配置中参数取数"+computeVarRules[l]+"决定因子配置的sql是否正确");
													throw new RuntimeException("请检查"+portItemCode+"的配置中参数取数"+computeVarRules[l]+"决定因子配置的sql是否正确");
												}
											}else{
												try{
													KDefineVar=KDefineVar.replace("year", year);
												}catch(Exception e){
													//说明参数中没有year或者quarter，继续执行
												}
												try{
													KDefineVar=KDefineVar.replace("quarter", quarter);
												}catch(Exception e){
													//说明参数中没有year或者quarter，继续执行
												}
												try{
													KDefineVar=KDefineVar.replace("rowno", rowno);
												}catch(Exception e){
													//说明参数中没有year或者quarter，继续执行
												}
												try{
													KDefineVar=KDefineVar.replace("day", day);
												}catch(Exception e){
													//说明参数中没有year或者quarter，继续执行
												}
												try{
													KDefineVar=KDefineVar.replace("reportId", id);
												}catch(Exception e){
													//说明参数中没有year或者quarter，继续执行
												}
												try{
													listKDefineValue=(List<Map>) dao.getList(KDefine,KDefineVar.split(","));
													if(listKDefineValue.size()==0){
														LOGGER.warn("请检查"+computeVarRules[l]+"的配置中配置的决定因子是否正确");
														continue;
														//throw new RuntimeException("请检查"+computeVarRules[l]+"的配置中配置的决定因子是否正确");
													}
													KDefine=String.valueOf( listKDefineValue.get(0).get("value"));
												}catch(Exception e){
													LOGGER.error("请检查"+portItemCode+"的配置中参数取数"+computeVarRules[l]+"决定因子配置的sql是否正确");
													throw new RuntimeException("请检查"+portItemCode+"的配置中参数取数"+computeVarRules[l]+"决定因子配置的sql是否正确");
												}
											}
										}else if(KDefine.contains("GD")){
											KDefine=String.valueOf(KDefine.split("_")[1]);
										}else{ //决定K的是指标因子
											listKDefineValue=(List<Map>) dao.getList("select case when coltype in ('01','07') then  textvalue else to_char(numvalue) end as value from cfreportrowadddatarisk where reportid=? and colcode=? and rowno=? and yearmonth=? and quarter=? ",id,KDefine,rowno,year,quarter);											
											if(listKDefineValue.size()==0){
												LOGGER.warn("请检查"+computeVarRules[l]+"的配置中配置的决定因子是否正确");
												continue;
												//throw new RuntimeException("请检查"+computeVarRules[l]+"的配置中配置的决定因子是否正确");
											}
											KDefine=String.valueOf(listKDefineValue.get(0).get("value"));
										}
										List<Map> listKValue=null;
										String sql1="select distinct kvalue as value from cal_kDefine where kCode=? and remark='1' and ((TO_NUMBER(kDefineFrom)<TO_NUMBER(?) and TO_NUMBER(kDefineTo)>=TO_NUMBER(?)) or (kDefineTo is null and TO_NUMBER(kDefineFrom)<TO_NUMBER(?)) or (TO_NUMBER(kDefineTo)>=TO_NUMBER(?) and kDefineFrom is null))";
										sql1=sql1+" union select distinct kvalue as value from cal_kDefine where kCode=? and remark='0' and ((TO_NUMBER(kDefineFrom)<=TO_NUMBER(?) and TO_NUMBER(kDefineTo)>TO_NUMBER(?)) or (kDefineTo is null and TO_NUMBER(kDefineFrom)<=TO_NUMBER(?)) or (TO_NUMBER(kDefineTo)>TO_NUMBER(?) and kDefineFrom is null))";
										sql1=sql1+" union select distinct kvalue as value from cal_kDefine where kCode = ? and remark <> '0' and remark<> '1' and TO_NUMBER(kDefineFrom) = TO_NUMBER(?)";
										String sql2="select distinct kvalue as value from cal_kDefine where kCode=? and kDefineTo is null and kDefineFrom=?";
										try{
											listKValue=(List<Map>) dao.getList(sql1,computeVarRules[l],KDefine,KDefine,KDefine,KDefine,computeVarRules[l],KDefine,KDefine,KDefine,KDefine,computeVarRules[l],KDefine);
											if(listKValue.size()==0){
												listKValue=(List<Map>) dao.getList(sql2,computeVarRules[l],KDefine);
											}
										}catch(Exception e){
											listKValue=(List<Map>) dao.getList(sql2,computeVarRules[l],KDefine);
										}
										String KValue="";
										try{
											KValue=(String) listKValue.get(0).get("value");
										}catch(Exception e){
											LOGGER.error(portItemCode+"配置的未知数"+computeVars[l]+"中K根据决定因子未取到值！");
											throw new RuntimeException(portItemCode+"配置的未知数"+computeVars[l]+"中K根据决定因子未取到值！");
										}
										List<Map> listKValueRule=(List<Map>) dao.getList("select distinct kvaluerule as value from cal_kDefine where kCode=? ",computeVarRules[l]);
										String KValueRule=(String) listKValueRule.get(0).get("value");
										if("1".equals(KValueRule)){
											ScriptEngine engine1 = manager.getEngineByName("js");
											//engine1.put("x", KDefine);
											try{
												int xCount=PubFun.subCount(KValue, "x");
												BigDecimal args=new BigDecimal(KDefine);
												for(int x=0;x<xCount;x++){
													engine1.put("x", args);
												}
											}catch(Exception e){
												LOGGER.warn(portItemCode+"配置的未知数"+computeVars[l]+"中的未知数x不是一个数值类型----"+KDefine);
												engine1.put("x", KDefine);
											} 
											try{
												KValue=String.valueOf(engine1.eval(KValue));
											}catch(Exception e){
												LOGGER.error(portItemCode+"指标因子"+computeVarRules[l]+"参数的计算规则错误!");
									        	throw new RuntimeException(portItemCode+"指标因子"+computeVarRules[l]+"参数的计算规则错误!");
											}
										}
										//engine.put(computeVars[l],KValue);
										try{
											BigDecimal args=new BigDecimal(KValue);
											engine.put(computeVars[l], args);
										}catch(Exception e){
											LOGGER.warn(portItemCode+"配置的未知数"+computeVars[l]+"不是一个数值类型----"+KValue);
											engine.put(computeVars[l], KValue);
										}
									}else if(computeVarRules[l].contains("M_")){ //矩阵逻辑 行可扩展不用
										RhoMatrixDefine rhoMatrixDefine =(RhoMatrixDefine) dao.get(RhoMatrixDefine.class, computeVarRules[l]);
										if(rhoMatrixDefine==null||"".equals(rhoMatrixDefine)){
											throw new RuntimeException("请检查"+portItemCode+"的配置中参数取数"+computeVarRules[l]+"是否正确");
										}
										int columnLine=rhoMatrixDefine.getColumnLine();
										String rhomatrixValue=rhoMatrixDefine.getRhomatrixValue();
										if(columnLine==1||columnLine==-1){ // 1行向量 -1 列向量
											String[] rowOrColMatrix=rhomatrixValue.split(",");
											int roclen=rowOrColMatrix.length;
											double[][] RowValue=new double[1][roclen];
											double[] ColValue=new double[roclen];
											for(int roc=0;roc<roclen;roc++){
												List<Map> listMDefineValue=null;
												BigDecimal MDefineValue= new BigDecimal(0);
												if(rowOrColMatrix[roc].contains("+")){
													String[]  rowOrColMatrixs=rowOrColMatrix[roc].split("\\+");
													ScriptEngine enginexroc = manager.getEngineByName("js");
													for(int rocm=0;rocm<rowOrColMatrixs.length;rocm++){
														if(rowOrColMatrixs[i].contains("-")){
															listMDefineValue=(List<Map>) dao.getList("select case when coltype in ('01','07') then  textvalue else to_char(numvalue) end as value from cfreportrowadddatarisk where reportid=? and colcode=? and rowno=? and yearmonth=? and quarter=? ",id,rowOrColMatrix[roc].substring(1),rowno,year,quarter);
															if(listMDefineValue.size()==0){
																throw new RuntimeException("请检查"+computeVarRules[l]+"的配置中配置的"+rowOrColMatrix[roc]+"中"+rowOrColMatrixs[rocm]+"指标代码是否正确");
															}
															try{
																enginexroc.put(rowOrColMatrixs[rocm].substring(1), new BigDecimal(String.valueOf(listMDefineValue.get(0).get("value"))));
															}catch(Exception e){
																throw new RuntimeException("请检查"+computeVarRules[l]+"的配置中配置的"+rowOrColMatrix[roc]+"中"+rowOrColMatrixs[rocm]+"指标代码所取到的值是否是数值类型");
															}
														}else{
															listMDefineValue=(List<Map>) dao.getList("select case when coltype in ('01','07') then  textvalue else to_char(numvalue) end as value from cfreportrowadddatarisk where reportid=? and colcode=? and rowno=? and yearmonth=? and quarter=? ",id,rowOrColMatrix[roc],rowno,year,quarter);
															if(listMDefineValue.size()==0){
																throw new RuntimeException("请检查"+computeVarRules[l]+"的配置中配置的"+rowOrColMatrix[roc]+"中"+rowOrColMatrixs[rocm]+"指标代码是否正确");
															}
															try{
															enginexroc.put(rowOrColMatrixs[rocm], new BigDecimal(String.valueOf(listMDefineValue.get(0).get("value"))));
															}catch(Exception e){
																throw new RuntimeException("请检查"+computeVarRules[l]+"的配置中配置的"+rowOrColMatrix[roc]+"中"+rowOrColMatrixs[rocm]+"指标代码所取到的值是否是数值类型");
															}
														}
													}
													Object result=enginexroc.eval(rowOrColMatrix[roc]);
													MDefineValue=(BigDecimal) result;
												}else{
													listMDefineValue=(List<Map>) dao.getList("select case when coltype in ('01','07') then  textvalue else to_char(numvalue) end as value from cfreportrowadddatarisk where reportid=? and colcode=? and rowno=? and yearmonth=? and quarter=? ",id,rowOrColMatrix[roc],rowno,year,quarter);
													if(listMDefineValue.size()==0){
														throw new RuntimeException("请检查"+computeVarRules[l]+"的配置中配置的"+rowOrColMatrix[roc]+"指标代码是否正确");
													}
													MDefineValue= new BigDecimal(String.valueOf(listMDefineValue.get(0).get("value")));
												}
												/*List<Map> listMDefineValue=(List<Map>) dao.getList("select case when coltype in ('01','07') then  textvalue else to_char(numvalue) end as value from cfreportrowadddata where colcode=? and rowno=? and yearmonth=? and quarter=? ",rowOrColMatrix[roc],rowno,year,quarter);											
												if(listMDefineValue.size()==0){
													throw new RuntimeException("请检查"+computeVarRules[l]+"的配置中配置的"+rowOrColMatrix[roc]+"指标代码是否正确");
												}
												BigDecimal MDefineValue= new BigDecimal(listMDefineValue.get(0).get("value").toString());*/
												//rhomatrixValue.replace(rowOrColMatrix[i], MDefineValue);
												if(columnLine==1){
													RowValue[0][roc]=MDefineValue.doubleValue();
												}else{
													ColValue[roc]=MDefineValue.doubleValue();
												}
											}
											if(columnLine==1){
												Matrix RowM=new Matrix(RowValue);
												engine.put(computeVars[l],RowM);
											}else{
												Matrix ColM=new Matrix(ColValue,roclen);
												engine.put(computeVars[l],ColM);
											}
										}else{
											String[] CrossMatrix=rhomatrixValue.split(",");
											double[][] CrossValue=new double[columnLine][ columnLine];
											try{
											for(int Row=0;Row<columnLine;Row++){
													for(int Col=0;Col<columnLine;Col++){
															CrossValue[Row][Col]=new BigDecimal(CrossMatrix[Row*columnLine+Col]).doubleValue();
													}
												}
												Matrix CrossM=new Matrix(CrossValue);
												engine.put(computeVars[l],CrossM);
											}catch(Exception e){
												throw new RuntimeException("请检查"+computeVarRules[l]+"的配置中配置的的值是否有无效数字");
											}
										}
									}
								}
							}
							System.out.println(calculateRule+"------------------");
							Object result=null;
							try{
								result=engine.eval(calculateRule);
							}catch(ScriptException e){
								LOGGER.error(portItemCode+"指标因子参数的计算规则错误!");
					        	//throw new RuntimeException(portItemCode+"指标因子参数的计算规则错误!");
								continue;
							}
							value=String.valueOf(result);
							if("Jama.Matrix".equals(result.getClass().getName())){
								value=String.valueOf(((Matrix)result).get(0,0));
							}
						}
						CfReportRowAddDataRiskId lacfReportRowAddDataId=new CfReportRowAddDataRiskId();
						CfReportRowAddDataRisk lacfReportRowAddData=new CfReportRowAddDataRisk();
						lacfReportRowAddDataId.setColCode(portItemCode);//列代码
						lacfReportRowAddDataId.setReportId(id);
						lacfReportRowAddDataId.setRowNo(rowno);//行数
						lacfReportRowAddData.setId(lacfReportRowAddDataId);
						lacfReportRowAddData.setYearMonth(year);//年度
						lacfReportRowAddData.setQuarter(quarter);//季度
						lacfReportRowAddData.setReportRate(reportType);//报送类型
						lacfReportRowAddData.setTableCode(portItemCode.split("_").length==2?portItemCode.split("_")[0]:portItemCode.split("_")[0]+"_"+portItemCode.split("_")[1]);//表代码
						lacfReportRowAddData.setReportType("1");//报表类别
						lacfReportRowAddData.setSource("9");//源1：指标导入；源2：自动提数业务；源3：自动提数财务；源4：精算模板导入；源5：投资模板导入；源6：计算
						lacfReportRowAddData.setOperator2(user.getUserCode());//操作人
						lacfReportRowAddData.setOperDate2(sdf.format(new Date()));//操作日期
						lacfReportRowAddData.setComCode(organCode);
						System.out.println(portItemCode+"的值为："+value);
						//CfReportRowAddDesc cricd=new CfReportRowAddDesc();
						List listcrrad= dao.queryWithJDBC("select crrad.coltype from CfReportRowAddDesc crrad where crrad.colCode='"+portItemCode+"'");
						List listocrrad= dao.queryWithJDBC("select ocrrad.coltype from rsys_CfReportRowAddDesc ocrrad where ocrrad.colCode='"+portItemCode+"'");
						String strOutItemType="";
				        if(listcrrad.size()>0){
				        	Map map=(Map) listcrrad.get(0);
				        	strOutItemType=(String) map.get("coltype");
				        }else{
				        	if(listocrrad.size()>0){
					        	Map mapo=(Map) listocrrad.get(0);
					        	strOutItemType=(String) mapo.get("coltype");
					        }else{
					        	LOGGER.error(portItemCode+"指标因子未找到!");
					        	//throw new RuntimeException(portItemCode+"指标因子未找到!");
					        	continue;
					        }
				        }
						if(strOutItemType==null||"".equals(strOutItemType)){
							System.out.println("行可扩展因子代码未定义");
						}
						System.out.println("coltype="+strOutItemType);
						String strColtype=strOutItemType;
						if(strColtype.equals("06")){//百分比型
							try{
								if("".equals(value)||value==null||"null".equals(value)){						
									value="0";
								}
								if(DBDecimals!=null&&!"".equals(DBDecimals)){
					    			   value =exportXbrlService.FormatData(value,DBDecimals); 
									}else{
										value =exportXbrlService.FormatData(value,"4"); 
									}
								lacfReportRowAddData.setColType("06");
								lacfReportRowAddData.setNumValue(new BigDecimal(value).setScale(8,BigDecimal.ROUND_HALF_UP));
								lacfReportRowAddData.setWanValue(new BigDecimal("0").setScale(12,BigDecimal.ROUND_HALF_UP));
							}catch(NumberFormatException e){
								 LOGGER.error(portItemCode+"指标因子取值不是百分比类型!");
								e.printStackTrace();
								continue;
							}
							lacfReportRowAddData.setTextValue("");
							lacfReportRowAddData.setDesText("");
						}else if(strColtype.equals("05")){//数量型
							try{
								if("".equals(value)||value==null||"null".equals(value)){							
									value="0";
								}
								if(DBDecimals!=null&&!"".equals(DBDecimals)){
					    			   value =exportXbrlService.FormatData(value,DBDecimals); 
									}else{
										value =exportXbrlService.FormatData(value,"0"); 
									}
								lacfReportRowAddData.setColType("05");
								lacfReportRowAddData.setNumValue(new BigDecimal(value).setScale(8,BigDecimal.ROUND_HALF_UP));
								lacfReportRowAddData.setWanValue(new BigDecimal(value).divide(new BigDecimal("10000"), 12, RoundingMode.HALF_UP));
							}catch(NumberFormatException e){
								 LOGGER.error(portItemCode+"指标因子取值不是数量类型!");
								e.printStackTrace();
								continue;
							}
							lacfReportRowAddData.setTextValue("");
							lacfReportRowAddData.setDesText("");
						}else if(strColtype.equals("04")){//数值型
							try{
								if("".equals(value)||value==null||"null".equals(value)){
									value="0";
								}
								if(DBDecimals!=null&&!"".equals(DBDecimals)){
					    			   value =exportXbrlService.FormatData(value,DBDecimals); 
									}else{
										value =exportXbrlService.FormatData(value,"2"); 
									}
								lacfReportRowAddData.setColType("04");
								lacfReportRowAddData.setNumValue(new BigDecimal(value).setScale(8,BigDecimal.ROUND_HALF_UP));
								lacfReportRowAddData.setWanValue(new BigDecimal(value).divide(new BigDecimal("10000"), 12, RoundingMode.HALF_UP));
							}catch(NumberFormatException e){
								 LOGGER.error(portItemCode+"指标因子取值不是数值类型!");
								e.printStackTrace();
								continue;
							}
							lacfReportRowAddData.setTextValue("");
							lacfReportRowAddData.setDesText("");		        	
						}else if(strColtype.equals("02")){//描述型
							try {
								lacfReportRowAddData.setColType("02");
								lacfReportRowAddData.setNumValue(new BigDecimal("0").setScale(4,BigDecimal.ROUND_HALF_UP));
								lacfReportRowAddData.setWanValue(new BigDecimal("0").setScale(8,BigDecimal.ROUND_HALF_UP));
								lacfReportRowAddData.setTextValue(value);
							} catch (NumberFormatException e) {
								 LOGGER.error(portItemCode+"指标因子取值不是描述类型!");
								e.printStackTrace();
								continue;
							}
							lacfReportRowAddData.setDesText("");		        	
						}else if(strColtype.equals("01")){//短文本类型
							try {
								lacfReportRowAddData.setColType("01");
								lacfReportRowAddData.setNumValue(new BigDecimal("0").setScale(4,BigDecimal.ROUND_HALF_UP));
								lacfReportRowAddData.setWanValue(new BigDecimal("0").setScale(8,BigDecimal.ROUND_HALF_UP));
								lacfReportRowAddData.setTextValue(value);
							} catch (NumberFormatException e) {
								 LOGGER.error(portItemCode+"指标因子取值不是短文本类型!");
								e.printStackTrace();
								continue;
							}
							lacfReportRowAddData.setDesText("");		        	
						}else if(strColtype.equals("07")){//日期型
							try {
								lacfReportRowAddData.setColType("07");
								lacfReportRowAddData.setNumValue(new BigDecimal("0").setScale(8,BigDecimal.ROUND_HALF_UP));
								lacfReportRowAddData.setWanValue(new BigDecimal("0").setScale(12,BigDecimal.ROUND_HALF_UP));
								//TODO
								lacfReportRowAddData.setTextValue(value);
								lacfReportRowAddData.setDesText("");		        	
							} catch (NumberFormatException e) {
								 LOGGER.error(portItemCode+"指标因子取值不是日期类型!");
								e.printStackTrace();
								continue;
							}
						}else{
							System.out.println("导入因子值类型有误！！！");
						}
						dao.create(lacfReportRowAddData);
						dao.flush();
					}
				}
			}
		}
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional
    public void putLoseInAll(String id, UserInfo user){
		
		//分险种数据表中 损失吸收调整-不考虑上限、特定类别保险合同损失吸收效应是否存在
		String getS07Lose=" select * from cfreportdatarisk cf where cf.outitemcode in('S07_00025','S07_00024','S01_00015','S07_00001','S07_00023','S01_00009','S07_00033','S01_00014','S01_00008','S01_00020','S01_00021','S01_00018','S01_00019','S07_00027','S01_00016') and cf.reportid='"+id+"'";
		List<CfReportDataRisk> risks = (List<CfReportDataRisk>) dao.getList(getS07Lose,CfReportDataRisk.class);
		if(risks.size()>0){
			for (int i = 0; i < risks.size(); i++) {
				CfReportDataRisk risk = risks.get(i);
				CfReportData reportData = new CfReportData();
				CfReportDataId reportDataId = new CfReportDataId();
				
				reportDataId.setReportId(id);
				reportDataId.setOutItemCode(risk.getReportItemCode());
				reportData.setId(reportDataId);

				reportData.setComCode(risk.getComCode());
				reportData.setMonth(risk.getMonth());
				reportData.setOutItemType(risk.getOutItemType());
				reportData.setQuarter(risk.getQuarter());
				reportData.setReportItemCode(risk.getReportItemCode());
				reportData.setReportItemValue(risk.getReportItemValue());
				reportData.setReportItemWanValue(risk.getReportItemWanValue());
				reportData.setReportItemValueSource(risk.getReportItemValueSource());
				reportData.setReportRate(risk.getReportRate());
				reportData.setReportType(risk.getReportType());
				reportData.setSource(risk.getSource());
				reportData.setComputeLevel(risk.getComputeLevel());
				reportData.setOperator(risk.getOperator());
				reportData.setOperDate(risk.getOperDate());
				//删除原数据
				String delSQlString="delete from cfreportdata cf where cf.outitemcode='"+risk.getReportItemCode()+"' and cf.reportid='"+id+"'";
				
				dao.excute(delSQlString);
				
				dao.create(reportData);
				dao.flush();
			}			
			
		}else{
			LOGGER.info("损失吸收调整-不考虑上限无数据");
		}
		
	}
	
}
