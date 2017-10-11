package com.sinosoft.zcfz.dto.interfase;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

public class ImpDataInfoDTO {
    private String interfacetype;
    private String interfacetable;
    private String year;
    private String quarter;
    private String yearmonth;
    private String datadate;
    private String reportId;
    private MultipartFile file;
    private String remark;
    private HttpServletRequest request;
    private String filePath;
    private int chunks;  
    private int chunk;
    private String date;
    
	public String getReportId() {
		return reportId;
	}
	public void setReportId(String reportId) {
		this.reportId = reportId;
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
	public String getDatadate() {
		return datadate;
	}
	public void setDatadate(String datadate) {
		this.datadate = datadate;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getInterfacetype() {
		return interfacetype;
	}
	public void setInterfacetype(String interfacetype) {
		this.interfacetype = interfacetype;
	}
	public String getInterfacetable() {
		return interfacetable;
	}
	public void setInterfacetable(String interfacetable) {
		this.interfacetable = interfacetable;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getQuarter() {
		return quarter;
	}
	public void setQuarter(String quarter) {
		this.quarter = quarter;
	}
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
    
    
}
