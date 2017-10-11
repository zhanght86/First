package com.sinosoft.zcfz.service.glnlpg.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sinosoft.entity.CalCfReportInfo;
import com.sinosoft.util.ExcelUtil;
import com.sinosoft.zcfz.dao.glnlpg.Alm_ComputeResultDao;
import com.sinosoft.zcfz.dao.glnlpg.Alm_GatherInfoDao;
import com.sinosoft.zcfz.dao.glnlpg.Alm_ItemDefineDao;
import com.sinosoft.zcfz.dao.glnlpg.Alm_ReportDataDao;
import com.sinosoft.zcfz.dao.glnlpg.Alm_ReportInfoDao;
import com.sinosoft.zcfz.dao.glnlpg.Alm_ResultDao;
import com.sinosoft.zcfz.dto.glnlpg.Alm_ComputeResultDTO;
import com.sinosoft.zcfz.dto.glnlpg.Alm_GatherInfoDTO;
import com.sinosoft.zcfz.dto.glnlpg.Alm_ItemDefineDTO;
import com.sinosoft.zcfz.dto.glnlpg.Alm_ReportDataDTO;
import com.sinosoft.zcfz.dto.glnlpg.Alm_ReportInfoDTO;
import com.sinosoft.zcfz.entity.Alm_ComputeResult;
import com.sinosoft.zcfz.entity.Alm_ComputeResultId;
import com.sinosoft.zcfz.entity.Alm_GatherInfo;
import com.sinosoft.zcfz.entity.Alm_ReportInfo;
import com.sinosoft.zcfz.entity.Alm_Result;
import com.sinosoft.zcfz.entity.Alm_ResultId;
import com.sinosoft.zcfz.service.glnlpg.Alm_ComputeResultService;

@Service
public class Alm_ComputeResultServiceImp implements Alm_ComputeResultService {
	private static final Logger LOGGER = LoggerFactory.getLogger(Alm_ComputeResultServiceImp.class);
	@Resource
	private Alm_ComputeResultDao sev_ComputeResultDao;
	@Resource
	private Alm_ReportInfoDao sev_ReportInfoDao;
	@Resource
	private Alm_ReportDataDao sev_ReportDataDao;
	@Resource
	private Alm_ItemDefineDao  sev_ItemDefineDao;
	@Resource
	private Alm_GatherInfoDao  sev_GatherInfoDao;
	@Resource
	private Alm_ResultDao sev_ResultDao;

