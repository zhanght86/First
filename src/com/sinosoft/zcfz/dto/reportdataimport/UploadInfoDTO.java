package com.sinosoft.zcfz.dto.reportdataimport;


import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

public class UploadInfoDTO {
	//参考plupload属性
	private HttpServletRequest request; 
	private MultipartFile multipartFile;
	private String name;
	//日期
	private String date;
	//文件名称
	private String fileName;
	//上传文件路径
    private String filePath;
	//上传后保存的文件名称
	private String newfileName;
    //备注
    private String remark;
    public static final String FileDir = "/upload";

    //上传总分割数 
    private int chunks;
	//上传分割块数
	private int chunk; 
	//reportcode值
	private String reportcode;
	//报表类别
	private String reporttype;
	//报表名称
	private String reportname;
	//年度
	private String yearmonth;
	//季度
	private String quarter;
	//部门信息:all-所有部门;account-财务部;investment-投资部;actuarial-精算部
	private String department;
	
	private String subject; //科目
	private String financeTable;
	private String investTable;
	private String reportId;
	//机构编码
	private String companycode;	
	//报表类型
	private String reportcategory;
	
	public String getReportcategory() {
		return reportcategory;
	}
	public void setReportcategory(String reportcategory) {
		this.reportcategory = reportcategory;
	}
	public String getCompanycode() {
		return companycode;
	}
	public void setCompanycode(String companycode) {
		this.companycode = companycode;
	}
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getFinanceTable() {
		return financeTable;
	}
	public void setFinanceTable(String financeTable) {
		this.financeTable = financeTable;
	}
	public String getInvestTable() {
		return investTable;
	}
	public void setInvestTable(String investTable) {
		this.investTable = investTable;
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getReporttype() {
		return reporttype;
	}
	public String getReportcode() {
		return reportcode;
	}
	public void setReportcode(String reportcode) {
		this.reportcode = reportcode;
	}
	public void setReporttype(String reporttype) {
		this.reporttype = reporttype;
	}
	public String getReportname() {
		return reportname;
	}
	public void setReportname(String reportname) {
		this.reportname = reportname;
	}

	public String getYearmonth() {
		return yearmonth;
	}
	public void setYearmonth(String yearmonth) {
		this.yearmonth = yearmonth;
	}
	public String getQuarter() {
		return quarter;
	}
	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}

	public int getChunks() {
		return chunks;
	}
	public void setChunks(int chunks) {
		this.chunks = chunks;
	}
	public int getChunk() {
		return chunk;
	}
	public void setChunk(int chunk) {
		this.chunk = chunk;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String filePath) {
		this.fileName=filePath;
	}
	public String getNewfileName() {
		return newfileName;
	}
	public void setNewfileName(String newfileName) {
		this.newfileName = newfileName;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public MultipartFile getMultipartFile() {
		return multipartFile;
	}
	public void setMultipartFile(MultipartFile multipartFile) {
		this.multipartFile = multipartFile;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public static String getFiledir() {
		return FileDir;
	}
	@Override
	public String toString() {
		return "UploadInfoDTO [request=" + request + ", multipartFile="
				+ multipartFile + ", name=" + name + ", date=" + date
				+ ", fileName=" + fileName + ", filePath=" + filePath
				+ ", newfileName=" + newfileName + ", remark=" + remark
				+ ", chunks=" + chunks + ", chunk=" + chunk + ", reportcode="
				+ reportcode + ", reporttype=" + reporttype + ", reportname="
				+ reportname + ", yearmonth=" + yearmonth + ", quarter="
				+ quarter + ", department=" + department + ", subject="
				+ subject + ", financeTable=" + financeTable + ", investTable="
				+ investTable + "]";
	}	
	
	
}
