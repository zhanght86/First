package com.sinosoft.controller.zcfz.reportform;

import java.io.IOException;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.sinosoft.common.InvokeResult;
import com.sinosoft.zcfz.dto.reportform.CfReportDownloadInfoDTO;
import com.sinosoft.entity.UploadFile;
import com.sinosoft.zcfz.service.impl.reportform.CfReportDownloadServiceImp;
import com.sinosoft.util.UploaderServlet;

@Controller
@RequestMapping("/reportdownload")
public class CfReportDownloadController {
	@Resource
	private CfReportDownloadServiceImp dl;
	@Resource
	private UploaderServlet us;
	@RequestMapping(path="/reportdownload", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult reportDownload(CfReportDownloadInfoDTO info,HttpServletRequest request,HttpServletResponse response){
		try {
			dl.reportDownload(info);
			UploadFile u=new UploadFile();
			u.setFileName(dl.getFileName());
			u.setFilePath(dl.getFilePath());	
			us.download(request, response, u);	
			return InvokeResult.success();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage()+"，文件读写出错。");
		}catch (NullPointerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage()+"，查询数据表出错。");
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
}
