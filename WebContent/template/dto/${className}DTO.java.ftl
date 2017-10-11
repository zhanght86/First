<#assign className = table.className>     
<#assign classNameLower = className?uncap_first>
package ${basepackage}.dto;  


public class ${className}DTO {  
      
    <#list table.columns as column>  
    /**  
     */ 
    private ${column.simpleJavaType} ${column.columnNameLower};  
    </#list>  
 
<@generateJavaColumns/>  
 
<#macro generateJavaColumns>  
    <#list table.columns as column>  
        <#if column.isDateTimeColumn>  
    public String get${column.columnName}String() {  
        return DateConvertUtils.format(get${column.columnName}(), FORMAT_${column.constantName});  
    }  
    public void set${column.columnName}String(String ${column.columnNameLower}) {  
        set${column.columnName}(DateConvertUtils.parse(${column.columnNameLower}, FORMAT_${column.constantName},${column.simpleJavaType}.class));  
    }  
        </#if>      
    public void set${column.columnName}(${column.simpleJavaType} ${column.columnNameLower}) {  
        this.${column.columnNameLower} = ${column.columnNameLower};  
    }  
      
    public ${column.simpleJavaType} get${column.columnName}() {  
        return this.${column.columnNameLower};  
    }  
    </#list>  
</#macro> 
}