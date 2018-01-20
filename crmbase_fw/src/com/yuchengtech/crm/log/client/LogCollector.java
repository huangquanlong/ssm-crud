package com.yuchengtech.crm.log.client;

import org.apache.log4j.Logger;

/***
 * 日志收集类
 * @author CHANGZH
 * @date 2014-07-29
 *
 */
public class LogCollector implements ILogCollector{
	
	protected static Logger log = Logger.getLogger(LogCollector.class);
	
	public String collectLog(Object obj) {
		String result = "success";
		try {
			//for (int i = 0; i <100; i ++) {
			LogCollectorManager.getInstance().dispatch(obj);
			//}
		} catch (Exception e) {
			log.info("日志信息记录出现异常！");
			e.printStackTrace();
			result = "failure";
		}
		return result;
	}

}
