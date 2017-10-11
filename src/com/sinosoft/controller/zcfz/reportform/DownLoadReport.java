package com.sinosoft.controller.zcfz.reportform;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sinosoft.common.InvokeResult;
import com.sinosoft.zcfz.service.reportform.QuarterReportService;

@Controller
@RequestMapping(path="/downLoadReport")
public class DownLoadReport{
	@Resource
    private   QuarterReportService     quarterReportService;
	@RequestMapping(path="/quarterReport" )
	@ResponseBody
    public   InvokeResult   DataLoad(HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		try {
			String  filepath =  quarterReportService.Loda(request, response);
			return InvokeResult.success(filepath);
			
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure("失败，原因是："+e.getMessage());
		}
    }
	@RequestMapping(path="/downLoad")
	@ResponseBody
	public  void   downLoad(HttpServletRequest request,HttpServletResponse response) throws IOException{
    	String   file = request.getParameter("filepath");
    	int in = file.lastIndexOf("/");
    	String   fileName = file.substring(in+1, file.length());
		BufferedInputStream     bis = null;
		BufferedOutputStream    bos = null;
	    try {
	    	response.setContentType("application/octet-stream");	
	    	response.setHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(fileName,"UTF-8"));			
			bis=new  BufferedInputStream(new FileInputStream(file));
			bos=new  BufferedOutputStream(response.getOutputStream());
			byte[]  by=new byte[2048];
			int     bytesRead;
			while(-1!=(bytesRead=bis.read(by,0,by.length))){
				bos.write(by, 0, bytesRead);
			}
			bis.close();
			bos.flush();	
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}finally { 
			if(bis!=null){
				bis.close();  
			}
			if(bos!=null){
				bos.close();
			}
			
	    }
    }
	
    
}
