package com.sinosoft.zcfz.service.impl.reportdataimport;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dao.reportdataimport.ItemDataManualDao;
import com.sinosoft.zcfz.dto.reportdataimport.ItemDataManualDTO;
import com.sinosoft.entity.CfReportData;
import com.sinosoft.entity.CfReportDataId;
import com.sinosoft.entity.CfReportItemCodeDesc;
import com.sinosoft.zcfz.service.reportdataimport.ItemDataManualService;

@Service
public class ItemDataManualImpl implements ItemDataManualService{
	@Resource
	private    ItemDataManualDao   itemdatamanualdao; 
	@Override
	public Page<?> search(int page, int rows, ItemDataManualDTO dto) {
		StringBuilder  sql_reportItemValue = null;
		StringBuilder  sql_textValue = null;
		StringBuilder  sql_month = null;
		StringBuilder  sql_quarter = null;
		StringBuilder  sql_outitemcode = null;
		StringBuilder  sql_comCode= null;
		StringBuilder  sql_reportRate = null;
			
		try {
		  sql_reportItemValue=new StringBuilder("select da.reportitemvalue "
				+ " from cfreportdata da "
				+ " where da.outitemcode=de.reportitemcode "
				+ " and  da.month like '%"+dto.getMonth().trim()+"%' "
				+ " and  da.quarter='"+dto.getQuarter().trim()+"'  "
				+ " and  da.reportrate='"+dto.getReportRate().trim()+"'");
		  sql_textValue=new StringBuilder("select da.textvalue "
				+ " from cfreportdata da "
				+ " where da.outitemcode=de.reportitemcode "
				+ " and  da.month like '%"+dto.getMonth().trim()+"%' "
				+ " and  da.quarter='"+dto.getQuarter().trim()+"'  "
				+ " and  da.reportrate='"+dto.getReportRate().trim()+"'");
		  sql_month=new StringBuilder("select da.month "
				+ " from cfreportdata da "
				+ " where da.outitemcode=de.reportitemcode "
				+ " and  da.month like '%"+dto.getMonth().trim()+"%' "
				+ " and  da.quarter='"+dto.getQuarter().trim()+"'  "
				+ " and  da.reportrate='"+dto.getReportRate().trim()+"'");
		  sql_quarter=new StringBuilder("select da.quarter "
				+ " from cfreportdata da "
				+ " where da.outitemcode=de.reportitemcode "
				+ " and  da.month like '%"+dto.getMonth().trim()+"%' "
				+ " and  da.quarter='"+dto.getQuarter().trim()+"'  "
				+ " and  da.reportrate='"+dto.getReportRate().trim()+"'");
		  sql_outitemcode=new StringBuilder("select da.outitemcode "
				+ " from cfreportdata da "
				+ " where da.outitemcode=de.reportitemcode "
				+ " and  da.month like '%"+dto.getMonth().trim()+"%' "
				+ " and  da.quarter='"+dto.getQuarter().trim()+"'  "
				+ " and  da.reportrate='"+dto.getReportRate().trim()+"'");
		  sql_comCode=new StringBuilder("select da.comcode "
				+ " from cfreportdata da "
				+ " where da.outitemcode=de.reportitemcode "
				+ " and  da.month like '%"+dto.getMonth().trim()+"%' "
				+ " and  da.quarter='"+dto.getQuarter().trim()+"'  "
				+ " and  da.reportrate='"+dto.getReportRate().trim()+"'");
		  sql_reportRate=new StringBuilder("select da.reportrate "
				+ " from cfreportdata da "
				+ " where da.outitemcode=de.reportitemcode "
				+ " and  da.month like '%"+dto.getMonth().trim()+"%' "
				+ " and  da.quarter='"+dto.getQuarter().trim()+"'  "
				+ " and  da.reportrate='"+dto.getReportRate().trim()+"'");
		} catch (Exception e) {
		System.out.println("问题："+e.getMessage());
		System.out.println("e.getCause()："+e.getCause());
                return  null;			
		
		}
		if(!(dto.getReportType()==null||"".equals(dto.getReportType()))){
	    	sql_reportItemValue.append(" and  da.reporttype='"+dto.getReportType().trim()+"'");
	    	sql_textValue.append(" and  da.reporttype='"+dto.getReportType().trim()+"'");
	    	sql_month.append(" and  da.reporttype='"+dto.getReportType().trim()+"'");
	    	sql_quarter.append(" and  da.reporttype='"+dto.getReportType().trim()+"'");
	    	sql_outitemcode.append(" and  da.reporttype='"+dto.getReportType().trim()+"'");
	    	sql_comCode.append(" and  da.reporttype='"+dto.getReportType().trim()+"'");
	    	sql_reportRate.append(" and  da.reporttype='"+dto.getReportType().trim()+"'");
		}
		StringBuilder querySql =new StringBuilder("select de.reportcode \"reportCode\" , des.reportname\"reportName\",  de.reportitemcode\"reportItemCode\",de.reportitemname \"reportItemName\",de.computelevel \"computElevel\", "
				+ "("+sql_reportItemValue+")\"reportItemValue\", "
				+ "("+sql_textValue+")\"textValue\", "
				+ "("+sql_month+")\"month\", "
				+ "("+sql_outitemcode+")\"outItemCode\", "
				+ "("+sql_comCode+")\"comCode\", "
				+ "("+sql_reportRate+")\"reportRate\", "
				+ "("+sql_quarter+")\"quarter\" "
				+ " from  userinfo u,cfreportdesc des,cfreportitemcodedesc de "
				+ "where  u.deptcode=des.department and des.reportcode=de.reportcode   and u.usercode='"+dto.getUserCode()+"' ");

	    if(!(dto.getReportItemCode()==null||"".equals(dto.getReportItemCode().trim()	))){
			querySql.append(" and  de.reportItemCode='"+dto.getReportItemCode().trim()+"'");
		}
	    if(!(dto.getReportName()==null||"".equals(dto.getReportName().trim()))){
			querySql.append(" and  de.reportcode='"+dto.getReportName().trim()+"'");
		}
	    
	    if(!("".equals(dto.getSortName(dto.getSort()))||"".equals(dto.getOrder())||dto.getSortName(dto.getSort())==null||dto.getOrder()==null)){
	    	querySql.append(" order by "+dto.getSortName(dto.getSort())+" "+dto.getOrder());
	    }
		
            return itemdatamanualdao.queryByPage(querySql.toString(),page,rows);
		
	}
	@Override
	@Transactional
	public boolean add(ItemDataManualDTO dto) {
		// 获取CfReportItemCodeDesc 表 reportItemCode 某一条记录的对象
		String   reportItemCode=dto.getRid();
		CfReportItemCodeDesc de=itemdatamanualdao.get(CfReportItemCodeDesc.class, reportItemCode);
		// 当前时间
		Date     date=new  Date();
		SimpleDateFormat      sdf=new    SimpleDateFormat("yyyy-MM-dd  hh:MM:ss");
		// 保存CfReportData 数据
		CfReportData      data=new       CfReportData();
		CfReportDataId    dataId=new     CfReportDataId();
		
		String   month=dto.getMonth().trim();
		String   quarter=dto.getQuarter().trim();
		if("1".equals(quarter)){
			data.setMonth(month+"03");
		}
		if("2".equals(quarter)){
			data.setMonth(month+"06");
		}
		if("3".equals(quarter)){
			data.setMonth(month+"09");
		}
		if("4".equals(quarter)){
			data.setMonth(month+"12");
		}
		data.setQuarter(quarter);
		dataId.setOutItemCode(dto.getReportItemCode().trim());
		data.setReportRate(dto.getReportRate().trim());
		data.setId(dataId);
		data.setComCode("000000");
		data.setComputeLevel(de.getComputeLevel());
		data.setOperator(dto.getUserCode().trim());
		data.setOperDate(sdf.format(date));
		data.setReportItemValue(dto.getReportItemValue());
		data.setTextValue(dto.getTextValue().trim());
		itemdatamanualdao.save(data);
		return false;
	}
	@Override
	@Transactional
	public void eidtor(CfReportDataId id, ItemDataManualDTO dto) {
		CfReportData data=itemdatamanualdao.get(CfReportData.class, id);
		// 当前时间
		Date     date=new  Date();
		SimpleDateFormat   sdf=new   SimpleDateFormat("yyyy-MM-dd  hh:MM:ss");
		data.setReportItemValue(dto.getReportItemValue());
		data.setTextValue(dto.getTextValue().trim());
		data.setOperator(dto.getUserCode());
		data.setOperDate(sdf.format(date));
		itemdatamanualdao.update(data);
	}
	
	
	
}
