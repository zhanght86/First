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

import Jama.Matrix;

import com.sinosoft.common.Constant;
import com.sinosoft.common.Dao;
import com.sinosoft.entity.CalCfReportInfo;
import com.sinosoft.entity.CalCfReportRelation;
import com.sinosoft.entity.CalRiskRelation;
import com.sinosoft.entity.CfReportData;
import com.sinosoft.entity.CfReportDataId;
import com.sinosoft.entity.CfReportElement;
import com.sinosoft.entity.CfReportItemCodeDesc;
import com.sinosoft.entity.OCfReportItemCodeDesc;
import com.sinosoft.entity.UserInfo;
import com.sinosoft.zcfz.service.reportform.ExportXbrlService;
import com.sinosoft.zcfz.service.reportformcompute.RiskComputeService;

@Service
public class RiskComputeImp implements RiskComputeService{
	
	@Resource
	private Dao dao;
	@Resource
	private ExportXbrlService exportXbrlService;
	private String organCode=Constant.ORGANCODE;
	private static final Logger LOGGER = LoggerFactory.getLogger(RiskComputeImp.class);
	
	
	/**
	 * 汇总计算的逻辑
	 * @param id
	 * @throws Exception 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Transactional
	public void ComplexCompute(String id,UserInfo user) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd" );
		CalCfReportInfo ccri=new CalCfReportInfo();
		ccri=(CalCfReportInfo) dao.get(CalCfReportInfo.class, id);
		String year=ccri.getYear();
		String quarter=ccri.getQuarter();
		//报送类型
		String reportType=ccri.getReportType();
		//汇总计算前删除汇总计算的数据防止唯一主键冲突
		String lsql2="delete from cfreportdata where month="+year+" and quarter="+quarter+" and outitemcode in (select portitemcode from cal_riskrelation where portitemtype='0' and interfacetype='1')";
		try{
			dao.excute(lsql2);
		}catch(Exception e){
			LOGGER.error("删除指标计算数据出错！");
        	throw new RuntimeException("删除指标计算数据出错！");
		}
		int ifpc=dao.getCount("select count(*) from (select distinct interfacePriority from cal_riskrelation where interfaceType=?)", "1");
		List<Map> interfacePrioritys=(List<Map>) dao.getList("select distinct interfacePriority as interfacePriority from cal_riskrelation where interfaceType=? order by interfacePriority", "1");
		for(int c=1;c<=ifpc;c++){
			String interfacePriority="";
			try{
				interfacePriority=String.valueOf(interfacePrioritys.get(c-1).get("interfacePriority"));
				System.out.println("开始计算优先级为："+interfacePriority+"的汇总数据----------------");
			}catch(Exception e){
				LOGGER.error("请检查优先级的配置是否正确！");
	        	throw new RuntimeException("请检查优先级的配置是否正确！");
			}
			List<CalRiskRelation> list=(List<CalRiskRelation>) dao.query("from CalRiskRelation where interfaceType='1' and interfacePriority='"+interfacePriority+"'");
			for(int i=0;i<list.size();i++){
				CalRiskRelation ccrr=list.get(i);
				//指标代码
				String portItemCode=ccrr.getPortItemCode();
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
								throw new RuntimeException("请检查"+portItemCode+"的配置中取数sql是否正确");
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
						listData=(List<Map>) dao.getList("select case when outitemtype in ('01','07') then  textvalue else to_char(reportitemvalue) end as value from cfreportdata where month=? and quarter=? and outitemcode=?",year,quarter,interfaceGetData);

					}
				}
				if("0".equals(portItemType)){
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
								 if(computeVarRules[l].contains("zb_")){
									String outitemCode=computeVarRules[l].substring(3);
									List<Map> listVarData=(List<Map>) dao.getList("select case when outitemtype in ('01','07') then  textvalue else to_char(reportitemvalue) end as value from cfreportdata where month=? and quarter=? and outitemcode=?",year,quarter,outitemCode);
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
									try{
										BigDecimal args=new BigDecimal(String.valueOf(listVarData.get(0).get("value")));
										engine.put(computeVars[l], args);
									}catch(Exception e){
										LOGGER.warn(portItemCode+"配置的未知数"+computeVars[l]+"不是一个数值类型----"+String.valueOf(listVarData.get(0).get("value")));
										engine.put(computeVars[l], String.valueOf(listVarData.get(0).get("value")));
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
					System.out.println(portItemCode+"的值为："+value);
					CfReportDataId cfReportDataId=new CfReportDataId();
				    CfReportData cfReportData=new CfReportData();
				    cfReportDataId.setOutItemCode(portItemCode);//指标代码
				    cfReportDataId.setReportId(id);
				    cfReportData.setMonth(year);//年度
			        cfReportData.setQuarter(quarter);//季度
			        cfReportData.setReportRate(reportType);//报送类型
			        cfReportData.setId(cfReportDataId);
			        cfReportData.setComCode(organCode);//机构代码
			        cfReportData.setOutItemCodeName("");//因子名称
			        cfReportData.setReportItemCode(portItemCode);//因子代码
			        cfReportData.setId(cfReportDataId);
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
				    		   if("".equals(value)||value==null||"null".equals(value)){		    		
				    			   value="0";
					    		   System.out.println(portItemCode+"无值可取,默认取0！！！");
				    		   }
				    		   if(DBDecimals!=null&&!"".equals(DBDecimals)){
				    			   value =exportXbrlService.FormatData(value,DBDecimals); 
								}else{
									value =exportXbrlService.FormatData(value,"4"); 
								}
				    		   cfReportData.setReportItemValue(new BigDecimal(value).setScale(8,BigDecimal.ROUND_HALF_UP));
				    		   cfReportData.setReportItemWanValue(new BigDecimal(0).setScale(12,BigDecimal.ROUND_HALF_UP));
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
				    		   if("".equals(value)||value==null||"null".equals(value)){		
				    			   value="0";
				    			   System.out.println(portItemCode+"无值可取,默认取0！！！");
				    		   }
				    		   if(DBDecimals!=null&&!"".equals(DBDecimals)){
				    			   value =exportXbrlService.FormatData(value,DBDecimals); 
								}else{
									value =exportXbrlService.FormatData(value,"0"); 
								}
			    			   cfReportData.setReportItemValue(new BigDecimal(value).setScale(8,BigDecimal.ROUND_HALF_UP));
			    			   cfReportData.setReportItemWanValue(new BigDecimal(value).divide(new BigDecimal("10000"), 12, RoundingMode.HALF_UP));
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
				    		   if("".equals(value)||value==null||"null".equals(value)){		    
				    			   value="0";
				    			   System.out.println(portItemCode+"无值可取,默认取0！！！");
				    		   }
				    		   if(DBDecimals!=null&&!"".equals(DBDecimals)){
				    			   value =exportXbrlService.FormatData(value,DBDecimals); 
								}else{
									value =exportXbrlService.FormatData(value,"2"); 
								}
			    			   cfReportData.setReportItemValue(new BigDecimal(value).setScale(8,BigDecimal.ROUND_HALF_UP));
			    			   cfReportData.setReportItemWanValue(new BigDecimal(value).divide(new BigDecimal("10000"), 12, RoundingMode.HALF_UP));
					       } catch (NumberFormatException e) {
					    	   LOGGER.error(portItemCode+"指标因子取值不是数值类型!");
					    	   e.printStackTrace();
					    	   continue;
					       }
				    	   cfReportData.setOutItemType("04");
				    	   cfReportData.setTextValue("");
				    	   cfReportData.setDesText(null);//描述性型
				    	   cfReportData.setFileText(null);//文件型
				       }else{
				    	   System.out.println("导入因子值类型有误！！！");
				       }
				       cfReportData.setSource("6");//源6：风险指标计算
				       cfReportData.setReportType("4");//报表类别 风险综合评级 法人机构
				       cfReportData.setComputeLevel("0");//计算级别
				       cfReportData.setReportItemValueSource("1");//指标值来源：1-初步计算数据手工修改；2-尾数自动调整
				       cfReportData.setOperator(user.getUserCode());//默认（操作人）
				       cfReportData.setOperDate(sdf.format(new Date()));//操作日期
				       cfReportData.setTemp("");
				       dao.create(cfReportData);
				       dao.flush();
				}
			}
		}
		ccri.setReportState("4");
		try {
			dao.update(ccri);
		}catch (Exception e) {
			LOGGER.error("更新报送单状态报错");
			e.printStackTrace();
			throw new RuntimeException("更新报送单状态报错！");
		}
	}
	@Transactional
	public void backComplexCompute(String id) {
		CalCfReportInfo ccri=new CalCfReportInfo();
		ccri=(CalCfReportInfo) dao.get(CalCfReportInfo.class, id);
		String sql="delete from cfreportdata where reportid='"+id+"' and source='9'";
		String sql1="delete from cfreportrowadddata where reportid='"+id+"' and source='9'";
		try{
			dao.excute(sql);
			dao.excute(sql1);
		}catch(Exception e){
			LOGGER.error("删除风险指标计算数据时出错！");
			e.printStackTrace();
			throw new RuntimeException("删除风险指标计算数据时出错！");
		}
		ccri.setReportState("3");
		try {
			dao.update(ccri);
		}catch (RuntimeException e) {
			LOGGER.error("更新报送单状态报错");
			e.printStackTrace();
			throw new RuntimeException("更新报送单状态报错");
		}
	}
}