	//根据评估单号和项目编号来查询所有的指标明细
	@SuppressWarnings("unchecked")
	@Transactional
	public List<?> qryComputeResult(String projectCode,String reportId) {
		StringBuffer querySql = new StringBuffer();
		try{
		if(projectCode.equals("p00")){
			querySql.append("select t.*,(select c.codename from cfcodemanage c where c.codecode=t.projectcode and c.codetype='projectCode') as projectName ");
			querySql.append("from alm_gatherinfo t where t.reportid='"+reportId+"' and t.adjusttype='1' order by t.projectcode");
			System.out.println(querySql);
			List<Alm_GatherInfoDTO> list =(List<Alm_GatherInfoDTO>) sev_GatherInfoDao.queryBysql(querySql.toString(),Alm_GatherInfoDTO.class);
			list.get(list.size()-1).setProjectName("分值合计");
			return list;
		}else{
			querySql.append("select t.itemnum itemNum,t.itemname itemName,t.standardscore standardScore,t.sysintegrityscore sysIntegrityScore,");
			querySql.append("s.integritysevresult integritySevResult,s.integrityscore integrityScore,t.folefficiencyscore folEfficiencyScore,");
			querySql.append("s.efficiencysevresult efficiencySevResult,s.efficiencyscore efficiencyScore,s.subtotalscore subtotalScore,s.sevbase SevBase, ");
			querySql.append(" (select c.codename from cfcodemanage c where c.codecode=s.integritysevresult and c.codetype='EvaluateResult') as intResult, ");
			querySql.append(" (select c.codename from cfcodemanage c where c.codecode=s.efficiencysevresult and c.codetype='EvaluateResult') as effResult ");
			querySql.append(" from alm_itemdefine t ");
			querySql.append("left join (select c.integritysevresult integritySevResult, c.integrityscore integrityScore,c.itemcode itemcode,");
			querySql.append("c.efficiencysevresult efficiencySevResult,c.efficiencyscore efficiencyScore,c.subtotalscore subtotalScore,c.sevbase SevBase ");
			querySql.append("from alm_computeresult c where c.reportid='"+ reportId+"') s on t.itemcode=s.itemcode");
			querySql.append(" where t.projectcode='" + projectCode+ "'");
			querySql.append(" order by t.itemcode");	
			System.out.println(querySql);
			List<Alm_ComputeResultDTO> list= (List<Alm_ComputeResultDTO>) sev_ComputeResultDao.queryBysql(querySql.toString(),Alm_ComputeResultDTO.class);
			return list;
		}
		}catch(Exception e){
			LOGGER.error("查询失败", e);	
			return null;
		}
	}
	//获取最新的一期按部门维度报告的单号
	@SuppressWarnings("unchecked")
	@Transactional
	public String get() {
		String sql="select s.reportid reportId from  sev_reportinfo s where s.flag!=0 order by reportId desc ";
		List<Alm_ReportInfoDTO> list=(List<Alm_ReportInfoDTO>) sev_ReportInfoDao.queryBysql(sql,Alm_ReportInfoDTO.class);
		//System.out.println(list.get(0).getReportId());
		if(list.size()!=0){
			return list.get(0).getReportId();
		}else{
			return null;
		}		
	}
	//获取最新的一期按部门维度报告的年份
	@SuppressWarnings("unchecked")
	@Transactional
	public String getyear() {
		String sql="select s.year reportId from  sev_reportinfo s where s.flag!=0 order by reportId desc ";
		List<Alm_ReportInfoDTO> list=(List<Alm_ReportInfoDTO>) sev_ReportInfoDao.queryBysql(sql,Alm_ReportInfoDTO.class);
		//System.out.println(list.get(0).getReportId());
		if(list.size()!=0){
			return list.get(0).getReportId();
		}else{
			return null;
		}

	}
	
