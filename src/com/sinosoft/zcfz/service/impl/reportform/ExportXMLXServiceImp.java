package com.sinosoft.zcfz.service.impl.reportform;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.annotation.Resource;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sinosoft.common.InvokeResult;
import com.sinosoft.common.text.DocumentException;
import com.sinosoft.common.text.PageSize;
import com.sinosoft.common.text.Paragraph;
import com.sinosoft.common.text.rtf.RtfWriter2;
import com.sinosoft.zcfz.dao.reportdataimport.CfReportDataDAO;
import com.sinosoft.zcfz.dao.reportdataimport.CfReportRowAddDataDAO;
import com.sinosoft.zcfz.dao.reportform.ExportXMLQueryDAO;
import com.sinosoft.zcfz.dto.reportform.ReportCreateInfoDTO;
import com.sinosoft.zcfz.service.reportform.ExportXMLService;
import com.sinosoft.util.Config;
import com.sinosoft.util.CreateZIPUtil;

@Service
public class ExportXMLXServiceImp implements ExportXMLService{
	private String roottag =  "";// 根标签，为DATA+6位机构代码+6位年月代码（200912）		
	private String areaid = "";//  数据机构，值为12位数字，前6位是机构代码，后6位是地区代码	
	private String organcode = ""; // 机构代码		
	private XMLOutputter xmlOut = null;				
	private String YearCode = "";// 	
	private String CfReportRate = "";// 频度：1-季度快报，2-季度报告，3-临时报告
	private String Quarter = "";// 报送季度	
	private String Month = "";// 月度	
	private String reportId = "";// 报送号
	private String CompanyType = "";// 报送公司所属类	
	private boolean bIsChangf = false;// 01 公司偿付能力报告	
	private boolean bIsFenh = false;// 02 分红险报告
	private boolean bIsCaiW = false;//03 公司财务报告 	
	private boolean bIsGrpW = false;//04 集团偿付能力报告	
	private String fileDirName = "";	
	private String DBType = "";// 系统使用数据库类型		
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

