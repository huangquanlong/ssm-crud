package com.yuchengtech.crm.exception;

public class BizException extends RuntimeException {

	public int getDirect() {
		return direct;
	}


	public int getLevel() {
		return level;
	}


	public String getCode() {
		return code;
	}


	public String getMsg() {
		Throwable tmp=this;
		String fullMsg="";
		while(tmp!=null){
			if(tmp instanceof BizException){
				BizException b=(BizException)tmp;
				fullMsg+=b.msg+"\r\b";
			}
			tmp=tmp.getCause();
		}
		return fullMsg;
	}


	public String getErrorPage() {
		return errorPage;
	}

	//0是输出到错误页，1是协议输出
	private int direct;
	//0信息，1警告，2错误
	private int level;
	//格式：系统代码-自定义代码，系统代码省掉说明是500，自定义代码省掉为标准事件，如404错误不需要再有自定代码
	private String code;
	private String msg;
	private String errorPage;
//	public BizException() {
//		// TODO Auto-generated constructor stub
//	}

	public BizException(int direct,int level,String code,String message,String errorPage, Throwable cause,Object... args) {
		super(String.format(message, args), cause);
		msg=String.format(message, args);
		this.direct=direct;
		this.level=level;
		this.code=code;
		this.errorPage=errorPage;
	}
	public BizException(int direct,int level,String code,String message,Object... args) {
		this(direct,level,code,message,null,null,args);
	}

}
