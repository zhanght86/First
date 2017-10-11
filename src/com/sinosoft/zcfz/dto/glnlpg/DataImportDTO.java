package com.sinosoft.zcfz.dto.glnlpg;


import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

public class DataImportDTO {
	public static final String FileDir = "/upload";

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
    //cao 重新命名
    private String temp;
    //cao工具名
    private String utilName;
    
    //上传总分割数 
    private int chunks;
	//上传分割块数
	private int chunk; 
	// 监测频率
	private String facType;
	// 部门
	private String deptNo;
	// 年
	private String facYear;
	// 月
	private String facMonth;
	// 季
	private String facQuarter;
	// 上传时间
	private String uploadTime;
	// 上传操作人
	private String uploadUser;
	// 机构代码
	private String companyLevel;
	// 测试类型
	private String testType;
	private String comCode;
	private String field;
	private String title;
	private Integer width;
	
	private String flag;//管理员标志
	private String branchFlag;//机构标志
	private String monitorcode; //监控频率代码
	
	private String tempType;//模板类型
	
	
	public String getTempType() {
		return tempType;
	}

	public void setTempType(String tempType) {
		this.tempType = tempType;
	}

	private long uerId;
	
	public long getUerId() {
		return uerId;
	}

	public void setUerId(long uerId) {
		this.uerId = uerId;
	}

	public String getMonitorcode() {
		return monitorcode;
	}

	public void setMonitorcode(String monitorcode) {
		this.monitorcode = monitorcode;
	}

	public String getUtilName() {
		return utilName;
	}

	public void setUtilName(String utilName) {
		this.utilName = utilName;
	}

	public String getTemp() {
		return temp;
	}

	public void setTemp(String temp) {
		this.temp = temp;
	}

	public String getBranchFlag() {
		return branchFlag;
	}

	public void setBranchFlag(String branchFlag) {
		this.branchFlag = branchFlag;
	}

	public String getTestType() {
		return testType;
	}

	public void setTestType(String testType) {
		this.testType = testType;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public DataImportDTO(){
	}
	
	public DataImportDTO(String field, String title, Integer width) {
		this.field = field;
		this.title = title;
		this.width = width;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public String getComCode() {
		return comCode;
	}

	public void setComCode(String comCode) {
		this.comCode = comCode;
	}

	public String getFacType() {
		return facType;
	}

	public void setFacType(String facType) {
		this.facType = facType;
	}
	public String getFacYear() {
		return facYear;
	}

	public String getDeptNo() {
		return deptNo;
	}

	public void setDeptNo(String deptNo) {
		this.deptNo = deptNo;
	}

	public void setFacYear(String facYear) {
		this.facYear = facYear;
	}

	public String getFacMonth() {
		return facMonth;
	}

	public void setFacMonth(String facMonth) {
		this.facMonth = facMonth;
	}

	public String getFacQuarter() {
		return facQuarter;
	}

	public void setFacQuarter(String facQuarter) {
		this.facQuarter = facQuarter;
	}

	public String getUploadTime() {
		return uploadTime;
	}



	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}

	public String getUploadUser() {
		return uploadUser;
	}

	public void setUploadUser(String uploadUser) {
		this.uploadUser = uploadUser;
	}

	public String getCompanyLevel() {
		return companyLevel;
	}

	public void setCompanyLevel(String companyLevel) {
		this.companyLevel = companyLevel;
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

	@Override
	public String toString() {
		return "DataImportDTO [request=" + request + ", multipartFile=" + multipartFile + ", name=" + name + ", date="
				+ date + ", fileName=" + fileName + ", filePath=" + filePath + ", newfileName=" + newfileName
				+ ", remark=" + remark + ", temp=" + temp + ", utilName=" + utilName + ", chunks=" + chunks + ", chunk="
				+ chunk + ", facType=" + facType + ", deptNo=" + deptNo + ", facYear=" + facYear + ", facMonth="
				+ facMonth + ", uploadTime=" + uploadTime + ", uploadUser=" + uploadUser
				+ ", companyLevel=" + companyLevel + ", testType=" + testType + ", comCode=" + comCode + ", field="
				+ field + ", title=" + title + ", width=" + width + ", flag=" + flag + ", branchFlag=" + branchFlag
				+ "]";
	}

	

}
