package com.sinosoft.zcfz.service.glnlpg.impl;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sinosoft.common.CurrentUser;
import com.sinosoft.common.Dao;
import com.sinosoft.common.Page;
import com.sinosoft.entity.CalCfReportInfo;
import com.sinosoft.entity.UserInfo;
import com.sinosoft.util.Config;
import com.sinosoft.util.EmailUtil;
import com.sinosoft.zcfz.dao.glnlpg.Alm_ItemDefineDao;
import com.sinosoft.zcfz.dao.glnlpg.Alm_ReportDataDao;
import com.sinosoft.zcfz.dao.glnlpg.Alm_ReportInfoDao;
import com.sinosoft.zcfz.dao.glnlpg.Alm_SevBaseDao;
import com.sinosoft.zcfz.dto.glnlpg.DataImportDTO;
import com.sinosoft.zcfz.dto.glnlpg.Alm_ItemDefineDTO;
import com.sinosoft.zcfz.dto.glnlpg.Alm_ReportDataDTO;
import com.sinosoft.zcfz.dto.glnlpg.Alm_ReportInfoDTO;
import com.sinosoft.zcfz.dto.glnlpg.Alm_SevBaseDTO;
import com.sinosoft.zcfz.dto.reportformcompute.CalCfReportInfoDTO;
import com.sinosoft.zcfz.entity.Alm_ItemDefine;
import com.sinosoft.zcfz.entity.Alm_ReportData;
import com.sinosoft.zcfz.entity.Alm_ReportDataId;
import com.sinosoft.zcfz.entity.Alm_ReportInfo;
import com.sinosoft.zcfz.entity.Alm_SevBase;
import com.sinosoft.zcfz.service.glnlpg.Alm_ItemDefineService;

