package com.sinosoft.zcfz.service.impl.reportformcompute;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sinosoft.common.CurrentUser;
import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dao.reportformcompute.CalCfReportInfoDao;
import com.sinosoft.zcfz.dto.reportformcompute.CalCfReportInfoDTO;
import com.sinosoft.entity.CalCfReportInfo;
import com.sinosoft.entity.UserInfo;
import com.sinosoft.zcfz.service.reportformcompute.CalCfReportInfoService;

@Service
public class CalCfReportInfoServiceImp implements CalCfReportInfoService{
	@Resource
	private CalCfReportInfoDao calCfReportInfoDao;
	@Transactional
	public boolean saveCalCfReportInfo(CalCfReportInfoDTO dto) {
		// TODO Auto-generated method stub
		CalCfReportInfo br=new CalCfReportInfo();
		/*SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		String reportId=dto.getYear()+"_"+dto.getQuarter()+"_"+dto.getReportType()+"_"+sdf.format(new Date());*/
		if(dto.getReportCateGory()==null||"".equals(dto.getReportCateGory())){
			dto.setReportCateGory("0");
		}
		String strDate = "";
		strDate = dto.getYear() + dto.getDataDate().replace("-", "").substring(4);
		//添加机构号
		UserInfo user=CurrentUser.CurrentUser;
		String comCode = user.getComCode();
		if(comCode==null||comCode.equals("")){
			dto.setComCode("8600100");
		}else{
			dto.setComCode(comCode);
		}
		
		String reportId=dto.getYear()+"_"+dto.getQuarter()+"_"+dto.getReportType()+"_"+ dto.getReportCateGory()+"_"+strDate+"_"+comCode;

	    boolean flag=false;//false表示数据库中没有所填的id，true表示数据库中存在id
	    if(calCfReportInfoDao.get(CalCfReportInfo.class,reportId)==null){
	    	br.setReportId(reportId);
		    br.setYear(dto.getYear());
		    br.setQuarter(dto.getQuarter());
		    br.setDataDate(strDate);
		    br.setReportType(dto.getReportType());
		    br.setReportCateGory(dto.getReportCateGory());
		    br.setReportState("0");
		    br.setHandleFlag("0");
		    br.setComCode(dto.getComCode());
			calCfReportInfoDao.save(br);
	    }else{
	    	flag=true;
	    }
	    return flag;
	}
	@Transactional
	public void updateCalCfReportInfo(String id, CalCfReportInfoDTO dto) {
		// TODO Auto-generated method stub
		CalCfReportInfo br =calCfReportInfoDao.get(CalCfReportInfo.class,id);
		
	    br.setReportId(dto.getReportId());
	    br.setYear(dto.getYear());
	    br.setQuarter(dto.getQuarter());
	    br.setDataDate(dto.getDataDate());
	    br.setReportType(dto.getReportType());
	    br.setReportState(dto.getReportState());
	    br.setReportCateGory(dto.getReportCateGory());
	    br.setHandleFlag(dto.getHandleFlag());
	    br.setRemark(dto.getRemark());
	    br.setComCode(dto.getComCode());
		calCfReportInfoDao.update(br);
	}
	@Transactional
	public void deleteCalCfReportInfo(String[] idArr) {

		calCfReportInfoDao.removes(idArr,CalCfReportInfo.class);
		String idArrs="";
		for(int i=0;i<idArr.length;i++){
			idArrs+="'"+idArr[i]+"',";
		}
		idArrs=idArrs.substring(0, idArrs.length()-1);
		String delsqlstatic="delete from cfreportdata where reportid in ("+idArrs+")";
		String delsqlextend="delete from cfreportrowadddata where reportid in ("+idArrs+")";
		calCfReportInfoDao.excute(delsqlstatic);
		calCfReportInfoDao.excute(delsqlextend);
	}
	public Page<?> qryCalCfReportInfoOfPage(int page, int rows,CalCfReportInfoDTO dto) {
		 // StringBuilder querySql =new StringBuilder("select c.*,c1.codename as quarterName,c2.codename as reportTypeName,c3.codename as reportStateName,c4.codename as reportCateGoryName from Cal_CfReportInfo c left join cfcodemanage c1 on c1.codecode=c.quarter and c1.codetype='quarter' left join cfcodemanage c2 on c2.codecode=c.reportType and c2.codetype='reporttype' left join cfcodemanage c3 on c3.codecode=c.reportState and c3.codetype='cfreportstate' left join cfcodemanage c4 on c4.codetype='reportCateGory' and c4.codecode=c.reportCateGory where 1=1");
		  StringBuilder querySql =new StringBuilder("select c.*,c1.codename as quarterName,c2.codename as reportTypeName,c3.codename as reportStateName,c4.codename as reportCateGoryName,c5.comname  as comName from Cal_CfReportInfo c left join cfcodemanage c1 on c1.codecode=c.quarter and c1.codetype='quarter' left join cfcodemanage c2 on c2.codecode=c.reportType and c2.codetype='reporttype' left join cfcodemanage c3 on c3.codecode=c.reportState and c3.codetype='cfreportstate' left join cfcodemanage c4 on c4.codetype='reportCateGory' and c4.codecode=c.reportCateGory left join branchinfo c5 on c5.comcode = c.comcode where 1=1");
		  if(!(dto.getReportId()==null||"".equals(dto.getReportId()))){
	    	    querySql.append("  and  ReportId like '%"+dto.getReportId()+"%'");
		    }
			if(!(dto.getYear()==null||"".equals(dto.getYear()))){
	    	    querySql.append("  and  Year like '%"+dto.getYear()+"%'");
		    }
			if(!(dto.getQuarter()==null||"".equals(dto.getQuarter()))){
	    	    querySql.append("  and  Quarter like '%"+dto.getQuarter()+"%'");
		    }
			if(!(dto.getReportType()==null||"".equals(dto.getReportType()))){
	    	    querySql.append("  and  ReportType like '%"+dto.getReportType()+"%'");
		    }
			if(!(dto.getReportState()==null||"".equals(dto.getReportState()))){
	    	    querySql.append("  and  c.ReportState ="+dto.getReportState()+" ");
		    }
			if(!(dto.getReportCateGory()==null||"".equals(dto.getReportCateGory()))){
				querySql.append("  and  c.reportCateGory = "+dto.getReportCateGory()+" ");
			}
			if(!(dto.getHandleFlag()==null||"".equals(dto.getHandleFlag()))){
	    	    querySql.append("  and  HandleFlag like '%"+dto.getHandleFlag()+"%'");
		    }
			if(!(dto.getRemark()==null||"".equals(dto.getRemark()))){
	    	    querySql.append("  and  Remark like '%"+dto.getRemark()+"%'");
		    }
			querySql.append(" order by year desc ,quarter desc");
		return calCfReportInfoDao.queryByPage(querySql.toString(),page,rows,CalCfReportInfoDTO.class);
	}
	public Page<?> qryCalCfReportInfoOfPage1(int page, int rows,CalCfReportInfoDTO dto) {
		   StringBuilder querySql =new StringBuilder("select c.*,c1.codename as quarterName,c2.codename as reportTypeName,c3.codename as reportStateName from Cal_CfReportInfo c left join cfcodemanage c1 on c1.codecode=c.quarter and c1.codetype='quarter' left join cfcodemanage c2 on c2.codecode=c.reportType and c2.codetype='reporttype' left join cfcodemanage c3 on c3.codecode=c.reportState and c3.codetype='cfreportstate' where 1=1");
		   if(!(dto.getReportId()==null||"".equals(dto.getReportId()))){
	    	    querySql.append("  and  ReportId like '%"+dto.getReportId()+"%'");
		    }
			if(!(dto.getYear()==null||"".equals(dto.getYear()))){
	    	    querySql.append("  and  Year like '%"+dto.getYear()+"%'");
		    }
			if(!(dto.getQuarter()==null||"".equals(dto.getQuarter()))){
	    	    querySql.append("  and  Quarter like '%"+dto.getQuarter()+"%'");
		    }
			if(!(dto.getReportType()==null||"".equals(dto.getReportType()))){
	    	    querySql.append("  and  ReportType like '%"+dto.getReportType()+"%'");
		    }
			if(!(dto.getReportState()==null||"".equals(dto.getReportState()))){
	    	    querySql.append("  and  c.ReportState ="+dto.getReportState()+"and c.ReportState <2 ");
		    }
			querySql.append(" and c.ReportState <2 ");
			if(!(dto.getHandleFlag()==null||"".equals(dto.getHandleFlag()))){
	    	    querySql.append("  and  HandleFlag like '%"+dto.getHandleFlag()+"%'");
		    }
			if(!(dto.getRemark()==null||"".equals(dto.getRemark()))){
	    	    querySql.append("  and  Remark like '%"+dto.getRemark()+"%'");
		    }
			
		return calCfReportInfoDao.queryByPage(querySql.toString(),page,rows,CalCfReportInfoDTO.class);
	}
	public Page<?> qryCalCfReportInfoOfPageByState(int page, int rows,CalCfReportInfoDTO dto,String type) {
		StringBuilder querySql =new StringBuilder("select c.*,c1.codename as quarterName,c2.codename as reportTypeName,c3.codename as reportStateName from Cal_CfReportInfo c left join cfcodemanage c1 on c1.codecode=c.quarter and c1.codetype='quarter' left join cfcodemanage c2 on c2.codecode=c.reportType and c2.codetype='reporttype' left join cfcodemanage c3 on c3.codecode=c.reportState and c3.codetype='cfreportstate' where 1=1");
		if("xbrl".equals(type)){
			querySql.append(" and c.reportState in ('5','6')");
		}else if("check".equals(type)){
			querySql.append(" and c.reportState in ('4','4.5')");
		}else if("complexcompute".equals(type)){
			querySql.append(" and c.reportState in ('2.5','2','3','4')");
		}else if("precompute".equals(type)){  //2.5初步检验有错
			querySql.append(" and c.reportState in ('1','2','2.5','3')");
		} else if("import".equals(type)){
			//0.4','财务接口数据已导入，投资未导入
			//'0.6','投资接口数据已导入，财务未导入
			querySql.append(" and c.reportState in ('0','0.4','0.6','1')");
		}
		if(!(dto.getReportId()==null||"".equals(dto.getReportId()))){
			querySql.append("  and  ReportId like '%"+dto.getReportId()+"%'");
		}
		if(!(dto.getYear()==null||"".equals(dto.getYear()))){
			querySql.append("  and  Year like '%"+dto.getYear()+"%'");
		}
		if(!(dto.getQuarter()==null||"".equals(dto.getQuarter()))){
			querySql.append("  and  Quarter like '%"+dto.getQuarter()+"%'");
		}
		if(!(dto.getReportType()==null||"".equals(dto.getReportType()))){
			querySql.append("  and  ReportType like '%"+dto.getReportType()+"%'");
		}
		if(!(dto.getReportState()==null||"".equals(dto.getReportState()))){
			querySql.append("  and  ReportState like '%"+dto.getReportState()+"%'");
		}
		if(!(dto.getHandleFlag()==null||"".equals(dto.getHandleFlag()))){
			querySql.append("  and  HandleFlag like '%"+dto.getHandleFlag()+"%'");
		}
		if(!(dto.getRemark()==null||"".equals(dto.getRemark()))){
			querySql.append("  and  Remark like '%"+dto.getRemark()+"%'");
		}
		return calCfReportInfoDao.queryByPage(querySql.toString(),page,rows,CalCfReportInfoDTO.class);
	}
	//根据报送单的类型进行查询
	public Page<?> qryCalCfReportInfoOfPageByType(int page, int rows,CalCfReportInfoDTO dto) {
		StringBuilder querySql =new StringBuilder("select c.*,c1.codename as quarterName,c2.codename as reportTypeName,c3.codename as reportStateName from Cal_CfReportInfo c left join cfcodemanage c1 on c1.codecode=c.quarter and c1.codetype='quarter' left join cfcodemanage c2 on c2.codecode=c.reportType and c2.codetype='reporttype' left join cfcodemanage c3 on c3.codecode=c.reportState and c3.codetype='cfreportstate' where 1=1");
		
		querySql.append(" and c.reporttype ='2' ");
		if(!(dto.getReportId()==null||"".equals(dto.getReportId()))){
			querySql.append("  and  ReportId like '%"+dto.getReportId()+"%'");
		}
		if(!(dto.getYear()==null||"".equals(dto.getYear()))){
			querySql.append("  and  Year like '%"+dto.getYear()+"%'");
		}
		if(!(dto.getQuarter()==null||"".equals(dto.getQuarter()))){
			querySql.append("  and  Quarter like '%"+dto.getQuarter()+"%'");
		}
		if(!(dto.getReportType()==null||"".equals(dto.getReportType()))){
			querySql.append("  and  ReportType like '%"+dto.getReportType()+"%'");
		}
		if(!(dto.getReportState()==null||"".equals(dto.getReportState()))){
			querySql.append("  and  ReportState like '%"+dto.getReportState()+"%'");
		}
		if(!(dto.getHandleFlag()==null||"".equals(dto.getHandleFlag()))){
			querySql.append("  and  HandleFlag like '%"+dto.getHandleFlag()+"%'");
		}
		if(!(dto.getRemark()==null||"".equals(dto.getRemark()))){
			querySql.append("  and  Remark like '%"+dto.getRemark()+"%'");
		}
		return calCfReportInfoDao.queryByPage(querySql.toString(),page,rows,CalCfReportInfoDTO.class);
	}
	public List<?> qryCalCfReportInfo() {
		StringBuilder querySql =new StringBuilder("select calCfReportInfo.* from Cal_CfReportInfo calCfReportInfo");
		List<?> list = calCfReportInfoDao.queryBysql(querySql.toString(),CalCfReportInfoDTO.class);
		return list;
	}
	public List<?> qryCalCfReportInfoAll() {
		List<?> list = calCfReportInfoDao.queryBysql("select calCfReportInfo.* from Cal_CfReportInfo calCfReportInfo",CalCfReportInfoDTO.class);
		return list;
	}
	public CalCfReportInfo findCalCfReportInfo(String id) {
		// TODO Auto-generated method stub
		return calCfReportInfoDao.get(CalCfReportInfo.class,id);
	}

}