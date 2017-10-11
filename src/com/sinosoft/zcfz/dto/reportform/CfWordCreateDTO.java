package com.sinosoft.zcfz.dto.reportform;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;



public class CfWordCreateDTO {
	//项目路径
	private String rootPath=getRootRealPath();
	//模板路劲
	private String modlePath;
	//下载路径
	private String outPath;
	//文档模板最终路径
	private String templatefile;
	//文档生成最终路径
	private String outputfile;
	private String reportId;
	//年度
	private String mYear;
	//季度
	private String mQuarter;
	//年月--201610
	private String mYearMonth;
	//显示所用日期--2016年10月10
	private String mTableDate;
	// 频度，取值范围{季报：1，年报：2，半年报：4，}
	// 报告类别
	private String mCfReportRate;
	// 报告类型
	private String mCfReportType;
	// 报送公司所属类型
	private String CompanyType;
    //操作人员代码
	private String mOpercode;
	// ip
	private String mIpadress = "";
	//cf2010????
	private String mReportEdition="cf2016";
	//表格中的固定数据
	private HashMap mHashMap_Const=new HashMap<String, Object>();
	
	private String wordType;
	
	public String getTemplatefile() {
		if(templatefile!=null&&!templatefile.equals("")){
		}else{
			setTemplatefile();
		}
		System.out.println(templatefile);
		return templatefile;
	}
	public void setTemplatefile() {
		if ("1".equals(mCfReportRate)) {
			this.templatefile =modlePath
					+ "/寿险公司季度快报偿付能力报告模版2016.doc";
		}else if ("2".equals(mCfReportRate)) {
			this.templatefile =modlePath
					+ "/寿险公司季度报告偿付能力报告模版2016.doc";
		} else {
			this.templatefile =modlePath
					+ "/寿险公司临时报告偿付能力报告模版2016.doc";
		}
	}

