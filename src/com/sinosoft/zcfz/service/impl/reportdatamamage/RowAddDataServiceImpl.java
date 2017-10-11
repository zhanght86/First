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
import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dao.reportdatamamage.RowAddDataDao;
import com.sinosoft.zcfz.dto.reportdatamamage.RowAddDTO;
import com.sinosoft.entity.CfReportRowAddData;
import com.sinosoft.entity.CfReportRowAddDataId;
import com.sinosoft.entity.RowAddHistory;
import com.sinosoft.zcfz.service.reportdatacheck.impl.DownloadCheckServiceImpl;
import com.sinosoft.zcfz.service.reportdatamamage.RowAddDataService;
import com.sinosoft.util.ExcelUtil;
@Service
public class RowAddDataServiceImpl implements RowAddDataService{
	@Resource
	private    RowAddDataDao   rowadddao;
	RowAddDTO rowadd=new RowAddDTO();
	private Log log  = LogFactory.getLog(DownloadCheckServiceImpl.class);

	@Override                                                                                                                                        
	public Page<?> qryIndexInfo(int page, int rows, RowAddDTO dto) {
		
		StringBuilder  querySql =new StringBuilder("select  (select  codename from  CFCODEMANAGE where  codetype='reporttype' and  codecode=da.reportrate) \"codeName\",(select codename  from CFCODEMANAGE  where codetype = 'reportname'and codecode = da.reporttype) \"reportType\",da.tableCode \"tableCode\",de.tableName \"tableName\",da.yearMonth \"yearMonth\",decode( da.quarter,1,'第一季度',2,'第二季度',3,'第三季度','第四季度')\"quarter\",da.reportRate \"reportRate\",  da.colCode \"colCode\",de.colName \"colName\" , da.rowNo \"rowNo\",da.numValue \"numValue\",da.textValue \"textValue\" from  CfReportRowAddData da,CfReportRowAddDesc de where da.tableCode=de.tableCode  and da.colCode=de.colCode ");
		
			    if(!("".equals(dto.getYearMonth())||dto.getYearMonth()==null)){
		    	    querySql.append("and  da.yearmonth like '%"+dto.getYearMonth().trim()+"%'");
			    }
			    if(!("".equals(dto.getQuarter())||dto.getQuarter()==null)){
		    	    querySql.append("and  da.quarter='"+dto.getQuarter().trim()+"'");
			    }
			    if(!("".equals(dto.getReportRate())||dto.getReportRate()==null)){
		    	    querySql.append("and  da.reportRate='"+dto.getReportRate().trim()+"'");
			    }
			    if(!(dto.getReportType()==null||"".equals(dto.getReportType()))){
					querySql.append("and  da.reportType='"+dto.getReportType().trim()+"'");
				}
			    if(!(dto.getTableCode()==null||"".equals(dto.getTableCode()))){
					querySql.append("and  da.tableCode like '%"+dto.getTableCode().trim()+"%'");
				}
			    
			    if(!(dto.getTableName() ==null||"".equals(dto.getTableName()))){
					querySql.append("and  de.tableName like '%"+dto.getTableName().trim()+"%'");
				}
			    if(!(dto.getColCode()==null||"".equals(dto.getColCode()))){
					querySql.append("and  da.colCode like '%"+dto.getColCode().trim()+"%'");
				}
			    if(!(dto.getColName()==null||"".equals(dto.getColName()))){
					querySql.append("and  de.colName like '%"+dto.getColName().trim()+"%'");
				}
			    if(!("".equals(dto.getSortName(dto.getSort()))||"".equals(dto.getOrder())||dto.getSortName(dto.getSort())==null||dto.getOrder()==null)){
			    	querySql.append(" order by "+dto.getSortName(dto.getSort())+" "+dto.getOrder());
			    }
		
		             return rowadddao.queryByPage(querySql.toString(),page,rows);
	}

