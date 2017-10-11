package com.sinosoft.common;
public class InvokeResult {

	private Object data;

	private String errorMsg;

	private boolean success;

	public static InvokeResult success(Object data) {
		InvokeResult result = new InvokeResult();
		result.data = data;
		result.success = true;
		return result;
	}

	public static InvokeResult success() {
		InvokeResult result = new InvokeResult();
		result.success = true;
		return result;
	}

	public static InvokeResult failure(String message) {
		InvokeResult result = new InvokeResult();
		result.success = false;
		result.errorMsg = message;
		return result;
	}
	
	public static InvokeResult failure(String message,Object data) {
		InvokeResult result = new InvokeResult();
		result.success = false;
		result.errorMsg = message;
		result.data = data;
		return result;
	}
	
	public Object getData() {
		return data;
	}

	public String getErrorMsg() {
		return errorMsg;
	}


	public boolean isSuccess() {
		return success;
	}

	
}