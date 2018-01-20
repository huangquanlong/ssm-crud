package com.yuchengtech.crm.exception;

import java.util.Map;

public interface IExceptionDefineTable {
	String getDefaultErrPage();
	String getDefaultErrMsg();
	void setDefaultErrPage(String errPage);
	void setDefaultErrMsg(String errMsg);
	Map<String, String> getErrPageMap();
	Map<String, String> getErrMsgMap();
}
