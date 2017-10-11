package com.sinosoft.controller.zcfz.lianghua;

import java.io.File;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sinosoft.common.InvokeResult;
import com.sinosoft.common.Page;
import com.sinosoft.zcfz.dto.reportform.DownloadDto;
import com.sinosoft.zcfz.dto.reportform.QueryUploadFilesDTO;
import com.sinosoft.zcfz.service.lianghua.LianghuaUploadFilesQueryService;
import com.sinosoft.entity.CfUploadDocument;
import com.sinosoft.entity.UploadFile;
import com.sinosoft.util.UploaderServlet;

@Controller
@RequestMapping("/uploadFiles")
public class LianghuaUploadFilesQueryController {
	@Resource
	private LianghuaUploadFilesQueryService upload;
	@Resource
	private UploaderServlet uploaderServlet;
    
	@ResponseBody
	@RequestMapping(path="/list")
	public Page<?> getErrorInfo(@RequestParam int page, @RequestParam int rows,QueryUploadFilesDTO dto){
		System.out.println(dto.getQuarter() + " : " +dto.getReportname()+ " : " +dto.getReporttype() +" : " + dto.getYear());
		
		return upload.getUploadFiles(page, rows, dto);
	}
	
	@ResponseBody
	@RequestMapping(value="/download")
	public InvokeResult download(DownloadDto dto, HttpServletRequest request,HttpServletResponse response){	
/*	   //多线程断点下载	
        DownloadInfo2 bean = new DownloadInfo2(request, response, dto.getDlFilePath(), dto.getDlFileName() );
        String filePath = dto.getDlFilePath() + File.separator;
		
		File file = new File(filePath +  dto.getDlFileName());
		
		if(!file.exists()){
			return InvokeResult.failure("文件下载失败，原因是：文件未找到");
		}
		
        try {
        	BatchDownloadFile2 down = new BatchDownloadFile2(bean);
            new Thread(down).start();
			return InvokeResult.success();
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure("文件下载失败，原因是："+e.getMessage()+"。"); 
		}*/
		CfUploadDocument document = upload.getUploadByID(dto.getUploadNo());
		String[] filesPath = dto.getDlPath().split(",");
		int length = Integer.parseInt(dto.getLength());
		File[] files = new File[length];
		
		//如果只下载单个文件，就普通下载
//		if(filesPath.length == 1){
			UploadFile downFile=new UploadFile();
			
		File file = new File(document.getFilepath());
			if(!file.exists()){
				return InvokeResult.failure("文件下载失败，原因是：文件未找到");
			}
			
			String path = filesPath[0];
			String fileName = path.substring(spiltString(path)+1, path.length());
			String filePath = path.substring(0, spiltString(path)+1);
		downFile.setFileName(document.getFilename());
		downFile.setFilePath(document.getFilepath());
			
			try {
				uploaderServlet.download(request,response,downFile);
				return InvokeResult.success();
			} catch (Exception e) {
				e.printStackTrace();
				return InvokeResult.failure("文件下载失败，原因是："+e.getMessage()+"。"); 
			}
//		}
		
/*		//如果下载多个文件，则打包下载
		for(int i=0; i<filesPath.length; i++) {
			System.out.println("---------------" + filesPath[i]);
			File file = new File(filesPath[i]);
			if(!file.exists()){
				return InvokeResult.failure("文件下载失败，原因是："+filesPath[i]+"文件未找到");
			}
			
			files[i]= file;
		}

		try {
			uploaderServlet.zipDownFile(request,response,files);
			return InvokeResult.success();
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure("文件下载失败，原因是："+e.getMessage()+"。"); 
		}*/
		
	}
	
	//获取路径下最后一个斜杠
    private int spiltString(String path){
        int linux = path.lastIndexOf("/");
        int windows = path.lastIndexOf("\\");     
        int length;
        
        if(linux >= windows){
            length = linux;
        } else {
            length = windows;
        }
        
        return length;
    }
	@RequestMapping(path="/downloadFilebill",method=RequestMethod.POST)
	public void downloadLine(HttpServletRequest request,
			HttpServletResponse response, @RequestParam String name,
			@RequestParam String queryConditions, @RequestParam String cols) {
		upload.downCheckResult(request, response, name,queryConditions, cols);
	}

}
