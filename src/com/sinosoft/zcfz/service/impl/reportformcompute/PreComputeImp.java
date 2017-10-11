package com.sinosoft.zcfz.service.impl.reportformcompute;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fr.report.core.A.q;
import com.sinosoft.common.Constant;
import com.sinosoft.common.Dao;
import com.sinosoft.entity.CalCfReportInfo;
import com.sinosoft.entity.CalCfReportRelation;
import com.sinosoft.entity.CfReportData;
import com.sinosoft.entity.CfReportDataId;
import com.sinosoft.entity.CfReportElement;
import com.sinosoft.entity.CfReportItemCodeDesc;
import com.sinosoft.entity.CfReportRowAddData;
import com.sinosoft.entity.CfReportRowAddDataId;
import com.sinosoft.entity.OCfReportItemCodeDesc;
import com.sinosoft.entity.UserInfo;
import com.sinosoft.zcfz.service.reportform.ExportXbrlService;
import com.sinosoft.zcfz.service.reportformcompute.CalPreComputeService;
@Service
public class PreComputeImp implements CalPreComputeService{
	
	@Resource
	private Dao dao;
	@Resource
	private ExportXbrlService exportXbrlService;
	private String organCode=Constant.ORGANCODE;
	private static final Logger LOGGER = LoggerFactory.getLogger(PreComputeImp.class);
	
	
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
		if(how==4){//处理获取每年的最后一个月份
			String[] result=new String[3];
			
			result[0]=String.valueOf(year-1);
			result[1]=String.valueOf(4);
			
			
			String yearMonth = result[0]+"12";
			result[2]=yearMonth;
			return result;
		}else if(how==5){//处理包含的会计月度 
			String[] result=new String[3];
			String yearMonthes1 = "";
			String yearMonthes2 = "";
			String yearMonthes3 = "";
			if(quarter==1){
				yearMonthes1 = "'"+year+"01'";
				yearMonthes2 = "'"+year+"02'";
				yearMonthes3 = "'"+year+"03'";
			}else if(quarter==2){
				yearMonthes1 = "'"+year+"04'";
				yearMonthes2 = "'"+year+"05'";
				yearMonthes3 = "'"+year+"06'";
			}else if(quarter==3){
				yearMonthes1 = "year"+"07";
				yearMonthes2 = "year"+"08";
				yearMonthes3 = "year"+"09";
			}else if(quarter==4){
				yearMonthes1 = "'"+year+"10'";
				yearMonthes2 = "'"+year+"11'";
				yearMonthes3 = "'"+year+"12'";
			}
			result[0] = yearMonthes1;
			result[1] = yearMonthes2;
			result[2] = yearMonthes3;
			return result;
		}else if(how==6){//获取每年的第一个月份
			String[] result=new String[1];
			String yearMonth = "";
			yearMonth = year+"01";
			result[0] = yearMonth;
			return result;
		}else{//获取前一个季度的最大报送号
			String[] result=new String[3];
			int resultquart=quarter-how;
			int resultyear=year;
			if(quarter-how<=0){
				resultquart=resultquart+4;
				resultyear=year-1;
			}
			result[0]=String.valueOf(resultyear);
			result[1]=String.valueOf(resultquart);
			
			//上一个季度的最新报送号
			String getIdSql = "select max(ca.reportid) from cal_cfreportinfo ca "
					+ " where ca.year='"+result[0]
					+ "' and ca.quarter='"+result[1]
					+ "' and ca.reporttype=2 ";
			List<Map> lastReportIdList = (List<Map>) dao.getList(getIdSql);
			String LastReprotId = lastReportIdList.get(0).get("max(ca.reportid)").toString().trim();
			result[2]=LastReprotId;
			return result;
		}
		
	}

	/**
	 * 初步计算的逻辑
	 * @param id
	 * @throws Exception 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional
	public void preCompute(String id,UserInfo user,String reporttypes) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd  HH:mm:ss" );
		CalCfReportInfo ccri=new CalCfReportInfo();
		ccri=(CalCfReportInfo) dao.get(CalCfReportInfo.class, id);
		String year=ccri.getYear();
		String quarter=ccri.getQuarter();
		String day=ccri.getDataDate();
		//初步计算前删除初步计算的数据防止唯一主键冲突
		String lsql1="delete from cfreportrowadddata where reportid='"+id+"'  and colcode in (select portitemcode from cal_cfreportrelation where portitemtype='1' and interfacetype='0' and remark='"+reporttypes+"')";  
		String lsql2="delete from cfreportdata where reportid='"+id+"' and outitemcode in (select portitemcode from cal_cfreportrelation where portitemtype='0' and interfacetype='0' and remark='"+reporttypes+"')";
		try{
			dao.excute(lsql1);
			dao.excute(lsql2);
		}catch(Exception e){
			LOGGER.error("删除初步计算数据出错！");
        	throw new RuntimeException("删除初步计算数据出错！");
		}
		//报送类型
		String reportType=ccri.getReportType();
		List<CalCfReportRelation> list=(List<CalCfReportRelation>) dao.query("from CalCfReportRelation where interfaceType='0' and remark='"+reporttypes+"'");
		for(int i=0;i<list.size();i++){
			CalCfReportRelation ccrr=list.get(i);
			//指标代码
			String portItemCode=ccrr.getPortItemCode();
			
			//取数规则
			String interfaceGetData=ccrr.getInterfaceGetData();
			if(interfaceGetData==null||"".equals(interfaceGetData)){
				continue;
			}
			//指标类型
			String portItemType=ccrr.getPortItemType();
			
			if(portItemCode.equals("3180412_02391")){
				System.out.println("33333333333");
			}
			//取数规则参数
			String InterfaceGetDataVar=ccrr.getInterfaceGetDataVar();
			if(InterfaceGetDataVar!=null&&!"".equals(InterfaceGetDataVar)){
				if(InterfaceGetDataVar.contains("firstquarterago")){
					String[] fistquarterago=getquarter(year,quarter,1);
					InterfaceGetDataVar=InterfaceGetDataVar.replace("year", fistquarterago[0]);
					InterfaceGetDataVar=InterfaceGetDataVar.replace("firstquarterago", fistquarterago[1]);					
					InterfaceGetDataVar = InterfaceGetDataVar.replace("reportId",fistquarterago[2]);
				}else if(InterfaceGetDataVar.contains("twoquarterago")){
					String[] twoquarterago=getquarter(year,quarter,1);
					InterfaceGetDataVar=InterfaceGetDataVar.replace("year", twoquarterago[0]);
					InterfaceGetDataVar=InterfaceGetDataVar.replace("twoquarterago", twoquarterago[1]);
					InterfaceGetDataVar = InterfaceGetDataVar.replace("reportId",twoquarterago[2]);
				}else if(InterfaceGetDataVar.contains("threequarterago")){
					String[] threequarterago=getquarter(year,quarter,1);
					InterfaceGetDataVar=InterfaceGetDataVar.replace("year", threequarterago[0]);
					InterfaceGetDataVar=InterfaceGetDataVar.replace("threequarterago", threequarterago[1]);
					InterfaceGetDataVar = InterfaceGetDataVar.replace("reportId",threequarterago[2]);

				}//处理取上一年年末数的参数 year  lastyearquarter yearmonth
				else if(InterfaceGetDataVar.contains("lastyearquarter")){
					String[] lastYearQuarter=getquarter(year,quarter,4);
					InterfaceGetDataVar=InterfaceGetDataVar.replace("year", lastYearQuarter[0]);
					InterfaceGetDataVar=InterfaceGetDataVar.replace("lastyearquarter", lastYearQuarter[1]);
					InterfaceGetDataVar = InterfaceGetDataVar.replace("yearmonth",lastYearQuarter[2]);
				}				
				//处理本季度数所包含的 会计月度  year quarter  LYearMonthes1 LYearMonthes2 LYearMonthes3
				else if(InterfaceGetDataVar.contains("LYearMonthes")){
					String[] LYearMonthes=getquarter(year,quarter,5);
					InterfaceGetDataVar = InterfaceGetDataVar.replace("LYearMonthes1",LYearMonthes[0]);
					InterfaceGetDataVar = InterfaceGetDataVar.replace("LYearMonthes2",LYearMonthes[1]);
					InterfaceGetDataVar = InterfaceGetDataVar.replace("LYearMonthes3",LYearMonthes[2]);
					InterfaceGetDataVar=InterfaceGetDataVar.replace("year", year);
					InterfaceGetDataVar=InterfaceGetDataVar.replace("quarter", quarter);
					InterfaceGetDataVar=InterfaceGetDataVar.replace("reportId", id);
				}
				//处理取本年年初数 本年1月份的数据 year firstYearMonth  
				else if(InterfaceGetDataVar.contains("firstYearMonth")){
					String[] firstYearMonth=getquarter(year,quarter,6);
					InterfaceGetDataVar=InterfaceGetDataVar.replace("firstYearMonth", firstYearMonth[0]);
					InterfaceGetDataVar=InterfaceGetDataVar.replace("year", year);
					InterfaceGetDataVar=InterfaceGetDataVar.replace("reportId", id);
				}
				else{
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
						System.out.println(InterfaceGetDataVar.length());
						listData= (List<Map>) dao.getList(interfaceGetData,InterfaceGetDataVar.trim().split(","));
					}else{
						listData=(List<Map>) dao.getList(interfaceGetData);
					}
					if(listData.size()==0){
						value="0";
					}else{
						value=String.valueOf(listData.get(0).get("value"));
					}
					System.out.println(portItemCode+"的值为："+value);
					CfReportDataId cfReportDataId=new CfReportDataId();
				    CfReportData cfReportData=new CfReportData();
				    cfReportDataId.setOutItemCode(portItemCode);//指标代码
				    cfReportDataId.setReportId(id);
			        cfReportData.setId(cfReportDataId);
			        cfReportData.setMonth(year);//年度
			        cfReportData.setQuarter(quarter);//季度
			        cfReportData.setReportRate(reportType);//报送类型
			        cfReportData.setComCode(organCode);//机构代码
			        cfReportData.setOutItemCodeName("");//因子名称
			        cfReportData.setReportItemCode(portItemCode);//因子代码
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
					    		   cfReportData.setReportItemValue(new BigDecimal(value).setScale(8,BigDecimal.ROUND_HALF_UP));
					    		   cfReportData.setReportItemWanValue(new BigDecimal(0).setScale(12,BigDecimal.ROUND_HALF_UP));
					    	   }
					       } catch (NumberFormatException e) {
					    	   LOGGER.error(portItemCode+"指标因子取值不是百分比类型!");
					    	   e.printStackTrace();
					    	   continue;
					       }
				    	   cfReportData.setOutItemType("06");
				    	   cfReportData.setTextValue("");
				    	   cfReportData.setDesText(null);//描述性型
				    	   cfReportData.setFileText(null);//文件型
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
				    			   cfReportData.setReportItemValue(new BigDecimal(value).setScale(8,BigDecimal.ROUND_HALF_UP));
				    			   cfReportData.setReportItemWanValue(new BigDecimal(value).divide(new BigDecimal("10000"), 12, RoundingMode.HALF_UP));
				    		   }
					       } catch (NumberFormatException e) {
					    	   LOGGER.error(portItemCode+"指标因子取值不是数量类型!");
					    	   e.printStackTrace();
					    	   continue;
					       }
				    	   cfReportData.setOutItemType("05");
				    	   cfReportData.setTextValue("");
				    	   cfReportData.setDesText(null);//描述性型
				    	   cfReportData.setFileText(null);//文件型
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
				    			   cfReportData.setReportItemValue(new BigDecimal(value).setScale(8,BigDecimal.ROUND_HALF_UP));
				    			   cfReportData.setReportItemWanValue(new BigDecimal(value).divide(new BigDecimal("10000"), 12, RoundingMode.HALF_UP));
				    		   }
					       } catch (NumberFormatException e) {
					    	   LOGGER.error(portItemCode+"指标因子取值不是数值类型!");
					    	   e.printStackTrace();
					    	   continue;
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
					    	   LOGGER.error(portItemCode+"指标因子取值不是文件类型!");
					    	   e.printStackTrace();
					    	   continue;
					       }
				    	   cfReportData.setTextValue("");
				    	   cfReportData.setDesText(null);//描述性型
				    	   cfReportData.setFileText(null);//文件型
				       }else if(strOutItemType.equals("02")){ //描述
				    	   try {
				    		   cfReportData.setOutItemType("02");
				    		   cfReportData.setReportItemValue(new BigDecimal(0).setScale(4,BigDecimal.ROUND_HALF_UP));
				    		   cfReportData.setReportItemWanValue(new BigDecimal(0).setScale(8,BigDecimal.ROUND_HALF_UP));
					       } catch (NumberFormatException e) {
					    	   LOGGER.error(portItemCode+"指标因子取值不是描述类型!");
					    	   e.printStackTrace();
					    	   continue;
					       }
				    	   cfReportData.setTextValue(null);
				    	   cfReportData.setDesText(null);//描述性型
				    	   cfReportData.setFileText(null);//文件型
				       }else if(strOutItemType.equals("01")){//短文本类型
				    	   cfReportData.setOutItemType("01");
				    	   cfReportData.setReportItemValue(new BigDecimal(0).setScale(4,BigDecimal.ROUND_HALF_UP));
				    	   cfReportData.setReportItemWanValue(new BigDecimal(0).setScale(8,BigDecimal.ROUND_HALF_UP));
				    	   try {
				    		   cfReportData.setTextValue(value);
				    		   if(value==null||"null".equals(value)){
				    			   cfReportData.setTextValue("");
				    		   }
					       } catch (NumberFormatException e) {
					    	   LOGGER.error(portItemCode+"指标因子取值不是短文本类型!");
					    	   e.printStackTrace();
					    	   continue;
					       }
				    	   cfReportData.setDesText(null);//描述性型
				    	   cfReportData.setFileText(null);//文件型
				       }
				       else if(strOutItemType.equals("07")){//日期型
				    	   cfReportData.setOutItemType("07");
				    	   cfReportData.setReportItemValue(new BigDecimal(0).setScale(8,BigDecimal.ROUND_HALF_UP));
				    	   cfReportData.setReportItemWanValue(new BigDecimal(0).setScale(12,BigDecimal.ROUND_HALF_UP));
				    	   try {
				    		   cfReportData.setTextValue(sdf.format(value));
					       } catch (NumberFormatException e) {
					    	   cfReportData.setTextValue(value);
					       }
				    	   cfReportData.setDesText(null);//描述性型
				    	   cfReportData.setFileText(null);//文件型
				       }else{
				    	   System.out.println("导入因子值类型有误！！！");
				       }
				       cfReportData.setSource("8");//源1：指标导入；源2：自动提数业务；源3：自动提数财务；源4：精算模板导入；源5：投资模板导入；源6：计算
				       cfReportData.setReportType(reporttypes);//报表类别
				       cfReportData.setComputeLevel("0");//计算级别
				       cfReportData.setReportItemValueSource("1");//指标值来源：1-初步计算数据手工修改；2-尾数自动调整
				       cfReportData.setOperator(user.getUserCode());//默认（操作人）
				       cfReportData.setOperDate(sdf.format(new Date()));//操作日期
				       cfReportData.setTemp("");
				       dao.create(cfReportData);
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
					CfReportRowAddDataId cfReportRowAddDataId=new CfReportRowAddDataId();
					CfReportRowAddData cfReportRowAddData=new CfReportRowAddData();
					cfReportRowAddDataId.setColCode(portItemCode);//列代码
					cfReportRowAddDataId.setRowNo(rowno);//行数
					cfReportRowAddDataId.setReportId(id);
					cfReportRowAddData.setId(cfReportRowAddDataId);
					cfReportRowAddData.setTableCode(portItemCode.split("_").length==2?portItemCode.split("_")[0]:portItemCode.split("_")[0]+"_"+portItemCode.split("_")[1]);//表代码
					cfReportRowAddData.setYearMonth(year);//年度
					cfReportRowAddData.setQuarter(quarter);//季度
					cfReportRowAddData.setReportRate(reportType);//报送类型
					cfReportRowAddData.setReportType(reporttypes);//报表类别
					cfReportRowAddData.setSource("8");//源1：指标导入；源2：自动提数业务；源3：自动提数财务；源4：精算模板导入；源5：投资模板导入；源6：初步计算 7.汇总计算
					cfReportRowAddData.setOperator2(user.getUserCode());//操作人
					cfReportRowAddData.setOperDate2(sdf.format(new Date()));//操作日期
					cfReportRowAddData.setComCode(organCode);
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
							cfReportRowAddData.setColType("06");
							if(DBDecimals!=null&&!"".equals(DBDecimals)){
				    			   value =exportXbrlService.FormatData(value,DBDecimals); 
								}else{
									value =exportXbrlService.FormatData(value,"4"); 
								}
							cfReportRowAddData.setNumValue(new BigDecimal(value).setScale(8,BigDecimal.ROUND_HALF_UP));
							cfReportRowAddData.setWanValue(new BigDecimal("0").setScale(12,BigDecimal.ROUND_HALF_UP));
						}catch(NumberFormatException e){
							 LOGGER.error(portItemCode+"指标因子取值不是百分比类型!");
							e.printStackTrace();
							continue;
						}
						cfReportRowAddData.setTextValue("");
						cfReportRowAddData.setDesText("");
					}else if(strColtype.equals("05")){//数量型
						try{
							if("".equals(value)||value==null){								
								value="0";
							}
							cfReportRowAddData.setColType("05");
							if(DBDecimals!=null&&!"".equals(DBDecimals)){
				    			   value =exportXbrlService.FormatData(value,DBDecimals); 
								}else{
									value =exportXbrlService.FormatData(value,"0"); 
								}
							cfReportRowAddData.setNumValue(new BigDecimal(value).setScale(8,BigDecimal.ROUND_HALF_UP));
							cfReportRowAddData.setWanValue(new BigDecimal(value).divide(new BigDecimal("10000"), 12, RoundingMode.HALF_UP));
						}catch(NumberFormatException e){
							 LOGGER.error(portItemCode+"指标因子取值不是数量类型!");
							e.printStackTrace();
							continue;
						}
						cfReportRowAddData.setTextValue("");
						cfReportRowAddData.setDesText("");
					}else if(strColtype.equals("04")){//数值型
						try{
							if("".equals(value)||value==null){
								value="0";
							}
							cfReportRowAddData.setColType("04");
							if(DBDecimals!=null&&!"".equals(DBDecimals)){
				    			   value =exportXbrlService.FormatData(value,DBDecimals); 
								}else{
									value =exportXbrlService.FormatData(value,"2"); 
								}
							cfReportRowAddData.setNumValue(new BigDecimal(value).setScale(8,BigDecimal.ROUND_HALF_UP));
							cfReportRowAddData.setWanValue(new BigDecimal(value).divide(new BigDecimal("10000"), 12, RoundingMode.HALF_UP));
						}catch(NumberFormatException e){
							 LOGGER.error(portItemCode+"指标因子取值不是数值类型!");
							e.printStackTrace();
							continue;
						}
						cfReportRowAddData.setTextValue("");
						cfReportRowAddData.setDesText("");		        	
					}else if(strColtype.equals("02")){//描述型
						try {
							cfReportRowAddData.setColType("02");
							cfReportRowAddData.setNumValue(new BigDecimal("0").setScale(4,BigDecimal.ROUND_HALF_UP));
							cfReportRowAddData.setWanValue(new BigDecimal("0").setScale(8,BigDecimal.ROUND_HALF_UP));
							cfReportRowAddData.setTextValue(value);
							if(value==null||"null".equals(value)){
								cfReportRowAddData.setTextValue("");
							}
						} catch (NumberFormatException e) {
							 LOGGER.error(portItemCode+"指标因子取值不是描述类型!");
							e.printStackTrace();
							continue;
						}
						cfReportRowAddData.setDesText("");		        	
					}else if(strColtype.equals("01")){//短文本类型
						try {
							cfReportRowAddData.setColType("01");
							cfReportRowAddData.setNumValue(new BigDecimal("0").setScale(4,BigDecimal.ROUND_HALF_UP));
							cfReportRowAddData.setWanValue(new BigDecimal("0").setScale(8,BigDecimal.ROUND_HALF_UP));
							cfReportRowAddData.setTextValue(value);
						} catch (NumberFormatException e) {
							 LOGGER.error(portItemCode+"指标因子取值不是短文本类型!");
							e.printStackTrace();
							continue;
						}
						cfReportRowAddData.setDesText("");		        	
					}else if(strColtype.equals("07")){//日期型
						try {
							cfReportRowAddData.setColType("07");
							cfReportRowAddData.setNumValue(new BigDecimal("0").setScale(8,BigDecimal.ROUND_HALF_UP));
							cfReportRowAddData.setWanValue(new BigDecimal("0").setScale(12,BigDecimal.ROUND_HALF_UP));
							//TODO
							cfReportRowAddData.setTextValue(value);
							cfReportRowAddData.setDesText("");		        	
						} catch (NumberFormatException e) {
							 LOGGER.error(portItemCode+"指标因子取值不是日期类型!");
							e.printStackTrace();
							continue;
						}
					}else{
						System.out.println("导入因子值类型有误！！！");
					}
					dao.create(cfReportRowAddData);
				}
			}
		}
		ccri.setReportState("2");
		try {
			dao.update(ccri);
		}catch (Exception e) {
			LOGGER.error("更新报送单状态报错");
			e.printStackTrace();
			throw new RuntimeException("更新报送单状态报错");
		}
	}

	@Transactional
	public void backPreCompute(String id,String reporttype) {
		CalCfReportInfo ccri=new CalCfReportInfo();
		ccri=(CalCfReportInfo) dao.get(CalCfReportInfo.class, id);
		String sql = "";
		String sql1 = "";
		//现金流
		if(reporttype.equals("3")){
			 sql="delete from cfreportdata where reportid='"+id+"' and source='8' and "
					 + " outitemcode in(select ca.portitemcode from cal_cfreportrelation ca where ca.remark='3' and portitemtype='0' )"; 
			 try{
					dao.excute(sql);
				}catch(Exception e){
					LOGGER.error("删除初步计算数据时出错！");
					e.printStackTrace();
					throw new RuntimeException("删除初步计算数据时出错！");
				}
		}
		//偿付能力报告
		else if(reporttype.equals("1")){
			sql="delete from cfreportdata where reportid='"+id+"' and source='8' and "
					 + " outitemcode in(select ca.portitemcode from cal_cfreportrelation ca where ca.remark='1' and portitemtype='0' )";
			 sql1="delete from cfreportrowadddata where reportid='"+id+"' and source='8' and "
					 + " colcode in(select ca.portitemcode from cal_cfreportrelation ca where ca.remark='1' and portitemtype='1' )";
		
			 try{
					dao.excute(sql);
					dao.excute(sql1);
				}catch(Exception e){
					LOGGER.error("删除初步计算数据时出错！");
					e.printStackTrace();
					throw new RuntimeException("删除初步计算数据时出错！");
				}
		}
		
		ccri.setReportState("1");
		try {
			dao.update(ccri);
		}catch (Exception e) {
			LOGGER.error("更新报送单状态报错");
			e.printStackTrace();
			throw new RuntimeException("更新报送单状态报错");
		}
	}
}
