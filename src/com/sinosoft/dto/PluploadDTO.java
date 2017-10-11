package com.sinosoft.dto;


import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;


public class PluploadDTO {
    private String name;  
    private int chunks;  
    private int chunk;  
    private HttpServletRequest request;  
    private MultipartFile multipartFile;  
    private String fileName;
    private String filePath;
    private String remark;
    
    public static final String FileDir = "/upload";
    
    
  
    public int getChunks() {  
        return chunks;  
    }  
  
    public void setChunks(int chunks) {  
        this.chunks = chunks;  
    }  
  

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public MultipartFile getMultipartFile() {
		return multipartFile;
	}

	public void setMultipartFile(MultipartFile multipartFile) {
		this.multipartFile = multipartFile;
	}


	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getChunk() {
		return chunk;
	}

	public void setChunk(int chunk) {
		this.chunk = chunk;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
