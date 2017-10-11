package com.sinosoft.controller.zcfz.interfaces;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sinosoft.common.InvokeResult;
import com.sinosoft.zcfz.dto.interfase.ImpDataInfoDTO;
import com.sinosoft.zcfz.service.interfaces.InterfaceService;
import com.sinosoft.util.Config;
import com.sinosoft.util.UploaderServlet;

@Controller
@RequestMapping(path="/InterDeal")
public class InterfacesController {
	@Resource
	private UploaderServlet us;
	@Resource
	private   InterfaceService   service;
	
	@RequestMapping(path="/impInvestData")
	@ResponseBody
	public void impInvestData(ImpDataInfoDTO info,HttpServletRequest request,HttpServletResponse response){
		System.out.println("开始上传");
		//上传文件			
	    try {
	    	info.setRequest(request);
	    	info.setFilePath(Config.getProperty("uploaddir")+"/"+"interfacedataupload");//文件上传地址
	    	//上传文件
			us.upload(info);
			//保存数据到接口表中
			info.setDate(us.getDate());
			service.saveInvestData(info);
			us.setFileName(null);
			response.getWriter().write("{\"status\":true,\"newName\":\""+us.getFileName()+"\"}");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				response.getWriter().write("{\"status\":false}");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	@RequestMapping(path="/impFinanceData")
	@ResponseBody
	public void impFianData(ImpDataInfoDTO info,HttpServletRequest request,HttpServletResponse response){
		System.out.println("开始上传");
		//上传文件			
	    try {
	    	info.setRequest(request);
	    	info.setFilePath(Config.getProperty("uploaddir")+"/"+"interfacedataupload");//文件上传地址
	    	//上传文件
			us.upload(info);
			//保存数据到接口表中
			info.setDate(us.getDate());
			service.saveFinaData(info);
			us.setFileName(null);
			//return InvokeResult.success();
			response.getWriter().write("{\"status\":true,\"newName\":\""+us.getFileName()+"\"}");
	    } catch (Exception e) {
			// TODO Auto-generated catch block
			//return InvokeResult.failure("数据上传失败，原因是："+e.getMessage()+"。");
			try {
				response.getWriter().write("{\"status\":false}");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	/**
	 * 接口数据处理，将接口表数据处理为中间表数据
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(path = "/datadeal", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult interfaceDataDeal(HttpServletRequest request,HttpServletResponse response){
		String reportid = request.getParameter("reportId");
		String impdate = request.getParameter("impdate");
		if(reportid.equals("")){
			return InvokeResult.failure("报送号不能为空");
		}
		String[] result = service.dealInterfaceData(reportid, impdate);
		if(result[0].equals("1")){
			return InvokeResult.success();
		}else{
			return InvokeResult.failure(result[1]);
		}
	}
}
