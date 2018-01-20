package com.yuchengtech.crm.log;

import org.apache.log4j.Logger;


/**
 * 线程池中的线程类
 */
public abstract class PooledThread implements Runnable {

	private boolean busying;

	private boolean running;
	
	private long threadId;
	
	private String threadName;

	private Integer threadNumber;

	private ThreadPool threadPool;
	
	protected static Logger logger;

	public abstract boolean needProcess();

	public abstract void process();

	public PooledThread(int threadId) {
		threadNumber = new Integer(threadId);
		running = true;
		if (logger == null) {
    		logger = Logger.getLogger(this.getClass());
    	}
	}

	/**
	 * 判断该线程是否正在工作
	 * @return 如果该线程正在工作则返回真，如果该线程已经挂起则返回假
	 */
	public boolean isBusying() {
		return busying;
	}

	/**
	 * 判断该线程是否仍在运行
	 * @return 如果该线程正在运行则返回真，如果该线程已经结束则返回假
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * 强制中止该线程
	 */
	public void stop() {
		running = false;
		synchronized (threadNumber) {
			threadNumber.notify();
		}
	}

	/**
	 * 挂起该线程暂停运行
	 */
	public void pause() {
		try {
			synchronized (threadNumber) {
				threadPool.threadNotify();
				threadNumber.wait();
			}
		} catch (InterruptedException e) {
			logger.error("挂起线程池中的线程时发生异常。", e);
			e.printStackTrace();
		}
		 
	}
	/**
	 * 唤醒该线程暂停运行
	 */
	public void resume() {
		synchronized (threadNumber) {			
			threadNumber.notify();
		}
	}
	 
	@Override
	public void run() {
		threadId = Thread.currentThread().getId();
		threadName = Thread.currentThread().getName();
		while (running) {
		
			if (needProcess()) {
				busying = true;
				process();
				busying = false;
			} else {
				pause();
			}
		}
	}
	
	public ThreadPool getThreadPool() {
		return threadPool;
	}

	protected void setThreadPool(ThreadPool threadPool) {
		this.threadPool = threadPool;
	}

	public long getThreadId() {
		return threadId;
	}
	
	public String getThreadName() {
		return threadName;
	}

	public Integer getThreadNumber() {
		return threadNumber;
	}

}
