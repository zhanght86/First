package com.sinosoft.zcfz.service.impl.reportform;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.annotation.Resource;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sinosoft.common.Constant;
import com.sinosoft.common.InvokeResult;
import com.sinosoft.common.text.DocumentException;
import com.sinosoft.common.text.PageSize;
import com.sinosoft.common.text.Paragraph;
import com.sinosoft.common.text.rtf.RtfWriter2;
import com.sinosoft.zcfz.dao.reportdataimport.CfReportDataDAO;
import com.sinosoft.zcfz.dao.reportdataimport.CfReportRowAddDataDAO;
import com.sinosoft.zcfz.dao.reportform.ExportXMLQueryDAO;
import com.sinosoft.dto.ContextDTO;
import com.sinosoft.dto.ElementDTO;
import com.sinosoft.dto.UnitDTO;
import com.sinosoft.zcfz.dto.reportform.ReportCreateInfoDTO;
import com.sinosoft.entity.BranchInfo;
import com.sinosoft.entity.CalCfReportInfo;
import com.sinosoft.entity.ElementInfo;
import com.sinosoft.entity.Unit;
import com.sinosoft.service.BranchInfoService;
import com.sinosoft.zcfz.service.reportform.ExportXbrlService;
import com.sinosoft.util.CreateZIPUtil;

@Service
public class ExportXBRLServiceImp implements ExportXbrlService{
	private String roottag =  "";// 根标签，为DATA+6位机构代码+6位年月代码（200912）		
	private String areaid = "";//  数据机构，值为12位数字，前6位是机构代码，后6位是地区代码	
	private String organcode = Constant.ORGANCODE; // 机构代码		
	private OutputFormat xmlOut = null;				
	private String YearCode = "";// 	
	private String CfReportRate = "";// 频度：1-季度快报，2-季度报告，3-临时报告
	private String reportCateGory="";// 报送报告类别1,10号文，0，偿付能力
	private String Quarter = "";// 报送季度	
	private String Month = "";// 月度	
	private String reportId; //报送号
	private String CompanyType = Constant.COMPANYTYPE;// 报送公司所属类	
	private boolean bIsChangf = false;// 01 公司偿付能力报告	
	private boolean bIsFenh = false;// 02 分红险报告
	private boolean bIsCaiW = false;//03 公司财务报告 	
	private boolean bIsGrpW = false;//04 集团偿付能力报告	
	private String fileDirName = "";	
	private String DBType = Constant.DBTYPE;// 系统使用数据库类型		
	private Vector fileList_Changf = new Vector();
	private Vector fileList_Fenh = new Vector();
	private Vector fileList_CaiW = new Vector();
	private List<?> listCfReportdata;
	private List<?> listRowAddDesc;
	private List<?> listRowData;
	private List<?> listColData;
	@Resource
	private CfReportDataDAO expcfReportDatadao;
	@Resource
	private CfReportRowAddDataDAO expcfReportRowAddDatadao;
	@Resource
	private ExportXMLQueryDAO querydao;
	@Resource
	private BranchInfoService branchInfoService;
	
	
	private String licenseNumber=Constant.LICENSENUMBER;
	
	private String reportWork = Constant.UPLOADDIR;
	
	private String inType=Constant.INTYPE;
	

	@Transactional
	public InvokeResult reportCreate(ReportCreateInfoDTO info) {
		
		// 得到外部传入数据
		getInputData(info);
        // 设置月份
	    setMonth();	 
	    
	    // 获取区域代码	    
	    setAreaid();
		// 生成根标签
	    setRoottag();   

		if(!getCompanyType())
		{
			System.out.println("获取公司类型出错！");
		}
		
		File f = new File(reportWork + "/xbrl/" + fileDirName);
		if(!f.exists())
		{
			f.mkdirs();
		}
		/*
		if(!checkDataIsNull())// 检查所有数据是否为空
		{ 
			return InvokeResult.failure("报送数据不完整，请确认报送数据。");
		}
		*/
		if(!createXbrlStyle())// 生成xbrl
		{
			return InvokeResult.failure("实例文档XML文件生成失败。");
		}
		/*if(!createReportDoc())// 生成Word文件
		{
			return InvokeResult.failure("Word文件生成失败。");
		}		
		if(!createReportZIP())// 压缩文件
		{
			return InvokeResult.failure("报送文件ZIP生成失败。");
		}	*/	
		CalCfReportInfo calCfReportInfo=getReportInfo(info);
		calCfReportInfo.setReportState("6");
		querydao.update(calCfReportInfo);
		return InvokeResult.success();
	}
	private void getInputData(ReportCreateInfoDTO info){
	    this.YearCode = info.getYear();
	    this.CfReportRate = info.getReportType();
	    this.reportCateGory=info.getReportCateGory(); //报告类型 - 0偿付能力报告 1风险评级报告
	    this.Quarter = info.getQuarter();
	    this.reportId=info.getReportId();
	    //fileDirName = this.CfReportRate+"-"+this.YearCode+"-"+this.Quarter;
	    fileDirName = this.reportId;
	}
	//设置月份
	private void setMonth()
	{
		/*if("1".endsWith(this.Quarter))
		{
			this.Month = this.YearCode + "03";
		}
		else if("2".endsWith(this.Quarter))
		{
			this.Month = this.YearCode + "06";
		}
		else if("3".endsWith(this.Quarter))
		{
			this.Month = this.YearCode + "09";
		}
		else if("4".endsWith(this.Quarter))
		{
			this.Month = this.YearCode + "12";
		}*/
		this.Month=this.YearCode;
	}
	private void setAreaid() {
		this.areaid = organcode + "000000";// 此次偿付能力报送以总公司为报送单位，不要求报送省分级别数据
	}
	private void setRoottag() 
	{
		String strRootTag = "DATA" + organcode + Month;		
		this.roottag = strRootTag;
	}
	  
