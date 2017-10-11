package com.sinosoft.zcfz.service.impl.reportdatamamage;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sinosoft.common.CurrentUser;
import com.sinosoft.common.Dao;
import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dao.reportdatamamage.CfReportDataDao;
import com.sinosoft.zcfz.dto.reportdatamamage.CfReportDTO;
import com.sinosoft.zcfz.dto.reportdatamamage.ReportHistoryDTO;
import com.sinosoft.entity.CfReportData;
import com.sinosoft.entity.CfReportDataId;
import com.sinosoft.entity.ReportHistory;
import com.sinosoft.entity.UserInfo;
import com.sinosoft.zcfz.service.reportdatacheck.impl.DownloadCheckServiceImpl;
import com.sinosoft.zcfz.service.reportdatamamage.CfReportDataService;
import com.sinosoft.util.Config;
import com.sinosoft.util.ExcelUtil;
@Service
public class CfReportDataServiceImpl implements  CfReportDataService{
	@Resource
	private    CfReportDataDao   cfreportdatadao; 
	CfReportDTO CF=new CfReportDTO();
	private Log log  = LogFactory.getLog(DownloadCheckServiceImpl.class);
	@Resource
	private    Dao   dao; 
	@Override   // 查询                                                                                                                                                                                               
	public Page<?> qryIndexInfo(int page, int rows, CfReportDTO  dto) {
			StringBuilder querySql =new StringBuilder(" select  (select  codename from  CFCODEMANAGE where  codetype='reporttype' and  codecode=da.reportrate) \"codeName\",(select codename  from CFCODEMANAGE  where codetype = 'reportname'and codecode = da.reporttype) \"reportType\",da.reportRate \"reportRate\", de.reportcode \"sheetCode\",decode( da.quarter,1,'第一季度',2,'第二季度',3,'第三季度','第四季度')\"quarter\",da.outItemCode \"outItemCode\", da.month \"month\",da.comCode  \"comCode\",de.reportItemName \"reportItemName\",round(da.reportItemValue ,dd.decimals)\"reportItemValue\", da.textValue  \"textValue\"  from  Cfreportdata da,CfReportItemCodeDesc de left join cfreportelement dd on dd.portitemcode= de.reportitemcode where da.outitemcode=de.reportitemcode  ");	
				if(!(dto.getMonth()==null||"".equals(dto.getMonth()))){
		    	    querySql.append("  and  da.month like '%"+dto.getMonth().trim()+"%'");
			    }
				if(!(dto.getQuarter()==null||"".equals(dto.getQuarter()))){
		    	    querySql.append(" and  da.quarter='"+dto.getQuarter().trim()+"'");
			    }   
			    if(!("".equals(dto.getReportRate())||dto.getReportRate()==null)){
		    	    querySql.append(" and  da.reportrate='"+dto.getReportRate().trim()+"'");
			    }
			    if(!(dto.getReportType()==null||"".equals(dto.getReportType()))){
					querySql.append(" and  da.reporttype='"+dto.getReportType().trim()+"'");
				}
			    if(!(dto.getOutItemCode()==null||"".equals(dto.getOutItemCode()))){
					querySql.append(" and  da.outitemcode='"+dto.getOutItemCode().trim()+"'");
				}
			    if(!(dto.getReportItemName()==null||"".equals(dto.getReportItemName()))){
					querySql.append(" and  de.reportItemName like '%"+dto.getReportItemName().trim()+"%'");
				}
			    if(!(dto.getSheetCode()==null||"".equals(dto.getSheetCode()))){
					querySql.append(" and  de.reportcode like '%"+dto.getSheetCode().trim()+"%'");
				}
			    if(!(dto.getSheetName()==null||"".equals(dto.getSheetName()))){
					querySql.append(" and  de.reportcode like '%"+dto.getSheetName().trim()+"%'");
				}
			    if(!("".equals(dto.getSortName(dto.getSort()))||"".equals(dto.getOrder())||dto.getSortName(dto.getSort())==null||dto.getOrder()==null)){
			    	querySql.append(" order by "+dto.getSortName(dto.getSort())+" "+dto.getOrder());
			    }
		
		    		Page<?> queryByPage = cfreportdatadao.queryByPage(querySql.toString(),page,rows);
		    		return queryByPage;
		
	}
    @Transactional  // 修改+保存修改轨迹
	public void update(CfReportDataId id,CfReportDTO dto) {
    	Date                time=new  Date();  
		 SimpleDateFormat    sdf=new  SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
		CfReportData report=cfreportdatadao.get(CfReportData.class, id);
		if (dto.getReportItemValue_after()!=null&&!dto.getReportItemValue_after().equals("")) {
			report.setReportItemValue(dto.getReportItemValue_after());
		}
		if (dto.getTextValue_after()!=null&&!dto.getTextValue_after().equals("")) {
			report.setTextValue(dto.getTextValue_after());
		}
		report.setOperDate(sdf.format(time));
		report.setOperator(dto.getUserCode());
		System.out.println("修改时间="+dto.getUserCode());
		cfreportdatadao.update(report);
		// 保存修改轨迹
		 ReportHistory  history=new ReportHistory();
		 
		
		 history.setReport_code(report.getId().getOutItemCode());              //  因子代码
		 history.setReport_rate(report.getReportRate());;              //  报告类型
		 history.setMonth(report.getMonth());                          //  年度
		 history.setQuarter(report.getQuarter());                      //  季度
		 history.setText_value(dto.getTextValue().trim());                     //  文本类型
		 history.setText_value_after(dto.getTextValue_after());                //  文本类型 修改后
		 history.setReport_name(dto.getReportItemName().trim());               //  因子名称
		 history.setReport_mvalue(dto.getReportItemValue());                   //  数值类型
		 history.setReport_mvalue_after(dto.getReportItemValue_after());       //  数值类型修改后
		 history.setOper_date(sdf.format(time));                               //  修改时间
		 history.setUser_name(dto.getUserCode());                              //  保存修改人
		 cfreportdatadao.save(history);                            
		  
	}
    @Transactional  // 指标文本块的插入
    public void insertTextBlock(CfReportDTO dto) throws Exception {
    	UserInfo user=CurrentUser.CurrentUser;
    	Date                time=new  Date();  
    	SimpleDateFormat    sdf=new  SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
    	CfReportDataId id=new CfReportDataId();
    	id.setReportId(dto.getReportId());//baogaoleixing
    	id.setOutItemCode(dto.getOutItemCode());
    	CfReportData report=cfreportdatadao.get(CfReportData.class, id);
    	String sql="";
    	if(report!=null&&!"".equals(report)){
    		//report.setDesText(dto.getDescText());
    		sql="update CfReportData set destext=? where ReportRate=? and Month=? and OutItemCode=? and Quarter=?";
    		System.out.println(sql+"------------");
    		try{
    			dao.commonUpdate(sql,dto.getDescText(),dto.getReportRate(),dto.getMonth(),dto.getOutItemCode(),dto.getQuarter());
    		}catch(Exception e){
        			e.printStackTrace();
        			throw new Exception(e.getMessage());
        		};
    	}else{
    		CfReportData reportnew=new CfReportData();
    		reportnew.setId(id);
    		reportnew.setComCode(Config.getProperty("OrganCode"));
    		reportnew.setDesText(dto.getDescText());
    		reportnew.setMonth(dto.getMonth());
    		reportnew.setQuarter(dto.getQuarter());
    		reportnew.setReportRate(dto.getReportRate());
    		reportnew.setOperator(user.getUserCode());
    		reportnew.setOperDate(sdf.format(time));
    		reportnew.setOutItemType("02"); //数据类型
    		reportnew.setComputeLevel("0");
    		reportnew.setReportItemValueSource("0");
    		reportnew.setReportType("2");//报表类型
    		StringBuffer sb=new StringBuffer();
    		sb.append("insert into CfReportData (ReportRate,Month,OutItemCode,Quarter,ComCode,Operator,OperDate,OutItemType,ComputeLevel,ReportItemValueSource,ReportType,reportitemcode,destext,reportId) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
    		/*
    		sb.append("(");
    		sb.append("'"+reportnew.getReportRate()+"'");
    		sb.append(",");
    		sb.append("'"+reportnew.getMonth()+"'");
    		sb.append(",");
    		sb.append("'"+reportnew.getId().getOutItemCode()+"'");
    		sb.append(",");
    		sb.append("'"+reportnew.getQuarter()+"'");
    		sb.append(",");
    		sb.append("'"+reportnew.getComCode()+"'");
    		sb.append(",");
    		sb.append("'"+reportnew.getOperator()+"'");
    		sb.append(",");
    		sb.append("'"+reportnew.getOperDate()+"'");
    		sb.append(",");
    		sb.append("'"+reportnew.getOutItemType()+"'");
    		sb.append(",");
    		sb.append("'"+reportnew.getComputeLevel()+"'");
    		sb.append(",");
    		sb.append("'"+reportnew.getReportItemValueSource()+"'");
    		sb.append(",");
    		sb.append("'"+reportnew.getReportType()+"'");
    		sb.append(",");
    		sb.append("'"+reportnew.getId().getOutItemCode()+"'");
    		sb.append(",");
    		sb.append("'"+reportnew.getDesText()+"'");
    		sb.append(",");
    		sb.append("'"+reportnew.getId().getReportId()+"'");
    		sb.append(")");
    		*/
    		try{
    			//cfreportdatadao.save(reportnew);
    			System.out.println(sb.toString()+"------------");
    			//dao.excute(sb.toString());
    			dao.commonUpdate(sb.toString(),
    					reportnew.getReportRate(),
    					reportnew.getMonth(),
    					reportnew.getId().getOutItemCode(),
		        		reportnew.getQuarter(),
		        		reportnew.getComCode(),
		        		reportnew.getOperator(),
		        		reportnew.getOperDate(),
		        		reportnew.getOutItemType(),
		        		reportnew.getComputeLevel(),
		        		reportnew.getReportItemValueSource(),
		        		reportnew.getReportType(),
		        		reportnew.getId().getOutItemCode(),
		        		reportnew.getDesText(),
		        		reportnew.getId().getReportId()
    					);    		
    		}catch(Exception e){
    			e.printStackTrace();
    			throw new Exception(e.getMessage());
    		}
    	}
    }
    public String getTextBlock(CfReportDTO dto){
    	CfReportDataId id=new CfReportDataId();
    	id.setReportId(dto.getReportId());//baogaoleixing
    	id.setOutItemCode(dto.getOutItemCode());
    	CfReportData report=cfreportdatadao.get(CfReportData.class, id);
    	if(report!=null&&!"".equals(report)){
    		String destext= report.getDesText();
    		//return htmlspecialchars(destext);
    		return destext;
    	}
    	return "";
    }
    /*private String htmlspecialchars(String str) {
    	str = str.replaceAll("&", "&amp;");
    	str = str.replaceAll("<", "&lt;");
    	str = str.replaceAll(">", "&gt;");
    	str = str.replaceAll("\"", "&quot;");
    	return str;
    }*/
    //删除数据
    @Transactional  // 
    public void deleteTextBlock(CfReportDTO dto) throws Exception {
	    CfReportDataId id=new CfReportDataId();
		id.setOutItemCode(dto.getOutItemCode());
		id.setReportId(dto.getReportId());
		CfReportData report=cfreportdatadao.get(CfReportData.class, id);
		if(report!=null&&!"".equals(report)){
			try{
				cfreportdatadao.remove(report);
			}catch(Exception e){
	    			e.printStackTrace();
	    			throw new Exception(e.getMessage());
	    		};
		}else{
			//修改删除无数据文本快提示信息
			//throw new Exception("无 可删除的数据!");
			throw new Exception("无可删除数据。");
		}
    }
    @Override  //  历史轨迹查询
	public Page<?> reportHistory(int page, int rows, ReportHistoryDTO dto) {
    	
		String  sq="select  codename from  CFCODEMANAGE where  codetype='reporttype' and  codecode=his.report_rate ";

    	StringBuilder  sql=new   StringBuilder("select ("+sq+") \"report_rate\" , his.report_code  \"report_code\", his.report_name  \"report_name\" ,  his.report_mvalue \"report_mvalue\" , his.report_mvalue_after  \"report_mvalue_after\" ,his.text_value \"text_value\" ,his.text_value_after \"text_value_after\"  , his.oper_date \"oper_date\" ,u.username \"user_name\"  from  Cfreportdata da,ReportHistory his, USERINFO u  where his.report_code=da.outitemcode  and  da.reportrate=his.report_rate  and  da.month=his.month and  da.quarter=his.quarter and u.usercode=his.user_name " );
			
				if(!(dto.getMonth()==""||dto.getMonth()==null)){
		        	sql.append(" and  da.month like '%"+dto.getMonth().trim()+"%'");
		        }
				if(!(dto.getQuarter()==""||dto.getQuarter()==null)){
		        	sql.append(" and  da.quarter='"+dto.getQuarter().trim()+"'");
		        }
		        if(!(dto.getReport_rate()==""||dto.getReport_rate()==null)){
		        	sql.append(" and  his.report_rate='"+dto.getReport_rate().trim()+"'");
		        }
				if(!(dto.getReport_name()==""||dto.getReport_name()==null)){
					sql.append(" and  da.reporttype='"+dto.getReport_name().trim()+"'");
				}
				if(!(dto.getReport_code()==""||dto.getReport_code()==null)){
					sql.append(" and  his.report_code='"+dto.getReport_code().trim()+"'");
				}
				if(!(dto.getSort()==""||dto.getOrder()==""||dto.getSort()==null||dto.getOrder()==null)){
					sql.append(" order by "+dto.getSort()+" "+dto.getOrder());
			    }
		
		
		return cfreportdatadao.queryByPage(sql.toString(),page,rows);
	}
    //固定因子数据管理导出
	@Override
	public void downLoad(HttpServletRequest request, HttpServletResponse response, String name, String queryConditions,
			String cols) {
		// TODO Auto-generated method stub
		ExcelUtil excelUtil = new ExcelUtil();
		//1.查询数据sql
		String sql ="select  (select  codename from  CFCODEMANAGE where  codetype='reporttype' and  codecode=da.reportrate) \"codeName\",(select codename  from CFCODEMANAGE  where codetype = 'reportname'and codecode = da.reporttype) \"reportType\",da.reportRate \"reportRate\", de.reportcode \"sheetCode\",decode( da.quarter,1,'第一季度',2,'第二季度',3,'第三季度','第四季度')\"quarter\",da.outItemCode \"outItemCode\", da.month \"month\",da.comCode  \"comCode\",de.reportItemName \"reportItemName\",round(da.reportItemValue ,dd.decimals)\"reportItemValue\", da.textValue  \"textValue\"  from  Cfreportdata da,CfReportItemCodeDesc de left join cfreportelement dd on dd.portitemcode= de.reportitemcode where da.outitemcode=de.reportitemcode "; 
		try {
			//2.将查询条件转换成DTO
			CF = new ObjectMapper().readValue(queryConditions.replaceAll("&quot;", "\""), CfReportDTO.class);
			List<?> jsonList = cfreportdatadao.queryBysql(spliceQuerySql(CF, sql));
			String datas = new ObjectMapper().writeValueAsString(jsonList);
			//3. 根据条件查询导出数据集
			excelUtil.export(request, response, name, cols, datas);
		} catch (Exception e) {
			log.error("导出失败，请查看具体原因！");
			e.printStackTrace();
		}
		
	}
	public String spliceQuerySql(CfReportDTO dto, String sql) {
		StringBuilder querySql = new StringBuilder(sql);
		if(!(dto.getMonth()==null||"".equals(dto.getMonth()))){
    	    querySql.append("  and  da.month like '%"+dto.getMonth().trim()+"%'");
	    }
		if(!(dto.getQuarter()==null||"".equals(dto.getQuarter()))){
    	    querySql.append(" and  da.quarter='"+dto.getQuarter().trim()+"'");
	    }   
	    if(!("".equals(dto.getReportRate())||dto.getReportRate()==null)){
    	    querySql.append(" and  da.reportrate='"+dto.getReportRate().trim()+"'");
	    }
	    if(!(dto.getReportType()==null||"".equals(dto.getReportType()))){
			querySql.append(" and  da.reporttype='"+dto.getReportType().trim()+"'");
		}
	    if(!(dto.getOutItemCode()==null||"".equals(dto.getOutItemCode()))){
			querySql.append(" and  da.outitemcode='"+dto.getOutItemCode().trim()+"'");
		}
	    if(!(dto.getReportItemName()==null||"".equals(dto.getReportItemName()))){
			querySql.append(" and  de.reportItemName like '%"+dto.getReportItemName().trim()+"%'");
		}
	    if(!(dto.getSheetCode()==null||"".equals(dto.getSheetCode()))){
			querySql.append(" and  de.reportcode like '%"+dto.getSheetCode().trim()+"%'");
		}
	    if(!(dto.getSheetName()==null||"".equals(dto.getSheetName()))){
			querySql.append(" and  de.reportcode like '%"+dto.getSheetName().trim()+"%'");
		}
	    if(!("".equals(dto.getSortName(dto.getSort()))||"".equals(dto.getOrder())||dto.getSortName(dto.getSort())==null||dto.getOrder()==null)){
	    	querySql.append(" order by "+dto.getSortName(dto.getSort())+" "+dto.getOrder());
	    }
		//querySql.append(" and da.month='"+CF.getMonth()+"' and da.quarter='"+CF.getQuarter()+"' order by da.outItemCode");
		return querySql.toString();
	}
	
	
	
}