	@Transactional
	public InvokeResult reportCreate(ReportCreateInfoDTO info) {
		// 得到外部传入数据
		getInputData(info);
		
	    // 获取系统使用数据库类型
	    setDBType();
	    // 获取机构代码
	    setOrgancode();
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
		
		File f = new File(Config.getProperty("ReportWork")+"CfReport/"+fileDirName );
		if(!f.exists())
		{
			f.mkdir();
		}
		if(!checkDataIsNull())// 检查所有数据是否为空
		{
			return InvokeResult.failure("没有可生成报送文件的数据，请确认季度数据已上传。");
		}
		if(!createStaticStyle())// 生成固定因子XML
		{
			return InvokeResult.failure("固定因子XML文件生成失败。");
		}
		
		if(!createExtendStyle())// 生成行可扩展XML
		{
			return InvokeResult.failure("行可扩展XML文件生成失败。");
		}
		if(!createReportDoc())// 生成Word文件
		{
			return InvokeResult.failure("Word文件生成失败。");
		}		
		if(!createReportZIP())// 压缩文件
		{
			return InvokeResult.failure("报送文件ZIP生成失败。");
		}		
		return InvokeResult.success();
	}
	private void getInputData(ReportCreateInfoDTO info){
	    this.YearCode = info.getYear();
	    this.CfReportRate = info.getReportType();
	    this.Quarter = info.getQuarter();
	    this.reportId=info.getReportId();
	    fileDirName = this.CfReportRate+"-"+this.YearCode+"-"+this.Quarter;
	}
	//获取数据库连接类型
	private void setDBType(){
		this.DBType = Config.getProperty("DBType");
	}
	//获取机构代码
	private void setOrgancode() {
		this.organcode = Config.getProperty("OrganCode");
		System.out.println("机构代码："+this.organcode);
	}
	//设置月份
	private void setMonth()
	{
		/*
		 * if("1".endsWith(this.Quarter)) { this.Month = this.YearCode + "03"; }
		 * else if("2".endsWith(this.Quarter)) { this.Month = this.YearCode +
		 * "06"; } else if("3".endsWith(this.Quarter)) { this.Month =
		 * this.YearCode + "09"; } else if("4".endsWith(this.Quarter)) {
		 * this.Month = this.YearCode + "12"; }
		 */
		this.Month = this.YearCode;
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
	    	this.CompanyType="Reinsurance";
	    }else if(this.organcode.equals("000009")){//寿再
	    	this.CompanyType="Reinsurance";
	    }else{
	    	this.CompanyType="Asset";//资产
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
				+"' and comcode='"+this.organcode
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
			         this.organcode+"' and reportId='"+
			         this.reportId+"'";
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
	private boolean createStaticStyle(){
	    String filePath = createStaticStyle(this.fileDirName);
		if(null == filePath || "".equals(filePath.trim())){						
			return false;						
		}
		fileList_Changf.add(filePath);
		return true;
	}
	private String createStaticStyle(String tFileDirName){
		getItemDataFromDB();
		Element elementRoot = new Element(this.roottag);
		Element elFirstLevel = new Element("area");
		elFirstLevel.addContent(new Element("areaid").addContent(this.areaid));
		int nRows = listCfReportdata.size();
		for(int cRow = 0;cRow<nRows;cRow++){
			Element elSecondLevel = new Element("item");
			Map<?,?> m=(Map<?,?>)listCfReportdata.get(cRow);
			String OutItemType = (String) m.get("OUTITEMTYPE");
			System.out.println(m.get("OUTITEMCODE")+"-"+m.get("REPORTITEMCODE")+"-"+m.get("to_char(CF.REPORTITEMVALUE)")+"-"+m.get("to_char(CF.REPORTITEMWANVALUE)")+"-"+m.get("TEXTVALUE")+"-"+m.get("OUTITEMTYPE")+"-"+m.get("OUTITEMNOTE")+"-"+m.get("DESTEXT"));
			// 根据因子值类型取值
			String strValue = "";
			if("06".equals(OutItemType)){// 06 百分比型
				strValue = (String) m.get("REPORTITEMVALUE");
				if( null != strValue && !"".equals(strValue.trim())){
					strValue = (new DecimalFormat("#0.0000")).format(Double.parseDouble(strValue));
				}
			}else if("05".equals(OutItemType)){//05 数量型
				strValue = (String) m.get("REPORTITEMVALUE");
				if( null != strValue && !"".equals(strValue.trim())){
					strValue = (new DecimalFormat("#0")).format(Double.parseDouble(strValue));
				}
			}else if("04".equals(OutItemType)){//04 数值型
				strValue = (String) m.get("REPORTITEMVALUE");
				if( null != strValue && !"".equals(strValue.trim())){
					strValue = (new DecimalFormat("#0.00")).format(Double.parseDouble(strValue));
				}
			}else if("03".equals(OutItemType)){//03 文件型，不包含在XML报文中
				continue;
			}else if("02".equals(OutItemType)){//02 描述型
				strValue = "\n" + m.get("DESTEXT") + "\n";
			}else if("01".equals(OutItemType)){//01 短文本型
				strValue = (String) m.get("TEXTVALUE");
			}else{
				System.out.println("未获取到因子值类型.");
			}
			
			if( null == strValue || "".equals(strValue) || "0.00".equals(strValue)){
				strValue = "-";
			}
			Element elThirdLevel_A = new Element("value").addContent(strValue);
			Element elThirdLevel_B = new Element("remark").addContent((String) m.get("OUTITEMNOTE"));
			Element elThirdLevel_C = new Element("PK");
			elThirdLevel_C.addContent(new Element("key").addContent((String) m.get("OUTITEMCODE")));			
			/*系统频度值的设定与保监会报文频度设定不一致，需要调整*/
			String strIntervaltype = "";
			if( "1".equals(this.CfReportRate)){
				strIntervaltype = "5";
			}
			else if( "2".equals(this.CfReportRate) ){
				strIntervaltype = "3";
			}
			else if( "3".equals(this.CfReportRate) ){
				strIntervaltype = "4";
			}
			elThirdLevel_C.addContent(new Element("intervaltype").addContent( strIntervaltype ));
			elSecondLevel.addContent(elThirdLevel_C);
			elSecondLevel.addContent(elThirdLevel_A);
			elSecondLevel.addContent(elThirdLevel_B);				
			elFirstLevel.addContent( elSecondLevel );
			m.clear();
		}
		elementRoot.addContent(elFirstLevel);
		
		Document doc = new Document(elementRoot);
		String filePath = outPut(doc,tFileDirName+"/"+organcode+"/item",tFileDirName+"/"+organcode);// 文件输出
		return filePath;
	}
	//输出XML
	private String outPut(Document doc,String DesFileName,String DesFolderName){
		String despath= "";
		try {
			xmlOut = new XMLOutputter("  ",true);		
			xmlOut.setEncoding("GBK");
			despath= Config.getProperty("ReportWork")+"CfReport/"+DesFileName+".xml";
			System.out.println("XML导出路径："+despath);
			System.out.println("DesFileName："+DesFileName);
			/*---判断文件路径是否存在，否则生成--begin---*/
			File f = new File(Config.getProperty("ReportWork")+"CfReport/"+DesFolderName );
			if(!f.exists()){
				f.mkdir();
			}
			/*---判断文件路径是否存在，否则生成--end---*/
			FileWriter writer = new FileWriter(despath);
			
			xmlOut.output(doc, writer);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		return despath;
	}
	//从数据库中获取因子数据
	private boolean getItemDataFromDB(){		
		// 根据系统使用数据库类型，SQL语句不一致
		String strSQL = "";
		if("SqlServer".equals(this.DBType)){// SqlServer
			strSQL = " SELECT CFR.OUTITEMCODE,CFR.REPORTITEMCODE,CONVERT(char(50), CF.REPORTITEMVALUE),CONVERT(char(50), CF.REPORTITEMWANVALUE),CF.TEXTVALUE,CFR.OUTITEMTYPE,CFR.OUTITEMNOTE,CF.DESTEXT"
						+ " FROM CFREPORTITEMCODEDESC cfr left join (SELECT * FROM CFREPORTDATA WHERE 1 =1  ";
		}
		else if("Oracle".equals(this.DBType)){// Oracle
			strSQL = " SELECT CFR.OUTITEMCODE,CFR.REPORTITEMCODE,to_char(CF.REPORTITEMVALUE),to_char(CF.REPORTITEMWANVALUE),CF.TEXTVALUE,CFR.OUTITEMTYPE,CFR.OUTITEMNOTE,CF.DESTEXT"
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
		strSQL = strSQL + " and  comcode='"+organcode + "'";
		//按照频度筛选
		strSQL = strSQL + " and reportrate = '"+ this.CfReportRate +"' "; 		
		strSQL = strSQL + ") cf  on cfr.Outitemcode = cf.Outitemcode  WHERE  1 = 1 ";		
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
		System.out.println(strSQL);
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
		if(!getExtendTableCode()){
			return "";
		}
		Element elementRoot = new Element(this.roottag);
		/*系统频度值的设定与保监会报文频度设定不一致，需要调整*/
		String strIntervaltype = "";
		if( "1".equals(this.CfReportRate)){
			strIntervaltype = "5";
		}else if( "2".equals(this.CfReportRate) ){
			strIntervaltype = "3";
		}else if( "3".equals(this.CfReportRate) ){
			strIntervaltype = "4";
		}
		Element elFirstLevel_A = new Element("intervaltype").addContent(strIntervaltype);
		Element elFirstLevel_B = new Element("area");		
		Element elSecondLevel_A = new Element("areaid").addContent(this.areaid);
		elFirstLevel_B.addContent(elSecondLevel_A);
		
		for(int nIndex=0;nIndex<listRowAddDesc.size();nIndex++){
			Map<?,?> m=(Map<?,?>)listRowAddDesc.get(nIndex);
			String strTableCode = (String) m.get("UPPER(cfr.tablecode)");
            // 通过表代码(tablecode)获取行号
			if(!getRowDataFromDB(strTableCode)){
				continue;
			}
			int rowscount = listRowData.size();
			// 没有数据的行可扩展表，整表都不报送
			if( rowscount > 0 ){
				Element elSecondLevel_B = new Element("table");
				Element elThirdLevel_A = new Element("tableid").addContent(strTableCode);
				Element elThirdLevel_B = new Element("rows");				
				for(int rownum = 0 ; rownum < rowscount ; rownum ++ ){
					Map<?,?> mr=(Map<?,?>)listRowData.get(rownum);
					String rowno = (String) mr.get("rowno");					
					Element elFourthLevel_B = new Element("row");
					Element elFifthLevel_A = new Element("rowindex").addContent( rowno );
					elFourthLevel_B.addContent(elFifthLevel_A);
					// 通过行号获取该行，所有列的数据
					if(!getColDataFromDB(strTableCode,rowno)){
						return "";
					}
					int colscount = listColData.size();
					for(int colnum = 0 ; colnum < colscount ; colnum ++ ){
						Map<?,?> mc=(Map<?,?>)listColData.get(colnum);
						String ColType = (String) mc.get("COLTYPE");
						String colcode = (String) mc.get("COLCODE");
						// 根据列类型取值
						String NumValue = "";
						if("06".equals(ColType)){// 06 百分比型
							NumValue = (String) mc.get("to_char(CF.NUMVALUE)");
							if( null != NumValue && !"".equals(NumValue.trim())){
								NumValue = (new DecimalFormat("#0.0000")).format(Double.parseDouble(NumValue));
							}
						}else if("05".equals(ColType)){// 05 数量型
							NumValue = (String) mc.get("to_char(CF.NUMVALUE)");
							if( null != NumValue && !"".equals(NumValue.trim())){
								NumValue = (new DecimalFormat("#0")).format(Double.parseDouble(NumValue));
							}
						}else if("04".equals(ColType)){// 04 数值型
							NumValue = (String) mc.get("to_char(CF.NUMVALUE)");
							if( null != NumValue && !"".equals(NumValue.trim())){
								NumValue = (new DecimalFormat("#0.00")).format(Double.parseDouble(NumValue));
							}
						}else if("01".equals(ColType) || "02".endsWith(ColType)){// 01 短文本类型,02 描述型 						
							NumValue = (String) mc.get("TEXTVALUE");
						}else {
							NumValue = "";
						}
						
						if( null == NumValue || "".equals(NumValue)){
							NumValue = "-";
						}
						String remark = (String) mc.get("OUTITEMNOTE");
						
						Element elFifthLevel_B = new Element("field");	
						Element elSixthLevel_A = new Element("fieldname").addContent(colcode);
						Element elSixthLevel_B = new Element("value").addContent(NumValue);
						Element elSixthLevel_C = new Element("remark").addContent(remark);
						
						elFifthLevel_B.addContent(elSixthLevel_A);
						elFifthLevel_B.addContent(elSixthLevel_B);
						elFifthLevel_B.addContent(elSixthLevel_C);						
						elFourthLevel_B.addContent(elFifthLevel_B);						
					}
					listColData.removeAll(listColData);
					elThirdLevel_B.addContent(elFourthLevel_B);			
				}
				listRowData.removeAll(listRowData);
				elSecondLevel_B.addContent(elThirdLevel_A);
				elSecondLevel_B.addContent(elThirdLevel_B);
				elFirstLevel_B.addContent(elSecondLevel_B);
			}
		}
		listRowAddDesc.removeAll(listRowAddDesc);
		elementRoot.addContent(elFirstLevel_A);
		elementRoot.addContent(elFirstLevel_B);		
		Document doc = new Document(elementRoot);
		String filePath =  outPut(doc,tFileDirName+"/"+organcode+"/row",tFileDirName+"/"+organcode);// 文件输出		
		return filePath;
	}
	private boolean getColDataFromDB(String tableid,String rowno){
		String strSQL = "";
		if("SqlServer".equals(this.DBType)){// SqlServer		
			strSQL = "select cf.ROWNO,CFR.COLTYPE,CFR.COLCODE,CONVERT(char(50), CF.NUMVALUE),CONVERT(char(50),CF.WANVALUE),CF.TEXTVALUE,CF.DESTEXT,CFR.OUTITEMNOTE " +
			" FROM CFREPORTROWADDDESC CFR left join (SELECT * FROM CFREPORTROWADDDATA WHERE 1 = 1 " + 
			"  AND QUARTER = '"+ this.Quarter + "' " +
			"  AND YEARMONTH = '" + this.Month + "' " + 
			"  AND REPORTRATE = '" + this.CfReportRate + "' " + 
			"  AND ROWNO = '" + rowno + "' and reportId='"+this.reportId+"') CF " + 
			"  on UPPER(CFR.TABLECODE) = UPPER(CF.TABLECODE)AND CFR.COLCODE = CF.COLCODE " + 
			"   WHERE 1=1  AND UPPER(CFR.TABLECODE) = '" + tableid + "'  ORDER BY cfr.colcode ASC ";
		}else if("Oracle".equals(this.DBType)){// Oracle
			strSQL = "select cf.ROWNO,CFR.COLTYPE,CFR.COLCODE,to_char(CF.NUMVALUE),to_char(CF.WANVALUE),CF.TEXTVALUE,CF.DESTEXT,CFR.OUTITEMNOTE " +
			" FROM CFREPORTROWADDDESC CFR left join (SELECT * FROM CFREPORTROWADDDATA WHERE 1 = 1 " + 
			"  AND QUARTER = '"+ this.Quarter + "' " +
			"  AND YEARMONTH = '" + this.Month + "' " + 
			"  AND REPORTRATE = '" + this.CfReportRate + "' " + 
			"  AND ROWNO = '" + rowno + "' and comcode='"+ this.organcode +"' and reportId='"+this.reportId+"') CF " + 
			"  on UPPER(CFR.TABLECODE) = UPPER(CF.TABLECODE)AND CFR.COLCODE = CF.COLCODE " + 
			"   WHERE 1=1  AND UPPER(CFR.TABLECODE) = '" + tableid + "'  ORDER BY cfr.colcode ASC ";
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
			" and reportId='"+this.reportId+"'"+
			" AND (ISNULL(CF.NUMVALUE, 0.00) <> 0.00 OR ISNULL(CF.WANVALUE, 0.00) <> 0.00 OR CF.TEXTVALUE IS NOT NULL OR CF.DESTEXT IS NOT NULL) " + 
			" ORDER BY (RowNo) ASC ";
		}else if("Oracle".equals(this.DBType)){// Oracle
			strSQL = "SELECT DISTINCT cf.rowno " +
			" FROM cfreportrowadddata cf WHERE 1= 1 and UPPER(cf.tablecode) = '"+ tableid +"' " +
			" AND (cf.yearmonth) = '" + this.Month + "' " + 
			" AND CF.QUARTER = '" + this.Quarter  + "' " + 
			" AND CF.REPORTRATE = '" + this.CfReportRate + "' " +
			" and reportId='"+this.reportId+"'"+
			" AND (nvl(CF.NUMVALUE, 0.00) <> 0.00 OR nvl(CF.WANVALUE, 0.00) <> 0.00 OR CF.TEXTVALUE IS NOT NULL OR CF.DESTEXT IS NOT NULL) " + 
			" ORDER BY (RowNo) ASC ";
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
		String despath= Config.getProperty("ReportWork")+"CfReport/"+tFileDirName+"/"+organcode+"/文件型因子上报.doc";
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
					String outItemCode = (String) m.get("OUTITEMCODE");
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
        File f1=new File(Config.getProperty("ReportWork")+"CfReport/"+this.fileDirName+"/"+organcode+"/item.xml");
        File f2=new File(Config.getProperty("ReportWork")+"CfReport/"+this.fileDirName+"/"+organcode+"/row.xml");
        //修改
        File f3=new File(Config.getProperty("ReportWork")+"CfReport/"+this.fileDirName+"/"+organcode+"/文件型因子上报.doc");
        System.out.println("-------------"+f3);
        File[] srcfile={f1,f2,f3};
        //压缩后的文件
        File zipfile=new File(Config.getProperty("ReportWork")+"CfReport/"+this.fileDirName+"/"+organcode+"/"+this.fileDirName+".zip");
        System.out.println("fileDirName----------------------------："+fileDirName);
        CreateZIPUtil.zipFiles(srcfile, zipfile);
		return true;
	}

}