	@Override
	@Transactional  // 修改+保存修改轨迹
	public void update(CfReportRowAddDataId id, RowAddDTO dto) {
		Date                time = new  Date();  
		SimpleDateFormat    sdf=new  SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
		//   修改 行可扩展固定因子
		CfReportRowAddData   rowadd = rowadddao.get(CfReportRowAddData.class, id);
		if (dto.getNumValue_after()!=null&&!dto.getNumValue_after().equals("")) {
			rowadd.setNumValue(dto.getNumValue_after());
		}
		if (dto.getTextValue_after()!=null&&!dto.getTextValue_after().equals("")) {
			rowadd.setTextValue(dto.getTextValue_after());
		}
		rowadd.setOperator2(dto.getUserCode());
		rowadd.setOperDate2(sdf.format(time));
		System.out.println("修改人"+dto.getUserCode());
		rowadddao.update(rowadd);
		
		//  保存修改历史记录
		
		RowAddHistory       history = new  RowAddHistory();
	    history.setTable_code(rowadd.getTableCode());
	    history.setTable_name(dto.getTableName().trim());
	    history.setCol_code(rowadd.getId().getColCode());
	    history.setCol_name(dto.getColName().trim());
	    history.setRow_no(rowadd.getId().getRowNo());
	    history.setYear_month(rowadd.getYearMonth());
	    history.setQuarter(rowadd.getQuarter());
	    history.setReport_rate(rowadd.getReportRate());
	    history.setReport_mvalue(dto.getNumValue());
	    history.setReport_mvalue_after(dto.getNumValue_after());
	    history.setText_value(dto.getTextValue());
	    history.setText_value_after(dto.getTextValue_after());
	    history.setOper_date(sdf.format(time));
	    history.setUser_name(dto.getUserCode());
	    rowadddao.save(history);
	}
	 @Override  //  历史轨迹查询
		public Page<?> rowAddHistory(int page, int rows, RowAddDTO dto) {
		 
		 String        sq="select  codename from  CFCODEMANAGE where  codetype='reporttype' and  codecode=his.report_rate";
		 StringBuilder   sql=new   StringBuilder("select ("+sq+")  \"report_rate\" , his.table_code  \"table_code\", his.table_name  \"table_name\" , his.col_code  \"col_code\" , his.col_name  \"col_name\" ,his.row_no  \"row_no\" ,  his.report_mvalue \"report_mvalue\" , his.report_mvalue_after  \"report_mvalue_after\" ,his.text_value \"text_value\" ,his.text_value_after \"text_value_after\"  , his.oper_date \"oper_date\" ,u.username \"user_name\"  from  CfReportRowAddData da,RowAddHistory his,USERINFO u  where his.table_code=da.tablecode  and his.col_Code=da.colCode and  da.rowno=his.row_no and da.yearmonth=his.year_month and da.quarter=his.quarter and da.reportrate=his.report_rate and u.usercode=his.user_name");
		 
					if(!("".equals(dto.getYearMonth())||dto.getYearMonth()==null)){
			        	sql.append(" and  his.year_month like '%"+dto.getYearMonth().trim()+"%'");
			        }
					if(!("".equals(dto.getQuarter())||dto.getQuarter()==null)){
			        	sql.append(" and  his.quarter='"+dto.getQuarter().trim()+"'");
			        }
			        if(!("".equals(dto.getReportRate())||dto.getReportRate()==null)){
			        	sql.append(" and  his.report_rate='"+dto.getReportRate().trim()+"'");
			        }
					if(!("".equals(dto.getReportType())||dto.getReportType()==null)){
						sql.append(" and  da.reporttype='"+dto.getReportType().trim()+"'");
					}
					if(!("".equals(dto.getTableCode())||dto.getTableCode()==null)){
						sql.append(" and  his.table_code='"+dto.getTableCode()+"'");
					}
					if(!(dto.getColCode()==null||"".equals(dto.getColCode()))){
						sql.append(" and  his.col_Code like '%"+dto.getColCode().trim()+"%'");
					}
				    if(!(dto.getColName()==null||"".equals(dto.getColName()))){
						sql.append(" and  his.col_Name like '%"+dto.getColName().trim()+"%'");
					}
				    if(!("".equals(dto.getSort())||"".equals(dto.getOrder())||dto.getSort()==null||dto.getOrder()==null)){
					   sql.append(" order by "+dto.getSort()+" "+dto.getOrder());
				    }
			
			
			          return rowadddao.queryByPage(sql.toString(),page,rows);
		}

