package com.yuchengtech.crm.log.client;
/***
 * 客户端日志处理接口
 * @author CHANGZH
 *
 */
public interface ILogManager {
	
	/***
	 * 处理日志方法
	 * @param logInfo 日志信息
	 */
	public void dealLogInfo(Object logInfo);

}