	public String getOutputfile() {
		if(outputfile!=null&&!outputfile.equals("")){
		}else{
			try {
				setOutputfile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return outputfile;
	}

	public void setOutputfile() throws IOException {
		File file=new File(outPath);
		if(!file.exists()){
			file.mkdirs();
		}
		this.outputfile =outPath+ "/" + mCfReportRate + "_" + mYear
				+ "_" + mQuarter + ".rtf";;
	}

	public String getmYear() {
		return mYear;
	}

	public void setmYear(String mYear) {
		this.mYear = mYear;
	}

	public String getmQuarter() {
		return mQuarter;
	}

	public void setmQuarter(String mQuarter) {
		
		this.mQuarter = mQuarter;
	}

	public String getmYearMonth() {
		setmYearMonth();
		return mYearMonth;
	}

	public void setmYearMonth() {
		/*if ("1".equals(this.mQuarter)) {
			this.mYearMonth = this.mYear + "03";
		} else if ("2".equals(this.mQuarter)) {
			this.mYearMonth = this.mYear + "06";
		} else if ("3".equals(this.mQuarter)) {
			this.mYearMonth = this.mYear + "09";
		} else if ("4".equals(this.mQuarter)) {
			this.mYearMonth = this.mYear + "12";
		}*/
		this.mYearMonth = this.mYear;
	}

	public String getmTableDate() {
		if(mTableDate.equals("")||mTableDate==null)setmTableDate();
		return mTableDate;
	}

	public void setmTableDate() {
		if ("1".equals(this.mQuarter)) {
			this.mTableDate = this.mYear + "-03-31";
		} else if ("2".equals(this.mQuarter)) {
			this.mTableDate = this.mYear + "-06-30";
		} else if ("3".equals(this.mQuarter)) {
			this.mTableDate = this.mYear + "-09-30";
		} else if ("4".equals(this.mQuarter)) {
			this.mTableDate = this.mYear + "-12-31";
		}

		mTableDate = mTableDate.replaceFirst("-", "年");
		mTableDate = mTableDate.replaceFirst("-", "月");
		mTableDate = mTableDate + "日";
		
		if ("1".equals(mCfReportRate))
		{
			mTableDate = this.mYear + "年";
		}
	}

	public String getmCfReportRate() {
		return mCfReportRate;
	}

	public void setmCfReportRate(String mCfReportRate) {
		//System.out.println(list.get(1));
		this.mCfReportRate = mCfReportRate;
	}

	public String getCompanyType() {
		return CompanyType;
	}

	public void setCompanyType(String companyType) {
		CompanyType = companyType;
	}

	public String getmOpercode() {
		return mOpercode;
	}

	public void setmOpercode(String mOpercode) {
		this.mOpercode = mOpercode;
	}

	public String getmIpadress() {
		return mIpadress;
	}

	public void setmIpadress(String mIpadress) {
		this.mIpadress = mIpadress;
	}

	public String getmReportEdition() {
		return mReportEdition;
	}

	public void setmReportEdition(String mReportEdition) {
		this.mReportEdition = mReportEdition;
	}

	public HashMap getmHashMap_Const() {
		return mHashMap_Const;
	}

	public void setmHashMap_Const(String name,String ename) {
		this.mHashMap_Const.clear();
		this.mHashMap_Const.put("cname", name);
		this.mHashMap_Const.put("cnameen", ename);
		this.mHashMap_Const.put("year", mYear);
		this.mHashMap_Const.put("quarter", mQuarter);
		this.mHashMap_Const.put("curdate", getCurrentDate());
		this.mHashMap_Const.put("tabledate", mTableDate);
		this.mHashMap_Const.put("yearcn", getYearCn());
	}

	public String getmCfReportType() {
		return mCfReportType;
	}

	public void setmCfReportType(String mCfReportType) {
		this.mCfReportType = mCfReportType;
	}
	private String getCurrentDate(){
		SimpleDateFormat format=new SimpleDateFormat("yyyy-mm-dd");
		Date date=new Date();
		String time=format.format(date);
		return time;
		
	}
	public String getYearCn() {
		String var = "";
		HashMap mHashMap_Num = new HashMap();
		mHashMap_Num.put("0", "零");
		mHashMap_Num.put("1", "一");
		mHashMap_Num.put("2", "二");
		mHashMap_Num.put("3", "三");
		mHashMap_Num.put("4", "四");
		mHashMap_Num.put("5", "五");
		mHashMap_Num.put("6", "六");
		mHashMap_Num.put("7", "七");
		mHashMap_Num.put("8", "八");
		mHashMap_Num.put("9", "九");

		char temp[] = this.mYear.toCharArray();
		for (int n = 0; n < temp.length; n++) {
			var = var + (String) mHashMap_Num.get(String.valueOf(temp[n]));
		}
		var = var + "年";
		System.out.println(var);
		return var;
	}
	public String getQuarterCn(){
		String var="";
			HashMap mHashMap_Quarter = new HashMap();
			mHashMap_Quarter.put("1", "第一季度");
			mHashMap_Quarter.put("2", "第二季度");
			mHashMap_Quarter.put("3", "第三季度");
			mHashMap_Quarter.put("4", "第四季度");
			var = var + (String) mHashMap_Quarter.get(this.mQuarter);
			return var;
	}
	public String getRootPath() {
		return rootPath;
	}
	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}
	private String getRootRealPath(){
		Properties prop=new Properties();
		String rootPath="";
		rootPath=getClass().getResource("/").getFile().toString().replaceFirst("/", "");
		rootPath=rootPath.substring(0,rootPath.length()-1);
		rootPath=rootPath.substring(0,rootPath.lastIndexOf("/"));
		rootPath=rootPath.substring(0,rootPath.lastIndexOf("/"));
		// 从properties文件中读取路径
		InputStream is = null;
		  try {
			// FileInputStream fis=new FileInputStream("config.properties");
			 is=this.getClass().getResourceAsStream("/prop/config.properties");
	         prop.load(is);
	         modlePath=rootPath+prop.getProperty("WordModle");
	         outPath=rootPath+prop.getProperty("CreateWord");
//	         is.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(is!=null){
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		 // System.out.println(modlePath);
		 // System.out.println(outPath);
		return rootPath;
		
	}
	
	public String getWordType() {
		return wordType;
	}
	public void setWordType(String wordType) {
		this.wordType = wordType;
	}
	public static void main(String[] args) throws IOException {
	new CfWordCreateDTO().setmCfReportRate("1");
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	
	
}
