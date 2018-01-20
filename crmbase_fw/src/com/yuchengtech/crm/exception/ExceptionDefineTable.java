package com.yuchengtech.crm.exception;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ExceptionDefineTable implements IExceptionDefineTable,ApplicationContextAware{
	private ApplicationContext ctx;
	private String dfaultErrMsg;
	private String defaultErrPage;
	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		ctx=arg0;
	}
	public Map<String, String> getErrMsgMap() {
		Map<String,IExceptionExtendPoint> map= ctx.getBeansOfType(IExceptionExtendPoint.class);
		Collection<IExceptionExtendPoint> col=map.values();
		Map<String, String> resultMap=new HashMap<String, String>();
		for(IExceptionExtendPoint p:col){
			Map<String, String> msgMap=p.getErrMsgMap();
			resultMap.putAll(msgMap);
		}
		return resultMap;
	}

	public Map<String, String> getErrPageMap() {
		Map<String,IExceptionExtendPoint> map= ctx.getBeansOfType(IExceptionExtendPoint.class);
		Collection<IExceptionExtendPoint> col=map.values();
		Map<String, String> resultMap=new HashMap<String, String>();
		for(IExceptionExtendPoint p:col){
			Map<String, String> errMap=p.getErrPageMap();
			resultMap.putAll(errMap);
		}
		return resultMap;
	}
	public String getDefaultErrMsg() {
		// TODO Auto-generated method stub
		return dfaultErrMsg;
	}
	public String getDefaultErrPage() {
		// TODO Auto-generated method stub
		return defaultErrPage;
	}
	public void setDefaultErrMsg(String errMsg) {
		// TODO Auto-generated method stub
		dfaultErrMsg=errMsg;
	}
	public void setDefaultErrPage(String errPage) {
		// TODO Auto-generated method stub
		defaultErrPage=errPage;
	}

}
