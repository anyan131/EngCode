package com.zte.engineer.CommitReportUtils;

public class BaseResult {

	private int code; //请求状态
	private String message;
	
	private  String errorKey;//错误代码
	
	public String getErrorKey() {
		return errorKey;
	}
	public void setErrorKey(String errorKey) {
		this.errorKey = errorKey;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
