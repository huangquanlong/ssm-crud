package com.yuchengtech.crm.exception;

import java.util.Map;

public interface IExceptionExtendPoint {
	Map<String, String> getErrPageMap();
	Map<String, String> getErrMsgMap();
	void setErrPageMap(Map<String, String> map);
	void setErrMsgMap(Map<String, String> map);
}
