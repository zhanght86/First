package com.sinosoft.dto;  


public class ElementDTO {  
      
    private String id;  
    private String contextRef;
    private String unitRef;
    private String decimals;
    private String data;
    private boolean nil; //适用标志
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContextRef() {
		return contextRef;
	}
	public void setContextRef(String contextRef) {
		this.contextRef = contextRef;
	}
	public String getUnitRef() {
		return unitRef;
	}
	public void setUnitRef(String unitRef) {
		this.unitRef = unitRef;
	}
	public String getDecimals() {
		return decimals;
	}
	public void setDecimals(String decimals) {
		this.decimals = decimals;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public Boolean getNil() {
		return nil;
	}
	public void setNil(Boolean nil) {
		this.nil = nil;
	}
	
}