@Service
public class Alm_ItemDefineServiceImp implements Alm_ItemDefineService {
	private static final Logger LOGGER = LoggerFactory.getLogger(Alm_ItemDefineServiceImp.class);
	@Resource
	private Alm_ItemDefineDao sev_ItemDefineDao;
	@Resource
	private Alm_ReportDataDao sev_ReportDataDao;
	@Resource
	private Alm_ReportInfoDao sev_ReportInfoDao;
	@Resource
	private Alm_SevBaseDao sev_SevBaseDao;
	@Resource
	private Dao dao;
	@Transactional
	public Page<?> qrySev_ItemDefineOfPage(Alm_ItemDefineDTO sid,int page,int rows){
		try{
			
			StringBuilder querySql = new StringBuilder("select ItemCode itemCode,cm.CODENAME projectCode,ItemNum itemNum,ItemName itemName,StandardScore standardScore,SysIntegrityScore sysIntegrityScore,");
			querySql.append("SysIntegrityWeight sysIntegrityWeight,FolEfficiencyScore folEfficiencyScore,FolEfficiencyWeight folEfficiencyWeight,sevid.deptno deptNo,DeptWeight deptWeight,sevid.temp temp");
			querySql.append(" from alm_itemdefine sevid left join cfcodemanage cm on sevid.ProjectCode = cm.CODECODE where sevid.itemtype in ('0','3') ");
			if(sid.getProjectCode()!=null&&!sid.getProjectCode().equals("")){
				querySql.append(" and sevid.projectcode='"+sid.getProjectCode()+"'");
			}
			if(sid.getItemNum()!=null&&!sid.getItemNum().equals("")){
				querySql.append(" and sevid.itemnum='"+sid.getItemNum()+"'");
			}
			querySql.append(" order by sevid.itemcode");
			return sev_ItemDefineDao.queryByPage(querySql.toString(), page, rows,Alm_ItemDefineDTO.class);
		}catch(Exception e){
			LOGGER.error("页面查询失败", e);
			return null;
		}
	}
	@Transactional
	public void updateManage(Alm_ItemDefineDTO sid) {		
		Alm_ItemDefine s = sev_ItemDefineDao.get(Alm_ItemDefine.class,sid.getItemCode());
		System.out.println(s);
		s.setDeptNo(sid.getDeptNo());
		s.setTemp(sid.getTemp());
		String[] deptNo = sid.getDeptNo().split(",");
		String[] deptWeight = sid.getDeptWeight().split(",");
		if(deptNo.length!=deptWeight.length){
			throw new  RuntimeException("权重个数与部门个数不匹配");
		}
		BigDecimal dou=BigDecimal.valueOf(0);	
		for(int i=0;i<deptWeight.length;i++){
			dou=dou.add(BigDecimal.valueOf(Double.parseDouble(deptWeight[i])));
		}
		System.out.println(dou.doubleValue());
		if(dou.doubleValue()==1){
			s.setDeptWeight(sid.getDeptWeight());
			sev_ItemDefineDao.update(s);
		}else{
			throw new  RuntimeException("权重和不为1，请重新填写");
		}

	}
	@Transactional
	public boolean saveCalCfReportInfo(CalCfReportInfoDTO dto) {
		CalCfReportInfo br=new CalCfReportInfo();
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
		dto.setReportCateGory("1");
		String reportId=dto.getYear()+"_"+dto.getQuarter()+"_"+dto.getReportType()+"_"+ dto.getReportCateGory()+"_"+strDate+"_"+comCode;

	    boolean flag=false;//false表示数据库中没有所填的id，true表示数据库中存在id
	    if(sev_ItemDefineDao.get(CalCfReportInfo.class,reportId)==null){
	    	br.setReportId(reportId);
		    br.setYear(dto.getYear());
		    br.setQuarter(dto.getQuarter());
		    br.setDataDate(strDate);
		    //默认设置为2 表示季度报告
		    br.setReportType("2");
		    //默认设置为1 表示资产负债管理能力评估
		    br.setReportCateGory("1");
		    br.setReportState("0");
		    br.setHandleFlag("0");
		    br.setFlag("1");
		    br.setComCode(dto.getComCode());
		    sev_ItemDefineDao.save(br);
	    }else{
	    	flag=true;
	    }
	    return flag;
	}
	//主要是数据操作 需要把定义表的内容拆到数据表里 然后在报单表里创建新一期报单号
	@SuppressWarnings("unchecked")
	@Transactional
	public void send(CalCfReportInfoDTO sid){		
		try{
			String sql="select t.itemcode,t.projectcode,t.deptweight,t.deptno,t.itemtype,t.temp from alm_itemdefine t  where t.itemType in ('0','3')";
			List<Alm_ItemDefineDTO> list=(List<Alm_ItemDefineDTO>) sev_ItemDefineDao.queryBysql(sql,Alm_ItemDefineDTO.class);
			for(Alm_ItemDefineDTO sidd:list){
				String[] str=sidd.getDeptNo().split(",");
				String[] weight=sidd.getDeptWeight().split(",");
				for(int i=0;i<str.length;i++){
					Alm_ReportData srd=new Alm_ReportData();
					Alm_ReportDataId srid=new Alm_ReportDataId(sid.getReportId(),sidd.getItemCode(),str[i]);
					srd.setId(srid);
					srd.setProjectCode(sidd.getProjectCode());
					//获取部门的权重 利用字符串切割分别获取 
					srd.setDeptWeight(BigDecimal.valueOf(Double.parseDouble(weight[i])));
					srd.setItemType(sidd.getItemType());
					srd.setSevStatus("es1");
					if(sidd.getTemp()!=null){
						srd.setTemp(sidd.getTemp());
					}
					sev_ReportDataDao.save(srd);
				}
			}
			//修改单号状态
			CalCfReportInfo cal = sev_ReportDataDao.get(CalCfReportInfo.class, sid.getReportId());
			cal.setFlag("2");
			sev_ReportDataDao.update(cal);
			
			//把sev_sevbase复制一份给新的一期
			String file="select t.reportId from cal_cfreportinfo t where t.reportCateGory='1' order by t.reportid desc";
			List<CalCfReportInfoDTO> filelist=(List<CalCfReportInfoDTO>) sev_ReportInfoDao.queryBysql(file,CalCfReportInfoDTO.class);
			if(filelist.size()!=0){
				String sevbase="select * from alm_sevbase s where s.type in ('1','3') and s.reportid='"+filelist.get(0).getReportId()+"' ";
				List<Alm_SevBase> baselist=(List<Alm_SevBase>) sev_ItemDefineDao.queryBysql(sevbase,Alm_SevBase.class);
				for(Alm_SevBase sidd:baselist){
					Alm_SevBase ssb=new Alm_SevBase();
					ssb.setReportId(sid.getReportId());
					ssb.setItemCode(sidd.getItemCode());
					ssb.setFileName(sidd.getFileName());
					ssb.setFilePath(sidd.getFilePath());
					ssb.setUploadTime(sidd.getUploadTime());
					ssb.setDeptNo(sidd.getDeptNo());
					ssb.setNewFileName(sidd.getNewFileName());
					ssb.setUploadUser(sidd.getUploadUser());
					ssb.setCompanyCode(sidd.getCompanyCode());
					ssb.setType(sidd.getType());
					sev_ReportDataDao.save(ssb);
				}
			}
		
//			//发送邮件提醒填写人进入系统进行填写
//			String dept = deptNo();
//			List<String> recipients=new ArrayList<String>();
//			String sql1="select u.email as deptNo from userinfo u left join act_id_membership a on a.user_id_=u.id where u.deptcode in ('"+dept+"') and a.group_id_='bmcontact' or a.group_id_='riskcontact'";
//			List<Alm_ReportDataDTO> srd=(List<Alm_ReportDataDTO>) sev_ReportDataDao.queryBysql(sql1,Alm_ReportDataDTO.class);
//			for(Alm_ReportDataDTO srdd:srd){
//				recipients.add(srdd.getDeptNo());
//			}
//			String year=cal.getYear();
//			System.out.println(recipients);
//			String url = Config.getProperty("sysUrl");
//			EmailUtil eu=new EmailUtil();
//			StringBuffer message = new StringBuffer("");
//			String subject = "11号文自评估";	
//			message.append("<html>"+"\n"+"<table align='center' cellpadding='3' cellspacing='0'>");			
//			message.append("<tr><td align='center' width='1000' colspan=5><h3>"+year+"年度风险管理自评估启动通知</h3></td></tr>"+"\n");			
//			message.append("<tr><td colspan=5 width='1000'>部门联系人：</td></tr>"+"\n");
//			message.append("<tr><td colspan=5 width='1000'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您有需填写的自评估。</td></tr>"+"\n");
//			message.append("<tr><td colspan=5 width='1000'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+sid.getEmail()+"</td></tr>"+"\n");	
//			message.append("<tr><td colspan=5 width='1000'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请点击如下链接，登录风险管理系统进行填写。</td></tr>"+"\n");
//			message.append("<tr><td colspan=5 width='1000'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='"+url+"?emailType=5'>"+url+"</a></td></tr>"+"\n");
//			message.append("</table>"+"\n"+"</html>");
//			//eu.sendHtmlMail(recipients,subject,message.toString());
		}catch(Exception e){
			LOGGER.error("需求发送失败", e);
		}
	}
	@Transactional
	public String get(){
		String sql="select * from sev_reportinfo t order by t.reportid desc";
		List<Alm_ReportInfoDTO> list=(List<Alm_ReportInfoDTO>) sev_ReportInfoDao.queryBysql(sql, Alm_ReportInfoDTO.class);
		String sql1="select t.reportId from sev_reportdata t where  t.sevstatus!='es1' and t.reportid='"+list.get(0).getReportId()+"'";
		List<Alm_ReportDataDTO> data=(List<Alm_ReportDataDTO>) sev_ReportInfoDao.queryBysql(sql1, Alm_ReportDataDTO.class);
		if(list.get(0).getFlag().equals("0")&&data.size()==0){
			return "ok";
		}else if(list.get(0).getFlag()!=0){
			return "ok";
		}else{
			return null;
		}
	}
	@Transactional
	public Page<?> listAll(int page, int rows) {
		String list = deptNo();
		//System.out.println(list);
		String sql = " select COMNAME deptNo from branchinfo where COMCODE in('"+list+"')";
		return sev_ItemDefineDao.queryByPage(sql, page, rows,Alm_ItemDefineDTO.class);
	}
	//目的是获取到所属部门的字符串 通过修改可以用到sql语句中的in中
	@Transactional
	public String deptNo() {
		String sql = " select DeptNo from sev_itemdefine ";
		//List<Sev_ItemDefine> list= (List<Sev_ItemDefine>) sev_ItemDefineDao.queryByhsql(sql);
		List<?> list= sev_ItemDefineDao.queryBysql(sql);
		StringBuffer strBuffer = new StringBuffer();
		for(int i=0;i<list.size();i++){
			//System.out.println(list.get(i).toString());
			String str =list.get(i).toString().substring(8,list.get(i).toString().length()-1);
			//System.out.println(str);
			strBuffer.append(str);
			strBuffer.append(',');
		}
		//System.out.println(strBuffer);
		return strBuffer.substring(0, strBuffer.length()-1).replaceAll(",","','");
		
	}
	@Transactional
	public String downloadHTTP(long id,HttpServletRequest request,HttpServletResponse response) {
		try{
			System.out.println(id);
			Alm_SevBase ss=sev_SevBaseDao.get(Alm_SevBase.class, id);
			String newFileName =ss.getNewFileName();
			String path=Config.getProperty("descripfile")+'/'+URLEncoder.encode(newFileName, "utf-8");
			String downloadPath = request.getScheme() + "://" + request.getServerName()
					+ ":" + request.getServerPort()
					+ request.getContextPath() 
					+path;
			System.out.println(downloadPath);
			return downloadPath;
		}catch(Exception e){
			LOGGER.error("评估说明下载失败", e);	
			return null;
		}
	}
	@Transactional
	public void saveFile(DataImportDTO dto) {
		try{
			Alm_SevBase ssb=new Alm_SevBase();
			System.out.println(ssb);
			ssb.setDeptNo(dto.getDeptNo());
		    ssb.setNewFileName(dto.getNewfileName());
		    ssb.setFileName(dto.getFileName());
		    ssb.setFilePath(dto.getFilePath());	    
		    ssb.setCompanyCode(dto.getComCode());
		    ssb.setUploadUser(dto.getUploadUser());
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    ssb.setUploadTime(sdf.format(new Date()));   
		    sev_SevBaseDao.save(ssb); 
		}catch(Exception e){
			LOGGER.error("评估说明文件上传记录存储失败", e);	
		}
	}
	@Transactional
	public List<?> fileList() {
		try{
			String sql="select t.id,t.filename,t.uploaduser,(select b.comname from branchinfo b where b.comcode=t.deptno) as deptNo,t.uploadtime from sev_sevbase t where t.reportid is null and t.itemcode is null ";
			System.out.println(sql);
			return sev_SevBaseDao.queryBysql(sql,Alm_SevBaseDTO.class);
		}catch(Exception e){
			LOGGER.error("评估说明文件查询失败", e);	
			return null;
		}
	}
}
