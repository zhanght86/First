package com.sinosoft.controller.zcfz.inetfase;

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
import com.sinosoft.zcfz.dto.interfase.CUX_SINO_INTERFACE_DTO;
import com.sinosoft.zcfz.dto.interfase.ImpDataInfoDTO;
import com.sinosoft.zcfz.service.interfase.Cux_Sino_Interface_Service;
import com.sinosoft.util.Config;
import com.sinosoft.util.UploaderServlet;

@Controller
@RequestMapping(path="/cux_sino")
public class InterfaceController {
	@Resource
	private UploaderServlet us;
	@Resource
	private   Cux_Sino_Interface_Service   service;
	@RequestMapping(path="/list")
	@ResponseBody
    public   Page<?>    queryByPage(@RequestParam  int page ,@RequestParam int rows ,  CUX_SINO_INTERFACE_DTO  dto){
    	return  service.queryByPage(page, rows, dto);
    }
	@RequestMapping(path="/impinvestdata")
	@ResponseBody
	public InvokeResult impInvestData(ImpDataInfoDTO info,HttpServletRequest request,HttpServletResponse response){
		System.out.println("开始上传");
		//上传文件			
	    try {
	    	info.setRequest(request);
	    	info.setFilePath(Config.getProperty("uploaddir")+"/"+"interfacedataupload");//文件上传地址
	    	//上传文件
			us.upload(info);
			//保存数据到接口表中
			info.setDate(us.getDate());
			service.saveInterfaceData(info);
			us.setFileName(null);
			return InvokeResult.success();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return InvokeResult.failure("数据上传失败，原因是："+e.getMessage()+"。");
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
		String year = request.getParameter("year");
		String quarter = request.getParameter("quarter");
		String year_month = request.getParameter("yearmonth");
		String impdate = request.getParameter("impdate");
		if(year.equals("")){
			return InvokeResult.failure("年度不能为空");
		}
		
		if(quarter.equals("")){
			return InvokeResult.failure("季度不能为空");
		}
		String[] result = service.dealInterfaceData(year, quarter, year_month, impdate);
		if(result[0].equals("1")){
			return InvokeResult.success();
		}else{
			return InvokeResult.failure(result[1]);
		}
	}
}
