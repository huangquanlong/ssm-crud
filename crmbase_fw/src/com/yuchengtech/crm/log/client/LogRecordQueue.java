package com.yuchengtech.crm.log.client;

import com.yuchengtech.crm.log.ThreadPool;
/**
 * 日志队列
 * @author CHANGZH
 * @date 2014-07-31
 * @param <T>  队列元素
 */
public class LogRecordQueue<T> extends AbstractQueue<T> {
	/** 日志线程池*/
	private ThreadPool threadPool;

	/***
	 *  插入队列
	 */
	public void add(T element) {
		super.add(element);
		getThreadPool().dispatch();
	}

	/**
	 * 日志队列大小
	 */
	public int getDefaultSize() {
		return 10000;
	}
	
	public ThreadPool getThreadPool() {
		return threadPool;
	}

	public void setThreadPool(ThreadPool threadPool) {
		this.threadPool = threadPool;
	}

}
