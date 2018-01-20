package com.yuchengtech.crm.log.client;

import org.apache.log4j.Logger;

import com.yuchengtech.crm.log.ThreadPool;

/***
 * 
 * 日志收集管理类
 * @author CHANGZH
 * @date 2014-07-29
 */
public class LogCollectorManager {

	private static LogCollectorManager instance;
	/**待处理日志信息*/
	private static LogRecordQueue<Object> logRecordQueue;
	/***日志*/
	private static Logger log = Logger.getLogger(LogCollectorManager.class);
	/**线程池*/
	private static ThreadPool threadPool;
	/**日志处理类*/
	private static String DATA_PROCESSOR_CLASS = "com.yuchengtech.crm.log.client.LogProcessor";
	/**** 线程池大小 */
	private static int POOL_SIZE = 5;
	
	/***取单态方法*/
	public static synchronized LogCollectorManager getInstance() {
		if (instance != null) {
			return instance;
		} else {
			instance = new LogCollectorManager();
			threadPool = new ThreadPool(POOL_SIZE, DATA_PROCESSOR_CLASS);
			logRecordQueue = new LogRecordQueue<Object>();
			logRecordQueue.setThreadPool(threadPool);
			
		}
		return instance;
	}
	
	/**
	 * 分发日志信息
	 * @param 日志信息 logInfo
	 */
	public void dispatch(Object logInfo) {
		log.debug("日志放入处理队列...");
		synchronized(logRecordQueue) {
			logRecordQueue.add(logInfo);
		}
	}
	
	/**
	 * 
	 * 取最近一条日志信息
	 */
	public synchronized Object getLastLogInfo() {
		Object obj = null;
		synchronized(logRecordQueue) {
			if (logRecordQueue.length() > 0) {
				obj = logRecordQueue.remove();
			}
		}
		return obj;
	}

	/**待处理日志信息个数*/
	public int getLogInfoSize() {
		int size = 0;
		synchronized(logRecordQueue) {
			size = logRecordQueue.length();
		}
		return size;
	}
	
}