	//获取 config.properties中配置的：偿付能力报告时用到的公司的类型	  
	private boolean getCompanyType()
	{
		if(this.organcode.equals("000007")){//集团
	    	this.CompanyType="Group2";
	    }else if(this.organcode.equals("000008")){//财再
	    	this.CompanyType="Propertyinsurance1";//pro1
	    }else if(this.organcode.equals("000009")){//寿再
	    	this.CompanyType="Lifeinsurance1";//life1
	    }else if(this.organcode.equals("000000")){//寿再
	    	String commonCompanyType = CompanyType;
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
		this.bIsChangf = true;
		  
		return true;
	  }
	public boolean checkDataIsNull(){
		
		String sql1 = "select * from cfreportdata where quarter='"+this.Quarter
				+"' and month='"+this.Month
				+"' and reportrate='"+this.CfReportRate
				+"' and comcode='"+this.reportId.substring(20)//(20,28)
				+"' and reportId='"+this.reportId
				+"'";
				
		System.out.println(sql1);
		List<?> list1=expcfReportDatadao.queryBySQL(sql1); 
		if(list1.size()<=0||list1==null){
			System.out.println("cfReportData没有可生成报送文件的数据，请确认季度数据已上传。");
			return false;
		}
		list1.removeAll(list1);
		String sql2 = "select * from cfreportrowadddata where quarter='"+
                     this.Quarter+"' and yearmonth='"+
			         this.Month+"' and reportrate='"+
                     this.CfReportRate+"' and comcode='"+
                     this.reportId.substring(20)+"' and reportId='"+
			         this.reportId+"'";
		//(20,28)
		System.out.println(sql2);
		List<?> list2= expcfReportRowAddDatadao.queryBySQL(sql2); 
	    if(list2.size()<=0||list2==null){
		    System.out.println("cfReportRowAddData没有可生成报送文件的数据，请确认季度数据已上传。");
		    return false;
	    }
	    list2.removeAll(list2);
		return true;
	}
	//生成固定格式的document
	private boolean createXbrlStyle(){
	    String filePath = createXbrlStyle(this.fileDirName);
		if(null == filePath || "".equals(filePath.trim())){						
			return false;						
		}
		fileList_Changf.add(filePath);
		return true;
	}
	public void test(){
		this.CfReportRate = "2";// 频度：1-季度快报，2-季度报告，3-临时报告
		this.Quarter = "1";// 报送季度	
		this.Month = "2016";// 月度	
		this.DBType="Oracle";
		this.YearCode="2016";
		this.organcode="000000";
		this.CompanyType="Lifeinsurance1";
		createXbrlStyle();
	}
	private List<ElementInfo> getElementInfo(){
		@SuppressWarnings("unchecked")
		List<ElementInfo> list=(List<ElementInfo>) querydao.queryByhsql("from ElementInfo");
		return list;
	} 
	private List<Unit> getUnits(){
		@SuppressWarnings("unchecked")
		List<Unit> list=(List<Unit>) querydao.queryByhsql("from Unit");
		return list;
	}
	//10号文查公司名
	public String getOrganName(String organcode){
		BranchInfo b=branchInfoService.findBranchInfo(organcode);
		if(b!=null){
			return b.getComName();
		}
		return "";
	}
	//偿二代报送
	public String getOrganName(String Quarter,String year,String CfReportRate,String organcode,String reportId){
		String outitemcode="";
		//确定是否是10号文
				String codcString="";
				if(reportId.substring(9,10).equals("1")){
					//codcString=reportId.substring(20, 28);
					codcString=reportId.substring(20);
				}else {
					codcString=organcode;
				}
		if("1".equals(CfReportRate)){
			outitemcode="3210000_00001";
		}else{
			outitemcode="C01_00001";
		}
		String sql1 = "select distinct TEXTVALUE AS TEXTVALUE from cfreportdata where quarter='"+Quarter
				+"' and month='"+year
				+"' and reportrate='"+CfReportRate
				+"' and comcode='"+codcString
				+"' and outitemcode='"+outitemcode+"'"
				+ " and reportId='"+reportId+"'";
		System.out.println(sql1);
		List<?> list1=expcfReportDatadao.queryBySQL(sql1); 
		if(list1.size()>0){
			if(!"".equals(list1.get(0))&&list1.get(0)!=null){
				Map map=(Map)list1.get(0);
				return (String) map.get("TEXTVALUE");
			}
		}
		return "";
	}
	private String GetDay(int Month){
		//第一季度 1 3
				//第二季度 4 6
				//第三季度 7 9
				//第四季度 10 12
		String result="";
		if(Month==1){
			//startMonthStr="01";
			result="31";
		}else if(Month==4){
			//startMonthStr="04";
			result="30";
		}else if(Month==7){
			//startMonthStr="07";
			result="31";
		}else if(Month==10){
			result="31";
		}else if(Month==3){
			//endMonthStr="03";
			result="31";
		}else if(Month==6){
			//endMonthStr="06";
			result="30";
		}else if(Month==9){
			//endMonthStr="09";
			result="30";
		}else if(Month==12){
			result="31";
		}
		return result;
	}
	/**
	 * 根据年度季度来生成一套日期规则
	 * @param 日期类型
	 * @param 年度
	 * @param 季度
	 * @return 
	 */
	public String[] GetPeriod(String periodType,String year,String Quarter){
		int intyear=Integer.parseInt(year);
		int QuarterInt=Integer.parseInt(Quarter);
		return GetPeriod(periodType,intyear,QuarterInt);
		
	} 
	public CalCfReportInfo getReportInfo(ReportCreateInfoDTO info){
		String reportId=info.getReportId();
		CalCfReportInfo calCfReportInfo=querydao.get(CalCfReportInfo.class, reportId);
		return calCfReportInfo;
	}
	/**
	 * 根据年度季度来生成一套日期规则
	 * @param 日期类型
	 * @param 年度
	 * @param 季度
	 * @return 
	 */
	public String[] GetPeriod(String periodType,int year,int Quarter){
		String[] result=new String[2];
		String day1="";
		String day2="";
		int startMonth=3*Quarter-2;
		//String startMonthStr=startMonth+"";
		int endMonth=3*Quarter;
		//String endMonthStr=endMonth+"";
		String endMonthStr="";
		String startMonthStr="";
		int instantStartMonth=(startMonth-1)==0?12:startMonth-1;
		day1=GetDay(instantStartMonth);
		day2=GetDay(endMonth);
		if(endMonth>10){
			endMonthStr=endMonth+"";
		}else{
			endMonthStr="0"+endMonth;
		}
		if(startMonth>=10){
			startMonthStr=startMonth+"";
		}else{
			startMonthStr="0"+startMonth;
		}
		if("instant".equals(periodType)){
			String instantStartMonthStr="";
			if(instantStartMonth>=10){
				instantStartMonthStr=instantStartMonth+"";
			}else{
				instantStartMonthStr="0"+instantStartMonth;
			}
			if("12".equals(instantStartMonthStr)){
				result[0]=year-1+"-"+instantStartMonthStr+"-"+day1; //期初
			}else{
				result[0]=year+"-"+instantStartMonthStr+"-"+day1; //期初
			}
			result[1]=year+"-"+endMonthStr+"-"+day2; //期末
		}else if("duration".equals(periodType)){
			result[0]=year+"-"+startMonthStr+"-"+"01";
			result[1]=year+"-"+endMonthStr+"-"+day2;
		}
		return result;
	} 
	/**
	 * 文本块转换
	 * @param str
	 * @return
	 */
	private String htmlspecialchars(String str) {
    	str = str.replaceAll("&", "&amp;");
    	str = str.replaceAll("<", "&lt;");
    	str = str.replaceAll(">", "&gt;");
    	str = str.replaceAll("\"", "&quot;");
    	return str;
    }
	/**
	 * 拼接数据精度
	 * @param str
	 * @return
	 */
	public String FormatData(String strValue,String decimals) {
		int strToInt =Integer.parseInt(decimals);
		String decimalFormat="";
		if(strToInt==0){
			decimalFormat="#0";
		}else{
			decimalFormat="#0.";
		}
		for(int i=0;i<strToInt;i++){
			decimalFormat+="0";
		}
		DecimalFormat df=new DecimalFormat(decimalFormat);
		df.setRoundingMode(RoundingMode.HALF_UP);
		String result=df.format(Double.parseDouble(strValue));
		return result;
	}
	private Map<String,List<?>> staticXbrl(List<ElementInfo> allElements,List<ContextDTO> contexts,List<UnitDTO> units,List<ElementDTO> elements,List<Unit> allUnits){
		//获取数据
		getItemDataFromDB();
		Map<String,List<?>> result=new HashMap<String,List<?>>();
		List<String> contextSet=new ArrayList<String>();
		List<String> unitSet=new ArrayList<String>();
		List<String[]> elementSet=new ArrayList<String[]>();
		int nRows = listCfReportdata.size();
		int elementsSize=allElements.size();
		int unitsSize=allUnits.size();
		for(int cRow = 0;cRow<nRows;cRow++){
			Map<?,?> m=(Map<?,?>)listCfReportdata.get(cRow);
			String elementId=(String) m.get("ELEMENTCODE");
			ElementInfo resultElement=null;
			String DBContext= (String) m.get("CONTEXTCODE");
			String DBUnit= (String) m.get("UNITCODE");
			String DBDecimals= (String) m.get("DECIMALS");
			String DBStartOrEndType= (String) m.get("STARTORENDTYPE"); //0期初 1期末 2本年
			//上下文前缀
			String contextPrefix="";
			boolean isAxis=true;
			for(int i=0;i<elementsSize;i++){
				ElementInfo e=allElements.get(i);
				if(e.getElementCode().equals(elementId)){
					resultElement=e;
					e=null;
					break;
				}
			}
			if(resultElement==null||"".equals(resultElement)){
				continue;
			}
			ContextDTO c=new ContextDTO();
			//判断上下文类型
			if("".equals(DBContext)||DBContext==null){
				isAxis=false;
				c.setContextType("0"); //非维度
				DBContext="";
			}
			//这里需要判断元素的时期类型
			//具体的日期信息需要从界面上传过来
			//这里需要写一个函数来定义取数的规则
			String[] period=GetPeriod(resultElement.getPeriodType(),this.YearCode,this.Quarter); //本季度
			String[] period2=new String[2];//前二季度
			if(Integer.parseInt(this.Quarter)-2<=0){
				period2=GetPeriod(resultElement.getPeriodType(),Integer.parseInt(this.YearCode)-1,Integer.parseInt(this.Quarter)-2+4); 
			}else{
				period2=GetPeriod(resultElement.getPeriodType(),Integer.parseInt(this.YearCode),Integer.parseInt(this.Quarter)-2); 
			}
			String[] period3=new String[2]; //前三季度
			if(Integer.parseInt(this.Quarter)-3<=0){
				period3=GetPeriod(resultElement.getPeriodType(),Integer.parseInt(this.YearCode)-1,Integer.parseInt(this.Quarter)-3+4); 
			}else{
				period3=GetPeriod(resultElement.getPeriodType(),Integer.parseInt(this.YearCode),Integer.parseInt(this.Quarter)-3); 
			}
			if(period==null||"".equals(period)){
				//throw new Exception("获取时期信息出错");
			}
			if("instant".equals(resultElement.getPeriodType())){ //0期初，1期末 2本年 3前二季度 4前三季度 
				c.setPeriodType("0");
				if("0".equals(DBStartOrEndType)){
					//contextPrefix="As_Of_"+period[0].replace("-", "_"); //标准生成格式
					if(isAxis){
						contextPrefix="FY"+period[0].replace("-", "")+"e2";
					}else{
						contextPrefix="C_instant_"+period[0];
					}
					c.setEndPeriod(period[0]);
				}else if("1".equals(DBStartOrEndType)){
					//contextPrefix="As_Of_"+period[1].replace("-", "_");
					if(isAxis){
						contextPrefix="FY"+period[1].replace("-", "")+"e2";
					}else{
						contextPrefix="C_instant_"+period[1];
					}
					c.setEndPeriod(period[1]);
				}else if("3".equals(DBStartOrEndType)){
					//contextPrefix="As_Of_"+period[1].replace("-", "_");
					if(isAxis){
						contextPrefix="FY"+period2[1].replace("-", "")+"e2";
					}else{
						contextPrefix="C_instant_"+period2[1];
					}
					c.setEndPeriod(period2[1]);
				}else if("4".equals(DBStartOrEndType)){
					//contextPrefix="As_Of_"+period[1].replace("-", "_");
					if(isAxis){
						contextPrefix="FY"+period3[1].replace("-", "")+"e2";
					}else{
						contextPrefix="C_instant_"+period3[1];
					}
					c.setEndPeriod(period3[1]);
				}else{
					//contextPrefix="As_Of_"+period[1].replace("-", "_");
					if(isAxis){
						contextPrefix="FY"+period[1].replace("-", "")+"e2";
					}else{
						contextPrefix="C_instant_"+period[1];
					}
					c.setEndPeriod(period[1]);
				}
			}else if("duration".equals(resultElement.getPeriodType())){
				//contextPrefix="From_"+period[0].replace("-", "_")+"_To_"+period[1].replace("-", "_");
				if("2".equals(DBStartOrEndType)){
					if(isAxis){
						contextPrefix="FY"+(this.YearCode+"0101")+"-"+period[1].replace("-", "")+"d2";
					}else{
						contextPrefix="C_duration_"+(this.YearCode+"-01-01")+"_"+period[1];
					}
					c.setStartPeriod(this.YearCode+"-01-01");
				}else{
					if(isAxis){
						contextPrefix="FY"+period[0].replace("-", "")+"-"+period[1].replace("-", "")+"d2";
					}else{
						contextPrefix="C_duration_"+period[0]+"_"+period[1];
					}
					c.setStartPeriod(period[0]);
				}
				c.setPeriodType("1");
				c.setEndPeriod(period[1]);
			}
			if("".equals(DBContext)){
				c.setContextCode(contextPrefix);
			}else{
				c.setContextCode(contextPrefix+"_"+DBContext);//标准格式
				//c.setContextCode("FY"+period[1].replace("-", "")+"e2_"+DBContext);
			}
			//拿这两个值判断是否是明确维度
			String axis="";
			String member="";
			String lineMember="";
			String lineRemark="";
			//上下文场景信息，如果是非维度则没有次项
			axis=(String) m.get("AXIS");
			member=(String) m.get("MEMBER");
			lineMember=(String) m.get("LINEMEMBER");
			lineRemark=(String) m.get("REMARK");
			c.setAxis(axis);
			c.setMember(member);
			c.setLineMember(lineMember);
			if(lineRemark!=null&&!"".equals(lineRemark)){
				//c.setContextCode(contextPrefix+"_"+DBContext+lineRemark+(cRow+1));
				//c.setContextCode("FY"+period[1].replace("-", "")+"e2_"+DBContext+lineRemark+(cRow+1));
				if("0".equals(lineRemark)){ //如果是0说明没有前缀
					c.setRemark((cRow+1)+"");
				}else{
					c.setRemark(lineRemark+(cRow+1));
				}
				if(cRow==0){
					c.setContextCode(contextPrefix+"_"+DBContext);
				}else{
					c.setContextCode(contextPrefix+"_"+DBContext+"_"+cRow);
				}
			}
			//单位
			if(DBUnit!=null&&!"".equals(DBUnit)){
				//getUnits
				UnitDTO u=new UnitDTO();
				Unit unit=new Unit();
				for(int i=0;i<unitsSize;i++){
					if(DBUnit.equals(allUnits.get(i).getUnitCode())){
						unit=allUnits.get(i);
						break;
					}
				}
				u.setUnitCode(DBUnit);
				u.setNumerator(unit.getNumerator());
				u.setDenominator(unit.getDenominator());
				if(!unitSet.contains(DBUnit)){
					unitSet.add(DBUnit);
					units.add(u);
				}
			}
			//元素
			String OutItemType = (String) m.get("OUTITEMTYPE");
			//判断不适用的字段
			String isused= "";
			if(m.get("ISUSED")!=null&&!"".equals(m.get("ISUSED"))){
				isused=(String) m.get("ISUSED");
			}
			//----------不适用------------
			String strValue = "";
			String elementType=resultElement.getType();
			/*if("num:percentItemType".equals(elementType)){ //百分比型
				strValue = (String) m.get("REPORTITEMVALUE");
				if( null != strValue && !"".equals(strValue.trim())){
					strValue = (new DecimalFormat("#0.0000")).format(Double.parseDouble(strValue));
					if(DBDecimals==null||"".equals(DBDecimals)){
						DBDecimals="0";
					}
				}
			}else if(){
				
			}*/
			ElementDTO ed=new ElementDTO();
			ed.setId(resultElement.getElementCode());
			ed.setContextRef(c.getContextCode());
			ed.setNil(false);
			if("0".equals(isused)){
				ed.setNil(true); //如果不适用，将标志位设为false
			}
			
			if("06".equals(OutItemType)){// 06 百分比型
				strValue = (String) m.get("REPORTITEMVALUE");
				if( null != strValue && !"".equals(strValue.trim())){
					if(DBDecimals!=null&&!"".equals(DBDecimals)){
						strValue =FormatData(strValue,DBDecimals); 
					}else{
						strValue =FormatData(strValue,"4"); 
						ed.setDecimals("4");
					}
				}
			}else if("05".equals(OutItemType)){//05 数量型
				strValue = (String) m.get("REPORTITEMVALUE");
				if( null != strValue && !"".equals(strValue.trim())){
					if(DBDecimals!=null&&!"".equals(DBDecimals)){
						strValue =FormatData(strValue,DBDecimals); 
					}else{
						strValue =FormatData(strValue,"0");
						ed.setDecimals("0");
					}
				}
			}else if("04".equals(OutItemType)){//04 数值型
				strValue = (String) m.get("REPORTITEMVALUE");
				if( null != strValue && !"".equals(strValue.trim())){
					if(DBDecimals!=null&&!"".equals(DBDecimals)){
						strValue =FormatData(strValue,DBDecimals); 
					}else{
						strValue = FormatData(strValue,"2"); 
						ed.setDecimals("2");
					}
				}
			}else if("03".equals(OutItemType)){//03 文件型，不包含在XML报文中
				continue;
			}else if("02".equals(OutItemType)){//02 描述型
				strValue=(String) m.get("DESTEXT");
				if(!"".equals(strValue)&&strValue!=null){
					htmlspecialchars(strValue);
				}else{	
					strValue = "\n" + m.get("DESTEXT") + "\n";
				}				
			}else if("01".equals(OutItemType)){//01 短文本型
				strValue = (String) m.get("TEXTVALUE");
				if("xbrli:booleanItemType".equals(resultElement.getType())){
					if("是".equals(strValue)){
						strValue="true";
					}else if("否".equals(strValue)){
						strValue="false";
					}
				}
			}else if("07".equals(OutItemType)){//07  
				strValue = (String) m.get("TEXTVALUE");
			}else{
				System.out.println("未获取到因子值类型.");
			}
			
			if( null == strValue || "".equals(strValue)||("\n"+null+"\n").equals(strValue)){
				//strValue = " "; //待定
				continue;
			}
			if("0.00".equals(strValue)||"0.0000".equals(strValue)){
				strValue = "0";
			}
			if(DBDecimals!=null&&!"".equals(DBDecimals)){
				if(new BigDecimal(strValue).compareTo(new BigDecimal(0))==0){
					strValue = "0";
				}
			}
			ed.setData(strValue);
			if(DBUnit!=null&&!"".equals(DBUnit)){
				ed.setUnitRef(DBUnit);
				//ed.setDecimals("0");
				if(DBDecimals!=null&&!"".equals(DBDecimals)){
					ed.setDecimals(DBDecimals);
				}
			}
			String[] es=new String[2];
			es[0]=resultElement.getElementCode();
			es[1]=c.getContextCode();
			if(elementSet.size()==0){
				elementSet.add(es);
				elements.add(ed);
			}
			for(int i=0;i<elementSet.size();i++){
				if(es[0].equals(elementSet.get(i)[0])&&es[1].equals(elementSet.get(i)[1])){
					break;
				}
				if(i==elementSet.size()-1){
					elementSet.add(es);
					elements.add(ed);
				}
			}
			if(!contextSet.contains(c.getContextCode())){
				contextSet.add(c.getContextCode());
				contexts.add(c);
			}
			/*if(!elementSet.contains(es)){ //元素去重
				elementSet.add(es);
				elements.add(ed);
			}*/
			es=null;
			//if(!elementSet.contains(resultElement.getElementCode())){
				//elementSet.add(resultElement.getElementCode());
				//elements.add(ed);
			//}
		}
		result.put("contexts", contexts);
		result.put("units", units);
		result.put("elements", elements);
		result.put("unitSet", unitSet);
		result.put("contextSet", contextSet);
		return result;
	}
	private Map<String,List<?>> extendXbrl(List<ElementInfo> allElements,Map<String,List<?>> staticXbrl,List<Unit> allUnits){
		List<ContextDTO> contexts=(List<ContextDTO>) staticXbrl.get("contexts");
		List<UnitDTO> units=(List<UnitDTO>) staticXbrl.get("units");
		List<ElementDTO> elements=(List<ElementDTO>) staticXbrl.get("elements");
		List<String> contextSet=(List<String>) staticXbrl.get("contextSet");
		List<String> unitSet=(List<String>) staticXbrl.get("unitSet");
		List<String[]> elementSet=new ArrayList<String[]>();
		Map<String,List<?>> result=new HashMap<String,List<?>>();
		if(!getExtendTableCode()){
			return staticXbrl;
		}
		//系统频度值的设定与保监会报文频度设定不一致，需要调整
		String strIntervaltype = "";
		if( "1".equals(this.CfReportRate)){
			strIntervaltype = "5";
		}else if( "2".equals(this.CfReportRate) ){
			strIntervaltype = "3";
		}else if( "3".equals(this.CfReportRate) ){
			strIntervaltype = "4";
		}
		int elementsSize=allElements.size();
		int unitsSize=allUnits.size();
		for(int nIndex=0;nIndex<listRowAddDesc.size();nIndex++){
			int countx=200;
			Map<?,?> m=(Map<?,?>)listRowAddDesc.get(nIndex);
			String strTableCode = (String) m.get("UPPER(cfr.tablecode)");
            // 通过表代码(tablecode)获取行号
			if(!getRowDataFromDB(strTableCode)){
				continue;
			}
			int rowscount = listRowData.size();
			// 没有数据的行可扩展表，整表都不报送
			if( rowscount > 0 ){
				for(int rownum = 0 ; rownum < rowscount ; rownum ++ ){
					Map<?,?> mr=(Map<?,?>)listRowData.get(rownum);
					String rowno = (String) mr.get("rowno");					
					// 通过行号获取该行，所有列的数据
					if(!getColDataFromDB(strTableCode,rowno)){
						//return staticXbrl;
						continue;
					}
					int colscount = listColData.size();
					for(int colnum = 0 ; colnum < colscount ; colnum ++ ){
						Map<?,?> mc=(Map<?,?>)listColData.get(colnum);
						String elementId=(String) mc.get("ELEMENTCODE");
						ElementInfo resultElement=null;
						String DBContext= (String) mc.get("CONTEXTCODE");
						String DBUnit= (String) mc.get("UNITCODE");
						String DBDecimals= (String) mc.get("DECIMALS");
						String DBStartOrEndType= (String) mc.get("STARTORENDTYPE"); //0期初 1期末
						//上下文前缀
						String contextPrefix="";
						//String contextPrefixAxis=""; //带轴的上下文前缀
						boolean isAxis=true; //是否带轴
						for(int i=0;i<elementsSize;i++){
							ElementInfo e=allElements.get(i);
							if(e.getElementCode().equals(elementId)){
								resultElement=e;
								e=null;
								break;
							}
						}
						ContextDTO c=new ContextDTO();
						//判断上下文类型
						if("".equals(DBContext)||DBContext==null){
							isAxis=false;
							c.setContextType("0"); //非维度
							DBContext="";
						}
						//这里需要判断元素的时期类型
						//具体的日期信息需要从界面上传过来
						//这里需要写一个函数来定义取数的规则
						if(resultElement==null||"".equals(resultElement)){
							continue;
						}
						String[] period=GetPeriod(resultElement.getPeriodType(),this.YearCode,this.Quarter); //本季度
						String[] period2=new String[2];//前二季度
						if(Integer.parseInt(this.Quarter)-2<=0){
							period2=GetPeriod(resultElement.getPeriodType(),Integer.parseInt(this.YearCode)-1,Integer.parseInt(this.Quarter)-2+4); 
						}else{
							period2=GetPeriod(resultElement.getPeriodType(),Integer.parseInt(this.YearCode),Integer.parseInt(this.Quarter)-2); 
						}
						String[] period3=new String[2]; //前三季度
						if(Integer.parseInt(this.Quarter)-3<=0){
							period3=GetPeriod(resultElement.getPeriodType(),Integer.parseInt(this.YearCode)-1,Integer.parseInt(this.Quarter)-3+4); 
						}else{
							period3=GetPeriod(resultElement.getPeriodType(),Integer.parseInt(this.YearCode),Integer.parseInt(this.Quarter)-3); 
						}
						if(period==null||"".equals(period)){
							//throw new Exception("获取时期信息出错");
						}
						if("instant".equals(resultElement.getPeriodType())){
							c.setPeriodType("0");
							if("0".equals(DBStartOrEndType)){
								//contextPrefix="As_Of_"+period[0].replace("-", "_");
								if(isAxis){
									contextPrefix="FY"+period[0].replace("-", "")+"e2";
								}else{
									contextPrefix="C_instant_"+period[0];
								}
								c.setEndPeriod(period[0]);
							}else if("1".equals(DBStartOrEndType)){
								//contextPrefix="As_Of_"+period[1].replace("-", "_");
								if(isAxis){
									contextPrefix="FY"+period[1].replace("-", "")+"e2";
								}else{
									contextPrefix="C_instant_"+period[1];
								}
								c.setEndPeriod(period[1]);
							}else if("3".equals(DBStartOrEndType)){
								//contextPrefix="As_Of_"+period[1].replace("-", "_");
								if(isAxis){
									contextPrefix="FY"+period2[1].replace("-", "")+"e2";
								}else{
									contextPrefix="C_instant_"+period2[1];
								}
								c.setEndPeriod(period2[1]);
							}else if("4".equals(DBStartOrEndType)){
								//contextPrefix="As_Of_"+period[1].replace("-", "_");
								if(isAxis){
									contextPrefix="FY"+period3[1].replace("-", "")+"e2";
								}else{
									contextPrefix="C_instant_"+period3[1];
								}
								c.setEndPeriod(period3[1]);
							}else{
								//contextPrefix="As_Of_"+period[1].replace("-", "_");
								if(isAxis){
									contextPrefix="FY"+period[1].replace("-", "")+"e2";
								}else{
									contextPrefix="C_instant_"+period[1];
								}
								c.setEndPeriod(period[1]);
							}
						}else if("duration".equals(resultElement.getPeriodType())){
							//contextPrefix="From_"+period[0].replace("-", "_")+"_To_"+period[1].replace("-", "_");
							//contextPrefix="C_duration_"+period[0]+"_"+period[1];
							if("2".equals(DBStartOrEndType)){
								if(isAxis){
									contextPrefix="FY"+(this.YearCode+"0101")+"-"+period[1].replace("-", "")+"d2";
								}else{
									contextPrefix="C_duration_"+(this.YearCode+"-01-01")+"_"+period[1];
								}
								c.setStartPeriod(this.YearCode+"-01-01");
							}else{
								if(isAxis){
									contextPrefix="FY"+period[0].replace("-", "")+"-"+period[1].replace("-", "")+"d2";
								}else{
									contextPrefix="C_duration_"+period[0]+"_"+period[1];
								}	
								c.setStartPeriod(period[0]);
							}
							c.setPeriodType("1");
							//c.setStartPeriod(period[0]);
							c.setEndPeriod(period[1]);
						}
						if("".equals(DBContext)||DBContext==null){
							c.setContextCode(contextPrefix);
						}else{
							c.setContextCode(contextPrefix+"_"+DBContext);
							//c.setContextCode("FY"+period[1].replace("-", "")+"e2_"+DBContext);
						}
						//拿这两个值判断是否是明确维度
						String axis="";
						String member="";
						String lineMember="";
						String lineRemark="";
						//上下文场景信息，如果是非维度则没有次项
						axis=(String) mc.get("AXIS");
						member=(String) mc.get("MEMBER");
						lineMember=(String) mc.get("LINEMEMBER");
						lineRemark=(String) mc.get("REMARK");
						c.setAxis(axis);
						c.setMember(member);
						c.setLineMember(lineMember);
						//c.setRemark(rownum+"");
						if(lineRemark!=null&&!"".equals(lineRemark)){
							boolean flag=lineRemark.contains("x");//CR09比较特殊的表,表示字表确定
							if("0".equals(lineRemark)){
								c.setRemark((rownum+1)+"");
							}else{
								if(flag){
									c.setRemark((lineRemark.substring(0,lineRemark.indexOf("x")+1)).replace("x", (rownum+1)+"")+","+lineRemark.replace("x", (rownum+1)+""));
									countx+=1;
								}else{
									c.setRemark(lineRemark+(rownum+1));
								}
							}
							//c.setContextCode(contextPrefix+"_"+DBContext+lineRemark+(rownum+1));
							//c.setContextCode("FY"+period[1].replace("-", "")+"e2_"+DBContext+lineRemark+(rownum+1));
							if(rownum==0){
								if(flag){
										c.setContextCode(contextPrefix+"_"+DBContext+"_"+0);
									}else{
										c.setContextCode(contextPrefix+"_"+DBContext);
									}
							}else{
								//if(flag){
								//	c.setContextCode(contextPrefix+"_"+DBContext+"_"+countx);
								//}else{
									c.setContextCode(contextPrefix+"_"+DBContext+"_"+rownum);
								//}
							}
						}
						//单位
						if(DBUnit!=null&&!"".equals(DBUnit)){
							UnitDTO u=new UnitDTO();
							Unit unit=new Unit();
							for(int i=0;i<unitsSize;i++){
								if(DBUnit.equals(allUnits.get(i).getUnitCode())){
									unit=allUnits.get(i);
									break;
								}
							}
							u.setUnitCode(DBUnit);
							u.setNumerator(unit.getNumerator());
							u.setDenominator(unit.getDenominator());
							if(!unitSet.contains(DBUnit)){
								unitSet.add(DBUnit);
								units.add(u);
							}
							if(!unitSet.contains(DBUnit)){
								unitSet.add(DBUnit);
								units.add(u);
							}
						}
						String ColType = (String) mc.get("COLTYPE");
						String colcode = (String) mc.get("COLCODE");
						// 根据列类型取值
						String NumValue = "";
						ElementDTO ed=new ElementDTO();
						ed.setId(resultElement.getElementCode());
						ed.setContextRef(c.getContextCode());
						if("06".equals(ColType)){// 06 百分比型
							NumValue = (String) mc.get("NUMVALUE");
							if( null != NumValue && !"".equals(NumValue.trim())){
								if(DBDecimals!=null&&!"".equals(DBDecimals)){
									NumValue =FormatData(NumValue,DBDecimals); 
								}else{
									NumValue =FormatData(NumValue,"4"); 
									ed.setDecimals("4");
								}
							}
						}else if("05".equals(ColType)){// 05 数量型
							NumValue = (String) mc.get("NUMVALUE");
							if( null != NumValue && !"".equals(NumValue.trim())){
								if(DBDecimals!=null&&!"".equals(DBDecimals)){
									NumValue =FormatData(NumValue,DBDecimals); 
								}else{
									NumValue =FormatData(NumValue,"0"); 
									ed.setDecimals("0");
								}
							}
						}else if("04".equals(ColType)){// 04 数值型
							NumValue = (String) mc.get("NUMVALUE");
							if( null != NumValue && !"".equals(NumValue.trim())){
								if(DBDecimals!=null&&!"".equals(DBDecimals)){
									NumValue =FormatData(NumValue,DBDecimals); 
								}else{
									NumValue =FormatData(NumValue,"2"); 
									ed.setDecimals("2");
								}
							}
						}else if("01".equals(ColType) || "02".endsWith(ColType)){// 01 短文本类型,02 描述型 		
							NumValue = (String) mc.get("TEXTVALUE");
							if("xbrli:booleanItemType".equals(resultElement.getType())){
								if("是".equals(NumValue)){
									NumValue="true";
								}else if("否".equals(NumValue)){
									NumValue="false";
								}
							}
						}else if("07".equals(ColType)){
							NumValue = (String) mc.get("TEXTVALUE");
						}else {
							NumValue = "";
						}
						//TODO ssss
						if( null == NumValue || "".equals(NumValue)){
							continue;
							//NumValue = " ";//此处待定
						}
						if("0.00".equals(NumValue)||"0.0000".equals(NumValue)){
							NumValue = "0";
						}
						if(DBDecimals!=null&&!"".equals(DBDecimals)){
							if(new BigDecimal(NumValue).compareTo(new BigDecimal(0))==0){
								NumValue = "0";
							}
						}
						/*if("0.00".equals(NumValue)){
							NumValue = "0";
						}*/
						ed.setData(NumValue);
						if(DBUnit!=null&&!"".equals(DBUnit)){
							ed.setUnitRef(DBUnit);
							//ed.setDecimals("0");
							if(DBDecimals!=null&&!"".equals(DBDecimals)){
								ed.setDecimals(DBDecimals);
							}
						}
						String[] es=new String[2];
						es[0]=resultElement.getElementCode();
						es[1]=c.getContextCode();
						if(elementSet.size()==0){
							elementSet.add(es);
							elements.add(ed);
						}
						for(int i=0;i<elementSet.size();i++){
							if(es[0].equals(elementSet.get(i)[0])&&es[1].equals(elementSet.get(i)[1])){
								break;
							}
							if(i==elementSet.size()-1){
								elementSet.add(es);
								elements.add(ed);
							}
						}
						if(!contextSet.contains(c.getContextCode())){
							contextSet.add(c.getContextCode());
							contexts.add(c);
						}
						/*if(!elementSet.contains(es)){ //元素去重
							elementSet.add(es);
							elements.add(ed);
						}*/
					}
					listColData.removeAll(listColData);
				}
				listRowData.removeAll(listRowData);
			}
		}
		listRowAddDesc.removeAll(listRowAddDesc);
		result.put("contexts", contexts);
		result.put("units", units);
		result.put("elements", elements);
		return result;
	}
	//整个逻辑为用一个map来放上下文，单位，元素信息，
	//然后用3个list方具体数据
	//依据固定因子还是行可扩展判断数据
	private Map<String,List<?>> getXbrlData(){
		List<ElementInfo> allElements=getElementInfo();
		List<Unit> allUnits=getUnits();
		Map<String,List<?>> result=new HashMap<String,List<?>>();
		List<ContextDTO> contexts=new ArrayList<ContextDTO>();
		List<UnitDTO> units=new ArrayList<UnitDTO>();
		List<ElementDTO> elements=new ArrayList<ElementDTO>();
		Map<String,List<?>>  staticXbrl=staticXbrl(allElements,contexts,units,elements,allUnits); //取固定因子的xbrl数据
		result=extendXbrl(allElements, staticXbrl,allUnits);
		return result;
	} 
	
	//根据不同的入口文件创建不同的命名空间
	private void addNamespace(Element elementRoot,String inType){
		String sql="select codecode,codename from CfCodeManage ccm where ccm.codeType='inType"+inType+"'";
		System.out.println(sql);
		List list= querydao.queryBysql(sql);
		int size=list.size();
		for(int i=0;i<size;i++){
			Map map=(Map) list.get(i);
			elementRoot.addNamespace((String)map.get("CODECODE"),(String)map.get("CODENAME"));
		}
	}
	
	private String createXbrlStyle(String tFileDirName){
		//12位保险公司经营许可证号
		String LicenseNumber=licenseNumber;
		//拼接数据逻辑，将所要的上下文，单位，元素进行拼接
		Map<String,List<?>> map=getXbrlData();
		@SuppressWarnings("unchecked")
		List<ContextDTO> contexts=(List<ContextDTO>) map.get("contexts");
		@SuppressWarnings("unchecked")
		List<UnitDTO> units=(List<UnitDTO>) map.get("units");
		@SuppressWarnings("unchecked")
		List<ElementDTO> elements=(List<ElementDTO>) map.get("elements");
		if("1".equals(this.CfReportRate)){
			inType="1";
		}
		int contextsSize=contexts.size();
		int unitsSize=units.size();
		int elementsSize=elements.size();
		/**整个xbrl文档分为4部分
			第一部分为xbrl文档的头部信息
			第二部分为xbrl的上下文信息
				1.实体信息<entity>
					1.1<identifier>
				2.日期信息<period>
					2.1<instant>or <startDate><endDate>
				3.场景信息<scenario>
					3.1<xbrldl:explicitMember>
					3.2<xbrldl:typedMembe>
					3.3<c-ross LineNumber>
			第三部分为单位信息
			单位信息包括
				1.<measure>
			第四部分为元素信息
				1.<c-ross:元素ID>
		 **/
		Document document = DocumentHelper.createDocument();
		Namespace link = new Namespace("link","http://www.xbrl.org/2003/linkbase");
		Namespace xbrldi = new Namespace("xbrldi","http://xbrl.org/2006/xbrldi");
		Namespace xbrlli = new Namespace("xbrli","http://www.xbrl.org/2003/instance");
		Namespace cross = new Namespace("c-ross","http://xbrl.circ.gov.cn/taxonomy/2015-12-31/c-ross");
		Namespace ifrsfull = new Namespace("ifrs-full","http://xbrl.ifrs.org/taxonomy/2014-03-05/ifrs-full");
		Namespace cas = new Namespace("cas","http://xbrl.mof.gov.cn/taxonomy/2015-03-31/cas");
		Namespace irr = new Namespace("c-ross_irr","http://xbrl.circ.gov.cn/taxonomy/2015-12-31/c-ross_irr");
		Element elementRoot=document.addElement(new QName("xbrl",xbrlli));
		if(reportId.substring(9,10).equals("1")){
			inType="0";
			}
		addNamespace(elementRoot,inType);
		//if("0".equals(inType)){
		elementRoot.addAttribute("xsi:schemaLocation", "http://xbrl.org/2006/xbrldi http://www.xbrl.org/2006/xbrldi-2006.xsd");
		//}
		//elementRoot.addAttribute("xmlns:link", "http://www.xbrl.org/2003/linkbase");
		//第一部分
		Element xbrlFirst=elementRoot.addElement(new QName("schemaRef",link));
		xbrlFirst.addAttribute("xlink:type", "simple");
		xbrlFirst.addAttribute("xlink:arcrole", "http://www.xbrl.org/2003/linkbase");
		//xbrlFirst.addAttribute("xlink:arcrole", "http://www.xbrl.org/2003/linkbase");
		if(reportId.substring(9,10).equals("1")){
			if("0".equals(inType)){
				xbrlFirst.addAttribute("xlink:href", "http://xbrl.circ.gov.cn/taxonomy/2015-12-31/c-ross/full_entry_point_2015-12-31.xsd");
			}else if("1".equals(inType)){
				xbrlFirst.addAttribute("xlink:href", "http://xbrl.circ.gov.cn/taxonomy/2015-12-31/c-ross/irr_life_com_entry_point_2015-12-31.xsd");
			}else if("2".equals(inType)){
				xbrlFirst.addAttribute("xlink:href", "http://xbrl.circ.gov.cn/taxonomy/2015-12-31/c-ross/irr_life_bra_entry_point_2015-12-31.xsd");
			}else if("3".equals(inType)){
				xbrlFirst.addAttribute("xlink:href", "http://xbrl.circ.gov.cn/taxonomy/2015-12-31/c-ross/irr_p&c_com_entry_point_2015-12-31");
			}else if("4".equals(inType)){
				xbrlFirst.addAttribute("xlink:href", "http://xbrl.circ.gov.cn/taxonomy/2015-12-31/c-ross/irr_p&c_bra_entry_point_2015-12-31.xsd");
			}else if("5".equals(inType)){
				xbrlFirst.addAttribute("xlink:href", "http://xbrl.circ.gov.cn/taxonomy/2015-12-31/c-ross/irr_re_com_entry_point_2015-12-31.xsd");
			}
		}else{
			if("0".equals(inType)){
				xbrlFirst.addAttribute("xlink:href", "http://xbrl.circ.gov.cn/taxonomy/2015-12-31/c-ross/full_entry_point_2015-12-31.xsd");
			}else if("1".equals(inType)){
				xbrlFirst.addAttribute("xlink:href", "http://xbrl.circ.gov.cn/taxonomy/2015-12-31/c-ross/q_brief_s_all_entry_point_2015-12-31.xsd");
			}else if("2".equals(inType)){
				xbrlFirst.addAttribute("xlink:href", "http://xbrl.circ.gov.cn/taxonomy/2015-12-31/c-ross/q_s_life_entry_point_2015-12-31.xsd");
			}else if("3".equals(inType)){
				xbrlFirst.addAttribute("xlink:href", "http://xbrl.circ.gov.cn/taxonomy/2015-12-31/c-ross/q_s_p&c_entry_point_2015-12-31.xsd");
			}else if("4".equals(inType)){
				xbrlFirst.addAttribute("xlink:href", "http://xbrl.circ.gov.cn/taxonomy/2015-12-31/c-ross/q_s_re_entry_point_2015-12-31.xsd");
			}
		}
		//第二部分
		for(int i=0;i<contextsSize;i++){
			ContextDTO cd=contexts.get(i);
			Element xbrlSecond=elementRoot.addElement(new QName("context",xbrlli));
			xbrlSecond.addAttribute("id",cd.getContextCode());
			Element xbrlSecondLevel1=xbrlSecond.addElement(new QName("entity",xbrlli));
			Element xbrlSecondLevel1Child=xbrlSecondLevel1.addElement(new QName("identifier",xbrlli));
			xbrlSecondLevel1Child.addAttribute("scheme", "http://www.circ.gov.cn/");
			xbrlSecondLevel1Child.addText(LicenseNumber);//一个12位的保险公司经营许可证号
			Element xbrlSecondLevel2=xbrlSecond.addElement(new QName("period",xbrlli));
			if("0".equals(cd.getPeriodType())){
				Element xbrlSecondLevel2Child1=xbrlSecondLevel2.addElement(new QName("instant",xbrlli));
				xbrlSecondLevel2Child1.addText(cd.getEndPeriod()); //时点
			}else if("1".equals(cd.getPeriodType())){
				Element xbrlSecondLevel2Child2=xbrlSecondLevel2.addElement(new QName("startDate",xbrlli));
				Element xbrlSecondLevel2Child3=xbrlSecondLevel2.addElement(new QName("endDate",xbrlli));
				xbrlSecondLevel2Child2.addText(cd.getStartPeriod());//开始日期
				xbrlSecondLevel2Child3.addText(cd.getEndPeriod());//结束日期
			}
			//拿这两个值判断是否是明确维度
			String axis=cd.getAxis();
			String member=cd.getMember();
			String lineMember=cd.getLineMember();
			String lineRemark=cd.getRemark();
			//String contextCode=cd.getContextCode();
			//上下文场景信息，如果是非维度则没有次项
			//明确维度项（可以有多个）
			if(!"".equals(axis)&&!"".equals(member)&&axis!=null&&member!=null){
				Element xbrlSecondLevel3=xbrlSecond.addElement(new QName("scenario",xbrlli));
				String[] axiss=axis.split(",");
				String[] members=member.split(",");
				String[] linenumbers=null;
				String[] lineRemarks=null;
				if(lineMember!=null&&!"".equals(lineMember)){
					linenumbers=lineMember.split(",");
				}
				if(lineRemark!=null&&!"".equals(lineRemark)){
					lineRemarks=lineRemark.split(",");
				}
				//类型化维度项（取数时根据需要for循环来取行号）
				if(lineMember!=null&&!"".equals(lineMember)){
					//String lineMemberAxis=lineMember.split("_")[0];
					//String lineMemberMember=lineMember.split("_")[1];
					if(linenumbers!=null){
						for(int m=0;m<linenumbers.length;m++){
							Element xbrlSecondLevel3Child2=xbrlSecondLevel3.addElement(new QName("typedMember",xbrldi));
							xbrlSecondLevel3Child2.addAttribute("dimension", "c-ross:"+linenumbers[m]);//轴信息
							//xbrlSecondLevel3Child2.addText(lineRemark);//member信息
							Element xbrlSecondLevel3Child2Child=xbrlSecondLevel3Child2.addElement(new QName("LineNumber",cross));
							xbrlSecondLevel3Child2Child.addText(lineRemarks[m]);//行号
						}
					}else{
						Element xbrlSecondLevel3Child2=xbrlSecondLevel3.addElement(new QName("typedMember",xbrldi));
						xbrlSecondLevel3Child2.addAttribute("dimension", "c-ross:"+lineMember);//轴信息
						//xbrlSecondLevel3Child2.addText(lineRemark);//member信息
						Element xbrlSecondLevel3Child2Child=xbrlSecondLevel3Child2.addElement(new QName("LineNumber",cross));
						xbrlSecondLevel3Child2Child.addText(lineRemark);//行号
					}
				}	
				for(int j=0;j<axiss.length;j++){
					Element xbrlSecondLevel3Child1=xbrlSecondLevel3.addElement(new QName("explicitMember",xbrldi));
					xbrlSecondLevel3Child1.addAttribute("dimension", "c-ross:"+axiss[j]);//轴信息
					//System.out.println(axiss[j]+"========================");
					xbrlSecondLevel3Child1.addText("c-ross:"+members[j]);//member信息
					//System.out.println(members[j]+"========================");
				}
			}
			//类型化维度项（取数时根据需要for循环来取行号）
			else if(lineMember!=null&&!"".equals(lineMember)){
				String[] linenumbers=lineMember.split(",");
				String[] lineRemarks=lineRemark.split(",");
				Element xbrlSecondLevel3=xbrlSecond.addElement(new QName("scenario",xbrlli));
				//String lineMemberAxis=lineMember.split("_")[0];
				//String lineMemberMember=lineMember.split("_")[1];
				for(int m=0;m<linenumbers.length;m++){
					Element xbrlSecondLevel3Child2=xbrlSecondLevel3.addElement(new QName("typedMember",xbrldi));
					xbrlSecondLevel3Child2.addAttribute("dimension", "c-ross:"+linenumbers[m]);//轴信息
					//xbrlSecondLevel3Child2.addText(lineRemark);//member信息
					Element xbrlSecondLevel3Child2Child=xbrlSecondLevel3Child2.addElement(new QName("LineNumber",cross));
					xbrlSecondLevel3Child2Child.addText(lineRemarks[m]);//行号
				}
				/*Element xbrlSecondLevel3Child2=xbrlSecondLevel3.addElement(new QName("typedMember",xbrldi));
				xbrlSecondLevel3Child2.addAttribute("dimension", "c-ross:"+lineMemberAxis);//轴信息
				//xbrlSecondLevel3Child2.addText(lineRemark);//member信息
				Element xbrlSecondLevel3Child2Child=xbrlSecondLevel3Child2.addElement(new QName("LineNumber",cross));
				xbrlSecondLevel3Child2Child.addText(lineRemark);//行号
*/			}
		}
		//第三部分
		//首先需要判断元素是否为数值类型，然后判断元素是否要加单位
		//分有分子和无分子两种情况	
		for(int i=0;i<unitsSize;i++){
			UnitDTO ud=units.get(i);
			Element xbrlThird=elementRoot.addElement(new QName("unit",xbrlli));
			xbrlThird.addAttribute("id", ud.getUnitCode());
			System.out.println(ud.getUnitCode());
			if(ud.getDenominator()==null||"".equals(ud.getDenominator())){
				Element xbrlThirdLevel=xbrlThird.addElement(new QName("measure",xbrlli));
				xbrlThirdLevel.addText(ud.getNumerator());
			}else{
				Element xbrlThirdLevel=xbrlThird.addElement(new QName("divide",xbrlli));
				Element xbrlThirdLevelChild1=xbrlThirdLevel.addElement(new QName("unitNumerator",xbrlli));
				Element xbrlThirdLevelChild1Child=xbrlThirdLevelChild1.addElement(new QName("measure",xbrlli));
				xbrlThirdLevelChild1Child.addText(ud.getNumerator());
				Element xbrlThirdLevelChild2=xbrlThirdLevel.addElement(new QName("unitDenominator",xbrlli));
				Element xbrlThirdLevelChild2Child=xbrlThirdLevelChild2.addElement(new QName("measure",xbrlli));
				xbrlThirdLevelChild2Child.addText(ud.getDenominator());
			}
		}
		//第四部分
		for(int i=0;i<elementsSize;i++){
			ElementDTO ed=elements.get(i);
			String elementId=ed.getId();
			String[] elementIds=elementId.split("_");
			String prefixElement="";
			String elementContext="";
			Namespace prefix=null;
			if(elementIds.length==2){
				prefixElement=elementIds[0];
				if("c-ross".equals(prefixElement)){
					prefix=cross;
				}else if("ifrs-full".equals(prefixElement)){
					prefix=ifrsfull;
				}else if("cas".equals(prefixElement)){
					prefix=cas;
				}
				elementContext=elementIds[1];
			}
			if(elementIds.length==3){
				prefixElement=elementIds[0]+"_"+elementIds[1];
				if("c-ross_irr".equals(prefixElement)){
					prefix=irr;
				}
				elementContext=elementIds[2];
			}
			Element xbrlFourth=elementRoot.addElement(new QName(elementContext,prefix));//c-ross+元素id
			//元素的上下文id
			xbrlFourth.addAttribute("contextRef",ed.getContextRef());
			if(ed.getUnitRef()!=null&&!"".equals(ed.getUnitRef())){
				//元素的单位id
				xbrlFourth.addAttribute("unitRef",ed.getUnitRef());
				//元素的精度
				xbrlFourth.addAttribute("decimals",ed.getDecimals());
			}
			System.out.println(ed.getNil());
			if(ed.getNil()){
				xbrlFourth.addAttribute("xsi:nil","true");
			}else{
				//事实值内容
				xbrlFourth.addText(ed.getData());
			}
		}
		//Document doc = new Document(elementRoot);
		//xbrlFirst.remove(xbrlFirst.getNamespace());
		String organName=getOrganName(this.Quarter,this.Month,this.CfReportRate,this.organcode,this.reportId);
		if(reportId.substring(9,10).equals("1")){
			organName=getOrganName(reportId.substring(20));
			//organName=getOrganName(reportId.substring(20, 28));
		}
		String reportType="";
		if("1".equals(this.CfReportRate)){
			reportType="2";
		}else if("2".equals(this.CfReportRate)){
			reportType="1";
			if(reportId.substring(9,10).equals("1")){
				reportType="3";
			}
		}else{
			reportType=this.CfReportRate;
		}
		String xbrlName=organName+"-"+LicenseNumber+"-"+reportType+"-"+GetPeriod("instant",this.YearCode,this.Quarter)[1].replace("-", "");
		//String xbrlName=organName+"-"+LicenseNumber+"-"+reportType+"-"+reportId.substring(20);
		
        //String filePath = outPut(document,tFileDirName+"/"+organcode+"/"+xbrlName,tFileDirName+"/"+organcode);// 文件输出
		if(reportId.substring(9,10).equals("1")){
			BranchInfo b=branchInfoService.findBranchInfo(reportId.substring(20));
			//BranchInfo b=branchInfoService.findBranchInfo(reportId.substring(20, 28));
			xbrlName=organName+"-"+b.getLicensenumber()+"-"+reportType+"-"+GetPeriod("instant",this.YearCode,this.Quarter)[1].replace("-", "");
			//xbrlName=organName+"-"+b.getLicensenumber()+"-"+reportType+"-"+reportId.substring(20);

		}
		System.out.println("xbrl名：--"+xbrlName);
		String filePath = outPut(document,tFileDirName+"/"+xbrlName,tFileDirName);// 文件输出
		return filePath;
	}
	//输出XML
	private String outPut(Document doc,String DesFileName,String DesFolderName){
		String despath= "";
		try {
			//xmlOut = new OutputFormat("  ",true);	
			xmlOut = OutputFormat.createPrettyPrint();
			//xmlOut.setEncoding("UTF-8");
			despath = reportWork + "/xbrl/" + DesFileName + ".xml";
			System.out.println("XML导出路径："+despath);
			System.out.println("DesFileName："+DesFileName);
			/*---判断文件路径是否存在，否则生成--begin---*/
			File f = new File(reportWork + "/xbrl/" + DesFolderName);
			if(!f.exists()){
				f.mkdirs();
			}
			/*---判断文件路径是否存在，否则生成--end---*/
			//FileWriter writer = new FileWriter(despath);
			//Writer fileWriter=new FileWriter(fileName);    
            //XMLWriter xmlWriter=new XMLWriter(writer,xmlOut);   
			XMLWriter xmlWriter=new XMLWriter(new OutputStreamWriter(new FileOutputStream(despath),"utf-8"),xmlOut);    
            xmlWriter.write(doc);   //写入文件中 
            xmlWriter.close();   
			//writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		return despath;
	}

		
	
	//从数据库中获取因子数据
	private boolean getItemDataFromDB(){
		//确定是否是10号文
				String codcString="";
				if(reportId.substring(9,10).equals("1")){
					//codcString=reportId.substring(20, 28);
					codcString=reportId.substring(20);
				}else {
					codcString=organcode;
				}
		// 根据系统使用数据库类型，SQL语句不一致
		String strSQL = "";
		if("SqlServer".equals(this.DBType)){// SqlServer
			strSQL = " SELECT CFR.OUTITEMCODE,CFR.REPORTITEMCODE,CONVERT(char(50), CF.REPORTITEMVALUE) REPORTITEMVALUE,CONVERT(char(50), CF.REPORTITEMWANVALUE) REPORTITEMWANVALUE,CF.TEXTVALUE,CFR.OUTITEMTYPE,CFR.OUTITEMNOTE,CF.DESTEXT,CFRE.PORTITEMCODE,CFRE.ELEMENTCODE,CFRE.CONTEXTCODE,CFRE.UNITCODE,CFRE.DECIMALS,CFRE.STARTORENDTYPE,CT.AXIS,CT.MEMBER,CT.LINEMEMBER,CT.REMARK,CF.ISUSED"
						+ " FROM CFREPORTITEMCODEDESC cfr left join (SELECT * FROM CFREPORTDATA WHERE 1 =1  ";
		}
		else if("Oracle".equals(this.DBType)){// Oracle
			strSQL = " SELECT CFR.OUTITEMCODE,CFR.REPORTITEMCODE,to_char(CF.REPORTITEMVALUE) REPORTITEMVALUE,to_char(CF.REPORTITEMWANVALUE) REPORTITEMWANVALUE,CF.TEXTVALUE,CFR.OUTITEMTYPE,CFR.OUTITEMNOTE,CF.DESTEXT,CFRE.PORTITEMCODE,CFRE.ELEMENTCODE,CFRE.CONTEXTCODE,CFRE.UNITCODE,CFRE.DECIMALS,CFRE.STARTORENDTYPE,CT.AXIS,CT.MEMBER,CT.LINEMEMBER,CT.REMARK,CF.ISUSED"
				+ " FROM CFREPORTITEMCODEDESC cfr left join (SELECT * FROM CFREPORTDATA WHERE 1 =1  ";
		}
		else{
			System.out.println("系统使用数据库类型获取失败！");
		  	return false;
		}
		strSQL = strSQL + " AND reportid = '"+reportId+ "' ";
		if(Quarter != null && !"".equals(Quarter)){
			strSQL = strSQL + " AND quarter = '"+Quarter+ "' ";
		}
		if(Month != null && !"".equals(Month)){
			strSQL = strSQL + " AND month = '"+Month+ "' ";
		}
		
		strSQL = strSQL + " and  comcode='"+codcString+ "'";
		//按照频度筛选
		strSQL = strSQL + " and reportrate = '"+ this.CfReportRate +"' "; 		
		strSQL = strSQL + ") cf  on cfr.REPORTITEMCODE = cf.Outitemcode  "
				+ "left join CfReportElement cfre "
				+ "on cfre.portitemcode=cfr.reportitemcode "
				+ "left join contexttable ct "
				+ "on ct.contextcode=cfre.contextcode "
				+ "WHERE  1 = 1 "
				+ "and cfre.cfreporttype='0'";	//0代表固定因子	
		//通过频度筛选指标描述表中需要报送的因子
		if("1".equals(CfReportRate)){//季度快报
			strSQL = strSQL + " AND cfr.quarterreport = '1' "; 
		}
		else if("2".equals(CfReportRate)){//季度报告
			strSQL = strSQL + " AND cfr.halfyearreport = '1' ";
		}
		else if("3".equals(CfReportRate)){//临时报告
			strSQL = strSQL + " AND cfr.yearreport = '1' ";
		}		
		// 根据系统分类判断哪些因子需要报送
		// 公司类型值就以系统分类字段名称为值
		strSQL = strSQL + " AND cfr." + this.CompanyType + " = '1' ";
		strSQL = strSQL + " ORDER BY CFR.OUTITEMCODE ASC ";		 
		System.out.println("生成xbrl文件的取数的"+strSQL);
		// 因子属性：01 公司偿付能力报告、02 分红险报告、03 公司财务报告
		listCfReportdata = querydao.queryBysql(strSQL);
		if(listCfReportdata.size()<=0){
			System.out.println("报送因子获取失败");
			return false;
		}
		return true;	
	}
	private boolean createExtendStyle(){
		// 01 公司偿付能力报告
		String filePath = createExtendStyle(this.fileDirName);
		if(null == filePath || "".equals(filePath.trim())){						
			return false;						
		}
		fileList_Changf.add(filePath);
		return true;
	}
	private String createExtendStyle(String tFileDirName){
		return tFileDirName;
	}
	private boolean getColDataFromDB(String tableid,String rowno){
		String strSQL = "";
		if("SqlServer".equals(this.DBType)){// SqlServer		
			strSQL = "select cf.ROWNO,CFR.COLTYPE,CFR.COLCODE,CONVERT(char(50), CF.NUMVALUE) NUMVALUE,CONVERT(char(50),CF.WANVALUE) WANVALUE,CF.TEXTVALUE,CF.DESTEXT,CFR.OUTITEMNOTE,CFRE.ELEMENTCODE,CFRE.CONTEXTCODE,CFRE.UNITCODE,CFRE.DECIMALS,CFRE.STARTORENDTYPE,CT.AXIS,CT.MEMBER,CT.LINEMEMBER,CT.REMARK " +
			" FROM CFREPORTROWADDDESC CFR left join (SELECT * FROM CFREPORTROWADDDATA WHERE 1 = 1 " + 
			"  AND QUARTER = '"+ this.Quarter + "' " +
			"  AND YEARMONTH = '" + this.Month + "' " + 
			"  AND REPORTRATE = '" + this.CfReportRate + "' " + 
			"  AND reportId='"+this.reportId+"'"+
			"  AND ROWNO = '" + rowno + "') CF " + 
			"  on UPPER(CFR.TABLECODE) = UPPER(CF.TABLECODE)AND CFR.COLCODE = CF.COLCODE "
			+ "left join CfReportElement cfre "
			+ "on cfre.portitemcode=cfr.COLCODE "
			+ "left join contexttable ct "
			+ "on ct.contextcode=cfre.contextcode "
			+ "WHERE 1=1 "
			+ "and cfre.cfreporttype='1' "	//1代表可扩展行	
			+ "AND UPPER(CFR.TABLECODE) = '" + tableid + "'  ORDER BY cfr.colcode ASC ";
		}else if("Oracle".equals(this.DBType)){// Oracle
			strSQL = "select cf.ROWNO,CFR.COLTYPE,CFR.COLCODE,to_char(CF.NUMVALUE) NUMVALUE,to_char(CF.WANVALUE) WANVALUE,CF.TEXTVALUE,CF.DESTEXT,CFR.OUTITEMNOTE,CFRE.ELEMENTCODE,CFRE.CONTEXTCODE,CFRE.UNITCODE,CFRE.DECIMALS,CFRE.STARTORENDTYPE,CT.AXIS,CT.MEMBER,CT.LINEMEMBER,CT.REMARK " +
			" FROM CFREPORTROWADDDESC CFR left join (SELECT * FROM CFREPORTROWADDDATA WHERE 1 = 1 " + 
			"  AND QUARTER = '"+ this.Quarter + "' " +
			"  AND YEARMONTH = '" + this.Month + "' " + 
			"  AND REPORTRATE = '" + this.CfReportRate + "' " + 
			"  AND reportId='"+this.reportId+"'"+
			"  AND ROWNO = '" + rowno + "' and comcode='"+ this.organcode +"' ) CF " + 
			"  on UPPER(CFR.TABLECODE) = UPPER(CF.TABLECODE)AND CFR.COLCODE = CF.COLCODE "
			+ "left join CfReportElement cfre "
			+ "on cfre.portitemcode=cfr.COLCODE "
			+ "left join contexttable ct "
			+ "on ct.contextcode=cfre.contextcode "
			+ "   WHERE 1=1  "
			+ "and cfre.cfreporttype='1' "	//1代表可扩展行	
			+ "AND UPPER(CFR.TABLECODE) = '" + tableid + "'  ORDER BY cfr.colcode ASC ";
		}else{
			System.out.println("系统使用数据库类型获取失败！");
		  	return false;
		}
		System.out.println(strSQL);
		listColData = querydao.queryBysql(strSQL);
		if (listColData.size()<=0) {
			System.out.println("查询行可扩展数据表无记录");
		  	return false;
	    }
		return true;
	}
	//从数据库中获取行数据
	private boolean getRowDataFromDB(String tableid){
		/*--通过表代码取行可扩展数据表中获取数据不为空的行的行号；数据全部为空的行，不报送--*/
		String strSQL = "";
		if("SqlServer".equals(this.DBType)){// SqlServer
			strSQL = "SELECT DISTINCT cf.rowno " +
			" FROM cfreportrowadddata cf WHERE 1= 1 and UPPER(cf.tablecode) = '"+ tableid +"' " +
			" AND (cf.yearmonth) = '" + this.Month + "' " + 
			" AND CF.QUARTER = '" + this.Quarter  + "' " + 
			" AND CF.REPORTRATE = '" + this.CfReportRate + "' " +
			" and cf.reportId='"+this.reportId+"'"+
			" AND (ISNULL(CF.NUMVALUE, 0.00) <> 0.00 OR ISNULL(CF.WANVALUE, 0.00) <> 0.00 OR CF.TEXTVALUE IS NOT NULL OR CF.DESTEXT IS NOT NULL) " + 
			" ORDER BY convert(int,RowNo) ASC ";
		}else if("Oracle".equals(this.DBType)){// Oracle
			strSQL = "SELECT DISTINCT cf.rowno " +
			" FROM cfreportrowadddata cf WHERE 1= 1 and UPPER(cf.tablecode) = '"+ tableid +"' " +
			" AND (cf.yearmonth) = '" + this.Month + "' " + 
			" AND CF.QUARTER = '" + this.Quarter  + "' " + 
			" AND CF.REPORTRATE = '" + this.CfReportRate + "' " +
			" and cf.reportId='"+this.reportId+"'"+
			" AND (ifnull(CF.NUMVALUE, 0.00) <> 0.00 OR ifnull(CF.WANVALUE, 0.00) <> 0.00 OR CF.TEXTVALUE IS NOT NULL OR CF.DESTEXT IS NOT NULL) " + 
			" ORDER BY to_number(RowNo) ASC ";
		}else{
			System.out.println("系统使用数据库类型获取失败！");
		  	return false;
		}
		System.out.println(strSQL);
		listRowData = querydao.queryBysql(strSQL);
		if (listRowData.size()<=0) {
			System.out.println("查询行可扩展数据表cfreportrowadddata无记录");
		  	return false;
	    }
		return true;
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
		listRowAddDesc = querydao.queryBysql(strSQL);
		if(listRowAddDesc.size()<=0){
			System.out.println("从cfreportrowadddesc获取报送因子获取失败");
			return false;
		}
		return true;
	}
	private boolean createReportDoc(){
		// 01 公司偿付能力报告
		String filePath = createReportDoc(this.fileDirName);
		if(null == filePath || "".equals(filePath.trim())){
			return false;
		}
		fileList_Changf.add(filePath);
		return true;
	}
	//创建word文件
	private String createReportDoc(String tFileDirName)
	{
		// 生成Word文件
		String despath = reportWork + "/doc/" + tFileDirName + "/" + organcode
				+ "/文件型因子上报.doc";
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(despath);
		} catch (FileNotFoundException e) {
			System.out.println("创建输出文件失败");
			e.printStackTrace();
			return "";
		}
		com.sinosoft.common.text.Document document = new com.sinosoft.common.text.Document(PageSize.A4);
		RtfWriter2.getInstance(document,outputStream);
		document.open();

		// 遍历需要生成文件型的因子
		int nRows = listCfReportdata.size();
		for( int cRow = 0 ; cRow < nRows ; cRow ++ )
		{
			Map<?,?> m=(Map<?,?>)listCfReportdata.get(cRow);
			String outItemType = (String) m.get("OUTITEMTYPE");
			if("03".equals(outItemType))
			{
			    try {			    	
					//String outItemCode = (String) m.get("OUTITEMCODE");
					String outItemCode = (String) m.get("REPORTITEMCODE");
					System.out.println("outItemCode = " + outItemCode);
					String tag_begin = "<%" + outItemCode + "%>";
					String tag_end = "</%" + outItemCode + "%>";
					
					String contextString = "因子取值说明：" + (String) m.get("OUTITEMNOTE");
				     // 正文格式左对齐 
				     // context.setAlignment(Element.ALIGN_LEFT); 
				     // context.setFont(contextFont); 
				     // 离上一段落（标题）空的行数 
				     // context.setSpacingBefore(5); 
				     // 设置第一行空的列数 
				     // context.setFirstLineIndent(20); 
					document.add(new Paragraph(tag_begin));
					document.add(new Paragraph("\n"));
					document.add(new Paragraph(contextString));
					document.add(new Paragraph("\n"));
					document.add(new Paragraph(tag_end));
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
		}	
		listCfReportdata.removeAll(listCfReportdata);
		document.close();		
		return despath;
	}
	private boolean createReportZIP(){
        //源文件
        File f1=new File(reportWork+"CfReport/"+this.fileDirName+"/"+organcode+"/item.xml");
        File f2=new File(reportWork+"CfReport/"+this.fileDirName+"/"+organcode+"/row.xml");
        //修改
        File f3=new File(reportWork+"CfReport/"+this.fileDirName+"/"+organcode+"/文件型因子上报.doc");
        System.out.println("-------------"+f3);
        File[] srcfile={f1,f2,f3};
        //压缩后的文件
        File zipfile=new File(reportWork+"CfReport/"+this.fileDirName+"/"+organcode+"/"+this.fileDirName+".zip");
        System.out.println("fileDirName----------------------------："+fileDirName);
        CreateZIPUtil.zipFiles(srcfile, zipfile);
		return true;
	}

}
