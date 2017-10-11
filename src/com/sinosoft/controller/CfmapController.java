package  com.sinosoft.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import  com.sinosoft.common.InvokeResult;
import  com.sinosoft.common.Page;
import  com.sinosoft.dto.CfmapDTO;
import  com.sinosoft.service.CfmapService;

@Controller
@RequestMapping(path="/cfmap")
public class CfmapController {
	@Resource
	private CfmapService cfmapService;
	@RequestMapping(path="/add", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult saveCfmap(CfmapDTO dto){
		try {
			cfmapService.saveCfmap(dto);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
		
	}
	@RequestMapping(path="/listpage")
	@ResponseBody
	public Page<?> qryCfmapOfPage(@RequestParam int page,@RequestParam int rows,CfmapDTO dto){
		return cfmapService.qryCfmapOfPage(page, rows,dto);
	}
	@RequestMapping(path="/listall")
	@ResponseBody
	public List<?> qryCfmapAll(){
		return cfmapService.qryCfmapAll();
	}
	@RequestMapping(path="/list")
	@ResponseBody
	public List<?> qryCfmap(){
		return cfmapService.qryCfmap();
	}
	@RequestMapping(path="/update")
	@ResponseBody
	public InvokeResult updateCfmap(@RequestParam long id,CfmapDTO dto){
		try {
			cfmapService.updateCfmap(id, dto);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
	@RequestMapping(path="/delete")
	@ResponseBody
	public InvokeResult deleteCfmap(@RequestParam String ids){
		try {
			String[] idArr = ids.split(",");
			cfmapService.deleteCfmap(idArr);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
}
