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

import com.sinosoft.entity.UploadFile;
import com.sinosoft.util.Config;

@Controller
@RequestMapping(path="/actuary")
public class ActuaryController {
//  精算数据模板下载
	@RequestMapping(path="/model")
	public   void  actuaryMode(HttpServletRequest   request,HttpServletResponse   response,UploadFile uf) throws IOException{
		uf.setFileName("精算数据模板.xlsm");
		uf.setFilePath(Config.getProperty("ReportWork")+"mould/excel/");
		BufferedInputStream     bis = null;
		BufferedOutputStream    bos = null;
		try {
			response.setContentType("application/octet-stream");
			response.setHeader("Content-disposition", "attachment;filename="+URLEncoder.encode(uf.getFileName(), "utf-8"));
			bis=new    BufferedInputStream(new  FileInputStream(uf.getFilePath()+uf.getFileName()));
			bos=new    BufferedOutputStream(response.getOutputStream());
			byte[]    by=new  byte[2048];
			int       bytesRead;
			while(-1!=(bytesRead=bis.read(by,0,by.length))){
				bos.write(by,0,bytesRead);
			}
			bis.close();
			bos.flush();
			bos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(bis!=null)
			bis.close();
			if(bos!=null)
			bos.close();
		}
		
	}
}
