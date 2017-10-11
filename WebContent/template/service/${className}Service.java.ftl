<#assign className = table.className>     
<#assign classNameLower = className?uncap_first>
package ${basepackage}.service;  

import java.util.List;
import ${basepackage}.common.Page;
import ${basepackage}.dto.${className}DTO;
import ${basepackage}.entity.${className};

public interface ${className}Service {  
    public void save${className}(${className}DTO dto);
	public void update${className}(long id,${className}DTO dto);
	public void delete${className}(String[] idArr);
	public Page<?> qry${className}OfPage(int page,int rows,${className}DTO dto);
	public List<?> qry${className}();
	public List<?> qry${className}All();
	public ${className} find${className}(long id);
}