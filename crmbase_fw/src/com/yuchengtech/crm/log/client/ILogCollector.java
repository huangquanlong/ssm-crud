package com.yuchengtech.crm.log.client;



/**
 * 日志收集接口
 * @author CHANGZH
 * @since 2014-07-29
 */
public interface ILogCollector {
	
	/**
	 * 日志收集方法
	 * @param logInfo 日志信息
	 * @return
	 */
	public String collectLog(Object logInfo);
	
}
