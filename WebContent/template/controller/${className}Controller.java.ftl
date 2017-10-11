<#assign className = table.className>     
<#assign classNameLower = className?uncap_first>
package ${basepackage}.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ${basepackage}.common.InvokeResult;
import ${basepackage}.common.Page;
import ${basepackage}.dto.${className}DTO;
import ${basepackage}.service.${className}Service;

@Controller
@RequestMapping(path="/${classNameLower}")
public class ${className}Controller {
	@Resource
	private ${className}Service ${classNameLower}Service;
	@RequestMapping(path="/add", method = RequestMethod.POST)
	@ResponseBody
	public InvokeResult save${className}(${className}DTO dto){
		try {
			${classNameLower}Service.save${className}(dto);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
		
	}
	@RequestMapping(path="/listpage")
	@ResponseBody
	public Page<?> qry${className}OfPage(@RequestParam int page,@RequestParam int rows,${className}DTO dto){
		return ${classNameLower}Service.qry${className}OfPage(page, rows,dto);
	}
	@RequestMapping(path="/listall")
	@ResponseBody
	public List<?> qry${className}All(){
		return ${classNameLower}Service.qry${className}All();
	}
	@RequestMapping(path="/list")
	@ResponseBody
	public List<?> qry${className}(){
		return ${classNameLower}Service.qry${className}();
	}
	@RequestMapping(path="/update")
	@ResponseBody
	public InvokeResult update${className}(@RequestParam long id,${className}DTO dto){
		try {
			${classNameLower}Service.update${className}(id, dto);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
	@RequestMapping(path="/delete")
	@ResponseBody
	public InvokeResult delete${className}(@RequestParam String ids){
		try {
			String[] idArr = ids.split(",");
			${classNameLower}Service.delete${className}(idArr);
			return InvokeResult.success(); 
		} catch (Exception e) {
			e.printStackTrace();
			return InvokeResult.failure(e.getMessage());
		}
	}
}