	//评估结果分析展示
		@SuppressWarnings("unchecked")
		@Transactional
		public List<?> listnew(Alm_GatherInfoDTO sg) {
			StringBuffer querySql = new StringBuffer();
			try{
				System.out.println(sg.getReportId());
				if(sg.getReportId()!=null&&sg.getAdjustType()!=null){
					querySql.append("select c.codename projectName,t.standardscore standardScore,t.integrityscore integrityScore,t.efficiencyscore efficiencyScore,t.subtotalscore subtotalScore,t.adjsubtotalscore adjSubtotalScore,t.weight weight,t.score score,to_char(round(t.rate*100,3),'990.99')||'%' as rateName "); 
					querySql.append("from sev_gatherinfo t left join codemanage c on c.codecode=t.projectcode where t.reportid='"+sg.getReportId()+"' and t.type=1 and t.adjusttype='"+sg.getAdjustType()+"' order by t.projectcode");
					System.out.println(querySql);
					List<Alm_GatherInfoDTO> list=(List<Alm_GatherInfoDTO>) sev_GatherInfoDao.queryBysql(querySql.toString(),Alm_GatherInfoDTO.class);
					if(list!=null&&!list.isEmpty()&&list.size()>0){
						list.get(list.size()-1).setProjectName("汇总");
					}
					return list;
				}else{
					String sql="select s.reportid reportId from  sev_reportinfo s where s.flag!=0 order by reportId desc ";
					List<Alm_ReportInfoDTO> list=(List<Alm_ReportInfoDTO>) sev_ReportInfoDao.queryBysql(sql,Alm_ReportInfoDTO.class);
					System.out.println(list.get(0).getReportId());
					querySql.append("select c.codename projectName,t.standardscore standardScore,t.integrityscore integrityScore,t.efficiencyscore efficiencyScore,t.subtotalscore subtotalScore,t.adjsubtotalscore adjSubtotalScore,t.weight weight,t.score score,to_char(round(t.rate*100,3),'990.99')||'%' as rateName "); 
					querySql.append("from sev_gatherinfo t left join codemanage c on c.codecode=t.projectcode where t.reportid='"+list.get(0).getReportId()+"'and t.type=1 and t.adjusttype='at1' order by t.projectcode");
					System.out.println(querySql);
					List<Alm_GatherInfoDTO> list1=(List<Alm_GatherInfoDTO>) sev_GatherInfoDao.queryBysql(querySql.toString(),Alm_GatherInfoDTO.class);
					if(list1!=null&&!list1.isEmpty()&&list1.size()>0){
						list1.get(list1.size()-1).setProjectName("汇总");
					}
					return list1;
				}
			}catch(Exception e){
				LOGGER.error("评估结果分析展示失败", e);
				return null;
			}
		}
	//部门分析维度展示
	@SuppressWarnings("unchecked")
	@Transactional
	public List<?> listdept(Alm_GatherInfoDTO sg) {
		StringBuffer querySql = new StringBuffer();
		System.out.println(sg.getReportId());
		List<Alm_GatherInfoDTO> list =  new ArrayList<Alm_GatherInfoDTO>();
		try{
			if(sg.getReportId()!=null){
				querySql.append("select b.comname projectName,t.count count,t.adjsubtotalscore adjSubtotalScore,t.standardscore standardScore,");
				querySql.append("to_char(round(t.rate*100,3),'990.99')||'%' as rateName from sev_gatherinfo t  ");
				querySql.append("left join branchinfo b on b.comcode=t.dept ");
				querySql.append("where t.type=2 and t.reportid='"+sg.getReportId()+"'");
				System.out.println(querySql);
				list=(List<Alm_GatherInfoDTO>) sev_GatherInfoDao.queryBysql(querySql.toString(),Alm_GatherInfoDTO.class);
				if(list!=null&&!list.isEmpty()&&list.size()>0){
					list.get(list.size()-1).setProjectName("汇总");
				}
				return list;
			}else{
				String sql="select s.reportid reportId from  sev_reportinfo s where s.flag!=0 order by reportId desc ";
				List<Alm_ReportInfoDTO> list1=(List<Alm_ReportInfoDTO>) sev_ReportInfoDao.queryBysql(sql,Alm_ReportInfoDTO.class);
				System.out.println(list1.get(0).getReportId());
				querySql.append("select b.comname projectName,t.count count,t.adjsubtotalscore adjSubtotalScore,t.standardscore standardScore,");
				querySql.append("to_char(round(t.rate*100,3),'990.99')||'%' as rateName from sev_gatherinfo t  ");
				querySql.append("left join branchinfo b on b.comcode=t.dept ");
				querySql.append("where t.type=2 and t.reportid='"+list1.get(0).getReportId()+"'");
				System.out.println(querySql);
				list=(List<Alm_GatherInfoDTO>) sev_GatherInfoDao.queryBysql(querySql.toString(),Alm_GatherInfoDTO.class);
				if(list!=null&&!list.isEmpty()&&list.size()>0){
					list.get(list.size()-1).setProjectName("汇总");
				}				
				return list;
			}
		}catch(Exception e){
			LOGGER.error("部门维度查询失败", e);
			return list;
		}
	}
	//判断数据完整性 评分结果有数据就算有数据 还需要判断该单号下的指标是否计算过了
	@SuppressWarnings("unchecked")
	@Transactional
	public String verify(String reportId){
		String sql="select t.subtotalScore subtotalScore,t.sevstatus sevstatus from alm_reportdata t where t.reportid='"+reportId+"'";
		List<Alm_ReportDataDTO> resultList=(List<Alm_ReportDataDTO>)sev_ReportDataDao.queryBysql(sql,Alm_ReportDataDTO.class);
		//System.out.println(resultList);
		for(Alm_ReportDataDTO sr:resultList){
			if(sr.getSubtotalScore()==null){
				throw new  RuntimeException("数据填写不完整！");
			}else if(!(sr.getSevStatus().equals("es3"))){
				throw new  RuntimeException("指标未全部提交，不允许计算！");
			}
		}
		//判断是否计算过
		String querySql="select c.flag from cal_cfreportinfo c where c.reportcategory='1' and c.reportid='"+reportId+"'";
		System.out.println(querySql);
		List<Alm_ReportInfoDTO> list =(List<Alm_ReportInfoDTO>) sev_ReportInfoDao.queryBysql(querySql,Alm_ReportInfoDTO.class);
		if(list.get(0).getFlag()!=2){
			throw new  RuntimeException("已经计算过的不允许再次计算！");
		}
		return "Y";
	}
	//进行数据计算
	@SuppressWarnings("unchecked")
	@Transactional
	public void calculation(String reportId){
		LOGGER.error("计算开始");
		//计算风险管理能力评估表 - 基础与环境
		String[] strs1={"p0106","p0107","p0108"};
		calculationItem(reportId,0,33,strs1,"p01",0.2,3);
		//计算风险管理能力评估表 - 控制与流程
		String[] strs2={"p0203","p0204","p0205"};
		calculationItem(reportId,34,59,strs2,"p02",0.4,0);
		//计算风险管理能力评估表 - 模型与工具
		String[] strs3={"p0303","p0304","p0305"};
		calculationItem(reportId,60,85,strs3,"p03",0.2,4);
		//计算风险管理能力评估表 - 绩效考核
		String[] strs4={"p0402","p0403","p0404"};
		calculationItem(reportId,86,96,strs4,"p04",0.1,3);
		//计算风险管理能力评估表 - 资产负债管理报告
		String[] strs5={"p0502","p0503","p0504"};
		calculationItem(reportId,97,104,strs5,"p05",0.1,0);
		LOGGER.error("计算结束");
		//评估分析页面数据计算
		sev_ComputeResultDao.flush();
	
		String querySql="select * from alm_gatherinfo a where a.reportid='"+reportId+"' and a.adjusttype='1'";
		List<Alm_GatherInfoDTO> scyx=(List<Alm_GatherInfoDTO>)sev_ComputeResultDao.queryBysql(querySql,Alm_GatherInfoDTO.class);
		System.out.println(scyx.size());
		BigDecimal dou1=BigDecimal.valueOf(0);//基础能力得分小计
		BigDecimal dou2=BigDecimal.valueOf(0);//提升能力得分小计
		BigDecimal dou3=BigDecimal.valueOf(0);//最终得分小计
		for(Alm_GatherInfoDTO yx:scyx){
			dou1=dou1.add(BigDecimal.valueOf(yx.getJcscore()));
			dou2=dou2.add(BigDecimal.valueOf(yx.getTsscore()));
			dou3=dou3.add(BigDecimal.valueOf(yx.getRate()));
		}
		Alm_GatherInfo sg =new Alm_GatherInfo();
		sg.setReportId(reportId);
		sg.setWeight("100%");
		sg.setJcscore(dou1);
		sg.setTsscore(dou2);
		sg.setRate(dou3);
		sg.setAdjustType("1");
		sev_GatherInfoDao.save(sg);
		
		//修改单号状态
		CalCfReportInfo cal = sev_ReportDataDao.get(CalCfReportInfo.class,reportId);
		cal.setFlag("3");
		sev_ReportDataDao.update(cal);
		System.out.println("报告单号状态修改完成");
		//复制结果表信息到结果表2去
		String sql1="select * from alm_computeresult t where  t.reportid='"+reportId+"' order by t.itemcode";
		List<Alm_ComputeResultDTO> sd=(List<Alm_ComputeResultDTO>)sev_ComputeResultDao.queryBysql(sql1,Alm_ComputeResultDTO.class);
		for(Alm_ComputeResultDTO s:sd){
			Alm_Result sr =new Alm_Result();
			Alm_ResultId srId=new Alm_ResultId(s.getReportId(),s.getItemCode());
			sr.setId(srId);
			sr.setProjectCode(s.getProjectCode());
			sr.setIntegritySevResult(s.getIntegritySevResult());
			sr.setEfficiencySevResult(s.getEfficiencySevResult());
			if(s.getIntegrityScore()!=null){
				sr.setIntegrityScore(BigDecimal.valueOf(s.getIntegrityScore()));
			}									
			if(s.getEfficiencyScore()!=null){
				sr.setEfficiencyScore(BigDecimal.valueOf(s.getEfficiencyScore()));
			}
			if(s.getSubtotalScore()!=null){
				sr.setSubtotalScore(BigDecimal.valueOf(s.getSubtotalScore()));
			}			
			sr.setItemType(s.getItemType());
			sr.setSevBase(s.getSevBase());
			sr.setSev_Total(s.getSev_Total());
			sr.setSev_Order(s.getSev_Order());
			sev_ResultDao.save(sr);
		}
		LOGGER.error("数据塞数完成");
	}
	/**
	 * 汇总计算 计算一个项目内所有指标，并把汇总信息存入结果分析表中
	 * @param reportId 单号
	 * @param begin 开始编号
	 * @param end   结束编号
	 * @param strs  汇总项编码
	 * @param projectCode 项目编码
	 * @param weight 项目权重
	 * @param score  项目提升能力标准分值
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public void calculationItem(String reportId,int begin,int end,String[] strs,String projectCode,Double weight,int score){
		try{
			String sql="select t.itemcode itemCode, count(t.itemcode) counts from alm_reportdata t  where t.reportid='"+reportId+"'group by t.itemcode order by t.itemcode";
			List<Alm_ReportDataDTO> sev=(List<Alm_ReportDataDTO>)sev_ReportDataDao.queryBysql(sql,Alm_ReportDataDTO.class);
			BigDecimal sum1=BigDecimal.valueOf(0);//统计不适用的分数
			BigDecimal sum2=BigDecimal.valueOf(0);//统计健全性所得分数
			BigDecimal sum3=BigDecimal.valueOf(0);//统计有效性所得分数
			BigDecimal sum4=BigDecimal.valueOf(0);//统计提升指标所得分数
			for (int i=begin;i<=end;i++) {
				//如果只有一个所属部门则直接把数据添加到结果表去
				if(sev.get(i).getCounts().equals("1")){
					String querySql="select * from alm_reportdata t where t.itemcode='"+sev.get(i).getItemCode()+"' and  t.reportid='"+reportId+"'";
					List<Alm_ReportDataDTO> serd=(List<Alm_ReportDataDTO>)sev_ReportDataDao.queryBysql(querySql,Alm_ReportDataDTO.class);
					//System.out.println(serd.get(0).getReportId()+"###"+serd.get(0).getItemCode());
					Alm_ComputeResultId  scId=new Alm_ComputeResultId(serd.get(0).getReportId(),serd.get(0).getItemCode());
					Alm_ComputeResult sc =new Alm_ComputeResult();
					sc.setId(scId);
					sc.setProjectCode(serd.get(0).getProjectCode());
					sc.setItemType(serd.get(0).getItemType());
					sc.setIntegritySevResult(serd.get(0).getIntegritySevResult());
					sc.setEfficiencySevResult(serd.get(0).getEfficiencySevResult());
					sc.setSevBase(serd.get(0).getSevBase());
					//如果为基础得分条目
					if(sc.getItemType()==0){
						if(serd.get(0).getIntegritySevResult()==5){
							String sql1="select t.sysintegrityscore sysIntegrityScore from alm_itemdefine t where t.itemcode='"+serd.get(0).getItemCode()+"'";
							List<Alm_ItemDefineDTO> si=(List<Alm_ItemDefineDTO>)sev_ItemDefineDao.queryBysql(sql1,Alm_ItemDefineDTO.class);
							sum1=sum1.add(BigDecimal.valueOf(si.get(0).getSysIntegrityScore()));
						}else{
							System.out.println("健全性所得分数:"+serd.get(0).getIntegrityScore());
							sum2=sum2.add(BigDecimal.valueOf(serd.get(0).getIntegrityScore()));
						}
						if(serd.get(0).getEfficiencySevResult()==5){
							String sql1="select t.folefficiencyscore folEfficiencyScore from alm_itemdefine t where t.itemcode='"+serd.get(0).getItemCode()+"'";
							List<Alm_ItemDefineDTO> si=(List<Alm_ItemDefineDTO>)sev_ItemDefineDao.queryBysql(sql1,Alm_ItemDefineDTO.class);
							sum1=sum1.add(BigDecimal.valueOf(si.get(0).getFolEfficiencyScore()));
						}else{
							System.out.println("有效性所得分数:"+serd.get(0).getEfficiencyScore());
							sum3=sum3.add(BigDecimal.valueOf(serd.get(0).getEfficiencyScore()));
						}
						sc.setIntegrityScore(BigDecimal.valueOf(serd.get(0).getIntegrityScore()));
						sc.setEfficiencyScore(BigDecimal.valueOf(serd.get(0).getEfficiencyScore()));
						sc.setSubtotalScore(BigDecimal.valueOf(serd.get(0).getSubtotalScore()));						
					}else{
						sc.setSubtotalScore(BigDecimal.valueOf(serd.get(0).getSubtotalScore()));
						sum4=sum4.add(sc.getSubtotalScore());
					}
					sev_ComputeResultDao.save(sc);
				//存多个部门需要通过计算分数查出结果
				}else{
					String querySql1="select * from alm_reportdata t where t.itemcode='"+sev.get(i).getItemCode()+"' and  t.reportid='"+reportId+"'";
					List<Alm_ReportDataDTO> serd=(List<Alm_ReportDataDTO>)sev_ReportDataDao.queryBysql(querySql1,Alm_ReportDataDTO.class);
					Alm_ComputeResult sc =new Alm_ComputeResult();
					Alm_ComputeResultId  scId=new Alm_ComputeResultId(serd.get(0).getReportId(),serd.get(0).getItemCode());
					sc.setId(scId);
					sc.setProjectCode(serd.get(0).getProjectCode());
					sc.setItemType(serd.get(0).getItemType());
					String querySql2="select t.sysintegrityscore sysIntegrityScore,t.folefficiencyscore folEfficiencyScore,t.standardScore standardScore  from alm_itemdefine t where t.itemcode='"+sev.get(i).getItemCode()+"'"; 
					List<Alm_ItemDefineDTO> seid=(List<Alm_ItemDefineDTO>)sev_ItemDefineDao.queryBysql(querySql2,Alm_ItemDefineDTO.class);
					BigDecimal num1=BigDecimal.valueOf(0);//合计健全性得分
					BigDecimal num2=BigDecimal.valueOf(0);//合计有效性得分
					StringBuffer sevBase=new StringBuffer();
					if(sc.getItemType()==0){
						for(Alm_ReportDataDTO s:serd){
							if(s.getSevBase()!=null){sevBase.append(s.getSevBase()+"_");}						
							if(s.getIntegritySevResult()==5){
								sc.setIntegritySevResult(s.getIntegritySevResult());						
							}else{
								num1=num1.add(BigDecimal.valueOf(s.getIntegrityScore()));
							}
							if(s.getEfficiencySevResult()==5){			
								sc.setEfficiencySevResult(s.getEfficiencySevResult());
							}else{
								num2=num2.add(BigDecimal.valueOf(s.getEfficiencyScore()));
							}
						}
						if(sevBase.length()!=0){sc.setSevBase(sevBase.toString().substring(0,sevBase.length()-1));}				
						//评估结果为空表示所属部门填写都不是不适用
						if(sc.getIntegritySevResult()==null){
							double num3=num1.divide(BigDecimal.valueOf(seid.get(0).getSysIntegrityScore()),4,BigDecimal.ROUND_HALF_UP).doubleValue();
							if(num3>=1||1-num3<0.01){
								sc.setIntegritySevResult(1);
								sc.setIntegrityScore(BigDecimal.valueOf(seid.get(0).getSysIntegrityScore()).multiply(BigDecimal.valueOf(1)));
							}else if(num3>=0.8&&num3<1){
								sc.setIntegritySevResult(2);
								sc.setIntegrityScore(BigDecimal.valueOf(seid.get(0).getSysIntegrityScore()).multiply(BigDecimal.valueOf(0.8)));
							}else if(num3>=0.5&&num3<0.8){
								sc.setIntegritySevResult(3);
								sc.setIntegrityScore(BigDecimal.valueOf(seid.get(0).getSysIntegrityScore()).multiply(BigDecimal.valueOf(0.5)));
							}else{
								sc.setIntegritySevResult(4);
								sc.setIntegrityScore(BigDecimal.valueOf(seid.get(0).getSysIntegrityScore()).multiply(BigDecimal.valueOf(0)));
							}
							sum2=sum2.add(sc.getIntegrityScore());
						}else{
							sum1=sum1.add(BigDecimal.valueOf(seid.get(0).getSysIntegrityScore()));
						}
						if(sc.getEfficiencySevResult()==null){
							double num4=num2.divide(BigDecimal.valueOf(seid.get(0).getFolEfficiencyScore()),4,BigDecimal.ROUND_HALF_UP).doubleValue();
							if(num4>=1||1-num4<0.01){
								sc.setEfficiencySevResult(1);
								sc.setEfficiencyScore(BigDecimal.valueOf(seid.get(0).getFolEfficiencyScore()).multiply(BigDecimal.valueOf(1)));
							}else if(num4>=0.8&&num4<1){
								sc.setEfficiencySevResult(2);
								sc.setEfficiencyScore(BigDecimal.valueOf(seid.get(0).getFolEfficiencyScore()).multiply(BigDecimal.valueOf(0.8)));
							}else if(num4>=0.5&&num4<0.8){
								sc.setEfficiencySevResult(3);
								sc.setEfficiencyScore(BigDecimal.valueOf(seid.get(0).getFolEfficiencyScore()).multiply(BigDecimal.valueOf(0.5)));
							}else{
								sc.setEfficiencySevResult(4);
								sc.setEfficiencyScore(BigDecimal.valueOf(seid.get(0).getFolEfficiencyScore()).multiply(BigDecimal.valueOf(0)));
							}
							sum3=sum3.add(sc.getEfficiencyScore());
							sc.setSubtotalScore(sc.getIntegrityScore().add(sc.getEfficiencyScore()));
						}else{
							sum1=sum1.add(BigDecimal.valueOf(seid.get(0).getFolEfficiencyScore()));
						}
					}else{
						for(Alm_ReportDataDTO s:serd){
							if(s.getSevBase()!=null){sevBase.append(s.getSevBase()+"_");}						
							if(s.getIntegritySevResult()==4){sc.setIntegritySevResult(s.getIntegritySevResult());}
							if(s.getEfficiencySevResult()==4){sc.setEfficiencySevResult(s.getEfficiencySevResult());}
						}
						if(sevBase.length()!=0){sc.setSevBase(sevBase.toString().substring(0,sevBase.length()-1));}	
						if(sc.getIntegritySevResult()==null&&sc.getEfficiencySevResult()==null){
							sc.setIntegritySevResult(1);
							sc.setEfficiencySevResult(1);
							sc.setSubtotalScore(BigDecimal.valueOf(seid.get(0).getStandardScore()));
							sum4=sum4.add(sc.getSubtotalScore());
						}else if(sc.getIntegritySevResult()!=null&&sc.getEfficiencySevResult()==null){
							sc.setIntegritySevResult(4);
							sc.setEfficiencySevResult(1);
							sc.setSubtotalScore(BigDecimal.valueOf(0));
						}else if(sc.getIntegritySevResult()==null&&sc.getEfficiencySevResult()!=null){
							sc.setIntegritySevResult(1);
							sc.setEfficiencySevResult(4);
							sc.setSubtotalScore(BigDecimal.valueOf(0));
						}else if(sc.getIntegritySevResult()!=null&&sc.getEfficiencySevResult()!=null){
							sc.setIntegritySevResult(4);
							sc.setEfficiencySevResult(4);
							sc.setSubtotalScore(BigDecimal.valueOf(0));
						}
					}
					sev_ComputeResultDao.save(sc);
				}
				//System.out.println("不适用得分："+sum1+"健全性得分："+sum2+"有效性得分："+sum3+"提升指标得分："+sum4);
			}		
			sev_ComputeResultDao.flush();		
			System.out.println("不适用得分："+sum1+"健全性得分："+sum2+"有效性得分："+sum3+"提升指标得分："+sum4);
//			//100
//			BigDecimal uu1=BigDecimal.valueOf(100);
//			//100-不适用得分
//			BigDecimal uu2=BigDecimal.valueOf(100).subtract(sum1);
//			//调整系数=(100)/(100-不适用得分)
//			BigDecimal uu3=uu1.divide(uu2,4,BigDecimal.ROUND_HALF_UP);
//			//System.out.println("调整系数：(100)/(100-不适用得分);
//			//System.out.println("健全性调整得分：健全性得分*调整系数;
//			//System.out.println("有效性调整得分：有效性得分*调整系数;		
//			
			//对最后的三列总结项进行储存
			Alm_ComputeResultId  scId=new Alm_ComputeResultId(reportId,strs[0]);
			Alm_ComputeResult sc =new Alm_ComputeResult();
			sc.setId(scId);
			sc.setProjectCode(projectCode);
			sc.setItemType(2);
			sc.setIntegrityScore(sum2);
			sc.setEfficiencyScore(sum3);
			sc.setSubtotalScore(sum2.add(sum3));
			sev_ComputeResultDao.save(sc);
			Alm_ComputeResultId  scId1=new Alm_ComputeResultId(reportId,strs[1]);
			Alm_ComputeResult sc1 =new Alm_ComputeResult();
			sc1.setId(scId1);
			sc1.setProjectCode(projectCode);
			sc1.setItemType(2);
			sc1.setSubtotalScore(sum4);
			sev_ComputeResultDao.save(sc1);
			Alm_ComputeResultId  scId2=new Alm_ComputeResultId(reportId,strs[2]);
			Alm_ComputeResult sc2 =new Alm_ComputeResult();
			sc2.setId(scId2);
			sc2.setProjectCode(projectCode);
			sc2.setItemType(2);
			sc2.setSubtotalScore(sum1);
			sev_ComputeResultDao.save(sc2);
			
			//给结果分析表内存数据
			Alm_GatherInfo sg =new Alm_GatherInfo();
			sg.setReportId(reportId);
			sg.setProjectCode(projectCode);
			sg.setJcstandardScore(BigDecimal.valueOf(100));
			sg.setIntegrityScore(sum2);
			sg.setEfficiencyScore(sum3);
			sg.setSubtotalScore(sum2.add(sum3));
			sg.setAdjScore(sum1);
			//汇总调整后得分计算公式：(得分小计)/(100-不适用得分)*100
			//100
			BigDecimal uu1=BigDecimal.valueOf(100);
			//100-不适用得分
			BigDecimal uu2=BigDecimal.valueOf(100).subtract(sum1);
			//调整系数=(100)/(100-不适用得分)
			BigDecimal uu3=uu1.divide(uu2,4,BigDecimal.ROUND_HALF_UP);
			sg.setAdjSubtotalScore(sg.getSubtotalScore().multiply(uu3));
			if(weight==0.1){
				sg.setWeight("10%");
				sg.setJcscore(sg.getAdjSubtotalScore().multiply(BigDecimal.valueOf(0.1)));
			}else if(weight==0.2){
				sg.setWeight("20%");
				sg.setJcscore(sg.getAdjSubtotalScore().multiply(BigDecimal.valueOf(0.2)));
			}else if(weight==0.4){
				sg.setWeight("40%");
				sg.setJcscore(sg.getAdjSubtotalScore().multiply(BigDecimal.valueOf(0.4)));
			}
			sg.setTsstandardScore(BigDecimal.valueOf(score));
			sg.setTsscore(sum4);
			sg.setRate(sg.getJcscore().add(sg.getTsscore()));
			sg.setAdjustType("1");
			sev_GatherInfoDao.save(sg);
			System.out.println("该表计算完成");
		}catch(Exception e){
			LOGGER.error("数据计算失败", e);
		}
	}
	//汇总数据导出 导出一个excel里面有10个sheet
	@SuppressWarnings("unchecked")
	@Override
	public void download(HttpServletRequest request, HttpServletResponse response, String reportId) {
		try{
		ExcelUtil excelUtil = new ExcelUtil();
		StringBuffer querySql1 = new StringBuffer();
		querySql1.append("select t.integrityscore integrityScore,t.efficiencyscore efficiencyScore,t.subtotalscore subtotalScore,t.adjsubtotalscore adjSubtotalScore,t.score score "); 
		querySql1.append("from sev_gatherinfo t left join codemanage c on c.codecode=t.projectcode where t.reportid='"+reportId+"'and t.type=1 and t.adjusttype='at1' order by t.projectcode");
		System.out.println(querySql1);
		List<Alm_GatherInfoDTO> datas1 =(List<Alm_GatherInfoDTO>) sev_GatherInfoDao.queryBysql(querySql1.toString(),Alm_GatherInfoDTO.class);
		StringBuffer querySql2=new StringBuffer();
		querySql2.append("select t.itemcode,(select c.codename from codemanage c where c.codecode=t.integritysevresult and c.codetype='EvaluateResult') as intResult,");
		querySql2.append("t.integrityscore,(select c.codename from codemanage c where c.codecode=t.efficiencysevresult and c.codetype='EvaluateResult') as effResult, ");
		querySql2.append("t.efficiencyscore,t.subtotalscore,t.sevbase from sev_computeresult t where t.reportid='"+reportId+"' order by t.itemcode");
		System.out.println(querySql2);
		List<Alm_ComputeResultDTO> datas2= (List<Alm_ComputeResultDTO>) sev_ComputeResultDao.queryBysql(querySql2.toString(),Alm_ComputeResultDTO.class);
		//excelUtil.export3(request, response,datas1,datas2);
		}catch(Exception e){
			LOGGER.error("计算结果导出失败", e);	
		}
	}
}