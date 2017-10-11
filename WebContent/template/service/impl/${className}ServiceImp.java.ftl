<#assign className = table.className>     
<#assign classNameLower = className?uncap_first>
package ${basepackage}.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ${basepackage}.common.Page;
import ${basepackage}.dao.${className}Dao;
import ${basepackage}.dto.${className}DTO;
import ${basepackage}.entity.${className};
import ${basepackage}.service.${className}Service;

@Service
public class ${className}ServiceImp implements ${className}Service{
	@Resource
	private ${className}Dao ${classNameLower}Dao;
	@Transactional
	public void save${className}(${className}DTO dto) {
		// TODO Auto-generated method stub
		${className} br=new ${className}();
		<#list table.columns as column>  
	    <#if column.columnNameLower="id">
	    <#else>
	    br.set${column.columnName}(dto.get${column.columnName}());
		</#if>
	    </#list>  
		${classNameLower}Dao.save(br);
	}
	@Transactional
	public void update${className}(long id, ${className}DTO dto) {
		// TODO Auto-generated method stub
		${className} br =${classNameLower}Dao.get(${className}.class,id);
		<#list table.columns as column>  
	    <#if column.columnNameLower="id">
	    <#else>
	    br.set${column.columnName}(dto.get${column.columnName}());
		</#if>
	    </#list>   
		${classNameLower}Dao.update(br);
	}
	@Transactional
	public void delete${className}(String[] idArr) {
		// TODO Auto-generated method stub
		Long[] ids=new Long[idArr.length];
		for(int i=0;i<idArr.length;i++){
			ids[i]=Long.parseLong(idArr[i]);
		}
		${classNameLower}Dao.removes(ids,${className}.class);
	}
	public Page<?> qry${className}OfPage(int page, int rows,${className}DTO dto) {
		  StringBuilder querySql =new StringBuilder("select ${classNameLower}.* from ${className} ${classNameLower} where 1=1");
		  <#list table.columns as column> 
			if(!(dto.get${column.columnName}()==null||"".equals(dto.get${column.columnName}()))){
	    	    querySql.append("  and  ${column.columnName} like '%"+dto.get${column.columnName}()+"%'");
		    }
		 </#list>  
		return ${classNameLower}Dao.queryByPage(querySql.toString(),page,rows,${className}DTO.class);
	}
	public List<?> qry${className}() {
		StringBuilder querySql =new StringBuilder("select ${classNameLower}.* from ${className} ${classNameLower}");
		List<?> list = ${classNameLower}Dao.queryBysql(querySql.toString(),${className}DTO.class);
		return list;
	}
	public List<?> qry${className}All() {
		List<?> list = ${classNameLower}Dao.queryBysql("select ${classNameLower}.* from ${className} ${classNameLower}",${className}DTO.class);
		return list;
	}
	public ${className} find${className}(long id) {
		// TODO Auto-generated method stub
		return ${classNameLower}Dao.get(${className}.class,id);
	}

}