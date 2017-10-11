package com.sinosoft.controller.zcfz.reportdataimport;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sinosoft.entity.UploadFile;
import com.sinosoft.util.Config;

@Controller
@RequestMapping(path="/finance")
public class FinanceController {
	// 财务模板下载
		@RequestMapping(path="/model",method=RequestMethod.POST)
		public  void  financeMade(HttpServletRequest request,HttpServletResponse response,UploadFile uf) throws IOException{
			uf.setFileName("财务数据模板.xlsm");
			uf.setFilePath(Config.getProperty("ReportWork")+"mould/excel/");
			BufferedInputStream     bis = null;
			BufferedOutputStream    bos = null;
		    try {
		    	response.setContentType("application/octet-stream");	
				response.setHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(uf.getFileName(),"UTF-8"));
				
				bis=new  BufferedInputStream(new FileInputStream(uf.getFilePath()+uf.getFileName()));
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				if(bis!=null)
				bis.close();
				if(bos!=null)
				bos.close();  
		    }
		}
}