	//行可扩展数据管理导出
		@Override
		public void downloadLine(HttpServletRequest request, HttpServletResponse response, String name,
				String queryConditions, String cols) {
			// TODO Auto-generated method stub
			ExcelUtil excelUtil = new ExcelUtil();
			//1.查询数据sql
			String sql ="select  (select  codename from  CFCODEMANAGE where  codetype='reporttype' and  codecode=da.reportrate) \"codeName\",(select codename  from CFCODEMANAGE  where codetype = 'reportname'and codecode = da.reporttype) \"reportType\",da.tableCode \"tableCode\",de.tableName \"tableName\",da.yearMonth \"yearMonth\",decode( da.quarter,1,'第一季度',2,'第二季度',3,'第三季度','第四季度')\"quarter\",da.reportRate \"reportRate\",  da.colCode \"colCode\",de.colName \"colName\" , da.rowNo \"rowNo\",da.numValue \"numValue\",da.textValue \"textValue\" from  CfReportRowAddData da,CfReportRowAddDesc de where da.tableCode=de.tableCode  and da.colCode=de.colCode ";
			try {
				//2.将查询条件转换成DTO
				rowadd = new ObjectMapper().readValue(queryConditions.replaceAll("&quot;", "\""), RowAddDTO.class);
				List<?> jsonList = rowadddao.queryBysql(spliceQuerySql(rowadd, sql));
				String datas = new ObjectMapper().writeValueAsString(jsonList);
				//3. 根据条件查询导出数据集
				excelUtil.export(request, response, name, cols, datas);
			} catch (Exception e) {
				log.error("导出失败，请查看具体原因！");
				e.printStackTrace();
			}
		}

		public String spliceQuerySql(RowAddDTO dto, String sql) {
			StringBuilder querySql = new StringBuilder(sql);
			 if(!("".equals(dto.getYearMonth())||dto.getYearMonth()==null)){
		    	    querySql.append("and  da.yearmonth like '%"+dto.getYearMonth().trim()+"%'");
			    }
			    if(!("".equals(dto.getQuarter())||dto.getQuarter()==null)){
		    	    querySql.append("and  da.quarter='"+dto.getQuarter().trim()+"'");
			    }
			    if(!("".equals(dto.getReportRate())||dto.getReportRate()==null)){
		    	    querySql.append("and  da.reportRate='"+dto.getReportRate().trim()+"'");
			    }
			    if(!(dto.getReportType()==null||"".equals(dto.getReportType()))){
					querySql.append("and  da.reportType='"+dto.getReportType().trim()+"'");
				}
			    if(!(dto.getTableCode()==null||"".equals(dto.getTableCode()))){
					querySql.append("and  da.tableCode like '%"+dto.getTableCode().trim()+"%'");
				}			    
			    if(!(dto.getTableName() ==null||"".equals(dto.getTableName()))){
					querySql.append("and  de.tableName like '%"+dto.getTableName().trim()+"%'");
				}
			    if(!(dto.getColCode()==null||"".equals(dto.getColCode()))){
					querySql.append("and  da.colCode like '%"+dto.getColCode().trim()+"%'");
				}
			    if(!(dto.getColName()==null||"".equals(dto.getColName()))){
					querySql.append("and  de.colName like '%"+dto.getColName().trim()+"%'");
				}
			    if(!("".equals(dto.getSortName(dto.getSort()))||"".equals(dto.getOrder())||dto.getSortName(dto.getSort())==null||dto.getOrder()==null)){
			    	querySql.append(" order by "+dto.getSortName(dto.getSort())+" "+dto.getOrder());
			    }
			//querySql.append(" and da.yearMonth='"+rowadd.getYearMonth()+"' and da.quarter='"+rowadd.getQuarter()+"' order by da.colCode");
			return querySql.toString();
		}
	
	

	

	
	
	

}
