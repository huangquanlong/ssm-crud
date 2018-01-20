package com.yuchengtech.crm.exception;

import java.util.Map;

public class DefaultExceptionExtendPoint implements IExceptionExtendPoint{
	Map<String, String> errPageMap;
	Map<String, String> errMsgMap;
	public Map<String, String> getErrMsgMap() {
		// TODO Auto-generated method stub
		return errMsgMap;
	}

	public Map<String, String> getErrPageMap() {
		// TODO Auto-generated method stub
		return errPageMap;
	}

	public void setErrMsgMap(Map<String, String> map) {
		// TODO Auto-generated method stub
		errMsgMap=map;
	}

	public void setErrPageMap(Map<String, String> map) {
		// TODO Auto-generated method stub
		errPageMap=map;
	}

}
